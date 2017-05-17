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

#14 
ListView complete, the primary focus is unit testing. I also have to submit my project summary. 

#15
It has been a while since I have updated my blog. I have completed my unit tests for testing hz-to-frequency, discovering a bug with the 3rd and 4th octaves during 
the process. I have made the ListView clickable, activating another activity, and sending the timestamp of the clicked song through the Intent. The new activity prints
the song onto the WebView. I am delighted with this progress. It is a Monday morning, a long eventful week spans before me. Here are my goals:

* Clustering algorithm #1 : k-means algorithm. 
* Back button on new activity that will bring me back to databaseentries.xml.
* Format output, after clustering, and print data onto the WebView. 
* Unit test - signIn (Android Espresso).

#16

I have successfully implemented a K-means cluster. I have opted for 2 clusters, representing crochet and quavers respectively, although the code for a third exists.
I have successfully created the back button mentioned above aswell, although the xml holds virtually no beauty. I have also formatted the output; this has introduced
plenty for me to focus on now for the next week. I have also created a flat-to-sharp converter and a keysignature object to handle printing:

* Debug flat-to-sharp converter. This includes getting the correct output.
* Serious amount of refactoring is needed. 
* Write tests for the new components. 
* Tidy XML; Make the project look presentable.

#17 

A serious amount of tests have been written. The flat-to-sharp converter works. I have discovered a bug, if there is no input, it crashes. The output is now correct for DMajor, it looks really good. I will try GMajor now to see if all is well. I need to test CMajor as well. 

#18 
I have implemented each of the keys. There are a few minor things I need to sort before moving onwards:

* Don't call concatenate function in the presence of spaces.
* Create a delete button.
* Test output by changing minimum required length to print. 

#19 
Great progress has been made. All task in #18 have been completed. I have a long click popupMenu which gives the user the option to delete an entry or view. I am 
currently refactoring my code, it was badly needed. So far so good:

* Implement a directory hierarchy which segregates scripts based on their functionality.
* Refactor code, include: create tools for webView/SongToJson.

--- Fun note, my .gitignore included *.java, my java files were never backed up onto git.... ever. Scary to think of what could of happened. #gitNoob

#20
Goals from task #19 have been taken care of. I have decided to code kMeans Cluster from scratch. Currently the cluster are hardcoded, which leaves little room for expansion, not to mention the smell emitting from it. I am at the final stage of this now, with a brand new test suit. It is possible for kmeans to get into an infinite loop, I have decided to add a counter to prevent this from happening. 

* Finish KMeans.
* Cluster to notes will be rewritten also. 

#21

KMeans is complete and tested. I have completed the ABCFormatConverter. It handles any note length at the moment, maintaining the correct beat count for each bar. When the note carries over the bar, a tie has been introduced. This section has been tested as well. Minute problem however, Accidentals within the keySignature are not taken care of. This needs to be fixed. I was reading about Coefficient of Variation, which may come in handy as a second condition for determining the number of clusters. Short notes are not being represented as cleanly as I want. 

#22

Big update. I have taken care of natural notes. I have also implemented time signature, displayed via spinner on the frontend. This gives the user the option of a bundle of common time signatures. The beatcount remains consistent through each time signature. The name of the song is now entered from the frontend, not saving the song until a name has been entered. These new features have been tested as well. Lastly, I have refactored my code, placing hardcoded values in XML resources files. There is a few more I need to take care of, but I understand the concepts involved now anyways. 

Moving forward, my primary focus is XML and display. These goals are provisional:

* Drawer-Navigation bar: which gives the user the option to logout and View a profile information section. 
* Profile information section: This will show the users email, name and potentially a picture. Considering placing a friendlist here too. 
* XML Design throughout. 

#23

I decided to get rid of the pager-fragment implementation. It was extremely difficult to operate the drawer-navigation bar in tandem with the fragments. Both rely on touch input, and fragment had a high precedence. I have a nagivation bar that connects you to record audio, database entries and logout. These all work, except for logout currently. This shouldn't be a problem, just a bit of tweeking is needed. The design can be updated as well, but I am happy with it currently. I had to refactor my code aswell, as a standard activity differs from a fragment activity, nothing too difficult however.

* Fix logout button.

#24

I have introduced a UserProfile page and a Friend finder that allows you to search database for other users. Code isn't working at the moment. I need to create /songId/ and /Friend Requests/ in same directory, then on request save the users UID in this folder. 

* Add two directories.
* Put a user request in incoming requests on the recipents side. 
* Create a popup, if you accept: Added to database, given a flag indicating which user it is from.

#25 

