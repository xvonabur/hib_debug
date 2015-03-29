package com.example.hib_debug.dao;

import com.example.hib_debug.dao.query.Query;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: benjamin
 * Date: 5/28/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Queries {

    public static class QService {

        public static final String ByIdList = "service-by-id-list";

        public static final Query.NamedQuery byIdList(List<Integer> idList){
            return Query.Named(ByIdList).set("idList").to(idList);
        }
    }

    public static class QVendor {

        public static final String ByIdList = "vendor-by-id-list";

        public static final Query.NamedQuery byIdList(List<Integer> idList){
            return Query.Named(ByIdList).set("idList").to(idList);
        }
    }

    public static class QConfiguration {
        public static final String AllWithRelationships = "all-configurations-with-relationships";

        public static final Query.NamedQuery allWithRelationships(){
            Query.NamedQuery q = Query.Named(AllWithRelationships);
            return q;
        }
    }

    public static class QMetroCity {
        public static final String AllWithRelationships = "all-metro-cities-with-relationships";

        public static final Query.NamedQuery allWithRelationships(){
            Query.NamedQuery q = Query.Named(AllWithRelationships);
            return q;
        }
    }

    public static class QMetroStation {
        public static final String ByParams = "metro-station-by-params";

        public static final Query.NamedQuery byParams(String metroCityName, Integer classCode){
            Query.NamedQuery q = Query.Named(ByParams);
            q.set("metroCityName").to(metroCityName);
            q.set("classCode").to(classCode);
            return q;
        }
    }

    public static class QTerminal {
        public static final String AllWithRelationships = "all-terminals-with-relationships";

        public static final Query.NamedQuery allWithRelationships(){
            Query.NamedQuery q = Query.Named(AllWithRelationships);
            return q;
        }
    }
}
