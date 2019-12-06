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

import com.alodiga.primefaces.ultima.controller.salePrice.LazySalePriceDataModel;
import com.portal.business.commons.data.AccessControlData;
import com.portal.business.commons.data.SalePriceData;
import com.portal.business.commons.data.UserData;
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
import com.portal.business.commons.models.SalePrice;
import com.portal.business.commons.models.SaleType;
import com.portal.business.commons.models.UserHasProfile;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

@ManagedBean(name="dtLazySalePriceView")
@ViewScoped
public class ListSalePriceController implements Serializable {
    
    
    // Reemplaza Lazy y para filtros
    private List<SalePrice> salePrices;
    private List<SalePrice> filtered;
    
    private LazyDataModel<SalePrice> lazyModel;
    private Country country= new Country();
    private Long enterpriseId;
    private Long countryId;
    private Long correspondentId;
    private Long saleTypeId;
    
    private SalePrice selectedSalePrice;
    private SalePriceData salePriceData = null;
//    private DualListModel<Profile> profiles;
//    private AccessControlData  accessControlData;

    public List<SalePrice> getSalePrices() {
        return salePrices;
    }

    public void setSalePrices(List<SalePrice> salePrices) {
        this.salePrices = salePrices;
    }

    public List<SalePrice> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<SalePrice> filtered) {
        this.filtered = filtered;
    }

    
    
    
    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getCorrespondentId() {
        return correspondentId;
    }

    public void setCorrespondentId(Long correspondentId) {
        this.correspondentId = correspondentId;
    }

    public Long getSaleTypeId() {
        return saleTypeId;
    }

    public void setSaleTypeId(Long saleTypeId) {
        this.saleTypeId = saleTypeId;
    }

    
    
    
    @PostConstruct
    public void init() {
        salePriceData = new SalePriceData();
        WsRequest request = new WsRequest();

        try {
            salePrices = salePriceData.loadSalePriceList(request);
            System.out.println("currentAmount"+salePrices.get(0).getCurrentSalePriceHistory().getCurrentAmount());
        } catch (GeneralException ex) {
            Logger.getLogger(ListSalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(ListSalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListSalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ex) {
            Logger.getLogger(ListSalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        }

        
//        try {
//            salePriceData = new SalePriceData();
//            request = new WsRequest();
//            //lazyModel = new LazySalePriceDataModel(salePriceData.loadSalePriceList(request));
//        
//            //$$$ 
//            //List<SalePrice> rrr = lazyModel.
//        
//            
//            //System.out.println("$$$$$$$$$$$$$$$$$$$$$$$");    
//            //System.out.println("" + rrr.toString());
//            
//        } catch (EmptyListException ex) {
//            ex.printStackTrace();
//        } catch (GeneralException ex) {
//            ex.printStackTrace();
//        } catch (RegisterNotFoundException ex) {
//            Logger.getLogger(ListSalePriceController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NullParameterException ex) {
//            Logger.getLogger(ListSalePriceController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public LazyDataModel<SalePrice> getLazyModel() {
        return lazyModel;
    }

    public SalePrice getSelectedSalePrice() {
        return selectedSalePrice;
    }

    public void setSelectedSalePrice(SalePrice selectedSalePrice) {
        this.selectedSalePrice = selectedSalePrice;
    }


//=========================================================
//    METODO SAVE
//=========================================================
    public void save() {
        
        WsRequest request = new WsRequest();
        UtilsData utilsData = new UtilsData();
        
        Enterprise enterpriseTmp        = new Enterprise();
        Country countryTmp              = new Country();
        Correspondent correspondentTmp  = new Correspondent();
        SaleType saleTypeTmp  = new SaleType();
        
        try {
            
//            System.out.println("$$$$$$$$$$$$$$$$$$$ ENTER SAVE");          
//            System.out.println("$$$$$$ EL ENTERPRISE " + enterpriseId);
          
            request.setParam(enterpriseId);
            enterpriseTmp = utilsData.loadEnterprise(request); 
            //System.out.println("&&&& ENTER " + enterpriseTmp.getName() + " EMAIL " + enterpriseTmp.getEmail());
            selectedSalePrice.setEnterprise(enterpriseTmp);

            request.setParam(countryId);  
            countryTmp = utilsData.loadCountry(request);
            //System.out.println("&&&& COUNTRY " + countryTmp.getName() + " SHORT " + countryTmp.getShortName());
            selectedSalePrice.setCountry(countryTmp);

            request.setParam(correspondentId);
            correspondentTmp = utilsData.loadCorrespondent(request);
            //System.out.println("&&&& correspondent " + correspondentTmp.getName() + " SHORT " + correspondentTmp.getCreationDate());
            selectedSalePrice.setCorrespondent(correspondentTmp);
            
            request.setParam(saleTypeId);
            saleTypeTmp = utilsData.loadSaleType(request);
            //System.out.println("&&&& paymentMethod " + paymentMethodTmp.getName() + " ENABLE " + paymentMethodTmp.isEnabled());
            selectedSalePrice.setSaleType(saleTypeTmp);
            
            //request.setParam(selectedSalePrice);
//            salePriceData.saveSalePrice(selectedSalePrice);
            request = new WsRequest();
            request.setParam(selectedSalePrice);
            selectedSalePrice = salePriceData.saveSalePrice(request);
        
        
        
        } catch (NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Faltan parametros"));
        } catch (GeneralException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(ListSalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("salePriceView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.LazySalePriceView.doRediret()");
        }
    }
    
    public void doChanceStatus() {
        try {
            WsRequest request = new WsRequest();
            //selectedSalePrice.setEnabled(!selectedSalePrice.getEnabled());
            request.setParam(selectedSalePrice);
            salePriceData.saveSalePrice(selectedSalePrice);
            System.out.println("com.alodiga.primefaces.ultima.controller.LazySalePriceView.doChanceStatus()");
        } catch (NullParameterException ex) {
            Logger.getLogger(ListSalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListSalePriceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("FIN doChanceStatus");
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

}
