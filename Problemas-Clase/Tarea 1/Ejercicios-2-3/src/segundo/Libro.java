/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segundo;

/**
 *
 * @author David
 */
public class Libro implements IPublicacion {
    private String nombre;
    private String autor;
    private String editorial;
    private int año;

    public Libro(String nombre, int año, String autor, String editorial) {
        this.nombre = nombre;
        this.año = año;
        this.autor = autor;
        this.editorial = editorial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    
    
    @Override
    public String detalles() {
        return "Libro: " + nombre + ", Autor: " + autor + ", Editorial: " + editorial + ", Año: " + año;
    }
    
}
