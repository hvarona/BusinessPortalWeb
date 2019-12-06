/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alodiga.primefaces.ultima.controller.storeBalance;

import com.portal.business.commons.data.AccessControlData;
import com.portal.business.commons.data.StoreBalanceData;
import com.portal.business.commons.data.StoreData;
import com.portal.business.commons.data.UserData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.Profile;
import com.portal.business.commons.models.Store;
import com.portal.business.commons.models.StoreBalance;
import com.portal.business.commons.models.UserHasProfile;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;

@ManagedBean(name="dtLazyStoreBalanceView")
@ViewScoped
public class ListStoreBalanceController implements Serializable {
    
    // Reemplaza Lazy y para filtros
    private List<StoreBalance> storeBalances ;
    private List<StoreBalance>filtered;
    
    // Negocio
    private StoreBalance selectedStoreBalance;
    private StoreBalanceData storeBalanceData = null;
    
    // Para el menu de seleccion Primefaces
    private String storeId;
    private Map<String,Long> storesMap = new HashMap<String, Long>();



    
    @PostConstruct
    public void init() {
        try {
            storeBalanceData = new StoreBalanceData();
            WsRequest request = new WsRequest();
            storeBalances = storeBalanceData.loadStoreBalance(request);
        
            
            StoreData storeData = new StoreData();
            List<Store> stores = storeData.getStore(request);
            for (Store store : stores) {
                storesMap.put(store.getLogin(), store.getId());
                
            }

            
        } catch (EmptyListException ex) {
            ex.printStackTrace();
        } catch (GeneralException ex) {
            ex.printStackTrace();
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(ListStoreBalanceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListStoreBalanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
    public StoreBalance getSelectedStoreBalance() {
        return selectedStoreBalance;
    }

    public void setSelectedStoreBalance(StoreBalance selectedStoreBalance) {
        this.selectedStoreBalance = selectedStoreBalance;
    }
   
    public List<StoreBalance> save2()  throws GeneralException, NullParameterException, RegisterNotFoundException, EmptyListException {
//        System.out.println("BICHAAAAA!!");
        StoreBalanceData storeBalanceData = new StoreBalanceData();
        
        WsRequest request=new WsRequest();
        
        List<StoreBalance> lista = storeBalanceData.loadStoreBalance(request);
        
        for(StoreBalance storeBalanceTmp : lista) {
            System.out.println("Este es el registro: " + storeBalanceTmp.toString());
        }
               
        return lista;
        
    }
    
    public void save() throws GeneralException, NullParameterException {
        
        WsRequest request = new WsRequest();
        
        request.setParam(Long.parseLong(storeId)); 
        Store store=null;
        StoreData storeData = new StoreData();
        try {
            store = storeData.loadStore(request);
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(ListStoreBalanceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        selectedStoreBalance.setStore(store);
        request.setParam(selectedStoreBalance);
        
        storeBalanceData.saveStoreBalance(request);
    }
    
    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
    
    public void doRediret() {
        try {
            
            FacesContext.getCurrentInstance().getExternalContext().redirect("storeBalanceView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.storeBalance.LazyStoreBalanceView.doRediret()");
        }
    }
    
    
    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public List<StoreBalance> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<StoreBalance> filtered) {
        this.filtered = filtered;
    }

    public List<StoreBalance> getStoreBalances() {
        return storeBalances;
    }

    public void setStoreBalances(List<StoreBalance> storeBalances) {
        this.storeBalances = storeBalances;
    }

    public Map<String, Long> getStoresMap() {
        return storesMap;
    }

    public void setStoresMap(Map<String, Long> storesMap) {
        this.storesMap = storesMap;
    }
    
}
