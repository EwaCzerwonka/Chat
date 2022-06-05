package dependencies;

import domain.FileWorker;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RequiredArgsConstructor
public class SynchronizedFileWorker implements contracts.FileManager {

    private final FileWorker worker;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    @Override
    public String upload(String pathName, Integer level) {
        String fileName;
        lock.writeLock().lock();
        fileName = worker.upload(pathName, level);
        lock.writeLock().unlock();
        return fileName;
    }

    @Override
    public String download(Integer level) {
        String filePath;
        lock.readLock().lock();
        filePath = worker.download(level);
        lock.readLock().unlock();
        return filePath;
    }
}
