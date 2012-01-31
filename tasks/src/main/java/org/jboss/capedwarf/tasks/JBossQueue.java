/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.capedwarf.tasks;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueConstants;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * JBoss Queue.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class JBossQueue implements Queue {
    private static Map<String, Queue> cache = new HashMap<String, Queue>();

    private final String queueName;

    public static synchronized Queue getQueue(String queueName) {
        Queue queue = cache.get(queueName);
        if (queue == null) {
            queue = new JBossQueue(queueName);
            cache.put(queueName, queue);
        }
        return queue;
    }

    private JBossQueue(String queueName) {
        validateQueueName(queueName);
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

    public TaskHandle add() {
        return add(TaskOptions.Builder.withDefaults());
    }

    public TaskHandle add(TaskOptions taskOptions) {
        return add(Collections.singleton(taskOptions)).get(0);
    }

    public List<TaskHandle> add(Iterable<TaskOptions> taskOptionses) {
        return add(DatastoreServiceFactory.getDatastoreService().getCurrentTransaction(), taskOptionses);
    }

    public TaskHandle add(Transaction transaction, TaskOptions taskOptions) {
        return add(transaction, Collections.singleton(taskOptions)).get(0);
    }

    public List<TaskHandle> add(Transaction transaction, Iterable<TaskOptions> taskOptionses) {
        return null;  // TODO
    }

    public boolean deleteTask(String taskName) {
        validateTaskName(taskName);
        return deleteTask(new TaskHandle(TaskOptions.Builder.withTaskName(taskName), queueName));
    }

    public boolean deleteTask(TaskHandle taskHandle) {
        return deleteTask(Collections.singletonList(taskHandle)).get(0);
    }

    public List<Boolean> deleteTask(List<TaskHandle> taskHandles) {
        return null;  // TODO
    }

    public List<TaskHandle> leaseTasks(long lease, TimeUnit unit, long countLimit) {
        return leaseTasksByTag(lease, unit, countLimit, null);
    }

    public List<TaskHandle> leaseTasksByTagBytes(long lease, TimeUnit unit, long countLimit, byte[] tag) {
        return leaseTasksByTag(lease, unit, countLimit, new String(tag));
    }

    public List<TaskHandle> leaseTasksByTag(long lease, TimeUnit unit, long countLimit, String tag) {
        return null;  // TODO
    }

    public void purge() {
        // TODO
    }

    public TaskHandle modifyTaskLease(TaskHandle taskHandle, long lease, TimeUnit unit) {
        return null;  // TODO
    }

    static void validateQueueName(String queueName) {
        if (queueName == null || queueName.length() == 0 || QueueConstants.QUEUE_NAME_PATTERN.matcher(queueName).matches() == false) {
            throw new IllegalArgumentException("Queue name does not match expression " + QueueConstants.QUEUE_NAME_REGEX + "; found '" + queueName + "'");
        }
    }

    static void validateTaskName(String taskName) {
        if (taskName == null || taskName.length() == 0 || QueueConstants.TASK_NAME_PATTERN.matcher(taskName).matches() == false) {
            throw new IllegalArgumentException("Task name does not match expression " + QueueConstants.TASK_NAME_REGEX + "; given taskname: '" + taskName + "'");
        }
    }
}
