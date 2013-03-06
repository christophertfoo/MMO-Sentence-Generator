package edu.hawaii.ctfoo.lang_generator;
/**
 * An object representing a single token from the parsed semantic representation.
 * @author Christopher Foo
 *
 */
public class ParseToken {
    /**
     * The type of the token (i.e. Subject, Object, Item, etc)
     */
    private String type = null;
    
    /**
     * The ID of the token's parent.  null if the token does not have a parent.
     */
    private String parent = null;
    
    /**
     * The ID of the token which is used to associate it with its children.
     * null if the token does not have any children.
     */
    private String id = null;
    
    /**
     * The value of the token.  null if the token is a parent token.
     */
    private String value = null;
    
    /**
     * The logic operation ({@link LogicOp}) used to connect this token with the others.
     */
    private LogicOp logic = null;
    
    /**
     * If the token was negated or not.
     */
    private boolean negated = false;
    
    /**
     * Gets the type of the token (i.e. Subject, Object, Item, etc).
     * @return The type of the token.
     */
    public String getType() {
        return this.type;
    }
    /**
     * Sets the type of the token (i.e. Subject, Object, Item, etc) to the given value.
     * @param type The new type of the token.
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Gets the ID of the token's parent. null if the token does not have a parent.
     * @return The ID of the token's parent.  null if the token does not have a parent.
     */
    public String getParent() {
        return this.parent;
    }
    /**
     * Sets the ID of the token's parent to the given value.
     * @param parent The ID of the token's new parent.  null indicates no parent.
     */
    public void setParent(String parent) {
        this.parent = parent;
    }
    /**
     * Gets the ID of the token. null if the token is not a parent.
     * @return The ID of the token.  null if the token is not a parent.
     */
    public String getId() {
        return this.id;
    }
    /**
     * Sets the ID of the token to the given value.
     * @param id The new ID of the token.  null indicates that it is not a parent.
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * Gets the value of the token.
     * @return The value of the token.  null if the token is a parent.
     */
    public String getValue() {
        return this.value;
    }
    /**
     * Sets the value of the token to the given value.
     * @param value The new value for the token.
     */
    public void setValue(String value) {
        this.value = value;
    }
    /**
     * Gets the logic operation used to link this token with the others.
     * @return The logic operation.
     */
    public LogicOp getLogic() {
        return this.logic;
    }
    /**
     * Sets the logic operation used to link this token with the others.
     * @param logic The new logic operation of the token.
     */
    public void setLogic(LogicOp logic) {
        this.logic = logic;
    }
    
    /**
     * Gets if the token was negated or not.
     * @return If the token was negated.
     */
    public boolean isNegated() {
        return negated;
    }
    /**
     * Sets whether the token was negated or not.
     * @param negated If the token was negated (true = negated).
     */
    public void setNegated(boolean negated) {
        this.negated = negated;
    }
    @Override
    /**
     * Returns a String of the ParseToken in JSON form.
     */
    public String toString() {
        StringBuilder returnString = new StringBuilder();
        String fieldValue;
        fieldValue = (this.type == null) ? "null, " : this.type + ", ";
        returnString.append("{ Type: " + fieldValue);

        fieldValue = (this.id == null) ? "null, " : this.id + ", ";
        returnString.append("ID: " + fieldValue);
        
        fieldValue = (this.parent == null) ? "null, " : this.parent + ", ";
        returnString.append("Parent: " + fieldValue);
        
        fieldValue = (this.value == null) ? "null, " : this.value + ", ";
        returnString.append("Value: " + fieldValue);
        
        fieldValue = (this.logic == null) ? "null, " : this.logic + ", ";
        returnString.append("Logic Operation: " + fieldValue);
        
        fieldValue = (this.negated) ? "true }" : "false }";
        returnString.append("Negated: " + fieldValue);
        
        return returnString.toString();
    }
}
