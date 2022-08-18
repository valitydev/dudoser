package dev.vality.dudoser.service;

import dev.vality.dudoser.handler.Handler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HandlerManager {

    private final List<Handler> handlers;

    public <C> Optional<Handler> getHandler(C change) {
        return handlers.stream()
                .filter(handler -> handler.accept(change))
                .findFirst();
    }
}
