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
