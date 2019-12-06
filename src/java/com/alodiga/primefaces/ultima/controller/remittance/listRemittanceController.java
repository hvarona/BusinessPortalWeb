package com.alodiga.primefaces.ultima.controller.remittance;

import com.portal.business.commons.data.RemittanceData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Remittance;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;

/**
 * @author frank
 */
@ManagedBean(name="listRemittanceController")
@ViewScoped
public class listRemittanceController implements Serializable  {
    
    private Remittance selectedRemittance;
    private List<Remittance> remittances;
    private List<Remittance> filtereds;
    private RemittanceData remittanceData = null;
    private Long id;
    
    public listRemittanceController(){}

    public listRemittanceController(Remittance selectedRemittance, List<Remittance> remittances, List<Remittance> filtereds) {
        this.selectedRemittance = selectedRemittance;
        this.remittances = remittances;
        this.filtereds = filtereds;
    }
    
    
    
    @PostConstruct
    public void init(){
        
        remittanceData = new RemittanceData();
        WsRequest request = new WsRequest();
        
        try {
            
            remittances = remittanceData.loadRemittance(request);
        
        } catch (GeneralException | NullParameterException | EmptyListException ex) {
            Logger.getLogger(listRemittanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
     }

    public Remittance getSelectedRemittance() {
        return selectedRemittance;
    }

    public void setSelectedRemittance(Remittance selectedRemittance) {
        this.selectedRemittance = selectedRemittance;
    }

    public List<Remittance> getRemittances() {
        return remittances;
    }

    public void setRemittances(List<Remittance> remittances) {
        this.remittances = remittances;
    }

    public List<Remittance> getFiltereds() {
        return filtereds;
    }

    public void setFiltereds(List<Remittance> filtereds) {
        this.filtereds = filtereds;
    }

    public RemittanceData getRemittanceData() {
        return remittanceData;
    }

    public void setRemittanceData(RemittanceData remittanceData) {
        this.remittanceData = remittanceData;
    }
    
    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
    
    
    public void save(){
    
        try {
//            System.out.println("Hola entro al metodo!!!!!!");
            remittanceData.saveRemittance(selectedRemittance);
        } catch (NullParameterException ex) {
            Logger.getLogger(listRemittanceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(listRemittanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    
    }
    
     public void doChanceStatus() {
          
        try {
            
            selectedRemittance.getPaymentNetwork().setEnabled(!selectedRemittance.getPaymentNetwork().isEnabled());
            remittanceData.saveRemittance(selectedRemittance);
            
        }catch (NullParameterException | GeneralException ex) {
   
            Logger.getLogger(listRemittanceController.class.getName()).log(Level.SEVERE, null, ex);
        
        }
    }

    @Override
   public int hashCode() {
       int hash = 7;
       hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
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
       final listRemittanceController other = (listRemittanceController) obj;
       if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
           return false;
       }
       return true;
   }
 
     
     
     
}
