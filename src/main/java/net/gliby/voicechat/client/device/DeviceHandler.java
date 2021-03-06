package net.gliby.voicechat.client.device;

import net.gliby.voicechat.client.sound.ClientStreamManager;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import java.util.ArrayList;
import java.util.List;

public class DeviceHandler {

    private final List<Device> devices = new ArrayList<Device>();


    public Device getDefaultDevice() {
        Info info = new Info(TargetDataLine.class, ClientStreamManager.getUniversalAudioFormat());
        if (!AudioSystem.isLineSupported(info)) {
            return null;
        } else {
            TargetDataLine line;
            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
            } catch (Exception var4) {
                return null;
            }

            return line != null ? this.getDeviceByLine(line) : null;
        }
    }

    private Device getDeviceByLine(TargetDataLine line) {
        for (int i = 0; i < this.devices.size(); ++i) {
            Device device = this.devices.get(i);
            if (device.getLine().getLineInfo().equals(line.getLineInfo())) {
                return device;
            }
        }

        return null;
    }

    public Device getDeviceByName(String deviceName) {
        for (int i = 0; i < this.devices.size(); ++i) {
            Device device = this.devices.get(i);
            if (device.getName().equals(deviceName)) {
                return device;
            }
        }

        return null;
    }

    public List<Device> getDevices() {
        return this.devices;
    }

    public boolean isEmpty() {
        return this.devices.isEmpty();
    }

    public List<? extends Device> loadDevices() {
        this.devices.clear();
        javax.sound.sampled.Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        int len$ = mixers.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            javax.sound.sampled.Mixer.Info info = mixers[i$];
            Mixer mixer = AudioSystem.getMixer(info);

            try {
                Info e = new Info(TargetDataLine.class, ClientStreamManager.getUniversalAudioFormat());
                TargetDataLine tdl = (TargetDataLine) mixer.getLine(e);
                if (info != null) {
                    this.devices.add(new Device(tdl, info));
                }
            } catch (LineUnavailableException var9) {
            } catch (IllegalArgumentException var10) {
            }
        }

        return this.devices;
    }
}
