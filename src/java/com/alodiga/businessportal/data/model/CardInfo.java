package com.alodiga.businessportal.data.model;

/**
 * Class that holds all the info of the Status of a card for checking balance
 *
 * @author hvarona
 */
public class CardInfo {

    private String cardNumber;
    private String accountNumber;
    private String accountBalance;
    private String accountDollarBalance;

    private String aliasCard;
    private String name;
    private String email;
    private String numberPhone;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountDollarBalance() {
        return accountDollarBalance;
    }

    public void setAccountDollarBalance(String accountDollarBalance) {
        this.accountDollarBalance = accountDollarBalance;
    }

    public String getAliasCard() {
        return aliasCard;
    }

    public void setAliasCard(String aliasCard) {
        this.aliasCard = aliasCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

}
