//const URI = "https://sarlanga-tournament.herokuapp.com/v3";
const URI = "http://localhost:8080/v3";

function inicio() {
	get("/");	
}

function cuenta(){
	get("/account");
}

function infoSala(num){
	get("/rooms/"+num);
}

function crearSala(num){
	get("/rooms/create?essence="+num);
}

function salas(){
	get("/rooms");
	toggle("salas");
}


function hide(element){
	var x = document.getElementById(element);
	x.style.display = "none";	
}


function show(element){
	var x = document.getElementById(element);
	x.style.display = "block";	
}

function toggle(element){
	var x = document.getElementById(element);
	if (x.style.display === "none") {
		show(element);
	} else {
		hide(element);
	}
}

function output(mensaje) {
	var output = document.getElementById('output');
	output.innerHTML = mensaje;	
}

function get(url){
	url = URI + url;
	const req = new XMLHttpRequest();
	req.open("GET", url);
	req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	req.withCredentials = true;
	req.send();
	req.onreadystatechange=(e)	=>	{
	    if (req.readyState == 4 && req.status == 200) {
	    	output(req.responseText);
	    }
	}
	console.log(document.cookie);
	
}
