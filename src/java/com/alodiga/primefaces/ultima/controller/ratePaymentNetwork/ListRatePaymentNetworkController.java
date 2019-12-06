package com.alodiga.primefaces.ultima.controller.ratePaymentNetwork;

import com.alodiga.primefaces.ultima.controller.city.ListCityController;
import com.portal.business.commons.data.PaymentNetworkData;
import com.portal.business.commons.data.RatePaymentNetworkData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.PaymentNetwork;
import com.portal.business.commons.models.RatePaymentNetwork;
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
@ManagedBean(name="listRatePaymentNetworkController")
@ViewScoped
public class ListRatePaymentNetworkController implements Serializable {
    
    private RatePaymentNetwork selectedRatePayment;
    private List<RatePaymentNetwork> RatePaymentNetworks;
    
    private List<RatePaymentNetwork> filtereds;
    private RatePaymentNetworkData userData = null;
    private PaymentNetworkData paymentNetworkData = null;
    private Map<String,String> paymentNetworks = null;
    
    @PostConstruct
    public void init() {
        
        userData = new RatePaymentNetworkData();
        paymentNetworkData = new PaymentNetworkData();
        reload();
    }

    public List<RatePaymentNetwork> getRatePaymentNetworks() {
        return RatePaymentNetworks;
    }

    public void setRatePaymentNetworks(List<RatePaymentNetwork> RatePaymentNetworks) {
        this.RatePaymentNetworks = RatePaymentNetworks;
    }

    public List<RatePaymentNetwork> getFiltereds() {
        return filtereds;
    }

    public void setFiltereds(List<RatePaymentNetwork> filtereds) {
        this.filtereds = filtereds;
    }

    public RatePaymentNetwork getSelectedRatePayment() {
        
//        System.out.println(selectedRatePayment.getPaymentNetwork().getName());
//        
        return selectedRatePayment;
    
    }

    public void setSelectedRatePayment(RatePaymentNetwork selectedRatePayment) {
        
        this.selectedRatePayment = selectedRatePayment;
        
        
    }

    public Map<String, String> getPaymentNetworks() {
        WsRequest request = new WsRequest();
        paymentNetworks = new TreeMap<String, String>();
        try {
            List<PaymentNetwork> paymentNetworks1 = paymentNetworkData.loadPaymentNetwork();
            for (PaymentNetwork paymentNetwork : paymentNetworks1) {
                paymentNetworks.put(paymentNetwork.getName(), paymentNetwork.getId().toString());
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCityController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return paymentNetworks;
    }

    public void setPaymentNetworks(Map<String, String> paymentNetworks) {
        this.paymentNetworks = paymentNetworks;
    }
    
    

    public void save(){
       
        System.out.println("Entro al metodo!!!!!!!!");
        UtilsData utilsData = new UtilsData();
        RatePaymentNetwork ratePaymentNetwork = null;
        RatePaymentNetwork objRatePaymentNetwork = null;
        PaymentNetwork objPaymentNetwork = null;
        RatePaymentNetworkData ratePaymentNetworkData = new RatePaymentNetworkData();
        
        try {
        
        ratePaymentNetwork = ratePaymentNetworkData.lastRatePaymentNetwork(selectedRatePayment.getId());    
        
        if (ratePaymentNetwork != null) {
            
            utilsData = new UtilsData();
            ratePaymentNetwork.setEndingDate(new Timestamp(new Date().getTime()));
            ratePaymentNetwork = utilsData.saveRatePaymentNetwork(ratePaymentNetwork);
            
            
            //Aparte inserta un nuevo registro
                objRatePaymentNetwork = new RatePaymentNetwork();
                //objPaymentNetwork = new PaymentNetwork();

                //objPaymentNetwork.setId(6l);
                objRatePaymentNetwork.setAmount(selectedRatePayment.getAmount());
                objRatePaymentNetwork.setBeginingDate(new Timestamp(new Date().getTime()));
                objRatePaymentNetwork.setEndingDate(null);
                objRatePaymentNetwork.setPaymentNetwork(selectedRatePayment.getPaymentNetwork());
                ratePaymentNetwork = utilsData.saveRatePaymentNetwork(objRatePaymentNetwork);
                reload();
        
        }
        
        //utilsData.saveRatePaymentNetwork(selectedRatePayment);
        //utilsData.saveStore(selectedRatePayment.getStore());
        //utilsData.savePaymentNetwork(selectedRatePayment.getPaymentNetwork());
        
        } catch (NullParameterException ex) {
            Logger.getLogger(ListRatePaymentNetworkController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListRatePaymentNetworkController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RegisterNotFoundException ex) {
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
            FacesContext.getCurrentInstance().getExternalContext().redirect("ratePaymentView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.LazyUserView.doRediret()");
        }
    }
      
      public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
      
      public void doChanceStatus() {
          
        try {
            
            selectedRatePayment.getPaymentNetwork().setEnabled(!selectedRatePayment.getPaymentNetwork().isEnabled());
            userData.saveRatePaymentNetwork(selectedRatePayment);
            
        }catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(ListRatePaymentNetworkController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
      
      public void reload(){
        try {
            RatePaymentNetworks = userData.loadPaymentNetworks();
        } catch (GeneralException | EmptyListException | NullParameterException ex) {
            Logger.getLogger(ListRatePaymentNetworkController.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
}
