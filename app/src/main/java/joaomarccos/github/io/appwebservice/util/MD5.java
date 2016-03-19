package joaomarccos.github.io.appwebservice.util;

import android.security.keystore.KeyProperties;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by joaomarcos on 26/02/16.
 */
public class MD5 {
    public static String hash(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(KeyProperties.DIGEST_MD5);
        byte[] digest = md.digest(data.getBytes());
        return new BigInteger(digest).toString(16);
    }
}
