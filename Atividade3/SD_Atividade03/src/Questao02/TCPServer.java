package Questao02;

/**
 * TCPServer: Servidor para conexao TCP com Threads Descricao: Recebe uma
 * conexao, cria uma thread, recebe uma mensagem e finaliza a conexao.
 *
 * Autor: Thais Zorawski Data de criiação: 18/10/2021
 */
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServer {
static final Logger SERVER_LOGGER = Logger.getLogger(TCPServer.class.getName());
    public static void main(String args[]) {
        try {
            int serverPort = 6666; // Server port

            /* Creates a socket and maps the port to wait for connection */
            ServerSocket listenSocket = new ServerSocket(serverPort);

            while (true) {
                SERVER_LOGGER.info("Server waiting for connection...");

                /* Waiting for connections */
                Socket clientSocket = listenSocket.accept();

                SERVER_LOGGER.info("Client connected. Creating thread...");

                /* Create a thread to service the connection */
                ClientThread c = new ClientThread(clientSocket);

                /* Initialize the thread */
                c.start();
            } //while

        } catch (IOException e) {
            System.out.println("Listen socket:" + e.getMessage());
        } //catch
    } //main
} //class

/**
 * Classe ClientThread: Thread responsavel pela comunicacao.
 *
 * Descricao: Rebebe um socket, cria os objetos de leitura e escrita, aguarda
 * solicitações clientes e responde com as ações correspondentes.
 */
class ClientThread extends Thread {

    public static String path = ".\\src\\serverStorage\\";
    File filename = new File(path);
    File[] files = filename.listFiles();

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    
    static final Logger CLIENT_LOGGER = Logger.getLogger(ClientThread.class.getName());


    /**
     * Constrói o cliente TCP que se comunica com o servidor através de um
     * protocolo binário, para ser executado pela thread.
     *
     * @param clientSocket é o socket para comunicação
     */
    public ClientThread(Socket clientSocket) {
        try {
            CLIENT_LOGGER.info("Client connected");
            this.clientSocket = clientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ioe) {
            CLIENT_LOGGER.log(Level.SEVERE, "Connection: {0}", ioe.getMessage());
        } //catch
    } //construtor

    /**
     * Helper que converte um valor long para uma lista de bytes.
     *
     * @param x um valor long
     * @return array em bytes do valor long
     */
    public byte[] longToByteArray(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    /**
     * Executa ao iniciar a thread. Gerencia a comunicação com o servidor.
     */
    @Override
    public void run() {
        try {
            byte[] requestBytesArgs = new byte[2];
            boolean stop = false;
            while (!stop) {
                in.read(requestBytesArgs);
                byte[] response;
                byte[] byteFilenameLength = new byte[1];
                int filenameLength;
                byte[] byteFilename;
                String stringFilename;

                // responseBytesArgs stores the response
                ByteArrayOutputStream responseBytesArgs = new ByteArrayOutputStream();
                responseBytesArgs.write((byte) 2);

                File file;

                switch (requestBytesArgs[1]) {
                    case 1:
                        CLIENT_LOGGER.info("Executing ADDFILE");
                        responseBytesArgs.write((byte) 1);

                        in.read(byteFilenameLength);
                        in.read(byteFilenameLength);
                        filenameLength = byteFilenameLength[0] & 0xFF;

                        byteFilename = new byte[filenameLength];
                        in.read(byteFilename);
                        stringFilename = new String(byteFilename, StandardCharsets.UTF_8);

                        long fileLength = in.readLong();
                        String pathFile = path + stringFilename;

                        // Saves the content of file
                        try {
                            DataOutputStream outStream = new DataOutputStream(new FileOutputStream(pathFile));

                            byte[] bytes = new byte[1];
                            for (int i = 0; i < fileLength; i++) {
                                in.read(bytes);
                                outStream.write(bytes);
                                outStream.flush();
                            }
                            outStream.close();

                            responseBytesArgs.write((byte) 1);
                            out.write(responseBytesArgs.toByteArray());
                        } catch (IOException e) {
                            e.printStackTrace();

                            responseBytesArgs.write((byte) 2);
                            out.write(responseBytesArgs.toByteArray());
                            CLIENT_LOGGER.log(Level.SEVERE, "Invalid file");
                        }
                        break;
                    case 2:
                        CLIENT_LOGGER.info("Executing DELETE");
                        responseBytesArgs.write((byte) 2);

                        in.read(byteFilenameLength);
                        filenameLength = byteFilenameLength[0] & 0xFF;

                        byteFilename = new byte[filenameLength];
                        in.read(byteFilename);
                        stringFilename = new String(byteFilename);

                        file = new File(path + stringFilename);

                        // Deletes file
                        if (file.delete()) {
                            responseBytesArgs.write((byte) 1);
                            out.write(responseBytesArgs.toByteArray());
                        } else {
                            responseBytesArgs.write((byte) 2);
                            out.write(responseBytesArgs.toByteArray());
                            CLIENT_LOGGER.log(Level.SEVERE, "Invalid file");
                        }
                        break;
                    case 3:
                        CLIENT_LOGGER.info("Executing GETFILESLIST");
                        responseBytesArgs.write((byte) 3);
                        responseBytesArgs.write((byte) 1);
                        responseBytesArgs.write((byte) files.length);

                        out.write(responseBytesArgs.toByteArray());

                        // Lists server files
                        for (File f : files) {
                            ByteArrayOutputStream sendFile = new ByteArrayOutputStream();

                            sendFile.write(f.getName().length());
                            sendFile.write((byte[]) f.getName().getBytes());

                            out.write(sendFile.toByteArray());
                            out.flush();
                        }
                        break;
                    case 4:
                        CLIENT_LOGGER.info("Executing GETFILE");
                        responseBytesArgs.write((byte) 4);

                        in.read(byteFilenameLength);
                        filenameLength = byteFilenameLength[0] & 0xFF;

                        byteFilename = new byte[filenameLength];
                        in.read(byteFilename);
                        stringFilename = new String(byteFilename);

                        file = new File(path + stringFilename);

                        // Sends file
                        if (file.isFile() && file.canRead()) {
                            InputStream inStream = new FileInputStream(file);

                            responseBytesArgs.write((byte) 1);
                            byte[] tam = longToByteArray(file.length());
                            responseBytesArgs.write(tam);

                            byte[] bytes = new byte[(int) file.length()];
                            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
                            dataInputStream.readFully(bytes);

                            responseBytesArgs.write(bytes);

                            out.write(responseBytesArgs.toByteArray());
                            dataInputStream.close();
                            inStream.close();
                        } else {
                            responseBytesArgs.write((byte) 2);
                            out.write(responseBytesArgs.toByteArray());
                            CLIENT_LOGGER.log(Level.SEVERE, "Invalid file.");
                        }
                        break;
                    case 5:
                        // Stop option
                        stop = true;
                        break;
                    default:
                        // Invalid option
                        stop = true;
                        CLIENT_LOGGER.log(Level.SEVERE, "Invalid key");
                }
            }
        } catch (EOFException eofe) {
            CLIENT_LOGGER.log(Level.SEVERE, "EOF: {0}", eofe.getMessage());
        } catch (IOException ioe) {
            CLIENT_LOGGER.log(Level.SEVERE, "IOE: {0}", ioe.getMessage());
        } finally {
            try {
                // Closes communication
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException ioe) {
                CLIENT_LOGGER.log(Level.SEVERE, "IOE: {0}", ioe);
            }
        }
        CLIENT_LOGGER.info("Communication with the client finished");
    } //run
} //class
