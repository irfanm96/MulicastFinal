import java.nio.*;
import java.io.*;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.*;

public class PacketEncoder{
	byte[] buffer;
	byte[] seqBytes;
	byte[] userBytes;
	private static final String ENCRYPTION_ALGORITHM = "ARCFOUR";
	PacketEncoder(int user, int seq, byte[] buffer){
		userBytes = ByteBuffer.allocate(4).putInt(user).array();
		seqBytes = ByteBuffer.allocate(4).putInt(seq).array();
		ByteArrayOutputStream BOS = new ByteArrayOutputStream();
		try{
			BOS.write(userBytes);
			BOS.write(seqBytes);
			BOS.write(buffer);
		} catch (IOException E) {}
		this.buffer=BOS.toByteArray();
	}
	PacketEncoder(int user, int seq, byte[] buffer, byte[] key){
		try{
			SecretKey secretKey = new SecretKeySpec(key, ENCRYPTION_ALGORITHM);
			Cipher rc4 = Cipher.getInstance(ENCRYPTION_ALGORITHM);
			rc4.init(Cipher.ENCRYPT_MODE, secretKey);
			userBytes = ByteBuffer.allocate(4).putInt(user).array();
			seqBytes = ByteBuffer.allocate(4).putInt(seq).array();
			ByteArrayOutputStream BOS = new ByteArrayOutputStream();
			BOS.write(userBytes);
			BOS.write(seqBytes);
			BOS.write(rc4.doFinal(buffer));
			this.buffer=BOS.toByteArray();
		} catch (Exception E) {
			E.printStackTrace();
		}
	}
}
