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
package com.alodiga.primefaces.ultima.controller.country;

import com.alodiga.primefaces.ultima.data.model.LazyCountryDataModel;
import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.generic.WsRequest;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.CountryTranslation;
import com.portal.business.commons.models.Language;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

@ManagedBean(name="dtListCountryController")
@ViewScoped
public class ListCountryController implements Serializable {
    
    private LazyDataModel<Country> lazyModel;
    
    private Country selectedCountry;
    private UtilsData utilsData = null;
    private String spanishAlias;
    private String englishAlias;
    
    @PostConstruct
    public void init() {
        try {
            utilsData = new UtilsData();
            WsRequest request = new WsRequest();
            lazyModel = new LazyCountryDataModel(utilsData.getCountries(request));
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCountryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCountryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCountryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LazyDataModel<Country> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<Country> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public UtilsData getUtilsData() {
        return utilsData;
    }

    public void setUtilsData(UtilsData utilsData) {
        this.utilsData = utilsData;
    }

    public String getSpanishAlias() {
        getCountryTraslationSpanish(selectedCountry);
        return spanishAlias;
    }

    public void setSpanishAlias(String spanishAlias) {
        this.spanishAlias = spanishAlias;
    }

    public String getEnglishAlias() {
        getCountryTraslationEnglish(selectedCountry);
        return englishAlias;
    }

    public void setEnglishAlias(String englishAlias) {
        this.englishAlias = englishAlias;
    }

    public void save() {
        try {
            Country country = null;
            List<CountryTranslation> countryTranslations = null;
            if (selectedCountry.getId() != null) {
                country = selectedCountry;
                countryTranslations = utilsData.getCountryTranslationByCountryId(country.getId());
            } 
            country = utilsData.saveCountry(country);
            processCountryTranslation(countryTranslations, country);
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
            FacesContext.getCurrentInstance().getExternalContext().redirect("countryView.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.LazyUserView.doRediret()");
        }
    }
    
    private void processCountryTranslation(List<CountryTranslation> countryTranslations, Country country) throws Exception {

        List<CountryTranslation> newCountryTranslations = null;
        if (countryTranslations != null) {
            for (CountryTranslation countryTranslation : countryTranslations) {
                countryTranslation.setAlias(countryTranslation.getLanguage().getId().equals(Language.SPANISH) ? spanishAlias : englishAlias);
                utilsData.saveCountryTranslation(countryTranslation);
            }
            //return countryTranslations;

        } else {
            newCountryTranslations = new ArrayList<CountryTranslation>();
            //Spanish
            CountryTranslation countryTranslation = new CountryTranslation();
            countryTranslation.setAlias(spanishAlias);
            countryTranslation.setCountry(country);
            Language language = new Language();
            language.setId(Language.SPANISH);
            countryTranslation.setLanguage(language);
            newCountryTranslations.add(countryTranslation);
            utilsData.saveCountryTranslation(countryTranslation);
            //English
            countryTranslation = new CountryTranslation();
            countryTranslation.setAlias(englishAlias);
            countryTranslation.setCountry(country);
            language = new Language();
            language.setId(Language.ENGLISH);
            countryTranslation.setLanguage(language);
            newCountryTranslations.add(countryTranslation);
            utilsData.saveCountryTranslation(countryTranslation);
        }

    }
    
    
    public String getCountryTraslationSpanish(Country country){
       List<CountryTranslation> countryTranslations;
        try {
            countryTranslations = utilsData.getCountryTranslationByCountryId(country.getId());
            spanishAlias = getCountryTraslationAlias(countryTranslations, Language.SPANISH);
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCountryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCountryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCountryController.class.getName()).log(Level.SEVERE, null, ex);
        }
       return spanishAlias;
    }
    
     public String getCountryTraslationEnglish(Country country){
       List<CountryTranslation> countryTranslations;
        try {
            countryTranslations = utilsData.getCountryTranslationByCountryId(country.getId());
            englishAlias = getCountryTraslationAlias(countryTranslations, Language.ENGLISH);
        } catch (EmptyListException ex) {
            Logger.getLogger(ListCountryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullParameterException ex) {
            Logger.getLogger(ListCountryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(ListCountryController.class.getName()).log(Level.SEVERE, null, ex);
        }
       return englishAlias;
    }
    
    
    public String getCountryTraslationAlias(List<CountryTranslation> countryTranslations, Long languageId) {
        String alias = "";
        if (countryTranslations != null) {
            for (CountryTranslation countryTranslation : countryTranslations) {
                if (countryTranslation.getLanguage().getId().equals(languageId)) {
                    alias = countryTranslation.getAlias();
                }
            }
        }
        return alias;
    }

    
}
