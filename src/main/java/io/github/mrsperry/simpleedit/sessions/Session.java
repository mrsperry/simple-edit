package io.github.mrsperry.simpleedit.sessions;

import io.github.mrsperry.simpleedit.sessions.selections.Selection;

public final class Session {
    private Selection selection;

    public Session() {
        this.selection = new Selection();
    }

    public static Session deserialize(final String data) {
        final Session session = new Session();

        final Selection selection = Selection.deserialize(data);
        if (selection == null) {
            return null;
        }

        session.setSelection(selection);
        return session;
    }

    public final Selection getSelection() {
        return this.selection;
    }

    public final void setSelection(final Selection selection) {
        this.selection = selection;
    }

    public final String serialize() {
        return this.selection.serialize();
    }
}
