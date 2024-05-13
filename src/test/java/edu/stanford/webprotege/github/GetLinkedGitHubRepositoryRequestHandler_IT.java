package edu.stanford.webprotege.github;

import edu.stanford.protege.github.server.GetLinkedGitHubRepositoryRequest;
import edu.stanford.protege.github.server.GitHubRepositoryCoordinates;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ExtendWith(MongoTestExtension.class)
class GetLinkedGitHubRepositoryRequestHandler_IT {

    protected static final String OWNER_NAME = "A";

    protected static final String REPOSITORY_NAME = "B";

    private GetLinkedGitHubRepositoryRequestHandler handler;

    @Autowired
    private LinkedGitHubRepositoryRecordStore store;

    private ProjectId projectId;

    @BeforeEach
    void setUp() {
        handler = new GetLinkedGitHubRepositoryRequestHandler(store);
        projectId = ProjectId.generate();
        var repositoryCoordinates = new GitHubRepositoryCoordinates(OWNER_NAME,
                                                                    REPOSITORY_NAME);
        store.save(new LinkedGitHubRepositoryRecord(projectId, repositoryCoordinates));
    }

    @Test
    void shouldGetRecord() {
        var response = handler.handleRequest(new GetLinkedGitHubRepositoryRequest(projectId), null);
        var r = response.block();
        assertThat(r.projectId()).isEqualTo(projectId);
        assertThat(r).isNotNull();
        assertThat(r.getRepositoryCoordinates()).isPresent();
        assertThat(r.getRepositoryCoordinates()).map(GitHubRepositoryCoordinates::ownerName).hasValue(OWNER_NAME);
        assertThat(r.getRepositoryCoordinates()).map(GitHubRepositoryCoordinates::repositoryName).hasValue(REPOSITORY_NAME);
    }
}