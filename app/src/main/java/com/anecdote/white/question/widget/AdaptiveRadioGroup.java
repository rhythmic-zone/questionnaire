package com.anecdote.white.question.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.anecdote.white.question.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A adaptive radio group widget
 */
public class AdaptiveRadioGroup extends ViewGroup {

    protected static final String TAG = "wrapperitem";

    /**
     * the default horizontal margin between each item
     */
    private static final int DEFAULT_ITEM_HORIZAL_MARGIN = 0;
    /**
     * the default vertical margin between each item
     */
    private static final int DEFAULT_ITEM_VERTICAL_MARGIN = 1;
    /**
     * the default font color
     * R.drawable.selector_button_textcolor
     */
    private static final int DEFAULT_FONT_COLOR = 0;
    /**
     * the default horizontal padding for each item
     */
    private static final int DEFAULT_HORIZAL_PADDING = 16;
    /**
     * the default vertical padding for each item
     */
    private static final int DEFAULT_VERTICAL_PADDING = 8;

    /**
     * the default font size
     */
    private static final int DEFAULT_FONT_SIZE = 16;

    private int itemBackground;
    private int horizalMargin;
    private int verticalMargin;
    private int horizalPadding;
    private int verticalPadding;
    private float fontSize;
    private int textColor;
    private int rows;
    private int columns;
    private int realChildWidth = 0;
    private boolean isRadioMode = true;

    private List<Item> content;

    public AdaptiveRadioGroup(Context context) {
        super(context);
        initComponent(-1);
    }

