package queue;

import java.lang.reflect.Array;

public class Queue<T> {
    private T[] queue;
    private int initialSize;
    private int size;
    private Class<T> c;
    private int nextEmptyPos = 0;
    private int head = 0;

    public Queue(Class<T> c, int size) {
        //All elements inside the array are of type T, type safety is guaranteed
        //noinspection unchecked
        queue = (T[]) Array.newInstance(c, size);
        this.initialSize = size;
        this.size = size;
        this.c = c;
    }

    public void add(T value) {
        checkQueueSizeLimit();
        queue[nextEmptyPos] = value;
        nextEmptyPos++;
    }

    public T remove() throws RemoveWhileQueueIsEmptyException {
        if (head == nextEmptyPos || head == -1) {
            throw new RemoveWhileQueueIsEmptyException();
        }
        head++;
        return queue[head - 1];
    }

    public void printQueue() {
        for (int i = head; i < nextEmptyPos; i++) {
            System.out.println(queue[i]);
        }
    }

    public int length() {
        return nextEmptyPos - head;
    }

    private void checkQueueSizeLimit() {
        if (nextEmptyPos >= size) {

            if (head != -1 && head == nextEmptyPos) {
                //If queue is fully emptied reduce queue to original size, reset head and nextEmptyPos

                //All elements inside the array are of type T, type safety is guaranteed
                //noinspection unchecked
                queue = (T[]) Array.newInstance(c, initialSize);
                head = -1;
                nextEmptyPos = 0;
                size = initialSize;
            } else if ((nextEmptyPos - head) <= size / 2) {
                //If queue is half empty rearrange elements
                int j = 0;
                for (int i = head; i < nextEmptyPos; i++, j++) {
                    queue[j] = queue[i];
                }
                nextEmptyPos = j;
                head = 0;
            } else {
                //If queue is more than half full generate larger array and rearrange elements

                //All elements inside the array are of type T, type safety is guaranteed
                //noinspection unchecked
                T[] newQueue = (T[]) Array.newInstance(c, 2 * size);
                int j = 0;
                for (int i = head; i < nextEmptyPos; i++, j++) {
                    newQueue[j] = queue[i];
                }
                queue = newQueue;
                nextEmptyPos = j;
                head = 0;
                size = 2 * size;
            }
        }
    }
}
