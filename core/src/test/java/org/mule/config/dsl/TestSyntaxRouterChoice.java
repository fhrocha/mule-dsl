/*
 * ---------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.config.dsl;

import static org.mule.config.dsl.expression.CoreExpr.string;

public class TestSyntaxRouterChoice {

    public void choiceRouterTest() {
        Mule.newInstance(new AbstractModule() {
            @Override
            public void configure() {
                flow("MyFlow")
                        .from("file:///Users/porcelli/test")
                        .choice()
                            .when(string(""))
                                .echo()
                                .echo()
                                .echo()
                                .choice()
                                    .when(string(""))
                                        .echo()
                                    .otherwise()
                                        .echo()
                                .endChoice()
                                .echo()
                                .messageProperties()
                                    .put(null, null)
                                    .remove(null)
                                .echo()
                            .when(string(""))
                                .echo()
                                .echo()
                            .otherwise()
                                .echo()
                                .echo()
                        .endChoice();

                flow("MyFlow2")
                        .invoke((Class<?>) null)
                        .choice()
                            .when(string(""))
                                .echo()
                                .echo()
                                .echo()
                                .choice()
                                    .when(string(""))
                                        .echo()
                                    .otherwise()
                                        .echo()
                                        .messageProperties()
                                            .put(null, null)
                                            .remove(null)
                                        .endMessageProperties()
                                .endChoice()
                                .echo()
                                .echo()
                            .when(string(""))
                                .echo()
                                .echo()
                            .otherwise()
                                .echo()
                                .echo()
                        .endChoice();

                flow("MyFlow2")
                        .invoke((Class<?>) null)
                        .choice()
                            .when(string(""))
                                .echo()
                                .echo()
                                .broadcast()
                                    .echo()
                                    .echo()
                                .endBroadcast()
                                .echo()
                                .choice()
                                    .when(string(""))
                                        .echo()
                                    .otherwise()
                                        .echo()
                                .endChoice()
                                .echo()
                                .echo()
                            .otherwise()
                                .echo()
                                .echo()
                                .echo()
                        .endChoice()
                        .choice()
                            .when(null)
                                .echo()
                            .otherwise()
                                .echo()
                        .endChoice()
                        .choice()
                            .when(null)
                                .echo()
                        .endChoice();
            }
        });
    }

}
