package com.glassbox.uml.trace;
public class TraceConfig {
public static boolean TRACING_ENABLED = false;
public static boolean AI_SUMMARY_MODE = true;
private static final ThreadLocal<Integer> callDepth = ThreadLocal.withInitial(() -> 0);
public static int getAndIncrementDepth() {
int depth = callDepth.get();
callDepth.set(depth + 1);
return depth;
}
public static void decrementDepth() {

callDepth.set(Math.max(0, callDepth.get() - 1));
}
}