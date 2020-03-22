package com.alodiga.primefaces.ultima.controller.remittance;

import com.alodiga.ws.remittance.services.WSRemittenceMobileProxy;
import com.alodiga.ws.remittance.services.WsCountryListResponse;
import com.alodiga.ws.remittance.services.WsDeliveryFormsReponse;
import com.alodiga.ws.remittance.services.WsLoginResponse;
import com.alodiga.ws.remittance.services.WsPaymentNetworkService;
import com.alodiga.ws.remittance.services.WsRemittenceResponse;
import com.alodiga.ws.remittance.services.WsSummaryPaymentNetworkAndRateResponse;
import com.portal.business.commons.models.City;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.DeliveryForm;
import com.portal.business.commons.models.PaymentNetwork;
import com.portal.business.commons.models.RemittancePaymentInfo;
import com.portal.business.commons.models.RemittancePerson;
import com.portal.business.commons.models.State;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "remittanceController")
@ViewScoped
public class RemittanceController {

    private Date applicationDate;
    private RemittancePaymentInfo paymentInfo;
    private RemittancePerson remittent;
    private RemittancePerson receiver;

    private Country selectedRemittentCountry;
    private Country selectedReceiverCountry;
    private List<Country> countries;

    private State selectedRemittentState;
    private State selectedReceiverState;
    private List<State> states;

    private City selectedRemittentCity;
    private City selectedReceiverCity;
    private List<City> cities;

    private DeliveryForm selectedDeliveryForm;
    private List<DeliveryForm> deliveryForms;

    private PaymentNetwork selectedPaymentNetwork;
    private List<PaymentNetwork> paymentNetworks;

    private int phase;

    private boolean hasError = false;

    private String errorMessage;

    private WSRemittenceMobileProxy remittenceProxy;
    private String token;

    private int retries = 0;

    private final String correspondentId = "1";

    //Remettence
    public static final String COMMENTARY_REMETTENCE = "REMESA";
    public static final Boolean SENDING_OPTION_SMS_REMETTENCE = true;
    public static final String BANK_REMETTENCE = "1";
    public static final String PAYMENT_SERVICE_REMETTENCE = "1";
    public static final String SALES_TYPE_REMETTENCE = "1";
    public static final String LANGUAGE_REMETTENCE = "1";
    public static final String STORE_REMETTENCE = "1";
    public static final String PAYMENT_METHOD_REMITTANCE = "3";
    public static final String SERVICE_TYPE_REMITTANCE = "1";
    public static final String USER_REMITTANCE = "1";
    public static final String CASH_BOX_REMITTANCE = "CAJERO";
    public static final Float ADDITIONAL_CHANGES_REMITTANCE = 1F;
    public static final String SALES_PRICE_REMITTANCE = "1";
    public static final String POINT_REMITTANCE = "1";
    public static final Long PRODUCT_REMITTANCE = 2L;

