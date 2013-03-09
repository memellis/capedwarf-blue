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

package org.jboss.capedwarf.bytecode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.AccessControlException;
import java.security.ProtectionDomain;

import com.google.apphosting.runtime.security.WhiteList;
import org.jboss.capedwarf.common.compatibility.Compatibility;

/**
 * Check white list
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
class WhiteListTransformer implements ClassFileTransformer {
    private volatile Boolean disabled;

    private boolean isDisabled(ClassLoader cl) {
        if (disabled == null) {
            synchronized (this) {
                if (disabled == null) {
                    Compatibility instance = Compatibility.getInstance(cl);
                    disabled = instance.isEnabled(Compatibility.Feature.DISABLE_WHITE_LIST);
                }
            }
        }
        return disabled;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (isDisabled(loader) == false && WhiteList.getWhiteList().contains(className) == false) {
            throw new AccessControlException(className + " not supported in WhiteList!");
        }
        return classfileBuffer;
    }
}