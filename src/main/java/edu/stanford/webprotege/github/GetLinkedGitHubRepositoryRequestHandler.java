package edu.stanford.webprotege.github;

import edu.stanford.protege.github.server.GetLinkedGitHubRepositoryRequest;
import edu.stanford.protege.github.server.GetLinkedGitHubRepositoryResponse;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.authorization.ProjectResource;
import edu.stanford.protege.webprotege.authorization.Resource;
import edu.stanford.protege.webprotege.ipc.AuthorizedCommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-10-20
 */
@WebProtegeHandler
public class GetLinkedGitHubRepositoryRequestHandler implements AuthorizedCommandHandler<GetLinkedGitHubRepositoryRequest, GetLinkedGitHubRepositoryResponse> {

    private final LinkedGitHubRepositoryRecordStore store;

    public GetLinkedGitHubRepositoryRequestHandler(LinkedGitHubRepositoryRecordStore store) {
        this.store = store;
    }

    @Nonnull
    @Override
    public Resource getTargetResource(GetLinkedGitHubRepositoryRequest request) {
        return ProjectResource.forProject(request.projectId());
    }

    @Nonnull
    @Override
    public Collection<ActionId> getRequiredCapabilities() {
        return Set.of(ActionId.valueOf("ViewProject"));
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
        var projectId = request.projectId();
        var future = CompletableFuture.supplyAsync(() -> {
            var found = store.findById(request.projectId());
            return found.map(r -> new GetLinkedGitHubRepositoryResponse(projectId, r.repositoryCoordinates()))
                                .orElse(new GetLinkedGitHubRepositoryResponse(projectId, null));
        });
        return Mono.fromFuture(future);
    }
}
