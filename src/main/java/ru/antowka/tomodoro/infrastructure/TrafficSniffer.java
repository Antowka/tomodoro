package ru.antowka.tomodoro.infrastructure;

import javafx.scene.control.Alert;
import org.pcap4j.core.*;
import org.pcap4j.packet.UdpPacket;
import ru.antowka.tomodoro.factory.ResourcesFactory;
import ru.antowka.tomodoro.infrastructure.settings.SettingManager;
import ru.antowka.tomodoro.infrastructure.settings.impl.TrafficSnifferSettingManager;
import ru.antowka.tomodoro.infrastructure.settings.setting.BlockedDomain;
import ru.antowka.tomodoro.infrastructure.settings.setting.TrafficSnifferSetting;

import java.io.IOException;
import java.net.*;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Traffic sniffer
 */
public class TrafficSniffer extends Thread {

    private static final String COUNT_KEY = TrafficSniffer.class.getName() + ".count";
    private static final int COUNT = Integer.getInteger(COUNT_KEY, -1);
    private static final String READ_TIMEOUT_KEY = TrafficSniffer.class.getName() + ".readTimeout";
    private static final int READ_TIMEOUT = Integer.getInteger(READ_TIMEOUT_KEY, 1000); // [ms]
    private static final String SNAPLEN_KEY = TrafficSniffer.class.getName() + ".snaplen";
    private static final int SNAPLEN = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]
    private PcapNetworkInterface dev;
    private InetAddress ipAddress;
    private boolean enable = false;
    private Alert alert;
    private Resources resources;
    private SettingManager<TrafficSnifferSetting> settingManager;

    public TrafficSniffer(SettingManager<TrafficSnifferSetting> settingManager) {

        this.settingManager = settingManager;

        resources = ResourcesFactory
                .getInstance()
                .getInstanceProduct();

        alert = new Alert(Alert.AlertType.WARNING);
    }

    @Override
    public void run() {
        enable();
    }

    public void enable() {

        TrafficSnifferSetting settings = settingManager.loadSettings();

        if (!settings.isEnable()) {
            disable();
            return;
        }

        try {
            InetAddress ip = InetAddress.getByName(settings.getInterfaceName());
            dev = Pcaps.getDevByAddress(ip);
            
            if(dev == null) {
                disable();
                return;
            }

            listenDevice(dev);

        } catch (NotOpenException | PcapNativeException | UnknownHostException e) {
            e.printStackTrace();
        }

        this.enable = true;
    }

    public void disable() {
        this.enable = false;
    }

    private void listenDevice(PcapNetworkInterface dev) throws PcapNativeException, NotOpenException {

        System.out.println(dev.getName());

        final PcapHandle handle = dev.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);

        handle.setFilter("udp port 53", BpfProgram.BpfCompileMode.NONOPTIMIZE);

        //Listener for new packets
        PacketListener listener = packet -> {

            if (packet.get(UdpPacket.class).getPayload() != null) {

                //packet decode to string
                String packetString = byteDecoder(packet.get(UdpPacket.class).getRawData());

                //traffic validator
                trafficValidator(packetString);
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

    private String byteDecoder(byte[] rawBytes){
        return new String(rawBytes, 0, rawBytes.length);
    }

    private void trafficValidator(String packetString) {

        TrafficSnifferSetting settings = settingManager.loadSettings();

//      if(packetString.contains(blockedDomain) && !alert.isShowing()) {
//          resources.playSirena();
//          Platform.runLater(() -> showAlert(blockedDomain));
//      }

        settings.getBlockedDomains()
                .stream()
                .filter(blockedDomain -> blockedDomain.isBlocked() && packetString.contains(blockedDomain.getDomain()))
                .forEach(blockedDomain -> {
                    System.out.println("Block resource:" + blockedDomain);
                    resources.playSirena();
                });
    }

    private void showAlert(String blockedDomainName) {
        alert.setTitle("Traffic Control");
        alert.setHeaderText("You are trying open web-site: " + blockedDomainName);
        alert.setContentText("Please close tab with blocked web site: " + blockedDomainName);
        alert.show();
    }
}