Problem fixed. I have added those two directories plus 2 more. You can add friends now. This is a two part process; firstly you send a request to the user which is stored in the FriendRequest folder within their userprofile. In tandem, another folder called PendingFriendRequest stores an identical copy of the request. Permission on the Firebase database needed to be modified to allow a user to write to a directory on another users account. I created an personalised Array adapter that displays PendingFriendRequests. I implemented a friendsList, allowing you to see all your currently held friends. 

You can now successfull send songs to a friend. Data transfer is similar to approach above. Custom ArrayAdapter have been generated to care for the ListView. Selection of the data you wish to share is done through a popup Dialog. Here are a handful of my goals moving forward:

* Accept song, story in database. Two separate lists?
* Set user profile picture on the new songs. 
* Modify Adapters to make them response to button.onClick(). 
* Discover bug with Dads phone. 

#26 

I can now send songs between users. Each some has a uid identifying the creator and a profile picture, only if it was recieved. The format for these songs is very nice. I have implemented a record button that flashes red when pressed. I got rid of the only two button system, record and stop, in place of a single button system that uses modulus to tell whether the audio is recording or not. Right now I have all the functionality I aimed achieved. I will undoubtedly discover a few errors within the next few days. Until then, I am focussed on design, design, design.

#27 

I have added icons to the navigation bar and removed user profile. Instead I will generate the users information at the top of the navigation bar, this way it is neater overall. The background material I was attempting to use is causing my code to crash, I need to go searching again. I added code to friend finder so existing friends will not be added to the recommended list. All is going well. I need to add some testing on my delete functions to make sure they are acting according to plan. ListViews need a beauty treatment. 

#28

User profile has been removed and replaced with a navigation_header located at the top of the drawer_layout. The overall design looks much neater with this. The top bar includes the users profile picture with their name and email address. I decluttered the record_audio page as well. It was getting far to messy. I decided to go for a leaner look. My plan now is to reload listview after button is clicked. This piece of functionality will be used multiple times on different lists. After this I am onto my final stretch of testing.  

#29

I have discovered two bugs that need attending too:

* displaying from databaseEntries.
* Hamburger is not responsive. 

I need to modify these and enhance the XML present in my listView pages. This is the current task, I don't expect it to take too long. Once this is complete I must most onto writing tests for friend request and song request to ensure firebase is acting the way I wish to act. 

#30 

Both bugs from above have been eliminated. I have chosen a standard colour scheme for the application and applied it throughout. I am impressed with how it all looks. I added titles to each page and a picture to compliment it. Also I added spaces to listView so they aren't so squashed. I have no pending friend/song data at the moment to test the layout of my new ListViews, I'll try this again tomorrow. 

* Buttons with ListView: responsive to click (check your notes). 
* Test pending song + pending friend. 

#31 

Very productive day was had. I have 90% of the design completed now. All ListViews are responsive, removing entries on click. I had a spot of bother with git. I somehow managed to delete my sendFriendRequest function. I restored a commit with the code and became stranded. Panic set in very rapidly. The problem lasted no longer than 15 minutes thank goodness. I am unsure what to move onto next. I two minor features I would like to implement:

* End the recording thread on activity change.
* Return to the correct place after using preview. 

I think I will hold off on advancing any future until I meet with Donal, it is time to focus on my exams. 

#32

I have implemented a midi file and implemented a tuner. Delighted with the new features. Now it is time to test and document my work and pray to god all goes well on the day. Happy days, happy days. 

#33

I have added an option that allows you to write a file internally or to print via WIFI. I modified the tuner a little today, allowing a larger window of error, increasing it from needing to be equal, to allowing on or two hz slack. Today I have refactored my friend request adapters. Taking them out of the customer array adapters that I created for both and places the code into a class separate class that will handle them. This will make testing them much easier, I hope. 

#34 

I fixed a bug that arose when using the tuner after the pitch detector. All is well in the world once more, time for some refactoring. 

#35 

Oh my days I did a lot of refactoring. 38 separate classes from a squashed 10 or so. Really happy with the work. I have checked the kMeans cluster tests and pitch detector tests, these appear to be running smoothly. Tomorrow I move onto my last stretch of factoring followed by my last suite of tests. It will be sad to see the end of this project. I have become too attached. 

#36 

I discovered a slight bug in my midi playback. Well not a bug. The abc has been processed to handle the key signature in place. This is not the cause with the midi player, which doesn't know anything about the key and relies on clear abc instructions. I created a MIDIController that will convert the abc format back to a version containing all the accidentals necessary to successfully playback. This works properly now.

#37 

I wrote Espresso tests to handle Login and Register simulation. Need to find a way to test firebase components, currently experiencing difficulties with my approach. I increase the tempo of the midi playback as well to spice things up. 
