package com.example.hib_debug.model;

import com.example.hib_debug.Persistent;
import com.example.hib_debug.dao.Queries;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.*;

import javax.persistence.*;
import javax.persistence.Index;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name = Queries.QTerminal.AllWithRelationships,
                query = "SELECT DISTINCT (t) from Terminal t " +
                        "LEFT JOIN FETCH t.transactions tr " +
                        "LEFT JOIN FETCH tr.currencies tc "
        )
})

@Entity
@Table(name="terminals",
    indexes = {
        @Index(name = "INDEX_terminals_id", columnList="id", unique = true)
})
@Indexed
@Spatial(name="distance", spatialMode = SpatialMode.HASH)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Terminal implements Persistent<Integer> {

    @Id
    @Column(name = "id", nullable = false)
    protected int id = 0;

    @Field
    @Column(name = "name", nullable = false)
    protected String name;

    @Field
    @Column(name = "owner", nullable = false)
    protected String owner;

    @Field
    @Column(name = "terminal_class", nullable = false)
    protected int terminalClass;

    @Field
    @Column(name = "status", nullable = false)
    protected int status = -1;

    @Field
    @Column(name = "country", nullable = false)
    protected String country;

    @Field
    @Column(name = "region", nullable = false)
    protected String region;

    @Field
    @Column(name = "city", nullable = false)
    protected String city;

    @Field
    @Column(name = "location", nullable = false)
    protected String location;

    @Field
    @Column(name = "zip", nullable = true)
    protected String zip;

    @Field
    @Column(name = "phone", nullable = true)
    protected String phone;

    @Field
    @Column(name = "metro_city", nullable = true)
    protected String metroCity;

    @Field
    @Column(name = "metro_station", nullable = true)
    protected String metroStation;

    @Longitude(of = "distance")
    @Column(name = "gps_longitude", nullable = false)
    protected double gpsLongitude = 0;

    @Latitude(of = "distance")
    @Column(name = "gps_latitude", nullable = false)
    protected double gpsLatitude = 0;

    @Transient
    protected double distance;

    @Field
    @Column(name = "work_hours", nullable = false, length = 1000)
    protected String workHours;

    @Field
    @Column(name = "configuration", nullable = false)
    protected int configuration = 0;

    @Field
    @Column(name = "withdraw_status", nullable = false)
    protected int withdrawStatus = 0;

    @Field
    @Column(name = "deposit_status", nullable = false)
    protected int depositStatus = 0;

    @Field
    @Column(name = "payment_status", nullable = false)
    protected int paymentStatus = 0;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "terminal", fetch = FetchType.EAGER)
    @OrderColumn(name="position")
    @IndexedEmbedded
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    protected List<Transaction> transactions = new ArrayList<Transaction>();

    @Field
    @Column(name = "description", nullable = false)
    protected String description;

    @Version
    private long version = -1;

    private Timestamp tsCreated = null;

    private Timestamp tsLastModified = null;

    @PrePersist
    public void prePersist(){
        if (transactions != null && !transactions.isEmpty()) {
            for (Transaction t : transactions) {
                t.setTerminal(this);
            }
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(tsCreated == null)tsCreated = now;
        tsLastModified = now;
    }

    public Terminal() {
    }

    public Terminal(int id, String name, String owner, int terminalClass, int status, String country, String region, String city, String location, String zip, String phone, String metroCity, String metroStation, double gpsLongitude, double gpsLatitude, double distance, String workHours, int configuration, int withdrawStatus, int depositStatus, int paymentStatus, List<Transaction> transactions, String description, long version, Timestamp tsCreated, Timestamp tsLastModified) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.terminalClass = terminalClass;
        this.status = status;
        this.country = country;
        this.region = region;
        this.city = city;
        this.location = location;
        this.zip = zip;
        this.phone = phone;
        this.metroCity = metroCity;
        this.metroStation = metroStation;
        this.gpsLongitude = gpsLongitude;
        this.gpsLatitude = gpsLatitude;
        this.distance = distance;
        this.workHours = workHours;
        this.configuration = configuration;
        this.withdrawStatus = withdrawStatus;
        this.depositStatus = depositStatus;
        this.paymentStatus = paymentStatus;
        this.transactions = transactions;
        this.description = description;
        this.version = version;
        this.tsCreated = tsCreated;
        this.tsLastModified = tsLastModified;
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

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner; 
    }

    public int getTerminalClass() {
        return terminalClass;
    }
    public void setTerminalClass(int terminalClass) {
        this.terminalClass = terminalClass; 
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status; 
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country; 
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region; 
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city; 
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location; 
    }

    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip; 
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone; 
    }

    public String getMetroCity() {
        return metroCity;
    }
    public void setMetroCity(String metroCity) {
        this.metroCity = metroCity; 
    }

    public String getMetroStation() {
        return metroStation;
    }
    public void setMetroStation(String metroStation) {
        this.metroStation = metroStation; 
    }

    public double getGpsLongitude() {
        return gpsLongitude;
    }
    public void setGpsLongitude(double gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    public double getGpsLatitude() {
        return gpsLatitude;
    }
    public void setGpsLatitude(double gpsLatitude) {
        this.gpsLatitude = gpsLatitude; 
    }

    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getWorkHours() {
        return workHours;
    }
    public void setWorkHours(String workHours) {
        this.workHours = workHours; 
    }

    public int getConfiguration() {
        return configuration;
    }
    public void setConfiguration(int configuration) {
        this.configuration = configuration; 
    }

    public int getWithdrawStatus() {
        return withdrawStatus;
    }
    public void setWithdrawStatus(int withdrawStatus) {
        this.withdrawStatus = withdrawStatus; 
    }

    public int getDepositStatus() {
        return depositStatus;
    }
    public void setDepositStatus(int depositStatus) {
        this.depositStatus = depositStatus; 
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus; 
    }

    @XmlElementWrapper(name = "transactions")
    @XmlElement(name = "transaction")
    public List<Transaction> getTransactions() {
        return transactions;
    }
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions; 
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public List<TransactionCurrency> getCurrencies(String type) {
        if (transactions != null) {
            for (Transaction tr : transactions) {
                if (!type.equals(tr.getType()))
                    continue;
                return tr.getCurrencies();
            }
        }
        return null;
    }


    public String getAvailableCurrenciesToString(String type) {
        String value = null;
        if (transactions != null) {
            for (Transaction tr : transactions) {
                if (!type.equals(tr.getType()))
                    continue;
                List<TransactionCurrency> currencies = tr.getCurrencies();
                if (currencies != null) {
                    for (TransactionCurrency tc : currencies) {
                        if (tc.getStatus() == 1) {
                            if (value == null)
                                value = tc.getName();
                            else
                                value = value + ", " + tc.getName();
                        }
                    }
                }
            }
        }
        return value;
    }


    public long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Terminal)) return false;

        Terminal terminal = (Terminal) o;

        List<Transaction> transactionArrayList = null;

        if (configuration != terminal.configuration) return false;
        if (depositStatus != terminal.depositStatus) return false;
        if (Double.compare(terminal.gpsLatitude, gpsLatitude) != 0) return false;
        if (Double.compare(terminal.gpsLongitude, gpsLongitude) != 0) return false;
        if (id != terminal.id) return false;
        if (paymentStatus != terminal.paymentStatus) return false;
        if (status != terminal.status) return false;
        if (terminalClass != terminal.terminalClass) return false;
        if (withdrawStatus != terminal.withdrawStatus) return false;
        if (!city.equals(terminal.city)) return false;
        if (!country.equals(terminal.country)) return false;
        if (!description.equals(terminal.description)) return false;
        if (!location.equals(terminal.location)) return false;
        if (metroCity != null ? !metroCity.equals(terminal.metroCity) : terminal.metroCity != null) return false;
        if (metroStation != null ? !metroStation.equals(terminal.metroStation) : terminal.metroStation != null)
            return false;
        if (!name.equals(terminal.name)) return false;
        if (!owner.equals(terminal.owner)) return false;
        if (phone != null ? !phone.equals(terminal.phone) : terminal.phone != null) return false;
        if (!region.equals(terminal.region)) return false;

        if (transactions != null && !transactions.isEmpty()) {
            transactionArrayList = new ArrayList<Transaction>(transactions);
        }

        if (transactionArrayList != null ? !transactionArrayList.equals(terminal.transactions) : terminal.transactions != null)
            return false;
        if (workHours != null ? !workHours.equals(terminal.workHours) : terminal.workHours != null) return false;
        if (zip != null ? !zip.equals(terminal.zip) : terminal.zip != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + owner.hashCode();
        result = 31 * result + terminalClass;
        result = 31 * result + status;
        result = 31 * result + country.hashCode();
        result = 31 * result + region.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + location.hashCode();
        result = 31 * result + (zip != null ? zip.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (metroCity != null ? metroCity.hashCode() : 0);
        result = 31 * result + (metroStation != null ? metroStation.hashCode() : 0);
        temp = Double.doubleToLongBits(gpsLongitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(gpsLatitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (workHours != null ? workHours.hashCode() : 0);
        result = 31 * result + configuration;
        result = 31 * result + withdrawStatus;
        result = 31 * result + depositStatus;
        result = 31 * result + paymentStatus;
        result = 31 * result + (transactions != null ? transactions.hashCode() : 0);
        result = 31 * result + description.hashCode();
        return result;
    }
}