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

public class UDPClient { //classe principal do cliente

    static final Logger CLIENT_LOGGER = Logger.getLogger(UDPClient.class.getName());

    /* função para impressão do recebido */
    public static void printer(byte[] pack) { //printer
        /* obtenção do primeito byte (tipo da mensagem) */
        int type = Integer.parseInt(Byte.toString(pack[0]));
        /* obtenção do segundo byte (tamanho do nome) */
        int filename_size = Integer.parseInt(Byte.toString(pack[1]));
        String filename = ""; //nome vazio
        int pos = 0; //posição nos bytes, inicio em 0

        for (int i = 2; i <= filename_size + 1; i++) { //for nick
            byte[] tmp = {pack[i]}; //byte temporário
            try {
                filename += new String(tmp, "UTF-8"); //pega bytes em String e coloca no nome do arquivo
            } catch (IOException e) {
                filename += ""; //exceção
            }
            pos = i;
        } //fim do for nick

        pos += 1; //incremento de posição

        System.out.println("Nome arquivo: " + filename);
    } //fim de printer

    public static void main(String args[]) { //função principal
        DatagramSocket dgramSocket = null; //Datagrama vazio
        String inputBuffer = "";
        Scanner reader = new Scanner(System.in); //scanner

        try {
            InetAddress serverAddr = InetAddress.getByName("127.0.0.1");
            int serverPort = 6666; // Server port
            dgramSocket = new DatagramSocket(); //cria datagram socket

            while (true) {
                System.out.print(">>> ");
                inputBuffer = reader.nextLine();
                if (inputBuffer.equals("Exit")) {
                    break;
                }

                int msg_type; // Controla o fluxo da mensagem

                File file = new File(inputBuffer);

                if (file.isFile() && file.canRead()) {
                    /* ========== Sinaliza que enviará um arquivo ========== */
                    msg_type = 0; // Request type: Open transmission
                    byte[] msg_type_b = {(new Integer(msg_type)).byteValue()};
                    byte[] filename_size = {(new Integer(inputBuffer.length())).byteValue()};
                    byte[] filename = inputBuffer.getBytes();
                    /* organização da mensagem a ser enviada em bytes no formato adequado */
                    byte[] m = new byte[msg_type_b.length + filename_size.length + filename.length];
                    System.arraycopy(msg_type_b, 0, m, 0, msg_type_b.length);
                    System.arraycopy(filename_size, 0, m, filename_size.length, filename_size.length);
                    System.arraycopy(filename, 0, m, (msg_type_b.length + filename_size.length), filename.length);
                    /* cria um pacote datagrama com a mensagem */
                    DatagramPacket request = new DatagramPacket(m, m.length, serverAddr, serverPort);
                    CLIENT_LOGGER.info("The file will be send to server on " + serverAddr + ":" + serverPort);
                    /* envia o pacote */
                    dgramSocket.send(request);
                    System.out.println("Enviou " + request.getData() + " " + request.getData().length + " "
                            + request.getAddress() + " " + request.getPort());

                    
                    /* ========== Envia o arquivo em pacotes de 1024 ========== */
                    msg_type = 1; // Request type: Sendding content
                    FileInputStream inputStream = new FileInputStream(file);
                    DataInputStream dataInputStream = new DataInputStream(inputStream);
                    byte[] packageBytes = new byte[1024];
                    int bytesSent;
                    int sizeFile = inputStream.available();
                    byte[] b = new byte[1024];
                    byte[] msg_type_content = {(new Integer(msg_type)).byteValue()};
                    // Envia cada pacote
                    for (int i = 0; i < sizeFile; i = i + 1024) {
                        int bytes = dataInputStream.read(b);
                        System.out.println("bytes " + sizeFile + ' ' + bytes);

                        byte[] filePackage_size = {(new Integer(inputBuffer.length())).byteValue()};
                        byte[] filePackage = b;

                        /* organização da mensagem a ser enviada em bytes no formato adequado */
                        byte[] m_content = new byte[msg_type_content.length + filePackage_size.length + filePackage.length];
                        System.arraycopy(msg_type_content, 0, m_content, 0, msg_type_content.length);
                        System.arraycopy(filePackage_size, 0, m_content, filePackage_size.length, filePackage_size.length);
                        System.arraycopy(filePackage, 0, m_content, (msg_type_content.length + filePackage_size.length), filePackage.length);

                        /* cria um pacote datagrama com a mensagem */
                        DatagramPacket content = new DatagramPacket(m_content, m_content.length, serverAddr, serverPort);

                        CLIENT_LOGGER.log(Level.INFO, "Sending file. ({0}/{1})", new Object[]{i, sizeFile});

                        /* envia o pacote */
                        dgramSocket.send(content);

                    }
                    dataInputStream.close();
                } else {
                    CLIENT_LOGGER.info("Invalid file. Try again.");
                }

            }

            /* libera o socket */
            dgramSocket.close();

            /* libera o scanner */
            reader.close();
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage()); //exceção
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage()); //exceção
        } //catch
    } //main		      	
} //class
