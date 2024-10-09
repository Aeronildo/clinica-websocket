import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MedicoClient extends WebSocketClient {

    public MedicoClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Conectado ao servidor da clínica");
        send("GET_LIST");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Servidor: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Desconectado do servidor: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public static void main(String[] args) {
        try {
            MedicoClient client = new MedicoClient(new URI("ws://localhost:8080"));
            client.connectBlocking();
            // Médico apenas acessa a lista
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
