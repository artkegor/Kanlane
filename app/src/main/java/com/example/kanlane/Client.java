package com.example.kanlane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8189);
        DataInputStream is = new DataInputStream(socket.getInputStream());
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        Thread writeThread = new Thread(


        );
        writeThread.setDaemon(true);
        writeThread.start();
        while (true) {
            String msg = is.readUTF();
            System.out.println("-> " + msg);
        }
    }
}