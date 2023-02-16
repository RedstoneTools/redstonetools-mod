package com.domain.redstonetools.utils;

/**
 * Oriented at performant string matching.
 *
 * This class is a node in a tree of nodes,
 * each node has a character associated with
 * it which it matches with and and arbitrary
 * amount of children.
 *
 * The insertion of strings is relatively slow
 * but the aim is to optimize matching as much
 * as possible.
 */
public class CharTreeNode {

    // the character this node matches
    final char matches;
    // the child nodes
    CharTreeNode[] children = new CharTreeNode[0];
    // if this is the tail of an inserted string
    boolean isTail = false;

    // the length of the shortest inserted
    // string
    int shortestStringLen = Integer.MAX_VALUE;

    public CharTreeNode() {
        this.matches = '\0';
    }

    public CharTreeNode(char matches) {
        this.matches = matches;
    }

    /**
     * Append a child to this char tree node.
     *
     * @param node The child node.
     */
    public void addChild(CharTreeNode node) {
        CharTreeNode[] old = children;
        children = new CharTreeNode[old.length + 1];
        System.arraycopy(old, 0, children, 0, old.length);
        children[children.length - 1] = node;
    }

    /**
     * Insert a new string into this tree.
     *
     * @param str The string.
     */
    public void insert(String str) {
        CharTreeNode curr = this;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            curr = curr.findOrCreateChild(c);
        }

        curr.isTail = true;

        if (str.length() < shortestStringLen)
            shortestStringLen = str.length();
    }

    /**
     * Find a child by character.
     *
     * @param c The character.
     * @return The child or null if absent.
     */
    public CharTreeNode findChild(char c) {
        CharTreeNode child;
        for (int i = 0; i < children.length; i++)
            if ((child = children[i]).matches == c)
                return child;
        return null;
    }

    /**
     * Find or create a child by character.
     *
     * @param c The character.
     * @return The child node created or retrieved.
     */
    public CharTreeNode findOrCreateChild(char c) {
        CharTreeNode child;
        for (int i = 0; i < children.length; i++) {
            if ((child = children[i]).matches == c) {
                return child;
            }
        }

        addChild(child = new CharTreeNode(c));
        return child;
    }

    /**
     * Checks to see if the given string starts
     * with any of the inserted strings.
     *
     * @param target The target string.
     * @return True/false.
     */
    public boolean startMatchesAny(String target) {
        CharTreeNode curr = this;
        final int l = target.length();
        for (int i = 0; i < l; i++) {
            char c = target.charAt(i);
            curr = curr.findChild(c);
            if (curr == null)
                return false;
            if (curr.isTail)
                return true;
        }

        return true;
    }

    /**
     * Tries to find the index at which the
     * string ends with one of the inserted
     * strings.
     *
     * If it can not be found, -1 is returned.
     *
     * @param target The target string.
     * @return The index or -1 if not present.
     */
    public int findEndMatch(String target) {
        CharTreeNode curr = this;
        final int l = target.length();

        if (shortestStringLen > l) {
            return -1;
        }

        outer:
        for (int i = l - 1 - shortestStringLen; i >= 0; i--) {
            int j = i;
            for (; j < l; j++) {
                char c2 = target.charAt(j);
                curr = curr.findChild(c2);
                if (curr == null) {
                    curr = this;
                    continue outer;
                }
            }

            if (curr.isTail)
                return i;
        }

        return -1;
    }

}
