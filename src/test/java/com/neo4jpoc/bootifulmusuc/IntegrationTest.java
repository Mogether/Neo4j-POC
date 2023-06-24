package com.neo4jpoc.bootifulmusuc;

import com.neo4jpoc.bootifulmusuc.Neo4JPocApp;
import com.neo4jpoc.bootifulmusuc.config.AsyncSyncConfiguration;
import com.neo4jpoc.bootifulmusuc.config.EmbeddedNeo4j;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { Neo4JPocApp.class, AsyncSyncConfiguration.class })
@EmbeddedNeo4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public @interface IntegrationTest {
}
