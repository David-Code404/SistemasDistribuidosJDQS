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
public class ServidorSegip {

    
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);

            Segip segip = new Segip();
            Naming.bind("Segip", segip);
            System.out.println("Servidor SEGIP iniciado y registro creado en el puerto 1099.");

        } catch (RemoteException ex) {
            
            if (ex.getCause() instanceof java.net.BindException) {
                System.out.println("AVISO: El registro RMI en el puerto 1099 ya está en uso (esto es normal si otro servidor ya lo inició).");
            
                try {
                    Naming.rebind("Segip", new Segip()); 
                    System.out.println("Servidor SEGIP registrado en el registro existente.");
                } catch (Exception e) {
                    Logger.getLogger(ServidorSegip.class.getName()).log(Level.SEVERE, "Fallo al re-registrar", e);
                }
            } else {
                Logger.getLogger(ServidorSegip.class.getName()).log(Level.SEVERE, "Error de RemoteException", ex);
            }
        } catch (AlreadyBoundException ex) {
            System.err.println("AVISO: El nombre 'Segip' ya estaba registrado. Se volverá a registrar (rebind).");
            try {
                Naming.rebind("Segip", new Segip());
            } catch (Exception e) {
                Logger.getLogger(ServidorSegip.class.getName()).log(Level.SEVERE, "Fallo al re-registrar", e);
            }
        } catch (Exception e) {
            Logger.getLogger(ServidorSegip.class.getName()).log(Level.SEVERE, "Error inesperado", e);
        }
    }
}
