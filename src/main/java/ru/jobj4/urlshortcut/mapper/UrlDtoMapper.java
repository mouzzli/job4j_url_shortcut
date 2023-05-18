package ru.jobj4.urlshortcut.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.jobj4.urlshortcut.dto.UrlDto;
import ru.jobj4.urlshortcut.model.Url;

@Mapper
public interface UrlDtoMapper {
    UrlDtoMapper INSTANCE = Mappers.getMapper(UrlDtoMapper.class);

    @Mapping(source = "url", target = "site")
    @Mapping(source = "counter", target = "total")
    UrlDto convertToUrlDto(Url url);
}
