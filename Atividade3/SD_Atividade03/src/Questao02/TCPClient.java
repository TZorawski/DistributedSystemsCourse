package Questao02;

/**
 * TCPClient: Cliente para conexao TCP com utilização de protocolo binário
 *
 * Descricao: Faz solicitações ao servidor: ADDFILE, DELETE, GETFILESLIST e
 * GETFILE. Ao enviar EXIT, a conexão é finalizada.
 *
 * Autor: Thais Zorawski Data de criiação: 18/10/2021
 */
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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
     * @param x um valor long
     * @return array em bytes do valor long
     */
    private static byte[] convertLongToByteArray(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(0, x);
        return buffer.array();
    }

    private static long convertByteArrayToLong(byte[] longBytes) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES);
        byteBuffer.put(longBytes);
        byteBuffer.flip();
        return byteBuffer.getLong();
    }

    public static void main(String args[]) {
        Socket clientSocket = null; // socket do cliente
        Scanner reader = new Scanner(System.in); // ler mensagens via teclado

        try {
            /* Endereço e porta do servidor */
            int serverPort = 6666;
            InetAddress serverAddr = InetAddress.getByName("127.0.0.1");

            /* conecta com o servidor */
            clientSocket = new Socket(serverAddr, serverPort);

            /* cria objetos de leitura e escrita */
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            /* protocolo de comunicação */
            String buffer = "";
            boolean stop = false;
            while (!stop) {
                System.out.print(">>> ");
                buffer = reader.nextLine(); // lê mensagem via teclado

                String[] stringArgs = buffer.split(" ");
                ByteArrayOutputStream bytesArgs = new ByteArrayOutputStream();

                bytesArgs.write((byte) 1);

                String filename = new String();
                byte[] response;
                switch (stringArgs[0]) {
                    case "ADDFILE":
                        bytesArgs.write((byte) 1);
                        filename = stringArgs[1];
                        File file = new File(path + filename);

                        ByteArrayOutputStream teste = new ByteArrayOutputStream();
                        teste.write((byte) filename.length());
//                        teste.write((byte[]) filename.getBytes());

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
                                System.out.println("SUCESSO");
                            } else {
                                System.out.println("ERRO");
                            }
                        } else {
                            System.out.println("Arquivo Inválido");
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
                            System.out.println("SUCESSO");
                        } else {
                            System.out.println("ERRO");
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

                            System.out.println("Total de Arquivos: " + number[0]);

                            for (int i = 0; i < number[0]; i++) {
                                byte filenameLength = in.readByte();

                                byte[] byteFilename = new byte[filenameLength];
                                in.read(byteFilename, 0, filenameLength);
                                filename = new String(byteFilename);
                                System.out.println("- " + filename);
                            }
                        } else {
                            System.out.println("ERRO");
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
                                System.out.println("SUCESSO");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("ERRO");
                        }

                        break;
                    case "EXIT":
                        bytesArgs.write((byte) 5);
                        stop = true;
                        break;
                    default:
                        System.out.println("Error key");
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
                clientSocket.close();
            } catch (IOException ioe) {
                System.out.println("IO: " + ioe);;
            }
        }
    } //main
} //class
