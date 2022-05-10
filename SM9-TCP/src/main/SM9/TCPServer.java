package main.SM9;// A Java program for a Server
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;
import main.SM9.EllipticCurve.SM9Curve;
import main.SM9.Utils.Hex;
import main.SM9.Utils.SM9Utils;

import java.math.BigInteger;
import java.net.*;
import java.io.*;

/**
 * SM9 TPC server.
 *
 * A server for generate private key Q2
 *
 * Created by Chan on 2022/4/21.
 */

public class TCPServer {
    SM9Curve mCurve = new SM9Curve();
    Paillier paillier = new Paillier();

    public TCPServer(SM9Curve mCurve, Paillier paillier) throws IOException {
        this.mCurve = mCurve;
        this.paillier = paillier;
    }
    public final BigInteger s1 = SM9Utils.genRandom(mCurve.random, mCurve.N); //generate s1

    ServerSocket server = new ServerSocket(6666);

    public  String getName() {
        try{
            System.out.println("connecting the App...");
            Socket sock = server.accept();
            System.out.println("connected from " + sock.getRemoteSocketAddress()); //connect socket

            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

            String name = in.readLine();

            return name;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPrivateKey(PrivateKey s){
        try{
            System.out.println("Ready to send s to the App...");
            Socket sock = server.accept();
            System.out.println("connected from " + sock.getRemoteSocketAddress()); //connect socket

            PrintWriter out = new PrintWriter(sock.getOutputStream(),true);

            out.println(s.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PrivateKey genSignPrivateKey(String id){
        try{
            //ServerSocket server = new ServerSocket(6666);
            System.out.println("PKG1 Server started");
            System.out.println("Waiting for PKG2 connection ...");

            Socket sock = server.accept();

            System.out.println("connected from " + sock.getRemoteSocketAddress()); //connect socket

            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter out = new PrintWriter(sock.getOutputStream(),true);

            //Step 1 start
            BigInteger a1 = SM9Utils.genRandom(mCurve.random,mCurve.N);
            BigInteger a2 = a1.modInverse(mCurve.N);

            BigInteger c0 = paillier.Encryption(a1);
            BigInteger h1 = SM9Utils.H1(id, mCurve.N);
            BigInteger c1 = a1.multiply(h1.add(s1)).mod(mCurve.N);
            //Step 1 end

            out.println(c0); //send c0 in String(auto change to String)
            out.println(c1); //send c1 in String

            BigInteger c2 = new BigInteger(in.readLine()); //pkg1 get c2
            BigInteger c3 = new BigInteger(in.readLine()); //pkg1 get c3

            //Step3 start
            BigInteger c4 = paillier.Decryption(c2);
            if(c3.equals(BigInteger.ZERO)||c4.equals(BigInteger.ZERO)){
                try {
                    throw new Exception("c3 or c4 is 0, please regenerate");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            BigInteger c = a2.multiply(c3.add(c4)).mod(mCurve.N);
            CurveElement Q1 = mCurve.P1.duplicate().mul(h1.multiply(c.modInverse(mCurve.N)).mod(mCurve.N)); //c^(-1)
            //Step3 end

            //Main.showMsg("Q1 in server:"+Q1);
            out.println(Hex.encodeToString(SM9Utils.G1ElementToBytes(Q1))); //send Q1 in Hex String

            CurveElement Q2 = mCurve.G1.newElement();
            Q2.setFromBytes(Hex.decode(in.readLine()));
            //Main.showMsg("Q2 in server: "+ Q2);

            return new PrivateKey(Q2);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
