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
public class Biblioteca {
    private String nombre;
    private double tamaño; // en metros cuadrados
    private ArrayList<Armario> armarios;

    public Biblioteca(String nombre, double tamaño) {
        this.nombre = nombre;
        this.tamaño = tamaño;
        this.armarios = new ArrayList<>();
    }

    public void agregarArmario(Armario armario) {
        armarios.add(armario);
    }

    public void listarBiblioteca() {
        System.out.println("Biblioteca: " + nombre + ", Tamaño: " + tamaño + " m²");
        for (Armario armario : armarios) {
            System.out.println("Armario: " + armario);
            armario.listarPublicaciones();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getTamaño() {
        return tamaño;
    }

    public void setTamaño(double tamaño) {
        this.tamaño = tamaño;
    }

    public ArrayList<Armario> getArmarios() {
        return armarios;
    }

    public void setArmarios(ArrayList<Armario> armarios) {
        this.armarios = armarios;
    }
    
}
