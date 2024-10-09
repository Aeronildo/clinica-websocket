import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClinicaServer extends WebSocketServer {

    private final CopyOnWriteArrayList<String> listaPacientes;
    private final Set<WebSocket> connections;

    public ClinicaServer(InetSocketAddress address) {
        super(address);
        listaPacientes = new CopyOnWriteArrayList<>();
        connections = Collections.synchronizedSet(new HashSet<>());
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections.add(conn);
        conn.send("Conectado ao servidor da clínica");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (message.startsWith("ADD ")) {
            String paciente = message.substring(4);
            listaPacientes.add(paciente);
            broadcastListaPacientes();
        } else if (message.equals("GET_LIST")) {
            conn.send("Lista de Pacientes: " + listaPacientes.toString());
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Servidor WebSocket iniciado!");
    }

    private void broadcastListaPacientes() {
        String listaAtual = "Lista de Pacientes: " + listaPacientes.toString();
        for (WebSocket conn : connections) {
            conn.send(listaAtual);
        }
    }

    public static void main(String[] args) {
        ClinicaServer server = new ClinicaServer(new InetSocketAddress(8080));
        server.start();
        System.out.println("Servidor da clínica rodando na porta 8080");
    }
}
