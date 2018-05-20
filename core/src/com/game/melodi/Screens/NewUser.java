package com.game.melodi.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.game.melodi.Animations.Background;
import com.game.melodi.Melodi;
import com.game.melodi.Networking.ServerStart;
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
    Label l1,l2,l3,l4,l5,l6;
    Dialog dialog;
    Image bg,bg2;
    boolean next;
    private MongoCursor<Document> cursor;
    private MongoCollection<Document> collection;

    private BitmapFont font;
    private TextureAtlas buttonsAtlas; //** image of buttons **//
    private Skin buttonSkin,metalskin; //** images are used as skins of the button **//
    private TextButton.TextButtonStyle style;
    private TextField.TextFieldStyle tstyle;
    private Label.LabelStyle lstyle;
    private Window.WindowStyle wstyle;

    private String user;
    private ServerStart server;


    Preferences prefs = Gdx.app.getPreferences("Login");
    String id;


    public NewUser(final Melodi game){
        this.game = game;
        this.server = game.getServer();
        inputTable = new Table();
        metalskin = new Skin(Gdx.files.internal("metal-ui.json"));
        bg = new Image(new TextureRegion(new Texture("signuser.png")));
        bg2 = new Image(new TextureRegion(new Texture("glosser.png")));
        bg.setBounds(Gdx.graphics.getWidth()/14,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        bg2.setBounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

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
        //l3 = new Label("",lstyle);
        l4 = new Label("Username",lstyle);
        l5 = new Label("Password",lstyle);
        //l6 = new Label("Email",lstyle);

        inputTable.add(l4);
        inputTable.row();
        inputTable.add(name).width(200).height(100).fill().pad(20);
        inputTable.add(l1);
        inputTable.row().pad(20);
        inputTable.add(l5);
        inputTable.row();
        inputTable.add(password).width(200).height(100).fill();
        inputTable.add(l2);
        inputTable.row().pad(20);
        //inputTable.add(l6);
        //inputTable.row();
        //inputTable.add(email).width(200).height(100).fill();
        //inputTable.add(l3);
        inputTable.row().pad(20);
        inputTable.add(submit);

        inputTable.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);

        game.stage.addActor(bg2);
        game.stage.addActor(bg);
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
                if(!checkUser() && checkFields()) {
                    complete();
                }

            }
        });
    }

    public boolean checkUser(){
        DBCollection coll = server.db.getCollection("username");
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



        /*collection = server.mongodb.getCollection("username");
        cursor = collection.find().iterator();
        try{
            while(cursor.hasNext()){
                //System.out.println(cursor.next().toJson());
                l1.setText("this name already exists, please choose another");
                return true;
            }
        } finally{
            cursor.close();
        }

        return false;*/

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
        /*if(email.getText()=="" || !email.getText().contains("@")){
            l3.setText("Email is invalid");
            return false;
        }*/
        /*if(email.getText().contains("@") && !name.getText().isEmpty() && !password.getText().isEmpty()){
            l1.setText("");
            l2.setText("");
            l3.setText("");
            return true;
        }*/
        if(!name.getText().isEmpty() && !password.getText().isEmpty()){
            l1.setText("");
            l2.setText("");
            //l3.setText("");
            return true;
        }
        return false;
    }

    public void addRecord(String key, String value){

        server.mongodb.getCollection("username").insertOne(new Document().append(key,value)
        .append("password",password.getText()));
        //.append("email",email.getText()));

    }

    public boolean complete(){
         dialog = new Dialog("Confirm", metalskin, "dialog")
         {
            public void result(Object obj) {
                System.out.println("result "+obj);
                if(obj.equals(true)){
                    l1.setText("");
                    addRecord("username", name.getText());
                    id = name.getText();
                    prefs.putString("ID", id);
                    prefs.putBoolean("existing", true);
                    prefs.flush();
                    System.out.println(prefs.getBoolean("existing"));
                    System.out.println(prefs.getString("ID"));
                    dispose();
                    next = true;
                    game.setScreen(new Menu(game));
                }
                else{
                    next = false;
                    dialog.hide();
                    dialog.cancel();
                }
            }
        };
        dialog.text("Submit this info?");
        dialog.setSize(600,600);
        dialog.setSize(600,600);
        dialog.button("Yes", true); //sends "true" as the result
        dialog.button("No", false);  //sends "false" as the result
        dialog.show(game.stage);

        return next;
    }

    public void backButton(){
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            // Do something
            game.setScreen(new Menu(game));
        }
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
        backButton();
    }

    public void dispose(){
        metalskin.dispose();
        game.stage.clear();

    }
}
