/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package diplomaacademico;

/**
 *
 * @author David-Code
 */
import java.net.*;

public class ServidorSeducaUDP {
    public static void main(String[] args) {
        
        try {
            DatagramSocket socket = new DatagramSocket(9876);
            byte[] buffer = new byte[1024];
            System.out.println("Servidor SEDUCA listo...");

            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);
                String received = new String(request.getData(), 0, request.getLength());

                if (received.startsWith("verificar-rude:")) {
                    String rude = received.split(":")[1];
                    String responseMessage = rude.equals("WaSeAr11021996")
                            ? "si:verificado con éxito"
                            : "no:no se encontró el título de bachiller";
                    
                    byte[] response = responseMessage.getBytes();
                    DatagramPacket responsePacket = new DatagramPacket(response, response.length, request.getAddress(), request.getPort());
                    socket.send(responsePacket);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
