package com.alodiga.primefaces.ultima.controller.utils;

import com.alodiga.primefaces.ultima.controller.city.ListCityController;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.City;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.County;
import com.portal.business.commons.models.State;
import com.portal.business.commons.utils.QueryConstants;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIOutput;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;


@ManagedBean(name="dtUtilsController")
@ViewScoped
public class UtilsController implements Serializable {
    
    
    private UtilsData utilsData = null;
    
    private Map<String,String> countries = null;
    private Map<String,String> states = null;
    private Map<String,String> cities = null;
    private Map<String,String> counties = null;
    
    @PostConstruct
    public void init() {
         utilsData = new UtilsData();
    }


    public Map<String, String> getStates() {
        return states;
    }

    public void setStates(Map<String, String> states) {
        this.states = states;
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
            Logger.getLogger(UtilsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(UtilsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(UtilsController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return countries;
    }

    public void setCountries(Map<String, String> countries) {
        this.countries = countries;
    }

    public Map<String, String> getCities() {
        return cities;
    }

    public void setCities(Map<String, String> cities) {
        this.cities = cities;
    }

    public Map<String, String> getCounties() {
        return counties;
    }

    public void setCounties(Map<String, String> counties) {
        this.counties = counties;
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
    
    public void reloadCities(AjaxBehaviorEvent event) {
        State state = (State) ((UIOutput)event.getSource()).getValue();
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_STATE_ID, state.getId());
        request.setParams(params);
        cities = new TreeMap<String, String>();
        try {
            List<City> cities1 = utilsData.getCitiesByState(request);
            for (City city : cities1) {
                cities.put(city.getName(),city.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reloadCountes(AjaxBehaviorEvent event) {
        State state = (State) ((UIOutput)event.getSource()).getValue();
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_STATE_ID, state.getId());
        request.setParams(params);
        counties = new TreeMap<String, String>();
        try {
            List<County> counties1 = utilsData.getCountiesByState(request);
            for (County county : counties1) {
                counties.put(county.getName(),county.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
    
   
  

    
}
