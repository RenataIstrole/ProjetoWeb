package br.com.fsma.projeto_web.modelo.dao;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.fsma.projeto_web.modelo.classes.Empresa;

public class EmpresaDao implements Serializable {

	private static final long serialVersionUID = 1L;

	private DAO<Empresa> dao;

	@PostConstruct
	void init() {
		this.dao = new DAO<Empresa>(this.em, Empresa.class);
	}

	@Inject
	private EntityManager em;

	public Object buscaBairroPorMunicipio(long id) {
		String jpql = " select e from Empresa e where e.municipio.id = :pid";
		TypedQuery<Empresa> query = em.createQuery(jpql, Empresa.class);
		query.setParameter("pid", id);
		try {
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
