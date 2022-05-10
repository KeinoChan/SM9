package com.SM9;

import com.SM9.EllipticCurve.SM9Curve;
import com.SM9.utils.SM9Utils;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;

import java.math.BigInteger;
import java.security.SecureRandom;


public class PKG1 {

    /**
     * curve is different in every class...?
     */

    static SM9Curve curve = new SM9Curve();

    protected final static BigInteger generate_s1(){
        return SM9Utils.genRandom(curve.random, curve.N);  //curve.random maybe the problem?
    }


    //Step1: select a1,set a2; calculate c0&c1, which then will be sent to PKG2
    public BigInteger generate_a1(Paillier paillier, SecureRandom random){  //final maybe?
        return SM9Utils.genRandom(random, paillier.n);//generate a1 for PKG1
    }

    public BigInteger get_a2(BigInteger a1, Paillier paillier){
        return a1.modInverse(paillier.n);
    }

    /**
     * Send c0 and c1 to PKG2
     * @param a1
     * @return
     */
    public BigInteger get_c0(BigInteger a1, Paillier paillier){
        return paillier.Encryption(a1);
    }

    public BigInteger get_c1(String id, BigInteger a1, BigInteger s1, Paillier paillier, SM9Curve curve){
        BigInteger h1 = SM9Utils.H1(id, curve.N);
        return a1.multiply(h1.add(s1)).mod(paillier.n);
    }
    //Step1 end


    //Step3: Calculate c4, check if c3 or c4 is 0, and calculate c, get Q1 further. Q1 will be sent to PKG2
    public BigInteger get_c4(BigInteger c2, Paillier paillier){
        return paillier.Decryption(c2);
    }

    /**
     * PKG 1 checks whether c3 and c4 are 0.
     * @param c3
     * @param c4
     * @return
     */
    public boolean isZero(BigInteger c3, BigInteger c4){
        boolean isZero = false;
        if(c3.signum()==0)
            isZero = true;
        if(c4.signum()==0)
            isZero = true;
        return isZero;
    }

    public BigInteger get_c(BigInteger a2, BigInteger c3, BigInteger c4, Paillier paillier){
        return a2.multiply(c3.add(c4)).mod(paillier.n);
    }

    public CurveElement get_Q1(BigInteger c, String id, Paillier paillier, SM9Curve curve){

        BigInteger h1 = SM9Utils.H1(id, curve.N);
        return curve.P1.duplicate().mul(h1.multiply(c.modInverse(paillier.n)).mod(paillier.n));
    }
    //Step3 end
}