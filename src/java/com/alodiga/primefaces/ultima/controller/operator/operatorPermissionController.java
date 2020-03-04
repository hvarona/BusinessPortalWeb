package com.alodiga.primefaces.ultima.controller.operator;

import com.portal.business.commons.models.Permission;
import com.portal.business.commons.models.PermissionHasProfile;
import com.portal.business.commons.models.Profile;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hvarona
 */
@ManagedBean (name = "operatorPermission")
@ViewScoped
public class operatorPermissionController {

    List<Permission> availablePermissions;

    Profile profile;

    @PostConstruct
    public void init() {

    }

    public List<Permission> getAvailablePermissions() {
        return availablePermissions;
    }

    public void setAvailablePermissions(List<Permission> availablePermissions) {
        this.availablePermissions = availablePermissions;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void reloadPermission() {
        availablePermissions = new ArrayList();
        List<PermissionHasProfile> phps = profile.getPermissionHasProfiles();
        for (PermissionHasProfile php : phps) {
            availablePermissions.add(php.getPermission());
        }
    }

}
