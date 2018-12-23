package com.ai3cloud.ml.model;

import java.util.Objects;

/**
 * The parent of all instance classes.
 * 
 * @author Jiayun Han
 * @since 1.0.0.0
 */
public class InstanceParent {

	private String klass;

	/**
	 * To satisfy child classes.
	 */
	public InstanceParent() {		
	}
	
	public InstanceParent(String klass) {
		setKlass(klass);
	}

	/**
	 * Returns the class of this instance
	 * 
	 * @return The class of this instance if it is classified; {@code null}
	 *         otherwise
	 */
	public String getKlass() {
		return klass;
	}

	public void setKlass(String value) {
		this.klass = Objects.requireNonNull(value);
	}
}
