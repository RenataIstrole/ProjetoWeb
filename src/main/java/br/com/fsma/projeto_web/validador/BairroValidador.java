package br.com.fsma.projeto_web.validador;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.fsma.projeto_web.modelo.dao.BairroDao;
import br.com.fsma.projeto_web.modelo.classes.Bairro;

@Named
@RequestScoped
public class BairroValidador {

	@Inject
	private BairroDao bairroDao;
	private String mensagem;

	public String getMensagem() {
		return mensagem;
	}

	public boolean naoPodeIncluir(Bairro bairro) {

		if (bairro.getNome().length() == 0) {
			mensagem = "Nome do bairro não informado.";
			return true;
		}

		if (bairro.getMunicipio() == null) {
			mensagem = "Cidade não informada.";
			return true;
		}

		if (bairro.getUf() == null) {
			mensagem = "Unidade federativa não informada.";
			return true;
		}

		Bairro nome = bairroDao.buscaPorNome(bairro.getNome());

		if (nome != null) {
			mensagem = "Esse bairro já foi cadastrado anteriormente.";
			return true;
		}
		
		return false;
		
	}

	public boolean naoPodeAlterar(Bairro bairro) {
		
		if (bairro.getNome().length() == 0) {
			mensagem = "Nome do bairro não informado.";
			return true;
		}

		if (bairro.getMunicipio() == null) {
			mensagem = "Cidade não informada.";
			return true;
		}

		if (bairro.getUf() == null) {
			mensagem = "Unidade federativa não informada.";
			return true;
		}

		Bairro nome = bairroDao.buscaPorNome(bairro.getNome());

		if (nome != null) {
			mensagem = "Esse bairro já foi cadastrado anteriormente.";
			return true;
		}
		return false;
	}

}
