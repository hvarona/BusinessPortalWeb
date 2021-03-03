package com.alodiga.businessportal.controlller.wallet;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.BalanceHistoryResponse;
import com.alodiga.wallet.ws.Product;
import com.alodiga.wallet.ws.ProductListResponse;
import com.alodiga.wallet.ws.TransactionResponse;
import com.portal.business.commons.data.BusinessData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.Business;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
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
@ManagedBean(name = "walletTransferToBusinessController")
@ViewScoped
public class walletTransferToBusinessController {

    private String userEmailOrPhone;

    private List<Product> activateProducts;

    private Product selectedProduct;

    private String accountNumber;

    private Double transferAmount = new Double(0);

    private long transactionId;

    private int phase = 0;

    private boolean hasError = false;

    private String errorMessage;

    private final APIAlodigaWalletProxy alodigaWS = new APIAlodigaWalletProxy();

    private final BusinessData businessData = new BusinessData();

    private Business businessToTransfer;

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

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double rechargeAmount) {
        this.transferAmount = rechargeAmount;
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        if (selectedProduct != null) {
            for (Product prod : activateProducts) {
                if (Objects.equals(prod.getId(), selectedProduct.getId())) {
                    this.selectedProduct = prod;
                }
            }
        }
    }

    public List<Product> getActivateProducts() {
        return activateProducts;
    }

    public void setActivateProducts(List<Product> activateProducts) {
        this.activateProducts = activateProducts;
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

    public void searchBusiness() {
        try {
            if (this.userEmailOrPhone == null || this.userEmailOrPhone.isEmpty()) {
                hasError = true;
                errorMessage = msg.getString("rechargeWalletRechargeErrorInvalidEmailPhone");
            } else {
                if (PHONE_PATTERN.matcher(userEmailOrPhone).matches()) {
                    businessToTransfer = businessData.getBusinessByPhone(userEmailOrPhone);
                } else {
                    businessToTransfer = businessData.getBusinessByEmail(userEmailOrPhone);
                }
                this.getBusinessProductList();
            }
        } catch (RemoteException ex) {
            Logger.getLogger(walletTransferToBusinessController.class.getName()).log(Level.SEVERE, null, ex);
            hasError = true;
            errorMessage = msg.getString("error.registerwsconnection");
        } catch (NullParameterException ex) {
            Logger.getLogger(walletTransferToBusinessController.class.getName()).log(Level.SEVERE, null, ex);
            hasError = true;
            errorMessage = msg.getString("error.nullParameter");
        } catch (GeneralException ex) {
            hasError = true;
            errorMessage = msg.getString("error.generalException");
        }

    }

    private void getBusinessProductList() throws RemoteException {
        activateProducts = new ArrayList();
        ProductListResponse productResponse = alodigaWS.getProductsByBusinessId(Long.toString(businessToTransfer.getId()));
        switch (productResponse.getCodigoRespuesta()) {
            case "00": {
                com.alodiga.wallet.ws.Product[] businessProducts = productResponse.getProducts();
                if (businessProducts == null || businessProducts.length <= 0) {
                    hasError = true;
                    errorMessage = msg.getString(("rechargeWalletRechargeErrorNoProducts"));
                } else {
                    activateProducts.addAll(Arrays.asList(businessProducts));
                }
                hasError = false;
                phase = 1;
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

    public void verifyTransfer() {
        try {
            transferAmount = (double) truncAmount(transferAmount.floatValue());
            BalanceHistoryResponse response = alodigaWS.getBalanceHistoryByProductAndBusinessId(loginBean.getCurrentBusiness().getId(), selectedProduct.getId());
            if (response != null) {
                switch (response.getCodigoRespuesta()) {
                    case "00": {
                        hasError = false;
                        if (response.getResponse().getCurrentAmount() <= transferAmount) {

                            phase = 2;
                        } else {
                            hasError = true;
                            errorMessage = msg.getString("error.transferLowBalance");
                        }
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
        } catch (RemoteException ex) {
            Logger.getLogger(walletTransferToUserController.class.getName()).log(Level.SEVERE, null, ex);
            hasError = true;
            errorMessage = msg.getString("error.registerwsconnection");
        }
    }

    public void transfer() {

        try {
        TransactionResponse response = alodigaWS.saveTransferBetweenBusinessAccount(selectedProduct.getId(), transferAmount.floatValue(), "Business Portal Transfer",businessToTransfer.getId(),loginBean.getCurrentBusiness().getId());
            if (response != null) {
                switch (response.getCodigoRespuesta()) {
                    case "00": {
                        hasError = false;
                        transactionId = response.getResponse().getId();
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
