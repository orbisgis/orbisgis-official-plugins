/*
 * OrbisGIS is an open source GIS application dedicated to research for
 * all geographic information science.
 * 
 * OrbisGIS is distributed under GPL 3 license. It is developped by the "GIS"
 * team of the Lab-STICC laboratory <http://www.lab-sticc.fr/>.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
 * Copyright (C) 2015-2017 Lab-STICC CNRS, UMR 6285.
 * 
 * This file is part of OrbisGIS.
 * 
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 * 
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.rscriptengine;

import org.eclipse.aether.repository.RemoteRepository;
import org.renjin.aether.AetherPackageLoader;
import org.renjin.aether.ConsoleRepositoryListener;
import org.renjin.aether.ConsoleTransferListener;
import org.renjin.eval.Session;
import org.renjin.eval.SessionBuilder;
import org.renjin.script.RenjinScriptEngine;
import org.renjin.script.RenjinScriptEngineFactory;
import org.renjin.sexp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Class managing and configuring the R engine get from Renjin.
 *
 * @author Erwan Bocher
 */
public class REngineFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger("gui." + REngineFactory.class);

    /**
     * Returns the R ScriptEngine
     * @return The R ScriptEngine
     */
    public static RenjinScriptEngine createRScriptEngine() {
        List<RemoteRepository> repoList = AetherPackageLoader.defaultRepositories();
        repoList.add(new RemoteRepository.Builder("renjindbi", "default", "https://nexus.bedatadriven.com/content/repositories/renjin-dbi/").build());
        AetherPackageLoader aetherLoader = new AetherPackageLoader(AetherPackageLoader.class.getClassLoader(), repoList);
        aetherLoader.setTransferListener(new ConsoleTransferListener());
        aetherLoader.setRepositoryListener(new ConsoleRepositoryListener(System.out));

        SessionBuilder builder = new SessionBuilder();
        builder.withDefaultPackages();
        builder.setClassLoader(aetherLoader.getClassLoader());
        builder.setPackageLoader(aetherLoader);
        Session session = builder.build();

        RenjinScriptEngine engine = new RenjinScriptEngineFactory().getScriptEngine(session);
        engine.getContext().setWriter(new OutputStreamWriter(new LoggingOutputStream(LOGGER, false)));
        engine.getContext().setErrorWriter(new OutputStreamWriter(new LoggingOutputStream(LOGGER, true)));
        return engine;
    }

    /**
     * Generate the R object containing the connection to pass it to the RenjinScripEngine.
     * @return The R object containing the connection
     */
    public static ListVector getConnectionRObject(Connection connection){
        List<SEXP> sexpList = new ArrayList<>();
        sexpList.add(new ExternalPtr<>(connection));
        AttributeMap attributeMap = AttributeMap.builder()
                .setClass("JDBCConnection", "DBIConnection")
                .setNames(new StringArrayVector("conn"))
                .build();
        return new ListVector(sexpList, attributeMap);
    }
}
