package com.alodiga.primefaces.ultima.controller.remittance;

import com.portal.business.commons.data.RemittanceData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.Remittance;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 * @author frank
 */
@Named(value = "remittanceController")
@ManagedBean
public class RemittanceController {

    private int id;
    private Date applicationDate;
    private String commentary;
    private float amountOrigin;
    private float totalAmount;
    private boolean sendingOptionSMS;
    private float amountDestiny;
    private String paymentServiceId;
    private String secondaryKey;
    private float additionalChanges;
    private Date creationDate;
    private Date creationHour;
    private RemittanceData remittanceData;
    
    public RemittanceController() {
    
    }

    public RemittanceController(int id, Date applicationDate, String commentary, float amountOrigin, float totalAmount, boolean sendingOptionSMS, float amountDestiny, String paymentServiceId, String secondaryKey, float additionalChanges, Date creationDate, Date creationHour) {
       
        this.id = id;
        this.applicationDate = applicationDate;
        this.commentary = commentary;
        this.amountOrigin = amountOrigin;
        this.totalAmount = totalAmount;
        this.sendingOptionSMS = sendingOptionSMS;
        this.amountDestiny = amountDestiny;
        this.paymentServiceId = paymentServiceId;
        this.secondaryKey = secondaryKey;
        this.additionalChanges = additionalChanges;
        this.creationDate = creationDate;
        this.creationHour = creationHour;
    }
    
    @PostConstruct
    public void init(){
    
    }

    
    public void save(){
    
        System.out.println("Hola entro al metodo!!!!!!");
        
        Remittance objRemittance = new Remittance(); 
        
        objRemittance.setApplicationDate((Timestamp) applicationDate);
        objRemittance.setCommentary(commentary);
        objRemittance.setAmountOrigin(amountOrigin);
        objRemittance.setTotalAmount(totalAmount);
        objRemittance.setSendingOptionSMS(sendingOptionSMS);
        objRemittance.setAmountDestiny(amountDestiny);
        objRemittance.setPaymentServiceId(paymentServiceId);
        objRemittance.setSecondaryKey(secondaryKey);
        objRemittance.setAdditionalChanges(additionalChanges);
        objRemittance.setCreationDate((Timestamp) creationDate);
        objRemittance.setCreationHour((Timestamp) creationHour);
     
        try {
            remittanceData.saveRemittance(objRemittance);
        } catch (NullParameterException ex) {
            Logger.getLogger(RemittanceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(RemittanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    
    }
    
      public void reset() {
        RequestContext.getCurrentInstance().reset("remittanceCreateForm:grid");
    }
      
      public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listRemittance.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.listReceiverView.doRediret()");
        }
    }
    
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public float getAmountOrigin() {
        return amountOrigin;
    }

    public void setAmountOrigin(float amountOrigin) {
        this.amountOrigin = amountOrigin;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean getSendingOptionSMS() {
        return sendingOptionSMS;
    }

    public void setSendingOptionSMS(boolean sendingOptionSMS) {
        this.sendingOptionSMS = sendingOptionSMS;
    }

    public float getAmountDestiny() {
        return amountDestiny;
    }

    public void setAmountDestiny(float amountDestiny) {
        this.amountDestiny = amountDestiny;
    }

    public String getPaymentServiceId() {
        return paymentServiceId;
    }

    public void setPaymentServiceId(String paymentServiceId) {
        this.paymentServiceId = paymentServiceId;
    }

    public String getSecondaryKey() {
        return secondaryKey;
    }

    public void setSecondaryKey(String secondaryKey) {
        this.secondaryKey = secondaryKey;
    }

    public float getAdditionalChanges() {
        return additionalChanges;
    }

    public void setAdditionalChanges(float additionalChanges) {
        this.additionalChanges = additionalChanges;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCreationHour() {
        return creationHour;
    }

    public void setCreationHour(Date creationHour) {
        this.creationHour = creationHour;
    }

    @Override
    public int hashCode() {
        
        int hash = 7;
        hash = 17 * hash + this.id;
        hash = 17 * hash + Objects.hashCode(this.applicationDate);
        hash = 17 * hash + Objects.hashCode(this.commentary);
        hash = 17 * hash + Objects.hashCode(this.amountOrigin);
        hash = 17 * hash + Objects.hashCode(this.totalAmount);
        hash = 17 * hash + Objects.hashCode(this.sendingOptionSMS);
        hash = 17 * hash + Objects.hashCode(this.amountDestiny);
        hash = 17 * hash + Objects.hashCode(this.paymentServiceId);
        hash = 17 * hash + Objects.hashCode(this.secondaryKey);
        hash = 17 * hash + Objects.hashCode(this.additionalChanges);
        hash = 17 * hash + Objects.hashCode(this.creationDate);
        hash = 17 * hash + Objects.hashCode(this.creationHour);
        return hash;
    
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RemittanceController other = (RemittanceController) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.applicationDate, other.applicationDate)) {
            return false;
        }
        if (!Objects.equals(this.commentary, other.commentary)) {
            return false;
        }
        if (!Objects.equals(this.amountOrigin, other.amountOrigin)) {
            return false;
        }
        if (!Objects.equals(this.totalAmount, other.totalAmount)) {
            return false;
        }
        if (!Objects.equals(this.sendingOptionSMS, other.sendingOptionSMS)) {
            return false;
        }
        if (!Objects.equals(this.amountDestiny, other.amountDestiny)) {
            return false;
        }
        if (!Objects.equals(this.paymentServiceId, other.paymentServiceId)) {
            return false;
        }
        if (!Objects.equals(this.secondaryKey, other.secondaryKey)) {
            return false;
        }
        if (!Objects.equals(this.additionalChanges, other.additionalChanges)) {
            return false;
        }
        if (!Objects.equals(this.creationDate, other.creationDate)) {
            return false;
        }
        if (!Objects.equals(this.creationHour, other.creationHour)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RemittanceController{" + "id=" + id + ", applicationDate=" + applicationDate + ", commentary=" + commentary + ", amountOrigin=" + amountOrigin + ", totalAmount=" + totalAmount + ", sendingOptionSMS=" + sendingOptionSMS + ", amountDestiny=" + amountDestiny + ", paymentServiceId=" + paymentServiceId + ", secondaryKey=" + secondaryKey + ", additionalChanges=" + additionalChanges + ", creationDate=" + creationDate + ", creationHour=" + creationHour + '}';
    }
    
}
