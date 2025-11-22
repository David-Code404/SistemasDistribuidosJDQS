/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author joses
 */
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

public class Servidor extends UnicastRemoteObject implements ICadenas {
    private String cadenaActual = "";

    public Servidor() throws RemoteException {
        super();
    }

    public boolean guardarFrase(String frase) throws RemoteException {
        this.cadenaActual = frase;
        return true;
    }

    public String convertirMayusculas() throws RemoteException {
        this.cadenaActual = this.cadenaActual.toUpperCase();
        return this.cadenaActual;
    }

    public String duplicarEspacios(int veces) throws RemoteException {
        String reemplazo = "";
        for (int i = 0; i < veces; i++) {
            reemplazo += " ";
        }
        this.cadenaActual = this.cadenaActual.replace(" ", reemplazo);
        return this.cadenaActual;
    }

    public String concatenar(String extra) throws RemoteException {
        this.cadenaActual = this.cadenaActual + extra;
        return this.cadenaActual;
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServicioCadenas", new Servidor());
            System.out.println("Servidor iniciado en puerto 1099");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}