package main.tq215.weatherapp.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public enum Picture {
    // an enum of Pictures found stored at ./Resources/Imgs/
    LOADING("Load.png"),
    COLD("Cold.png"),
    WARM("Warm.png"),
    RAIN("Rain.png"),
    CLOUD("Cloud.png"),
    SUNCLOUD("Suncloud.png"),
    SUNRAINCLOUD("Sunraincloud.png"),

    LOADINGSMALL("LoadSmall.png"),
    COLDSMALL("ColdSmall.png"),
    WARMSMALL("WarmSmall.png"),
    RAINSMALL("RainSmall.png"),
    CLOUDSMALL("CloudSmall.png"),
    SUNCLOUDSMALL("SuncloudSmall.png"),
    SUNRAINCLOUDSMALL("SunraincloudSmall.png");

    private BufferedImage icon;
    private final static String imgFolder = "./Resources/Imgs/";

    Picture(String fileName) {
        try {
            this.icon = ImageIO.read(new File(imgFolder + fileName));
        } catch (IOException e) {
            System.out.println("Unable to load image at " + imgFolder + fileName);
            this.icon = null;
        }
    }

    public BufferedImage getIcon() {
        return icon;
    }
}
