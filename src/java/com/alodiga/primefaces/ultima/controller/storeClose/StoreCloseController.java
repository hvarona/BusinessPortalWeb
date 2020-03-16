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

import com.portal.business.commons.data.AccessControlData;
import com.portal.business.commons.data.StoreCloseData;
import com.portal.business.commons.data.StoreData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Profile;
import com.portal.business.commons.models.Store;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
//import org.primefaces.model.DualListModel;
import com.portal.business.commons.models.StoreClose;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

@ManagedBean
public class StoreCloseController {
    
    private Long id;
    private Float closeamount;
    private Date closedate;
    private Store store;
    private Long storeId;
    private String description;
    private Long storeView;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getStoreView() {
        return storeView;
    }

    public void setStoreView(Long storeView) {
        this.storeView = storeView;
    }
    
    
    
    private StoreClose storeClose;
    private StoreCloseData storeCloseData;
    
        private String messages = null;

    
    @PostConstruct
    public void init(){
        HttpServletRequest req= (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        storeClose = (StoreClose) req.getAttribute("storeClose");
        storeCloseData = new StoreCloseData();
        WsRequest request = new WsRequest();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getCloseAmount() {
        return closeamount;
    }

    public void setCloseAmount(Float closeamount) {
        this.closeamount = closeamount;
    }

    public Float getCloseamount() {
        return closeamount;
    }

    public void setCloseamount(Float closeamount) {
        this.closeamount = closeamount;
    }

    public Date getClosedate() {
        return closedate;
    }

    public void setClosedate(Date closedate) {
        this.closedate = closedate;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    
    
    

    public StoreClose getStoreClose() {
        return storeClose;
    }

    public void setStoreClose(StoreClose storeClose) {
        this.storeClose = storeClose;
    }

    public StoreCloseData getStoreCloseData() {
        return storeCloseData;
    }

    public void setStoreCloseData(StoreCloseData storeCloseData) {
        this.storeCloseData = storeCloseData;
    }

   

    public void save() throws GeneralException, NullParameterException {
        

// $$$ TODO Implementar
        
        System.out.println("##### Entering save");

        storeClose = new StoreClose();
        storeClose.setCloseAmount(closeamount);
        Timestamp daTimestamp = new Timestamp(closedate.getTime());
        storeClose.setCloseDate(daTimestamp);
        
        // Segun Frank  
        WsRequest request = new WsRequest();
        request.setParam(storeId);
        StoreData storeData = new StoreData();        
        //Store storeNew = new Store();     
        //Store storeNew=null;
        try {
            store = storeData.loadStore(request);
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(StoreCloseController.class.getName()).log(Level.SEVERE, null, ex);
        }
        

//storeNew.setId(1L);  
        
        storeClose.setStore(store);   

//

        request = new WsRequest();
        request.setParam(storeClose);
        storeCloseData.saveStoreClose(request);
        messages = "El storeClose ha sido guardado con exito";
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(messages));
        
    }
        
    public void reset() {
       // RequestContext.getCurrentInstance().reset("UserCreateForm:grid");
        RequestContext.getCurrentInstance().reset("StoreCloseCreateForm:grid");
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listStoreClose.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.User1View.doRediret()");
        }
    }
}
