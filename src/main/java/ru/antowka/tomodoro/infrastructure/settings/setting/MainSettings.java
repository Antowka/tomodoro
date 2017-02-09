package ru.antowka.tomodoro.infrastructure.settings.setting;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Settings
 */
@XmlRootElement
public class MainSettings {

    private int workTime;

    private int relaxTime;


    public int getWorkTime() {
        return workTime;
    }

    @XmlElement
    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getRelaxTime() {
        return relaxTime;
    }

    @XmlElement
    public void setRelaxTime(int relaxTime) {
        this.relaxTime = relaxTime;
    }
}
