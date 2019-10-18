import java.nio.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class PacketDecoder{
	byte[] buffer;
	int seq;
	int user;
	PacketDecoder(byte[] buffer){
		byte[] seqBytes = new byte[4];
		user = ByteBuffer.wrap(Arrays.copyOf(buffer, 4)).getInt();
		System.arraycopy(buffer, 4, seqBytes, 0, 4);
		seq = ByteBuffer.wrap(seqBytes).getInt();
		System.arraycopy(buffer, 8, this.buffer, 0, buffer.length-8);
	}
}