    @PostConstruct
    public void init() {
        try {
            phase = 0;
            countries = new ArrayList();
            states = new ArrayList();
            cities = new ArrayList();
            deliveryForms = new ArrayList();
            paymentNetworks = new ArrayList();
            remittent = new RemittancePerson();
            receiver = new RemittancePerson();
            paymentInfo = new RemittancePaymentInfo();
            remittenceProxy = new WSRemittenceMobileProxy();
            loginWs();
            //List of Countries TODO cambiar a ws
            WsCountryListResponse response = remittenceProxy.getCountries();
            switch (response.getCode()) {
                case "0": {

                    for (com.alodiga.ws.remittance.services.Country wsCountry : response.getCountry()) {
                        Country country = new Country();
                        country.setId(wsCountry.getId());
                        country.setIso(wsCountry.getIso());
                        country.setName(wsCountry.getName());
                        country.setShortName(wsCountry.getShortName());
                        countries.add(country);
                    }
                }
                break;
                default:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", response.getMessage()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", ex.getMessage()));
        }

    }

    private void loginWs() {
        try {
            WsLoginResponse loginResponse = remittenceProxy.loginWS("alodiga", "d6f80e647631bb4522392aff53370502");
            if (loginResponse.getCode().equals("0")) {
                token = loginResponse.getToken();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public RemittancePaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(RemittancePaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public RemittancePerson getRemittent() {
        return remittent;
    }

    public void setRemittent(RemittancePerson remittent) {
        this.remittent = remittent;
    }

    public RemittancePerson getReceiver() {
        return receiver;
    }

    public void setReceiver(RemittancePerson receiver) {
        this.receiver = receiver;
    }

    public Country getSelectedRemittentCountry() {
        return selectedRemittentCountry;
    }

    public void setSelectedRemittentCountry(Country selectedRemittentCountry) {
        for (Country country : countries) {
            if (country.equals(selectedRemittentCountry)) {
                this.selectedRemittentCountry = country;
                break;
            }
        }
        this.remittent.getAddress().setCountry(this.selectedRemittentCountry);
        this.remittent.getAddress().setCountryName(this.selectedRemittentCountry.getName());

    }

    public Country getSelectedReceiverCountry() {
        return selectedReceiverCountry;

    }

    public void setSelectedReceiverCountry(Country selectedReceiverCountry) {
        for (Country country : countries) {
            if (country.equals(selectedReceiverCountry)) {
                this.selectedReceiverCountry = country;
                break;
            }
        }
        this.receiver.getAddress().setCountry(this.selectedReceiverCountry);
        this.receiver.getAddress().setCountryName(this.selectedReceiverCountry.getName());
    }

    public List<Country> getCountries() {
        return countries;
    }

    public State getSelectedRemittentState() {
        return selectedRemittentState;
    }

    public void setSelectedRemittentState(State selectedRemittentState) {
        this.selectedRemittentState = selectedRemittentState;
    }

    public State getSelectedReceiverState() {
        return selectedReceiverState;
    }

    public void setSelectedReceiverState(State selectedReceiverState) {
        this.selectedReceiverState = selectedReceiverState;
    }

    public List<State> getStates() {
        return states;
    }

    public City getSelectedRemittentCity() {
        return selectedRemittentCity;
    }

    public void setSelectedRemittentCity(City selectedRemittentCity) {
        this.selectedRemittentCity = selectedRemittentCity;
    }

    public City getSelectedReceiverCity() {
        return selectedReceiverCity;
    }

    public void setSelectedReceiverCity(City selectedReceiverCity) {
        this.selectedReceiverCity = selectedReceiverCity;
    }

    public List<City> getCities() {
        return cities;
    }

    public List<DeliveryForm> getDeliveryForms() {
        return deliveryForms;
    }

    public DeliveryForm getSelectedDeliveryForm() {
        return selectedDeliveryForm;
    }

    public void setSelectedDeliveryForm(DeliveryForm selectedDeliveryForm) {
        this.selectedDeliveryForm = selectedDeliveryForm;
        this.paymentInfo.setDeliveryForm(selectedDeliveryForm);
    }

    public PaymentNetwork getSelectedPaymentNetwork() {
        return selectedPaymentNetwork;
    }

    public void setSelectedPaymentNetwork(PaymentNetwork selectedPaymentNetwork) {
        System.out.println("Set Selected Payment");
        for (PaymentNetwork paymentNetwork : paymentNetworks) {
            if (paymentNetwork.equals(selectedPaymentNetwork)) {
                this.selectedPaymentNetwork = paymentNetwork;
            }
        }
        this.paymentInfo.setPaymentNetwork(this.selectedPaymentNetwork);
    }

    public List<PaymentNetwork> getPaymentNetworks() {
        return paymentNetworks;
    }

    public boolean isHasError() {
        return hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getPhase() {
        return phase;
    }

    public void handleReceiverCountryChange() {
        try {
            deliveryForms = new ArrayList();
            paymentNetworks = new ArrayList();
            WsPaymentNetworkService response = remittenceProxy.getPaymentNetworkByCountryId(token, Long.toString(this.selectedReceiverCountry.getId()));
            switch (response.getCode()) {
                case "0": {
                    for (com.alodiga.ws.remittance.services.PaymentNetwork wsPayment : response.getPaymentNetworks()) {
                        PaymentNetwork payment = new PaymentNetwork();
                        payment.setId(wsPayment.getId());
                        payment.setName(wsPayment.getName());
                        payment.setCountry(this.selectedReceiverCountry);
                        paymentNetworks.add(payment);
                    }
                    retries = 0;
                }
                break;
                case "50": {
                    if (retries < 3) {
                        loginWs();
                        ++retries;
                        this.handleReceiverCountryChange();

                    }
                }
                break;
                default:
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", response.getMessage()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage()));
        }
    }

    public void handlePaymentNetworkChange() {
        System.out.println("Handler Payment " + this.selectedPaymentNetwork);
        try {
            deliveryForms = new ArrayList();
            if (this.selectedPaymentNetwork != null) {
                WsDeliveryFormsReponse response = remittenceProxy.getDeliveryFormByPamentNetwork(token, Long.toString(this.selectedPaymentNetwork.getId()));
                System.out.println("Delivery Forms response " + response.getCode());
                switch (response.getCode()) {
                    case "0": {
                        deliveryForms = new ArrayList();
                        for (com.alodiga.ws.remittance.services.DeliveryForm wsDelivery : response.getDeliveryForms()) {
                            DeliveryForm delivery = new DeliveryForm();
                            delivery.setId(wsDelivery.getId());
                            delivery.setName(wsDelivery.getName());
                            deliveryForms.add(delivery);
                        }
                        System.out.println("Delivery Forms " + deliveryForms.size());
                    }
                    break;
                    case "50": {
                        if (retries < 3) {
                            loginWs();
                            ++retries;
                            this.handlePaymentNetworkChange();
                        }
                    }
                    break;
                    default:
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", response.getMessage()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", e.getMessage()));
        }
    }

    public void submitPaymentInfo() {
        phase = 1;
    }

    public void submitRemittentInfo() {
        phase = 2;
    }

    public void submitReceiverInfo() {
        try {
            WsSummaryPaymentNetworkAndRateResponse response = remittenceProxy.getRemettenceSummary(token, 
                    Long.toString(remittent.getAddress().getCountry().getId()), 
                    Long.toString(receiver.getAddress().getCountry().getId()), 
                    Long.parseLong(paymentInfo.getRatePaymentNetworkId()), 
                    paymentInfo.getAmountOrigin(), paymentInfo.isIncludeFee());
            switch (response.getCode()) {
                case "0": {
                    paymentInfo.setTotalAmount(response.getRealAmountToSend());
                    paymentInfo.setAmountDestiny(Float.parseFloat(response.getReceiverAmount()));
                    paymentInfo.setAmountOrigin(response.getAmountToSendRemettence());
                    paymentInfo.setExchangeRateId(Long.toString(response.getExchangeRateDestiny().getId()));
                    remittent.setCurrencyId(Long.toString(response.getExchangeRateSource().getCurrency().getId()));
                    receiver.setCurrencyId(Long.toString(response.getExchangeRateDestiny().getCurrency().getId()));

                    phase = 3;
                }
                break;
                case "50": {
                    if (retries < 3) {
                        loginWs();
                        ++retries;
                        this.submitReceiverInfo();
                    }
                }
                break;
                default:
                    hasError = true;
                    errorMessage = response.getMessage();
            }

        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            errorMessage = e.getMessage();
        }

    }

    public void confirmRemittance() {
        try {

            WsRemittenceResponse response = remittenceProxy.saverRemittence(
                    applicationDate.toString(),
                    COMMENTARY_REMETTENCE,
                    paymentInfo.getAmountOrigin(),
                    paymentInfo.getTotalAmount(),
                    SENDING_OPTION_SMS_REMETTENCE,
                    paymentInfo.getAmountDestiny(),
                    BANK_REMETTENCE,
                    PAYMENT_SERVICE_REMETTENCE,
                    ADDITIONAL_CHANGES_REMITTANCE,
                    correspondentId,
                    SALES_TYPE_REMETTENCE,
                    paymentInfo.getExchangeRateId(),
                    paymentInfo.getRatePaymentNetworkId(),
                    SALES_PRICE_REMITTANCE,
                    LANGUAGE_REMETTENCE,
                    remittent.getCurrencyId(),
                    receiver.getCurrencyId(),
                    STORE_REMETTENCE,
                    PAYMENT_METHOD_REMITTANCE,
                    SERVICE_TYPE_REMITTANCE,
                    Long.toString(paymentInfo.getPaymentNetwork().getId()),
                    POINT_REMITTANCE,
                    USER_REMITTANCE,
                    CASH_BOX_REMITTANCE,
                    Long.toString(paymentInfo.getDeliveryForm().getId()),
                    remittent.getFirstName(),
                    remittent.getMiddleName(),
                    remittent.getLastName(),
                    remittent.getSecondSurname(),
                    remittent.getPhoneNumber(),
                    remittent.getEmail(),
                    ((remittent.getAddress().getCountry() != null) ? Long.toString(remittent.getAddress().getCountry().getId()) : null),
                    ((remittent.getAddress().getCity() != null) ? Long.toString(remittent.getAddress().getCity().getId()) : null),
                    ((remittent.getAddress().getState() != null) ? Long.toString(remittent.getAddress().getState().getId()) : null),
                    remittent.getAddress().getCountryName(),
                    remittent.getAddress().getCityName(),
                    remittent.getAddress().getAddress(),
                    remittent.getAddress().getZipCode(),
                    receiver.getFirstName(),
                    receiver.getMiddleName(),
                    receiver.getLastName(),
                    receiver.getSecondSurname(),
                    receiver.getPhoneNumber(),
                    receiver.getEmail(),
                    ((receiver.getAddress().getCountry() != null) ? Long.toString(receiver.getAddress().getCountry().getId()) : null),
                    ((receiver.getAddress().getCity()) != null ? Long.toString(receiver.getAddress().getCity().getId()) : null),
                    ((receiver.getAddress().getState()) != null ? Long.toString(receiver.getAddress().getState().getId()) : null),
                    receiver.getAddress().getCountryName(),
                    receiver.getAddress().getCityName(),
                    receiver.getAddress().getAddress(),
                    receiver.getAddress().getZipCode()
            );

            phase = 4;
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            errorMessage = e.getMessage();
        }
    }

    public void reset() {
        FacesContext.getCurrentInstance().getViewRoot().getViewMap().clear();
    }
}
