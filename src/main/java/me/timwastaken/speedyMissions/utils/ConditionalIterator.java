package me.timwastaken.speedyMissions.utils;

import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConditionalIterator<T, O> implements Iterator<O> {
    private final Iterator<T> iterator;
    private final Predicate<T> condition;
    private final Function<T, O> transform;

    private T peeked = null;

    public ConditionalIterator(Iterator<T> iterator, Predicate<T> condition, Function<T, O> transform) {
        this.iterator = iterator;
        this.condition = condition;
        this.transform = transform;
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
    public O next() {
        if (this.peeked == null) throw new NoSuchElementException();
        return this.transform.apply(this.peeked);
    }
}
