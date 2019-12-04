package br.com.fsma.projeto_web.validador;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.fsma.projeto_web.modelo.classes.Uf;
import br.com.fsma.projeto_web.modelo.dao.UfDao;

import static br.com.fsma.projeto_web.util.StringUtil.*;

@Named
@RequestScoped
public class UfValidador {

	@Inject
	private UfDao ufDao;
	private String mensagem;

	public boolean naoPodeIncluir(Uf uf) {

		if (uf.getSigla().length() < 2) {
			mensagem = "Sigla com menos de 2 caracteres.";
			return true;
		}

		if (isEmpty(uf.getNome())) {
			mensagem = "Nome não informado.";
			return true;
		}

		Uf sigla = ufDao.buscaPorSigla(uf.getSigla());

		if (sigla != null) {
			mensagem = "Essa sigla já foi cadastrada anteriormente.";
			return true;
		}

		Uf nome = ufDao.buscaPorNome(uf.getNome());

		if (nome != null) {
			mensagem = "Esse nome já foi cadastrado anteriormente.";
			return true;
		}

		return false;

	}

	public boolean naoPodeAlterar(Uf uf) {

		if (uf.getSigla().length() != 2) {
			mensagem = "Sigla com menos de 2 caracteres.";
			return true;
		}

		if (isEmpty(uf.getNome())) {
			mensagem = "Nome não informado.";
			return true;
		}

		return false;

	}

	public String getMensagem() {
		return mensagem;
	}

}
