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
        TCPClient client = new TCPClient(mCurve,paillier);

        BigInteger s1 = server.s1;
        BigInteger s2 = client.s2;

        Thread Server = new Thread(() -> {
            //生成签名私钥

            String name = server.getName();
            Main.showMsg("Received user name: "+name);
            PrivateKey s = server.genSignPrivateKey(name);
            Main.showMsg("User's private key for signature 'ds_A':");
            Main.showMsg(s.toString());

            server.sendPrivateKey(s); //send s

            Main.showMsg("Some values during the signature");
            String msg = "Chinese IBS standard";
            Main.showMsg("Message to be signed 'M':");
            Main.showMsg(msg);

            SM9 sm9 = new SM9(mCurve);
            BigInteger ks = (s1.add(s2)).mod(mCurve.N); //ks = (s1+s2) mod n
            BigInteger h1 = SM9Utils.H1(name, mCurve.N);
            BigInteger t1 = h1.add(ks);
            if(t1.equals(BigInteger.ZERO))
                try {
                    throw new Exception("Need to update the master private key");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            BigInteger t2 = ks.multiply(t1.modInverse(mCurve.N)).mod(mCurve.N); //change the two mod from mCurve.N to paillier.n (doesn't work)
            PrivateKey ds = new PrivateKey(mCurve.P1.duplicate().mul(t2));
            Main.showMsg("User's private key for signature 'ds_A'-ds:");
            Main.showMsg(ds.toString());

            //生成主公钥Ppub
            CurveElement ppubs = mCurve.P2.duplicate().mul(ks);
            MasterPublicKey pubkey = new MasterPublicKey(ppubs);
            ResultSignature signature = sm9.sign(pubkey, s, msg.getBytes());
            Main.showMsg("signature of the message '(h,s)':");
            Main.showMsg(signature.toString());

            //验签
            if(sm9.verify(pubkey, name, msg.getBytes(), signature))
                Main.showMsg("verify success");
            else
                Main.showMsg("verify failed");

        });

        Thread Client = new Thread(() -> {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            client.run();
        });

        Server.start();
        Client.start();

    }

    public static void showMsg(String msg) {
        System.out.println(msg);
    }
}
