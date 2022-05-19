package main.SM9;// A Java program for a Client
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;
import main.SM9.EllipticCurve.SM9Curve;
import main.SM9.Utils.Hex;
import main.SM9.Utils.SM9Utils;

import java.math.BigInteger;
import java.net.*;
import java.io.*;

/**
 * SM9 TPC client.
 *
 * The client for generate private key Q2
 *
 * Created by Chan on 2022/4/22.
 */

public class TCPClient {

    public static void main(String[] args) throws Exception{

        SM9Curve mCurve = new SM9Curve();
        Paillier paillier = new Paillier();
        BigInteger s2 = SM9Utils.genRandom(mCurve.random, mCurve.N); //generate s2

        Socket sock = new Socket("192.168.1.109", 6666);
        System.out.println("Connected");
        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);

        BigInteger c0 = new BigInteger(in.readLine()); //PKG2 get c0
        BigInteger c1 = new BigInteger(in.readLine()); //PKG2 get c1

        //Step2 Start
        BigInteger b1 = SM9Utils.genRandom(mCurve.random,mCurve.N);
        BigInteger w2 = SM9Utils.genRandom(mCurve.random,mCurve.N); //select b1, w2
        BigInteger b2 = mCurve.N.subtract(b1); //calculate b2 = N-b1

        BigInteger z1 = SM9Utils.genRandom(mCurve.random,mCurve.N);
        BigInteger z2 = SM9Utils.genRandom(mCurve.random,mCurve.N); //select z1&z2

        BigInteger c2_0 = w2.multiply(s2).mod(mCurve.N).add(z1.multiply(mCurve.N));
        BigInteger c2_1 = paillier.cipher_multi(c0,c2_0);
        BigInteger c2_2 = paillier.Encryption(z2.multiply(mCurve.N).add(b1));
        BigInteger c2 = paillier.cipher_add(c2_2,c2_1); //get c2

        BigInteger c3 = w2.multiply(c1).add(b2).mod(mCurve.N); //get c3
        //Step 2 end

        out.println(c2); //send c2
        out.println(c3); //send c3

        //Step4 start
        String Q1_HexString = in.readLine();

        CurveElement Q1 = mCurve.G1.newElement();
        Q1.setFromBytes(Hex.decode(Q1_HexString));
        //Main.showMsg("Q1 in client: "+Q1.toString());

        CurveElement Q2 = (CurveElement) mCurve.P1.duplicate().sub(Q1.mul(w2));
        //Step4 end

        //send Q2
        out.println(Hex.encodeToString(SM9Utils.G1ElementToBytes(Q2),true));//send Q2(ds) to server
        //System.out.println("Q2 in client: "+Q2);


        //System.out.println("c0:"+c0);
        //System.out.println("E(c0):"+paillier.Encryption(c0));

        out.println(s2);

        sock.close();

    }



}
