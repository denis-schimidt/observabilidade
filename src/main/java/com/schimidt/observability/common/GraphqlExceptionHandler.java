package com.schimidt.observability.common;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
class GraphqlExceptionHandler implements DataFetcherExceptionResolver {

    @Override
    public Mono<List<GraphQLError>> resolveException(Throwable exception, DataFetchingEnvironment env) {

        log.info("Handling exception: {}", exception.getMessage());

        if (exception instanceof ConstraintViolationException) {
            final List<String> errorsMessage = ((ConstraintViolationException) exception).getConstraintViolations().stream()
                    .map(v-> "%s -> %s".formatted(v.getPropertyPath().toString().replaceFirst("\\w*\\.", ""), v.getMessage()))
                    .toList();

            return Mono.fromCallable(() -> errorsMessage.stream()
                    .map(e-> {
                        GraphQLError graphQLError = GraphqlErrorBuilder.newError(env)
                                .errorType(ErrorType.ValidationError)
                                .message(e)
                                .build();
                        graphQLError.getLocations().clear();

                        return graphQLError;
                    }).toList());
        }

        return Mono.empty();
    }
}
