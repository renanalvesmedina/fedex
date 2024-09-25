package com.mercurio.lms.vendas.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClienteTest {

    @Test
    public void testGetBlEnviaDacteXmlFat() {
        final Cliente cliente = new Cliente();
        assertNotNull(cliente.getBlEnviaDacteXmlFat());
        assertFalse(cliente.getBlEnviaDacteXmlFat());

        cliente.setBlEnviaDacteXmlFat(true);
        assertTrue(cliente.getBlEnviaDacteXmlFat());

        cliente.setBlEnviaDacteXmlFat(false);
        assertFalse(cliente.getBlEnviaDacteXmlFat());

        cliente.setBlEnviaDacteXmlFat(null);
        assertNotNull(cliente.getBlEnviaDacteXmlFat());
        assertFalse(cliente.getBlEnviaDacteXmlFat());
    }

    @Test
    public void testGetBlEnviaDocsFaturamentoNas() {
        final Cliente cliente = new Cliente();
        assertFalse(cliente.getBlEnviaDocsFaturamentoNas());

        cliente.setBlEnviaDocsFaturamentoNas(true);
        assertTrue(cliente.getBlEnviaDocsFaturamentoNas());

        cliente.setBlEnviaDocsFaturamentoNas(false);
        assertFalse(cliente.getBlEnviaDocsFaturamentoNas());

        cliente.setBlEnviaDocsFaturamentoNas(null);
        assertFalse(cliente.getBlEnviaDocsFaturamentoNas());
    }

    @Test
    public void testAoDefinirBlEnviaDacteXmlFatComoVerdadeiroDeveDefiniroBlEnviaDocsFaturamentoNasComFalso() {
        final Cliente cliente = new Cliente();

        cliente.setBlEnviaDocsFaturamentoNas(true);
        assertTrue(cliente.getBlEnviaDocsFaturamentoNas());

        cliente.setBlEnviaDacteXmlFat(true);
        assertFalse(cliente.getBlEnviaDocsFaturamentoNas());

        cliente.setBlEnviaDacteXmlFat(false);
        assertFalse(cliente.getBlEnviaDocsFaturamentoNas());
    }

    @Test
    public void testAoDefinirBlEnviaDocsFaturamentoNasComoVerdadeiroDeveDefinirBlEnviaDacteXmlFatComoFalsoEhViceVersa() {
        final Cliente cliente = new Cliente();

        cliente.setBlEnviaDacteXmlFat(true);
        assertTrue(cliente.getBlEnviaDacteXmlFat());

        cliente.setBlEnviaDocsFaturamentoNas(true);
        assertFalse(cliente.getBlEnviaDacteXmlFat());

        cliente.setBlEnviaDocsFaturamentoNas(false);
        assertFalse(cliente.getBlEnviaDacteXmlFat());
    }
}