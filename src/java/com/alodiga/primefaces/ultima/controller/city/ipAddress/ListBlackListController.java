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
package com.alodiga.primefaces.ultima.controller.city.ipAddress;

import com.alodiga.primefaces.ultima.controller.user.ListUserController;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.IpBlackList;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;


@ManagedBean(name="dtListBlackListController")
@ViewScoped
public class ListBlackListController implements Serializable {
    
    
    private IpBlackList selectedIpBlackList;
    private UtilsData utilsData = null;
    List<IpBlackList> ipBlackLists;
    List<IpBlackList> filteredIpBlackLists;
    private String ipAddress;
    private String info;
    
    
    @PostConstruct
    public void init() {
        try {
            utilsData = new UtilsData();
            WsRequest request = new WsRequest();
            ipBlackLists = utilsData.getBlackList();
        } catch (EmptyListException ex) {
            Logger.getLogger(ListBlackListController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListBlackListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public IpBlackList getSelectedIpBlackList() {
        return selectedIpBlackList;
    }

    public void setSelectedIpBlackList(IpBlackList selectedIpBlackList) {
        this.selectedIpBlackList = selectedIpBlackList;
    }

    public List<IpBlackList> getIpBlackLists() {
        return ipBlackLists;
    }

    public void setIpBlackLists(List<IpBlackList> ipBlackLists) {
        this.ipBlackLists = ipBlackLists;
    }

    public List<IpBlackList> getFilteredIpBlackLists() {
        return filteredIpBlackLists;
    }

    public void setFilteredIpBlackLists(List<IpBlackList> filteredIpBlackLists) {
        this.filteredIpBlackLists = filteredIpBlackLists;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void save() {
        try {
            IpBlackList blackList = new IpBlackList();
            blackList.setIpAddress(ipAddress);
            Date now = new Date((new java.util.Date()).getTime());
            Timestamp creationDate = new Timestamp(now.getTime());
            blackList.setDate(creationDate);
            blackList.setInfo(info);
            if (!utilsData.isIpAddresInBlackList(ipAddress)) {
                blackList = utilsData.saveIpBlackList(blackList);
                ipBlackLists = utilsData.getBlackList();
                String messages = "La direccion Ip " + ipAddress + " ha sido agregada a la lista negra";
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(messages));
            } else {
                String messages = "La direccion Ip " + ipAddress + " ya existe en la lista negra";
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(messages));
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        }
    }
    
    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
    
     public void doDelete() {
        try {
            utilsData.deleteIpBlackList(selectedIpBlackList.getIpAddress());
            ipBlackLists = utilsData.getBlackList();
            String messages = "La direccion Ip " + selectedIpBlackList.getIpAddress() + " ha sido eliminada de la lista negra";
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(messages));
        } catch (NullParameterException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.city.ipAddress.ListBlackListController.doDelete()");
            Logger.getLogger(ListUserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.city.ipAddress.ListBlackListController.doDelete()");
            Logger.getLogger(ListUserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ex) {
            Logger.getLogger(ListBlackListController.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
       
}
