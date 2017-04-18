function displayMusic(){
        var chorus = getTuneDetails();
        ABCJS.renderAbc('notation', chorus);
}

function getTuneDetails(){
    return Android.sendData();
}

function chordProgression(){
    return Android.chordProgression();
}