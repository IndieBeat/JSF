package principal;

import javax.persistence.EntityManager;

import modelo.JPAUtil;
import modelo.dominio.Usuario;

public class VerSQLRel extends CrearEventos {

	public VerSQLRel() {
		super();
	}

	public void printObjMemBD(String desc, Usuario u) {
		System.out.print("\tMem:<" + u + "> DB:<" + VerEventosUsandoJDBC.getUsuarioJDBC(u) + "> =>");
		System.out.println(desc);
	}
	
	public static void main(String[] args) {
		VerSQLRel e = new VerSQLRel();

		System.out.println("Ciclo de vida de los objetos:");
		System.out.println("======================");
		Usuario u = new Usuario();
		u.setNombre("Nerea");
		u.setPassword("1234");
		u.setTipo("profesor");
		System.out.println("new => TRANSIENT");
		e.printObjMemBD("Nerea está en memoria, pero no en la BD ", u);
		EntityManager em = JPAUtil.getEntityManager();

		try {
			em.getTransaction().begin();
			em.persist(u);

			System.out.println("save => MANAGED");
			e.printObjMemBD("Nerea todavía no está en la BD porque no se ha hecho commit", u);
			u.setPassword("1235");
			e.printObjMemBD(
					"Se ha ejecutado u.setPassword(\"1235\") pero no commit. Por tanto, Nerea todavía no está en la BD.",
					u);
			em.getTransaction().commit();

			System.out.println("close (commit) => DETACHED");
			e.printObjMemBD("Se ha hecho commit, se los cambios de han hecho en la BD ", u);
			u.setPassword("1236");
			e.printObjMemBD(
					"Se ha ejecutado u.setPassword(\"1236\"), pero el objeto no está conectado con la BD (detached)",
					u);
		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em.close();
		}

		em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(u);
			em.getTransaction().commit();
		} catch (Exception ex) {
			System.out.println(
					"save => ERROR: como el objeto está 'detached', save intenta meter el mismo objeto de nuevo y saltará un error de clave primaria.");
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
		} finally {
			em.close();
		}

		em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(u);
			System.out.println("merge => MANAGED");
			e.printObjMemBD(
					"ahora el objeto es gestionado, pero para poder ver la nueva contraseña, habrá que hacer commit",
					u);
			em.getTransaction().commit();
			System.out.println("close (commit) => DETACHED");
			e.printObjMemBD("\tSe ha hecho commit, se ve la nueva contraseña en la base de datos", u);
		} catch (Exception ex) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw ex;
		} finally {
			em.close();
		}

		u.setPassword("1237");
		e.printObjMemBD(
				"Se ha ejecutado u.setPassword(\"1237\"), pero el objeto no está conectado a la base de datos (detached)",
				u);
		em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			u = em.find(Usuario.class, u.getNombre());
			System.out.println("find => MANAGED");
			e.printObjMemBD(
					"Se ha vuelto a recuperar el usuario de la base de datos, por tanto, se ha perdido el cambio de la contraseña",
					u);
			em.remove(u);
			System.out.println("remove => REMOVED");
			e.printObjMemBD("Hasta que se haga commit no se borrará el objeto de la base de datos.", u);
			em.getTransaction().commit();
			System.out.println("close (commit) => DETACHED");
			e.printObjMemBD("Se ha hecho commit, no está en la base de datos pero sí en memoria.", u);
		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em.close();
		}
	}
}
