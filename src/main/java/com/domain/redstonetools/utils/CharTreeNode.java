package com.domain.redstonetools.utils;

/**
 * Oriented at performant string matching.
 */
public class CharTreeNode {

    final char matches;
    CharTreeNode[] children = new CharTreeNode[0];
    boolean isTail = false; // if this is the tail of an inserted string

    public CharTreeNode() {
        this.matches = '\0';
    }

    public CharTreeNode(char matches) {
        this.matches = matches;
    }

    public void addChild(CharTreeNode node) {
        CharTreeNode[] old = children;
        children = new CharTreeNode[old.length + 1];
        System.arraycopy(old, 0, children, 0, old.length);
        children[children.length - 1] = node;
    }

    public void insert(String str) {
        CharTreeNode curr = this;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            curr = curr.findOrCreateChild(c);
        }

        curr.isTail = true;
    }

    public CharTreeNode findChild(char c) {
        CharTreeNode child;
        for (int i = 0; i < children.length; i++)
            if ((child = children[i]).matches == c)
                return child;
        return null;
    }

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

    public int findEndMatch(String target) {
        CharTreeNode curr = this;
        final int l = target.length();

        outer:
        for (int i = l - 1; i >= 0; i--) {
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
