package com.gabrielMJr.twaire.assistant.device_hw_sw;

import java.util.Date;
import android.icu.text.SimpleDateFormat;
//import java.text.SimpleDateFormat;

public class DateGetter {
    
    
    // Attributes
    private static Date date;
    
    // Simple date format second
    private static final SimpleDateFormat sdfs = new SimpleDateFormat("ss");
    
    // Simple date format minute
    private static final SimpleDateFormat sdfm = new SimpleDateFormat("mm");
    
    // Simple date format hour
    private static final SimpleDateFormat sdfh = new SimpleDateFormat("hh");
    
    // Simple date format day_month
    private static final SimpleDateFormat sdfd = new SimpleDateFormat("dd");
    
    // Simple date format Month
    private static final SimpleDateFormat sdfM = new SimpleDateFormat("MM");
    
    // Simple date format year
    private static final SimpleDateFormat sdfy = new SimpleDateFormat("yyyy");
    
    // Simple date format day_year
    private static final SimpleDateFormat sdfdy = new SimpleDateFormat("DD");
    
    
    // Getters
    // second
    public String getSecond()
    {
        date = new Date();
        return sdfs.format(date);
    }
    
    // minute
    public String getMinute()
    {
        date = new Date();
        return sdfm.format(date);
    }
    
    // hour
    public String getHour()
    {
        date = new Date();
        return sdfh.format(date);
    }
    
    // day
    public String getDay()
    {
        date = new Date();
        return sdfd.format(date);
    }
    
    // month in number
    public String getMonth()
    {
        date = new Date();
        return sdfM.format(date);
    }
    
    // month name
    public String getMonthName()
    {
        int month_number = Integer.valueOf(getMonth());
        
        switch (month_number)     
        {
            case 1:
                return "January";
                
            case 2:
                return "February";
                
            case 3:
                return "March";
                
            case 4:
                return "April";
                
            case 5:
                return "May";
                
            case 6:
                return "June";
                
            case 7:
                return "July";
                
            case 8:
                return "August";
                
            case 9:
                return "Semptember";
                
            case 10:
                return "October";
                
            case 11:
                return "November";
                
            case 12:
                return "December";
                
            default:
                return "unknown";
        }
    }
    
    // year
    public String getYear()
    {
        date = new Date();
        return sdfy.format(date);
    }
    
    // day_year
    public String getDayYear()
    {
        date = new Date();
        return sdfdy.format(date);
    }
}
