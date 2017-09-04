package de.drippinger.service.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
public class PersonDto implements Serializable {

	private Long id;

	private String name;

}
