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
package com.alodiga.primefaces.ultima.controller.remittanceHasRemittenceStatus;

import com.portal.business.commons.data.RemittanceHasRemittenceStatusData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.RemittanceHasRemittenceStatus;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
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

@ManagedBean(name="dtLazyRemittanceHasRemittenceStatusView")
@ViewScoped
public class ListRemittanceHasRemittenceStatusController implements Serializable {
    
    // Reemplaza Lazy y para filtros
    private List<RemittanceHasRemittenceStatus> remittanceHasRemittenceStatuses;
    private List<RemittanceHasRemittenceStatus> filtered;
    
    private LazyDataModel<RemittanceHasRemittenceStatus> lazyModel;
    
    private Long remittanceId;
    private Long remittanceStatusId;
    private Timestamp beginningDate;
    private Timestamp endingDate;
    private Long userId;
    
    private RemittanceHasRemittenceStatus selectedRemittanceHasRemittenceStatus;
    private RemittanceHasRemittenceStatusData remittanceHasRemittenceStatusData = null;

    public List<RemittanceHasRemittenceStatus> getRemittanceHasRemittenceStatuses() {
        return remittanceHasRemittenceStatuses;
    }

    public void setRemittanceHasRemittenceStatuses(List<RemittanceHasRemittenceStatus> remittanceHasRemittenceStatuses) {
        this.remittanceHasRemittenceStatuses = remittanceHasRemittenceStatuses;
    }

    public List<RemittanceHasRemittenceStatus> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<RemittanceHasRemittenceStatus> filtered) {
        this.filtered = filtered;
    }

    
    
    
    @PostConstruct
    public void init() {
        remittanceHasRemittenceStatusData = new RemittanceHasRemittenceStatusData();
        
        WsRequest request = new WsRequest();
        
        remittanceHasRemittenceStatuses = remittanceHasRemittenceStatusData.loadRemittanceHasRemittenceStatusList(request);
        
//        lazyModel = new LazyRemittanceHasRemittenceStatusDataModel(remittanceHasRemittenceStatusData.loadRemittanceHasRemittenceStatusList(request));
    
    }

    public LazyDataModel<RemittanceHasRemittenceStatus> getLazyModel() {
        return lazyModel;
    }

    public RemittanceHasRemittenceStatus getSelectedRemittanceHasRemittenceStatus() {
        return selectedRemittanceHasRemittenceStatus;
    }

    public void setSelectedRemittanceHasRemittenceStatus(RemittanceHasRemittenceStatus selectedRemittanceHasRemittenceStatus) {
        this.selectedRemittanceHasRemittenceStatus = selectedRemittanceHasRemittenceStatus;
    }

    public void save() {
        try {
            
            
            WsRequest request = new WsRequest();
            request.setParam(selectedRemittanceHasRemittenceStatus);
            remittanceHasRemittenceStatusData.saveRemittanceHasRemittenceStatus(request);
        } catch (NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Faltan parametros"));
        } catch (GeneralException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        }
    }
    
    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("remittanceHasRemittenceStatusView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.LazyRemittanceHasRemittenceStatusView.doRediret()");
        }
    }
    
    public void doChanceStatus() {
        try {
            WsRequest request = new WsRequest();
            //selectedRemittanceHasRemittenceStatus.setEnabled(!selectedRemittanceHasRemittenceStatus.getEnabled());
            request.setParam(selectedRemittanceHasRemittenceStatus);
            remittanceHasRemittenceStatusData.saveRemittanceHasRemittenceStatus(request);
            System.out.println("com.alodiga.primefaces.ultima.controller.LazyRemittanceHasRemittenceStatusView.doChanceStatus()");
        } catch (NullParameterException ex) {
            Logger.getLogger(ListRemittanceHasRemittenceStatusController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListRemittanceHasRemittenceStatusController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("FIN doChanceStatus");
    }

    public Long getRemittanceId() {
        return remittanceId;
    }

    public void setRemittanceId(Long remittanceId) {
        this.remittanceId = remittanceId;
    }

    public Long getRemittanceStatusId() {
        return remittanceStatusId;
    }

    public void setRemittanceStatusId(Long remittanceStatusId) {
        this.remittanceStatusId = remittanceStatusId;
    }

    public Timestamp getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(Timestamp beginningDate) {
        this.beginningDate = beginningDate;
    }

    public Timestamp getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Timestamp endingDate) {
        this.endingDate = endingDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    
    

}
