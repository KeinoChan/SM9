package main;

import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;
import main.SM9.*;
import main.SM9.EllipticCurve.SM9Curve;
import main.SM9.Utils.SM9Utils;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) throws Exception {
        SM9Curve mCurve = new SM9Curve();
        Paillier paillier = new Paillier();
        TCPServer server = new TCPServer(mCurve,paillier);


        String name = server.getName(); //connect with App server1
        Main.showMsg("Received user name: "+name);

        PrivateKey s = server.genSignPrivateKey(name); //connect with the Client
        Main.showMsg("User's private key for signature 'ds_A':");
        Main.showMsg(s.toString());

        server.sendPrivateKey(s); //send s


    }

    public static void showMsg(String msg) {
        System.out.println(msg);
    }
}
