package edu.stanford.webprotege.github.handler;

import edu.stanford.protege.webprotege.authorization.BasicCapability;
import edu.stanford.protege.webprotege.authorization.Capability;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Resource;
import edu.stanford.protege.webprotege.ipc.AuthorizedCommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.webprotege.github.persistence.LinkedGitHubRepositoryRecordStore;
import edu.stanford.webprotege.github.message.GetLinkedGitHubRepositoryRequest;
import edu.stanford.webprotege.github.message.GetLinkedGitHubRepositoryResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-20
 */
@WebProtegeHandler
public class GetLinkedGitHubRepositoryRequestHandler implements AuthorizedCommandHandler<GetLinkedGitHubRepositoryRequest, GetLinkedGitHubRepositoryResponse> {

    private static final Logger logger = LoggerFactory.getLogger(GetLinkedGitHubRepositoryRequestHandler.class);

    private final LinkedGitHubRepositoryRecordStore store;

    public GetLinkedGitHubRepositoryRequestHandler(LinkedGitHubRepositoryRecordStore store) {
        this.store = store;
    }

    @Nonnull
    @Override
    public Resource getTargetResource(GetLinkedGitHubRepositoryRequest request) {
        return ProjectResource.forProject(request.projectId());
    }

    @NotNull
    @Override
    public Collection<Capability> getRequiredCapabilities() {
        return List.of(BasicCapability.valueOf("ViewProject"));
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return GetLinkedGitHubRepositoryRequest.CHANNEL;
    }

    @Override
    public Class<GetLinkedGitHubRepositoryRequest> getRequestClass() {
        return GetLinkedGitHubRepositoryRequest.class;
    }

    @Override
    public Mono<GetLinkedGitHubRepositoryResponse> handleRequest(GetLinkedGitHubRepositoryRequest request,
                                                                 ExecutionContext executionContext) {
        logger.info("Getting linked GitHub repository for {}", request.projectId());
        var projectId = request.projectId();
        var future = CompletableFuture.supplyAsync(() -> {
            var found = store.findById(request.projectId());
            logger.info("Found linked GitHub repository {}", found);
            return found.map(r -> new GetLinkedGitHubRepositoryResponse(projectId, r.repositoryCoordinates()))
                                .orElse(new GetLinkedGitHubRepositoryResponse(projectId, null));
        });
        return Mono.fromFuture(future);
    }
}
