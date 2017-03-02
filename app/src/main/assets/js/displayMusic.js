function displayMusic(){
//        var chorus =  "%%staffwidth 200\nX: 1 \nT: Cooley's \nM: 4/4 \nL: 1/8 \nK: Emin \n|:D2|EmEB{c}BA B2 EB|~B2 AB dBAG|\nDFDAD BDAD|FDAD dAFD| \nEmEBBA B2 EB:|";
        var chorus = getTuneDetails();
        ABCJS.renderAbc('notation', chorus);
}

function getTuneDetails(){
    return Android.sendData();
}
//    var recordingName = "Cooley;
//    var timeSignature = "4/4";
//    var keySignature = "Emin";
//    var L= "1/8";
//    var noteProgression = "|:D2|EmEB{c}BA B2 EB|~B2 AB dBAG|\nDFDAD BDAD|FDAD dAFD| \nEmEBBA B2 EB:|";
//    var chorus = "%%staffwidth 200\nX: 1 \nT: " + recordingName + " \nM: " + timeSignature + " \nL: " + L + "\nK: " + keySignature + "\n" + noteProgression;
//    ABCJS.renderAbc('notation', chorus);

//        System.out.println("****************************88");

//	var chorus = '%%staffwidth 500\nX: 1\nT: Chorus\nV: T1 clef=treble name="Soprano"\nV: T2 clef=treble name="Alto"\nV: B1 clef=bass name="Tenor"\nV: B2 clef=bass name="Bass"\nL:1/8\nK:G\nP:First Part\n[V: T1]"C"ed"Am"ed "F"cd"G7"gf |\n[V: T2]GGAA- A2BB |\n[V: B1]C3D- DF,3 |\n[V: B2]C,2A,,2 F,,2G,,2 |';


//    System.out.println("****************************88");

//
//function createChorus(){
//    var recordingName = "Cooley;
//    var timeSignature = "4/4";
//    var keySignature = "Emin";
//    var L= "1/8";
//    var noteProgression = "|:D2|EmEB{c}BA B2 EB|~B2 AB dBAG|\nDFDAD BDAD|FDAD dAFD| \nEmEBBA B2 EB:|";
//    var chorus = "%%staffwidth 200\nX: 1 \nT: " + recordingName + " \nM: " + timeSignature + " \nL: " + L + "\nK: " + keySignature + "\n" + noteProgression;
//}

//function hello(){
//    System.out.println("HEEEEEELLLLLLLOOOOOOOO");
//}