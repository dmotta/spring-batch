package org.springframework.batch.sample.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

/**
 * Remembers all items written - useful for testing.
 */
public class ItemTrackingItemWriter<T> implements ItemWriter<T> {

	private List<T> items = new ArrayList<T>();
	
	private T failed = null;

	private int failure = -1;

	private int counter = 0;

	public void write(List<? extends T> items) throws Exception {
		if (failed!=null && items.contains(failed)) {
			throw new RuntimeException("write failed again");			
		}
		this.items.addAll(items);
		int current = counter;
		counter += items.size();
		if (current < failure && counter >= failure) {
			failed = items.get(failure-current-1);
			this.items.remove(failed);
			throw new RuntimeException("write failed");
		}
	}

	public List<T> getItems() {
		return items;
	}

	public void setWriteFailure(int failure) {
		this.failure = failure;
	}

}