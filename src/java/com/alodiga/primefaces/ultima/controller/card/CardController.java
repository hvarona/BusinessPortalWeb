package com.alodiga.primefaces.ultima.controller.card;

import com.alodiga.primefaces.ultima.data.model.CardInfo;
import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.ActivateCardResponses;
import com.alodiga.wallet.ws.CheckStatusCardResponses;
import com.alodiga.wallet.ws.CheckStatusCredentialAccount;
import com.alodiga.wallet.ws.CheckStatusCredentialCard;
import com.alodiga.wallet.ws.DesactivateCardResponses;
import com.portal.business.commons.data.LogData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.UserCardTransactionLog;
import com.portal.business.commons.models.UserCardTransactionLog.TransactionAction;
import com.portal.business.commons.utils.AlodigaCryptographyUtils;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author hvarona
 */
@ManagedBean(name = "cardController")
@ViewScoped
public class CardController {

    private static final String ACTIVE_STATUS = "01";
    private static final String DEACTIVE_STATUS = "24";

    private String cardNumber;

    private String timezone;

    private Long userId;

    private CardInfo cardInfo;

    private String cardStatus;

    APIAlodigaWalletProxy proxy = new APIAlodigaWalletProxy();

    ResourceBundle bundle;

    @ManagedProperty(value = "#{languajeBean}")
    LanguajeBean lenguajeBean;

    @ManagedProperty(value = "#{loginBean}")
    LoginBean loginBean;

    private LogData logData;

    private boolean hasError = false;

    private String errorMessage;

    private String messages;

    @PostConstruct
    public void init() {
        if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
            bundle = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            bundle = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
        }
        userId = 379L; //TODO implement User Id
        logData = new LogData();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public String getCardStatusData() {
        try {
            return bundle.getString("cardstatus." + cardStatus);
        } catch (Exception e) {
            return cardStatus;
        }
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public LanguajeBean getLenguajeBean() {
        return lenguajeBean;
    }

    public void setLenguajeBean(LanguajeBean lenguajeBean) {
        this.lenguajeBean = lenguajeBean;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public void activateCard() {
        try {
            String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(cardNumber);
            ActivateCardResponses response = proxy.activateCard(userId, cardEncrypted, timezone, ACTIVE_STATUS);
            System.out.println("codigo respuesta : " + response.getCodigoRespuesta());
            System.out.println("Mensaje respuesta : " + response.getMensajeRespuesta());
            switch (response.getCodigoRespuesta()) {
                case "00": {
                    if (response.getCredentialResponse() != null) {
                        System.out.println("Mensaje respuesta : " + response.getCredentialResponse().getCode());
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(bundle.getString("cardActivateSuccess")));
                        saveLog(TransactionAction.ACTIVATE, true);
                    } else {
                        messages = bundle.getString("cardActivateFail");
                        saveLog(TransactionAction.ACTIVATE, false);
                    }
                }
                break;
                case "99": {
                    messages = bundle.getString("cardActivateFail");
                }
                break;
                default:

                    messages = response.getMensajeRespuesta();
            }

        } catch (RemoteException ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
            saveLog(TransactionAction.ACTIVATE, false);
            messages = bundle.getString("error.cardwsconnection");
        }

        if (messages != null && !messages.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", messages));
        }

    }

    public void deactivateCard() {
                try {

            String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(cardNumber);
            DesactivateCardResponses response = proxy.desactivateCard(userId, cardEncrypted, timezone, DEACTIVE_STATUS);
            System.out.println("codigo respuesta : " + response.getCodigoRespuesta());
            System.out.println("Mensaje respuesta : " + response.getMensajeRespuesta());
            switch (response.getCodigoRespuesta()) {
                case "00": {
                    if (response.getCredentialResponse() != null) {
                        System.out.println("Estado respuesta : " + response.getCredentialResponse().getCode());
                        saveLog(TransactionAction.DEACTIVATE, true);
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(bundle.getString("cardDeactivateSuccess")));
                    } else {
                        saveLog(TransactionAction.DEACTIVATE, false);
                        messages = bundle.getString("cardDeactivateFail");
                    }

                }
                break;
                case "99": {
                    messages = bundle.getString("cardDeactivateFail");
                }
                break;
                default:
                    messages = response.getMensajeRespuesta();
            }
        } catch (RemoteException ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
            saveLog(TransactionAction.DEACTIVATE, false);
            messages = bundle.getString("error.cardwsconnection");
        }
        if (messages != null && !messages.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", messages));
        }

    }

