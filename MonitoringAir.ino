#include <OneWire.h>
#include <DallasTemperature.h>
#include <Arduino.h>
#include <WiFi.h>
#include <Firebase_ESP_Client.h>
#include <time.h>

// Provide the token generation process info.
#include "addons/TokenHelper.h"

// Provide the RTDB payload printing info and other helper functions.
//CONFIG DATA YANG DIPERLUKAN UNTUK MENGIRIM DATA KE SERVER FIREBASE

#include "addons/RTDBHelper.h"
#define WIFI_SSID "hpkentang" //WIFI KAMPUS
#define WIFI_PASSWORD "aimbot12"//PW WIFI KAMPUS
#define API_KEY "AIzaSyDJT3U7MFM00UOF7ffn1ktYQonURZRM6JI"
#define USER_EMAIL "mardoslg03@gmail.com"
#define USER_PASSWORD "westronger10"

// Insert RTDB URLefine the RTDB URL
#define DATABASE_URL "https://monitoring-air-8b41c-default-rtdb.asia-southeast1.firebasedatabase.app/"

// Define Firebase objects
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

// Simpan uid token untuk bisa input data ke firebase
String uid;

// main structure database to be updated in setup with the user UID
String databasePath;
// Database child nodes
String tempPath = "/temperature";
String turPath = "/turbidity";
String acidPath = "/acidity";
String timePath = "/timestamp";

// Parent Node (akan diupdate ketika loop() has been running)
String parentPath;

int timestamp;
FirebaseJson json;

//server untuk membuat timestamp 
const char* ntpServer = "pool.ntp.org";

//DAFTAR PIN SENSOR 
int sensorTDS = 34; // analog pin
#define SENSOR_PIN  17 // ESP32 Digital pin GPIO17 connected to DS18B20 sensor's DATA pin
const int ph_Pin = 32; // analog pin

OneWire oneWire(SENSOR_PIN);
DallasTemperature DS18B20(&oneWire);
static float kekeruhan;
static float teg;
float tempC; // temperature in Celsius
float tempF; // temperature in Fahrenheit
float Po = 0;
float PH_step;
int nilai_analog_PH;
double TeganganPh;

//untuk kalibrasi PH 
float PH4 = 1.250;
float PH7 = 1.570;

// Timer variables (atur waktu untuk kirim data ke server melalui timer Delay satuan miliSekon)
unsigned long sendDataPrevMillis = 0;
unsigned long timerDelay = 60000;// 60.000 MILISEKON = 1 MENIT

// Initialize WiFi
void initWiFi() {
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to WiFi ..");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print('.');
    delay(1000);
  }
  Serial.println(WiFi.localIP());
  Serial.println();
}

unsigned long getTime() {
  time_t now;
  struct tm timeinfo;
  if (!getLocalTime(&timeinfo)) {
    return(0);
  }
  time(&now);
  return now;
}

void setup() {
  
  Serial.begin(115200); // initialize serial
  DS18B20.begin();    // initialize the DS18B20 sensor
  pinMode (ph_Pin , INPUT);

  initWiFi();
  configTime(0, 0, ntpServer);
  
  // Assign the api key (required)
  config.api_key = API_KEY;

  // Assign the user sign in credentials
  auth.user.email = USER_EMAIL;
  auth.user.password = USER_PASSWORD;

  // Assign the RTDB URL (required)
  config.database_url = DATABASE_URL;

  Firebase.reconnectWiFi(true);
  fbdo.setResponseSize(4096);

   // Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h

  // Assign the maximum retry of token generation
  config.max_token_generation_retry = 5;

  // Initialize the library with the Firebase authen and config
  Firebase.begin(&config, &auth);

  // Getting the user UID might take a few seconds
  Serial.println("Getting User UID");
  while ((auth.token.uid) == "") {
    Serial.print('.');
    delay(1000);
  }
  // Print user UID
  uid = auth.token.uid.c_str();
  Serial.print("User UID: ");
  Serial.println(uid);

  // Update database path
  databasePath = "/UsersData/" + uid + "/readings";
     
}

//akan diulang
void loop() {

  // SENSOR SUHU
  DS18B20.requestTemperatures();       // send the command to get temperatures
  tempC = DS18B20.getTempCByIndex(0);  // read temperature in °C
  tempF = tempC * 9 / 5 + 32; // convert °C to °F
  
  // SENSOR TURBIDITY
  teg = analogRead(sensorTDS)*(5.0/4095.0); // hitung tegangan yang didapat
  kekeruhan = 100.00-(teg/2.99)*100.00; 
  // NTU = 100.00-(tegangan yang diterima / tegangan didapat saat air jernih)*100.00
  
  // SENSOR PH
  nilai_analog_PH = analogRead(ph_Pin);
  TeganganPh = 3.3 / 4095.0 * nilai_analog_PH;

  Serial.print("- Suhu Air: ");
  Serial.print(tempC);    // print the temperature in °C
  Serial.print("°C");
  Serial.print("  ~  ");  // separator between °C and °F
  Serial.print(tempF);    // print the temperature in °F
  Serial.println("°F");
  Serial.print("- Kekeruhan : ");
  Serial.print(kekeruhan);
  Serial.println(" NTU");
  Serial.print("Volt NTU: ");
  Serial.print(teg);
  Serial.println(" Volt");
  
  // PERHITUNGAN KONVERSI ANALOG KE PH
  PH_step=(PH4-PH7)/3;
  Po = 7.00 +((PH7 - TeganganPh) / PH_step); // Po= 7.00 ((teganganPh7 -Tegangan ph / PH STEP);
  
  Serial.print("- Nilai PH Cairan:  ");
  Serial.println(Po, 2);
  Serial.print("TeganganPh: ");
  Serial.print(TeganganPh);
  Serial.println(" Volt");
  delay(4000);

  // Send new readings to database ubah timerDelay mengatur jadwal kirim data ke server satuan milisekon
  
  if (Firebase.ready() && (millis() - sendDataPrevMillis > timerDelay || sendDataPrevMillis == 0)){
    sendDataPrevMillis = millis();

    //Get current timestamp
    timestamp = getTime();
    Serial.print ("time: ");
    Serial.println (timestamp);

    parentPath= databasePath + "/" + String(timestamp);

    json.set(tempPath.c_str(), String(tempC));
    json.set(turPath.c_str(), String(kekeruhan));
    json.set(acidPath.c_str(), String(Po));
    json.set(timePath, String(timestamp));
    Serial.printf("Set json... %s\n", Firebase.RTDB.setJSON(&fbdo, parentPath.c_str(), &json) ? "ok" : fbdo.errorReason().c_str());
  }
}
  
    
