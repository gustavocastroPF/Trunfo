package server;

import game.Attributes;
import game.Card;
import game.Session;
import game.States;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gustavo
 */
public class Server {

    private ArrayList<Session> players;
    private ArrayList<Card> board;
    private Attributes currentAttribute;
    private Integer id;
    ServerSocket serverSocket;

    public Server() {
        players = new ArrayList<>();
        board = new ArrayList<>();
        defineAttribute();
    }

    public void addPlayer(Session s) {
        players.add(s);
    }

    public void createServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public Socket waitConnection() throws IOException {
        Socket socket = serverSocket.accept();
        return socket;
    }

    public void closeConnection() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer gerarId() {
        this.id = +1;
        return this.id;
    }

    public void addCards(Card c) {
        board.add(c);
    }

    public synchronized String winner() throws InterruptedException {

        while (!isLast()) {
            wait();
        }

        String winner = computeCards();
        board.clear();
        notifyAll();
        return winner;
    }

    public boolean isLast() {
        boolean isLast = true;

        for (Session s : players) {
            if (s.getState() == States.PLAYING) {
                isLast = false;
            }
        }

        return isLast;
    }

    public String computeCards() {
        Integer max = 0;
        Card winner = new Card();

        if (currentAttribute == Attributes.ATTACK) {
            for (Card c : board) {
                if (c.getAttack() > max) {
                    winner = c;
                }
            }
        } else if (currentAttribute == Attributes.DEFENSE) {

            for (Card c : board) {
                if (c.getDefense() > max) {
                    winner = c;
                }
            }

        } else if (currentAttribute == Attributes.ABILITY) {
            for (Card c : board) {
                if (c.getAbility() > max) {
                    winner = c;
                }
            }
        }

        winner.getPlayer().setWins(winner.getPlayer().getWins() + 1);
        return "Vencedor: " + winner.getPlayer().getNome() + "Usando a carta: " + winner.getName();
    }

    public void defineAttribute() {
        if (Math.round(Math.random() * 2) == 0) {
            currentAttribute = Attributes.ATTACK;
        } else if (Math.round(Math.random() * 2) == 1) {
            currentAttribute = Attributes.DEFENSE;
        } else {
            currentAttribute = Attributes.ABILITY;
        }
    }

    public ArrayList<Session> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Session> players) {
        this.players = players;
    }

    public ArrayList<Card> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<Card> board) {
        this.board = board;
    }

    public Attributes getCurrentAttribute() {
        return currentAttribute;
    }

    public void setCurrentAttribute(Attributes currentAttribute) {
        this.currentAttribute = currentAttribute;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static void main(String[] args) {

        Server servidor = new Server();

        System.out.println("Criando Servidor...");

        try {
            servidor.createServer(5555);
        } catch (IOException ex) {
            System.out.println("Erro:" + ex.getMessage());
        }

        try {
            while (true) {

                System.out.println("Esperando conexão...");

                Socket socket = servidor.waitConnection();

                System.out.println("Conexão aceita.");

                Session session = new Session(socket, servidor);
                Thread t = new Thread(session);
                t.start();
                servidor.addPlayer(session);

                //fim
            }
        } catch (IOException ex) {
            System.out.println("Erro: " + ex.getMessage());

        } finally {
            servidor.closeConnection();
        }

    }

    public String rank() {
        String rank="\n";
        for(Session s : players){
            rank+="Jogador: "+s.getNome()+" Número de vitórias: "+s.getWins()+" \n";
        }
        return rank;
    }

}
