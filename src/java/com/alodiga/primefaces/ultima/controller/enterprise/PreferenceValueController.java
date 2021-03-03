package com.alodiga.primefaces.ultima.controller.enterprise;

import com.alodiga.remittance.beans.LanguajeBean;
import com.alodiga.remittance.beans.LoginBean;
import com.alodiga.wallet.common.exception.EmptyListException;
import com.alodiga.wallet.common.exception.GeneralException;
import com.alodiga.wallet.common.exception.NullParameterException;
import com.alodiga.wallet.ws.Product;
import com.portal.business.commons.data.BusinessData;
import com.portal.business.commons.data.PreferencesData;
import com.portal.business.commons.enumeration.BusinessServiceType;
import com.portal.business.commons.models.PreferenceFieldEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class PreferenceValueController {

    private Map<PreferenceFieldEnum, String> generalTransactionalsLimits;

    private List<ServiceTransactionLimits> serviceTransactionLimites = new ArrayList();

    @ManagedProperty(value = "#{languajeBean}")
    private LanguajeBean languageBean;

    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    private ResourceBundle msg;

    private String messages;

    @PostConstruct
    public void init() {
        try {
            if (languageBean == null || languageBean.getLanguaje() == null || languageBean.getLanguaje().isEmpty()) {
                msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag("es"));
            } else {
                msg = ResourceBundle.getBundle("com.alodiga.remittance.messages.message", Locale.forLanguageTag(languageBean.getLanguaje()));
            }
            PreferencesData preferenceData = new PreferencesData();
            generalTransactionalsLimits = new BusinessData().getTransactionalLimitByBusiness(loginBean.getCurrentBusiness());
            serviceTransactionLimites.add(new ServiceTransactionLimits("Activación de Tarjeta", BusinessServiceType.CARD_ACTIVATION, preferenceData.getPreferencesByBusiness(loginBean.getCurrentBusiness(), BusinessServiceType.CARD_ACTIVATION, 3L), null));
            serviceTransactionLimites.add(new ServiceTransactionLimits("Consulta de Tarjeta", BusinessServiceType.CARD_CONSULT, preferenceData.getPreferencesByBusiness(loginBean.getCurrentBusiness(), BusinessServiceType.CARD_CONSULT, 3L), null));
            serviceTransactionLimites.add(new ServiceTransactionLimits("Recarga de Tarjeta", BusinessServiceType.CARD_RECHARGE, preferenceData.getPreferencesByBusiness(loginBean.getCurrentBusiness(), BusinessServiceType.CARD_RECHARGE, 3L), null));
            serviceTransactionLimites.add(new ServiceTransactionLimits("Recarga Billetera ", BusinessServiceType.RECHARGE, preferenceData.getPreferencesByBusiness(loginBean.getCurrentBusiness(), BusinessServiceType.RECHARGE, loginBean.getBusinessProduct().getId()), loginBean.getBusinessProduct()));
            serviceTransactionLimites.add(new ServiceTransactionLimits("Remesas ", BusinessServiceType.REMITTANCE, preferenceData.getPreferencesByBusiness(loginBean.getCurrentBusiness(), BusinessServiceType.REMITTANCE, loginBean.getBusinessProduct().getId()), loginBean.getBusinessProduct()));

            /*ProductEJB productEJB = (ProductEJB) EJBServiceLocator.getInstance().get(com.alodiga.wallet.common.utils.EjbConstants.PRODUCT_EJB);
            List<Product> products = productEJB.getProducts(new EJBRequest());
            for (Product product : products) {
                switch (product.getName()) {
                    case "Tarjeta Prepagada":
                        serviceTransactionLimites.add(new ServiceTransactionLimits("Activación de Tarjeta", BusinessServiceType.CARD_ACTIVATION, preferenceData.getPreferencesByBusiness(loginBean.getCurrentBusiness(), BusinessServiceType.CARD_ACTIVATION, product.getId()), product));
                        serviceTransactionLimites.add(new ServiceTransactionLimits("Consulta de Tarjeta", BusinessServiceType.CARD_CONSULT, preferenceData.getPreferencesByBusiness(loginBean.getCurrentBusiness(), BusinessServiceType.CARD_CONSULT, product.getId()), product));
                        serviceTransactionLimites.add(new ServiceTransactionLimits("Recarga de Tarjeta", BusinessServiceType.CARD_RECHARGE, preferenceData.getPreferencesByBusiness(loginBean.getCurrentBusiness(), BusinessServiceType.CARD_RECHARGE, product.getId()), product));
                        break;
                    default:
                        if (product.getName().equals(loginBean.getBusinessProduct().getName())) {
                            serviceTransactionLimites.add(new ServiceTransactionLimits("Recarga Billetera ", BusinessServiceType.RECHARGE, preferenceData.getPreferencesByBusiness(loginBean.getCurrentBusiness(), BusinessServiceType.RECHARGE, product.getId()), product));
                            serviceTransactionLimites.add(new ServiceTransactionLimits("Remesas ", BusinessServiceType.REMITTANCE, preferenceData.getPreferencesByBusiness(loginBean.getCurrentBusiness(), BusinessServiceType.REMITTANCE, product.getId()), product));
                        }
                        break;
                }
            }*/
        } catch (Exception ex) {
            Logger.getLogger(PreferenceValueController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public float getDiscountRate() {
        return 2.0f;
    }

    public float getRechargeWalletCommission() {
        return loginBean.getComission(BusinessServiceType.RECHARGE).getCommissionValue();
    }

    public float getRemittanceCommission() {
        return loginBean.getComission(BusinessServiceType.REMITTANCE).getCommissionValue();
    }

    public float getCardConsultCommission() {
        return loginBean.getComission(BusinessServiceType.CARD_CONSULT).getCommissionValue();
    }

    public float getCardActivationCommission() {
        return loginBean.getComission(BusinessServiceType.CARD_ACTIVATION).getCommissionValue();
    }

    public float getCardRechargeCommission() {
        return loginBean.getComission(BusinessServiceType.CARD_RECHARGE).getCommissionValue();
    }

    public String getTimeoutInactiveSession() {
        return generalTransactionalsLimits.get(PreferenceFieldEnum.TIMEOUT_INACTIVE_SESSION_ID);
    }

    public String getMaxWrongLoginIntent() {
        return generalTransactionalsLimits.get(PreferenceFieldEnum.MAX_WRONG_LOGIN_INTENT_NUMBER_ID);
    }

    public String getDisabledTransaction() {
        return generalTransactionalsLimits.get(PreferenceFieldEnum.DISABLED_TRANSACTION_ID).equals("1") ? msg.getString("yes") : msg.getString("no");
    }

    public String getDefaultSmsProvider() {
        return generalTransactionalsLimits.get(PreferenceFieldEnum.DEFAULT_SMS_PROVIDER_ID);
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public LanguajeBean getLanguageBean() {
        return languageBean;
    }

    public void setLanguageBean(LanguajeBean languageBean) {
        this.languageBean = languageBean;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public List<ServiceTransactionLimits> getServiceTransactionLimites() {
        return serviceTransactionLimites;
    }

    public void setServiceTransactionLimites(List<ServiceTransactionLimits> serviceTransactionLimites) {
        this.serviceTransactionLimites = serviceTransactionLimites;
    }

    public String getServiceTypeName(BusinessServiceType type) {
        if (!msg.containsKey("ServiceType." + type.name())) {
            return type.name();
        }
        return msg.getString("ServiceType." + type.name());
    }

    public class ServiceTransactionLimits {

        private final String title;
        private final BusinessServiceType type;
        private final Map<PreferenceFieldEnum, String> transactionalsLimits;
        private final Product prodcut;

        public ServiceTransactionLimits(String title, BusinessServiceType type, Map<PreferenceFieldEnum, String> transactionalsLimits, Product prodcut) {
            this.title = title;
            this.type = type;
            this.transactionalsLimits = transactionalsLimits;
            this.prodcut = prodcut;
        }

        public BusinessServiceType getType() {
            return type;
        }

        public Product getProdcut() {
            return prodcut;
        }

        public String getTitle() {
            return title;
        }

        public String getMaxDailyTransactionAmount() {
            if (transactionalsLimits == null || !transactionalsLimits.containsKey(PreferenceFieldEnum.MAX_TRANSACTION_AMOUNT_DAILY_LIMIT_ID)) {
                return msg.getString("unlimited");
            }
            return transactionalsLimits.get(PreferenceFieldEnum.MAX_TRANSACTION_AMOUNT_DAILY_LIMIT_ID);
        }

        public String getMaxMonthlyTransactionAmount() {
            if (transactionalsLimits == null || !transactionalsLimits.containsKey(PreferenceFieldEnum.MAX_TRANSACTION_AMOUNT_MONTH_LIMIT_ID)) {
                return msg.getString("unlimited");
            }
            return transactionalsLimits.get(PreferenceFieldEnum.MAX_TRANSACTION_AMOUNT_MONTH_LIMIT_ID);
        }

        public String getMaxYearTransactionAmount() {
            if (transactionalsLimits == null || !transactionalsLimits.containsKey(PreferenceFieldEnum.MAX_TRANSACTION_AMOUNT_YEAR_LIMIT_ID)) {
                return msg.getString("unlimited");
            }
            return transactionalsLimits.get(PreferenceFieldEnum.MAX_TRANSACTION_AMOUNT_YEAR_LIMIT_ID);
        }

        public String getMaxDailyTransactionQuantity() {
            if (transactionalsLimits == null || !transactionalsLimits.containsKey(PreferenceFieldEnum.MAX_TRANSACTION_QUANTITY_DAILY_LIMIT_ID)) {
                return msg.getString("unlimited");
            }
            return transactionalsLimits.get(PreferenceFieldEnum.MAX_TRANSACTION_QUANTITY_DAILY_LIMIT_ID);
        }

        public String getMaxMonthlyTransactionQuantity() {
            if (transactionalsLimits == null || !transactionalsLimits.containsKey(PreferenceFieldEnum.MAX_TRANSACTION_QUANTITY_MONTH_LIMIT_ID)) {
                return msg.getString("unlimited");
            }
            return transactionalsLimits.get(PreferenceFieldEnum.MAX_TRANSACTION_QUANTITY_MONTH_LIMIT_ID);
        }

        public String getMaxYearTransactionQuantity() {
            if (transactionalsLimits == null || !transactionalsLimits.containsKey(PreferenceFieldEnum.MAX_TRANSACTION_QUANTITY_YEAR_LIMIT_ID)) {
                return msg.getString("unlimited");
            }
            return transactionalsLimits.get(PreferenceFieldEnum.MAX_TRANSACTION_QUANTITY_YEAR_LIMIT_ID);
        }
    }
}
