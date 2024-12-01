package org.berzerk.model.Strategy;

import org.berzerk.controllers.BerzerkController;

public interface InitializationStrategy {
    void initialize(BerzerkController controller);
}
