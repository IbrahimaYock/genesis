package com.genesis;

import com.genesis.model.Contact;
import com.genesis.model.Entreprise;
import com.genesis.model.StatutContact;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.Assert.assertEquals;

public class EntrepriseIT {
    private static final String API_ROOT = "http://localhost:8080/api/entreprises";

    @Test
    public void whenGetAllEntreprises_thenOK() {
        final Response response = RestAssured.get(API_ROOT);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenGetCreatedEntrepriseById_thenOK() {
        final Entreprise entreprise = createEntreprise();
        final String location = createEntrepriseAsUri(entreprise);

        final Response response = RestAssured.get(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenGetNotExistEntrepriseById_thenNotFound() {
        final Response response = RestAssured.get(API_ROOT + "/" + randomNumeric(4));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    @Test
    public void whenCreateNewEntreprise_thenCreated() {
        final Entreprise entreprise = createEntreprise();

        final Response response = RestAssured.given()
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .body(entreprise)
                                             .post(API_ROOT);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }



    @Test
    public void whenUpdateCreatedEntreprise_thenUpdated() {
        final Entreprise entreprise = createEntreprise();
        final String location = createEntrepriseAsUri(entreprise);
        entreprise.setId(Long.parseLong(location.split("api/entreprises/")[1]));
        Response response = RestAssured.given()
                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                       .body(entreprise)
                                       .put(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenDeleteCreatedEntreprise_thenOk() {
        final Entreprise entreprise = createEntreprise();
        final String location = createEntrepriseAsUri(entreprise);

        Response response = RestAssured.delete(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }


    private Entreprise createEntreprise() {
        final Entreprise entreprise = new Entreprise();
        entreprise.setAdresse("Bruxelles");
        entreprise.setNumeroTVA("1234");
        entreprise.setContacts(Arrays.asList(createContact()));
        entreprise.setAutreAdresses(Arrays.asList("Paris", "Dakar"));
        entreprise.setId((long) Math.random());
        return entreprise;
    }

    private Contact createContact() {
        final Contact contact = new Contact();
        contact.setFirstName("Ibrahima");
        contact.setLastName("Yock");
        contact.setStatutContact(StatutContact.EMPLOYE);
        contact.setId((long) Math.random());
        contact.setAdresse("Gand");
        return contact;
    }

    private String createEntrepriseAsUri(final Entreprise entreprise) {
        final Response response = RestAssured.given()
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .body(entreprise)
                                             .post(API_ROOT);
        return API_ROOT + "/" + response.jsonPath().get("id");
    }
}
