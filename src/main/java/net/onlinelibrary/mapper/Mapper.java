package net.onlinelibrary.mapper;

public interface Mapper<TEntity, TDto> {
    TEntity toEntity(TDto dto);
    TDto toDto(TEntity entity);
}
