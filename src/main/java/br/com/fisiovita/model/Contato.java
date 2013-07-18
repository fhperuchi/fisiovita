package br.com.fisiovita.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import org.springframework.validation.annotation.Validated;

import com.googlecode.objectify.annotation.Entity;

@Validated
@Entity
public class Contato implements Serializable {

	private static final long serialVersionUID = 1;
	
	@Id
	private String email;
	private String nome;
	private String interesse;
	private String telefone;
	private String mensagem;
	
	private List<String> erros = new ArrayList<String>();
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getInteresse() {
		return interesse;
	}
	public void setInteresse(String interesse) {
		this.interesse = interesse;
	}
	public List<String> getErros() {
		return erros;
	}
	public void setErros(List<String> erros) {
		this.erros = erros;
	}
	@Override
	public String toString() {
		return "Contato [nome=" + nome + ", interesse=" + interesse + ", email=" + email + ", telefone=" + telefone + ", mensagem=" + mensagem
				+ ", erros=" + erros + "]";
	}
}
