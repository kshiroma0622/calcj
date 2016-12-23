package kshiroma0622.calcj;

import java.util.*;

public class Util {
    public static <T> List<T> newArryaList() {
        return new ArrayList<T>();
    }

    public static <T> List<T> newLinkedList() {
        return new LinkedList<T>();
    }

    public static <T> List<T> newLinkedList(T[] seq) {
        if (seq == null) {
            return null;
        }
        List<T> list = newLinkedList();
        for (T t : seq) {
            list.add(t);
        }
        return list;
    }

    public static <K, V> Map<K, V> newMap() {
        return new HashMap<K, V>();
    }

    public static <E> Set<E> newSet() {
        return new java.util.HashSet<E>();
    }

    public static boolean isEmpty(Collection collection) {
        if (collection == null) {
            return true;
        }
        return collection.isEmpty();
    }

    public static <V> boolean withInRange(List<V> list, int index) {
        if (isEmpty(list)) {
            return false;
        }
        if (index < 0) {
            return false;
        }
        int lastIndex = list.size() - 1;

        return lastIndex >= index;
    }

    public static <V> V get(List<V> list, int index) {
        if (!withInRange(list, index)) {
            return null;
        }
        return list.get(index);
    }

    public static <V> V getLast(List<V> list) {
        if (isEmpty(list)) {
            return null;
        }
        int index = list.size() - 1;
        return list.get(index);
    }

    public static void throwPGError() {
        throw new ProgramException();
    }

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }
}
