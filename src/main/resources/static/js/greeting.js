function SSTcheck() {
    const dropdown = document.getElementById("mySelect");
    const check = document.getElementById("SST");
    const val = dropdown.value;

    if (val == "Drager" || val == "G5" || val == "C1"){
        check.hidden = false;
    } else {
        check.hidden = true;
    }
}

addNote = function(val) {
    var note= document.getElementById("note")

    if (val.checked == true) {
        note.className = "form-control";
    } else {
        note.className = "";
    }
}

