package com.example.hib_debug.service;

import com.example.hib_debug.model.Terminal;

import java.util.HashMap;
import java.util.List;

public interface TerminalSrv  {

    public List<Terminal> getByParams(HashMap<String, Object> params);

    public void createIndexes();

}
