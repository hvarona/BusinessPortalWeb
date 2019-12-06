/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.primefaces.ultima.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


/**
 *
 * @author luis
 */
@FacesConverter("com.alodiga.primefaces.ultima.controller.ConvertDateToTimestamp")
public class ConvertDateToTimestamp implements Converter {

//    public ConvertDateToTimestamp() {
//        super();
//        setTimeZone(TimeZone.getDefault());
//// here you can set your custom date pattern for your project
//// setPattern("M/d/yy");
//    }

    public Timestamp ConvertDateToTimestamp(Date myDateTime) {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm");
        //java.util.Date badDate = formatter.parse(request.getParameter("datetime")); 
        Timestamp date = new Timestamp(myDateTime.getTime());
        return date;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Timestamp timee;
        Date datee = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        
        try {
                    //System.out.println("               uuyyuu " + value);

            datee = simpleDateFormat.parse(value);
        } catch (ParseException ex) {
            Logger.getLogger(ConvertDateToTimestamp.class.getName()).log(Level.SEVERE, null, ex);
        }
        timee = new Timestamp(datee.getTime());
        return timee;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value.toString();
        
    }
}
