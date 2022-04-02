#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <EEPROM.h>

String ssid = "";
String password = "";
const char* host = "rorschach1996.000webhostapp.com";
bool accessMode = false;

ESP8266WebServer server(80);

void setup() {
  Serial.begin(115200);
  Serial.setDebugOutput(true);
  Serial.println("Water tank Audit");
  delay(5000);
  if (checkConnection()) {
    return;
  }
  setAccessPoint();
}


void loop() {
  server.handleClient();

  if (!accessMode && !isConnected()) {
    setAccessPoint();
  }
  else if (isConnected()) {
    int water_level = analogRead(A0);
    Serial.println("Water level: ");
    Serial.println(water_level);

    WiFiClient client;
    const int httpPort = 80;
    if (!client.connect(host, httpPort)) {
      Serial.println("connection failed");
      return;
    }

    String url = "/water_tank/insert.php?water_level=" + String(water_level);
    Serial.print("Requesting URL: ");
    Serial.println(url);

    client.print(String("GET ") + url + " HTTP/1.1\r\n" +
                 "Host: " + host + "\r\n" +
                 "Connection: close\r\n\r\n");
    delay(100);

    while (client.available()) {
      String line = client.readStringUntil('\r');
      Serial.print(line);
    }

    Serial.println();
    Serial.println("closing connection");
  }
  delay(5000);

}

void connect(String ssid, String password) {
  WiFi.disconnect();
  delay(1000);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.mode(WIFI_STA);
  WiFi.setAutoReconnect(true);
  WiFi.persistent(true);
  WiFi.begin(ssid, password);
  if (checkConnection()) {
    Serial.println();
    Serial.println("WiFi connected");
    Serial.println("----------------------");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());
    Serial.print("Netmask: ");
    Serial.println(WiFi.subnetMask());
    Serial.print("Gateway: ");
    Serial.println(WiFi.gatewayIP());
    Serial.println("----------------------");
  }
}

bool isConnected() {
  return WiFi.status() == WL_CONNECTED;
}

bool checkConnection() {
  int count = 0;
  while (count < 30) {
    if (isConnected()) {
      accessMode = false;
      return true;
    }
    delay(500);
    Serial.print(".");
    count++;
  }
  accessMode = false;
  return false;
}

void setAccessPoint() {
  accessMode = true;
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  delay(100);
  int wifiList = WiFi.scanNetworks();
  if (wifiList == 0) {
    Serial.println("No nearby WiFi networks");
  }
  else {
    for (int i = 0; i < wifiList; i++) {
      Serial.print(i + 1);
      Serial.print(" : ");
      Serial.println(WiFi.SSID(i));
    }
  }
  Serial.println("Sadhae");
  WiFi.softAP("Water_Tank_Device", "Luj9tb2g$1001", 6);
  launchWeb();
}

void launchWeb() {
  server.on("/", []() {
    String webContent = "<!DOCTYPE html> <html> <head> <meta name='viewport' content='width=device-width, initial-scale=1'> <title> Water Tank </title> <style>  Body {    font-family: Calibri;     background-color: white;  }   button {    background-color: #347aeb;    width: 70%;     color: orange;    padding: 15px;    border: none;     cursor: pointer;    color: #FFFFFF;     margin-top: 20px;     margin-left: 15%;    border-radius: 20px;  }   input[type=text], input[type=password] {    width: 100%;    margin: 8px 0;    padding: 12px 20px;     display: inline-block;    border: 2px solid green;    box-sizing: border-box;   }   .container {    width: 60%;     margin: 0 auto;     padding: 25px;    background-color: lightblue;    border-radius: 25px;  } </style> </head> <body>     <center> <h1>Water Tank Device</h1> </center>     <form method = 'get' action = 'wifiConfig'>         <div class='container'>             <label>SSID : </label>             <input type='text' placeholder='Enter SSID' name='SSID' required><br><br>             <label>Password : </label>             <input type='password' placeholder='Enter Password' name='password' required>             <button type='submit'>Login</button>         </div>     </form> </body> </html>  ";
    server.send(200, "text/html", webContent);
  });

  server.on("/wifiConfig", []() {
    String ssid = server.arg("SSID");
    String password = server.arg("password");

    if (ssid.length() > 0 && password.length() > 0) {
      String content = "{\"Success\":\"Updating to new WiFi credentials, restarting the device\"}";
      server.send(404, "application/json", content);
      connect(ssid, password);
    }
    else {
      String content = "{\"Error\":\"404 not found\"}";
      server.send(404, "application/json", content);
    }
  });

  server.begin();
}
