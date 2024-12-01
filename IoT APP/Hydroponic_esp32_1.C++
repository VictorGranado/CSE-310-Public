#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <DHT.h>
#include <DHT_U.h>
#include <WiFi.h>
#include <WebServer.h> // Include the WebServer library

// I2C LCD with address 0x27 (common address) and 16x2 size
LiquidCrystal_I2C lcd(0x27, 16, 2);

int dhtPin = 13;
// Pins for ultrasonic sensor
const int trigPin = 16;
const int echoPin = 17;

DHT dht(dhtPin, DHT11);

// Constants for tank dimensions
const float tankHeight = 16.7; // Height of the water tank in centimeters
const float maxDistance = 200.0; // Maximum distance the ultrasonic sensor can measure in centimeters

const int fanPin = 18;
const int waterAtomizerPin = 19;

// Define built-in LED pin (GPIO 2 for most ESP32 boards)
const int ledPin = 2; 

// Wi-Fi credentials
const char* ssid = "optix_legacy";
const char* password = "onmyhonor";

// Create a WebServer object on port 80
WebServer server(80);

// Global variables for sensor readings
float tempC = 0;
float humidity = 0;
int waterLevelPercentage = 0;

void setup() {
  lcd.init(); // Initialize I2C LCD
  lcd.backlight(); // Turn on the LCD backlight

  dht.begin(); // Initialize DHT sensor

  // Display initial text on LCD
  lcd.setCursor(9, 0);
  lcd.print("WL: ");

  // Initialize ultrasonic sensor pins
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);

  pinMode(fanPin, OUTPUT);
  pinMode(waterAtomizerPin, OUTPUT);

  pinMode(ledPin, OUTPUT);  // Set LED pin as output

  Serial.begin(115200); // For debugging purposes

  // Wi-Fi connectivity setup
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWiFi connected.");
  
  // Print the IP address of the ESP32
  Serial.println("ESP32 IP address: ");
  Serial.println(WiFi.localIP());

  // Start the web server
  server.on("/", handleRoot);  // Route for the root URL
  server.on("/data", handleSensorData);  // Route for sensor data
  server.begin();  // Start the web server
  Serial.println("HTTP server started.");
}

void loop() {
  // Handle client requests to the server
  server.handleClient();

  // Flicker the built-in LED
  digitalWrite(ledPin, HIGH); // Turn the LED on
  delay(100);                 // Wait for 100ms
  digitalWrite(ledPin, LOW);  // Turn the LED off
  delay(100);                 // Wait for 100ms

  tempC = dht.readTemperature(); // Read temperature in Celsius
  humidity = dht.readHumidity(); // Read humidity percentage

  // DHT sensor error handling
  if (isnan(tempC) || isnan(humidity)) {
    lcd.setCursor(0, 0);
    lcd.print("Sensor Error ");
    Serial.println("DHT sensor error: Invalid readings");
    return; // Skip further processing if the DHT sensor fails
  }

  int roundedTemperature = round(tempC); // Round temp to a whole number
  int roundedHumidity = round(humidity); // Round humidity to a whole number

  // Display temperature and humidity on the LCD
  lcd.setCursor(0, 0);
  lcd.print("Tmp: ");
  lcd.print(roundedTemperature);
  lcd.print("C");

  lcd.setCursor(0, 1);
  lcd.print("Hum: ");
  lcd.print(roundedHumidity);
  lcd.print("%");

  // Temperature control
  if (roundedTemperature >= 26.0) {
    lcd.setCursor(0, 0);
    lcd.print("         "); // Clear previous temperature
    lcd.setCursor(0, 0);
    lcd.print("TOO HOT");
    digitalWrite(fanPin, HIGH);
  } else if (roundedTemperature <= 20.0) {
    lcd.setCursor(0, 0);
    lcd.print("         "); // Clear previous temperature
    lcd.setCursor(0, 0);
    lcd.print("TOO COLD");
    digitalWrite(fanPin, LOW);
  }

  // Humidity control
  if (roundedHumidity >= 90.0) {
    lcd.setCursor(0, 1);
    lcd.print("                "); // Clear previous humidity
    lcd.setCursor(0, 1);
    lcd.print("TOO WET");
    digitalWrite(waterAtomizerPin, LOW); // Stop the atomizer
  } else if (roundedHumidity <= 60.0) {
    lcd.setCursor(0, 1);
    lcd.print("                "); // Clear previous humidity
    lcd.setCursor(0, 1);
    lcd.print("TOO DRY");
    digitalWrite(waterAtomizerPin, HIGH); // Turn on the atomizer
  }

  // Ultrasonic sensor control
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  long duration = pulseIn(echoPin, HIGH);
  float distance = duration * 0.034 / 2; // Calculate distance based on pulse

  // Ultrasonic sensor error handling
  if (distance == 0 || distance > maxDistance) {
    lcd.setCursor(12, 0);
    lcd.print("Err"); // Display error on LCD
    Serial.println("Ultrasonic sensor error: Invalid distance");
    return; // Skip further processing if the ultrasonic sensor fails
  }

  // Calculate the percentage of water level
  waterLevelPercentage = (1 - distance / tankHeight) * 100;
  waterLevelPercentage = constrain(waterLevelPercentage, 0, 100); // Ensure percentage is within 0 to 100 range

  // Display water level percentage on LCD
  lcd.setCursor(12, 0); 
  lcd.print(waterLevelPercentage);
  lcd.print("%");

  // Display water level on Serial Monitor
  Serial.print("Water level: ");
  Serial.print(waterLevelPercentage);
  Serial.println("%");

  delay(100); // Small delay before the next loop
}

// Function to handle requests to the root URL
void handleRoot() {
  String html = "<html><head><title>ESP32 Sensor Data</title>";
  html += "<script>";
  html += "function fetchData() {";
  html += "  fetch('/data')"; // Request data from /data endpoint
  html += "    .then(response => response.json())"; // Parse JSON response
  html += "    .then(data => {";
  html += "      document.getElementById('temperature').innerText = 'Temperature: ' + data.temperature + ' °C';";
  html += "      document.getElementById('humidity').innerText = 'Humidity: ' + data.humidity + ' %';";
  html += "      document.getElementById('waterLevel').innerText = 'Water Level: ' + data.waterLevel + ' %';";
  html += "    });";
  html += "}";
  html += "setInterval(fetchData, 2000);"; // Fetch data every 2 seconds
  html += "</script></head><body onload='fetchData()'>"; // Call fetchData on page load
  html += "<h1>ESP32 Sensor Data</h1>";
  html += "<p id='temperature'>Temperature: </p>";
  html += "<p id='humidity'>Humidity: </p>";
  html += "<p id='waterLevel'>Water Level: </p>";
  html += "</body></html>";
  
  server.send(200, "text/html", html); // Send the HTML response
}

// Function to handle requests for sensor data
void handleSensorData() {
  String jsonResponse = "{";
  jsonResponse += "\"temperature\":" + String(tempC, 1) + ",";
  jsonResponse += "\"humidity\":" + String(humidity, 1) + ",";
  jsonResponse += "\"waterLevel\":" + String(waterLevelPercentage);
  jsonResponse += "}";

  server.sendHeader("Access-Control-Allow-Origin", "*"); // Add CORS header
  server.send(200, "application/json", jsonResponse);
}

}