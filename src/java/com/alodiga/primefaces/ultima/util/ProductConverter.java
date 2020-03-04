package com.alodiga.primefaces.ultima.util;

import com.alodiga.primefaces.ultima.controller.wallet.rechargeController.Product;
import com.portal.business.commons.data.PosData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Pos;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author hvarona
 */
@FacesConverter("productConverter")
public class ProductConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        try {
            Long idProduct = Long.parseLong(submittedValue);
            Product prod = new Product();
            prod.setId(idProduct);
            return prod;
        } catch (NumberFormatException ex) {
            Logger.getLogger(PosConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            if (value instanceof Product) {
                return Long.toString(((Product) value).getId());
            } else {
                return value.toString();
            }

        }
    }

}
