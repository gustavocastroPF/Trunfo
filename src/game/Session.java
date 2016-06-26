package game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Server;

/**
 *
 * @author gustavo
 */
public class Session implements Runnable {

    private Socket socket;
    private Server server;
    private Status status;
    private States state;
    private String nome;
    private Integer id;
    private Integer wins;
    private ArrayList<Card> deck;

    public Session(Socket socket, Server servidor) {
        this.socket = socket;
        this.server = servidor;
        deck = new ArrayList<>();
        status = Status.NONEXISTENT;
        state = States.PLAYING;
        id = server.gerarId();
        wins = 0;
    }

    public Card generateCard() {
        Card c = new Card();

        c.setPlayer(this);
        c.setName("asiduhasiudash");
        c.setId(deck.size() + 1);
        c.setAttack((int) Math.round(Math.random() * 30));
        c.setDefense((int) Math.round(Math.random() * 30));
        c.setAbility((int) Math.round(Math.random() * 30));

        System.out.println(c.getName() + "-" + c.getAbility());

        return c;
    }

    public Card buscaPorId(Integer id) {
        Card c = new Card();

        for (Card card : deck) {
            if (card.getId() == id) {
                c = card;
            }
        }

        return c;
    }

    @Override
    public void run() {
        try {
            connection();
        } catch (Exception ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connection() throws IOException, Exception {
        try {

            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            String clientMsg = "";
            boolean on = true;

            while (on) {
                clientMsg = input.readUTF();
                System.out.println("Mensagem recebida do cliente " + socket.getInetAddress() + ": " + clientMsg);

                String[] protocol = clientMsg.split("#");
                String answer = "";

                if (status == Status.CREATED && deck.size() == 0) {
                    for (int i = 0; i < 5; i++) {
                        deck.add(generateCard());
                    }
                }

                switch (status) {
                    case NONEXISTENT:
                        switch (protocol[0]) {
                            case "CREATE":
                                try {
                                    nome = protocol[1];
                                    for (int i = 0; i < 5; i++) {
                                        deck.add(generateCard());
                                    }
                                    status = Status.CREATED;
                                    answer = "CREATEREPLY#OK#" + server.getCurrentAttribute();
                                } catch (Exception e) {
                                    answer = "ERROR#PARAMETROS INVÁLIDOS: " + e.getMessage();
                                }
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
                                    case "LIST":
                                        String cards = "";
                                        for (Card c : deck) {
                                            cards += "\n" + c.getName() + " - Id: " + c.getId() + " - Ataque: " + c.getAttack() + " - Defesa: " + c.getDefense() + " - Habilidade: " + c.getAbility() + "\n";
                                        }
                                        answer = "LISTREPLAY#" + cards;
                                        break;
                                    case "THROW":
                                        Card c = buscaPorId(Integer.parseInt(protocol[1]));
                                        server.addCards(c);
                                        deck.remove(c);
                                        state = States.WAITING;
                                        answer = "THROWREPLY#" + server.winner();
                                        state = States.PLAYING;
                                        break;
                                    case "RANK":
                                        answer = "RANKREPLY#" + server.rank();
                                        break;
                                    case "EXIT":
                                        answer = "EXITREPLY";
                                        break;
                                    default:
                                        answer = "ERROR#OPÇÃO_INVÁLIDA";
                                        break;
                                }
                                break;
                            case DEFENSE:
                                switch (protocol[0]) {
                                    case "LIST":
                                        String cards = "";
                                        for (Card c : deck) {
                                            cards += "\n" + c.getName() + " - Id: " + c.getId() + " - Ataque: " + c.getAttack() + " - Defesa: " + c.getDefense() + " - Habilidade: " + c.getAbility() + "\n";
                                        }
                                        answer = "LISTREPLAY#" + cards;
                                        break;
                                    case "THROW":
                                        Card c = buscaPorId(Integer.parseInt(protocol[1]));
                                        server.addCards(c);
                                        deck.remove(c);
                                        state = States.WAITING;
                                        answer = "THROWREPLY#" + server.winner();
                                        state = States.PLAYING;
                                        break;
                                    case "RANK":
                                        answer = "RANKREPLY#" + server.rank();
                                        break;
                                    case "EXIT":
                                        answer = "EXITREPLY";
                                        break;
                                    default:
                                        answer = "ERROR#OPÇÃO_INVÁLIDA";
                                        break;
                                }
                                break;
                            case ABILITY:
                                switch (protocol[0]) {
                                    case "LIST":
                                        String cards = "";
                                        for (Card c : deck) {
                                            cards += "\n" + c.getName() + " - Id: " + c.getId() + " - Ataque: " + c.getAttack() + " - Defesa: " + c.getDefense() + " - Habilidade: " + c.getAbility() + "\n";
                                        }
                                        answer = "LISTREPLAY#" + cards;
                                        break;
                                    case "THROW":
                                        Card c = buscaPorId(Integer.parseInt(protocol[1]));
                                        server.addCards(c);
                                        deck.remove(c);
                                        state = States.WAITING;
                                        answer = "THROWREPLY#" + server.winner();
                                        state = States.PLAYING;
                                        break;
                                    case "RANK":
                                        answer = "RANKREPLY#" + server.rank();
                                        break;
                                    case "EXIT":
                                        answer = "EXITREPLY";
                                        break;
                                    default:
                                        answer = "ERROR#OPÇÃO_INVÁLIDA";
                                        break;
                                }
                                break;
                        }
                        break;
                }
                output.writeUTF(answer);
                System.out.println("Resposta: " + answer);
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

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

}
