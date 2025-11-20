/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package diplomaacademico;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.net.*;
import java.io.*;

public class ServidorUniversidadRMI extends UnicastRemoteObject implements IUniversidad {

    // Constructor
    protected ServidorUniversidadRMI() throws RemoteException {
        super();
    }

    // Método remoto para emitir diploma
    @Override
    public Diploma emitirDiploma(String ci, String nombres, String primerApellido, String segundoApellido, String fechaNacimiento, String carrera) throws RemoteException {
        StringBuilder errores = new StringBuilder();

        // Verificación con SEGIP (RMI)
        try {
            Registry registrySEGIP = LocateRegistry.getRegistry("localhost", 1098);
            ISegip segip = (ISegip) registrySEGIP.lookup("SEGIP");
            Respuesta segipRespuesta = segip.verificarCI(ci);
            if (!segipRespuesta.isEstado()) {
                errores.append(segipRespuesta.getMensaje()).append("\n");
            }
        } catch (Exception e) {
            errores.append("Error al verificar con SEGIP.\n");
        }

        // Verificación con SEDUCA (UDP)
        try {
            String rude = generarRUDE(nombres, primerApellido, segundoApellido, fechaNacimiento);
            String mensajeSeduca = verificarSeduca(rude);
            if (mensajeSeduca.startsWith("no")) {
                errores.append(mensajeSeduca).append("\n");
            }
        } catch (Exception e) {
            errores.append("Error al verificar con SEDUCA.\n");
        }

        // Verificación con SERECI (TCP)
        try {
            String mensajeSereci = verificarSereci(nombres, primerApellido, segundoApellido, fechaNacimiento);
            if (mensajeSereci.startsWith("no")) {
                errores.append(mensajeSereci).append("\n");
            }
        } catch (Exception e) {
            errores.append("Error al verificar con SERECI.\n");
        }

        if (errores.length() > 0) {
            return new Diploma(null, null, null, errores.toString());
        }

        String nombreCompleto = nombres + " " + primerApellido + " " + segundoApellido;
        return new Diploma(nombreCompleto, carrera, "Emitido el 2024-09-19", "");
    }

    // Método para generar RUDE
    private String generarRUDE(String nombres, String primerApellido, String segundoApellido, String fechaNacimiento) {
        return nombres.substring(0, 2) + primerApellido.substring(0, 2) + segundoApellido.substring(0, 2) + fechaNacimiento.replace("-", "");
    }

    // Método para verificar con SEDUCA (UDP)
    private String verificarSeduca(String rude) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        byte[] buffer = ("verificar-rude:" + rude).getBytes();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 9876);
        socket.send(packet);

        buffer = new byte[1024];
        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        socket.close();
        return new String(packet.getData(), 0, packet.getLength());
    }

    // Método para verificar con SERECI (TCP)
    private String verificarSereci(String nombres, String primerApellido, String segundoApellido, String fechaNacimiento) throws IOException {
        Socket socket = new Socket("localhost", 6789);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Enviar el mensaje al servidor SERECI
        String mensaje = "Ver-fecha:" + nombres.trim() + "," + primerApellido.trim() + " " + segundoApellido.trim() + "," + fechaNacimiento.trim();
        out.println(mensaje);

        String respuesta = in.readLine();
        socket.close();
        return respuesta;
    }


    // Método main para iniciar el servidor Universidad (RMI)
    public static void main(String[] args) {
        try {
            // Crear una instancia del objeto remoto Universidad
            ServidorUniversidadRMI universidad = new ServidorUniversidadRMI();

            // Crear el registro RMI en el puerto 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // Registrar el objeto remoto en el registro RMI con el nombre "Universidad"
            registry.rebind("Universidad", universidad);

            System.out.println("Servidor Universidad RMI está listo en el puerto 1099.");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}