package ru.vtkachenko.simpletmsback.mapper;

public interface EntityMapper<D, E> {
    public D toDto(E entity);
    public E toEntity(D dto);
}
