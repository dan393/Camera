package com.develogical.camera;

import com.sun.org.apache.xpath.internal.Arg;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class CameraTest {

    private final MemoryCard memoryCard = Mockito.mock(MemoryCard.class);
    private final Sensor sensor = Mockito.mock(Sensor.class);
    private Camera camera = new Camera(sensor, memoryCard);

    @Test
    public void switchingTheCameraOnPowersUpTheSensor() {
        // write your test here
        camera.powerOn();
        verify(sensor).powerUp();
    }

    @Test
    public void switchingTheCameraOffPowersDownTheSensor() {
        camera.powerOff();
        verify(sensor).powerDown();
    }

    @Test
    public void pressShutterPowerOnCopiesDataToMemoryCard() {
        camera.powerOn();
        camera.pressShutter();
        verify(sensor).readData();
        verify(memoryCard).write(any(), any());
    }

    @Test
    public void pressShutterPowerOffDoesNothing() {
        camera.powerOff();
        camera.pressShutter();
        ArgumentCaptor<WriteCompleteListener>  argumentCaptor = ArgumentCaptor.forClass(WriteCompleteListener.class);
        verify(sensor, never()).readData();
        verify(memoryCard, never()).write(any(), argumentCaptor.capture());
    }

    @Test
    public void dataBeingWrittenDoesntPowerOffSensor() {
        camera.powerOn();
        camera.pressShutter();
        ArgumentCaptor<WriteCompleteListener>  argumentCaptor = ArgumentCaptor.forClass(WriteCompleteListener.class);
        verify(sensor).readData();
        verify(memoryCard).write(any(), argumentCaptor.capture());

        camera.powerOff();

        verify(sensor, never()).powerDown();

        WriteCompleteListener writeCompleteListener = argumentCaptor.getValue();
        writeCompleteListener.writeComplete();
        verify(sensor).powerDown();
    }

}
