package com.apps.adrcotfas.burpeebuddy.edit_challenges.view;

import androidx.core.util.Pair;

import com.apps.adrcotfas.burpeebuddy.common.viewmvc.ObservableViewMvc;
import com.apps.adrcotfas.burpeebuddy.db.challenge.Challenge;

import java.util.List;

public interface ChallengeView extends ObservableViewMvc<ChallengeView.Listener> {

    void bindChallenges(List<Pair<Challenge, Integer>> challenges);

    public interface Listener {
        void onLongClick(int id);
    }
}