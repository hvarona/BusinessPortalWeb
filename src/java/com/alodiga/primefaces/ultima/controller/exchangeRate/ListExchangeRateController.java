package com.alodiga.primefaces.ultima.controller.exchangeRate;

import com.alodiga.primefaces.ultima.controller.city.ListCityController;
import com.alodiga.primefaces.ultima.controller.ratePaymentNetwork.ListRatePaymentNetworkController;
import com.portal.business.commons.data.ExchangeRateData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.ExchangeRate;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 * @author frank
 */
@ManagedBean(name="listExchangeRateController")
@ViewScoped
public class ListExchangeRateController implements Serializable {
    
    private ExchangeRate selectedExchangeRate;
    private List<ExchangeRate> exchangeRates;
    private List<ExchangeRate> filtereds;
    private ExchangeRateData userData = null;
    private ExchangeRateData exchangeRateData = null;
    private Map<String,String> countrys = null;

    
    @PostConstruct
    public void init() {
        
        userData = new ExchangeRateData();
        exchangeRateData = new ExchangeRateData();
         WsRequest request = new WsRequest();
       // reload();
        try {
            exchangeRates = exchangeRateData.loadExchangeRate(request);
        } catch (GeneralException ex) {
            Logger.getLogger(ListExchangeRateController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ex) {
            Logger.getLogger(ListExchangeRateController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListExchangeRateController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<ExchangeRate> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(List<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public List<ExchangeRate> getFiltereds() {
        return filtereds;
    }

    public void setFiltereds(List<ExchangeRate> filtereds) {
        this.filtereds = filtereds;
    }

    public ExchangeRate getSelectedExchangeRate() {
        return selectedExchangeRate;
    }

    public void setSelectedExchangeRate(ExchangeRate selectedExchangeRate) {
        this.selectedExchangeRate = selectedExchangeRate;
    }

    public Map<String, String> getCountrys() {
        
        
        
          WsRequest request = new WsRequest();
          UtilsData utilsData = new UtilsData();
         countrys = new TreeMap<String, String>();
        try {
            List<Country> paymentNetworks1 = utilsData.getCountries(request);
            for (Country paymentNetwork : paymentNetworks1) {
                countrys.put(paymentNetwork.getName(), paymentNetwork.getId().toString());
            }
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return countrys;
    }

    public void setCountrys(Map<String, String> countrys) {
        this.countrys = countrys;
    }


    public void save(){
       
        System.out.println("Entro al metodo!!!!!!!!");
        UtilsData utilsData = new UtilsData();
        ExchangeRate exchangeRate = null;
        ExchangeRate objExchangeRate = null;
       
        ExchangeRateData exchangeRateData = new ExchangeRateData();
        
        try {
        
        exchangeRate = exchangeRateData.lastExchangeRateByCountryId(selectedExchangeRate.getId());    
        
        if (exchangeRate != null) {
            
            utilsData = new UtilsData();
            exchangeRate.setEndingDate(new Timestamp(new Date().getTime()));
            utilsData.saveExchangeRate(exchangeRate);
            
            
            //Aparte inserta un nuevo registro
                objExchangeRate = new ExchangeRate();
                //objPaymentNetwork = new PaymentNetwork();

                //objPaymentNetwork.setId(6l);
                objExchangeRate.setAmount(selectedExchangeRate.getAmount());
                objExchangeRate.setBeginingDate(new Timestamp(new Date().getTime()));
                objExchangeRate.setEndingDate(null);
                objExchangeRate.setCountry(selectedExchangeRate.getCountry());
                utilsData.saveExchangeRate(objExchangeRate);
                reload();
        
        }
        
        
        } catch (NullParameterException | GeneralException | RegisterNotFoundException ex) {
            Logger.getLogger(ListRatePaymentNetworkController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean filterByPrice(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if(filterText == null||filterText.equals("")) {
            return true;
        }
         
        if(value == null) {
            return false;
        }
         
        return ((Comparable) value).compareTo(Integer.valueOf(filterText)) > 0;
    }
    
    public void doRediret() {
          
          try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("exchangeRateView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.LazyUserView.doRediret()");
        }
    }
      
    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
    
      public void reload(){
        try {
             WsRequest request = new WsRequest();
            List<ExchangeRate> ExchangeRate = exchangeRateData.loadExchangeRate(request);
        } catch (GeneralException | EmptyListException | NullParameterException ex) {
            Logger.getLogger(ListRatePaymentNetworkController.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
}
