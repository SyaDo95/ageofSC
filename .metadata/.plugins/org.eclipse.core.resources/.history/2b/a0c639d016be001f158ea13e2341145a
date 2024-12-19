package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {
    private static final int PORT = 5000;
    private List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        new GameServer().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Game Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected!");

                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcast(GameAction action) {
        for (ClientHandler client : clients) {
            client.sendAction(action);
        }
    }

    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}

class ClientHandler extends Thread {
    private Socket socket;
    private GameServer server;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientHandler(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            Object receivedObject;
            while ((receivedObject = in.readObject()) != null) {
                if (receivedObject instanceof GameAction) {
                    GameAction action = (GameAction) receivedObject;
                    System.out.println("Received action: " + action.getActionType());

                    // 브로드캐스트
                    server.broadcast(action);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.removeClient(this);
        }
    }

    public void sendAction(GameAction action) {
        try {
            out.writeObject(action);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
