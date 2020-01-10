package com.amit.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.amit.R;

import static com.amit.dialog.Anim.POP;
import static com.amit.dialog.Anim.SIDE;
import static com.amit.dialog.Anim.SLIDE;

/**
 * Created by Amit Jangid on 21,May,2018
 **/
@SuppressWarnings({"WeakerAcces", "unused"})
public class AlertDialogBox
{
    private Anim Anim;
    private boolean cancel;
    private Icon visibility;
    private Activity activity;

    private AlertDialogListener pListener, nListener;
    private String title, message, positiveBtnText, negativeBtnText;

    @DrawableRes
    private int icon;

    @ColorInt
    private int pBtnColor, nBtnColor, bgColor, pBtnTextColor, nBtnTextColor;

    private AlertDialogBox(Builder builder)
    {
        this.icon = builder.icon;
        this.Anim = builder.Anim;
        this.title = builder.title;
        this.cancel = builder.cancel;

        this.message = builder.message;
        this.bgColor = builder.bgColor;
        this.activity = builder.activity;

        this.pListener = builder.pListener;
        this.nListener = builder.nListener;

        this.pBtnColor = builder.pBtnColor;
        this.nBtnColor = builder.nBtnColor;
        this.visibility = builder.visibility;

        this.pBtnTextColor = builder.pBtnTextColor;
        this.nBtnTextColor = builder.nBtnTextColor;

        this.positiveBtnText = builder.positiveBtnText;
        this.negativeBtnText = builder.negativeBtnText;
    }

    public static class Builder
    {
        private Anim Anim;
        private Icon visibility;
        private Activity activity;

        private boolean cancel;
        private AlertDialogListener pListener, nListener;
        private String title, message, positiveBtnText, negativeBtnText;

        @DrawableRes private int icon;
        @ColorInt private int pBtnColor, pBtnTextColor, nBtnColor, nBtnTextColor, bgColor;

        public Builder(Activity activity)
        {
            this.activity = activity;
        }

        public Builder setTitle(String title)
        {
            this.title = title;
            return this;
        }

        public Builder setBackgroundColor(int bgColor)
        {
            this.bgColor = bgColor;
            return this;
        }

        public Builder setMessage(String message)
        {
            this.message = message;
            return this;
        }

        public Builder setPositiveBtnText(String positiveBtnText)
        {
            this.positiveBtnText = positiveBtnText;
            return this;
        }

        public Builder setPositiveBtnTextColor(int pBtnTextColor)
        {
            this.pBtnTextColor = pBtnTextColor;
            return this;
        }

        public Builder setPositiveBtnBackground(int pBtnColor)
        {
            this.pBtnColor = pBtnColor;
            return this;
        }

        public Builder setNegativeBtnText(String negativeBtnText)
        {
            this.negativeBtnText = negativeBtnText;
            return this;
        }

        public Builder setNegativeBtnTextColor(int nBtnTextColor)
        {
            this.nBtnTextColor = nBtnTextColor;
            return this;
        }

        public Builder setNegativeBtnBackground(int nBtnColor)
        {
            this.nBtnColor = nBtnColor;
            return this;
        }

        //setIcon
        public Builder setIcon(int icon, Icon visibility)
        {
            this.icon = icon;
            this.visibility = visibility;
            return this;
        }

        public Builder setAnim(com.amit.dialog.Anim Anim)
        {
            this.Anim = Anim;
            return this;
        }

        //set Positive listener
        public Builder onPositiveClicked(AlertDialogListener pListener)
        {
            this.pListener = pListener;
            return this;
        }

        //set Negative listener
        public Builder onNegativeClicked(AlertDialogListener nListener)
        {
            this.nListener = nListener;
            return this;
        }

        public Builder isCancellable(boolean cancel)
        {
            this.cancel = cancel;
            return this;
        }

        public AlertDialogBox build()
        {
            TextView message1, title1;
            ImageView iconImg;
            Button nBtn, pBtn;
            View view;

            final Dialog dialog;

            if (Anim == POP)
            {
                dialog = new Dialog(activity, R.style.PopTheme);
            }
            else if (Anim == SIDE)
            {
                dialog = new Dialog(activity, R.style.SideTheme);
            }
            else if (Anim == SLIDE)
            {
                dialog = new Dialog(activity, R.style.SlideTheme);
            }
            else
            {
                dialog = new Dialog(activity);
            }

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(cancel);
            dialog.setContentView(R.layout.custom_alert_dialog);

            //getting resources
            view = dialog.findViewById(R.id.background);
            title1 = dialog.findViewById(R.id.title);
            message1 = dialog.findViewById(R.id.message);
            iconImg = dialog.findViewById(R.id.icon);
            nBtn = dialog.findViewById(R.id.negativeBtn);
            pBtn = dialog.findViewById(R.id.positiveBtn);
            title1.setText(title);
            message1.setText(message);

            if (positiveBtnText != null)
            {
                pBtn.setText(positiveBtnText);
            }

            if (pBtnColor != 0)
            {
                GradientDrawable bgShape = (GradientDrawable) pBtn.getBackground();
                bgShape.setColor(pBtnColor);
            }

            if (pBtnTextColor != 0)
            {
                pBtn.setTextColor(pBtnTextColor);
            }

            if (nBtnColor != 0)
            {
                GradientDrawable bgShape = (GradientDrawable) nBtn.getBackground();
                bgShape.setColor(nBtnColor);
            }

            if (nBtnTextColor != 0)
            {
                nBtn.setTextColor(nBtnTextColor);
            }

            if (negativeBtnText != null)
            {
                nBtn.setText(negativeBtnText);
            }

            iconImg.setImageResource(icon);

            if (visibility == Icon.Visible)
            {
                iconImg.setVisibility(View.VISIBLE);
            }
            else
            {
                iconImg.setVisibility(View.GONE);
            }

            if (bgColor != 0)
            {
                view.setBackgroundColor(bgColor);
            }

            if (pListener != null)
            {
                pBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        pListener.onClick();
                        dialog.dismiss();
                    }
                });
            }
            else
            {
                pBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        dialog.dismiss();
                    }
                });
            }

            if (nListener != null)
            {
                nBtn.setVisibility(View.VISIBLE);

                nBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        nListener.onClick();
                        dialog.dismiss();
                    }
                });
            }

            dialog.show();
            return new AlertDialogBox(this);
        }
    }
}
