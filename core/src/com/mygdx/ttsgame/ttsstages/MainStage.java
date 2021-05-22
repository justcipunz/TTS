package com.mygdx.ttsgame.ttsstages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.ttsgame.GameEndScreen;
import com.mygdx.ttsgame.TTSGame;
import com.mygdx.ttsgame.ttsactors.Background;
import com.mygdx.ttsgame.ttsactors.EnemyActor;
import com.mygdx.ttsgame.ttsactors.GroundActor;
import com.mygdx.ttsgame.ttsactors.MainActor;
import com.mygdx.ttsgame.utils.BodyUtils;
import com.mygdx.ttsgame.utils.Constants;
import com.mygdx.ttsgame.utils.WorldUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainStage extends Stage implements ContactListener {

    // This will be our viewport measurements while working with the debug renderer
    private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;
    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;
    private Rectangle screenLeftSide;
    private Rectangle screenRightSide;
    private Vector3 touchPoint;
    private World world;
    private GroundActor groundActor;
    private MainActor mainActor;
    final TTSGame Game;
    public static int ochkov = 0;
    public static int stadya = 0;
    public static int schot = 0;

    public MainStage(final TTSGame gam) {
        super(new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT,
                new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)));
        setUpWorld();
        setupCamera();
        setupTouchControlAreas();
        this.Game = gam;
    }

    private void setUpWorld() {
        world = WorldUtils.createWorld();
        // Let the world now you are handling contacts
        world.setContactListener(this);
        setUpBackground();
        setUpGround();
        setUpRunner();
        createEnemy();
    }

    private void setUpBackground() {
        addActor(new Background());
    }


    private void setUpGround() {
        groundActor = new GroundActor(WorldUtils.createGround(world));
        addActor(groundActor);
    }

    private void setUpRunner() {
        mainActor = new MainActor(WorldUtils.createRunner(world));
        addActor(mainActor);
    }


    private void setupCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }

    private void setupTouchControlAreas() {
        touchPoint = new Vector3();
        screenLeftSide = new Rectangle(0, 0, getCamera().viewportWidth / 2, getCamera().viewportHeight);
        screenRightSide = new Rectangle(getCamera().viewportWidth / 2, 0, getCamera().viewportWidth / 2,
                getCamera().viewportHeight);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        Array<Body> bodies = new Array<Body>(world.getBodyCount());
        world.getBodies(bodies);

        for (Body body : bodies) {
            update(body);
        }

        // Fixed timestep
        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }

        //TODO: Implement interpolation

    }

    private void update(Body body) {
        if (!BodyUtils.bodyInBounds(body)) {
            if (BodyUtils.bodyIsEnemy(body) && !mainActor.isHit()) {

                ochkov++;
                schot=stadya*10+ochkov;

                if (ochkov < 10) {
                    createEnemy();

                }else {

                }
            }
            world.destroyBody(body);
        }

    }

    private void createEnemy() {
        EnemyActor enemy = new EnemyActor(WorldUtils.createEnemy(world));
        addActor(enemy);
    }


    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        // Need to get the actual coordinates
        translateScreenToWorldCoordinates(x, y);

        if (rightSideTouched(touchPoint.x, touchPoint.y)) {
            mainActor.jump();
        } else if (leftSideTouched(touchPoint.x, touchPoint.y)) {
            mainActor.dodge();
        }

        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (mainActor.isDodging()) {
            mainActor.stopDodge();
        }

        return super.touchUp(screenX, screenY, pointer, button);
    }

    private boolean rightSideTouched(float x, float y) {
        return screenRightSide.contains(x, y);
    }

    private boolean leftSideTouched(float x, float y) {
        return screenLeftSide.contains(x, y);
    }

    /**
     * Helper function to get the actual coordinates in my world
     * @param x
     * @param y
     */

    private void translateScreenToWorldCoordinates(int x, int y) {
        getCamera().unproject(touchPoint.set(x, y, 0));
    }

    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsEnemy(b)) ||
                (BodyUtils.bodyIsEnemy(a) && BodyUtils.bodyIsRunner(b))) {
            mainActor.hit();
            ochkov=0;
            stadya=0;
        } else if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsGround(b)) ||
                (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsRunner(b))) {
            mainActor.landed();

        }
    }

    @Override
    public void endContact(Contact contact) {
        if (mainActor != null) {
            if (mainActor.isHit())
                Game.setScreen(new GameEndScreen(Game));
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}