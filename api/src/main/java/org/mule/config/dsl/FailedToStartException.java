/*
 * ---------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.config.dsl;

import org.mule.config.i18n.Message;

/**
 * Thrown when can't start the mule context.
 *
 * @author porcelli
 */
public class FailedToStartException extends DSLException {
    /**
     * * @param message the exception message
     */
    public FailedToStartException(final Message message) {
        super(message);
    }

    /**
     * @param message the exception message
     */
    public FailedToStartException(final String message) {
        super(message);
    }

    /**
     * @param message the exception message
     * @param cause   the exception that cause this exception to be thrown
     */
    public FailedToStartException(final Message message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message the exception message
     * @param cause   the exception that cause this exception to be thrown
     */
    public FailedToStartException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
