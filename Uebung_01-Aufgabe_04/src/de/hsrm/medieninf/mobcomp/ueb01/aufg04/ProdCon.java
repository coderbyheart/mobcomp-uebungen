package de.hsrm.medieninf.mobcomp.ueb01.aufg04;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;

import android.util.Log;

public class ProdCon implements Runnable {
	private boolean isConsumer = false;
	private ConcurrentLinkedQueue<Integer> queue;
	private boolean running = true;

	private Object mPauseLock;
	private boolean mPaused = false;
	
	private CyclicBarrier barrier;
	
	private final int UPPER_LIMIT = 100000;
	private int consumed = 0;
	private int lastPrime = 1;

	public ProdCon(ConcurrentLinkedQueue<Integer> queue) {
		setQueue(queue);
		mPauseLock = new Object();
	}

	public ProdCon(ConcurrentLinkedQueue<Integer> queue, boolean isConsumer, CyclicBarrier barrier) {
		setQueue(queue);
		setConsumer(isConsumer);
		mPauseLock = new Object();
		this.barrier = barrier; 
	}

	public boolean isConsumer() {
		return isConsumer;
	}

	public void setConsumer(boolean isConsumer) {
		this.isConsumer = isConsumer;
	}

	public ConcurrentLinkedQueue<Integer> getQueue() {
		return queue;
	}

	public void setQueue(ConcurrentLinkedQueue<Integer> queue) {
		this.queue = queue;
	}

	public void run() {
		String type = isConsumer() ? "Consumer" : "Producer";
		long start = System.currentTimeMillis();
		while (running) {
			if (isConsumer()) {
				if (consumed > UPPER_LIMIT) {
					Log.v(PrimSum.TAG, consumed + " Primzahlen konsumiert");
					stop();
				} else {
					consume();
				}
			} else {
				produce();
			}
			synchronized (mPauseLock) {
				while (mPaused) {
					Log.v(PrimSum.TAG, type + ": pausiert");
					try {
						mPauseLock.wait();
					} catch (InterruptedException e) {
					}
				}
			}
		}
		long end = System.currentTimeMillis();
		Log.v(PrimSum.TAG, type + ": Laufzeit war " + (end - start) + " ms.");
		if (isConsumer()) {
			try {
				barrier.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
		Log.v(PrimSum.TAG, type + ": Fertig.");
	}

	/**
	 * Call this on pause.
	 */
	public void onPause() {
		synchronized (mPauseLock) {
			mPaused = true;
		}
	}

	/**
	 * Call this on resume.
	 */
	public void onResume() {
		synchronized (mPauseLock) {
			mPaused = false;
			mPauseLock.notifyAll();
		}
	}

	private void produce() {
		int prime = getNextPrime();
		queue.offer(new Integer(prime));
		synchronized (queue) {
			queue.notifyAll();
		}
	}

	private void consume() {
		while (!queue.isEmpty()) {
			Integer prime = queue.poll();
			if (prime != null) {
				consumed++;
				lastPrime = prime;
				// Log.v(PrimSum.TAG, "Consumer: " + prime);
			}
		}
	}


	public int getLastPrime() {
		return lastPrime;
	}

	public void setLastPrime(int lastPrime) {
		this.lastPrime = lastPrime;
	}

	public int getNextPrime() {
		int i = lastPrime + 1;
		for (;; i++) {
			if (isPrime(i))
				break;
		}
		lastPrime = i;
		return i;
	}

	/**
	 * Prüft ob n eine Primzahl ist
	 * 
	 * @param n
	 * @return Boolean
	 */
	public boolean isPrime(int n) {
		if (n < 2) {
			return false;
		} else if (n == 2) {
			return true;
		} else if (n % 2 == 0) {
			return false;
		} else {
			for (int i = 3; i * i <= n; i += 2) {
				if (n % i == 0) {
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * Stop den Thread
	 */
	public void stop()
	{
		running = false;
	}

	public boolean isRunning() {
		return running;
	}
}
