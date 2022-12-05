# Early Bird

<div style="display:flex;">
<img alt="App image" src="https://github.com/kunal-mahatha/Early-Bird/blob/main/EarlyBird-snaps/Early-Bird-Banner.gif?raw=true" width="100%">
</div>

<img align="right" width="150" height="150" src="https://github.com/kunal-mahatha/Early-Bird/blob/main/EarlyBird-snaps/logo2.png?raw=true">


Early Bird is a smart A.I. powered Alarm Clock App which will stop once the user Start Brushing. It brings pure alarm experience to you by combining powerful features and clean interface. The interface of our Alarm clock is designed to be simple, intuitive and efficient. By removing what is not essential, and adding the A.I. mode, we tried every possible way to wake you up.

## Project Structure

    .             
    │
    ├── android-app/EarlyBird             # Android App (EarlyBird)
    │                     
    └── ml-model/Clocky                   # ML Model (Clocky)

-----------------------------------------------------------------------------------------------------------------------------------------------------------
## Android App (Early Bird)
Early Bird is an Android App in which Clocky(ML Model) is deployed.


# 

<div style="display:flex;">
<img alt="App image" src="https://github.com/kunal-mahatha/Early-Bird/blob/main/EarlyBird-snaps/1-min.png?raw=true" width="16%">
<img alt="App image" src="https://github.com/kunal-mahatha/Early-Bird/blob/main/EarlyBird-snaps/2-min.png?raw=true" width="16%">
<img alt="App image" src="https://github.com/kunal-mahatha/Early-Bird/blob/main/EarlyBird-snaps/3-min.png?raw=true" width="16%">
<img alt="App image" src="https://github.com/kunal-mahatha/Early-Bird/blob/main/EarlyBird-snaps/4-min.png?raw=true" width="16%">
<img alt="App image" src="https://github.com/kunal-mahatha/Early-Bird/blob/main/EarlyBird-snaps/5-min.png?raw=true" width="16%">
<img alt="App image" src="https://github.com/kunal-mahatha/Early-Bird/blob/main/EarlyBird-snaps/6-min.png?raw=true" width="16%">
</div>

#

## Model Architecture

    .
    ├── TFLite Model     --- MobileNetV2 is converted to TFlite
    │
    └── Google ML Kit    
    
## Features
 - Clean and User-Friendly UI
 
 - No Ads or Bloatwares
 - 100% Offline App
 - App is on-device inherence for user's privacy.
 - Privacy :
    - No unnessary permissions asked.
    - No data is collected
 - Mapout Feature
    - It gives you some time to get out of your bed and grab your toothbrush
    - User can customise mapout time
 - **No button** to stop your Alarm, Alarm will Stop automatically after it recognises that you brushing for atleast 5 seconds.
 - Notification bar update if phone is Screen Locked
 - Alarm will not stop even if you close the App.

## How to use the App?
 - Install the from Google Play Store, direct link is [Here]()
  
 - Open the App and Tap on (+) button to add New Alarm.
 - Tap the Clock and Set the time through the circular seekbar.
 - Set the Alarm by Tapping on (SAVE) button.
 - Tap on the Notification when The Alarm rings.
 - Tap on (MAP OUT) button.
 - Grab your toothbrush, start brushing, and show it to the camera.
 - After brushing for 5 seconds, the Alarm will Stop and App will be closed Automatically.

-----------------------------------------------------------------------------------------------------------------------------------------------------------
## Machine Learning Model (Clocky)
Clocky is a Deep Learning Computer Vision Model for detection of Toothbrush. It is made with MobileNetV2 Architecture and Caffe Model.

## Model Architecture

    .
    ├── MobileNetV2      
    │
    └── Caffe Model      

### Plots
<div style="display:flex;">
<img alt="App image" src="https://github.com/kunal-mahatha/Early-Bird/blob/main/EarlyBird-snaps/ebplot.jpg?raw=true" width="100%">
</div>

----------------------------------------------------------------------------------------------------------------------------------------------------------------

## How to Contribute to EarlyBird?
To contribute to Clocky (The DL Computer Vision Model) [Click Here](https://github.com/kunal-mahatha/Early-Bird/tree/main/ml-model)

To contribute to EarlyBird App [Click Here](https://github.com/kunal-mahatha/Early-Bird/tree/main/android-app)

-----------------------------------------------------------------------------------------------------------------------------------------------------------------

## COMING SOON !!
 - Dark Mode
 
 - UI Improvements
 
 These features will be added in the next update.
 
 ---

<div style="display:flex;">
<img alt="App image" src="https://github.com/kunal-mahatha/Early-Bird/blob/main/EarlyBird-snaps/Author.gif?raw=true" width="100%">
</div>
