package net.fabricmc.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExampleModTest {
    private ExampleMod exampleMod;

    @BeforeEach
    void setUp() {
        exampleMod = new ExampleMod();
    }

    @Test
    void onInitialize() {
        assertDoesNotThrow(exampleMod::onInitialize);
    }
}