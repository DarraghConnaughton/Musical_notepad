function displayMusic(){
        var chorus = getTuneDetails();
        ABCJS.renderAbc('notation', chorus);
}

function getTuneDetails(){
    return Android.sendData();
}

function playAudio(){
    var piano = new Instrument('piano');
    piano.play(chordProgression());
}

function chordProgression(){
    return Android.chordProgression();
}