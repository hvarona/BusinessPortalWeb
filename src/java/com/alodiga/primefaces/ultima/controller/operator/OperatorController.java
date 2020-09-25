package com.alodiga.primefaces.ultima.controller.operator;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.portal.business.commons.data.OperatorData;
import com.portal.business.commons.data.PosData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.BPLanguage;
import com.portal.business.commons.models.Operator;
import com.portal.business.commons.models.BPPermission;
import com.portal.business.commons.models.Pos;
import com.portal.business.commons.models.BPProfile;
import com.portal.business.commons.models.Store;
import com.portal.business.commons.utils.Encoder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author hvarona
 */
@ManagedBean
@ViewScoped
public class OperatorController {

    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private boolean receiveNotification;
    private String login;
    private String password;
    private String phoneNumber;
    private BPProfile profile;
    private BPLanguage language;

    private OperatorData operatorData = null;

    private Store selectedStore = null;

    private Pos selectPos = null;

    @ManagedProperty(value = "#{loginBean}")
    LoginBean loginBean;

    @ManagedProperty(value = "#{operatorPermission}")
    operatorPermissionController operatorPermissionController;

    List<BPPermission> includedPermissions;

    private String messages = null;

    private ResourceBundle msg;

    private List<Pos> posList = new ArrayList();

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean lenguajeBean;

    @PostConstruct
    public void init() {
        if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
        }
        operatorData = new OperatorData();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isReceiveNotification() {
        return receiveNotification;
    }

    public void setReceiveNotification(boolean receiveNotification) {
        this.receiveNotification = receiveNotification;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public List<BPPermission> getIncludedPermissions() {
        return includedPermissions;
    }

    public void setIncludedPermissions(List<BPPermission> includedPermissions) {
        this.includedPermissions = includedPermissions;
    }

    public void reloadPermission() {
        operatorPermissionController.setProfile(profile);
        operatorPermissionController.reloadPermission();
        this.includedPermissions = operatorPermissionController.getAvailablePermissions();
    }

    public void setOperatorPermissionController(operatorPermissionController operatorPermissionController) {
        this.operatorPermissionController = operatorPermissionController;
    }

    public void setLenguajeBean(LanguajeBean lenguajeBean) {
        this.lenguajeBean = lenguajeBean;
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

    public List<Pos> getPosList() {
        return posList;
    }

    public void setPosList(List<Pos> posList) {
        this.posList = posList;
    }

    public void save() {
        try {
            Operator operator = new Operator();
            operator.setFirstName(firstName);
            operator.setLastName(lastName);
            operator.setLogin(login);
            operator.setPhoneNumber(phoneNumber);
            operator.setEmail(email);
            operator.setEnabled(true);
            operator.setReceiveNotification(receiveNotification);
            operator.setProfile(profile);
            operator.setLanguage(loginBean.getUserSession().getLanguage());
            operator.setCommerce(loginBean.getCurrentBusiness());
            operator.setCreationDate(new Timestamp(System.currentTimeMillis()));

            try {
                operator.setPassword(Encoder.MD5(password));
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException ignore) {
            }

            List<BPPermission> excludedPermission = new ArrayList();
            List<BPPermission> availablePermissions = operatorPermissionController.getAvailablePermissions();

            for (BPPermission perm : availablePermissions) {
                if (!includedPermissions.contains(perm)) {
                    excludedPermission.add(perm);
                }
            }
            operator.setExcludedPermission(excludedPermission);

            if (selectPos != null) {
                operator.setPos(selectPos);
            }

            if (selectedStore != null) {
                operator.setStore(selectedStore);
            }
            operatorData.saveOperator(operator);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msg.getString("operatorSaveSuccesfull")));
        } catch (NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Faltan parametros"));
        } catch (GeneralException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        }
    }

    public void reset() {
        RequestContext.getCurrentInstance().reset("OperatorCreateForm:dataGrid");
    }

    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listOperator.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.PosController.doRediret()");
        }
    }

}
