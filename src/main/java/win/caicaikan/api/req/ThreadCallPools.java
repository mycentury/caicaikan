package win.caicaikan.api.req;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public class ThreadCallPools<T extends Callable<?>> {
	private static final Logger logger = Logger.getLogger(ThreadCallPools.class);

	public ThreadCallPools(int maxSize) {
		this.maxSize = maxSize;
	}

	// 公用线程池
	public final static ExecutorService executor = Executors.newCachedThreadPool();
	private final Integer maxSize;
	public AtomicInteger currentSize = new AtomicInteger(0);

	public Future<?> submit(Callable<?> t) {
		Future<?> re = null;
		while (true) {
			if (currentSize.incrementAndGet() < maxSize) {
				re = executor.submit(t);
				break;
			} else {
				currentSize.decrementAndGet();
			}
			try {
				synchronized (this) {
					wait(1000 * 2);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}
		}
		return re;
	}

	public void free() {
		currentSize.getAndDecrement();
		synchronized (this) {
			notifyAll();
		}
	}
	
	

	public abstract static class PoolCallableRunnable<V> implements Callable<V> {

		private ThreadCallPools<? extends PoolCallableRunnable<V>> pools;

		public PoolCallableRunnable(ThreadCallPools<? extends PoolCallableRunnable<V>> pools) {
			this.pools = pools;
		}

		@Override
		public V call() throws Exception {
			try {
				return excute();
			} catch (Exception e) {
				logger.error(e);
				throw e;
			} finally {
				pools.free();
			}
		}

		protected abstract V excute();
	}

}
