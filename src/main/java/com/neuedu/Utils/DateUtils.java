package com.neuedu.Utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;


public class DateUtils {
    private static final String STANDARD_FORMAT="yyyy-MM-dd HH:mm:ss";
    /**
     *Date-->string
     **/
    public  static String dateToStr(Date date,String formate){
        DateTime dateTime = new DateTime(date);
        return  dateTime.toString(formate);
    }
    public  static String dateToStr(Date date){
        DateTime dateTime = new DateTime(date);
        return  dateTime.toString(STANDARD_FORMAT);
    }

    /**
     * string-->Date
     * */
    public static Date strToDate(String string){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(string);
        return dateTime.toDate();
    }
    public static Date strToDate(String string,String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = dateTimeFormatter.parseDateTime(string);
        return dateTime.toDate();
    }
    public static void main(String[] args){
        System.out.println(dateToStr(new Date()));
        System.out.println(strToDate("2019-01-08 14:43:15"));
    }
}
