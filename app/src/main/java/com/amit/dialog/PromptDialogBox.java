package com.amit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amit.R;
import com.amit.anim.AnimLoader;
import com.amit.utilities.Utils;

/**
 * Created by Amit Jangid on 22,May,2018
 **/
public class PromptDialogBox extends Dialog
{
    public static final int DIALOG_TYPE_INFO = 0;
    public static final int DIALOG_TYPE_HELP = 1;
    public static final int DIALOG_TYPE_ERROR = 2;
    public static final int DIALOG_TYPE_SUCCESS = 3;
    public static final int DIALOG_TYPE_WARNING = 4;

    public static final int DIALOG_TYPE_DEFAULT = DIALOG_TYPE_INFO;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    private static final int DEFAULT_RADIUS = 6;
    private AnimationSet mAnimIn, mAnimOut;

    private View mDialogView;
    private TextView mTitleTv, mContentTv, mPositiveBtn;
    private onPositiveListener mOnPositiveListener;

    private int mDialogType;
    private boolean mIsShowAnim;
    private CharSequence mTitle, mContent, mBtnText;

    public PromptDialogBox(Context context)
    {
        this(context, 0);
    }

    public PromptDialogBox(Context context, int theme)
    {
        super(context, R.style.PromptDialogTheme);
        init();
    }

    private void init()
    {
        mAnimIn = AnimLoader.getInAnimation(getContext());
        mAnimOut = AnimLoader.getOutAnimation(getContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initView();
        initListener();
    }

    private void initView()
    {
        View contentView = View.inflate(getContext(), R.layout.layout_prompt_dialog, null);
        setContentView(contentView);
        resizeDialog();

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mTitleTv = contentView.findViewById(R.id.tvTitle);
        mContentTv = contentView.findViewById(R.id.tvContent);
        mPositiveBtn = contentView.findViewById(R.id.btnPositive);

        View llBtnGroup = findViewById(R.id.llBtnGroup);
        ImageView logoIv = contentView.findViewById(R.id.logoIv);
        logoIv.setBackgroundResource(getLogoResId(mDialogType));

        LinearLayout topLayout = contentView.findViewById(R.id.topLayout);
        ImageView triangleIv = new ImageView(getContext());

        triangleIv.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(getContext(), 10)));

        triangleIv.setImageBitmap(createTriangle(
                (int) (Utils.getScreenSize(getContext()).x * 0.9),
                Utils.dp2px(getContext(), 10)));

        topLayout.addView(triangleIv);
        setBtnBackground(mPositiveBtn);
        setBottomCorners(llBtnGroup);

