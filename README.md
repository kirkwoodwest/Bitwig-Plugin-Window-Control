# Bitwig Plugin Window Control
 An experimental extension to show/hide VST windows for a channel or all channels in bitwig
 
![image](https://user-images.githubusercontent.com/6645471/204392354-ee2b3a35-fea8-4534-83b6-88281ca0b33e.png)

## Usage
In the studio i/o panel there is a Cursor Track and a couple buttons or opening/closing windows. Navigate to the channel you want and press open/Close plugin windows.

Note: Does not work with chain devices or FX Slots.

## Installation
- Copy the .bwextension file into your Bitwig Studio/Extensions folder
- In the controllers section of Settings, add a new controller. Select Kirkwood West -> Bitwig Plugin Control

This should appear in the Controller Section

![image](https://user-images.githubusercontent.com/6645471/204392413-18f5b790-2b50-4e5f-a27a-dad973cd55b6.png)

You may need to hit the power button twice before the settings show up. When you make changes here you will need to hit the power button for re-initialization of the extension.

Be careful with the track and device and layer counts! If you allocate too many Bitwig takes a while to process these. If you must optimize completely, set the All channel and send count to 1. 

- This panel should appear in your Studio I/O Section in Bitwig.

![image](https://user-images.githubusercontent.com/6645471/204392794-800cff88-6341-4137-97c2-2f20daeefae6.png)

## Feedback
Looking forward to developing this more. So if you have ideas or feedback, I woud like to hear them. kirkwoodwest@gmail.com 
There are some things missing from the API as well and with some other users behind it we could get more API support. Its possible the bitwig overloards see this as a feature to eventually gets sucked up into Bitwig Studio.
