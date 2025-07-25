package edu.stanford.webprotege.github.handler;

import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.webprotege.github.service.GitHubAppInstallationCheckerService;
import edu.stanford.webprotege.github.message.GetGitHubAppInstallationStatusRequest;
import edu.stanford.webprotege.github.message.GetGitHubAppInstallationStatusResponse;
import edu.stanford.webprotege.github.model.GitHubAppInstallationStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@WebProtegeHandler
public class GetGitHubAppInstallationStatusRequestHandler implements CommandHandler<GetGitHubAppInstallationStatusRequest, GetGitHubAppInstallationStatusResponse> {

    private static final Logger logger = LoggerFactory.getLogger(GetGitHubAppInstallationStatusRequestHandler.class);

    private final GitHubAppInstallationCheckerService checkerService;

    public GetGitHubAppInstallationStatusRequestHandler(GitHubAppInstallationCheckerService checkerService) {
        this.checkerService = checkerService;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return GetGitHubAppInstallationStatusRequest.CHANNEL;
    }

    @Override
    public Class<GetGitHubAppInstallationStatusRequest> getRequestClass() {
        return GetGitHubAppInstallationStatusRequest.class;
    }

    @Override
    public Mono<GetGitHubAppInstallationStatusResponse> handleRequest(GetGitHubAppInstallationStatusRequest request, ExecutionContext executionContext) {
        logger.info("Getting gitHub app installation status");
        return checkerService.isAppInstalledOnRepo(request.repositoryCoordinates().ownerName(), request.repositoryCoordinates().repositoryName())
                .map(installed -> {
                    var installationStatus = installed ? GitHubAppInstallationStatus.INSTALLED : GitHubAppInstallationStatus.NOT_INSTALLED;
                    return new GetGitHubAppInstallationStatusResponse(request.projectId(), request.repositoryCoordinates(), installationStatus);
                });
    }
}
