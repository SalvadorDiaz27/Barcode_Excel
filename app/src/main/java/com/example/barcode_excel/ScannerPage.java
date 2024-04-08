package com.example.barcode_excel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScannerPage extends AppCompatActivity {
    private EditText etCodigoDelObjeto, etDescripcionDelObjeto;
    ;
    private ImageView ivBarcodeScanner;
    private TextView tvCubiculo;
    private DatabaseHelper dbHelper;
    private RecyclerView rvListaObjetos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_page);

        etCodigoDelObjeto = findViewById(R.id.etCodigoDelObjeto);
        ivBarcodeScanner = findViewById(R.id.ivBarcodeScanner);
        tvCubiculo = findViewById(R.id.tvCubiculo);
        etDescripcionDelObjeto = findViewById(R.id.etDescripcionDelObjeto); // Asegúrate de tener este EditText en tu layout
        rvListaObjetos = findViewById(R.id.rvListaObjetos); // Inicializa tu RecyclerView aquí
        rvListaObjetos.setLayoutManager(new LinearLayoutManager(this));


        dbHelper = new DatabaseHelper(this);

        String cubiculoSeleccionado = getIntent().getStringExtra("CUBICULO_SELECCIONADO");
        if (cubiculoSeleccionado != null) {
            tvCubiculo.setText("Cubículo " + cubiculoSeleccionado);
            cargarDatosHistorial(cubiculoSeleccionado.replaceAll("\\D+", ""));
        }

        ivBarcodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ScannerPage.this);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
            } else {
                String codigoEscaneado = result.getContents();
                etCodigoDelObjeto.setText(codigoEscaneado);
                buscarEnBaseDeDatos(codigoEscaneado);
            }
        }
    }

    private void buscarEnBaseDeDatos(String codigo) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {DatabaseHelper.COLUMN_DESCRIPCION};
        String selection = DatabaseHelper.COLUMN_INVENTARIO + " = ?";
        String[] selectionArgs = {codigo};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String descripcion = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPCION));
            etDescripcionDelObjeto.setText(descripcion);
            cursor.close();

            // Inserta el registro en la tabla de historial
            insertarRegistroHistorial(codigo, tvCubiculo.getText().toString());
        } else {
            Toast.makeText(this, "Objeto no encontrado.", Toast.LENGTH_LONG).show();
            etDescripcionDelObjeto.setText(""); // Limpiar la descripción ya que el objeto no se encontró
        }

        db.close();
    }

    private void insertarRegistroHistorial(String codigo, String cubiculoTexto) {
        String cubiculoCodigo = cubiculoTexto.replace("Cubículo ", "");

        // Obtener la fecha actual en el formato deseado
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String fechaActual = dateFormat.format(new Date());

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_HISTORIAL,
                new String[]{DatabaseHelper.COLUMN_BARCODE},
                DatabaseHelper.COLUMN_BARCODE + "=? AND " + DatabaseHelper.COLUMN_DATE + "=?",
                new String[]{codigo, fechaActual},
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_BARCODE, codigo);
            values.put(DatabaseHelper.COLUMN_CUBICLE_CODE, cubiculoCodigo);
            values.put(DatabaseHelper.COLUMN_DATE, fechaActual);

            db.insert(DatabaseHelper.TABLE_HISTORIAL, null, values);
            cargarDatosHistorial(cubiculoCodigo); // Actualizar la lista después de cada inserción
        } else {
            Toast.makeText(this, "Registro ya existe para el día de hoy.", Toast.LENGTH_SHORT).show();
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }


    private void cargarDatosHistorial(String cubiculoCodigo) {
        List<HistorialRegistro> registros = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Asegúrate de limpiar la lista de registros antes de cargar los nuevos datos
        registros.clear();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_HISTORIAL,
                new String[] { DatabaseHelper.COLUMN_BARCODE, DatabaseHelper.COLUMN_CUBICLE_CODE, DatabaseHelper.COLUMN_DATE },
                DatabaseHelper.COLUMN_CUBICLE_CODE + "=?",
                new String[] { cubiculoCodigo },
                null,
                null,
                DatabaseHelper.COLUMN_DATE + " DESC"); // Ordenar por fecha para mostrar los registros más recientes primero

        while (cursor.moveToNext()) {
            String barcode = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BARCODE));
            String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE));
            registros.add(new HistorialRegistro(barcode, cubiculoCodigo, date));
        }

        cursor.close();
        db.close();

        HistorialAdapter adapter = new HistorialAdapter(registros);
        rvListaObjetos.setAdapter(adapter);
    }
}