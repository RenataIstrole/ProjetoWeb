package br.com.fsma.projeto_web.validador;

import static br.com.fsma.projeto_web.util.StringUtil.isEmpty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.fsma.projeto_web.filtros.MunicipioFiltro;
import br.com.fsma.projeto_web.modelo.classes.Municipio;
import br.com.fsma.projeto_web.modelo.dao.BairroDao;
import br.com.fsma.projeto_web.modelo.dao.EmpresaDao;
import br.com.fsma.projeto_web.modelo.dao.MunicipioDao;
import br.com.fsma.projeto_web.modelo.dao.UfDao;

@Named
@RequestScoped
public class MunicipioValidador {

	@Inject
	private MunicipioDao municipioDao;
	@Inject
	private UfDao daoUf;
	@Inject
	private BairroDao daoBairro;
	@Inject
	private EmpresaDao daoEmpresa;
	
	private String mensagem;

	public String getMensagem() {
		return mensagem;
	}

	public boolean naoPodeIncluir(Municipio municipio) {

		if (municipio.getUf() == null) {
			mensagem = "UF não informada.";
			return true;
		}
		
		if (isEmpty(municipio.getNome())) {
			mensagem = "Nome do municipio não informado.";
			return true;
		}
		
		if (municipio.getNome().length() > 30) {
		    mensagem = "O nome do município excedeu o tamanho máximo de 30 caracteres.";
			return true;
		}

		Municipio municipioAux = municipioDao.buscaMunicipioPorNomeUf(municipio.getUf(), municipio.getNome());

		
		if (municipioAux != null) {
			mensagem = "Esse municipio já foi cadastrado!";
			return true;
		}

		return false;

	}

	public boolean naoPodeAlterar(Municipio municipio) {

		if (municipio.getNome().length() == 0) {
			mensagem = "Nome do municipio não informado!";
			return true;
		}

		if (municipio.getUf() == null) {
			mensagem = "Unidade federativa não informada.";
			return true;
		}
		
		if (municipio.getNome().length() > 30) {
		    mensagem = "O nome do município excedeu o tamanho máximo de 30 caracteres.";
			return true;
		}

		Municipio nome = municipioDao.buscaPorNome(municipio.getNome());

		if (nome != null) {
			mensagem = "Esse municipio já foi cadastrado!";
			return true;
		}

		return false;

	}

	public boolean naoPodeBuscar(MunicipioFiltro filtro) {
		if(filtro.getUfId() == null) {
			mensagem = "A busca não pode ser realizada sem a informação da UF.";
			return true;
		}
		return false;
	}

	public boolean naoPodeExcluir(Municipio municipio) {
		if(daoBairro.buscaBairroPorMunicipio(municipio.getId()) != null) {
			mensagem = "Não pôde ser excluido pois possui Bairro vinculado";
		}
//		if(daoEmpresa.buscaBairroPorMunicipio(municipio.getId()) != null) {
//			mensagem = "Não pôde ser excluido pois possui Empresa vinculada";
//		}
		return false;
	}

}
