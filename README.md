# Passive-Aggressive plant monitorisation device

## Overview
This project will keep track of the humidity and temperature inside a room for a specific plant. When the values are too high or too low, a passive-aggressive message will be sent to the user.

For getting the temperature and humidiy values, we will use a DHT sensor.

Data will be stored in a cloud server and displayed through an application.

## Pre-requisites
- Raspberry Pi 4 model B
- DHT 11 sensor
- Mini breadboard
- Servo Motor

## Schematics

![image](https://user-images.githubusercontent.com/47315066/115553955-a7ece580-a2b6-11eb-98d0-646fb2f5fad4.png)


## Setup and build 

### Setup
1. Raspberry pi
- Deploy /AT on the board
- Install dependencies 
```
sudo pip3 install requests python-firebase pigpio

git clone https://github.com/adafruit/Adafruit_Python_DHT.git

cd Adafruit_Python_DHT

sudo python setup.py install
```

2. Firebase
- Create firebase project
- Set up Realtime Database

```
{
  "rules": {
    ".read": "true",  // 2021-5-17
    ".write": "true",  // 2021-5-17
  }
}
```

3. Mobile app
- Create a new project and add it to the firebase console
- Add the generated json file to the android project
- Create a new (LiveData) entity in the firebase
![image](https://user-images.githubusercontent.com/47315066/115563175-edfa7700-a2bf-11eb-8652-808a1f2648b7.png)


### Build and run 
1. Run the script 
```
python3 script.py
````

2. Deploy the Mobile application 
Build and run, either on a phisycal device or an emulator


## Demo 
Video can be found in: https://photos.app.goo.gl/g1ji1ta6VkQdEa5G8
