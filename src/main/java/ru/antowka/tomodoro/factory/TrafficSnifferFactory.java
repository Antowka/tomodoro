package ru.antowka.tomodoro.factory;

import ru.antowka.tomodoro.infrastructure.TrafficSniffer;

import java.util.Arrays;

/**
 * Factory for TrafficSniffer
 */
public class TrafficSnifferFactory implements Factory<TrafficSniffer> {

    private static TrafficSnifferFactory trafficSnifferFactory;
    private TrafficSniffer trafficSniffer;

    //// TODO: 02.12.2016 set to settings
    private String[] blockedDomains = new String[]{
       "facebook",
       "vk",
       "instagram",
       "odnoklassniki"
    };

    /**
     * For singleton
     *
     * @return
     */
    public static TrafficSnifferFactory getInstance() {

        if(trafficSnifferFactory == null) {
            trafficSnifferFactory = new TrafficSnifferFactory();

            String myLibraryPath = "";

            if(System.getenv("os").contains("win")) {
                myLibraryPath = System.getenv("SystemRoot" + "\\System\\");
            }

            //init params for j4pcap
            System.setProperty("jna.library.path", myLibraryPath);
            System.setProperty("org.pcap4j.core.pcapLibName", "wpcap");
            System.setProperty("org.pcap4j.core.packetLibName", "Packet");

            trafficSnifferFactory.trafficSniffer = new TrafficSniffer(Arrays.asList(trafficSnifferFactory.blockedDomains));
        }

        return trafficSnifferFactory;
    }


    @Override
    public TrafficSniffer getInstanceProduct() {
        return trafficSniffer;
    }
}
