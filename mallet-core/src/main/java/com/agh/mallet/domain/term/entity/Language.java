package com.agh.mallet.domain.term.entity;

import com.agh.mallet.infrastructure.exception.MalletIllegalArgumentException;

import java.util.Arrays;

public enum Language {

    EN,
    PL;

    public static Language from(String language) {
        return Arrays.stream(values())
                .filter(value -> value.name().equalsIgnoreCase(language))
                .findAny()
                .orElseThrow(() -> new MalletIllegalArgumentException("Provided language is not supported"));
    }

}
