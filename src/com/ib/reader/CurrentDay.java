/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ib.reader;

import java.util.Calendar;

/**
 *
 * @author Siteng Jin
 */
public class CurrentDay {
    public static String findCurrentDay(){
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        
        switch (day) {
            case Calendar.SUNDAY:
                return new String("Sun");
            case Calendar.SATURDAY:
                return new String("Sat");
            case Calendar.MONDAY:
                return new String("Mon");
            case Calendar.TUESDAY:
                return new String("Tue");
            case Calendar.WEDNESDAY:
                return new String("Wed");
            case Calendar.THURSDAY:
                return new String("Thu");
            case Calendar.FRIDAY:
                return new String("Fri");
            default: 
                return null;
        }
    }
}
