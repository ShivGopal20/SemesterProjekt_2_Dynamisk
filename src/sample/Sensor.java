package sample;

import jssc.SerialPort;
import jssc.SerialPortException;

public class Sensor extends Thread {
    private static final Sensor globalSensor = new Sensor();
    String input;
    //SerialPort connection oprettes og portnavn skal tilpasses ift den anvendte port i arduino programmet.
    SerialPort serialPort = new SerialPort("/dev/cu.usbmodem141101");//ToDo: Change port path

    private Sensor() {   // Her tilkaldes JSSC SerialPort opsætningen via Port() metoden.

        Port();
    }

    public static Sensor getGlobalSensor() {    // En global Oprettes

        return globalSensor;
    }
    //Metode til at danne forbindelse med serialporten.
    public void Port() {
        try {
            //Standard SerialPort opsætning med baud rate på 57600.
            serialPort.openPort();  // Porten åbnes.
            serialPort.setParams(57600, 8, 1, 0);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            serialPort.setDTR(true);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    //Metoden tjekker om der kommer data, desuden læses inputtet som en streng.
    public String sensorData() {
        input = null;
        try {
            if (serialPort.getInputBufferBytesCount() > 0) {
                input = serialPort.readString();
            } else {
                Thread.sleep(5);
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return input;
    }

    //Metoden Lukker porten, men er ikke brugt idet at den også gav problemer.
    public void PortCloser() {
        try {
            serialPort.closePort();

        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

}
