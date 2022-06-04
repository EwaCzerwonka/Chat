package contracts;

import domain.Worker;

public interface ServerWorkers {

    void add(Worker worker);

    void remove(Worker worker);

    void broadcast(String text, Integer roomNr);

    void display(Worker worker, String text);

}
