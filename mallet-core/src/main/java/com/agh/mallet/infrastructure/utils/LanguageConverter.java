package com.agh.mallet.infrastructure.utils;

import com.agh.api.Language;
import com.agh.mallet.infrastructure.exception.MalletIllegalArgumentException;

import java.util.Arrays;

public class LanguageConverter {

    private LanguageConverter() {}

    public static Language from(com.agh.mallet.domain.term.entity.Language language) {
        return Arrays.stream(Language.values())
                .filter(value -> value.name().equalsIgnoreCase(language.name()))
                .findAny()
                .orElseThrow(() -> new MalletIllegalArgumentException("Provided language is not supported"));
    }

}
