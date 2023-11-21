package com.zhc.cloud.common.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhuhongcang
 * @date 2023/4/19
 */
public interface IMapper<S, T> {
    /**
     * @param source
     * @return
     */
    T to(S source);

    /**
     * @param target
     * @return
     */
    S from(T target);

    default List<T> toList(List<S> sourceList) {
        return sourceList == null || sourceList.isEmpty() ? Collections.EMPTY_LIST : sourceList.stream().map(this::to).collect(Collectors.toList());
    }

    default List<S> fromList(List<T> targetList) {
        return targetList == null || targetList.isEmpty() ? Collections.EMPTY_LIST : targetList.stream().map(this::from).collect(Collectors.toList());
    }
}
