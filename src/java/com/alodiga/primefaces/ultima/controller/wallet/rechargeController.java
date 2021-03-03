package com.alodiga.primefaces.ultima.controller.wallet;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.ProductListResponse;
import com.alodiga.wallet.ws.RechargeValidationResponse;
import com.alodiga.wallet.ws.TransactionResponse;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.RespuestaUsuario;
import com.ericsson.alodiga.ws.Usuario;
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
import java.util.regex.Pattern;
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
@ManagedBean(name = "rechargeController")
@ViewScoped
public class rechargeController {

    private String userEmailOrPhone;

    private String userEmail;

    private String userPhone;

    private String firstName;

    private String lastName;

    private String accountNumber;

    private boolean includeFees;

    private Double rechargeAmount = new Double(0);

    private float businessFee;

    private float alodigaFee;

    private float totalCharge;

    private long transactionId;

    private int phase = 0;

    private boolean hasError = false;

    private String errorMessage;

    private final APIAlodigaWalletProxy allodigaWS = new APIAlodigaWalletProxy();

    private final APIRegistroUnificadoProxy registerWS = new APIRegistroUnificadoProxy();

    private Usuario userForRecharge;

    private final BusinessData businessData = new BusinessData();
    private final PreferencesData preferenceData = new PreferencesData();

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean languageBean;

    private ResourceBundle msg;

    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d+");

