/**
 * Descrição: sistema de upload de arquivos via UDP. O servidor UDP receberá as 
 * partes dos arquivos (1024 bytes), verificará ao final a integridade via um 
 * checksum (SHA-1) e armazenará o arquivo em uma pasta padrão.
 * Sugestões: o servidor pode receber o nome e tamanho do arquivo como o primeiro
 * pacote e o checksum como o último. Testar o servidor com arquivos textos e 
 * binários (ex: imagens, pdf) de tamanhos arbitrários (ex: 100 bytes, 4KiB,
 * 4MiB). O protocolo para a comunicação deve ser criado e especificado textualmente
 * ou graficamente.
 * 
 * Autores: Guilherme Vasco da Silva, Thais Zorawski
 * Data da criação: 31/10/2021
 * Data de atualização: 01/11/2021
 */
package questao02;

import java.net.*;
import java.io.*;
import java.util.Scanner; //scanner para ler o digitado
import java.lang.Integer; //Integer para a conversão em byte[]
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPServer { //sclasse principal do cliente

    static final Logger SERVER_LOGGER = Logger.getLogger(UDPServer.class.getName());
    public static String path = ".\\src\\questao02\\serverStorage\\";

    /* função para impressão do recebido */
    public static void printer(byte[] pack) { //printer
        /* obtenção do primeito byte (tipo da mensagem) */
        int type = Integer.parseInt(Byte.toString(pack[0]));
        /* obtenção do segundo byte (tamanho do nick) */
        int filename_size = Integer.parseInt(Byte.toString(pack[1]));
        String filename = ""; //nick vazio
        int pos = 0; //posição nos bytes, inicio em 0

        for (int i = 2; i <= filename_size + 1; i++) { //for nick
            byte[] tmp = {pack[i]}; //byte temporário
            try {
                filename += new String(tmp, "UTF-8"); //pega bytes em String e coloca no apelido
            } catch (IOException e) {
                filename += ""; //exceção
            }
            pos = i;
        } //fim do for nick

        pos += 1; //incremento de posição

        System.out.println("Nome arquivo: " + filename);
    } //fim de printer
    
    /* retorna apenas o dado principal do pacote recebido */
    public static String getData(byte[] pack) { //printer
        /* obtenção do segundo byte (tamanho do dado) */
        int data_size = Integer.parseInt(Byte.toString(pack[1]));
        String data = ""; //dado vazio
        int pos = 0; //posição nos bytes, inicio em 0

        for (int i = 2; i <= data_size + 1; i++) { //for dado
            byte[] tmp = {pack[i]}; //byte temporário
            try {
                data += new String(tmp, "UTF-8"); //pega bytes em String e coloca no apelido
            } catch (IOException e) {
                data += ""; //exceção
            }
            pos = i;
        } //fim do for

        pos += 1; //incremento de posição

        return data;
    } //fim de printer
    
    /* retorna apenas o tipo do pacote recebido */
    public static int getType(byte[] pack) { //printer
        /* obtenção do primeito byte (tipo da mensagem) */
        int type = Integer.parseInt(Byte.toString(pack[0]));

        return type;
    } //fim de printer

    public static void main(String args[]) { //função principal
        DatagramSocket dgramSocket = null; //Datagrama vazio

        try {
            InetAddress serverAddr = InetAddress.getByName("127.0.0.1");
            int serverPort = 6666; // Server port
            dgramSocket = new DatagramSocket(6666); //cria datagram socket
            /* cria um buffer de tamanho 325 para receber datagramas */
            byte[] buffer = new byte[1024]; //tamanho máximo da mensagem

            int action = 1;
            String filename;

            while (action != 0) {
                    SERVER_LOGGER.info("Server waiting for some client request...");

                    /* Waiting for connections */
                    DatagramPacket dgramPacket = new DatagramPacket(buffer, buffer.length);

                    /* atualização funciona de forma manual por conta desse receive */
                    dgramSocket.receive(dgramPacket);  // aguarda a chegada de datagramas
                    System.out.println("chegou dgramPacket");

                    SERVER_LOGGER.log(Level.INFO, "Client request received. Waiting for file from {0}:{1}...", new Object[]{dgramPacket.getAddress(), dgramPacket.getPort()});

                    /* chamada da função printer para imprimir mensagem recebida */
                    printer(dgramPacket.getData());
                    filename = getData(dgramPacket.getData());


                /* Recebe os pacotes com o conteúdo */
                DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(path + filename));
                do {
                    //
                    byte[] contentBuffer = new byte[1024]; //tamanho máximo da mensagem
                    DatagramPacket contentDgramPacket = new DatagramPacket(contentBuffer, contentBuffer.length);

                    dgramSocket.receive(contentDgramPacket);  // aguarda a chegada de datagramas

                    String contentData = getData(contentDgramPacket.getData());
                    dataOutputStream.writeChars(contentData);
                    
                    if (contentDgramPacket.getLength() < 1024) {
                        action = 3;
                    }
                    
                } while (action == 2);
                
                dataOutputStream.close();
                SERVER_LOGGER.log(Level.INFO, "File received successfully");

                
                byte[] checksumBuffer = new byte[1024]; //tamanho máximo da mensagem
                DatagramPacket checksumDgramPacket = new DatagramPacket(checksumBuffer, checksumBuffer.length);
                dgramSocket.receive(checksumDgramPacket);  // aguarda a chegada de datagramas
                action = 0;
            }

            /* libera o socket */
            dgramSocket.close();

        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        } //catch

    } //main		      	
} //class

