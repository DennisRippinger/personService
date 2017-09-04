package de.drippinger.service;

import de.drippinger.service.dto.PersonDto;

import java.util.Optional;

public interface PersonService {

    /**
     * Save a person.
     *
     * @param personDto the entity to save
     * @return the persisted entity
     */
    PersonDto save(PersonDto personDto);


    /**
     * Get by the "id" of a person.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<PersonDto> findOne(Long id);

    /**
     * Delete by the "id" person.
     *
     * @param id the id of the entity
     */
    boolean delete(Long id);

}
