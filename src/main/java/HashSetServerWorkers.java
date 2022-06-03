
import java.util.HashSet;
import java.util.Set;

class HashSetServerWorkers implements ServerWorkers {

    private final Set<Worker> workers = new HashSet<>();

    @Override
    public void add(Worker worker) {
        workers.add(worker);
    }

    @Override
    public void remove(Worker worker) {
        workers.remove(worker);
    }

    @Override
    public void broadcast(String text, Integer roomNr) {
        workers.stream()
                .filter(worker -> worker.getRoomNumber() == roomNr)
                .forEach(worker -> worker.send(text));
    }

}