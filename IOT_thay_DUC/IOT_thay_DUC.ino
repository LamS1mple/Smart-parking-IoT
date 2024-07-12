#include <ESP32Servo.h>
#include <PubSubClient.h>
#ifdef ESP8266
#include <ESP8266WiFi.h>
#else
#include <WiFi.h>
#endif

#include <ArduinoJson.h>
#include <PubSubClient.h>
#include <WiFiClientSecure.h>
#include <SPI.h>
#include <MFRC522.h>


const int RST_PIN = 22;  // Reset pin
const int SS_PIN = 21;   // Slave select pin

MFRC522 mfrc522(SS_PIN, RST_PIN);  // Create MFRC522 instance
const char* ssid = "iPhone7++";
const char* password = "15122000";



/******* MQTT Broker Connection Details *******/
const char* mqtt_server = "7d293d0a813a4e7cb21f434d78c3e8a9.s1.eu.hivemq.cloud";
const char* mqtt_username = "laam2002";
const char* mqtt_password = "Laam2002";
const int mqtt_port = 8883;

WiFiClientSecure espClient;

/**** MQTT Client Initialisation Using WiFi Connection *****/
PubSubClient client(espClient);

unsigned long lastMsg = 0;
#define MSG_BUFFER_SIZE (50)
char msg[MSG_BUFFER_SIZE];
/****** root certificate *********/

static const char* root_ca PROGMEM = R"EOF(
-----BEGIN CERTIFICATE-----
MIIFazCCA1OgAwIBAgIRAIIQz7DSQONZRGPgu2OCiwAwDQYJKoZIhvcNAQELBQAw
TzELMAkGA1UEBhMCVVMxKTAnBgNVBAoTIEludGVybmV0IFNlY3VyaXR5IFJlc2Vh
cmNoIEdyb3VwMRUwEwYDVQQDEwxJU1JHIFJvb3QgWDEwHhcNMTUwNjA0MTEwNDM4
WhcNMzUwNjA0MTEwNDM4WjBPMQswCQYDVQQGEwJVUzEpMCcGA1UEChMgSW50ZXJu
ZXQgU2VjdXJpdHkgUmVzZWFyY2ggR3JvdXAxFTATBgNVBAMTDElTUkcgUm9vdCBY
MTCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBAK3oJHP0FDfzm54rVygc
h77ct984kIxuPOZXoHj3dcKi/vVqbvYATyjb3miGbESTtrFj/RQSa78f0uoxmyF+
0TM8ukj13Xnfs7j/EvEhmkvBioZxaUpmZmyPfjxwv60pIgbz5MDmgK7iS4+3mX6U
A5/TR5d8mUgjU+g4rk8Kb4Mu0UlXjIB0ttov0DiNewNwIRt18jA8+o+u3dpjq+sW
T8KOEUt+zwvo/7V3LvSye0rgTBIlDHCNAymg4VMk7BPZ7hm/ELNKjD+Jo2FR3qyH
B5T0Y3HsLuJvW5iB4YlcNHlsdu87kGJ55tukmi8mxdAQ4Q7e2RCOFvu396j3x+UC
B5iPNgiV5+I3lg02dZ77DnKxHZu8A/lJBdiB3QW0KtZB6awBdpUKD9jf1b0SHzUv
KBds0pjBqAlkd25HN7rOrFleaJ1/ctaJxQZBKT5ZPt0m9STJEadao0xAH0ahmbWn
OlFuhjuefXKnEgV4We0+UXgVCwOPjdAvBbI+e0ocS3MFEvzG6uBQE3xDk3SzynTn
jh8BCNAw1FtxNrQHusEwMFxIt4I7mKZ9YIqioymCzLq9gwQbooMDQaHWBfEbwrbw
qHyGO0aoSCqI3Haadr8faqU9GY/rOPNk3sgrDQoo//fb4hVC1CLQJ13hef4Y53CI
rU7m2Ys6xt0nUW7/vGT1M0NPAgMBAAGjQjBAMA4GA1UdDwEB/wQEAwIBBjAPBgNV
HRMBAf8EBTADAQH/MB0GA1UdDgQWBBR5tFnme7bl5AFzgAiIyBpY9umbbjANBgkq
hkiG9w0BAQsFAAOCAgEAVR9YqbyyqFDQDLHYGmkgJykIrGF1XIpu+ILlaS/V9lZL
ubhzEFnTIZd+50xx+7LSYK05qAvqFyFWhfFQDlnrzuBZ6brJFe+GnY+EgPbk6ZGQ
3BebYhtF8GaV0nxvwuo77x/Py9auJ/GpsMiu/X1+mvoiBOv/2X/qkSsisRcOj/KK
NFtY2PwByVS5uCbMiogziUwthDyC3+6WVwW6LLv3xLfHTjuCvjHIInNzktHCgKQ5
ORAzI4JMPJ+GslWYHb4phowim57iaztXOoJwTdwJx4nLCgdNbOhdjsnvzqvHu7Ur
TkXWStAmzOVyyghqpZXjFaH3pO3JLF+l+/+sKAIuvtd7u+Nxe5AW0wdeRlN8NwdC
jNPElpzVmbUq4JUagEiuTDkHzsxHpFKVK7q4+63SM1N95R1NbdWhscdCb+ZAJzVc
oyi3B43njTOQ5yOf+1CceWxG1bQVs5ZufpsMljq4Ui0/1lvh+wjChP4kqKOJ2qxq
4RgqsahDYVvTH9w7jXbyLeiNdd8XM2w9U/t7y0Ff/9yi0GE44Za4rF2LN9d11TPA
mRGunUHBcnWEvgJBQl9nJEiU0Zsnvgc/ubhPgXRR4Xq37Z0j4r7g1SgEEzwxA57d
emyPxgcYxn/eR44/KJ4EBs+lVDR3veyJm+kXQ99b21/+jh5Xos1AnX5iItreGCc=
-----END CERTIFICATE-----
)EOF";

