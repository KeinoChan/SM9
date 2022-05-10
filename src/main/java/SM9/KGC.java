package SM9;

import SM9.EllipticCurve.SM9Curve;
import SM9.utils.SM9Utils;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * SM9 Key Generator Center.
 * Generate the Master key pair and Sign private key.
 *
 * Created by Chan on 2022/4/7.
 */

public class KGC {
    public SM9Curve mCurve;
    Paillier paillier = new Paillier();

    public KGC(SM9Curve curve) {
        mCurve = curve;
    }

    public SM9Curve getCurve() {
        return mCurve;
    }

    final BigInteger s1 = PKG1.generate_s1();//generate the uniform s1&s2
    final BigInteger s2 = PKG2.generate_s2();// both for Master private key and sign private key

    /**
     * Generate SM9 signature master key pair.
     * @return
     */
    public MasterKeyPair genSignMasterKeyPair()
    {
        BigInteger ks = (s1.add(s2)).mod(mCurve.N); //ks = (s1+s2) mod n
        CurveElement ppubs = mCurve.P2.duplicate().mul(ks);

        return new MasterKeyPair(new MasterPrivateKey(ks), new MasterPublicKey(ppubs));
    }



   /**
     * PKG
     *Generate SM9 signature private key.
     *
     * t1 = H1(IDA||hid,N) + ks = h1 + ks; (t1≠0)
     *t2 = ks*t^(-1) mod N;
     *dsA = [t2]P1
     *
     * @param //MasterPrivateKey
     * @param //id IDA in String
     * @return user's signature private key: ds.
     * @throws Exception
     */
/*    PrivateKey genSignPrivateKey(MasterPrivateKey MasterPrivateKey, String id) throws Exception {
        BigInteger h1 = SM9Utils.H1(id, mCurve.N);
        BigInteger t1 = h1.add(MasterPrivateKey.ks).mod(mCurve.N);
        if(t1.equals(BigInteger.ZERO))
            throw new Exception("Need to update the master private key");
        BigInteger t2 = MasterPrivateKey.ks.multiply(t1.modInverse(mCurve.N)).mod(mCurve.N);
        CurveElement ds = mCurve.P1.duplicate().mul(t2);
        return new PrivateKey(ds);
    }*/
    PrivateKey genSignPrivateKey(String id) throws Exception {
        //Test
        BigInteger ks = s1.add(s2).mod(paillier.n);
        BigInteger h1 = SM9Utils.H1(id, mCurve.N);
        BigInteger t1 = h1.add(ks);
        if(t1.equals(BigInteger.ZERO))
            throw new Exception("Need to update the master private key");
        BigInteger t2 = ks.multiply(t1.modInverse(mCurve.N)).mod(mCurve.N); //change the two mod from mCurve.N to paillier.n (doesn't work)
        CurveElement ds = mCurve.P1.duplicate().mul(t2);


//Step1 start
        BigInteger a1 = SM9Utils.genRandom(mCurve.random,mCurve.N);
        BigInteger a2 = a1.modInverse(mCurve.N);

        BigInteger c0 = paillier.Encryption(a1);
        System.out.println("c0's value: "+c0);

        //BigInteger h1 = SM9Utils.H1(id, mCurve.N);
        BigInteger c1 = a1.multiply(h1.add(s1)).mod(mCurve.N);
        //Step1 end

        //Step2 start
        BigInteger b1 = SM9Utils.genRandom(mCurve.random,mCurve.N);
        BigInteger w2 = SM9Utils.genRandom(mCurve.random,mCurve.N);
        BigInteger b2 = mCurve.N.subtract(b1);

        //doesn't work when z1 is negative
        BigInteger z1 = SM9Utils.genRandom(mCurve.random,mCurve.N);
        BigInteger z2 = SM9Utils.genRandom(mCurve.random,mCurve.N);
        //BigInteger z1 = BigInteger.ZERO;
        //BigInteger z2 = BigInteger.ZERO;

        BigInteger c2_0 = w2.multiply(s2).mod(mCurve.N).add(z1.multiply(mCurve.N));
        BigInteger c2_1 = paillier.cipher_multi(c0,c2_0);
        BigInteger c2_2 = paillier.Encryption(z2.multiply(mCurve.N).add(b1));
        BigInteger c2 = paillier.cipher_add(c2_2,c2_1);

        BigInteger c3 = w2.multiply(c1).add(b2).mod(mCurve.N);
        //Step2 end

        //Step3 start
        BigInteger c4 = paillier.Decryption(c2);

        System.out.println("c4 really is:"+c4.toString());
        System.out.println("c4 should be:"+a1.multiply(w2.multiply(s2).mod(mCurve.N).add(z1.multiply(mCurve.N))).add(z2.multiply(mCurve.N)).add(b1));

        BigInteger c = a2.multiply(c3.add(c4)).mod(mCurve.N);

        CurveElement Q1 = mCurve.P1.duplicate().mul(h1.multiply(c.modInverse(mCurve.N)).mod(mCurve.N)); //c^(-1)
        System.out.println("Q1: "+Q1);
        System.out.println("Q1 in String: "+Q1);
        System.out.println("Q1 to Bytes: "+Q1.toBytes());
        System.out.println("Q1 to (String)bytesArrays: "+Arrays.toString(SM9Utils.G1ElementToBytes(Q1)));
        //Step3 end

        //Step4 start
        /**
         * Test generate Q2 via Q3 which is generated from Q1 byte
         */

        CurveElement Q2 = (CurveElement) mCurve.P1.duplicate().sub(Q1.mul(w2));

        Main.showMsg("BitLength of N:");
        System.out.println(mCurve.N.bitLength());
        Main.showMsg("BitLength of n:");
        System.out.println(paillier.n.bitLength());

        Main.showMsg("Q2:");
        Main.showMsg(Q2.toString());

        Main.showMsg("ds:");
        Main.showMsg(ds.toString());

        return new PrivateKey(Q2);
    }

}
