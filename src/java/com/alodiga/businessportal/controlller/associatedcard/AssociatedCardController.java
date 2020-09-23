package com.alodiga.businessportal.controlller.associatedcard;

import com.alodiga.businessportal.data.model.CardInfo;
import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.ActivateCardResponses;
import com.alodiga.wallet.ws.CheckStatusCardResponses;
import com.alodiga.wallet.ws.CheckStatusCredentialAccount;
import com.alodiga.wallet.ws.CheckStatusCredentialCard;
import com.alodiga.wallet.ws.DesactivateCardResponses;
import com.alodiga.wallet.ws.RechargeValidationResponse;
import com.alodiga.wallet.ws.TransactionResponse;
import com.portal.business.commons.cms.CmsCard;
import com.portal.business.commons.cms.data.CmsData;
import com.portal.business.commons.data.BusinessData;
import com.portal.business.commons.data.LogData;
import com.portal.business.commons.enumeration.OperationType;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.models.BusinessBalanceIncoming;
import com.portal.business.commons.models.UserCardTransactionLog;
import com.portal.business.commons.models.UserCardTransactionLog.TransactionAction;
import com.portal.business.commons.utils.AlodigaCryptographyUtils;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * @author henry
 */
@ManagedBean(name = "associatedCardController")
@ViewScoped
public class AssociatedCardController {

    private static final String ACTIVE_STATUS = "01";
    private static final String DEACTIVE_STATUS = "24";

    private String parentCardNumber;

    private CmsCard parentCard;

    private List<CmsCard> associatedCards;

    private List<CmsCard> filteredAssociatedCards;

    private CmsCard selectedCard;

    private CardInfo cardInfo;

    private String cardStatus;

    private String timezone;

    private final APIAlodigaWalletProxy proxy = new APIAlodigaWalletProxy();

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private ResourceBundle msg;

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean lenguajeBean;

    private CmsData cmsData;

    private LogData logData;

    private boolean hasError = false;

    private String errorMessage;

    private String messages;

    private Long transactionId;

    private boolean includeFees;

    private float businessFee;

    private float alodigaFee;

    private float totalCharge;

    private Double rechargeAmount = new Double(0);

    private static final Long CREDENTIALS_USER_ID = 379L;

    private boolean isRecharge = false;

    private boolean isConsult = false;

    private boolean isActivate = false;

    private int rechargePhase = 0;

    @PostConstruct
    public void init() {
        if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
        }
        cmsData = new CmsData();
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public void setLenguajeBean(LanguajeBean lenguajeBean) {
        this.lenguajeBean = lenguajeBean;
    }

    public String getParentCardNumber() {
        return parentCardNumber;
    }

    public void setParentCardNumber(String parentCardNumber) {
        this.parentCardNumber = parentCardNumber;
    }

