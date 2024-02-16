package client;


import io.restassured.authentication.BasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.*;
import utils.PropertyUtils;

public class Client {
    protected RequestSpecification requestSpecification;
    protected ResponseSpecification responseSpecification;
    protected RequestSpecification requestSpecificationWithInvalidCredentials;


    public Client() {
        buildRequestSpecification();
        buildResponseSpecification();
        buildRequestSpecificationWithInvalidCredentials();
    }




    public RequestSpecification buildRequestSpecification() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(PropertyUtils.getBaseUrl())
                .setPort(Integer.parseInt(PropertyUtils.getPort()))
                .setAuth(getBasicAuthScheme())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        return requestSpecification;
    }

    public RequestSpecification buildRequestSpecificationWithInvalidCredentials() {
        requestSpecificationWithInvalidCredentials = new RequestSpecBuilder()
                .setBaseUri(PropertyUtils.getBaseUrl())
                .setPort(Integer.parseInt(PropertyUtils.getPort()))
                .setAuth(getBasicAuthSchemeWithInvalidCredentials())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        return requestSpecificationWithInvalidCredentials;
    }

    public ResponseSpecification buildResponseSpecification() {
        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
        return responseSpecification;
    }

    private BasicAuthScheme getBasicAuthScheme() {
        BasicAuthScheme basicAuthScheme = new BasicAuthScheme();
        basicAuthScheme.setUserName(PropertyUtils.getUsername());
        basicAuthScheme.setPassword(PropertyUtils.getPassword());
        return basicAuthScheme;
    }

    private BasicAuthScheme getBasicAuthSchemeWithInvalidCredentials() {
        BasicAuthScheme basicAuthScheme = new BasicAuthScheme();
        basicAuthScheme.setUserName(PropertyUtils.getUsername() + "1");
        basicAuthScheme.setPassword(PropertyUtils.getPassword());
        return basicAuthScheme;
    }
}
