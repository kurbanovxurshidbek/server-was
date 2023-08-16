package com.server;

import com.server.calculator.Calculator;
import com.server.calculator.operators.PositiveNumber;
import com.server.http.ClientRequestHandler;
import com.server.http.HttpRequest;
import com.server.http.HttpResponse;
import com.server.http.QueryStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebApplicationServer {
    private static final Logger logger = LoggerFactory.getLogger(WebApplicationServer.class);
    private final int port;


    public WebApplicationServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("[WebApplicationServer] started {} port.", port);

            Socket clientSocket;
            logger.info("[WebApplicationServer] waiting for client.");

            while ((clientSocket = serverSocket.accept()) != null) {
                logger.info("[WebApplicationServer] client connected.");

                //getRequestInMainThread(clientSocket);
                //getRequestInNewThread(clientSocket);
                getRequestInThreadPool(clientSocket);
            }
        }
    }

    /**
     * Step3 - Request in New Thread (Thread Pool with fixed number of threads)
     */
    private void getRequestInThreadPool(Socket clientSocket) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(new ClientRequestHandler(clientSocket));
    }

    /**
     * Step2 - Request in New Thread (CPU and memory problem since created new thread)
     */
    private void getRequestInNewThread(Socket clientSocket) throws IOException {
        new Thread(new ClientRequestHandler(clientSocket)).start();
    }

    /**
     * Step1 - Request in Main Thread (Can't get new request while processing first one)
     */
    private void getRequestInMainThread(Socket clientSocket) throws IOException {

        try (InputStream in = clientSocket.getInputStream(); OutputStream out = clientSocket.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            DataOutputStream dos = new DataOutputStream(out);
            //getRequestDetails(br);

            HttpRequest httpRequest = new HttpRequest(br);
            if (httpRequest.isGetRequest() && httpRequest.matchPath("/calculate")) {
                QueryStrings queryStrings = httpRequest.getQueryStrings();
                int operand1 = Integer.parseInt(queryStrings.getValue("operand1"));
                String operator = queryStrings.getValue("operator");
                int operand2 = Integer.parseInt(queryStrings.getValue("operand2"));

                int result = Calculator.calculate(new PositiveNumber(operand1),operator,new PositiveNumber(operand2));
                byte[] body = String.valueOf(result).getBytes();

                HttpResponse response = new HttpResponse(dos);
                response.response200Header("application/json", body.length);
                response.responseBody(body);
            }
        }
    }

    /**
     * Step0 - Request details in InputStreamReader
     */
    private void getRequestDetails(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != "") {
            System.out.println(line);
        }
    }
}
