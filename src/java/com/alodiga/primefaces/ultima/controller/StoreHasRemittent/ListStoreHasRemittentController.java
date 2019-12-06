package com.alodiga.primefaces.ultima.controller.StoreHasRemittent;

import com.portal.business.commons.data.StoreHasRemittentData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.StoreHasRemittent;
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
@ManagedBean(name="listStoreHasRemittentController")
@ViewScoped
public class ListStoreHasRemittentController implements Serializable  {
    
    private Long id;
    private StoreHasRemittent selectedStoreHasRemittent;
    private List<StoreHasRemittent> storeHasRemittents;
    private List<StoreHasRemittent> filtereds;
    private StoreHasRemittentData storeHasRemittentData;
    
    public ListStoreHasRemittentController(){}
    
    @PostConstruct
    public void init(){
        
        storeHasRemittentData = new StoreHasRemittentData();
        WsRequest request = new WsRequest();
        
            try {
                storeHasRemittents = storeHasRemittentData.loadStoreHasRemittentData(request);
            } catch (EmptyListException | GeneralException | NullParameterException ex) {
                Logger.getLogger(ListStoreHasRemittentController.class.getName()).log(Level.SEVERE, null, ex);
            }
           
     }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StoreHasRemittent getSelectedStoreHasRemittent() {
        return selectedStoreHasRemittent;
    }

    public void setSelectedStoreHasRemittent(StoreHasRemittent selectedStoreHasRemittent) {
        this.selectedStoreHasRemittent = selectedStoreHasRemittent;
    }

    public List<StoreHasRemittent> getStoreHasRemittents() {
        return storeHasRemittents;
    }

    public void setStoreHasRemittents(List<StoreHasRemittent> storeHasRemittents) {
        this.storeHasRemittents = storeHasRemittents;
    }

    public List<StoreHasRemittent> getFiltereds() {
        return filtereds;
    }

    public void setFiltereds(List<StoreHasRemittent> filtereds) {
        this.filtereds = filtereds;
    }

    public StoreHasRemittentData getStoreHasRemittentData() {
        return storeHasRemittentData;
    }

    public void setStoreHasRemittentData(StoreHasRemittentData storeHasRemittentData) {
        this.storeHasRemittentData = storeHasRemittentData;
    }

    public void save(){
    
        try {
            System.out.println("Hola entro al metodo!!!!!!");
            storeHasRemittentData.saveStoreHasRemittentData(selectedStoreHasRemittent);
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(ListStoreHasRemittentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
      public void handleReturnDialog(SelectEvent event){
        if (event != null && event.getObject() != null){
        }
    }
     public void doChanceStatus() {
         
           try {
            
            WsRequest request = new WsRequest();
            selectedStoreHasRemittent.getRemittent().getPerson().setEnabled(!selectedStoreHasRemittent.getRemittent().getPerson().getEnabled());
            request.setParam(selectedStoreHasRemittent);
            storeHasRemittentData.saveStoreHasRemittentData(selectedStoreHasRemittent);
            
        }catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(ListStoreHasRemittentController.class.getName()).log(Level.SEVERE, null, ex);
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
       final ListStoreHasRemittentController other = (ListStoreHasRemittentController) obj;
       if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
           return false;
       }
       return true;
   }
   
   
   
}
