package com.glassbox.exception;
/**
* Base checked exception for the GlassBox Engine.
*
* <p><b>SOLID Principle Applied:</b> Open/Closed Principle (OCP).
* This base exception allows the engine to catch all custom exceptions generically
* (closed for modification) while allowing specific modules to introduce new
* exception types by extending this class (open for extension).</p>
*
* <p><b>Abstraction:</b> Provides a common contract for all engine-specific errors.</p>
*/
public abstract class GlassBoxException extends Exception {
public GlassBoxException(String message) {
super(message);
}
public GlassBoxException(String message, Throwable cause) {
super(message, cause);
}
}