package com.alodiga.businessportal.controlller.register;

import com.alodiga.businessportal.enumeration.BusinessType;
import com.alodiga.remittance.beans.LanguajeBean;
import com.portal.business.commons.models.Register;
import com.portal.business.commons.models.RegisterLegalPerson;
import com.portal.business.commons.models.RegisterNaturalPerson;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hvarona
 */
@ManagedBean(name = "businessRegisteController")
@ViewScoped
public class BusinessRegisterController {

    private BusinessType businessType;

    private Register register;

    private RegisterNaturalPerson registerNaturalPerson;

    private RegisterLegalPerson registerLegalPerson;

    private ResourceBundle msg;

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean lenguajeBean;

    @PostConstruct
    public void init() {
        if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
        }
    }

    public void setLenguajeBean(LanguajeBean lenguajeBean) {
        this.lenguajeBean = lenguajeBean;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public RegisterNaturalPerson getRegisterNaturalPerson() {
        return registerNaturalPerson;
    }

    public void setRegisterNaturalPerson(RegisterNaturalPerson registerNaturalPerson) {
        this.registerNaturalPerson = registerNaturalPerson;
    }

    public RegisterLegalPerson getRegisterLegalPerson() {
        return registerLegalPerson;
    }

    public void setRegisterLegalPerson(RegisterLegalPerson registerLegalPerson) {
        this.registerLegalPerson = registerLegalPerson;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public BusinessType[] getBusinessTypes() {
        return BusinessType.values();
    }

}
