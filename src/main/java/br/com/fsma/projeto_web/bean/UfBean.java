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

import br.com.fsma.projeto_web.modelo.classes.Uf;
import br.com.fsma.projeto_web.modelo.dao.UfDao;
import br.com.fsma.projeto_web.tx.Transacional;
import br.com.fsma.projeto_web.validador.UfValidador;

@Named
@ViewScoped
public class UfBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UfDao ufDao;
	@Inject
	private UfValidador validador;

	private Uf uf;
	private List<Uf> ufs = new ArrayList<Uf>();

	private enum Status {
		LISTANDO, ALTERANDO, INCLUINDO
	};

	private Status status;

	@PostConstruct
	public void init() {
		ufs = ufDao.buscaTodasUfs();
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

	public Uf getUf() {
		return uf;
	}

	public List<Uf> getUfs() {
		return ufs;
	}

	public void solicitaIncluir() {
		uf = new Uf();
		status = Status.INCLUINDO;
	}

	@Transacional
	public void confirmaInclusao() {

		if (validador.naoPodeIncluir(uf)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", validador.getMensagem()));
			return;
		}
		ufDao.adiciona(uf);
		ufs = ufDao.buscaTodasUfs();
		status = Status.LISTANDO;
	}

	public void cancelarInclusao() {
		status = Status.LISTANDO;
		uf = null;
	}

	public void solicitaAlterar(Long ufId) {
		uf = ufDao.buscaPorId(ufId);
		status = Status.ALTERANDO;
	}

	@Transacional
	public void confirmaAlteracao() {
		if (validador.naoPodeAlterar(uf)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", validador.getMensagem()));
			return;
		}
		ufDao.atualiza(uf);
		ufs = ufDao.buscaTodasUfs();
		status = Status.LISTANDO;
	}

	@Transacional
	public void confirmaExclusao(Long ufId) {
		uf = ufDao.buscaPorId(ufId);
		ufDao.remove(uf);
		ufs.remove(uf);
		status = Status.LISTANDO;
	}

	public void cancelarAlteracao() {
		status = Status.LISTANDO;
		uf = null;
	}
}
