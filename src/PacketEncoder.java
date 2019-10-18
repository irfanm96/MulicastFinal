import java.nio.*;
import java.io.*;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PacketEncoder{
	byte[] buffer;
	byte[] seqBytes;
	byte[] userBytes;
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
}
