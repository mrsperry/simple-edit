package io.github.mrsperry.simpleedit.sessions;

import io.github.mrsperry.simpleedit.SimpleEdit;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Stream;

public final class SessionManager {
    private static final HashMap<UUID, Session> sessions = new HashMap<>();
    private static Path sessionPath;

    public static void initialize(final File dataFolder) {
        final Logger logger = SimpleEdit.getInstance().getLogger();

        final Path path = Path.of(dataFolder.getPath(), "sessions");
        SessionManager.sessionPath = path;

        if (Files.exists(path)) {
            try (final Stream<Path> stream = Files.list(path)) {
                stream.forEach(file -> {
                    final String name = file.getFileName().toString().replace(".session", "");

                    final UUID uuid;
                    try {
                        uuid = UUID.fromString(name);
                    } catch (final IllegalArgumentException ex) {
                        logger.severe("Could not convert file name to UUID: " + name);
                        return;
                    }

                    final String data;
                    try {
                        data = Files.readString(file);
                    } catch (final IOException ex) {
                        logger.severe("Could not read session data of: " + name);
                        return;
                    }

                    final Session session = Session.deserialize(data);
                    if (session == null) {
                        logger.severe("Deleting malformed session data: " + name);

                        try {
                            if (!file.toFile().delete()) {
                                throw new Exception();
                            }
                        } catch (final Exception ex) {
                            logger.severe("Could not delete malformed session file!");
                        }

                        return;
                    }

                    SessionManager.sessions.put(uuid, session);
                });
            } catch (final IOException ex) {
                logger.severe("An exception occurred while reading session data; some sessions may be lost!");
            }
        } else {
            try {
                Files.createDirectory(path);
            } catch (final Exception ex) {
                logger.severe("Could not create the sessions directory; sessions may not be able to save!");
            }
        }
    }

    /**
     * Gets a player's session or creates a new one if it could not be found
     *
     * @param id The UUID of the player
     * @return The player's session
     */
    public static Session getSession(final UUID id) {
        if (SessionManager.sessions.containsKey(id)) {
            return SessionManager.sessions.get(id);
        }

        final Session session = new Session();
        SessionManager.sessions.put(id, session);
        return session;
    }

    public static void setSession(final UUID id, final Session session) {
        SessionManager.sessions.put(id, session);
    }

    public static void save() {
        final Logger logger = SimpleEdit.getInstance().getLogger();
        final Path path = SessionManager.sessionPath;

        for (final UUID uuid : SessionManager.sessions.keySet()) {
            final String id = uuid.toString();
            final File file = new File(path.toString() + "/" + id + ".session");

            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (final Exception ex) {
                logger.severe("Could not create session save file for: " + id);
                continue;
            }

            try (PrintWriter writer = new PrintWriter(file)) {
                writer.write(SessionManager.sessions.get(uuid).serialize());
            } catch (final Exception ex) {
                logger.severe("Could not write to session save file for: " + id);
            }
        }
    }
}
