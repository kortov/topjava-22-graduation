package ru.kortov.topjava.graduation.converter;


import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface EntityConverter<E, D> {

    D convertToDto(E entity);

    E convertToEntity(D dto);

    @NonNull
    default List<D> convertToDtoList(@Nullable Collection<? extends E> entityCollection) {
        if (CollectionUtils.isEmpty(entityCollection)) {
            return Collections.emptyList();
        }
        return entityCollection.stream().map(this::convertToDto).toList();
    }

    @NonNull
    default List<E> convertToEntityList(@Nullable Collection<? extends D> dtoCollection) {
        if (CollectionUtils.isEmpty(dtoCollection)) {
            return Collections.emptyList();
        }
        return dtoCollection.stream().map(this::convertToEntity).toList();
    }

}
