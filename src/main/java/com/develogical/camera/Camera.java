package com.develogical.camera;

public class Camera {
    private final Sensor sensor;
    private final MemoryCard memoryCard;
    private  boolean isOn;
    private boolean isWritingData;

    public Camera(Sensor sensor, MemoryCard memoryCard) {
        this.sensor = sensor;
        this.memoryCard = memoryCard;
    }

    public void pressShutter() {
        if (!isOn) {
            return;
        }
        isWritingData = true;
        memoryCard.write(sensor.readData(), () -> {
            sensor.powerDown();
            isWritingData = false;
        });
    }

    public void powerOn() {
        sensor.powerUp();
        isOn = true;
    }

    public void powerOff() {
        isOn = false;
        if (isWritingData) {
            return;
        }
        sensor.powerDown();
    }
}

