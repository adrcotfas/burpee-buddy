package com.apps.adrcotfas.burpeebuddy.main.edit_exercises.item;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.adrcotfas.burpeebuddy.R;
import com.apps.adrcotfas.burpeebuddy.common.viewmvc.BaseObservableViewMvc;
import com.apps.adrcotfas.burpeebuddy.db.exercise.Exercise;

public class ExercisesItemViewMvcImpl extends BaseObservableViewMvc<ExercisesItemViewMvc.Listener>
        implements ExercisesItemViewMvc {

    private final RelativeLayout mItem;
    private final TextView mTitle;
    private Exercise mExercise;
    private final ImageView mVisibilityButton;
    private final FrameLayout mScroll;

    public ExercisesItemViewMvcImpl(LayoutInflater inflater, ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.view_exercise_list_item, parent, false));

        mItem = findViewById(R.id.item);
        mScroll = findViewById(R.id.scroll_icon_container);
        mTitle = findViewById(R.id.title);
        mVisibilityButton = findViewById(R.id.visibility_icon);
        findViewById(R.id.visibility_icon_container).setOnClickListener(v -> {
            mVisibilityButton.setImageDrawable(getDrawable(mExercise.visible
                    ? R.drawable.ic_check_off
                    : R.drawable.ic_check));

            for (Listener l : getListeners()) {
                l.onVisibilityToggle(mExercise.name, !mExercise.visible);
            }
        });

        findViewById(R.id.edit).setOnClickListener(v -> {
            //TODO: open dialog, when the OK button is clicked, call l.onExerciseEdit
//            for (Listener l : getListeners()) {
//                l.onExerciseEdit(mExercise);
//            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void bindExercise(Exercise exercise) {
        mExercise = exercise;
        mTitle.setText(exercise.name);
        mVisibilityButton.setImageDrawable(getDrawable(mExercise.visible
                ? R.drawable.ic_check
                : R.drawable.ic_check_off));
    }

    @Override
    public FrameLayout getScrollHandle() {
        return mScroll;
    }

    @Override
    public RelativeLayout getItem() {
        return mItem;
    }
}
