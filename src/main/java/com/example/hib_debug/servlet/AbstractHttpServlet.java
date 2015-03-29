package com.example.hib_debug.servlet;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

/** 
 * Basic abstract servlet. It logs exceptions and request time.
 *
 * @author
 * @version 2.0
 */
public abstract class AbstractHttpServlet extends HttpServlet {

    private static final String FORMAT_JSON    = "JSON";
    private static final String DEFAULT_FORMAT = FORMAT_JSON;

    protected Logger log = Logger.getLogger(this.getClass());

    // Request ID for logging purposes
    protected UUID requestUUID;


    // Make JSON representation of request
    protected abstract void doJson(HttpServletRequest req, JsonGenerator res) throws Exception;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // To make autowired annotation work
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    // This method is called by the servlet container to process a GET request.
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException {
        doGetAndPost(req, res);
    }


    // This method is called by the servlet container to process a POST request.
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException {
        doGetAndPost(req, res);
    }


    public void doGetAndPost(HttpServletRequest req, HttpServletResponse res)
        throws IOException, ServletException {

        requestUUID = UUID.randomUUID();

        log.info("(" + requestUUID + ") "
                + req.getMethod()
                + " "
                + req.getRequestURL().toString()
                + (req.getQueryString() == null ? "" : "?"
                + req.getQueryString()));

        // body of the document
        PrintWriter out = res.getWriter();

        // get the "format" param from the URL
        String format = req.getParameter("format");
        if (format == null || format.equals("") || (!format.equals(FORMAT_JSON)))
            format = DEFAULT_FORMAT;

        long timeStart = System.currentTimeMillis();
        JsonGenerator g = null;
        try {
            if (FORMAT_JSON.equals(format)) {
                // JSON constructor
                JsonFactory f = new JsonFactory();
                // sets output method to stream (body of the document)
                g = f.createGenerator(out);
                doJson(req, g);
            }
        } catch (Exception e) {
            log.error(e);
            throw new ServletException(e.getMessage());
        } finally {
            if (g != null && !g.isClosed())
                g.close();
            out.close();
        }
        long timeEnd = System.currentTimeMillis();
        log.info("(" + requestUUID + ") Processing time: " + (timeEnd - timeStart) + "ms");
    }

}
