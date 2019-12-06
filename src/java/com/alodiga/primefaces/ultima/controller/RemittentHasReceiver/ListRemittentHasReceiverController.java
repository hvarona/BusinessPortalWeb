package com.alodiga.primefaces.ultima.controller.RemittentHasReceiver;

import com.portal.business.commons.data.RemittentHasReceiverData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.RemittentHasReceiver;
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
@ManagedBean(name="listRemittentHasReceiverController")
@ViewScoped
public class ListRemittentHasReceiverController implements Serializable  {
    
    private Long id;
    private RemittentHasReceiver selectedRemittentHasReceiver;
    private List<RemittentHasReceiver> RemittentHasReceivers;
    private List<RemittentHasReceiver> filtereds;
    private RemittentHasReceiverData remittentHasReceiverData;
    
    public ListRemittentHasReceiverController(){}
    
    @PostConstruct
    public void init(){
        
        remittentHasReceiverData = new RemittentHasReceiverData();
        WsRequest request = new WsRequest();
        
            try {
                RemittentHasReceivers = remittentHasReceiverData.loadRemittentHasReceiver(request);
            } catch (EmptyListException | GeneralException | NullParameterException ex) {
                Logger.getLogger(ListRemittentHasReceiverController.class.getName()).log(Level.SEVERE, null, ex);
            }
           
     }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RemittentHasReceiver getSelectedRemittentHasReceiver() {
        return selectedRemittentHasReceiver;
    }

    public void setSelectedRemittentHasReceiver(RemittentHasReceiver selectedRemittentHasReceiver) {
        this.selectedRemittentHasReceiver = selectedRemittentHasReceiver;
    }

    public List<RemittentHasReceiver> getRemittentHasReceivers() {
        return RemittentHasReceivers;
    }

    public void setRemittentHasReceivers(List<RemittentHasReceiver> RemittentHasReceivers) {
        this.RemittentHasReceivers = RemittentHasReceivers;
    }

    public List<RemittentHasReceiver> getFiltereds() {
        return filtereds;
    }

    public void setFiltereds(List<RemittentHasReceiver> filtereds) {
        this.filtereds = filtereds;
    }

    public RemittentHasReceiverData getRemittentHasReceiverData() {
        return remittentHasReceiverData;
    }

    public void setRemittentHasReceiverData(RemittentHasReceiverData remittentHasReceiverData) {
        this.remittentHasReceiverData = remittentHasReceiverData;
    }
     

    public void save(){
    
        try {
            System.out.println("Hola entro al metodo!!!!!!");
            remittentHasReceiverData.saveRemittentHasReceiver(selectedRemittentHasReceiver);
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(ListRemittentHasReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
      public void handleReturnDialog(SelectEvent event){
        if (event != null && event.getObject() != null){
        }
    }
     public void doChanceStatus() {
         
           try {
            
            WsRequest request = new WsRequest();
            selectedRemittentHasReceiver.getReceiver().getPerson().setEnabled(!selectedRemittentHasReceiver.getReceiver().getPerson().getEnabled());
            request.setParam(selectedRemittentHasReceiver);
            remittentHasReceiverData.saveRemittentHasReceiver(selectedRemittentHasReceiver);
            
        }catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(ListRemittentHasReceiverController.class.getName()).log(Level.SEVERE, null, ex);
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
       final ListRemittentHasReceiverController other = (ListRemittentHasReceiverController) obj;
       if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
           return false;
       }
       return true;
   }
     
}
