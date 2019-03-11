package com.example.seckill.simulation;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationTest {

    private static final Random rd = new Random();
    private static final ExecutorService executorService = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20));

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/sec-kill/init?number=10"))
                .build();
        HttpResponse<String> rep = client.send(req, HttpResponse.BodyHandlers.ofString());
        String data = rep.body();
        System.out.println(data.equals("OK") ? "INIT SUCCESS" : "INIT FAILURE");

        for (int i = 0; i < 10; i++) {
            executorService.execute(new BuyerSimulation());
        }
    }

    private static class BuyerSimulation implements Runnable {

        private static AtomicInteger i = new AtomicInteger(0);

        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(rd.nextInt(300));
                    HttpClient client = HttpClient.newBuilder().build();
                    HttpRequest req = HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create("http://localhost:8080/sec-kill?userId=" + i.incrementAndGet() + "&goodsId=1"))
                            .build();
                    HttpResponse<String> rep = client.send(req, HttpResponse.BodyHandlers.ofString());
                    String data = rep.body();
                    System.out.println(data.equals("OK"));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
