package br.com.fisiovita.objectify;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Embedded;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;

/**
 * Dao Genérico para acesso ao DataStore via Objectify Baseada em:
 * https://gist.github.com/1102416
 */
@SuppressWarnings("all")
public abstract class ObjectifyGenericDAO<T> extends DAOBase implements IGenericDAO<T> {

	private static final int MAX_RESULTS = 1000;

	private static final int DIVISOR_ARGUMENTOS = 3;

	static final int BAD_MODIFIERS = Modifier.FINAL | Modifier.STATIC | Modifier.TRANSIENT;

	protected Class<T> clazz; //NOSONAR

	@SuppressWarnings("unchecked")
	public ObjectifyGenericDAO() {
		clazz = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	/**
	 * We've got to get the associated domain class somehow
	 * 
	 * @param clazz
	 */
	protected ObjectifyGenericDAO(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Key<T> put(T entity)
	{
		return ofy().put(entity);
	}

	@Override
	public Map<Key<T>, T> putAll(Iterable<T> entities) {
		return ofy().put(entities);
	}

	@Override
	public void delete(T entity) {
		ofy().delete(entity);
	}

	@Override
	public void deleteKey(Key<T> entityKey) {
		ofy().delete(entityKey);
	}

	@Override
	public void deleteAll(Iterable<T> entities) {
		ofy().delete(entities);
	}

	@Override
	public void deleteKeys(Iterable<Key<T>> keys) {
		ofy().delete(keys);
	}

	@Override
	public T get(Long id) {
		return ofy().get(this.clazz, id);
	}

	@Override
	public T get(Key<T> key) {
		return ofy().get(key);
	}

	/*
	 * Convenience method to generate a typed Key<T> for a given id
	 */
	@Override
	public Key<T> getKey(Long id) {
		return new Key<T>(clazz, id);
	}

	/*
	 * Get ALL entities of type <T> in the datastore. DANGEROUS!! INEFFICIENT!!
	 * Only doing this for dev/debugging purposes.
	 */
	@Override
	public List<T> listAll() {
		Query<T> q = ofy().query(clazz);
		return asList(q.fetch());
	}

	/*
	 * Get ALL entities of type <T> in the datastore. DANGEROUS!! INEFFICIENT!!
	 * Only doing this for dev/debugging purposes.
	 */
	public List<T> listAllOrderBy(String fieldName) {
		Query<T> q = ofy().query(clazz).order(fieldName);
		return asList(q.fetch());
	}

	/**
	 * Convenience method to get all objects matching a single property
	 * 
	 * @param propName
	 * @param propValue
	 * @return T matching Object
	 */
	@Override
	public T getByProperty(String propName, Object propValue) {
		Query<T> q = ofy().query(clazz);
		q.filter(propName, propValue);
		return q.get();
	}

	@Override
	public List<T> listByProperty(String propName, Object propValue) {
		Query<T> q = ofy().query(clazz);
		q.filter(propName, propValue);
		return asList(q.fetch());
	}

	public List<T> listByPropertyOrder(String propName, Object propValue, String order) {
		Query<T> q = ofy().query(clazz);
		q = q.filter(propName, propValue).order(order);
		return asList(q.fetch());
	}

	@Override
	public List<Key<T>> listKeysByProperty(String propName, Object propValue) {
		Query<T> q = ofy().query(clazz);
		q.filter(propName, propValue);
		return asKeyList(q.fetchKeys());
	}

	@Override
	public T getByExample(T exampleObj) {
		Query<T> queryByExample = buildQueryByExample(exampleObj);
		Iterable<T> iterableResults = queryByExample.fetch();
		Iterator<T> i = iterableResults.iterator();
		T obj = i.next();
		if (i.hasNext()) {
			throw new IllegalStateException("Too many results");
		}
		return obj;
	}

	@Override
	public List<T> listByExample(T exampleObj) {
		Query<T> queryByExample = buildQueryByExample(exampleObj);
		return asList(queryByExample.fetch());
	}

	protected List<T> asList(Iterable<T> iterable) {
		ArrayList<T> list = new ArrayList<T>();
		for (T t : iterable) {
			list.add(t);
		}
		return list;
	}

	private List<Key<T>> asKeyList(Iterable<Key<T>> iterableKeys) {
		ArrayList<Key<T>> keys = new ArrayList<Key<T>>();
		for (Key<T> key : iterableKeys) {
			keys.add(key);
		}
		return keys;
	}

	private Query<T> buildQueryByExample(T exampleObj) {
		Query<T> q = ofy().query(clazz);

		// Add all non-null properties to query filter
		for (Field field : this.getAllFields()) {
			// Ignore transient, embedded, array, and collection properties
			if (field.isAnnotationPresent(Transient.class) || (field.isAnnotationPresent(Embedded.class)) //NOSONAR
					|| (field.getType().isArray()) || (Collection.class.isAssignableFrom(field.getType()))
					|| ((field.getModifiers() & BAD_MODIFIERS) != 0)) { 
				continue;
			}

			field.setAccessible(true);

			Object value;
			try {
				value = field.get(exampleObj);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
			if (value != null) {
				q.filter(field.getName(), value);
			}
		}

		return q;
	}

	protected Query<T> where(Query<T> query, Object... args) {
	    
	    Query<T> queryFilter = query;

		if (args.length % DIVISOR_ARGUMENTOS != 0 || args.length == 0) {
			throw new IllegalArgumentException(
					"Numero de argumentos inválidos. Deve ser (Query{, chave, operação, valor}+).");
		}

		for (int i = 0; i < args.length; i += DIVISOR_ARGUMENTOS) {
			String chave;
			String operacao;
			Object valor;

			if (args[i + 2] != null) {
				if (!(args[i] instanceof String)) {
					throw new IllegalArgumentException("Chave [" + i + "] deve ser String"); //NOPMD
				}
				if (!(args[i + 1] instanceof String)) {
					throw new IllegalArgumentException("Operação [" + i + 1 + "] deve ser String");
				}
				if (args[i + 2] instanceof String && StringUtils.isEmpty((String) args[i + 2])) {
					continue;
				}

				chave = (String) args[i];
				operacao = (String) args[i + 1];
				valor = args[i + 2];

				if (!StringUtils.isEmpty(operacao)) {
					operacao = " " + operacao;
				}

				queryFilter = queryFilter.filter(chave + operacao, valor);
			}
		}

		return query;
	}

	private List<T> getListUsinWhereSemCap(int start, int quantidade, Object... args) { //NOSONAR
		Query<T> query = getQuery();

		if (args.length % DIVISOR_ARGUMENTOS != 0 || args.length == 0) {
			throw new IllegalArgumentException(
					"Numero de argumentos inválidos. Deve ser ({, chave, operação, valor}+).");
		}

		List<String> chaveInequality = new ArrayList<String>();
		List<String> operacaoInequality = new ArrayList<String>();
		List<Object> valorInequality = new ArrayList<Object>();
		String usadoInequality = null;

		for (int i = 0; i < args.length; i += DIVISOR_ARGUMENTOS) {
			String chave;
			String operacao;
			Object valor;

			if (args[i + 2] != null) {
				if (!(args[i] instanceof String)) {
					throw new IllegalArgumentException("Chave [" + i + "] deve ser String");
				}
				if (!(args[i + 1] instanceof String)) {
					throw new IllegalArgumentException("Operação [" + i + 1 + "] deve ser String");
				}
				if (args[i + 2] instanceof String && StringUtils.isEmpty((String) args[i + 2])) {
					continue;
				}

				chave = (String) args[i];
				operacao = (String) args[i + 1];
				valor = args[i + 2];

				if (!StringUtils.isEmpty(operacao)) {
					operacao = " " + operacao;
				}

				if (operacao.equalsIgnoreCase(" LIKE%") || operacao.equalsIgnoreCase(" %LIKE")
						|| operacao.equalsIgnoreCase(" %LIKE%")) {
					chaveInequality.add(chave);
					operacaoInequality.add(operacao);
					valorInequality.add(valor);
					continue;
				}

				if (operacao.equals(" >=") || operacao.equals(" <=") || operacao.equals(" >") || operacao.equals(" <")) {
					if (usadoInequality != null && !usadoInequality.equals(chave)) {
						chaveInequality.add(chave);
						operacaoInequality.add(operacao);
						valorInequality.add(valor);
						continue;
					} else {
						usadoInequality = chave;
					}
				}

				query = query.filter(chave + operacao, valor);
			}
		}

		List<T> resultados = query.offset(start).list();

		int quantidadeRetornada = resultados.size();
		
		if (chaveInequality.size() > 1) {
			processFiltersInAList(resultados, chaveInequality, operacaoInequality, valorInequality);
		}

		if(quantidadeRetornada >= MAX_RESULTS && resultados.size() < quantidade){
			resultados.addAll(getListUsinWhereSemCap(start + quantidadeRetornada, quantidade - resultados.size(), args));
		}

		return resultados;
	}

	protected List<T> getListUsinWhere(int start, int limit, Object... args) {
		List<T> lista = getListUsinWhereSemCap(0, limit + start, args);

		if (lista.size() > start + limit) {
			lista = lista.subList(start, start + limit);
		} else if (start > 0) {
			lista = lista.subList(start, lista.size());
		}

		return lista;
	}

	protected List<T> getListUsinWhereAndOrder(int start, int limit, String sortField, Object... args) {
		List<T> lista = getListUsinWhereAndOrderSemCap(0, limit + start, sortField, args);

		if (lista.size() > start + limit) {
			lista = lista.subList(start, start + limit);
		} else if (start > 0) {
			lista = lista.subList(start, lista.size());
		}

		return lista;
	}

	private List<T> getListUsinWhereAndOrderSemCap(int start, int quantidade, String sortField, Object... args) { //NOSONAR
		Query<T> query = getQuery();

		if (args.length % DIVISOR_ARGUMENTOS != 0 || args.length == 0) {
			throw new IllegalArgumentException(
					"Numero de argumentos inválidos. Deve ser ({, chave, operação, valor}+).");
		}

		List<String> chaveInequality = new ArrayList<String>();
		List<String> operacaoInequality = new ArrayList<String>();
		List<Object> valorInequality = new ArrayList<Object>();

		for (int i = 0; i < args.length; i += DIVISOR_ARGUMENTOS) {
			String chave;
			String operacao;
			Object valor;

			if (args[i + 2] != null) {
				if (!(args[i] instanceof String)) {
					throw new IllegalArgumentException("Chave [" + i + "] deve ser String");
				}
				if (!(args[i + 1] instanceof String)) {
					throw new IllegalArgumentException("Operação [" + i + 1 + "] deve ser String");
				}
				if (args[i + 2] instanceof String && StringUtils.isEmpty((String) args[i + 2])) {
					continue;
				}

				chave = (String) args[i];
				operacao = (String) args[i + 1];
				valor = args[i + 2];

				if (!StringUtils.isEmpty(operacao)) {
					operacao = " " + operacao;
				}

                if (operacao.equalsIgnoreCase(" LIKE%") || operacao.equalsIgnoreCase(" %LIKE") //NOSONAR
                                || operacao.equalsIgnoreCase(" LIKE") || operacao.equalsIgnoreCase(" %LIKE%")
                                || operacao.equals(" >=") || operacao.equals(" <=") || operacao.equals(" >")
                                || operacao.equals(" <") || operacao.equals(" !=") || "empty".equalsIgnoreCase(valor.toString())
                                || operacao.equalsIgnoreCase(" IN")) {
                    chaveInequality.add(chave);
                    operacaoInequality.add(operacao);
                    valorInequality.add(valor);
                    continue;
                }

				if(operacao.equalsIgnoreCase(" ISNULL")){
					query = query.filter(chave, null);
				}else{
					query = query.filter(chave + operacao, valor);
				}
				
			}
		}

		if (sortField != null) {
			String[] sortFieldArray = sortField.split(";");
			for (int i = 0; i < sortFieldArray.length; i++) {
				query = query.order(sortFieldArray[i]);
			}
		}

		List<T> resultados = query.offset(start).list();

		int quantidadeRetornada = resultados.size();
				
		if (chaveInequality.size() > 0) {
			processFiltersInAList(resultados, chaveInequality, operacaoInequality, valorInequality);
		}

		if(quantidadeRetornada >= MAX_RESULTS && resultados.size() < quantidade){
			resultados.addAll(getListUsinWhereAndOrderSemCap(start + quantidadeRetornada, quantidade - resultados.size(), sortField, args));
		}

		return resultados;
	}

	private void processFiltersInAList(List<T> resultados, List<String> chaveInequality,
			List<String> operacaoInequality, List<Object> valorInequality) {

		List<T> itensForRemove = new ArrayList<T>();

		for (T row : resultados) {
			if (!filterRow(row, chaveInequality, operacaoInequality, valorInequality)) {
				itensForRemove.add(row);
			}
		}

		resultados.removeAll(itensForRemove);
	}

    private boolean filterRow(T row, List<String> chaveInequality, List<String> operacaoInequality,
                    List<Object> valorInequality) {

        for (int i = 0; i < chaveInequality.size(); i++) {
            String campo = chaveInequality.get(i);
            String operacao = operacaoInequality.get(i);
            Object valorFiltro = valorInequality.get(i);

            String[] campos = campo.split("\\|");
            boolean match = false;
            for (int j = 0; j < campos.length; j++) {

                try {
                    Object valorDoCampo = getValue(campos[j], row);

                    if (filterCondition(valorDoCampo, operacao, valorFiltro)) {
                        match = true;
                        break;
                    }
                } catch (SecurityException e) {
                    throw new SecurityException("SecurityException on get fild " + campos[j] + " in class " //NOPMD
                                    + clazz.getName(), e);
                } catch (NoSuchFieldException e) {
                    throw new IllegalStateException("NoSuchFieldException on get fild " + campos[j] + " in class "
                                    + clazz.getName(), e);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("IllegalArgumentException on read fild " + campos[j] + " in class "
                                    + clazz.getName(), e);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("IllegalAccessException on read fild " + campos[j] + " in class "
                                    + clazz.getName(), e);
                }
            }

            if (!match) {
                return false;
            }

        }

        return true;
    }

	private Object getValue(String campo, Object row) throws NoSuchFieldException, IllegalArgumentException, // NOSONAR 
			IllegalAccessException {
    	
		String[] fields = campo.split("\\.");
		Class classe = clazz;
		Field field = null;
		Object rowTemp = row;
		
		for (int i = 0; i < fields.length; i++) {

			field = getField(classe, fields[i]);
			field.setAccessible(true);
			rowTemp = field.get(rowTemp);
			if (i < fields.length - 1) {
				classe = field.getType();
			}
		}

		return rowTemp;
	}

	private boolean filterCondition(Object valorDoCampo, String operacao, Object valorFiltro) {

		if (valorDoCampo == null) {
			return false;
		}

		if (operacao.equals(" >=")) {
			return ((Comparable) valorDoCampo).compareTo(valorFiltro) >= 0;
		} else if (operacao.equals(" <=")) {
			return ((Comparable) valorDoCampo).compareTo(valorFiltro) <= 0;
		} else if (operacao.equals(" >")) {
			return ((Comparable) valorDoCampo).compareTo(valorFiltro) > 0;
		} else if (operacao.equals(" <")) {
			return ((Comparable) valorDoCampo).compareTo(valorFiltro) < 0;
		} else if (operacao.equalsIgnoreCase(" LIKE%")) {
			return valorDoCampo.toString().startsWith(valorFiltro.toString());
		} else if (operacao.equalsIgnoreCase(" %LIKE")) {
			return valorDoCampo.toString().endsWith(valorFiltro.toString());
		} else if (operacao.equalsIgnoreCase(" %LIKE%")) {
			return valorDoCampo.toString().contains(valorFiltro.toString());
		} else if (operacao.equalsIgnoreCase(" LIKE")) {
            return valorDoCampo.toString().equals(valorFiltro.toString());
        }else if ("empty".equalsIgnoreCase(valorFiltro.toString())) {
            if(" !=".equalsIgnoreCase(operacao)){
                return !"".equalsIgnoreCase(valorDoCampo.toString());
            }
            return "".equalsIgnoreCase(valorDoCampo.toString());
        }else if (" IN".equalsIgnoreCase(operacao)){
        	return ((Collection)valorFiltro).contains(valorDoCampo);
        }

		throw new IllegalStateException("Operador " + operacao + " não encontrado ao realizar filtro");
	}

	protected Query<T> getQuery() {
		return ofy().query(clazz);
	}
	
    private List<Field> getAllFields() {
        List<Field> fieldList = new LinkedList<Field>();

        if (clazz == null) {
            return fieldList;
        }

       return this.getAllFields(clazz);
    }


    /**
     * Retorna all fields.
     *
     * @param classe classe
     * @return all fields
     */
    private List<Field> getAllFields(Class classe) {
        List<Field> fieldList = new LinkedList<Field>();

        if (classe == null) {
            return fieldList;
        }

        for (Class<T> temp = classe; (temp != null && !temp.equals(Object.class)); temp = ((Class<T>) temp
                        .getSuperclass())) {
            fieldList.addAll(Arrays.asList(temp.getDeclaredFields()));
        }

        return fieldList;
    }
    

	private Field getField(Class classe, String fieldName) throws NoSuchFieldException {

        Field retorno = null;
        
        for (Field field : this.getAllFields(classe)) {
            if (field.getName().equals(fieldName)) {
                retorno = field;
                break;
            }
        }

        if(retorno == null)
        {
            throw new NoSuchFieldException(fieldName);
        }
        
        return retorno;
    }
}