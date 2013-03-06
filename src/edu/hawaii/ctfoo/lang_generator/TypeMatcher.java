package edu.hawaii.ctfoo.lang_generator;

/**
 * An implementation of the {@link MatchFunctor} used for finding ParseTokens
 * with the given String keys.
 * 
 * @author Christopher Foo
 * 
 */
public class TypeMatcher implements MatchFunctor<String, ParseToken> {

    @Override
    /**
     * Matches if the given searchItem's type and the key match.
     */
    public boolean match(String key, ParseToken searchItem) {
        if (searchItem == null) {
            return false;
        }

        if (searchItem.getType() == key
                || searchItem.getType().equalsIgnoreCase(key)) {
            return true;
        }

        else {
            return false;
        }
    }

}
