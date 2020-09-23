package com.alodiga.primefaces.ultima.controller.accountbank;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.portal.business.commons.data.AccountBankData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.AccountBank;
import com.portal.business.commons.models.AccountTypeBank;
import com.portal.business.commons.models.Bank;
import com.portal.business.commons.models.Pos;
import com.portal.business.commons.models.StatusAccountBank;
import com.portal.business.commons.models.Store;
import com.portal.business.commons.utils.AlodigaCryptographyUtils;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author hvarona
 */
@ManagedBean
public class AccountBankController {

    private Long id;
    private String accountBankNumber;
    private Bank bank;
    private AccountTypeBank accountTypeBank;
    private StatusAccountBank statusAccountBank;

    private AccountBankData accountBankData = null;

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private ResourceBundle msg;

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean lenguajeBean;

    @PostConstruct
    public void init() {
        accountBankData = new AccountBankData();
        if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountBankNumber() {
        return accountBankNumber;
    }

    public void setAccountBankNumber(String accountBankNumber) {
        this.accountBankNumber = accountBankNumber;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public AccountTypeBank getAccountTypeBank() {
        return accountTypeBank;
    }

    public void setAccountTypeBank(AccountTypeBank accountTypeBank) {
        this.accountTypeBank = accountTypeBank;
    }

    public StatusAccountBank getStatusAccountBank() {
        return statusAccountBank;
    }

    public void setStatusAccountBank(StatusAccountBank statusAccountBank) {
        this.statusAccountBank = statusAccountBank;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public void setLenguajeBean(LanguajeBean lenguajeBean) {
        this.lenguajeBean = lenguajeBean;
    }

    public void save() {
        try {
            AccountBank accountBank = new AccountBank();
            accountBank.setAccountNumber(accountBankNumber);
            accountBank.setAccountTypeBankId(accountTypeBank);
            accountBank.setBankId(bank);
            accountBank.setCommerce(loginBean.getCurrentBusiness());
            accountBank.setCreateDate(new Timestamp(new Date().getTime()));
            accountBank.setStatusAccountBankId(statusAccountBank);
            accountBankData.saveAccountBank(accountBank);

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg.getString("accountBankSaveSuccesfull")));
            FacesContext.getCurrentInstance().getExternalContext().redirect("listAccountBank.xhtml");
        } catch (NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", msg.getString("error.missparameters")));
        } catch (GeneralException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", msg.getString("error.missparameters")));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", msg.getString("error.general")));
        }
    }

    public void reset() {
        RequestContext.getCurrentInstance().reset("AccountBankCreateForm:dataGrid");
    }

    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listAccountBank.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.AccountBankController.doRediret()");
        }
    }

 
}
