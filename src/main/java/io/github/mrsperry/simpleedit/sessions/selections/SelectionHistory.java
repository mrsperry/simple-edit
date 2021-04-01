package io.github.mrsperry.simpleedit.sessions.selections;

import io.github.mrsperry.simpleedit.sessions.actions.Action;

import java.util.Stack;

public final class SelectionHistory {
    private final Stack<Action> undos;
    private final Stack<Action> redos;

    public SelectionHistory() {
        this.undos = new Stack<>();
        this.redos = new Stack<>();
    }

    public final void record(final Action action) {
        this.undos.add(action);
    }

    public final boolean undo(int amount) {
        return this.modify(this.undos, this.redos, true, amount);
    }

    public final boolean redo(int amount) {
        return this.modify(this.redos, this.undos, false, amount);
    }

    private boolean modify(final Stack<Action> out, final Stack<Action> in, final boolean undo, int amount) {
        if (out.isEmpty()) {
            return false;
        }

        do {
            final Action action = out.pop();
            if (undo) {
                action.undo();
            } else {
                action.redo();
            }

            in.push(action);
            amount--;
        } while (!out.isEmpty() && amount > 0);

        return true;
    }
}
