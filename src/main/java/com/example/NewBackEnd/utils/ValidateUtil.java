package com.example.NewBackEnd.utils;

import java.util.List;
import java.util.Set;

public class ValidateUtil {
    public static boolean IsNotNull(Object obj) {
        return obj != null;
    }
    public static boolean IsNotNullOrBlank(String str) {
        return str != null && !str.isBlank();
    }

    public static <T>boolean IsNotNullOrEmptyForSet(Set<T> set) {
        return set != null && !set.isEmpty();
    }
    public static <T>boolean IsNotNullOrEmptyForList(List<T> list) {
        return list != null && !list.isEmpty();
    }

}
