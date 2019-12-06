package com.alodiga.primefaces.ultima.controller.paymentNetworkPoint;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.PaymentNetworkPoint;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.io.IOException;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

@ManagedBean
public class PaymentPointController {
    
    private Long id;
    private String name;
    private boolean enabled;
    
    @PostConstruct
    public void init(){
       
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void save() throws NullParameterException, GeneralException {
        
       UtilsData utilsData = new UtilsData();
       PaymentNetworkPoint objPaymentNetworkPoint = new PaymentNetworkPoint();
       
       objPaymentNetworkPoint.setEnabled(enabled);
       objPaymentNetworkPoint.setName(name);
       utilsData.savePaymentNetworkPoint( objPaymentNetworkPoint);
            
       System.out.println("Entro al metodo!!");
    }
    
    public void reset() {
        RequestContext.getCurrentInstance().reset("paymentCreateForm:grid");
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listPaymentNetworkPoint.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.listReceiverView.doRediret()");
        }
    }
}
