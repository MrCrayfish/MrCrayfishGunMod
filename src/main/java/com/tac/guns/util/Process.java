package com.tac.guns.util;

@FunctionalInterface
public interface Process<T> {
	T process(T t);
}