package com.alex.web.data.storage.mapper;

/**
 * This interface defines one method for mapping from source type to some target type.It helps us to change types
 * between layers.
 * @param <F> source type
 * @param <T> target type
 */

@FunctionalInterface
public interface Mapper <F,T>{
    T map(F object);
}