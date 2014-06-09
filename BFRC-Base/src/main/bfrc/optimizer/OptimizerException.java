package bfrc.optimizer;

@SuppressWarnings("serial")
public class OptimizerException extends RuntimeException {

	public OptimizerException() {
		super();
	}

	public OptimizerException(String message) {
		super(message);
	}

	public OptimizerException(Throwable cause) {
		super(cause);
	}

	public OptimizerException(String message, Throwable cause) {
		super(message, cause);
	}
}
