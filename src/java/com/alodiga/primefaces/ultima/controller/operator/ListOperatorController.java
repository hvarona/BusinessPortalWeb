package com.alodiga.primefaces.ultima.controller.operator;

import com.alodiga.remittance.beans.LoginBean;
import com.portal.business.commons.data.OperatorData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.Language;
import com.portal.business.commons.models.Operator;
import com.portal.business.commons.models.Profile;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
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
@ManagedBean(name = "bpListOperatorController")
@ViewScoped
public class ListOperatorController implements Serializable {

    private Operator selectedOperator;

    private OperatorData operatorData = null;

    private List<Operator> operatorList;
    private List<Operator> filteredOperator;

    private Map<String, String> languages = new TreeMap();
    private Map<String, String> profiles = new TreeMap();

    private Language language = null;
    private Profile profile = null;

    @ManagedProperty(value = "#{loginBean}")
    LoginBean loginBean;

    @PostConstruct
    public void init() {
        try {
            operatorData = new OperatorData();
            operatorList = operatorData.getOperatorList(loginBean.getCurrentCommerce());
        } catch (EmptyListException | GeneralException ex) {
            Logger.getLogger(ListOperatorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Operator getSelectedOperator() {
        return selectedOperator;
    }

    public void setSelectedOperator(Operator selectedOperator) {
        this.selectedOperator = selectedOperator;
    }

    public List<Operator> getOperatorList() {
        return operatorList;
    }

    public void setOperatorList(List<Operator> operatorList) {
        this.operatorList = operatorList;
    }

    public List<Operator> getFilteredOperator() {
        return filteredOperator;
    }

    public void setFilteredOperator(List<Operator> filteredOperator) {
        this.filteredOperator = filteredOperator;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public Map<String, String> getLanguages() {
        return languages;
    }

    public void setLanguages(Map<String, String> languages) {
        this.languages = languages;
    }

    public Map<String, String> getProfiles() {
        return profiles;
    }

    public void setProfiles(Map<String, String> profiles) {
        this.profiles = profiles;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }

    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("createOperator.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.ListOperatorController.doRediret()");
        }
    }

    public void save() {
        try {
            Operator operator = null;
            if (selectedOperator.getId() != null) {
                operator = selectedOperator;
            }
            operatorData.saveOperator(operator);
        } catch (GeneralException | NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        }
    }

}
