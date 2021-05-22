package com.mygdx.ttsgame.box2d;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.ttsgame.consts_enums.UserDataType;
import com.mygdx.ttsgame.ttsstages.MainStage;
import com.mygdx.ttsgame.utils.Constants;

public class EnemyUserData extends UserData {

    private Vector2 linearVelocity;
    private String[] textureRegions;

    public EnemyUserData(float width, float height, String[] textureRegions) {
        super(width, height);
        userDataType = UserDataType.ENEMY;
        if (MainStage.stadya == 1) {


            linearVelocity = Constants.ENEMY_LINEAR_VELOCITY2;
        } else {
            linearVelocity = Constants.ENEMY_LINEAR_VELOCITY;
        }
        this.textureRegions = textureRegions;
    }

    public void setLinearVelocity(Vector2 linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    public String[] getTextureRegions() {
        return textureRegions;
    }
}
