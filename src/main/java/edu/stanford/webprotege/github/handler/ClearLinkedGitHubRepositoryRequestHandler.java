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
import edu.stanford.webprotege.github.persistence.LinkedGitHubRepositoryRecordStore;
import edu.stanford.webprotege.github.message.ClearLinkedGitHubRepositoryRequest;
import edu.stanford.webprotege.github.message.ClearLinkedGitHubRepositoryResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2024-05-12
 */
@WebProtegeHandler
public class ClearLinkedGitHubRepositoryRequestHandler implements AuthorizedCommandHandler<ClearLinkedGitHubRepositoryRequest, ClearLinkedGitHubRepositoryResponse> {

    private final Logger logger = LoggerFactory.getLogger(ClearLinkedGitHubRepositoryRequestHandler.class);

    private final LinkedGitHubRepositoryRecordStore store;

    private final EventDispatcher eventDispatcher;

    public ClearLinkedGitHubRepositoryRequestHandler(LinkedGitHubRepositoryRecordStore store,
                                                     EventDispatcher eventDispatcher) {
        this.store = store;
        this.eventDispatcher = eventDispatcher;
    }

    @Nonnull
    @Override
    public Resource getTargetResource(ClearLinkedGitHubRepositoryRequest request) {
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
        return ClearLinkedGitHubRepositoryRequest.CHANNEL;
    }

    @Override
    public Class<ClearLinkedGitHubRepositoryRequest> getRequestClass() {
        return ClearLinkedGitHubRepositoryRequest.class;
    }

    @Override
    public Mono<ClearLinkedGitHubRepositoryResponse> handleRequest(ClearLinkedGitHubRepositoryRequest request,
                                                                 ExecutionContext executionContext) {
        var projectId = request.projectId();
        if(store.findById(projectId).isPresent()) {
            store.deleteById(projectId);
            logger.info("{} Cleared linked project to GitHub repository", projectId);
            eventDispatcher.dispatchEvent(new LinkedGitHubRepositoryChangedEvent(EventId.generate(),
                                                                                 projectId, null));
        }
        var response = new ClearLinkedGitHubRepositoryResponse(projectId);
        return Mono.just(response);
    }
}
