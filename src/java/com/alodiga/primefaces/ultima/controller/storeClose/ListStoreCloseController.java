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
package com.alodiga.primefaces.ultima.controller.storeClose;

import com.portal.business.commons.data.StoreCloseData;
import com.portal.business.commons.data.StoreData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Store;
import com.portal.business.commons.models.StoreClose;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

@ManagedBean(name="dtLazyStoreCloseView")
@ViewScoped
public class ListStoreCloseController implements Serializable {
    
    //private LazyDataModel<StoreClose> lazyModel;
    
    private StoreClose selectedStoreClose;
    private StoreCloseData storeCloseData = null;
    private String storeId;

    
        // Reemplaza Lazy y para filtros
    private List<StoreClose> storeClose;
    private List<StoreClose> filtered;
    
    // Negocio    
    
    private Map<String,Long> storesMap = new HashMap<String, Long>();

    
    Date store;

    public List<StoreClose> getStoreClose() {
        return storeClose;
    }

    public void setStoreCloses(List<StoreClose> storeClose) {
        this.storeClose = storeClose;
    }

    public List<StoreClose> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<StoreClose> filtered) {
        this.filtered = filtered;
    }

    
    
        @PostConstruct
    public void init() {
        storeCloseData = new StoreCloseData();
        WsRequest request = new WsRequest();
        try {
            storeClose = storeCloseData.loadStoreClose(request);
            StoreData storeData = new StoreData();
            /*List<Store> stores = storeData.getStore(request);
            for (Store store : stores) {
                storesMap.put(store.getName(), store.getId());
            } */     
        } catch (EmptyListException ex) {
            ex.printStackTrace();
        } catch (GeneralException ex) {
            ex.printStackTrace();
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(ListStoreCloseController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListStoreCloseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    

    public Map<String, Long> getStoresMap() {
        return storesMap;
    }

    public void setStoresMap(Map<String, Long> storesMap) {
        this.storesMap = storesMap;
    }
    
//    public LazyDataModel<StoreClose> getLazyModel() {
//        return lazyModel;
//    }

    public StoreClose getSelectedStoreClose() {
        return selectedStoreClose;
    }

    public void setSelectedStoreClose(StoreClose selectedStoreClose) {
        this.selectedStoreClose = selectedStoreClose;
    }

//    public DualListModel<Profile> getProfiles() {
//         try{
//            accessControlData = new AccessControlData();
//            StoreCloseData = new StoreCloseData();
//            WsRequest request = new WsRequest();
//            List<Profile>  list = accessControlData.getProfiles(request);
//            List<Profile>  profilesSource = new ArrayList<Profile>();
//            List<Profile> profilesTarget =  new ArrayList<Profile>();
//            if (selectedStoreClose != null) {
//
//                for (UserHasProfile userHasProfile : selectedStoreClose.StoreClose){
//                    profilesSource.add(userHasProfile.getProfile());
//                }
//                for (Profile profile : list) {
//                    if (!profilesSource.contains(profile)) {
//                        profilesTarget.add(profile);
//                    }
//
//                }
//            }else{
//                 profilesSource = list;
//            }
//            
//            profiles = new DualListModel<Profile>(profilesSource, profilesTarget);
//        } catch (EmptyListException ex) {
//        } catch (GeneralException ex) {
//        } catch (NullParameterException ex) {
//        }
//        return profiles;
//    }
//
//    public void setProfiles(DualListModel<Profile> profiles) {
//        this.profiles = profiles;
//    }

    public void save() throws GeneralException, NullParameterException {
        
        System.out.println("################## ENTRE AL SAVE");
//        System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHhh");
//        System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHhh");
//        System.out.println("el time es: " +  selectedStoreClose.getClosedate().toString() );
        WsRequest request = new WsRequest();
//        selectedStoreClose.setClosedate(new java.sql.Timestamp(store.getTime()));

        request.setParam(Long.parseLong(storeId));
        Store store = null;
        StoreData storeData = new StoreData();
        try {
            store = storeData.loadStore(request);
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(ListStoreCloseController.class.getName()).log(Level.SEVERE, null, ex);
        }

        selectedStoreClose.setStore(store);
        request.setParam(selectedStoreClose);
        storeCloseData.saveStoreClose(request);
    }
    
    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
    
    public void doRediret() {
        try {
            
                    //System.out.println("##### Entering redirect");

            FacesContext.getCurrentInstance().getExternalContext().redirect("storeCloseView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.storeClose.LazyStoreCloseView.doRediret()");
        }
    }
    
}
