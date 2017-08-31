package com.ljw.uitest.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ljw.uitest.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 配合ViewPager的Navigation Bar 多个Tab
 *
 * @version 1.0
 *          <p>
 *          Created by TestLiu on 2015/9/6.
 */
public class PagerSlidingTabStrip extends HorizontalScrollView {

    public interface HeaderFooterClickListener {
        void onHeaderClick(int position);

        void onFooterClick(int position);
    }

    public interface TabClickListener {
        void onClick(int position);
    }

    public interface IconTabProvider {
        int getPageIconResId(int position);
    }

    // Header、 Footer内容
    public final static class TabContent {
        private int iconId;   //图标
        private String text;  //文字

        public TabContent(int iconId, String text) {
            this.iconId = iconId;
            this.text = text;
        }

        public int getIconId() {
            return iconId;
        }

        public void setIconId(int iconId) {
            this.iconId = iconId;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    // @formatter:off
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };
    // @formatter:on

    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private final InternalPagerListener pagerListener = new InternalPagerListener();
    private final InternalAdapterObserver adapterObserver = new InternalAdapterObserver();
    public OnPageChangeListener pageListener;
    private ViewPager viewPager;

    private HeaderFooterClickListener headerFooterClickListener;
    private TabClickListener tabClickListener;
    private LinearLayout tabsContainer;

    private int tabCount;
    private RectF rect;
    private int lastScrollX = 0;
    private Locale locale;
    private int currentPosition = 0, _currentPosition = 0;
    private int selectedPosition = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;
    private Paint dividerPaint;

    // 列表头部
    private List<TabContent> headers = new ArrayList<>();
    // 列表尾部
    private List<TabContent> footers = new ArrayList<>();

    // 整个列表内容是否铺满宽度(所有item等宽宽度)
    private boolean isMatchWidth;
    // 整个列表内容宽度，需要铺满宽度才生效
    private int matchWidth = -1;
    // 最大item平分个数，超过此数量后自动失效
    private int maxMatchSize = 3;

    // ViewPager 是否平滑滚动
    private boolean pagerSmoothScroll = true;
    // 游标是否和文字对齐(等宽)
    private boolean isCursorAlignText;
    // 游标是否圆角
    private boolean isCursorRoundCorner;
    // 游标圆角半径
    private int cornerRadius;
    // 指示器高度是否匹配整个控件
    private boolean isIndicatorMatchHeight;

    // 文字是否开启自动大写
    private boolean textAllCaps;
    // 最大线宽
    private int maxLineWidth = 0;
    // 底部栏顶部外边距
    private int underlineMarginTop = 0;
    // 列表触发自动滚动宽度
    private int scrollOffset = 52;
    // 游标高度
    private int indicatorHeight = 8;
    // 控件底部线高度
    private int underlineHeight = 2;
    // 分割线距离顶部和底部边距
    private int dividerPadding = 12;
    // 文字内容内边距（影响item宽度）
    private int tabPadding = 0;
    // 分割线宽度
    private float dividerWidth = 1.0f;

    // item 文字大小
    private int tabTextSize = 12;
    // item 未选中文字颜色
    private int tabTextColor = 0xFF666666;
    // item 文字字体
    private Typeface tabTypeface = null;
    // item 文字字体样式
    private int tabTypefaceStyle = Typeface.NORMAL;
    // item 背景
    private int tabBackgroundResId = R.drawable.ui_background_tab;

