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

import br.com.fsma.projeto_web.filtros.MunicipioFiltro;
import br.com.fsma.projeto_web.modelo.classes.Municipio;
import br.com.fsma.projeto_web.modelo.classes.Uf;
import br.com.fsma.projeto_web.modelo.dao.MunicipioDao;
import br.com.fsma.projeto_web.modelo.dao.UfDao;
import br.com.fsma.projeto_web.tx.Transacional;
import br.com.fsma.projeto_web.validador.MunicipioValidador;

@Named
@ViewScoped
public class MunicipioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MunicipioDao municipioDao;
	@Inject
	private UfDao ufDao;
	@Inject
	private MunicipioValidador validador;
	private MunicipioFiltro filtro;
	private Municipio municipio;
	private List<Municipio> municipios = new ArrayList<Municipio>();
	private Uf uf;
	private List<Uf> ufs;
	private Long idUfSelecionada;
	

	private enum Status {
		LISTANDO, ALTERANDO, INCLUINDO
	};

	private Status status;

	@PostConstruct
	public void init() {
		filtro = new MunicipioFiltro();
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

	public List<Municipio> getMunicipios() {
		return municipios;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public Uf getUf() {
		return uf;
	}

	public List<Uf> listaUfs() {
		ufs = ufDao.buscaTodasUfs();
		return ufs;
	}

	public Long getIdUfSelecionada() {
		return idUfSelecionada;
	}

	public void setIdUfSelecionada(Long idUfSelecionada) {
		this.idUfSelecionada = idUfSelecionada;
	}

	public MunicipioFiltro getFiltro() {
		return filtro;
	}

	public void solicitaIncluir() {
		municipio = new Municipio();
		status = Status.INCLUINDO;
	}

	@Transacional
	public void confirmaInclusao() {

		if (idUfSelecionada == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "!", "UF não informada."));
			return;
		}
		
		Uf uf = ufDao.buscaPorId(idUfSelecionada);
		municipio.setUf(uf);

		if (validador.naoPodeIncluir(municipio)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "!", validador.getMensagem()));
			return;
		}
		
		municipioDao.adiciona(municipio);
		municipios = municipioDao.buscaTodosMunicipios();
		status = Status.LISTANDO;
	}

	public void cancelarInclusao() {
		status = Status.LISTANDO;
		municipio = null;
	}

	public void solicitaAlterar(Long municipioId) {
		municipio = municipioDao.buscaPorId(municipioId);
		status = Status.ALTERANDO;
	}

	@Transacional
	public void confirmaAlteracao() {

		if (idUfSelecionada == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "!", "UF não informada."));
			return;
		}
		
		Uf uf = ufDao.buscaPorId(idUfSelecionada);
		municipio.setUf(uf);

		if (validador.naoPodeAlterar(municipio)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", validador.getMensagem()));
			return;
		}
		municipioDao.atualiza(municipio);
		municipios.remove(municipio);
		municipios.add(municipio);
		status = Status.LISTANDO;
	}

	@Transacional
	public void confirmaExclusao(Long municipioId) {
		municipio = municipioDao.buscaPorId(municipioId);
		if(validador.naoPodeExcluir(municipio)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", validador.getMensagem()));
			return;
		}
		municipioDao.remove(municipio);
		municipios.remove(municipio);
		status = Status.LISTANDO;
	}

	public void cancelarAlteracao() {
		status = Status.LISTANDO;
		municipio = null;
	}

	public void cancelarBusca() {
		status = Status.LISTANDO;
	}

	public void buscaMunicipio() {
		if(validador.naoPodeBuscar(filtro)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "!", validador.getMensagem()));
		}
		municipios = municipioDao.busca(filtro);
		status = Status.LISTANDO;
	}

}
