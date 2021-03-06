package io.fabric8.launcher.web.providers;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.fabric8.launcher.core.spi.DirectoryReaper;

import static io.fabric8.launcher.base.Paths.deleteDirectory;

/**
 * Deletes temporary directories
 *
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 */
@ApplicationScoped
public class DirectoryReaperImpl implements DirectoryReaper {

    @Inject
    ExecutorService executor;

    private static final Logger log = Logger.getLogger(DirectoryReaperImpl.class.getName());

    @Override
    public void delete(Path path) {
        if (path != null) {
            if (executor != null) {
                executor.submit(() -> performDelete(path));
            } else {
                performDelete(path);
            }
        }
    }

    private void performDelete(Path path) {
        log.log(Level.INFO, "Deleting {0}", path);
        try {
            deleteDirectory(path);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error while deleting" + path, e);
        }
    }
}
