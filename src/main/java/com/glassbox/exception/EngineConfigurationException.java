package com.glassbox.exception;
/**
* Exception thrown when the core engine fails to initialize or is misconfigured.
*
* <p><b>Inheritance:</b> Inherits from {@link GlassBoxException} to participate
* in the engine's custom exception hierarchy.</p>
*/
public class EngineConfigurationException extends GlassBoxException {

public EngineConfigurationException(String message) {
super(message);
}
public EngineConfigurationException(String message, Throwable cause) {
super(message, cause);
}
}