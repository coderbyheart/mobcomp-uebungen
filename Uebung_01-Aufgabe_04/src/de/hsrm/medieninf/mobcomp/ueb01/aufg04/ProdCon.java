package de.hsrm.medieninf.mobcomp.ueb01.aufg04;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.util.Log;

public class ProdCon implements Runnable {
	private boolean isConsumer = false;
	private ConcurrentLinkedQueue<Integer> queue;

	public ProdCon(ConcurrentLinkedQueue<Integer> queue) {
		setQueue(queue);
	}

	public ProdCon(ConcurrentLinkedQueue<Integer> queue, boolean isConsumer) {
		setQueue(queue);
		setConsumer(isConsumer);
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
		if (isConsumer()) {
			while (true) {
				consume();
			}
		} else {
			while (true) {
				produce();
			}
		}

	}

	// private final int UPPER_LIMIT = 10000000;
	private final int UPPER_LIMIT = 100;
	private int lastPrime = 2;

	public int getNextPrime() {
		int i = lastPrime;
		while (++i <= UPPER_LIMIT) {
			int i1 = (int) Math.ceil(Math.sqrt(i));
			while (i1 > 1) {
				if ((i != i1) && (i % i1 == 0)) {
					continue;
				} else {
					break;
				}
			}
		}
		return i;
	}

	private void produce() {
		int prime = getNextPrime();
		queue.offer(new Integer(prime));
		synchronized (queue) {
			queue.notifyAll();
		}
		Log.v(PrimSum.TAG, "Producer: " + prime);
	}

	private void consume() {
		while (!queue.isEmpty()) {
			Integer prime = queue.poll();
			if (prime != null) {
				Log.v(PrimSum.TAG, "Consumer: " + prime);
			}
		}
	}
}
