package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gustavo
 */
public class Client {

    Socket socket;
    Boolean ligado;

    public Client() {
        ligado = true;
    }

    public Socket criarConexao(String host, int porta) throws IOException {
        System.out.println("Esperando/criando conexão...");
        socket = new Socket(host, porta);

        return socket;
    }
 
    public void trataConexao(Socket socket) throws IOException {
        System.out.println("Criando streams...");
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());

        System.out.println("Tratando conexão...");

        String msg = "";

        ThreadReceiver run = new ThreadReceiver(input, ligado, this);

        new Thread(run).start();

        while (ligado) {
            System.out.print("$");
            Scanner scanner = new Scanner(System.in);
            msg = scanner.nextLine();
            msg = msg.toUpperCase();
            output.writeUTF(msg);
        }
    }

    public void recebeuMensagem(String msg) {
        System.out.print("Recebido: " + msg + "\n$ ");
        if (msg.equals("EXITREPLY")) {
            ligado = false;
            System.exit(0);
        }
    }

    public void fechaSocket() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

        Client c;
        c = new Client();

        try {
            //1
            Socket socket = c.criarConexao("localhost", 5555);
            //2
            c.trataConexao(socket);

        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            c.fechaSocket();
        }

    }

}
