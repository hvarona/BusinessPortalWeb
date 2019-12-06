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
package com.alodiga.primefaces.ultima.controller.city;

import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.City;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.State;
import com.portal.business.commons.utils.QueryConstants;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;


@ManagedBean(name="dtListCityController")
@ViewScoped
public class ListCityController implements Serializable {
    
    
    private City selectedCity;
    private UtilsData utilsData = null;
    List<City> cities;
    List<City> filteredCities;
    private Country country;
    private State state;
    
    private Map<String,String> countries = null;
    private Map<String,String> states =  null;
    
    @PostConstruct
    public void init() {
        try {
            utilsData = new UtilsData();
            WsRequest request = new WsRequest();
            cities = utilsData.getCity(request);
            states =  new TreeMap<String, String>();
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public City getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(City selectedCity) {
        this.selectedCity = selectedCity;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<City> getFilteredCities() {
        return filteredCities;
    }

    public void setFilteredCities(List<City> filteredCities) {
        this.filteredCities = filteredCities;
    }

    public Map<String, String> getStates() {
            WsRequest request = new WsRequest();
            Map params = new HashMap();
            if (selectedCity!=null)
              params.put(QueryConstants.PARAM_COUNTRY_ID, selectedCity!=null?selectedCity.getState().getCountry().getId():null);
            else     
              params.put(QueryConstants.PARAM_COUNTRY_ID, country!=null?country.getId():null);
            request.setParams(params);
            states = new TreeMap<String, String>();
            try {
                List<State> states1 = utilsData.getStateByCountry(request);
                for (State state : states1) {
                    states.put(state.getName(), state.getId().toString());
                }
            } catch (EmptyListException ex) {
                Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (GeneralException ex) {
                Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NullParameterException ex) {
                Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
            }
        return states;
    }
    
    public void reloadStates(AjaxBehaviorEvent event) { 
       Country country = (Country) ((UIOutput)event.getSource()).getValue();
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_COUNTRY_ID, country.getId());
        request.setParams(params);
        states = new TreeMap<String, String>();
        try {
            List<State> states1 = utilsData.getStateByCountry(request);
            for (State state : states1) {
                states.put(state.getName(),state.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    public void setStates(Map<String, String> states) {
        this.states = states;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    
    public Map<String, String> getCountries() {
        WsRequest request = new WsRequest();
        countries = new TreeMap<String, String>();
        try {
            List<Country> countries1 = utilsData.getCountries(request);
            for (Country country : countries1) {
                countries.put(country.getName(),country.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return countries;
    }

    public void setCountries(Map<String, String> countries) {
        this.countries = countries;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
   
    public void save() {
        try {
            City city = null;
            if (selectedCity.getId() != null) {
                city = selectedCity;
            } 
            city = utilsData.saveCity(city);
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
            FacesContext.getCurrentInstance().getExternalContext().redirect("cityView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.ListCityController.doRediret()");
        }
    }
    
  

    
}
