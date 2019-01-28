const URI = "https://sarlanga-tournament.herokuapp.com/v3";
//const URI = "http://localhost:8080/v3";

function cuenta(){
	hideByType("submenu");
	get("/account");
}

function salas(){
	hideByType("submenu");
	get("/rooms");
	toggle("salas");
}

function salaInfo(num){
	get("/rooms/"+num);
}

function salaEntrar(num){
	get("/rooms/"+num+"/enter");
}

function salaCrear(num){
	get("/rooms/create?essence="+num);
}

function salaIniciar(){
	get("/rooms/start");
}

function salaEliminar(){
	get("/rooms/delete");
}


function equipo(){
	hideByType("submenu");
	get("/team")
	toggle("equipo");
}

function equipoAgregarCriatura(idCriatura, line, position){
	console.log(idCriatura+" "+line+" "+position)
	get("/team/add/"+idCriatura+"/"+line+"/"+position);	
}
function equipoQuitarCriatura(idCriatura){
	get("/team/remove/"+idCriatura);	
}

function equipoAccion(metodo, idCriatura, idCarta){
	get("/team/"+metodo+"/"+idCriatura+"/"+idCarta);	
}

function equipoConfirmar(){
	get("/team/confirm");
}

function partida(){
	hideByType("submenu");
	get("/match");
	toggle("partida");
}

function partidaEstado(){
	get("/match/status");	
}

function partidaMensajes(){
	get("/match/messages");	
}

function partidaEjecutarAccion(idAccion, idObjetivo){
	get("/match/action/"+idAccion+"/"+idObjetivo);	
}

function hideByType(type){
	var x = document.getElementsByClassName(type);
	var i;
	for (i = 0; i < x.length; i++) { 
		x[i].style.display = "none";
	}
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
	req.setRequestHeader('Access-Control-Allow-Headers', '*');
	req.setRequestHeader('Access-Control-Allow-Origin', '*');
	req.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	req.withCredentials = true;
	req.send();
	req.onreadystatechange=(e)	=>	{
	    if (req.readyState == 4 && req.status == 200) {
	    	output(req.responseText);
	    }
	}
}

