package com.github.eutkin.sakuga.domain;


import com.github.eutkin.sakuga.domain.tree.ConditionNode;

public record Rule(
        String id,
        Integer number,
        String name,
        int priority,
        RecommendedAction recommendedAction,
        ConditionNode condition
) {}