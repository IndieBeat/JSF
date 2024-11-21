package modelo.dominio;

import java.util.Date;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class EventoLogin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String descripcion;
	private Date fecha;
	@ManyToOne
	private Usuario usuario;
	private boolean login;

	public EventoLogin() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
		if (login)
			this.descripcion = usuario.getNombre() + " ha hecho login correctamente";
		else
			this.descripcion = usuario.getNombre() + " ha intentado hacer login";
	}

	public String toString() {
		return id+"/"+descripcion+"/"+fecha+"/"+usuario+"/"+login;
	}
}
