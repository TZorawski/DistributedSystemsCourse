/**
 * Descrição: Cliente utilizando TCP que envia mensagens a um servidor do tipo
 * "CONNECT user,password", "PWD", "CHDIR path", "GETFILES", "GETDIRS" e "EXIT".
 * Autores: Guilherme Vasco da Silva, Thais Zorawski
 * Data da criação: 15/10/2021
 * Data de atualização: 18/10/2021
 */

package Questao01;

import java.net.*;
import java.io.*;
import java.util.Scanner; //scanner para ler o digitado
import java.math.BigInteger; //biblioteca Big Integer para uso no SHA-512
import java.security.MessageDigest; //bibilioteca Message Digest para uso do SHA-512

public class TCPClient { //classe principal do cliente

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
    } //SHA-512

	public static void main (String args[]) { //main
	    Socket clientSocket = null; // socket do cliente
            Scanner reader = new Scanner(System.in); // ler mensagens via teclado
            
            try{
                /* Endereço e porta do servidor */
                int serverPort = 6666;   
                InetAddress serverAddr = InetAddress.getByName("127.0.0.1");
                
                /* conecta com o servidor */  
                clientSocket = new Socket(serverAddr, serverPort);  
                
                /* cria objetos de leitura e escrita */
                DataInputStream in = new DataInputStream( clientSocket.getInputStream());
                DataOutputStream out =new DataOutputStream( clientSocket.getOutputStream());
            
                /* protocolo de comunicação */
                String buffer = "";
                int flag = 0;
                while (true) {
                    while (flag == 0){ //CONNECT
                        System.out.print("Mensagem: "); //imprime antes da mensagem
                        buffer = reader.nextLine(); // lê mensagem via teclado

                        if (buffer.contains("CONNECT ")){ //comando CONNECT
                            /* remove comando e deixa somente parâmetros do buffer */
                            String cut = buffer.substring(8, (buffer.length()));
                            String[] parts = cut.split(","); //split em user e password
                            String sha512 = getSHA512(parts[1]); //conversão em SHA-512 da senha
                            buffer = parts[0] + ',' + sha512; //buffer com user, password em SHA-512
                        }

                        out.writeUTF(buffer); // envia a mensagem para o servidor

                        buffer = in.readUTF(); // aguarda resposta do servidor
                        if (buffer.equals("SUCCESS")){ //usuário e senha corretos
                            flag = 1;
                        }
                        System.out.println("Server disse: " + buffer);
                    } //CONNECT

                    //Troca de mensagens
                    System.out.print("Mensagem: "); //imprime antes da mensagem
                    buffer = reader.nextLine(); //leitura do buffer
                
                    out.writeUTF(buffer); //envio do buffer ao servidor
		
                    if (buffer.equals("PARAR")) break; //comando EXIT

                    buffer = in.readUTF(); //leitura do buffer
                    System.out.println("Server disse: " + buffer); //imprime buffer enviado do servidor
                } 
	    } catch (UnknownHostException ue){ //catch
		    System.out.println("Socket:" + ue.getMessage());
        } catch (EOFException eofe){
		    System.out.println("EOF:" + eofe.getMessage());
        } catch (IOException ioe){
		    System.out.println("IO:" + ioe.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ioe) {
                System.out.println("IO: " + ioe);;
            }
        } //END OF FILE
     } //main
} //class
