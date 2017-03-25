#First blog entry

Excited to the making progress on the project. My objectives are:
* Set up Android studio IDE.
* Write a basic C++ program.

//Set up Android studio IDE.
Successfully installed android studio. I chose the minimum SDK to be API 15: Android 4.0.3 (IceCreamSandwich), which will support 97.4% of devices. 


#Second Entry

I have neglected this blog. I have successfully added the audio recorder to the project. This was completed using MediaRecorder. Unfortunately, this method of audio recording does not facilitate the encoding format I need for this project: PCM. The project has the layout I require, the only change needed to make will be the introduction of an AudioRecord to replace the MediaRecorder. My current objectives:
* Replace MediaRecorder with an AudioRecord.
* Fix bug: On rotation of screen all previously entered data is deleted. 

#Third Entry

Replacing the MediaRecorder with an AudioRecord proved to be much more difficult that initially expected. MediaRecorder completes the entire process
for you, you barely need to do a thing. AudioRecord on the other hand, requires you to read and store the audio, short by short. 
* Discover a way of playing music to ensure earlier steps are working correctly. 

I have calculated the magnitude and frequency using:
* magnitude - Math.sqrt((buffer[i]*buffer[i])+(buffer[i+1]*buffer[i+1]))
* frequency - Index * SampleRate/N

The output is rather strange. I am using a pure tone of 440Hz, although my answer does not reflect this. 

#Fourth Entry

I got in touch with Bryan Duggan from DIT, the creator of Tunepal, inquiring about the technique he used to obtain musical notes from audio recording. He used PYIN 
for my tunepal, a C++ library that utilises the YIN algorithm. He recommends this algorithm as it is more accurate than using FFT for pitch detection. I am currently
using TarsosDSP, a java library that offers the Yin algorithm. 

Goals for the upcoming week: 
* Detect single notes using YIN algorithm. 
* Meet with Bryan Duggan.
* Research multiple note detection. 

#Fifth Entry

I have successfully implemented pitch detection. The system now recognises each note that is played. I have also developed a way of keeping track of what octave that note is on. This will come in handy later on, when I attempt to convert this array of scattered notes, into formal sheet music. I do not deem it necessary to meet with Bryan Duggan anymore. I understand his advice and shouldn't take up any more of his time. New goals include: 

* Research timing, time signature, deriving length of each note played.
* Bug: Third octave pitch detection doesn't work correctly. 

#Sixth Entry

Successful weekend. I now have the note played being displayed on the screen. The length of each note is saved as well. My plans for the coming week:

* Clean up display.
* Set up a database.
* Do something with the length of notes.

#Seventh Entry

I have successfully installed a LAMP stack. This stack consists of Apache, MySQL and PHP. This post is a subpost of #Sixth 
dealing with the task: Set up a database. Steps include: 

* Secure Apache with Let's Encrypt
* Install phpMyAdmin

#Eighth Entry

I have decided to use Firebase data base instead. I have connected to the database and sent data. There is no authentication at the moment, this can be added at a later
stage. I have decided to refactor the code. It is getting quite messy, soon it will be difficult to make any sense of it. 

#Ninth Entry

I have experienced a sea of difficulty trying to write my pitches to a MIDI file. Questioning if this is the correct approach. MIDI would allow for quick display of 
musical notation, but may not be worth the time and effort. My approach:

* Music21 directly input notes to be displayed, timing not a consideration at the moment.

This is the same library I would have used to print the notation anyways. There are difficulties however. Music21 is a Python library, which will cause some problems.
My solution to this is either: 

* Android-python27: allowing python to be part of my project.
* SL4A: same as above, different approach.
* Create a python server that handles the musical notation. Not sure of the limitations here. 

#Tenth Entry

This is a long overdue entry, a lot has changed. I decided to use a javascript library - abcjs to print my sheet music. In order to do this I needed to set up a 
WebView inside my application that would display the HTML. I have this successfully working at the moment, printing a generic song. I am also able to pass parameters 
from the Java side as well. I am now working on the display of the project. I have created a login and register page. These pages connect to the firebase database. 
I am currently debugging the transition stage between pages, as my app keeps briefly crashing. Overall I am delighted with my progress. A fellow class mate suggested I 
use a pager to display my content, this will come in time. My current goals are as follows:

* Fix bug occuring with activity swapping. 
* Create logout button.
* Save temporary song in database under the current user and recall it. 
* logout/in and try retrieve the saved song.

#Eleventh Entry

Great progress has been made yet again, woo. I have removed the bug mentioned above. I have also created the log in button. I have implemented the page mentioned above,
it looks very slick. Goals:

* Store and retrieve song, ensuring song is stored under user profile. 
* Create activity that prints the name of each song that is available to choose from, making each option clickable. 

#12

Testing is now the main priority: Unit testing. 

#13
Unit testing still needs to occur. Save and retrieve from the database now works correctly. New focus is:

* Unit testing
* ListView 
