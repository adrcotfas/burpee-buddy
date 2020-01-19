package com.apps.adrcotfas.burpeebuddy.edit_exercises;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.apps.adrcotfas.burpeebuddy.R;
import com.apps.adrcotfas.burpeebuddy.common.Events;
import com.apps.adrcotfas.burpeebuddy.db.AppDatabase;
import com.apps.adrcotfas.burpeebuddy.db.exercise.Exercise;
import com.apps.adrcotfas.burpeebuddy.edit_exercises.dialog.AddEditExerciseDialog;
import com.apps.adrcotfas.burpeebuddy.edit_exercises.view.ExercisesViewMvc;
import com.apps.adrcotfas.burpeebuddy.edit_exercises.view.ExercisesViewMvcImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class ExercisesFragment extends Fragment implements ExercisesViewMvc.Listener {
    private static final String TAG = "ExercisesFragment";

    private ExercisesViewMvc mViewMvc;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mViewMvc = new ExercisesViewMvcImpl(inflater, container);

        AppDatabase.getDatabase(getContext()).exerciseDao().getAll().observe(
                getViewLifecycleOwner(), exercises ->
                        mViewMvc.bindExercises(exercises));
        setHasOptionsMenu(true);
        return mViewMvc.getRootView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewMvc.registerListener(this);
    }

    @Override
    public void onDestroy() {
        if (mViewMvc != null){
            mViewMvc.unregisterListener(this);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu,
                                    MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_stuff, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            AddEditExerciseDialog.getInstance(null, false)
                    .show(getActivity().getSupportFragmentManager(), TAG);
            return true;
        }
        return false;
    }

    @Override
    public void onVisibilityToggle(String exercise, boolean visibility) {
        AppDatabase.editVisibility(getContext(), exercise, visibility);
    }

    //TODO: to avoid clipping it may be needed to call this when the user navigates back
    // instead of every time the user rearranges a row
    @Override
    public void onExercisesRearranged(List<Exercise> exercises) {
        for (int i = 0; i < exercises.size(); ++i) {
            AppDatabase.editExerciseOrder(getContext(), exercises.get(i).name, i);
        }
    }

    @Override
    public void onExerciseEditClicked(Exercise exercise) {
        AddEditExerciseDialog.getInstance(exercise, true)
                .show(getActivity().getSupportFragmentManager(), TAG);
    }

    @Subscribe
    public void onMessageEvent(Events.EditExercise event) {
        AppDatabase.editExercise(getContext(), event.exerciseToEdit, event.exercise);
    }

    @Subscribe
    public void onMessageEvent(Events.AddExercise event) {
        AppDatabase.addExercise(getContext(), event.exercise);
    }

    @Subscribe
    public void onMessageEvent(Events.DeleteExercise event) {
        AppDatabase.deleteExercise(getContext(), event.name);
    }
}
