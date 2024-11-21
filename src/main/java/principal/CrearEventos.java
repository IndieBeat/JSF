package principal;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import modelo.JPAUtil;
import modelo.dominio.EventoLogin;
import modelo.dominio.Usuario;

import java.util.*;

public class CrearEventos {
	public CrearEventos() {
	}

	private void createAndStoreEventoLogin(String usuario, boolean login, Date fecha) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();

			TypedQuery<Usuario> q = em.createQuery("SELECT u FROM Usuario u WHERE u.nombre =:usuario", Usuario.class);
			q.setParameter("usuario", usuario);
			List<Usuario> result = q.getResultList();

			if (!result.isEmpty()) {
				EventoLogin e = new EventoLogin();
				e.setUsuario(result.get(0));
				e.setLogin(login);
				e.setFecha(fecha);
				em.persist(e);
				em.getTransaction().commit();
			} else {
				System.out.println("Error al crear instancia de EventoLogin: no existe usuario " + usuario);
				em.getTransaction().rollback();
			}
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	private List<EventoLogin> listaEventos() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			List<EventoLogin> result = em.createQuery("from EventoLogin", EventoLogin.class).getResultList();
			em.getTransaction().commit();
			return result;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	private void createAndStoreUsuarios(String nombre, String password, String tipo) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Usuario u = new Usuario();
			u.setNombre(nombre);
			u.setPassword(password);
			u.setTipo(tipo);
			em.persist(u);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	public static void main(String[] args) {
		CrearEventos e = new CrearEventos();
		System.out.println("Creación de eventos:");

		e.createAndStoreUsuarios("Ane", "125", "alumno");

		e.createAndStoreEventoLogin("Ane", true, new Date());
		e.createAndStoreEventoLogin("Ane", false, new Date());

		System.out.println("Listado de eventos:");

		EntityManager em = JPAUtil.getEntityManager();

		try {
			em.getTransaction().begin();
			List<EventoLogin> result = em.createQuery("from EventoLogin", EventoLogin.class).getResultList();

			for (int i = 0; i < result.size(); i++) {
				EventoLogin ev = (EventoLogin) result.get(i);
				System.out.println("Id: " + ev.getId() + " Descripción: " + ev.getDescripcion() + " Fecha: "
						+ ev.getFecha() + " Login: " + ev.isLogin());
			}
			em.getTransaction().commit();
		} catch (Exception ex) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw ex;
		} finally {
			em.close();
		}
	}
}