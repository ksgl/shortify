package com.example.shortify.database;

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimestampConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
//        DateFormat dateFormat = new SimpleDateFormat("dd MMM, HH:mm");
//        String strDate = dateFormat.format(date);
//        return strDate;
    }
//    static private DateFormat df = new SimpleDateFormat("dd MMM, HH:mm");
//    @TypeConverter
//    public static Date toDate(String value) {
//        try {
//            return df.parse(value);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String toString(Date date) {
//        return df.format(date);
//    }
}


//    object TiviTypeConverters {
//private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
//
//@TypeConverter
//@JvmStatic
//    fun toOffsetDateTime(value: String?): OffsetDateTime? {
//            return value?.let {
//            return formatter.parse(value, OffsetDateTime::from)
//            }
//            }
//
//@TypeConverter
//@JvmStatic
//    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
//            return date?.format(formatter)
//            }
//            }