package SM9;

import SM9.EllipticCurve.SM9Curve;

public class Main {

    public static void main(String[] args) throws Exception {
        SM9Curve sm9Curve = new SM9Curve();
        SM9 sm9 = new SM9(sm9Curve);
        SM9Test.test_sm9_sign(new KGC(sm9Curve), sm9); //for SM9

    }

    public static void showMsg(String msg) {
        System.out.println(msg);
    }
}
