package com.alodiga.primefaces.ultima.controller.card;

import com.alodiga.primefaces.ultima.controller.wallet.rechargeController;
import com.alodiga.businessportal.data.model.CardInfo;
import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.CardResponse;
import com.alodiga.wallet.ws.RechargeValidationResponse;
import com.alodiga.wallet.ws.TransactionResponse;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.portal.business.commons.data.BusinessData;
import com.portal.business.commons.data.PreferencesData;
import com.portal.business.commons.enumeration.BPTransactionStatus;
import com.portal.business.commons.enumeration.BusinessServiceType;
import com.portal.business.commons.enumeration.OperationType;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.BusinessBalanceIncoming;
import com.portal.business.commons.models.LimitedsTransactionsResponse;
import com.portal.business.commons.models.ResponseCode;
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
@ManagedBean(name = "rechargeCardController")
@ViewScoped
public class RechargeCardController {

    private static final String ACTIVE_STATUS = "01";
    private static final String DEACTIVE_STATUS = "24";

    private String userEmail;

    private String timezone;

    private Long userId;

    private CardInfo cardInfo;

    private String cardStatus;

    private boolean includeFees;

    private Double rechargeAmount = new Double(0);

    private float rechargeAfterBusiness;

    private float businessFee;

    private float alodigaFee;

    private float totalCharge;

    private long transactionId;

    private int phase = 0;

    private final APIAlodigaWalletProxy allodigaWS = new APIAlodigaWalletProxy();

    private final APIRegistroUnificadoProxy registerWS = new APIRegistroUnificadoProxy();

    private ResourceBundle msg;

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean lenguajeBean;

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private boolean hasError = false;

    private String errorMessage;

    private String messages;
    private final BusinessData businessData = new BusinessData();
    private final PreferencesData preferenceData = new PreferencesData();

    @PostConstruct
    public void init() {
        if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
        }
        userId = 379L; //TODO implement User Id
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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
            return msg.getString("cardstatus." + cardStatus);
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

    public boolean isIncludeFees() {
        return includeFees;
    }

    public void setIncludeFees(boolean includeFees) {
        this.includeFees = includeFees;
    }

    public Double getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Double rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public float getBusinessFee() {
        return businessFee;
    }

    public void setBusinessFee(float businessFee) {
        this.businessFee = businessFee;
    }

    public float getAlodigaFee() {
        return alodigaFee;
    }

    public void setAlodigaFee(float alodigaFee) {
        this.alodigaFee = alodigaFee;
    }

