package com.alodiga.primefaces.ultima.controller.store;

import com.portal.business.commons.data.PreferenceData;
import com.portal.business.commons.data.StoreData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.managers.PreferenceManager;
import com.portal.business.commons.models.PreferenceField;
import com.portal.business.commons.models.PreferenceFieldEnum;
import com.portal.business.commons.models.PreferenceValue;
import com.portal.business.commons.models.Store;
import com.portal.business.commons.models.StoreSetting;
import com.portal.business.commons.utils.QueryConstants;
import java.util.ArrayList;
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
public class StoreSettingController {

    private String login;
    private Store store;
    private String storeName;
    private Double max_remittance = new Double(0);
    private Double max_dialy = new Double(0);
    private Double max_mounthly = new Double(0);
    private Double max_yearly = new Double(0);
//    private Double max_dialy_remittent = new Double(0);
//    private Double max_mounthly_remittent = new Double(0);
//    private Double max_yearly_remittent = new Double(0);
//    private Double max_dialy_receiver = new Double(0);
//    private Double max_mounthly_receiver = new Double(0);
//    private Double max_yearly_receiver = new Double(0);
    private Long MAX_AMOUNT_DAILY_PER_STORE;
    private Long MAX_AMOUNT_MONTHLY_PER_STORE;
    private Long MAX_AMOUNT_YEARLY_PER_STORE;
    private Long MAX_AMOUNT_PER_REMITTANCE;
//    private Long MAX_AMOUNT_DAILY_PER_REMITTENT;
//    private Long MAX_AMOUNT_MONTHLY_PER_REMITTENT;
//    private Long MAX_AMOUNT_YEARLY_PER_REMITTENT;
//    private Long MAX_AMOUNT_DAILY_PER_RECEIVER;
//    private Long MAX_AMOUNT_MONTHLY_PER_RECEIVER;
//    private Long MAX_AMOUNT_YEARLY_PER_RECEIVER;
    private boolean exist = false;
    private UtilsData utilsData = null;
    private StoreData storeData = null;
    private PreferenceData preferenceData = null;
    private String messages = null;
   
    
    @PostConstruct
    public void init() {
        utilsData = new UtilsData();
        storeData = new StoreData();
        preferenceData =new PreferenceData();
        setData();
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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
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

//    public Double getMax_dialy_remittent() {
//        return max_dialy_remittent;
//    }
//
//    public void setMax_dialy_remittent(Double max_dialy_remittent) {
//        this.max_dialy_remittent = max_dialy_remittent;
//    }
//
//    public Double getMax_mounthly_remittent() {
//        return max_mounthly_remittent;
//    }
//
//    public void setMax_mounthly_remittent(Double max_mounthly_remittent) {
//        this.max_mounthly_remittent = max_mounthly_remittent;
//    }
//
//    public Double getMax_yearly_remittent() {
//        return max_yearly_remittent;
//    }
//
//    public void setMax_yearly_remittent(Double max_yearly_remittent) {
//        this.max_yearly_remittent = max_yearly_remittent;
//    }
//
//    public Double getMax_dialy_receiver() {
//        return max_dialy_receiver;
//    }
//
//    public void setMax_dialy_receiver(Double max_dialy_receiver) {
//        this.max_dialy_receiver = max_dialy_receiver;
//    }
//
//    public Double getMax_mounthly_receiver() {
//        return max_mounthly_receiver;
//    }
//
//    public void setMax_mounthly_receiver(Double max_mounthly_receiver) {
//        this.max_mounthly_receiver = max_mounthly_receiver;
//    }
//
//    public Double getMax_yearly_receiver() {
//        return max_yearly_receiver;
//    }
//
//    public void setMax_yearly_receiver(Double max_yearly_receiver) {
//        this.max_yearly_receiver = max_yearly_receiver;
//    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    
    
    public void search() {
        
        WsRequest request = new WsRequest();
        Map params = new HashMap<String, Object>();
        params.put(QueryConstants.PARAM_LOGIN, login);
        request.setParams(params);
        try {
            store = storeData.loadStoreByLogin(request);
            storeName = store.getFirstName()+ " "+ store.getLastName();
            loadPreferences(store.getId());
        } catch (RegisterNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Tienda no encontrada!", "Tienda no encontrada"));
        } catch (NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Faltan Parametros!", "Faltan Parametros"));
        } catch (GeneralException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error General!", "Error General"));
        }

    }
    
     private void loadPreferences(Long storeId) {
        try {
            setData();
            WsRequest request = new WsRequest();
            List<PreferenceField> fields = preferenceData.getPreferenceFields(request);
            for (PreferenceField field : fields) {
                try {
                    StoreSetting pValue = storeData.loadActiveStoreSettingsByStoreIdAndFieldId(field.getId(), storeId);

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
//                    if (field.getId().equals(MAX_AMOUNT_DAILY_PER_REMITTENT)) {
//                        max_dialy_remittent = Double.parseDouble(pValue.getValue());
//                    }
//
//                    if (field.getId().equals(MAX_AMOUNT_MONTHLY_PER_REMITTENT)) {
//                        max_mounthly_remittent = Double.parseDouble(pValue.getValue());
//                    }
//
//                    if (field.getId().equals(MAX_AMOUNT_YEARLY_PER_REMITTENT)) {
//                        max_yearly_remittent = Double.parseDouble(pValue.getValue());
//                    }
//                    if (field.getId().equals(MAX_AMOUNT_DAILY_PER_RECEIVER)) {
//                        max_dialy_receiver = Double.parseDouble(pValue.getValue());
//                    }
//                    if (field.getId().equals(MAX_AMOUNT_MONTHLY_PER_RECEIVER)) {
//                        max_mounthly_receiver = Double.parseDouble(pValue.getValue());
//                    }
//                    if (field.getId().equals(MAX_AMOUNT_YEARLY_PER_RECEIVER)) {
//                        max_yearly_receiver = Double.parseDouble(pValue.getValue());
//                    }
                    exist = true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        }

    }
    
     public void setData() {
        MAX_AMOUNT_PER_REMITTANCE = PreferenceFieldEnum.MAX_AMOUNT_PER_REMITTANCE.getId();
        MAX_AMOUNT_DAILY_PER_STORE = PreferenceFieldEnum.MAX_AMOUNT_DAILY_PER_STORE.getId();
        MAX_AMOUNT_MONTHLY_PER_STORE = PreferenceFieldEnum.MAX_AMOUNT_MONTHLY_PER_STORE.getId();
        MAX_AMOUNT_YEARLY_PER_STORE = PreferenceFieldEnum.MAX_AMOUNT_YEARLY_PER_STORE.getId();
//        MAX_AMOUNT_DAILY_PER_REMITTENT = PreferenceFieldEnum.MAX_AMOUNT_DAILY_PER_REMITTENT.getId();
//        MAX_AMOUNT_MONTHLY_PER_REMITTENT = PreferenceFieldEnum.MAX_AMOUNT_MONTHLY_PER_REMITTENT.getId();
//        MAX_AMOUNT_YEARLY_PER_REMITTENT = PreferenceFieldEnum.MAX_AMOUNT_YEARLY_PER_REMITTENT.getId();
//        MAX_AMOUNT_DAILY_PER_RECEIVER = PreferenceFieldEnum.MAX_AMOUNT_DAILY_PER_RECEIVER.getId();
//        MAX_AMOUNT_MONTHLY_PER_RECEIVER = PreferenceFieldEnum.MAX_AMOUNT_MONTHLY_PER_RECEIVER.getId();
//        MAX_AMOUNT_YEARLY_PER_RECEIVER = PreferenceFieldEnum.MAX_AMOUNT_YEARLY_PER_RECEIVER.getId();
    }
    
    public void saveStoreSetting() {

        try {

            WsRequest _request = new WsRequest();
            List<StoreSetting> preferenceValues = new ArrayList<StoreSetting>();
            preferenceValues.add(createStoreSetting(new PreferenceField(MAX_AMOUNT_PER_REMITTANCE), max_remittance.toString(), store));
            preferenceValues.add(createStoreSetting(new PreferenceField(MAX_AMOUNT_DAILY_PER_STORE), max_dialy.toString(), store));
            preferenceValues.add(createStoreSetting(new PreferenceField(MAX_AMOUNT_MONTHLY_PER_STORE), max_mounthly.toString(), store));
            preferenceValues.add(createStoreSetting(new PreferenceField(MAX_AMOUNT_YEARLY_PER_STORE), max_yearly.toString(), store));
            _request.setParam(preferenceValues);
            storeData.saveStoreSettings(_request);
            PreferenceManager preferenceManager = PreferenceManager.getInstance();
            preferenceManager.refresh();
            messages = "Se ha actualizado la configuraciòn de la tienda " + storeName;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(messages));
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error General!", "Error General"));
        }
    }

    private StoreSetting createStoreSetting(PreferenceField field, String value, Store store) {
        StoreSetting storeSetting = new StoreSetting();
        storeSetting.setPreferenceField(field);
        storeSetting.setValue(value);
        storeSetting.setStore(store);
        return storeSetting;
    }

    public void reset() {
        exist = false;
        login = "";
        max_remittance = 0d;
        max_dialy = 0d;
        max_mounthly = 0d;
        max_yearly = 0d;
//        max_dialy_remittent = 0d;
//        max_mounthly_remittent = 0d;
//        max_yearly_remittent = 0d;
//        max_dialy_receiver = 0d;
//        max_mounthly_receiver = 0d;
//        max_yearly_receiver = 0d;
        storeName = "";
        RequestContext.getCurrentInstance().reset("StoreSettingForm:grid");
        RequestContext.getCurrentInstance().reset("StoreSettingForm:grid1");
    }
    

}
