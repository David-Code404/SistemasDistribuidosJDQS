/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segundo;

import java.util.ArrayList;

/**
 *
 * @author David
 */
public class Periodico implements IPublicacion {
    private String nombre;
    private String fecha;
    private int año;
    private ArrayList<String> suplementos;

    public Periodico(String nombre, int año, String fecha, ArrayList<String> suplementos) {
        this.nombre = nombre;
        this.año = año;
        this.fecha = fecha;
        this.suplementos = suplementos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public ArrayList<String> getSuplementos() {
        return suplementos;
    }

    public void setSuplementos(ArrayList<String> suplementos) {
        this.suplementos = suplementos;
    }

    
    
    @Override
    public String detalles() {
        return "Periódico: " + nombre + ", Fecha: " + fecha + ", Año: " + año + ", Suplementos: " + suplementos;
    }
    
}
