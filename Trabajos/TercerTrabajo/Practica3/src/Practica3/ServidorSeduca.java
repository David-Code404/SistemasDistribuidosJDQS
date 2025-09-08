/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David-Code
 */
public class ServidorSeduca {
   

    public static void main(String[] args) {
        int port = 5002;
        ServerSocket server;
        String rude = "";
        String mensaje = "";

        try {
            server = new ServerSocket(port);
            System.out.println("Servidor SEDUCA iniciado en puerto " + port);

            while (true) {
                Socket client;
                PrintStream toClient;
                client = server.accept();

                BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                System.out.println("Cliente se conectó al servidor SEDUCA");

                String operacion = fromClient.readLine();
                String[] comandos = operacion.split("-");

                if (comandos.length == 2 && comandos[0].equals("verificar")) {
                    rude = comandos[1];
                    System.out.println("SEDUCA: Verificando RUDE: " + rude);

                    if (!rude.equals("ERROR") && rude.length() > 10) {
                        mensaje = "si";
                    } else {
                        mensaje = "no";
                    }

                    System.out.println("SEDUCA: Resultado de verificación RUDE: " + mensaje);
                } else {
                    mensaje = "error en formato de solicitud";
                }

                System.out.println("SEDUCA: Mensaje recibido: " + operacion);

                toClient = new PrintStream(client.getOutputStream());
                toClient.println("respuesta:" + mensaje);
                client.close();
            }
        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }
    }

}
