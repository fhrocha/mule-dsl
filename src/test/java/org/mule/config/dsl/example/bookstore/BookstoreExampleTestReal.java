/*
 * $Id: 20811 2011-03-30 16:05:20Z porcelli $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.config.dsl.example.bookstore;

import org.mule.config.domain.Transformer;
import org.mule.config.dsl.*;

public class BookstoreExampleTestReal {
    public static void main(String... args) {
        Application bookstoreApp = Mule.createApplication(new BookStore());
        bookstoreApp.start();
        Flow dwFlow = bookstoreApp.getFlow("DataWarehouse");
        dwFlow.stop();
    }

    public static class BookStore extends AbstractModule {
        @Override
        public void configure() {

            defineTransformer("HtmlContentTypeTransformer")
                    .extend(MessageProcessorTransformer.class)
                        .addProperty("Content-Type", "text/html")
                        .removeProperty("content-type");

            SMTPConnector mySMTPConn = newConnector()
                    .extend(SMTPConnector.class)
                    .user("${user}")
                    .password("${password}")
                    .host("${host}");

            newFlow("CatalogService")
                    .from(listen(Protocol.HTTP, "0.0.0.0")
                                .onPort(8777)
                                .onPath("/services/order")
                             .using(CXF.class)
                                .withClass(OrderService.class),
                          listen("servlet://catalog"))
                    .execute(CatalogServiceImpl.class).asSingleton();

            newFlow("OrderService")
                    .listen(Protocol.HTTP, "0.0.0.0")
                        .onPort(8777)
                        .onPath("/services/order")
                        .using(CXF.class)
                            .withClass(OrderService.class)
                    .execute(OrderServiceImpl.class).asSingleton()
                    .send(Protocol.VM, "emailNotification")
                    .send(Protocol.VM, "dataWarehouse");

            newFlow("DataWarehouse")
                    .listen(Protocol.VM, "dataWarehouse")
                    .execute(DataWarehouse.class)
                    .transformWith(ref(Transformer.class, "HtmlContentTypeTransformer"))
                    .send("stats");

            newFlow("EmailNotificationService")
                    .listen(Protocol.VM, "emailNotification")
                    .transformWith(OrderToEmailTransformer.class)
                    .transformWith(StringToEmailTransformer.class)
                    .send(mySMTPConn)
                        .from("${from}")
                        .subject("Your order has been placed!");


        }

        private FlowInboundListenBuilder listen(String s) {
            return null;  //To change body of created methods use File | Settings | File Templates.
        }

        private FlowInboundListenBuilder listen(Protocol http, String s) {
            return null;  //To change body of created methods use File | Settings | File Templates.
        }
    }
}