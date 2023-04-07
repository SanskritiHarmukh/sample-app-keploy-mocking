package com.example.testing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import com.example.mocking.Post;
import com.example.mocking.PostClient;
import io.keploy.service.mock.MockLib;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class PostClientTest {

    private MockWebServer mockWebServer;
    private PostClient post;
    private static String RESPONSE_ALL;


    static {
        try {
            RESPONSE_ALL = new String(
                    (PostClient.class.getClassLoader().
                            getResourceAsStream
                                    ("posts-all-success.json"))
                            .readAllBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void init() {
        this.mockWebServer = new MockWebServer();
        this.post = new PostClient(mockWebServer.url("/").
                toString());
    }


    @Test
    public void fetchAllPosts() throws InterruptedException, IOException {

      new MockLib("okhttpCall"); //setting name of the mock
        mockWebServer.enqueue(new MockResponse().
                addHeader("Content-Type", "application/json; "
                        + "charset=utf-8")
                .setBody(RESPONSE_ALL).setResponseCode(200));
        List<Post> result = post.fetchAllPosts();

        assertEquals(2, result.size());
        assertEquals("title 1", result.get(0).title());
        assertEquals("1", result.get(0).userId());
    }


    @After
    public void aftertest() {
        try {
            mockWebServer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
