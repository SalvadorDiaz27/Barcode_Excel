package com.example.barcode_excel;

public class HistorialRegistro {
    private String barcode;
    private String cubicleCode;
    private String date;

    public HistorialRegistro(String barcode, String cubicleCode, String date) {
        this.barcode = barcode;
        this.cubicleCode = cubicleCode;
        this.date = date;
    }

    // Getters
    public String getBarcode() { return barcode; }
    public String getCubicleCode() { return cubicleCode; }
    public String getDate() { return date; }
}