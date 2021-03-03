package com.alodiga.primefaces.ultima.controller.accountbank;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.exception.EmptyListException;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.genericEJB.EJBRequest;
import com.alodiga.wallet.common.model.AccountBank;
import com.alodiga.wallet.common.model.AccountTypeBank;
import com.alodiga.wallet.common.model.Bank;
import com.alodiga.wallet.common.model.StatusAccountBank;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import java.io.IOException;
import java.sql.Timestamp;
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

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private ResourceBundle msg;

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean lenguajeBean;

    private BusinessPortalEJB proxy;

    @PostConstruct
    public void init() {
        if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
        }

        proxy = (BusinessPortalEJB) EJBServiceLocator.getInstance().get(com.alodiga.wallet.common.utils.EjbConstants.BUSINESS_PORTAL_EJB);
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
            accountBank.setBusinessId(loginBean.getCurrentBusiness().getId());
            accountBank.setCreateDate(new Timestamp(new Date().getTime()));
            List<StatusAccountBank> status = proxy.getStatusAccountBanks(new EJBRequest());
            for (StatusAccountBank stat : status) {
                if (stat.getDescription().equalsIgnoreCase("activo")) {
                    statusAccountBank = stat;
                    break;
                }
            }
            if (statusAccountBank == null) {
                if (status.size() > 0) {
                    statusAccountBank = status.get(0);
                } else {
                    statusAccountBank = null;
                }
            }
            accountBank.setStatusAccountBankId(statusAccountBank);
            proxy.saveAccountBank(accountBank);

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg.getString("accountBankSaveSuccesfull")));
            FacesContext.getCurrentInstance().getExternalContext().redirect("listAccountBank.xhtml");
        } catch (RegisterNotFoundException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", msg.getString("error.missparameters")));
        } catch (NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", msg.getString("error.missparameters")));
        } catch (GeneralException | IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", msg.getString("error.general")));
        } catch (EmptyListException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", msg.getString("error.internalError")));
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
