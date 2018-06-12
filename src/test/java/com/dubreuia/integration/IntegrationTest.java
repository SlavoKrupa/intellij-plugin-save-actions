package com.dubreuia.integration;

import static com.intellij.testFramework.LightProjectDescriptor.EMPTY_PROJECT_DESCRIPTOR;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;

import com.dubreuia.core.SaveActionManager;
import com.dubreuia.model.Storage;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.impl.LightTempDirTestFixtureImpl;

public class IntegrationTest {

    protected static final String REFORMAT_WITHOUT_ACTIVATION = "ReformatWithoutActivation";
    protected static final String REFORMAT_WITH_ACTIVATION = "ReformatWithActivation";


    // TODO add package for each actions
    // TODO add to travis
    // TODO migrate to java 5

    private CodeInsightTestFixture fixture;

    protected Storage storage;

    @Before
    public void before() throws Exception {
        IdeaTestFixtureFactory factory = IdeaTestFixtureFactory.getFixtureFactory();
        IdeaProjectTestFixture fixture = factory.createLightFixtureBuilder(EMPTY_PROJECT_DESCRIPTOR).getFixture();
        this.fixture = factory.createCodeInsightFixture(fixture, new LightTempDirTestFixtureImpl(true));
        this.fixture.setUp();
        this.fixture.setTestDataPath(getTestDataPath());
        this.storage = ServiceManager.getService(fixture.getProject(), Storage.class);
    }

    private String getTestDataPath() {
        Path classes = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        Path resources = Paths.get(classes.getParent().toString(), "resources");
        Path root = Paths.get(resources.toString(), getClass().getPackage().getName().split("[.]"));
        return root.toString();
    }

    protected void assertFile(String testCase) {
        fixture.configureByFile("before/" + testCase + ".java");
        new WriteCommandAction.Simple(fixture.getProject()) {
            @Override
            protected void run() {
                // set modification timestamp ++
                ((PsiFileImpl) fixture.getFile()).clearCaches();

                // call plugin on document
                new SaveActionManager().beforeDocumentSaving(fixture.getDocument(fixture.getFile()));
            }
        }.execute();
        fixture.checkResultByFile("after/" + testCase + ".java");
    }

}
