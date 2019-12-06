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

import com.portal.business.commons.data.UtilsData;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.Country;
import com.portal.business.commons.models.CountryTranslation;
import com.portal.business.commons.models.Language;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
public class CountryController {
    private Long id;
    private String name;
    private String shortName;
    private String code;
    private String alternativeName1;
    private String alternativeName2;
    private String alternativeName3;
    private String spanishAlias;
    private String englishAlias;
    private UtilsData utilsData = null;
    private String messages = null;
    private StreamedContent codeFile;
    private StreamedContent abreviattionFile;
    
    @PostConstruct
    public void init(){
            utilsData = new UtilsData();
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlternativeName1() {
        return alternativeName1;
    }

    public void setAlternativeName1(String alternativeName1) {
        this.alternativeName1 = alternativeName1;
    }

    public String getAlternativeName2() {
        return alternativeName2;
    }

    public void setAlternativeName2(String alternativeName2) {
        this.alternativeName2 = alternativeName2;
    }

    public String getAlternativeName3() {
        return alternativeName3;
    }

    public void setAlternativeName3(String alternativeName3) {
        this.alternativeName3 = alternativeName3;
    }

    public String getSpanishAlias() {
        return spanishAlias;
    }

    public void setSpanishAlias(String spanishAlias) {
        this.spanishAlias = spanishAlias;
    }

    public String getEnglishAlias() {
        return englishAlias;
    }

    public void setEnglishAlias(String englishAlias) {
        this.englishAlias = englishAlias;
    }
    

    public void save() {
        try {
            Country country = new Country();
            country.setName(name);
            country.setShortName(shortName);
            country.setAlternativeName1(alternativeName1);
            country.setAlternativeName2(alternativeName2);
            country.setAlternativeName3(alternativeName3);
            country.setCode(code);

            country = utilsData.saveCountry(country);
            try {
                processCountryTranslation(country);
            } catch (Exception ex) {
                System.out.println("com.alodiga.primefaces.ultima.controller.CountryController.save()"+ex.getMessage());
            }
            messages = "El pais " + name + " ha sido guardado con exito";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(messages));
        } catch (NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "Faltan parametros"));
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        }
    }
    
    private void processCountryTranslation( Country country) throws Exception {

        List<CountryTranslation> newCountryTranslations = new ArrayList<CountryTranslation>();
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
    
  
    
    public void reset() {
        RequestContext.getCurrentInstance().reset("CountryCreateForm:grid");
    }
    
    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("listCountry.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.CountryController.doRediret()");
        }
    }
    
    public void doEdit() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("editCountry.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.CountryController.doRediret()");
        }
        
    }


 
    public StreamedContent getCodeFile() {
         InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/docs/code.pdf");
        codeFile = new DefaultStreamedContent(stream, "docs", "code.pdf");
        return  codeFile;
    }
    
    public StreamedContent getAbbreviationFile() {
         InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/docs/countries-abbreviation.pdf");
        abreviattionFile = new DefaultStreamedContent(stream, "docs", "countries-abbreviation.pdf");
        return abreviattionFile;
    }
}
