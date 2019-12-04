package br.com.fsma.projeto_web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.fsma.projeto_web.validador.BairroValidador;
import br.com.fsma.projeto_web.filtros.BairroFiltro;
import br.com.fsma.projeto_web.modelo.classes.Municipio;
import br.com.fsma.projeto_web.modelo.dao.BairroDao;
import br.com.fsma.projeto_web.modelo.dao.MunicipioDao;
import br.com.fsma.projeto_web.modelo.dao.UfDao;
import br.com.fsma.projeto_web.modelo.classes.Bairro;
import br.com.fsma.projeto_web.modelo.classes.Uf;
import br.com.fsma.projeto_web.tx.Transacional;

@Named
@ViewScoped
public class BairroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private BairroDao bairroDao;
	@Inject
	private MunicipioDao municipioDao;
	@Inject
	private UfDao ufDao;
	@Inject
	private BairroValidador validador;
	
	private BairroFiltro filtro;
	private Bairro bairro;
	private List<Bairro> bairros = new ArrayList<Bairro>();
	private Municipio municipio;
	private List<Municipio> municipios = new ArrayList<Municipio>();
	private Uf uf;
	private List<Uf> ufs;
	private Long idUfSelecionada;
	private Long idMunicipioSelecionada;

	private enum Status {LISTANDO, ALTERANDO, INCLUINDO};

	private Status status;

	@PostConstruct
	public void init() {
		ufs = ufDao.buscaTodasUfs();
		filtro = new BairroFiltro();
		status = Status.LISTANDO;
	}

	public boolean isEditando() {
		return (status == Status.INCLUINDO) || (status == Status.ALTERANDO);
	}

	public boolean isAlterando() {
		return status == Status.ALTERANDO;
	}

	public boolean isListando() {
		return status == Status.LISTANDO;
	}

	public boolean isIncluindo() {
		return status == Status.INCLUINDO;
	}

	public Bairro getBairro() {
		return bairro;
	}

	public List<Bairro> getBairros() {
		return bairros;
	}

	public List<Municipio> getMunicipios() {
		return municipios;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public Uf getUf() {
		return uf;
	}

	public List<Uf> getUfs() {
		return ufs;
	}

	public List<Uf> listaUfs() {
		ufs = ufDao.buscaTodasUfs();
		return ufs;
	}

	public List<Municipio> listaMunicipios() {
		municipios = municipioDao.buscaMunicipioPorUf(filtro.getUfId());
		return municipios;
	}

	public List<Municipio> listaMunicipiosCad() {
		return municipioDao.buscaMunicipioPorUf(filtro.getUfId());
	}

	public Long getIdUfSelecionada() {
		return idUfSelecionada;
	}

	public void setIdUfSelecionada(Long idUfSelecionada) {
		this.idUfSelecionada = idUfSelecionada;
	}

	public Long getIdMunicipioSelecionada() {
		return idMunicipioSelecionada;
	}

	public void setIdMunicipioSelecionada(Long idMunicipioSelecionada) {
		this.idMunicipioSelecionada = idMunicipioSelecionada;
	}

	public BairroFiltro getFiltro() {
		return filtro;
	}

	public void solicitaIncluir() {
		bairro = new Bairro();
		status = Status.INCLUINDO;
	}

	@Transacional
	public void confirmaInclusao() {

		Municipio municipio = municipioDao.buscaPorId(idMunicipioSelecionada);
		Uf uf = ufDao.buscaPorId(filtro.getUfId());
		bairro.setMunicipio(municipio);
		bairro.setUf(uf);

		if (validador.naoPodeIncluir(bairro)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "!", validador.getMensagem()));
			return;
		}

		bairroDao.adiciona(bairro);
		bairros = bairroDao.busca(filtro);
		status = Status.LISTANDO;
	}

	public void cancelarInclusao() {
		status = Status.LISTANDO;
		municipio = null;
	}

	public void solicitaAlterar(Long bairroId) {
		bairro = bairroDao.buscaPorId(bairroId);
		status = Status.ALTERANDO;
	}

	@Transacional
	public void confirmaAlteracao() {

		Municipio municipio = municipioDao.buscaPorId(idMunicipioSelecionada);
		Uf uf = ufDao.buscaPorId(filtro.getUfId());
		bairro.setMunicipio(municipio);
		bairro.setUf(uf);

		if (validador.naoPodeAlterar(bairro)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "!", validador.getMensagem()));
			return;
		}
		bairroDao.atualiza(bairro);
		bairros = bairroDao.busca(filtro);
		status = Status.LISTANDO;
	}

	@Transacional
	public void confirmaExclusao(Long bairroId) {
		bairroDao.remove(bairro);
		bairro = null;
		bairros = bairroDao.busca(filtro);
		status = Status.LISTANDO;
	}

	public void cancelarAlteracao() {
		status = Status.LISTANDO;
		municipio = null;
	}

	public void cancelarBusca() {
		status = Status.LISTANDO;
	}

	public void buscaBairro() {
		bairros = bairroDao.busca(filtro);
		status = Status.LISTANDO;
	}

	public Long getUfIdFiltro() {
		return filtro.getUfId();
	}

	public void setUfIdFiltro(Long id) {
		filtro.setUfId(id);
	}

}
