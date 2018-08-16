package com.nowcoder.async;

import java.util.List;

public interface EventHandler {

    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}
