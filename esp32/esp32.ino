#include <WiFi.h>
#include <WiFiClient.h>
#include <WebServer.h>

#include <Adafruit_NeoPixel.h>
 
#define PIN 15
#define LED_COUNT 100

#include "wifi_credentials.h"

const char* ssid = _SSID;
const char* password = _PASSWORD;

WebServer server(80);

Adafruit_NeoPixel strip = Adafruit_NeoPixel(LED_COUNT, PIN, NEO_GRB + NEO_KHZ800);

//no need authentification
void handleNotFound() {
  String message = "File Not Found\n\n";
  message += "URI: ";
  message += server.uri();
  message += "\nMethod: ";
  message += (server.method() == HTTP_GET) ? "GET" : "POST";
  message += "\nArguments: ";
  message += server.args();
  message += "\n";
  for (uint8_t i = 0; i < server.args(); i++) {
    message += " " + server.argName(i) + ": " + server.arg(i) + "\n";
  }
  server.send(404, "text/plain", message);
}

void handleDisplay() {
  Serial.println("Arguments: " + server.arg(0));
  String args = server.arg(0);
  args.replace(" ", "");
  Serial.println("Cleaned: " + args);
  args.replace("[", "");
  args.replace("]", "");
  Serial.println("Pure data: " + args);

  uint8_t data[3 * LED_COUNT] = {0};
  int r=0, t=0;
  for (int i=0; i < args.length(); i++) { 
    if(args.charAt(i) == ',') { 
      if (t < 3*LED_COUNT)
        data[t] = args.substring(r, i).toInt(); 
      r=(i+1); 
      t++; 
    }
  }
  if (t < 3*LED_COUNT)
    data[t] = args.substring(r).toInt();

  for (int i = 0; i < 300; i++) {
    Serial.printf("%i,", data[i]);
  }
  Serial.println();

  // Display data
  strip.clear();
    for(int i = 0; i < LED_COUNT; i++) {
      strip.setPixelColor(i, data[3*i], data[3*i+1], data[3*i+2]);
    }
  strip.show();
  
  server.send(200, "text/plain", "OK");
}

void setup(void) {
  strip.begin();
  strip.show();
  for (int i = 0; i < LED_COUNT; i++) {
    strip.setPixelColor(i, 10, 10, 10);
  }
  strip.show();

  
  Serial.begin(115200);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.println("");

  // Wait for connection
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  server.on("/display", HTTP_POST, handleDisplay);

  server.onNotFound(handleNotFound);
  //here the list of headers to be recorded
  const char * headerkeys[] = {"User-Agent", "Cookie"} ;
  size_t headerkeyssize = sizeof(headerkeys) / sizeof(char*);
  //ask server to track these headers
  server.collectHeaders(headerkeys, headerkeyssize);
  server.begin();
  Serial.println("HTTP server started");
}

void loop(void) {
  server.handleClient();
}
