/**
 * Descrição: Cliente TCP que se comunica com servidor de arquivos através de 
 * protocolo binário. Opções de requisição:
 *  - ADDFILE: envia um arquivo ao servidor.
 *  - DELETE: remove um arquivo do servidor.
 *  - GETFILESLIST: exibe a lista de arquivos do servidor.
 *  - GETFILE: recebe um arquivo do servidor.
 *  - EXIT: encerra a comunicação com o servidor.
 * 
 * Autores: Guilherme Vasco da Silva, Thais Zorawski
 * Data da criação: 18/10/2021
 * Data de atualização: 24/10/2021
 */

package Questao02;

/**
 * TCPClient: Cliente para conexao TCP com utilização de protocolo binário
 *
 * Descricao: Faz solicitações ao servidor: ADDFILE, DELETE, GETFILESLIST e
 * GETFILE. Ao enviar EXIT, a conexão é finalizada.
 */
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * Classe TCPClient: Classe responsavel pela comunicacao.
 *
 * Descricao: A partir de um socket, cria os objetos de leitura e escrita, envia
 * solicitações para o servidor e recebe as respostas com as ações
 * correspondentes.
 */
public class TCPClient {
    public static String path = ".\\src\\clientStorage\\";

    /**
     * Helper que converte um valor long para uma lista de bytes.
     *
     * @param longBytes um valor long
     * @return array em bytes do valor long
     */
    private static byte[] convertLongToByteArray(long longBytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(0, longBytes);
        return buffer.array();
    }

     /**
     * Helper que converte uma lista de bytes para um valor long.
     *
     * @param longBytes um array em bytes do valor long
     * @return valor long
     */
    private static long convertByteArrayToLong(byte[] longBytes) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
        byteBuffer.put(longBytes);
        byteBuffer.flip();
        return byteBuffer.getLong();
    }

    public static void main(String args[]) {
        Socket clientSocket = null; // Client socket
        Scanner reader = new Scanner(System.in); // Reads messages from keyboard

        try {
            int serverPort = 6666; // Server port
            InetAddress serverAddr = InetAddress.getByName("127.0.0.1"); // Server IP

            clientSocket = new Socket(serverAddr, serverPort); // Connects to the server

            /* Write and Read objects */
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            String buffer = "";
            boolean stop = false;
            while (!stop) {
                System.out.print(">>> ");
                buffer = reader.nextLine();

                String[] stringArgs = buffer.split(" ");
                ByteArrayOutputStream bytesArgs = new ByteArrayOutputStream();

                bytesArgs.write((byte) 1);

                String filename;
                byte[] response;
                switch (stringArgs[0]) {
                    case "ADDFILE":
                        bytesArgs.write((byte) 1);
                        filename = stringArgs[1];
                        File file = new File(path + filename);

                        ByteArrayOutputStream teste = new ByteArrayOutputStream();
                        teste.write((byte) filename.length());

                        // Sends file
                        if (file.isFile() && file.canRead()) {
                            bytesArgs.write((byte) 1);
                            bytesArgs.write((byte) filename.length());
                            bytesArgs.write((byte[]) filename.getBytes());
                            bytesArgs.write(convertLongToByteArray(file.length()));

                            byte[] bytes = new byte[(int) file.length()];
                            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
                            dataInputStream.readFully(bytes);
                            dataInputStream.close();
                            bytesArgs.write(bytes);

                            out.write(bytesArgs.toByteArray());

                            response = new byte[3];
                            in.read(response);

                            if (response[2] == 1) {
                                System.out.println("SUCCESS");
                            } else {
                                System.out.println("ERROR");
                            }
                        } else {
                            System.out.println("Invalid file");
                        }
                        break;
                    case "DELETE":
                        bytesArgs.write((byte) 2);
                        filename = stringArgs[1];

                        bytesArgs.write((byte) filename.length());
                        bytesArgs.write((byte[]) filename.getBytes());

                        out.write(bytesArgs.toByteArray());

                        response = new byte[3];
                        in.read(response);

                        if (response[2] == 1) {
                            System.out.println("SUCCESS");
                        } else {
                            System.out.println("ERROR");
                        }
                        break;
                    case "GETFILESLIST":
                        bytesArgs.write((byte) 3);

                        out.write(bytesArgs.toByteArray());

                        response = new byte[3];
                        in.read(response);

                        if (response[2] == 1) {
                            byte[] number = new byte[1];
                            in.read(number);

                            System.out.println("Total files: " + number[0]);

                            // Shows files list
                            for (int i = 0; i < number[0]; i++) {
                                byte filenameLength = in.readByte();

                                byte[] byteFilename = new byte[filenameLength];
                                in.read(byteFilename, 0, filenameLength);
                                filename = new String(byteFilename);
                                System.out.println("- " + filename);
                            }
                        } else {
                            System.out.println("ERROR");
                        }
                        break;
                    case "GETFILE":
                        bytesArgs.write((byte) 4);
                        filename = stringArgs[1];

                        bytesArgs.write((byte) filename.length());
                        bytesArgs.write((byte[]) filename.getBytes());

                        out.write(bytesArgs.toByteArray());

                        response = new byte[3];
                        in.read(response);

                        if (response[2] == 1) {
                            DataOutputStream outStream = new DataOutputStream(new FileOutputStream(path + filename));

                            // Saves the content of file
                            try {
                                byte[] byteFileLength = new byte[8];
                                in.read(byteFileLength);
                                long fileLength = convertByteArrayToLong(byteFileLength);
                                
                                byte[] bf = new byte[1];
                                for (int i = 0; i < fileLength; i++) {
                                    in.read(bf);
                                    outStream.write(bf);
                                    outStream.flush();
                                }
                                outStream.close();
                                System.out.println("SUCCESS");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("ERROR");
                        }

                        break;
                    case "EXIT":
                        // Stop option
                        bytesArgs.write((byte) 5);
                        stop = true;
                        break;
                    default:
                        // Invalid option
                        System.out.println("Invalid key");
                        bytesArgs.write((byte) 5);
                }
            }
        } catch (UnknownHostException ue) {
            System.out.println("Socket:" + ue.getMessage());
        } catch (EOFException eofe) {
            System.out.println("EOF:" + eofe.getMessage());
        } catch (IOException ioe) {
            System.out.println("IO:" + ioe.getMessage());
        } finally {
            try {
                // Closes communication
                clientSocket.close();
            } catch (IOException ioe) {
                System.out.println("IO: " + ioe);;
            }
        }
    } //main
} //class
