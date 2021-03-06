package com.apps.adrcotfas.burpeebuddy.main.view;

import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;

import com.apps.adrcotfas.burpeebuddy.common.viewmvc.ObservableView;
import com.apps.adrcotfas.burpeebuddy.db.challenge.Challenge;
import com.apps.adrcotfas.burpeebuddy.db.exercise.Exercise;
import com.apps.adrcotfas.burpeebuddy.db.goal.Goal;

import java.util.List;

public interface MainView extends ObservableView<MainView.Listener> {

    void showIntroduction();

    void updateExercises(List<Exercise> exercises);
    void updateGoals(List<Goal> goals);
    void updateChallenges(List<Pair<Challenge, Integer>> challenges);

    /**
     * Returns the currently selected exercise
     */
    MutableLiveData<Exercise> getExercise();

    /**
     * Returns the currently selected goal
     */
    Goal getGoal();

    void toggleStartButtonState(boolean state);

    public interface Listener {
        void onStartButtonClicked();

        void onEditChallengesClicked();
        void onEditExercisesClicked();
        void onEditGoalsClicked();
    }
}
