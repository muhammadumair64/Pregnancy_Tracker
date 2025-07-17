package com.example.pregnancytrackerignite.data.horizontalCallender;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.pregnancytrackerignite.R;
import com.example.pregnancytrackerignite.data.models.DateModel;
import com.example.pregnancytrackerignite.data.utils.OnDateSelectListener;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HorizontalCalendar extends RelativeLayout {
    private RecyclerView mRecyclerView;
    private HorizontalCalendarAdapter mHorizontalCalendarAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ImageView mLeftArrowImageView;
    private ImageView mRightArrowImageView;
    private TextView mLabelTextView;
    private TextView mMonthAndDateTextView;
    private List<DateModel> mInputDates;
    private int mCenterChildPosition = -1;
    private LayoutClickListener mLayoutClickListener;
    private OnDateSelectListener mOnDateSelectListener;
    protected int mBgResourceId;
    protected int mSelectedBgResourceId;
    protected int mTextColorResourceId;
    protected int mSelectedTextColorResourceId;
    private DateFormatSymbols mDateFormatSymbols;
    private int mLanguage = 0;
    private int mIconsColorResourceId;
    protected String mDateFontStyle = "";
    protected int mDateTextSize = -1;
    protected int mWeekTextSize = -1;
    private Runnable mRunnable;

    public void setOnDateSelectListener(OnDateSelectListener onDateSelectListener) {
        mOnDateSelectListener = onDateSelectListener;
    }

    public void setForward() {
        // Ensure mCenterChildPosition is updated correctly
        if (mLayoutClickListener != null) {
            mCenterChildPosition = mLayoutClickListener.getSelection();
            Log.d("RecyclerView", "Current Center Position: " + mCenterChildPosition);

            // Ensure we're not at the last item
            if (mCenterChildPosition < mInputDates.size() - 1) {
                mCenterChildPosition++;  // Move to next item
                Log.d("RecyclerView", "New Center Position after Forward: " + mCenterChildPosition);

                // Smooth scroll to the next position
                mRecyclerView.smoothScrollToPosition(mCenterChildPosition);
            }

            // Notify the listener with the updated position
            mLayoutClickListener.onLayoutClick(mCenterChildPosition, mRecyclerView);
        }
    }


    public void setBackward() {
        if (mLayoutClickListener != null) {
            mCenterChildPosition = mLayoutClickListener.getSelection();
            if ((mCenterChildPosition == ((mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() + mLinearLayoutManager.findLastCompletelyVisibleItemPosition()) / 2) ||
                    mCenterChildPosition == mLinearLayoutManager.findFirstCompletelyVisibleItemPosition()) && mCenterChildPosition > 2) {
                mRecyclerView.scrollToPosition(mCenterChildPosition - 3);
            } else {
                mCenterChildPosition--;
                if (mCenterChildPosition >= 0) {
                    mRecyclerView.scrollToPosition(mCenterChildPosition);
                }
            }
            mLayoutClickListener.onLayoutClick(mCenterChildPosition, mRecyclerView);
        }
    }


    public void setTodaySelected() {
//        mHorizontalCalendarAdapter.setTodayAsSelected();
    }

    protected void setLayoutClickListener(LayoutClickListener layoutClickListener) {
        mLayoutClickListener = layoutClickListener;
        if (mHorizontalCalendarAdapter != null)
            mHorizontalCalendarAdapter.setOnDateSelectListener(new OnDateSelectListener() {
                @Override
                public void onSelect(DateModel dateModel) {
                    setMonthAndYear(dateModel);
                    if (mOnDateSelectListener != null && mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE)
                        mOnDateSelectListener.onSelect(dateModel);
                }
            });
    }

    public HorizontalCalendar(Context context) {
        super(context);
        init(context, null);
    }

    public HorizontalCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HorizontalCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_horizontal_calendar, this);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLeftArrowImageView = findViewById(R.id.leftArrow);
        mRightArrowImageView = findViewById(R.id.rightArrow);
        mLabelTextView = findViewById(R.id.labelTextView);
        mMonthAndDateTextView = findViewById(R.id.monthAndDateTextView);
        if (attrs == null) return;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HorizontalCalendar, 0, 0);
        int numberOfDays = typedArray.getInteger(R.styleable.HorizontalCalendar_numOfDays, 60);
        String label = typedArray.getString(R.styleable.HorizontalCalendar_setLabel);
        mBgResourceId = typedArray.getResourceId(R.styleable.HorizontalCalendar_setBgColor, R.drawable.rect_dark_gray);
        mIconsColorResourceId = typedArray.getResourceId(R.styleable.HorizontalCalendar_setIconsColor, R.color.selector_image_color);
        mTextColorResourceId = typedArray.getResourceId(R.styleable.HorizontalCalendar_setTextColor, R.color.dark_gray);
        mSelectedBgResourceId = typedArray.getResourceId(R.styleable.HorizontalCalendar_setSelectedBgColor, R.drawable.rect_green);
        mSelectedTextColorResourceId = typedArray.getResourceId(R.styleable.HorizontalCalendar_setSelectedTextColor, R.color.white);
        mLanguage = typedArray.getInt(R.styleable.HorizontalCalendar_setLanguage, 0);
        if (mIconsColorResourceId != 0) {
            mLeftArrowImageView.setImageTintList(AppCompatResources.getColorStateList(context, mIconsColorResourceId));
            mRightArrowImageView.setImageTintList(AppCompatResources.getColorStateList(context, mIconsColorResourceId));
        }
        if (typedArray.hasValue(R.styleable.HorizontalCalendar_setLabelColor)) mLabelTextView.setTextColor(ContextCompat.getColor(context, typedArray.getResourceId(R.styleable.HorizontalCalendar_setLabelColor, R.color.dark_gray)));
        mMonthAndDateTextView.setTextColor(ContextCompat.getColor(context, typedArray.getResourceId(R.styleable.HorizontalCalendar_setMonthColor, R.color.black)));
        if (typedArray.hasValue(R.styleable.HorizontalCalendar_setLabelTextSize))
            mLabelTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.HorizontalCalendar_setLabelTextSize, 13));
        if (typedArray.hasValue(R.styleable.HorizontalCalendar_setMonthTextSize))
            mMonthAndDateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.HorizontalCalendar_setMonthTextSize, 15));
        if (typedArray.hasValue(R.styleable.HorizontalCalendar_setDateTextSize)) mDateTextSize = typedArray.getDimensionPixelSize(R.styleable.HorizontalCalendar_setDateTextSize, 22);
        if (typedArray.hasValue(R.styleable.HorizontalCalendar_setWeekTextSize)) mWeekTextSize = typedArray.getDimensionPixelSize(R.styleable.HorizontalCalendar_setWeekTextSize, 12);
        mLeftArrowImageView.setEnabled(false);
        mRightArrowImageView.setEnabled(numberOfDays > 5);
        setCalender(numberOfDays);
        setLabel(label);
    }

    private void setCalender(int noOfDays) {
        Calendar calendar = Calendar.getInstance();

        // Set the date to the 1st day of the previous month
        calendar.add(Calendar.MONTH, -1); // Move to the previous month
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Set to the 1st day of the month
        Date startDate = calendar.getTime(); // Store this start date

        if (mInputDates != null) {
            mInputDates.clear();
        }
        if (mInputDates == null) {
            mInputDates = new ArrayList<>();
        }

        for (int index = 0; index < noOfDays; index++) {
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, index); // Increment days starting from startDate

            DateModel dateModel = new DateModel();
            dateModel.day = calendar.get(Calendar.DAY_OF_MONTH);

            dateModel.dayOfWeek = titleCase(getDateFormatSymbols().getShortWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)]);
            if (dateModel.dayOfWeek != null && !dateModel.dayOfWeek.isEmpty() && dateModel.dayOfWeek.length() > 4) {
                dateModel.dayOfWeek = dateModel.dayOfWeek.substring(0, 3);
            }

            dateModel.month = titleCase(getDateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)]);
            dateModel.year = calendar.get(Calendar.YEAR);
            dateModel.monthNumber = calendar.get(Calendar.MONTH) + 1;

            mInputDates.add(dateModel);
        }
        setData();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setData() {
        if (mLinearLayoutManager == null)
            mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        if (mHorizontalCalendarAdapter == null)
            mHorizontalCalendarAdapter = new HorizontalCalendarAdapter();
        mHorizontalCalendarAdapter.setData(mInputDates, this);
        mRecyclerView.setAdapter(mHorizontalCalendarAdapter);
        final SnapHelper snapHelper = new LinearSnapHelper();
        if (mRecyclerView.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(mRecyclerView);
        else mRecyclerView.setOnFlingListener(snapHelper);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getOnFlingListener() != null) {
                    if (recyclerView.getLayoutManager() != null) {
                        View centerView = snapHelper.findSnapView(recyclerView.getLayoutManager());
                        if (centerView != null)
                            mCenterChildPosition = recyclerView.getLayoutManager().getPosition(centerView);
                    }
                } else recyclerView.setOnFlingListener(snapHelper);
                // to prevent mutliple calls
                if (mLayoutClickListener != null && newState != RecyclerView.SCROLL_STATE_DRAGGING)
                    mLayoutClickListener.onLayoutClick(mCenterChildPosition, recyclerView);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int center = mCenterChildPosition;
                if (recyclerView.getOnFlingListener() != null) {
                    if (recyclerView.getLayoutManager() != null) {
                        View centerView = snapHelper.findSnapView(recyclerView.getLayoutManager());
                        if (centerView != null)
                            mCenterChildPosition = recyclerView.getLayoutManager().getPosition(centerView);
                    }
                } else recyclerView.setOnFlingListener(snapHelper);
                mLeftArrowImageView.setEnabled(mInputDates.size() > 5 && mCenterChildPosition > 2);
                mRightArrowImageView.setEnabled(mInputDates.size() > 5 && mCenterChildPosition < mInputDates.size() - 3);
                // to prevent mutliple calls
                if (mLayoutClickListener != null && mCenterChildPosition != center)
                    mLayoutClickListener.onLayoutClick(mCenterChildPosition, recyclerView);
            }
        });
        mRightArrowImageView.setOnClickListener(v -> setForward());
        mLeftArrowImageView.setOnClickListener(v -> setBackward());
        mRightArrowImageView.setLongClickable(true);
        mLeftArrowImageView.setLongClickable(true);
        mLeftArrowImageView.setOnLongClickListener(view -> {
            if (mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
                stopHandler(mLeftArrowImageView, mRunnable);
                createRunning(mLeftArrowImageView, -1);
            }
            return false;
        });
        mLeftArrowImageView.setOnTouchListener((v, event) -> {
           /* if (event.getAction() == MotionEvent.ACTION_DOWN)
             handler.postDelayed(longPressedLeft, 500);*/
            if (event.getAction() == MotionEvent.ACTION_UP) {
                stopHandler(mLeftArrowImageView, mRunnable);
            }
            return false;
        });
        mRightArrowImageView.setOnLongClickListener(view -> {
            if (mLinearLayoutManager.findLastCompletelyVisibleItemPosition() != mInputDates.size() - 1) {
                stopHandler(mRightArrowImageView, mRunnable);
                createRunning(mRightArrowImageView, 1);
            }
            return false;
        });
        mRightArrowImageView.setOnTouchListener((v, event) -> {
            /*if (event.getAction() == MotionEvent.ACTION_DOWN)
                handler.postDelayed(longPressedRight, 500);*/
            if (event.getAction() == MotionEvent.ACTION_UP) {
                stopHandler(mRightArrowImageView, mRunnable);
            }
            return false;
        });

        scrollToItem();
    }

    private void scrollToItem() {
        Calendar calendar = Calendar.getInstance();
        int todayDay = calendar.get(Calendar.DAY_OF_MONTH);
        String todayMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int todayYear = calendar.get(Calendar.YEAR);

// Find the index of today's date accurately
       int mRowIndex = -1;
        for (int i = 0; i < mInputDates.size(); i++) {
            DateModel dateModel = mInputDates.get(i);
            if (dateModel.day == todayDay &&
                    dateModel.month.equalsIgnoreCase(todayMonth) &&
                    dateModel.year == todayYear) {
                mRowIndex = i;
                break;
            }
        }

// Verify if mRowIndex has been set correctly and is in range
        if (mRowIndex != -1) {
            mInputDates.size();
            mRecyclerView.scrollToPosition(mRowIndex -2);
        }
    }

    protected interface LayoutClickListener {
        void onLayoutClick(int position);

        void onLayoutClick(int position, RecyclerView recyclerView);

        int getSelection();
    }

    private void setMonthAndYear(DateModel dateModel) {
        String monthAndYear = "";
        if (dateModel != null)
            monthAndYear = dateModel.month + " " + dateModel.year;
        if (!monthAndYear.equalsIgnoreCase(mMonthAndDateTextView.getText() + ""))
            mMonthAndDateTextView.setText(monthAndYear);
    }

    public void setLabel(String label) {
        mLabelTextView.setVisibility((label == null || label.equals("null") || label.isEmpty()) ? GONE : VISIBLE);
        mLabelTextView.setText(label);
    }

    private DateFormatSymbols getDateFormatSymbols() {
        if (mDateFormatSymbols == null) {
            if (mLanguage == 1)
                mDateFormatSymbols = new DateFormatSymbols(Locale.ENGLISH);
            else mDateFormatSymbols = new DateFormatSymbols();
        }
        return mDateFormatSymbols;
    }

    private String titleCase(String text) {
        if (text != null && !text.isEmpty())
            text = text.replace(text.charAt(0), Character.toUpperCase(text.charAt(0)));
        return text;
    }

    private void stopHandler(View view, Runnable action) {
        try {
            if (view != null) {
                if (action != null) {
                    view.getHandler().removeCallbacks(action);
                    view.removeCallbacks(action);
                    removeCallbacks(action);
                }
                view.getHandler().removeCallbacksAndMessages(null);
            }
            if (action != null)
                this.getHandler().removeCallbacks(action);
            this.getHandler().removeCallbacksAndMessages(null);
        } catch (Exception ignored) {
        }
        action = null;
        mRunnable = null;
    }

    private void createRunning(View view, int num) {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (!view.isEnabled()) {
                    stopHandler(view, this);
                } else {
                    if (num > 0)
                        mRecyclerView.scrollToPosition(mLinearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                    else
                        mRecyclerView.scrollToPosition(mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() - 1);
                    view.post(this);
                }
            }
        };
        view.post(mRunnable);
    }


}
