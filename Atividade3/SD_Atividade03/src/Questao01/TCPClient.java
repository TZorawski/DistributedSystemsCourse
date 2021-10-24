package Questao01;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.math.BigInteger;
import java.security.MessageDigest;

public class TCPClient {

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

	public static void main (String args[]) {
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
                        System.out.print("Mensagem: ");
                        buffer = reader.nextLine(); // lê mensagem via teclado

                        if (buffer.contains("CONNECT ")){
                            String cut = buffer.substring(8, (buffer.length()));
                            String[] parts = cut.split(",");
                            String sha512 = getSHA512(parts[1]);
                            buffer = parts[0] + ',' + sha512;
                        }

                        out.writeUTF(buffer); // envia a mensagem para o servidor

                        buffer = in.readUTF(); // aguarda resposta do servidor
                        if (buffer.equals("SUCCESS")){
                            flag = 1;
                        }
                        System.out.println("Server disse: " + buffer);
                    } //CONNECT

                    //Troca de mensagens
                    System.out.print("Mensagem: ");
                    buffer = reader.nextLine();
                
                    out.writeUTF(buffer);
		
                    if (buffer.equals("PARAR")) break;

                    buffer = in.readUTF();
                    System.out.println("Server disse: " + buffer);
                } 
	    } catch (UnknownHostException ue){
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
        }
     } //main
} //class
