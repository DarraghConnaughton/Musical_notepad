createChorus = function (){
//                var recordingName="";
//                var timeSignature="";
//                var timeSignature="";
//                var keySignature="";
//                var L="";
//                var noteProgression="";
//                var chorus=;
//
        var recordingName = "Cooley";
        var timeSignature = "4/4";
        var keySignature = "Emin";
        var L= "1/8";
        var noteProgression = "|:D2|EmEB{c}BA B2 EB|~B2 AB dBAG|\nDFDAD BDAD|FDAD dAFD| \nEmEBBA B2 EB:|";
        var chorus = "%%staffwidth 200\nX: 1 \nT: " + recordingName + " \nM: " + timeSignature + " \nL: " + L + "\nK: " + keySignature + "\n" + noteProgression;
        return chorus;
}