package com.mygdx.ttsgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.mygdx.ttsgame.ttsscreens.MainScreen;

public class MenuScreen implements Screen {
    final TTSGame Game;
    public static Texture backgroundTexture1;
    public static Texture titleTexture;
    private Stage stage;
    private Skin skin;
    private Image gameover;
    private TextButton retry;


    public MenuScreen(final TTSGame gam) {

        Game = gam;
        backgroundTexture1 = new Texture("background.png");
        titleTexture = new Texture("title.png");
        stage=new Stage(new FillViewport(640, 360));
        skin=new Skin(Gdx.files.internal("skin/uiskin.json"));
        gameover = new Image();
        retry=new TextButton("Play",skin);

        retry.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                Game.setScreen(new MainScreen(Game));

            }
        });

        gameover.setPosition(320-gameover.getWidth(), 320- gameover.getHeight());
        retry.setSize(200,100);
        retry.setPosition(220,50);
        stage.addActor(retry);
        stage.addActor(gameover);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        Game.batch.begin();
        Game.batch.draw(backgroundTexture1, 0, 0, 800, 480);
        Game.batch.draw(titleTexture, 150, 200, 500, 250);
        Game.batch.end();
        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        Game.dispose();
    }
}
