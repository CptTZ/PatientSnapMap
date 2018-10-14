"use strict";

var alertObj = $('.alert');

// Disabling form submissions if there are invalid fields
function set_up_vaild() {
    window.addEventListener('load', function () {
        var form = document.getElementById('login');
        document.getElementById('btn-login').addEventListener('click', function (event) {
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
    alertObj.stop().fadeTo('slow', 1);
    setTimeout(function () {
        alertObj.stop().fadeTo('slow', 0, function () {
            alertObj.hide();
        });
    }, 2500);
}

$("#login").submit(function (e) {
    alertObj.hide();
    e.preventDefault();

    var uid = $('#inputUid').val();
    var pass = $('#inputPwd').val();
    var pid = $('#inputPid').val();

    if (uid.indexOf('.') !== -1) {
        showErrMsg('Invalid username!');
        return;
    }

    // Send request to server
    $.ajax({
        url: 'http://127.0.0.1:10088/api/login',
        data: JSON.stringify({
            pid: pid,
            username: uid,
            password: pass
        }),
        type: 'POST',
        contentType: "application/json; charset=utf-8",
        dataType: "json",

        success: function (data) {
            if (data.code === 1) {
                console.log("Success");
                window.location = 'about:blank';
            } else {
                showErrMsg(data.msg + '! Please contact support.');
            }
        },

        error: function (error) {
            $('#btn-login').removeClass('disabled');
            showErrMsg('');
            console.log(error);
        }

    });

});
