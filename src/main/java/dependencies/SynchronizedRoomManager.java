package dependencies;

import contracts.Manager;
import domain.Worker;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SynchronizedRoomManager implements Manager {

    private final Manager manager;
    @Override
    public synchronized boolean joinRoom(Integer roomNumber, Worker worker) {
        return manager.joinRoom(roomNumber, worker);
    }

    @Override
    public synchronized void leaveRoom(Worker worker) {
        manager.leaveRoom(worker);
    }
}
