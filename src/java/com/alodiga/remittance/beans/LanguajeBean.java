/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.remittance.beans;

import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.util.Locale;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author usuario
 */


@SessionScoped
@ManagedBean(name="languajeBean")
public class LanguajeBean implements Serializable{
    
    private String languaje;

    public LanguajeBean() {
        languaje = "es";
        System.out.println("Entro al constructor");
    }

    public String getLanguaje() {
        return languaje;
    }

    public void setLanguaje(String languaje) {
        this.languaje = languaje;
    }
    
    public void localityChanged(ValueChangeEvent e){
        String newLocaleValue = e.getNewValue().toString();
        if (newLocaleValue.equals("en")){
            languaje= "en";
            FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.ENGLISH);
        }else{
            FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("es","espana"));
            languaje= "es";
        }
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        session.setAttribute("languaje", languaje);
    }

    
}
