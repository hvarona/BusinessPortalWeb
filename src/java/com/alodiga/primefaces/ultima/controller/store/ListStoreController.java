package com.alodiga.primefaces.ultima.controller.store;

import com.alodiga.primefaces.ultima.controller.city.ListCityController;
import com.alodiga.primefaces.ultima.controller.user.ListUserController;
import com.portal.business.commons.data.StoreData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.City;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.County;
import com.portal.business.commons.models.Enterprise;
import com.portal.business.commons.models.Language;
import com.portal.business.commons.models.Period;
import com.portal.business.commons.models.State;
import com.portal.business.commons.models.Store;
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


@ManagedBean(name="dtListStoreController")
@ViewScoped
public class ListStoreController implements Serializable {
    
    
    private Store selectedStore;
    private StoreData storeData = null;
    private UtilsData utilsData = null;
    List<Store> stores;
    List<Store> filteredStore;
    private boolean isPrePaid;
    private Country country;
    private State state;
    private City city;
    private County county;
    
    private Map<String,String> countries = null;
    private Map<String,String> states = null;
    private Map<String,String> cities = null;
    private Map<String,String> counties = null;
    private Map<String,String> languages = null;
    private Map<String,String> periods = null;
    private Map<String,String> enterprises = null;
    
    @PostConstruct
    public void init() {
        try {
            storeData = new StoreData();
            utilsData = new UtilsData();
            WsRequest request = new WsRequest();
            stores = storeData.getStore(request);
            request.setParam(47L);
            try {
                country = utilsData.loadCountry(request);
            } catch (RegisterNotFoundException ex) {
                Logger.getLogger(ListStoreController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NullParameterException ex) {
            Logger.getLogger(ListStoreController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ex) {
            Logger.getLogger(ListStoreController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListStoreController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Store getSelectedStore() {
        return selectedStore;
    }

    public void setSelectedStore(Store selectedStore) {
        this.selectedStore = selectedStore;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public List<Store> getFilteredStore() {
        return filteredStore;
    }

    public void setFilteredStore(List<Store> filteredStore) {
        this.filteredStore = filteredStore;
    }

    public Map<String, String> getStates() {
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        if (selectedStore != null) {
            params.put(QueryConstants.PARAM_COUNTRY_ID, selectedStore != null ? selectedStore.getAddress().getCountry().getId() : null);
        } else {
            params.put(QueryConstants.PARAM_COUNTRY_ID, country != null ? country.getId() : null);
        }
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
            Logger.getLogger(ListStoreController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListStoreController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListStoreController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return countries;
    }

    public void setCountries(Map<String, String> countries) {
        this.countries = countries;
    }

    public Map<String, String> getCities() {
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        if (selectedStore != null) {
             params.put(QueryConstants.PARAM_STATE_ID, selectedStore != null ? selectedStore.getAddress().getState().getId() : null);
        } else {
            params.put(QueryConstants.PARAM_STATE_ID, state != null ? state.getId() : null);
        }
        request.setParams(params);
        cities = new TreeMap<String, String>();
        try {
            List<City> cities1 = utilsData.getCitiesByState(request);
            for (City city : cities1) {
                cities.put(city.getName(), city.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cities;
    }

    public void setCities(Map<String, String> cities) {
        this.cities = cities;
    }
    
    public Map<String, String> getLanguages() {
        WsRequest request = new WsRequest();

        languages = new TreeMap<String, String>();
        try {
            List<Language> languages1 = utilsData.getLanguages(request);
            for (Language language : languages1) {
                languages.put(language.getDescription(), language.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return languages;
    }
    
    public void setLanguages(Map<String, String> languages) {
        this.languages = languages;
    }
   

    public Map<String, String> getPeriods() {
         WsRequest request = new WsRequest();

        periods = new TreeMap<String, String>();
        try {
            List<Period> periods1 = utilsData.getPeriods(request);
            for (Period period : periods1) {
                periods.put(period.getName(), period.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return periods;
    }

    public void setPeriods(Map<String, String> periods) {
        this.periods = periods;
    }

    public Map<String, String> getEnterprises() {
        WsRequest request = new WsRequest();
        enterprises = new TreeMap<String, String>();
        try {
            List<Enterprise> enterprise1 = utilsData.getEnterprises();
            for (Enterprise enterprise : enterprise1) {
                enterprises.put(enterprise.getName(), enterprise.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return enterprises;
    }

    public void setEnterprises(Map<String, String> enterprises) {
        this.enterprises = enterprises;
    }
    
    public Map<String, String> getCounties() {
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        if (selectedStore != null) {
             params.put(QueryConstants.PARAM_STATE_ID, selectedStore != null ? selectedStore.getAddress().getState().getId() : null);
        } else {
            params.put(QueryConstants.PARAM_STATE_ID, state != null ? state.getId() : null);
        }
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
            for (State state1 : states1) {
                states.put(state1.getName(),state1.getId().toString());
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
        State state = (State) ((UIOutput) event.getSource()).getValue();
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_STATE_ID, state.getId());
        request.setParams(params);
        cities = new TreeMap<String, String>();
        try {
            List<City> cities1 = utilsData.getCitiesByState(request);
            for (City city1 : cities1) {
                cities.put(city1.getName(), city1.getId().toString());
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
   
    public void doChanceStatus() {
        try {
            WsRequest request = new WsRequest();
            selectedStore.setEnabled(!selectedStore.isEnabled());
            request.setParam(selectedStore);
            storeData.saveStore(request);
        } catch (NullParameterException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.ListStoreController.doChanceStatus()");
            Logger.getLogger(ListUserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.ListStoreController.doChanceStatus()");
            Logger.getLogger(ListUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public County getCounty() {
        return county;
    }

    public void setCounty(County county) {
        this.county = county;
    }
    

    public void save() {
        try {
            Store store = null;
            if (selectedStore.getId() != null) {
                store = selectedStore;
            } 
            storeData.saveStore(store);
        } catch (NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Faltan parametros"));
        } catch (GeneralException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
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
            FacesContext.getCurrentInstance().getExternalContext().redirect("storeView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.ListStoreController.doRediret()");
        }
    }

    public boolean isIsPrePaid() {
        return isPrePaid;
    }

    public void setIsPrePaid(boolean isPrePaid) {
        this.isPrePaid = isPrePaid;
    }
    
  
    
}
