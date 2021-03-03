package com.alodiga.businessportal.converter;

import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.model.AccountBank;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("accountBankConverter")
public class AccountBankConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue == null) {
            return null;
        }
        AccountBank accountBank = null;
        try {
            System.out.println("AccountBankConverter se pide convertir : " + submittedValue);
            BusinessPortalEJB proxy = (BusinessPortalEJB) EJBServiceLocator.getInstance().get(com.alodiga.wallet.common.utils.EjbConstants.BUSINESS_PORTAL_EJB);
            Long accountBankId = Long.parseLong(submittedValue);
            accountBank = proxy.loadAccountBankById(accountBankId);
            System.out.println("AccountBankConverter convirtio el account bank " + accountBank.getId());
        } catch (GeneralException | NullParameterException | RegisterNotFoundException | NumberFormatException ex) {
            Logger.getLogger(AccountBankConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return accountBank;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            if (value instanceof AccountBank) {
                return ((AccountBank) value).getId().toString();
            } else {
                return value.toString();
            }

        }
    }
}
