package com.mercateo.reflection;

public interface InvocationRecorder<T> {
    public Call<T> getInvocationRecordingResult();
}
