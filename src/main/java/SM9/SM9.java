package SM9;

import SM9.EllipticCurve.SM9Curve;
import SM9.utils.SM9Utils;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;

import java.math.BigInteger;

/**
 * SM9 signature & signature verification algorithm
 *
 * Created by Chan on 2022/4/8.
 */

public class SM9 {
    protected SM9Curve mCurve;

    public SM9(SM9Curve curve){
        mCurve = curve;
    }

    public SM9Curve getCurve() {
        return mCurve;
    }

    /**
     * SM9 sign.
     *
     * @param masterPublicKey signed master public key
     * @param privateKey signed private key
     * @param data source data
     * @return signature value, (h,s).
     */
    public ResultSignature sign(MasterPublicKey masterPublicKey, PrivateKey privateKey, byte[] data)
    {
        BigInteger l, h;

        //Step1 : g = e(P1, Ppub-s)
        Element g = mCurve.pairing(mCurve.P1, masterPublicKey.Q);

        do {
            //Step2: generate r
            BigInteger r = SM9Utils.genRandom(mCurve.random, mCurve.N);

            //Step3 : calculate w=g^r
            Element w = g.duplicate().pow(r);

            //Step4 : calculate h=H2(M||w,N)
            h = SM9Utils.H2(data, w, mCurve.N);

            //Step5 : l=(r-h)mod N
            l = r.subtract(h).mod(mCurve.N);
        } while(l.equals(BigInteger.ZERO));

        //Step6 : S=[l]dSA=(xS,yS)
        CurveElement s = privateKey.d.duplicate().mul(l);

        //Step7 : signature=(h,s)
        return new ResultSignature(h, s);
    }

    /**
     * SM9 verify.
     *
     * @param masterPublicKey signed master public key
     * @param id signed private key
     * @param data source data
     * @param signature SM9 signature value for source data
     * @return true present verify success.
     */
    public boolean verify(MasterPublicKey masterPublicKey, String id, byte[] data, ResultSignature signature)
    {
        // Step1 : check if h in the range [1, N-1]
        if(!SM9Utils.isBetween(signature.h, mCurve.N))
            return false;

        // Step2 : check if S is on G1
        if(!signature.s.isValid())
            return false;

        // Step3 : g = e(P1, Ppub-s)
        Element g = mCurve.pairing(mCurve.P1, masterPublicKey.Q);

        // Step4 : calculate t=g^h
        Element t = g.pow(signature.h);

        // Step5 : calculate h1=H1(IDA||hid,N)
        BigInteger h1 = SM9Utils.H1(id, mCurve.N);

        // Step6 : P=[h1]P2+Ppubs
        CurveElement p = mCurve.P2.duplicate().mul(h1).add(masterPublicKey.Q);

        // Step7 : u=e(S,P)
        Element u = mCurve.pairing(signature.s, p);

        // Step8 : w=u*t
        Element w2 = u.mul(t);

        // Step9 : h2=H2(M||w,N)
        BigInteger h2 = SM9Utils.H2(data, w2, mCurve.N);

        return h2.equals(signature.h);
    }


}
