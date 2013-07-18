package br.com.fisiovita.repository;

import org.springframework.stereotype.Repository;

import br.com.fisiovita.model.Contato;
import br.com.fisiovita.objectify.ObjectifyGenericDAO;

import com.googlecode.objectify.ObjectifyService;

/**
 * @author elopes
 * 
 */
@Repository
public class ContatoRepository extends ObjectifyGenericDAO<Contato> {
	
	static {
		ObjectifyService.register(Contato.class);
	}

	/**
	 * 
	 */
	public ContatoRepository() {
		super();
	}

}