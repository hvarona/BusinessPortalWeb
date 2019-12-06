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
package com.alodiga.primefaces.ultima.controller.excludeList;


import com.portal.business.commons.data.ExcludeListData;

import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
//import org.primefaces.model.DualListModel;
import com.portal.business.commons.models.ExcludeList;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

@ManagedBean
public class ExcludeListsController {
    
    //Objeto y el DATA 
    private ExcludeList excludeList;
    private ExcludeListData excludeListData;
    
    //Propiedades del Objeto
    private Long id;
    private Long enterprise;
    private Long country;
    private Long correspondent;
    private Long paymentMethod;
    
    private Map<String,Long> enterprisesMap = new HashMap<String, Long>();
    private Map<String,Long> countriesMap = new HashMap<String, Long>();
    private Map<String,Long> correspondentsMap = new HashMap<String, Long>();
    private Map<String,Long> paymentMethodsMap = new HashMap<String, Long>();

    //Propiedades de Prime
    private String messages = null;

    public Map<String, Long> getEnterprisesMap() {
        return enterprisesMap;
    }

    public void setEnterprisesMap(Map<String, Long> enterprisesMap) {
        this.enterprisesMap = enterprisesMap;
    }

    public Map<String, Long> getCountriesMap() {
        return countriesMap;
    }

    public void setCountriesMap(Map<String, Long> countriesMap) {
        this.countriesMap = countriesMap;
    }

    public Map<String, Long> getCorrespondentsMap() {
        return correspondentsMap;
    }

    public void setCorrespondentsMap(Map<String, Long> correspondentsMap) {
        this.correspondentsMap = correspondentsMap;
    }

    public Map<String, Long> getPaymentMethodsMap() {
        return paymentMethodsMap;
    }

    public void setPaymentMethodsMap(Map<String, Long> paymentMethodsMap) {
        this.paymentMethodsMap = paymentMethodsMap;
    }

    
    @PostConstruct
    public void init(){
        System.out.println("WEEPAAA $$$$$$$$$$$$$$$$ EN INIT");

        HttpServletRequest req= (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        excludeList = (ExcludeList) req.getAttribute("excludeList");
        excludeListData = new ExcludeListData();


        
        
        WsRequest request = new WsRequest();
        
        


        
        

        

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
  

    public ExcludeList getExcludeList() {
        return excludeList;
    }

    public void setExcludeList(ExcludeList excludeList) {
        this.excludeList = excludeList;
    }

    public ExcludeListData getExcludeListData() {
        return excludeListData;
    }

    public void setExcludeListData(ExcludeListData excludeListData) {
        this.excludeListData = excludeListData;
    }

   

    public void save() throws RegisterNotFoundException {
        WsRequest request;
        excludeList = new ExcludeList();
        messages = "El excludeList ha sido guardado con exito";
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(messages));
    }

        
    public void reset() {
       // RequestContext.getCurrentInstance().reset("UserCreateForm:grid");
        RequestContext.getCurrentInstance().reset("ExcludeListCreateForm:grid");
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listExcludeList.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.ExcludeListView.doRediret()");
        }
    }
}