        int radius = Utils.dp2px(getContext(), DEFAULT_RADIUS);
        float[] outerRadii = new float[]{radius, radius, radius, radius, 0, 0, 0, 0};

        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);

        shapeDrawable.getPaint().setColor(getContext().getResources().getColor(getColorResId(mDialogType)));
        LinearLayout llTop = findViewById(R.id.llTop);
        llTop.setBackground(shapeDrawable);

        mTitleTv.setText(mTitle);
        mContentTv.setText(mContent);
        mPositiveBtn.setText(mBtnText);
        mTitleTv.setTextColor(getContext().getResources().getColor(getColorResId(mDialogType)));
    }

    private void resizeDialog()
    {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (Utils.getScreenSize(getContext()).x * 0.9);
        getWindow().setAttributes(params);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        startWithAnimation(mIsShowAnim);
    }

    @Override
    public void dismiss()
    {
        dismissWithAnimation(mIsShowAnim);
    }

    private void startWithAnimation(boolean showInAnimation)
    {
        if (showInAnimation)
        {
            mDialogView.startAnimation(mAnimIn);
        }
    }

    private void dismissWithAnimation(boolean showOutAnimation)
    {
        if (showOutAnimation)
        {
            mDialogView.startAnimation(mAnimOut);
        }
        else
        {
            super.dismiss();
        }
    }

    private int getLogoResId(int mDialogType)
    {
        switch (mDialogType)
        {
            case DIALOG_TYPE_DEFAULT:

                return R.drawable.ic_info;

            case DIALOG_TYPE_HELP:

                return R.drawable.ic_help;

            case DIALOG_TYPE_ERROR:

                return R.drawable.ic_wrong;

            case DIALOG_TYPE_SUCCESS:

                return R.drawable.ic_success;

            case DIALOG_TYPE_WARNING:

                return R.drawable.icon_warning;

            default:

                return R.drawable.ic_info;
        }
    }

    private int getColorResId(int mDialogType)
    {
        switch (mDialogType)
        {
            case DIALOG_TYPE_DEFAULT:

                return R.color.color_type_info;

            case DIALOG_TYPE_HELP:

                return R.color.color_type_help;

            case DIALOG_TYPE_ERROR:

                return R.color.color_type_wrong;

            case DIALOG_TYPE_SUCCESS:

                return R.color.color_type_success;

            case DIALOG_TYPE_WARNING:

                return R.color.color_type_warning;

            default:

                return R.color.color_type_info;
        }
    }

    private int getSelBtn(int mDialogType)
    {
        switch (mDialogType)
        {
            case DIALOG_TYPE_DEFAULT:

                return R.drawable.sel_btn;

            case DIALOG_TYPE_HELP:

                return R.drawable.sel_btn_help;

            case DIALOG_TYPE_ERROR:

                return R.drawable.sel_btn_wrong;

            case DIALOG_TYPE_SUCCESS:

                return R.drawable.sel_btn_success;

            case DIALOG_TYPE_WARNING:

                return R.drawable.sel_btn_warning;

            default:

                return R.drawable.sel_btn;
        }
    }

    private void initAnimListener()
    {
        mAnimOut.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                mDialogView.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        callDismiss();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }

    private void initListener()
    {
        mPositiveBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mOnPositiveListener != null)
                {
                    mOnPositiveListener.onClick(PromptDialogBox.this);
                }
            }
        });

        initAnimListener();
    }

    private void callDismiss()
    {
        super.dismiss();
    }

    private Bitmap createTriangle(int width, int height)
    {
        if (width <= 0 || height <= 0)
        {
            return null;
        }

        return getBitmap(width, height, getContext().getResources().getColor(getColorResId(mDialogType)));
    }

    private Bitmap getBitmap(int width, int height, int backgroundColor)
    {
        Bitmap bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(backgroundColor);
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width / 2, height);
        path.close();

        canvas.drawPath(path, paint);
        return bitmap;
    }

    private void setBtnBackground(final TextView btnOk)
    {
        btnOk.setTextColor(createColorStateList(getContext().getResources().getColor(getColorResId(mDialogType)),
                getContext().getResources().getColor(R.color.color_dialog_gray)));

        btnOk.setBackground(getContext().getResources().getDrawable(getSelBtn(mDialogType)));
    }

    private void setBottomCorners(View llBtnGroup)
    {
        int radius = Utils.dp2px(getContext(), DEFAULT_RADIUS);
        float[] outerRadii = new float[]{0, 0, 0, 0, radius, radius, radius, radius};

        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);

        shapeDrawable.getPaint().setColor(Color.WHITE);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        llBtnGroup.setBackground(shapeDrawable);
    }

    private ColorStateList createColorStateList(int normal, int pressed)
    {
        return createColorStateList(normal, pressed, Color.BLACK, Color.BLACK);
    }

    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable)
    {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];

        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};

        states[5] = new int[]{};
        return new ColorStateList(states, colors);
    }

    public PromptDialogBox setAnimationEnable(boolean enable)
    {
        mIsShowAnim = enable;
        return this;
    }

    public PromptDialogBox setTitleText(CharSequence title)
    {
        mTitle = title;
        return this;
    }

    public PromptDialogBox setTitleText(int resId)
    {
        return setTitleText(getContext().getString(resId));
    }

    public PromptDialogBox setContentText(CharSequence content)
    {
        mContent = content;
        return this;
    }

    public PromptDialogBox setContentText(int resId)
    {
        return setContentText(getContext().getString(resId));
    }

    public TextView getTitleTextView()
    {
        return mTitleTv;
    }

    public TextView getContentTextView()
    {
        return mContentTv;
    }

    public int getDialogType()
    {
        return mDialogType;
    }

    public PromptDialogBox setDialogType(int type)
    {
        mDialogType = type;
        return this;
    }

    public PromptDialogBox setPositiveListener(CharSequence btnText, onPositiveListener l)
    {
        mBtnText = btnText;
        return setPositiveListener(l);
    }

    public PromptDialogBox setPositiveListener(int stringResId, onPositiveListener l)
    {
        return setPositiveListener(getContext().getString(stringResId), l);
    }

    public PromptDialogBox setPositiveListener(onPositiveListener l)
    {
        mOnPositiveListener = l;
        return this;
    }

    public PromptDialogBox setAnimationIn(AnimationSet animIn)
    {
        mAnimIn = animIn;
        return this;
    }

    public PromptDialogBox setAnimationOut(AnimationSet animOut)
    {
        mAnimOut = animOut;
        initAnimListener();
        return this;
    }

    public interface onPositiveListener
    {
        void onClick(PromptDialogBox dialog);
    }
}
