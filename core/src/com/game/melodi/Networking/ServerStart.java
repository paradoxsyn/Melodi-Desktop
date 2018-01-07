package com.game.melodi.Networking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.IIOException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by RabitHole on 12/30/2017.
 */


public class ServerStart {
    private Socket socket;
    private MongoClientURI uri;
    public MongoClient client;
    public MongoDatabase mongodb;
    public DB db;
    private FindIterable<Document> iterable;


    public ServerStart() {
        //If you want to start up a server
        mongoConnect();
    }


    public void mongoConnect(){
        uri = new MongoClientURI("mongodb://paradoxsyn:shadow101@melodi-shard-00-00-qrcji.mongodb.net:27017,melodi-shard-00-01-qrcji.mongodb.net:27017,melodi-shard-00-02-qrcji.mongodb.net:27017/test?ssl=true&replicaSet=Melodi-shard-0&authSource=admin");
        client = new MongoClient(uri);
        mongodb = client.getDatabase("users");

        db = client.getDB("users");
        //System.out.println("Connection success");

        //iterate through users to display all names
        /*iterable = db.getCollection("username").find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                System.out.println(document);
            }
        });*/

        //client.close();

    }

    public boolean checkExisting(){



        return false;
    }

    public void serverConnect() {
        //Socket IO

        try {
            socket = IO.socket("http://localhost:8080");
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void configSocket(){
        //Socket IO

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Gdx.app.log("SocketIO", "Connected");
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data  = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO","My ID "+id);
                }catch(JSONException j){
                    j.printStackTrace();
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data  = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketIO","New Player Connect: "+id);
                }catch(JSONException j){
                    j.printStackTrace();
                }
            }
        });
    }

}
