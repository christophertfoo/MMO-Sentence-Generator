package edu.hawaii.ctfoo.lang_generator;

/**
 * A functor used to find matching nodes in a {@link Tree}.
 * 
 * @author Christopher Foo
 * 
 * @param <T>
 *            The type of the key to match with.
 * @param <U>
 *            The type of the nodes that will be searched.
 */
public interface MatchFunctor<T, U> {

    /**
     * Matches the given node with the given key.
     * 
     * @param key
     *            The key to be matched.
     * @param node
     *            The node to be checked.
     * @return If the key and the node match or not.
     */
    public boolean match(T key, U node);
}
