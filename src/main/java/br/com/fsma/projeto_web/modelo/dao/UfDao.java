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

import br.com.fsma.projeto_web.modelo.classes.Uf;

@Named
@RequestScoped
public class UfDao implements Serializable {

	private static final long serialVersionUID = 1L;

	private DAO<Uf> dao;

	@PostConstruct
	void init() {
		this.dao = new DAO<Uf>(this.em, Uf.class);
	}

	@Inject
	private EntityManager em;

	public boolean existe(Uf uf) {

		TypedQuery<Uf> query = em.createQuery(" select u from Uf u " + " where u.sigla = :pSigla", Uf.class);

		query.setParameter("pSigla", uf.getSigla());
		try {
			@SuppressWarnings("unused")
			Uf resultado = query.getSingleResult();
			return true;
		} catch (NoResultException ex) {
			return false;
		}
	}

	public void adiciona(Uf uf) {
		dao.adiciona(uf);
	}

	public void atualiza(Uf uf) {
		em.merge(uf);
	}

	public void remove(Uf uf) {
		dao.remove(uf);
	}

	public Uf buscaPorId(Long id) {
		return dao.buscaPorId(id);
	}

	public List<Uf> buscaTodasUfs() {
		String jpql = "select u from Uf u order by u.nome";
		TypedQuery<Uf> query = em.createQuery(jpql, Uf.class);
		return query.getResultList();
	}

	public List<Uf> listaTodosPaginada(int firstResult, int maxResults) {
		return dao.listaTodosPaginada(firstResult, maxResults);
	}

	public Uf buscaPorSigla(String sigla) {
		String jpql = " select u from Uf u where u.sigla = :pSigla";
		TypedQuery<Uf> query = em.createQuery(jpql, Uf.class);
		query.setParameter("pSigla", sigla);
		try {
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	public Uf buscaPorNome(String nome) {
		String jpql = " select u from Uf u where u.nome = :pNome";
		TypedQuery<Uf> query = em.createQuery(jpql, Uf.class);
		query.setParameter("pNome", nome);
		try {
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	
}
