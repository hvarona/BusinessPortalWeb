package com.alodiga.primefaces.ultima.controller.receiver;

import com.portal.business.commons.data.PersonData;
import com.portal.business.commons.data.ReceiverData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.City;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.County;
import com.portal.business.commons.models.Receiver;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.event.SelectEvent;

/**
 * @author frank
 */
@ManagedBean(name="listReceiverController")
@ViewScoped
public class ListReceiverController implements Serializable {
    
    private Country country;
    private State state;
    private City city;
    private County county;
  
    private Map<String,String> statesMap = null;
    private Map<String,String> countriesMap = null;
    private Map<String,String> citiesMap = null;
    private Map<String,String> countiesMap = null;
        
    private UtilsData utilsData = null;
    private ReceiverData receiverData = null;
    
    private String cityValue;
    private Receiver selectedReceiver;
    private List<Receiver> receivers;
    private List<Receiver> filtereds;
    private String icon;
    
    public ListReceiverController() {
    }
    
    @PostConstruct
    public void init(){
            
         receiverData = new ReceiverData();
         utilsData = new UtilsData();
         WsRequest request = new WsRequest();
         
         try {
                 
                   //DataTable
                   receivers = receiverData.getReceivers(request);
            
        } catch (GeneralException | EmptyListException | NullParameterException ex) {
            Logger.getLogger(ListReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
       public void reloadCities(AjaxBehaviorEvent event) {
        
        State state = (State) ((UIOutput) event.getSource()).getValue();
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_STATE_ID, state.getId());
        request.setParams(params);
        citiesMap = new TreeMap<String, String>();
        
        try {
            List<City> citiesTemp = utilsData.getCitiesByState(request);
            for (City cityList : citiesTemp) {
                citiesMap.put(cityList.getName(), cityList.getId().toString());
            }
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ListReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
      public void reloadStates(AjaxBehaviorEvent event) {
        
        Country country = (Country) ((UIOutput)event.getSource()).getValue();
       
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_COUNTRY_ID, country.getId());
        request.setParams(params);
        statesMap = new TreeMap<String, String>();
        
        try {
            List<State> statesList = utilsData.loadStateList(request);
            for (State stateTemp : statesList){
                statesMap.put(stateTemp.getName(),stateTemp.getId().toString());
            }
        
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ListReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    public void reloadCountes(AjaxBehaviorEvent event) {
        
        City city = (City) ((UIOutput)event.getSource()).getValue();
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_CITY_ID, city.getId());
        request.setParams(params);
        countiesMap = new TreeMap<String, String>();
        
        try {
            List<County> countiesTemp = utilsData.getCountiesByState(request);
            for (County countyList : countiesTemp) {
                countiesMap.put(countyList.getName(),countyList.getId().toString());
            }
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ListReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("receiverView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.LazyUserView.doRediret()");
        }
    }
     
    public void doChanceStatus(){
          
        try {
            
            WsRequest request = new WsRequest();
            selectedReceiver.getPerson().setEnabled(!selectedReceiver.getPerson().getEnabled());
            request.setParam(selectedReceiver);
            receiverData.saveReceiver(request);
            
        }catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(ListReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleReturnDialog(SelectEvent event){
        if (event != null && event.getObject() != null) {
        }
    }
     
    public void save() throws NullParameterException, GeneralException{
        
              PersonData  personData = new PersonData();
             //UtilsData utilsData = new UtilsData(); 
             //System.out.println("A modificar: "+selectedReceiver.getPerson().getId());
              personData.saveReceiver(selectedReceiver);
              personData.savePerson(selectedReceiver.getPerson());
              personData.saveAddress(selectedReceiver.getPerson().getAddress());
              utilsData.saveCountry(selectedReceiver.getPerson().getAddress().getCountry());
              utilsData.saveState(selectedReceiver.getPerson().getAddress().getState());
              utilsData.saveCity(selectedReceiver.getPerson().getAddress().getCity());
              utilsData.saveCounty(selectedReceiver.getPerson().getAddress().getCounty());
              
    }


    public Receiver getSelectedReceiver(){
        return selectedReceiver;
    }

    public void setSelectedReceiver(Receiver selectedReceiver){
        this.selectedReceiver = selectedReceiver;
    }

    public List<Receiver> getReceivers(){
        return receivers;
    }

    public void setReceivers(List<Receiver> receivers){
        this.receivers = receivers;
    }

    public String getCityValue(){
        return cityValue;
    }

    public void setCityValue(String cityValue){
        this.cityValue = cityValue;
    }

    public List<Receiver> getFiltereds(){
        return filtereds;
    }

    public void setFiltereds(List<Receiver> filtereds){
        this.filtereds = filtereds;
    }

    public ReceiverData getReceiverData(){
        return receiverData;
    }

    public void setReceiverData(ReceiverData receiverData){
        this.receiverData = receiverData;
    }

    public String getIcon(){
        return icon;
    }

    public void setIcon(String icon){
        this.icon = icon;
    }
    
    public String icon(Receiver receiver){
        if (receiver.getPerson().getEnabled())
            return "ui-icon-check";
        else
            return "ui-icon-block";
    }

    public Map<String, String> getStatesMap() {
        
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        if (selectedReceiver != null) {
            params.put(QueryConstants.PARAM_COUNTRY_ID, selectedReceiver != null ? selectedReceiver.getPerson().getAddress().getCountry().getId() : null);
        } else {
            params.put(QueryConstants.PARAM_COUNTRY_ID, country != null ? country.getId() : null);
        }
        request.setParams(params);
        statesMap = new TreeMap<String, String>();
        try {
            
            List<State> statesTemp = utilsData.getStateByCountry(request);
            
            for (State stateList : statesTemp) {
                statesMap.put(stateList.getName(), stateList.getId().toString());
            }
        
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ListReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return statesMap;
    }

    public void setStatesMap(Map<String, String> statesMap) {
        this.statesMap = statesMap;
    }

    public Map<String, String> getCountriesMap() {
        
        WsRequest request = new WsRequest();
        countriesMap = new TreeMap<String, String>();
        try{
            List<Country> countriesTemp = utilsData.getCountries(request);
            for (Country countryList : countriesTemp) {
                countriesMap.put(countryList.getName(),countryList.getId().toString());
            }
        } catch (EmptyListException | NullParameterException | GeneralException ex) {
            Logger.getLogger(ListReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return countriesMap;
    }

    public void setCountriesMap(Map<String, String> countriesMap) {
        this.countriesMap = countriesMap;
    }

    public Map<String, String> getCitiesMap() {
        
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        if (selectedReceiver != null) {
             params.put(QueryConstants.PARAM_STATE_ID, selectedReceiver != null ? selectedReceiver.getPerson().getAddress().getState().getId() : null);
        } else {
            params.put(QueryConstants.PARAM_STATE_ID, state != null ? state.getId() : null);
        }
        request.setParams(params);
        citiesMap = new TreeMap<String, String>();
        
        try {
            List<City> cities1 = utilsData.getCitiesByState(request);
            for (City city1 : cities1) {
                citiesMap.put(city1.getName(), city1.getId().toString());
            }
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ListReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return citiesMap;
    }

    public void setCitiesMap(Map<String, String> citiesMap) {
        this.citiesMap = citiesMap;
    }

    public Map<String, String> getCountiesMap() {
        
           WsRequest request = new WsRequest();
           Map params = new HashMap();
        if (selectedReceiver != null) {
             params.put(QueryConstants.PARAM_CITY_ID, selectedReceiver != null ? selectedReceiver.getPerson().getAddress().getState().getId() : null);
        } else {
            params.put(QueryConstants.PARAM_CITY_ID, state != null ? state.getId() : null);
        }
        request.setParams(params);
        countiesMap = new TreeMap<String, String>();
        try {
            List<County> counties1 = utilsData.getCountiesByState(request);
            for (County county : counties1) {
                countiesMap.put(county.getName(),county.getId().toString());
            }
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ListReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return countiesMap;
    }

    public void setCountiesMap(Map<String, String> countysMap) {
        this.countiesMap = countysMap;
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
   
 }
    
