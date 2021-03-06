package com.apps.adrcotfas.burpeebuddy.workout.view;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.DialogFragment;

import com.apps.adrcotfas.burpeebuddy.R;
import com.apps.adrcotfas.burpeebuddy.common.BuddyApplication;
import com.apps.adrcotfas.burpeebuddy.common.Events;
import com.apps.adrcotfas.burpeebuddy.db.exercise.ExerciseType;
import com.apps.adrcotfas.burpeebuddy.db.goal.GoalType;
import com.apps.adrcotfas.burpeebuddy.settings.SettingsHelper;
import com.apps.adrcotfas.burpeebuddy.workout.manager.InProgressWorkout;
import com.apps.adrcotfas.burpeebuddy.workout.manager.State;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.EventBus;

import static com.apps.adrcotfas.burpeebuddy.common.BuddyApplication.BREAK_DURATION_FACTOR;
import static com.apps.adrcotfas.burpeebuddy.db.goal.GoalToString.formatSeconds;

public class SetFinishedDialog extends DialogFragment {

    private InProgressWorkout getWorkout() {
        return BuddyApplication.getWorkoutManager().getWorkout();
    }

    private int mBreakDuration;
    private MaterialAlertDialogBuilder mBuilder;

    @Override
    public final Dialog onCreateDialog(Bundle savedInstBundle) {
        setCancelable(false);

        mBreakDuration = getWorkout().getGoalDurationBreak();

        mBuilder = new MaterialAlertDialogBuilder(getActivity());
        final View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_set_finished, null, false);

        setupButtonsAndTitle(v);
        setupAutoStartBreakCheckbox(v);

        if ((getWorkout().getExerciseType() == ExerciseType.UNCOUNTABLE)
                && getWorkout().getGoalType() == GoalType.TIME) {
            v.findViewById(R.id.reps_container).setVisibility(View.VISIBLE);
            setupRepsEditText(v);
            setupModifierButtons(v);
        }

        v.findViewById(R.id.congratulations).setVisibility(getWorkout().isFinalSet() ? View.VISIBLE : View.GONE);

        //TODO: add skip break button
        Dialog d = mBuilder
                .setView(v)
                .create();
        d.setCanceledOnTouchOutside(false);
        return d;
    }

    private void setupAutoStartBreakCheckbox(View v) {
        final MaterialCheckBox checkBox = v.findViewById(R.id.auto_break_checkbox);

        if (SettingsHelper.autoStartBreak(getWorkout().getExerciseType())
                || (getWorkout().getExerciseType() == ExerciseType.UNCOUNTABLE
                    && getWorkout().getGoalType() == GoalType.TIME)
                || getWorkout().isFinalSet()) {
            checkBox.setVisibility(View.GONE);
            return;
        } else {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked)
                    -> SettingsHelper.setAutoStartBreak(getWorkout().getExerciseType(), isChecked));
        }
        switch (getWorkout().getExerciseType()) {
            case TIME_BASED:
                checkBox.setText(R.string.auto_start_break_time_based);
                break;
            case COUNTABLE:
                checkBox.setText(R.string.auto_start_break_countable);
                break;
            case UNCOUNTABLE:
                checkBox.setText(R.string.auto_start_break_uncountable);
                break;
            case INVALID:
                break;
        }
    }

    private void setupButtonsAndTitle(View v) {
        switch (getWorkout().getState()) {
            case SET_FINISHED:
                setupBreakSeekbar(v);
                mBuilder.setTitle(getTitle())
                        .setPositiveButton(getString(R.string.dialog_start_break),
                                (dialog, which) -> {
                                    getWorkout().incrementCurrentSet();
                                    EventBus.getDefault().post(new Events.StartBreak(mBreakDuration));
                                });
                break;
            case WORKOUT_FINISHED:
                mBuilder.setTitle(getTitle())
                        .setPositiveButton(android.R.string.ok, (d, w) -> {
                            onFinalSetOkClicked();
                        });
                break;
            default:
                // do nothing
        }
    }

    @NonNull
    private String getTitle() {
        if (SettingsHelper.autoStartBreak(getWorkout().getExerciseType())) {
            return "Workout finished";
        }
        return getString(R.string.dialog_set_finished) + "(" + getWorkout().getCurrentSet() + "/" + getWorkout().getGoalSets() + ")";
    }

    private void onFinalSetOkClicked() {
        getWorkout().setState(State.WORKOUT_FINISHED_IDLE);
        EventBus.getDefault().post(new Events.FinishedWorkoutIdle());
    }

    private void setupBreakSeekbar(View v) {
        LinearLayout breakContainer = v.findViewById(R.id.break_container);
        breakContainer.setVisibility(View.VISIBLE);

        AppCompatSeekBar breakSeekbar = v.findViewById(R.id.break_seekbar);
        TextView breakDesc = v.findViewById(R.id.break_value);
        breakSeekbar.setProgress(getWorkout().getGoalDurationBreak() / BREAK_DURATION_FACTOR);

        breakSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final int seconds = progress * BREAK_DURATION_FACTOR;
                mBreakDuration = seconds;
                breakDesc.setText(formatBreakDesc(seconds));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){}
        });

        if (mBreakDuration / BREAK_DURATION_FACTOR == breakSeekbar.getProgress()) {
            breakDesc.setText(formatBreakDesc(mBreakDuration));
        }
    }

    private void setupModifierButtons(View v) {
        final TextInputEditText repsEditText = v.findViewById(R.id.reps);
        MaterialButton plus_1 = v.findViewById(R.id.reps_plus_one);
        MaterialButton minus_1 = v.findViewById(R.id.reps_minus_one);
        MaterialButton multiply_2 = v.findViewById(R.id.reps_multiply_2);

        // TODO: extract limit of 500 to preferences
        plus_1.setOnClickListener(v1 -> {
            String s = repsEditText.getText().toString();
            final int value = s.equals("") ? 0 : Integer.valueOf(s);

            repsEditText.setText(value == 499
                    ? String.valueOf(500)
                    : String.valueOf(value + 1));
        });
        minus_1.setOnClickListener(v1 -> {
            String s = repsEditText.getText().toString();
            final int value = s.equals("") ? 0 : Integer.valueOf(s);
            repsEditText.setText(value == 0
                    ? String.valueOf(0)
                    : String.valueOf(value - 1));
        });
        multiply_2.setOnClickListener(v1 -> {
            String s = repsEditText.getText().toString();
            final int value = s.equals("") ? 0 : Integer.valueOf(s);
            repsEditText.setText(value >= 250
                    ? String.valueOf(500)
                    : String.valueOf(value * 2));
        });
    }

    private void setupRepsEditText(View v) {
        TextInputLayout repsEditTextLayout = v.findViewById(R.id.reps_layout);
        TextInputEditText repsEditText = v.findViewById(R.id.reps);
        repsEditText.setText(String.valueOf(getWorkout().getCurrentReps()));

        repsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                AlertDialog dialog = (AlertDialog) getDialog();
                int value = 0;
                if (s.length() == 0) {
                    repsEditTextLayout.setError(getString(R.string.goal_type_reps));
                    if (dialog != null) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    }
                } else {
                    repsEditTextLayout.setError(null);
                    if (dialog != null) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                    value = Integer.valueOf(s.toString());
                }
                getWorkout().updateCurrentReps(value);
            }
        });
    }

    @NonNull
    private String formatBreakDesc(int seconds) {
        return getString(R.string.goal_dialog_break) + ": " + formatSeconds(seconds);
    }
}
