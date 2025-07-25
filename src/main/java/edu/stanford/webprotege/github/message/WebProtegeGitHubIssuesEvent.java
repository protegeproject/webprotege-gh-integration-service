package edu.stanford.webprotege.github.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Event;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.webprotege.github.model.GitHubIssuesEvent;

import static edu.stanford.webprotege.github.message.WebProtegeGitHubIssuesEvent.CHANNEL;

@JsonTypeName(CHANNEL)
public record WebProtegeGitHubIssuesEvent(@JsonProperty("eventId") EventId eventId,
                                          @JsonProperty("issuesEvent") GitHubIssuesEvent issuesEvent) implements Event {

    public static final String CHANNEL = "webprotege.github.events.IssuesEvent";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
