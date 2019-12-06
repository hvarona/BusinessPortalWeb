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

import com.portal.business.commons.data.StoreBalanceData;
import com.portal.business.commons.data.StoreData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.Store;
import java.sql.Timestamp;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import com.portal.business.commons.models.StoreBalance;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

@ManagedBean
public class StoreBalanceController {
    private Long id;
    private Float creditLimit;
    private Date beginningDate;
    private Date endingDate;
    private Store store;
    private Country country;
    private Long storeId;
    private StoreBalance storeBalance;
    private StoreBalanceData storeBalanceData;
    
    private String messages = null;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
    //$$$


    
    @PostConstruct
    public void init(){
        HttpServletRequest req= (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        storeBalance = (StoreBalance) req.getAttribute("storeBalance");
        storeBalanceData = new StoreBalanceData();
        WsRequest request = new WsRequest();
        


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Float creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Date getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(Date beginningDate) {
        this.beginningDate = beginningDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
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

    
    
    

    public StoreBalance getStoreBalance() {
        return storeBalance;
    }

    public void setStoreBalance(StoreBalance storeBalance) {
        this.storeBalance = storeBalance;
    }

    public StoreBalanceData getStoreBalanceData() {
        return storeBalanceData;
    }

    public void setStoreBalanceData(StoreBalanceData storeBalanceData) {
        this.storeBalanceData = storeBalanceData;
    }

   

    public void save() throws GeneralException, NullParameterException {
                System.out.println("##### Entering save");

        WsRequest request;
        
        Timestamp daTimestamp;
        storeBalance = new StoreBalance();
        
        daTimestamp = new Timestamp(beginningDate.getTime());
        storeBalance.setBeginningDate(daTimestamp);
        
        daTimestamp = new Timestamp(endingDate.getTime());
        storeBalance.setEndingDate(daTimestamp);
        
        storeBalance.setCreditLimit(creditLimit);
        
        
// Segun Frank  
        StoreData storeData = new StoreData();        
        Store storeNew = new Store();     
        storeNew.setId(1L);  
        storeBalance.setStore(storeNew);
//

        request = new WsRequest();
        request.setParam(storeId);
        try {
            storeBalance.setStore(storeData.loadStore(request));
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(StoreBalanceController.class.getName()).log(Level.SEVERE, null, ex);
        }

        request = new WsRequest();
        request.setParam(storeBalance);
        storeBalanceData.saveStoreBalance(request);
        messages = "El storeBalance ha sido guardado con exito";
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(messages));
        
    }
        
    public void reset() {
       // RequestContext.getCurrentInstance().reset("UserCreateForm:grid");
        RequestContext.getCurrentInstance().reset("StoreBalanceCreateForm:grid");
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listStoreBalance.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.User1View.doRediret()");
        }
    }
}
