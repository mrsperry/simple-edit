package io.github.mrsperry.simpleedit.sessions;

public final class Session {
    private Selection selection;

    public Session() {
        this.selection = new Selection();
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

    public static Session deserialize(final String[] data) {
        final Session session = new Session();
        session.setSelection(Selection.deserialize(data[0]));

        return session;
    }
}
