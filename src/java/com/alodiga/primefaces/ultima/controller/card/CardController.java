package com.alodiga.primefaces.ultima.controller.card;

import com.alodiga.primefaces.ultima.controller.wallet.rechargeController;
import com.alodiga.businessportal.data.model.CardInfo;
import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.ActivateCardResponses;
import com.alodiga.wallet.ws.CheckStatusCardResponses;
import com.alodiga.wallet.ws.CheckStatusCredentialAccount;
import com.alodiga.wallet.ws.CheckStatusCredentialCard;
import com.alodiga.wallet.ws.DesactivateCardResponses;
import com.portal.business.commons.data.BusinessData;
import com.portal.business.commons.data.LogData;
import com.portal.business.commons.data.PreferencesData;
import com.portal.business.commons.enumeration.BPTransactionStatus;
import com.portal.business.commons.enumeration.BusinessServiceType;
import com.portal.business.commons.enumeration.OperationType;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.LimitedsTransactionsResponse;
import com.portal.business.commons.models.ResponseCode;
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

    private final LogData logData = new LogData();

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
        UserCardTransactionLog log = createLog();
        try {
            messages = null;
            String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(cardNumber);
            BusinessData businessData = new BusinessData();
            saveInProcessLog(log);
            LimitedsTransactionsResponse limitedsTransactionsResponse = businessData.validTransacionalLimit(loginBean.getCurrentBusiness(), OperationType.CARD_ACTIVATED);
            if (limitedsTransactionsResponse.getCode() == ResponseCode.SUCESS.getCodigo()) {
                ActivateCardResponses response = proxy.activateCardbyBusiness(loginBean.getCurrentBusiness().getId(), cardEncrypted, timezone);
                switch (response.getCodigoRespuesta()) {
                    case "00": {
                        if (response.getCredentialResponse() != null) {
                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage(bundle.getString("cardActivateSuccess")));
                            saveCompleteLog(log, TransactionAction.ACTIVATE, true);
                        } else {
                            messages = bundle.getString("cardActivateFail");
                            saveCompleteLog(log, TransactionAction.ACTIVATE, false);
                        }
                    }
                    break;
                    case "99": {
                        messages = bundle.getString("cardActivateFail");
                    }
                    break;
                    default:
                        messages = bundle.getString("error.general") + " : " + response.getCodigoRespuesta();
                }
            } else {
                hasError = true;
                errorMessage = bundle.getString("error.registerwsconnection");
            }
        } catch (GeneralException | NullParameterException | RemoteException ex) {
            Logger.getLogger(rechargeController.class.getName()).log(Level.SEVERE, null, ex);
            hasError = true;
            errorMessage = bundle.getString("error.cardwsconnection");
        }

        if (messages != null && !messages.isEmpty()) {
            saveFailedLog(log);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", messages));
        }

    }

    public void deactivateCard() {
        UserCardTransactionLog log = createLog();
        try {
            messages = null;
            String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(cardNumber);
            saveInProcessLog(log);
            DesactivateCardResponses response = proxy.desactivateCardByBusiness(loginBean.getCurrentBusiness().getId(), cardEncrypted, timezone);
            switch (response.getCodigoRespuesta()) {
                case "00": {
                    if (response.getCredentialResponse() != null) {
                        saveCompleteLog(log, TransactionAction.DEACTIVATE, true);
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(bundle.getString("cardDeactivateSuccess")));
                    } else {
                        saveCompleteLog(log, TransactionAction.DEACTIVATE, false);
                        messages = bundle.getString("cardDeactivateFail");
                    }

                }
                break;
                case "99": {
                    messages = bundle.getString("cardDeactivateFail");
                }
                break;
                default:
                    messages = bundle.getString("error.general") + " : " + response.getCodigoRespuesta();
            }
        } catch (RemoteException ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
            messages = bundle.getString("error.cardwsconnection");
        }
        if (messages != null && !messages.isEmpty()) {
            saveFailedLog(log);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", messages));
        }

    }

    public void checkStatusCard() {
        try {
            hasError = false;
            String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(cardNumber);
            PreferencesData preferenceData = new PreferencesData();
            LimitedsTransactionsResponse limitedsTransactionsResponse = preferenceData.validateService(loginBean.getCurrentBusiness(), BusinessServiceType.CARD_ACTIVATION, 3);
            if (limitedsTransactionsResponse.getCode() == ResponseCode.SUCESS.getCodigo()) {
                CheckStatusCardResponses statusCardResponse = proxy.checkStatusCardByBusiness(cardEncrypted, timezone);
                switch (statusCardResponse.getCodigoRespuesta()) {
                    case "00": {
                        CheckStatusCredentialCard statusCard = statusCardResponse.getCheckStatusCredentialCard();
                        if (statusCard != null) {
                            String code = statusCard.getStateCode();

                            switch (code) {
                                case ACTIVE_STATUS:
                                    cardStatus = "active";
                                    break;
                                case DEACTIVE_STATUS:
                                    cardStatus = "deactive";
                                    break;
                                default:
                                    cardStatus = statusCard.getDescription();
                            }
                        }
                    }
                    break;
                    case "58": {
                        cardStatus = null;
                        hasError = true;
                        errorMessage = bundle.getString("error.invalidCardNumber");
                    }
                    break;
                    default:
                        cardStatus = null;
                        hasError = true;
                        errorMessage = bundle.getString("error.general") + " : " + statusCardResponse.getCodigoRespuesta();
                }
            } else {
                hasError = true;
                errorMessage = bundle.getString("error.registerwsconnection");
            }
        } catch (RemoteException ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
            cardStatus = null;
            hasError = true;
            errorMessage = bundle.getString("error.cardwsconnection");
        }
    }

    public void checkBalanceCard() {
        UserCardTransactionLog log = createLog();
        try {
            hasError = false;
            cardInfo = new CardInfo();
            cardInfo.setCardNumber(cardNumber);
            String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(cardNumber);
            saveInProcessLog(log);
            CheckStatusCardResponses statusCardResponse = proxy.checkStatusCardByBusiness(cardEncrypted, timezone);
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
                        saveCompleteLog(log, TransactionAction.CONSULT, true);
                    } else {
                        saveCompleteLog(log, TransactionAction.CONSULT, false);
                    }
                }
                break;
                case "58": {
                    saveFailedLog(log);
                    cardStatus = null;
                    hasError = true;
                    errorMessage = bundle.getString("error.invalidCardNumber");
                }
                break;
                default:
                    saveFailedLog(log);
                    cardInfo = null;
                    hasError = true;
                    errorMessage = bundle.getString("error.general") + " : " + statusCardResponse.getCodigoRespuesta();
            }
        } catch (Exception ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
            saveFailedLog(log);
            cardInfo = null;
            hasError = true;
            errorMessage = bundle.getString("error.cardwsconnection");
        }

    }

    public UserCardTransactionLog createLog() {
        try {
            UserCardTransactionLog log = new UserCardTransactionLog();
            log.setCardNumber(cardNumber);
            log.setDateLog(new Date());
            log.setCreatedDate(new Date());
            log.setSuccess(false);
            log.setTransactionAction(null);
            log.setTransactionStatus(BPTransactionStatus.CREATED);
            log.setUser(loginBean.getUserSession());
            return logData.saveUserCardTransactionLog(log);
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void saveInProcessLog(UserCardTransactionLog log) {
        try {
            log.setCardNumber(cardNumber);
            log.setDateLog(new Date());
            log.setSuccess(false);
            log.setTransactionAction(null);
            log.setTransactionStatus(BPTransactionStatus.IN_PROCESS);
            log.setUser(loginBean.getUserSession());
            logData.saveUserCardTransactionLog(log);

        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveCompleteLog(UserCardTransactionLog log, TransactionAction action, boolean success) {
        try {
            log.setCardNumber(cardNumber);
            log.setDateLog(new Date());
            log.setSuccess(success);
            log.setTransactionAction(action);
            log.setUser(loginBean.getUserSession());
            log.setTransactionStatus(BPTransactionStatus.COMPLETED);
            logData.saveUserCardTransactionLog(log);
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveFailedLog(UserCardTransactionLog log) {
        try {
            log.setCardNumber(cardNumber);
            log.setDateLog(new Date());
            log.setSuccess(false);
            log.setTransactionAction(null);
            log.setUser(loginBean.getUserSession());
            log.setTransactionStatus(BPTransactionStatus.FAILED);
            logData.saveUserCardTransactionLog(log);
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
