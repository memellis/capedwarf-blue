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

package org.jboss.capedwarf.datastore.query;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.google.appengine.api.datastore.Entity;

/**
 * Total stats update
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractUpdate implements Update {
    protected final Entity trigger;

    protected AbstractUpdate(Entity trigger) {
        this.trigger = trigger;
    }

    public Entity update(Entity entity) {
        Entity updated = new Entity(entity.getKind());
        doUpdate(entity, updated);
        return updated;
    }

    protected abstract void doUpdate(Entity current, Entity newEntity);

    // TODO -- better impl
    protected static long countBytes(Entity entity) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(entity);
            out.flush();
            return baos.size();
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot count entity: " + entity);
        }
    }

    protected static Long toLong(Entity entity, String property) {
        Object value = entity.getProperty(property);
        return (value != null) ? Number.class.cast(value).longValue() : null;
    }
}