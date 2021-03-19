package io.github.mrsperry.simpleedit.sessions.selections;

import io.github.mrsperry.simpleedit.sessions.actions.Action;
import java.util.ArrayList;
import java.util.List;

public final class SelectionHistory {
    final List<Action> undos;
    final List<Action> redos;

    public SelectionHistory() {
        this.undos = new ArrayList<>();
        this.redos = new ArrayList<>();
    }

    public final void record(final Action action) {
        this.undos.add(0, action);
    }

    public final boolean undo() {
        if (this.undos.size() == 0) {
            return false;
        }

        final Action action = this.undos.remove(0);
        action.undo();
        this.redos.add(0, action);
        return true;
    }

    public final boolean redo() {
        if (this.redos.size() == 0) {
            return false;
        }

        final Action action = this.redos.remove(0);
        action.redo();
        this.undos.add(0, action);
        return true;
    }
}
