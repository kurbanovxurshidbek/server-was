package com.server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        new WebApplicationServer(8080).start(); // == Tomcat
    }
}