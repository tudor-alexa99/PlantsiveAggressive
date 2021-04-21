# Passive-Aggressive plant monitorisation device

## Overview
This project will keep track of the humidity and temperature inside a room for a specific plant. When the values are too high or too low, a passive-aggressive message will be sent to the user.

For getting the temperature and humidiy values, we will use a DHT sensor.

Data will be stored in a cloud server and displayed through an application.

## Prequisites
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
```

2. Firebase
- Create firebase project
- Set up Realtime Database

3. Mobile app
- Create a new project and add it to the firebase console
- Add the generated json file to the android project

### Build and run 
1. Run the script 
```
python3 script.py
````

2. Deploy the Mobile application 
