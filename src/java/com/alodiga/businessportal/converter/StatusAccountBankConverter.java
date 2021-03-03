package com.alodiga.businessportal.converter;

import com.alodiga.wallet.common.ejb.BusinessPortalEJB;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.common.exception.RegisterNotFoundException;
import com.alodiga.wallet.common.model.StatusAccountBank;
import com.alodiga.wallet.common.utils.EJBServiceLocator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("statusAccountBankConverter")
public class StatusAccountBankConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        StatusAccountBank statusAccountBank = null;
        try {
            BusinessPortalEJB proxy = (BusinessPortalEJB) EJBServiceLocator.getInstance().get(com.alodiga.wallet.common.utils.EjbConstants.BUSINESS_PORTAL_EJB);
            Integer statusId = Integer.parseInt(submittedValue);
            statusAccountBank = proxy.loadStatusAccountBankById(statusId);
        } catch (GeneralException | NullParameterException | RegisterNotFoundException | NumberFormatException ex) {
            Logger.getLogger(StatusAccountBankConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return statusAccountBank;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            if (value instanceof StatusAccountBank) {
                  return ((StatusAccountBank) value).getId().toString();
            } else {
                return value.toString();
            }

        }
    }
}
