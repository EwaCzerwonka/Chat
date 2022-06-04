package domain;

import domain.Worker;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public
class ServerEvent {

    private final ServerEventType type;
    private String payload;
    private Worker source;

}
