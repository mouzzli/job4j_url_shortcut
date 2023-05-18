package ru.jobj4.urlshortcut.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.jobj4.urlshortcut.dto.CodeDto;
import ru.jobj4.urlshortcut.model.Url;

@Mapper
public interface CodeDtoMapper {

    CodeDtoMapper INSTANCE = Mappers.getMapper(CodeDtoMapper.class);

    CodeDto toCodeDto(Url url);
}
