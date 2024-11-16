package modelo.bean;

import java.io.Serializable;
import java.util.Date;

import org.primefaces.event.SelectEvent;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named("login")
@SessionScoped
public class LoginBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String password;
	private Date fecha;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public String comprobar() {
		if(nombre.length()!=password.length()) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(
					"Error: La longitud del nombre y de la contraseña son diferentes."));
			return null;
		}
		
		if (nombre.equals("pirata")) {
			return "error";
		} else {
			return "ok";
		}
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public void onDateSelect(SelectEvent event) {
		FacesContext.getCurrentInstance().addMessage(null,
		new FacesMessage("Fecha escogida: "+event.getObject()));
	}
}