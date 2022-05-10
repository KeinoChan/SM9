package SM9;

import SM9.utils.SM9Utils;

/**
 * SM9 master key pair.
 *
 * Created by Chan on 2022/4/7.
 */

public class MasterKeyPair {
    MasterPrivateKey prikey;
    MasterPublicKey pubkey;

    public MasterKeyPair(MasterPrivateKey privateKey, MasterPublicKey publicKey) {
        prikey = privateKey;
        pubkey = publicKey;
    }


    public MasterPrivateKey getPrivateKey() {
        return prikey;
    }

    public MasterPublicKey getPublicKey() {
        return pubkey;
    }


    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("SM9 Master key pair:");
        sb.append(SM9Utils.NEW_LINE);
        sb.append(prikey.toString());
        sb.append(SM9Utils.NEW_LINE);
        sb.append(pubkey.toString());

        return sb.toString();
    }
}
