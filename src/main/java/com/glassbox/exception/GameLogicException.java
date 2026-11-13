package com.glassbox.exception;
/**
* Exception thrown when an invalid game rule is evaluated or attempted.
*
* <p><b>Inheritance:</b> Inherits from {@link GlassBoxException}.</p>
*/
public class GameLogicException extends GlassBoxException {
public GameLogicException(String message) {
super(message);
}
public GameLogicException(String message, Throwable cause) {
super(message, cause);
}
}