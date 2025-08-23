/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package segundo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


/**
 *
 * @author David
 */
public class Segundo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Crear publicaciones
            Libro libro = new Libro("El Quijote", 1605, "Miguel de Cervantes", "Editorial XYZ");
            Revista revista = new Revista("Revista de Ciencia", 2022, "Enero", "Técnica");
            ArrayList<String> suplementos = new ArrayList<>();
            suplementos.add("Revista");
            suplementos.add("Crucigrama");
            Periodico periodico = new Periodico("El Diario", 2023, "2023-02-17", suplementos);

            // Crear armarios
            ArrayList<IPublicacion> publicacionesArmario1 = new ArrayList<>();
            publicacionesArmario1.add(libro);
            publicacionesArmario1.add(revista);
            Armario armario1 = new Armario("A001", "Madera", publicacionesArmario1);

            ArrayList<IPublicacion> publicacionesArmario2 = new ArrayList<>();
            publicacionesArmario2.add(periodico);
            Armario armario2 = new Armario("A002", "Metálico", publicacionesArmario2);

            // Crear biblioteca
            Biblioteca biblioteca = new Biblioteca("Biblioteca Central", 500.0);
            biblioteca.agregarArmario(armario1);
            biblioteca.agregarArmario(armario2);

            // Guardar los datos en la base de datos
            guardarBiblioteca(biblioteca);
            System.out.println("Datos guardados correctamente en la base de datos.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void guardarBiblioteca(Biblioteca biblioteca) throws SQLException {
        String query = "INSERT INTO biblioteca (nombre, tamaño) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, biblioteca.getNombre());
            stmt.setDouble(2, biblioteca.getTamaño());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int bibliotecaId = rs.getInt(1);
                for (Armario armario : biblioteca.getArmarios()) {
                    guardarArmario(armario, bibliotecaId);
                }
            }
        }
    }

    public static void guardarArmario(Armario armario, int bibliotecaId) throws SQLException {
        String query = "INSERT INTO armario (codigo, material, biblioteca_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, armario.getCodigo());
            stmt.setString(2, armario.getMaterial());
            stmt.setInt(3, bibliotecaId);
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int armarioId = rs.getInt(1);
                for (IPublicacion publicacion : armario.getPublicaciones()) {
                    guardarPublicacion(publicacion, armarioId);
                }
            }
        }
    }

    public static void guardarPublicacion(IPublicacion publicacion, int armarioId) throws SQLException {
        String query = "INSERT INTO publicacion (tipo, nombre, año, autor, editorial, mes, tipo_revista, fecha, armario_id) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, publicacion.getClass().getSimpleName());
            stmt.setString(2, publicacion.getNombre());
            stmt.setInt(3, publicacion.getAño());
            stmt.setString(4, publicacion instanceof Libro ? ((Libro) publicacion).getAutor() : null);
            stmt.setString(5, publicacion instanceof Libro ? ((Libro) publicacion).getEditorial() : null);
            stmt.setString(6, publicacion instanceof Revista ? ((Revista) publicacion).getMes() : null);
            stmt.setString(7, publicacion instanceof Revista ? ((Revista) publicacion).getTipo() : null);
            stmt.setDate(8, publicacion instanceof Periodico ? Date.valueOf(((Periodico) publicacion).getFecha()) : null);
            stmt.setInt(9, armarioId);
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int publicacionId = rs.getInt(1);
                if (publicacion instanceof Periodico) {
                    guardarSuplementos((Periodico) publicacion, publicacionId);
                }
            }
        }
    }

    public static void guardarSuplementos(Periodico periodico, int publicacionId) throws SQLException {
        String query = "INSERT INTO suplemento (tipo, periodico_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            for (String suplemento : periodico.getSuplementos()) {
                stmt.setString(1, suplemento);
                stmt.setInt(2, publicacionId);
                stmt.executeUpdate();
            }
        }
    }
}
