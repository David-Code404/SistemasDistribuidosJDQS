package soquetstcpmultihilo;

import java.io.*;
import java.net.*;

public class ServerMultihilo {
    public static void main(String[] args) throws IOException {
        
        String host = "0.0.0.0";
        ServerSocket serverSocket = new ServerSocket(5056,50,InetAddress.getByName(host));
        
        System.out.println("Servidor iniciado. Esperando clientes...");

        while (true) {
            Socket s = null;
            try {
        
                s = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + s);

        
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Creando un nuevo hilo para este cliente...");

        
                Thread t = new ClientHandler(s, dis, dos);
                t.start();
            } catch (IOException e) {
                if (s != null) {
                    s.close();
                }
                e.printStackTrace();
            }
        }
    }
}