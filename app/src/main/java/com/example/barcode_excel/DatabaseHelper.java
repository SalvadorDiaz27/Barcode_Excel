package com.example.barcode_excel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Nombre de la base de datos
    private static final String DATABASE_NAME = "inventario.db";

    // Version de la base de datos
    private static final int DATABASE_VERSION = 1;

    // Nombre de las tablas
    public static final String TABLE_NAME = "inventario";
    public static final String TABLE_HISTORIAL = "historial";

    // Nombres de las columnas de la tabla "inventario"
    public static final String COLUMN_CUBO = "cubo";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_MARCA = "marca";
    public static final String COLUMN_MODELO = "modelo";
    public static final String COLUMN_SERIE = "serie";
    public static final String COLUMN_TIPO_BIEN = "tipo_bien";
    public static final String COLUMN_TIPO_ADQUISICION = "tipo_adquisicion";
    public static final String COLUMN_INVENTARIO = "inventario";
    public static final String COLUMN_NUMERO_PEDIDO = "numero_pedido";
    public static final String COLUMN_CUENTA = "cuenta";
    public static final String COLUMN_FECHA_INVENTARIO = "fecha_inventario";
    public static final String COLUMN_UBICADO = "ubicado";
    public static final String COLUMN_NOMBRE = "nombre";

    // Nombres de las columnas de la tabla "historial"
    public static final String COLUMN_BARCODE = "barcode";
    public static final String COLUMN_CUBICLE_CODE = "cubicle_code";
    public static final String COLUMN_DATE = "date";

    //Consulta SQL para crear la tabla
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_CUBO + " TEXT , " +
                    COLUMN_DESCRIPCION + " TEXT, " +
                    COLUMN_MARCA + " TEXT, " +
                    COLUMN_MODELO + " TEXT, " +
                    COLUMN_SERIE + " TEXT, " +
                    COLUMN_TIPO_BIEN + " TEXT, " +
                    COLUMN_TIPO_ADQUISICION + " TEXT, " +
                    COLUMN_INVENTARIO + " TEXT, " +
                    COLUMN_NUMERO_PEDIDO + " TEXT, " +
                    COLUMN_CUENTA + " TEXT, " +
                    COLUMN_FECHA_INVENTARIO + " TEXT, " +
                    COLUMN_UBICADO + " TEXT, " +
                    COLUMN_NOMBRE + " TEXT);";

    private static final String CREATE_TABLE_HISTORIAL = "CREATE TABLE " + TABLE_HISTORIAL + "("
            + COLUMN_BARCODE + " TEXT,"
            + COLUMN_CUBICLE_CODE + " TEXT,"
            + COLUMN_DATE + " TEXT"
            + ");";


    public static class InventarioModel {
        private String cubo;
        private String descripcion;
        private String marca;
        private String modelo;
        private String serie;
        private String tipo_bien;
        private String tipo_adquisicion;
        private String inventario;
        private String numero_pedido;
        private String cuenta;
        private String fecha_inventario;
        private String ubicado;
        private String nombre;

        public String getCubo() {
            return cubo;
        }

        public void setCubo(String cubo) {
            this.cubo = cubo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getMarca() {
            return marca;
        }

        public void setMarca(String marca) {
            this.marca = marca;
        }

        public String getModelo() {
            return modelo;
        }

        public void setModelo(String modelo) {
            this.modelo = modelo;
        }

        public String getSerie() {
            return serie;
        }

        public void setSerie(String serie) {
            this.serie = serie;
        }

        public String getTipo_bien() {
            return tipo_bien;
        }

        public void setTipo_bien(String tipo_bien) {
            this.tipo_bien = tipo_bien;
        }

        public String getTipo_adquisicion() {
            return tipo_adquisicion;
        }

        public void setTipo_adquisicion(String tipo_adquisicion) {
            this.tipo_adquisicion = tipo_adquisicion;
        }

        public String getInventario() {
            return inventario;
        }

        public void setInventario(String inventario) {
            this.inventario = inventario;
        }

        public String getNumero_pedido() {
            return numero_pedido;
        }

        public void setNumero_pedido(String numero_pedido) {
            this.numero_pedido = numero_pedido;
        }

        public String getCuenta() {
            return cuenta;
        }

        public void setCuenta(String cuenta) {
            this.cuenta = cuenta;
        }

        public String getFecha_inventario() {
            return fecha_inventario;
        }

        public void setFecha_inventario(String fecha_inventario) {
            this.fecha_inventario = fecha_inventario;
        }

        public String getUbicado() {
            return ubicado;
        }

        public void setUbicado(String ubicado) {
            this.ubicado = ubicado;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }

    public static class HistorialModel {
        private String barcode;
        private String code;
        private String date;

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }


    public List<InventarioModel> getAllData() {
        List<InventarioModel> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_CUBO,
                COLUMN_DESCRIPCION,
                COLUMN_MARCA,
                COLUMN_MODELO,
                COLUMN_SERIE,
                COLUMN_TIPO_BIEN,
                COLUMN_TIPO_ADQUISICION,
                COLUMN_INVENTARIO,
                COLUMN_NUMERO_PEDIDO,
                COLUMN_CUENTA,
                COLUMN_FECHA_INVENTARIO,
                COLUMN_UBICADO,
                COLUMN_NOMBRE
        };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            InventarioModel item = new InventarioModel();
            // Configura los atributos del modelo con los datos de la base de datos
            item.setCubo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUBO)));
            item.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION)));
            item.setMarca(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MARCA)));
            item.setModelo(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MODELO)));
            item.setSerie(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SERIE)));
            item.setTipo_bien(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO_BIEN)));
            item.setTipo_adquisicion(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO_ADQUISICION)));
            item.setInventario(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INVENTARIO)));
            item.setNumero_pedido(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NUMERO_PEDIDO)));
            item.setCuenta(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUENTA)));
            item.setFecha_inventario(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_INVENTARIO)));
            item.setUbicado(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UBICADO)));
            item.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)));


            // Repite para los demás atributos

            dataList.add(item);
        }

        cursor.close();
        db.close();

        return dataList;
    }


    public List<HistorialModel> getAllDataHistorial() {
        List<HistorialModel> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_BARCODE,
                COLUMN_CUBICLE_CODE,
                COLUMN_DATE
        };

        Cursor cursor = db.query(
                TABLE_HISTORIAL,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            HistorialModel item = new HistorialModel();
            // Configura los atributos del modelo con los datos de la base de datos
            item.setBarcode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BARCODE)));
            item.setCode(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUBICLE_CODE)));
            item.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
            // Repite para los demás atributos

            dataList.add(item);
        }

        cursor.close();
        db.close();

        return dataList;
    }

    public Cursor obtenerDatosInventarioConHistorial() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorResultado = null;

        try {
            // Obtener la lista de fechas únicas de la tabla Historial
            Cursor cursorFechas = db.rawQuery("SELECT DISTINCT date FROM Historial", null);

            List<String> fechas = new ArrayList<>();

            if (cursorFechas.moveToFirst()) {
                do {
                    fechas.add(cursorFechas.getString(0));
                } while (cursorFechas.moveToNext());
            }

            cursorFechas.close();

            // Construir la consulta dinámica
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT i.*, ");

            for (String fecha : fechas) {
                queryBuilder.append("(SELECT cubicle_code FROM Historial WHERE Barcode = i.inventario AND date = '")
                        .append(fecha)
                        .append("') AS Cubiculo_")
                        .append(fecha.replace("/", "_"))
                        .append(", ");
            }

            queryBuilder.setLength(queryBuilder.length() - 2);  // Eliminar la coma al final
            queryBuilder.append(" FROM Inventario i");

            // Ejecutar la consulta
            cursorResultado = db.rawQuery(queryBuilder.toString(), null);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error al obtener datos con historial: " + e.getMessage());
        }

        return cursorResultado;
    }


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_HISTORIAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Crear la tabla nuevamente
        onCreate(db);
    }
}