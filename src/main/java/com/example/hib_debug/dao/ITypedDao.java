package com.example.hib_debug.dao;

import com.example.hib_debug.dao.query.Query;
import com.example.hib_debug.Persistent;
import com.example.hib_debug.dao.query.Options;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * A typed version of the {@link IPersistenceProvider} interface. It provides type-safe method for domain object persistence.
 *
 * 
 * @author Benjamin Diedrichsen
 * 
 * @param <KEY>
 *            The type of primary key used for the {@link com.example.hib_debug.Persistent}
 * @param <E>
 *            The type of domain object the instance of {@link ITypedDao} works on
 */

public interface ITypedDao<KEY extends Serializable, E extends Persistent<KEY>> {

	/**
	 * See corresponding method in {@link IPersistenceProvider}.
	 * 
	 */
	boolean delete(E entity);

	/**
	 * See corresponding method in {@link IPersistenceProvider}.
	 * 
	 * @return
	 */
	boolean deleteAll(Collection<E> entities);

	/**
	 * See corresponding method in {@link IPersistenceProvider}.
	 * 
	 */
	List<E> findAll();

	/**
	 * See corresponding method in {@link IPersistenceProvider}.
	 * 
	 */
	E findById(KEY id);
	
	/**
	 * See corresponding method in {@link IPersistenceProvider}.
	 * 
	 */
	E findById(KEY id, Options.AccessPlan options);


	/**
	 * See corresponding method in {@link IPersistenceProvider}.
	 * 
	 */
	E persist(E entity);

	/**
	 * See corresponding method in {@link IPersistenceProvider}.
	 * 
	 */
	List<E> persistAll(List<E> entities);

	/**
	 * See corresponding method in {@link IPersistenceProvider}.
	 * 
	 */
	void runTransactional(IUnitOfWork t);

	/**
	 * See corresponding method in {@link IPersistenceProvider}.
	 * 
	 */
	long countAll();

    /**
     * See corresponding method in {@link IPersistenceProvider}.
     *
     */
	E find(Query.TypedQuery query);

    /**
     * See corresponding method in {@link IPersistenceProvider}.
     *
     */
	<R> List<R> query(Class<R> queryResultType, Query queryToRun);

    /**
     * See corresponding method in {@link IPersistenceProvider}.
     *
     */
    List<E> findAll(Query.TypedQuery query);


    <KEY extends Serializable, E extends Persistent<KEY>> ITypedDao forEntity(Class<KEY> keyType, Class<E> entityType);

    boolean isSameVersion(E one, E other);

    /**
     * See corresponding method in {@link IPersistenceProvider}.
     *
     */
    void flush();
}
