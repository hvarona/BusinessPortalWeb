package com.alodiga.businessportal.controlller.request;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.portal.business.commons.cms.CmsCity;
import com.portal.business.commons.cms.CmsCountry;
import com.portal.business.commons.cms.CmsDocumentPersonType;
import com.portal.business.commons.cms.CmsState;
import com.portal.business.commons.cms.data.CmsData;
import com.portal.business.commons.data.CardPreRequestData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.CardPreRequest;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.context.RequestContext;

/**
 *
 * @author henry
 */
@ManagedBean(name = "requestController")
@ViewScoped
public class RequestController {

    private static final String LINK_URL = "http://tajetas.alodiga.com?idRequest=";
    private static final String FROM_EMAIL = "alodigaWallet@alodiga.com";

    private List<CmsDocumentPersonType> documentTypes = new ArrayList();
    private CmsDocumentPersonType documentType;

    private List<CmsCountry> countries;
    private CmsCountry country;

    private List<CmsCity> cities = new ArrayList();
    private CmsCity city;

    private List<CmsState> states = new ArrayList();
    private CmsState state;

    private CardPreRequest cardPreRequest;

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private ResourceBundle msg;

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean lenguajeBean;

    private CmsData cmsData;

    @PostConstruct
    public void init() {
        if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
        }
        cmsData = new CmsData();
        cardPreRequest = new CardPreRequest();
        try {
            countries = cmsData.getCountries();
        } catch (GeneralException ex) {
            ex.printStackTrace();
            countries = new ArrayList();
        }
    }

    public List<CmsDocumentPersonType> getDocumentTypes() {
        return documentTypes;
    }

    public void setDocumentTypes(List<CmsDocumentPersonType> documentTypes) {
        this.documentTypes = documentTypes;
    }

    public CmsDocumentPersonType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(CmsDocumentPersonType documentType) {
        this.documentType = documentType;
        this.cardPreRequest.setDocumentType(documentType);
    }

    public List<CmsCountry> getCountries() {
        return countries;
    }

    public void setCountries(List<CmsCountry> countries) {
        this.countries = countries;
    }

    public CmsCountry getCountry() {
        return country;
    }

    public void setCountry(CmsCountry country) {
        this.country = country;
        this.state = null;
        this.city = null;
        this.documentType = null;
        this.cities = new ArrayList();
        this.getCardPreRequest().setCountry(country);
        if (country != null) {
            try {
                this.states = cmsData.getStates(country);
                this.documentTypes = cmsData.getDocumentTypes(country);
            } catch (NullParameterException | GeneralException ex) {
                ex.printStackTrace();
                this.states = new ArrayList();
            }
        } else {
            this.states = new ArrayList();
            this.documentTypes = new ArrayList();
        }
    }

    public List<CmsCity> getCities() {
        return cities;
    }

    public void setCities(List<CmsCity> cities) {
        this.cities = cities;
    }

    public CmsCity getCity() {
        return city;
    }

    public void setCity(CmsCity city) {
        this.city = city;
    }

    public List<CmsState> getStates() {
        return states;
    }

    public void setStates(List<CmsState> states) {
        this.states = states;

    }

    public CmsState getState() {
        return state;
    }

    public void setState(CmsState state) {
        this.state = state;
        this.city = null;
        this.getCardPreRequest().setState(state);
        if (state != null) {
            try {
                this.cities = cmsData.getCities(state);
            } catch (NullParameterException | GeneralException ex) {
                ex.printStackTrace();
                this.cities = new ArrayList();
            }
        }
    }

    public CardPreRequest getCardPreRequest() {
        return cardPreRequest;
    }

    public void setCardPreRequest(CardPreRequest cardPreRequest) {
        this.cardPreRequest = cardPreRequest;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public void setLenguajeBean(LanguajeBean lenguajeBean) {
        this.lenguajeBean = lenguajeBean;
    }

    public void save() {

        try {

            cardPreRequest.setStatus(CardPreRequest.CardPreRequestStatus.PREREQUEST);

            CardPreRequest preRequest = new CardPreRequestData().saveorUpdateCardPreReques(cardPreRequest);
            if (cardPreRequest.getSendInfo().equals(CardPreRequest.CardPreRequestSendInfo.EMAIL) || cardPreRequest.getSendInfo().equals(CardPreRequest.CardPreRequestSendInfo.BOTH)) {
                sendMail(preRequest);
            }
            if (cardPreRequest.getSendInfo().equals(CardPreRequest.CardPreRequestSendInfo.SMS) || cardPreRequest.getSendInfo().equals(CardPreRequest.CardPreRequestSendInfo.BOTH)) {
                sendSms(preRequest);
            }
        } catch (NullParameterException | GeneralException ex) {
            ex.printStackTrace();
        }
    }

    private void sendMail(CardPreRequest cardPreRequest) {
        String text = "Alodiga le invita a disfrutar de los beneficios de la tarjeta prepagada, complete su solicitud aqui " + LINK_URL + cardPreRequest.getId() + "&isEmail";

        String subject = "prueba";

        APIAlodigaWalletProxy proxy = new APIAlodigaWalletProxy();
        try {
            proxy.sendMail(subject, text, cardPreRequest.getEmail(), FROM_EMAIL);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    private void sendSms(CardPreRequest cardPreRequest) {
        String text = "Alodiga le invita a disfrutar de los beneficios de la tarjeta prepagada, complete su solicitud aqui " + LINK_URL + cardPreRequest.getId() + "&isSms";
    }

    public void reset() {
        this.setCountry(null);
        this.cardPreRequest = new CardPreRequest();
        PrimeFaces.current().resetInputs("GenerateRequestForm:dataGrid");

    }

}
