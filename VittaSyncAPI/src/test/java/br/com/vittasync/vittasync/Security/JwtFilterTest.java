package br.com.vittasync.vittasync.Security;

import br.com.vittasync.vittasync.Service.JwtService;
import br.com.vittasync.vittasync.Service.SessaoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtFilterTest {

    private JwtService jwtService;
    private SessaoService sessaoService;
    private JwtFilter jwtFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        jwtService = mock(JwtService.class);
        sessaoService = mock(SessaoService.class);
        jwtFilter = new JwtFilter(jwtService, sessaoService);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);

        SecurityContextHolder.clearContext();
    }

    @Test
    void testOptionsRequestPassaDireto() throws Exception {
        when(request.getMethod()).thenReturn("OPTIONS");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testSemAuthorizationPassaDireto() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testTokenNaoAtivoRetorna401() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(sessaoService.isTokenAtivo("token123")).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testTokenValidoConfiguraSecurityContext() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");
        when(sessaoService.isTokenAtivo("token123")).thenReturn(true);
        when(jwtService.validarToken("token123")).thenReturn(true);
        when(jwtService.extrairCpf("token123")).thenReturn("12345678901");

        jwtFilter.doFilterInternal(request, response, filterChain);

        assert SecurityContextHolder.getContext().getAuthentication() != null;
        assert SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("12345678901");

        verify(filterChain, times(1)).doFilter(request, response);
    }
}
