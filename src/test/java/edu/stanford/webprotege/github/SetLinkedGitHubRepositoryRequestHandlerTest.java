package edu.stanford.webprotege.github;

import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.webprotege.github.handler.SetLinkedGitHubRepositoryRequestHandler;
import edu.stanford.webprotege.github.message.LinkedGitHubRepositoryChangedEvent;
import edu.stanford.webprotege.github.message.SetLinkedGitHubRepositoryRequest;
import edu.stanford.webprotege.github.model.GitHubRepositoryCoordinates;
import edu.stanford.webprotege.github.persistence.LinkedGitHubRepositoryRecordStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@ExtendWith(MongoTestExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class SetLinkedGitHubRepositoryRequestHandlerTest {

    protected static final String OWNER_NAME = "A";

    protected static final String REPOSITORY_NAME = "B";

    private SetLinkedGitHubRepositoryRequestHandler handler;

    @Autowired
    private LinkedGitHubRepositoryRecordStore store;

    private ProjectId projectId;

    @Mock
    private EventDispatcher eventDispatcher;

    private GitHubRepositoryCoordinates coordinates;

    @BeforeEach
    void setUp() {
        handler = new SetLinkedGitHubRepositoryRequestHandler(store, eventDispatcher);
        projectId = ProjectId.generate();
        coordinates = new GitHubRepositoryCoordinates(OWNER_NAME,
                                                                    REPOSITORY_NAME);
    }

    @AfterEach
    void tearDown() {
        store.deleteAll();
    }

    @Test
    void shouldSetRecord() {
        var response = handler.handleRequest(new SetLinkedGitHubRepositoryRequest(projectId, coordinates), null);
        var r = response.block();
        assertThat(store.count()).isEqualTo(1L);
    }

    @Test
    void shouldReturnCorrectResponse() {
        var response = handler.handleRequest(new SetLinkedGitHubRepositoryRequest(projectId, coordinates), null);
        var r = response.block();
        assertThat(r).isNotNull();
        assertThat(r.projectId()).isEqualTo(projectId);
        assertThat(r.repositoryCoordinates().ownerName()).isEqualTo(OWNER_NAME);
        assertThat(r.repositoryCoordinates().repositoryName()).isEqualTo(REPOSITORY_NAME);
    }

    @Test
    void shouldFireEvent() {
        var response = handler.handleRequest(new SetLinkedGitHubRepositoryRequest(projectId, coordinates), null);
        var r = response.block();
        verify(eventDispatcher, times(1)).dispatchEvent(any(LinkedGitHubRepositoryChangedEvent.class));
    }
}