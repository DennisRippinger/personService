package de.drippinger.repository;

import de.drippinger.domain.Person;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

@Repository
public interface PersonRepository extends EntityRepository<Person, Long> {


}
