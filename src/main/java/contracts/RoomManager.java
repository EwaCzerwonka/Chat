package contracts;

import domain.Worker;

public interface RoomManager {
    boolean joinRoom(Integer roomNumber, Worker worker);
    void leaveRoom(Worker worker);
    boolean canRead(Worker worker, Integer roomNumber);
}
