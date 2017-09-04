package de.drippinger.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Person {

	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

	@NotNull
	@Length(min = 3, max = 20)
	private String name;

}
