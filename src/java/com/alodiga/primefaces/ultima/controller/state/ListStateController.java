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
package com.alodiga.primefaces.ultima.controller.state;

import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.State;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;


@ManagedBean(name="dtListStateController")
@ViewScoped
public class ListStateController implements Serializable {
    
    
    private State selectedState;
    private UtilsData utilsData = null;
    List<State> states;
    List<State> filteredStates;
    
     private Map<String,String> countries = new TreeMap<String, String>();
    
    @PostConstruct
    public void init() {
        try {
            utilsData = new UtilsData();
            WsRequest request = new WsRequest();
            states = utilsData.getState(request);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListStateController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ex) {
            Logger.getLogger(ListStateController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListStateController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public State getSelectedState() {
        return selectedState;
    }

    public void setSelectedState(State selectedState) {
        this.selectedState = selectedState;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public List<State> getFilteredStates() {
        return filteredStates;
    }

    public void setFilteredStates(List<State> filteredStates) {
        this.filteredStates = filteredStates;
    }

    public Map<String, String> getCountries() {
        WsRequest request = new WsRequest();
        try {
            List<Country> countries1 = utilsData.getCountries(request);
            for (Country country : countries1) {
                countries.put(country.getName(),country.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(ListStateController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListStateController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListStateController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return countries;
    }

    public void setCountries(Map<String, String> countries) {
        this.countries = countries;
    }
   

    public void save() {
        try {
            State state = null;
            if (selectedState.getId() != null) {
                state = selectedState;
            } 
            state = utilsData.saveState(state);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        }
    }
    
    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("stateView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.ListStateController.doRediret()");
        }
    }
    
  

    
}
