# OpenTracing Toolbox

> ⚠
>  
> This project is in maintenance mode - because OpenTracing has been [archived](https://www.cncf.io/blog/2022/01/31/cncf-archives-the-opentracing-project/).
> 
> Please [migrate your OpenTracing applications to OpenTelemetry](https://opentelemetry.io/docs/migration/opentracing/).
> 
> We will continue to support this project with bugfixes until most applications in Zalando have been migrated to OpenTelemetry.

[![Stability: Active](https://masterminds.github.io/stability/active.svg)](https://masterminds.github.io/stability/active.html)
![Build Status](https://github.com/zalando/opentracing-toolbox/workflows/build/badge.svg)
[![Coverage Status](https://img.shields.io/coveralls/zalando/opentracing-toolbox/main.svg)](https://coveralls.io/r/zalando/opentracing-toolbox)
[![Code Quality](https://img.shields.io/codacy/grade/69e173024eec403797466e147a2051a3/main.svg)](https://www.codacy.com/app/whiskeysierra/opentracing-toolbox)
[![Release](https://img.shields.io/github/release/zalando/opentracing-toolbox.svg)](https://github.com/zalando/opentracing-toolbox/releases)
[![Maven Central](https://img.shields.io/maven-central/v/org.zalando/opentracing-toolbox.svg)](https://maven-badges.herokuapp.com/maven-central/org.zalando/opentracing-toolbox)
[![OpenTracing](https://img.shields.io/badge/OpenTracing-enabled-blue.svg)](http://opentracing.io)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/zalando/opentracing-toolbox/main/LICENSE)

*OpenTracing Toolbox* is a collection of libraries that build on top of [OpenTracing](https://opentracing.io/) and provide extensions and plugins to existing instrumentations.

- **Status**: Under development and used in production

## Features

- Legacy [FlowID](#flowid) compatibility support 
- JDBC support
- Extensible [OpenTracing Proxy](#proxy) (wrapper) w/ support for listeners and interceptors
- Servlet support
- Spring Web and Webflux support
- Sensible defaults

## Modules

### FlowID

The `opentracing-flowid` module replaces the former [`zalando/tracer`](https://github.com/zalando/tracer) library by providing support for the `X-Flow-ID` header propagation on top of OpenTracing.

Go checkout out its [README](opentracing-flowid) for details. 

### JDBC

The `opentracing-jdbc` module is a direct **competitor** to [opentracing-contrib/java-jdbc](https://github.com/opentracing-contrib/java-jdbc). 

Go checkout out its [README](opentracing-jdbc) for details. 

### Proxy

The `opentracing-proxy` module is a direct **competitor** to [opentracing-contrib/java-api-extensions](https://github.com/opentracing-contrib/java-api-extensions). 

Go checkout out its [README](opentracing-proxy) for details. 

### Servlet Extension

The `opentracing-servlet-extension` module is an **extension** to `io.opentracing.contrib:opentracing-web-servlet-filter` and only useful if used in conjunction.

Go checkout out its [README](opentracing-servlet-extension) for details. 

### Spring Web Extension

The `opentracing-spring-web-extension` module is an **extension** to `io.opentracing.contrib:opentracing-spring-web` and only useful if used in conjunction.

Go checkout out its [README](opentracing-spring-extension/opentracing-spring-web-extension) for details. 

### Spring Webflux Extension

The `opentracing-spring-webflux-extension` module is an **extension** to `io.opentracing.contrib:opentracing-spring-web` and only useful if used in conjunction.

Go checkout out its [README](opentracing-spring-extension/opentracing-spring-webflux-extension) for details. 

## Getting Help

If you have questions, concerns, bug reports, etc., please file an issue in this repository's [Issue Tracker](../../issues).

## Getting Involved/Contributing

To contribute, simply make a pull request and add a brief description (1-2 sentences) of your addition or change. For
more details, check the [contribution guidelines](.github/CONTRIBUTING.md).

## Alternatives

Tracer, by design, does not provide sampling, metrics or annotations. Neither does it use the semantics of spans as
most of the following projects do. If you require any of these, you're highly encouraged to try them.

- [The OpenTracing Project](http://opentracing.io/)
- [Apache HTrace](http://htrace.incubator.apache.org/)
- [Spring Cloud Sleuth](http://cloud.spring.io/spring-cloud-sleuth/)
- [Zipkin](http://zipkin.io/)
