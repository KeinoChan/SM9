package com.SM9;

import com.SM9.utils.SM9Utils;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;

/**
 * SM9 master public pair.
 *
 * A sign master public key is a multiplying point with master private key of the base point of G2 group.
 *
 * Created by Chan on 2022/4/7.
 */

public class MasterPublicKey {
    CurveElement Q;

    public MasterPublicKey (CurveElement point){
        this.Q = point;
    }


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("sm9 master public key:");
        sb.append(SM9Utils.NEW_LINE);

        sb.append(SM9Utils.toHexString(SM9Utils.G2ElementToByte(Q)));

        return sb.toString();
    }

}