    public float getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(float totalCharge) {
        this.totalCharge = totalCharge;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public float getTotalFee() {
        return truncAmount(businessFee + alodigaFee);
    }

    public String getHeader() {
        if (hasError) {
            return msg.getString("errorTitle");
        }
        switch (phase) {
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

    public void searchCard() {

        try {
            hasError = false;
            cardInfo = new CardInfo();
            cardInfo.setEmail(userEmail);
            //String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(cardNumber);
            LimitedsTransactionsResponse limitedsTransactionsResponse = preferenceData.validateService(loginBean.getCurrentBusiness(), BusinessServiceType.CARD_RECHARGE, 3);
            if (limitedsTransactionsResponse.getCode() == ResponseCode.SUCESS.getCodigo()) {
                CardResponse cardResponse = allodigaWS.getCardByEmail(userEmail);
                switch (cardResponse.getCodigoRespuesta()) {
                    case "00": {
                        //cardInfo.setAccountNumber(cardResponse.getCard().getAssignedAccount());
                        if (cardResponse.getAliasCard() != null && !cardResponse.getAliasCard().isEmpty()) {
                            cardInfo.setAliasCard(cardResponse.getAliasCard());
                            cardInfo.setName(cardResponse.getName());
                            cardInfo.setNumberPhone(cardResponse.getNumberPhone());
                            phase = 1;
                        } else {
                            phase = 0;
                            cardStatus = null;
                            hasError = true;
                            errorMessage = msg.getString("error.invalidUserCardEmail");
                        }
                    }
                    break;

                    case "58": {
                        cardStatus = null;
                        hasError = true;
                        errorMessage = msg.getString("error.invalidCardNumber");
                    }
                    break;
                    default:
                        cardInfo = null;
                        hasError = true;
                        //errorMessage = statusCardResponse.getMensajeRespuesta();
                        errorMessage = msg.getString("error.general") + " : " + cardResponse.getCodigoRespuesta();
                }
            } else {
                cardInfo = null;
                hasError = true;
                errorMessage = msg.getString(limitedsTransactionsResponse.getMessage());
            }
        } catch (Exception ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
            cardInfo = null;
            hasError = true;
            errorMessage = msg.getString("error.cardwsconnection");
        }

    }

    public void verifyRecharge() {
        try {
            rechargeAmount = (double) truncAmount(rechargeAmount.floatValue());
            double rechargeSubmit = rechargeAmount;
            RechargeValidationResponse response = allodigaWS.validateRechargeCard(rechargeSubmit, includeFees);
            if (response != null) {
                switch (response.getCodigoRespuesta()) {
                    case "00": {
                        hasError = false;
                        alodigaFee = response.getTotalFee().floatValue();
                        rechargeAmount = response.getAmountBeforeFee();

                        totalCharge = rechargeAmount.floatValue() - businessFee;

                        businessFee = ((long) ((rechargeAmount * loginBean.getBusinessPercentFee() / 100) * 100)) / 100;

                        phase = 2;
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
            Logger.getLogger(rechargeController.class.getName()).log(Level.SEVERE, null, ex);
            hasError = true;
            errorMessage = msg.getString("error.registerwsconnection");
        }
    }

    public void recharge() {
        BusinessBalanceIncoming log = createIncomingLog();
        try {
            saveInProcessIncomingLog(log);
            LimitedsTransactionsResponse limitedsTransactionsResponse = preferenceData.validateService(loginBean.getCurrentBusiness(), BusinessServiceType.CARD_RECHARGE, 3);
            if (limitedsTransactionsResponse.getCode() == ResponseCode.SUCESS.getCodigo()) {
                //String cardEncrypted = AlodigaCryptographyUtils.aloDesencrypt(cardNumber);
                TransactionResponse response = allodigaWS.rechargeCard(loginBean.getCurrentBusiness().getId(), userEmail, rechargeAmount, includeFees);
                if (response != null) {
                    switch (response.getCodigoRespuesta()) {
                        case "00": {
                            hasError = false;
                            transactionId = response.getResponse().getId();
                            phase = 3;
                            saveCompleteLog(log);
                            /*BusinessBalanceIncoming log = new BusinessBalanceIncoming();
                            log.setBusiness(loginBean.getCurrentBusiness());
                            log.setUser(loginBean.getUserSession());
                            log.setBusinessFee(businessFee);
                            log.setAmountWithoutFee(totalCharge - businessFee);
                            log.setTotalAmount(totalCharge);
                            log.setDateTransaction(new Date());
                            log.setTransactionId(transactionId);
                            log.setType(OperationType.CARD_RECHARGE);
                            businessData.saveIncomingBalance(log);*/

                            FacesContext.getCurrentInstance().addMessage(null,
                                    new FacesMessage("Recarga realizada con exito"));
                        }
                        break;
                        default:
                            saveFailedLog(log);
                            hasError = true;
                            errorMessage = msg.getString("error.general") + " : " + response.getCodigoRespuesta();
                    }
                } else {
                    saveFailedLog(log);
                    hasError = true;
                    errorMessage = msg.getString("error.registerwsconnection");
                }
            } else {
                saveFailedLog(log);
                hasError = true;
                errorMessage = msg.getString("error.registerwsconnection");
            }
        } catch (RemoteException ex) {
            saveFailedLog(log);
            Logger.getLogger(rechargeController.class.getName()).log(Level.SEVERE, null, ex);
            hasError = true;
            errorMessage = msg.getString("error.registerwsconnection");
        }
    }

    public void reset() {
        FacesContext.getCurrentInstance().getViewRoot().getViewMap().clear();
    }

    public BusinessBalanceIncoming createIncomingLog() {
        try {
            BusinessBalanceIncoming log = new BusinessBalanceIncoming();
            log.setBusiness(loginBean.getCurrentBusiness());
            log.setUser(loginBean.getUserSession());
            log.setBusinessFee(0);
            log.setAmountWithoutFee(0);
            log.setTotalAmount(0);
            log.setDateTransaction(new Date());
            log.setCreatedDate(new Date());
            log.setTransactionId(-1L);
            log.setTransactionStatus(BPTransactionStatus.CREATED);
            log.setType(OperationType.CARD_RECHARGE);
            return businessData.saveIncomingBalance(log);
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void saveInProcessIncomingLog(BusinessBalanceIncoming log) {
        try {
            log.setDateTransaction(new Date());
            log.setTransactionStatus(BPTransactionStatus.IN_PROCESS);
            businessData.saveIncomingBalance(log);

        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveCompleteLog(BusinessBalanceIncoming log) {
        try {
            log.setBusinessFee(alodigaFee);
            log.setBusinessCommission(businessFee);
            log.setAmountWithoutFee(rechargeAmount);
            log.setTotalAmount(totalCharge);
            log.setDateTransaction(new Date());
            log.setTransactionId(transactionId);
            log.setTransactionStatus(BPTransactionStatus.COMPLETED);
            businessData.saveIncomingBalance(log);
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveFailedLog(BusinessBalanceIncoming log) {
        try {
            log.setDateTransaction(new Date());
            log.setTransactionStatus(BPTransactionStatus.FAILED);
            businessData.saveIncomingBalance(log);
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(CardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
