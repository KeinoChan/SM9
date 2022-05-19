package main;

import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class SocketServerTest {

        public static void main(String[] args) throws Exception {

            BigInteger r = new BigInteger(1024, new Random());
            System.out.println("r 的值："+r);

            ServerSocket server = new ServerSocket(5555);
            Socket sock = server.accept();
            System.out.println("connected from " + sock.getRemoteSocketAddress()); //connect socket

    }
}
