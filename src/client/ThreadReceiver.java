package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ThreadReceiver implements Runnable {

    private DataInputStream input;
    private Boolean ligado;
    private Client c;

    public ThreadReceiver(DataInputStream input, Boolean ligado, Client c) {
        this.input = input;
        this.ligado = ligado;
        this.c = c;
    }

    @Override
    public void run() {

        while (ligado) {
            try {
                while (ligado) {
                    c.recebeuMensagem(input.readUTF());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
