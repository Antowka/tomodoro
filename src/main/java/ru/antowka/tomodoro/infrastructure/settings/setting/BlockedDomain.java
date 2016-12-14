package ru.antowka.tomodoro.infrastructure.settings.setting;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class BlockedDomain {
    private String domain;
    private boolean blocked;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
