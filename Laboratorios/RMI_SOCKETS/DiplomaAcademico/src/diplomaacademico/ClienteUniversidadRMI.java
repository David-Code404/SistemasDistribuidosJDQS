/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package diplomaacademico;

/**
 *
 * @author David-Code
 */

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteUniversidadRMI {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            IUniversidad universidad = (IUniversidad) registry.lookup("Universidad");

            
            String ci = "1140506";
            String nombres = "Walter Jhamil";
            String primerApellido = "Segovia";
            String segundoApellido = "Arellano";
            String fechaNacimiento = "11-02-1996";
            String carrera = "Ing. en Ciencias de la Computación";

            
            Diploma diploma = universidad.emitirDiploma(ci, nombres, primerApellido, segundoApellido, fechaNacimiento, carrera);

            // Mostrar resultado
            if (diploma.getMensaje().isEmpty()) {
                System.out.println("Diploma emitido correctamente:");
                System.out.println("Nombre Completo: " + diploma.getNombreCompleto());
                System.out.println("Carrera: " + diploma.getCarrera());
                System.out.println("Fecha: " + diploma.getFecha());
            } else {
                System.out.println("Errores en la emisión del diploma: " + diploma.getMensaje());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
