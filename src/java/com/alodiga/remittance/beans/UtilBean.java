package com.alodiga.remittance.beans;

import com.portal.business.commons.data.OperatorData;
import com.portal.business.commons.data.PosData;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author hvarona
 */
@ManagedBean(name = "utilBean")
@RequestScoped
public class UtilBean {

    private static final PosData posData = new PosData();
    private static final OperatorData operatorData = new OperatorData();

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public int getStoreAmount() {
        try {
            return posData.getStoreList(loginBean.getCurrentBusiness()).size();
        } catch (EmptyListException ex) {
            Logger.getLogger(UtilBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(UtilBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getPosAmount() {
        try {
            return posData.getPosList(loginBean.getCurrentBusiness()).size();
        } catch (EmptyListException ex) {
            Logger.getLogger(UtilBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(UtilBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int getOperatorAmount() {
        try {
            return operatorData.getOperatorList(loginBean.getCurrentBusiness()).size();
        } catch (EmptyListException ex) {
            Logger.getLogger(UtilBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GeneralException ex) {
            Logger.getLogger(UtilBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}
