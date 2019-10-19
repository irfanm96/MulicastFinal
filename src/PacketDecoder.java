import java.nio.*;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.*;

public class PacketDecoder {
    byte[] buffer;
    byte[] buffer_c;
    int seq;
    int user;
    private static final String ENCRYPTION_ALGORITHM = "ARCFOUR";

    PacketDecoder(byte[] buffer) {
        byte[] seqBytes = new byte[4];
        this.buffer = new byte[1000];
        user = ByteBuffer.wrap(Arrays.copyOf(buffer, 4)).getInt();
        System.arraycopy(buffer, 4, seqBytes, 0, 4);
        seq = ByteBuffer.wrap(seqBytes).getInt();
        System.arraycopy(buffer, 8, this.buffer, 0, buffer.length - 8);
    }

    PacketDecoder(byte[] buffer, byte[] key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
            Cipher rc4 = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            rc4.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] seqBytes = new byte[4];
            this.buffer_c = new byte[1000];
            this.buffer=new byte[1000];
            user = ByteBuffer.wrap(Arrays.copyOf(buffer, 4)).getInt();
            System.arraycopy(buffer, 4, seqBytes, 0, 4);
            seq = ByteBuffer.wrap(seqBytes).getInt();
            System.out.println("sequence number " + seq);
            System.arraycopy(buffer, 8, this.buffer_c, 0, buffer.length - 8);
            this.buffer = rc4.doFinal(buffer_c);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
