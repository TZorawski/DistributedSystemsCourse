/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javacode.Addressbook;

/**
 *
 * @author thais
 */
public class Server {

    public static void main(String args[]) {
        try {
            int serverPort = 7000;
            ServerSocket listenSocket = new ServerSocket(serverPort);

            Socket clientSocket;
            DataInputStream inClient;

            while (true) {
                clientSocket = listenSocket.accept();
                System.out.println("aceitando");
                inClient = new DataInputStream(clientSocket.getInputStream());
                System.out.println("criou receptor");
                String valueStr = inClient.readUTF();
                System.out.println("Leu linha");
                System.out.println("Valor " + valueStr);
                int sizeBuffer = Integer.valueOf(valueStr);
                byte[] buffer = new byte[sizeBuffer];
                inClient.read(buffer);
                System.out.println("Leu linha");
                System.out.println("Valor " + valueStr);
               

                Addressbook.Matricula m = Addressbook.Matricula.parseFrom(buffer);

                System.out.println("Matricula: " + m.toString());

                // Processamento
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
