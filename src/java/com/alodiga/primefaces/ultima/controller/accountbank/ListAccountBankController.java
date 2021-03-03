package com.alodiga.primefaces.ultima.controller.accountbank;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.exception.EmptyListException;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.genericEJB.EJBRequest;
import com.alodiga.wallet.common.model.AccountBank;
import com.alodiga.wallet.common.model.AccountTypeBank;
import com.alodiga.wallet.common.model.Bank;
import com.alodiga.wallet.common.model.StatusAccountBank;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author hvarona
 */
@ManagedBean(name = "dtListAccountBankController")
@ViewScoped
public class ListAccountBankController implements Serializable {

    private AccountBank selectedAccountBank;

    private List<AccountBank> accountBanks;
    private List<AccountBank> filteredAccountBank;

    private Map<String, String> banks = null;
    private Bank bank = null;
    private Map<String, String> accountTypeBanks = null;
    private AccountTypeBank accountTypeBank = null;
    private Map<String, String> statusAccountBanks = null;
    private StatusAccountBank statusAccountBank = null;

    @ManagedProperty(value = "#{loginBean}")
    LoginBean loginBean;

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
        try {
            EJBRequest request = new EJBRequest();
            Map<String, Object> params = new HashMap();
            params.put("businessId", loginBean.getCurrentBusiness().getId());
            request.setParams(params);
            accountBanks = proxy.getAccountBanksByBusiness(loginBean.getCurrentBusiness().getId());
            proxy = (BusinessPortalEJB) EJBServiceLocator.getInstance().get(com.alodiga.wallet.common.utils.EjbConstants.BUSINESS_PORTAL_EJB);
        } catch (GeneralException | NullParameterException ex) {
            Logger.getLogger(ListAccountBankController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ignored) {
            accountBanks = new ArrayList();
        }

    }

    public AccountBank getSelectedAccountBank() {
        return selectedAccountBank;
    }

    public void setSelectedAccountBank(AccountBank selectedAccountBank) {
        this.selectedAccountBank = selectedAccountBank;
    }

    public List<AccountBank> getAccountBanks() {
        return accountBanks;
    }

    public void setAccountBanks(List<AccountBank> accountBanks) {
        this.accountBanks = accountBanks;
    }

    public List<AccountBank> getFilteredAccountBank() {
        return filteredAccountBank;
    }

    public void setFilteredAccountBank(List<AccountBank> filteredAccountBank) {
        this.filteredAccountBank = filteredAccountBank;
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

    public Map<String, String> getBanks() {
        if (banks == null) {
            banks = new TreeMap();
            try {
                List<Bank> list = proxy.getBanks(new EJBRequest());
                for (Bank b : list) {
                    banks.put(b.getName(), String.valueOf(b.getId()));
                }
            } catch (GeneralException ex) {
                Logger.getLogger(ListAccountBankController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EmptyListException ignored) {

            } catch (NullParameterException ignored) {

            }
        }
        return banks;
    }

    public void setBanks(Map<String, String> banks) {
        this.banks = banks;
    }

    public Map<String, String> getAccountTypeBanks() {
        if (accountTypeBanks == null) {
            accountTypeBanks = new TreeMap();
            try {
                List<AccountTypeBank> list = proxy.getAccountTypeBanks(new EJBRequest());
                for (AccountTypeBank a : list) {
                    accountTypeBanks.put(a.getDescription(), a.getId().toString());
                }
            } catch (GeneralException ex) {
                Logger.getLogger(ListAccountBankController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EmptyListException | NullParameterException ignored) {

            }
        }
        return accountTypeBanks;
    }

    public void setAccountTypeBanks(Map<String, String> accountTypeBanks) {
        this.accountTypeBanks = accountTypeBanks;
    }

    public Map<String, String> getStatusAccountBanks() {
        if (statusAccountBanks == null) {
            statusAccountBanks = new TreeMap();
            try {
                List<StatusAccountBank> list = proxy.getStatusAccountBanks(new EJBRequest());
                for (StatusAccountBank s : list) {
                    statusAccountBanks.put(s.getDescription(), s.getId().toString());
                }
            } catch (GeneralException ex) {
                Logger.getLogger(ListAccountBankController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EmptyListException ignored) {

            } catch (NullParameterException ignored) {

            }
        }
        return statusAccountBanks;
    }

    public void setStatusAccountBanks(Map<String, String> statusAccountBanks) {
        this.statusAccountBanks = statusAccountBanks;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public void setLenguajeBean(LanguajeBean lenguajeBean) {
        this.lenguajeBean = lenguajeBean;
    }

    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }

    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("createAccountBank.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.ListAccountBankController.doRediret()");
        }
    }

    public void save() {
        try {
            AccountBank accountBank = null;
            if (selectedAccountBank.getId() != null) {
                accountBank = selectedAccountBank;
            }
            proxy.saveAccountBank(accountBank);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg.getString("accountBankSaveSuccesfull")));
        } catch (GeneralException | NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
