/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segundo;

/**
 *
 * @author David
 */
public class Revista implements IPublicacion {
    private String nombre;
    private String mes;
    private String tipo;
    private int año;

    public Revista(String nombre, int año, String mes, String tipo) {
        this.nombre = nombre;
        this.año = año;
        this.mes = mes;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    
    
    @Override
    public String detalles() {
        return "Revista: " + nombre + ", Mes: " + mes + ", Tipo: " + tipo + ", Año: " + año;
    }
    
}
