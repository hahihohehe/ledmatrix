#include "Adafruit_NeoPixel.h"

#define LED_PIN    6
#define LED_COUNT 100
 
// Declare our NeoPixel strip object:
Adafruit_NeoPixel strip(LED_COUNT, LED_PIN);
byte data[3 * LED_COUNT];
int index = 0;

void setup() {
  strip.begin();
  strip.show();
  for (int i = 0; i < LED_COUNT; i++) {
    strip.setPixelColor(i, i, i, 100 - i);
  }
  strip.show();
  Serial.begin(9600);
}

void loop() {
  if (Serial.available() > 0) {
    data[index++] = Serial.read();
    if (index == 300) {
      strip.clear();
      for(int i = 0; i < LED_COUNT; i++) {
        strip.setPixelColor(i, data[3*i], data[3*i+1], data[3*i+2]);
      }
      strip.show();
      index = 0;
    }
  }
}
