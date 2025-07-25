package edu.stanford.webprotege.github.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Event;
import edu.stanford.protege.webprotege.common.EventId;
import edu.stanford.webprotege.github.model.GitHubPushEvent;

import static edu.stanford.webprotege.github.message.WebProtegeGitHubPushEvent.CHANNEL;

@JsonTypeName(CHANNEL)
public record WebProtegeGitHubPushEvent(@JsonProperty("eventId") EventId eventId,
                                        @JsonProperty("pushEvent") GitHubPushEvent pushEvent) implements Event {

    public static final String CHANNEL = "webprotege.github.events.PushEvent";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
