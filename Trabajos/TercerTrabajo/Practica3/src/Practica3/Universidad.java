/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author David-Code
 */
public class Universidad extends UnicastRemoteObject implements IUniversidad {
    
    public Universidad() throws RemoteException {
        super();
    }
    
    @Override
    public Diploma EmitirDiploma(String ci, String nombres, String primerApellido, String segundoApellido, String fecha_nacimiento, Carrera carrera) throws RemoteException {
        Diploma aux = null;
        String mensajeError = "";
        boolean datosValidos = true;
        String nombreCompleto = nombres + " " + primerApellido + " " + segundoApellido;
        
        // Calcular RUDE
        String rude = calcularRude(nombres, primerApellido, segundoApellido, fecha_nacimiento);
        
        try {
        
            System.out.println("Universidad: Consultando a SEGIP...");
            ISegip segip;
            boolean verificacionSegip = false;
            
            try {
                segip = (ISegip) Naming.lookup("rmi://localhost/Segip"); // instanciar un objeto remoto
                verificacionSegip = segip.Verificar(ci, nombres, primerApellido + " " + segundoApellido);
                System.out.println("Universidad: Resultado de SEGIP: " + verificacionSegip);
                
                if (!verificacionSegip) {
                    datosValidos = false;
                    mensajeError += "Los datos de CI no son correctos. ";
                }
                
            } catch (NotBoundException | MalformedURLException ex) {
                Logger.getLogger(Universidad.class.getName()).log(Level.SEVERE, null, ex);
                datosValidos = false;
                mensajeError += "Error al conectar con SEGIP. ";
            }
            
        
            System.out.println("Universidad: Consultando a SEDUCA...");
            boolean verificacionSeduca = false;
            
            try {
                int port = 5002;
                Socket client = new Socket("localhost", port);
                PrintStream toServer = new PrintStream(client.getOutputStream());
                BufferedReader fromServer = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
                
                toServer.println("verificar-" + rude);
                String result = fromServer.readLine();
                System.out.println("Universidad: Respuesta de SEDUCA: " + result);
                
                if (result.startsWith("respuesta:")) {
                    String respuesta = result.substring("respuesta:".length());
                    verificacionSeduca = respuesta.equals("si");
                    
                    if (!verificacionSeduca) {
                        datosValidos = false;
                        mensajeError += "No se encontro titulo de bachiller. ";
                    }
                } else {
                    datosValidos = false;
                    mensajeError += "Error en la respuesta de SEDUCA. ";
                }
                
                client.close();
                
            } catch (IOException ex) {
                Logger.getLogger(Universidad.class.getName()).log(Level.SEVERE, null, ex);
                datosValidos = false;
                mensajeError += "Error al conectar con SEDUCA. ";
            }
            
            // 3. Llamar a SERECI (código de cliente UDP)
            System.out.println("Universidad: Consultando a SERECI...");
            boolean verificacionSereci = false;
            
            try {
                DatagramSocket socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName("localhost");
                int port = 5003;
                
                String mensaje = "Ver-fecha:" + nombres + "," + primerApellido + " " + segundoApellido + "," + fecha_nacimiento;
                byte[] buffer = mensaje.getBytes();
                
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
                System.out.println("Universidad: Enviando solicitud a SERECI: " + mensaje);
                
                // Esperar respuesta
                buffer = new byte[1024];
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                
                String respuesta = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Universidad: Respuesta de SERECI: " + respuesta);
                
                verificacionSereci = respuesta.equals("verificacion correcta");
                
                if (!verificacionSereci) {
                    datosValidos = false;
                    mensajeError += "Fecha de nacimiento incorrecta. ";
                }
                
                socket.close();
                
            } catch (SocketException | UnknownHostException ex) {
                Logger.getLogger(Universidad.class.getName()).log(Level.SEVERE, null, ex);
                datosValidos = false;
                mensajeError += "Error al conectar con SERECI. ";
            } catch (IOException ex) {
                Logger.getLogger(Universidad.class.getName()).log(Level.SEVERE, null, ex);
                datosValidos = false;
                mensajeError += "Error en la comunicacion con SERECI. ";
            }
            
        
            if (datosValidos) {
                aux = new Diploma(nombreCompleto, carrera, java.time.LocalDate.now().toString(), "");
            } else {
                aux = new Diploma("", null, "", mensajeError);
            }
            
            return aux;
            
        } catch (Exception ex) {
            Logger.getLogger(Universidad.class.getName()).log(Level.SEVERE, null, ex);
            return new Diploma("", null, "", "Error en el proceso de emision de diploma: " + ex.getMessage());
        }
    }
    
    private String calcularRude(String nombres, String primerApellido, String segundoApellido, String fechaNacimiento) {
    try {
        String parte1 = nombres.substring(0, 2);
        String parte2 = primerApellido.substring(0, 2);
        String parte3 = segundoApellido.substring(0, 2);

        
        String[] partesFecha = fechaNacimiento.split("[ /\\-]");
        String fechaFormateada = partesFecha[0] + partesFecha[1] + partesFecha[2];
        
        return parte1 + parte2 + parte3 + fechaFormateada;
    } catch (Exception e) {
        System.out.println("Error al calcular RUDE: " + e.getMessage());
        return "ERROR";
    }
}
}