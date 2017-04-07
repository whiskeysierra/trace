package org.zalando.tracer.spring;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.zalando.tracer.LoggingTraceListener;
import org.zalando.tracer.MDCTraceListener;
import org.zalando.tracer.StackedMDCTraceListener;
import org.zalando.tracer.TraceListener;
import org.zalando.tracer.Tracer;
import org.zalando.tracer.TracerFactory;
import org.zalando.tracer.aspectj.TracedAspect;
import org.zalando.tracer.httpclient.TracerHttpRequestInterceptor;
import org.zalando.tracer.servlet.TracerFilter;

import javax.servlet.Filter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static javax.servlet.DispatcherType.ASYNC;
import static javax.servlet.DispatcherType.REQUEST;

@Configuration
@ConditionalOnClass(Tracer.class)
@EnableConfigurationProperties(TracerProperties.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@Import({DefaultGeneratorResolver.class, TracerAutoConfiguration.AspectConfiguration.class})
public class TracerAutoConfiguration {

    public static final String FILTER_NAME = "tracerFilter";

    // IDEA doesn't support @EnableConfigurationProperties
    private final TracerProperties properties;
    private final GeneratorResolver resolver;

    @Autowired
    public TracerAutoConfiguration(final TracerProperties properties, final GeneratorResolver resolver) {
        this.properties = properties;
        this.resolver = resolver;
    }

    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnProperty(name = "tracer.filter.enabled", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(name = FILTER_NAME)
    public FilterRegistrationBean tracerFilter(final Tracer tracer) {
        final Filter filter = new TracerFilter(tracer);
        final FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setName(FILTER_NAME);
        registration.setDispatcherTypes(REQUEST, ASYNC);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean(TracerHttpRequestInterceptor.class)
    public TracerHttpRequestInterceptor tracerHttpRequestInterceptor(final Tracer tracer) {
        return new TracerHttpRequestInterceptor(tracer);
    }

    @ConditionalOnClass(Aspect.class)
    @ConditionalOnProperty(name = "tracer.aspect.enabled", havingValue = "true", matchIfMissing = true)
    @EnableAspectJAutoProxy
    public static class AspectConfiguration {

        @Bean
        public TracedAspect tracedAspect(final Tracer tracer) {
            final TracedAspect aspect = new TracedAspect();
            aspect.setTracer(tracer);
            return aspect;
        }

    }

    @Bean
    public Tracer tracer(
            @SuppressWarnings({"SpringJavaAutowiringInspection", "OptionalUsedAsFieldOrParameterType"})
            final Optional<List<TraceListener>> listeners) {
        final TracerFactory.Builder builder = Tracer.builder()
                .stacked(properties.isStacked())
                .append(properties.isAppend())
                .listeners(listeners.orElse(emptyList()));

        final Map<String, String> traces = properties.getTraces();

        traces.forEach((name, type) ->
                builder.trace(name, resolver.resolve(type)));

        return builder.build();
    }

    @Bean
    @ConditionalOnProperty(name = "tracer.mdc.enabled", havingValue = "true", matchIfMissing = true)
    public TraceListener mdcTraceListener() {
        return properties.isStacked() ?
                new StackedMDCTraceListener() :
                new MDCTraceListener();
    }

    @Bean
    @ConditionalOnProperty(name = "tracer.logging.enabled", havingValue = "true")
    public LoggingTraceListener loggingTraceListener() {
        final String category = properties.getLogging().getCategory();
        return category == null ?
                new LoggingTraceListener() :
                new LoggingTraceListener(LoggerFactory.getLogger(category));
    }

}
