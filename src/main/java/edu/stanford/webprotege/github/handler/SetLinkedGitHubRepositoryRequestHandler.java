package edu.stanford.webprotege.github.handler;

import edu.stanford.protege.webprotege.authorization.BasicCapability;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Resource;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.protege.webprotege.ipc.AuthorizedCommandHandler;
import edu.stanford.protege.webprotege.ipc.EventDispatcher;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.webprotege.github.message.LinkedGitHubRepositoryChangedEvent;
import edu.stanford.webprotege.github.persistence.LinkedGitHubRepositoryRecord;
import edu.stanford.webprotege.github.persistence.LinkedGitHubRepositoryRecordStore;
import edu.stanford.webprotege.github.message.SetLinkedGitHubRepositoryRequest;
import edu.stanford.webprotege.github.message.SetLinkedGitHubRepositoryResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-20
 */
@WebProtegeHandler
public class SetLinkedGitHubRepositoryRequestHandler implements AuthorizedCommandHandler<SetLinkedGitHubRepositoryRequest, SetLinkedGitHubRepositoryResponse> {

    private final Logger logger = LoggerFactory.getLogger(SetLinkedGitHubRepositoryRequestHandler.class);

    private final LinkedGitHubRepositoryRecordStore store;

    private final EventDispatcher eventDispatcher;

    public SetLinkedGitHubRepositoryRequestHandler(LinkedGitHubRepositoryRecordStore store,
                                                   EventDispatcher eventDispatcher) {
        this.store = store;
        this.eventDispatcher = eventDispatcher;
    }

    @Nonnull
    @Override
    public Resource getTargetResource(SetLinkedGitHubRepositoryRequest request) {
        return ProjectResource.forProject(request.projectId());
    }

    @NotNull
    @Override
    public Collection<Capability> getRequiredCapabilities() {
        return List.of(BasicCapability.valueOf("EditProjectSettings"));
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return SetLinkedGitHubRepositoryRequest.CHANNEL;
    }

    @Override
    public Class<SetLinkedGitHubRepositoryRequest> getRequestClass() {
        return SetLinkedGitHubRepositoryRequest.class;
    }

    @Override
    public Mono<SetLinkedGitHubRepositoryResponse> handleRequest(SetLinkedGitHubRepositoryRequest request,
                                                               ExecutionContext executionContext) {
        var projectId = request.projectId();
        logger.info("{} Handling request to link project to GitHub repository: {}", projectId, request.repositoryCoordinates());
        var existing = store.findById(projectId);
        var repositoryCoordinates = request.repositoryCoordinates();
        var record = LinkedGitHubRepositoryRecord.of(projectId, repositoryCoordinates);
        if(!existing.equals(Optional.of(record))) {
            store.save(record);
            logger.info("{} Linked project to GitHub repository at {}", projectId, repositoryCoordinates.getFullName());
            eventDispatcher.dispatchEvent(new LinkedGitHubRepositoryChangedEvent(EventId.generate(),
                                                                                 projectId, repositoryCoordinates));
        }
        var response = new SetLinkedGitHubRepositoryResponse(projectId, repositoryCoordinates);
        return Mono.just(response);
    }
}
