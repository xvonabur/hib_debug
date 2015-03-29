package com.example.hib_debug.dao;

import com.example.hib_debug.model.Terminal;
import org.apache.log4j.Logger;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.hibernate.search.spatial.DistanceSortField;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.*;

public class TerminalDAO extends TypedDao<Integer, Terminal> {

    protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private DbPersistenceProvider persistenceProvider;

    public TerminalDAO() {
        super(Integer.class, Terminal.class);
    }

    public void createIndexes() {
        EntityManager em = this.persistenceProvider.entityManager();

        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            log.error(e);
        }
    }


    protected IPersistenceProvider getPersistenceProvider() {
        return persistenceProvider;
    }

    // JPA doesn't support spatials, so we need to unwrap session from Entity Manager
    public List<Terminal> getByParams(HashMap<String, Object> params) {


        List<Terminal> terminals = new ArrayList<Terminal>();
        List results = null;

        if (params != null && !params.isEmpty()) {

            EntityManager em = this.persistenceProvider.entityManager();
            Session session = em.unwrap(Session.class);
            FullTextSession fullTextSession = Search.getFullTextSession(session);

            // create native Lucene query using the query DSL
            QueryBuilder qb = fullTextSession.getSearchFactory()
                    .buildQueryBuilder().forEntity(Terminal.class).get();

            BooleanJunction bj = qb.bool();

            Integer count = (Integer) params.get("count");
            Integer offset = (Integer) params.get("offset");
            Integer maxDist = (Integer) params.get("maxDist");
            Double gpsLatitude = (Double) params.get("gpsLatitude");
            Double gpsLongitude = (Double) params.get("gpsLongitude");

            if (count != null && offset != null && maxDist != null
                    && count > 0 && offset >= 0 && maxDist > 0) {

                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if ((value != null && value instanceof Integer && (Integer) value > -1
                            && !key.equals("count") && !key.equals("offset") && !key.equals("maxDist")) ||
                            value != null && value instanceof String) {
                        bj.must(qb.keyword().onField(key).matching(value).createQuery());
                    }
                }

                bj.must(qb.spatial()
                        .onField("distance")
                        .within(maxDist, Unit.KM)
                        .ofLatitude(gpsLatitude)
                        .andLongitude(gpsLongitude)
                        .createQuery());

                Query luceneQuery = bj.createQuery();

                FullTextQuery hibQuery = fullTextSession.createFullTextQuery(luceneQuery, Terminal.class);
                Sort distanceSort = new Sort(
                        new DistanceSortField(gpsLatitude, gpsLongitude, "distance"));
                hibQuery.setSort(distanceSort);
                hibQuery.setProjection(FullTextQuery.SPATIAL_DISTANCE, FullTextQuery.THIS);
                hibQuery.setSpatialParameters(gpsLatitude, gpsLongitude, "distance");
                hibQuery.setFetchSize(count);
                hibQuery.setFirstResult(offset);
                hibQuery.setReadOnly(true);

                long timeStart = System.currentTimeMillis();

                // execute search
                results = hibQuery.list();
                Iterator<Object[]> iterator = results.iterator();

                while (iterator.hasNext()) {
                    Object[] resultObject = iterator.next();
                    if (resultObject.length == 2) {
                        double distanceInMeters = (Double) resultObject[0] * 1000;
                        Terminal terminal = (Terminal) resultObject[1];
                        terminal.setDistance(distanceInMeters);
                        terminals.add(terminal);
                    }
                }

                long timeEnd = System.currentTimeMillis();
                log.info("Getting from db Terminals"
                        + " by params time: "
                        + (timeEnd - timeStart)
                        + "ms");
            } else {
                log.error("Empty param list passed to TerminalDAO class. Cannot find terminals.");
            }
        }

        return terminals;
    }
}
