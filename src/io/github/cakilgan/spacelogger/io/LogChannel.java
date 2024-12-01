package io.github.cakilgan.spacelogger.io;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class LogChannel implements AutoCloseable {
    private final ExecutorService worker;
    private final ConcurrentLinkedQueue<LogMessage<?>> queue;
    private final List<LogMessage<?>> allocated;
    private final AtomicBoolean running;
    private volatile Thread currentThread;
    private int count = 0;

    public LogChannel(String channelName) {
        ThreadFactory factory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(channelName);
                currentThread = thread;
                return thread;
            }
        };
        this.worker = Executors.newSingleThreadExecutor(factory);
        this.queue = new ConcurrentLinkedQueue<>();
        this.running = new AtomicBoolean(false);

        allocated =new CopyOnWriteArrayList<>();
    }

    public void push(LogMessage<?> msg) {
        queue.offer(msg);
        startIfNotRunning();
    }

    public void startIfNotRunning() {
        if (running.compareAndSet(false, true)) {
            worker.submit(() -> {
                try {
                    while (running.get() || !queue.isEmpty()) {
                        LogMessage<?> logMsg = queue.poll();
                        if (logMsg != null) {
                            processMsg(logMsg);
                            if (logMsg.priority.equals(LogPriority.ALLOC)){
                                allocated.add(logMsg);
                            }
                        }
                    }
                } finally {
                    running.set(false);
                }
            });
        }
    }

    public void stop() {
        running.set(false);
    }

    @Override
    public void close() {
        stop();
        worker.shutdown();
    }

    public List<LogMessage<?>> getAllocated() {
        return allocated;
    }
    public Thread getCurrentThread() {
        return currentThread;
    }
    public int getCount() {
        return count;
    }

    protected abstract void processMsg(LogMessage<?> logMsg);
}
