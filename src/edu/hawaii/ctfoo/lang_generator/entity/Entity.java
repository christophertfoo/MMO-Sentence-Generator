package edu.hawaii.ctfoo.lang_generator.entity;

import edu.hawaii.ctfoo.lang_generator.LogicOp;

/**
 * An entity in a sentence.
 * 
 * @author Christopher Foo
 * 
 */
public abstract class Entity {

    /**
     * The logic operation connecting this entity to the others.
     */
    protected LogicOp logic;

    /**
     * If the entity was negated.
     */
    protected boolean negated;

    /**
     * Creates a new Entity with default values.
     */
    public Entity() {
        this.logic = null;
        this.negated = false;
    }

    /**
     * Gets the logic operation used to connect this entity with the others.
     * 
     * @return The logic operation.
     */
    public LogicOp getLogic() {
        return this.logic;
    }

    /**
     * Checks if this entity was negated.
     * 
     * @return If the entity was negated.
     */
    public boolean isNegated() {
        return this.negated;
    }
}
