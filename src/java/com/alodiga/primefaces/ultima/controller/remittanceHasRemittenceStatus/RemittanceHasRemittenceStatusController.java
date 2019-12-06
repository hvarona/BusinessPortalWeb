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
package com.alodiga.primefaces.ultima.controller.remittanceHasRemittenceStatus;

import com.portal.business.commons.data.AccessControlData;
import com.portal.business.commons.models.RemittanceHasRemittenceStatus;
import com.portal.business.commons.data.RemittanceHasRemittenceStatusData;
import com.portal.business.commons.data.RemittanceData;
import com.portal.business.commons.data.UserData;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Profile;
import com.portal.business.commons.models.Remittance;
import com.portal.business.commons.models.RemittanceStatus;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import com.portal.business.commons.models.SalePrice;
import com.portal.business.commons.models.User;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

@ManagedBean
public class RemittanceHasRemittenceStatusController {
    
    //Objeto y el DATA 
    private RemittanceHasRemittenceStatus remittanceHasRemittenceStatus;
    private RemittanceHasRemittenceStatusData remittanceHasRemittenceStatusData;
    private RemittanceData remittanceData;
    private UserData userData;
    
    //Propiedades del Objeto
    private Long id;
    private Long remittenceStatusId;
    private Long remittenceId;
    private Date beginningDate;
    private Date endingDate;
    private String comments;
    private Long userId;
    
    private Map<String,Long> remittancesMap = new HashMap<String, Long>();
    private Map<String,Long> remittanceStatusMap = new HashMap<String, Long>();
    private Map<String,Long> userMap = new HashMap<String, Long>();

    

    //Propiedades de Prime
    private String messages = null;

    public Map<String, Long> getRemittancesMap() {
        return remittancesMap;
    }

    public void setRemittancesMap(Map<String, Long> remittancesMap) {
        this.remittancesMap = remittancesMap;
    }

    public Map<String, Long> getRemittanceStatusMap() {
        return remittanceStatusMap;
    }

    public void setRemittanceStatusMap(Map<String, Long> remittanceStatusMap) {
        this.remittanceStatusMap = remittanceStatusMap;
    }

    public Map<String, Long> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, Long> userMap) {
        this.userMap = userMap;
    }

    
    @PostConstruct
    public void init(){
        HttpServletRequest req= (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        remittanceHasRemittenceStatus = (RemittanceHasRemittenceStatus) req.getAttribute("remittanceHasRemittenceStatus");
        remittanceHasRemittenceStatusData = new RemittanceHasRemittenceStatusData();
        remittanceData = new RemittanceData();
        userData = new UserData();

        
        WsRequest request = new WsRequest();

        try {
            List<Remittance> remittances = remittanceData.getRemittances();
            for(Remittance remittanceTmp : remittances ) {
                remittancesMap.put(remittanceTmp.getRemittanceNumber(), remittanceTmp.getId());
            }
            
        } catch (GeneralException | RegisterNotFoundException | NullParameterException | EmptyListException ex) {
            Logger.getLogger(RemittanceHasRemittenceStatusController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            List<RemittanceStatus> remittanceStatuses = remittanceData.getRemittenceStatus(request);
            for(RemittanceStatus remittanceStatusTmp : remittanceStatuses) {
                remittanceStatusMap.put(remittanceStatusTmp.getName(), remittanceStatusTmp.getId());
            }
            
        } catch (GeneralException | EmptyListException | NullParameterException ex) {
            Logger.getLogger(RemittanceHasRemittenceStatusController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            List<User> users = userData.getUsers(request);
            for(User userTmp : users) {
                userMap.put(userTmp.getFirstName() + " " + userTmp.getLastName(), userTmp.getId());
            }
            
        } catch (EmptyListException | GeneralException ex) {
            Logger.getLogger(RemittanceHasRemittenceStatusController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RemittanceHasRemittenceStatus getRemittanceHasRemittenceStatus() {
        return remittanceHasRemittenceStatus;
    }

    public void setRemittanceHasRemittenceStatus(RemittanceHasRemittenceStatus remittanceHasRemittenceStatus) {
        this.remittanceHasRemittenceStatus = remittanceHasRemittenceStatus;
    }

    public Long getRemittenceStatusId() {
        return remittenceStatusId;
    }

    public void setRemittenceStatusId(Long remittenceStatusId) {
        this.remittenceStatusId = remittenceStatusId;
    }

    public Long getRemittenceId() {
        return remittenceId;
    }

    public void setRemittenceId(Long remittenceId) {
        this.remittenceId = remittenceId;
    }

    public Date getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(Date beginningDate) {
        this.beginningDate = beginningDate;
    }

    public Date getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

   

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    



   

    public void save() throws RegisterNotFoundException {
        try {
            
            remittanceHasRemittenceStatusData = new RemittanceHasRemittenceStatusData();
            remittanceData = new RemittanceData();
            userData = new UserData();

            WsRequest request;

            remittanceHasRemittenceStatus = new RemittanceHasRemittenceStatus();
            
            request = new WsRequest();
            request.setParam(remittenceStatusId);
            remittanceHasRemittenceStatus.setRemittenceStatus((RemittanceStatus) remittanceData.loadRemittenceStatus(request));

            request = new WsRequest();
            request.setParam(remittenceId);
            //remittanceHasRemittenceStatus.setRemittance((Remittance) remittanceData.loadSingleRemittance(request));

            remittanceHasRemittenceStatus.setBeginningDate((Timestamp) beginningDate);
            remittanceHasRemittenceStatus.setEndingDate((Timestamp) endingDate);
            remittanceHasRemittenceStatus.setComments(comments);

            request = new WsRequest();
            request.setParam(userId);
            remittanceHasRemittenceStatus.setUser(userData.loadUser(request));

            request = new WsRequest();
            request.setParam(remittanceHasRemittenceStatus);
            remittanceHasRemittenceStatusData.saveRemittanceHasRemittenceStatus(request);
            
            messages = "El remittanceHasRemittenceStatus ha sido guardado con exito";
            
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
        RequestContext.getCurrentInstance().reset("RemittanceHasRemittenceStatusCreateForm:grid");
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listRemittanceHasRemittenceStatus.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.User1View.doRediret()");
        }
    }
}
