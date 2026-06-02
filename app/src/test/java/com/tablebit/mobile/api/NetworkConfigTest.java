package com.tablebit.mobile.api;

import com.tablebit.mobile.data.api.NetworkConfig;

import org.junit.Test;

import static org.junit.Assert.*;

public class NetworkConfigTest {

    @Test
    public void baseUrl_endsWithSlash() {
        assertTrue("BASE_URL debe terminar con /",
                NetworkConfig.BASE_URL.endsWith("/"));
    }

    @Test
    public void baseUrl_isLocalhost() {
        assertTrue("BASE_URL debe apuntar a localhost",
                NetworkConfig.BASE_URL.contains("127.0.0.1"));
    }

    @Test
    public void storageUrl_containsBaseUrl() {
        assertTrue("STORAGE_URL debe contener BASE_URL",
                NetworkConfig.STORAGE_URL.startsWith(NetworkConfig.BASE_URL));
    }

    @Test
    public void apiUrl_containsBaseUrl() {
        assertTrue("API_URL debe contener BASE_URL",
                NetworkConfig.API_URL.startsWith(NetworkConfig.BASE_URL));
    }

    @Test
    public void apiUrl_endsWithApi() {
        assertTrue("API_URL debe terminar con api/",
                NetworkConfig.API_URL.endsWith("api/"));
    }

    @Test
    public void storageUrl_endsWithStorage() {
        assertTrue("STORAGE_URL debe terminar con storage/",
                NetworkConfig.STORAGE_URL.endsWith("storage/"));
    }
}
