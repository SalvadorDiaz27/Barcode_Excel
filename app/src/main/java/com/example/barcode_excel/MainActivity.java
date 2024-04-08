package com.example.barcode_excel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        Cursor cursor = dbHelper.obtenerDatosInventarioConHistorial();

        CardView ScannerCard = findViewById(R.id.CardEscann);
        CardView HistoryCard = findViewById(R.id.CardHistorial);
        CardView ExitCard = findViewById(R.id.ExitCard);
        CardView InventoryCard = findViewById(R.id.InventoryCard);
        progressBar = findViewById(R.id.progressBar);

        ScannerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Scanner.class));
            }
        });

        HistoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportarHistorialAExcel();
            }
        });

        InventoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportarInventarioAExcel();
            }
        });

        ExitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Cierra la actividad actual
                System.exit(0); // Termina la ejecución de la aplicación
            }
        });
    }

    private String obtenerRutaArchivoExcel() {
        return "InventarioData.xlsx";
    }

    private boolean datosInventarioDisponibles() {
        // Obtiene todos los datos del inventario utilizando el método de tu DatabaseHelper
        List<DatabaseHelper.InventarioModel> inventarioItems = dbHelper.getAllData();


        // Verifica si la lista de inventario no es nula y tiene elementos
        if (inventarioItems != null && !inventarioItems.isEmpty()) {
            // Si hay datos de inventario disponibles, devuelve true
            return true;
        } else {
            // Si la lista está vacía o nula, significa que no hay datos de inventario disponibles, devuelve false
            return false;
        }
    }

    private void mostrarProgreso(final boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    private void exportarInventarioAExcel() {
        if (datosInventarioDisponibles()) {
            Cursor cursor = dbHelper.obtenerDatosInventarioConHistorial();

            // Verifica si el Cursor no es nulo y tiene datos
            if (cursor != null && cursor.moveToFirst()) {
                Excel_exporters excelExporter = new Excel_exporters();
                String fileName = obtenerRutaArchivoExcel();

                try {
                    // Exporta el inventario a Excel utilizando el Cursor obtenido
                    excelExporter.exportarInventario(this, cursor, fileName);
                    Toast.makeText(this, "Inventario exportado correctamente", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("MainActivity", "Error al exportar a Excel", e);
                    Toast.makeText(this, "Error al exportar el inventario", Toast.LENGTH_SHORT).show();
                } finally {
                    // Cierra el Cursor después de su uso
                    cursor.close();
                }
            } else {
                Toast.makeText(this, "No hay datos de inventario para exportar", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No hay datos de inventario para exportar", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean datosHistorialDisponibles() {
        List<DatabaseHelper.HistorialModel> historialItems = dbHelper.getAllDataHistorial();


        // Verifica si la lista de inventario no es nula y tiene elementos
        if (historialItems != null && !historialItems.isEmpty()) {
            // Si hay datos de inventario disponibles, devuelve true
            return true;
        } else {
            // Si la lista está vacía o nula, significa que no hay datos de inventario disponibles, devuelve false
            return false;
        }
    }

    private List<DatabaseHelper.HistorialModel> obtenerDatosHistorial() {
        return dbHelper.getAllDataHistorial();
    }

    private String obtenerRutaArchivoHistorialExcel() {
        // Puedes ajustar la ruta según tus necesidades
        return "HistorialData.xlsx";
    }

    private void exportarHistorialAExcel() {
        if (datosHistorialDisponibles()) {
            Excel_exporters excelExporter = new Excel_exporters();
            List<DatabaseHelper.HistorialModel> historialItems = obtenerDatosHistorial();
            String fileName = obtenerRutaArchivoHistorialExcel();

            try {
                excelExporter.exportarHistorial(this, historialItems, fileName);
                Toast.makeText(this, "Inventario exportado correctamente", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("MainActivity", "Error al exportar a Excel", e);
                Toast.makeText(this, "Error al exportar el inventario", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No hay datos de inventario para exportar", Toast.LENGTH_SHORT).show();
        }
    }
}
