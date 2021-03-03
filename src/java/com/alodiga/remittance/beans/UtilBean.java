package com.alodiga.remittance.beans;

import com.portal.business.commons.data.BusinessData;
import com.portal.business.commons.data.BusinessSellData;
import com.portal.business.commons.data.OperatorData;
import com.portal.business.commons.data.PosData;
import com.portal.business.commons.enumeration.OperationType;
import com.portal.business.commons.exceptions.EmptyListException;
import com.portal.business.commons.exceptions.GeneralException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author hvarona
 */
@ManagedBean(name = "utilBean")
@SessionScoped
public class UtilBean {

    private static final PosData posData = new PosData();
    private static final OperatorData operatorData = new OperatorData();

    private static final int MAX_WEEEKS = 8;

    private BarChartModel salesChart = null;
    private ChartSeries salesSeries;
    long lastWeeksSales = 0;

    private BarChartModel walletRechargesChart = null;
    private ChartSeries walletRechargesSeries;
    long lastWeeksWalletRecharges = 0;

    private BarChartModel cardRechargesChart = null;
    private ChartSeries cardRechargesSeries;
    long lastWeeksCardRecharges = 0;

    private final BusinessSellData sellData = new BusinessSellData();
    private final BusinessData businessData = new BusinessData();

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;
    ResourceBundle msg;

    @ManagedProperty(value = "#{languajeBean}")
    LanguajeBean lenguajeBean;

    @PostConstruct
    public void init() {
        if (lenguajeBean == null || lenguajeBean.getLanguaje() == null || lenguajeBean.getLanguaje().isEmpty()) {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
        } else {
            msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(lenguajeBean.getLanguaje()));
        }
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public void setLenguajeBean(LanguajeBean lenguajeBean) {
        this.lenguajeBean = lenguajeBean;
    }

    private void loadSellChart() {
        salesChart = new BarChartModel();

        salesSeries = new ChartSeries();
        salesSeries.setLabel(msg.getString("sells"));
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();

        salesSeries.set(1, getWeekSales(startDate, endDate));

        for (int i = 1; i < MAX_WEEEKS + 1; i++) {
            endDate = cal.getTime();
            cal.setTimeInMillis(cal.getTimeInMillis() - (7 * 24 * 60 * 60000));
            startDate = cal.getTime();
            salesSeries.set(i, getWeekSales(startDate, endDate));
        }

        salesChart.addSeries(salesSeries);
        salesChart.setTitle("Bar Chart");
        salesChart.setLegendPosition("ne");

        Axis xAxis = salesChart.getAxis(AxisType.X);
        xAxis.setLabel(msg.getString("week"));

        Axis yAxis = salesChart.getAxis(AxisType.Y);
        yAxis.setLabel(msg.getString("sells"));
    }

    private void loadWalletRechargeChart() {
        walletRechargesChart = new BarChartModel();

        walletRechargesSeries = new ChartSeries();
        walletRechargesSeries.setLabel(msg.getString("walletRecharges"));
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();

        walletRechargesSeries.set(1, getWeekWalletRecharges(startDate, endDate));

        for (int i = 1; i < MAX_WEEEKS + 1; i++) {
            endDate = cal.getTime();
            cal.setTimeInMillis(cal.getTimeInMillis() - (7 * 24 * 60 * 60000));
            startDate = cal.getTime();
            walletRechargesSeries.set(i, getWeekWalletRecharges(startDate, endDate));
        }

        walletRechargesChart.addSeries(walletRechargesSeries);
        walletRechargesChart.setTitle("Bar Chart");
        walletRechargesChart.setLegendPosition("ne");

        Axis xAxis = walletRechargesChart.getAxis(AxisType.X);
        xAxis.setLabel(msg.getString("week"));

        Axis yAxis = walletRechargesChart.getAxis(AxisType.Y);
        yAxis.setLabel(msg.getString("walletRecharges"));
    }

    private void loadCardRechargesChart() {
        cardRechargesChart = new BarChartModel();

        cardRechargesSeries = new ChartSeries();
        cardRechargesSeries.setLabel(msg.getString("cardRecharges"));
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();

        cardRechargesSeries.set(1, getWeekCardRecharges(startDate, endDate));

        for (int i = 1; i < MAX_WEEEKS + 1; i++) {
            endDate = cal.getTime();
            cal.setTimeInMillis(cal.getTimeInMillis() - (7 * 24 * 60 * 60000));
            startDate = cal.getTime();
            cardRechargesSeries.set(i, getWeekCardRecharges(startDate, endDate));
        }

        cardRechargesChart.addSeries(cardRechargesSeries);
        cardRechargesChart.setTitle("Bar Chart");
        cardRechargesChart.setLegendPosition("ne");

        Axis xAxis = cardRechargesChart.getAxis(AxisType.X);
        xAxis.setLabel(msg.getString("week"));
        

        Axis yAxis = cardRechargesChart.getAxis(AxisType.Y);
        yAxis.setLabel(msg.getString("cardRecharges"));
    }

    public BarChartModel getSalesChart() {
        if (salesChart == null) {
            loadSellChart();
        }
        salesSeries.set(1, lastWeeksSales);

        return salesChart;
    }

    public BarChartModel getWalletRechargesChart() {
        if (walletRechargesChart == null) {
            loadWalletRechargeChart();
        }
        walletRechargesSeries.set(1, lastWeeksWalletRecharges);

        return walletRechargesChart;
    }

    public BarChartModel getCardRechargesChart() {
        if (cardRechargesChart == null) {
            loadCardRechargesChart();
        }
        cardRechargesSeries.set(1, lastWeeksCardRecharges);
        return cardRechargesChart;
    }

    public long getWeekSales() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 1);
        Date startDate = cal.getTime();

        lastWeeksSales = getWeekSales(startDate, new Date());
        return lastWeeksSales;
    }

    public long getWeekWalletRecharges() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 1);
        Date startDate = cal.getTime();

        lastWeeksWalletRecharges = getWeekWalletRecharges(startDate, new Date());
        return lastWeeksWalletRecharges;
    }

    public long getWeekCardRecharges() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 1);
        Date startDate = cal.getTime();

        lastWeeksCardRecharges = getWeekCardRecharges(startDate, new Date());
        return lastWeeksCardRecharges;
    }

    private long getWeekSales(Date start, Date end) {
        //return (long) (Math.random() * 100) + 10;
        try {
            return sellData.getBusinessSalesNumber(loginBean.getCurrentBusiness(), start, end);
        } catch (GeneralException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private long getWeekWalletRecharges(Date start, Date end) {
        //return (long) (Math.random() * 100);
        try {
            return businessData.getBusinessTransactionsNumber(loginBean.getCurrentBusiness(), start, end, OperationType.RECHARGE);
        } catch (GeneralException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private long getWeekCardRecharges(Date start, Date end) {
        //return (long) (Math.random() * 100);
        try {
            return businessData.getBusinessTransactionsNumber(loginBean.getCurrentBusiness(), start, end, OperationType.CARD_RECHARGE);
        } catch (GeneralException ex) {
            ex.printStackTrace();
        }
        return 0;
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
