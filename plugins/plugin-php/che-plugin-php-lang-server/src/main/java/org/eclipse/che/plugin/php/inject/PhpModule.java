/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.plugin.php.inject;

import static com.google.inject.multibindings.MapBinder.newMapBinder;
import static com.google.inject.multibindings.Multibinder.newSetBinder;

import com.google.inject.AbstractModule;
import org.eclipse.che.api.languageserver.LanguageServerConfig;
import org.eclipse.che.api.project.server.handlers.ProjectHandler;
import org.eclipse.che.api.project.server.type.ProjectTypeDef;
import org.eclipse.che.inject.DynaModule;
import org.eclipse.che.plugin.php.languageserver.PhpLanguageServerConfig;
import org.eclipse.che.plugin.php.projecttype.PhpProjectGenerator;
import org.eclipse.che.plugin.php.projecttype.PhpProjectType;

/** @author Kaloyan Raev */
@DynaModule
public class PhpModule extends AbstractModule {
  public static final String LANGUAGE_ID = "php";

  @Override
  protected void configure() {
    newSetBinder(binder(), ProjectTypeDef.class).addBinding().to(PhpProjectType.class);

    newSetBinder(binder(), ProjectHandler.class).addBinding().to(PhpProjectGenerator.class);

    newMapBinder(binder(), String.class, LanguageServerConfig.class)
        .addBinding("org.eclipse.che.plugin.php.languageserver")
        .to(PhpLanguageServerConfig.class)
        .asEagerSingleton();
  }
}
