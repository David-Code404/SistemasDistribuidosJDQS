/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author joses
 */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ICadenas servicio = (ICadenas) registry.lookup("ServicioCadenas");
            
            int opcion = 0;
            while (opcion != 5) {
                System.out.println("1. Guardar frase");
                System.out.println("2. Convertir Mayusculas");
                System.out.println("3. Duplicar espacios");
                System.out.println("4. Concatenar");
                System.out.println("5. Salir");
                System.out.print("Opcion: ");
                
                try {
                    opcion = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    opcion = 0;
                }

                switch (opcion) {
                    case 1:
                        System.out.print("Ingrese frase: ");
                        String f = scanner.nextLine();
                        System.out.println("Guardado: " + servicio.guardarFrase(f));
                        break;
                    case 2:
                        System.out.println("Resultado: " + servicio.convertirMayusculas());
                        break;
                    case 3:
                        System.out.print("Veces: ");
                        int v = Integer.parseInt(scanner.nextLine());
                        System.out.println("Resultado: " + servicio.duplicarEspacios(v));
                        break;
                    case 4:
                        System.out.print("Extra: ");
                        String ex = scanner.nextLine();
                        System.out.println("Resultado: " + servicio.concatenar(ex));
                        break;
                    case 5:
                        System.out.println("Fin");
                        break;
                    default:
                        System.out.println("Invalido");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}