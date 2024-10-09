import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Scanner;

public class FuncionarioClient extends WebSocketClient {

    public FuncionarioClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Conectado ao servidor da clínica");
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
            FuncionarioClient client = new FuncionarioClient(new URI("ws://localhost:8080"));
            client.connectBlocking(); // Aguarda a conexão ser estabelecida

            Scanner scanner = new Scanner(System.in);
            String input;

            // Loop para permitir entradas contínuas até o usuário digitar "sair"
            do {
                System.out.println("Digite o nome do paciente (ou 'sair' para finalizar):");
                input = scanner.nextLine(); // Lê a entrada do usuário

                if (!input.equalsIgnoreCase("sair")) {
                    client.send("ADD " + input); // Envia o nome do paciente ao servidor
                }

            } while (!input.equalsIgnoreCase("sair"));

            // Fechar a conexão após o término
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
