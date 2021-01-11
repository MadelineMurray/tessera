package com.quorum.tessera.version;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PrivacyGroupVersionTest {

    private PrivacyGroupVersion version = new PrivacyGroupVersion();

    @Test
    public void getVersion() {
        assertThat(version.getVersion()).isEqualTo("3");
    }
}
