package com.mercurio.lms.sim.model.dao;

import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoNaturaDMN;
import com.mercurio.adsm.framework.model.*;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.LongUtils;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.*;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

/**
 * DAO pattern.
 * <p>
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 *
 * @spring.bean
 */
public class EventoDocumentoServicoDAO extends BaseCrudDao<EventoDocumentoServico, Long> {

    /**
     * Nome da classe que o DAO é responsável por persistir.
     */
    protected final Class<EventoDocumentoServico> getPersistentClass() {
        return EventoDocumentoServico.class;
    }

    public List<EventoDocumentoServico> findEventoDocumentoServicoComEntregaRealizada(Long idDoctoServico) {
        List param = new ArrayList();
        StringBuilder hql = new StringBuilder();

        hql.append("SELECT eds ");
        hql.append("FROM ");
        hql.append(EventoDocumentoServico.class.getSimpleName() + " eds ");
        hql.append("WHERE ");
        hql.append(" eds.blEventoCancelado = 'N' ");
        hql.append(" AND eds.doctoServico.idDoctoServico = ? ");
        hql.append(" AND eds.ocorrenciaEntrega.cdOcorrenciaEntrega = ? ");

        param.add(idDoctoServico);
        param.add(Short.valueOf("1"));

        return getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
    }

    public List<EventoDocumentoServico> findByEventoByDocumentoServico(Long idEvento, Long idDoctoServico) {
        DetachedCriteria dc = createDetachedCriteria();
        dc.add(Restrictions.eq("evento.idEvento", idEvento));
        dc.add(Restrictions.eq("doctoServico.idDoctoServico", idDoctoServico));
        dc.add(Restrictions.eq("blEventoCancelado", Boolean.FALSE));
        dc.addOrder(Order.desc("dhEvento"));

        return findByDetachedCriteria(dc);
    }

