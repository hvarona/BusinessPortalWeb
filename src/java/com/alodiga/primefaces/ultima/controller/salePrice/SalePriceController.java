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
package com.alodiga.primefaces.ultima.controller.salePrice;

import com.portal.business.commons.data.AccessControlData;
import com.portal.business.commons.data.SalePriceData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Correspondent;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.Enterprise;
import com.portal.business.commons.models.PaymentMethod;
import com.portal.business.commons.models.Profile;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
//import org.primefaces.model.DualListModel;
import com.portal.business.commons.models.SalePrice;
import com.portal.business.commons.models.SalePriceHistory;
import com.portal.business.commons.models.SaleType;
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
public class SalePriceController {
    
    //Objeto y el DATA 
    private SalePrice salePrice;
    private SalePriceData salePriceData;
    
    //Propiedades del Objeto
    private Long id;
    private Long enterprise;
    private Long country;
    private Long correspondent;
    private Long saleType;
    private Double amount = new Double(0);
    
    private Map<String,Long> enterprisesMap = new HashMap<String, Long>();
    private Map<String,Long> countriesMap = new HashMap<String, Long>();
    private Map<String,Long> correspondentsMap = new HashMap<String, Long>();
    private Map<String,Long> saleTypeMap = new HashMap<String, Long>();

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

    public Map<String, Long> getSaleTypeMap() {
        return saleTypeMap;
    }

    public void setSaleTypeMap(Map<String, Long> saleTypeMap) {
        this.saleTypeMap = saleTypeMap;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @PostConstruct
    public void init(){
        System.out.println("WEEPAAA $$$$$$$$$$$$$$$$ EN INIT");
        amount=0d;
        HttpServletRequest req= (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        salePrice = (SalePrice) req.getAttribute("salePrice");
        salePriceData = new SalePriceData();

        UtilsData enterpriseData = new UtilsData();
        
        
        WsRequest request = new WsRequest();
        
        try {
            List<Enterprise> enterprises = enterpriseData.getEnterprises();
              for (Enterprise enterpriseTmp : enterprises) {
                enterprisesMap.put(enterpriseTmp.getName(), enterpriseTmp.getId());
              } 
        
        
        } catch (EmptyListException ex) {
            Logger.getLogger(SalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(SalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(SalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            List<Country> countries = enterpriseData.getCountries();
            for (Country countryTmp : countries) {
                countriesMap.put(countryTmp.getName(), countryTmp.getId());
            }
        
        } catch (EmptyListException ex) {
            Logger.getLogger(SalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(SalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(SalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            List<Correspondent> correspondents = enterpriseData.getCorrespondentList();
            for (Correspondent correspondentTmp : correspondents) {
                correspondentsMap.put(correspondentTmp.getName(), correspondentTmp.getId());
            }
            
        } catch (EmptyListException ex) {
            Logger.getLogger(SalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(SalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(SalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        
        try {
            List<SaleType> saleTypes = enterpriseData.getSaleTypes(request);
            for (SaleType saleType : saleTypes) {
                saleTypeMap.put(saleType.getName(), saleType.getId());
            }
        
        
        } catch (EmptyListException ex) {
            Logger.getLogger(SalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(SalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(SalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        }

        

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Long enterprise) {
        this.enterprise = enterprise;
    }



    public Long getCountry() {
        return country;
    }

    public void setCountry(Long country) {
        this.country = country;
    }

    public Long getCorrespondent() {
        return correspondent;
    }

    public void setCorrespondent(Long correspondent) {
        this.correspondent = correspondent;
    }

    public Long getSaleType() {
        return saleType;
    }

    public void setSaleType(Long saleType) {
        this.saleType = saleType;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    

    public SalePrice getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(SalePrice salePrice) {
        this.salePrice = salePrice;
    }

    public SalePriceData getSalePriceData() {
        return salePriceData;
    }

    public void setSalePriceData(SalePriceData salePriceData) {
        this.salePriceData = salePriceData;
    }

   

    public void save() throws RegisterNotFoundException {
        try {
            
            UtilsData utilsData = new UtilsData();
            
            WsRequest request;
            
            salePrice = new SalePrice();

            
            
            
            request = new WsRequest();
            request.setParam(enterprise);
            salePrice.setEnterprise(utilsData.loadEnterprise(request)); 

            request = new WsRequest();
            request.setParam(country);
            salePrice.setCountry(utilsData.loadCountry(request));

            request = new WsRequest();
            request.setParam(correspondent);
            salePrice.setCorrespondent(utilsData.loadCorrespondent(request));
            
            request = new WsRequest();
            request.setParam(saleType);
            salePrice.setSaleType(utilsData.loadSaleType(request));

            List<SalePriceHistory> salePriceHistorys = new ArrayList<SalePriceHistory>();
            SalePriceHistory salePriceHistory  =new SalePriceHistory();
            String newAmount = amount.toString();
            salePriceHistory.setCurrentAmount(Float.parseFloat(newAmount));
            salePriceHistory.setSalePrice(salePrice);
            Timestamp time = new Timestamp(new Date().getTime());
            salePriceHistory.setBeginningDate(time);
            salePriceHistorys.add(salePriceHistory);
            salePrice.setSalePriceHistorys(salePriceHistorys);
            /*----------------------------------*/
           request = new WsRequest();
           request.setParam(salePrice);
           salePriceData.saveSalePrice(request);
            
            messages = "El salePrice ha sido guardado con exito";
            
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(messages));
        } catch (NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Faltan parametros"));
        } catch (GeneralException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        }
    }

        
    public void reset() {
       // RequestContext.getCurrentInstance().reset("UserCreateForm:grid");
        RequestContext.getCurrentInstance().reset("SalePriceCreateForm:grid");
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listSalePrice.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.User1View.doRediret()");
        }
    }
}
