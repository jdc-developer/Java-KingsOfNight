package jdc.kings.state.objects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Action<T, K> {
	
	private Method action;
	private T declaringClass;
	private K actionParameter;
	
	public Method getAction() {
		return action;
	}
	
	public void setAction(Method action) {
		this.action = action;
	}
	
	public T getDeclaringClass() {
		return declaringClass;
	}

	public void setDeclaringClass(T declaringClass) {
		this.declaringClass = declaringClass;
	}

	public K getActionParameter() {
		return actionParameter;
	}
	
	public void setActionParameter(K actionParameter) {
		this.actionParameter = actionParameter;
	}
	
	public void callAction() {
		try {
			action.invoke(declaringClass, actionParameter);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Action() {
		super();
	}

	public Action(Method action, T declaringClass, K actionParameter) {
		super();
		this.action = action;
		this.declaringClass = declaringClass;
		this.actionParameter = actionParameter;
	}
	
	
}
