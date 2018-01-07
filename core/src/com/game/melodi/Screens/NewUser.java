package com.game.melodi.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.game.melodi.Melodi;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

/**
 * Created by RabitHole on 12/31/2017.
 */

public class NewUser extends ScreenAdapter {

    Melodi game;
    Table inputTable;
    TextButton submit;
    TextField name,password,email;
    int w,h;
    Label l1,l2,l3;

    private BitmapFont font;
    private TextureAtlas buttonsAtlas; //** image of buttons **//
    private Skin buttonSkin,metalskin; //** images are used as skins of the button **//
    private TextButton.TextButtonStyle style;
    private TextField.TextFieldStyle tstyle;
    private Label.LabelStyle lstyle;
    private Window.WindowStyle wstyle;

    MongoCollection<Document> players = game.server.mongodb.getCollection("username");
    private FindIterable<Document> iterable;
    private String user;

    Preferences prefs = Gdx.app.getPreferences("Login");
    String id;


    public NewUser(final Melodi game){
        this.game = game;
        inputTable = new Table();
        metalskin = new Skin(Gdx.files.internal("metal-ui.json"));

        w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();


        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/IndieFlower.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        Gdx.input.setInputProcessor(game.stage); //** stage is responsive **//

        buttonsAtlas = new TextureAtlas("menu.pack"); //**button atlas image **//
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas); //** skins for on and off **//
        fontParameter.size = 54;
        font = fontGenerator.generateFont(fontParameter);

        style = new TextButton.TextButtonStyle(); //** Button properties **//
        style.up = buttonSkin.getDrawable("buttonOff");
        style.down = buttonSkin.getDrawable("buttonOn");

        style.font = font;

        tstyle = metalskin.get(TextField.TextFieldStyle.class);
        tstyle.font = font;

        lstyle = metalskin.get(Label.LabelStyle.class);
        lstyle.font = font;

        wstyle = metalskin.get(Window.WindowStyle.class);

        name = new TextField("",metalskin);
        password = new TextField("",metalskin);
        email = new TextField("",metalskin);
        submit = new TextButton("Submit",style);

        l1 = new Label("",lstyle);
        l2 = new Label("",lstyle);
        l3 = new Label("",lstyle);

        inputTable.add(name).width(200).height(100).fill().pad(20);
        inputTable.add(l1);
        inputTable.row().pad(20);
        inputTable.add(password).width(200).height(100).fill();
        inputTable.add(l2);
        inputTable.row().pad(20);
        inputTable.add(email).width(200).height(100).fill();
        inputTable.add(l3);
        inputTable.row().pad(20);
        inputTable.add(submit);

        inputTable.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);

        game.stage.addActor(inputTable);

        submit();


    }

    public void submit(){


        submit.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//
                user = name.getText();
                return true;

            }

            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("my app", "Released");
                System.out.println(user);
                if(!checkUser()) {
                    l1.setText("");
                    addRecord("username", name.getText());
                    id = name.getText();
                    prefs.putString("ID",id);
                    prefs.putBoolean("existing",true);
                    prefs.flush();
                    System.out.println(prefs.getBoolean("existing"));
                    System.out.println(prefs.getString("ID"));

                    complete();
                }

            }
        });
    }

    public boolean checkUser(){
        DBCollection coll = game.server.db.getCollection("username");
        BasicDBObject query = new BasicDBObject("username",user);
        DBCursor cursor = coll.find(query);

        try{
            while(cursor.hasNext()){
                l1.setText("This name already exists, please choose another");
                return true;
            }
        }finally{
            cursor.close();
        }
        return false;
    }

    public boolean checkFields(){
        if(name.getText()==""){
            l1.setText("Username cannot be empty");
            return false;
        }
        if(password.getText()==""){
            l2.setText("Password cannot be empty");
            return false;
        }
        if(email.getText()=="" || !email.getText().contains("@")){
            l3.setText("Email is invalid");
            return false;
        }
        if(email.getText().contains("@") && !name.getText().isEmpty() && !password.getText().isEmpty()){
            l1.setText("");
            l2.setText("");
            l3.setText("");
            return true;
        }
        return false;
    }

    public void addRecord(String key, String value){
        players.insertOne(new Document().append(key,value)
        .append("password",password.getText())
        .append("email",email.getText()));

    }

    public void complete(){
        Dialog dialog = new Dialog("Warning", metalskin, "dialog") {
            public void result(Object obj) {
                System.out.println("result "+obj);
            }
        };
        dialog.text("Are you sure you want to quit?");
        dialog.button("Yes", true); //sends "true" as the result
        dialog.button("No", false);  //sends "false" as the result
        dialog.show(game.stage);
    }


    @Override
    public void resize(int width, int height) {
        game.viewPort.update(width, height, true);
        game.stage.getCamera().update();
    }

    @Override
    public void render(float delta){
        //elapsedTime += Gdx.graphics.getDeltaTime();
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.stage.act();
        game.stage.draw();
    }

    public void dispose(){
        game.stage.dispose();
    }
}
