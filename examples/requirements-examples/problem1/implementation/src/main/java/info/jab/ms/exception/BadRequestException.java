package info.jab.ms.exception;

public final class BadRequestException extends RuntimeException {

	public BadRequestException(String message) {
		super(message);
	}
}