    // 游标颜色
    private int indicatorColor = 0xFF666666;
    // 底线颜色
    private int underlineColor = 0x1A000000;
    // item 分隔竖线颜色
    private int dividerColor = 0x1A000000;
    // 选中item文字颜色
    private int selectedTabTextColor = 0xFF666666;
    //选中文字是否加粗
    private boolean selectedTextBold = true;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings("all")
    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(false);
        setWillNotDraw(false);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);
        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);

        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

        tabTextColor = a.getColor(R.styleable.PagerSlidingTabStrip_tab_text_color, tabTextColor);
        tabTextSize = a.getDimensionPixelOffset(R.styleable.PagerSlidingTabStrip_tab_text_size, tabTextSize);
        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_indicator_color, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_underline_color, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_divider_color, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_indicator_height, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_underline_height, underlineHeight);
        selectedTabTextColor = a.getColor(R.styleable.PagerSlidingTabStrip_selected_tab_text_color, selectedTabTextColor);
        dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_divider_padding, dividerPadding);
        dividerWidth = a.getDimension(R.styleable.PagerSlidingTabStrip_divider_width, dividerWidth);
        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_tab_padding_left_right, tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_tab_background, tabBackgroundResId);
        isMatchWidth = a.getBoolean(R.styleable.PagerSlidingTabStrip_should_match_width, isMatchWidth);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_scroll_offset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_text_all_caps, textAllCaps);
        underlineMarginTop = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_underline_margin_top, underlineMarginTop);
        maxLineWidth = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_max_line_width, maxLineWidth);
        isCursorAlignText = a.getBoolean(R.styleable.PagerSlidingTabStrip_cursor_align_text, isCursorAlignText);
        isCursorRoundCorner = a.getBoolean(R.styleable.PagerSlidingTabStrip_cursor_round_corner, isCursorRoundCorner);
        cornerRadius = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_corner_radius, indicatorHeight);
        isIndicatorMatchHeight = a.getBoolean(R.styleable.PagerSlidingTabStrip_indicator_match_height, isIndicatorMatchHeight);
        pagerSmoothScroll = a.getBoolean(R.styleable.PagerSlidingTabStrip_pager_smooth_scroll, pagerSmoothScroll);
        maxMatchSize = a.getInt(R.styleable.PagerSlidingTabStrip_max_match_size, maxMatchSize);
        selectedTextBold = a.getBoolean(R.styleable.PagerSlidingTabStrip_selected_text_bold, selectedTextBold);
        TypedValue tv = a.peekValue(R.styleable.PagerSlidingTabStrip_match_width);
        if (null != tv) {
            switch (tv.type) {
                case TypedValue.TYPE_DIMENSION:
                    matchWidth = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_match_width, matchWidth);
                    break;
                case TypedValue.TYPE_FIRST_INT:
                    matchWidth = a.getInt(R.styleable.PagerSlidingTabStrip_match_width, matchWidth);
                    break;
            }
        }

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        rect = new RectF();

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);

        addView(tabsContainer, new LayoutParams(
                isMatchWidth ? LayoutParams.MATCH_PARENT : LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT));

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    public void setViewPager(ViewPager pager) {
        this.viewPager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        this.viewPager.getAdapter().registerDataSetObserver(adapterObserver);

        pager.addOnPageChangeListener(pagerListener);
        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.pageListener = listener;
    }

    /**
     * 此方法必须在ViewPager Adapter 刷新之前调用
     */
    public void notifyDataSetChanged() {

        tabsContainer.removeAllViews();

        tabCount = (null != viewPager ? viewPager.getAdapter().getCount() : 0) + headers.size() + footers.size();

        for (int index = 0; index < tabCount; index++) {
            if (null != viewPager) {
                if (viewPager.getAdapter() instanceof IconTabProvider) {
                    if (index < headers.size()) { //headers
                        TabContent content = headers.get(index);
                        if (0 != content.getIconId()) {
                            addIconTab(index, content.getIconId());
                        } else {
                            addTextTab(index, content.getText());
                        }
                    } else if (index >= headers.size() && index < tabCount - footers.size()) {
                        addIconTab(index, ((IconTabProvider) viewPager.getAdapter()).getPageIconResId(index - headers.size()));
                    } else { //footers
                        TabContent content = footers.get(index + 1 - footers.size() - viewPager.getAdapter().getCount());
                        if (0 != content.getIconId()) {
                            addIconTab(index, content.getIconId());
                        } else {
                            addTextTab(index, content.getText());
                        }
                    }
                } else {
                    if (index < headers.size()) { //headers
                        TabContent content = headers.get(index);
                        if (!TextUtils.isEmpty(content.getText())) {
                            addTextTab(index, content.getText());
                        } else {
                            addIconTab(index, content.getIconId());
                        }
                    } else if (index >= headers.size() && index < tabCount - footers.size()) {
                        addTextTab(index, viewPager.getAdapter().getPageTitle(index - headers.size()).toString());
                    } else { //footers
                        TabContent content = footers.get(index + 1 - footers.size() - viewPager.getAdapter().getCount());
                        if (!TextUtils.isEmpty(content.getText())) {
                            addTextTab(index, content.getText());
                        } else {
                            addIconTab(index, content.getIconId());
                        }
                    }
                }
            } else {
                if (index < headers.size()) { //headers
                    TabContent content = headers.get(index);
                    if (0 != content.getIconId()) {
                        addIconTab(index, content.getIconId());
                    } else {
                        addTextTab(index, content.getText());
                    }
                } else { //footers
                    TabContent content = footers.get(index + 1 - footers.size());
                    if (0 != content.getIconId()) {
                        addIconTab(index, content.getIconId());
                    } else {
                        addTextTab(index, content.getText());
                    }
                }
            }
        }

        updateTabStyles();

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                currentPosition = (null != viewPager ? viewPager.getCurrentItem() + headers.size() : currentPosition);
                scrollToChild(currentPosition, 0);
            }
        });

        invalidate();
    }

    private void addTextTab(final int position, String title) {

        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();

        addTab(position, tab);
    }

    private void addIconTab(final int position, int resId) {

        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);

        addTab(position, tab);
    }

    @SuppressLint(value = "NewApi")
    private void addTab(final int position, final View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabClickListener != null)
                    tabClickListener.onClick(position);

                if (null != viewPager) {
                    if (position < headers.size()) { //headers
                        if (null != headerFooterClickListener) {
                            headerFooterClickListener.onHeaderClick(position);
                        }
                    } else if (position >= headers.size() && position < tabCount - footers.size()) {
                        viewPager.setCurrentItem(position - headers.size(), pagerSmoothScroll);
                    } else { //footers
                        if (null != headerFooterClickListener) {
                            headerFooterClickListener.onFooterClick(position - footers.size() - viewPager.getAdapter().getCount());
                        }
                    }
                } else {
                    currentPosition = _currentPosition = selectedPosition = position;
                    scrollToChild(position, 0);
                    if (position < headers.size()) { //headers
                        if (null != headerFooterClickListener) {
                            headerFooterClickListener.onHeaderClick(position);
                        }
                    } else { //footers
                        if (null != headerFooterClickListener) {
                            headerFooterClickListener.onFooterClick(position - footers.size());
                        }
                    }
                    updateTabStyles();
                }
            }
        });

        tab.setPadding(tabPadding, 0, tabPadding, 0);
        tabsContainer.addView(tab, position, isMatchWidth && tabCount <= maxMatchSize
                ? expandedTabLayoutParams : defaultTabLayoutParams);
    }

    /**
     * 手动调用tab点击
     *
     * @param index
     */
    public void tabClick(int index) {
        if (tabsContainer.getChildCount() > index) {
            View view = tabsContainer.getChildAt(index);
            if (view != null)
                view.callOnClick();
        }
    }

    private void updateTabStyles() {
        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);
            v.setBackgroundResource(tabBackgroundResId);
            if (v instanceof TextView) {
                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                tab.setTypeface(tabTypeface, tabTypefaceStyle);

                // pre-ICS-build
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    } else {
                        tab.setText(tab.getText().toString().toUpperCase(locale));
                    }
                }
                if (i == selectedPosition) {
                    tab.setTextColor(selectedTabTextColor);
                    if (selectedTextBold) tab.setTypeface(tabTypeface, Typeface.BOLD);
                } else {
                    tab.setTextColor(tabTextColor);
                }
            }
        }
    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            smoothScrollTo(newScrollX, 0);
        }
    }

    @Override
    @SuppressLint("NewApi")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        matchWidth = LayoutParams.MATCH_PARENT == matchWidth ? getMeasuredWidth() : matchWidth;
        if (0 < tabCount)
            expandedTabLayoutParams.width = matchWidth / tabCount;
        else
            expandedTabLayoutParams.width = matchWidth;

        if (isMatchWidth && tabCount <= maxMatchSize) {
            tabsContainer.getLayoutParams().width = matchWidth;
            for (int pos = 0; pos < tabCount; pos++) {
                tabsContainer.getChildAt(pos).setMinimumWidth(expandedTabLayoutParams.width);
            }
        }

        final int height = getHeight();
        // draw indicator line
        rectPaint.setColor(indicatorColor);
        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(_currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && _currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(_currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }

        if (maxLineWidth != 0 && lineRight - lineLeft > maxLineWidth) {
            if (isCursorAlignText & !isCursorRoundCorner) {
                canvas.drawRect(
                        lineLeft + (lineRight - lineLeft - maxLineWidth) / 2,
                        height - indicatorHeight - underlineMarginTop,
                        lineRight - (lineRight - lineLeft - maxLineWidth) / 2,
                        height - underlineMarginTop, rectPaint);
            } else if (isCursorRoundCorner & !isCursorAlignText) {
                rect.set(lineLeft + (lineRight - lineLeft - maxLineWidth) / 2,
                        height - indicatorHeight - underlineMarginTop,
                        lineRight - (lineRight - lineLeft - maxLineWidth) / 2,
                        height - underlineMarginTop);
                canvas.drawRoundRect(rect, cornerRadius, cornerRadius, rectPaint);
            } else if (isCursorRoundCorner & isCursorAlignText) {
                rect.set(lineLeft + (lineRight - lineLeft - maxLineWidth) / 2,
                        height - indicatorHeight - underlineMarginTop,
                        lineRight - (lineRight - lineLeft - maxLineWidth) / 2,
                        height - underlineMarginTop);
                canvas.drawRoundRect(rect, cornerRadius, cornerRadius, rectPaint);
            } else {
                rect.set(lineLeft + (lineRight - lineLeft - maxLineWidth) / 2,
                        height - indicatorHeight - underlineMarginTop,
                        lineRight - (lineRight - lineLeft - maxLineWidth) / 2,
                        height - underlineMarginTop);
                canvas.drawRoundRect(rect, cornerRadius, cornerRadius, rectPaint);
            }
        } else {
            if (isCursorAlignText & !isCursorRoundCorner) {
                canvas.drawRect(
                        lineLeft + tabPadding,
                        isIndicatorMatchHeight ? 0 : height - indicatorHeight - underlineMarginTop,
                        lineRight - underlineMarginTop - tabPadding,
                        height, rectPaint);
            } else if (isCursorRoundCorner & !isCursorAlignText) {
                rect.set(
                        (int) (lineLeft),
                        isIndicatorMatchHeight ? 0 : height - indicatorHeight - underlineMarginTop,
                        (int) (lineRight - underlineMarginTop),
                        height);
                canvas.drawRoundRect(rect, cornerRadius, cornerRadius, rectPaint);
            } else if (isCursorRoundCorner & isCursorAlignText) {
                rect.set(
                        (int) (lineLeft + tabPadding),
                        isIndicatorMatchHeight ? 0 : height - indicatorHeight - underlineMarginTop,
                        (int) (lineRight - underlineMarginTop - tabPadding),
                        height);
                canvas.drawRoundRect(rect, cornerRadius, cornerRadius, rectPaint);
            } else {
                canvas.drawRect(
                        lineLeft,
                        isIndicatorMatchHeight ? 0 : height - indicatorHeight - underlineMarginTop,
                        lineRight - underlineMarginTop,
                        height, rectPaint);
            }
        }

        // draw underline
        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

        // draw divider
        dividerPaint.setColor(dividerColor);
        dividerPaint.setStrokeWidth(dividerWidth);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(),
                    dividerPadding,
                    tab.getRight(),
                    isIndicatorMatchHeight ? height - dividerPadding :
                            height - dividerPadding - indicatorHeight,
                    dividerPaint);
        }
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public float getDividerWidth() {
        return dividerWidth;
    }

    public void setDividerWidth(float dividerWidth) {
        this.dividerWidth = dividerWidth;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public void enableMatchWidth(boolean matchWidth) {
        if (this.isMatchWidth != matchWidth) {
            this.isMatchWidth = matchWidth;
            notifyDataSetChanged();
            requestLayout();
        }
    }

    public void setMaxMatchSize(int maxMatchSize) {
        if (this.maxMatchSize != maxMatchSize) {
            this.maxMatchSize = maxMatchSize;
            notifyDataSetChanged();
        }
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    public boolean isIndicatorMatchHeight() {
        return isIndicatorMatchHeight;
    }

    public void setIsIndicatorMatchHeight(boolean isIndicatorMatchHeight) {
        this.isIndicatorMatchHeight = isIndicatorMatchHeight;
        updateTabStyles();
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        updateTabStyles();
    }

    public void setSelectedTextColor(int textColor) {
        this.selectedTabTextColor = textColor;
        updateTabStyles();
    }

    public void setSelectedTextColorResource(int resId) {
        this.selectedTabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public boolean isCursorAlignText() {
        return isCursorAlignText;
    }

    public void setIsCursorAlignText(boolean isCursorAlignText) {
        this.isCursorAlignText = isCursorAlignText;
        updateTabStyles();
    }

    public boolean isCursorRoundCorner() {
        return isCursorRoundCorner;
    }

    public void setIsCursorRoundCorner(boolean isCursorRoundCorner) {
        this.isCursorRoundCorner = isCursorRoundCorner;
        updateTabStyles();
    }

    public int getMatchWidth() {
        return matchWidth;
    }

    public void setMatchWidth(int matchWidth) {
        if (this.matchWidth != matchWidth) {
            this.matchWidth = LayoutParams.MATCH_PARENT == matchWidth ? getMeasuredWidth() : matchWidth;
            notifyDataSetChanged();
        }
    }

    public int getSelectedTextColor() {
        return selectedTabTextColor;
    }

    public synchronized void addHeader(TabContent... header) {
        headers.clear();
        headers.addAll(Arrays.asList(header));
        notifyDataSetChanged();
    }

    public synchronized void addFooter(TabContent... footer) {
        footers.clear();
        footers.addAll(Arrays.asList(footer));
        notifyDataSetChanged();
    }

    public void setHeaderFooterClickListener(HeaderFooterClickListener onClick) {
        this.headerFooterClickListener = onClick;
    }

    public void setTabClickListener(TabClickListener _tabClickListener){
        this.tabClickListener = _tabClickListener;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    private class InternalPagerListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            boolean isScrollToRight = positionOffset > currentPositionOffset;
            currentPosition = position + headers.size();
            _currentPosition = position + headers.size();
            if (isScrollToRight) {//向右滑
                if (positionOffset < 0.5f) {//还在左半屏

                } else {//到右边
                    currentPosition++;
                }
            } else {
                if (positionOffset > 0.5f) {//在右边
                    currentPosition++;
                } else {//到左半屏

                }
            }
            if (tabsContainer.getChildCount() <= _currentPosition) {
                notifyDataSetChanged();
            }
            currentPositionOffset = positionOffset;

            scrollToChild(
                    _currentPosition,
                    (int) (positionOffset * tabsContainer.getChildAt(_currentPosition).getWidth()));

            invalidate();
            if (pageListener != null) {
                pageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            selectedPosition = currentPosition;
            updateTabStyles();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(viewPager.getCurrentItem() + headers.size(), 0);
            }

            if (pageListener != null) {
                pageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            selectedPosition = position + headers.size();
            updateTabStyles();
            if (pageListener != null) {
                pageListener.onPageSelected(selectedPosition);
            }
        }

    }

    private final class InternalAdapterObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

    }

}
