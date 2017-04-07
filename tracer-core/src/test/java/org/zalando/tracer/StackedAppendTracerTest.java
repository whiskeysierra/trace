package org.zalando.tracer;

import org.junit.Test;
import org.mockito.InOrder;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public final class StackedAppendTracerTest extends AbstractTracerTest {

    private final TraceListener listener = mock(TraceListener.class);
    private final StackedTraceListener stackedListener = mock(StackedTraceListener.class);

    private final Tracer tracer = Tracer.builder()
            .stacked()
            .append()
            .traces(asList("X-Trace-ID", "X-Request-ID"))
            .trace("X-Foo-ID", Arrays.asList("foo", "bar").iterator()::next)
            .listener(listener)
            .listener(stackedListener)
            .build();

    @Override
    protected Tracer unit() {
        return tracer;
    }

    @Test
    public void shouldNotFailToStartWithoutProvidedValuesIfAlreadyStarted() {
        tracer.start();
        tracer.start();
    }

    @Test
    public void shouldNotFailToStartWithProvidedValuesIfAlreadyStarted() {
        tracer.start(trace -> "foo");
        tracer.start(trace -> "bar");
    }

    @Test
    public void shouldKeepPreviousTraceWhenStacking() {
        final Trace trace = tracer.get("X-Trace-ID");

        tracer.start();
        final String previous = trace.getValue();
        tracer.start();
        assertThat(trace.getValue(), is(not(previous)));
        tracer.stop();
        assertThat(trace.getValue(), is(previous));
    }

    @Test
    public void shouldTriggerListenersCorrectlyWhenStacking() {
        final InOrder inOrder = inOrder(listener);

        tracer.start();
        inOrder.verify(listener).onStart("X-Foo-ID", "foo");
        tracer.start();
        inOrder.verify(listener).onStart("X-Foo-ID", "bar");
        tracer.stop();
        inOrder.verify(listener).onStop("X-Foo-ID", "bar");
        tracer.stop();
        inOrder.verify(listener).onStop("X-Foo-ID", "foo");
    }

    @Test
    public void shouldTriggerStackedListenersCorrectlyWhenStacking() {
        final InOrder inOrder = inOrder(stackedListener);

        tracer.start();
        inOrder.verify(stackedListener).onStart("X-Foo-ID", "foo");
        tracer.start();
        inOrder.verify(stackedListener).onStart("X-Foo-ID", "bar");
        tracer.stop();
        inOrder.verify(stackedListener).onStop("X-Foo-ID", "bar");
        tracer.stop();
        inOrder.verify(stackedListener).onStop("X-Foo-ID", "foo");
    }

}