package edu.stanford.webprotege.github;

import edu.stanford.protege.github.server.ClearLinkedGitHubRepositoryRequest;
import edu.stanford.protege.github.server.GitHubRepositoryCoordinates;
import edu.stanford.protege.github.server.LinkedGitHubRepositoryChangedEvent;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-13
 */
@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@ExtendWith(MockitoExtension.class)
@ExtendWith(MongoTestExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Testcontainers
public class ClearLinkedGitHubRepositoryRequestHandler_IT {

    protected static final String OWNER_NAME = "A";

    protected static final String REPOSITORY_NAME = "B";

    private ClearLinkedGitHubRepositoryRequestHandler handler;

    @Autowired
    private LinkedGitHubRepositoryRecordStore store;

    private ProjectId projectId;

    @Mock
    private EventDispatcher eventDispatcher;


    @BeforeEach
    void setUp() {
        handler = new ClearLinkedGitHubRepositoryRequestHandler(store, eventDispatcher);
        projectId = ProjectId.generate();
        store.save(new LinkedGitHubRepositoryRecord(projectId, new GitHubRepositoryCoordinates("A", "B")));
    }

    @AfterEach
    void tearDown() {
        store.deleteAll();
    }

    @Test
    void shouldClearRecord() {
        var response = handler.handleRequest(new ClearLinkedGitHubRepositoryRequest(projectId), null);
        var r = response.block();
        assertThat(store.count()).isEqualTo(0);
    }

    @Test
    void shouldReturnCorrectResponse() {
        var response = handler.handleRequest(new ClearLinkedGitHubRepositoryRequest(projectId), null);
        var r = response.block();
        assertThat(r).isNotNull();
        assertThat(r.projectId()).isEqualTo(projectId);
    }

    @Test
    void shouldFireEvent() {
        var response = handler.handleRequest(new ClearLinkedGitHubRepositoryRequest(projectId), null);
        var r = response.block();
        verify(eventDispatcher, times(1)).dispatchEvent(any(LinkedGitHubRepositoryChangedEvent.class));
    }
}