    public CmsCard getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(CmsCard selectedCard) {
        this.selectedCard = selectedCard;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public List<CmsCard> getAssociatedCards() {
        return associatedCards;
    }

    public void setAssociatedCards(List<CmsCard> associatedCards) {
        this.associatedCards = associatedCards;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
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

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }

    public List<CmsCard> getFilteredAssociatedCards() {
        return filteredAssociatedCards;
    }

    public void setFilteredAssociatedCards(List<CmsCard> filteredAssociatedCards) {
        this.filteredAssociatedCards = filteredAssociatedCards;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public float getBusinessFee() {
        return businessFee;
    }

    public void setBusinessFee(float businessFee) {
        this.businessFee = businessFee;
    }

    public boolean isIncludeFees() {
        return includeFees;
    }

    public void setIncludeFees(boolean includeFees) {
        this.includeFees = includeFees;
    }

    public float getAlodigaFee() {
        return alodigaFee;
    }

    public void setAlodigaFee(float alodigaFee) {
        this.alodigaFee = alodigaFee;
    }

    public boolean isIsRecharge() {
        return isRecharge;
    }

    public boolean isIsConsult() {
        return isConsult;
    }

    public boolean isIsActivate() {
        return isActivate;
    }

    public void searchAssociated() {
        associatedCards = null;
        parentCard = null;

        try {
            parentCard = cmsData.getCardByNumber(parentCardNumber);
        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(AssociatedCardController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (NullParameterException ex) {
            Logger.getLogger(AssociatedCardController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (GeneralException ex) {
            Logger.getLogger(AssociatedCardController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        try {
            associatedCards = cmsData.getCardByCustomer(parentCard.getNaturalCustomer());
        } catch (EmptyListException ex) {
            Logger.getLogger(AssociatedCardController.class.getName()).log(Level.SEVERE, null, ex);
            associatedCards = new ArrayList();
        } catch (GeneralException ex) {
            Logger.getLogger(AssociatedCardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void activateCard() {
        try {
            String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(selectedCard.getCardNumber());
            ActivateCardResponses response = proxy.activateCardbyBusiness(loginBean.getCurrentBusiness().getId(), cardEncrypted, timezone);
            switch (response.getCodigoRespuesta()) {
                case "00": {
                    if (response.getCredentialResponse() != null) {
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(msg.getString("cardActivateSuccess")));
                        saveLog(TransactionAction.ACTIVATE, true);
                    } else {
                        messages = msg.getString("cardActivateFail");
                        saveLog(TransactionAction.ACTIVATE, false);
                    }
                }
                break;
                case "99": {
                    messages = msg.getString("cardActivateFail");
                }
                break;
                default:

                    messages = msg.getString("error.general") + " : " + response.getCodigoRespuesta();
            }

        } catch (RemoteException ex) {
            Logger.getLogger(AssociatedCardController.class.getName()).log(Level.SEVERE, null, ex);
            saveLog(TransactionAction.ACTIVATE, false);
            messages = msg.getString("error.cardwsconnection");
        }

        if (messages != null && !messages.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", messages));
        }

    }

    public void deactivateCard() {
        try {

            String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(selectedCard.getCardNumber());
            DesactivateCardResponses response = proxy.desactivateCardByBusiness(loginBean.getCurrentBusiness().getId(), cardEncrypted, timezone);
            switch (response.getCodigoRespuesta()) {
                case "00": {
                    if (response.getCredentialResponse() != null) {
                        saveLog(TransactionAction.DEACTIVATE, true);
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(msg.getString("cardDeactivateSuccess")));
                    } else {
                        saveLog(TransactionAction.DEACTIVATE, false);
                        messages = msg.getString("cardDeactivateFail");
                    }

                }
                break;
                case "99": {
                    messages = msg.getString("cardDeactivateFail");
                }
                break;
                default:
                    messages = msg.getString("error.general") + " : " + response.getCodigoRespuesta();
            }
        } catch (RemoteException ex) {
            Logger.getLogger(AssociatedCardController.class.getName()).log(Level.SEVERE, null, ex);
            saveLog(TransactionAction.DEACTIVATE, false);
            messages = msg.getString("error.cardwsconnection");
        }
        if (messages != null && !messages.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", messages));
        }

    }

    public void checkStatusCard() {
        try {
            hasError = false;
            String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(selectedCard.getCardNumber());
            System.out.println("cardEncrypted " + cardEncrypted);
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
                default:
                    cardStatus = null;
                    hasError = true;
                    errorMessage = msg.getString("error.general") + " : " + statusCardResponse.getCodigoRespuesta();
            }
        } catch (Exception ex) {
            Logger.getLogger(AssociatedCardController.class.getName()).log(Level.SEVERE, null, ex);
            cardStatus = null;
            hasError = true;
            errorMessage = msg.getString("error.cardwsconnection");
        }
    }

    public void checkBalanceCard() {

        try {
            hasError = false;
            cardInfo = new CardInfo();
            cardInfo.setCardNumber(selectedCard.getCardNumber());
            String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(selectedCard.getCardNumber());
            CheckStatusCardResponses statusCardResponse = proxy.checkStatusCardByBusiness(cardEncrypted, timezone);
            switch (statusCardResponse.getCodigoRespuesta()) {
                case "00": {
                    CheckStatusCredentialCard statusCard = statusCardResponse.getCheckStatusCredentialCard();
                    if (statusCard != null) {
                        String accountNumber = statusCard.getAccountNumber();
                        cardInfo.setAccountNumber(accountNumber);
                        String accountEncrypted = AlodigaCryptographyUtils.aloDesencrypt(accountNumber);
                        CheckStatusCredentialAccount statusAccount = proxy.checkStatusAccount(CREDENTIALS_USER_ID, accountEncrypted, timezone).getCheckStatusCredentialAccount();
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
                    errorMessage = msg.getString("error.general") + " : " + statusCardResponse.getCodigoRespuesta();
            }
        } catch (Exception ex) {
            Logger.getLogger(AssociatedCardController.class.getName()).log(Level.SEVERE, null, ex);
            saveLog(TransactionAction.CONSULT, false);
            cardInfo = null;
            hasError = true;
            errorMessage = msg.getString("error.cardwsconnection");
        }

    }

    public void rechargeCard() {

    }

    public void saveLog(TransactionAction action, boolean success) {
        try {
            UserCardTransactionLog log = new UserCardTransactionLog();
            log.setCardNumber(selectedCard.getCardNumber());
            log.setDateLog(new Date());
            log.setSuccess(success);
            log.setTransactionAction(action);
            log.setUser(loginBean.getUserSession());
            logData.saveUserCardTransactionLog(log);
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(AssociatedCardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void verifyRecharge() {
        try {
            rechargeAmount = (double) truncAmount(rechargeAmount.floatValue());
            if (includeFees) {
                businessFee = truncAmount((rechargeAmount.floatValue() * loginBean.getBusinessPercentFee()) / (100 + loginBean.getBusinessPercentFee()));
            }
            double rechargeSubmit = includeFees ? rechargeAmount - businessFee : rechargeAmount;
            RechargeValidationResponse response = proxy.validateRechargeCard(rechargeSubmit, includeFees);
            if (response != null) {
                switch (response.getCodigoRespuesta()) {
                    case "00": {
                        hasError = false;
                        alodigaFee = response.getTotalFee().floatValue();
                        rechargeAmount = response.getAmountBeforeFee();
                        if (!includeFees) {
                            businessFee = ((long) ((totalCharge * loginBean.getBusinessPercentFee() / 100) * 100)) / 100;
                        }

                        totalCharge = response.getTotalAmount().floatValue() + businessFee;
                        rechargePhase = 2;
                    }
                    break;
                    default:
                        hasError = true;
                        errorMessage = msg.getString("error.general") + " : " + response.getCodigoRespuesta();
                }
            } else {
                hasError = true;
                errorMessage = msg.getString("error.registerwsconnection");
            }
        } catch (Exception ex) {
            Logger.getLogger(AssociatedCardController.class.getName()).log(Level.SEVERE, null, ex);
            hasError = true;
            errorMessage = msg.getString("error.registerwsconnection");
        }
    }

    public void recharge() {

        try {
            String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(selectedCard.getCardNumber());
            TransactionResponse response = proxy.rechargeCard(loginBean.getCurrentBusiness().getId(), cardEncrypted, rechargeAmount, includeFees);
            if (response != null) {
                switch (response.getCodigoRespuesta()) {
                    case "00": {
                        hasError = false;
                        transactionId = response.getResponse().getId();
                        rechargePhase = 3;

                        BusinessBalanceIncoming log = new BusinessBalanceIncoming();
                        log.setBusiness(loginBean.getCurrentBusiness());
                        log.setUser(loginBean.getUserSession());
                        log.setBusinessFee(businessFee);
                        log.setAmountWithoutFee(totalCharge - businessFee);
                        log.setTotalAmount(totalCharge);
                        log.setDateTransaction(new Date());
                        log.setTransactionId(transactionId);
                        log.setType(OperationType.RECHARGE);
                        BusinessData businessData = new BusinessData();
                        businessData.saveIncomingBalance(log);

                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage("Recarga realizada con exito"));
                    }
                    break;
                    default:
                        hasError = true;
                        errorMessage = msg.getString("error.general") + " : " + response.getCodigoRespuesta();
                }
            } else {
                hasError = true;
                errorMessage = msg.getString("error.registerwsconnection");
            }
        } catch (Exception ex) {
            Logger.getLogger(AssociatedCardController.class.getName()).log(Level.SEVERE, null, ex);
            hasError = true;
            errorMessage = msg.getString("error.registerwsconnection");
        }
    }

    public void reset() {
        FacesContext.getCurrentInstance().getViewRoot().getViewMap().clear();
    }

    public String getHeader() {
        if (hasError) {
            return msg.getString(timezone);
        }
        switch (rechargePhase) {
            case 2:
                return msg.getString("confirm");
            case 3:
                return msg.getString("result");
            case 1:
            default:
                return msg.getString("dataAdquisition");
        }
    }

    private float truncAmount(float in) {
        return (float) Math.floor(in * 100) / 100;
    }

    public String getPhase() {
        return "1";
    }

    public String getIncludeFees() {
        return "";
    }

    public String getRechargeAmount() {
        return "";
    }

}
