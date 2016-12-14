package ru.antowka.tomodoro.infrastructure.settings.setting;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Settings for TrafficSniffer
 */
@XmlRootElement
public class TrafficSnifferSetting {

    private boolean enable;

    private List<BlockedDomain> blockedDomains = new ArrayList<>();


    public boolean isEnable() {
        return enable;
    }

    @XmlElement
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<BlockedDomain> getBlockedDomains() {
        return blockedDomains;
    }

    @XmlElementWrapper
    @XmlElement(name="blockedDomain")
    public void setBlockedDomains(List<BlockedDomain> blockedDomains) {
        this.blockedDomains = blockedDomains;
    }
}
