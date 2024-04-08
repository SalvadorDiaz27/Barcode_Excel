package com.example.barcode_excel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Scanner extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CubiculoAdapter adapter;
    private List<CubiculoAdapter.Cubiculo> cubiculos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.listRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cargar los datos de los cubículos desde la base de datos
        cargarCubiculosDesdeDB();

        // Configurar el adaptador con los datos
        adapter = new CubiculoAdapter(cubiculos);
        recyclerView.setAdapter(adapter);   
    }

    private void cargarCubiculosDesdeDB() {
        // Abrir la base de datos
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Definir las columnas que quiero devolver en la consulta
        String[] projection = {
                DatabaseHelper.COLUMN_CUBO, // Asegúrate de que es el nombre correcto de la columna
        };

        // Realizar la consulta SQL para obtener los cubículos sin duplicados
        Cursor cursor = db.query(true, DatabaseHelper.TABLE_NAME, projection, null, null, DatabaseHelper.COLUMN_CUBO, null, null, null);

        // Iterar sobre el cursor para llenar la lista de cubículos
        while (cursor.moveToNext()) {
            String cubo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CUBO));
            cubiculos.add(new CubiculoAdapter.Cubiculo(cubo));
        }
        cursor.close();
        db.close();
    }
}