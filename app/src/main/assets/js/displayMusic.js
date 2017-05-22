function displayMusic(){
        var chorus = getTuneDetails();
        ABCJS.renderAbc('notation', chorus);
}
function getTuneDetails(){
    return Android.sendData();
}
function getABCTuneDetails(){
    return Android.sendABCData();
}
function playAudio(){
    var piano = new Instrument('piano');
    piano.play({tempo:200},chordProgression());
}

function chordProgression(){
    return Android.chordProgression();
}