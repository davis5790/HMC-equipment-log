EnableSubmit = function(val)
{
    var email= document.getElementById("emailField")

    if (val.checked == true) {
        email.className = "form-control";
    } else {
        email.className = "";
    }
}
