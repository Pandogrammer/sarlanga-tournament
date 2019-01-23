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
	const Http = new XMLHttpRequest();
	Http.open("GET", url);
	Http.send();
	Http.onreadystatechange=(e)	=>	{
		output(Http.responseText);
	}
}