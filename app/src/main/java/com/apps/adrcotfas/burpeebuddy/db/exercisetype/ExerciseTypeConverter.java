package com.apps.adrcotfas.burpeebuddy.db.exercisetype;

import androidx.room.TypeConverter;

import static com.apps.adrcotfas.burpeebuddy.db.exercisetype.ExerciseType.INVALID;

public class ExerciseTypeConverter {

    @TypeConverter
    public ExerciseType getGoalFromInt(int data) {
        for (ExerciseType type : ExerciseType.values()) {
            if (type.getValue() == data) {
                return type;
            }
        }
        return INVALID;
    }

    @TypeConverter
    public int getIntFromGoal(ExerciseType type) {
        return type.getValue();
    }
}
