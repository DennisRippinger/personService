package de.drippinger.service.resources;

import de.drippinger.ArquillianBuilder;
import de.drippinger.service.dto.PersonDto;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static de.drippinger.domain.BddAssertions.then;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@RunWith(Arquillian.class)
public class PersonResourceTest {

    private WebTarget webTarget;

    @Test
    @RunAsClient
    public void test_CRUD_Lifecycle_positive(@ArquillianResteasyResource("api") WebTarget webTarget) throws Exception {
        this.webTarget = webTarget; // Field injection not working

        // GIVEN: An empty DB.

        // WHEN: A Person called 'Dennis' is POSTed
        PersonDto person = new PersonDto();
        person.setName("Dennis");
        callPOSTForPerson(person);

        // THEN: We GET the Person with the id '1'
        PersonDto result = callGETForPerson(1L);

        then(result).hasId(1L);
        then(result).hasName("Dennis");

        // WHEN: We alter the Name to 'Markus'
        person.setName("Markus");
        callPUTForPerson(1L, person);

        // THEN: We PUT the Person with the id '1' which has the name 'Markus'
        result = callGETForPerson(1L);

        then(result).hasId(1L);
        then(result).hasName("Markus");

        // FINALLY: We delete the Person with id '1'
        callDELETEForPerson(1L);
    }

    @Test
    @RunAsClient
    public void test_CRUD_Lifecycle_fail_to_short(@ArquillianResteasyResource("api") WebTarget webTarget) throws Exception {
        this.webTarget = webTarget; // Field injection not working

        // GIVEN: An empty DB.

        // WHEN: A Person called '_' is POSTed
        assertThatThrownBy(() -> {
            PersonDto person = new PersonDto();
            person.setName("_");
            callPOSTForPerson(person);
        })
            // THEN: Fails because to short
            .isExactlyInstanceOf(ComparisonFailure.class);
    }

    @Test
    @RunAsClient
    public void test_CRUD_Lifecycle_fail_to_long(@ArquillianResteasyResource("api") WebTarget webTarget) throws Exception {
        this.webTarget = webTarget; // Field injection not working

        // GIVEN: An empty DB.

        // WHEN: A Person with a to long name is POSTed
        assertThatThrownBy(() -> {
            PersonDto person = new PersonDto();
            person.setName(String.join("", Collections.nCopies(21, "*")));
            callPOSTForPerson(person);
        })
            // THEN: Fails because to long
            .isExactlyInstanceOf(ComparisonFailure.class);
    }

    private PersonDto callGETForPerson(Long id) {
        Response response = webTarget
            .path("/persons/" + id)
            .request(MediaType.APPLICATION_JSON)
            .get();

        PersonDto result = response.readEntity(PersonDto.class);
        response.close();
        return result;
    }

    private void callPOSTForPerson(PersonDto personDto) {
        Response response = webTarget
            .path("/persons/")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.json(personDto));


        assertThat(response.getStatus())
            .isEqualTo(CREATED.getStatusCode())
            .describedAs("POST was not successful");

        response.close();
    }

    private void callDELETEForPerson(Long id) {
        Response response = webTarget
            .path("/persons/" + id)
            .request(MediaType.APPLICATION_JSON)
            .delete();

        assertThat(response.getStatus())
            .isEqualTo(OK.getStatusCode())
            .describedAs("DELETE was not successful");

        response.close();
    }

    private void callPUTForPerson(Long id, PersonDto personDto) {
        Response response = webTarget
            .path("/persons/" + id)
            .request(MediaType.APPLICATION_JSON)
            .put(Entity.json(personDto));


        assertThat(response.getStatus())
            .isEqualTo(OK.getStatusCode())
            .describedAs("PUT was not successful");

        response.close();
    }

    @Deployment
    public static WebArchive createDeployment() {
        return ArquillianBuilder.createBaseDeployment();
    }

}
