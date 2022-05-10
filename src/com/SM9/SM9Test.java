package com.SM9;

import com.Main;

/**
 * SM9 standard test.
 *
 * Created by Chan on 2022/4/8.
 */

public class SM9Test {
    private SM9Test() {

    }

    public static void test_sm9_sign(KGC kgc, SM9 sm9) throws Exception{
        Main.showMsg("\n----------------------------------------------------------------------\n");
        Main.showMsg("SM9 sign Test\n");

        String id_A = "Alice";

        //生成签名主密钥对
        MasterKeyPair signMasterKeyPair = kgc.genSignMasterKeyPair();
        Main.showMsg("Master private key for signature 'ks':");
        Main.showMsg(signMasterKeyPair.getPrivateKey().toString());
        Main.showMsg("Master public key for signature 'Ppub-s':");
        Main.showMsg(signMasterKeyPair.getPublicKey().toString());

        //显示ID信息
        Main.showMsg("The identity of A 'IDA':");
        Main.showMsg(id_A);

        //生成签名私钥
        //PrivateKey signPrivateKey = kgc.genSignPrivateKey(signMasterKeyPair.getPrivateKey(), id_A);
        PrivateKey signPrivateKey = kgc.genSignPrivateKey(id_A);
        Main.showMsg("User's private key for signature 'ds_A':");
        Main.showMsg(signPrivateKey.toString());


        //签名
        Main.showMsg("Some values during the signature");
        String msg = "Chinese IBS standard";
        Main.showMsg("Message to be signed 'M':");
        Main.showMsg(msg);

        ResultSignature signature = sm9.sign(signMasterKeyPair.getPublicKey(), signPrivateKey, msg.getBytes());
        Main.showMsg("signature of the message '(h,s)':");
        Main.showMsg(signature.toString());

        //验签
        if(sm9.verify(signMasterKeyPair.getPublicKey(), id_A, msg.getBytes(), signature))
            Main.showMsg("verify success");
        else
            Main.showMsg("verify failed");
    }




}
