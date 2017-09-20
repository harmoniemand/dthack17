/*
 *  This sketch sends data via HTTP GET requests to data.sparkfun.com service.
 *
 *  You need to get streamId and privateKey at data.sparkfun.com and paste them
 *  below. Or just customize this script to talk to other HTTP servers.
 *
 */

#include <ESP8266WiFi.h>
#include <WiFiClientSecure.h>
#include "FS.h"


const char* ssid     = "TelekomHackathon";
const char* password = "#dthack17";
String device_id;
const char* host = "dthack17.de";

class accelerator_data
{
  public:
    accelerator_data(float pgpsx, float pgpsy, float pacce);
    ~accelerator_data();

    float GetGpsx();
    float GetGpsy();
    float GetAcce();
    void SetGpsx(float gpsx);
    void SetGpsy(float gpsy);
    void SetAcce(float acce);
  private:
    float gpsx;
    float gpsy;
    float acce;
};

accelerator_data::accelerator_data(float pgpsx, float pgpsy, float pacce)
{
  gpsx = pgpsx;
  gpsy = pgpsy;
  acce = pacce;
}

accelerator_data::~accelerator_data()
{
    gpsx = 0.0;
    gpsy = 0.0;
    acce = 0.0;
}

String readFile(String path) {
  if (SPIFFS.exists(path)) {
    File file = SPIFFS.open(path, "r");
    return file.readString();
  }

  return "default";
}


void setup_send() {
  Serial.begin(115200);
  delay(10);

  device_id= readFile("device.txt");
  // We start by connecting to a WiFi network

  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  
  const int httpsPort = 443;
  WiFiClientSecure client;
  if (!client.connect(host, httpsPort)) {
    Serial.println("connection failed");
    return;
  } else {
    Serial.println("connected to server");
    send(1.0, 2.0, 3.0, client);
    Serial.println("Send ausgeführt");
  }
}

void send(float gpsx, float gpsy, float acce, WiFiClientSecure &client) {

  if(!client.connected())
  {
    Serial.println("is not connected...");
  }
    String postdata = String("{\"latitude\":" + String(gpsx) + ", \"longitude\":" + String(gpsy) + ", \"acceleration\":" + String(acce) + ", \"device\":\"" + device_id+"\"}");
    Serial.println(postdata);
  // Make a HTTP request:
    client.print("POST /api/sensors HTTP/1.1\r\n");
    client.print("Host: dthack17.de\r\n");
    client.print("User-Agent: Arduino/1.0\r\n");
    // client.println("Accept: /");
    client.print("Connection: close\r\n");
    client.print("Content-Length: " + String(postdata.length())+"\r\n");
    client.print("Content-Type: application/json\r\n\r\n");
    // client.println("Content-Type: application/x-www-form-urlencoded");
    //client.println();// important need an empty line here
    // header end
    client.print(postdata);// the payload
    int repeatCounter = 10;
    while (!client.available() && repeatCounter--) {
      delay(500);
    }
    Serial.println("closing connection");
}

