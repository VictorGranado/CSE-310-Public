# Overview  
The ESP32 Smart Hydroponics Monitor is designed to manage and monitor essential parameters for a hydroponic system. This microcontroller-based system collects real-time data, including temperature, humidity, and water level, displaying it on an LCD and a web interface. Users can monitor and control the system remotely via Wi-Fi, making it a powerful solution for efficient plant growth in a controlled environment.  

The purpose of this project is to simplify the management of hydroponic systems by automating environmental monitoring and providing actionable data insights.  

[Software Demo Video](https://www.youtube.com/watch?v=4ApJ5mPsqtc)  

# Features  
- **Real-time Monitoring**: Displays temperature, humidity, and water level on an LCD and web interface.  
- **Web Dashboard**: Interactive dashboard hosted on the ESP32's web server to visualize sensor data in real time.  
- **Temperature and Humidity Control**: Automatically activates a fan or water atomizer based on sensor readings.  
- **Ultrasonic Water Level Sensor**: Monitors tank water level and calculates percentage for visual and remote display.  
- **Wi-Fi Connectivity**: Remote monitoring and control via a web browser.  
- **Error Handling**: Built-in error messages for sensor malfunctions.  
- **Interactive LCD Display**: Real-time updates for local system status.  

# Development Environment  
- **Code Editor**: Arduino IDE for programming the ESP32.  
- **ESP32 Board**: ESP32-WROOM-32 microcontroller.  
- **Programming Language**: C++ (Arduino Framework).  
- **Hardware Components**:  
  - ESP32 microcontroller  
  - DHT11 sensor for temperature and humidity  
  - Ultrasonic sensor for water level measurement  
  - LiquidCrystal_I2C for LCD display  
  - Fan and water atomizer for environmental control  
- **Libraries Used**:  
  - `Wire.h` and `LiquidCrystal_I2C.h` for I2C communication with the LCD.  
  - `DHT.h` for temperature and humidity sensor integration.  
  - `WiFi.h` and `WebServer.h` for Wi-Fi connectivity and web server functionality.  

# Useful Websites  
- [ESP32 Documentation](https://docs.espressif.com/projects/esp-idf/en/latest/)  
- [Arduino IDE Installation Guide](https://www.arduino.cc/en/Guide)  
- [DHT Sensor Library Documentation](https://github.com/adafruit/DHT-sensor-library)  
- [LiquidCrystal_I2C Library Guide](https://www.arduino.cc/reference/en/libraries/liquidcrystal_i2c/)  

# How to Run  
1. Install the Arduino IDE and add ESP32 support by following [this guide](https://randomnerdtutorials.com/installing-the-esp32-board-in-arduino-ide-windows-instructions/).  
2. Clone or download this repository.  
3. Install the required libraries through the Arduino Library Manager or manually.  
4. Connect the ESP32 board to your computer via USB.  
5. Open the `.ino` file in the Arduino IDE.  
6. Configure your Wi-Fi credentials in the code (`ssid` and `password`).  
7. Upload the code to the ESP32 board.  
8. Open the Serial Monitor at 115200 baud rate to verify successful connection and IP address.  
9. Access the web dashboard by entering the ESP32's IP address in your browser.  

