import time
import board
import adafruit_dht
import pigpio
import asyncio
from firebase import firebase

# Initialising
dht_device = adafruit_dht.DHT11(board.D25)
pi = pigpio.pi()
servo = 18
pi.set_mode(servo, pigpio.OUTPUT)

firebase_url = "https://fir-authdemo-8ec18-default-rtdb.firebaseio.com"
firebase = firebase.FirebaseApplication(firebase_url, None)

fan_on = False
task = None

# Functions
async def fan():
    sleep_sec = 0.5

    while True:
        if fan_on:
            print("Fan is on, fanning")
            pi.set_servo_pulsewidth(servo, 900)  # ~-30 degrees
            await asyncio.sleep(sleep_sec)

            pi.set_servo_pulsewidth(servo, 1450)  # 0 degrees
            await asyncio.sleep(sleep_sec)

            pi.set_servo_pulsewidth(servo, 1800)  # ~30 degrees
            await asyncio.sleep(sleep_sec)
        else:
            print("Fan is off, continuing...")
            await asyncio.sleep(1)
            continue


def clean_up():
    if task is not None:
        task.cancel()
    dht_device.exit()
    pi.set_servo_pulsewidth(servo, 0)
    pi.stop()


# Main
async def main():
    global fan_on
    global task

    task = asyncio.create_task(fan())

    while True:
        try:
            # Print the values to the serial port
            thresholdTemp = firebase.get("/liveData", None)["thresholdTemp"]
            print("Threshold Temp: {:.1f} C".format(thresholdTemp))

            temperature_c = dht_device.temperature
            humidity = dht_device.humidity

            print("Temp: {:.1f} C    Humidity: {}% ".format(temperature_c, humidity))

            if temperature_c > thresholdTemp:
                fan_on = True
                flag = "HIGH"
            elif temperature_c < 10:
                fan_on = False
                flag = "LOW"
            else:
                fan_on = False
                flag = "OK"

            firebase.patch(
                "/liveData",
                {
                    "currentTemp": temperature_c,
                    "currentHumid": humidity,
                    "flag": flag,
                },
            )

            await asyncio.sleep(2)
        except KeyboardInterrupt:
            clean_up()
            break
        except RuntimeError as error:
            # Errors happen fairly often, DHT's are hard to read, just keep going
            print(error.args[0])
            await asyncio.sleep(2)
            continue
        except Exception as e:
            print(e)
            clean_up()
            break


asyncio.run(main())