const int servoPin = 13;

int giaTriHongNgoai1;
int giaTriHongNgoai2;
int mot = 0;
int hai = 0;
int ba = 0;
int bon = 0;
Servo servo;


void setup_wifi() {
  delay(10);
  Serial.print("\nConnecting to ");
  Serial.println(ssid);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  randomSeed(micros());
  Serial.println("\nWiFi connected\nIP address: ");
  Serial.println(WiFi.localIP());
}
void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    String clientId = "ESP8266Client-";  // Create a random client ID
    clientId += String(random(0xffff), HEX);
    // Attempt to connect
    if (client.connect(clientId.c_str(), mqtt_username, mqtt_password)) {
      Serial.println("connected");

      client.subscribe("servo");
      client.subscribe("dong_mo");

    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");  // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}
void callback(char* topic, byte* payload, unsigned int length) {
  String incommingMessage = "";
  for (int i = 0; i < length; i++) incommingMessage += (char)payload[i];

  Serial.println("Message arrived [" + String(topic) + "] " + incommingMessage);

  //--- check the incomming message
  if (strcmp(topic, "servo") == 0) {
    if (incommingMessage == "Mo") {
      while (digitalRead(32) == 0 || digitalRead(33) == 0) {
        servo.write(90);
        digitalWrite(13, HIGH);
      }
      delay(3000);
      digitalWrite(13, LOW);
    }
  }
  if (strcmp(topic, "dong_mo") == 0) {
    if (incommingMessage == "Mo") {
      servo.write(90);
      digitalWrite(13, HIGH);
    } else {
      servo.write(0);
      digitalWrite(13, LOW);
    }
  }
}
void publishMessage(const char* topic, String payload, boolean retained) {
  if (client.publish(topic, payload.c_str(), true))
    Serial.println("Message publised [" + String(topic) + "]: " + payload);
}
void setup() {
  Serial.begin(115200);
  servo.attach(servoPin);

  while (!Serial) delay(1);
  setup_wifi();

  pinMode(13, OUTPUT);

  espClient.setCACert(root_ca);  // enable this line and the the "certificate" code for secure connection

  client.setServer(mqtt_server, mqtt_port);
  client.setCallback(callback);
  SPI.begin();                        // Init SPI bus
  mfrc522.PCD_Init();                 // Init MFRC522
  mfrc522.PCD_DumpVersionToSerial();  // Show details of PCD – MFRC522 Card Reader details

  pinMode(32, INPUT_PULLUP);
  pinMode(33, INPUT_PULLUP);
  pinMode(34, INPUT_PULLUP);
  pinMode(35, INPUT_PULLUP);
  pinMode(26, INPUT_PULLUP);
  pinMode(27, INPUT_PULLUP);
}

int khac1 = 0;
int khac2 = 0;
int khac3 = 0;
int khac4 = 0;
void vaoChuong(int a, int b) {
  if (a == 1 && digitalRead(b) == 0) {
    DynamicJsonDocument doc3(1024);
    doc3["topic"] = "vao_chuong";
    if (b >= 33) {
      doc3["id"] = b - 33;

    } else {
      doc3["id"] = b - 23;
    }

    char mqtt_message[128];
    serializeJson(doc3, mqtt_message);

    publishMessage("esp32_data", mqtt_message, true);
    delay(2000);
  }
}
void loop() {
  if (!client.connected()) reconnect();  // check if client is connected
       
  client.loop();

  DynamicJsonDocument doc(1024);
  doc["topic"] = "trangThaiViTri";
  doc["ba"] = digitalRead(34);
  doc["bon"] = digitalRead(35);
  doc["nam"] = digitalRead(26);
  doc["sau"] = digitalRead(27);



  char mqtt_message2[1000];
  serializeJson(doc, mqtt_message2);

  publishMessage("esp32_data", mqtt_message2, true);
  delay(1000);

  vaoChuong(khac1, 34);
  vaoChuong(khac2, 35);
  vaoChuong(khac3, 26);
  vaoChuong(khac4, 27);


  khac1 = digitalRead(34);
  khac2 = digitalRead(35);
  khac3 = digitalRead(26);
  khac4 = digitalRead(27);
  if (!mfrc522.PICC_IsNewCardPresent()) {
    return;
  }

  // Select one of the cards
  if (!mfrc522.PICC_ReadCardSerial()) {
    return;
  }
  int mot = digitalRead(32);
  int hai = digitalRead(33);
  int ba = digitalRead(34);
  int bon = digitalRead(35);

  Serial.print("UID tag :");
  String content = "";
  byte letter;
  for (byte i = 0; i < mfrc522.uid.size; i++) {

    content.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " "));
    content.concat(String(mfrc522.uid.uidByte[i], HEX));
  }
  content.toUpperCase();
  Serial.println(content);
  DynamicJsonDocument doc1(1024);
  doc1["topic"] = "esp32_data";
  doc1["cardID"] = content;
  doc1["mot"] = mot;
  doc1["hai"] = hai;
  doc1["ba"] = ba;
  doc1["bon"] = bon;
  char mqtt_message[128];
  serializeJson(doc1, mqtt_message);

  publishMessage("esp32_data", mqtt_message, true);
  delay(3000);
}