package main.SM9;

import main.SM9.EllipticCurve.SM9CurveParameters;
import main.SM9.Utils.SM9Utils;

import java.math.BigInteger;

/**
 * SM9 master private key.
 *
 * A master private key is a big random integer between [1, N-1].
 *
 * Created by Chan on 2022/4/7.
 */

public class MasterPrivateKey {
    public BigInteger ks;
    public MasterPrivateKey(BigInteger d) {
        this.ks = d;
    }


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("sm9 master private key:");
        sb.append(SM9Utils.NEW_LINE);
        sb.append(SM9Utils.toHexString(SM9Utils.BigIntegerToBytes(ks, SM9CurveParameters.nBits/8)));

        return sb.toString();
    }

}
