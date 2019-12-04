package br.com.fsma.projeto_web.modelo.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.fsma.projeto_web.filtros.BairroFiltro;
import br.com.fsma.projeto_web.modelo.classes.Bairro;

@Named
@RequestScoped
public class BairroDao implements Serializable {

	private static final long serialVersionUID = 1L;

	private DAO<Bairro> dao;

	@PostConstruct
	void init() {
		this.dao = new DAO<Bairro>(this.em, Bairro.class);
	}

	@Inject
	private EntityManager em;

	public boolean existe(Bairro bairro) {

		TypedQuery<Bairro> query = em.createQuery(" select b from Bairro b " + " where b.nome = :pNome", Bairro.class);
		
		query.setParameter("pNome", bairro.getNome());
		try {
			@SuppressWarnings("unused")
			Bairro resultado = query.getSingleResult();
			return true;
		} catch (NoResultException ex) {
			return false;
		}
	}

	public void adiciona(Bairro bairro) {
		dao.adiciona(bairro);
	}

	public void atualiza(Bairro bairro) {
		em.merge(bairro);
	}

	public void remove(Bairro bairro) {
		dao.remove(bairro);
	}

	public Bairro buscaPorId(Long id) {
		return dao.buscaPorId(id);
	}

	public List<Bairro> buscaTodosBairros() {
		String jpql = "select b from Bairro b order by b.nome";
		TypedQuery<Bairro> query = em.createQuery(jpql, Bairro.class);
		return query.getResultList();
	}

	public List<Bairro> listaTodosPaginada(int firstResult, int maxResults) {
		return dao.listaTodosPaginada(firstResult, maxResults);
	}

	public Bairro buscaPorNome(String nome) {
		String jpql = " select b from Bairro b where b.nome = :pNome";
		TypedQuery<Bairro> query = em.createQuery(jpql, Bairro.class);
		query.setParameter("pNome", nome.trim());
		try {
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	public List<Bairro> busca(BairroFiltro filtro) {
		String jpql = "select b from Bairro b where b.municipio.id = :idMunicipio";

		if (filtro.getNomeBairro() != null && filtro.getNomeBairro().length() > 0) {
			jpql = jpql + " and b.nome like :nomeBairro";
		}

		TypedQuery<Bairro> query = em.createQuery(jpql, Bairro.class);
		query.setParameter("idMunicipio", filtro.getMunicipioId());

		if (filtro.getNomeBairro() != null && filtro.getNomeBairro().length() > 0) {
			query.setParameter("nomeBairro", filtro.getNomeBairro() + "%");
		}

		System.out.println(".............. " + jpql);
		return query.getResultList();
		
	}

	public List<Bairro> buscaBairroPorMunicipio(Long municipioId) {
		String jpql = " select b from Bairro b where b.municipio.id = :pId order by b.nome";
		TypedQuery<Bairro> query = em.createQuery(jpql, Bairro.class);
		query.setParameter("pId", municipioId);
		try {
			return query.getResultList();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
