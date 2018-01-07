var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var mongoose = require('mongoose');

var MongoClient = require('mongodb').MongoClient;
var uri = "mongodb+srv://paradoxsyn:shadow101@melodi-qrcji.mongodb.net/test"

server.listen(8080, function(){
	console.log("Server is now running...");
});

MongoClient.connect(uri, function(err,db) {
	console.log("Connected to DB...");
	db.close();
});

io.on('connection',function(socket){
	console.log("Player connected");
	socket.emit('socketID',{ id: socket.id });
	socket.on('disconnect',function(){
		console.log("Player disconnected");
	})
})


