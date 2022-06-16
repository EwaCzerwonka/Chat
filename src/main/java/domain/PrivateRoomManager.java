package domain;

import contracts.RoomManager;

import java.util.*;


public class PrivateRoomManager implements RoomManager {
    private final Set<PrivateRoom> rooms = new HashSet<>();

    public boolean joinRoom(Integer number, Worker worker){
        boolean isCreated = false;
        List<PrivateRoom> found = rooms.stream().filter(room -> room.getRoomNumber() == number).toList();
        if(found.isEmpty()){
            PrivateRoom room = new PrivateRoom(number);
            room.addToRoom(worker);
            rooms.add(room);
            isCreated = true;
        } else {
            found.get(0).addToRoom(worker);
        }
        return isCreated;
    }

    public void leaveRoom(Worker worker){
        PrivateRoom privateRoom = rooms.stream().filter(room -> room.getRoomNumber() == worker.getRoomNumber()).toList().get(0);
        privateRoom.removeFromRoom(worker);
    }


    public boolean canRead(Worker worker, Integer roomNumber) {
        return rooms.stream()
                .filter(room -> room.getRoomNumber() == roomNumber)
                .anyMatch(room -> room.getHistoryWorkers().contains(worker));
    }
}
