package ru.antowka.tomodoro;

import org.pcap4j.core.*;
import java.util.List;

/**
 * Traffic sniffer
 */
public class TrafficSniffer implements Runnable {

    private static final String COUNT_KEY = TrafficSniffer.class.getName() + ".count";
    private static final int COUNT = Integer.getInteger(COUNT_KEY, 5);
    private static final String READ_TIMEOUT_KEY = TrafficSniffer.class.getName() + ".readTimeout";
    private static final int READ_TIMEOUT = Integer.getInteger(READ_TIMEOUT_KEY, 10); // [ms]
    private static final String SNAPLEN_KEY = TrafficSniffer.class.getName() + ".snaplen";
    private static final int SNAPLEN = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]
    private List<PcapNetworkInterface> allDevs;

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
            handle.setFilter("tcp port https", BpfProgram.BpfCompileMode.OPTIMIZE);

            //Listener for new packets
            PacketListener listener = packet -> {
                System.out.println(handle.getTimestamp());
                System.out.println(packet);
            };

            try {
                handle.loop(COUNT, listener);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
