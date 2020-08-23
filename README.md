# Guess-The-Song

Guess The Song ReadMe

Guess The Song is an Android Mobile Application which allows the person playing to collect song lyrics around Bay Campus, Swansea University
and based on the lyrics collected to attempt to guess from which song these lyrics are.

Once installed from the APK file the game starts from it's Main Menu. The user can start playing, by pressing the play button, can select
if they want to play with classic or modern songs with a toggle or to see all of the songs they have either guessed correctly, incorrectly 
or skipped. The main menu screen also shows the total points the user has.

The Map screen shows where the user currently is and where the next lyric is. To collect the lyric all the user has to do is walk into the
circle surrounding the lyric and it will automatically be collected for them. A pop up will appear congratulating them and showing the lyric
they have just collected. The collected lyrics list is accessible at all times from the button in the lower right corner of the screen. Each
song has 20 points maximum available. They are awarded if the user manages to guess the song from a single lyric. Each lyric they collect will
lower the points available by one.

Guesses can be submitted through the lyrics screen. It is accessible through the button on the Map screen. At the top there is an input which 
makes suggestions once the user has started typing the name of the song. A guess is submitted when one of the suggestions is tapped.A pop up
appears notifying the user if it was correct.If it is correct the respective number of points shall be added to the user's total and a new song 
will be loaded. If the guess is incorrect 2 points will be deducted from the song's total. Below the input field are all of the lyrics 
collected by the user so far. The user can scroll through all of them. Once a lyric has been collected the number of points left will also 
be displayed at the bottom left corner of the screen. If the user decides that they can't guess which song is that they are able to skip it by 
pressing on the button at the lower right corner. However, they will lose all of the points associated with the song.

When the user taps the Song History button back at the Main Menu they will be presented will all of the songs they have attempeted - guessed 
correctly, incorrectly and skipped. They are able to tap on the song they want to be able to see its full text. Once at the full page screen
the user can scroll through the song full text or click a button by the title of the song and have it launch in the YouTube app on their phone.
If they do not have the app they will be taken to the Play Store where it can be downloaded.

Technical Information:

The following is an overviwe of the .kt classes. Detailed information is available in the form of comments in them.

The entire project shall be submitted as there are numerous unique files in the assets and resources folder.

Additional Features: 
Points system - -1 point for every collected lyric, -2 points for every wrong guess.
Song History - All of the songs the user has seen.
Full Text - Full text of all songs user has seen.
YouTube player from Song History


FileReaderObject - This object provides file reading functionality. The lists of songs and the songs' txt files are loaded here and the operations
around selection of a song and lyric is handled in this object. It is initially called on application startup, however numerous methods are called 
from different places in the application. The setting of an adapter for the guess suggestions is also handled here.

LyricAdapter - this class is the adapter for the recycler views used throughout the application. The same class is used at all locations as the data
is comparable. It provides functionality for setting the recycler view and also initialises a listenere which is able to provide the position of the
element tapped.

LyricsActivity - this class handles the guessing, suggestions, skipping and points updating. It holdes the listeners for the AutoCompleteTextView and 
the Song Skip button as well as a RecyclerView, where the lyrics collected for each song are displayed.

MainMenuActivity - the main menu is the launcher activity. It is the first one we see when the app is opened and it is here where the first songs are
loaded. The listeners for the two buttons and the switch are also handled here.

MapsActivity - this is the activity responsible for the API call to Google Maps. Here we get the current location data for the user and set how often
location requests are made. The lyrics are also randomly generated here and are set within the bounds of Bay Campus. On every locations request we also
check if the user is close enough to the lyric and the collection is handled if they are.

PopUpActivity - this activity is called throughout the application. It displays as a pop up over the main activity. It's text is different based on the
reason it was called.

Song - this class holds the information for the song object. It can return the name and text of the song for which it has information.

SongHistoryActivity - This class holds the recycler view containing all of the songs the user has gone through. It also handles the requests for additional
information by holding the listener which opens the full text of the selected song.

TextViewActivity - This activity displays the full text of a selected song. It also provides functionality to play the song in the YouTube app. This is accessed
by tapping on the buton on the right side of the screen.


