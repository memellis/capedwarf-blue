package org.jboss.test.capedwarf.testsuite.jpa.test;

import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import java.io.File;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class TestUtils {

    private static MavenDependencyResolver resolver;

    protected static MavenDependencyResolver getResolver() {
        if (resolver == null)
            resolver = DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom(getPomPath());
        return resolver;
    }

    // we need testsuite/pom.xml file
    protected static String getPomPath() {
        File root = new File(".");
        String path = "pom.xml";
        if (root.getAbsolutePath().contains("testsuite") == false)
            path = "testsuite/" + path;
        return path;
    }

    protected static void addLibraries(WebArchive war) {
        // default JPA libs
        war.addAsLibraries(getResolver().artifact("com.google.appengine:appengine-api-1.0-sdk").resolveAsFiles());
        war.addAsLibraries(getResolver().artifact("org.datanucleus:datanucleus-core").resolveAsFiles());
        war.addAsLibraries(getResolver().artifact("org.datanucleus:datanucleus-jpa").resolveAsFiles());
        war.addAsLibraries(getResolver().artifact("com.google.appengine.orm:datanucleus-appengine").resolveAsFiles());
        war.addAsLibraries(getResolver().artifact("com.google.appengine:jdo2-api").resolveAsFiles());
        war.addAsLibraries(getResolver().artifact("com.google.appengine:geronimo-jta_1.1_spec").resolveAsFiles());
    }
}
