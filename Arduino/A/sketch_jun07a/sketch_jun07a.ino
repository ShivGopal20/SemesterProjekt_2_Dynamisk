#include <TimerOne.h>

#include <SPI.h>


const long tSampleInMicros = 50000; 
//1000000;  
// Sample time in microseconds

SPISettings settings(8000000, MSBFIRST, SPI_MODE0);

void setup() {

Timer1.initialize(tSampleInMicros);          // initialize timer1, and set the period
  Timer1.attachInterrupt(measureAndSend);    // attaches callback() as a timer overflow interrupt
 
    Serial.begin(9600);      //baud rate ok?               
    SPI.begin();
    SPI.beginTransaction(settings);
    pinMode(10, OUTPUT);
    digitalWrite(10, HIGH);
}

void loop() {}                            // Let the Loop method be empty


int getECGADC(){
    digitalWrite(10, LOW);
    int Tal = SPI.transfer16(0x00);
    digitalWrite(10, HIGH);

return Tal;
  }

 void measureAndSend(){
   int b = getECGADC();
   Serial.println(b);
  }
