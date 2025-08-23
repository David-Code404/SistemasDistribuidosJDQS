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
public class Armario {
    private String codigo;
    private String material; // madera o metálico
    private ArrayList<IPublicacion> publicaciones;

    public Armario(String codigo, String material, ArrayList<IPublicacion> publicaciones) {
        this.codigo = codigo;
        this.material = material;
        this.publicaciones = publicaciones;
    }

    public void agregarPublicacion(IPublicacion publicacion) {
        publicaciones.add(publicacion);
    }

    public void listarPublicaciones() {
        for (IPublicacion pub : publicaciones) {
            System.out.println(pub.detalles());
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public ArrayList<IPublicacion> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(ArrayList<IPublicacion> publicaciones) {
        this.publicaciones = publicaciones;
    }
    
}
