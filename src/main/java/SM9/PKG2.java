package SM9;

import SM9.EllipticCurve.SM9Curve;
import SM9.utils.SM9Utils;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;

import java.math.BigInteger;
import java.security.SecureRandom;

public class PKG2 {

    static SM9Curve curve = new SM9Curve();

    public static final BigInteger generate_s2(){
        return SM9Utils.genRandom(curve.random, curve.N);
    } //generate s2


    //Step2: select b1&w1, set b2, calculate c2&c3(select z1 and z2 during calculation) ,which then will be sent to PKG1
    public BigInteger generate_b1(Paillier paillier, SecureRandom random){
        return SM9Utils.genRandom(random, paillier.n);//generate b1 for PKG2
    }

    public BigInteger generate_w2(Paillier paillier, SecureRandom random){
        return SM9Utils.genRandom(random, paillier.n);
    }
    public BigInteger get_b2(BigInteger b1, Paillier paillier){
        return paillier.n.subtract(b1); //b2 = n-b1
    }

    public BigInteger generate_z1(Paillier paillier, SecureRandom random){
        return SM9Utils.genRandom(random, paillier.n); //An integer only known by PKG2
    }
    public BigInteger generate_z2(Paillier paillier, SecureRandom random){
        return SM9Utils.genRandom(random, paillier.n); //An integer only known by PKG2
    }

    public BigInteger get_c2(BigInteger w2, BigInteger s2, BigInteger c0, BigInteger b1, BigInteger z1, BigInteger z2, Paillier paillier){
        //calculate c2
        BigInteger c2_0 = w2.multiply(s2).mod(paillier.n).add(z1.multiply(paillier.n));
        BigInteger c2_1 = paillier.cipher_multi(c0,c2_0);
        BigInteger c2_2 = paillier.Encryption(z2.multiply(paillier.n).add(b1));
        BigInteger c2 = paillier.cipher_add(c2_1,c2_2);
        return c2;
    }

    public BigInteger get_c3(BigInteger w2, BigInteger c1, BigInteger b2, Paillier paillier){
        return w2.multiply(c1).add(b2).mod(paillier.n);
    }
    //Step2 end

    /**
     * Q2 = P1-[w2]Q1
     * @param Q1
     * @param w2
     * @param curve
     * @return
     */
    //Step4: calculate Q2, which is exactly the id_A of the user
    public CurveElement get_Q2(CurveElement Q1, BigInteger w2, SM9Curve curve){
        return curve.P1.duplicate().add(Q1.duplicate().mul(w2).invert());
    }
    //Step4 end
}
