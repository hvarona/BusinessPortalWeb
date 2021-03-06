package com.alodiga.primefaces.ultima.util;

import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.exceptions.RegisterNotFoundException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.City;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("cityConverter")
public class CityConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        City city = null;
        try {
            UtilsData utilsData = new UtilsData();
            WsRequest request = new WsRequest();
            request.setParam(Long.parseLong(submittedValue));
            city = utilsData.loadCity(request);

        } catch (RegisterNotFoundException ex) {
            Logger.getLogger(CityConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(CityConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(CityConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return city;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(value);
	}
		}
	}
