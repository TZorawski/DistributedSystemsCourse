package aula_udp;

/**
 * UDPClient: Cliente UDP Descricao: Envia uma msg em um datagrama e recebe a
 * mesma msg do servidor
 */
import java.net.*;
import java.io.*;
import javax.swing.JOptionPane;
import java.util.Scanner; //scanner para ler o digitado
import java.lang.Integer;

public class UDPClient {

    public static void printer(byte[] pack) {
        int type = Integer.parseInt(Byte.toString(pack[0]));
        int tam_apl = Integer.parseInt(Byte.toString(pack[1]));
        String apelido = "";
        int tam_msg = 0;
        String msg = "";
        int pos = 0;

        for (int i = 2; i <= tam_apl+1; i++){
            byte[] tmp = {pack[i]};
            try{
                apelido += new String(tmp, "UTF-8");
            } catch (IOException e) {
                apelido += "";
            }
            pos = i;
        }

        pos += 1;
        tam_msg = Integer.parseInt(Byte.toString(pack[pos]));

        for (int j = (pos+1); j <= (pos+tam_msg+1); j++){
            byte[] tmp = {pack[j]};
            try{
                msg += new String(tmp, "UTF-8");
            } catch (IOException e) {
                msg += "";
            }
        }

        if (type == 3) {
            System.out.println(apelido + " enviou o link: " + msg);
        } else{
            System.out.println(apelido + ": " + msg);
        }
    }

    public static void main(String args[]) {
        DatagramSocket dgramSocket = null;
        String msg = "";
        Scanner reader = new Scanner(System.in);

        try {
            System.out.print("IP (padrao 127.0.0.1): ");
            String dstIP = reader.nextLine();

            if(dstIP.equals("")){
                dstIP = "127.0.0.1";
            }

            dgramSocket = new DatagramSocket();
            System.out.println("Sua porta: " + dgramSocket.getLocalPort());

            System.out.print("Porta de destino: ");
            int dstPort = Integer.parseInt(reader.nextLine());
            
            System.out.println("Criada conexao com " + dstIP + ":" + dstPort);

            System.out.print("Nickname: ");
            String nick = reader.nextLine();
            if (nick.length() > 64){
                String cut = nick.substring(0, 64);
                nick = cut;
            }
            
            /* armazena o IP do destino */
            InetAddress serverAddr = InetAddress.getByName(dstIP);
            int serverPort = dstPort; // porta do servidor

            do {
                int msg_type = 1;
                System.out.print("Mensagem: "); //imprime antes da mensagem
                msg = reader.nextLine();

                if (msg.length() > 255){
                    String cut_msg = msg.substring(0, 255);
                    msg = cut_msg;
                }

                if (msg.contains("http://") || msg.contains("https://")){
                    msg_type = 3;
                }
                if (msg.contains("ECHO")){
                    msg_type = 4;
                }

                byte[] msg_type_b = { (new Integer(msg_type)).byteValue() };
                byte[] tam_apl = { (new Integer(nick.length())).byteValue() };
                byte[] apelido = nick.getBytes();
                byte[] tam_msg = { (new Integer(msg.length())).byteValue() };
                byte[] msg_b = msg.getBytes();

                byte[] m = new byte[msg_type_b.length + tam_apl.length + apelido.length + tam_msg.length + msg_b.length];
                System.arraycopy(msg_type_b, 0, m, 0, msg_type_b.length);
                System.arraycopy(tam_apl, 0, m, msg_type_b.length, tam_apl.length);
                System.arraycopy(apelido, 0, m, (msg_type_b.length + tam_apl.length), apelido.length);
                System.arraycopy(tam_msg, 0, m, (msg_type_b.length + tam_apl.length + apelido.length), tam_msg.length);
                System.arraycopy(msg_b, 0, m, (msg_type_b.length + tam_apl.length + apelido.length + tam_msg.length), msg_b.length);


                /* cria um pacote datagrama */
                DatagramPacket request
                        = new DatagramPacket(m, m.length, serverAddr, serverPort);

                /* envia o pacote */
                dgramSocket.send(request);
                if (msg_type == 4) {
                    System.out.println(nick + ": " + msg);
                }

                /* cria um buffer de tamanho 325 para receber datagramas */
                byte[] buffer = new byte[325];
                DatagramPacket dgramPacket = new DatagramPacket(buffer, buffer.length, serverAddr, serverPort);

                dgramSocket.receive(dgramPacket);  // aguarda a chegada de datagramas

                printer(dgramPacket.getData());

            } while (!msg.equals("desconectar"));

            /* libera o socket */
            dgramSocket.close();
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } //catch
    } //main		      	
} //class
