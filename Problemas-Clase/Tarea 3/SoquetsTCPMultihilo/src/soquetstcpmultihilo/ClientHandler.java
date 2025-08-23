package soquetstcpmultihilo;

import java.io.*;
import java.net.Socket;

class ClientHandler extends Thread {

    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    double num1 = 0;
    double num2 = 0;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {
        String received;

        try {
            while (true) {
                
                dos.writeUTF(
                        "\n=== MENÚ ===\n"
                        + "1. Ingresar datos\n"
                        + "2. Sumar\n"
                        + "3. Restar\n"
                        + "4. Multiplicar\n"
                        + "5. Dividir\n"
                        + "6. Salir\n"
                        + "Elige una opción:"
                );

                
                received = dis.readUTF();
                System.out.println("Cliente " + s.getRemoteSocketAddress() + " eligió: " + received);

                if (received.equals("6")) {
                    dos.writeUTF("Saliendo...");
                    System.out.println("Cerrando la conexión con el cliente: " + s);
                    this.s.close();
                    break;
                }

                switch (received) {
                    case "1":
                
                        dos.writeUTF("Ingresa el primer número:");
                        try {
                            num1 = Double.parseDouble(dis.readUTF());
                        } catch (NumberFormatException e) {
                            dos.writeUTF("Número inválido. Vuelve a ingresar.");
                            break;
                        }

                        dos.writeUTF("Ingresa el segundo número:");
                        try {
                            num2 = Double.parseDouble(dis.readUTF());
                        } catch (NumberFormatException e) {
                            dos.writeUTF("Número inválido. Vuelve a ingresar.");
                            break;
                        }

                        dos.writeUTF("Datos guardados correctamente.");
                        break;

                    case "2":
                        dos.writeUTF("Resultado: " + (num1 + num2));
                        break;

                    case "3":
                        dos.writeUTF("Resultado: " + (num1 - num2));
                        break;

                    case "4":
                        dos.writeUTF("Resultado: " + (num1 * num2));
                        break;

                    case "5":
                        if (num2 == 0)
                            dos.writeUTF("Error: División entre cero no permitida.");
                        else
                            dos.writeUTF("Resultado: " + (num1 / num2));
                        break;

                    default:
                        dos.writeUTF("Opción inválida.");
                        break;
                }
            }
        } catch (IOException e) {
            
            System.out.println("Cliente desconectado: " + s.getRemoteSocketAddress());
        }
    }
}