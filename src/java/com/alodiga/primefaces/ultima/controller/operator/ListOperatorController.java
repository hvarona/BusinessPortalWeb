package com.alodiga.primefaces.ultima.controller.operator;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.portal.business.commons.data.OperatorData;
import com.portal.business.commons.data.PosData;
import com.portal.business.commons.data.StoreData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.BPLanguage;
import com.portal.business.commons.models.Operator;
import com.portal.business.commons.models.BPPermission;
import com.portal.business.commons.models.BPPermissionHasProfile;
import com.portal.business.commons.models.Pos;
import com.portal.business.commons.models.BPProfile;
import com.portal.business.commons.models.BPProfileData;
import com.portal.business.commons.models.Store;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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

    private BPLanguage language = null;
    private BPProfile profile = null;

    private List<Store> stores;

    private List<Pos> posList = new ArrayList();

    private Store selectedStore = null;

    private Pos selectPos = null;

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean lenguajeBean;

    private ResourceBundle msg;

    private List<BPPermission> availablePermissions;
    private List<BPPermission> includedPermissions;

    @PostConstruct
    public void init() {
        if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
        }
        try {
            operatorData = new OperatorData();
            operatorList = operatorData.getOperatorList(loginBean.getCurrentBusiness());
            stores = new StoreData().getStores(loginBean.getCurrentBusiness());
        } catch (GeneralException ex) {
            Logger.getLogger(ListOperatorController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ignored) {

        } catch (NullParameterException ex) {
            Logger.getLogger(ListOperatorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Operator getSelectedOperator() {
        return selectedOperator;
    }

    public void setSelectedOperator(Operator selectedOperator) {
        this.selectedOperator = selectedOperator;
        getPermissions();
        this.selectedStore = selectedOperator.getStore();
        this.selectPos = selectedOperator.getPos();
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
        if (languages == null || languages.isEmpty()) {
            languages = new TreeMap();
            try {
                List<BPLanguage> languageList = operatorData.getLanguageList();
                for (BPLanguage singleLangugage : languageList) {
                    languages.put(singleLangugage.getDescription(), singleLangugage.getId().toString());
                }
            } catch (EmptyListException | GeneralException ex) {
                Logger.getLogger(ListOperatorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return languages;
    }

    public void setLanguages(Map<String, String> languages) {
        this.languages = languages;
    }

    public Map<String, String> getProfiles() {
        if (profiles == null || profiles.isEmpty()) {
            profiles = new TreeMap();
            try {
                List<BPProfile> profileList = operatorData.getProfileList();
                for (BPProfile singleProfile : profileList) {
                    String name = singleProfile.getName();
                    for (BPProfileData data : singleProfile.getProfileData()) {
                        if (new Locale(data.getLanguage().getIso()).getLanguage().equals(msg.getLocale().getLanguage())) {
                            name = data.getAlias();
                        }
                    }
                    profiles.put(name, singleProfile.getId().toString());
                }

            } catch (EmptyListException | GeneralException ex) {
                Logger.getLogger(ListOperatorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return profiles;
    }

    public void setProfiles(Map<String, String> profiles) {
        this.profiles = profiles;
    }

    public BPProfile getProfile() {
        return profile;
    }

    public void setProfile(BPProfile profile) {
        this.profile = profile;
    }

    public BPLanguage getLanguage() {
        return language;
    }

    public void setLanguage(BPLanguage language) {
        this.language = language;
    }

    public void getPermissions() {
        availablePermissions = new ArrayList();
        includedPermissions = new ArrayList();
        if (selectedOperator == null || selectedOperator.getProfile() == null) {
            return;
        }
        List<BPPermissionHasProfile> phps = selectedOperator.getProfile().getPermissionHasProfiles();
        for (BPPermissionHasProfile php : phps) {
            availablePermissions.add(php.getPermission());
        }
        for (BPPermission perm : availablePermissions) {
            if (!selectedOperator.getExcludedPermission().contains(perm)) {
                includedPermissions.add(perm);
            }
        }
    }

    public List<BPPermission> getAvailablePermissions() {
        return availablePermissions;
    }

    public void setAvailablePermissions(List<BPPermission> availablePermissions) {
        this.availablePermissions = availablePermissions;
    }

    public List<BPPermission> getIncludedPermissions() {
        return includedPermissions;
    }

    public void setIncludedPermissions(List<BPPermission> includedPermissions) {
        this.includedPermissions = includedPermissions;
    }

    public void setLenguajeBean(LanguajeBean lenguajeBean) {
        this.lenguajeBean = lenguajeBean;
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

    public Store getSelectedStore() {
        return selectedStore;
    }

    public void setSelectedStore(Store selectedStore) {
        this.posList = new ArrayList();
        this.selectedStore = selectedStore;
        this.selectPos = null;
        try {
            this.posList = new PosData().getPosByStore(selectedStore);
        } catch (EmptyListException ex) {
            Logger.getLogger(OperatorController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(OperatorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Pos getSelectPos() {
        return selectPos;
    }

    public void setSelectPos(Pos selectPos) {
        this.selectPos = selectPos;
    }

    public void changeEnable(Operator operator) {
        try {
            operatorData.saveOperator(operator);
            if (operator.getEnabled()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg.getString("operatorEnabled")));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg.getString("operatorDisabled")));
            }
        } catch (NullParameterException | GeneralException ex) {
            Logger.getLogger(ListOperatorController.class.getName()).log(Level.SEVERE, null, ex);
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
            } else {
                throw new NullParameterException("ID null");
            }

            List<BPPermission> excludedPermission = new ArrayList();

            for (BPPermission perm : availablePermissions) {
                if (!includedPermissions.contains(perm)) {
                    excludedPermission.add(perm);
                }
            }
            operator.setExcludedPermission(excludedPermission);
            operator.setStore(selectedStore);
            operator.setPos(selectPos);

            operatorData.saveOperator(operator);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg.getString("operatorSaveSuccesfull")));
        } catch (GeneralException | NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", msg.getString("errorGeneral")));
        }
    }

}
