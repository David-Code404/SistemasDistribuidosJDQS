/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package diplomaacademico;

/**
 *
 * @author David-Code
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorSegipRMI implements ISegip{
    protected ServidorSegipRMI() throws RemoteException {
        super();
    }

    public Respuesta verificarCI(String ci) throws RemoteException {
        if ("1140506".equals(ci)) {
            return new Respuesta(true, "Los Datos son correctos.");
        } else {
            return new Respuesta(false, "Los Datos del CI no son correctos.");
        }
    }

    public static void main(String[] args) {
        
        try {
            ServidorSegipRMI segip = new ServidorSegipRMI();
            Registry registry = LocateRegistry.createRegistry(1098);
            registry.rebind("SEGIP", segip);
            System.out.println("Servidor SEGIP está listo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
