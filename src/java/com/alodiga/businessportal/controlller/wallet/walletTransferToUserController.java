package com.alodiga.businessportal.controlller.wallet;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.BalanceHistoryResponse;
import com.alodiga.wallet.ws.ProductListResponse;
import com.alodiga.wallet.ws.TransactionResponse;
import com.alodiga.wallet.ws.TransactionValidationResponse;
import com.ericsson.alodiga.ws.APIRegistroUnificadoProxy;
import com.ericsson.alodiga.ws.RespuestaUsuario;
import com.ericsson.alodiga.ws.Usuario;
import java.rmi.RemoteException;
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
@ManagedBean(name = "walletTransferToUserController")
@ViewScoped
public class walletTransferToUserController {

    private String userEmailOrPhone;

    private String userEmail;

    private String userPhone;

    private String firstName;

    private String lastName;

    private String accountNumber;

    private Double transferAmount = new Double(0);

    private long transactionId;

    private Float balance = 0f;

    private boolean includeFees = false;

    private Float transactionFee = 0f;

    private Float totalCharge = 0f;

    private int phase = 0;

    private boolean hasError = false;

    private String errorMessage;

    private final APIAlodigaWalletProxy alodigaWS = new APIAlodigaWalletProxy();

    private final APIRegistroUnificadoProxy registerWS = new APIRegistroUnificadoProxy();

    private Usuario userForTransfer;

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
        try {
            BalanceHistoryResponse historyResponse = alodigaWS.getBalanceHistoryByProductAndBusinessId(loginBean.getCurrentBusiness().getId(), loginBean.getBusinessProduct().getId());
            switch (historyResponse.getCodigoRespuesta()) {
                case "00":
                    balance = historyResponse.getResponse().getCurrentAmount();
                    break;
                default:
                    balance = 0f;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            balance = 0f;
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

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double rechargeAmount) {
        this.transferAmount = rechargeAmount;
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

    public boolean isIncludeFees() {
        return includeFees;
    }

    public void setIncludeFees(boolean includeFees) {
        this.includeFees = includeFees;
    }

    public Float getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(Float transactionFee) {
        this.transactionFee = transactionFee;
    }

    public Float getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(Float totalCharge) {
        this.totalCharge = totalCharge;
    }

    public void searchUser() {
        try {
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
                            userForTransfer = response.getDatosRespuesta();
                            this.processUserInfo();
                            this.getUserProductList();
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
            }
        } catch (RemoteException ex) {
            Logger.getLogger(walletTransferToUserController.class.getName()).log(Level.SEVERE, null, ex);
            hasError = true;
            errorMessage = msg.getString("error.registerwsconnection");
        }

    }

    private void getUserProductList() throws RemoteException {
        ProductListResponse productResponse = alodigaWS.getProductsByUserId(Integer.toString(userForTransfer.getUsuarioID()));
        switch (productResponse.getCodigoRespuesta()) {
            case "00": {
                com.alodiga.wallet.ws.Product[] userProducts = productResponse.getProducts();
                if (userProducts == null || userProducts.length <= 0) {
                    hasError = true;
                    errorMessage = msg.getString(("rechargeWalletRechargeErrorNoProducts"));
                } else {
                    for (com.alodiga.wallet.ws.Product userProduct : userProducts) {
                        if (userProduct.equals(loginBean.getBusinessProduct())) {
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
                errorMessage = msg.getString(("rechargeWalletRechargeErrorNoProducts"));
        }
    }

    private float truncAmount(float in) {
        return (float) Math.floor(in * 100) / 100;
    }

    private void processUserInfo() {
        this.firstName = userForTransfer.getNombre();
        this.lastName = userForTransfer.getApellido();
        this.accountNumber = userForTransfer.getCuenta().getNumeroCuenta();
        this.userEmail = userForTransfer.getEmail();
        this.userPhone = userForTransfer.getMovil();
    }

    public void verifyTransfer() {
        transferAmount = (double) truncAmount(transferAmount.floatValue());
        if (balance >= transferAmount) {
            hasError = false;
            phase = 2;
        } else {
            hasError = true;
            errorMessage = msg.getString("error.transferLowBalance");
            return;
        }

        try {
            TransactionValidationResponse response = alodigaWS.validateBusinessTransferToUser(transferAmount.floatValue(), loginBean.getBusinessProduct().getId(), includeFees);
            if (response != null) {
                switch (response.getCodigoRespuesta()) {
                    case "00": {
                        hasError = false;
                        transactionFee = response.getFee();
                        transferAmount = new Double(response.getAmountBeforeFee());

                        totalCharge = response.getAmountAfterFee();

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
            Logger.getLogger(walletTransferToUserController.class.getName()).log(Level.SEVERE, null, ex);
            hasError = true;
            errorMessage = msg.getString("error.registerwsconnection");
        }
    }

    public void transfer() {

        try {
            TransactionResponse response = alodigaWS.saveTransferBetweenBusinessWithUser(loginBean.getBusinessProduct().getId(), transferAmount.floatValue(), "Business Portal Transfer", (long) userForTransfer.getUsuarioID(), loginBean.getCurrentBusiness().getId());
            if (response != null) {
                switch (response.getCodigoRespuesta()) {
                    case "00": {
                        hasError = false;
                        transactionId = Long.parseLong(response.getIdTransaction());
                        phase = 3;
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(msg.getString("walletTransferSuccesfull")));
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
            Logger.getLogger(walletTransferToUserController.class.getName()).log(Level.SEVERE, null, ex);
            hasError = true;
            errorMessage = msg.getString("error.registerwsconnection");
        }
    }

    public void reset() {
        FacesContext.getCurrentInstance().getViewRoot().getViewMap().clear();
    }
}
