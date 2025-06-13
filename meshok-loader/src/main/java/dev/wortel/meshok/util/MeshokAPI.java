package dev.wortel.meshok.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class MeshokAPI {
    @Value("${meshok.access-token}")
    private String token;

    private String sendRequest(String method, Map<String, String> params) throws Exception {
        String baseUrl = "https://api.meshok.net/sAPIv1/";
        String url = baseUrl + method;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + token);
        con.setDoOutput(true);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (!postData.isEmpty()) postData.append('&');
            postData.append(param.getKey());
            postData.append('=');
            postData.append(param.getValue());
        }

        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);
        con.getOutputStream().write(postDataBytes);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public String getItemList() {
        try {
            return sendRequest("getItemList", new HashMap<>());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getFinishedItemList() throws Exception {
        return sendRequest("getFinishedItemList", new HashMap<>());
    }

    public String getUnsoldFinishedItemList() throws Exception {
        return sendRequest("getUnsoldFinishedItemList", new HashMap<>());
    }

    public String getSoldFinishedItemList() throws Exception {
        return sendRequest("getSoldFinishedItemList", new HashMap<>());
    }

    public String getItemInfo(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        try {
            return sendRequest("getItemInfo", params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getAccountInfo() throws Exception {
        return sendRequest("getAccountInfo", new HashMap<>());
    }

    public String getCommonDescriptionList() throws Exception {
        return sendRequest("getCommonDescriptionList", new HashMap<>());
    }

    public String getSubCategory(String id) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return sendRequest("getSubCategory", params);
    }

    public String getCategoryInfo(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        try {
            return sendRequest("getCategoryInfo", params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getCurencyList() throws Exception {
        return sendRequest("getCurencyList", new HashMap<>());
    }

    public String getCountryList() throws Exception {
        return sendRequest("getCountryList", new HashMap<>());
    }

    public String getCitiesList(String id) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return sendRequest("getCitiesList", params);
    }

    public String stopSale(String id) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return sendRequest("stopSale", params);
    }

    public String relistItem(String id) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return sendRequest("relistItem", params);
    }

    public String deleteItem(String id) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return sendRequest("deleteItem", params);
    }

    public String listItem(Map<String, String> params) throws Exception {
        return sendRequest("listItem", params);
    }

    public String updateItem(Map<String, String> params) throws Exception {
        return sendRequest("updateItem", params);
    }
}