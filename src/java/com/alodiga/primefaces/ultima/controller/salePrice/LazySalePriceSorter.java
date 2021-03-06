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
import java.util.Comparator;
import org.primefaces.model.SortOrder;

public class LazySalePriceSorter implements Comparator<SalePrice> {

    private String sortField;
    
    private SortOrder sortOrder;
    
    public LazySalePriceSorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public int compare(SalePrice salePrice1, SalePrice salePrice2) {
        try {
            Object value1 = SalePrice.class.getField(this.sortField).get(salePrice1);
            Object value2 = SalePrice.class.getField(this.sortField).get(salePrice2);

            int value = ((Comparable)value1).compareTo(value2);
            
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}
