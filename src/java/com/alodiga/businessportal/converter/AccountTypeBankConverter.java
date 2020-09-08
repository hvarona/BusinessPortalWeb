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
package com.alodiga.businessportal.converter;

import com.portal.business.commons.data.AccountBankData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.models.AccountTypeBank;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("accountTypeBankConverter")
public class AccountTypeBankConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        AccountTypeBank accountTypeBank = null;
        try {
            AccountBankData accountBankData = new AccountBankData();
            Long accountTypeBankId = Long.parseLong(submittedValue);
            accountTypeBank = accountBankData.getAccountTypeBank(accountTypeBankId);
        } catch (GeneralException | NullParameterException | RegisterNotFoundException | NumberFormatException ex) {
            Logger.getLogger(AccountTypeBankConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return accountTypeBank;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            if (value instanceof AccountTypeBank) {
                return ((AccountTypeBank) value).getId().toString();
            } else {
                return value.toString();
            }

        }
    }
}
