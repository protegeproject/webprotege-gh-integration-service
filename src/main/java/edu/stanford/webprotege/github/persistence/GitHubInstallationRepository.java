package edu.stanford.webprotege.github.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.webprotege.github.model.GitHubInstallationId;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GitHubInstallationRepository {

    public static final String COLLECTION_NAME = "GitHubInstallationIds";

    private final ObjectMapper mapper;

    private final MongoTemplate mongoTemplate;

    public GitHubInstallationRepository(ObjectMapper mapper, MongoTemplate mongoTemplate) {
        this.mapper = mapper;
        this.mongoTemplate = mongoTemplate;
    }

    public void setInstallationId(ProjectId projectId, GitHubInstallationId installationId) {
        var record = new GitHubInstallationRecord(projectId, installationId);
        var doc = mapper.convertValue(record, Document.class);
        mongoTemplate.getCollection(COLLECTION_NAME)
                .insertOne(doc);
    }

    public List<GitHubInstallationId> getInstallationIds(ProjectId projectId) {
        var query = new Document("projectId", projectId.id());
        var docs = mongoTemplate.getCollection(COLLECTION_NAME)
                .find();
        var result = new ArrayList<GitHubInstallationId>();
        docs.forEach(d -> {
            result.add(mapper.convertValue(d, GitHubInstallationRecord.class).installationId());
        });
//        var record = mapper.convertValue(doc, GitHubInstallationRecord.class);
        return result;
    }

    public void clearInstallationId(ProjectId projectId) {
        var query = new Document("projectId", projectId.id());
        mongoTemplate.getCollection(COLLECTION_NAME).deleteOne(query);
    }
}
