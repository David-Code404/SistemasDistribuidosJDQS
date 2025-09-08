/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David-Code
 */
public class ServidorUniversidad {
    

    
    public static void main(String[] args) {
        try {
    
            LocateRegistry.createRegistry(1099);

            Universidad universidad = new Universidad();
            Naming.bind("Universidad", universidad);
            System.out.println("Servidor Universidad iniciado y registrado correctamente.");

        } catch (RemoteException ex) {
    
            if (ex.getCause() instanceof java.net.BindException) {
                System.out.println("AVISO en Universidad: El registro RMI ya está en uso (¡Perfecto! Usaremos el existente).");
                try {
    
                    Naming.rebind("Universidad", new Universidad());
                    System.out.println("Servidor Universidad registrado en el registro existente.");
                } catch (Exception e) {
                    Logger.getLogger(ServidorUniversidad.class.getName()).log(Level.SEVERE, "Fallo al re-registrar", e);
                }
            } else {
                Logger.getLogger(ServidorUniversidad.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (AlreadyBoundException ex) {
            System.err.println("AVISO: El nombre 'Universidad' ya estaba registrado. Se volverá a registrar (rebind).");
            try {
                Naming.rebind("Universidad", new Universidad());
            } catch (Exception e) {
                Logger.getLogger(ServidorUniversidad.class.getName()).log(Level.SEVERE, "Fallo al re-registrar", e);
            }
        } catch (Exception e) {
            Logger.getLogger(ServidorUniversidad.class.getName()).log(Level.SEVERE, "Error inesperado", e);
        }
    }

}
