package com.alodiga.primefaces.ultima.controller.accountbank;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.portal.business.commons.data.AccountBankData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.AccountBank;
import com.portal.business.commons.models.AccountTypeBank;
import com.portal.business.commons.models.BPBank;
import com.portal.business.commons.models.StatusAccountBank;
import com.portal.business.commons.models.Store;
import java.io.IOException;
import java.io.Serializable;
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

    private AccountBankData accountBankData = null;

    private List<AccountBank> accountBanks;
    private List<AccountBank> filteredAccountBank;

    private Map<String, String> banks = null;
    private BPBank bank = null;
    private Map<String, String> accountTypeBanks = null;
    private AccountTypeBank accountTypeBank = null;
    private Map<String, String> statusAccountBanks = null;
    private StatusAccountBank statusAccountBank = null;

    @ManagedProperty(value = "#{loginBean}")
    LoginBean loginBean;

    private ResourceBundle msg;

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean lenguajeBean;

    @PostConstruct
    public void init() {
        if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
        }
        try {
            accountBankData = new AccountBankData();
            //posList = posData.getPosList(loginBean.getCurrentBusiness());
            accountBanks = accountBankData.getAccountBankByCommerce(loginBean.getCurrentBusiness());
        } catch (GeneralException ex) {
            Logger.getLogger(ListAccountBankController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ignored) {

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

    public BPBank getBank() {
        return bank;
    }

    public void setBank(BPBank bank) {
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
                WsRequest request = new WsRequest();
                List<BPBank> list = accountBankData.getBanks(request);
                for (BPBank b : list) {
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
                WsRequest request = new WsRequest();
                List<AccountTypeBank> list = accountBankData.getAccountTypeBanks(request);
                for (AccountTypeBank a : list) {
                    accountTypeBanks.put(a.getDescription(), a.getId().toString());
                }
            } catch (GeneralException ex) {
                Logger.getLogger(ListAccountBankController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EmptyListException ignored) {

            } catch (NullParameterException ignored) {

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
                WsRequest request = new WsRequest();
                List<StatusAccountBank> list = accountBankData.getStatusAccountBanks(request);
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
            FacesContext.getCurrentInstance().getExternalContext().redirect("createPos.xhtml");
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
            accountBankData.saveAccountBank(accountBank);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg.getString("accountBankSaveSuccesfull")));
        } catch (GeneralException | NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
