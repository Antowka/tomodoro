package ru.antowka.tomodoro;

import org.pcap4j.core.*;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;

import java.io.EOFException;
import java.util.List;

/**
 * Traffic sniffer
 */
public class TrafficSniffer implements Runnable {

    private static final String COUNT_KEY = TrafficSniffer.class.getName() + ".count";
    private static final int COUNT = Integer.getInteger(COUNT_KEY, -1);
    private static final String READ_TIMEOUT_KEY = TrafficSniffer.class.getName() + ".readTimeout";
    private static final int READ_TIMEOUT = Integer.getInteger(READ_TIMEOUT_KEY, 1000); // [ms]
    private static final String SNAPLEN_KEY = TrafficSniffer.class.getName() + ".snaplen";
    private static final int SNAPLEN = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]
    private List<PcapNetworkInterface> allDevs;
    private PacketListener listener;
    private boolean enable = false;


    TrafficSniffer() {

        //init params
        String myLibraryPath = System.getProperty("user.dir");//or another absolute or relative path
        System.setProperty("jna.library.path", myLibraryPath + "\\libs\\");
        System.setProperty("org.pcap4j.core.pcapLibName", "wpcap");
        System.setProperty("org.pcap4j.core.packetLibName", "Packet");

        try {
            allDevs = Pcaps.findAllDevs();
        } catch (PcapNativeException e) {
            e.printStackTrace();
            return;
        }
    }

    public void enable() {
        this.enable = true;
    }

    public void disable() {
        this.enable = false;
    }

    public void run() {
        try {
            listenTraffic();
        } catch (NotOpenException | PcapNativeException e) {
            e.printStackTrace();
        }
    }

    private void listenTraffic() throws PcapNativeException, NotOpenException {

        for(PcapNetworkInterface dev : allDevs) {

            final PcapHandle handle = dev.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);
            handle.setFilter("udp port 53", BpfProgram.BpfCompileMode.OPTIMIZE);

            //Listener for new packets
            listener = packet -> {

                System.out.println(handle.getTimestamp());
                if(packet.get(UdpPacket.class).getPayload() != null) {
                    String hex = packet.get(UdpPacket.class).toHexString();
                    System.out.println(convertHexToString(hex));
                }

                //stop tread
                if (!enable) {
                    synchronized (this) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("WORKING");
            };

            try {
                handle.loop(COUNT, listener);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String convertHexToString(String hex){

        String r = "";
        return r;
    }
}
