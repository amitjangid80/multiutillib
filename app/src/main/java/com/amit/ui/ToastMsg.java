package com.amit.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.amit.R;

@SuppressWarnings({"WeakerAccess", "unused"})
@SuppressLint("InflateParams")
public class ToastMsg
{
    @ColorInt
    private static int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");

    @ColorInt
    private static int ERROR_COLOR = Color.parseColor("#D50000");

    @ColorInt
    private static int INFO_COLOR = Color.parseColor("#3F51B5");

    @ColorInt
    private static int SUCCESS_COLOR = Color.parseColor("#388E3C");

    @ColorInt
    private static int WARNING_COLOR = Color.parseColor("#FFA900");

    @ColorInt
    private static int NORMAL_COLOR = Color.parseColor("#353A3E");

    private static final Typeface LOADED_TOAST_TYPEFACE =
            Typeface.create("sans-serif-condensed", Typeface.NORMAL);

    private static Typeface currentTypeFace = LOADED_TOAST_TYPEFACE;
    private static int textSize = 16; // in SP
    private static boolean tintIcon = true;

    public enum ToastPosition
    {
        TOP, BOTTOM, CENTER
    }

    public ToastMsg()
    {
        // avoiding instantiation
    }

    @CheckResult
    public static Toast normal(@NonNull Context context,
                               @NonNull CharSequence message,
                               ToastPosition toastPosition)
    {
        return normal(context, message, Toast.LENGTH_SHORT, null, false, toastPosition);
    }

    @CheckResult
    public static Toast normal(@NonNull Context context,
                               @NonNull CharSequence message,
                               Drawable icon,
                               ToastPosition toastPosition)
    {
        return normal(context, message, Toast.LENGTH_SHORT, icon, true, toastPosition);
    }

    @CheckResult
    public static Toast normal(@NonNull Context context,
                               @NonNull CharSequence message,
                               int duration,
                               ToastPosition toastPosition)
    {
        return normal(context, message, duration, null, false, toastPosition);
    }

    @CheckResult
    public static Toast normal(@NonNull Context context,
                               @NonNull CharSequence message,
                               int duration,
                               Drawable icon,
                               ToastPosition toastPosition)
    {
        return normal(context, message, duration, icon, true, toastPosition);
    }

    @CheckResult
    public static Toast normal(@NonNull Context context,
                               @NonNull CharSequence message,
                               int duration, Drawable icon,
                               boolean withIcon,
                               ToastPosition toastPosition)
    {
        return custom(context, message, icon, NORMAL_COLOR,
                duration, withIcon, true, toastPosition);
    }

    @CheckResult
    public static Toast warning(@NonNull Context context,
                                @NonNull CharSequence message,
                                ToastPosition toastPosition)
    {
        return warning(context, message, Toast.LENGTH_SHORT, true, toastPosition);
    }

    @CheckResult
    public static Toast warning(@NonNull Context context,
                                @NonNull CharSequence message,
                                int duration,
                                ToastPosition toastPosition)
    {
        return warning(context, message, duration, true, toastPosition);
    }

    @CheckResult
    public static Toast warning(@NonNull Context context,
                                @NonNull CharSequence message,
                                int duration,
                                boolean withIcon,
                                ToastPosition toastPosition)
    {
        return custom(context, message,
                ToastMsgUtils.getDrawable(context, R.drawable.ic_error_outline_white_48dp),
                WARNING_COLOR,
                duration,
                withIcon,
                true,
                toastPosition);
    }

    @CheckResult
    public static Toast info(@NonNull Context context,
                             @NonNull CharSequence message,
                             ToastPosition toastPosition)
    {
        return info(context, message, Toast.LENGTH_SHORT, true, toastPosition);
    }

    @CheckResult
    public static Toast info(@NonNull Context context,
                             @NonNull CharSequence message,
                             int duration,
                             ToastPosition toastPosition)
    {
        return info(context, message, duration, true, toastPosition);
    }

    @CheckResult
    public static Toast info(@NonNull Context context,
                             @NonNull CharSequence message,
                             int duration,
                             boolean withIcon,
                             ToastPosition toastPosition)
    {
        return custom(context, message,
                ToastMsgUtils.getDrawable(context, R.drawable.ic_info_outline_white_48dp),
                INFO_COLOR,
                duration,
                withIcon,
                true,
                toastPosition);
    }

