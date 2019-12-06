package com.alodiga.primefaces.ultima.controller.paymentNetworkPoint;

import com.portal.business.commons.data.PaymentNetworkPointData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.PaymentNetworkPoint;
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

@ManagedBean(name="listPaymentPointController")
@ViewScoped
public class ListPaymentPointController implements Serializable {
    
    private PaymentNetworkPoint selectedPaymentPoint;
    private List<PaymentNetworkPoint> paymentNetworkPoints;
    private List<PaymentNetworkPoint> filtereds;
    private PaymentNetworkPointData paymentNetworkPointData = null;
    private PaymentNetworkPoint icon;
    
    @PostConstruct
    public void init(){
           
            paymentNetworkPointData = new PaymentNetworkPointData();
            
        try {
            
            paymentNetworkPoints = paymentNetworkPointData.loadPaymentNetworkPoint();
        
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            Logger.getLogger(ListPaymentPointController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
    }

    public PaymentNetworkPoint getSelectedPaymentPoint() {
        return selectedPaymentPoint;
    }

    public void setSelectedPaymentPoint(PaymentNetworkPoint selectedPaymentPoint) {
        this.selectedPaymentPoint = selectedPaymentPoint;
    }

    public List<PaymentNetworkPoint> getPaymentNetworkPoints() {
        return paymentNetworkPoints;
    }

    public void setPaymentNetworkPoints(List<PaymentNetworkPoint> paymentNetworkPoints) {
        this.paymentNetworkPoints = paymentNetworkPoints;
    }

    public List<PaymentNetworkPoint> getFiltereds() {
        return filtereds;
    }

    public void setFiltereds(List<PaymentNetworkPoint> filtereds) {
        this.filtereds = filtereds;
    }

    public PaymentNetworkPointData getPaymentNetworkPointData() {
        return paymentNetworkPointData;
    }

    public void setPaymentNetworkPointData(PaymentNetworkPointData paymentNetworkPointData) {
        this.paymentNetworkPointData = paymentNetworkPointData;
    }

    public void save() throws NullParameterException, GeneralException {
        
       UtilsData utilsData = new UtilsData();
       utilsData.savePaymentNetworkPoint(selectedPaymentPoint);
            
       System.out.println("Entro al metodo!!");
    }
    
    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("paymentPointView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.LazyUserView.doRediret()");
        }
    }
    
    public void doChanceStatus() {
        
        try {
            WsRequest request = new WsRequest();
            selectedPaymentPoint.setEnabled(!selectedPaymentPoint.isEnabled());
            request.setParam(selectedPaymentPoint);
            paymentNetworkPointData.savePaymentNetworkPoint(request);
            System.out.println("com.alodiga.primefaces.ultima.controller.LazyUserView.doChanceStatus()");
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(ListPaymentPointController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public PaymentNetworkPoint getIcon() {
        return icon;
    }

    public void setIcon(PaymentNetworkPoint icon) {
        this.icon = icon;
    }

   
    public String icon(PaymentNetworkPoint icon) {
        if (icon.isEnabled())
            return "ui-icon-check";
        else
            return "ui-icon-block";
    }
    
    
    
}

