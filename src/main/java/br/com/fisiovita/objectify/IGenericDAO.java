package br.com.fisiovita.objectify;

import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;

public interface IGenericDAO<T> {
	
	Key<T> put(T entity);
	Map<Key<T>, T> putAll(Iterable<T> entities);
	void delete(T entity);
	void deleteKey(Key<T> entityKey);
	void deleteAll(Iterable<T> entities);
	void deleteKeys(Iterable<Key<T>> keys);
	T get(Long id) throws EntityNotFoundException;
	T get(Key<T> key) throws EntityNotFoundException;
	
	/*
	 * Convenience method to generate a typed Key<T> for a given id
	 */
	Key<T> getKey(Long id);

	/*
	 * Get ALL entities of type <T> in the datastore. Potentially very inefficient!!
	 * 	Make sure you have a good reason to use this!!
	 */
	List<T> listAll();

	/**
	 * Convenience method to get all objects matching a single property
	 *
	 * @param propName
	 * @param propValue
	 * @return T matching Object
	 */
	T getByProperty(String propName, Object propValue);
	List<T> listByProperty(String propName, Object propValue);
	List<Key<T>> listKeysByProperty(String propName, Object propValue);
	T getByExample(T exampleObj);
	List<T> listByExample(T exampleObj);

}
