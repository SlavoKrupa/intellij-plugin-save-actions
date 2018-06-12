package com.dubreuia.integration.activate;

import static com.dubreuia.model.Action.activate;
import static com.dubreuia.model.Action.reformat;

import org.junit.Test;

import com.dubreuia.integration.IntegrationTest;

public class ActivationIntegrationTest extends IntegrationTest {

    @Test
    public void should_reformat_without_activation_produces_same_file() {
        storage.setEnabled(reformat, true);
        assertFile(REFORMAT_WITHOUT_ACTIVATION);
    }

    @Test
    public void should_reformat_with_activation_produces_indented_file() {
        storage.setEnabled(activate, true);
        storage.setEnabled(reformat, true);
        assertFile(REFORMAT_WITH_ACTIVATION);
    }

    @Test
    public void should_reformat_with_shortcut_produces_same_file() {
        storage.setEnabled(activate, true);
        storage.setEnabled(reformat, true);
        assertFile(REFORMAT_WITH_ACTIVATION);
    }

}
