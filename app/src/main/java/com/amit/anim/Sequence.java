package com.amit.anim;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by AMIT JANGID on 2019-12-05.
 * Copyright (c) 2019 Amit Jangid. All rights reserved.
**/
public class Sequence
{
    private final int delay;
    private final int animId;
    private final int duration;
    private final int startOffset;

    private final Context context;
    private final Animation animation;
    private final Direction direction;

    private List<View> viewList = new ArrayList<>();

    public static class Builder
    {
        private Context context;
        private Animation animation;
        private ViewGroup viewGroup;
        private Direction direction = Direction.FORWARD;

        private static final int DEFAULT_DELAY = 0;
        private static final int DEFAULT_OFFSET = 100;
        private static final int DEFAULT_DURATION = 500;

        private int animId;
        private int delay = DEFAULT_DELAY;
        private int duration = DEFAULT_DURATION;
        private int startOffset = DEFAULT_OFFSET;

        Builder(ViewGroup viewGroup)
        {
            this.viewGroup = viewGroup;
        }

        public Builder offset(int offset)
        {
            this.startOffset = offset;
            return this;
        }

        public Builder duration(int duration)
        {
            this.duration = duration;
            return this;
        }

        public Builder delay(int delay)
        {
            this.delay = delay;
            return this;
        }

        public Builder flow(Direction direction)
        {
            this.direction = direction;
            return this;
        }

        public Builder anim(Context context, int animId)
        {
            this.animId = animId;
            this.context = context;

            return this;
        }

        public Builder anim(Context context, Animation animation)
        {
            this.context = context;
            this.animation = animation;

            return this;
        }

        public Sequence start()
        {
            return new Sequence(this);
        }
    }

    public static Builder origin(ViewGroup viewGroup)
    {
        return new Builder(viewGroup);
    }

    private Sequence(Builder builder)
    {
        this.delay = builder.delay;
        this.animId = builder.animId;
        this.context = builder.context;
        this.duration = builder.duration;
        this.direction = builder.direction;
        this.animation = builder.animation;
        this.startOffset = builder.startOffset;

        ViewGroup viewGroup = builder.viewGroup;

        // calling fetch child layout method
        fetchChildLayout(viewGroup);

        // calling arrange layouts method
        arrangeLayouts(viewList);

        // calling set animation method
        setAnimation();
    }

    private void fetchChildLayout(ViewGroup viewGroup)
    {
        int count = viewGroup.getChildCount();

        for (int i = 0; i < count; i++)
        {
            View view = viewGroup.getChildAt(i);

            if (view instanceof ViewGroup)
            {
                fetchChildLayout((ViewGroup) view);
            }
            else
            {
                if (view.getVisibility() == View.VISIBLE)
                {
                    view.setVisibility(View.INVISIBLE);
                    viewList.add(view);
                }
            }
        }
    }

    private void arrangeLayouts(List<View> viewList)
    {
        switch (direction)
        {
            case BACKWARD:

                Collections.reverse(viewList);

                break;

            case RANDOM:

                Collections.shuffle(viewList);

                break;
        }
    }

    private void setAnimation()
    {
        int count = viewList.size();

        for (int i = 0; i < count; i++)
        {
            final View view = viewList.get(i);
            final int offset = i * startOffset;

            // calling reset animation method
            resetAnimation(view);

            List<Animator> animatorList = new ArrayList<>();

            // calling and adding get start object animator method
            animatorList.add(getStartObjectAnimator(offset, view));

            if (animId != 0)
            {
                // calling and adding get res animator method
                animatorList.add(getResAnimator(context, animId, view));
            }
            else if (animation != null)
            {
                // calling and adding get res animator method
                animatorList.add(getResAnimator(context, animation.getAnimId(), view));
            }
            else
            {
                animatorList.add(ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1));
            }

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animatorList);
            animatorSet.setDuration(duration);

            if (delay == 0)
            {
                animatorSet.setStartDelay(i * startOffset);
            }
            else if (i == 0)
            {
                animatorSet.setStartDelay(delay);
            }
            else
            {
                animatorSet.setStartDelay((i * startOffset) + delay);
            }

            animatorSet.start();
        }
    }

    private void resetAnimation(View view)
    {
        /*ViewCompat.setAlpha(view, 1);
        ViewCompat.setScaleX(view, 1);
        ViewCompat.setAlpha(view, 1);
        ViewCompat.setScaleX(view, 1);
        ViewCompat.setScaleY(view, 1);

        ViewCompat.setTranslationX(view, 0);
        ViewCompat.setTranslationY(view, 0);

        ViewCompat.setRotation(view, 0);
        ViewCompat.setRotationX(view, 0);
        ViewCompat.setTranslationY(view, 0);*/

        view.setAlpha(1);
        view.setScaleX(1);
        view.setScaleY(1);

        view.setTranslationX(0);
        view.setTranslationY(0);

        view.setRotation(0);
        view.setRotationX(0);
        view.setTranslationY(0);
    }

    private ObjectAnimator getStartObjectAnimator(int offset, final View view)
    {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 0, 1);
        objectAnimator.setDuration(1).setStartDelay(offset);

        objectAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {

            }

            @Override
            public void onAnimationCancel(Animator animation)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {

            }
        });

        return objectAnimator;
    }

    private Animator getResAnimator(Context context, int animId, View view)
    {
        Animator animator = AnimatorInflater.loadAnimator(context, animId);
        animator.setTarget(view);

        return animator;
    }
}
