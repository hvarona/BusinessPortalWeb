package com.alodiga.primefaces.ultima.controller.city.ipAddress;

import com.portal.business.commons.data.AccountData;
import com.portal.business.commons.data.PreferenceData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Account;
import com.portal.business.commons.models.AccountHasIpAddress;
import com.portal.business.commons.models.IpAddress;
import com.portal.business.commons.utils.QueryConstants;
import java.sql.Timestamp;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class ChangeIPAddressController {

    private String login;
    private Account account;
    private String ip;
    private String description;
    private AccountHasIpAddress selectedIpAddress;
    private List<AccountHasIpAddress> accountHasIpAddress = null;
    private boolean exist = false;
    private UtilsData utilsData = null;
    private AccountData accountData = null;
    private PreferenceData preferenceData = null;
    private String messages = null;
   
    
    @PostConstruct
    public void init() {
        utilsData = new UtilsData();
        accountData = new AccountData();
    }
   
    
    public String getMessages() {
        return messages;
    }
    
    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<AccountHasIpAddress> getAccountHasIpAddress() {
        return accountHasIpAddress;
    }

    public void setAccountHasIpAddress(List<AccountHasIpAddress> accountHasIpAddress) {
        this.accountHasIpAddress = accountHasIpAddress;
    }

    public AccountHasIpAddress getSelectedIpAddress() {
        return selectedIpAddress;
    }

    public void setSelectedIpAddress(AccountHasIpAddress selectedIpAddress) {
        this.selectedIpAddress = selectedIpAddress;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

   
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void search() {
        
        WsRequest request = new WsRequest();
        Map params = new HashMap<String, Object>();
        params.put(QueryConstants.PARAM_LOGIN, login);
        request.setParams(params);
        try {
            account = accountData.loadAccountByLogin(request);
            if (!account.getAccountHasIpAddress().isEmpty()) {
                exist = true;
                accountHasIpAddress = account.getAccountHasIpAddress();
            }
        } catch (RegisterNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cuenta no encontrada!", "Cuenta no encontrada"));
        } catch (NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Faltan Parametros!", "Faltan Parametros"));
        } catch (GeneralException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error General!", "Error General"));
        }

    }
    
    
    public void saveIPAddress() {
         boolean exist = false;
           List<AccountHasIpAddress> list = account.getAccountHasIpAddress();
        try {
            IpAddress ipAddress = utilsData.loadIpddress(ip);
            for (AccountHasIpAddress ip : list) {
                if (ip.getIpAddress().getIpAddress().equals(ipAddress.getIpAddress()) && ip.getEndingDate()==null) {
                    exist = true;
                     messages = "La direccion IP "+ ip + " ya se encuentra agregada a la cuenta " + account.getLogin() ;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(messages));
                }
            }
            if (!exist) {
                AccountHasIpAddress accountHasIp = new AccountHasIpAddress();
                accountHasIp.setAccount(account);
                accountHasIp.setIpAddress(ipAddress);
                accountHasIp.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
                list.add(accountHasIp);
                account.setAccountHasIpAddress(list);
                messages = "Se ha agregado la IP "+ ip + " a la cuenta " + account.getLogin() ;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(messages));
            }
        } catch (RegisterNotFoundException ex) {
            IpAddress ipAddress = new IpAddress();
            ipAddress.setIpAddress(ip);
            ipAddress.setDescription(description);
            AccountHasIpAddress accountHasIp = new AccountHasIpAddress();
            accountHasIp.setAccount(account);
            accountHasIp.setIpAddress(ipAddress);
            accountHasIp.setBeginningDate(new Timestamp(new java.util.Date().getTime()));
            list.add(accountHasIp);
            account.setAccountHasIpAddress(list); 
            messages = "Se ha agregado la IP "+ ip + " a la cuenta " + account.getLogin() ;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(messages));
        } catch (NullParameterException ex) {
            ex.printStackTrace();
        } catch (GeneralException ex) {
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error General!", "Error General"));
        } finally {
            try {
                account = accountData.saveAccount(account);
                search();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }



    public void reset() {
        exist = false;
        login = "";
        RequestContext.getCurrentInstance().reset("ChangeIpAddressForm:grid");
        RequestContext.getCurrentInstance().reset("ChangeIpAddressForm:grid1");
        RequestContext.getCurrentInstance().reset("ChangeIpAddressForm:grid2");
    }
    
    
    public void doDelete() {
        try {
            if (selectedIpAddress != null) {
                selectedIpAddress.setEndingDate(new Timestamp(new java.util.Date().getTime()));
                accountData.updateAccountHasIpAddress(selectedIpAddress);
                search();
                String messages = "La direccion Ip " + selectedIpAddress.getIpAddress() + " ha sido eliminada de la cuenta";
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(messages));
            }
        } catch (NullParameterException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.city.ipAddress.ListBlackListController.doDelete()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Faltan Parametros"));
        } catch (GeneralException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.city.ipAddress.ListBlackListController.doDelete()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Genaral!", "Error General"));
        }

    }
}
