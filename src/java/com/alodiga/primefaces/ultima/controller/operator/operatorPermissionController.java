package com.alodiga.primefaces.ultima.controller.operator;

import com.alodiga.remittance.beans.LanguajeBean;
import com.portal.business.commons.models.BPPermission;
import com.portal.business.commons.models.BPPermissionHasProfile;
import com.portal.business.commons.models.BPProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hvarona
 */
@ManagedBean(name = "operatorPermission")
@ViewScoped
public class operatorPermissionController {

    List<BPPermission> availablePermissions;

    BPProfile profile;

    private ResourceBundle msg;

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean lenguajeBean;

    @PostConstruct
    public void init() {
        if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
        }

    }

    public List<BPPermission> getAvailablePermissions() {
        return availablePermissions;
    }

    public void setAvailablePermissions(List<BPPermission> availablePermissions) {
        this.availablePermissions = availablePermissions;
    }

    public BPProfile getProfile() {
        return profile;
    }

    public void setProfile(BPProfile profile) {
        this.profile = profile;
    }

    public void setLenguajeBean(LanguajeBean lenguajeBean) {
        this.lenguajeBean = lenguajeBean;
    }

    public void reloadPermission() {
        availablePermissions = new ArrayList();
        List<BPPermissionHasProfile> phps = profile.getPermissionHasProfiles();
        for (BPPermissionHasProfile php : phps) {
            availablePermissions.add(php.getPermission());
        }
    }

    public String getPermissionLabel(BPPermission permission) {
        return permission.getPermissionDataByLanguageId(lenguajeBean.getLanguage().getId()).getAlias();
    }

}
