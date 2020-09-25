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
package org.primefaces.ultima.view.input;

import com.portal.business.commons.data.AccessControlData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.BPProfile;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.component.datatable.DataTable;

@ManagedBean
public class SelectProfileView {
    
    private List<BPProfile> profiles;
    private BPProfile profile;
    private DataTable dataTable;
    


//    @PostConstruct
//    public void init() {
//        try {
//            AccessControlData accessControlData = new AccessControlData();
//            WsRequest request = new WsRequest();
//             profiles = accessControlData.getProfiles(request);
//        } catch (EmptyListException ex) {
//            Logger.getLogger(SelectProfileView.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (GeneralException ex) {
//            Logger.getLogger(SelectProfileView.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NullParameterException ex) {
//            Logger.getLogger(SelectProfileView.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
    
    
    
    public List<BPProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<BPProfile> profiles) {
        this.profiles = profiles;
    }

    public BPProfile getProfile() {
        return profile;
    }

    public void setProfile(BPProfile profile) {
        this.profile = profile;
    }
    
    public void save() {
        System.out.println("org.primefaces.ultima.view.input.SelectProfileView.save()");
	addMessage("Data saved");
    }   
     public void addMessage(String summary) {
	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
	FacesContext.getCurrentInstance().addMessage(null, message);
   }
     

    
}
