package edu.hawaii.ctfoo.lang_generator;

/**
 * Represents the two logical operations (AND & OR) in the semantic
 * representations of the Language Generator.
 * 
 * @author Christopher Foo
 * 
 */
public enum LogicOp {
    AND, OR;

    @Override
    public String toString() {
        switch (this) {
        case AND:
            return "AND";
        case OR:
            return "OR";
        default:
            return "Undefined LogicOp";
        }
    }
}
