import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class PrivateRoom {
 private final Integer roomNumber;

 private Set<Worker> workers = new HashSet<>();
 private Set<Worker> historyWorkers = new HashSet<>();


 PrivateRoom(Integer roomNumber){
     this.roomNumber = roomNumber;
 }

 public void addToRoom(Worker worker){
     workers.add(worker);
     historyWorkers.add(worker);
 }

 public boolean removeFromRoom(Worker worker) {
     workers.remove(worker);
     return workers.isEmpty();
 }
}
