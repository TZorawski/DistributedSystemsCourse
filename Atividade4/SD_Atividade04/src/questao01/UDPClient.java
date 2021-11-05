/**
 * Descrição: Chat P2P que possibilita dois clientes trocarem mensagens entre si.
 * As mensagens possuem o formato: Message Type (1 byte) + Nick Size (1 byte) +
 * Nick [Nick Size] (1-64 bytes) + Message Size (1 byte) + Message [Message Size]
 * (0-255 bytes).
 * Os tipo de mensagem são: 1 - mensagem normal, 2 - emoji (inativo), 3 - URL,
 * 4 - ECHO.
 * Autores: Guilherme Vasco da Silva, Thais Zorawski
 * Data da criação: 22/10/2021
 * Data de atualização: 27/10/2021
 */

package aula_udp;

import java.net.*;
import java.io.*;
import java.util.Scanner; //scanner para ler o digitado
import java.lang.Integer; //Integer para a conversão em byte[]

public class UDPClient { //classe principal do cliente

    /* função para impressão do recebido */
    public static void printer(byte[] pack) { //printer
        /* obtenção do primeito byte (tipo da mensagem) */
        int type = Integer.parseInt(Byte.toString(pack[0]));
        /* obtenção do segundo byte (tamanho do nick) */
        int tam_apl = Integer.parseInt(Byte.toString(pack[1]));
        String apelido = ""; //nick vazio
        int tam_msg = 0; //tamanho da mensagem
        String msg = ""; //mensagem vazia
        int pos = 0; //posição nos bytes, inicio em 0

        for (int i = 2; i <= tam_apl+1; i++){ //for nick
            byte[] tmp = {pack[i]}; //byte temporário
            try{
                apelido += new String(tmp, "UTF-8"); //pega bytes em String e coloca no apelido
            } catch (IOException e) {
                apelido += ""; //exceção
            }
            pos = i;
        } //fim do for nick

        pos += 1; //incremento de posição
        /* obtenção do tamanho da mensagem */
        tam_msg = Integer.parseInt(Byte.toString(pack[pos]));

        for (int j = (pos+1); j <= (pos+tam_msg+1); j++){ //for mensagem
            byte[] tmp = {pack[j]}; //byte temporário
            try{
                msg += new String(tmp, "UTF-8"); //pega bytes em String e coloca na mensagem
            } catch (IOException e) {
                msg += ""; //exceção
            }
        } //fim do for mensagem

        if (type == 3) { //if para mensagem tipo 3 (URL)
            System.out.println(apelido + " enviou o link: " + msg);
        } else{ //mensagem tipo 1
            System.out.println(apelido + ": " + msg);
        }
    } //fim de printer

    public static void main(String args[]) { //função principal
        DatagramSocket dgramSocket = null; //Datagrama vazio
        String msg = ""; //mensagem vazia
        Scanner reader = new Scanner(System.in); //scanner

        try {
            /* lê o ip desejado */
            System.out.print("IP (padrao 127.0.0.1): ");
            String dstIP = reader.nextLine();

            /* se não for digitado ip, utiliza localhost */
            if(dstIP.equals("")){ 
                dstIP = "127.0.0.1";
            }

            dgramSocket = new DatagramSocket(); //cria datagram socket
            System.out.println("Sua porta: " + dgramSocket.getLocalPort()); //imprime porta

            /* recebe porta de destino */
            System.out.print("Porta de destino: ");
            int dstPort = Integer.parseInt(reader.nextLine());
            
            /* conexão estabelecida */
            System.out.println("Conexao estabelecida com " + dstIP + ":" + dstPort);

            /* recebe nickname */
            System.out.print("Nickname: ");
            String nick = reader.nextLine();

            /* limita nick em 64 bytes */
            if (nick.length() > 64){
                String cut = nick.substring(0, 64);
                nick = cut;
            }
            
            /* armazena o IP do destino e porta */
            InetAddress serverAddr = InetAddress.getByName(dstIP);
            int serverPort = dstPort; // porta do servidor
            do { //do while
                int msg_type = 1; //tipo da mensagem
                /* recebe mensagem a ser enviada */
                System.out.print("Mensagem: ");
                msg = reader.nextLine();

                /* limita mensagem em 255 bytes */
                if (msg.length() > 255){
                    String cut_msg = msg.substring(0, 255);
                    msg = cut_msg;
                }

                /* identifica se mensagem é do tipo 3 (URL) */
                if (msg.contains("http://") || msg.contains("https://")){
                    msg_type = 3;
                }
                /* identifica se mensagem é do tipo 4 (ECHO) */
                if (msg.contains("ECHO")){
                    msg_type = 4;
                }

                /* obtenção dos valores em byte da mensagem */
                byte[] msg_type_b = { (new Integer(msg_type)).byteValue() };
                byte[] tam_apl = { (new Integer(nick.length())).byteValue() };
                byte[] apelido = nick.getBytes();
                byte[] tam_msg = { (new Integer(msg.length())).byteValue() };
                byte[] msg_b = msg.getBytes();
                /* Integer gera warnings por ser depreciado */

                /* organização da mensagem a ser enviada em bytes no formato adequado */
                byte[] m = new byte[msg_type_b.length + tam_apl.length + apelido.length + tam_msg.length + msg_b.length];
                System.arraycopy(msg_type_b, 0, m, 0, msg_type_b.length);
                System.arraycopy(tam_apl, 0, m, msg_type_b.length, tam_apl.length);
                System.arraycopy(apelido, 0, m, (msg_type_b.length + tam_apl.length), apelido.length);
                System.arraycopy(tam_msg, 0, m, (msg_type_b.length + tam_apl.length + apelido.length), tam_msg.length);
                System.arraycopy(msg_b, 0, m, (msg_type_b.length + tam_apl.length + apelido.length + tam_msg.length), msg_b.length);


                /* cria um pacote datagrama com a mensagem */
                DatagramPacket request = new DatagramPacket(m, m.length, serverAddr, serverPort);

                /* envia o pacote */
                dgramSocket.send(request);

                /* para mensagens do tipo ECHO, imprime mensagem no remetente */
                if (msg_type == 4) {
                    System.out.println(nick + ": " + msg);
                }

                /* cria um buffer de tamanho 325 para receber datagramas */
                byte[] buffer = new byte[325]; //tamanho máximo da mensagem + 2

                /* cria datagram packet */
                DatagramPacket dgramPacket = new DatagramPacket(buffer, buffer.length, serverAddr, serverPort);

                /* atualização funciona de forma manual por conta desse receive */
                dgramSocket.receive(dgramPacket);  // aguarda a chegada de datagramas

                /* chamada da função printer para imprimir mensagem recebida */
                printer(dgramPacket.getData());

            } while (!msg.equals("desconectar")); //fim de do while

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
