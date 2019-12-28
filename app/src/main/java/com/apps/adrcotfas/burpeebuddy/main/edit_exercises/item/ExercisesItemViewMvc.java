package com.apps.adrcotfas.burpeebuddy.main.edit_exercises.item;

import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.apps.adrcotfas.burpeebuddy.common.viewmvc.ObservableViewMvc;
import com.apps.adrcotfas.burpeebuddy.db.exercise.Exercise;

public interface ExercisesItemViewMvc extends ObservableViewMvc<ExercisesItemViewMvc.Listener> {

    public interface Listener {
        void onExerciseEdit(String exercise, Exercise newExercise);
        void onVisibilityToggle(String exercise, boolean visible);
    }

    void bindExercise(Exercise exercise);

    /**
     * Returns the scroll handle used for rearranging the items inside the list
     */
    FrameLayout getScrollHandle();

    /**
     * Returns the parent layout
     */
    RelativeLayout getItem();
}
