package main;

import java.net.Socket;

public class SocketClientTest {

        public static void main(String[] args) throws Exception {
            Socket sock = new Socket("127.0.0.1", 5555);
            System.out.println("Connected");

            sock.close();
            
    }
}
