/*
 * ---------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.config.dsl;

import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.junit.Test;
import org.mule.api.lifecycle.Callable;

import java.lang.annotation.*;

import static org.mule.config.dsl.expression.CoreExpr.payload;

public class TestInvokeExpectedErrors {

    @Test(expected = RuntimeException.class)
    public void nullCallable() {
        Mule.newInstance(new AbstractModule() {
            @Override
            public void configure() {
                flow("MyFlow")
                        .from("file:///Users/porcelli/test")
                        .invoke((Callable) null);
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void nullObject() {
        Mule.newInstance(new AbstractModule() {
            @Override
            public void configure() {
                flow("MyFlow")
                        .from("file:///Users/porcelli/test")
                        .invoke((Object) null);
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void nullType() {
        Mule.newInstance(new AbstractModule() {
            @Override
            public void configure() {
                flow("MyFlow")
                        .from("file:///Users/porcelli/test")
                        .invoke((Class<?>) null);
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void methodArgNull() {
        Mule.newInstance(new AbstractModule() {
            @Override
            public void configure() {
                flow("MyFlow")
                        .from("file:///Users/porcelli/test")
                        .invoke(Complex.class).methodAnnotatedWith((Class<? extends Annotation>) null);
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void methodArgNull2() {
        Mule.newInstance(new AbstractModule() {
            @Override
            public void configure() {
                flow("MyFlow")
                        .from("file:///Users/porcelli/test")
                        .invoke(Complex.class).methodAnnotatedWith((Annotation) null);
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void methodArgsExpected() {
        Mule.newInstance(new AbstractModule() {
            @Override
            public void configure() {
                flow("MyFlow")
                        .from("file:///Users/porcelli/test")
                        .invoke(Complex.class).methodAnnotatedWith(Names.named("ToInvoke")).withoutArgs();
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void methodArgsExpectedTyped() {
        Mule.newInstance(new AbstractModule() {
            @Override
            public void configure() {
                flow("MyFlow")
                        .from("file:///Users/porcelli/test")
                        .invoke(Complex2.class).methodAnnotatedWith(ToInvoke.class).withoutArgs();
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void cantFindAnnotationTyped() {
        Mule.newInstance(new AbstractModule() {
            @Override
            public void configure() {
                flow("MyFlow")
                        .from("file:///Users/porcelli/test")
                        .invoke(Complex2.class).methodAnnotatedWith(ToInvoke2.class).withoutArgs();
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void cantFindAnnotation() {
        Mule.newInstance(new AbstractModule() {
            @Override
            public void configure() {
                flow("MyFlow")
                        .from("file:///Users/porcelli/test")
                        .invoke(Complex2.class).methodAnnotatedWith(Names.named("XXX")).withoutArgs();
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void methodAndArgsDontMatch() {
        Mule.newInstance(new AbstractModule() {
            @Override
            public void configure() {
                flow("MyFlow")
                        .from("file:///Users/porcelli/test")
                        .invoke(Complex2.class).methodAnnotatedWith(ToInvoke2.class).args(payload());
            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void methodAndArgsDontMatch2() {
        Mule.newInstance(new AbstractModule() {
            @Override
            public void configure() {
                flow("MyFlow")
                        .from("file:///Users/porcelli/test")
                        .invoke(Complex.class).methodAnnotatedWith(Names.named("ToInvoke2")).args(payload());
            }
        });
    }


    public static class Complex {
        @Named("ToInvoke")
        void execute(final String string) {

        }

        @Named("ToInvoke2")
        void execute2() {

        }

    }

    public static class Complex2 {
        @ToInvoke
        void execute(final String string) {

        }

        @ToInvoke2
        void execute2() {

        }

    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ToInvoke2 {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ToInvoke {
    }

}