    public AdaptiveRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttribute(attrs);
        initComponent(-1);
    }

    /**
     * @param content 显示的内容列表
     */
    public void setContent(List<Item> content) {
        this.content = content;
        initComponent(-1);
    }

    /**
     * @param content             显示的内容列表
     * @param defaultCheckedIndex 默认选中的序号 0---(content.size()-1)}
     */
    public void setContent(List<Item> content, int defaultCheckedIndex) {
        this.content = content;
        initComponent(defaultCheckedIndex);
    }

    public void setContent(List<Item> content, List<Integer> defaultCheckedIndexs) {
        this.content = content;
        initComponent(defaultCheckedIndexs);
    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.adaptiveRadioButton);
        // TODO 字体单位默认使用SP，getDimension不被推荐使用
        fontSize = array.getDimension(R.styleable.adaptiveRadioButton_textsize, DEFAULT_FONT_SIZE);
        textColor = array.getColor(R.styleable.adaptiveRadioButton_textcolor, DEFAULT_FONT_COLOR);
        verticalMargin = (int) array.getDimension(R.styleable.adaptiveRadioButton_verticalMargin,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_ITEM_VERTICAL_MARGIN, getContext().getResources().getDisplayMetrics()));
        horizalMargin = (int) array.getDimension(R.styleable.adaptiveRadioButton_horizalMargin,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_ITEM_HORIZAL_MARGIN, getContext().getResources().getDisplayMetrics()));
        verticalPadding = (int) array.getDimension(R.styleable.adaptiveRadioButton_verticalPadding,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_VERTICAL_PADDING, getContext().getResources().getDisplayMetrics()));
        horizalPadding = (int) array.getDimension(R.styleable.adaptiveRadioButton_horizalPadding,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HORIZAL_PADDING, getContext().getResources().getDisplayMetrics()));
        itemBackground = array.getResourceId(R.styleable.adaptiveRadioButton_itemBackGround, 0);
        Log.d("main", "fontSize:" + fontSize + " ,verticalMargin:" + verticalMargin + "  verticalPadding" + verticalPadding + "  horizontalMargin:" + horizalMargin + "  horizontal Padding"
                + horizalPadding + " ");
        array.recycle();

    }

    private void initComponent(int defaultIndex) {
        List<Integer> list = new ArrayList<>();
        list.add(defaultIndex);
        initComponent(list);
    }


    private void initComponent(List<Integer> defaultIndexs) {
        removeAllViews();
        if (content == null || content.size() == 0) {
            Item item = new Item();
            item.setDescription("");
            item.setValue("B");
            item.setDisplayContent(item.getValue() + "、" + item.getDescription());
            addItem(item, false);

            item = new Item();
            item.setDescription("");
            item.setValue("A");
            item.setDisplayContent(item.getValue() + "、" + item.getDescription());
            addItem(item, true);
        } else {
            for (int i = 0; i < content.size(); i++) {
                if (defaultIndexs != null && defaultIndexs.contains(i))
                    addItem(content.get(i), true);
                else
                    addItem(content.get(i), false);
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void addItem(@NonNull final Item item, boolean isChecked) {

        if (item.isEdit) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            final LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            final EditText editText = new EditText(getContext());
            params.weight = 1;
            editText.setLayoutParams(params);
            editText.setTag("___");
            editText.setText(item.getEditText());
            editText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.requestFocus();
                }
            });
            editText.setPadding(horizalPadding, verticalPadding, horizalPadding, verticalPadding);
            Button button = new Button(getContext());
            button.setText("confirm");
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFocusable(true);
                    setFocusableInTouchMode(true);
                    requestFocus();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindowToken(), 0);
                    item.setValue(editText.getText().toString());
                    item.setEditText(editText.getText().toString());
                    callBackAll();
                    callItemClickBack();
                }
            });
            linearLayout.addView(editText);
            linearLayout.addView(button);
            linearLayout.setLayoutParams(params1);
            addView(linearLayout);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            CheckBox cb = new CheckBox(getContext());
            cb.setText(item.getDisplayContent());
            cb.setTag(item.getValue());
            if (textColor != 0) {
                cb.setTextColor(textColor);
            } else {
                ColorStateList csl = getResources().getColorStateList(R.color.selector_qa_text_blackandwhite);
                cb.setTextColor(csl);
            }
            cb.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            cb.setChecked(isChecked);
            cb.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            cb.setCompoundDrawables(null, null, null, null);
            cb.setPadding(horizalPadding, verticalPadding, horizalPadding, verticalPadding);
            cb.setButtonDrawable(android.R.color.transparent);

            if (itemBackground != 0) {
                cb.setBackgroundResource(itemBackground);
            } else {
                StateListDrawable sd = new StateListDrawable();
                sd.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(Color.parseColor("#38D2D4")));
                sd.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(Color.parseColor("#38D2D4")));
                sd.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(Color.parseColor("#38D2D4")));
                sd.addState(new int[]{}, new ColorDrawable(Color.parseColor("#FAFAFA")));
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) cb.setBackground(sd);
                else
                    cb.setBackgroundDrawable(sd);
            }

            cb.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (v instanceof CheckBox) {
                        if (isRadioMode)
                            clearAllButThis((CheckBox) v);
                        else {
                            callBackAll();
                        }
                        callItemClickBack();
                    }
                }
            });
            cb.setLayoutParams(params);
            addView(cb);
        }

    }

    private void clearAllButThis(CheckBox cb) {
        clearAll(this);
        cb.setChecked(true);
        if (mOnCheckedValueChangeListener != null)
            mOnCheckedValueChangeListener.onCheckedValueChange(getItemByValue((String) cb.getTag()));
    }

    @SuppressWarnings("must be last called")
    private void callItemClickBack() {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick();
        }
    }

    private void callBackAll() {
        if (onCheckedObtainAllValueListener != null) {
            callBackList.clear();
            obtainItem(this);
            onCheckedObtainAllValueListener.onObtainAllValue(callBackList);
        }
    }

    private void clearAll(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof CheckBox) {
                ((CheckBox) viewGroup.getChildAt(i)).setChecked(false);
            } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                clearAll((ViewGroup) viewGroup.getChildAt(i));
            }
        }
    }

    private List<Item> callBackList = new ArrayList<>();

    private void obtainItem(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) viewGroup.getChildAt(i);
                if (checkBox.isChecked() && getItemByValue((String) checkBox.getTag()) != null)
                    callBackList.add(getItemByValue((String) checkBox.getTag()));
            } else if (viewGroup.getChildAt(i) instanceof EditText) {
                EditText editText = (EditText) viewGroup.getChildAt(i);
                if (editText != null && editText.getText() != null && !TextUtils.isEmpty(editText.getText().toString()) && getItemByMode(true) != null) {
                    callBackList.add(getItemByMode(true));
                }
            } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                obtainItem((ViewGroup) viewGroup.getChildAt(i));
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int exactWidth = MeasureSpec.getSize(widthMeasureSpec);
        int childW = MeasureSpec.makeMeasureSpec(exactWidth - 2 * (horizalMargin), MeasureSpec.EXACTLY);
        int childH = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.UNSPECIFIED);
        measureChildren(childW, childH);
        int childMaxWidth = 0, childHeightCounts = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childMaxWidth < childView.getMeasuredWidth())
                childMaxWidth = childView.getMeasuredWidth();
        }
        // rows = (exactWidth - horizalMargin) / (childMaxWidth);
        rows = horizalMargin / childMaxWidth;
        if (rows > getChildCount()) {
            rows = getChildCount();
        } else if (rows <= 0) rows = 1;
        columns = (int) Math.ceil(((float) getChildCount()) / rows);
        int lastAddHeight = 0;
        int currentValue = 0;
        for (int i = 0; i < getChildCount(); i++) {
            if (currentValue != (i / rows)) {
                lastAddHeight = 0;
            }
            currentValue = i / rows;
            if (getChildAt(i).getMeasuredHeight() > lastAddHeight) {
                childHeightCounts -= lastAddHeight;
                childHeightCounts += getChildAt(i).getMeasuredHeight();
                lastAddHeight = getChildAt(i).getMeasuredHeight();
            }
        }
        int realHeight = (columns + 1) * verticalMargin + childHeightCounts;
        realChildWidth = (exactWidth - (rows + 1) * horizalMargin) / rows;
        setMeasuredDimension(exactWidth, realHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childHeightCounts = 0;
        for (int i = 0; i < getChildCount(); i++) {
            int left = 0, right = 0, top = 0, bottom = 0;
            View childView = getChildAt(i);
            int xPos = i % rows;
            int yPos = i / rows;
            int realEachHeight = getMaxHeightPerColumn(i / rows * rows);
            left = (xPos + 1) * horizalMargin + xPos * realChildWidth;
            right = left + realChildWidth;
            top = (yPos + 1) * verticalMargin + childHeightCounts;
            bottom = top + realEachHeight;

            childView.layout(left, top, right, bottom);

            if (rows == 1 || (i != 0 && i % rows == (rows - 1))) {
                childHeightCounts += realEachHeight;
            }

        }
    }

    private int getMaxHeightPerColumn(int startChildIndex) {
        int maxHeight = 0;
        if (startChildIndex >= getChildCount()) return 0;
        for (int i = startChildIndex; i < startChildIndex + rows; i++) {
            if (getChildAt(i) == null) {
                return maxHeight;
            }
            if (getChildAt(i).getMeasuredHeight() > maxHeight) {
                maxHeight = getChildAt(i).getMeasuredHeight();
            }
        }
        return maxHeight;
    }


    private OnCheckedValueChangeListener mOnCheckedValueChangeListener;
    private OnCheckedObtainAllValueListener onCheckedObtainAllValueListener;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnCheckedValueChangeListener(OnCheckedValueChangeListener mOnCheckedValueChangeListener) {
        this.mOnCheckedValueChangeListener = mOnCheckedValueChangeListener;
    }

    public void setOnCheckedObtainAllValueListener(OnCheckedObtainAllValueListener onCheckedObtainAllValueListener) {
        this.onCheckedObtainAllValueListener = onCheckedObtainAllValueListener;
    }

    public void setRadioMode(boolean isRadioMode) {
        this.isRadioMode = isRadioMode;
    }

    public interface OnCheckedValueChangeListener {
        void onCheckedValueChange(Item item);
    }

    public interface OnCheckedObtainAllValueListener {
        void onObtainAllValue(List<Item> items);
    }

    public interface OnItemClickListener {
        void onItemClick();
    }

    /**
     * if the value has reduplicated value ,it will find the first item.
     *
     * @return
     */
    private Item getItemByValue(String value) {
        if (content == null || content.size() == 0 || value == null) return null;
        for (Item item : content) {
            if (item.getValue().equals(value)) return item;
        }
        return null;
    }

    private Item getItemByMode(boolean isEdit) {
        if (content == null || content.size() == 0) return null;
        for (Item item : content) {
            if (item.isEdit == isEdit) return item;
        }
        return null;
    }


    public class Item {
        private String value;
        private String description;
        private String displayContent;
        private boolean isEdit = false;
        private String editText = "";

        public Item setIsEdit(boolean isEdit) {
            this.isEdit = isEdit;
            return this;
        }

        public boolean isEdit() {
            return this.isEdit;
        }

        /**
         * @return
         */
        public String getValue() {
            return value;
        }

        /**
         * the value should be unique so that can search for it correctly
         *
         * @param value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * @return
         */
        public String getDescription() {
            return description;
        }

        /**
         * @param description
         */
        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * 展示在页面上的内容
         *
         * @return
         */
        public String getDisplayContent() {
            return displayContent;
        }

        /**
         * 设置展示在页面上的内容
         *
         * @param displayContent
         */
        public void setDisplayContent(String displayContent) {
            this.displayContent = displayContent;
        }

        public String getEditText() {
            return editText;
        }

        public Item setEditText(String editText) {
            this.editText = editText;
            return this;
        }
    }

}
