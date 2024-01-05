package org.example.studentApp;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.example.Utils.TestUtils;
import org.example.model.StudentPojo;
import org.example.path.EndPoint;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CURDWITHPOJO {
    RequestSpecification requestSpecification;
    Response response;
    ValidatableResponse validatableResponse;
    int studentId;
    static String firstName = "Zaranaaa"+ TestUtils.getRamdomValue();
    static String lastName = "Soniiii"+ TestUtils.getRamdomValue();
    static String email = "keseho@gmail.com"+ TestUtils.getRamdomValue();
    static String programme = "Software Testing"+ TestUtils.getRamdomValue();
    @Test(priority=1)
    public void getAllStudentList(){
    response= given().log().all()
            .when()
            .get("http://localhost:8080/student/list");
    response.then().log().all().statusCode(200);
    }
    @Test(priority = 2)
    public void postStudentInfo(){
        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        // in data, course in list so create a line34
        List<String>course = new ArrayList<>();
        course.add("RestAssured");
        course.add("backend");
        studentPojo.setCourses(course);
        response= given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .body(studentPojo)
                .post("http://localhost:8080/student");
        response.then().log().all().statusCode(201)
                .body("msg", equalTo("Student added"));
    }
    //When don't know about id that time use extract method
    @Test(priority = 3)
    public void getAllStudentAndExtractIdOfPostData(){
        HashMap<String, Object> studentDetails = given().log().all()
                .when()
                .get(EndPoint.GET_ALL_STUDENTS)
                .then()
                .extract()
                .path("findAll{it.firstName=='" + firstName + "'}.get(0)");//groovey method
        System.out.println(studentDetails);
        studentId =(int)studentDetails.get("id");// to print seperate Id
    }
    @Test(priority = 4)
    public void getSingleStudentinfo(){
        given()
                .pathParam("id",studentId)
                .get("http://localhost:8080/student/{id}")
                .then().statusCode(200)
                .body("firstName",equalTo(firstName));
    }
    @Test(priority = 5)
    public void verifyDeletedStudentId(){
    given()
            .pathParam("id",studentId)
            .when()
            .delete("http://localhost:8080/student/{id}")
            .then()
            .statusCode(204);
    }
    @Test(priority = 6)
    public void verifyDeletedStudentIdinfo(){
        given()
                .pathParam("id",studentId)
                .when()
                .get("http://localhost:8080/student/{id}")
                .then()
                .statusCode(404);

    }
}
