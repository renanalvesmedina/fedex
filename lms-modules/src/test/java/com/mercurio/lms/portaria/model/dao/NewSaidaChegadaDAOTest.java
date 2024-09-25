package com.mercurio.lms.portaria.model.dao;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.util.LmsMocks;
import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class NewSaidaChegadaDAOTest {

    private List<Serializable> entidades = new ArrayList<Serializable>();
    private List<Serializable> entidadesASeremInseridas = new ArrayList<Serializable>();
    private NewSaidaChegadaDAO newSaidaChegadaDAO;

    @BeforeTest
    public void start() {
        LmsMocks.mockDefaultContext();
        
        newSaidaChegadaDAO = new NewSaidaChegadaDAO();

        EventoDocumentoServico eventoDocumentoServico = new EventoDocumentoServico();

        eventoDocumentoServico.setFilial(new Filial());
        eventoDocumentoServico.getFilial().setIdFilial(370L);

        eventoDocumentoServico.setEvento(new Evento());
        eventoDocumentoServico.getEvento().setIdEvento(370L);

        eventoDocumentoServico.setDoctoServico(new DoctoServico());
        eventoDocumentoServico.getDoctoServico().setIdDoctoServico(61819250L);

        eventoDocumentoServico.setIdEventoDocumentoServico(370L);
        eventoDocumentoServico.setDhEvento(new DateTime());
        eventoDocumentoServico.setDhInclusao(new DateTime());

        eventoDocumentoServico.setUsuario(new Usuario());
        eventoDocumentoServico.getUsuario().setIdUsuario(8070L);

        eventoDocumentoServico.setBlEventoCancelado(false);
        eventoDocumentoServico.setNrDocumento("FLN 00024299");
        eventoDocumentoServico.setObComplemento("POA - Porto A'le''''gre");

        eventoDocumentoServico.setTpDocumento(new DomainValue());
        eventoDocumentoServico.getTpDocumento().setValue("MAV");

        for (int i = 0; i < 600; i++) {
            EventoDocumentoServico eds = new EventoDocumentoServico();
            try {
                BeanUtils.copyProperties(eds, eventoDocumentoServico);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            eds.setIdEventoDocumentoServico((long) i);
            entidades.add(eds);
        }

        entidadesASeremInseridas = (ArrayList<Serializable>) ((ArrayList<Serializable>) entidades).clone();
    }

    @Test
    public void testProcessarEntidadesASeremInseridasSaidaPortaria() throws Exception {

        Class[] cArg = new Class[2];
        cArg[0] = StringBuilder.class;
        cArg[1] = EventoDocumentoServico.class;

        Method method = newSaidaChegadaDAO.getClass().getDeclaredMethod("adicionarSubqueryInsertEventoDocumentoServico", cArg);
        method.setAccessible(true);

        StringBuilder eventoDocumentoServicoSql = new StringBuilder();

        int eventoDocumentoServicoCount = 0;

        for (int i = 0; i < entidades.size(); i++) {
            if (entidades.get(i) instanceof EventoDocumentoServico) {
                entidadesASeremInseridas.remove(entidades.get(i));
                EventoDocumentoServico eventoDocumentoServico = (EventoDocumentoServico) entidades.get(i);
                if (eventoDocumentoServicoCount == 0) {
                    eventoDocumentoServicoSql.append(
                            "INSERT " +
                                    "INTO EVENTO_DOCUMENTO_SERVICO " +
                                    "(" + NewSaidaChegadaDAO.COLS_EVENTO_DOCUMENTO_SERVICO + ", ID_EVENTO_DOCUMENTO_SERVICO) " +
                                    "SELECT " +
                                    NewSaidaChegadaDAO.COLS_EVENTO_DOCUMENTO_SERVICO +
                                    ",EVENTO_DOCUMENTO_SERVICO_SQ.nextval AS ID_EVENTO_DOCUMENTO_SERVICO FROM (");
                }

                method.invoke(newSaidaChegadaDAO, eventoDocumentoServicoSql, eventoDocumentoServico);

                eventoDocumentoServicoCount++;
                if (eventoDocumentoServicoCount > 2) {
                    Assert.assertTrue(eventoDocumentoServicoSql.toString().contains("''"));

                    System.out.printf(eventoDocumentoServicoSql.toString());

                    eventoDocumentoServicoSql = new StringBuilder();
                    eventoDocumentoServicoCount = 0;
                } else {
                    eventoDocumentoServicoSql.append("UNION ALL ");
                }
            }
        }
    }


}