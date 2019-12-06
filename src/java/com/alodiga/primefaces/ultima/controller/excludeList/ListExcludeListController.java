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
//import com.remettence.commons.data.SOALData;
//import com.remettence.commons.data.UtilsData;
import com.portal.business.commons.models.ExcludeList;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

@ManagedBean(name="dtLazyExcludeListView")
@ViewScoped
public class ListExcludeListController implements Serializable {
    
    
    // Reemplaza Lazy y para filtros
    private List<ExcludeList> excludeLists;
    private List<ExcludeList> filtered;
    
    private LazyDataModel<ExcludeList> lazyModel;
   // private Country country= new Country();
    private Long enterpriseId;
    private Long countryId;
    private Long correspondentId;
    private Long paymentMethodId;
    
    private ExcludeList selectedExcludeList;
    private ExcludeListData excludeListData = null;
//    private DualListModel<Profile> profiles;
//    private AccessControlData  accessControlData;

    public List<ExcludeList> getExcludeLists() {
        return excludeLists;
    }

    public void setExcludeLists(List<ExcludeList> excludeLists) {
        this.excludeLists = excludeLists;
    }



    public List<ExcludeList> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<ExcludeList> filtered) {
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

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
    
    
    
    
    
    
    @PostConstruct
    public void init() {
        excludeListData = new ExcludeListData();
        WsRequest request = new WsRequest();

        try {
            excludeLists = excludeListData.loadExcludeListList(request);
        } catch (GeneralException ex) {
            Logger.getLogger(ListExcludeListController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListExcludeListController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ex) {
            Logger.getLogger(ListExcludeListController.class.getName()).log(Level.SEVERE, null, ex);
        }

        
//        try {
//            excludeListData = new ExcludeListData();
//            request = new WsRequest();
//            //lazyModel = new LazyExcludeListDataModel(excludeListData.loadExcludeListList(request));
//        
//            //$$$ 
//            //List<ExcludeList> rrr = lazyModel.
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
//            Logger.getLogger(ListExcludeListController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NullParameterException ex) {
//            Logger.getLogger(ListExcludeListController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public LazyDataModel<ExcludeList> getLazyModel() {
        return lazyModel;
    }

    public ExcludeList getSelectedExcludeList() {
        return selectedExcludeList;
    }

    public void setSelectedExcludeList(ExcludeList selectedExcludeList) {
        this.selectedExcludeList = selectedExcludeList;
    }


//=========================================================
//    METODO SAVE
//=========================================================
    public void save() {
        
        WsRequest request = new WsRequest();
//        UtilsData utilsData = new UtilsData();
//        
//        Enterprise enterpriseTmp        = new Enterprise();
//        Country countryTmp              = new Country();
//        Correspondent correspondentTmp  = new Correspondent();
//        PaymentMethod paymentMethodTmp  = new PaymentMethod();
request = new WsRequest();
request.setParam(selectedExcludeList);
selectedExcludeList = excludeListData.saveExcludeList(selectedExcludeList);
    }
    
    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("excludeListView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.LazyExcludeListView.doRediret()");
        }
    }
    
    public void doChanceStatus() {
        WsRequest request = new WsRequest();
        request.setParam(selectedExcludeList);
        excludeListData.saveExcludeList(selectedExcludeList);
        System.out.println("com.alodiga.primefaces.ultima.controller.LazyExcludeListView.doChanceStatus()");
        
        System.out.println("FIN doChanceStatus");
    }



}
