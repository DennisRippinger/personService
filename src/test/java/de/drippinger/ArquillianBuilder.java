package de.drippinger;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.File;

public class ArquillianBuilder {

    public static WebArchive createBaseDeployment() {
        return ShrinkWrap.create(WebArchive.class, "personService.war")
            .addPackages(true, JAXRSActivator.class.getPackage())
            .addAsLibraries(resolveMavenDependencies())
            .addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml")
            .addAsResource(new File("src/main/resources/META-INF/apache-deltaspike.properties"), "META-INF/apache-deltaspike.properties")
            .addAsWebInfResource(new File("src/main/webapp/WEB-INF/h2-ds.xml"))
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    private static File[] resolveMavenDependencies() {
        return Maven
            .resolver()
            .loadPomFromFile("pom.xml")
            .importRuntimeDependencies()
            .resolve()
            .withTransitivity()
            .asFile();
    }

}
