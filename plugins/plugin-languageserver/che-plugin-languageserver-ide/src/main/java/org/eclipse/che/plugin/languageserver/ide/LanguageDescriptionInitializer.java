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
package org.eclipse.che.plugin.languageserver.ide;

import com.google.gwt.core.client.JsArrayString;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Set;
import org.eclipse.che.api.languageserver.shared.model.LanguageDescription;
import org.eclipse.che.ide.editor.orion.client.OrionContentTypeRegistrant;
import org.eclipse.che.ide.editor.orion.client.OrionHoverRegistrant;
import org.eclipse.che.ide.editor.orion.client.OrionOccurrencesRegistrant;
import org.eclipse.che.ide.editor.orion.client.jso.OrionContentTypeOverlay;
import org.eclipse.che.ide.editor.orion.client.jso.OrionHighlightingConfigurationOverlay;
import org.eclipse.che.plugin.languageserver.ide.highlighting.OccurrencesProvider;
import org.eclipse.che.plugin.languageserver.ide.hover.HoverProvider;

/** @author Evgen Vidolob */
@Singleton
public class LanguageDescriptionInitializer {

  private final OrionContentTypeRegistrant contentTypeRegistrant;
  private final OrionHoverRegistrant orionHoverRegistrant;
  private final OrionOccurrencesRegistrant orionOccurrencesRegistrant;
  private final OccurrencesProvider occurrencesProvider;
  private final HoverProvider hoverProvider;

  @Inject
  public LanguageDescriptionInitializer(
      OrionContentTypeRegistrant contentTypeRegistrant,
      OrionHoverRegistrant orionHoverRegistrant,
      OrionOccurrencesRegistrant orionOccurrencesRegistrant,
      HoverProvider hoverProvider,
      OccurrencesProvider occurrencesProvider) {
    this.contentTypeRegistrant = contentTypeRegistrant;
    this.orionHoverRegistrant = orionHoverRegistrant;
    this.orionOccurrencesRegistrant = orionOccurrencesRegistrant;
    this.hoverProvider = hoverProvider;
    this.occurrencesProvider = occurrencesProvider;
  }

  @Inject
  void initialize(Set<LanguageDescription> languageDescriptions) {

    JsArrayString contentTypes = JsArrayString.createArray().cast();

    for (LanguageDescription languageDescription : languageDescriptions) {

      String mimeType = languageDescription.getMimeType();
      contentTypes.push(mimeType);
      OrionContentTypeOverlay contentType = OrionContentTypeOverlay.create();
      contentType.setId(mimeType);
      contentType.setName(languageDescription.getLanguageId());
      contentType.setFileName(
          languageDescription
              .getFileNames()
              .toArray(new String[languageDescription.getFileNames().size()]));
      contentType.setExtension(
          languageDescription
              .getFileExtensions()
              .toArray(new String[languageDescription.getFileExtensions().size()]));
      contentType.setExtends("text/plain");

      // highlighting
      OrionHighlightingConfigurationOverlay config = OrionHighlightingConfigurationOverlay.create();
      config.setId(languageDescription.getLanguageId() + ".highlighting");
      config.setContentTypes(mimeType);
      config.setPatterns(languageDescription.getHighlightingConfiguration());
      contentTypeRegistrant.registerFileType(contentType, config);
    }
    orionHoverRegistrant.registerHover(contentTypes, hoverProvider);
    orionOccurrencesRegistrant.registerOccurrencesHandler(contentTypes, occurrencesProvider);
  }
}
