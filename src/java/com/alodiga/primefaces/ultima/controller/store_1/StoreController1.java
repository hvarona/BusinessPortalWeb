package com.alodiga.primefaces.ultima.controller.store_1;

import com.portal.business.commons.data.StoreData;
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
import com.portal.business.commons.models.Enterprise;
import com.portal.business.commons.models.Language;
import com.portal.business.commons.models.Period;
import com.portal.business.commons.models.State;
import com.portal.business.commons.models.Store;
import com.portal.business.commons.utils.QueryConstants;
import com.portal.business.commons.utils.EjbUtils;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.context.RequestContext;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class StoreController1 {
    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Timestamp creationDate;
    private String email;
    private String phoneNumber;
    private boolean enabled;
    private String address;
    private Enterprise enterprise;
    private Language language;
    private Double balance = new Double(0);
    private Double crediLimit = new Double(0);
    private Timestamp lastBillingDate;
    private Timestamp nexBillingDate;
    private boolean isPrePaid;
    private Period period;
    private Country country;
    private State state;
    private City city;
    private County county;
    private UtilsData utilsData = null;
    private StoreData storeData = null;
    private String messages = null;
    private Map<String,String> countries = null;
    private Map<String,String> states = null;
    private Map<String,String> cities = null;
    private Map<String,String> counties = null;  
    private Map<String,String> languages = null;
    private Map<String,String> periods = null;
     private Map<String,String> enterprises = null;
    private String zip;
    
    
    @PostConstruct
    public void init() {
        utilsData = new UtilsData();
        storeData = new StoreData();
        WsRequest request = new WsRequest();
        request.setParam(47L);
        balance = 0d;
        crediLimit = 0d;
        try {
            country = utilsData.loadCountry(request);
        } catch (NullParameterException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getCrediLimit() {
        return crediLimit;
    }

    public void setCrediLimit(Double crediLimit) {
        this.crediLimit = crediLimit;
    }

    public Timestamp getLastBillingDate() {
        return lastBillingDate;
    }

    public void setLastBillingDate(Timestamp lastBillingDate) {
        this.lastBillingDate = lastBillingDate;
    }

    public Timestamp getNexBillingDate() {
        return nexBillingDate;
    }

    public void setNexBillingDate(Timestamp nexBillingDate) {
        this.nexBillingDate = nexBillingDate;
    }

    public boolean getIsPrePaid() {
        return isPrePaid;
    }

    public void setIsPrePaid(boolean isPrePaid) {
        this.isPrePaid = isPrePaid;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
    
     public Map<String, String> getStates() {
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_COUNTRY_ID, country != null ? country.getId() : null);
        request.setParams(params);
        states = new TreeMap<String, String>();
        try {
            List<State> states1 = utilsData.getStateByCountry(request);
            for (State state : states1) {
                states.put(state.getName(), state.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return countries;
    }

    public void setCountries(Map<String, String> countries) {
        this.countries = countries;
    }

    public Map<String, String> getCities() {
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_STATE_ID, state != null ? state.getId() : null);
        request.setParams(params);
        cities = new TreeMap<String, String>();
        try {
            List<City> cities1 = utilsData.getCitiesByState(request);
            for (City city : cities1) {
                cities.put(city.getName(), city.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return languages;
    }
    
    public void setLanguages(Map<String, String> languages) {
        this.languages = languages;
    }
   

//    public Map<String, String> getPeriods() {
//         WsRequest request = new WsRequest();
//
//        periods = new TreeMap<String, String>();
//        try {
//            List<Period> periods1 = utilsData.getPeriods(request);
//            for (Period period : periods1) {
//                periods.put(period.getName(), period.getId().toString());
//            }
//        } catch (EmptyListException ex) {
//            Logger.getLogger(StoreController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (GeneralException ex) {
//            Logger.getLogger(StoreController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NullParameterException ex) {
//            Logger.getLogger(StoreController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return periods;
//    }

    public void setPeriods(Map<String, String> periods) {
        this.periods = periods;
    }
    
    public Map<String, String> getCounties() {
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
        } catch (EmptyListException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return counties;
    }

    public void setCounties(Map<String, String> counties) {
        this.counties = counties;
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
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return enterprises;
    }

    public void setEnterprises(Map<String, String> enterprises) {
        this.enterprises = enterprises;
    }

    
    public void reloadStates(AjaxBehaviorEvent event) {
        State state = (State) ((UIOutput)event.getSource()).getValue();
        WsRequest request = new WsRequest();
        Map params = new HashMap();
        params.put(QueryConstants.PARAM_COUNTRY_ID, state.getId());
        request.setParams(params);
        states = new TreeMap<String, String>();
        try {
            List<State> states1 = utilsData.getStateByCountry(request);
            for (State state1 : states1) {
                states.put(state1.getName(),state1.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(StoreController1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save() {
        try {
            Store store = new Store();
            store.setLogin(login);
            store.setPassword(password);
            store.setFirstName(firstName);
            store.setLastName(lastName);
            store.setIsPrePaid(isPrePaid);
            System.err.println("balance"+balance);
            System.err.println("crediLimit"+crediLimit);
            String cBalance = balance.toString();
            String cCrediLimit = crediLimit.toString();
            Date now = new Date((new java.util.Date()).getTime());
            Timestamp creationDate = new Timestamp(now.getTime());
            store.setCreationDate(creationDate);
            if (isPrePaid){
                store.setBalance(Float.parseFloat(cBalance));
                store.setCrediLimit(0f);
                store.setPeriod(null);
                store.setLastBillingDate(creationDate);
                store.setNexBillingDate(creationDate);
            }else {
                store.setCrediLimit(Float.parseFloat(cCrediLimit));
                store.setBalance(Float.parseFloat(cCrediLimit) * (-1));
                store.setPeriod(period);    
                store.setLastBillingDate(creationDate);
                Date date = EjbUtils.addDays(creationDate, period.getDays());
                nexBillingDate = new Timestamp(date.getTime());
                store.setNexBillingDate(nexBillingDate);
            }
            store.setEnterprise(enterprise);
            store.setLanguage(language);
            store.setEmail(email);
            store.setPhoneNumber(phoneNumber);
            store.setEnabled(true);
            Address address1 = new Address();
            address1.setCountry(country);
            address1.setState(state);
            address1.setCity(city);
            address1.setCounty(county);
            address1.setAddress(address);
            address1.setZipCode(zip);
            store.setAddress(address1);
            storeData.saveStore(store);
           
            messages = "La Tienda " + firstName + " "+ lastName+" ha sido guardado con exito";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(messages));
        } catch (NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Faltan parametros"));
        } catch (GeneralException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        }
    }
    
        
    public void reset() {
        RequestContext.getCurrentInstance().reset("StoreCreateForm:grid");
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listStore.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.StoreController.doRediret()");
        }
    }
    
    public void doEdit() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("editStore.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.StoreController.doRediret()");
        }
    }
}
