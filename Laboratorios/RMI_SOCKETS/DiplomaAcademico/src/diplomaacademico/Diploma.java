/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package diplomaacademico;
import java.io.Serializable;
/**
 *
 * @author David-Code
 */
public class Diploma implements Serializable{
    private String nombreCompleto;
    private String carrera;
    private String fecha;
    private String mensaje;

    public Diploma(String nombreCompleto, String carrera, String fecha, String mensaje) {
        this.nombreCompleto = nombreCompleto;
        this.carrera = carrera;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getCarrera() {
        return carrera;
    }

    public String getFecha() {
        return fecha;
    }

    public String getMensaje() {
        return mensaje;
    }
}
