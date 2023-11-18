package com.agh.mallet.domain.term.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "TERM")
public class TermJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "TERM", nullable = false)
    private String term;
    @Enumerated(EnumType.STRING)
    @Column(name = "LANGUAGE", nullable = false)
    private Language language;

    @Column(name = "DEFINITION")
    private String definition;

    @Column(name = "TERM_DICTIONARY")
    private boolean isTermDictionary;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private TermJPAEntity translation;

    public TermJPAEntity() {}

    public TermJPAEntity(String term, Language language, String definition, TermJPAEntity translation) {
        this.term = term;
        this.language = language;
        this.definition = definition;
        this.translation = translation;
    }

    public TermJPAEntity(String term, Language language, String definition) {
        this.term = term;
        this.language = language;
        this.definition = definition;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }


    public Long getId() {
        return id;
    }

    public String getTerm() {
        return term;
    }

    public Language getLanguage() {
        return language;
    }

    public String getDefinition() {
        return definition;
    }

    public boolean isTermDictionary() {
        return isTermDictionary;
    }

    public TermJPAEntity getTranslation() {
        return translation;
    }

    //TODO DO POPRAWY, JAK U VLADA ZROBIC
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TermJPAEntity that = (TermJPAEntity) o;

        return new EqualsBuilder().append(term, that.term).append(language, that.language).append(definition, that.definition).append(translation, that.translation).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(term).append(language).append(definition).append(translation).toHashCode();
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setTermDictionary(boolean termDictionary) {
        isTermDictionary = termDictionary;
    }

    public void setTranslation(TermJPAEntity translation) {
        this.translation = translation;
    }
}
