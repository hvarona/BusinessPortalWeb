package com.alodiga.primefaces.ultima.controller.paymentNetwork;

import com.alodiga.primefaces.ultima.controller.paymentNetworkPoint.ListPaymentPointController;
import com.portal.business.commons.data.PaymentNetworkData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.PaymentNetwork;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

@ManagedBean(name="listPaymentController")
@ViewScoped
public class ListPaymentController implements Serializable {
    
    private PaymentNetwork selectedPayment;
    private List<PaymentNetwork> paymentNetworks;
    private List<PaymentNetwork> filtereds;
    private PaymentNetworkData paymentNetworksData = null;
    private PaymentNetwork icon;
    
    @PostConstruct
    public void init(){
           
            paymentNetworksData = new PaymentNetworkData();
            
        try {
            
            paymentNetworks = paymentNetworksData.loadPaymentNetwork();
        
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ListPaymentPointController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    public List<PaymentNetwork> getPaymentNetworks() {
        return paymentNetworks;
    }

    public void setPaymentNetworks(List<PaymentNetwork> paymentNetworks) {
        this.paymentNetworks = paymentNetworks;
    }

    public List<PaymentNetwork> getFiltereds() {
        return filtereds;
    }

    public void setFiltereds(List<PaymentNetwork> filtereds) {
        this.filtereds = filtereds;
    }

    public PaymentNetworkData getPaymentNetworksData() {
        return paymentNetworksData;
    }

    public void setPaymentNetworksData(PaymentNetworkData paymentNetworksData) {
        this.paymentNetworksData = paymentNetworksData;
    }

    public PaymentNetwork getSelectedPayment() {
        return selectedPayment;
    }

    public void setSelectedPayment(PaymentNetwork selectedPayment) {
        this.selectedPayment = selectedPayment;
    }
   
    public void save() throws NullParameterException, GeneralException {
        
       UtilsData utilsData = new UtilsData();
       utilsData.savePaymentNetwork(selectedPayment);
            
       System.out.println("Entro al metodo!!");
    }
    
    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("paymentView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.LazyUserView.doRediret()");
        }
    }
    
    public void doChanceStatus() {
        
        try {
            WsRequest request = new WsRequest();
            selectedPayment.setEnabled(!selectedPayment.isEnabled());
            request.setParam(selectedPayment);
            paymentNetworksData.savePaymentNetwork(request);
            
            System.out.println("com.alodiga.primefaces.ultima.controller.LazyUserView.doChanceStatus()");
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(ListPaymentPointController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public PaymentNetwork getIcon() {
        return icon;
    }

    public void setIcon(PaymentNetwork icon) {
        this.icon = icon;
    }


    
    
    public String icon(PaymentNetwork icon) {
        
        
        if (icon.isEnabled())
            return "ui-icon-check";
        else
            return "ui-icon-block";
    }
}



