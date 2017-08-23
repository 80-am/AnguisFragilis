window.onload = function(){
var score = document.getElementById('counter');
score = 0;
var blip = new Audio('munch.mp3');
var end = new Audio('gameOver.wav')

var doSound = true;

// setting game board
var rand = function (min, max) {
  k = Math.floor(Math.random() * (max - min) + min);
  return (Math.round( k / s) * s);
}

var newA = function () {
  a = [rand(0, innerWidth),rand(0, innerHeight)];
  console.log("Score: " + score);
  counter.innerHTML = score;
  score = score +1;
  if (doSound) {
    blip.play();

  }
},
	newB = function () {
    sBody = [{x: 500,y: 50}]; // set start position?
  }
var sC = document.getElementById('startGame'),
	g = sC.getContext('2d'),
	sBody = null,
	d = 1, // start direction
	a = null, // size of canvas
	s = 50; newB(); newA(); // size of player and food width/height
sC.width = innerWidth;
sC.height = innerHeight;

setInterval(function(){
	if (a[0] + s >= sC.width || a[1] + s >= sC.height) newA();
  g.clearRect(0,0,sC.width,sC.height);
	g.fillStyle = "#9E5EB0"; // change color of food
  g.fillRect(...a , s, s); // printing food what is ...a?
	g.fillStyle = "#A5FFD6"; // change color of player

  // check to see if you are eating yourself and dying
	sBody.forEach(function(el, i){
		if (el.x == sBody[sBody.length - 1].x && el.y == sBody[sBody.length - 1].y &&
      i < sBody.length - 1) {
        sBody.splice(0,sBody.length-1), sBody = [{x:0,y:0}], d = 1;
        if (doSound) {
          end.play();
        }
        score = 1;
        }
  });

  // adding body
	var m = sBody[0], f = {x: m.x,y: m.y}, l = sBody[sBody.length - 1];
	if (d == 1)  f.x = l.x + s, f.y = Math.round(l.y / s) * s;
	if (d == 2) f.y = l.y + s, f.x = Math.round(l.x / s) * s;
	if (d == 3) f.x = l.x - s, f.y = Math.round(l.y / s) * s;
	if (d == 4) f.y = l.y - s, f.x = Math.round(l.x / s) * s;
	sBody.push(f);
	sBody.splice(0,1);
	sBody.forEach(function(pob, i){ // position of body
		if (d == 1) if (pob.x > Math.round(sC.width / s) * s) pob.x = 0; // 1 = right direction
		if (d == 2) if (pob.y > Math.round(sC.height / s) * s) pob.y = 0;  // 2 = down direction
		if (d == 3) if (pob.x < 0) pob.x = Math.round(sC.width / s) * s;  // 3 = left direction
		if (d == 4) if (pob.y < 0) pob.y = Math.round(sC.height / s) * s;  // 4 = up direction
		if (pob.x == a[0] && pob.y == a[1]) newA(), sBody.unshift({
      x: f.x - s, y:l.y
    })
		g.fillRect(pob.x, pob.y, s, s);
	});
}, 60);

// keyboard controller
// movement with arrowbuttons and mute
onkeydown = function (e) {
  console.log('jfef ehfil fhiler');
	var k = e.keyCode;
	if ([38,39,40,37].indexOf(k) >= 0)
		e.preventDefault();
	if (k == 39 && d != 3) d = 1;
	if (k == 40 && d != 4) d = 2;
	if (k == 37 && d != 1) d = 3;
	if (k == 38 && d != 2) d = 4;

  //muting with key 'm'
  if (k == 77) {
    doSound = !doSound;
  }

  //'escape' for home page
  if (k == 27){
	document.location.href = "/";
  }


};

}
