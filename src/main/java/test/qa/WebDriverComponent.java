package test.qa;

public abstract class WebDriverComponent<T> extends WebDriverUtilities {
	
	public int testXOffset = 0;
	
	WebDriverComponent() {
		super();
		log.debug( "Calling constructor for WebDriverComponent ..." );
	}

	@SuppressWarnings("unchecked")
	public T get() {
		try {
			isLoaded();
			return (T) this;
		} catch (Error e) {
			load();
		}

		isLoaded();

		return (T) this;
	}

	protected abstract void load();

	protected abstract void isLoaded() throws Error;
	
}
