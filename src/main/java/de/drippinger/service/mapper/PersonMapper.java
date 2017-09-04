package de.drippinger.service.mapper;

import de.drippinger.domain.Person;
import de.drippinger.service.dto.PersonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Optional;

@Mapper(componentModel = "cdi")
public interface PersonMapper {

	PersonDto toDto(Person person);

    Person toEntity(PersonDto personDto);

}
