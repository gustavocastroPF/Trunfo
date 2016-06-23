package server;

import game.Attributes;
import game.Card;
import game.Session;
import game.States;
import java.util.ArrayList;

/**
 *
 * @author gustavo
 */
public class Server {

    private ArrayList<Session> players;
    private ArrayList<Card> board;
    private Attributes currentAttribute;
    private Integer id;

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

        /*
        verificar se é o ultimo
                se n for wait() --> quando acordarem, na saida do método, notifyall
        se for, computaVencedor, 
        gera string de resposta
        notifyAll();
         */
        String winner = "";

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

    public void computeCards() {
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

        String answer = "WINNER#" + winner.getNome();
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
}
