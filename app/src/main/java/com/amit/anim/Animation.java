package com.amit.anim;

import com.amit.R;

/**
 * Created by AMIT JANGID on 2019-12-05.
 * Copyright (c) 2019 Amit Jangid. All rights reserved.
**/
public enum Animation
{
    BOUNCE_IN(R.anim.bounce_in),
    FADE_IN(R.anim.fade_in),
    FADE_IN_DOWN(R.anim.fade_in_down),
    FADE_IN_UP(R.anim.fade_in_up),
    FADE_IN_LEFT(R.anim.fade_in_left),
    FADE_IN_RIGHT(R.anim.fade_in_right),
    ROTATE_IN(R.anim.rotate_in);

    private int animId;

    Animation(int animId)
    {
        this.animId = animId;
    }

    public int getAnimId()
    {
        return this.animId;
    }
}
