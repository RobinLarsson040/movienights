const CLIENT_ID = "198522656770-v545sbbit4ce15fjp6po4qem7ichp7b5.apps.googleusercontent.com";


myStorage = window.localStorage;

function start() {

    if(myStorage.getItem("Authorization") !== null){
        $('#loginForm').attr('style', 'display: none');
    }

    gapi.load('auth2', function () {
        auth2 = gapi.auth2.init({
            client_id: CLIENT_ID,
            scope: "https://www.googleapis.com/auth/calendar.events"
        });
    });
}

$('#signinButton').click(function () {
    if(localStorage.getItem("Authorization") !== null){
        auth2.grantOfflineAccess().then(signInCallback);
    }else{
        alert("YOU MUST SIGN IN BEFORE LINKING YOUR GOOGLE ACCOUNT")
    }

});

$('#loginForm').submit(function (e) {
    e.preventDefault();
    console.log("LOGIN");
    $.ajax({
        type: "POST",
        url: 'http://localhost:8080/login',
        data: JSON.stringify({
            email: $("#email").val(),
            password: $("#password").val()
        }),
        contentType: 'application/json',
        processData: false,
        success: function (data, textStatus, request) {
            console.log(request.getResponseHeader('Authorization'));
            console.log(request.getResponseHeader('UserID'));

            let authorization = request.getResponseHeader("Authorization");
            let userID = request.getResponseHeader("UserID");

            localStorage.setItem('Authorization', authorization);
            localStorage.setItem('UserID', userID);
            $('#loginForm').attr('style', 'display: none');

        },
        error: function (request, textStatus, errorThrown) {
            alert("WRONG USERNAME OR PASSWORD");
        }
    });
});

$('#registerForm').submit(function (e) {
    e.preventDefault();
    console.log("REGISTER");
    $.ajax({
        type: "POST",
        url: 'http://localhost:8080/users',
        data: JSON.stringify({
            email: $("#emailRegister").val(),
            password: $("#passwordRegister").val(),
            firstName: $("#firstNameRegister").val(),
            lastName: $("#lastNameRegister").val()
        }),
        contentType: 'application/json',
        processData: false,
        success: function (data, textStatus, request) {

            $('#registerForm').attr('style', 'display: none');

        },
        error: function (request, textStatus, errorThrown) {
            alert(textStatus);
            alert(errorThrown);
        }
    });
});

function signInCallback(authResult) {
    console.log('authResult', authResult);
    if (authResult['code']) {
        $('#signinButton').attr('style', 'display: none');
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/storeauthcode',
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
                'Authorization': localStorage.getItem("Authorization")
            },
            contentType: 'application/octet-stream; charset=utf-8',
            success: function (result) {
            },
            processData: false,
            data: authResult['code']
        });
    } else {
        // There was an error.
    }
}