    public void checkStatusCard() {
        try {
            hasError = false;
            String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(cardNumber);
            CheckStatusCardResponses statusCardResponse = proxy.checkStatusCard(userId, cardEncrypted, timezone);
            System.out.println("codigo respuesta : " + statusCardResponse.getCodigoRespuesta());
            System.out.println("Mensaje respuesta : " + statusCardResponse.getMensajeRespuesta());
            switch (statusCardResponse.getCodigoRespuesta()) {
                case "00": {
                    CheckStatusCredentialCard statusCard = statusCardResponse.getCheckStatusCredentialCard();
                    if (statusCard != null) {
                        String code = statusCard.getStateCode();
                        System.out.println("state code :" + statusCard.getStateCode());
                        System.out.println("state description :  " + statusCard.getDescription());

                        switch (code) {
                            case "01":
                                cardStatus = "active";
                                break;
                            case "24":
                                cardStatus = "deactive";
                                break;
                            default:
                                cardStatus = statusCard.getDescription();
                        }
                    }
                }
                break;
                default:
                    cardStatus = null;
                    hasError = true;
                    errorMessage = statusCardResponse.getMensajeRespuesta();
            }
        } catch (Exception ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
            cardStatus = null;
            hasError = true;
            errorMessage = bundle.getString("error.cardwsconnection");
        }
    }

    public void checkBalanceCard() {

        try {
            hasError = false;
            cardInfo = new CardInfo();
            cardInfo.setCardNumber(cardNumber);
            String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(cardNumber);
            CheckStatusCardResponses statusCardResponse = proxy.checkStatusCard(userId, cardEncrypted, timezone);
            System.out.println("codigo respuesta : " + statusCardResponse.getCodigoRespuesta());
            System.out.println("Mensaje respuesta : " + statusCardResponse.getMensajeRespuesta());
            switch (statusCardResponse.getCodigoRespuesta()) {
                case "00": {
                    CheckStatusCredentialCard statusCard = statusCardResponse.getCheckStatusCredentialCard();
                    if (statusCard != null) {
                        String accountNumber = statusCard.getAccountNumber();
                        cardInfo.setAccountNumber(accountNumber);
                        String accountEncrypted = AlodigaCryptographyUtils.aloDesencrypt(accountNumber);
                        CheckStatusCredentialAccount statusAccount = proxy.checkStatusAccount(userId, accountEncrypted, timezone).getCheckStatusCredentialAccount();
                        cardInfo.setAccountBalance(statusAccount.getAvailablePurchases());
                        cardInfo.setAccountDollarBalance(statusAccount.getDollarBalance());
                        saveLog(TransactionAction.CONSULT, true);
                    } else {
                        saveLog(TransactionAction.CONSULT, false);
                    }
                }
                break;
                default:
                    cardInfo = null;
                    hasError = true;
                    errorMessage = statusCardResponse.getMensajeRespuesta();
            }
        } catch (Exception ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
            saveLog(TransactionAction.CONSULT, false);
            cardInfo = null;
            hasError = true;
            errorMessage = bundle.getString("error.cardwsconnection");
        }

    }

    public void saveLog(TransactionAction action, boolean success) {
        try {
            UserCardTransactionLog log = new UserCardTransactionLog();
            log.setCardNumber(cardNumber);
            log.setDateLog(new Date());
            log.setSuccess(success);
            log.setTransactionAction(action);
            log.setUser(loginBean.getUserSession());
            logData.saveUserCardTransactionLog(log);
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}