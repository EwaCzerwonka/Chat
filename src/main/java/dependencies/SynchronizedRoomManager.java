package dependencies;

import contracts.RoomManager;
import domain.Worker;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SynchronizedRoomManager implements RoomManager {

    private final RoomManager roomManager;
    @Override
    public synchronized boolean joinRoom(Integer roomNumber, Worker worker) {
        return roomManager.joinRoom(roomNumber, worker);
    }

    @Override
    public synchronized void leaveRoom(Worker worker) {
        roomManager.leaveRoom(worker);
    }
}
