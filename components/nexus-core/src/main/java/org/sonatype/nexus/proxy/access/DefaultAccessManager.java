/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.proxy.access;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.proxy.AccessDeniedException;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.repository.Repository;

/**
 * A default access manager relying onto default NexusAuthorizer.
 *
 * @author cstamas
 */
@Named
@Singleton
public class DefaultAccessManager
    implements AccessManager
{
  private final NexusItemAuthorizer nexusItemAuthorizer;

  @Inject
  public DefaultAccessManager(final NexusItemAuthorizer nexusItemAuthorizer) {
    this.nexusItemAuthorizer = nexusItemAuthorizer;
  }

  public void decide(Repository repository, ResourceStoreRequest request, Action action)
      throws AccessDeniedException
  {
    //only bother checking item authorizer if there is no flag in request stating authorization
    //has been taken care of
    if (!request.getRequestContext().containsKey(AccessManager.REQUEST_AUTHORIZED)
        && !nexusItemAuthorizer.authorizePath(repository, request, action)) {
      // deny the access
      throw new AccessDeniedException("Access denied on repository ID='" + repository.getId() + "', path='"
          + request.getRequestPath() + "', action='" + action + "'!");
    }
  }
}
