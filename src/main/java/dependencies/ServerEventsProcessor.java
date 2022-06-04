package dependencies;

import contracts.ServerWorkers;
import domain.ServerEvent;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class ServerEventsProcessor implements Consumer<ServerEvent> {

    private final ServerWorkers serverWorkers;

    @Override
    public void accept(ServerEvent event) {
        switch (event.getType()) {
            case MESSAGE_RECEIVED -> serverWorkers.broadcast(event.getPayload(), event.getSource().getRoomNumber());
            case CONNECTION_CLOSED -> serverWorkers.remove(event.getSource());
            case ROOM_CREATED -> serverWorkers.broadcast(event.getPayload(), 0);
            case HELP -> serverWorkers.display(event.getSource(), event.getPayload());
        }
    }

}
