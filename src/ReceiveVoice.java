import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.*;
import java.nio.charset.*;

public class ReceiveVoice extends Voice {

    private final int packetSize = 1000;
    private int port;
    private MulticastSocket socket;
    private InetAddress host;
    private int seq[] = new int[16];
    private int user;
    private String key="";

    public ReceiveVoice(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    private void initSocket() {

        try {
            this.socket = new MulticastSocket(this.port);

            //avoid hearing my voice again
            this.socket.setLoopbackMode(true);//avoid loop back

            //used for windows environments ,it enables the multicast
            this.socket.setBroadcast(true);
            //join the multicast group
            this.socket.joinGroup(this.host);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void run() {
	byte[] keyBytes = key.getBytes(Charset.forName("UTF-8"));
        initSocket();
        // Create a packet
        DatagramPacket packet = new DatagramPacket(new byte[this.packetSize], (this.packetSize));
        //setup audio outputs, from parent class
        try {
            this.setupOutput();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        for (; ; ) {
            try {
                this.socket.receive(packet);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
	    PacketDecoder PD = new PacketDecoder(packet.getData(), keyBytes);
	    if(seq[PD.user] == 0 && PD.seq < 768) seq[PD.user] = PD.seq;
	    if (PD.user <= 16){
		if (PD.seq - seq[PD.user] <= 20){
            	// Play the audio
            	this.getSourceDataLine().write(PD.buffer, 0, this.packetSize);
	    }
	    else {System.out.println("Discarding out of sequence packet: "+PD.seq+"th");}
	    }            
	    packet.setLength(this.packetSize);
        }

    }

    public static void main(String[] args) {

        // Check the whether the arguments are given
        if (args.length != 1) {
            System.out.println("Multicast ip Required");
            return;
        }

        final int port = 3575;

        SendVoice sendVoice;
        ReceiveVoice receiveVoice;

        try {

            //create the thread for sending packets
            sendVoice = new SendVoice(InetAddress.getByName(args[0]), port);
            sendVoice.start(); // start the thread

            //create thread for receiving packets
            receiveVoice = new ReceiveVoice(InetAddress.getByName(args[0]), port);
            receiveVoice.start();//start receiving packets

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
