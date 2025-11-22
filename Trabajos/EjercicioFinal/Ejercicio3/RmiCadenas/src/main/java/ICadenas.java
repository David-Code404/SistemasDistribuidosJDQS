/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

/**
 *
 * @author joses
 */
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICadenas extends Remote {
    boolean guardarFrase(String frase) throws RemoteException;
    String convertirMayusculas() throws RemoteException;
    String duplicarEspacios(int veces) throws RemoteException;
    String concatenar(String extra) throws RemoteException;
}