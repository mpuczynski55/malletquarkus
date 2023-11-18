package com.agh.mallet.configuration;


import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.enterprise.context.Dependent;

@Dependent
public class PasswordEncoder {

    private static final Argon2 encoder;

    static {
        encoder = Argon2Factory.create();
    }

    public String hash(String rawPassword) {
        return encoder.hash(22, 65536, 1, rawPassword);
    }

    public boolean matches(String rawPassword, String databasePassword) {
        return encoder.verify(databasePassword, rawPassword);
    }

}
