package com.alodiga.primefaces.ultima.controller.enterprise;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.portal.business.commons.data.PreferenceData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.managers.PreferenceManager;
import com.portal.business.commons.models.BPPreference;
import com.portal.business.commons.models.BPPreferenceField;
import com.portal.business.commons.models.BPPreferenceValue;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class PreferenceValueController {

    private List<BPPreference> preferences;

    private BPPreference selectedPreference;
    private List<BPPreferenceField> selectedFields;

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean languageBean;

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private PreferenceData preferenceData = null;
    private String messages = null;

    @PostConstruct
    public void init() {
        try {
            preferenceData = new PreferenceData();
            preferences = preferenceData.getPreferences();
        } catch (GeneralException | RegisterNotFoundException ex) {
            Logger.getLogger(PreferenceValueController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ignored) {

        }
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public LanguajeBean getLanguageBean() {
        return languageBean;
    }

    public void setLanguageBean(LanguajeBean languageBean) {
        this.languageBean = languageBean;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public List<BPPreference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<BPPreference> preferences) {
        this.preferences = preferences;
    }

    public BPPreference getSelectedPreference() {
        return selectedPreference;
    }

    public void setSelectedPreference(BPPreference selectedPreference) {
        this.selectedPreference = selectedPreference;
    }

    public void reloadSelectedFields() {
        try {
            if (selectedPreference != null) {
                this.selectedFields = selectedPreference.getPreferenceFields();
                for (BPPreferenceField field : selectedFields) {
                    field.setCurrentLanguage(languageBean.getLanguage());
                    try {
                        field.setCurrentValue(preferenceData.loadActivePreferenceValuesByBusinessAndField(field, loginBean.getCurrentBusiness()).getValue());
                    } catch (GeneralException | RegisterNotFoundException | NullParameterException ex) {
                        field.setCurrentValue("");
                    }
                }
            } else {
                this.selectedFields = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<BPPreferenceField> getSelectedFields() {
        return selectedFields;
    }

    public void setSelectedFields(List<BPPreferenceField> selectedFields) {
        this.selectedFields = selectedFields;
    }

    public void savePreferenceValue() {
        ResourceBundle bundle = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(languageBean.getLanguaje()));
        try {

            List<BPPreferenceValue> values = new ArrayList();
            for (BPPreferenceField field : selectedFields) {
                if (field.getCurrentValue() != null && !field.getCurrentValue().isEmpty()) {
                    BPPreferenceValue value = new BPPreferenceValue();
                    value.setBeginningDate(new Date());
                    value.setBusiness(loginBean.getCurrentBusiness());
                    value.setPreferenceField(field);
                    value.setValue(field.getCurrentValue());
                    values.add(value);
                }
            }
            preferenceData.savePreferenceValues(values);
            PreferenceManager.refresh();
            messages = bundle.getString("savedPreference");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(messages));
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            bundle.getString("error.general"),
                            bundle.getString("error.general")));
        }
    }
}
