package Questao01;

import java.net.*;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;

public class TCPServer {
    public static void main(String args[]) {
        try {
            int serverPort = 6666; // porta do servidor

            /* cria um socket e mapeia a porta para aguardar conexao */
            ServerSocket listenSocket = new ServerSocket(serverPort);

            while (true) {
                System.out.println("Servidor aguardando conexão ...");

                /* aguarda conexoes */
                Socket clientSocket = listenSocket.accept();

                System.out.println("Cliente conectado ... Criando thread ...");

                /* cria um thread para atender a conexao */
                ClientThread c = new ClientThread(clientSocket);

                /* inicializa a thread */
                c.start();
            } //while

        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        } //catch
    } //main
} //class

/**
 * Classe ClientThread: Thread responsavel pela comunicacao
 * Descricao: Rebebe um socket, cria os objetos de leitura e escrita,
 * aguarda msgs clientes e responde com a msg + :OK
 */
class ClientThread extends Thread {

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public ClientThread(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("Connection:" + ioe.getMessage());
        } //catch
    } //construtor

    public static String pwd(){
        String pwd_path = "";
        try{
            pwd_path = new java.io.File(".").getCanonicalPath();
        } catch(Exception e){
            pwd_path = "/home";
        }
        return pwd_path;
     } //PWD

     public static String getSHA512(String input){ //SHA-512

        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return toReturn;
    } //SHA-512

    /* metodo executado ao iniciar a thread - start() */
    @Override
    public void run() {
        String path = pwd();
        File f = new File(path);

        String[] users = {"user", "root", "admin"};
        String[] pass = {"user123", "root", "admin@"};
        int pos;
        int flag = 0;
        int cont = 0;

        try {
            String buffer = "";
            outer:
            while (true) {
                while (flag == 0){ //Login e senha
                    buffer = in.readUTF();   /* aguarda o envio de dados */
                    if (buffer.equals("EXIT")) break outer;
                    String[] parts = buffer.split(",");
                    if (parts.length > 1){
                        String client_user = parts[0];
                        System.out.println("Cliente user: " + client_user);

                        String client_pass = parts[1];
                        System.out.println("Cliente password: " + client_pass);
                        
                        for (String user : users){
                            if (client_user.equals(user)){
                                pos = cont;
                                String sha512 = getSHA512(pass[pos]);
                                if (client_pass.equals(sha512)){
                                    buffer = "SUCCESS";
                                    flag = 1;
                                    break;
                                }
                            }
                            buffer = "ERROR";
                            cont++;
                        }
                    }
                    cont = 0;
                    pos = 0;

                    System.out.println(buffer);
                    
                    out.writeUTF(buffer);
                } //CONNECT

                //Troca de mensagens
                buffer = in.readUTF();
                System.out.println("Cliente disse: " + buffer);

                //EXIT
                if (buffer.equals("EXIT")) break;

                //PWD
                if (buffer.equals("PWD")){
                    buffer = path;
                }

                //CHDIR
                if (buffer.contains("CHDIR ")){
                    String path_brute = buffer.substring(6, (buffer.length()));

                    f = new File(path_brute);
                    if (f.isDirectory()){
                        path = path_brute;
                        buffer = "SUCCESS";
                    } else{
                        buffer = "ERROR";
                    }
                }

                //GETFILES
                if (buffer.equals("GETFILES")){
                    String[] pathnames = f.list();
                    int qnt = 0;
                    buffer = "\n";
                    for (String pathname : pathnames) {
                        if (pathname.contains(".")){
                            qnt++;
                            buffer += pathname + "\n";
                        }
                    }
                    buffer += "\nTotal files: " + qnt;
                }

                //GETDIRS
                if (buffer.equals("GETDIRS")){
                    String[] pathnames = f.list();
                    int qnt = 0;
                    buffer = "\n";
                    for (String pathname : pathnames) {
                        if (!(pathname.contains("."))){
                            qnt++;
                            buffer += pathname + "\n";
                        }
                    }
                    buffer += "\nTotal directories: " + qnt;
                }

                out.writeUTF(buffer);
            }
        } catch (EOFException eofe) {
            System.out.println("EOF: " + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IOE: " + ioe.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException ioe) {
                System.err.println("IOE: " + ioe);
            }
        }
        System.out.println("Thread comunicação cliente finalizada.");
    } //run
} //class
