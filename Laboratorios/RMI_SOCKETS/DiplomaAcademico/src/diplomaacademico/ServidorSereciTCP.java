package diplomaacademico;

import java.net.*;
import java.io.*;

public class ServidorSereciTCP {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6789);
            System.out.println("Servidor SERECI listo...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String request = in.readLine();
                if (request.startsWith("Ver-fecha:")) {
                    String[] datos = request.split(":")[1].split(",");
                    String nombres = datos[0].trim();
                    String apellidos = datos[1].trim();
                    String fecha = datos[2].trim();

                    // Verificar los datos enviados
                    if (nombres.equals("Walter Jhamil") && apellidos.equals("Segovia Arellano") && fecha.equals("11-02-1996")) {
                        out.println("si:verificación correcta");
                    } else {
                        out.println("no:error fecha nacimiento no correcta");
                    }
                }
                clientSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
