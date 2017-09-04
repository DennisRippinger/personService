package de.drippinger.service.impl;

import de.drippinger.domain.Person;
import de.drippinger.repository.PersonRepository;
import de.drippinger.service.PersonService;
import de.drippinger.service.dto.PersonDto;
import de.drippinger.service.mapper.PersonMapper;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
@Transactional
@ApplicationScoped
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    @Inject
    public PersonServiceImpl(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    public PersonDto save(PersonDto personDto) {
        log.debug("Request to save Person: {}", personDto);

        Person person = personMapper.toEntity(personDto);
        person = personRepository.save(person);
        PersonDto result = personMapper.toDto(person);

        return result;
    }

    @Override
    public Optional<PersonDto> findOne(Long id) {
        log.debug("Request to get Person: {}", id);
        Person person = personRepository.findBy(id);

        return Optional.ofNullable(personMapper.toDto(person));
    }

    @Override
    public boolean delete(Long id) {
        log.debug("Request to delete Person: {}", id);
        Person person = personRepository.findBy(id);
        if (isNull(person)) {
            return false;
        }
        personRepository.remove(person);
        return true;
    }
}
