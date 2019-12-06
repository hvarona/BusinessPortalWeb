package com.alodiga.primefaces.ultima.controller.RemittentHasReceiver;

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
@Named(value = "remittanceController")
@ManagedBean
public class RemittentHasReceiverController {

    private int id;
    
    public RemittentHasReceiverController() {
    
    }

    public RemittentHasReceiverController(int id, Date applicationDate, String commentary, float amountOrigin, float totalAmount, boolean sendingOptionSMS, float amountDestiny, String paymentServiceId, String secondaryKey, float additionalChanges, Date creationDate, Date creationHour) {
       
        this.id = id;
    }
    
    @PostConstruct
    public void init(){
    
    }

    
    public void save(){
    
        System.out.println("Hola entro al metodo!!!!!!");
               
    }
    
      public void reset() {
        RequestContext.getCurrentInstance().reset("remittanceCreateForm:grid");
    }
      
      public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listRemittentHasReceiver.xhtml");
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
