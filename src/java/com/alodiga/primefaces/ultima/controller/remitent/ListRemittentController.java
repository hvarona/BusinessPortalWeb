package com.alodiga.primefaces.ultima.controller.remitent;

import com.alodiga.primefaces.ultima.controller.receiver.ListReceiverController;
import com.portal.business.commons.data.RemittentData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.City;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.County;
import com.portal.business.commons.models.Remittent;
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

/**
 * @author frank
 */
@ManagedBean(name="listRemittentController")
@ViewScoped
public class ListRemittentController implements Serializable  {
    
    private Country country;
    private State state;
    private City city;
    private County county;
    
    private Map<String,String> countriesMap = null;
    private Map<String,String> statesMap = null;
    private Map<String,String> citiesMap = null;
    private Map<String,String> countysMap = null;
    
    private UtilsData utilsData = null;
     
    private Long id;
    private Remittent selectedRemittent;
    private List<Remittent> remittents;
    private List<Remittent> filtereds;
    private RemittentData remittentData=null;
    
    public ListRemittentController(){}
    
    @PostConstruct
    public void init(){
         utilsData = new UtilsData();
        remittentData = new RemittentData();
        WsRequest request = new WsRequest();
        
        try {
            
            remittents = remittentData.getRemitent(request);
        
        } catch (GeneralException | NullParameterException | EmptyListException ex) {
            Logger.getLogger(ListRemittentController.class.getName()).log(Level.SEVERE, null, ex);
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

    public Map<String, String> getStatesMap() {
        return statesMap;
    }

    public Map<String, String> getCitiesMap() {
        return citiesMap;
    }

    public void setCitiesMap(Map<String, String> citiesMap) {
        this.citiesMap = citiesMap;
    }

    public Map<String, String> getCountysMap() {
        return countysMap;
    }

    public void setCountysMap(Map<String, String> countysMap) {
        this.countysMap = countysMap;
    }
    
    public void setStatesMap(Map<String, String> statesMap) {
        this.statesMap = statesMap;
    }

    public Map<String, String> getCountriesMap() {
        
         WsRequest request = new WsRequest();
        countriesMap = new TreeMap<String, String>();
        
        try{
            List<Country> countriesTemp = utilsData.getCountries(request);
            System.out.println("Impresion" +countriesTemp);
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

     public void reloadStates(AjaxBehaviorEvent event) {
          
        State state = (State) ((UIOutput)event.getSource()).getValue();
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_COUNTRY_ID, state.getId());
        request.setParams(params);
        statesMap = new TreeMap<String, String>();
        
        try {
            List<State> statesList = utilsData.loadStateList(request);;
            for (State stateTemp : statesList){
                statesMap.put(stateTemp.getName(),stateTemp.getId().toString());
            }
        
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
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
    
      
    public void reloadCountes(AjaxBehaviorEvent event) {
        State state = (State) ((UIOutput)event.getSource()).getValue();
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_STATE_ID, state.getId());
        request.setParams(params);
        countysMap = new TreeMap<String, String>();
        
        try {
            List<County> countiesTemp = utilsData.getCountiesByState(request);
            for (County countyList : countiesTemp) {
                countysMap.put(countyList.getName(),countyList.getId().toString());
            }
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ListReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Remittent> getRemittents(){
        return remittents;
    }

    public void setRemittents(List<Remittent> remittents){
        this.remittents = remittents;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }
    

    public RemittentData getRemittentData(){
        return remittentData;
    }

    public void setRemittentData(RemittentData remittentData){
        this.remittentData = remittentData;
    }
    
    public Remittent getSelectedRemittent(){
        return selectedRemittent;
    }

    public void setSelectedRemittent(Remittent selectedRemittent){
        this.selectedRemittent = selectedRemittent;
    }

    public List<Remittent> getFiltereds(){
        return filtereds;
    }

    public void setFiltereds(List<Remittent> filtereds){
        this.filtereds = filtereds;
    }

    public RemittentData getRemittanceData(){
        return remittentData;
    }

    public void setRemittanceData(RemittentData remittentData){
        this.remittentData = remittentData;
    }
    
    public void handleReturnDialog(SelectEvent event){
        if (event != null && event.getObject() != null){
        }
    }
    
    public void save(){
    
        try {
            System.out.println("Hola entro al metodo!!!!!!");
            remittentData.saveRemittent(selectedRemittent);
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(ListRemittentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
     public void doChanceStatus() {
         
           try {
            
            WsRequest request = new WsRequest();
            selectedRemittent.getPerson().setEnabled(!selectedRemittent.getPerson().getEnabled());
            request.setParam(selectedRemittent);
            remittentData.saveRemittent(selectedRemittent);
            
        }catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(ListRemittentController.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
     
     

    @Override
   public int hashCode() {
       int hash = 7;
       hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
       return hash;
   }

   @Override
   public boolean equals(Object obj) {
       if (obj == null) {
           return false;
       }
       if (getClass() != obj.getClass()) {
           return false;
       }
       final ListRemittentController other = (ListRemittentController) obj;
       if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
           return false;
       }
       return true;
   }
     
}
