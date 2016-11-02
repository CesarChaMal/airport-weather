package com.crossover.trial.util;

/**
 * @author Cesar Chavez
 *
 */
public class CloseableContext extends ClassLoader implements AutoCloseable {
	private Thread _currentThread;
	private ClassLoader _ocl;

	private CloseableContext(Thread currentThread, ClassLoader ocl) {
		this._currentThread = currentThread;
		this._ocl = ocl;
	}

	@Override
	public void close() {
		this._currentThread.setContextClassLoader(this._ocl);
	}

	public static CloseableContext contextClassLoader(Class loader) {
	    return contextClassLoader(loader.getClassLoader());
	}

	public static CloseableContext contextClassLoader(ClassLoader loader) {
	    final Thread currentThread = Thread.currentThread();
	    final ClassLoader ocl = currentThread.getContextClassLoader();
	    currentThread.setContextClassLoader(loader);
	    return new CloseableContext(currentThread, ocl);
	}
}