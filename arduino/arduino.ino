#include "Adafruit_NeoPixel.h"

#define LED_PIN    6
#define LED_COUNT 100
 
// Declare our NeoPixel strip object:
Adafruit_NeoPixel strip(LED_COUNT, LED_PIN);

void setup() {
  strip.begin();
  strip.show();
  for (int i = 1; i < LED_COUNT; i++) {
    strip.setPixelColor(i, i, i, 100 - i);
  }
  strip.show();
}

void loop() {
}
