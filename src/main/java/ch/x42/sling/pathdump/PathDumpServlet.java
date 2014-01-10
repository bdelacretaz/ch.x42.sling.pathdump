/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ch.x42.sling.pathdump;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

/** Dump all paths from the current resource down, in text format */
@SuppressWarnings("serial")
@Component
@Service(Servlet.class)
@Properties({ 
    @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
    @Property(name = "sling.servlet.selectors", value = "pathdump"),
    @Property(name = "sling.servlet.extensions", value = "txt")
})
public class PathDumpServlet extends SlingSafeMethodsServlet {
    
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        try {
            dumpNode(response.getWriter(), request.getResource().adaptTo(Node.class));
        } catch(RepositoryException rex) {
            throw new ServletException("dumpNode failed", rex);
        }
        
        response.getWriter().flush();
    }
    
    private void dumpNode(PrintWriter pw, Node n) throws RepositoryException {
        pw.println(n.getPath());
        final NodeIterator it = n.getNodes();
        while(it.hasNext()) {
            dumpNode(pw, it.nextNode());
        }
    }
}

