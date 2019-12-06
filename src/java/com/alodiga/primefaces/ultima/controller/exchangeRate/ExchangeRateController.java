package com.alodiga.primefaces.ultima.controller.exchangeRate;

import com.alodiga.primefaces.ultima.controller.ratePaymentNetwork.ListRatePaymentNetworkController;
import com.portal.business.commons.data.ExchangeRateData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.ExchangeRate;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author frank
 */
@ManagedBean
public class ExchangeRateController {
    
   private float amount;
   private Date beginingDate;
   private Date endingDate;
   private String country;
   
   
    public ExchangeRateController(){
    
    }
    
    
    
     @PostConstruct
    public void init(){
    
    }
    
    
     public void save(){
         
           UtilsData utilsData = new UtilsData();
           ExchangeRate exchangeRate = null;
           utilsData = new UtilsData();
           Country objCountry = new Country();
          
       try {
         
           System.out.println("Holaaaaa metodo!!");
           
           exchangeRate.setEndingDate(new Timestamp(new Date().getTime()));
           exchangeRate.setAmount(amount);
           objCountry.setName(country);
           exchangeRate.setCountry(objCountry);
           utilsData.saveExchangeRate(exchangeRate);
           reload();
           
       } catch (NullParameterException | GeneralException ex) {
           Logger.getLogger(ExchangeRateController.class.getName()).log(Level.SEVERE, null, ex);
       }

     }
     
      public void reset(){
        RequestContext.getCurrentInstance().reset("UserCreateForm:grid");
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listExchangeRate.xhtml");
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    public void reload(){
        WsRequest request = new WsRequest();
        ExchangeRateData exchangeRateData = new ExchangeRateData();
        try {
            exchangeRateData.loadExchangeRate(request);
        } catch (GeneralException | EmptyListException | NullParameterException ex) {
            Logger.getLogger(ListRatePaymentNetworkController.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
}
