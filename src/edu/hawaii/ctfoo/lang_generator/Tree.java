package edu.hawaii.ctfoo.lang_generator;

import java.util.ArrayList;
import java.util.List;

/**
 * A node in a tree structure. An entire tree structure is made out of these
 * nodes connected by their parent / children relationships.
 * 
 * @author Christopher Foo
 * 
 * @param <T>
 *            The type of the nodes in the Tree.
 */
public class Tree<T> {

    /**
     * The node at the top of this tree.
     */
    private T node;

    /**
     * This node's parent.
     */
    private Tree<T> parent;

    /**
     * A list of children nodes.
     */
    private List<Tree<T>> children;

    /**
     * Creates a new Tree with the given value for its node and given parent.
     * 
     * @param node
     *            The value of the node.
     * @param parent
     *            The node's parent. null if it has no parent.
     */
    public Tree(T node, Tree<T> parent) {
        this.node = node;
        this.parent = parent;
        this.children = new ArrayList<Tree<T>>();
    }

    /**
     * Gets the value of the node.
     * 
     * @return The value of the node.
     */
    public T getNode() {
        return node;
    }

    /**
     * Sets the value of the node to the given value.
     * 
     * @param node
     *            The new value of the node.
     */
    public void setNode(T node) {
        this.node = node;
    }

    /**
     * Gets the parent of the node.
     * 
     * @return The node's parent. null if the node does not have a parent.
     */
    public Tree<T> getParent() {
        return parent;
    }

    /**
     * Sets the parent of the node.
     * 
     * @param parent
     *            The parent of the node. null indicates that it has no parent.
     */
    public void setParent(Tree<T> parent) {
        this.parent = parent;
    }

    /**
     * Gets the direct children of the node (1 level).
     * 
     * @return The direct children of this node.
     */
    public List<Tree<T>> getDirectChildren() {
        return children;
    }

    /**
     * Adds a child to this node.
     * 
     * @param child
     *            The child node to add to this node.
     * @return The added node.
     */
    public Tree<T> addChild(Tree<T> child) {
        this.children.add(child);
        child.parent = this;
        return child;
    }

    /**
     * Adds a child to this node.
     * 
     * @param childValue
     *            The value of the child node to add to this node.
     * @return The added node.
     */
    public Tree<T> addChild(T childValue) {
        Tree<T> child = new Tree<T>(childValue, this);
        this.children.add(child);
        return child;
    }

    /**
     * Finds the direct children of this node that match the matcher with the
     * given key.
     * 
     * @param key
     *            The key to match with the matcher.
     * @param matcher
     *            The {@link MatchFunctor} used to match the key with a node.
     * @return A list of matching children nodes.
     */
    public <U> List<Tree<T>> findDirect(U key, MatchFunctor<U, T> matcher) {
        ArrayList<Tree<T>> matches = new ArrayList<Tree<T>>();
        for (Tree<T> child : this.children) {
            if (matcher.match(key, child.node)) {
                matches.add(child);
            }
        }

        return matches;
    }

    /**
     * Finds all children (and their children, etc.) that match the matcher with
     * the given key.
     * 
     * @param key
     *            The key to match with the matcher.
     * @param matcher
     *            The {@link MatchFunctor} used to match the key with a node.
     * @return A list of all matching nodes below this node.
     */
    public <U> List<Tree<T>> findAll(U key, MatchFunctor<U, T> matcher) {
        ArrayList<Tree<T>> matches = new ArrayList<Tree<T>>();
        for (Tree<T> child : this.children) {
            if (matcher.match(key, child.node)) {
                matches.add(child);
            }
            matches.addAll(child.findAll(key, matcher));
        }
        return matches;
    }

    /**
     * Recursively builds the string to represent this {@link Tree} and its
     * children (and thier children, etc.)
     * 
     * @param indent
     *            The current level of indentation for this level of nodes.
     * @return A string of the Tree's structure.
     */
    private String toStringHelper(String indent) {
        StringBuilder builder = new StringBuilder();
        builder.append(indent + this.node.toString() + "\n");

        for (Tree<T> child : this.children) {
            builder.append(child.toStringHelper(indent + "\t"));
        }

        return builder.toString();
    }

    @Override
    /**
     * Returns a String representing this node and all of its children (and their children, etc)
     */
    public String toString() {
        return this.toStringHelper("");
    }
}
