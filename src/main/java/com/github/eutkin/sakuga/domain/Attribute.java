package com.github.eutkin.sakuga.domain;

public record Attribute(String name, String type) {

    public static String ID = "id";
    public static String MAIN_RULE_ID = "main_rule_id";
}
