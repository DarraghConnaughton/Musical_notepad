function displayMusic(){
//        var chorus =  "%%staffwidth 200\nX: 1 \nT: Cooley's \nM: 4/4 \nL: 1/8 \nK: Emin \n|:D2|EmEB{c}BA B2 EB|~B2 AB dBAG|\nDFDAD BDAD|FDAD dAFD| \nEmEBBA B2 EB:|";
        var chorus = getTuneDetails();
        ABCJS.renderAbc('notation', chorus);
}

function getTuneDetails(){
    return Android.sendData();
}