package main.tq215.weatherapp.utils;

public enum Picture {
    LOADING("LoadIcon.png"),
    COLD("Cold.png"),
    WARM("Warm.png"),
    RAIN("Rain.png"),
    CLOUD("Cloud.png"),
    RAINCLOUD("Raincloud.png"),
    SUNCLOUD("Suncloud.png"),
    SUNRAINCLOUD("Sunraincloud.png");

    private final String fileName;

    Picture(String fileName) {
        this.fileName = fileName;
    }
}
