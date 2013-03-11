package edu.hawaii.ctfoo.lang_generator.entity;

import edu.hawaii.ctfoo.lang_generator.ParseToken;
import edu.hawaii.ctfoo.lang_generator.Tree;

/**
 * An entity representing an Instance (i.e. dungeon) in a game.
 * @author Christopher Foo
 *
 */
public class Instance extends Entity {

    /**
     * The name of the instance.
     */
    private String name;
    
    /**
     * The mode of the instance.
     */
    private String mode;
    
    /**
     * The difficulty level of the instance.
     */
    private String difficulty;

    /**
     * Creates a new Instance based on the given {@link Tree}.
     * @param instanceToken The Tree of an Instance token node.
     */
    public Instance(Tree<ParseToken> instanceToken) {
        // Set default values
        this.name = "";
        this.mode = "";
        this.difficulty = "";
        
        // Get the values from the parsed representation
        if(instanceToken.getNode().getType().equalsIgnoreCase("instance")) {
           if(instanceToken.getNode().isNegated()) {
                this.negated = true;
            }
           
           String type;
           String value;

           // Read all of the attributes for the instance into the appropriate
           // fields
           for (Tree<ParseToken> instanceAttribute : instanceToken.getDirectChildren()) {
               
               type = instanceAttribute.getNode().getType();
               
               value = instanceAttribute.getNode().getValue();
               
               // Set name
               if(type.equalsIgnoreCase("name")) {
                   this.name = value;
               }
               
               // Set mode
               else if(type.equalsIgnoreCase("mode")) {
                   this.mode = value;
               }
               
               // Set difficulty
               else if(type.equalsIgnoreCase("difficulty")) {
                   this.difficulty = value;
               }
               
               // Ignore unrecognized attributes
           }
        }
    }

    /**
     * Gets the name of the Instance.
     * @return The name of the Instance.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the mode of the Instance.
     * @return The mode of the Instance.
     */
    public String getMode() {
        return this.mode;
    }

    /**
     * Gets the difficulty level of the Instance.
     * @return The difficulty level of the Instance.
     */
    public String getDifficulty() {
        return this.difficulty;
    }

    @Override
    /**
     * Returns a String representation of this Instance.
     */
    public String toString() {
        if (this.name.equals("")) {
            return "< Error Incomplete Instance Encountered >";
        }
        StringBuilder builder = new StringBuilder();
        if (!this.difficulty.equals("")) {
            builder.append(this.difficulty + " ");
        }

        if (!this.mode.equals("")) {
            builder.append(this.mode + " ");
        }

        builder.append(this.name);
        return builder.toString().trim();
    }
}
