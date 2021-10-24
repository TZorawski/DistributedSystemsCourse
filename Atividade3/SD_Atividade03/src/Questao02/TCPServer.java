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

public class TCPServer {

    public static void main(String args[]) {
        try {
            int serverPort = 6666; // porta do servidor

            /* cria um socket e mapeia a porta para aguardar conexao */
            ServerSocket listenSocket = new ServerSocket(serverPort);

            while (true) {
                System.out.println("Servidor aguardando conexao ...");

                /* aguarda conexoes */
                Socket clientSocket = listenSocket.accept();

                System.out.println("Cliente conectado ... Criando thread ...");

                /* cria um thread para atender a conexao */
                ClientThread c = new ClientThread(clientSocket);

                /* inicializa a thread */
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

    /**
     * Constrói o cliente TCP que se comunica com o servidor através de um
     * protocolo binário, para ser executado pela thread.
     *
     * @param clientSocket é o socket para comunicação
     */
    public ClientThread(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("Connection:" + ioe.getMessage());
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
        byte[] ar = {buffer.get(0), buffer.get(1), buffer.get(2), buffer.get(3)};
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

                ByteArrayOutputStream responseBytesArgs = new ByteArrayOutputStream();
                responseBytesArgs.write((byte) 2);

                File file;

                switch (requestBytesArgs[1]) {
                    case 1:
                        System.out.println("ADDFILE");
                        responseBytesArgs.write((byte) 1);

                        in.read(byteFilenameLength);
                        filenameLength = byteFilenameLength[0] & 0xFF;

                        in.read(byteFilenameLength);
                        filenameLength = byteFilenameLength[0] & 0xFF;

                        byteFilename = new byte[filenameLength];
                        in.read(byteFilename);
                        stringFilename = new String(byteFilename, StandardCharsets.UTF_8);

                        long fileLength = in.readLong();
                        String pathFile = path + stringFilename;

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
                        }
                        break;
                    case 2:
                        System.out.println("DELETE");
                        responseBytesArgs.write((byte) 2);

                        in.read(byteFilenameLength);
                        filenameLength = byteFilenameLength[0] & 0xFF;

                        byteFilename = new byte[filenameLength];
                        in.read(byteFilename);
                        stringFilename = new String(byteFilename);

                        file = new File(path + stringFilename);

                        if (file.delete()) {
                            responseBytesArgs.write((byte) 1);
                            out.write(responseBytesArgs.toByteArray());
                        } else {
                            responseBytesArgs.write((byte) 2);
                            out.write(responseBytesArgs.toByteArray());
                        }
                        break;
                    case 3:
                        System.out.println("GETFILESLIST");
                        responseBytesArgs.write((byte) 3);
                        responseBytesArgs.write((byte) 1);
                        responseBytesArgs.write((byte) files.length);

                        out.write(responseBytesArgs.toByteArray());

                        for (File f : files) {
                            ByteArrayOutputStream sendFile = new ByteArrayOutputStream();

                            sendFile.write(f.getName().length());
                            sendFile.write((byte[]) f.getName().getBytes());

                            out.write(sendFile.toByteArray());
                            out.flush();
                        }
                        break;
                    case 4:
                        System.out.println("GETFILE");
                        responseBytesArgs.write((byte) 4);

                        in.read(byteFilenameLength);
                        filenameLength = byteFilenameLength[0] & 0xFF;

                        byteFilename = new byte[filenameLength];
                        in.read(byteFilename);
                        stringFilename = new String(byteFilename);

                        file = new File(path + stringFilename);

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
                        }
                        break;
                    case 5:
                        stop = true;
                        break;
                    default:
                        stop = true;
                        System.out.println("Error key");
                }
            }
        } catch (EOFException eofe) {
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
        }
        System.out.println("Thread comunicação cliente finalizada.");
    } //run
} //class
