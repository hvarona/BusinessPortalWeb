package com.alodiga.primefaces.ultima.controller.enterprise;

import com.portal.business.commons.data.PreferenceData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.managers.PreferenceManager;
import com.portal.business.commons.models.Enterprise;
import com.portal.business.commons.models.PreferenceField;
import com.portal.business.commons.models.PreferenceFieldEnum;
import com.portal.business.commons.models.PreferenceValue;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import java.util.List;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class PreferenceValueController {

    private Enterprise enterprise;
    private Double max_remittance = new Double(0);
    private Double max_dialy = new Double(0);
    private Double max_mounthly = new Double(0);
    private Double max_yearly = new Double(0);
    private Double max_dialy_remittent = new Double(0);
    private Double max_mounthly_remittent = new Double(0);
    private Double max_yearly_remittent = new Double(0);
    private Double max_dialy_receiver = new Double(0);
    private Double max_mounthly_receiver = new Double(0);
    private Double max_yearly_receiver = new Double(0);
    private Double max_time = new Double(0);
    private Double max_session = new Double(0);
    private Double stipulated_period = new Double(0);        
    private boolean enabled_transaction = false;
    private Long MAX_AMOUNT_DAILY_PER_STORE;
    private Long MAX_AMOUNT_MONTHLY_PER_STORE;
    private Long MAX_AMOUNT_YEARLY_PER_STORE;
    private Long MAX_AMOUNT_PER_REMITTANCE;
    private Long MAX_AMOUNT_DAILY_PER_REMITTENT;
    private Long MAX_AMOUNT_MONTHLY_PER_REMITTENT;
    private Long MAX_AMOUNT_YEARLY_PER_REMITTENT;
    private Long MAX_AMOUNT_DAILY_PER_RECEIVER;
    private Long MAX_AMOUNT_MONTHLY_PER_RECEIVER;
    private Long MAX_AMOUNT_YEARLY_PER_RECEIVER;
    private Long TIMEOUT_INACTIVE_SESSION;
    private Long MAX_WRONG_LOGIN_INTENT_NUMBER;
    private Long DISABLED_TRANSACTION;
    private Long STIPULATED_PERIOD;
    private UtilsData utilsData = null;
    private PreferenceData preferenceData = null;
    private String messages = null;
   
    
    @PostConstruct
    public void init() {
        utilsData = new UtilsData();
        preferenceData =new PreferenceData();
        WsRequest request = new WsRequest();
        request.setParam(Enterprise.ALODIGA_USA);
        try {
            enterprise = utilsData.loadEnterprise(request);
            loadPreferences();
        } catch (RegisterNotFoundException ex) {
        } catch (NullParameterException ex) {
        } catch (GeneralException ex) {
            ex.printStackTrace();
        }
        setData();
    }
   
    
    public String getMessages() {
        return messages;
    }
    
    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Double getMax_remittance() {
        return max_remittance;
    }

    public void setMax_remittance(Double max_remittance) {
        this.max_remittance = max_remittance;
    }

    public Double getMax_dialy() {
        return max_dialy;
    }

    public void setMax_dialy(Double max_dialy) {
        this.max_dialy = max_dialy;
    }

    public Double getMax_mounthly() {
        return max_mounthly;
    }

    public void setMax_mounthly(Double max_mounthly) {
        this.max_mounthly = max_mounthly;
    }

    public Double getMax_yearly() {
        return max_yearly;
    }

    public void setMax_yearly(Double max_yearly) {
        this.max_yearly = max_yearly;
    }

    public Double getMax_dialy_remittent() {
        return max_dialy_remittent;
    }

    public void setMax_dialy_remittent(Double max_dialy_remittent) {
        this.max_dialy_remittent = max_dialy_remittent;
    }

    public Double getMax_mounthly_remittent() {
        return max_mounthly_remittent;
    }

    public void setMax_mounthly_remittent(Double max_mounthly_remittent) {
        this.max_mounthly_remittent = max_mounthly_remittent;
    }

    public Double getMax_yearly_remittent() {
        return max_yearly_remittent;
    }

    public void setMax_yearly_remittent(Double max_yearly_remittent) {
        this.max_yearly_remittent = max_yearly_remittent;
    }

    public Double getMax_dialy_receiver() {
        return max_dialy_receiver;
    }

    public void setMax_dialy_receiver(Double max_dialy_receiver) {
        this.max_dialy_receiver = max_dialy_receiver;
    }

    public Double getMax_mounthly_receiver() {
        return max_mounthly_receiver;
    }

    public void setMax_mounthly_receiver(Double max_mounthly_receiver) {
        this.max_mounthly_receiver = max_mounthly_receiver;
    }

    public Double getMax_yearly_receiver() {
        return max_yearly_receiver;
    }

    public void setMax_yearly_receiver(Double max_yearly_receiver) {
        this.max_yearly_receiver = max_yearly_receiver;
    }

    public Double getMax_time() {
        return max_time;
    }

    public void setMax_time(Double max_time) {
        this.max_time = max_time;
    }

    public Double getMax_session() {
        return max_session;
    }

    public void setMax_session(Double max_session) {
        this.max_session = max_session;
    }

    public Double getStipulated_period() {
        return stipulated_period;
    }

    public void setStipulated_period(Double stipulated_period) {
        this.stipulated_period = stipulated_period;
    }

    public boolean isEnabled_transaction() {
        return enabled_transaction;
    }

    public void setEnabled_transaction(boolean enabled_transaction) {
        this.enabled_transaction = enabled_transaction;
    }


    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    
     private void loadPreferences() {
        try {
            setData();
            WsRequest request = new WsRequest();
            List<PreferenceField> fields = preferenceData.getPreferenceFields(request);
            for (PreferenceField field : fields) {
                try {
                    PreferenceValue pValue = preferenceData.loadActivePreferenceValuesByEnterpriseIdAndFieldId(enterprise.getId(),field.getId());
                    if (field.getId().equals(MAX_WRONG_LOGIN_INTENT_NUMBER)) {
                        max_session = Double.parseDouble(pValue.getValue());
                    }
                    if (field.getId().equals(TIMEOUT_INACTIVE_SESSION)) {
                         max_time = Double.parseDouble(pValue.getValue());
                    }
                    if (field.getId().equals(DISABLED_TRANSACTION)) {
                       boolean checked = Integer.parseInt(pValue.getValue()) == 1 ? true : false;
                       enabled_transaction = checked;
                    }
                    if (field.getId().equals(STIPULATED_PERIOD)) {
                        stipulated_period = Double.parseDouble(pValue.getValue());
                    }
                    if (field.getId().equals(MAX_AMOUNT_PER_REMITTANCE)) {
                        max_remittance = Double.parseDouble(pValue.getValue());
                    }
                    if (field.getId().equals(MAX_AMOUNT_DAILY_PER_STORE)) {
                        max_dialy = Double.parseDouble(pValue.getValue());
                    }
                    if (field.getId().equals(MAX_AMOUNT_MONTHLY_PER_STORE)) {
                        max_mounthly = Double.parseDouble(pValue.getValue());
                    }
                    if (field.getId().equals(MAX_AMOUNT_YEARLY_PER_STORE)) {
                        max_yearly = Double.parseDouble(pValue.getValue());
                    }
                    if (field.getId().equals(MAX_AMOUNT_DAILY_PER_REMITTENT)) {
                        max_dialy_remittent = Double.parseDouble(pValue.getValue());
                    }

                    if (field.getId().equals(MAX_AMOUNT_MONTHLY_PER_REMITTENT)) {
                        max_mounthly_remittent = Double.parseDouble(pValue.getValue());
                    }

                    if (field.getId().equals(MAX_AMOUNT_YEARLY_PER_REMITTENT)) {
                        max_yearly_remittent = Double.parseDouble(pValue.getValue());
                    }
                    if (field.getId().equals(MAX_AMOUNT_DAILY_PER_RECEIVER)) {
                        max_dialy_receiver = Double.parseDouble(pValue.getValue());
                    }
                    if (field.getId().equals(MAX_AMOUNT_MONTHLY_PER_RECEIVER)) {
                        max_mounthly_receiver = Double.parseDouble(pValue.getValue());
                    }
                    if (field.getId().equals(MAX_AMOUNT_YEARLY_PER_RECEIVER)) {
                        max_yearly_receiver = Double.parseDouble(pValue.getValue());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error General!", "Error General"));
        }

    }
    
    public void setData() {
        MAX_AMOUNT_PER_REMITTANCE = PreferenceFieldEnum.MAX_AMOUNT_PER_REMITTANCE.getId();
        MAX_AMOUNT_DAILY_PER_STORE = PreferenceFieldEnum.MAX_AMOUNT_DAILY_PER_STORE.getId();
        MAX_AMOUNT_MONTHLY_PER_STORE = PreferenceFieldEnum.MAX_AMOUNT_MONTHLY_PER_STORE.getId();
        MAX_AMOUNT_YEARLY_PER_STORE = PreferenceFieldEnum.MAX_AMOUNT_YEARLY_PER_STORE.getId();
        MAX_AMOUNT_DAILY_PER_REMITTENT = PreferenceFieldEnum.MAX_AMOUNT_DAILY_PER_REMITTENT.getId();
        MAX_AMOUNT_MONTHLY_PER_REMITTENT = PreferenceFieldEnum.MAX_AMOUNT_MONTHLY_PER_REMITTENT.getId();
        MAX_AMOUNT_YEARLY_PER_REMITTENT = PreferenceFieldEnum.MAX_AMOUNT_YEARLY_PER_REMITTENT.getId();
        MAX_AMOUNT_DAILY_PER_RECEIVER = PreferenceFieldEnum.MAX_AMOUNT_DAILY_PER_RECEIVER.getId();
        MAX_AMOUNT_MONTHLY_PER_RECEIVER = PreferenceFieldEnum.MAX_AMOUNT_MONTHLY_PER_RECEIVER.getId();
        MAX_AMOUNT_YEARLY_PER_RECEIVER = PreferenceFieldEnum.MAX_AMOUNT_YEARLY_PER_RECEIVER.getId();
        TIMEOUT_INACTIVE_SESSION = PreferenceFieldEnum.TIMEOUT_INACTIVE_SESSION.getId();
        MAX_WRONG_LOGIN_INTENT_NUMBER = PreferenceFieldEnum.MAX_WRONG_LOGIN_INTENT_NUMBER.getId();
        DISABLED_TRANSACTION = PreferenceFieldEnum.DISABLED_TRANSACTION.getId();
        STIPULATED_PERIOD = PreferenceFieldEnum.STIPULATED_PERIOD.getId();
    }

    public void savePreferenceValue() {

        try {

            WsRequest _request = new WsRequest();
            List<PreferenceValue> preferenceValues = new ArrayList<PreferenceValue>();
            preferenceValues.add(createPreferenceValue(new PreferenceField(MAX_AMOUNT_PER_REMITTANCE), max_remittance.toString(), enterprise));
            preferenceValues.add(createPreferenceValue(new PreferenceField(MAX_AMOUNT_DAILY_PER_STORE), max_dialy.toString(), enterprise));
            preferenceValues.add(createPreferenceValue(new PreferenceField(MAX_AMOUNT_MONTHLY_PER_STORE), max_mounthly.toString(), enterprise));
            preferenceValues.add(createPreferenceValue(new PreferenceField(MAX_AMOUNT_YEARLY_PER_STORE), max_yearly.toString(), enterprise));
            
            preferenceValues.add(createPreferenceValue(new PreferenceField(MAX_WRONG_LOGIN_INTENT_NUMBER), max_session.toString(), enterprise));
            preferenceValues.add(createPreferenceValue(new PreferenceField(TIMEOUT_INACTIVE_SESSION), max_time.toString(), enterprise));
            preferenceValues.add(createPreferenceValue(new PreferenceField(DISABLED_TRANSACTION), "" + changeEnable(enabled_transaction), enterprise));
            preferenceValues.add(createPreferenceValue(new PreferenceField(STIPULATED_PERIOD), "" + stipulated_period.toString(), enterprise));
            _request.setParam(preferenceValues);
            preferenceData.savePreferenceValues(_request);
            PreferenceManager preferenceManager = PreferenceManager.getInstance();
            preferenceManager.refresh();
            messages = "Se ha actualizado la configuraciòn de las remesas ";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(messages));
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error General!", "Error General"));
        }
    }
    
    public String changeEnable(boolean enabled_transaction){
        if(enabled_transaction)
            return "1";
        else
            return "0";
    }

    private PreferenceValue createPreferenceValue(PreferenceField field, String value, Enterprise enterprise) {
        PreferenceValue preferenceValue = new PreferenceValue();
        preferenceValue.setPreferenceField(field);
        preferenceValue.setValue(value);
        preferenceValue.setEnterprise(enterprise);
        return preferenceValue;
    }

    public void reset() {
        loadPreferences();
        RequestContext.getCurrentInstance().reset("PreferenceValueForm:grid");
    }
    

}
