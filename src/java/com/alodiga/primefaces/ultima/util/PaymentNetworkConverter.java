/*
 * Copyright 2009 Prime Technology.
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
package com.alodiga.primefaces.ultima.util;

import com.portal.business.commons.data.PaymentNetworkData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.PaymentNetwork;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("paymentNetworkConverter")
public class PaymentNetworkConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        PaymentNetwork paymentNetwork = null;
        try {
            PaymentNetworkData paymentNetworkData = new PaymentNetworkData();
            WsRequest request = new WsRequest();
            request.setParam(Long.parseLong(submittedValue));
            paymentNetwork = paymentNetworkData.loadPaymentNetworkById(request);

        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(PaymentNetworkConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(PaymentNetworkConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(PaymentNetworkConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return paymentNetwork;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(value);
	}
		}
	}
