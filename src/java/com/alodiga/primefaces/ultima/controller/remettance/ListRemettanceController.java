package com.alodiga.primefaces.ultima.controller.remettance;

import com.alodiga.primefaces.ultima.controller.city.ListCityController;
import com.alodiga.primefaces.ultima.controller.user.ListUserController;
import com.portal.business.commons.data.RemittanceData;
import com.portal.business.commons.data.RemittanceHasRemittenceStatusData;
import com.portal.business.commons.data.StoreData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Remittance;
import com.portal.business.commons.models.RemittanceHasRemittenceStatus;
import com.portal.business.commons.models.RemittanceStatus;
import com.portal.business.commons.models.Store;
import com.portal.business.commons.utils.QueryConstants;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


@ManagedBean(name="dtListRemittanceController")
@ViewScoped
public class ListRemettanceController implements Serializable {
    
    
    private Remittance selectedRemittance;
    private RemittanceData remittanceData = null;
    private StoreData storeData = null;
    private UtilsData utilsData = null;
    private RemittanceHasRemittenceStatusData hasRemittenceStatusData = null;
    List<Remittance> remittances;
    List<Remittance> filteredRemittance;
    private String amountRemittance;
    private String amountReciever;
    private Store store;
    private RemittanceStatus status;
    List<Remittance> remittancesByRemittent;
    List<Remittance> remittancesByReceiver;
    List<RemittanceHasRemittenceStatus> remittanceStatuses;
    private String comments;
    private boolean activeAuthorize;
    private boolean activeCancel;
    private Map<String,String> remittanceStatus = null;
    private Map<String,String> stores = null;
    List<Remittance> remittancesPendding;
    
