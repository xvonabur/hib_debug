package com.example.hib_debug.service;

import com.example.hib_debug.dao.TerminalDAO;
import com.example.hib_debug.model.Terminal;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class TerminalSrvImpl implements TerminalSrv {

    protected String daoClass;

    private String shortClassName;

    protected Logger log = Logger.getLogger(this.getClass());


    public String getShortClassName() {
        if (shortClassName == null || shortClassName.isEmpty()) {
            String[] className = getDaoClass().split("\\.");
            int classNameLength = className.length;
            shortClassName = className[classNameLength - 1];
        }
        return shortClassName;
    }

    @Autowired
    protected TerminalDAO terminalDAO;

    public TerminalDAO getTerminalDAO() {
        return terminalDAO;
    }

    public void setTerminalDAO(TerminalDAO terminalDAO) {
        this.terminalDAO = terminalDAO;
    }

    public String getDaoClass() {
        if (daoClass == null) daoClass = Terminal.class.getCanonicalName();
        return daoClass;
    }


    @Transactional(readOnly = true)
    public List<Terminal> getByParams(HashMap<String, Object> params) {
        List<Terminal> terminals = null;



        terminals = terminalDAO.getByParams(params);

        return terminals;
    }

    public void createIndexes() {
        terminalDAO.createIndexes();
    }
}
