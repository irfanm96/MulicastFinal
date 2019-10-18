import java.nio.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class PacketDecoder{
	byte[] buffer;
	int seq;
	int user;
	PacketDecoder(byte[] buffer){
		try {
		byte[] seqBytes = new byte[4];
		user = System.wrap().getInt(copyOf(buffer, 4));
		System.arraycopy(buffer, 4, seqBytes, 0, 4);
		seq = System.wrap().getInt(seqBytes);
		this.buffer=System.arraycopy(buffer, 8, this.buffer, 0, buffer.length-8);
		} catch (IOException E) {}
	}
}
