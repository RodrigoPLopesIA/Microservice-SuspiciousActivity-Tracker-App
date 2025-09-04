package com.rodrigo.ms.suspicious_activity_tracker.integration.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import com.rodrigo.ms.suspicious_activity_tracker.integration.config.TestContainerPostgresqlConfig;

public class SuspiciousActivityControllerTest extends TestContainerPostgresqlConfig {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    @DisplayName("should return 200 when get all suspicious activity")
    public void testGetAllSuspiciousActivity() {
        RestAssured
            .given()
            .when()
            .get("/suspicious_activity")
            .then()
            .statusCode(200);
    }

    @Test
    @DisplayName("should create a new suspicious activity and return 201")
    public void testCreateSuspiciousActivity() {
        var requestBody = """
            {
                "userId": "550e8400-e29b-41d4-a716-446655440000",
                "endpoint": "https://api.meuservico.com/v1/resource",
                "ipAddress": "192.168.0.100",
                "description": "Usuário tentou acessar recurso não autorizado."
            }""";
        
        

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post("/suspicious_activity")
            .then()
            .statusCode(201)
            .body("id", Matchers.notNullValue())
            .body("userId", Matchers.notNullValue())
            .body("endpoint", Matchers.equalTo("https://api.meuservico.com/v1/resource"))
            .body("ipAddress", Matchers.equalTo("192.168.0.100"))
            .body("description", Matchers.equalTo("Usuário tentou acessar recurso não autorizado."));
    }

    @Test
    @DisplayName("should return 404 when get suspicious activity by non-existent id")
    public void testGetSuspiciousActivityByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        RestAssured
            .given()
            .when()
            .get("/suspicious_activity/{id}", randomId)
            .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("should create, update and get suspicious activity")
    public void testUpdateAndGetSuspiciousActivity() {
        // Create
        var userId = "550e8400-e29b-41d4-a716-446655440000";
        var createBody = """
            {
                "userId": "550e8400-e29b-41d4-a716-446655440000",
                "endpoint": "https://api.meuservico.com/v1/resource",
                "ipAddress": "192.168.0.100",
                "description": "Usuário tentou acessar recurso não autorizado."
            }""";

        var id =
            RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(createBody)
                .when()
                .post("/suspicious_activity")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Update
        var updateBody = """
            {
                "userId": "550e8400-e29b-41d4-a716-446655440000",
                "endpoint": "https://api.meuservico.com/v1/resource",
                "ipAddress": "192.168.0.100",
                "description": "Usuário tentou acessar recurso não autorizado."
            }""";

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(updateBody)
            .when()
            .put("/suspicious_activity/{id}", id)
            .then()
            .statusCode(200)
            .body("endpoint", Matchers.equalTo("https://api.meuservico.com/v1/resource"))
            .body("ipAddress", Matchers.equalTo("192.168.0.100"))
            .body("description", Matchers.equalTo("Usuário tentou acessar recurso não autorizado."));

        // Get by id
        RestAssured
            .given()
            .when()
            .get("/suspicious_activity/{id}", id)
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo(id))
            .body("userId", Matchers.equalTo(userId.toString()))
            .body("endpoint", Matchers.equalTo("https://api.meuservico.com/v1/resource"))
            .body("ipAddress", Matchers.equalTo("192.168.0.100"))
            .body("description", Matchers.equalTo("Usuário tentou acessar recurso não autorizado."));
    }

    @Test
    @DisplayName("should delete suspicious activity and return 204")
    public void testDeleteSuspiciousActivity() {
        // Create
        var userId = "550e8400-e29b-41d4-a716-446655440000";
        var createBody = """
            {
                "userId": "550e8400-e29b-41d4-a716-446655440000",
                "endpoint": "https://api.meuservico.com/v1/resource",
                "ipAddress": "192.168.0.100",
                "description": "Usuário tentou acessar recurso não autorizado."
            }""";

        var id =
            RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(createBody)
                .when()
                .post("/suspicious_activity")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Delete
        RestAssured
            .given()
            .when()
            .delete("/suspicious_activity/{id}", id)
            .then()
            .statusCode(204);

        // Try to get deleted
        RestAssured
            .given()
            .when()
            .get("/suspicious_activity/{id}", id)
            .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("should return 404 when updating non-existent suspicious activity")
    public void testUpdateSuspiciousActivityNotFound() {
        UUID randomId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        var updateBody = """
            {
                "userId": "550e8400-e29b-41d4-a716-446655440000",
                "endpoint": "https://api.meuservico.com/v1/resource",
                "ipAddress": "192.168.0.100",
                "description": "Usuário tentou acessar recurso não autorizado."
            }""";

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(updateBody)
            .when()
            .put("/suspicious_activity/{id}", randomId)
            .then()
            .statusCode(404);
    }

    @Test
    @DisplayName("should return 404 when deleting non-existent suspicious activity")
    public void testDeleteSuspiciousActivityNotFound() {
        UUID randomId = UUID.randomUUID();
        RestAssured
            .given()
            .when()
            .delete("/suspicious_activity/{id}", randomId)
            .then()
            .statusCode(404);
    }
}
