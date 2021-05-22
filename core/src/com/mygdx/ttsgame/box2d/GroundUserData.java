package com.mygdx.ttsgame.box2d;

import com.mygdx.ttsgame.consts_enums.UserDataType;

public class GroundUserData extends UserData {

    public GroundUserData(float width, float height) {
        super(width, height);
        userDataType = UserDataType.GROUND;
    }

}
