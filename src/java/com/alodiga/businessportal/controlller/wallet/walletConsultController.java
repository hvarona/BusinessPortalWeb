package com.alodiga.businessportal.controlller.wallet;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.alodiga.wallet.ws.APIAlodigaWalletProxy;
import com.alodiga.wallet.ws.BalanceHistoryResponse;
import com.alodiga.wallet.ws.TransactionListResponse;
import com.alodiga.wallet.ws.Maw_transaction;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author hvarona
 */
@ManagedBean(name = "walletConsultController")
@ViewScoped
public class walletConsultController {

    private Float balance = 0F;

    private Date startDate;

    private Date endDate;

    private List<Maw_transaction> resultList = null;

    private List<Maw_transaction> filteredResult = new ArrayList();

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean languageBean;

    private ResourceBundle msg;

    private final APIAlodigaWalletProxy alodigaWS = new APIAlodigaWalletProxy();

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

    public void setLanguageBean(LanguajeBean languageBean) {
        this.languageBean = languageBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getMaxDate() {
        return new Date();
    }

    public List<Maw_transaction> getResultList() {
        return resultList;
    }

    public void setResultList(List<Maw_transaction> resultList) {
        this.resultList = resultList;
    }

    public List<Maw_transaction> getFilteredResult() {
        return filteredResult;
    }

    public void setFilteredResult(List<Maw_transaction> filteredResult) {
        this.filteredResult = filteredResult;
    }

    private float truncAmount(float in) {
        return (float) Math.floor(in * 100) / 100;
    }

    public String formatCreationDate(Calendar CreationDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        return sdf.format(CreationDate.getTime());
    }
    

    public void reset() {
        FacesContext.getCurrentInstance().getViewRoot().getViewMap().clear();
    }

    public void doReport() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            TransactionListResponse response = alodigaWS.getTransactionsByBusinessIdBetweenDate(loginBean.getCurrentBusiness().getId(), sdf.format(startDate), sdf.format(endDate));
            switch (response.getCodigoRespuesta()) {
                case "00":
                    resultList = new ArrayList();
                    resultList.addAll(Arrays.asList(response.getTransactions()));
                    break;
                default:
                    resultList = new ArrayList();
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
            resultList = new ArrayList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
