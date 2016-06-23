package game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import server.Server;

/**
 *
 * @author gustavo
 */
public class Session {

    private Socket socket;
    private Server server;
    private Status status;
    private States state;
    private String nome;
    private Integer id;

    private ArrayList<Card> deck;

    public Session(Socket socket, Server servidor) {
        this.socket = socket;
        this.server = servidor;
        status = Status.NONEXISTENT;
        state = States.PLAYING;
        id = server.gerarId();
    }

    public Card buscaPorId(Integer id) {
        Card c = new Card();

        for (int i = 0; i < deck.size(); i++) {
            if (deck.get(i).getId() == id) {
                c = deck.get(i);
            }
        }

        return c;
    }

    public void connection() throws IOException, Exception {
        try {

            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            String clientMsg = "";
            boolean on = true;

            while (on) {
                clientMsg = input.readUTF();
                String[] protocol = clientMsg.split("#");
                String answer = "";

                switch (status) {
                    case NONEXISTENT:
                        switch (protocol[0]) {
                            case "CREATE":
                                nome = protocol[1];
                                /*
                                *Gerar o deck de 5 cartas
                                 */
                                status = Status.CREATED;
                                answer = "CREATEREPLY#OK";
                                break;
                            default:
                                answer = "ERROR#OPÇÃO_INVÁLIDA";
                                break;
                        }
                        break;

                    case CREATED:
                        switch (server.getCurrentAttribute()) {
                            case ATTACK:
                                switch (protocol[0]) {
                                    case "THROW":
                                        Card c = buscaPorId(Integer.parseInt(protocol[1]));
                                        server.addCards(c);
                                        state = States.WAITING;
                                        answer = "THROWREPLY#" + server.winner();
                                        break;
                                }
                                break;
                            case DEFENSE:
                                switch (protocol[0]) {
                                    case "THROW":
                                        Card c = buscaPorId(Integer.parseInt(protocol[1]));
                                        server.addCards(c);
                                        answer = "THROWREPLY#OK";
                                        break;
                                }
                                break;
                            case ABILITY:
                                switch (protocol[0]) {
                                    case "THROW":
                                        Card c = buscaPorId(Integer.parseInt(protocol[1]));
                                        server.addCards(c);
                                        answer = "THROWREPLY#OK";
                                        break;
                                }
                                break;
                        }
                        break;
                }

            }

        } catch (Exception e) {
            throw e;
        } finally {
            socket.close();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

}
