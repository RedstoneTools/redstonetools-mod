package com.domain.redstonetools.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CharTreeTest {

    @Test
    void test() {
        CharTreeNode node = new CharTreeNode();
        node.insert("_hey");
        node.insert("_hello");

        Assertions.assertEquals(2, node.findEndMatch("1_hey"));

    }

}
