package com.alodiga.businessportal.controlller.profile;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.model.AffiliationRequest;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import com.portal.business.commons.models.Business;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author henry
 */
@ManagedBean(name = "businessProfileController")
@ViewScoped
public class businessProfileController {

    private Business business;

    private AffiliationRequest affiliationRequest;

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private ResourceBundle msg;

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean lenguajeBean;
    private BusinessPortalEJB proxy;

    @PostConstruct
    public void init() {
        business = loginBean.getCurrentBusiness();
        if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
        }

        proxy = (BusinessPortalEJB) EJBServiceLocator.getInstance().get(com.alodiga.wallet.common.utils.EjbConstants.BUSINESS_PORTAL_EJB);

        if (business.getIdPerson() != null) {
            try {
                affiliationRequest = proxy.loadAffiliationRequestById(Long.parseLong(business.getIdPerson()));

            } catch (RegisterNotFoundException | com.alodiga.wallet.common.exception.NullParameterException | com.alodiga.wallet.common.exception.GeneralException ex) {
                Logger.getLogger(businessProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public AffiliationRequest getAffiliationRequest() {
        return affiliationRequest;
    }

    public void setAffiliationRequest(AffiliationRequest affiliationRequest) {
        this.affiliationRequest = affiliationRequest;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public void setLenguajeBean(LanguajeBean lenguajeBean) {
        this.lenguajeBean = lenguajeBean;
    }

    public boolean hasAffiliationRequest() {
        return affiliationRequest != null;
    }

    public boolean isNaturalPerson() {
        return affiliationRequest.getBusinessPersonId().getPersonTypeId().getIndNaturalPerson();
    }

    public boolean isLegalPerson() {
        return !affiliationRequest.getBusinessPersonId().getPersonTypeId().getIndNaturalPerson();
    }

    public boolean isAdminUser() {
        return loginBean.getUserSession() instanceof Business;
    }

}
