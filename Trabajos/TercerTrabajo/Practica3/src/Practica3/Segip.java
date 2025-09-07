/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author David-Code
 */
public class Segip extends UnicastRemoteObject implements ISegip {

    public Segip() throws RemoteException {
        super();
    }

    
    @Override
    public boolean Verificar(String ci, String nombres, String apellidos) throws RemoteException {
        System.out.println("SEGIP: Verificando CI: " + ci + ", Nombres: " + nombres + ", Apellidos: " + apellidos);

        // --- LÓGICA DE VALIDACIÓN MEJORADA Y GENERAL ---
        // En lugar de verificar una longitud exacta, ahora solo comprobamos
        // que los datos no lleguen vacíos.
        boolean isValid = !ci.isEmpty()
                && !nombres.isEmpty()
                && !apellidos.isEmpty();

        System.out.println("SEGIP: Resultado de verificacion: " + (isValid ? "Válido" : "Inválido"));
        return isValid;
    }

}
