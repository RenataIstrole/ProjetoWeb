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

import br.com.fsma.projeto_web.filtros.MunicipioFiltro;
import br.com.fsma.projeto_web.modelo.classes.Municipio;
import br.com.fsma.projeto_web.modelo.classes.Uf;

@Named
@RequestScoped
public class MunicipioDao implements Serializable {

	private static final long serialVersionUID = 1L;

	private DAO<Municipio> dao;

	@PostConstruct
	void init() {
		this.dao = new DAO<Municipio>(this.em, Municipio.class);
	}

	@Inject
	private EntityManager em;

	public boolean existe(Municipio municipio) {

		TypedQuery<Municipio> query = em.createQuery(" select m from Municipio m " + " where m.nome = :pNome",
				Municipio.class);

		query.setParameter("pNome", municipio.getNome());
		try {
			@SuppressWarnings("unused")
			Municipio resultado = query.getSingleResult();
			return true;
		} catch (NoResultException ex) {
			return false;
		}
	}

	public void adiciona(Municipio municipio) {
		dao.adiciona(municipio);
	}

	public void atualiza(Municipio municipio) {
		em.merge(municipio);
	}

	public void remove(Municipio municipio) {
		dao.remove(municipio);
	}

	public Municipio buscaPorId(Long id) {
		return dao.buscaPorId(id);
	}

	public List<Municipio> buscaTodosMunicipios() {
		String jpql = "select m from Municipio m order by m.nome";
		TypedQuery<Municipio> query = em.createQuery(jpql, Municipio.class);
		return query.getResultList();
	}

	public List<Municipio> listaTodosPaginada(int firstResult, int maxResults) {
		return dao.listaTodosPaginada(firstResult, maxResults);
	}

	public Municipio buscaPorNome(String nome) {
		String jpql = " select m from Municipio m where m.nome = :pNome";
		TypedQuery<Municipio> query = em.createQuery(jpql, Municipio.class);
		query.setParameter("pNome", nome.trim());
		try {
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	public List<Municipio> busca(MunicipioFiltro filtro) {
		String jpql = "select m from Municipio m where m.uf.id = :idUf";

		if (filtro.getNomeMunicipio() != null && filtro.getNomeMunicipio().length() > 0) {
			jpql = jpql + " and m.nome like :nomeMunicipio";
		}

		TypedQuery<Municipio> query = em.createQuery(jpql, Municipio.class);
		query.setParameter("idUf", filtro.getUfId());

		if (filtro.getNomeMunicipio() != null && filtro.getNomeMunicipio().length() > 0) {
			query.setParameter("nomeMunicipio", filtro.getNomeMunicipio() + "%");
		}
		return query.getResultList();
	}

	public List<Municipio> buscaMunicipioPorUf(Long id) {
		String jpql = " select m from Municipio m where m.uf.id = :pId order by m.nome";
		TypedQuery<Municipio> query = em.createQuery(jpql, Municipio.class);
		query.setParameter("pId", id);
		try {
			return query.getResultList();
		} catch (NoResultException ex) {
			return null;
		}
	}

	public Municipio buscaMunicipioPorNomeUf(Uf uf, String nomeMunicipio) {
		String jpql = " select m from Municipio m where m.uf = :pUf and m.nome = :pNomeMunicipio";
		TypedQuery<Municipio> query = em.createQuery(jpql, Municipio.class);
		query.setParameter("pNomeMunicipio", nomeMunicipio);
		query.setParameter("pUf", uf);
		try {
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
