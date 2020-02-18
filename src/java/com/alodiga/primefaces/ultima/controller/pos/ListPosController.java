package com.alodiga.primefaces.ultima.controller.pos;

import com.alodiga.remittance.beans.LoginBean;
import com.portal.business.commons.data.PosData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import com.portal.business.commons.exceptions.NullParameterException;
import com.portal.business.commons.models.Pos;
import com.portal.business.commons.models.Store;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author hvarona
 */
@ManagedBean(name = "dtListPosController")
@ViewScoped
public class ListPosController implements Serializable {

    private Pos selectedPos;

    private PosData posData = null;

    private List<Pos> posList;
    private List<Pos> filteredPos;

    private Map<String, String> stores;

    private Store store = null;

    @ManagedProperty(value = "#{loginBean}")
    LoginBean loginBean;

    @PostConstruct
    public void init() {
        try {
            posData = new PosData();
            posList = posData.getPosList(loginBean.getCurrentCommerce());
        } catch (EmptyListException | GeneralException ex) {
            Logger.getLogger(ListPosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Pos getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(Pos selectedPos) {
        this.selectedPos = selectedPos;
    }

    public List<Pos> getPosList() {
        return posList;
    }

    public void setPosList(List<Pos> posList) {
        this.posList = posList;
    }

    public List<Pos> getFilteredPos() {
        return filteredPos;
    }

    public void setFilteredPos(List<Pos> filteredPos) {
        this.filteredPos = filteredPos;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Map<String, String> getStores() {
        stores = new TreeMap();
        try {
            List<Store> stores1 = posData.getStoreList(loginBean.getCurrentCommerce());
            for (Store stor : stores1) {
                stores.put(stor.getName(), stor.getId().toString());
            }
        } catch (EmptyListException | GeneralException ex) {
            Logger.getLogger(ListPosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stores;
    }

    public void setStores(Map<String, String> stores) {
        this.stores = stores;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public void handleReturnDialog(SelectEvent event) {
        if (event != null && event.getObject() != null) {
        }
    }

    public void doRediret() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("createPos.xhtml");
        } catch (IOException ex) {
            System.out.println("com.alodiga.primefaces.ultima.controller.ListPosController.doRediret()");
        }
    }

    public void save() {
        try {
            Pos pos = null;
            if (selectedPos.getId() != null) {
                pos = selectedPos;
            }
            posData.savePos(pos);
        } catch (GeneralException | NullParameterException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error General"));
        }
    }

}