    @PostConstruct
    public void init() {
        
            remittanceData = new RemittanceData();
            storeData = new StoreData();
            utilsData = new UtilsData();
            hasRemittenceStatusData = new RemittanceHasRemittenceStatusData();
            WsRequest request = new WsRequest();
        try {
            remittances = remittanceData.getRemittances();
        } catch (GeneralException ex) {
        } catch (RegisterNotFoundException ex) {
        } catch (NullParameterException ex) {
        } catch (EmptyListException ex) {
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

    public List<Remittance> getFilteredRemittance() {
        return filteredRemittance;
    }

    public void setFilteredRemittance(List<Remittance> filteredRemittance) {
        this.filteredRemittance = filteredRemittance;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public RemittanceStatus getStatus() {
        return status;
    }

    public void setStatus(RemittanceStatus status) {
        this.status = status;
    }

    public List<Remittance> getRemittancesPendding() {
        try {
            remittancesPendding = remittanceData.getRemittancesByStatusId(RemittanceStatus.APRROVAL_PENDDING);
        } catch (GeneralException ex) {
            Logger.getLogger(ListRemettanceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(ListRemettanceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListRemettanceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ex) {
            Logger.getLogger(ListRemettanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return remittancesPendding;
    }

    public void setRemittancesPendding(List<Remittance> remittancesPendding) {
        this.remittancesPendding = remittancesPendding;
    }
    
    
    public Map<String, String> getRemittanceStatus() {
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        remittanceStatus = new TreeMap<String, String>();
        try {
            List<RemittanceStatus> statuses = remittanceData.getRemittenceStatus(request);
            for (RemittanceStatus status : statuses) {
                remittanceStatus.put(status.getName(), status.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return remittanceStatus;
    }

    public void setRemittanceStatus(Map<String, String> remittanceStatus) {
        this.remittanceStatus = remittanceStatus;
    }

    public Map<String, String> getStores() {
        WsRequest request = new WsRequest();
        Map params = new HashMap();

        request.setParams(params);
        stores = new TreeMap<String, String>();
        try {
            List<Store> stores1 = storeData.getStore(request);
            for (Store store : stores1) {
                stores.put(store.getFirstName(), store.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stores;
    }

    public void setStores(Map<String, String> stores) {
        this.stores = stores;
    }

    public String getAmountRemittance() {
        amountRemittance = "1521";
        return amountRemittance;
    }

    public void setAmountRemittance(String amountRemittance) {
        this.amountRemittance = amountRemittance;
    }

    public String getAmountReciever() {
        amountReciever= "1521";
        return amountReciever;
    }

    public void setAmountReciever(String amountReciever) {
        this.amountReciever = amountReciever;
    }

    public List<Remittance> getRemittancesByRemittent() {
        if (selectedRemittance != null) {
            try {
                remittancesByRemittent = remittanceData.getRemittancesByRemittentId(selectedRemittance.getRemittent().getId());
            } catch (GeneralException ex) {
                Logger.getLogger(ListRemettanceController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullParameterException ex) {
                Logger.getLogger(ListRemettanceController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EmptyListException ex) {
                Logger.getLogger(ListRemettanceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return remittancesByRemittent;
    }

    public void setRemittancesByRemittent(List<Remittance> remittancesByRemittent) {
        this.remittancesByRemittent = remittancesByRemittent;
    }

    public List<Remittance> getRemittancesByReceiver() {
        if (selectedRemittance != null) {
            try {
                remittancesByReceiver = remittanceData.getRemittancesByReceiverId(selectedRemittance.getReceiver().getId());
            } catch (GeneralException ex) {
                Logger.getLogger(ListRemettanceController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullParameterException ex) {
                Logger.getLogger(ListRemettanceController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EmptyListException ex) {
                Logger.getLogger(ListRemettanceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return remittancesByReceiver;
    }
    

    public void setRemittancesByReceiver(List<Remittance> remittancesByReceiver) {
        this.remittancesByReceiver = remittancesByReceiver;
    }

    public List<RemittanceHasRemittenceStatus> getRemittanceStatuses() {
        if (selectedRemittance != null) {
            try {
                remittanceStatuses = remittanceData.getRemittancesStatusByRemittanceId(selectedRemittance.getId());
            } catch (GeneralException ex) {
                Logger.getLogger(ListRemettanceController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RegisterNotFoundException ex) {
                Logger.getLogger(ListRemettanceController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullParameterException ex) {
                Logger.getLogger(ListRemettanceController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EmptyListException ex) {
                Logger.getLogger(ListRemettanceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return remittanceStatuses;
    }

    public void setRemittanceStatuses(List<RemittanceHasRemittenceStatus> remittanceStatuses) {
        this.remittanceStatuses = remittanceStatuses;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public void doChanceStatus() {
        try {
            WsRequest request = new WsRequest();
//            selectedRemittance.setEnabled(!selectedStore.isEnabled());
//            request.setParam(selectedStore);
            storeData.saveStore(request);
        } catch (NullParameterException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.ListStoreController.doChanceStatus()");
            Logger.getLogger(ListUserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.ListStoreController.doChanceStatus()");
            Logger.getLogger(ListUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   

    public void authorize() {
        
        try {
            RemittanceHasRemittenceStatus remittanceHasRemittenceStatus  = hasRemittenceStatusData.updateRemittenceStatus(selectedRemittance,RemittanceStatus.PROCESSED,1L,comments);
//            se ejecuta la remesa
//            remittanceData.executeRemittance(selectedRemittance, selectedRemittance.getRemittent(), selectedRemittance.getReceiver(), selectedRemittance.getAddressReciever(), selectedRemittance.getAddressRemittent(), selectedRemittance.getAmountDestiny(), selectedRemittance.getAmountOrigin());
        } catch (NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Faltan parametros"));
        } catch (GeneralException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        }
    }
    
     public void cancel() {
        try {
// anular remesa con proveedor
            RemittanceHasRemittenceStatus remittanceHasRemittenceStatus  = hasRemittenceStatusData.updateRemittenceStatus(selectedRemittance,RemittanceStatus.NULLED,1L,comments);
        } catch (NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Faltan parametros"));
        } catch (GeneralException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        }
    }
     
     
    
     public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("editRemittance.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.ListStoreController.doRediret()");
        }
    }

    public boolean isActiveAuthorize() {
        activeAuthorize = false;
        if (selectedRemittance!=null && selectedRemittance.getStatus().getId().equals(RemittanceStatus.APRROVAL_PENDDING))
            activeAuthorize = true;
        return activeAuthorize;
    }

    public void setActiveAuthorize(boolean activeAuthorize) {
        this.activeAuthorize = activeAuthorize;
    }

    public boolean isActiveCancel() {
        activeCancel = false;
        if (selectedRemittance!=null && !selectedRemittance.getStatus().getId().equals(RemittanceStatus.STORE_CLOSED) && !selectedRemittance.getStatus().getId().equals(RemittanceStatus.CLOSED) 
            && !selectedRemittance.getStatus().getId().equals(RemittanceStatus.NULLED) && !selectedRemittance.getStatus().getId().equals(RemittanceStatus.DELIVERED))
            activeCancel = true;
        return activeCancel;
    }

    public void setActiveCancel(boolean activeCancel) {
        this.activeCancel = activeCancel;
    }
     
    
}
