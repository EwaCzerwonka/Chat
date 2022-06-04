package contracts;

import domain.Worker;

public interface Manager {
    boolean joinRoom(Integer roomNumber, Worker worker);
    void leaveRoom(Worker worker);
}
