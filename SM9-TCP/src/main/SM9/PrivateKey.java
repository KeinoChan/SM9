package main.SM9;

import main.SM9.EllipticCurve.SM9Curve;
import main.SM9.Utils.SM9Utils;
import it.unisa.dia.gas.plaf.jpbc.field.curve.CurveElement;

import java.io.ByteArrayOutputStream;

/**
 * SM9 private key.
 *
 * A sign private key is a multiplying point with t2 of the base point of G1 group.
 *
 * Created by Chan on 2022/4/8.
 */

public class PrivateKey {
    CurveElement d;

    public PrivateKey(CurveElement point) {
        this.d = point;
    }

    public static PrivateKey fromByteArray(SM9Curve curve, byte[] source) {
        byte hid = source[0];
        CurveElement d;

            d = curve.G1.newElement();

        d.setFromBytes(source, 1);

        return new PrivateKey(d);
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //bos.write(hid);
        byte[] temp = d.toBytes();
        bos.write(temp, 0, temp.length);
        return bos.toByteArray();
    }

    @Override
    public String toString() {
        return "SM9 private key:"+ SM9Utils.NEW_LINE+SM9Utils.toHexString(SM9Utils.G1ElementToBytes(d));
    }

}
