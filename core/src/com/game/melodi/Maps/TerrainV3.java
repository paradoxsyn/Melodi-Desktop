package com.game.melodi.Maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.game.melodi.Animations.Background;

import java.util.Arrays;

import static com.game.melodi.Melodi.HEIGHT;
import static com.game.melodi.Melodi.WIDTH;
import static com.game.melodi.Melodi.player;

/**
 * Created by Paradox on 5/24/2017.
 */

public class TerrainV3 {
    int hillVerts;
    int numIndices;
    int maxPhysics;

    SpriteBatch batch;
    Texture img;
    ShaderProgram shader;

    private final int songLen = player.player.getInfo().length;
    private final int maxHillVerts = 40000;
    private final int groundStep = 20;

    public Vector2[] xyPoints;
    Vector2[] groundVerts;
    Vector2[] groundTexCoords;
    Vector2[] grassVerts;
    Vector2[] grassTexCoords;

    //Textures
    Body body;
    World world;
    Fixture fixture;

    Sprite groundSprite;
    Sprite grassSprite;

    String vshade;
    String fshade;
    Mesh mesh;


    public TerrainV3(){
        body = null;
        maxPhysics = Gdx.graphics.getWidth()/groundStep;
        groundSprite = new Sprite(new Texture(Gdx.files.internal("ground.png")));
        grassSprite = new Sprite(new Texture(Gdx.files.internal("grass.png")));


    }

    public void init(){

        BodyDef bd = new BodyDef();
        world = new World(new Vector2(WIDTH,HEIGHT),true);
        bd.position.set(0,0);
        bd.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bd);
        vshade = Gdx.files.internal("shaders/defaultvertex.glsl").readString();
        fshade = Gdx.files.internal("shaders/defaultfrag.glsl").readString();

        xyPoints = new Vector2[maxHillVerts];
        groundVerts = new Vector2[maxHillVerts];
        groundTexCoords = new Vector2[maxHillVerts];
        grassVerts = new Vector2[maxHillVerts];
        grassTexCoords = new Vector2[maxHillVerts];

        shader = new ShaderProgram(vshade,fshade);
        mesh = new Mesh( true, 6, 0,
                new VertexAttribute( VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
                new VertexAttribute( VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0" ) );

        generateXY();
        //generateMESH();

        System.out.println("Generating XY: " + Arrays.toString(xyPoints));
        System.out.println("Mesh verts: "+Arrays.toString(grassVerts) + "\n" + "Mesh tex" + Arrays.toString(grassTexCoords));
    }

    public void destroy(){

    }

    public Vector2[] generateXY(){
        float yOffset = 350f;
        int index = 0;
        for(int i = 0; i < songLen; i++){
            //xyPoints[i].set(index,yOffset - MathUtils.sin(i) * 60 * i / 100.f);
            xyPoints[i] = new Vector2();
            xyPoints[i].x = index;
            xyPoints[i].y = yOffset - MathUtils.sin(i) * 60 * i / 100.f;
            index+=groundStep;
        }

        return xyPoints;
    }

    public void generateMESH(){
        hillVerts=0;
        //Vector2[] p = xyPoints.clone();
        for(int i=0;i<songLen;i++){

            grassVerts[i] = new Vector2();
            groundVerts[i] = new Vector2();
            grassTexCoords[i] = new Vector2();
            groundTexCoords[i] = new Vector2();

            //p[i] = xyPoints[i];
            //p[i] = new Vector2();
            grassVerts[hillVerts].x = xyPoints[i].x;
            grassVerts[hillVerts].y = xyPoints[i].y-12f;
            grassTexCoords[hillVerts++].x = xyPoints[i].x / grassSprite.getWidth();
            grassTexCoords[hillVerts++].y = 1f;
            grassVerts[hillVerts].x = xyPoints[i].x;
            grassVerts[hillVerts].y = xyPoints[i].y;
            grassTexCoords[hillVerts++].x = xyPoints[i].x/grassSprite.getWidth();
            grassTexCoords[hillVerts++].y = 0f;
        }

        hillVerts=0;

        for(int i = 0;i<songLen;i++) {
            //p[i] = xyPoints[i];
            groundVerts[hillVerts].x = xyPoints[i].x;
            groundVerts[hillVerts].y = 0;
            groundTexCoords[hillVerts].x = xyPoints[i].x / groundSprite.getWidth();
            groundTexCoords[hillVerts].y = 0;
            groundVerts[hillVerts].x = xyPoints[i].x - 5f;
            groundVerts[hillVerts].y = xyPoints[i].y - 5f;
            groundTexCoords[hillVerts].x = xyPoints[i].x / groundSprite.getWidth();
            groundTexCoords[hillVerts].y = xyPoints[i].y / groundSprite.getHeight();
        }
    }

    public void updatePhy(int startIndex, int endIndex){
        EdgeShape shape = new EdgeShape();
        for(int i=startIndex;i<endIndex;i++){
            Vector2 p1 = new Vector2(xyPoints[i].x/32,xyPoints[i].y/32);
            Vector2 p2 = new Vector2(xyPoints[i+1].x/32,xyPoints[i+1].y/32);
            shape.set(p1,p2);
            numIndices++;
        }
    }

    public void render(){
        //bind texture
        //enable vertex
    }

    public void update(float dt){
        //stage.getcam.getX - .5f;
        }
}