    public List<EventoDocumentoServico> findEventoDoctoServico(Long idDoctoServico, Short cdEvento, DomainValue tpEvento, Boolean blEventoCancelado) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "eds");
        dc.createAlias("eds.evento", "e");
        dc.add(Restrictions.eq("eds.doctoServico.idDoctoServico", idDoctoServico));
        dc.add(Restrictions.eq("eds.blEventoCancelado", blEventoCancelado));
        dc.add(Restrictions.eq("e.cdEvento", cdEvento));
        dc.add(Restrictions.eq("e.tpEvento", tpEvento));
        return findByDetachedCriteria(dc);
    }

    /*
     * LMS 3169 -- Retorna uma lista e eventoDocumentoServico
     * se estiver baixado como entregue e possuir algum codigo de entrega parcial
     */
    public List<EventoDocumentoServico> findEventoDoctoServicoComCodEntregaParcial(Long idDoctoServico, Short cdEvento, DomainValue tpEvento, Boolean blEventoCancelado) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "eds");
        dc.createAlias("eds.evento", "e");
        dc.createAlias("eds.doctoServico", "ds");
        dc.createAlias("eds.doctoServico.manifestoEntregaDocumentos", "med");
        dc.add(Restrictions.eq("eds.doctoServico.idDoctoServico", idDoctoServico));
        dc.add(Restrictions.eq("eds.blEventoCancelado", blEventoCancelado));
        dc.add(Restrictions.eq("e.cdEvento", cdEvento));
        dc.add(Restrictions.eq("e.tpEvento", tpEvento));
        dc.add(Restrictions.isNotNull("med.tpEntregaParcial"));
        return findByDetachedCriteria(dc);
    }

    /**
     * Procura evento(s) no doctoServico informado que seja igual ao código do
     * evento passado por parâmetro.
     *
     * @param idDoctoServico
     * @param cdEvento
     * @return
     */
    public List<EventoDocumentoServico> findEventoDoctoServico(Long idDoctoServico, Short cdEvento) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "eds");
        dc.createAlias("eds.evento", "e");
        dc.add(Restrictions.eq("eds.doctoServico.idDoctoServico", idDoctoServico));
        dc.add(Restrictions.eq("eds.blEventoCancelado", false));
        dc.add(Restrictions.eq("e.cdEvento", cdEvento));
        return findByDetachedCriteria(dc);
    }

    public EventoDocumentoServico findEventoDoctoServicoFilial(Long idDoctoServico, Short cdEvento, Long idFilialEvento) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "eds");

        dc.createAlias("eds.evento", "e");
        dc.add(Restrictions.eq("eds.doctoServico.idDoctoServico", idDoctoServico));
        dc.add(Restrictions.eq("eds.blEventoCancelado", false));
        dc.add(Restrictions.eq("eds.filial.id", idFilialEvento));
        dc.add(Restrictions.eq("e.cdEvento", cdEvento));
        dc.addOrder(Order.desc("eds.id"));

        List<EventoDocumentoServico> eventoDocumentoServicos = findByDetachedCriteria(dc);

        if (CollectionUtils.isEmpty(eventoDocumentoServicos)) {
            return null;
        }

        return eventoDocumentoServicos.get(0);
    }

    /**
     * Procura evento(s) no Monitoramento informado que seja igual ao código do
     * evento passado por parâmetro.
     *
     * @param idMonitoramentoDescarga
     * @param cdEvento
     * @return
     */
    public List<EventoDocumentoServico> findEventoDoctoServicoByMonitoramento(Long idMonitoramentoDescarga, Short cdEvento) {
        DetachedCriteria dcConhecimento = DetachedCriteria.forClass(NotaFiscalConhecimento.class, "nfc");
        dcConhecimento.setProjection(Projections.distinct(Projections.property("nfc.conhecimento.id")));
        dcConhecimento.createAlias("nfc.conhecimento", "c");
        dcConhecimento.createAlias("nfc.volumeNotaFiscais", "vnfl");
        dcConhecimento.add(Restrictions.eq("vnfl.monitoramentoDescarga.id", idMonitoramentoDescarga));

        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "eds");
        dc.createAlias("eds.evento", "e");
        dc.add(Subqueries.propertyIn("eds.doctoServico.id", dcConhecimento));
        dc.add(Restrictions.eq("eds.blEventoCancelado", Boolean.FALSE));
        dc.add(Restrictions.eq("e.cdEvento", cdEvento));
        return findByDetachedCriteria(dc);
    }

    /**
     * Procura evento(s) no doctoServico informado que seja igual ao código e a
     * filial do evento informado. passado por parâmetro.
     *
     * @param idDoctoServico
     * @param cdEvento
     * @return
     */
    public List<EventoDocumentoServico> findEventoDoctoServico(Long idDoctoServico, Long idFilialEvento, Short[] cdEvento, boolean somenteNaoCancelados, DateTime dhInicio, DateTime dhFim) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "eds");
        dc.createAlias("eds.evento", "e");
        dc.add(Restrictions.eq("eds.doctoServico.idDoctoServico", idDoctoServico));
        dc.add(Restrictions.eq("eds.filial.id", idFilialEvento));
        dc.add(Restrictions.in("e.cdEvento", cdEvento));

        if (somenteNaoCancelados) {
            dc.add(Restrictions.eq("eds.blEventoCancelado", Boolean.FALSE));
        }

        if (dhInicio != null && dhFim != null) {
            dc.add(Restrictions.ge("eds.dhInclusao", dhInicio));
            dc.add(Restrictions.le("eds.dhInclusao", dhFim));
        }

        return findByDetachedCriteria(dc);
    }

    public List<Long> findIdsByIdDoctoServico(Long idDoctoServico) {
        String query = "select pojo.idEventoDocumentoServico "
                + "from " + EventoDocumentoServico.class.getName() + " as pojo "
                + "join pojo.doctoServico as ds "
                + "where ds.idDoctoServico = :idDoctoServico ";

        return getAdsmHibernateTemplate().findByNamedParam(query, "idDoctoServico", idDoctoServico);
    }

    public EventoDocumentoServico findUltimoEventoDoctoServico(Long idDoctoServico, String tpEvento, Boolean blEventoCancelado) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "edsa");
        dc.createAlias("edsa.evento", "ea");
        dc.add(Restrictions.eq("edsa.doctoServico.id", idDoctoServico));
        dc.add(Restrictions.eq("edsa.blEventoCancelado", blEventoCancelado));
        dc.add(Restrictions.eq("ea.tpEvento", tpEvento));
        dc.add(Restrictions.isNull("ea.cancelaEvento"));
        dc.addOrder(Order.desc("edsa.dhEvento"));

        List list = getAdsmHibernateTemplate().findByCriteria(dc);

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        return (EventoDocumentoServico) list.get(0);
    }

    public void removeByIdDoctoServico(Serializable idDoctoServico) {
        String query = "delete from " + EventoDocumentoServico.class.getName() + " as eds where eds.doctoServico.id = :id";
        getAdsmHibernateTemplate().removeById(query, idDoctoServico);
    }

    /**
     * Método que retorna a maior dhEvento da tabela de EventoDocumentoServico
     * com o ID do DoctoServico e IDs de LocalizacaoMercadoria
     *
     * @param idDoctoServico Long
     * @param idsLocalizacaoMercadoria List
     * @return
     */
    public DateTime findMaiorDhEventoByIdDoctoServicoByIdsLocalizacaoMercadoria(Long idDoctoServico, List<Long> idsLocalizacaoMercadoria) {
        StringBuilder hql = new StringBuilder();

        hql.append(" select max(eds.dhEvento.value)\n");
        hql.append("   from " + EventoDocumentoServico.class.getName() + " as eds\n");
        hql.append("   join eds.evento as evn \n");
        hql.append("  where eds.doctoServico.id = :idDoctoServico \n");
        hql.append("    and evn.localizacaoMercadoria.id in (:idsLocalizacaoMercadoria) \n");

        Query query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
        query.setParameter("idDoctoServico", idDoctoServico);
        query.setParameterList("idsLocalizacaoMercadoria", idsLocalizacaoMercadoria);

        return (DateTime) query.uniqueResult();
    }

    /**
     * Método que busca todos os eventos do documento de serviço não cancelados
     * cujo evento associado possua localização associada
     *
     * @param idDocumentoServico Long
     * @return
     * @author Andresa Vargas
     */
    public List<EventoDocumentoServico> findEventosDoctoServicoNaoCancelados(Long idDocumentoServico) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "eds")
                .createAlias("eds.doctoServico", "dcto")
                .createAlias("eds.evento", "e")
                .add(Restrictions.eq("eds.blEventoCancelado", Boolean.FALSE))
                .add(Restrictions.eq("dcto.idDoctoServico", idDocumentoServico))
                .add(Restrictions.isNotNull("e.localizacaoMercadoria.idLocalizacaoMercadoria"));
        dc.addOrder(Order.desc("eds.dhEvento.value"));
        dc.addOrder(Order.desc("eds.id"));
        return findByDetachedCriteria(dc);
    }

    /**
     * find para buscar o Evento do Documento de Serviço conforme os critérios
     * enviados
     *
     * @param nrDocumento
     * @param cdEvento
     * @param idFilial
     * @param dhEvento
     * @return EventoDocumentoServico
     * @author Andresa Vargas
     */
    public EventoDocumentoServico findEventoDocumentoServico(String nrDocumento, Short cdEvento, Long idFilial, DateTime dhEvento) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "eds")
                .createAlias("eds.doctoServico", "dcto")
                .createAlias("eds.evento", "e")
                .createAlias("eds.filial", "f")
                .add(Restrictions.eq("eds.nrDocumento", nrDocumento))
                .add(Restrictions.eq("e.cdEvento", cdEvento))
                .add(Restrictions.eq("f.idFilial", idFilial))
                .add(Restrictions.eq("eds.dhEvento.value", dhEvento));

        return (EventoDocumentoServico) getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    /**
     * Retorna a lista de eventos não cancelados do documento informado
     * filtrando por o código do evento
     *
     * @param idDoctoServico
     * @param cdEvento
     * @return
     * @author Mickaël Jalbert
     * @since 21/02/2007
     */
    public List<EventoDocumentoServico> findEventoDoctoServico(Long idDoctoServico, Short[] cdEvento) {
        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("e");
        hql.addFrom(EventoDocumentoServico.class.getName(), "e");
        hql.addCriteria("e.doctoServico.id", "=", idDoctoServico);
        hql.addCriteriaIn("e.evento.cdEvento", cdEvento);
        hql.addCriteria("e.blEventoCancelado", "=", Boolean.FALSE);

        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

    public Integer getRowCountEventoDoctoServicoReentrega(Long idDoctoServico) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "eds")
                .setProjection(Projections.rowCount())
                .createAlias("eds.ocorrenciaEntrega", "oe")
                .createAlias("eds.evento", "e")
                .add(Restrictions.eq("eds.doctoServico.id", idDoctoServico))
                .add(Restrictions.eq("eds.blEventoCancelado", Boolean.FALSE))
                .add(Restrictions.eq("oe.blOcasionadoMercurio", Boolean.FALSE))
                .add(Restrictions.eq("e.tpEvento", "R"));

        return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
    }

    /**
     * @param idDoctoServico Long
     * @param idOcorrenciaBloqueio Long
     * @return
     */
    public Boolean validateExisteEventoDoctoServicoMaiorEventoBloqueio(Long idDoctoServico, Long idOcorrenciaBloqueio) {
        StringBuilder query = new StringBuilder()
                .append("from ")
                .append(EventoDocumentoServico.class.getName()).append(" as eds ")
                .append("inner join eds.doctoServico as ds ")
                .append("inner join ds.localizacaoMercadoria as lm ")
                .append("inner join eds.evento as evento ")
                .append("where ")
                .append("ds.id = ? ")
                .append("and evento.cdEvento not in (48, 49, 53, 54, 19, 20, 113, 114) ")
                .append("and eds.dhInclusao.value > ")
                .append("(select op.dhLiberacao.value ")
                .append("from ")
                .append(OcorrenciaDoctoServico.class.getName()).append(" as op ")
                .append("where ")
                .append("op.doctoServico.id = ? ")
                .append("and op.id = ?) ");

        List<Long> param = new ArrayList<Long>();
        param.add(idDoctoServico);
        param.add(idDoctoServico);
        param.add(idOcorrenciaBloqueio);

        Integer qtdRows = getAdsmHibernateTemplate().getRowCountForQuery(query.toString(), param.toArray());
        return CompareUtils.gt(qtdRows, IntegerUtils.ZERO);
    }

    public EventoDocumentoServico findEventoDoctoServicoByLastDhEventoByIdDoctoServico(Long idDoctoServico, Boolean blOrigemCancelamentoRIM) {
        StringBuilder hql = new StringBuilder()
                .append(" select eds ")
                .append("from ").append(EventoDocumentoServico.class.getName()).append(" as eds ")
                .append("inner join fetch eds.evento as evento ")
                .append("where eds.doctoServico.id = ? ");

        if (blOrigemCancelamentoRIM != null && blOrigemCancelamentoRIM) {
            hql.append("and evento.cdEvento in (35, 132) ");
        }

        hql.append("order by eds.dhEvento.value desc ");

        List<EventoDocumentoServico> lista = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idDoctoServico});

        if (CollectionUtils.isEmpty(lista)) {
            return null;
        }

        return lista.get(0);
    }

    public EventoDocumentoServico findLastEventoNaoCanceladoByCd(Long idDoctoServico, short cdEvento) {
        StringBuilder hql = new StringBuilder()
                .append(" select eds ")
                .append("from ").append(EventoDocumentoServico.class.getName()).append(" as eds ")
                .append("inner join fetch eds.evento as evento ")
                .append("where eds.doctoServico.id = ? ")
                .append("and evento.cdEvento = ? ")
                .append("and eds.blEventoCancelado = 'N' ")
                .append("order by eds.dhEvento.value desc ");

        List<EventoDocumentoServico> lista = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idDoctoServico, cdEvento});

        if (CollectionUtils.isEmpty(lista)) {
            return null;
        }

        return lista.get(0);
    }

    /**
     * Método utilizado pela Integração. Busca Ultimo Evento do DoctoServico
     *
     * @param idDoctoServico
     * @author Andre Valadas
     */
    public EventoDocumentoServico findUltimoEventoDocumenttoServico(Long idDoctoServico, String tpEvento, Boolean blEventoCancelado) {
        //Busca data do último evento
        DetachedCriteria dcMax = DetachedCriteria.forClass(getPersistentClass(), "edsb");
        dcMax.setProjection(Projections.max("edsb.dhEvento.value"));
        dcMax.createAlias("edsb.evento", "eb");
        dcMax.add(Restrictions.eqProperty("edsb.doctoServico.id", "edsa.doctoServico.id"));
        dcMax.add(Restrictions.eqProperty("edsb.blEventoCancelado", "edsa.blEventoCancelado"));
        dcMax.add(Restrictions.eqProperty("eb.tpEvento", "ea.tpEvento"));

        //Busca EventoDoctoServico
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "edsa");
        dc.createAlias("edsa.evento", "ea");
        dc.add(Restrictions.eq("edsa.doctoServico.id", idDoctoServico));
        dc.add(Restrictions.eq("edsa.blEventoCancelado", blEventoCancelado));
        dc.add(Restrictions.eq("ea.tpEvento", tpEvento));
        dc.add(Subqueries.propertyEq("edsa.dhEvento.value", dcMax));

        List eventoList = getAdsmHibernateTemplate().findByCriteria(dc);

        if (CollectionUtils.isEmpty(eventoList)) {
            return null;
        }

        return (EventoDocumentoServico) eventoList.get(0);
    }

    public ResultSetPage<EventoDocumentoServico> findPaginatedByIdDocumento(Long id, FindDefinition findDefinition) {
        List<Object> param = new ArrayList();
        param.add(id);

        StringBuilder query = new StringBuilder()
                .append("from ")
                .append(EventoDocumentoServico.class.getName()).append(" as evtDoc ")
                .append("left join fetch evtDoc.evento as evt ")
                .append("left join fetch evt.descricaoEvento as dsEvt ")
                .append("left join fetch evtDoc.usuario as usu ")
                .append("left join fetch evtDoc.filial as fil ")
                .append("where ")
                .append("evtDoc.doctoServico.idDoctoServico = ? ")
                .append("order by evtDoc.dhEvento.value desc ");

        return getAdsmHibernateTemplate().findPaginated(
                query.toString(), query.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
    }

    public List<EventoDocumentoServico> findByDocumentoServico(Long idDoctoServico) {
        return findByDocumentoServico(idDoctoServico, false, true);
    }

    /**
     * fachada criada para nao precisar duplicar o metodo
     * findByDocumentoServico(Long idDoctoServico), pois para o projeto tracking
     * precisamos que as recusas (que não tem localizaçãoMercadoria sejam
     * retornadas) Se innerJoinLocalizacaoMercadoria é true então faz inner
     * join, senão faz left join
     *
     * @param idDoctoServico
     * @param innerJoinLocalizacaoMercadoria
     * @return List<EventoDocumentoServico>
     */
    public List<EventoDocumentoServico> findByDocumentoServico(Long idDoctoServico, Boolean innerJoinLocalizacaoMercadoria, Boolean orderByDhEventoAsc) {
        StringBuilder hql = new StringBuilder()
                .append("select eventoDocumentoServico ")
                .append("from " + this.getPersistentClass().getName() + " as eventoDocumentoServico ")
                .append("inner join fetch eventoDocumentoServico.evento as evento ")
                .append("inner join fetch eventoDocumentoServico.filial as filial ")
                .append("inner join fetch filial.pessoa as pessoa ")
                .append("inner join fetch evento.descricaoEvento as descricaoEvento ");

        if (innerJoinLocalizacaoMercadoria) {
            hql.append("inner join fetch evento.localizacaoMercadoria as localizacaoMercadoria ");
        } else {
            hql.append("left join fetch evento.localizacaoMercadoria as localizacaoMercadoria ");
        }

        hql.append("left join fetch eventoDocumentoServico.ocorrenciaEntrega as ocorrenciaEntrega ")
                .append("left join fetch eventoDocumentoServico.ocorrenciaPendencia as ocorrenciaPendencia ")
                .append("where eventoDocumentoServico.doctoServico.id = :idDoctoServico ")
                .append("and eventoDocumentoServico.blEventoCancelado = :blEventoCancelado ");

        if (orderByDhEventoAsc) {
            hql.append("order by eventoDocumentoServico.dhEvento.value ");
        } else {
            hql.append("order by eventoDocumentoServico.dhEvento.value desc ");
        }

        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), new String[]{"idDoctoServico", "blEventoCancelado"}, new Object[]{idDoctoServico, Boolean.FALSE});
    }

    /**
     * Responsável por consultar os Enventos de baixa dos conhecimentos , que
     * possuem assiantura digitalizada, porem não foi enviada.
     *
     * @param idsCliente id dos clientes que devem ser considerados.
     * @return lista de Eventos
     */
    public List<EventoDocumentoServico> findEventosBaixadosComAssNaoEnviadas(List<Long> idsCliente) {
        Map<String, Object> parametersValues = new HashMap<String, Object>();

        parametersValues.put("idsCliente", idsCliente);
        parametersValues.put("blEnviado", Boolean.FALSE);
        parametersValues.put("blEventoCancelado", Boolean.FALSE);

        StringBuilder hql = new StringBuilder();

        hql.append("select eventoDocumentoServico ");
        hql.append("from " + this.getPersistentClass().getName() + " as eventoDocumentoServico ");
        hql.append("inner join fetch eventoDocumentoServico.doctoServico as docto ");
        hql.append("inner join fetch docto.filialByIdFilialOrigem as filialOrigem ");
        hql.append("inner join fetch eventoDocumentoServico.evento as evento ");
        hql.append("left  join fetch eventoDocumentoServico.ocorrenciaEntrega as ocorrenciaEntrega ");
        hql.append("left  join fetch eventoDocumentoServico.ocorrenciaPendencia as ocorrenciaPendencia ");
        hql.append("left  join fetch eventoDocumentoServico.filial as filial ");
        hql.append("inner join fetch evento.descricaoEvento as descricaoEvento ");
        hql.append("left join fetch evento.localizacaoMercadoria as localizacaoMercadoria ");
        hql.append("inner join docto.devedorDocServs dds ");
        hql.append("inner join dds.cliente cliente , ");
        hql.append("com.mercurio.lms.entrega.model.ComprovanteEntrega as comprovante ");

        hql.append("where comprovante.idDoctoServico = docto.idDoctoServico ");
        hql.append("and cliente.id in (:idsCliente) ");
        hql.append("and comprovante.blEnviado = :blEnviado ");
        hql.append("and comprovante.assinatura is not null ");
        hql.append("and (comprovante.nrTentativaEnvio is null or comprovante.nrTentativaEnvio < 5) ");
        hql.append("and evento.cdEvento = 21 ");
        hql.append("and eventoDocumentoServico.blEventoCancelado = :blEventoCancelado ");

        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parametersValues);
    }

    /**
     * Método responsável por retornar a menor dhEvento do
     * EventoDocumentoServico relacionado ao DoctoServico e Evento com os ids
     * passados como parametros
     *
     * @param idEvento
     * @param idDoctoServico
     * @return DateTime dhEvento
     */
    public DateTime findMenorDhEventoByIdEventoEIdDoctoServico(Long idEvento, Long idDoctoServico) {
        StringBuilder hql = new StringBuilder();

        SqlTemplate query = new SqlTemplate();
        hql.append(EventoDocumentoServico.class.getName()).append(" as eds ");
        hql.append("   join eds.evento as evn ");

        query.addFrom(hql.toString());
        query.addProjection("TRUNC(min(eds.dhEvento.value))");

        query.addCriteria("eds.doctoServico.idDoctoServico", "=", idDoctoServico);
        query.addCriteria("eds.evento.idEvento", "=", idEvento);
        query.addCriteria("eds.blEventoCancelado", "=", Boolean.FALSE);

        return JodaTimeUtils.toDateTime(getAdsmHibernateTemplate(), getAdsmHibernateTemplate().findUniqueResult(query.getSql(), query.getCriteria()));
    }

    @SuppressWarnings("unchecked")
    public List<EventoDocumentoServico> findByDoctoServico(Long idDoctoServico, Short... cdEvento) {
        StringBuilder sb = new StringBuilder();

        sb.append("from " + getPersistentClass().getName() + " evdo ");
        sb.append("inner join fetch evdo.evento even ");
        sb.append("inner join fetch evdo.doctoServico doct ");
        sb.append("where doct.idDoctoServico = ? ");

        Object[] params;

        if (cdEvento == null) {
            params = new Object[]{idDoctoServico};
        } else {
            params = new Object[1 + cdEvento.length];
            params[0] = idDoctoServico;
            sb.append("and even.cdEvento in (");
            for (int i = 0; i < cdEvento.length; i++) {
                params[i + 1] = cdEvento[i];
                if (i == 0) {
                    sb.append("?");
                } else {
                    sb.append(",?");
                }

            }
            sb.append(") ");
        }

        return getAdsmHibernateTemplate().find(sb.toString(), params);
    }

    @SuppressWarnings("unchecked")
    public EventoDocumentoServico findFirstEventoByDoctoServicoAfterDtInicio(Long idDoctoServico, DateTime dtInicio, Short... cdEvento) {
        List<EventoDocumentoServico> eventos = this.findByDoctoServico(idDoctoServico, cdEvento);

        if (CollectionUtils.isEmpty(eventos)) {
            return null;
        }

        EventoDocumentoServico evento = null;

        for (EventoDocumentoServico e : eventos) {
            // Somente eventos com dh_evento maior ou igual ao ínicio serão considerados
            // Resgata o evento mais próximo do início
            if (e.getDhEvento().compareTo(dtInicio) >= 0 && (evento == null || e.getDhEvento().isBefore(evento.getDhEvento()))) {
                evento = e;
            }
        }

        return evento;
    }

    public Object[] findInfoEventoAtualDoctoByIdDoctoServico(Long idDoctoServico) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("idDoctoServico", idDoctoServico);

        StringBuilder sql = new StringBuilder();

        sql.append("select doc.id_docto_servico as id, fo.sg_filial || '-' || doc.nr_docto_servico as ctrc,")
                .append(" case")
                .append(" when e.id_localizacao_mercadoria is not null then upper(ds_localizacao_mercadoria_i)")
                .append(" else upper(de.ds_descricao_evento_i)")
                .append(" end ||")
                .append(" ' ' ||")
                .append(" case")
                .append(" when eds.ob_complemento is not null then upper(eds.ob_complemento)")
                .append(" else upper(p.nm_fantasia)")
                .append(" end ||")
                .append(" ' EM ' ||")
                .append(" to_char(eds.dh_evento,'dd/mm/yyyy hh24:mi') as evento,")
                .append(" fo.id_filial as id_filial,")
                .append(" to_char(eds.dh_evento,'dd/mm/yyyy hh24:mi') as data_evento,")
                .append(" to_char(c.dt_coleta,'dd/mm/yyyy') as data_coleta,")
                .append(" case when eds.id_ocorrencia_entrega is not null")
                .append(" then oe.ds_ocorrencia_entrega_i")
                .append(" else")
                .append(" case when eds.id_ocorrencia_pendencia is not null")
                .append(" then op.ds_ocorrencia_i")
                .append(" else ''")
                .append(" end")
                .append(" end as detalhe,")
                .append(" to_char(doc.dt_prev_entrega,'dd/mm/yyyy') as previsao,")
                .append(" eds.id_evento_documento_servico,")
                .append(" mde.id_monitoramento_doc_eletronic as id_monitoramento_doc,")
                .append(" to_char(doc.dh_emissao,'dd/mm/yyyy hh24:mi') as data_emissao,")
                .append(" md.nm_municipio || '-' || fd.sg_filial as destino,")
                .append(" to_char(eds1.dh_evento, 'dd/mm/yyyy hh24:mi') as finalizacao ")
                .append(" from docto_servico doc,")
                .append(" filial fo,")
                .append(" filial fd,")
                .append(" evento_documento_servico eds,")
                .append(" evento_documento_servico eds1,")
                .append(" evento e,")
                .append(" localizacao_mercadoria lm,")
                .append(" descricao_evento de,")
                .append(" filial f1,")
                .append(" pessoa p,")
                .append(" pessoa pd,")
                .append(" ocorrencia_entrega oe,")
                .append(" ocorrencia_pendencia op,")
                .append(" monitoramento_doc_eletronico mde,")
                .append(" conhecimento c,")
                .append(" endereco_pessoa epd,")
                .append(" municipio md")
                .append(" where doc.id_docto_servico = :idDoctoServico")
                .append(" and c.id_conhecimento (+) = doc.id_docto_servico")
                .append(" and fo.id_filial = doc.id_filial_origem")
                .append(" and fd.id_filial = doc.id_filial_destino")
                .append(" and eds.id_docto_servico = doc.id_docto_servico")
                .append(" and eds.id_evento_documento_servico = (select max(eds1.id_evento_documento_servico) from evento_documento_servico eds1, evento e1")
                .append(" where eds1.id_docto_servico = doc.id_docto_servico")
                .append(" and eds1.bl_evento_cancelado = 'N'")
                .append(" and eds1.id_evento <> 541")
                .append(" and e1.id_evento = eds1.id_evento")
                .append(" and e1.bl_exibe_cliente = 'S')")
                .append(" and e.id_evento = eds.id_evento")
                .append(" and lm.id_localizacao_mercadoria (+) = e.id_localizacao_mercadoria")
                .append(" and de.id_descricao_evento = e.id_descricao_evento")
                .append(" and f1.id_filial = eds.id_filial")
                .append(" and p.id_pessoa = f1.id_filial")
                .append(" and oe.id_ocorrencia_entrega (+) = eds.id_ocorrencia_entrega")
                .append(" and op.id_ocorrencia_pendencia (+) = eds.id_ocorrencia_pendencia")
                .append(" and mde.tp_situacao_documento(+)='A'")
                .append(" and mde.id_docto_servico(+)= doc.id_docto_servico")
                .append(" and mde.ds_dados_documento(+) is not null")
                .append(" and eds1.id_docto_servico (+) = doc.id_docto_servico")
                .append(" and eds1.bl_evento_cancelado (+) = 'N'")
                .append(" and eds1.id_ocorrencia_entrega (+) = 5")
                .append(" and eds1.id_evento (+) = 348")
                .append(" and doc.id_cliente_destinatario = pd.id_pessoa")
                .append(" and pd.id_pessoa = epd.id_pessoa")
                .append(" and epd.id_municipio = md.id_municipio")
                .append(" order by eds.dh_evento");

        ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("id", Hibernate.LONG);
                sqlQuery.addScalar("ctrc", Hibernate.STRING);
                sqlQuery.addScalar("evento", Hibernate.STRING);
                sqlQuery.addScalar("previsao", Hibernate.STRING);
                sqlQuery.addScalar("detalhe", Hibernate.STRING);
                sqlQuery.addScalar("id_monitoramento_doc", Hibernate.LONG);
                sqlQuery.addScalar("data_coleta", Hibernate.STRING);
                sqlQuery.addScalar("data_emissao", Hibernate.STRING);
                sqlQuery.addScalar("destino", Hibernate.STRING);
                sqlQuery.addScalar("finalizacao", Hibernate.STRING);
            }
        };

        List<Object[]> listObj = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);

        if (CollectionUtils.isEmpty(listObj)) {
            return null;
        }

        return listObj.get(0);
    }

    public EventoDocumentoServico findEventosEspecificosByIdDoctoServicoAndCdEventoAndIdFilial(Long idDoctoServico, Short cdEvento, Long idFilial) {
        Map<String, Object> parametersValues = new HashMap<String, Object>();
        parametersValues.put("blEventoCancelado", Boolean.FALSE);
        parametersValues.put("idDoctoServico", idDoctoServico);

        StringBuilder query = new StringBuilder();
        query.append("SELECT eds ")
                .append("FROM ")
                .append(EventoDocumentoServico.class.getSimpleName()).append(" eds ")
                .append("JOIN eds.evento e ")
                .append("JOIN eds.doctoServico ds ")
                .append("JOIN eds.filial f ")
                .append("WHERE ")
                .append("	 eds.blEventoCancelado =:blEventoCancelado ")
                .append("AND ds.idDoctoServico =:idDoctoServico ");

        if (LongUtils.hasValue(idFilial)) {
            query.append("AND f.idFilial =:idFilial ");
            parametersValues.put("idFilial", idFilial);
        }

        if (cdEvento != null) {
            query.append("AND e.cdEvento =:cdEvento ");
            parametersValues.put("cdEvento", cdEvento);
        }

        List<EventoDocumentoServico> l = getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametersValues);

        if (CollectionUtils.isEmpty(l)) {
            return null;
        }

        return l.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findAllEventosByIdDoctoServico(Long idDoctoServico) {
        StringBuilder hql = new StringBuilder();

        hql
                .append("SELECT ")
                .append("new Map( ")
                .append("to_char(eds.dhEvento.value, 'dd/MM/yyyy hh24:mi') as dhEvento, ")
                .append("lm.dsLocalizacaoMercadoria as dsLocalizacaoMercadoria, ")
                .append("de.dsDescricaoEvento as dsDescricaoEvento, ")
                .append("eds.obComplemento as obComplemento, ")
                .append("f.sgFilial as sgFilial, ")
                .append("p.nmFantasia as nmFantasia, ")
                .append("e.cdEvento as cdEvento ")
                .append(") ")
                .append("FROM ")
                .append(EventoDocumentoServico.class.getSimpleName()).append(" eds ")
                .append("JOIN eds.doctoServico ds ")
                .append("JOIN eds.evento e ")
                .append("JOIN e.descricaoEvento de ")
                .append("LEFT JOIN e.localizacaoMercadoria lm ")
                .append("JOIN eds.filial f ")
                .append("JOIN f.pessoa p ")
                .append("WHERE eds.blEventoCancelado =:blEventoCancelado ")
                .append("AND e.blExibeCliente =:blExibeCliente ")
                .append("AND ds.idDoctoServico =:idDoctoServico ")
                .append("ORDER BY eds.dhEvento.value DESC ");

        Map<String, Object> parametersValues = new HashMap<String, Object>();
        parametersValues.put("blEventoCancelado", Boolean.FALSE);
        parametersValues.put("blExibeCliente", Boolean.TRUE);
        parametersValues.put("idDoctoServico", idDoctoServico);

        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parametersValues);
    }

    public List findDadosEvento(Long idEventoDocumentoServico) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idEventoDocumentoServico", idEventoDocumentoServico);

        StringBuilder queryEvento = new StringBuilder();
        queryEvento.append("SELECT eds.dh_evento as dhEvento, \n");
        queryEvento.append("  e.CD_EVENTO              AS cdEventoLms, \n");
        queryEvento.append("  de.DS_DESCRICAO_EVENTO_I AS dsEventoLms, \n");
        queryEvento.append("  eds.id_docto_servico AS idDoctoServico, \n");
        queryEvento.append("  ds.nr_docto_servico AS nrDoctoServico, \n");
        queryEvento.append("  f.sg_filial AS sgFilial, \n");
        queryEvento.append("  etoc.cd_ocorrencia_cliente as cdOcorrenciaCliente, \n");
        queryEvento.append("  etoc.ds_ocorrencia AS dsOcorrenciaCliente, \n");
        queryEvento.append("  tomador.NR_IDENTIFICACAO nrIdentificacaoTomador, \n");
        queryEvento.append("  tomador.ID_PESSOA AS idClienteTomador\n");
        queryEvento.append("FROM evento_documento_servico eds, \n");
        queryEvento.append("  evento e, \n");
        queryEvento.append("  descricao_evento de, \n");
        queryEvento.append("  docto_servico ds, \n");
        queryEvento.append("  filial f, \n");
        queryEvento.append("  edi_tabela_ocoren_det etoc, \n");
        queryEvento.append("  edi_tabela_ocoren_cliente etocli, \n");
        queryEvento.append("  pessoa tomador \n");
        queryEvento.append("WHERE eds.id_evento_documento_servico = :idEventoDocumentoServico \n");
        queryEvento.append("AND e.id_evento               = eds.id_evento \n");
        queryEvento.append("AND de.id_descricao_evento    = e.id_descricao_evento \n");
        queryEvento.append("AND ((etoc.tp_ocorrencia      = '1' \n");
        queryEvento.append("AND eds.ID_OCORRENCIA_ENTREGA = etoc.id_lms) \n");
        queryEvento.append("OR (etoc.tp_ocorrencia        = '3' \n");
        queryEvento.append("AND eds.id_evento             = etoc.id_lms)) \n");
        queryEvento.append("AND eds.id_docto_servico      = ds.id_docto_servico \n");
        queryEvento.append("AND ds.id_filial_origem      = f.id_filial \n");
        queryEvento.append("AND etoc.ID_EDI_TABELA_OCOREN = etocli.ID_EDI_TABELA_OCOREN \n");
        queryEvento.append("AND etocli.ID_CLIENTE         = tomador.id_pessoa \n");
        queryEvento.append("AND etocli.ID_CLIENTE        IN \n");
        queryEvento.append("  (SELECT MAX(id_cliente) \n");
        queryEvento.append("  FROM devedor_doc_serv_fat dds \n");
        queryEvento.append("  WHERE dds.id_docto_servico = eds.id_docto_servico \n");
        queryEvento.append("  ) ");

        return getAdsmHibernateTemplate().findBySql(queryEvento.toString(), parameters, new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("dhEvento", Hibernate.TIMESTAMP);
                query.addScalar("cdEventoLms", Hibernate.SHORT);
                query.addScalar("dsEventoLms", Hibernate.STRING);
                query.addScalar("idDoctoServico", Hibernate.LONG);
                query.addScalar("nrDoctoServico", Hibernate.LONG);
                query.addScalar("sgFilial", Hibernate.STRING);
                query.addScalar("cdOcorrenciaCliente", Hibernate.SHORT);
                query.addScalar("dsOcorrenciaCliente", Hibernate.STRING);
                query.addScalar("nrIdentificacaoTomador", Hibernate.STRING);
                query.addScalar("idClienteTomador", Hibernate.LONG);

            }
        }, AliasToNestedMapResultTransformer.getInstance());
    }

    public String findCdOcorrenciaClienteEventoDoctoServicoEdi(Long idDocumentoServico, Long idEventoDocumentoServico) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("idDocumentoServico", idDocumentoServico);

        StringBuilder queryEvento = construirSqlParaRetornarCdOcorrenciaCliente();

        if (idEventoDocumentoServico != null) {
            parameters.put("idEventoDocumentoServico", idEventoDocumentoServico);
            queryEvento.append("AND EDS.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDocumentoServico ");
        }

        List queryResult = retornarQueryResult(parameters, queryEvento);

        if (CollectionUtils.isEmpty(queryResult)) {
            return null;
        }

        return (String) queryResult.get(0);
    }

    public String findCdOcorrenciaClienteEventoDoctoServicoEdi(Long idDocumentoServico, DateTime dhEvento) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("idDocumentoServico", idDocumentoServico);

        StringBuilder queryEvento = construirSqlParaRetornarCdOcorrenciaCliente();

        if (dhEvento != null) {
            parameters.put("dhEvento", dhEvento);
            queryEvento.append("AND EDS.DH_EVENTO = :dhEvento ");
        }

        List queryResult = retornarQueryResult(parameters, queryEvento);

        if (CollectionUtils.isEmpty(queryResult)) {
            return null;
        }

        return (String) queryResult.get(0);
    }

    private StringBuilder construirSqlParaRetornarCdOcorrenciaCliente() {
        StringBuilder queryEvento = new StringBuilder();

        queryEvento.append("SELECT \n");
        queryEvento.append("ETCD.CD_OCORRENCIA_CLIENTE \n");
        queryEvento.append("FROM \n");
        queryEvento.append("DEVEDOR_DOC_SERV DDS INNER JOIN EVENTO_DOCUMENTO_SERVICO EDS \n");
        queryEvento.append("ON DDS.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO \n");
        queryEvento.append("INNER JOIN DOCTO_SERVICO DS \n");
        queryEvento.append("ON (DS.ID_DOCTO_SERVICO = DDS.ID_DOCTO_SERVICO AND DS.ID_FILIAL_ORIGEM NOT IN (3829766, 651, 910)) \n");
        queryEvento.append("INNER JOIN EVENTO E \n");
        queryEvento.append("ON EDS.ID_EVENTO = E.ID_EVENTO \n");
        queryEvento.append("INNER JOIN EDI_TABELA_OCOREN_DET ETCD ON (ETCD.ID_EDI_TABELA_OCOREN = (SELECT ETC.ID_EDI_TABELA_OCOREN \n");
        queryEvento.append("FROM EDI_TABELA_CLIENTE ETC \n");
        queryEvento.append("WHERE ETC.ID_CLIENTE = DDS.ID_CLIENTE) \n");
        queryEvento.append("AND ((ETCD.ID_LMS = EDS.ID_OCORRENCIA_ENTREGA AND ETCD.TP_OCORRENCIA = 1) \n");
        queryEvento.append("OR \n");
        queryEvento.append("(ETCD.ID_LMS = EDS.ID_OCORRENCIA_PENDENCIA AND ETCD.TP_OCORRENCIA = 2) \n");
        queryEvento.append("OR (ETCD.ID_LMS = E.ID_LOCALIZACAO_MERCADORIA AND ETCD.TP_OCORRENCIA = 3 AND \n");
        queryEvento.append("ETCD.ID_LMS <> 601) \n");
        queryEvento.append("OR (ETCD.ID_LMS = EDS.ID_EVENTO AND ETCD.TP_OCORRENCIA = 3 AND \n");
        queryEvento.append("ETCD.ID_LMS = 601 AND EDS.ID_FILIAL = DS.ID_FILIAL_DESTINO))) \n");
        queryEvento.append("INNER JOIN DESCRICAO_EVENTO DE \n");
        queryEvento.append("ON E.ID_DESCRICAO_EVENTO = DE.ID_DESCRICAO_EVENTO \n");
        queryEvento.append("WHERE \n");
        queryEvento.append("EDS.ID_DOCTO_SERVICO = :idDocumentoServico ");
        return queryEvento;
    }

    private List retornarQueryResult(Map<String, Object> parameters,
                                     StringBuilder queryEvento) {
        List queryResult = getAdsmHibernateTemplate().findBySql(queryEvento.toString(), parameters, new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("CD_OCORRENCIA_CLIENTE", Hibernate.STRING);
            }
        });
        return queryResult;
    }

    public EventoDocumentoServicoNaturaDMN doComprovanteEntregaNatura(EventoDocumentoServicoNaturaDMN eventoDocumentoServicoNaturaDMN, List<Long> idsCliente) {
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("idDoctoService", eventoDocumentoServicoNaturaDMN.getIdDoctoServico());
        parameters.put("idNotaFiscalConhecimento", eventoDocumentoServicoNaturaDMN.getIdNotaFiscalConhecimento());
        parameters.put("idsCliente", idsCliente);

        StringBuilder queryEvento = new StringBuilder();

        queryEvento.append("  SELECT  \n");
        queryEvento.append("       P.NM_PESSOA NM_DESTINATARIO, \n");
        queryEvento.append("       NVL(MED.NM_RECEBEDOR, ' ') NM_RECEBEDOR, \n");
        queryEvento.append("       FO.SG_FILIAL || ' - ' || FD.SG_FILIAL ORIGEMDESTINO, \n");
        queryEvento.append("       TO_CHAR(EDS.DH_EVENTO, 'YYYY-MM-DD') DT_ENTREGA, \n");
        queryEvento.append("       FO.SG_FILIAL || ' ' || TO_CHAR(C.NR_CONHECIMENTO) CTE, \n");
        queryEvento.append("       NFC.NR_NOTA_FISCAL NF, \n");
        queryEvento.append("       DC.DS_VALOR_CAMPO PEDIDO, \n");
        queryEvento.append("       NFC.QT_VOLUMES VOL, \n");
        queryEvento.append("       CE.ASSINATURA ASSINATURA, \n");
        queryEvento.append("       CE.BL_ENVIADO BL_ENVIADO, \n");
        queryEvento.append("       NVL(MED.NR_RG_RECEBEDOR, ' ') NR_RG_RECEBEDOR, \n");
        queryEvento.append("       CASE \n");
        queryEvento.append("        WHEN MED.TP_GRAU_RECEB IS NULL THEN \n");
        queryEvento.append("            ' ' \n");
        queryEvento.append("        ELSE \n");
        queryEvento.append("            (SELECT VI18N(VD.DS_VALOR_DOMINIO_I, 'pt_BR') FROM DOMINIO D \n");
        queryEvento.append("                INNER JOIN VALOR_DOMINIO VD ON VD.ID_DOMINIO = D.ID_DOMINIO \n");
        queryEvento.append("                WHERE D.NM_DOMINIO = 'DM_GRAU_PARENTESCO' AND VL_VALOR_DOMINIO = MED.TP_GRAU_RECEB) \n");
        queryEvento.append("       END AS DS_PARENTESCO, \n");

        queryEvento.append("       (SELECT GRPR.BL_GRAU_RISCO_PERG_RESP FROM GRAU_RISCO_PERGUNTA_RESP GRPR \n");
        queryEvento.append("                INNER JOIN GRAU_RISCO_PERGUNTA GRP ON GRPR.ID_GRAU_RISCO_PERGUNTA = GRP.ID_GRAU_RISCO_PERGUNTA AND GRP.ID_GRAU_RISCO_PERGUNTA = 3 \n");
        queryEvento.append("                WHERE CE.ID_COMPROVANTE_ENTREGA = GRPR.ID_COMPROVANTE_ENTREGA) \n");
        queryEvento.append("        AS CAIXA_LACRADA, \n");
        queryEvento.append("        (SELECT GRPR.BL_GRAU_RISCO_PERG_RESP FROM GRAU_RISCO_PERGUNTA_RESP GRPR \n");
        queryEvento.append("                INNER JOIN GRAU_RISCO_PERGUNTA GRP ON GRPR.ID_GRAU_RISCO_PERGUNTA = GRP.ID_GRAU_RISCO_PERGUNTA AND GRP.ID_GRAU_RISCO_PERGUNTA = 1 \n");
        queryEvento.append("                WHERE CE.ID_COMPROVANTE_ENTREGA = GRPR.ID_COMPROVANTE_ENTREGA) \n");
        queryEvento.append("        AS CONFERIDO, \n");
        queryEvento.append("        (SELECT DC.DS_VALOR_CAMPO  \n");
        queryEvento.append("               FROM INFORMACAO_DOCTO_CLIENTE IDC \n");
        queryEvento.append("               INNER JOIN DADOS_COMPLEMENTO DC ON DC.ID_INFORMACAO_DOCTO_CLIENTE = IDC.ID_INFORMACAO_DOCTO_CLIENTE \n");
        queryEvento.append("               WHERE DS_CAMPO IN ('Cod. Conf', 'Cód. Conf') \n");
        queryEvento.append("               AND IDC.ID_CLIENTE IN (:idsCliente) \n");
        queryEvento.append("               AND DC.ID_CONHECIMENTO = :idDoctoService) \n");
        queryEvento.append("         AS GRAU_RISCO, \n");
        queryEvento.append("       NVL(MED.TP_FORMA_BAIXA, '') TP_FORMA_BAIXA \n");

        queryEvento.append("  FROM DOCTO_SERVICO DS \n");
        queryEvento.append("  	INNER JOIN CONHECIMENTO C ON DS.ID_DOCTO_SERVICO = C.ID_CONHECIMENTO \n");
        queryEvento.append("  	INNER JOIN COMPROVANTE_ENTREGA CE ON CE.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND CE.ASSINATURA IS NOT NULL \n");
        queryEvento.append("  	INNER JOIN NOTA_FISCAL_CONHECIMENTO NFC ON C.ID_CONHECIMENTO = NFC.ID_CONHECIMENTO AND CE.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO \n");
        queryEvento.append("  	INNER JOIN PESSOA P ON DS.ID_CLIENTE_DESTINATARIO = P.ID_PESSOA \n");
        queryEvento.append("  	INNER JOIN MANIFESTO_ENTREGA_DOCUMENTO MED ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO AND MED.ID_OCORRENCIA_ENTREGA = 5 \n");
        queryEvento.append("  	INNER JOIN FILIAL FO ON DS.ID_FILIAL_ORIGEM = FO.ID_FILIAL \n");
        queryEvento.append("  	INNER JOIN FILIAL FD ON DS.ID_FILIAL_DESTINO = FD.ID_FILIAL \n");
        queryEvento.append("  	INNER JOIN EVENTO_DOCUMENTO_SERVICO EDS ON DS.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO AND EDS.BL_EVENTO_CANCELADO = 'N' \n");
        queryEvento.append("  	INNER JOIN EVENTO E ON EDS.ID_EVENTO = E.ID_EVENTO AND E.CD_EVENTO = 21 \n");
        queryEvento.append("  	INNER JOIN DADOS_COMPLEMENTO DC ON C.ID_CONHECIMENTO = DC.ID_CONHECIMENTO \n");
        queryEvento.append("  	INNER JOIN INFORMACAO_DOCTO_CLIENTE IDC ON IDC.ID_CLIENTE = DS.ID_CLIENTE_REMETENTE \n");
        queryEvento.append("      		AND IDC.ID_INFORMACAO_DOCTO_CLIENTE = DC.ID_INFORMACAO_DOCTO_CLIENTE \n");
        queryEvento.append("      		AND IDC.DS_CAMPO = 'Pedido' \n");
        queryEvento.append("  WHERE DS.ID_DOCTO_SERVICO = :idDoctoService \n");
        queryEvento.append("  AND NFC.ID_NOTA_FISCAL_CONHECIMENTO = :idNotaFiscalConhecimento \n");

        List queryResult = getAdsmHibernateTemplate().findBySql(queryEvento.toString(), parameters, new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("NM_DESTINATARIO", Hibernate.STRING);
                query.addScalar("NM_RECEBEDOR", Hibernate.STRING);
                query.addScalar("ORIGEMDESTINO", Hibernate.STRING);
                query.addScalar("DT_ENTREGA", Hibernate.DATE);
                query.addScalar("CTE", Hibernate.STRING);
                query.addScalar("NF", Hibernate.STRING);
                query.addScalar("PEDIDO", Hibernate.STRING);
                query.addScalar("VOL", Hibernate.INTEGER);
                query.addScalar("ASSINATURA", Hibernate.BLOB);
                query.addScalar("BL_ENVIADO", Hibernate.STRING);
                query.addScalar("NR_RG_RECEBEDOR", Hibernate.STRING);
                query.addScalar("DS_PARENTESCO", Hibernate.STRING);
                query.addScalar("CAIXA_LACRADA", Hibernate.STRING);
                query.addScalar("CONFERIDO", Hibernate.STRING);
                query.addScalar("GRAU_RISCO", Hibernate.INTEGER);
                query.addScalar("TP_FORMA_BAIXA", Hibernate.STRING);
            }
        });

        if (CollectionUtils.isEmpty(queryResult)) {
            return eventoDocumentoServicoNaturaDMN;
        }

        Object[] values = (Object[]) queryResult.get(0);

        eventoDocumentoServicoNaturaDMN.setNomeDestinatario((String) values[0]);
        eventoDocumentoServicoNaturaDMN.setNomeRecebedor((String) values[1]);
        eventoDocumentoServicoNaturaDMN.setOrigemDestino((String) values[2]);
        eventoDocumentoServicoNaturaDMN.setDtEntrega((Date) values[3]);
        eventoDocumentoServicoNaturaDMN.setCte((String) values[4]);
        eventoDocumentoServicoNaturaDMN.setNrNotaFiscal((String) values[5]);
        eventoDocumentoServicoNaturaDMN.setPedido((String) values[6]);
        eventoDocumentoServicoNaturaDMN.setVolumes((Integer) values[7]);
        eventoDocumentoServicoNaturaDMN.setNumRgRecebedor((String) values[10]);
        eventoDocumentoServicoNaturaDMN.setGrauParentesco((String) values[11]);

        if (values[9] == null || "N".equalsIgnoreCase(values[9].toString())) {
            eventoDocumentoServicoNaturaDMN.setBlEnviado(false);
        } else {
            eventoDocumentoServicoNaturaDMN.setBlEnviado(true);
        }

        if (values[12] == null) {
            eventoDocumentoServicoNaturaDMN.setCaixaLacrada("");
        } else {
            eventoDocumentoServicoNaturaDMN.setCaixaLacrada("S".equalsIgnoreCase((String) values[12]) ? "Sim" : "Não");
        }

        if (values[13] == null) {
            eventoDocumentoServicoNaturaDMN.setConferido("");
        } else {
            eventoDocumentoServicoNaturaDMN.setConferido("S".equalsIgnoreCase((String) values[13]) ? "Sim" : "Não");
        }

        if (values[14] == null) {
            eventoDocumentoServicoNaturaDMN.setGrauRisco(null);
        } else {
            eventoDocumentoServicoNaturaDMN.setGrauRisco((Integer) values[14]);
        }
        eventoDocumentoServicoNaturaDMN.setTpFormaBaixa((String) values[15]);

        doAssinaturaComprovanteEntrega(eventoDocumentoServicoNaturaDMN, values);

        return eventoDocumentoServicoNaturaDMN;
    }

    private void doAssinaturaComprovanteEntrega(EventoDocumentoServicoNaturaDMN eventoDocumentoServicoNaturaDMN, Object[] values) {
        try {
            Blob assinatura = (Blob) values[8];
            int sizeAss = (int) assinatura.length();

            eventoDocumentoServicoNaturaDMN.setAssinatura(assinatura.getBytes(1, sizeAss));
        } catch (SQLException e) {
            logger.error("Erro ao converter assinatura em formato Blob para array de bytes", e);
        }
    }

    public Short findCdOcorrenciaCliente(Long idCliente, Long idLMS, String tpOcorrencia, String nmTabelaOcorrencia) {
        Map<String, Object> parameters = new HashMap<String, Object>();

        parameters.put("idCliente", idCliente);
        parameters.put("idLMS", idLMS);
        parameters.put("tpOcorrencia", tpOcorrencia);
        parameters.put("nmTabelaOcorrencia", nmTabelaOcorrencia);

        StringBuilder queryEvento = new StringBuilder();

        queryEvento.append("SELECT etod.cd_ocorrencia_cliente as cdOcorrenciaCliente \n");
        queryEvento.append("FROM edi_tabela_ocoren eto, \n");
        queryEvento.append("  edi_tabela_ocoren_det etod, \n");
        queryEvento.append("  edi_tabela_cliente etc \n");

        queryEvento.append("WHERE etc.id_cliente = :idCliente \n");
        queryEvento.append("AND etc.id_edi_tabela_ocoren = eto.id_edi_tabela_ocoren \n");
        queryEvento.append("AND etod.id_edi_tabela_ocoren = eto.id_edi_tabela_ocoren \n");

        queryEvento.append("AND etod.id_lms = :idLMS \n");
        queryEvento.append("AND etod.tp_ocorrencia = :tpOcorrencia \n");
        queryEvento.append("AND eto.nm_tabela_ocoren = :nmTabelaOcorrencia ");

        List list = getAdsmHibernateTemplate().findBySql(queryEvento.toString(), parameters, new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("cdOcorrenciaCliente", Hibernate.SHORT);
            }
        }, AliasToNestedMapResultTransformer.getInstance());

        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        Map<String, Object> dados = (Map<String, Object>) list.get(0);
        return (Short) dados.get("cdOcorrenciaCliente");
    }

    public EventoDocumentoServico findByIdoctoServicoIdEventoIdOcorrenciaEntrega(Long idDoctoServico, Long idEvento, Long idOcorrenciaEntrega) {
        StringBuilder hql = new StringBuilder()
                .append(" select eds ")
                .append("from ").append(EventoDocumentoServico.class.getName()).append(" as eds ")
                .append("join fetch eds.evento as evento ")
                .append("join fetch eds.ocorrenciaEntrega as oe ")
                .append("where eds.doctoServico.id = ? ")
                .append("and evento.idEvento = ? ")
                .append("and oe.idOcorrenciaEntrega = ? ")
                .append("and eds.blEventoCancelado = 'N' ")
                .append("order by eds.dhEvento.value desc ");


        List<EventoDocumentoServico> lista = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idDoctoServico, idEvento, idOcorrenciaEntrega});

        if (CollectionUtils.isEmpty(lista)) {
            return null;
        }

        return lista.get(0);
    }

    public boolean existsEventoNaoCanceladoComOcorrenciaEntregaParcialByIdDoctoServico(Long idDoctoServico) {
        StringBuilder query = this.sqlEventoComOcorrenciaEntregaParcial()
                .append(" AND EDS.blEventoCancelado = 'N' ");

        List<Long> param = new ArrayList<Long>();
        param.add(idDoctoServico);

        Integer qtdRows = getAdsmHibernateTemplate().getRowCountForQuery(query.toString(), param.toArray());
        return CompareUtils.gt(qtdRows, IntegerUtils.ZERO);
    }

    public boolean existsEventoComOcorrenciaEntregaParcialByIdDoctoServico(Long idDoctoServico) {
        List<Long> param = new ArrayList<Long>();
        param.add(idDoctoServico);

        Integer qtdRows = getAdsmHibernateTemplate().getRowCountForQuery(this.sqlEventoComOcorrenciaEntregaParcial().toString(), param.toArray());
        return CompareUtils.gt(qtdRows, IntegerUtils.ZERO);
    }

    private StringBuilder sqlEventoComOcorrenciaEntregaParcial() {
        return new StringBuilder()
                .append(" FROM ")
                .append(EventoDocumentoServico.class.getName()).append(" as EDS, ")
                .append(OcorrenciaEntrega.class.getName()).append(" as OE ")
                .append(" WHERE EDS.doctoServico.idDoctoServico = ? ")
                .append("   AND EDS.ocorrenciaEntrega = OE.idOcorrenciaEntrega ")
                .append("   AND OE.cdOcorrenciaEntrega = 102 ");
    }

    public List<EventoDocumentoServico> findMaiorEvento(DoctoServico doctoServico, DateTime dataHoraReferencia){
        StringBuilder hql = new StringBuilder("select eds from EventoDocumentoServico eds ")
                .append(" where eds.dhEvento.value > :dhReferencia and eds.doctoServico = :doctoServico ")
                .append(" and eds.evento.localizacaoMercadoria is not null ")
                .append(" order by eds.dhEvento desc");

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("dhReferencia", dataHoraReferencia);
        parameters.put("doctoServico", doctoServico);
        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parameters);
    }


    public boolean existsEventoFinalizacaoEntregaParcialByIdDoctoServico(Long idDoctoServico) {
        Integer qtdRows = getAdsmHibernateTemplate().getRowCountForQuery(this.sqlEventoFinalizacaoEntregaParcial().toString(), new Object[]{idDoctoServico});
        return CompareUtils.gt(qtdRows, IntegerUtils.ZERO);
    }

    private StringBuilder sqlEventoFinalizacaoEntregaParcial() {
        return new StringBuilder()
                .append(" FROM ")
                .append(EventoDocumentoServico.class.getName()).append(" as EDS ")
                .append(" WHERE EDS.doctoServico.idDoctoServico = ? ")
                .append("   AND EDS.evento.cdEvento = 154 ");
    }

    public List<Object[]> findNotasEventoDoctoServicoWine(
            Long idEventoDocumentoServico, String cnpjParcial, String nmTabelaOcorren) {
        String query = "	select to_char(eds.dh_evento, 'yyyy-MM-dd hh24:mi:ss') AS dataEvento, etod.ds_ocorrencia AS descricaoTracking, nfc.NR_CHAVE AS chaveNF"+
                "	from evento_documento_servico eds,   "+
                "	devedor_doc_serv dds,  "+
                "	nota_fiscal_conhecimento nfc,  "+
                "	evento e,  "+
                "	descricao_evento de, "+
                "	edi_tabela_cliente etc, "+
                "	edi_tabela_ocoren eto, "+
                "	edi_tabela_ocoren_det etod "+
                "	where  "+
                "	  eds.id_evento = e.id_evento "+
                "	  and e.ID_DESCRICAO_EVENTO = de.ID_DESCRICAO_EVENTO "+
                "	  and nfc.id_conhecimento = eds.ID_DOCTO_SERVICO "+
                "	  and dds.id_docto_servico = eds.ID_DOCTO_SERVICO "+
                "	  and dds.ID_CLIENTE  in (select id_pessoa from pessoa where nr_identificacao like '"+cnpjParcial+"%')  "+
                "	  and eds.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDoctoServico "+
                "	  and etc.id_cliente = dds.id_cliente "+
                "	  and etc.id_edi_tabela_ocoren = etod.id_edi_tabela_ocoren "+
                "	  and eto.id_edi_tabela_ocoren = etc.id_edi_tabela_ocoren "+
                "	  and eto.nm_tabela_ocoren = :nmTabelaOcorren "+
                "	  and ( "+
                "		(etod.id_lms = eds.id_ocorrencia_entrega and etod.tp_ocorrencia = 1) "+
                "		or (  etod.id_lms = eds.id_ocorrencia_pendencia and etod.tp_ocorrencia = 2) "+
                "		or (  etod.id_lms = eds.id_evento and etod.tp_ocorrencia = 3) "+
                "		) ";

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("nmTabelaOcorren", nmTabelaOcorren);
        param.put("idEventoDoctoServico", idEventoDocumentoServico);


        List<Object[]> notasEvento = getAdsmHibernateTemplate().findBySql(query, param, new ConfigureSqlQuery() {

            @Override
            public void configQuery(SQLQuery sql) {
                sql.addScalar("dataEvento", Hibernate.STRING);
                sql.addScalar("descricaoTracking", Hibernate.STRING);
                sql.addScalar("chaveNF", Hibernate.STRING);

            }
        });


        return notasEvento;
    }

    public EventoDocumentoServico findEventoDoctoServico(Long idEventoDocumentoServico) {
        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "eds");

        dc.createAlias("eds.doctoServico", "ds");
        dc.add(Restrictions.eq("eds.idEventoDocumentoServico", idEventoDocumentoServico));
        dc.add(Restrictions.eq("eds.blEventoCancelado", false));

        List<EventoDocumentoServico> eventoDocumentoServicos = findByDetachedCriteria(dc);

        if (CollectionUtils.isEmpty(eventoDocumentoServicos)) {
            return null;
        }

        return eventoDocumentoServicos.get(0);
    }

    public List<Object[]> findNotasEventoDoctoServicoCremer(
            Long idEventoDocumentoServico, List<String> cnpjParcial, String nmTabelaOcorren
    ) {

        StringBuilder query = new StringBuilder();

        query.append("select nfc.NR_CHAVE AS chaveNF, etod.cd_ocorrencia_cliente as code, ")
                .append("to_char(eds.dh_evento, 'yyyy-MM-dd hh24:mi:ss') AS start_date, ")
                .append("to_char(eds.dh_evento, 'yyyy-MM-dd hh24:mi:ss') AS end_date, dds.id_cliente ")
                .append(" from evento_documento_servico eds, devedor_doc_serv dds, ")
                .append("nota_fiscal_conhecimento nfc, evento e, descricao_evento de, ")
                .append("edi_tabela_cliente etc, edi_tabela_ocoren eto, edi_tabela_ocoren_det etod ")
                .append("where eds.id_evento = e.id_evento and e.ID_DESCRICAO_EVENTO = de.ID_DESCRICAO_EVENTO ")
                .append("and nfc.id_conhecimento = eds.ID_DOCTO_SERVICO ")
                .append("and dds.id_docto_servico = eds.ID_DOCTO_SERVICO and etc.id_cliente = dds.id_cliente ")
                .append("and etc.id_edi_tabela_ocoren = etod.id_edi_tabela_ocoren ")
                .append("and eto.id_edi_tabela_ocoren = etc.id_edi_tabela_ocoren ")
                .append("and etod.id_lms = eds.id_ocorrencia_entrega and etod.tp_ocorrencia = 1 ")
                .append("and exists (select 1 from pessoa p ")
                .append("          where p.id_pessoa  = dds.ID_CLIENTE and substr(nr_identificacao,1,8) in (:cnpjParcial)) ")
                .append("and eds.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDoctoServico ")
                .append("and eto.nm_tabela_ocoren = :nmTabelaOcorren");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("nmTabelaOcorren", nmTabelaOcorren);
        param.put("idEventoDoctoServico", idEventoDocumentoServico);
        param.put("cnpjParcial", cnpjParcial);

        List<Object[]> notasEvento = getAdsmHibernateTemplate()
                .findBySql(
                        query.toString(),
                        param,
                        new ConfigureSqlQuery() {
                            @Override
                            public void configQuery(SQLQuery sql) {
                                sql.addScalar("chaveNF", Hibernate.STRING);
                                sql.addScalar("code", Hibernate.STRING);
                                sql.addScalar("start_date", Hibernate.STRING);
                                sql.addScalar("end_date", Hibernate.STRING);
                                sql.addScalar("id_cliente", Hibernate.LONG);

                            }
                        }
                );
        return notasEvento;


    }

    public List<Object[]> findNotasEventoDoctoServicoDecathlon
            (
                    Long idEventoDocumentoServico, List<String> cnpjParcial
            ) {

        StringBuilder query = new StringBuilder();

        moustProjectionNotasDecathlon(query);

        Map<String, Object> param = new HashMap<String, Object>();

        param.put("idEventoDoctoServico", idEventoDocumentoServico);
        param.put("cnpjParcial", cnpjParcial);

        return getAdsmHibernateTemplate()
                .findBySql(
                        query.toString(),
                        param,
                        configureSqlQueryNotasDecathlon()
                );
    }

    private ConfigureSqlQuery configureSqlQueryNotasDecathlon(){
        return new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("numeroNF", Hibernate.STRING);
                sqlQuery.addScalar("codigoIntegracao", Hibernate.STRING);
                sqlQuery.addScalar("descricao", Hibernate.STRING);
                sqlQuery.addScalar("DataOcorrencia", Hibernate.STRING);
                sqlQuery.addScalar("idCliente", Hibernate.LONG);
                sqlQuery.addScalar("CPFCNPJRemetente", Hibernate.STRING);
                sqlQuery.addScalar("CPFCNPJDestinatario", Hibernate.STRING);
                sqlQuery.addScalar("tipoOcorrencia", Hibernate.STRING);
            }
        };
    }

    private void moustProjectionNotasDecathlon(StringBuilder query){
        query.append("select nfc.NR_NOTA_FISCAL AS numeroNF, oe.cd_ocorrencia_entrega AS codigoIntegracao")
            .append(", oe.ds_ocorrencia_entrega_i AS descricao")
            .append(", to_char(eds.dh_evento, 'dd/MM/yyyy hh24:mi:ss') AS DataOcorrencia, dds.id_cliente AS idCliente")
            .append(", pesRem.nr_identificacao AS CPFCNPJRemetente, pesDest.nr_identificacao  AS CPFCNPJDestinatario")
            .append(", 'E' tipoOcorrencia from evento_documento_servico eds, docto_servico ds, devedor_doc_serv dds")
            .append(", nota_fiscal_conhecimento nfc, ocorrencia_entrega oe, pessoa pesRem , pessoa pesDest ")
            .append("where eds.id_docto_servico = ds.id_docto_servico and dds.id_docto_servico = ds.id_docto_servico ")
            .append("and nfc.id_conhecimento = ds.id_docto_servico and pesRem.Id_Pessoa = ds.id_cliente_remetente ")
            .append("and pesDest.Id_Pessoa = ds.id_cliente_destinatario ")
            .append("and eds.id_ocorrencia_entrega = oe.id_ocorrencia_entrega ")
            .append("and dds.ID_CLIENTE in (select id_pessoa from pessoa where substr(nr_identificacao,1,8) in (:cnpjParcial)) ")
            .append("and eds.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDoctoServico")
            .append(" UNION ")
            .append("select nfc.NR_NOTA_FISCAL AS numeroNF, op.cd_ocorrencia as codigoIntegracao")
            .append(", op.DS_OCORRENCIA_I AS descricao")
            .append(", to_char(eds.dh_evento, 'dd/MM/yyyy hh24:mi:ss') AS DataOcorrencia, dds.id_cliente AS idCliente")
            .append(", pesRem.nr_identificacao AS CPFCNPJRemetente, pesDest.nr_identificacao  AS CPFCNPJDestinatario")
            .append(", 'P' tipoOcorrencia from evento_documento_servico eds, docto_servico ds, devedor_doc_serv dds")
            .append(", nota_fiscal_conhecimento nfc, ocorrencia_pendencia op, pessoa pesRem , pessoa pesDest ")
            .append("where eds.id_docto_servico = ds.id_docto_servico and dds.id_docto_servico = ds.id_docto_servico ")
            .append("and nfc.id_conhecimento = ds.id_docto_servico and pesRem.Id_Pessoa = ds.id_cliente_remetente ")
            .append("and pesDest.Id_Pessoa = ds.id_cliente_destinatario ")
            .append("and eds.id_ocorrencia_pendencia = op.id_ocorrencia_pendencia ")
            .append("and dds.ID_CLIENTE in (select id_pessoa from pessoa where substr(nr_identificacao,1,8) in (:cnpjParcial)) ")
            .append("and eds.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDoctoServico");
    }

    public List<Object[]> findNotasEventoDoctoServicoRotaCremer(
            Long idEventoDocumentoServico, List<String> cnpjParcial
    ) {

        StringBuilder query = new StringBuilder();

        query.append("select to_char(ds.dt_prev_entrega, 'yyyyMMdd') as data, fl.sg_filial as sgFilial, ")
            .append("pt.nr_identificacao as codigo, pt.nm_pessoa as razao, nfc.nr_chave as chaveNota, ")
            .append("ds.nr_docto_servico as nrDoctoServico, dds.id_cliente as idCliente, ")
            .append("to_char(ds.dt_prev_entrega, 'yyyy-MM-dd') as routeDate from docto_servico ds, ")
            .append("filial fl,  pessoa pt, evento_documento_servico eds, devedor_doc_serv dds, evento e, ")
            .append("descricao_evento de, nota_fiscal_conhecimento nfc, conhecimento c ")
            .append(" where eds.id_docto_servico = ds.id_docto_servico and ds.id_filial_origem = fl.id_filial ")
            .append("and ds.id_docto_servico = c.id_conhecimento and c.id_conhecimento = nfc.id_conhecimento ")
            .append("and ds.id_filial_origem = pt.id_pessoa and eds.id_evento = e.id_evento ")
            .append("and e.ID_DESCRICAO_EVENTO = de.ID_DESCRICAO_EVENTO ")
            .append("and dds.id_docto_servico = eds.ID_DOCTO_SERVICO ")
            .append("and exists (select 1 from pessoa p where p.id_pessoa  = dds.ID_CLIENTE ")
            .append("              and substr(nr_identificacao,1,8) in (:cnpjParcial)) ")
            .append("and eds.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDoctoServico");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("idEventoDoctoServico", idEventoDocumentoServico);
        param.put("cnpjParcial", cnpjParcial);

        return getAdsmHibernateTemplate()
                .findBySql(
                        query.toString(),
                        param,
                        new ConfigureSqlQuery() {
                            @Override
                            public void configQuery(SQLQuery sql) {
                                sql.addScalar("data", Hibernate.STRING);
                                sql.addScalar("sgFilial", Hibernate.STRING);
                                sql.addScalar("codigo", Hibernate.STRING);
                                sql.addScalar("razao", Hibernate.STRING);
                                sql.addScalar("chaveNota", Hibernate.STRING);
                                sql.addScalar("nrDoctoServico", Hibernate.STRING);
                                sql.addScalar("idCliente", Hibernate.LONG);
                                sql.addScalar("routeDate", Hibernate.STRING);
                            }
                        }
                );

    }

    public List<Object[]> findNotasEventoDecathlon(Long idEventoDocumentoServico, List<String> cnpjParcial) {
        StringBuilder query = new StringBuilder();

        query.append("select nfc.NR_NOTA_FISCAL AS numeroNF")
                .append(", evt.cd_evento AS codigoIntegracao")
                .append(", de.ds_descricao_evento_i AS descricao")
                .append(", to_char(eds.dh_evento, 'dd/MM/yyyy hh24:mi:ss') AS DataOcorrencia")
                .append(", dds.id_cliente AS idCliente")
                .append(", pesRem.nr_identificacao AS CPFCNPJRemetente, pesDest.nr_identificacao  AS CPFCNPJDestinatario")
                .append(", 'T' tipoOcorrencia")
                .append(" from evento_documento_servico eds, docto_servico ds, devedor_doc_serv dds")
                .append(", nota_fiscal_conhecimento nfc, evento evt, descricao_evento de , pessoa pesRem , pessoa pesDest")
                .append(" where eds.id_docto_servico = ds.id_docto_servico")
                .append(" and dds.id_docto_servico = ds.id_docto_servico")
                .append(" and nfc.id_conhecimento = ds.id_docto_servico")
                .append(" and pesRem.Id_Pessoa = ds.id_cliente_remetente")
                .append(" and pesDest.Id_Pessoa = ds.id_cliente_destinatario")
                .append(" and eds.id_evento = evt.id_evento")
                .append(" and evt.id_descricao_evento = de.id_descricao_evento")
                .append(" and dds.ID_CLIENTE in (select id_pessoa from pessoa where substr(nr_identificacao,1,8) in (:cnpjParcial))")
                .append(" and eds.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDoctoServico");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("idEventoDoctoServico" , idEventoDocumentoServico);
        param.put("cnpjParcial" , cnpjParcial);

        List<Object[]> notasEvento = getAdsmHibernateTemplate().findBySql(query.toString(), param,
                new ConfigureSqlQuery() {
                    @Override
                    public void configQuery(SQLQuery sqlQuery) {
                        sqlQuery.addScalar("numeroNF" , Hibernate.STRING);
                        sqlQuery.addScalar("codigoIntegracao" , Hibernate.STRING);
                        sqlQuery.addScalar("descricao" , Hibernate.STRING);
                        sqlQuery.addScalar("DataOcorrencia" , Hibernate.STRING);
                        sqlQuery.addScalar("idCliente" , Hibernate.LONG);
                        sqlQuery.addScalar("CPFCNPJRemetente" , Hibernate.STRING);
                        sqlQuery.addScalar("CPFCNPJDestinatario" , Hibernate.STRING);
                        sqlQuery.addScalar("tipoOcorrencia" , Hibernate.STRING);
                    }
                }
        );
        return notasEvento;
    }

    public List<Object[]> findNotasEventoDoctoServicoFinalizaRotaCremer(
            Long idEventoDocumentoServico, Long idFilialEvento, List<String> cnpjParcial
    ) {

        StringBuilder query = new StringBuilder();

        query.append("select to_char(ds.dt_prev_entrega, 'yyyy-MM-dd') as occurrenceDate")
             .append(", fl.sg_filial as sgFilial, ds.nr_docto_servico as nrDoctoServico")
             .append(", dds.id_cliente as idCliente from docto_servico ds, filial fl,  pessoa pt, ")
             .append("evento_documento_servico eds, devedor_doc_serv dds, evento e, descricao_evento de ")
             .append("where eds.id_docto_servico = ds.id_docto_servico and ds.id_filial_origem = fl.id_filial ")
             .append("and ds.id_filial_origem = pt.id_pessoa and eds.id_evento = e.id_evento ")
             .append("and e.ID_DESCRICAO_EVENTO = de.ID_DESCRICAO_EVENTO ")
             .append("and dds.id_docto_servico = eds.ID_DOCTO_SERVICO ")
             .append("and exists (select 1 from pessoa p where p.id_pessoa  = dds.ID_CLIENTE ")
             .append("              and substr(nr_identificacao,1,8) in (:cnpjParcial)) ")
             .append("and eds.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDoctoServico ")
             .append("and ds.ID_FILIAL_DESTINO = :idFilialEvento");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("idEventoDoctoServico", idEventoDocumentoServico);
        param.put("cnpjParcial", cnpjParcial);
        param.put("idFilialEvento", idFilialEvento);

        return getAdsmHibernateTemplate()
                .findBySql(
                        query.toString(),
                        param,
                        new ConfigureSqlQuery() {
                            @Override
                            public void configQuery(SQLQuery sql) {
                                sql.addScalar("occurrenceDate", Hibernate.STRING);
                                sql.addScalar("sgFilial", Hibernate.STRING);
                                sql.addScalar("nrDoctoServico", Hibernate.STRING);
                                sql.addScalar("idCliente", Hibernate.LONG);
                            }
                        }
                );

    }

    public List<Object[]> findNotasEventoDoctoServicoAgendamentoCremer
        (Long idEventoDocumentoServico, List<String> cnpjParcial, String nmTabelaOcorren) {

        StringBuilder query = mountProjectionAgendamentoCremer();

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("idEventoDoctoServico", idEventoDocumentoServico);
        param.put("cnpjParcial", cnpjParcial);
        param.put("nmTabelaOcorren", nmTabelaOcorren);

        return getAdsmHibernateTemplate().findBySql(query.toString(), param, configureSqlQueryAgendamentoCremer());
    }

    private StringBuilder mountProjectionAgendamentoCremer(){

        StringBuilder query = new StringBuilder();

        query.append("SELECT nfc.nr_chave As key, to_char(NVL(AGE.DT_AGENDAMENTO,EDS.DH_EVENTO), 'yyyy-MM-dd') AS dateScheduling, ");
        query.append("ETOD.DS_OCORRENCIA AS annotation, ");
        query.append("CASE WHEN AGE.TP_AGENDAMENTO = 'TA' THEN CD_OCORRENCIA_CLIENTE ELSE NULL END AS code, ");
        query.append("DDS.ID_CLIENTE FROM DEVEDOR_DOC_SERV DDS, NOTA_FISCAL_CONHECIMENTO NFC, EDI_TABELA_OCOREN ETO, ");
        query.append("EDI_TABELA_CLIENTE ETC, EDI_TABELA_OCOREN_DET ETOD, EVENTO_DOCUMENTO_SERVICO EDS, ");
        query.append("AGENDAMENTO_DOCTO_SERVICO ADS, AGENDAMENTO_ENTREGA AGE ");
        query.append("WHERE DDS.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO AND DDS.ID_DOCTO_SERVICO = NFC.ID_CONHECIMENTO ");
        query.append("and EDS.ID_DOCTO_SERVICO = ADS.ID_DOCTO_SERVICO ");
        query.append("AND ADS.ID_AGENDAMENTO_ENTREGA = AGE.ID_AGENDAMENTO_ENTREGA ");
        query.append("AND ETO.ID_EDI_TABELA_OCOREN = ETC.ID_EDI_TABELA_OCOREN ");
        query.append("AND ETC.ID_EDI_TABELA_OCOREN = ETOD.ID_EDI_TABELA_OCOREN ");
        query.append("AND ETO.NM_TABELA_OCOREN = :nmTabelaOcorren AND DDS.ID_CLIENTE = ETC.ID_CLIENTE ");
        query.append("AND EDS.ID_EVENTO = ETOD.ID_LMS AND EDS.BL_EVENTO_CANCELADO = 'N' ");
        query.append("AND EDS.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDoctoServico AND nvl(AGE.ID_REAGENDAMENTO,0) = 0 ");
        query.append("and ADS.ID_AGENDAMENTO_ENTREGA IN ");
        query.append("      (SELECT MAX(ADSER.ID_AGENDAMENTO_ENTREGA) FROM AGENDAMENTO_DOCTO_SERVICO ADSER, ");
        query.append("          AGENDAMENTO_ENTREGA AGEE ");
        query.append("              WHERE ADSER.ID_AGENDAMENTO_ENTREGA = AGEE.ID_AGENDAMENTO_ENTREGA ");
        query.append("               GROUP BY ADSER.ID_DOCTO_SERVICO) ");
        query.append("and exists (select 1 from pessoa p where p.id_pessoa  = DDS.ID_CLIENTE ");
        query.append("              and substr(nr_identificacao,1,8) in (:cnpjParcial)) ");

        return query;
    }

    private ConfigureSqlQuery configureSqlQueryAgendamentoCremer(){
        return new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("key", Hibernate.STRING);
                sqlQuery.addScalar("dateScheduling", Hibernate.STRING);
                sqlQuery.addScalar("annotation", Hibernate.STRING);
                sqlQuery.addScalar("code", Hibernate.STRING);
                sqlQuery.addScalar("ID_CLIENTE", Hibernate.LONG);

            }
        };
    }

    public List<Object[]> findNotasEventoAlcon
        (Long idEventoDocumentoServico, String tabelaOcoren, String accIdentifier, List<String> cnpjParcial) {

        StringBuilder query = mountProjectionAlcon();

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("idEventoDoctoServico" , idEventoDocumentoServico);
        param.put("accIdentifier", accIdentifier);
        param.put("tabelaOcoren", tabelaOcoren);
        param.put("cnpjParcial" , cnpjParcial);

        return getAdsmHibernateTemplate()
            .findBySql(query.toString(), param, configureSqlQueryAlcon());

    }

    private StringBuilder mountProjectionAlcon(){
        StringBuilder query = new StringBuilder();

        query.append("SELECT (Select DS_CONTEUDO from parametro_geral ");
        query.append("where NM_PARAMETRO_GERAL = :accIdentifier) AS accountIdentifier, 'SCAC' AS carrierIdentifierType, ");
        query.append("'BRFEDEXA' AS carrierIdentifierValue, 'PRO' AS shipmentIdentifiersType, ");
        query.append("nfc.nr_nota_fiscal AS shipmentIdentifiersValue, ETOD.DS_OCORRENCIA AS statusCode, ");
        query.append("      case when ETOD.CD_OCORRENCIA = 6 THEN 'ORIGIN'");
        query.append("          when ETOD.CD_OCORRENCIA in (55,56) THEN 'TERMINAL'");
        query.append("          when ETOD.CD_OCORRENCIA in (21,58) THEN 'DESTINATION'");
        query.append("          else null end AS stopType, ");
        query.append("(select count(1) from EVENTO_DOCUMENTO_SERVICO EDS1, EVENTO EV1");
        query.append("          where EDS1.ID_EVENTO = EV1.ID_EVENTO AND ID_DOCTO_SERVICO = DDS.ID_DOCTO_SERVICO ");
        query.append("              AND EV1.CD_EVENTO IN (6,55,56,58,21)) AS stopNumber, ");
        query.append("ENP.NR_CEP AS addressPostalCode, ENP.DS_ENDERECO AS addressLines, MUN.NM_MUNICIPIO AS city, ");
        query.append("SG_RESUMIDA AS country, TO_CHAR(CAST(DH_EVENTO AS TIMESTAMP WITH LOCAL TIME ZONE), 'YYYY-MM-DD HH24:MI:SSTZD') AS timestamp, DDS.id_cliente AS idCliente ");
        query.append("FROM DEVEDOR_DOC_SERV DDS, NOTA_FISCAL_CONHECIMENTO NFC, EDI_TABELA_OCOREN ETO,");
        query.append("     EDI_TABELA_CLIENTE ETC, EDI_TABELA_OCOREN_DET ETOD, EVENTO_DOCUMENTO_SERVICO EDS,");
        query.append("     PESSOA PES, ENDERECO_PESSOA ENP, MUNICIPIO MUN, UNIDADE_FEDERATIVA UNF, PAIS ");
        query.append("WHERE DDS.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO AND DDS.ID_DOCTO_SERVICO = NFC.ID_CONHECIMENTO ");
        query.append("  AND ETO.ID_EDI_TABELA_OCOREN = ETC.ID_EDI_TABELA_OCOREN");
        query.append("  AND ETC.ID_EDI_TABELA_OCOREN = ETOD.ID_EDI_TABELA_OCOREN");
        query.append("  AND DDS.ID_CLIENTE = ETC.ID_CLIENTE AND EDS.ID_EVENTO = ETOD.ID_LMS");
        query.append("  AND PES.ID_PESSOA = EDS.ID_FILIAL AND ENP.ID_MUNICIPIO = MUN.ID_MUNICIPIO");
        query.append("  AND ENP.ID_ENDERECO_PESSOA = PES.ID_ENDERECO_PESSOA");
        query.append("  AND MUN.ID_UNIDADE_FEDERATIVA = UNF.ID_UNIDADE_FEDERATIVA AND UNF.ID_PAIS = PAIS.ID_PAIS");
        query.append("  AND EDS.BL_EVENTO_CANCELADO = 'N' AND ETO.NM_TABELA_OCOREN = :tabelaOcoren");
        query.append("  AND EDS.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDoctoServico");
        query.append("  AND EXISTS(SELECT 1 FROM PESSOA P ");
        query.append("       WHERE P.ID_PESSOA = DDS.ID_CLIENTE and substr(P.NR_IDENTIFICACAO,1,8) IN (:cnpjParcial))");

        return query;
    }

    private ConfigureSqlQuery configureSqlQueryAlcon(){
        return  new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("accountIdentifier" , Hibernate.STRING);
                sqlQuery.addScalar("carrierIdentifierType" , Hibernate.STRING);
                sqlQuery.addScalar("carrierIdentifierValue" , Hibernate.STRING);
                sqlQuery.addScalar("shipmentIdentifiersType" , Hibernate.STRING);
                sqlQuery.addScalar("shipmentIdentifiersValue" , Hibernate.STRING);
                sqlQuery.addScalar("statusCode" , Hibernate.STRING);
                sqlQuery.addScalar("stopType" , Hibernate.STRING);
                sqlQuery.addScalar("stopNumber", Hibernate.SHORT);
                sqlQuery.addScalar("addressPostalCode", Hibernate.STRING);
                sqlQuery.addScalar("addressLines", Hibernate.STRING);
                sqlQuery.addScalar("city", Hibernate.STRING);
                sqlQuery.addScalar("country", Hibernate.STRING);
                sqlQuery.addScalar("timestamp", Hibernate.STRING);
                sqlQuery.addScalar("idCliente" , Hibernate.LONG);
            }
        };
    }

    public List<Object[]> findNotasEventoDoctoServicoDell(Long idFatura) {

        StringBuilder query = new StringBuilder();

        query.append("SELECT /*+RULE*/DS.ID_SERVICO, FAT.ID_FATURA, FAT.NR_FATURA, to_char( trunc( CAST(FAT.DT_EMISSAO AS DATE) ), 'ddMMyyyy' ) AS EMIS_FAT, ")
                .append("(SELECT PEF.NR_IDENTIFICACAO FROM LMS_PD.PESSOA PEF WHERE PEF.ID_PESSOA = FAT.ID_FILIAL) AS CNPJ_EMIT_FAT, ")
                .append("(SELECT PCF.NR_IDENTIFICACAO FROM LMS_PD.PESSOA PCF WHERE PCF.ID_PESSOA = FAT.ID_CLIENTE) AS CNPJ_CLI_FAT, ")
                .append("FAT.VL_TOTAL, to_char( trunc( CAST(FAT.DT_VENCIMENTO AS DATE) ), 'ddMMyyyy' ) AS DT_VENCIMENTO, DS.NR_DOCTO_SERVICO, to_char( trunc( CAST(ds.dh_emissao AS DATE) ), 'ddMMyyyy' ) AS EMIS_CTE, ds.nr_cfop, ")
                .append("(SELECT PFO.NR_IDENTIFICACAO FROM LMS_PD.PESSOA PFO WHERE PFO.ID_PESSOA = DS.ID_FILIAL_ORIGEM) AS NR_FIL_ORIG, ")
                .append("PCR.NR_IDENTIFICACAO AS NR_CLI_REM, PCD.NR_IDENTIFICACAO AS NR_CLI_DEST, DDSF.VL_DEVIDO, ")
                .append("DS.VL_BASE_CALC_IMPOSTO, DS.VL_IMPOSTO, DS.VL_ICMS_ST, ")
                .append("round(DS.VL_BASE_CALC_IMPOSTO * (SELECT to_number(TRIM(PG.DS_CONTEUDO)) FROM LMS_PD.PARAMETRO_GERAL PG WHERE PG.NM_PARAMETRO_GERAL = 'PC_PIS_INTEGRACAO_DELL')/100,2) AS PIS, ")
                .append("round(DS.VL_BASE_CALC_IMPOSTO * (SELECT to_number(TRIM(PG1.DS_CONTEUDO)) FROM LMS_PD.PARAMETRO_GERAL PG1 WHERE PG1.NM_PARAMETRO_GERAL = 'PC_COFINS_INTEGRACAO_DELL')/100, 2) AS COFINS, ")
                .append("nvl(DS.QT_VOLUMES, 0) AS QT_VOLUMES, DS.PS_REAL, C.TP_FRETE, DS.PS_REFERENCIA_CALCULO, ")
                .append("nvl((SELECT VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO PDS, LMS_PD.PARCELA_PRECO PP WHERE PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND PP.CD_PARCELA_PRECO = 'IDFretePeso'), 0.00) AS VL_FRETE_PESO, ")
                .append("nvl((SELECT VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO PDS, LMS_PD.PARCELA_PRECO PP WHERE PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND PP.CD_PARCELA_PRECO = 'IDDespacho'), 0.00) AS VL_DESPACHO, ")
                .append("nvl((SELECT VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO PDS, LMS_PD.PARCELA_PRECO PP WHERE PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND PP.CD_PARCELA_PRECO = 'IDPedagio'), 0.00) AS VL_PEDAGIO, ")
                .append("C.TP_CONHECIMENTO, MDE.NR_CHAVE, ")
                .append("(SELECT UPPER(SUBSTR(REPLACE(REPLACE(VD.DS_VALOR_DOMINIO_I,'pt_BR»',''),'¦',''),1,60)) ")
                .append("FROM LMS_PD.VALOR_DOMINIO VD, LMS_PD.DOMINIO D WHERE VD.ID_DOMINIO = D.ID_DOMINIO AND NM_DOMINIO = 'DM_SITUACAO_DOC_ELETRONICO' AND VD.VL_VALOR_DOMINIO = MDE.TP_SITUACAO_DOCUMENTO) AS DS_SITUACAO_DOC, ")
                .append("(SELECT PEN.NR_IDENTIFICACAO FROM LMS_PD.PESSOA PEN WHERE PEN.ID_PESSOA = NFC.ID_CLIENTE) AS CNPJ_EMIT_NFC, ")
                .append("nvl(NFC.DS_SERIE, '') AS DS_SERIE, NFC.NR_NOTA_FISCAL, to_char( trunc( CAST(NFC.DT_EMISSAO AS DATE) ), 'ddMMyyyy' ) AS DT_EMISSAO, NFC.VL_TOTAL AS NFC_VL_TOTAL, ")
                .append("(SELECT S.TP_MODAL FROM LMS_PD.SERVICO S WHERE S.ID_SERVICO = DS.ID_SERVICO) AS TP_MODAL, ")
                .append("nvl(DS.PS_CUBADO_AFERIDO, 0.000) AS PS_CUBADO_AFERIDO, PCR.NM_PESSOA AS NOME_REM, EPR.DS_ENDERECO || ',' || EPR.NR_ENDERECO AS ENDERECO_REM, ")
                .append("MUNR.NM_MUNICIPIO AS MUNICIPIO_REM, UFR.SG_UNIDADE_FEDERATIVA AS UF_REM, ")
                .append("PCD.NM_PESSOA AS NOME_DEST, EPD.DS_ENDERECO || ',' || EPD.NR_ENDERECO AS ENDERECO_DEST, ")
                .append("MUND.NM_MUNICIPIO AS MUNICIPIO_DEST, UFD.SG_UNIDADE_FEDERATIVA AS UF_DEST, ")
                .append("nvl((SELECT SUM(VL_PARCELA_BRUTO) FROM LMS_PD.PARCELA_DOCTO_SERVICO PDS, LMS_PD.PARCELA_PRECO PP WHERE PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND PP.CD_PARCELA_PRECO IN ('IDAdvalorem','IDAdvalorem1','IDAdvalorem2', 'IDFreteValor')), 0) AS VL_ADVALOREM, ")
                .append("nvl((SELECT VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO PDS, LMS_PD.PARCELA_PRECO PP WHERE PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND PP.CD_PARCELA_PRECO = 'IDTde'), 0) AS VL_TDE, ")
                .append("to_char( trunc( CAST((SELECT MAX(EDS.DH_EVENTO) FROM LMS_PD.EVENTO_DOCUMENTO_SERVICO EDS, LMS_PD.EVENTO EV WHERE EDS.ID_EVENTO = EV.ID_EVENTO AND EDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND EDS.BL_EVENTO_CANCELADO = 'N' AND EV.CD_EVENTO = 21) AS DATE) ), 'ddMMyyyy' ) AS DT_ENTREGA, ")
                .append("FAT.ID_CLIENTE, ")
                .append("nvl((SELECT PDS.VL_PARCELA_BRUTO FROM PARCELA_DOCTO_SERVICO PDS, PARCELA_PRECO PP WHERE DS.ID_DOCTO_SERVICO = PDS.ID_DOCTO_SERVICO AND PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND PP.CD_PARCELA_PRECO in('IDAgendamentoColetaEntrega', 'IDAgendamentoEntregaSDF')), 0) AS VL_AGENDAMENTO, ")
                .append("nvl((SELECT PDS.VL_PARCELA_BRUTO FROM PARCELA_DOCTO_SERVICO PDS, PARCELA_PRECO PP WHERE DS.ID_DOCTO_SERVICO = PDS.ID_DOCTO_SERVICO AND PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND PP.CD_PARCELA_PRECO = 'IDGris'), 0) AS VL_GRIS, ")
                .append("nvl((SELECT PDS.VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO PDS, LMS_PD.PARCELA_PRECO PP WHERE PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND PP.CD_PARCELA_PRECO = 'IDTaxaPaletizacao'), 0) AS VL_PALETE, ")
                .append("nvl((SELECT PDS.VL_PARCELA_BRUTO FROM PARCELA_DOCTO_SERVICO PDS, PARCELA_PRECO PP WHERE DS.ID_DOCTO_SERVICO = PDS.ID_DOCTO_SERVICO AND PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND PP.CD_PARCELA_PRECO = 'IDFretePeso'), 0) AS VL_FRETE_VALOR, ")
                .append("lpad(FO.CD_FILIAL, 3, 0) || to_char(ds.DH_EMISSAO, 'rrmmdd') || lpad(ds.NR_DOCTO_SERVICO , 9, 0) AS FILIAL_CTRC, ")
                .append("nvl((SELECT PDS.VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO PDS, LMS_PD.PARCELA_PRECO PP WHERE PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND PP.CD_PARCELA_PRECO = 'IDTrt'), 0) AS VL_TRT, ")
                .append("nvl((SELECT PDS.VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO PDS, LMS_PD.PARCELA_PRECO PP WHERE PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND PP.CD_PARCELA_PRECO = 'IDTas'), 0) AS VL_TAS_ADM, ")
                .append("nvl((SELECT PDS.VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO PDS, LMS_PD.PARCELA_PRECO PP WHERE PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND PP.CD_PARCELA_PRECO = 'IDSuframa'), 0) AS VL_SUFRAMA, ")
                .append("nvl((SELECT PDS.VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO PDS, LMS_PD.PARCELA_PRECO PP WHERE PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND PP.CD_PARCELA_PRECO = 'IDEmex'), 0) AS VL_EMEX, ")
                .append("(SELECT PT.NR_IDENTIFICACAO FROM LMS_PD.PESSOA PT WHERE PT.ID_PESSOA = DDSF.ID_CLIENTE) AS TOMADOR ")
                .append("FROM LMS_PD.FATURA FAT ")
                .append("JOIN LMS_PD.ITEM_FATURA ITF ON FAT.ID_FATURA = ITF.ID_FATURA ")
                .append("JOIN LMS_PD.DEVEDOR_DOC_SERV_FAT DDSF ON ITF.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT ")
                .append("JOIN LMS_PD.DOCTO_SERVICO DS ON DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ")
                .append("JOIN LMS_PD.FILIAL FO ON DS.ID_FILIAL_ORIGEM = FO.ID_FILIAL ")
                .append("JOIN LMS_PD.CONHECIMENTO C ON DS.ID_DOCTO_SERVICO = C.ID_CONHECIMENTO ")
                .append("JOIN LMS_PD.NOTA_FISCAL_CONHECIMENTO NFC ON C.ID_CONHECIMENTO = NFC.ID_CONHECIMENTO ")
                .append("JOIN LMS_PD.MONITORAMENTO_DOC_ELETRONICO MDE ON DS.ID_DOCTO_SERVICO = MDE.ID_DOCTO_SERVICO ")
                .append("JOIN LMS_PD.PESSOA PCR ON DS.ID_CLIENTE_REMETENTE = PCR.ID_PESSOA ")
                .append("JOIN LMS_PD.ENDERECO_PESSOA EPR ON PCR.ID_ENDERECO_PESSOA = EPR.ID_ENDERECO_PESSOA ")
                .append("JOIN LMS_PD.MUNICIPIO MUNR ON EPR.ID_MUNICIPIO = MUNR.ID_MUNICIPIO ")
                .append("JOIN LMS_PD.UNIDADE_FEDERATIVA UFR ON MUNR.ID_UNIDADE_FEDERATIVA = UFR.ID_UNIDADE_FEDERATIVA ")
                .append("JOIN LMS_PD.PESSOA PCD ON DS.ID_CLIENTE_DESTINATARIO = PCD.ID_PESSOA ")
                .append("JOIN LMS_PD.ENDERECO_PESSOA EPD ON PCD.ID_ENDERECO_PESSOA = EPD.ID_ENDERECO_PESSOA ")
                .append("JOIN LMS_PD.MUNICIPIO MUND ON EPD.ID_MUNICIPIO = MUND.ID_MUNICIPIO ")
                .append("JOIN LMS_PD.UNIDADE_FEDERATIVA UFD ON MUND.ID_UNIDADE_FEDERATIVA = UFD.ID_UNIDADE_FEDERATIVA ")
                .append("WHERE DS.TP_DOCUMENTO_SERVICO = 'CTE' ")
                .append("AND FAT.ID_FATURA = :idFatura");

        Map<String, Object> param = new HashMap<>();
        param.put("idFatura", idFatura);

        return getAdsmHibernateTemplate().findBySql(query.toString(), param, configureSqlQueryEventoDadosFaturaDellTraxCte());
    }

    private ConfigureSqlQuery configureSqlQueryEventoDadosFaturaDellTraxCte(){
        return new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("ID_SERVICO", Hibernate.LONG);
                sqlQuery.addScalar("ID_FATURA", Hibernate.LONG);
                sqlQuery.addScalar("NR_FATURA", Hibernate.STRING);
                sqlQuery.addScalar("EMIS_FAT", Hibernate.STRING);
                sqlQuery.addScalar("CNPJ_EMIT_FAT", Hibernate.STRING);
                sqlQuery.addScalar("CNPJ_CLI_FAT", Hibernate.STRING);
                sqlQuery.addScalar("VL_TOTAL", Hibernate.STRING);
                sqlQuery.addScalar("DT_VENCIMENTO", Hibernate.STRING);
                sqlQuery.addScalar("NR_DOCTO_SERVICO", Hibernate.STRING);
                sqlQuery.addScalar("EMIS_CTE", Hibernate.STRING);
                sqlQuery.addScalar("nr_cfop", Hibernate.STRING);
                sqlQuery.addScalar("NR_FIL_ORIG", Hibernate.STRING);
                sqlQuery.addScalar("NR_CLI_REM", Hibernate.STRING);
                sqlQuery.addScalar("NR_CLI_DEST", Hibernate.STRING);
                sqlQuery.addScalar("VL_DEVIDO", Hibernate.STRING);
                sqlQuery.addScalar("VL_BASE_CALC_IMPOSTO", Hibernate.STRING);
                sqlQuery.addScalar("VL_IMPOSTO", Hibernate.STRING);
                sqlQuery.addScalar("VL_ICMS_ST", Hibernate.STRING);
                sqlQuery.addScalar("PIS", Hibernate.STRING);
                sqlQuery.addScalar("COFINS", Hibernate.STRING);
                sqlQuery.addScalar("QT_VOLUMES", Hibernate.STRING);
                sqlQuery.addScalar("PS_REAL", Hibernate.STRING);
                sqlQuery.addScalar("TP_FRETE", Hibernate.STRING);
                sqlQuery.addScalar("PS_REFERENCIA_CALCULO", Hibernate.STRING);
                sqlQuery.addScalar("VL_FRETE_PESO", Hibernate.STRING);
                sqlQuery.addScalar("VL_DESPACHO", Hibernate.STRING);
                sqlQuery.addScalar("VL_PEDAGIO", Hibernate.STRING);
                sqlQuery.addScalar("TP_CONHECIMENTO", Hibernate.STRING);
                sqlQuery.addScalar("NR_CHAVE", Hibernate.STRING);
                sqlQuery.addScalar("DS_SITUACAO_DOC", Hibernate.STRING);
                sqlQuery.addScalar("CNPJ_EMIT_NFC", Hibernate.STRING);
                sqlQuery.addScalar("DS_SERIE", Hibernate.STRING);
                sqlQuery.addScalar("NR_NOTA_FISCAL", Hibernate.STRING);
                sqlQuery.addScalar("DT_EMISSAO", Hibernate.STRING);
                sqlQuery.addScalar("NFC_VL_TOTAL", Hibernate.STRING);
                sqlQuery.addScalar("TP_MODAL", Hibernate.STRING);
                sqlQuery.addScalar("PS_CUBADO_AFERIDO", Hibernate.STRING);
                sqlQuery.addScalar("NOME_REM", Hibernate.STRING);
                sqlQuery.addScalar("ENDERECO_REM", Hibernate.STRING);
                sqlQuery.addScalar("MUNICIPIO_REM", Hibernate.STRING);
                sqlQuery.addScalar("UF_REM", Hibernate.STRING);
                sqlQuery.addScalar("NOME_DEST", Hibernate.STRING);
                sqlQuery.addScalar("ENDERECO_DEST", Hibernate.STRING);
                sqlQuery.addScalar("MUNICIPIO_DEST", Hibernate.STRING);
                sqlQuery.addScalar("UF_DEST", Hibernate.STRING);
                sqlQuery.addScalar("VL_ADVALOREM", Hibernate.STRING);
                sqlQuery.addScalar("VL_TDE", Hibernate.STRING);
                sqlQuery.addScalar("DT_ENTREGA", Hibernate.STRING);
                sqlQuery.addScalar("ID_CLIENTE", Hibernate.LONG);
                sqlQuery.addScalar("VL_AGENDAMENTO", Hibernate.STRING);
                sqlQuery.addScalar("VL_GRIS", Hibernate.STRING);
                sqlQuery.addScalar("VL_PALETE", Hibernate.STRING);
                sqlQuery.addScalar("VL_FRETE_VALOR", Hibernate.STRING);
                sqlQuery.addScalar("FILIAL_CTRC", Hibernate.STRING);
                sqlQuery.addScalar("VL_TRT", Hibernate.STRING);
                sqlQuery.addScalar("VL_TAS_ADM", Hibernate.STRING);
                sqlQuery.addScalar("VL_SUFRAMA", Hibernate.STRING);
                sqlQuery.addScalar("VL_EMEX", Hibernate.STRING);
                sqlQuery.addScalar("TOMADOR", Hibernate.STRING);
            }
        };
    }
    
    public List<Object[]> findNotasEventoDoctoServicoBosch
    (Long idEventoDocumentoServico, List<String> cnpjParcial, String nmTabelaOcorren){
        StringBuilder query = new StringBuilder();

        query.append("SELECT NFC.NR_NOTA_FISCAL AS numeroNotaFiscaL, NFC.DS_SERIE AS serieNotaFiscal, ");
        query.append("to_char(EDS.DH_EVENTO,'RRRRMMDD') AS dataOcorrencia, ");
        query.append("to_char(EDS.DH_EVENTO,'HH24MISS') AS horaSaida, cd_ocorrencia_cliente AS codigoOcorrencia, ");
        query.append("SUBSTR(REPLACE(REPLACE(ETOD.DS_OCORRENCIA,'pt_BR»',''),'¦',''),1,30) AS descricaoOcorrencia, ");
        query.append("OB_COMPLEMENTO AS observacaoOcorrencia, 'D' AS identificacaoEntreda, DDS.ID_CLIENTE AS idCliente, ");
        query.append("PC.CD_COLETA_CLIENTE AS nrNotaQM ");
        query.append("FROM DEVEDOR_DOC_SERV DDS, NOTA_FISCAL_CONHECIMENTO NFC, EDI_TABELA_OCOREN ETO, ");
        query.append("EDI_TABELA_CLIENTE ETC, EDI_TABELA_OCOREN_DET ETOD, EVENTO_DOCUMENTO_SERVICO EDS, ");
        query.append("DOCTO_SERVICO DOS, PEDIDO_COLETA PC ");
        query.append("WHERE DDS.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO AND DDS.ID_DOCTO_SERVICO = NFC.ID_CONHECIMENTO ");
        query.append("AND ETO.ID_EDI_TABELA_OCOREN = ETC.ID_EDI_TABELA_OCOREN ");
        query.append("AND ETC.ID_EDI_TABELA_OCOREN = ETOD.ID_EDI_TABELA_OCOREN AND DDS.ID_CLIENTE = ETC.ID_CLIENTE ");
        query.append("AND PC.id_pedido_coleta = DOS.id_pedido_coleta AND DOS.id_docto_servico = DDS.id_docto_servico ");
        query.append("AND EDS.ID_EVENTO = ETOD.ID_LMS AND EDS.BL_EVENTO_CANCELADO = 'N' ");
        query.append("AND ETO.NM_TABELA_OCOREN = :nmTabelaOcorren ");
        query.append("AND EDS.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDoctoServico ");
        query.append("AND EXISTS (select 1 from pessoa p ");
        query.append("              where p.id_pessoa = dds.ID_CLIENTE and nr_identificacao in (:cnpjParcial))");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("idEventoDoctoServico", idEventoDocumentoServico);
        param.put("cnpjParcial", cnpjParcial);
        param.put("nmTabelaOcorren", nmTabelaOcorren);

        List<Object[]> notasEvento = getAdsmHibernateTemplate()
                .findBySql(query.toString(), param, configureSqlQueryEventoDoctoServicoBosch());
        return notasEvento;
    }

    private ConfigureSqlQuery configureSqlQueryEventoDoctoServicoBosch(){
        return new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("numeroNotaFiscaL", Hibernate.LONG);
                sqlQuery.addScalar("serieNotaFiscal", Hibernate.STRING);
                sqlQuery.addScalar("dataOcorrencia", Hibernate.STRING);
                sqlQuery.addScalar("horaSaida", Hibernate.STRING);
                sqlQuery.addScalar("codigoOcorrencia", Hibernate.STRING);
                sqlQuery.addScalar("descricaoOcorrencia", Hibernate.STRING);
                sqlQuery.addScalar("observacaoOcorrencia", Hibernate.STRING);
                sqlQuery.addScalar("identificacaoEntreda", Hibernate.STRING);
                sqlQuery.addScalar("idCliente", Hibernate.LONG);
                sqlQuery.addScalar("nrNotaQM", Hibernate.STRING);

            }
        };
    }


    public List<Object[]> findNotasEventoBlackDecker(Long idEventoDocumentoServico, String tabelaOcoren, List<String> cnpjParcial) {
        StringBuilder query = mountProjectionBlackDecker();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("tabelaOcoren", tabelaOcoren);
        param.put("idEventoDoctoServico", idEventoDocumentoServico);
        param.put("cnpjParcial", cnpjParcial);
        return getAdsmHibernateTemplate().findBySql(query.toString(), param, configureSqlQueryBlackDecker());
    }

    private StringBuilder mountProjectionBlackDecker() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT NFC.NR_NOTA_FISCAL AS numero, ")
                .append("       NFC.DS_SERIE AS serie, ")
                .append("       EMBARCADOR.NR_IDENTIFICACAO AS cnpj, ")
                .append("       ETOD.CD_OCORRENCIA_CLIENTE AS tipoEntrega, ")
                .append("       to_char(EDS.DH_EVENTO,'dd-mm-rrrr') AS dtOcorrencia, ")
                .append("       ETC.ID_CLIENTE as idCliente ")
                .append("  FROM DEVEDOR_DOC_SERV DDS ")
                .append("       ,NOTA_FISCAL_CONHECIMENTO NFC ")
                .append("       ,EDI_TABELA_OCOREN ETO ")
                .append("       ,EDI_TABELA_CLIENTE ETC ")
                .append("       ,EDI_TABELA_OCOREN_DET ETOD ")
                .append("       ,EVENTO_DOCUMENTO_SERVICO EDS ")
                .append("       ,PESSOA EMBARCADOR ")
                .append(" WHERE DDS.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO ")
                .append("   AND DDS.ID_DOCTO_SERVICO = NFC.ID_CONHECIMENTO ")
                .append("   AND ETO.ID_EDI_TABELA_OCOREN = ETC.ID_EDI_TABELA_OCOREN ")
                .append("   AND ETC.ID_EDI_TABELA_OCOREN = ETOD.ID_EDI_TABELA_OCOREN ")
                .append("   AND DDS.ID_CLIENTE = ETC.ID_CLIENTE ")
                .append("   AND ((ETOD.TP_OCORRENCIA = 1 AND EDS.ID_OCORRENCIA_ENTREGA = ETOD.ID_LMS )  ")
                .append("      OR(ETOD.TP_OCORRENCIA = 2 AND EDS.ID_OCORRENCIA_PENDENCIA = ETOD.ID_LMS )  ")
                .append("      OR(ETOD.TP_OCORRENCIA = 3 AND EDS.ID_EVENTO = ETOD.ID_LMS )) ")
                .append("   AND EMBARCADOR.ID_PESSOA = DDS.ID_CLIENTE ")
                .append("   AND EDS.BL_EVENTO_CANCELADO = 'N' ")
                .append("   AND ETO.NM_TABELA_OCOREN =  :tabelaOcoren ")
                .append("   AND EDS.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDoctoServico ")
                .append("   AND EXISTS(SELECT 1 FROM PESSOA P WHERE P.ID_PESSOA = DDS.ID_CLIENTE and substr(P.NR_IDENTIFICACAO,1,8) IN (:cnpjParcial)) ");
        return query;
    }

    private ConfigureSqlQuery configureSqlQueryBlackDecker(){
        return  new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("numero" , Hibernate.STRING);
                sqlQuery.addScalar("serie" , Hibernate.STRING);
                sqlQuery.addScalar("cnpj" , Hibernate.STRING);
                sqlQuery.addScalar("tipoEntrega" , Hibernate.STRING);
                sqlQuery.addScalar("dtOcorrencia" , Hibernate.STRING);
                sqlQuery.addScalar("idCliente", Hibernate.LONG);
            }
        };
    }

    public List<Object[]> findNotasEventoDadosFaturaDell(Long idFatura) {

        StringBuilder query = new StringBuilder();

        query.append("SELECT  mde.nr_documento_eletronico as documentNumber, ")
                .append("       FAT.NR_FATURA AS nrFatura, ")
                .append("       to_char( trunc( CAST(FAT.DT_EMISSAO AS DATE) ), 'ddMMyyyy' ) AS documentIssueDate, ")
                .append("       PEF.NR_IDENTIFICACAO AS issuersCnpj, ")
                .append("       PES.NR_IDENTIFICACAO AS recipientsCnpj, ")
                .append("       FAT.VL_TOTAL AS goodsTotalAmount, ")
                .append("       (SELECT DS.NR_CFOP FROM LMS_PD.ITEM_FATURA ITF, ")
                .append("		DEVEDOR_DOC_SERV_FAT DDSF, ")
                .append("		DOCTO_SERVICO DS ")
                .append("		WHERE FAT.ID_FATURA = ITF.ID_FATURA ")
                .append("		AND ITF.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT ")
                .append("		AND DDSF.ID_DOCTO_SERVICO  = DS.ID_DOCTO_SERVICO AND ROWNUM = 1) AS documentoCfop, ")
                .append("		FAT.ID_CLIENTE AS idCliente, ")
                .append("		DS.VL_TOTAL_DOC_SERVICO AS vlTotalDocServico")
                .append(" FROM PESSOA PES, ")
                .append("       PESSOA PEF, ")
                .append("       FATURA FAT, ")
                .append( "      ITEM_FATURA ITF, ")
                .append("       DEVEDOR_DOC_SERV_FAT DDSF,  ")
                .append("       monitoramento_doc_eletronico MDE, ")
                .append("       DOCTO_SERVICO DS ")
                .append("		WHERE FAT.ID_CLIENTE = PES.ID_PESSOA ")
                .append(" AND FAT.ID_FILIAL = PEF.ID_PESSOA ")
                .append(" AND FAT.ID_FATURA = ITF.ID_FATURA ")
                .append(" AND ITF.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT ")
                .append(" AND DDSF.ID_DOCTO_SERVICO  = MDE.ID_DOCTO_SERVICO ")
                .append(" AND DDSF.ID_DOCTO_SERVICO  = DS.ID_DOCTO_SERVICO ")
                .append(" AND FAT.ID_FATURA = :idFatura ");

        Map<String, Object> param = new HashMap<>();
        param.put("idFatura", idFatura);
        List<Object[]> dadosFatura = getAdsmHibernateTemplate()
                .findBySql(query.toString(), param, configureSqlQueryEventoDadosFaturaDell());
        return dadosFatura;
    }

    private ConfigureSqlQuery configureSqlQueryEventoDadosFaturaDell(){
        return new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("documentNumber", Hibernate.STRING);
                sqlQuery.addScalar("documentIssueDate", Hibernate.STRING);
                sqlQuery.addScalar("issuersCnpj", Hibernate.STRING);
                sqlQuery.addScalar("recipientsCnpj", Hibernate.STRING);
                sqlQuery.addScalar("goodsTotalAmount", Hibernate.STRING);
                sqlQuery.addScalar("documentoCfop", Hibernate.STRING);
                sqlQuery.addScalar("idCliente", Hibernate.LONG);
                sqlQuery.addScalar("nrFatura", Hibernate.STRING);
                sqlQuery.addScalar("vlTotalDocServico", Hibernate.STRING);
            }
        };
    }

    public List<Object[]> findNotasEventoDadosNfseDell(Long idFatura) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT  mde.nr_documento_eletronico AS documentNumber, ")
                .append("       to_char( trunc( CAST(DS.DH_EMISSAO AS DATE) ), 'ddMMyyyy' ) AS documentIssueDate, ")
                .append("       DS.NR_CFOP AS documentCFOP, ")
                .append(" 		CASE WHEN DS.TP_DOCUMENTO_SERVICO = 'NTE' ")
                .append("       THEN (select to_number(replace(DS_CONTEUDO,'.','')) from parametro_geral where nm_parametro_geral = 'COD_TRIB_TRANSPORTE') ")
                .append(" 		ELSE (SELECT SOT.NR_SERVICO_OFICIAL_TRIBUTO ")
                .append(" 		FROM SERV_ADICIONAL_DOC_SERV sadc, ")
                .append("		SERVICO_ADICIONAL sa, ")
                .append("		SERVICO_OFICIAL_TRIBUTO SOT ")
                .append("		WHERE SADC.ID_SERVICO_ADICIONAL =  SA.ID_SERVICO_ADICIONAL ")
                .append(" 		AND SA.ID_SERVICO_OFICIAL_TRIBUTO = SOT.ID_SERVICO_OFICIAL_TRIBUTO ")
                .append(" 		AND SADC.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO) END AS itemCode, ")
                .append(" 		(SELECT IMS.PC_ALIQUOTA ")
                .append(" 		FROM IMPOSTO_SERVICO IMS ")
                .append(" 		WHERE NFS.ID_NOTA_FISCAL_SERVICO = IMS.ID_NOTA_FISCAL_SERVICO ")
                .append(" 		AND IMS.TP_IMPOSTO = 'IS') AS taxaIss, ")
                .append("       DS.VL_TOTAL_DOC_SERVICO AS totalPrice, ")
                .append(" 		(SELECT IMS.VL_IMPOSTO ")
                .append("		FROM IMPOSTO_SERVICO IMS ")
                .append("		WHERE NFS.ID_NOTA_FISCAL_SERVICO = IMS.ID_NOTA_FISCAL_SERVICO ")
                .append("		AND IMS.TP_IMPOSTO = 'IS') AS issValue")
                .append("		FROM FATURA FAT, ")
                .append("		ITEM_FATURA ITF, ")
                .append(" 		DEVEDOR_DOC_SERV_FAT DDSF, ")
                .append(" 		DOCTO_SERVICO DS, ")
                .append(" 		NOTA_FISCAL_SERVICO NFS, ")
                .append(" 		monitoramento_doc_eletronico mde ")
                .append("		WHERE FAT.ID_FATURA = ITF.ID_FATURA ")
                .append(" 		AND ITF.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT ")
                .append(" 		AND DDSF.ID_DOCTO_SERVICO  = DS.ID_DOCTO_SERVICO ")
                .append("		AND DS.ID_DOCTO_SERVICO = mde.ID_DOCTO_SERVICO ")
                .append("		AND DS.ID_DOCTO_SERVICO = NFS.ID_NOTA_FISCAL_SERVICO(+) ")
                .append("       AND FAT.ID_FATURA = :idFatura ");

        Map<String, Object> param = new HashMap<>();
        param.put("idFatura", idFatura);
        List<Object[]> dadosNfse = getAdsmHibernateTemplate()
                .findBySql(query.toString(), param, configureSqlQueryEventoDadosNfseDell());
        return dadosNfse;
    }

    private ConfigureSqlQuery configureSqlQueryEventoDadosNfseDell(){
        return new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("documentNumber", Hibernate.STRING);
                sqlQuery.addScalar("documentIssueDate", Hibernate.STRING);
                sqlQuery.addScalar("documentCFOP", Hibernate.STRING);
                sqlQuery.addScalar("itemCode", Hibernate.STRING);
                sqlQuery.addScalar("taxaIss", Hibernate.STRING);
                sqlQuery.addScalar("totalPrice", Hibernate.STRING);
                sqlQuery.addScalar("issValue", Hibernate.STRING);
            }
        };
    }

    public List<Object[]> findNotasEventoDadosComplementoFaturaDell(Long idFatura) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT PEF.NR_IDENTIFICACAO AS issuersCnpj, ")
        .append("       FAT.NR_FATURA AS documentNumber, ")
        .append("       to_char( trunc( CAST(FAT.DT_EMISSAO AS DATE) ), 'ddMMyyyy' ) AS documentIssueDate, ")
        .append("       PES.NR_IDENTIFICACAO AS recipientsCnpj, ")
        .append("       to_char( trunc( CAST(FAT.DT_VENCIMENTO AS DATE) ), 'ddMMyyyy' ) AS dueDate, ")
        .append("       FAT.VL_TOTAL AS installmentAmount ")
        .append("  FROM FATURA FAT, ")
        .append("       PESSOA PES, ")
        .append("       PESSOA PEF ")
        .append(" WHERE FAT.ID_CLIENTE = PES.ID_PESSOA ")
        .append("   AND FAT.ID_FILIAL = PEF.ID_PESSOA ")
        .append("   AND FAT.ID_FATURA = :idFatura ");

        Map<String, Object> param = new HashMap<>();
        param.put("idFatura", idFatura);
        List<Object[]> dadosComplementoFatura = getAdsmHibernateTemplate()
                .findBySql(query.toString(), param, configureSqlQueryEventoDadosComplementoFaturaDell());
        return dadosComplementoFatura;
    }

    private ConfigureSqlQuery configureSqlQueryEventoDadosComplementoFaturaDell(){
        return new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("issuersCnpj", Hibernate.STRING);
                sqlQuery.addScalar("documentNumber", Hibernate.STRING);
                sqlQuery.addScalar("documentIssueDate", Hibernate.STRING);
                sqlQuery.addScalar("recipientsCnpj", Hibernate.STRING);
                sqlQuery.addScalar("dueDate", Hibernate.STRING);
                sqlQuery.addScalar("installmentAmount", Hibernate.STRING);
            }
        };
    }

    public List<Object[]> findNotasEventoDadosComplementoNfseDell(Long idFatura) {
        StringBuilder query = new StringBuilder();
        query.append(" SELECT DS.NR_DOCTO_SERVICO AS numeroOrdemDell, ")
        .append(" to_char( trunc( CAST(DS.DH_EMISSAO AS DATE) ), 'ddMMyyyy' ) AS dataColeta, ")
        .append(" EPR.DS_ENDERECO ||','||EPR.NR_ENDERECO AS enderecoOrigem1, ")
        .append(" MUNR.NM_MUNICIPIO AS cidadeOrigem, ")
        .append(" UFR.SG_UNIDADE_FEDERATIVA AS ufOrigem, ")
        .append(" PESR.NM_PESSOA AS nomeParaEntrega, ")
        .append(" EPD.DS_ENDERECO ||','||EPD.NR_ENDERECO AS enderecoEntrega, ")
        .append(" MUND.NM_MUNICIPIO AS cidadeEntrega, ")
        .append(" UFD.SG_UNIDADE_FEDERATIVA AS ufEntrega, ")
        .append(" CASE WHEN DS.TP_DOCUMENTO_SERVICO = 'NSE' ")
        .append(" THEN 0 ")
        .append(" WHEN DS.TP_DOCUMENTO_SERVICO = 'NTE' ")
        .append(" THEN (SELECT SUM(NFC.VL_TOTAL) ")
        .append(" FROM NOTA_FISCAL_CONHECIMENTO NFC ")
        .append(" WHERE DS.ID_DOCTO_SERVICO = NFC.ID_CONHECIMENTO) END AS valorTotalOrdem, ")
        .append(" (SELECT PDS.VL_PARCELA_BRUTO ")
        .append(" FROM PARCELA_DOCTO_SERVICO PDS, ")
        .append(" PARCELA_PRECO PP ")
        .append(" WHERE DS.ID_DOCTO_SERVICO = PDS.ID_DOCTO_SERVICO ")
        .append(" AND PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ")
        .append(" AND PP.CD_PARCELA_PRECO in ('IDAdvalorem', 'IDAdvalorem1', 'IDAdvalorem2', 'IDFreteValor')) AS adValorem, ")
        .append(" DS.PS_REFERENCIA_CALCULO AS tipoServico, ")
        .append(" (SELECT IMS.VL_IMPOSTO ")
        .append(" FROM IMPOSTO_SERVICO IMS ")
        .append(" WHERE IMS.ID_NOTA_FISCAL_SERVICO = NFS.ID_NOTA_FISCAL_SERVICO ")
        .append(" AND IMS.TP_IMPOSTO = 'IS') AS valorTotalIss, ")
        .append(" (SELECT PDS.VL_PARCELA ")
        .append(" FROM PARCELA_DOCTO_SERVICO PDS, ")
        .append(" PARCELA_PRECO PP ")
        .append(" WHERE DS.ID_DOCTO_SERVICO = PDS.ID_DOCTO_SERVICO ")
        .append(" AND PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ")
        .append(" AND PP.CD_PARCELA_PRECO = 'IDDespacho') AS valorTotalDespacho, ")
        .append(" (SELECT PDS.VL_PARCELA_BRUTO ")
        .append(" FROM PARCELA_DOCTO_SERVICO PDS, ")
        .append(" PARCELA_PRECO PP ")
        .append(" WHERE DS.ID_DOCTO_SERVICO = PDS.ID_DOCTO_SERVICO ")
        .append(" AND PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ")
        .append(" AND PP.CD_PARCELA_PRECO in('IDAgendamentoColeta', 'IDAgendamentoColetaEntrega', 'IDAgendamentoEntregaSDF')) AS totalAgenda, ")
        .append(" (SELECT PDS.VL_PARCELA_BRUTO ")
        .append(" FROM PARCELA_DOCTO_SERVICO PDS, ")
        .append(" PARCELA_PRECO PP ")
        .append(" WHERE DS.ID_DOCTO_SERVICO = PDS.ID_DOCTO_SERVICO ")
        .append(" AND PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ")
        .append(" AND PP.CD_PARCELA_PRECO = 'IDGris') AS valorTotalGris, ")
        .append(" (SELECT PDS.VL_PARCELA_BRUTO ")
        .append(" FROM PARCELA_DOCTO_SERVICO PDS, ")
        .append(" PARCELA_PRECO PP ")
        .append(" WHERE DS.ID_DOCTO_SERVICO = PDS.ID_DOCTO_SERVICO ")
        .append(" AND PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ")
        .append(" AND PP.CD_PARCELA_PRECO = 'IDPedagio') AS valorTotalPedagio, ")
        .append(" CASE WHEN DS.TP_DOCUMENTO_SERVICO = 'NSE' ")
        .append(" THEN (SELECT PDS.VL_PARCELA ")
        .append(" FROM PARCELA_DOCTO_SERVICO PDS ")
        .append(" WHERE DS.ID_DOCTO_SERVICO = PDS.ID_DOCTO_SERVICO) - DS.VL_IMPOSTO ")
        .append(" WHEN DS.TP_DOCUMENTO_SERVICO = 'NTE' ")
        .append(" THEN (SELECT PDS.VL_PARCELA ")
        .append(" FROM PARCELA_DOCTO_SERVICO PDS, ")
        .append(" PARCELA_PRECO PP ")
        .append(" WHERE DS.ID_DOCTO_SERVICO = PDS.ID_DOCTO_SERVICO ")
        .append(" AND PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ")
        .append(" AND PP.CD_PARCELA_PRECO = 'IDFretePeso') END AS valorTotalFrete, ")
        .append(" to_char( trunc( CAST(FAT.DT_VENCIMENTO AS DATE) ), 'ddMMyyyy' ) AS vencimentoFatura, ")
        .append(" F.SG_FILIAL ||to_char(ds.DH_EMISSAO,'rrmmdd') || lpad(ds.NR_DOCTO_SERVICO ,9,0) AS filCtrc, ")
        .append(" to_char( trunc( CAST(DS.DH_EMISSAO AS DATE) ), 'ddMMyyyy' ) AS dataEmissaoCte, ")
        .append(" DS.NR_DOCTO_SERVICO AS numeroCte, ")
        .append(" FAT.VL_TOTAL AS valorTotalInvoice, ")
        .append(" DS.PS_REAL AS chargeableWeight, ")
        .append(" (SELECT IMS.PC_ALIQUOTA ")
        .append(" FROM IMPOSTO_SERVICO IMS ")
        .append(" WHERE NFS.ID_NOTA_FISCAL_SERVICO = IMS.ID_NOTA_FISCAL_SERVICO ")
        .append(" AND IMS.TP_IMPOSTO = 'IS') AS valorIssRate, ")
        .append(" (SELECT pg.DS_CONTEUDO FROM lms_pd.PARAMETRO_GERAL pg WHERE pg.NM_PARAMETRO_GERAL = 'PC_PIS_INTEGRACAO_DELL') AS valorPisRate, ")
        .append(" (SELECT pg.DS_CONTEUDO FROM lms_pd.PARAMETRO_GERAL pg WHERE pg.NM_PARAMETRO_GERAL = 'PC_COFINS_INTEGRACAO_DELL') AS valorCofinsRate, ")
        .append(" DS.TP_DOCUMENTO_SERVICO AS tpDoctoServico, ")
        .append(" FAT.ID_FATURA AS idFatura, ")
        .append(" to_char( trunc( CAST(FAT.DT_EMISSAO AS DATE) ), 'ddMMyyyy' ) AS dataEmissaoFatura ")
        .append(" FROM FATURA FAT, ")
        .append(" ITEM_FATURA ITF, ")
        .append(" DEVEDOR_DOC_SERV_FAT DDSF, ")
        .append(" DOCTO_SERVICO DS, ")
        .append(" NOTA_FISCAL_SERVICO NFS, ")
        .append(" PESSOA PESR, ")
        .append(" INSCRICAO_ESTADUAL IER, ")
        .append(" ENDERECO_PESSOA EPR, ")
        .append(" MUNICIPIO MUNR, ")
        .append(" UNIDADE_FEDERATIVA UFR, ")
        .append(" PESSOA PESD, ")
        .append(" INSCRICAO_ESTADUAL IED, ")
        .append(" ENDERECO_PESSOA EPD, ")
        .append(" MUNICIPIO MUND, ")
        .append(" UNIDADE_FEDERATIVA UFD, ")
        .append(" PESSOA PESFE, ")
        .append(" FILIAL F ")
        .append(" WHERE FAT.ID_FATURA = ITF.ID_FATURA")
        .append(" AND ITF.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT")
        .append(" AND DDSF.ID_DOCTO_SERVICO  = DS.ID_DOCTO_SERVICO")
        .append(" AND DS.ID_DOCTO_SERVICO = NFS.ID_NOTA_FISCAL_SERVICO(+)")
        .append(" AND DS.ID_CLIENTE_REMETENTE = PESR.ID_PESSOA ")
        .append(" AND DS.ID_IE_REMETENTE = IER.ID_INSCRICAO_ESTADUAL ")
        .append(" AND PESR.ID_ENDERECO_PESSOA = EPR.ID_ENDERECO_PESSOA ")
        .append(" AND EPR.ID_MUNICIPIO = MUNR.ID_MUNICIPIO ")
        .append(" AND MUNR.ID_UNIDADE_FEDERATIVA  = UFR.ID_UNIDADE_FEDERATIVA ")
        .append(" AND DS.ID_IE_DESTINATARIO = IED.ID_INSCRICAO_ESTADUAL ")
        .append(" AND DS.ID_CLIENTE_DESTINATARIO = PESD.ID_PESSOA ")
        .append(" AND PESD.ID_ENDERECO_PESSOA  = EPD.ID_ENDERECO_PESSOA ")
        .append(" AND EPD.ID_MUNICIPIO = MUND.ID_MUNICIPIO ")
        .append(" AND MUND.ID_UNIDADE_FEDERATIVA  = UFD.ID_UNIDADE_FEDERATIVA ")
        .append(" AND DS.ID_FILIAL_ORIGEM = PESFE.ID_PESSOA ")
        .append(" AND DS.ID_FILIAL_ORIGEM = F.ID_FILIAL ")
        .append(" AND FAT.ID_FATURA = :idFatura ");

        Map<String, Object> param = new HashMap<>();
        param.put("idFatura", idFatura);
        List<Object[]> dadosComplementoFatura = getAdsmHibernateTemplate()
                .findBySql(query.toString(), param, configureSqlQueryEventoDadosComplementoNfseDell());
        return dadosComplementoFatura;
    }

    private ConfigureSqlQuery configureSqlQueryEventoDadosComplementoNfseDell(){
        return new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("numeroOrdemDell", Hibernate.STRING);
                sqlQuery.addScalar("dataColeta", Hibernate.STRING);
                sqlQuery.addScalar("enderecoOrigem1", Hibernate.STRING);
                sqlQuery.addScalar("cidadeOrigem", Hibernate.STRING);
                sqlQuery.addScalar("ufOrigem", Hibernate.STRING);
                sqlQuery.addScalar("nomeParaEntrega", Hibernate.STRING);
                sqlQuery.addScalar("enderecoEntrega", Hibernate.STRING);
                sqlQuery.addScalar("cidadeEntrega", Hibernate.STRING);
                sqlQuery.addScalar("ufEntrega", Hibernate.STRING);
                sqlQuery.addScalar("valorTotalOrdem", Hibernate.STRING);
                sqlQuery.addScalar("adValorem", Hibernate.STRING);
                sqlQuery.addScalar("valorTotalIss", Hibernate.STRING);
                sqlQuery.addScalar("valorTotalDespacho", Hibernate.STRING);
                sqlQuery.addScalar("totalAgenda", Hibernate.STRING);
                sqlQuery.addScalar("valorTotalGris", Hibernate.STRING);
                sqlQuery.addScalar("valorTotalPedagio", Hibernate.STRING);
                sqlQuery.addScalar("valorTotalFrete", Hibernate.STRING);
                sqlQuery.addScalar("vencimentoFatura", Hibernate.STRING);
                sqlQuery.addScalar("filCtrc", Hibernate.STRING);
                sqlQuery.addScalar("dataEmissaoCte", Hibernate.STRING);
                sqlQuery.addScalar("numeroCte", Hibernate.STRING);
                sqlQuery.addScalar("valorTotalInvoice", Hibernate.STRING);
                sqlQuery.addScalar("chargeableWeight", Hibernate.STRING);
                sqlQuery.addScalar("valorIssRate", Hibernate.STRING);
                sqlQuery.addScalar("valorPisRate", Hibernate.STRING);
                sqlQuery.addScalar("valorCofinsRate", Hibernate.STRING);
                sqlQuery.addScalar("tipoServico", Hibernate.STRING);
                sqlQuery.addScalar("tpDoctoServico", Hibernate.STRING);
                sqlQuery.addScalar("idFatura", Hibernate.STRING);
                sqlQuery.addScalar("dataEmissaoFatura", Hibernate.STRING);
            }
        };
    }

    public List<Object[]> findEnvioCteToutBox(Long idEventoDocumentoServico) {

        String query = montaSelectEnvioCteToutBox();

        Map<String, Object> param = new HashMap<>();
        param.put("idEventoDocumentoServico", idEventoDocumentoServico);

        return getAdsmHibernateTemplate().findBySql(query, param,
                sqlQuery -> {
                    sqlQuery.addScalar("NR_CHAVE" , Hibernate.STRING);
                    sqlQuery.addScalar("VL_TOTAL_DOC_SERVICO" , Hibernate.DOUBLE);
                    sqlQuery.addScalar("NR_DIAS_PREV_ENTREGA" , Hibernate.LONG);
                    sqlQuery.addScalar("ID_CLIENTE" , Hibernate.LONG);
                    sqlQuery.addScalar("ID_DOCTO_SERVICO" , Hibernate.LONG);
                }
        );

    }

    private String montaSelectEnvioCteToutBox() {

        return  " SELECT NFC.NR_CHAVE, " +
                "DS.VL_TOTAL_DOC_SERVICO, " +
                "DS.NR_DIAS_PREV_ENTREGA, " +
                "DDS.ID_CLIENTE, " +
                "DS.ID_DOCTO_SERVICO " +
                "FROM LMS_PD.EVENTO_DOCUMENTO_SERVICO EDS, LMS_PD.DOCTO_SERVICO DS, LMS_PD.DEVEDOR_DOC_SERV DDS, " +
                "LMS_PD.NOTA_FISCAL_CONHECIMENTO NFC, LMS_PD.EDI_TABELA_OCOREN ETO, LMS_PD.EDI_TABELA_CLIENTE ETC, LMS_PD.EDI_TABELA_OCOREN_DET ETOD " +
                "WHERE DS.ID_DOCTO_SERVICO = DDS.ID_DOCTO_SERVICO " +
                "AND DDS.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO " +
                "AND DDS.ID_DOCTO_SERVICO = NFC.ID_CONHECIMENTO " +
                "AND ETO.ID_EDI_TABELA_OCOREN = ETC.ID_EDI_TABELA_OCOREN " +
                "AND ETC.ID_EDI_TABELA_OCOREN = ETOD.ID_EDI_TABELA_OCOREN " +
                "AND DDS.ID_CLIENTE = ETC.ID_CLIENTE " +
                "AND EDS.ID_EVENTO = ETOD.ID_LMS " +
                "AND ETOD.CD_OCORRENCIA = '6' " +
                "AND EDS.BL_EVENTO_CANCELADO = 'N' " +
                "AND ETO.NM_TABELA_OCOREN = 'TELEFONICA_TOUTBOX' " +
                "AND EDS.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDocumentoServico ";

    }

    public List<Object[]> findOcorrenciaEntregaToutBox(Long idEventoDocumentoServico) {

        String query = montaSelectOcorrenciaEntregaToutBox();

        Map<String, Object> param = new HashMap<>();
        param.put("idEventoDocumentoServico", idEventoDocumentoServico);

        return getAdsmHibernateTemplate().findBySql(query, param,
                sqlQuery -> {
                    sqlQuery.addScalar("nfKey" , Hibernate.STRING);
                    sqlQuery.addScalar("eventCode" , Hibernate.STRING);
                    sqlQuery.addScalar("date" , Hibernate.STRING);
                    sqlQuery.addScalar("description" , Hibernate.STRING);
                    sqlQuery.addScalar("address" , Hibernate.STRING);
                    sqlQuery.addScalar("number" , Hibernate.STRING);
                    sqlQuery.addScalar("city" , Hibernate.STRING);
                    sqlQuery.addScalar("state" , Hibernate.STRING);
                    sqlQuery.addScalar("idCliente" , Hibernate.LONG);
                    sqlQuery.addScalar("idDoctoServico" , Hibernate.LONG);
                }
        );

    }

    private String montaSelectOcorrenciaEntregaToutBox() {

        return  "SELECT NFC.NR_CHAVE nfKey, " +
                "ETOD.CD_OCORRENCIA_CLIENTE eventCode, " +
                "TO_CHAR(EDS.DH_EVENTO, 'RRRR-MM-DD\"T\"HH24:MI:SS') \"date\", " +
                "ETOD.DS_OCORRENCIA description, " +
                "EP.DS_ENDERECO address, " +
                "EP.NR_ENDERECO \"number\", " +
                "MU.NM_MUNICIPIO city, " +
                "UF.SG_UNIDADE_FEDERATIVA state, " +
                "DDS.ID_CLIENTE idCliente, " +
                "DS.ID_DOCTO_SERVICO idDoctoServico " +
                "FROM LMS_PD.EVENTO_DOCUMENTO_SERVICO EDS, LMS_PD.DEVEDOR_DOC_SERV DDS, LMS_PD.NOTA_FISCAL_CONHECIMENTO NFC, " +
                "LMS_PD.EDI_TABELA_OCOREN ETO, LMS_PD.EDI_TABELA_CLIENTE ETC, LMS_PD.EDI_TABELA_OCOREN_DET ETOD, " +
                "LMS_PD.PESSOA PE, LMS_PD.ENDERECO_PESSOA EP, LMS_PD.MUNICIPIO MU, LMS_PD.UNIDADE_FEDERATIVA UF, LMS_PD.DOCTO_SERVICO DS " +
                "WHERE DS.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO " +
                "AND DDS.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO " +
                "AND DDS.ID_DOCTO_SERVICO = NFC.ID_CONHECIMENTO " +
                "AND ETO.ID_EDI_TABELA_OCOREN = ETC.ID_EDI_TABELA_OCOREN " +
                "AND ETC.ID_EDI_TABELA_OCOREN = ETOD.ID_EDI_TABELA_OCOREN " +
                "AND DDS.ID_CLIENTE = ETC.ID_CLIENTE " +
                "AND ((ETOD.TP_OCORRENCIA = 1 and EDS.ID_OCORRENCIA_ENTREGA = ETOD.ID_LMS ) " +
                "OR (ETOD.TP_OCORRENCIA = 2 and EDS.ID_OCORRENCIA_PENDENCIA = ETOD.ID_LMS ) " +
                "OR (ETOD.TP_OCORRENCIA = 3 and EDS.ID_EVENTO = ETOD.ID_LMS ) " +
                "OR (ETOD.TP_OCORRENCIA = 4 and DS.ID_LOCALIZACAO_MERCADORIA = ETOD.ID_LMS )) " +
                "AND EDS.ID_FILIAL = PE.ID_PESSOA " +
                "AND PE.ID_ENDERECO_PESSOA = EP.ID_ENDERECO_PESSOA " +
                "AND EP.ID_MUNICIPIO = MU.ID_MUNICIPIO " +
                "AND MU.ID_UNIDADE_FEDERATIVA = UF.ID_UNIDADE_FEDERATIVA " +
                "AND EDS.BL_EVENTO_CANCELADO = 'N' " +
                "AND ETO.NM_TABELA_OCOREN = 'TELEFONICA_TOUTBOX' " +
                "AND EDS.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDocumentoServico ";

    }

    public List<Object[]> findOcorrenciaEntregaIntelipost(Map<String, Object> param) {

        String query = montaSelectOcorrenciaIntelipost();

        return getAdsmHibernateTemplate().findBySql(query, param, this.configureSqlQueryIntelipost());


    }

    private String montaSelectOcorrenciaIntelipost() {
        return new StringBuilder()
                .append("SELECT ")
                .append("Embarcador.nm_pessoa Shipper,")
                .append("Embarcador.nr_identificacao shipper_federal_tax_id,")
                .append("nfc.nr_chave invoice_key,")
                .append("TO_CHAR(EDS.DH_EVENTO, 'RRRR-MM-DD\"T\"HH24:MI:SS') || TO_CHAR(SYSTIMESTAMP, 'TZH:TZM') event_date,")
                .append("ETOD.CD_OCORRENCIA_CLIENTE original_code, ")
                .append("DDS.ID_CLIENTE idCliente, ")
                .append("DDS.ID_DOCTO_SERVICO idDoctoServico ")
                .append(" FROM ")
                .append("DEVEDOR_DOC_SERV DDS")
                .append(",NOTA_FISCAL_CONHECIMENTO NFC")
                .append(",EDI_TABELA_OCOREN ETO")
                .append(",EDI_TABELA_CLIENTE ETC")
                .append(",EDI_TABELA_OCOREN_DET ETOD")
                .append(",EVENTO_DOCUMENTO_SERVICO EDS")
                .append(",PESSOA Embarcador")
                .append(" WHERE ")
                .append(" DDS.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO")
                .append(" AND DDS.ID_DOCTO_SERVICO = NFC.ID_CONHECIMENTO")
                .append(" AND ETO.ID_EDI_TABELA_OCOREN = ETC.ID_EDI_TABELA_OCOREN")
                .append(" AND ETC.ID_EDI_TABELA_OCOREN = ETOD.ID_EDI_TABELA_OCOREN")
                .append(" AND DDS.ID_CLIENTE = ETC.ID_CLIENTE")
                .append(" AND ((etod.tp_ocorrencia = 1 and EDS.id_ocorrencia_entrega = etod.id_lms )")
                .append("   or(etod.tp_ocorrencia = 2 and EDS.id_ocorrencia_pendencia = etod.id_lms )")
                .append("   or(etod.tp_ocorrencia = 3 and EDS.ID_EVENTO = etod.id_lms ))")
                .append(" AND embarcador.ID_PESSOA = DDS.ID_CLIENTE")
                .append(" AND EDS.BL_EVENTO_CANCELADO = 'N'")
                .append(" AND ETO.NM_TABELA_OCOREN = 'TELEFONICA_INTELIPOST'")
                .append(" AND EDS.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDoctoServico")
                .toString();
    }

    private ConfigureSqlQuery configureSqlQueryIntelipost(){
        return sqlQuery -> {
            sqlQuery.addScalar("shipper", Hibernate.STRING);
            sqlQuery.addScalar("shipper_federal_tax_id", Hibernate.STRING);
            sqlQuery.addScalar("invoice_key", Hibernate.STRING);
            sqlQuery.addScalar("event_date", Hibernate.STRING);
            sqlQuery.addScalar("original_code", Hibernate.STRING);
            sqlQuery.addScalar("idCliente", Hibernate.LONG);
            sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
        };
    }
}
