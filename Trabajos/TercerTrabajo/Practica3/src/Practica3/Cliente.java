/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Practica3;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David-Code
 */

public class Cliente {

    public static void main(String[] args) {
        IUniversidad universidad;
        Diploma diploma;
        // Creamos un objeto Scanner para leer la entrada del usuario
        Scanner scanner = new Scanner(System.in); 

        try {
            System.out.println("Cliente: Conectando con el Servidor Universidad...");
            universidad = (IUniversidad) Naming.lookup("rmi://localhost/Universidad"); // Se conecta al servidor RMI

            // --- INICIO DEL FORMULARIO POR TECLADO ---
            System.out.println("\n--- Formulario de Solicitud de Diploma ---");
            System.out.print("Ingrese CI: ");
            String ci = scanner.nextLine();

            System.out.print("Ingrese Nombres: ");
            String nombres = scanner.nextLine();

            System.out.print("Ingrese Primer Apellido: ");
            String primerApellido = scanner.nextLine();

            System.out.print("Ingrese Segundo Apellido: ");
            String segundoApellido = scanner.nextLine();
            
            System.out.print("Ingrese Fecha de Nacimiento (ej: 11-2-1996): ");
            String fechaNacimiento = scanner.nextLine();

            // --- Menú para seleccionar la Carrera ---
            System.out.println("Seleccione la Carrera:");
            System.out.println("1. Sistemas");
            System.out.println("2. TI");
            System.out.println("3. CienciaComputacion");
            System.out.print("Opción: ");
            int opcionCarrera = scanner.nextInt();
            
            Carrera carreraSeleccionada;
            switch (opcionCarrera) {
                case 1:
                    carreraSeleccionada = Carrera.Sistemas;
                    break;
                case 2:
                    carreraSeleccionada = Carrera.TI;
                    break;
                case 3:
                    carreraSeleccionada = Carrera.CienciaComputacion;
                    break;
                default:
                    System.out.println("Opción no válida. Se usará CienciaComputacion por defecto.");
                    carreraSeleccionada = Carrera.CienciaComputacion;
                    break;
            }
            // --- FIN DEL FORMULARIO ---

            System.out.println("\nCliente: Enviando datos al servidor para su verificación...");
            // Se llama al método remoto con los datos ingresados por el usuario
            diploma = universidad.EmitirDiploma(ci, nombres, primerApellido, segundoApellido, fechaNacimiento, carreraSeleccionada);
            
            System.out.println("--- Resultado de la Solicitud ---");
            if (diploma.getMensaje().equals("")) {
                System.out.println("¡Verificación completada! Diploma emitido:");
                // Se imprime el diploma usando el método toString() que definiste
                System.out.println(diploma);
            } else {
                 System.out.println("No se pudo emitir el diploma por los siguientes motivos:");
                 System.out.println(diploma.getMensaje());
            }

        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            System.err.println("Error en el cliente: " + ex.getMessage());
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            scanner.close(); // Se cierra el scanner para liberar recursos
        }
    }
}