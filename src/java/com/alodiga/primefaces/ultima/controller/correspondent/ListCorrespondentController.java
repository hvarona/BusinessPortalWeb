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
package com.alodiga.primefaces.ultima.controller.correspondent;

import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Correspondent;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;


@ManagedBean(name="dtListCorrespondentController")
@ViewScoped
public class ListCorrespondentController implements Serializable {
    
    
    private Correspondent selectedCorrespondent;
    private UtilsData utilsData = null;
    List<Correspondent> correspondents;
    List<Correspondent> filteredCorrespondents;
    
    
    @PostConstruct
    public void init() {
        try {
            utilsData = new UtilsData();
            WsRequest request = new WsRequest();
            correspondents = utilsData.getCorrespondentList();
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCorrespondentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCorrespondentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCorrespondentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Correspondent getSelectedCorrespondent() {
        return selectedCorrespondent;
    }

    public void setSelectedCorrespondent(Correspondent selectedCorrespondent) {
        this.selectedCorrespondent = selectedCorrespondent;
    }

    public List<Correspondent> getCorrespondents() {
        return correspondents;
    }

    public void setCorrespondents(List<Correspondent> correspondents) {
        this.correspondents = correspondents;
    }

    public List<Correspondent> getFilteredCorrespondents() {
        return filteredCorrespondents;
    }

    public void setFilteredCorrespondents(List<Correspondent> filteredCorrespondents) {
        this.filteredCorrespondents = filteredCorrespondents;
    }

   
   
    public void save() {
        try {
            Correspondent correspondent = null;
            if (selectedCorrespondent.getId() != null) {
                correspondent = selectedCorrespondent;
            } 
            correspondent = utilsData.saveCorrespondent(correspondent);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        }
    }
    
    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("correspondentView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.ListCorrespondentController.doRediret()");
        }
    }
    
  

    
}
