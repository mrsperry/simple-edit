package io.github.mrsperry.simpleedit.sessions.selections;

import io.github.mrsperry.simpleedit.sessions.actions.Action;
import java.util.Stack;

public final class SelectionHistory {
    final Stack<Action> undos;
    final Stack<Action> redos;

    public SelectionHistory() {
        this.undos = new Stack<>();
        this.redos = new Stack<>();
    }

    public final void record(final Action action) {
        this.undos.add(action);
    }

    public final boolean undo() {
        if (this.undos.isEmpty()) {
            return false;
        }

        final Action action = this.undos.pop();
        action.undo();
        this.redos.push(action);
        return true;
    }

    public final boolean redo() {
        if (this.redos.isEmpty()) {
            return false;
        }

        final Action action = this.redos.pop();
        action.redo();
        this.undos.push(action);
        return true;
    }
}
