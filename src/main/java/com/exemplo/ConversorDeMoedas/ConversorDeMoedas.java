package com.exemplo.ConversorDeMoedas;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConversorDeMoedas {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Conversor de Moedas ===");
        System.out.println("Escolha uma conversão:");
        System.out.println("1. USD para BRL");
        System.out.println("2. EUR para BRL");
        System.out.println("3. GBP para BRL");
        System.out.println("4. BRL para USD");
        System.out.println("5. BRL para EUR");
        System.out.println("6. BRL para GBP");

        int opcao = scanner.nextInt();
        String moedaOrigem = "";
        String moedaDestino = "";

        switch (opcao) {
            case 1: moedaOrigem = "USD"; moedaDestino = "BRL"; break;
            case 2: moedaOrigem = "EUR"; moedaDestino = "BRL"; break;
            case 3: moedaOrigem = "GBP"; moedaDestino = "BRL"; break;
            case 4: moedaOrigem = "BRL"; moedaDestino = "USD"; break;
            case 5: moedaOrigem = "BRL"; moedaDestino = "EUR"; break;
            case 6: moedaOrigem = "BRL"; moedaDestino = "GBP"; break;
            default:
                System.out.println("Opção inválida!");
                scanner.close();
                return;
        }

        System.out.println("Digite o valor a ser convertido:");
        double valor = scanner.nextDouble();

        obterTaxaDeConversao(moedaOrigem, moedaDestino, valor);
        scanner.close();
    }

    private static void obterTaxaDeConversao(String moedaOrigem, String moedaDestino, double valor) {
        try {
            String url = "https://api.exchangerate-api.com/v4/latest/" + moedaOrigem;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

            if (json.has("rates") && json.get("rates").getAsJsonObject().has(moedaDestino)) {
                double taxa = json.get("rates").getAsJsonObject().get(moedaDestino).getAsDouble();
                double resultado = valor * taxa;

                System.out.printf("Resultado: %.2f %s%n", resultado, moedaDestino);
            } else {
                System.out.println("Erro: Moeda não suportada.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao acessar a API: " + e.getMessage());
        }
    }
}
