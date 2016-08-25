package com.github.manjunathc23.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.github.manjunathc23.params.CircularImageOptionsParams;

@SuppressWarnings("unused")
public class CircularImageView extends ImageView {
    private static final String TAG = CircularImageView.class.getSimpleName();
    private static final ImageView.ScaleType DEFAULT_SCALE_TYPE = ImageView.ScaleType.CENTER_CROP;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final boolean DEFAULT_BORDER_OVERLAY = false;
    private static final int DEFAULT_BORDER_COLOR = Color.WHITE;
    private static final int DEFAULT_FILL_COLOR = Color.TRANSPARENT;
    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();
    private final Paint mFillPaint = new Paint();
    private final Matrix mShaderMatrix = new Matrix();
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mFillColor = DEFAULT_FILL_COLOR;
    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private ColorFilter mColorFilter;

    private int mBitmapWidth;
    private int mBitmapHeight;
    private float mBorderRadius;
    private float mDrawableRadius;

    private boolean mReady;
    private boolean mSetupPending;
    private boolean mBorderOverlay;
    private boolean mDisableCircularTransformation;

    public CircularImageView(Context context) {
        super(context);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView,
                defStyle, 0);
        mFillColor = a.getColor(R.styleable.CircularImageView_civ_fill_color,
                DEFAULT_FILL_COLOR);
        mBorderColor = a.getColor(R.styleable.CircularImageView_civ_border_color,
                DEFAULT_BORDER_COLOR);
        mBorderOverlay = a.getBoolean(R.styleable.CircularImageView_civ_border_overlay,
                DEFAULT_BORDER_OVERLAY);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircularImageView_civ_border_width,
                DEFAULT_BORDER_WIDTH);
        a.recycle();
        init();
    }

    @Override
    public ImageView.ScaleType getScaleType() {
        return DEFAULT_SCALE_TYPE;
    }

    @Override
    public void setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType != DEFAULT_SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.",
                    scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDisableCircularTransformation) {
            super.onDraw(canvas);
            return;
        }

        //Sanity check
        if (mBitmap == null) {
            return;
        }

        //Fill the color of the bitmap
        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(),
                    mDrawableRadius, mFillPaint);
        }

        //Draw circular image
        canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(),
                mDrawableRadius, mBitmapPaint);

        //Draw border
        if (mBorderWidth > 0) {
            canvas.drawCircle(mBorderRect.centerX(), mBorderRect.centerY(),
                    mBorderRadius, mBorderPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setupCircularImageView();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    @Override
    public ColorFilter getColorFilter() {
        return mColorFilter;
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        if (colorFilter == mColorFilter) {
            return;
        }
        mColorFilter = colorFilter;
        applyColorFilter(colorFilter);
        invalidate();
    }

    @Nullable
    private Bitmap getBitmapFromDrawable(@Nullable Drawable drawable) {
        //Sanity
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int colorDrawableDimension = getContext().getResources()
                .getInteger(R.integer.color_dimension);

        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(colorDrawableDimension,
                        colorDrawableDimension,
                        Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, "Unable to fetch the bitmap from circle image view : " + e);
            return null;
        }
    }

    //region public methods
    public void setAdditionalData(CircularImageOptionsParams params) {
        setFillColor(params.getFillColor());
        setBorderWidth(params.getBorderWidth());
        setBorderColor(params.getBorderColor());
        setFillColorResource(params.getFillColorRes());
        setBorderOverlay(params.isShowBorderOverlay());
        setBorderColorResource(params.getBorderColorRes());
        setDisableCircularTransformation(params.isDisableCircularTransformation());
    }

    @ColorInt
    public int getBorderColor() {
        return mBorderColor;
    }

    //region internal setters
    private void setBorderColor(@ColorInt int borderColor) {
        if (borderColor == mBorderColor || borderColor == CircularImageOptionsParams.INVALID_RES) {
            return;
        }
        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    @ColorInt
    public int getFillColor() {
        return mFillColor;
    }

    private void setFillColor(@ColorInt int fillColor) {
        if (fillColor == mFillColor || fillColor == CircularImageOptionsParams.INVALID_RES) {
            return;
        }
        mFillColor = fillColor;
        mFillPaint.setColor(fillColor);
        invalidate();
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }
    //endregion public methods

    private void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth || borderWidth == CircularImageOptionsParams.INVALID_RES) {
            return;
        }
        mBorderWidth = borderWidth;
        setupCircularImageView();
    }

    public boolean isBorderOverlay() {
        return mBorderOverlay;
    }

    private void setBorderOverlay(boolean borderOverlay) {
        if (borderOverlay == mBorderOverlay) {
            return;
        }
        mBorderOverlay = borderOverlay;
        setupCircularImageView();
    }

    public boolean isDisableCircularTransformation() {
        return mDisableCircularTransformation;
    }

    private void setDisableCircularTransformation(boolean disableCircularTransformation) {
        if (mDisableCircularTransformation == disableCircularTransformation) {
            return;
        }

        mDisableCircularTransformation = disableCircularTransformation;
        initializeBitmap();
    }

    //region internal helper methods
    private synchronized void init() {
        super.setScaleType(DEFAULT_SCALE_TYPE);
        mReady = true;
        if (mSetupPending) {
            setupCircularImageView();
            mSetupPending = false;
        }
    }
    //endregion internal helper methods

    private void initializeBitmap() {
        if (mDisableCircularTransformation) {
            mBitmap = null;
            return;
        }

        mBitmap = getBitmapFromDrawable(getDrawable());
        setupCircularImageView();
    }

    private void setupCircularImageView() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }

        if (mBitmap == null) {
            invalidate();
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(mFillColor);

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        mBorderRect.set(calculateBounds());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2.0f,
                (mBorderRect.width() - mBorderWidth) / 2.0f);

        mDrawableRect.set(mBorderRect);
        if (!mBorderOverlay) {
            mDrawableRect.inset(mBorderWidth, mBorderWidth);
        }
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f,
                mDrawableRect.width() / 2.0f);
        applyColorFilter(mColorFilter);
        updateShaderMatrix();
        invalidate();
    }

    private void applyColorFilter(ColorFilter colorFilter) {
        if (mBitmapPaint == null) {
            return;
        }
        mBitmapPaint.setColorFilter(colorFilter);
    }

    private RectF calculateBounds() {
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int sideLength = Math.min(availableWidth, availableHeight);
        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;
        return new RectF(left, top, left + sideLength, top + sideLength);
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;
        mShaderMatrix.set(null);
        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }
        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left,
                (int) (dy + 0.5f) + mDrawableRect.top);
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    private void setBorderColorResource(@ColorRes int borderColorRes) {
        if (borderColorRes == CircularImageOptionsParams.INVALID_RES) {
            return;
        }
        setBorderColor(ContextCompat.getColor(getContext(), borderColorRes));
    }

    private void setFillColorResource(@ColorRes int fillColorRes) {
        if (fillColorRes == CircularImageOptionsParams.INVALID_RES) {
            return;
        }
        setFillColor(ContextCompat.getColor(getContext(), fillColorRes));
    }
    //end region internal setters

}
