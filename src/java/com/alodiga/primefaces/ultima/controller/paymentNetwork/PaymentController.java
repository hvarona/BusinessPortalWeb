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
package com.alodiga.primefaces.ultima.controller.paymentNetwork;

import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.PaymentNetwork;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.io.IOException;
import static java.lang.Compiler.enable;

import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

@ManagedBean
public class PaymentController {
 
    private Long id;
    private String name;
    
    @PostConstruct
    public void init(){
        
       
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save() throws NullParameterException, GeneralException {
    
       PaymentNetwork paymentNetwork = null; 
       PaymentNetwork objPaymentNetwork = new PaymentNetwork(); 
       UtilsData utilsData = new UtilsData();

       objPaymentNetwork.setName(name);
       objPaymentNetwork.setEnabled(true);
       paymentNetwork = utilsData.savePaymentNetwork(objPaymentNetwork);
    
    }
    
    public void reset() {
        RequestContext.getCurrentInstance().reset("paymentCreateForm:grid");
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listPaymentNetwork.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.listReceiverView.doRediret()");
        }
    }
}
