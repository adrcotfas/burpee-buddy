package com.apps.adrcotfas.burpeebuddy.db.goal;

import androidx.room.TypeConverter;

import static com.apps.adrcotfas.burpeebuddy.db.goal.GoalType.INVALID;

public class GoalTypeConverter {

    @TypeConverter
    public static GoalType getGoalTypeFromInt(int data) {
        for (GoalType goal : GoalType.values()) {
            if (goal.getValue() == data) {
                return goal;
            }
        }
        return INVALID;
    }

    @TypeConverter
    public static int getIntFromGoal(GoalType goal) {
        return goal.getValue();
    }
}
