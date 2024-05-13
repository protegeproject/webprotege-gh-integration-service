package edu.stanford.webprotege.github;

import edu.stanford.protege.github.server.GitHubRepositoryCoordinates;
import edu.stanford.protege.webprotege.common.ProjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "webprotege.rabbitmq.commands-subscribe=false")
@ExtendWith(MongoTestExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class LinkedGitHubRepositoryRecordStore_IT {

    @Autowired
    private LinkedGitHubRepositoryRecordStore store;

    private ProjectId projectId;

    private GitHubRepositoryCoordinates coords;

    private LinkedGitHubRepositoryRecord record;

    @BeforeEach
    void setUp() {
        projectId = ProjectId.generate();
        coords = new GitHubRepositoryCoordinates("A", "B");
        record = new LinkedGitHubRepositoryRecord(projectId, coords);
    }

    @AfterEach
    void tearDown() {
        store.deleteAll();
    }

    @Test
    void shouldSaveRecord() {
        store.save(record);
        assertThat(store.count()).isEqualTo(1L);
    }

    @Test
    void shouldSaveDuplicateRecord() {
        store.save(record);
        store.save(record);
        assertThat(store.count()).isEqualTo(1L);
    }

    @Test
    void shouldOverwrite() {
        store.save(record);
        store.save(new LinkedGitHubRepositoryRecord(projectId, new GitHubRepositoryCoordinates("C", "D")));
        assertThat(store.count()).isEqualTo(1L);
        assertThat(store.findById(projectId)).map(LinkedGitHubRepositoryRecord::repositoryCoordinates).map(GitHubRepositoryCoordinates::ownerName).hasValue("C");
        assertThat(store.findById(projectId)).map(LinkedGitHubRepositoryRecord::repositoryCoordinates).map(GitHubRepositoryCoordinates::repositoryName).hasValue("D");
    }

    @Test
    void shouldFind() {
        store.save(record);
        var found = store.findById(projectId);
        assertThat(found).isPresent();
        assertThat(found).map(LinkedGitHubRepositoryRecord::projectId).hasValue(projectId);
    }

    @Test
    void shouldDeleteById() {
        store.save(record);
        assertThat(store.count()).isEqualTo(1L);
        store.deleteById(projectId);
        assertThat(store.count()).isEqualTo(0L);
    }
}