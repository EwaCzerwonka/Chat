
interface ServerWorkers {

    void add(Worker worker);

    void remove(Worker worker);

    void broadcast(String text, Integer roomNr);

}
