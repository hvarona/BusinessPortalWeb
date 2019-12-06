package com.alodiga.primefaces.ultima.controller.receiver;

import com.portal.business.commons.data.PersonData;
import com.portal.business.commons.data.ReceiverData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Address;
import com.portal.business.commons.models.City;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.County;
import com.portal.business.commons.models.Person;
import com.portal.business.commons.models.Receiver;
import com.portal.business.commons.models.State;
import com.portal.business.commons.utils.QueryConstants;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.context.RequestContext;

/**
 * @author frank
 */
@ManagedBean
//@SessionScoped 
@ViewScoped
public class ReceiverController {
        
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String secondSurname;
    private String email;
    private String phoneNumber;
    private String addressReceiver;
    private Country countryReceiver;
    private String countyReceiver;
    private String cityReceiver;
    private String stateReceiver;
    private String zipCode;
    private boolean enabled;
    private String nameCity;
    private String nameState;
    private String nameCountry;
    private String shortName;
    private String codeCountry;
    private String alternative1;
    private String alternative2;
    private String alternative13;
    
    private Country country;
    private State state;
    private City city;
    private County county;
    private UtilsData utilsData = null;
    private ReceiverData receiverData = null;
    
    private Map<String,String> countries = null;
    private Map<String,String> states = null;
    private Map<String,String> cities = null;
    private Map<String,String> counties = null;  
    private Map<String,String> periods = null;
    private Map<String,String> enterprises = null;
       
