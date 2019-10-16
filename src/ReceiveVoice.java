import java.io.IOException;
import java.net.*;

public class ReceiveVoice extends Main {

    private final int packetSize = 1000;
    private int port;
    private MulticastSocket socket;
    private InetAddress host;

    private void initSocket() throws IOException {
        this.socket = new MulticastSocket(this.port);

        //avoid hearing my voice again
        this.socket.setLoopbackMode(true);//avoid loop back

        //used for windows environments ,it enables the multicast
        this.socket.setBroadcast(true);
        //join the multicast group
        this.socket.joinGroup(this.host);
    }

    @Override
    public void run() {

        try {

            initSocket();
            // Create a packet
            DatagramPacket packet = new DatagramPacket(new byte[this.packetSize], (this.packetSize));
            //setup audio outputs, from parent class
            this.playAudio();
            for (; ; ) {
//                System.out.print("hi");
                try {

//                    System.out.println("receiving");

                    this.socket.receive(packet);

                    // Print the packet
                    this.getSourceDataLine().write(packet.getData(), 0, this.packetSize); //playing the audio

                    packet.setLength(this.packetSize);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.socket.close();
        }
    }

    public ReceiveVoice(InetAddress host, int port) {
        this.host = host;
        this.port = port;
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