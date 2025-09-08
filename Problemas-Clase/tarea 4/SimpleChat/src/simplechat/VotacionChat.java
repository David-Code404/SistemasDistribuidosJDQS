/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simplechat;

/**
 *
 * @author Gerson
 */
import org.jgroups.*;
import org.jgroups.util.Util;
import java.io.*;
import java.util.*;

public class VotacionChat extends ReceiverAdapter {

    private JChannel channel;
    private String user_name;
    private final List<String> state = new LinkedList<>();

    // Acumulador de votos global
    private final Map<String, Integer> votos = new HashMap<>();

    public VotacionChat(String user_name) {
        this.user_name = user_name;
        votos.put("tuto", 0);
        votos.put("rodrigo", 0);
    }

    @Override
    public void viewAccepted(View new_view) {
        System.out.println("Vista del grupo actualizada: " + new_view);
    }

    @Override
    public void receive(Message msg) {
        String line = (String) msg.getObject();
        System.out.println(msg.getSrc() + ": " + line);

        synchronized (state) {
            state.add(line);
        }

        // Procesar votos si el mensaje contiene resultados
        procesarMensaje(line);

        // Mostrar resultados actuales
        mostrarResultados();
    }

    private void procesarMensaje(String line) {
        try {
            // Formato esperado: mesa X, tuto Y, rodrigo Z
            line = line.toLowerCase();
            String[] partes = line.split(",");

            for (String parte : partes) {
                if (parte.contains("tuto")) {
                    int votosTuto = Integer.parseInt(parte.replaceAll("\\D+", ""));
                    votos.put("tuto", votos.get("tuto") + votosTuto);
                }
                if (parte.contains("rodrigo")) {
                    int votosRodrigo = Integer.parseInt(parte.replaceAll("\\D+", ""));
                    votos.put("rodrigo", votos.get("rodrigo") + votosRodrigo);
                }
            }
        } catch (Exception e) {
            System.out.println("⚠ Error al procesar el mensaje: " + line);
        }
    }

    private void mostrarResultados() {
        System.out.println("=== RESULTADOS ACUMULADOS ===");
        System.out.println("Tuto: " + votos.get("tuto"));
        System.out.println("Rodrigo: " + votos.get("rodrigo"));
        System.out.println("=============================");
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        synchronized (state) {
            Util.objectToStream(state, new DataOutputStream(output));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setState(InputStream input) throws Exception {
        List<String> list = (List<String>) Util.objectFromStream(new DataInputStream(input));
        synchronized (state) {
            state.clear();
            state.addAll(list);
        }
        System.out.println("Estado recibido (" + list.size() + " mensajes históricos ):");
        for (String str : list) {
            System.out.println(str);
            procesarMensaje(str);
        }
        mostrarResultados();
    }

    public void start() throws Exception {
        channel = new JChannel();
        channel.setReceiver(this);
        channel.connect("ClusterVotacion");
        channel.getState(null, 10000);
        eventLoop();
        channel.close();
    }

    private void eventLoop() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.print("> ");
                System.out.flush();
                String line = in.readLine();
                if (line == null || line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                    break;
                }

                line = "[" + user_name + "] " + line;
                Message msg = new Message(null, line);
                channel.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

