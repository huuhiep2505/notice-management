//package com.noticemanagement.demo.controller;
//
//import java.io.IOException;
//import org.junit.jupiter.api.Test;
//
//public class NoticeManagementControllerTest {
//
//  @Test
//  public void givenUserDoesNotExists_whenUserInfoIsRetrieved_then404IsReceived()
//      throws ClientProtocolException, IOException {
//
//    // Given
//    String name = RandomStringUtils.randomAlphabetic( 8 );
//    HttpUriRequest request = new HttpGet( "https://localhost:8080/users/" + name );
//
//    // When
//    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
//
//    // Then
//    assertThat(
//        httpResponse.getStatusLine().getStatusCode(),
//        equalTo(HttpStatus.SC_NOT_FOUND));
//  }
//}
