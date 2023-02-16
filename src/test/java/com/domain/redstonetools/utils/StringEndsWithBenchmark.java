package com.domain.redstonetools.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;


public class StringEndsWithBenchmark {

    static final CharTreeNode CHAR_TREE_NODE = new CharTreeNode();
    static final Pattern PATTERN = Pattern.compile(
            "(_wool\\s)|(_concrete\\s)|(_stained_glass\\s)|(_terracotta\\s)|(_concrete_powder\\s)"
    );

    static {
        CHAR_TREE_NODE.insert("_wool");
        CHAR_TREE_NODE.insert("_stained_glass");
        CHAR_TREE_NODE.insert("_concrete");
        CHAR_TREE_NODE.insert("_terracotta");
        CHAR_TREE_NODE.insert("_concrete_powder");
    }


    @Test
    void test() {
        Benchmarks.performBenchmark("CharTreeNode", i -> {
            int idx = CHAR_TREE_NODE.findEndMatch("white_concrete");
        }, 1_000_000 /* max passes */, 1_000_000_000 /* max time */).print();

        Benchmarks.performBenchmark("CompiledRegex", i -> {
            int idx = PATTERN.matcher("white_concrete").regionStart();
        }, 1_000_000 /* max passes */, 1_000_000_000 /* max time */).print();
    }

}
