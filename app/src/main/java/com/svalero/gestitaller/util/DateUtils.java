package com.svalero.gestitaller.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    /**
     * Convierte mi formato de fecha personalizado (dd/MM/yyyy) a formato
     * LocalDate de la BBDD (yyyy-MM-dd)
     *
     * @param dateString fecha con formato LocalDate (dd/MM/yyyy)
     * @return fecha con formato LocalDate (yyyy-MM-dd)
     */
    public static LocalDate fromMyDateFormatStringToLocalDate(String dateString) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");  // Para indicar el formato
        return LocalDate.parse(dateString.trim(), formato); // Devuelve el String parseado con el formato
    }
}
