package edu.hawaii.ctfoo.lang_generator.entity;

import edu.hawaii.ctfoo.lang_generator.MatchFunctor;
import edu.hawaii.ctfoo.lang_generator.ParseToken;
import edu.hawaii.ctfoo.lang_generator.Tree;
import edu.hawaii.ctfoo.lang_generator.TypeMatcher;

/**
 * An amount of money consisting of a denomination and its currency.
 * 
 * @author Christopher Foo
 * 
 */
public class MoneyAmount {

    /**
     * The denomination of the amount.
     */
    private double denomination = -1;

    /**
     * The currency of the money amount.
     */
    private String currency = "";

    /**
     * Creates a new MoneyAmount with the given denomination and currency.
     * 
     * @param denomination
     *            The denomination of the amount.
     * @param currency
     *            The currency of the amount.
     */
    public MoneyAmount(double denomination, String currency) {
        this.denomination = denomination;
        this.currency = currency;
    }

    /**
     * Creates a new MoneyAmount token by parsing the {@link Tree} from the
     * given money token.
     * 
     * @param moneyToken
     *            The node containing the money amount token.
     */
    public MoneyAmount(Tree<ParseToken> moneyToken) {

        if (moneyToken.getNode().getType().equalsIgnoreCase("moneyamount")) {
            MatchFunctor<String, ParseToken> typeMatcher = new TypeMatcher();

            // Get the denomination for the MoneyAmount (defaults to
            // last one if there are multiple)
            for (Tree<ParseToken> denominationToken : moneyToken.findAll(
                    "denomination", typeMatcher)) {
                try {
                    this.denomination = Integer.parseInt(denominationToken
                            .getNode().getValue());
                } catch (NumberFormatException e) {
                    System.err.println("Error: Could not parse "
                            + denominationToken.getNode().getValue()
                            + " as an integer.");
                }
            }

            // Get the currency for the MoneyAmount (defaults to the
            // last one if there are multiple)
            for (Tree<ParseToken> currencyToken : moneyToken.findAll(
                    "currency", typeMatcher)) {
                this.currency = currencyToken.getNode().getValue();
            }
        }
    }

    /**
     * Gets the denomination of the amount.
     * 
     * @return The denomination of the amount.
     */
    public double getDenomination() {
        return denomination;
    }

    /**
     * Gets the currency of the amount.
     * 
     * @return The currency of the amount.
     */
    public String getCurrency() {
        return currency;
    }

    @Override
    /**
     * Returns a String representation of the MoneyAmount.
     */
    public String toString() {
        if (this.denomination - (-1.0) < 0.0001 || this.currency.equals("")) {
            return "< Error: Incomplete currency encountered >";
        } else {

            if (this.denomination > 1000) {
                return (this.denomination / 1000) + "k "
                        + this.currency.toLowerCase();
            } else {
                return this.denomination + " " + this.currency.toLowerCase();
            }

        }
    }

}
