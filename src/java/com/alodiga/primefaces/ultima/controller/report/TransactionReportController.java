package com.alodiga.primefaces.ultima.controller.report;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.portal.business.commons.data.BusinessData;
import com.portal.business.commons.enumeration.BPTransactionStatus;
import com.portal.business.commons.enumeration.OperationType;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.models.BusinessTransaction;
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
@ManagedBean(name = "transactionReportController")
@ViewScoped
public class TransactionReportController {

    private Date startDate;

    private Date endDate;

    private List<OperationType> operationTypes;

    private OperationType selectedOperationType;

    private List<BusinessTransaction> resultList;

    private List<BusinessTransaction> filteredResult;

    private BusinessData businessData;

    private BusinessTransaction selectedTransaction;

    private String reportSingleId;

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    ResourceBundle msg;

    @ManagedProperty(value = "#{languajeBean}")
    LanguajeBean lenguajeBean;

    @PostConstruct
    public void init() {
        if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
        }
        businessData = new BusinessData();
        operationTypes = new ArrayList();
        for (OperationType operationType : OperationType.values()) {
            if (!operationType.equals(OperationType.WITHDRAW)) {
                operationTypes.add(operationType);
            }
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

    public List<OperationType> getOperationTypes() {
        return operationTypes;
    }

    public void setOperationTypes(List<OperationType> operationTypes) {
        this.operationTypes = operationTypes;
    }

    public OperationType getSelectedOperationType() {
        return selectedOperationType;
    }

    public void setSelectedOperationType(OperationType selectedOperationType) {
        this.selectedOperationType = selectedOperationType;
    }

    public List<BusinessTransaction> getResultList() {
        return resultList;
    }

    public void setResultList(List<BusinessTransaction> resultList) {
        this.resultList = resultList;
    }

    public List<BusinessTransaction> getFilteredResult() {
        return filteredResult;
    }

    public void setFilteredResult(List<BusinessTransaction> filteredResult) {
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

    public String getOperationTypeName(OperationType operationType) {
        return msg.getString("OperationType." + operationType.toString());

    }

    public String getOperationStatus(BPTransactionStatus status) {
        return msg.getString("TransactionStatus." + status.getCode());
    }

    public BusinessTransaction getSelectedTransaction() {
        return selectedTransaction;
    }

    public void setSelectedTransaction(BusinessTransaction selectedTransaction) {
        this.selectedTransaction = selectedTransaction;
    }

    public String getReportSingleId() {
        return reportSingleId;
    }

    public void setReportSingleId(String reportSingleId) {
        this.reportSingleId = reportSingleId;
    }

    public void doReport() {
        try {
            resultList = businessData.getBusinessTransactions(loginBean.getCurrentBusiness(), startDate, endDate, selectedOperationType);
        } catch (EmptyListException ex) {
            resultList = new ArrayList();
        } catch (GeneralException ex) {
            Logger.getLogger(TransactionReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doSingleReport() {
        try {
            resultList = new ArrayList();
            resultList.add(businessData.getBusinessTranasction(loginBean.getCurrentBusiness(), reportSingleId));

        } catch (GeneralException ex) {
            Logger.getLogger(SellReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
