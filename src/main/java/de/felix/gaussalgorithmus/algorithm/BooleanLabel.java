package de.felix.gaussalgorithmus.algorithm;

import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * @author EinfachBeez | https://github.com/EinfachBeez
 */
public class BooleanLabel extends Label {

    private boolean activated;

    public BooleanLabel() {
        activated = false;
    }

    public BooleanLabel(String text) {
        super(text);
        activated = false;
    }

    public BooleanLabel(String text, Node graphic) {
        super(text, graphic);
        activated = false;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isActivated() {
        return activated;
    }
}
