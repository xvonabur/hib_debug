package com.example.hib_debug.model;


import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Indexed
@Table(name="transactions",
        indexes = {
                @Index(name = "INDEX_transactions_id", columnList="id", unique = true)
        })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected int id = 0;

    @Field
    @Column(name = "type", nullable = false)
    protected String type;


    @IndexedEmbedded
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "transaction", fetch = FetchType.EAGER)
    @OrderColumn(name="position")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    protected List<TransactionCurrency> currencies =
        new ArrayList<TransactionCurrency>();

    @ContainedIn
    @ManyToOne
    @JoinColumn(name="terminal_id", nullable = false)
    protected Terminal terminal;

    @Version
    private long version = -1;

    private Timestamp tsCreated = null;

    private Timestamp tsLastModified = null;

    @PrePersist
    public void prePersist(){
        if (currencies != null && !currencies.isEmpty()) {
            for (TransactionCurrency tc : currencies) {
                tc.setTransaction(this);
            }
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(tsCreated == null)tsCreated = now;
        tsLastModified = now;
    }


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type; 
    }

    public void addCurrency(TransactionCurrency transactionCurrency) {
        currencies.add(transactionCurrency);
    }

    public List<TransactionCurrency> getCurrencies() {
        return currencies;
    }
    public void setCurrencies(List<TransactionCurrency> currencies) {
        this.currencies = currencies;
    }

    public Terminal getTerminal() {
        return terminal;
    }
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }


    public long getVersion() {
        return version;
    }


    public Transaction() {
    }

    public Transaction(String type, List<TransactionCurrency> currencies, Terminal terminal, long version, Timestamp tsCreated, Timestamp tsLastModified) {
        this.type = type;
        this.currencies = currencies;
        this.terminal = terminal;
        this.version = version;
        this.tsCreated = tsCreated;
        this.tsLastModified = tsLastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;

        Transaction that = (Transaction) o;

        List<TransactionCurrency> currenciesList = null;

        if (currencies != null && !currencies.isEmpty()) {
            currenciesList = new ArrayList<TransactionCurrency>(currencies);
        }

        if (currenciesList != null && !currenciesList.equals(that.currencies)) return false;
        if (!type.equals(that.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + currencies.hashCode();
        return result;
    }
}