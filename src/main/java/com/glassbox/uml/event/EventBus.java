package com.glassbox.uml.event;
import java.util.*; import java.util.concurrent.ConcurrentHashMap; import java.util.concurrent.CopyOnWriteArrayList; import java.util.function.Consumer; import java.util.logging.*;
/** Observer bus. Subscriber failures are isolated from gameplay. */
public final class EventBus {
 private static final EventBus INSTANCE=new EventBus(); private static final Logger LOG=Logger.getLogger(EventBus.class.getName());
 private final Map<Class<?>,CopyOnWriteArrayList<Consumer<?>>> listeners=new ConcurrentHashMap<>(); private EventBus(){}
 public static EventBus getInstance(){return INSTANCE;}
 public <T extends GlassBoxEvent> void subscribe(Class<T> type,Consumer<T> listener){listeners.computeIfAbsent(type,k->new CopyOnWriteArrayList<>()).add(listener);}
 public <T extends GlassBoxEvent> void unsubscribe(Class<T> type,Consumer<T> listener){List<Consumer<?>> l=listeners.get(type);if(l!=null)l.remove(listener);}
 @SuppressWarnings("unchecked") public void publish(GlassBoxEvent event){for(var entry:listeners.entrySet())if(entry.getKey().isAssignableFrom(event.getClass()))for(Consumer<?> raw:entry.getValue())try{((Consumer<GlassBoxEvent>)raw).accept(event);}catch(Throwable ex){LOG.log(Level.WARNING,"Observer failed; gameplay continues",ex);}}
 public void clearAllSubscribers(){listeners.clear();}
}
