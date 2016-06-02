package org.springframework.data.crate.integration;

import io.crate.testing.CrateTestCluster;
import io.crate.testing.CrateTestServer;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class CrateBaseIntegrationTest {

    private static final String CRATE_SERVER_VERSION = clientVersion();
    static CrateTestServer server;

    private static String clientVersion() {
        String cp = System.getProperty("java.class.path");
        Matcher m = Pattern.compile("crate-client-([\\d\\.]{5,})\\.jar").matcher(cp);

        if (m.find()) {
            return m.group(1);
        }
        throw new RuntimeException("unable to get version of crate-client");
    }

    @ClassRule
    public static CrateTestCluster testCluster = CrateTestCluster.fromVersion(CRATE_SERVER_VERSION)
            .keepWorkingDir(false)
            .build();

    @BeforeClass
    public static void beforeClass() {
        server = testCluster.randomServer();
    }

}