    @PostConstruct
    public void init(){
        
          utilsData = new UtilsData();
          receiverData = new ReceiverData();
          WsRequest request = new WsRequest();
          request.setParam(47L);
        try {
            country = utilsData.loadCountry(request);
            countryReceiver = utilsData.loadCountry(request);
        } catch (RegisterNotFoundException | NullParameterException | GeneralException ex) {
            Logger.getLogger(ReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    public void save(){
//        
//           PersonData  personData = new PersonData();
//           Receiver objReceiver = new Receiver();
//           Person objPerson = new Person();
//           Address objAddress = new Address(); 
//           City objCity = new City(); 
//           State objState = new State();
//           Country objCountry = new Country(); 
//           County objCounty = new County(); 
//           
//           try{
//           
//                objCountry.setName(nameCountry);
//                objCountry.setShortName(shortName);
//                objCountry.setCode(codeCountry);
//                objCountry.setAlternativeName1(alternative1);
//                objCountry.setAlternativeName2(alternative2);
//                objCountry.setAlternativeName3(alternative13);
//                utilsData.saveCountry(objCountry);
//
//                objState.setName(nameState);
//                objState.setCountry(objCountry);
//                utilsData.saveState(objState);
//
//                objCity.setName(nameCity);
//                objCity.setState(objState);
//                utilsData.saveCity(objCity);
//
//                objCounty.setName(countyReceiver);
//                objCounty.setState(objState);
//                utilsData.saveCounty(objCounty);
//                               
//                objAddress.setAddress(addressReceiver);
//                objAddress.setCity(objCity);
//                objAddress.setCityName(nameCity);
//                objAddress.setCountry(objCountry);
//                objAddress.setCounty(objCounty);
//                objAddress.setCountyName(nameCountry);
//                objAddress.setState(objState);
//                objAddress.setStateName(nameState);
//                objAddress.setZipCode(zipCode);
//                personData.saveAddress(objAddress);
//
//                objPerson.setFirstName(firstName);
//                objPerson.setLastName(lastName);
//                objPerson.setMiddleName(middleName);
//                objPerson.setSecondSurname(secondSurname);
//                objPerson.setPhoneNumber(phoneNumber);
//                objPerson.setEmail(email);
//                objPerson.setEnabled(enabled);
//                objPerson.setCreationDate(new Timestamp(new Date().getTime()));
//                objPerson.setAddress(objAddress);
//                personData.savePerson(objPerson);
//
//                objReceiver.setPerson(objPerson);
//                personData.saveReceiver(objReceiver);
//            
//            }catch(NullParameterException | GeneralException e){
//                
//            e.getMessage();
//           
//           }finally{
//           
//           }
//     
//    }
    
    public void save(){
        
           PersonData  personData = new PersonData();
           Receiver objReceiver = new Receiver();
           Person objPerson = new Person();
           Address objAddress = new Address(); 
           City objCity = new City(); 
           State objState = new State();
           Country objCountry = new Country(); 
           County objCounty = new County(); 
           
           try{
           
                objCountry.setId(country.getId());
                utilsData.saveCountry(objCountry);

                objState.setId(state.getId());
                objState.setCountry(objCountry);
                utilsData.saveState(objState);

                objCity.setId(city.getId());
                objCity.setState(objState);
                utilsData.saveCity(objCity);

                objCounty.setId(country.getId());
                objCounty.setState(objState);
                utilsData.saveCounty(objCounty);
                               
                objAddress.setAddress(addressReceiver);
                objAddress.setCity(objCity);
                objAddress.setCityName(nameCity);
                objAddress.setCountry(objCountry);
                objAddress.setCounty(objCounty);
                objAddress.setCountyName(nameCountry);
                objAddress.setState(objState);
                objAddress.setStateName(nameState);
                objAddress.setZipCode(zipCode);
                personData.saveAddress(objAddress);

                objPerson.setFirstName(firstName);
                objPerson.setLastName(lastName);
                objPerson.setMiddleName(middleName);
                objPerson.setSecondSurname(secondSurname);
                objPerson.setPhoneNumber(phoneNumber);
                objPerson.setEmail(email);
                objPerson.setEnabled(enabled);
                objPerson.setCreationDate(new Timestamp(new Date().getTime()));
                objPerson.setAddress(objAddress);
                personData.savePerson(objPerson);

                objReceiver.setPerson(objPerson);
                personData.saveReceiver(objReceiver);
            
            }catch(NullParameterException | GeneralException e){
                
            e.getMessage();
           
           }finally{           
           }
     
    }
    
    public void reset() {
        RequestContext.getCurrentInstance().reset("receiverCreateForm:grid");
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listReceiver.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.listReceiverView.doRediret()");
        }
    }
    
    
    public Map<String, String> getCountries(){
        
        WsRequest request = new WsRequest();
        countries = new TreeMap<String, String>();
        try {
            List<Country> countries1 = utilsData.getCountries(request);
            for (Country country1 : countries1) {
                countries.put(country1.getName(),country1.getId().toString());
            }
        }catch (EmptyListException | NullParameterException | GeneralException ex) {
            Logger.getLogger(ReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return countries;
    }

    public void setCountries(Map<String, String> countries){
        this.countries = countries;
    }

    public Map<String, String> getStates(){
        
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_COUNTRY_ID, country != null ? country.getId() : null);
        request.setParams(params);
        states = new TreeMap<String, String>();
        try {
            List<State> states1 = utilsData.getStateByCountry(request);
            for (State state1 : states1) {
                states.put(state1.getName(), state1.getId().toString());
            }
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return states;
    }

    public void setStates(Map<String, String> states){
        this.states = states;
    }

    public Map<String, String> getCities(){
        
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        
        params.put(QueryConstants.PARAM_STATE_ID, state != null ? state.getId() : null);
        request.setParams(params);
        cities = new TreeMap<String, String>();
        
        try {
            List<City> cities1 = utilsData.getCitiesByState(request);
            for (City city1 : cities1) {
                cities.put(city1.getName(), city1.getId().toString());
            }
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ListReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return cities;
    }

    public void setCities(Map<String, String> cities) {
        this.cities = cities;
    }

    public Map<String, String> getCounties(){
        
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_STATE_ID, state != null ? state.getId() : null);
        request.setParams(params);
        counties = new TreeMap<String, String>();
        try {
            List<County> counties1 = utilsData.getCountiesByState(request);
            for (County county : counties1) {
                counties.put(county.getName(),county.getId().toString());
            }
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ListReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return counties;
    }

    public void setCounties(Map<String, String> counties){
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
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void reloadCities(AjaxBehaviorEvent event){
        
        State state = (State) ((UIOutput) event.getSource()).getValue();
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_STATE_ID, state.getId());
        request.setParams(params);
        cities = new TreeMap<String, String>();
        
        try {
            List<City> citiesTemp = utilsData.getCitiesByState(request);
            for (City cityList : citiesTemp) {
                cities.put(cityList.getName(), cityList.getId().toString());
            }
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ListReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void reloadCountes(AjaxBehaviorEvent event){
        
        City city = (City) ((UIOutput)event.getSource()).getValue();
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_CITY_ID, city.getId());
        request.setParams(params);
        counties = new TreeMap<String, String>();
        
        try {
            List<County> countiesTemp = utilsData.getCountiesByState(request);
            for (County countyList : countiesTemp) {
                counties.put(countyList.getName(),countyList.getId().toString());
            }
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ListReceiverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public Map<String, String> getPeriods(){
        return periods;
    }

    public void setPeriods(Map<String, String> periods){
        this.periods = periods;
    }

    public Map<String, String> getEnterprises(){
        return enterprises;
    }

    public void setEnterprises(Map<String, String> enterprises){
        this.enterprises = enterprises;
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
public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public UtilsData getUtilsData() {
        return utilsData;
    }

    public void setUtilsData(UtilsData utilsData) {
        this.utilsData = utilsData;
    }

    
    public ReceiverData getReceiverData() {
        return receiverData;
    }

    public void setReceiverData(ReceiverData receiverData) {
        this.receiverData = receiverData;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    public String getNameState() {
        return nameState;
    }

    public void setNameState(String nameState) {
        this.nameState = nameState;
    }

    public String getNameCountry() {
        return nameCountry;
    }

    public void setNameCountry(String nameCountry) {
        this.nameCountry = nameCountry;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCodeCountry() {
        return codeCountry;
    }

    public void setCodeCountry(String codeCountry) {
        this.codeCountry = codeCountry;
    }

    public String getAlternative1() {
        return alternative1;
    }

    public void setAlternative1(String alternative1) {
        this.alternative1 = alternative1;
    }

    public String getAlternative2() {
        return alternative2;
    }

    public void setAlternative2(String alternative2) {
        this.alternative2 = alternative2;
    }

    public String getAlternative13() {
        return alternative13;
    }

    public void setAlternative13(String alternative13) {
        this.alternative13 = alternative13;
    }

    public String getAddressReceiver() {
        return addressReceiver;
    }

    public void setAddressReceiver(String addressReceiver) {
        this.addressReceiver = addressReceiver;
    }

    public Country getCountryReceiver() {
        return countryReceiver;
    }

    public void setCountryReceiver(Country countryReceiver) {
        this.countryReceiver = countryReceiver;
    }

    public String getCountyReceiver(){
        return countyReceiver;
    }

    public void setCountyReceiver(String countyReceiver){
        this.countyReceiver = countyReceiver;
    }

    public String getCityReceiver(){
        return cityReceiver;
    }

    public void setCityReceiver(String cityReceiver){
        this.cityReceiver = cityReceiver;
    }

    public String getStateReceiver(){
        return stateReceiver;
    }

    public void setStateReceiver(String stateReceiver){
        this.stateReceiver = stateReceiver;
    }

    public String getZipCode(){
        return zipCode;
    }

    public void setZipCode(String zipCode){
        this.zipCode = zipCode;
    }
   
}
