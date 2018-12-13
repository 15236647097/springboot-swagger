package com.neo.web;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Created by wyn on 2018/10/22.
 */
@Component
@Path("/test")
public class TestPath {

    @Path("/select")
    @GET
    public String aa(){
        System.out.println("asdf1235");
        return "aasdfs";
    }
}
