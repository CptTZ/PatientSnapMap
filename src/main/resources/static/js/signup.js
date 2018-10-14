"use strict";

var serverPrefix = 'http://127.0.0.1:8000';
var alertObj = $('.alert');
var form = document.getElementById('signup');

// Disabling form submissions if there are invalid fields
function set_up_vaild() {
    window.addEventListener('load', function () {
        document.getElementById('btn-signup').addEventListener('click', function (event) {
            if (form.checkValidity() === false) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    }, false);
}

set_up_vaild();

function showErrMsg(msg) {
    if (msg == '') {
    } else {
        $('#alertmsg').html(msg);
    }
    var alertObj = $('.alert');
    alertObj.stop().fadeTo('slow', 1);
    setTimeout(function () {
        alertObj.stop().fadeTo('slow', 0, function () {
            alertObj.hide();
        });
    }, 2500);
}

$('#signup').submit(function (e) {
    alertObj.hide();
    e.preventDefault();

    var uid = $('#inputUid').val();
    var pass1 = $('#input_pw1').val();
    var pass2 = $('#input_pw2').val();
    var dob = $('#inputDoB').val();
    var f_name = $('#inputFN').val();
    var l_name = $('#inputLN').val();
    var nickname = $('#inputNick').val();
    var email = $('#inputEM').val();

    // Valid on password
    if (pass1 !== pass2) {
        showErrMsg('Password does not match!');
        return;
    }

    // Valid on DoB
    var trueDob = moment(dob, 'M/D/YYYY', true);
    if (trueDob.isValid() === false) {
        showErrMsg('DoB wrong format!');
        $('#inputDoB').focus();
        return;
    } else if (moment(new Date()).isBefore(trueDob)) {
        showErrMsg('DoB cannot in the furture!');
        $('#inputDoB').focus();
        return;
    }

    // Check gender
    var gender = 2;
    if ($('#in_male').is(":checked")) {
        gender = 0;
    } else if ($('#in_female').is(":checked")) {
        gender = 1;
    }

    // Send request to server
    $.ajax({
        url: serverPrefix + '/register',
        type: 'POST',
        data: 'userID=' + uid + '&password=' + pass2 +
            '&nickname=' + nickname + '&firstname=' + f_name +
            '&lastname=' + l_name + '&gender=' + gender +
            '&email=' + email + '&DOB=' + trueDob.format(),

        success: function (data) {
            var e = data;
            console.log(e);
            if (e.status == 200) {
                // Good reg
                $('#regSuccess').modal('show');
            } else if (e.status == 404) {
                showErrMsg('Duplicate User ID.');
            } else {
                showErrMsg('');
            }
        },

        error: function (error) {
            $('#btn-login').removeClass('disabled');
            showErrMsg('');
            console.log(error);
        },

    });
});
