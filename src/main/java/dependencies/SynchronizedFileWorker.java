package dependencies;

import domain.FileWorker;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RequiredArgsConstructor
public class SynchronizedFileWorker implements contracts.FileManager {

    private final FileWorker fileWorker;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    @Override
    public String upload(String pathName, Integer level) {
        String fileName;
        lock.writeLock().lock();
        fileName = fileWorker.upload(pathName, level);
        lock.writeLock().unlock();
        return fileName;
    }

    @Override
    public String download(Integer level) {
        String filePath;
        lock.readLock().lock();
        filePath = fileWorker.download(level);
        lock.readLock().unlock();
        return filePath;
    }

    @Override
    public File prepareFile(String filePath) {
        File file;
        lock.writeLock().lock();
        file = fileWorker.prepareFile(filePath);
        lock.writeLock().unlock();
        return file;
    }

    @Override
    public void saveToFile(String text, File file) {
        lock.writeLock().lock();
        fileWorker.saveToFile(text, file);
        lock.writeLock().unlock();
    }

    @Override
    public List<String> getHistory(String roomNumber, File file) {
        List<String> list;
        lock.readLock().lock();
        list = fileWorker.getHistory(roomNumber, file);
        lock.readLock().unlock();
        return list;
    }
}
