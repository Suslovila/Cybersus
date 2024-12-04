package com.suslovila.cybersus.utils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SusArrayList<E> implements Collection<E> {
    private E[] elements;
    private int currentLength;

    public SusArrayList() {
        elements = (E[]) new Object[3];
    }


    public boolean add(E element) {
        if (currentLength == elements.length) {
            resize();
        }
        elements[currentLength++] = element;
        return true;
    }

    public E get(int index) {
        checkIndex(index);

        return elements[index];
    }

    @Override
    public boolean remove(Object object) {
        Integer index = getIndexOFirst(element -> element.equals(object));
        if (index != null) {
            removeAt(index);
        }
        return index != null;
    }

    private void resize() {
        elements = Arrays.copyOf(elements, (elements.length + 1) * 2);
    }

    public int size() {
        return currentLength;
    }

    public boolean isEmpty() {
        return currentLength == 0;
    }

    public boolean contains(Object object) {
        Integer index = getIndexOFirst(element -> element.equals(object));
        return index != null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < currentLength;
            }

            @Override
            public E next() {
                if (index >= currentLength) {
                    throw new NoSuchElementException();
                }
                return elements[index++];
            }
        };
    }

    public Object[] toArray() {
        return Arrays.copyOf(elements, currentLength);
    }

    public void removeAt(int index) {
        checkIndex(index);
        int numMoved = currentLength - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }

        elements[--currentLength] = null;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= currentLength) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + currentLength);
        }
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return collection.stream().allMatch(this::contains);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        boolean containsAny = false;
        for (E element : collection) {
            containsAny = containsAny || add(element);
        }

        return containsAny;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean changed = false;
        for (Object element : collection) {
            changed = changed || remove(element);
        }

        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        boolean modified = false;
        for (int i = 0; i < currentLength; i++) {
            if (!collection.contains(elements[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    public void clear() {
        Arrays.fill(elements, null);
        currentLength = 0;
    }

    public <T> T[] toArray(T[] a) {
        if (a.length < elements.length) {
            return (T[]) Arrays.copyOf(elements, elements.length, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, elements.length);

        return a;
    }

    private Integer getIndexOFirst(Predicate<E> predicate, int indexFrom, int indexToExcluded) {
        for (int i = indexFrom; i < indexToExcluded; i++) {
            if (predicate.test(elements[i])) {
                return i;
            }
        }

        return null;
    }

    private Integer getIndexOFirst(Predicate<E> predicate) {
        return getIndexOFirst(predicate, 0, currentLength);
    }

    private void removeElementAt(Integer index) {
        if (index != null && Math.abs(index) < elements.length && elements[index] != null) {
            elements[index] = null;
            currentLength -= 1;
        }
    }

    private void foreachNotNull(Consumer<E> consumer) {
        for (int i = 0; i < elements.length; i++) {
            E element = elements[i];
            if (element != null) {
                consumer.accept(element);
            }
        }
    }

    private void foreachIndexed(BiConsumer<Integer, E> consumer) {
        for (int i = 0; i < currentLength; i++) {
            E element = elements[i];
            consumer.accept(i, element);
        }
    }

    public String toString() {
        Iterator<E> it = iterator();
        if (!it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (; ; ) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (!it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }
}