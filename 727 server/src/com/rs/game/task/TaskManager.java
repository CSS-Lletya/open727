package com.rs.game.task;

import java.util.Iterator;
import java.util.Objects;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

/**
 * Handles the processing and execution of {@link Task}s.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class TaskManager {
	
	/**
	 * A {@link ObjectList} of tasks that have been submitted.
	 */
	private final ObjectList<Task> tasks = new ObjectArrayList<>();
	
	/**
	 * Runs an iteration of the {@link Task} processing logic. All {@link Exception}s thrown by {@code Task}s.
	 */
	public void sequence() {
		if(!tasks.isEmpty()) {
			Iterator<Task> $it = tasks.iterator();
			while($it.hasNext()) {
				Task it = $it.next();
				
				if(!it.isRunning()) {
					$it.remove();
					continue;
				}
				it.onSequence();
				if(it.needsExecute() && it.canExecute()) {
					try {
						it.execute();
					} catch(Exception e) {
						it.onException(e);
					}
				}
			}
		}
	}
	
	/**
	 * Schedules {@code t} to run in the underlying {@code TaskManager}.
	 * @param task The {@link Task} to schedule.
	 */
	public void submit(Task task) {
		if(!task.canExecute()) {
			return;
		}
		task.onSubmit();
		if(task.isInstant()) {
			try {
				task.execute();
			} catch(Exception e) {
				e.printStackTrace();
				task.onException(e);
			}
		}
		tasks.add(task);
	}
	
	/**
	 * Iterates through all active {@link Task}s and cancels all that have {@code attachment} as their attachment.
	 */
	public void cancel(Object attachment) {
		for(Task tasks : tasks) {
			if(Objects.equals(attachment, tasks.getAttachment()))
				tasks.cancel();
		}
	}
}