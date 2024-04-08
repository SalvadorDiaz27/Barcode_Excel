package com.example.barcode_excel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Excel_exporters extends AppCompatActivity {

    private static String obtenerRutaDirectorioExcel() {
        return Environment.getExternalStorageDirectory().getPath() + "/Documents/InventarioExcel";
    }

    private static String obtenerRutaArchivoExcel(String nombreArchivo) {
        File directorioExcel = new File(obtenerRutaDirectorioExcel());
        if (!directorioExcel.exists()) {
            directorioExcel.mkdirs();
        }
        return directorioExcel.getPath() + "/" + nombreArchivo;
    }

    public static boolean exportarInventario(Context context, Cursor cursor, String fileName) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // Crear fila de encabezado
        Row headerRow = sheet.createRow(0);

        // Obtener las columnas existentes del cursor
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            headerRow.createCell(i).setCellValue(cursor.getColumnName(i));
        }

        // Obtener las nuevas columnas dinámicamente
        if (cursor.moveToFirst()) {
            for (int i = cursor.getColumnCount(); i < cursor.getColumnCount() + cursor.getColumnCount() - 1; i++) {
                headerRow.createCell(i).setCellValue("Cubiculo_" + cursor.getColumnName(i - cursor.getColumnCount() + 1));
            }
        }

        // Poblar datos
        if (cursor != null && cursor.moveToFirst()) {
            int rowIndex = 1;
            do {
                Row row = sheet.createRow(rowIndex++);

                // Poblar las columnas existentes
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    row.createCell(i).setCellValue(cursor.getString(i));
                }

                // Poblar las nuevas columnas dinámicamente
                for (int i = cursor.getColumnCount(); i < cursor.getColumnCount() + cursor.getColumnCount() - 1; i++) {
                    String fecha = cursor.getColumnName(i - cursor.getColumnCount() + 1).replace("_", "/");
                    String barcode = cursor.getString(cursor.getColumnIndex("inventario"));

                    // Obtener el valor de la nueva columna usando la consulta dinámica
                    String cubiculoValue = obtenerCubiculoDinamico(context, barcode, fecha);
                    row.createCell(i).setCellValue(cubiculoValue);
                }

            } while (cursor.moveToNext());
        }

        // Guardar el libro de trabajo en almacenamiento externo
        try {
            String filePath = obtenerRutaArchivoExcel(fileName);
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            Toast.makeText(context, "Inventario exportado a Excel", Toast.LENGTH_SHORT).show();
            return true;
        } catch (IOException e) {
            Log.e("ExcelExporter", "Error exporting to Excel: " + e.getMessage());
            Toast.makeText(context, "Error al exportar el inventario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean exportarHistorial(Context context, List<DatabaseHelper.HistorialModel> historialItems, String fileName) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet2");


        // Crear fila de encabezado
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Barcode");
        headerRow.createCell(1).setCellValue("cubicule_code");
        headerRow.createCell(2).setCellValue("date");


        // Poblar datos
        for (int rowIndex = 0; rowIndex < historialItems.size(); rowIndex++) {
            Row row = sheet.createRow(rowIndex + 1);
            DatabaseHelper.HistorialModel item = historialItems.get(rowIndex);

            row.createCell(0).setCellValue(item.getBarcode());
            row.createCell(1).setCellValue(item.getCode());
            row.createCell(2).setCellValue(item.getDate());

        }

        // Guardar el libro de trabajo en almacenamiento externo
        try {
            String filePath = obtenerRutaArchivoExcel(fileName);
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            Toast.makeText(context, "Historial exportado a Excel", Toast.LENGTH_SHORT).show();
            return true;
        } catch (IOException e) {
            Log.e("ExcelExporter", "Error exporting to Excel: " + e.getMessage());
            Toast.makeText(context, "Error al exportar el historial: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private static String obtenerCubiculoDinamico(Context context, String barcode, String fecha) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String cubiculoValue = "*";

        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getReadableDatabase();

            String query = "SELECT cubicle_code FROM Historial WHERE Barcode = ? AND date = ?";
            cursor = db.rawQuery(query, new String[]{barcode, fecha});

            if (cursor.moveToFirst()) {
                cubiculoValue = cursor.getString(cursor.getColumnIndex("cubicle_code"));
            }
        } catch (Exception e) {
            Log.e("ExcelExporter", "Error obteniendo Cubiculo dinámico: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return cubiculoValue;
    }
}