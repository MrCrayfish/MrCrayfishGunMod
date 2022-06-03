package com.tac.guns.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Used to exclude certain fields via GSON strategy, search up "GSON Exclude using Strategy stackoverflow" for source and discussion.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TGExclude {}
