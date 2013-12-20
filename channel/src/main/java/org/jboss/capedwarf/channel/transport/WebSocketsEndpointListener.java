/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.jboss.capedwarf.channel.transport;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;

import org.jboss.capedwarf.channel.manager.ChannelEndpoint;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class WebSocketsEndpointListener implements ServletContextListener {
    private static ServerContainer getServerContainer(ServletContextEvent sce) {
        return getServerContainer(sce.getServletContext());
    }

    static ServerContainer getServerContainer(ServletContext context) {
        ServerContainer container = (ServerContainer) context.getAttribute(ServerContainer.class.getName());
        if (container == null) {
            throw new IllegalArgumentException(String.format("No %s present!", ServerContainer.class.getName()));
        }
        return container;
    }

    public void contextInitialized(ServletContextEvent sce) {
        ServerContainer container = getServerContainer(sce);
        try {
            ServerEndpointConfig.Builder builder = ServerEndpointConfig.Builder.create(ChannelEndpoint.class, "/_ah/channel_ws");
            ServerEndpointConfig conifg = builder.build();
            container.addEndpoint(conifg);
        } catch (DeploymentException e) {
            throw new IllegalStateException(e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}