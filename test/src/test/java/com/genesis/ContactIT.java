package com.genesis;

import com.genesis.model.Contact;
import com.genesis.model.StatutContact;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.Assert.assertEquals;

public class ContactIT {

    private static final String API_ROOT = "http://localhost:8080/api/contacts";

    @Test
    public void whenGetAllContacts_thenOK() {
        final Response response = RestAssured.get(API_ROOT);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }


    @Test
    public void whenGetCreatedContactById_thenOK() {
        final Contact contact = createContact();
        final String location = createContactAsUri(contact);

        final Response response = RestAssured.get(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenGetNotExistContactById_thenNotFound() {
        final Response response = RestAssured.get(API_ROOT + "/" + randomNumeric(4));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    @Test
    public void whenCreateNewContact_thenCreated() {
        final Contact contact = createContact();

        final Response response = RestAssured.given()
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .body(contact)
                                             .post(API_ROOT);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    public void whenInvalidContact_thenError() {
        final Contact contact = createContact();
        contact.setStatutContact(StatutContact.FREELANCE);

        final Response response = RestAssured.given()
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .body(contact)
                                             .post(API_ROOT);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    public void whenUpdateCreatedContact_thenUpdated() {
        final Contact contact = createContact();
        final String location = createContactAsUri(contact);
        contact.setFirstName("Jonathan");
        contact.setId(Long.parseLong(location.split("api/contacts/")[1]));
        Response response = RestAssured.given()
                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                       .body(contact)
                                       .put(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenDeleteCreatedContact_thenOk() {
        final Contact contact = createContact();
        final String location = createContactAsUri(contact);

        Response response = RestAssured.delete(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
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

    private String createContactAsUri(final Contact contact) {
        final Response response = RestAssured.given()
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .body(contact)
                                             .post(API_ROOT);
        return API_ROOT + "/" + response.jsonPath().get("id");
    }

}