package com.example.hib_debug.servlet;

import com.example.hib_debug.model.Terminal;
import com.example.hib_debug.model.TerminalTransactions;
import com.example.hib_debug.service.TerminalSrv;
import com.fasterxml.jackson.core.JsonGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


/** 
 * This class handles cities requests: http://../getTerminals.
 * 
 *
 * @author
 * @version 1.0
 */
public class TerminalsServlet 
  extends AbstractHttpServlet {
    
    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_CLASS           = -1;
    private static final int DEFAULT_STATUS          = -1;
    private static final int DEFAULT_COUNT           = 30;
    private static final int DEFAULT_OFFSET          =  0;
    private static final double DEFAULT_GPS          =  0;
    private static final int DEFAULT_MAX_DISTANCE    =  100;
    private static final int DEFAULT_WITHDRAW_STATUS = -1;
    private static final int DEFAULT_DEPOSIT_STATUS  = -1;
    private static final int DEFAULT_PAYMENT_STATUS  = -1;

    @Autowired
    protected TerminalSrv terminalSrv = null;

    public void init(ServletConfig config) throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
        terminalSrv.createIndexes();
    }

    protected void doJson(HttpServletRequest req, JsonGenerator res) throws Exception {

        int terminalOffset = getIntParam(req, "offset", DEFAULT_OFFSET);

        List<Terminal> resultList = createList(req);

        // Pack to json
        res.writeStartObject(); // starts JSON object
        res.writeNumberField("total", resultList.size()); // total matching terminals
        res.writeNumberField("offset", terminalOffset); // current offset value
        res.writeFieldName("list"); // Terminal array
        res.writeStartArray();

        Iterator it = resultList.iterator();

        while (it.hasNext()) {

            Terminal t = (Terminal)it.next();

            res.writeStartObject(); // starts a new cell
            res.writeNumberField("id", t.getId()); 
            res.writeNumberField("dist", (int) t.getDistance());
            res.writeNumberField("lat", t.getGpsLatitude());
            res.writeNumberField("lon", t.getGpsLongitude());
            if (t.getName() != null && !"".equals(t.getName()))
                res.writeStringField("name", t.getName());
            if (t.getOwner() != null && !"".equals(t.getOwner()))
                res.writeStringField("owner", t.getOwner());
            if (t.getLocation() != null && !"".equals(t.getLocation()))
                res.writeStringField("address", t.getLocation());
            if (t.getCity() != null && !"".equals(t.getCity()))
                res.writeStringField("city", t.getCity());
            if (t.getRegion() != null && !"".equals(t.getRegion()))
                res.writeStringField("region", t.getRegion());
            if (t.getCountry() != null && !"".equals(t.getCountry()))
                res.writeStringField("country", t.getCountry());
            if (t.getZip() != null && !"".equals(t.getZip()))
                res.writeStringField("zip", t.getZip());
            if (t.getPhone() != null && !"".equals(t.getPhone()))
                res.writeStringField("phone", t.getPhone());
            if (t.getDescription() != null && !"".equals(t.getDescription()))
               res.writeStringField("misc", t.getDescription());
            if (t.getMetroStation() != null && !"".equals(t.getMetroStation())) 
                res.writeStringField("metro", t.getMetroStation());
            if (t.getWorkHours() != null && !"".equals(t.getWorkHours()))
                res.writeStringField("workhours", t.getWorkHours());
            if (t.getConfiguration() != 0)
                res.writeNumberField("configuration", t.getConfiguration());
            res.writeBooleanField("withdraw", t.getWithdrawStatus() == 1); 
            if (t.getWithdrawStatus() == 1)
                res.writeStringField("withdraw_currency", t.getAvailableCurrenciesToString(TerminalTransactions.WITHDRAWAL));
            res.writeBooleanField("deposit", t.getDepositStatus() == 1);
            if (t.getDepositStatus() == 1)
                res.writeStringField("deposit_currency", t.getAvailableCurrenciesToString(TerminalTransactions.DEPOSIT));
            res.writeBooleanField("payment", t.getPaymentStatus() == 1);
            if (t.getPaymentStatus() == 1)
                res.writeStringField("payment_currency", t.getAvailableCurrenciesToString(TerminalTransactions.PAYMENT));
            if (t.getStatus() != -1)
                res.writeNumberField("status", t.getStatus());
            res.writeEndObject(); // ends created cell for the list (terminal)
        }
        res.writeEndArray(); // for field 'list'
        res.writeEndObject(); 

    }

    private String getStringParam(HttpServletRequest req, String paramName, String defaultValue) {
        String value = req.getParameter(paramName);
        if (value != null && "".equals(value))
            value = defaultValue;
        return value;
    }

    private int getIntParam(HttpServletRequest req, String paramName, int defaultValue) {
        int paramValue = defaultValue;
        String value = req.getParameter(paramName);
        if (value != null && !"".equals(value)) {
            try {
                paramValue = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                log.error("Could not parse '" + paramName + "' parameter to an int: " + value);
            }
        }
        return paramValue;
    }

    private double getDoubleParam(HttpServletRequest req, String paramName, double defaultValue) {
        double paramValue = defaultValue;
        String value = req.getParameter(paramName);
        if (value != null && !"".equals(value)) {
            try {
                paramValue = Double.parseDouble(value);
            } catch (NumberFormatException e) {
                log.error("Could not parse '" + paramName + "' parameter to an double: " + value);
            }
        }
        return paramValue;
    }

    private List<Terminal> createList(HttpServletRequest req) {

        // Check input parameters
        HashMap<String, Object> paramsForDbQuery = new HashMap<String, Object>();

        paramsForDbQuery.put("count", getIntParam(req, "count", DEFAULT_COUNT));
        paramsForDbQuery.put("offset", getIntParam(req, "offset", DEFAULT_OFFSET));
        // get the "class" param from the URL
        paramsForDbQuery.put("terminalClass", getIntParam(req, "class", DEFAULT_CLASS));
        // get the "status" param from the URL
        paramsForDbQuery.put("status", getIntParam(req, "status", DEFAULT_STATUS));
        // get the "lat" param from the URL
        paramsForDbQuery.put("gpsLatitude", getDoubleParam(req, "lat", DEFAULT_GPS));
        // get the "lon" param from the URL
        paramsForDbQuery.put("gpsLongitude", getDoubleParam(req, "lon", DEFAULT_GPS));
        // get the "max_dist" param from the URL
        paramsForDbQuery.put("maxDist", getIntParam(req, "max_dist", DEFAULT_MAX_DISTANCE));
        // get the "owner" param from the URL
        paramsForDbQuery.put("owner", getStringParam(req, "owner", null));
        // get the "country" param from the URL
        paramsForDbQuery.put("country", getStringParam(req, "country", null));
        // get the "region" param from the URL
        paramsForDbQuery.put("region", getStringParam(req, "region", null));
        // get the "city" param from the URL
        paramsForDbQuery.put("city", getStringParam(req, "city", null));

        // get the "address" param from the URL
        String address = getStringParam(req, "address", null);
        // get the "location" param from the URL
        String location = getStringParam(req, "location", null);
        if (address != null)
            location = address;

        paramsForDbQuery.put("location", location);
        // get the "metro" param from the URL
        paramsForDbQuery.put("metroStation", getStringParam(req, "metro", null));
        // get the "withdraw" param from the URL
        paramsForDbQuery.put("withdrawStatus", getIntParam(req, "withdraw", DEFAULT_WITHDRAW_STATUS));
        // get the "deposit" param from the URL
        paramsForDbQuery.put("depositStatus", getIntParam(req, "deposit", DEFAULT_DEPOSIT_STATUS));
        // get the "payment" param from the URL
        paramsForDbQuery.put("paymentStatus", getIntParam(req, "payment", DEFAULT_PAYMENT_STATUS));

        // Get terminal list and incremental terminal list
        List<Terminal> terminals = terminalSrv.getByParams(paramsForDbQuery);

        return terminals;
    }

}