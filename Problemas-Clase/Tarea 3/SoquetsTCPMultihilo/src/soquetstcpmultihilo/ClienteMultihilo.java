package soquetstcpmultihilo;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteMultihilo {

    public static void main(String[] args) throws IOException {
        try {
            Scanner scn = new Scanner(System.in);
            InetAddress ip = InetAddress.getByName("localhost");
            Socket s = new Socket(ip, 5056);

            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            while (true) {
                
                System.out.println(dis.readUTF());

                
                String tosend = scn.nextLine();
                dos.writeUTF(tosend);

                
                if (tosend.equals("6")) {
                    String finalMessage = dis.readUTF();
                    System.out.println(finalMessage);
                    System.out.println("Cerrando esta conexión: " + s);
                    s.close();
                    System.out.println("Conexión cerrada.");
                    break;
                }

                
                if (tosend.equals("1")) {
                
                    System.out.println(dis.readUTF());
                    dos.writeUTF(scn.nextLine());

                
                    System.out.println(dis.readUTF());
                    dos.writeUTF(scn.nextLine());
                    
                    
                    System.out.println(dis.readUTF());

                } else {
                    
                    String received = dis.readUTF();
                    System.out.println(received);
                }
            }

            
            scn.close();
            dis.close();
            dos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}