    @CheckResult
    public static Toast success(@NonNull Context context,
                                @NonNull CharSequence message,
                                ToastPosition toastPosition)
    {
        return success(context, message, Toast.LENGTH_SHORT, true, toastPosition);
    }

    @CheckResult
    public static Toast success(@NonNull Context context,
                                @NonNull CharSequence message,
                                int duration,
                                ToastPosition toastPosition)
    {
        return success(context, message, duration, true, toastPosition);
    }

    @CheckResult
    public static Toast success(@NonNull Context context,
                                @NonNull CharSequence message,
                                int duration,
                                boolean withIcon,
                                ToastPosition toastPosition)
    {
        return custom(context, message,
                ToastMsgUtils.getDrawable(context, R.drawable.ic_check_white_48dp),
                SUCCESS_COLOR,
                duration,
                withIcon,
                true,
                toastPosition);
    }

    @CheckResult
    public static Toast error(@NonNull Context context,
                              @NonNull CharSequence message,
                              ToastPosition toastPosition)
    {
        return error(context, message, Toast.LENGTH_SHORT, true, toastPosition);
    }

    @CheckResult
    public static Toast error(@NonNull Context context,
                              @NonNull CharSequence message,
                              int duration,
                              ToastPosition toastPosition)
    {
        return error(context, message, duration, true, toastPosition);
    }

    @CheckResult
    public static Toast error(@NonNull Context context,
                              @NonNull CharSequence message,
                              int duration,
                              boolean withIcon,
                              ToastPosition toastPosition)
    {
        return custom(context, message,
                ToastMsgUtils.getDrawable(context, R.drawable.ic_clear_white_48dp),
                ERROR_COLOR,
                duration,
                withIcon,
                true,
                toastPosition);
    }

    @CheckResult
    public static Toast custom(@NonNull Context context,
                               @NonNull CharSequence message,
                               Drawable icon,
                               int duration,
                               boolean withIcon,
                               ToastPosition toastPosition)
    {
        return custom(context, message, icon,
                -1,
                duration,
                withIcon,
                false,
                toastPosition);
    }

    @CheckResult
    public static Toast custom(@NonNull Context context,
                               @NonNull CharSequence message,
                               @DrawableRes int iconRes,
                               @ColorInt int tintColor,
                               int duration, boolean withIcon,
                               boolean shouldTint,
                               ToastPosition toastPosition)
    {
        return custom(context, message,
                ToastMsgUtils.getDrawable(context, iconRes),
                tintColor,
                duration,
                withIcon,
                shouldTint,
                toastPosition);
    }

