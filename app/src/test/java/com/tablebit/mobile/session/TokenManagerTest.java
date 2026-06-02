package com.tablebit.mobile.session;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TokenManagerTest {

    @Mock
    private Context mockContext;

    @Mock
    private SharedPreferences mockPrefs;

    @Mock
    private SharedPreferences.Editor mockEditor;

    private TokenManager tokenManager;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs);
        when(mockPrefs.edit()).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);
        when(mockEditor.putInt(anyString(), anyInt())).thenReturn(mockEditor);

        tokenManager = new TokenManager(mockContext);
    }

    @Test
    public void isLoggedIn_noToken_returnsFalse() {
        when(mockPrefs.getString("auth_token", null)).thenReturn(null);
        assertFalse(tokenManager.isLoggedIn());
    }

    @Test
    public void isLoggedIn_withToken_returnsTrue() {
        when(mockPrefs.getString("auth_token", null)).thenReturn("token123");
        assertTrue(tokenManager.isLoggedIn());
    }

    @Test
    public void saveToken_storesInSharedPreferences() {
        tokenManager.saveToken("test-token");
        verify(mockEditor).putString("auth_token", "test-token");
        verify(mockEditor).apply();
    }

    @Test
    public void getToken_returnsSavedToken() {
        when(mockPrefs.getString("auth_token", null)).thenReturn("saved-token");
        assertEquals("saved-token", tokenManager.getToken());
    }

    @Test
    public void saveUserInfo_storesAllFields() {
        tokenManager.saveUserInfo(1, "Juan", "juan@test.com", "admin_restaurante");
        verify(mockEditor).putInt("user_id", 1);
        verify(mockEditor).putString("user_name", "Juan");
        verify(mockEditor).putString("user_email", "juan@test.com");
        verify(mockEditor).putString("user_role", "admin_restaurante");
        verify(mockEditor).apply();
    }

    @Test
    public void getUserId_returnsDefaultMinusOne() {
        when(mockPrefs.getInt("user_id", -1)).thenReturn(-1);
        assertEquals(-1, tokenManager.getUserId());
    }

    @Test
    public void getUserRole_returnsClienteByDefault() {
        when(mockPrefs.getString("user_role", "cliente")).thenReturn("cliente");
        assertEquals("cliente", tokenManager.getUserRole());
    }

    @Test
    public void isAdmin_adminRole_returnsTrue() {
        when(mockPrefs.getString("user_role", "cliente")).thenReturn("admin_restaurante");
        assertTrue(tokenManager.isAdmin());
    }

    @Test
    public void isAdmin_clienteRole_returnsFalse() {
        when(mockPrefs.getString("user_role", "cliente")).thenReturn("cliente");
        assertFalse(tokenManager.isAdmin());
    }

    @Test
    public void clearSession_clearsPreferences() {
        tokenManager.clearSession();
        verify(mockEditor).clear();
        verify(mockEditor).apply();
    }
}
