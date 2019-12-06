package com.alodiga.primefaces.ultima.controller.StoreHasRemittent;

import com.portal.business.commons.data.RemittanceData;
import java.io.IOException;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 * @author frank
 */
@Named(value = "storeHasRemittentController")
@ManagedBean
public class StoreHasRemittentController {

    private int id;
   
    
    public StoreHasRemittentController() {
    
    }

    public StoreHasRemittentController(int id, Date applicationDate, String commentary, float amountOrigin, float totalAmount, boolean sendingOptionSMS, float amountDestiny, String paymentServiceId, String secondaryKey, float additionalChanges, Date creationDate, Date creationHour) {
       
        this.id = id;
    }
    
    @PostConstruct
    public void init(){
    
    }

    
    public void save(){
      
    }
    
      public void reset() {
        RequestContext.getCurrentInstance().reset("remittanceCreateForm:grid");
    }
      
      public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listStoreHasRemittent.xhtml");
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

   
}
