package edu.hawaii.ctfoo.lang_generator.sentence;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import edu.hawaii.ctfoo.lang_generator.LogicOp;
import edu.hawaii.ctfoo.lang_generator.MatchFunctor;
import edu.hawaii.ctfoo.lang_generator.ParseToken;
import edu.hawaii.ctfoo.lang_generator.Tree;
import edu.hawaii.ctfoo.lang_generator.TypeMatcher;
import edu.hawaii.ctfoo.lang_generator.entity.Entity;

/**
 * Represents a simple English sentence about a single event.
 * 
 * @author Christopher Foo
 * 
 */
public abstract class Sentence {

    /**
     * The objects (acted upon) of the event. Each sublist is connected by an OR
     * and every element in each sublist is connected by an AND.
     */
    protected List<List<Entity>> eventObject;

    /**
     * The subjects (actors) of the event. Each sublist is connected by an OR
     * and every element in each sublist is connected by an AND.
     */
    protected List<List<Entity>> eventSubject;

    /**
     * An array of valid classes for the Sentence's objects.
     */
    protected Class<?>[] validObjectClasses;

    /**
     * An array of valid classes for the Sentence's subjects.
     */
    protected Class<?>[] validSubjectClasses;

    /**
     * Creates and initializes a new Sentence.
     * 
     * @param validObjectClasses
     *            An array of the valid classes for the Sentence's objects.
     * @param validSubjectClasses
     *            An array of the valid classes for the Sentence's subjects.
     */
    public Sentence(Class<?>[] validObjectClasses,
            Class<?>[] validSubjectClasses) {
        this.validObjectClasses = validObjectClasses;
        this.validSubjectClasses = validSubjectClasses;
        this.eventObject = new ArrayList<List<Entity>>();
        this.eventSubject = new ArrayList<List<Entity>>();
    }

    /**
     * Adds a new object to the eventObjects list.
     * 
     * @param newObject
     *            The new object to add.
     * @param index
     *            The index of the sublist to add it to.
     */
    public void addObject(Entity newObject, int index) {
        if (checkObject(newObject)) {
            if (index >= this.eventObject.size()) {
                this.eventObject.add(new ArrayList<Entity>());
            }
            this.eventObject.get(index).add(newObject);
        }
    }

    /**
     * Adds a new subject to the eventSubjects list.
     * 
     * @param newSubject
     *            The new subject to add.
     * @param index
     *            The index of the sublist to add it to.
     */
    public void addSubject(Entity newSubject, int index) {
        if (checkSubject(newSubject)) {
            if (index >= this.eventSubject.size()) {
                this.eventSubject.add(new ArrayList<Entity>());
            }
            this.eventSubject.get(index).add(newSubject);
        }
    }

    /**
     * Reads in all of the subjects or objects for the given sentence.
     * 
     * @param sentenceToken
     *            The node containing the token for the sentence.
     * @param type
     *            Either "subject" to read in the subjects or "object" to read
     *            in the objects.
     */
    public void readSubObj(Tree<ParseToken> sentenceToken, String type) {
        MatchFunctor<String, ParseToken> typeMatcher = new TypeMatcher();

        if (type.equalsIgnoreCase("subject") || type.equalsIgnoreCase("object")) {

            List<List<Entity>> targetList;
            if (type.equalsIgnoreCase("subject")) {
                targetList = this.eventSubject;
            } else {
                targetList = this.eventObject;
            }

            int index = 0;
            for (Tree<ParseToken> typeNode : sentenceToken.findAll(type,
                    typeMatcher)) {
                ParseToken typeToken = typeNode.getNode();

                // Put in new list if OR
                if (typeToken.getLogic() == LogicOp.OR
                        && (index >= targetList.size() || targetList.get(index)
                                .size() > 0)) {
                    index++;
                }

                // Get all of the subject or object entities
                for (Tree<ParseToken> typeChild : typeNode.getDirectChildren()) {
                    ParseToken tokenChild = typeChild.getNode();
                    Entity entity = null;
                    if (typeToken.isNegated()) {
                        tokenChild.setNegated(true);
                    }

                    // Use reflection to get a new instance of the entity
                    try {
                        Class<? extends Entity> foundClass = Class.forName(
                                "edu.hawaii.ctfoo.lang_generator.entity."
                                        + typeChild.getNode().getType())
                                .asSubclass(Entity.class);
                        Constructor<? extends Entity> foundClassConstructor = foundClass
                                .getConstructor(Tree.class);
                        entity = foundClassConstructor.newInstance(typeChild);
                        if (entity != null) {

                            // Update index if OR
                            if (tokenChild.getLogic() == LogicOp.OR
                                    && (index >= targetList.size() || targetList
                                            .get(index).size() > 0)) {
                                index++;
                            }

                            // Add to the appropriate list
                            if (type.equalsIgnoreCase("object")) {
                                this.addObject(entity, index);
                            } else {
                                this.addSubject(entity, index);
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        // Ignore it
                    } catch (IllegalArgumentException e) {
                        // Ignore it
                    } catch (InstantiationException e) {
                        // Ignore it
                    } catch (IllegalAccessException e) {
                        // Ignore it
                    } catch (InvocationTargetException e) {
                        // Ignore it
                    } catch (SecurityException e) {
                        // Ignore it
                    } catch (NoSuchMethodException e) {
                        // Ignore it
                    }
                }
            }
        }
    }

    /**
     * Checks the objects of the Sentence to ensure that they are all of valid
     * classes.
     * 
     * @return If all of the objects are valid. Returns false if at least one is
     *         invalid.
     */
    protected boolean checkObject() {
        for (List<Entity> objectList : this.eventObject) {
            for (Entity objectElement : objectList) {
                if (!checkObject(objectElement)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the given {@link Entity} is a valid class for the Sentence's
     * objects.
     * 
     * @param object
     *            The Entity to check.
     * @return If the Entity is a valid object class.
     */
    protected boolean checkObject(Entity object) {
        for (Class<?> validClass : this.validObjectClasses) {
            if (validClass.isInstance(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks all of the subjects of the Sentence to ensure that they are all of
     * valid classes for the Sentence's subjects.
     * 
     * @return If all of the subjects are valid. Returns false if at least one
     *         of the subjects is of an invalid class.
     */
    protected boolean checkSubject() {
        for (List<Entity> subjectList : this.eventSubject) {
            for (Entity subjectElement : subjectList) {
                if (!checkSubject(subjectElement)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the given {@link Entity} is a valid class for this Sentence's
     * subjects.
     * 
     * @param subject
     *            The Entity to check.
     * @return If the given Entity is a valid subject class.
     */
    protected boolean checkSubject(Entity subject) {
        for (Class<?> validClass : this.validSubjectClasses) {
            if (validClass.isInstance(subject)) {
                return true;
            }
        }
        return false;
    }
}
