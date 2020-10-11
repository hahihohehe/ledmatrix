#include "Adafruit_NeoPixel.h"

#define LED_PIN    6
#define LED_COUNT 300
 
// Declare our NeoPixel strip object:
Adafruit_NeoPixel strip(LED_COUNT, LED_PIN);

void setup() {
  strip.begin();
  strip.show();
}

void loop() {
  for (int i = 0; i < LED_COUNT; i++) {
    strip.clear();
    strip.setPixelColor(i, 255, 0, 0);
    strip.show();
  }
  for (int i = 0; i < LED_COUNT; i++) {
    strip.clear();
    strip.setPixelColor(i, 0, 255, 0);
    strip.show();
  }
  for (int i = 0; i < LED_COUNT; i++) {
    strip.clear();
    strip.setPixelColor(i, 0, 0, 255);
    strip.show();
  }
}
