package me.timwastaken.speedyMissions.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class ConditionalIterator<T> implements Iterator<T> {
    private final Iterator<T> iterator;
    private final Predicate<T> condition;

    private T peeked = null;

    public ConditionalIterator(Iterator<T> iterator, Predicate<T> condition) {
        this.iterator = iterator;
        this.condition = condition;
    }

    @Override
    public boolean hasNext() {
        do {
            if (!this.iterator.hasNext()) return false;
            this.peeked = iterator.next();
        } while (!this.condition.test(peeked));
        return true;
    }

    @Override
    public T next() {
        if (this.peeked == null) throw new NoSuchElementException();
        return this.peeked;
    }
}
