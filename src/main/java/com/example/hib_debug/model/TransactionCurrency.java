package com.example.hib_debug.model;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Indexed
@Table(name="transaction_currencies",
        indexes = {
                @Index(name = "INDEX_transaction_currencies_id", columnList="id", unique = true)
        })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TransactionCurrency implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    protected int id;

    @Field
    @Column(name = "name", nullable = false)
    protected String name;

    @Field
    @Column(name = "status", nullable = false)
    protected int status = -1;

    @ContainedIn
    @ManyToOne
    @JoinColumn(name="transaction_id", nullable = false)
    protected Transaction transaction;

    @Version
    private long version = -1;

    private Timestamp tsCreated = null;

    private Timestamp tsLastModified = null;

    @PrePersist
    public void prePersist(){
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

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name; 
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status; 
    }

    public Transaction getTransaction() {
        return transaction;
    }
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }


    public long getVersion() {
        return version;
    }

    public TransactionCurrency() {
    }

    public TransactionCurrency(String name, int status, Transaction transaction, long version, Timestamp tsCreated, Timestamp tsLastModified) {
        this.name = name;
        this.status = status;
        this.transaction = transaction;
        this.version = version;
        this.tsCreated = tsCreated;
        this.tsLastModified = tsLastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionCurrency)) return false;

        TransactionCurrency that = (TransactionCurrency) o;

        if (status != that.status) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + status;
        return result;
    }
}