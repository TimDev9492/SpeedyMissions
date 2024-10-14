package me.timwastaken.speedyMissions.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class MappingIterator<I, O> implements Iterator<O> {
    private final Iterator<I> iterator;
    private final Function<I, O> transform;

    public MappingIterator(Iterator<I> iterator, Function<I, O> transform) {
        this.iterator = iterator;
        this.transform = transform;
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public O next() {
        if (!this.iterator.hasNext()) throw new NoSuchElementException();
        return this.transform.apply(this.iterator.next());
    }
}
