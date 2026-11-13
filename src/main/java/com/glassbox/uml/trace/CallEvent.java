package com.glassbox.uml.trace;
import com.glassbox.uml.event.GlassBoxEvent;
public class CallEvent implements GlassBoxEvent {
public final String callerClass;
public final String calleeClass;
public final String methodName;
public final String argsSummary;
public final int depth;
private final long timestamp = System.currentTimeMillis();
public CallEvent(String callerClass, String calleeClass, String methodName, String argsSummary, int depth) {
this.callerClass = callerClass;
this.calleeClass = calleeClass;
this.methodName = methodName;
this.argsSummary = argsSummary;
this.depth = depth;
}
@Override
public long getTimestamp() { return timestamp; }
@Override
public String toString() {
String indent = " ".repeat(depth);
return indent + "-> " + calleeClass + "." + methodName + "(" + argsSummary + ") [from " + callerClass + "]";
}
}