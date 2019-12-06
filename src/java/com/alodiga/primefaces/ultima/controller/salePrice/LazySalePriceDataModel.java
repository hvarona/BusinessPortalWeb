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
package com.alodiga.primefaces.ultima.controller.salePrice;

import com.portal.business.commons.models.SalePrice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * Dummy implementation of LazyDataModel that uses a list to mimic a real datasource like a database.
 */
public class LazySalePriceDataModel extends LazyDataModel<SalePrice> {
    
    private List<SalePrice> datasource;
    
    public LazySalePriceDataModel(List<SalePrice> datasource) {
        this.datasource = datasource;
    }
    
    @Override
    public SalePrice getRowData(String rowKey) {
        for(SalePrice salePrice : datasource) {
            if(salePrice.getId().toString().equals(rowKey))
                return salePrice;
        }

        return null;
    }

    @Override
    public Object getRowKey(SalePrice salePrice) {
        return salePrice.getId().toString();
    }

    @Override
    public List<SalePrice> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
        List<SalePrice> data = new ArrayList<SalePrice>();

        //filter
        //System.out.println("##### Entering this");
            for (SalePrice salePrice : datasource) {
                boolean match = true;

                if (filters != null) {
                    for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                        try {
                            String filterProperty = it.next();
                            Object filterValue = filters.get(filterProperty);
                            String fieldValue = String.valueOf(salePrice.getClass().getField(filterProperty).get(salePrice));

                            if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                                match = true;
                            } else {
                                match = false;
                                break;
                            }
                        } catch (Exception e) {
                            match = false;
                        }
                    }
                }

                if (match) {
                    data.add(salePrice);
                }
            }


        //sort
        if(sortField != null) {
            Collections.sort(data, new LazySalePriceSorter(sortField, sortOrder));
        }

        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);

        //paginate
        if(dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            }
            catch(IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        }
        else {
            return data;
        }
    }
    
       
}