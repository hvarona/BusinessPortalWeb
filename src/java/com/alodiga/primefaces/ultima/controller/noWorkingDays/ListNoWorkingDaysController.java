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
package com.alodiga.primefaces.ultima.controller.noWorkingDays;

import com.portal.business.commons.data.AccessControlData;
import com.portal.business.commons.data.NoWorkingDaysData;
import com.portal.business.commons.data.UserData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Profile;
import com.portal.business.commons.models.NoWorkingDays;
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

@ManagedBean(name="dtLazyNoWorkingDaysView")
@ViewScoped
public class ListNoWorkingDaysController implements Serializable {
    
    // Reemplaza Lazy y para filtros
    private List<NoWorkingDays> noWorkingDays;
    private List<NoWorkingDays> filtered;
    
    // Negocio    
    private NoWorkingDays selectedNoWorkingDays;
    private NoWorkingDaysData noWorkingDaysData = null;

    public List<NoWorkingDays> getNoWorkingDays() {
        return noWorkingDays;
    }

    public void setNoWorkingDays(List<NoWorkingDays> noWorkingDays) {
        this.noWorkingDays = noWorkingDays;
    }

    public List<NoWorkingDays> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<NoWorkingDays> filtered) {
        this.filtered = filtered;
    }
    
    
    
    
    
    @PostConstruct
    public void init() {
        
        noWorkingDaysData = new NoWorkingDaysData();
        WsRequest request = new WsRequest();
        
        try {
            noWorkingDays = noWorkingDaysData.loadNoWorkingDays(request);
        } catch (GeneralException ex) {
            Logger.getLogger(ListNoWorkingDaysController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(ListNoWorkingDaysController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListNoWorkingDaysController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ex) {
            Logger.getLogger(ListNoWorkingDaysController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public NoWorkingDays getSelectedNoWorkingDays() {
        return selectedNoWorkingDays;
    }

    public void setSelectedNoWorkingDays(NoWorkingDays selectedNoWorkingDays) {
        this.selectedNoWorkingDays = selectedNoWorkingDays;
    }


        public List<NoWorkingDays> save2() throws GeneralException, RegisterNotFoundException, NullParameterException, EmptyListException {
            System.out.println("BICHAAAAA!!");
            NoWorkingDaysData noWorkingDaysData = new NoWorkingDaysData();
        
            WsRequest request=new WsRequest();
        
            List<NoWorkingDays> lista;
            lista = noWorkingDaysData.loadNoWorkingDaysMio(request); //bicharNoWorkingDays(request);
        
            for(NoWorkingDays noWorkingDaysTmp : lista) {
                System.out.println("Este es el registro: " + noWorkingDaysTmp.toString());
            }
        
            noWorkingDays = lista;
       
            System.out.println("Hola");
            return lista;
        }
    
    
    public void save() {
        try {
            WsRequest request = new WsRequest();
            request.setParam(selectedNoWorkingDays);
            noWorkingDaysData.saveNoWorkingDay(request);
        } catch (GeneralException ex) {
            Logger.getLogger(ListNoWorkingDaysController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListNoWorkingDaysController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("noWorkingDaysView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.LazyNoWorkingDaysView.doRediret()");
        }
    }
    
    public void doChanceStatus() {
        try {
            WsRequest request = new WsRequest();
            selectedNoWorkingDays.setEnabled(!selectedNoWorkingDays.getEnabled());
            request.setParam(selectedNoWorkingDays);
            noWorkingDaysData.saveNoWorkingDay(request);
            System.out.println("com.alodiga.primefaces.ultima.controller.LazyNoWorkingDaysView.doChanceStatus()");
            
            System.out.println("FIN doChanceStatus");
        } catch (GeneralException ex) {
            Logger.getLogger(ListNoWorkingDaysController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListNoWorkingDaysController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
