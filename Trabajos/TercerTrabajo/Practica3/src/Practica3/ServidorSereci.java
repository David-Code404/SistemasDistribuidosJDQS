/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David-Code
 */
public class ServidorSereci {

    public static void main(String[] args) {
        int port = 5003;
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(port);
            System.out.println("Servidor SERECI iniciado en puerto " + port);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("SERECI: Solicitud recibida: " + received);

                // Format: Ver-fecha:Nombres,Apellidos,fecha
                String[] parts = received.split(":");
                if (parts.length == 2) {
                    String[] datos = parts[1].split(",");
                    if (datos.length == 3) {
                        String nombres = datos[0];
                        String apellidos = datos[1];
                        String fechaNacimiento = datos[2];

                        boolean fechaValida = verificarFechaNacimiento(fechaNacimiento);

                        String response = fechaValida ? "verificacion correcta" : "error fecha nacimiento no correcta";

                        DatagramPacket responsePacket = new DatagramPacket(
                                response.getBytes(),
                                response.getBytes().length,
                                packet.getAddress(),
                                packet.getPort()
                        );

                        socket.send(responsePacket);
                        System.out.println("SERECI: Respuesta enviada: " + response);
                    }
                }

                // Limpiar el buffer para la próxima solicitud
                buffer = new byte[1024];
            }
        } catch (SocketException ex) {
            Logger.getLogger(ServidorSereci.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServidorSereci.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    // Reemplaza el método verificarFechaNacimiento en ServidorSereci.java con este:
    private static boolean verificarFechaNacimiento(String fecha) {
        if (fecha == null || fecha.isEmpty()) {
            return false;
        }

        // Usamos la misma expresión regular para aceptar guiones, barras o espacios
        String[] parts = fecha.split("[ /\\-]");
        if (parts.length != 3) {
            return false;
        }

        try {
            int dia = Integer.parseInt(parts[0]);
            int mes = Integer.parseInt(parts[1]);
            int anio = Integer.parseInt(parts[2]);

            // Validaciones básicas (se pueden mejorar, pero cumplen el objetivo)
            if (dia < 1 || dia > 31) {
                return false;
            }
            if (mes < 1 || mes > 12) {
                return false;
            }
            if (anio < 1900 || anio > 2025) {
                return false; // Actualizado al año actual
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
