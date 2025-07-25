package edu.stanford.webprotege.github.persistence;

import edu.stanford.protege.webprotege.common.ProjectId;
import org.springframework.data.repository.CrudRepository;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-20
 */
public interface LinkedGitHubRepositoryRecordStore extends CrudRepository<LinkedGitHubRepositoryRecord, ProjectId> {


}
