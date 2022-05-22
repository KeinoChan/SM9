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


        //验签
        SM9 sm9 = new SM9(mCurve);
        BigInteger s1 = server.s1;
        BigInteger s2 = server.gets2();

        BigInteger ks = (s1.add(s2)).mod(mCurve.N);
        BigInteger h1 = SM9Utils.H1(name,mCurve.N);
        BigInteger t1 = h1.add(ks);

        BigInteger t2 = ks.multiply(t1.modInverse(mCurve.N)).mod(mCurve.N);
        PrivateKey ds = new PrivateKey(mCurve.P1.duplicate().mul(t2));
        //生成主公钥
        CurveElement ppubs = mCurve.P2.duplicate().mul(ks);
        MasterPublicKey pubkey = new MasterPublicKey(ppubs);

        //此处应显示签名
        String msg = "Chinese SM9 Standard";
        ResultSignature signature = sm9.sign(pubkey,s,msg.getBytes());

        if(sm9.verify(pubkey,name,msg.getBytes(),signature))
            System.out.println("verify success");
        else
            System.out.println("verify failed");

    }

    public static void showMsg(String msg) {
        System.out.println(msg);
    }
}
