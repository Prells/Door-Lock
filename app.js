/*
 * Please see the included README.md file for license terms and conditions.
 */


// This file is a suggested starting place for your code.
// It is completely optional and not required.
// Note the reference that includes it in the index.html file.


/*jslint browser:true, devel:true, white:true, vars:true */
/*global $:true, intel:false app:false, dev:false, cordova:false */


// For improved debugging and maintenance of your app, it is highly
// recommended that you separate your JavaScript from your HTML files.
// Use the addEventListener() method to associate events with DOM elements.
// For example:

// var el ;
// el = document.getElementById("id_myButton") ;
// el.addEventListener("click", myEventHandler, false) ;



// The function below is an example of a way to "start" your app. if you convert
// your app to a Cordova app, this function will call the standard Cordova
// "hide splashscreen" function. If this is a web app that does not use Cordova
// this function is quietly ignored and does nothing.

// You can add other code to this function or add additional functions that are
// triggered by the same event. The app.Ready event used here is created by the
// init-dev.js file. It serves as a unifier for a variety of "ready" events.
// See the init-dev.js file for more details. If you prefer to use other events
// to start your app, you can use those in addition to, or instead of this event.

// NOTE: change "dev.LOG" in "init-dev.js" to "true" to enable some console.log
// messages that can help you debug app initialization issues.

function onAppReady() {
    if( navigator.splashscreen && navigator.splashscreen.hide ) {   // Cordova API detected
        navigator.splashscreen.hide() ;
    }
}
document.addEventListener("app.Ready", onAppReady, false) ;

var webSocket;
/**
* Handles the log in to the server
**/
function connect(){
    //var WebSocket;
   
 //   document.getElementById('myDiv').innerHTML='connecting';
    var user = document.getElementById('name').value;
    var pass1 = document.getElementById('fpass').value;
    var pass2 = document.getElementById('conpass').value;
    if(pass1 !== pass2){
        alert('Passwords do not match');
        return;
    }
    user = 'user: ' + user;
    pass1 = 'password: ' + pass1;
    webSocket.send(user);
    webSocket.send(pass1);
    webSocket.onmessage = function(e){
        if(e.data === 'confirmed'){
           hide();
           deviceShow();
           document.getElementById('myDiv').innerHTML=e.data;
        }
        else{
            document.getElementById('myDiv').innerHTML=e.data;
            return;
        }
    };
}
/**
* Handles the initial connection to the server
**/
function popup(){
    document.getElementById('form').style.display = "block";
    webSocket = new WebSocket("ws://localhost:8080/HomeSec/server");
    //webSocket.send("Connected");
    webSocket.onopen = function(){
        webSocket.send('Ping');
    };
}
function hide(){
    document.getElementById('form').style.display = "none";
    document.getElementById('loginform').style.display = "none";
}
function deviceHide(){
    document.getElementById('device').style.display = "none";
}

function deviceShow(){
    document.getElementById('device').style.display = "block";
}
/**
* Sends the lock signal to the server
**/
function lock(){
   // var pass = prompt('Please enter your password', 'Password');
//    webSocket.send(pass);
 //   webSocket.onmessage = function(e){
   //     if(e.data === 'confirmed'){
            webSocket.send('lock');
     //   }
//    };   
}
/**
* Sends the unlock signal to the server
**/
function unlock(){
 //   var pass = prompt('Please enter your password', 'Password');
   // webSocket.send(pass);
  //  webSocket.onmessage = function(e){
    //    if(e.data === 'confirmed'){
            webSocket.send('unlock');
      //  }
    //};
} 

function password(){    document.getElementById('passform').style.display = "block";
    //document.getElementById('submit').style.display = "block";
}

function setTime(){
   // webSocket.send(form.pass.value);   
    var time = document.getElementById('settime').value;
    webSocket.send("set: " + time);
}
/**
* Handles the connection to the server when clicking the * log in button
**/
function login(){
    document.getElementById('loginform').style.display = "block";
    webSocket = new WebSocket("ws://localhost:8080/HomeSec/server");
  //  webSocket.
    //webSocket.send("Connected");
    webSocket.onopen = function(){
        webSocket.send('Ping');
    };
}
/**
* Handles logging in the user
**/
function loginUser(){
    var user = "login user: " + document.getElementById('username').value;
    var pass = "login password: " + document.getElementById('logpass').value;
    webSocket.send(user);
    webSocket.onmessage = function(e){
        if(e.data === 'Username not found'){
            alert('Username not found');
            return;
        }
    };
    webSocket.send(pass);
    webSocket.onmessage = function(e){
        if(e.data === 'confirmed'){
            deviceShow();
            hide();
            document.getElementById('myDiv').innerHTML = e.data;
        }
        else{
            document.getElementById('myDiv').innerHTML = e.data;
        }
    };
}

function messages(){
    webSocket.send('get messages');
}
