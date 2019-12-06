/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alodiga.primefaces.ultima.controller.ratePaymentNetwork;

import com.portal.business.commons.data.RatePaymentNetworkData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.models.PaymentNetwork;
import com.portal.business.commons.models.RatePaymentNetwork;
import java.io.IOException;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author frank
 */
@ManagedBean
public class RatePaymentNetworkController {
    
   private float amount;
   private Date beginingDate;
   private Date endingDate;
    
    public RatePaymentNetworkController(){
    
    }
    
    @PostConstruct
    public void init(){
    
    }
    
    
     public void save(){
         
        System.out.println("Holaaaaa metodo!!");
//        RatePaymentNetwork ratePaymentNetwork = null;
//        UtilsData utilsData = null;
//        RatePaymentNetwork objRatePaymentNetwork = null;
//        PaymentNetwork objPaymentNetwork = null;
//        RatePaymentNetworkData ratePaymentNetworkData = new RatePaymentNetworkData();
//        
//        ratePaymentNetwork = ratePaymentNetworkData.lastRatePaymentNetwork(ratePaymentNetworkId);
     
     }
     
      public void reset(){
        RequestContext.getCurrentInstance().reset("UserCreateForm:grid");
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listRatePaymentNetwork.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.User1View.doRediret()");
        }
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getBeginingDate() {
        return beginingDate;
    }

    public void setBeginingDate(Date beginingDate) {
        this.beginingDate = beginingDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }
    
    
}
