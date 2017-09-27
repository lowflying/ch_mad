package com.cherena.myapp;

/**
 * Created by Cherena on 26/09/2017.
 */

public interface RequestListener<T> {

    public void getResult(T object);
}
