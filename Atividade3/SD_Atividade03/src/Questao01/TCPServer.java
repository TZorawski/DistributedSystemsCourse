/**
 * Descrição: Servidor utilizando TCP que suporta mensagens de múltiplos clientes
 * e processa mensagens do tipo "CONNECT user,password", "PWD", "CHDIR path",
 * "GETFILES", "GETDIRS" e "EXIT".
 * Autores: Guilherme Vasco da Silva, Thais Zorawski
 * Data da criação: 15/10/2021
 * Data de atualização: 18/10/2021
 */

package Questao01;

import java.net.*;
import java.io.*;
import java.math.BigInteger; //biblioteca Big Integer para uso no SHA-512
import java.security.MessageDigest; //bibilioteca Message Digest para uso do SHA-512

public class TCPServer { //classe principal do servidor
    public static void main(String args[]) { //main
        try {
            int serverPort = 6666; // porta do servidor

            /* cria um socket e mapeia a porta para aguardar conexao */
            ServerSocket listenSocket = new ServerSocket(serverPort);

            while (true) { //loop
                System.out.println("Servidor aguardando conexão ...");

                /* aguarda conexoes */
                Socket clientSocket = listenSocket.accept();

                System.out.println("Cliente conectado ... Criando thread ...");

                /* cria um thread para atender a conexao */
                ClientThread c = new ClientThread(clientSocket);

                /* inicializa a thread */
                c.start();
            } //fim do loop

        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        } //catch
    } //fim da main
} //fim da classe TCPServer

/**
 * Classe ClientThread: Thread responsavel pela comunicacao
 * Descricao: Rebebe um socket, cria os objetos de leitura e escrita,
 * aguarda mensagens dos Cliente e responde de acordo com o comando.
 * Se o comando não existir ele retorna o que foi digitado
 */
class ClientThread extends Thread {

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public ClientThread(Socket clientSocket) { //construtor
        try {
            this.clientSocket = clientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("Connection:" + ioe.getMessage());
        } //catch
    } //construtor

    /* Função para o comando PWD, utiliza a biblioteca File para retornar diretório atual */
    public static String pwd(){
        String pwd_path = "";
        try{
            /* getCanonicalPath() obtém o diretório atual */
            pwd_path = new java.io.File(".").getCanonicalPath();
        } catch(Exception e){
            pwd_path = "/home";
        } //catch
        return pwd_path;
     } //fim do PWD

     /* Função para a criptografia SHA-512 */
     public static String getSHA512(String input){ //SHA-512

        String toReturn = null;
        try {
            /* Message Digest utilizado para criptografar em SHA-512 */
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset(); //reset
            digest.update(input.getBytes("utf8")); //update recebendo em UTF8
            /* Utilização do Big Integer para poder retornar valor já em SHA-512 */
            toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        } //catch
        
        return toReturn;
    } //fim do SHA-512

    /* metodo executado ao iniciar a thread - start() */
    @Override
    public void run() {
        String path = pwd(); //obtém diretório atual pela função pwd
        File f = new File(path);

        String[] users = {"user", "root", "admin"}; //usuários
        String[] pass = {"user123", "root", "admin@"}; //senhas, respectivamente
        int pos;
        int flag = 0;
        int cont = 0;

        try {
            String buffer = "";
            outer: //utilizado para sair de dois ciclos while
            while (true) { //loop
                while (flag == 0){ //Login e senha
                    buffer = in.readUTF();   /* aguarda o envio de dados */
                    if (buffer.equals("EXIT")) break outer; //saída dos dois loops
                    String[] parts = buffer.split(","); //split do buffer em user e password
                    if (parts.length > 1){ //leitura correta de user e password
                        String client_user = parts[0];
                        System.out.println("Cliente user: " + client_user); //user enviado

                        String client_pass = parts[1];
                        System.out.println("Cliente password: " + client_pass); //password enviado
                        
                        for (String user : users){ //verifica usuários salvos no servidor
                            if (client_user.equals(user)){ //compara usuário
                                pos = cont;
                                String sha512 = getSHA512(pass[pos]); //converte senhas no servidor para SHA-512
                                if (client_pass.equals(sha512)){ //compara senha
                                    buffer = "SUCCESS";
                                    flag = 1; //flag de usuário e senha corretos
                                    break;
                                }
                            }
                            buffer = "ERROR"; //usuário e senha incorretos
                            cont++;
                        }
                    }
                    cont = 0;
                    pos = 0;

                    System.out.println(buffer); //imprime conteúdo do buffer no servidor
                    
                    out.writeUTF(buffer); //envia resposta para cliente
                } //CONNECT

                //Troca de mensagens
                buffer = in.readUTF(); /* aguarda o envio de dados */
                System.out.println("Cliente disse: " + buffer); //imprime mensagem do cliente no servidor

                //comando EXIT
                if (buffer.equals("EXIT")) break;

                //comando PWD
                if (buffer.equals("PWD")){
                    buffer = path;
                } //fim do PWD

                //comando CHDIR
                if (buffer.contains("CHDIR ")){
                    /* pega do buffer somente a parte do diretório */
                    String path_brute = buffer.substring(6, (buffer.length()));

                    //=* cria um File a partir do diretório enviado */
                    f = new File(path_brute);

                    if (f.isDirectory()){ //confere se File é um diretório válido
                        path = path_brute;
                        buffer = "SUCCESS";
                    } else{
                        buffer = "ERROR"; //diretório inválido
                    }
                } //fim do CHDIR

                //comando GETFILES
                if (buffer.equals("GETFILES")){
                    String[] pathnames = f.list(); //lista de arquivos e diretórios no atual
                    int qnt = 0;
                    buffer = "\n"; //organização do buffer
                    for (String pathname : pathnames) { //roda pela lista
                        if (pathname.contains(".")){ //se contém "." é um arquivo
                            qnt++; //adicona à quantidade de arquivos
                            buffer += pathname + "\n"; //nome do arquivo encontrado
                        }
                    }
                    buffer += "\nTotal files: " + qnt; //total de arquivos
                } //fim do GEFILES

                //comando GETDIRS
                if (buffer.equals("GETDIRS")){
                    String[] pathnames = f.list(); //lista de arquivos e diretórios no atual
                    int qnt = 0;
                    buffer = "\n"; //organização do buffer
                    for (String pathname : pathnames) { //roda pela lista
                        if (!(pathname.contains("."))){ //se não contém "." é um diretório
                            qnt++; //adicona à quantidade de diretórios
                            buffer += pathname + "\n"; //nome do diretório encontrado
                        }
                    }
                    buffer += "\nTotal directories: " + qnt; //total de diretórios
                } //fim do GETDIRS

                out.writeUTF(buffer); //envia resposta para cliente
            }
        } catch (EOFException eofe) { //catch
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
        }//END OF FILE
        System.out.println("Thread comunicação cliente finalizada."); //conexão finalizada
    } //run
} //class
