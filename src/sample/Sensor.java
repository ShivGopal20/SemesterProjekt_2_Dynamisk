package sample;

import jssc.SerialPort;
import jssc.SerialPortException;

public class Sensor extends Thread {
    private static final Sensor globalSensor = new Sensor();
    String input;
    // SerialPort connection objekts oprettes og Portnavn skal skiftes som findes under "Tools" i arduino programmen.
    SerialPort serialPort = new SerialPort("/dev/cu.usbmodem141101");//ToDo: Change port path

    private Sensor() {   // Her tilkaldes JSSC- SerialPort Opsætning
        //PortOpener();
        Port();
    }

    public static Sensor getGlobalSensor() {    // En global Oprettes

        return globalSensor;
    }


    // https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples#:~:text=Singleton%20pattern%20restricts%20the%20instantiation,objects%2C%20caching%20and%20thread%20pool.

    public void Port() {
        try {
            //Standard SeriaPort opsætning med buad rate på 57600

            serialPort.openPort();  // åbner porten
            serialPort.setParams(57600, 8, 1, 0);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            serialPort.setDTR(true);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    //Metoden tjekker om der kommer overhoved data og om data er positiv. Desuden læser den som string
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

    public void PortCloser() {   // Metoden Lukker porten
        try {
            serialPort.closePort();

        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

}