    @SuppressLint("ShowToast")
    @CheckResult
    public static Toast custom(@NonNull Context context,
                               @NonNull CharSequence message,
                               Drawable icon,
                               @ColorInt int tintColor,
                               int duration,
                               boolean withIcon,
                               boolean shouldTint,
                               ToastPosition toastPosition)
    {
        final Toast currentToast = new Toast(context);

        final View toastLayout = ((LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.toast_message_layout, null);

        final ImageView toastIcon = toastLayout.findViewById(R.id.toastIcon);
        final TextView tvToastMessage = toastLayout.findViewById(R.id.tvToastMessage);

        Drawable drawableFrame;

        if (shouldTint)
        {
            // drawableFrame = ToastMsgUtils.tint9PatchDrawableFrame(context, tintColor);
            drawableFrame = ToastMsgUtils.getDrawable(context, R.drawable.toast_background);
            drawableFrame.setColorFilter(tintColor, PorterDuff.Mode.ADD);
        }
        else
        {
            // drawableFrame = ToastMsgUtils.getDrawable(context, R.drawable.toast_frame);
            drawableFrame = ToastMsgUtils.getDrawable(context, R.drawable.toast_background);
        }

        ToastMsgUtils.setBackground(toastLayout, drawableFrame);

        if (withIcon)
        {
            if (icon == null)
            {
                throw new IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true.");
            }

            if (tintIcon)
            {
                icon = ToastMsgUtils.tintIcon(icon, DEFAULT_TEXT_COLOR);
            }

            ToastMsgUtils.setBackground(toastIcon, icon);
        }
        else
        {
            toastIcon.setVisibility(View.GONE);
        }

        switch (toastPosition)
        {
            case TOP:
                currentToast.setGravity(Gravity.TOP, 0, 0);
                break;

            case BOTTOM:
                currentToast.setGravity(Gravity.BOTTOM, 0, 0);
                break;

            case CENTER:
                currentToast.setGravity(Gravity.CENTER, 0, 0);
                break;

            default:
                break;
        }

        tvToastMessage.setText(message);
        tvToastMessage.setTextColor(DEFAULT_TEXT_COLOR);
        tvToastMessage.setTypeface(currentTypeFace);
        tvToastMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        currentToast.setDuration(duration);
        currentToast.setView(toastLayout);
        return currentToast;
    }

    public static class Config
    {
        @ColorInt
        private int DEFAULT_TEXT_COLOR = ToastMsg.DEFAULT_TEXT_COLOR;

        @ColorInt
        private int ERROR_COLOR = ToastMsg.ERROR_COLOR;

        @ColorInt
        private int INFO_COLOR = ToastMsg.INFO_COLOR;

        @ColorInt
        private int SUCCESS_COLOR = ToastMsg.SUCCESS_COLOR;

        @ColorInt
        private int WARNING_COLOR = ToastMsg.WARNING_COLOR;

        private Typeface typeface = ToastMsg.currentTypeFace;
        private boolean tintIcon = ToastMsg.tintIcon;
        private int textSize = ToastMsg.textSize;

        private Config()
        {
            // avoiding instantiation
        }

        @CheckResult
        public static Config getInstance()
        {
            return new Config();
        }

        public static void reset()
        {
            ToastMsg.DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");
            ToastMsg.ERROR_COLOR = Color.parseColor("#D50000");
            ToastMsg.INFO_COLOR = Color.parseColor("#3F51B5");
            ToastMsg.SUCCESS_COLOR = Color.parseColor("#388E3C");
            ToastMsg.WARNING_COLOR = Color.parseColor("#FFA900");
            ToastMsg.currentTypeFace = LOADED_TOAST_TYPEFACE;
            ToastMsg.textSize = 16;
            ToastMsg.tintIcon = true;
        }

        @CheckResult
        public Config setTextColor(@ColorInt int textColor)
        {
            DEFAULT_TEXT_COLOR = textColor;
            return this;
        }

        @CheckResult
        public Config setErrorColor(@ColorInt int errorColor)
        {
            ERROR_COLOR = errorColor;
            return this;
        }

        @CheckResult
        public Config setInfoColor(@ColorInt int infoColor)
        {
            INFO_COLOR = infoColor;
            return this;
        }

        @CheckResult
        public Config setSuccessColor(@ColorInt int successColor)
        {
            SUCCESS_COLOR = successColor;
            return this;
        }

        @CheckResult
        public Config setWarningColor(@ColorInt int warningColor)
        {
            WARNING_COLOR = warningColor;
            return this;
        }

        @CheckResult
        public Config setToastTypeface(@NonNull Typeface typeface)
        {
            this.typeface = typeface;
            return this;
        }

        @CheckResult
        public Config setTextSize(int sizeInSp)
        {
            this.textSize = sizeInSp;
            return this;
        }

        @CheckResult
        public Config tintIcon(boolean tintIcon)
        {
            this.tintIcon = tintIcon;
            return this;
        }

        public void apply()
        {
            ToastMsg.DEFAULT_TEXT_COLOR = DEFAULT_TEXT_COLOR;
            ToastMsg.ERROR_COLOR = ERROR_COLOR;
            ToastMsg.INFO_COLOR = INFO_COLOR;
            ToastMsg.SUCCESS_COLOR = SUCCESS_COLOR;
            ToastMsg.WARNING_COLOR = WARNING_COLOR;
            ToastMsg.currentTypeFace = typeface;
            ToastMsg.textSize = textSize;
            ToastMsg.tintIcon = tintIcon;
        }
    }
}
