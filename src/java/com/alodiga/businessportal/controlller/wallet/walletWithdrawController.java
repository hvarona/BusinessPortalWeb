package com.alodiga.businessportal.controlller.wallet;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.exception.EmptyListException;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.model.AccountBank;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.BalanceHistoryResponse;
import com.alodiga.wallet.ws.Product;
import com.alodiga.wallet.ws.ProductListResponse;
import com.alodiga.wallet.ws.TransactionResponse;
import com.alodiga.wallet.ws.TransactionValidationResponse;
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
@ManagedBean(name = "walletWithdrawController")
@ViewScoped
public class walletWithdrawController {

    private List<AccountBank> bankList;

    private AccountBank selectedBank;

    private List<String> accountBankIdList;

    private String selectedAccountBankId;

    private Float balance = 0f;

    private Double withdrawAmount = new Double(0);
    
    private boolean includeFees = false;

    private Float transactionFee = 0f;

    private Float totalCharge = 0f;

    private long transactionId;

    private int phase = 0;

    private boolean hasError = false;

    private String errorMessage;

    //TODO cambiar para que sea dinamico
    private final Long documentTypeId = -1L;

    private final Long originApplicationId = -1L;

    private final APIAlodigaWalletProxy alodigaWS = new APIAlodigaWalletProxy();

    private BusinessPortalEJB alodigaEJB;

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
            alodigaEJB = (BusinessPortalEJB) EJBServiceLocator.getInstance().get(com.alodiga.wallet.common.utils.EjbConstants.BUSINESS_PORTAL_EJB);
            bankList = alodigaEJB.getAccountBanksByBusiness(loginBean.getCurrentBusiness().getId());
            accountBankIdList = new ArrayList();
            for (AccountBank accountBank : bankList) {
                accountBankIdList.add(Long.toString(accountBank.getId()));
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
            } catch (RemoteException ex) {
                ex.printStackTrace();
                balance = 0f;
            }
        } catch (EmptyListException ex) {
            Logger.getLogger(walletWithdrawController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(walletWithdrawController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(walletWithdrawController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<AccountBank> getBankList() {
        return bankList;
    }

    public void setBankList(List<AccountBank> bankList) {
        this.bankList = bankList;
    }

    public AccountBank getSelectedBank() {
        return selectedBank;
    }

    public void setSelectedBank(AccountBank selectedBank) {
        this.selectedBank = selectedBank;
    }

    public Double getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(Double rechargeAmount) {
        this.withdrawAmount = rechargeAmount;
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

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
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
    
    

    public List<String> getAccountBankIdList() {
        return accountBankIdList;
    }

    public void setAccountBankIdList(List<String> accountBankIdList) {
        this.accountBankIdList = accountBankIdList;
    }

    public String getSelectedAccountBankId() {
        return selectedAccountBankId;
    }

    public void setSelectedAccountBankId(String selectedAccountBankId) {
        this.selectedAccountBankId = selectedAccountBankId;
        this.selectedBank = null;
        for (AccountBank accountBank : bankList) {
            if (selectedAccountBankId.equals(Long.toString(accountBank.getId()))) {
                this.selectedBank = accountBank;
                break;
            }
        }
    }

    public String getAccountBankName(String accountBankId) {
        for (AccountBank accountBank : bankList) {
            if (accountBankId.equals(Long.toString(accountBank.getId()))) {
                return accountBank.getBankId().getName() + " : " + accountBank.getAccountNumber();
            }
        }
        return null;
    }

    private float truncAmount(float in) {
        return (float) Math.floor(in * 100) / 100;
    }

    public void verifyWithdraw() {
        phase = 0;
        withdrawAmount = (double) truncAmount(withdrawAmount.floatValue());
        if (balance >= withdrawAmount) {
            hasError = false;
            phase = 1;
        } else {
            hasError = true;
            errorMessage = msg.getString("error.transferLowBalance");
        }
        try {
            TransactionValidationResponse response = alodigaWS.validateBusinessTransferToUser(withdrawAmount.floatValue(), loginBean.getBusinessProduct().getId(), includeFees);
            if (response != null) {
                switch (response.getCodigoRespuesta()) {
                    case "00": {
                        hasError = false;
                        transactionFee = response.getFee();
                        withdrawAmount = new Double(response.getAmountBeforeFee());

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

    public void withdraw() {

        try {
            TransactionResponse response = alodigaWS.manualWithdrawalsBusiness(selectedBank.getBankId().getId(), selectedBank.getId(), selectedBank.getAccountNumber(), withdrawAmount.floatValue(), loginBean.getBusinessProduct().getId(), "Retiro", loginBean.getCurrentBusiness().getId(), 1024L);
            if (response != null) {
                switch (response.getCodigoRespuesta()) {
                    case "00": {
                        hasError = false;
                        transactionId = Long.parseLong(response.getIdTransaction());
                        phase = 2;
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(msg.getString("walletWithdrawSuccesfull")));
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
            Logger.getLogger(walletWithdrawController.class.getName()).log(Level.SEVERE, null, ex);
            hasError = true;
            errorMessage = msg.getString("error.registerwsconnection");
        }
    }

    public void reset() {
        FacesContext.getCurrentInstance().getViewRoot().getViewMap().clear();
    }
}
