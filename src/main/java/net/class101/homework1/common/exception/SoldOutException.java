package net.class101.homework1.common.exception;

@SuppressWarnings("serial")
public class SoldOutException extends IllegalStateException{

    public SoldOutException(String string) {
        super(string);
    }
}
