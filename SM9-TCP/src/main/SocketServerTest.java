package main;

import main.SM9.Utils.Hex;
import main.SM9.Utils.SM9Utils;

import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class SocketServerTest {

        public static void main(String[] args) throws Exception {

            BigInteger r = new BigInteger(1024, new Random());
            System.out.println("r 的值："+r);
            String rDec = r.toString(16).toUpperCase();
            System.out.println(rDec);


            BigInteger beta = new BigInteger("B640000002A3A6F1D603AB4FF58EC74521F2934B1A7AEEDBE56F9B27E351457B", 16);
            System.out.println("beta:"+beta);

            BigInteger s = new BigInteger("3547461311252004136815905956608839056438732586398306" +
                    "2656231239332188871537243822752272216440011698073756274884898901033872681013319063" +
                    "01479801375086339721264922857996204276284446917139562388988883028000520697606287904037420" +
                    "5789784419017493845606267053741780216478057563186349466845658140839288170795333309137");

            System.out.println(s.toString(16).toUpperCase());

    }
}