    @PostConstruct
    public void init() {
        if (languageBean == null || languageBean.getLanguaje() == null || languageBean.getLanguaje().isEmpty()) {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(languageBean.getLanguaje()));
        }
    }

    public String getUserEmailOrPhone() {
        return userEmailOrPhone;
    }

    public void setUserEmailOrPhone(String userEmailOrPhone) {
        this.userEmailOrPhone = userEmailOrPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
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

    public void setLanguageBean(LanguajeBean languageBean) {
        this.languageBean = languageBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void searchUser() {
        try {
            LimitedsTransactionsResponse limitedsTransactionsResponse = preferenceData.validateService(loginBean.getCurrentBusiness(), BusinessServiceType.RECHARGE, loginBean.getBusinessProduct().getId());
            if (limitedsTransactionsResponse.getCode() == ResponseCode.SUCESS.getCodigo()) {
                if (this.userEmailOrPhone == null || this.userEmailOrPhone.isEmpty()) {
                    hasError = true;
                    errorMessage = msg.getString("rechargeWalletRechargeErrorInvalidEmailPhone");
                } else {
                    RespuestaUsuario response;
                    if (PHONE_PATTERN.matcher(userEmailOrPhone).matches()) {
                        response = registerWS.getUsuariopormovil("usuarioWS", "passwordWS", userEmailOrPhone);
                    } else {
                        response = registerWS.getUsuarioporemail("usuarioWS", "passwordWS", userEmailOrPhone);
                    }
                    if (response != null) {
                        switch (response.getCodigoRespuesta()) {
                            case "00": {
                                userForRecharge = response.getDatosRespuesta();
                                this.processUserInfo();
                                this.verifirUserProduct();
                            }
                            break;
                            case "97": {
                                hasError = true;
                                errorMessage = msg.getString("error.rechargeUserNoExist");
                            }
                            default:
                                hasError = true;
                                errorMessage = msg.getString("error.general") + " (" + response.getCodigoRespuesta() + ") : " + response.getMensajeRespuesta();
                        }
                    } else {
                        hasError = true;
                        errorMessage = msg.getString("error.registerwsconnection");
                    }
                }
            } else {
                hasError = true;
                errorMessage = msg.getString(limitedsTransactionsResponse.getMessage());
            }
        } catch (RemoteException ex) {
            Logger.getLogger(rechargeController.class.getName()).log(Level.SEVERE, null, ex);
            hasError = true;
            errorMessage = msg.getString("error.registerwsconnection");
        }
    }

    private void verifirUserProduct() throws RemoteException {
        ProductListResponse productResponse = allodigaWS.getProductsByUserId(Integer.toString(userForRecharge.getUsuarioID()));
        switch (productResponse.getCodigoRespuesta()) {
            case "00": {
                com.alodiga.wallet.ws.Product[] userProducts = productResponse.getProducts();
                if (userProducts == null || userProducts.length <= 0) {
                    hasError = true;
                    errorMessage = msg.getString(("rechargeWalletRechargeErrorNoProducts"));
                } else {
                    for (com.alodiga.wallet.ws.Product userProduct : userProducts) {
                        if (!userProduct.equals(loginBean.getBusinessProduct())) {
                            hasError = false;
                            phase = 1;
                            return;
                        }
                    }
                    hasError = true;
                    errorMessage = msg.getString(("rechargeWalletRechargeErrorNoProducts"));

                }
            }
            break;
            default:
                hasError = true;
                errorMessage = msg.getString(("rechargeWalletErrorFetchignProducts") + " " + productResponse.getCodigoRespuesta());
        }
    }

    public float getTotalFee() {
        return truncAmount(businessFee + alodigaFee);
    }

    private float truncAmount(float in) {
        return (float) Math.floor(in * 100) / 100;
    }

    private void processUserInfo() {
        this.firstName = userForRecharge.getNombre();
        this.lastName = userForRecharge.getApellido();
        this.accountNumber = userForRecharge.getCuenta().getNumeroCuenta();
        this.userEmail = userForRecharge.getEmail();
        this.userPhone = userForRecharge.getMovil();
    }

    public void verifyRecharge() {
        try {
            rechargeAmount = (double) truncAmount(rechargeAmount.floatValue());
            double rechargeSubmit = rechargeAmount;
            LimitedsTransactionsResponse limitedsTransactionsResponse = preferenceData.validateServiceAmount(loginBean.getCurrentBusiness(), BusinessServiceType.RECHARGE, loginBean.getBusinessProduct().getId(), (float) rechargeSubmit);
            if (limitedsTransactionsResponse.getCode() == ResponseCode.SUCESS.getCodigo()) {
                RechargeValidationResponse response = allodigaWS.validateRechargeProduct((long) userForRecharge.getUsuarioID(), loginBean.getBusinessProduct().getId(), rechargeSubmit, includeFees);

                if (response != null) {
                    switch (response.getCodigoRespuesta()) {
                        case "00": {
                            hasError = false;
                            alodigaFee = truncAmount(response.getTotalFee().floatValue());
                            totalCharge = truncAmount(response.getAmountBeforeFee().floatValue());

                            businessFee = truncAmount((float) (rechargeAmount * loginBean.getBusinessPercentFee() / 100));

                            //totalCharge = truncAmount(rechargeAmount.floatValue() - alodigaFee);
                            rechargeAmount = (double) truncAmount(response.getTotalAmount().floatValue());
                            phase = 2;
                        }
                        break;
                        default:
                            hasError = true;
                            errorMessage = msg.getString("error.general") + " (" + response.getCodigoRespuesta() + ") : " + response.getMensajeRespuesta();
                    }
                } else {
                    hasError = true;
                    errorMessage = msg.getString("error.registerwsconnection");
                }
            } else {
                hasError = true;
                errorMessage = msg.getString(limitedsTransactionsResponse.getMessage());
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
            TransactionResponse response = allodigaWS.rechargeWalletProduct(loginBean.getCurrentBusiness().getId(), (long) userForRecharge.getUsuarioID(), loginBean.getBusinessProduct().getId(), (double) totalCharge, false);
            if (response != null) {
                switch (response.getCodigoRespuesta()) {
                    case "00": {
                        hasError = false;
                        transactionId = response.getResponse().getId();
                        phase = 3;
                        saveCompleteLog(log);

                        //businessData.saveIncomingBalance(log);
                        //TODO save transaction log
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage("Recarga realizada con exito"));
                    }
                    break;
                    default:
                        saveFailedLog(log);
                        hasError = true;
                        errorMessage = msg.getString("error.general") + " (" + response.getCodigoRespuesta() + ") : " + response.getMensajeRespuesta();
                }
            } else {
                saveFailedLog(log);
                hasError = true;
                errorMessage = msg.getString("error.registerwsconnection");
            }
        } catch (Exception ex) {
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
            log.setType(OperationType.RECHARGE);
            return businessData.saveIncomingBalance(log);
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(rechargeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void saveInProcessIncomingLog(BusinessBalanceIncoming log) {
        try {
            log.setDateTransaction(new Date());
            log.setTransactionStatus(BPTransactionStatus.IN_PROCESS);
            businessData.saveIncomingBalance(log);

        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(rechargeController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(rechargeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveFailedLog(BusinessBalanceIncoming log) {
        try {
            log.setDateTransaction(new Date());
            log.setTransactionStatus(BPTransactionStatus.FAILED);
            businessData.saveIncomingBalance(log);
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(rechargeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
