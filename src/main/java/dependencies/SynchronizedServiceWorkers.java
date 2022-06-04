package dependencies;

import contracts.ServerWorkers;
import domain.Worker;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RequiredArgsConstructor
public
class SynchronizedServiceWorkers implements ServerWorkers {

    private final ServerWorkers serverWorkers;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void add(Worker worker) {
        lock.writeLock().lock();
        serverWorkers.add(worker);
        lock.writeLock().unlock();
    }

    @Override
    public void remove(Worker worker) {
        lock.writeLock().lock();
        serverWorkers.remove(worker);
        lock.writeLock().unlock();
    }

    @Override
    public void broadcast(String text, Integer roomNr) {
        lock.readLock().lock();
        serverWorkers.broadcast(text, roomNr);
        lock.readLock().unlock();
    }

    @Override
    public void display(Worker worker, String text) {
        lock.readLock().lock();
        serverWorkers.display(worker, text);
        lock.readLock().unlock();
    }

}
