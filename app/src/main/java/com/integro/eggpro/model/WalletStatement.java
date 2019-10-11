package com.integro.eggpro.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WalletStatement implements Serializable {

    private String date;

    private String uid;

    @SerializedName("Debit")
    private String debit;

    @SerializedName("Credit")
    private String credit;

    private String balance;

    private String paymentId;

    private String description;

    private String receipt;

    private String id;

    public WalletStatement(String date, String uid, String debit, String credit,
                           String balance, String paymentId, String description, String receipt, String id) {
        this.date = date;
        this.uid = uid;
        this.debit = debit;
        this.credit = credit;
        this.balance = balance;
        this.paymentId = paymentId;
        this.description = description;
        this.receipt = receipt;
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public String getUid() {
        return uid;
    }

    public String getDebit() {
        return debit;
    }

    public String getCredit() {
        return credit;
    }

    public String getBalance() {
        return balance;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getDescription() {
        return description;
    }

    public String getReceipt() {
        return receipt;
    }

    public String getId() {
        return id;
    }
}
