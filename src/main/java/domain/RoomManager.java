package domain;

import contracts.Manager;

import java.util.*;


public class RoomManager implements Manager {
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
        if(privateRoom.removeFromRoom(worker))
            rooms.remove(privateRoom);
    }
}
