/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alodiga.primefaces.ultima.controller.noWorkingDays;

import com.portal.business.commons.data.AccessControlData;
import com.portal.business.commons.data.NoWorkingDaysData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Profile;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
//import org.primefaces.model.DualListModel;
import com.portal.business.commons.models.NoWorkingDays;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

@ManagedBean
public class NoWorkingDaysController {
    private Long id;
    private int day;
    private int month;
    private int year;
    private Boolean enabled;
    private String description;
    private NoWorkingDays noWorkingDays;
    private NoWorkingDaysData noWorkingDaysData;
    
        private String messages = null;

    
    @PostConstruct
    public void init(){
        HttpServletRequest req= (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        noWorkingDays = (NoWorkingDays) req.getAttribute("noWorkingDays");
        noWorkingDaysData = new NoWorkingDaysData();
        WsRequest request = new WsRequest();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NoWorkingDays getNoWorkingDays() {
        return noWorkingDays;
    }

    public void setNoWorkingDays(NoWorkingDays noWorkingDays) {
        this.noWorkingDays = noWorkingDays;
    }

    public NoWorkingDaysData getNoWorkingDaysData() {
        return noWorkingDaysData;
    }

    public void setNoWorkingDaysData(NoWorkingDaysData noWorkingDaysData) {
        this.noWorkingDaysData = noWorkingDaysData;
    }

   

    public void save() {
        try {
            noWorkingDays = new NoWorkingDays();
            noWorkingDays.setDay(day);
            noWorkingDays.setMonth(month);
            noWorkingDays.setYear(year);
            noWorkingDays.setDescription(description);
            noWorkingDays.setEnabled(enabled);
            WsRequest request = new WsRequest();
            request.setParam(noWorkingDays);
            noWorkingDaysData.saveNoWorkingDay(request);
            messages = "El noWorkingDays ha sido guardado con exito";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(messages));
        } catch (GeneralException ex) {
            Logger.getLogger(NoWorkingDaysController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(NoWorkingDaysController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void reset() {
       // RequestContext.getCurrentInstance().reset("UserCreateForm:grid");
        RequestContext.getCurrentInstance().reset("NoWorkingDaysCreateForm:grid");
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listNoWorkingDays.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.User1View.doRediret()");
        }
    }
}
