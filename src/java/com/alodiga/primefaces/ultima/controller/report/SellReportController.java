package com.alodiga.primefaces.ultima.controller.report;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.portal.business.commons.data.BusinessData;
import com.portal.business.commons.data.BusinessSellData;
import com.portal.business.commons.data.PosData;
import com.portal.business.commons.data.StoreData;
import com.portal.business.commons.enumeration.BPTransactionStatus;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.BusinessSell;
import com.portal.business.commons.models.Pos;
import com.portal.business.commons.models.Store;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hvarona
 */
@ManagedBean(name = "sellReportController")
@ViewScoped
public class SellReportController {

    private Date startDate;

    private Date endDate;

    private List<Store> stores;

    private List<Pos> posList;

    private List<BusinessSell> resultList;

    private List<BusinessSell> filteredResult;

    private Store selectedStore = null;

    private Pos selectedPos = null;

    private String reportSingleId;

    private BusinessSellData businessSellData;

    private BusinessSell selectedSell;

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    ResourceBundle msg;

    @ManagedProperty(value = "#{languajeBean}")
    LanguajeBean lenguajeBean;

    @PostConstruct
    public void init() {
        try {
            if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
                msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
            } else {
                msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
            }
            businessSellData = new BusinessSellData();

            stores = new StoreData().getStores(loginBean.getCurrentBusiness());
            posList = new PosData().getPosList(loginBean.getCurrentBusiness());
        } catch (EmptyListException | GeneralException | NullParameterException ex) {
            ex.printStackTrace();
        }
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

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public List<Pos> getPosList() {
        return posList;
    }

    public void setPosList(List<Pos> posList) {
        this.posList = posList;
    }

    public List<BusinessSell> getResultList() {
        return resultList;
    }

    public void setResultList(List<BusinessSell> resultList) {
        this.resultList = resultList;
    }

    public List<BusinessSell> getFilteredResult() {
        return filteredResult;
    }

    public void setFilteredResult(List<BusinessSell> filteredResult) {
        this.filteredResult = filteredResult;
    }

    public Date getMaxDate() {
        return new Date();
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public void setLenguajeBean(LanguajeBean lenguajeBean) {
        this.lenguajeBean = lenguajeBean;
    }

    public BusinessSell getSelectedSell() {
        return selectedSell;
    }

    public void setSelectedSell(BusinessSell selectedSell) {
        this.selectedSell = selectedSell;
    }

    public String getReportSingleId() {
        return reportSingleId;
    }

    public void setReportSingleId(String reportSingleId) {
        this.reportSingleId = reportSingleId;
    }

    public String getOperationStatus(BPTransactionStatus status) {
        if (!msg.containsKey("TransactionStatus." + status.getCode())) {
            return status.getCode();
        }
        return msg.getString("TransactionStatus." + status.getCode());
    }

    public void doReport() {
        try {
            if (selectedPos != null) {
                resultList = businessSellData.getBusinessSales(selectedPos, startDate, endDate);
            } else if (selectedStore != null) {
                resultList = businessSellData.getBusinessSales(selectedStore, startDate, endDate);
            } else {
                resultList = businessSellData.getBusinessSales(loginBean.getCurrentBusiness(), startDate, endDate);
            }

        } catch (GeneralException ex) {
            Logger.getLogger(SellReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doSingleReport() {
        try {
            resultList = new ArrayList();
            resultList.add(businessSellData.getBusinessSell(loginBean.getCurrentBusiness(), reportSingleId));

        } catch (GeneralException ex) {
            Logger.getLogger(SellReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
