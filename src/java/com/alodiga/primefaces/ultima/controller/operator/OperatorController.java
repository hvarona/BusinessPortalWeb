package com.alodiga.primefaces.ultima.controller.operator;

import com.alodiga.remittance.beans.LoginBean;
import com.portal.business.commons.data.OperatorData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.Language;
import com.portal.business.commons.models.Operator;
import com.portal.business.commons.models.Permission;
import com.portal.business.commons.models.Profile;
import com.portal.business.commons.utils.Encoder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
    private Profile profile;
    private Language language;

    private OperatorData operatorData = null;

    @ManagedProperty(value = "#{loginBean}")
    LoginBean loginBean;

    @ManagedProperty(value = "#{operatorPermission}")
    operatorPermissionController operatorPermissionController;

    List<Permission> includedPermissions;

    private String messages = null;

    @PostConstruct
    public void init() {
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

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public List<Permission> getIncludedPermissions() {
        return includedPermissions;
    }

    public void setIncludedPermissions(List<Permission> includedPermissions) {
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

            List<Permission> excludedPermission = new ArrayList();

            List<Permission> availablePermissions = operatorPermissionController.getAvailablePermissions();

            for (Permission perm : availablePermissions) {
                if (!includedPermissions.contains(perm)) {
                    excludedPermission.add(perm);
                }
            }

            operator.setExcludedPermission(excludedPermission);

            operatorData.saveOperator(operator);
            messages = "El operador " + login + " ha sido guardado con exito";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(messages));
        } catch (NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Faltan parametros"));
        } catch (GeneralException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        }
    }

    public void reset() {
        RequestContext.getCurrentInstance().reset("PosCreateForm:dataGrid");
    }

    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listPos.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.PosController.doRediret()");
        }
    }

}
