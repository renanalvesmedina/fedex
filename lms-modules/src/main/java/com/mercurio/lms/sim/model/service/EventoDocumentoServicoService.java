
package com.mercurio.lms.sim.model.service;

import br.com.tntbrasil.integracao.domains.dell.layout.traxcte.*;
import br.com.tntbrasil.integracao.domains.dell.layout.traxnfse.*;
import br.com.tntbrasil.integracao.domains.sim.*;
import br.com.tntbrasil.integracao.domains.telefonica.toutbox.cte.ItemCteToutBoxDMN;
import br.com.tntbrasil.integracao.domains.telefonica.toutbox.eventos.EventoData;
import br.com.tntbrasil.integracao.domains.telefonica.toutbox.eventos.Events;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.DadosComplementoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.portaria.model.service.utils.EventoDoctoServicoHelper;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.dao.EventoDocumentoServicoDAO;
import com.mercurio.lms.sim.model.dao.LMEventoDAO;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.ConvertStringList;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.ws.rs.WebApplicationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Classe de serviço para CRUD:
 * <p>
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.sim.eventoDocumentoServicoService"
 */
public class EventoDocumentoServicoService extends CrudService<EventoDocumentoServico, Long> {

    private static final String CONSULTORA_NATURA = "Consultora Natura";
    private static final String PEDIDO = "Pedido";
    private static final String NUMERO_CONTROLE_NATURA = "Numero controle Natura";

    private static final String DUAS_CASAS_DECIMAIS = "#0.00";
    private static final String TRES_CASAS_DECIMAIS = "#0.000";
    private static final String QUATRO_CASAS_DECIMAIS = "#0.0000";

    private ManifestoService manifestoService;
    private ControleCargaService controleCargaService;
    private MeioTransporteService meioTransporteService;
    private MotoristaService motoristaService; 
    private DadosComplementoService dadosComplementoService;
    private DoctoServicoService doctoServicoService;
    private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
    private ConfiguracoesFacade configuracoesFacade;
    private LMEventoDAO lmEventoDao;
    private PessoaService pessoaService;
    private ParametroGeralService parametroGeralService;

    /**
     * @param idDoctoServico
     * @param cdEvento
     * @param tpEvento
     * @param blEventoCancelado
     * @return List<EventoDocumentoServico>
     */
    public List<EventoDocumentoServico> findEventoDoctoServico(Long idDoctoServico, Short cdEvento, DomainValue tpEvento, Boolean blEventoCancelado) {
        return getEventoDocumentoServicoDAO().findEventoDoctoServico(idDoctoServico, cdEvento, tpEvento, blEventoCancelado);
    }

    
    public EventoDocumentoServico findEventoDoctoServicoFilial(Long idDoctoServico, Short cdEvento, Long idFilialEvento) {
        return getEventoDocumentoServicoDAO().findEventoDoctoServicoFilial(idDoctoServico, cdEvento, idFilialEvento);
    }

    /**
     * @param idDoctoServico    Long
     * @param cdEvento          Short
     * @param tpEvento          DomainValue
     * @param blEventoCancelado Boolean
     * @return List<EventoDocumentoServico>
     */
    public List<EventoDocumentoServico> findEventoDoctoServicoComCodEntregaParcial(Long idDoctoServico, Short cdEvento, DomainValue tpEvento, Boolean blEventoCancelado) {
        return getEventoDocumentoServicoDAO().findEventoDoctoServicoComCodEntregaParcial(idDoctoServico, cdEvento, tpEvento, blEventoCancelado);
    }

    /**
     * Procura evento(s) no Monitoramento informado que seja igual ao código do evento
     * passado por parâmetro.
     *
     * @param idMonitoramentoDescarga
     * @param cdEvento
     * @return
     */
    public List<EventoDocumentoServico> findEventoDoctoServicoByMonitoramento(Long idMonitoramentoDescarga, Short cdEvento) {
        return getEventoDocumentoServicoDAO().findEventoDoctoServicoByMonitoramento(idMonitoramentoDescarga, cdEvento);
    }


    /**
     * Procura evento(s) no doctoServico informado que seja igual ao código do evento
     * passado por parâmetro.
     *
     * @param idDoctoServico
     * @param cdEvento
     * @return
     */
    public List<EventoDocumentoServico> findEventoDoctoServico(Long idDoctoServico, Short cdEvento) {
        return getEventoDocumentoServicoDAO().findEventoDoctoServico(idDoctoServico, cdEvento);
    }

    /**
     * Procura evento(s) no doctoServico informado que seja igual ao código e a filial do evento informado.
     * passado por parâmetro.
     *
     * @param idDoctoServico
     * @param idFilialEvento
     * @param cdEvento
     * @param somenteNaoCancelados
     * @param dhInicio
     * @param dhFim
     * @return
     */
    public List<EventoDocumentoServico> findEventoDoctoServico(Long idDoctoServico, Long idFilialEvento, Short[] cdEvento, boolean somenteNaoCancelados, DateTime dhInicio, DateTime dhFim) {
        return getEventoDocumentoServicoDAO().findEventoDoctoServico(idDoctoServico, idFilialEvento, cdEvento, somenteNaoCancelados, dhInicio, dhFim);
    }

    /**
     * Retorna o último Evento cadastrado para o Documento de Servico
     *
     * @param idDoctoServico
     * @return
     */
    public EventoDocumentoServico findUltimoEventoDoctoServico(Long idDoctoServico) {
        return getEventoDocumentoServicoDAO().findUltimoEventoDoctoServico(idDoctoServico, null, null);
    }

    public EventoDocumentoServico findUltimoEventoDoctoServico(Long idDoctoServico, String tpEvento, Boolean blEventoCancelado) {
        return getEventoDocumentoServicoDAO().findUltimoEventoDoctoServico(idDoctoServico, tpEvento, blEventoCancelado);
    }

    public void removeByIdDoctoServico(Long idDoctoServico) {
        getEventoDocumentoServicoDAO().removeByIdDoctoServico(idDoctoServico);
    }

    public EventoDocumentoServicoDMN findEventoDocumentoServicoDMNById(java.lang.Long id) {
        return EventoDoctoServicoHelper.convertEventoDoctoServico((EventoDocumentoServico) super.findById(id));
    }

    /**
     * Recupera uma instância de <code>EventoDocumentoServico</code> a partir do ID.
     *
     * @param id representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     */
    public EventoDocumentoServico findById(java.lang.Long id) {
        return (EventoDocumentoServico) super.findById(id);
    }

    /**
     * Verifica se o evento informado foi lancado no documento de servico
     *
     * @param idEvento           Long
     * @param idDocumentoServico Long
     * @return List<EventoDocumentoServico>
     */
    public List<EventoDocumentoServico> findByEventoByDocumentoServico(Long idEvento, Long idDocumentoServico) {
        return getEventoDocumentoServicoDAO().findByEventoByDocumentoServico(idEvento, idDocumentoServico);
    }

    /**
     * Apaga uma entidade através do Id.
     *
     * @param id indica a entidade que deverá ser removida.
     */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

    /**
     * Apaga várias entidades através do Id.
     *
     * @param ids lista com as entidades que deverão ser removida.
     */
    @Override
    @ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

    /**
     * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
     *
     * @param bean entidade a ser armazenada.
     * @return entidade que foi armazenada.
     */
    public java.io.Serializable store(EventoDocumentoServico bean) {
        return super.store(bean);
    }


    public List<Long> findIdsByIdDoctoServico(Long idDoctoServico) {
        return getEventoDocumentoServicoDAO().findIdsByIdDoctoServico(idDoctoServico);
    }

    /**
     * Método que retorna a maior dhEvento da tabela de EventoDocumentoServico com
     * o ID do DoctoServico e IDs de LocalizacaoMercadoria
     *
     * @param idDoctoServico           Long
     * @param idsLocalizacaoMercadoria List<Long>
     * @return DateTime
     */
    public DateTime findMaiorDhEventoByIdDoctoServicoByIdsLocalizacaoMercadoria(Long idDoctoServico, List<Long> idsLocalizacaoMercadoria) {
        return this.getEventoDocumentoServicoDAO().findMaiorDhEventoByIdDoctoServicoByIdsLocalizacaoMercadoria(idDoctoServico, idsLocalizacaoMercadoria);
    }

    /**
     * @param idDocumentoServico Long
     * @return List<EventoDocumentoServico>
     * @author Andresa Vargas
     */
    public List<EventoDocumentoServico> findEventosDoctoServicoNaoCancelados(Long idDocumentoServico) {
        return getEventoDocumentoServicoDAO().findEventosDoctoServicoNaoCancelados(idDocumentoServico);
    }

    public ResultSetPage<Map<String, Object>> findPaginatedEventosByIdDoctoServico(Long idDoctoServico) {
        ResultSetPage<Map<String, Object>> rs = lmEventoDao.findPaginatedEventos(idDoctoServico);
        List<Map<String, Object>> result = rs.getList();
        for (Map<String, Object> map : result) {
            if (map.get("dhEvento") != null) {
                String dia = JTDateTimeUtils.getWeekdayName((DateTime) map.get("dhEvento"));
                map.put("dia", dia);
            }
        }
        return rs;
    }

    /**
     * Retorna a lista de eventos não cancelados do documento informado filtrando por o código do evento
     *
     * @param idDoctoServico
     * @param cdEvento
     * @return
     * @author Mickaël Jalbert
     * @since 21/02/2007
     */
    public List<EventoDocumentoServico> findEventoDoctoServico(Long idDoctoServico, Short[] cdEvento) {
        return getEventoDocumentoServicoDAO().findEventoDoctoServico(idDoctoServico, cdEvento);
    }

    //####################### *** MÉTODO PARA INTEGRAÇÃO *** ###################################//

    /**
     * @param nrDocumento String
     * @param cdEvento    Short
     * @param idFilial    Long
     * @param dhEvento    DateTime
     * @return EventoDocumentoServico
     */
    public EventoDocumentoServico findEventoDocumentoServico(String nrDocumento, Short cdEvento, Long idFilial, DateTime dhEvento) {
        return getEventoDocumentoServicoDAO().findEventoDocumentoServico(nrDocumento, cdEvento, idFilial, dhEvento);
    }

    public Integer getRowCountEventoDoctoServicoReentrega(Long idDoctoServico) {
        return getEventoDocumentoServicoDAO().getRowCountEventoDoctoServicoReentrega(idDoctoServico);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     *
     * @param dao Instância do DAO.
     */
    public void setEventoDocumentoServicoDAO(EventoDocumentoServicoDAO dao) {
        setDao(dao);
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private EventoDocumentoServicoDAO getEventoDocumentoServicoDAO() {
        return (EventoDocumentoServicoDAO) getDao();
    }

    public void setLmEventoDao(LMEventoDAO lmEventoDao) {
        this.lmEventoDao = lmEventoDao;
    }

    /**
     * @param idDoctoServico Long
     * @return List<EventoDocumentoServico>
     */
    public List<EventoDocumentoServico> findEventoDocumentoServicoComEntregaRealizada(Long idDoctoServico) {
        return getEventoDocumentoServicoDAO().findEventoDocumentoServicoComEntregaRealizada(idDoctoServico);
    }

    public EventoDocumentoServico findEventosByIdDoctoServicoAndCdEventoAndIdFilial(Long idDoctoServico, Short cdEvento, Long idFilial) {
        return getEventoDocumentoServicoDAO().findEventosEspecificosByIdDoctoServicoAndCdEventoAndIdFilial(idDoctoServico, cdEvento, idFilial);
    }

    public List<Map<String, Object>> findAllEventosByIdDoctoServico(Long idDoctoServico) {
        return getEventoDocumentoServicoDAO().findAllEventosByIdDoctoServico(idDoctoServico);
    }

    /**
     * Valida se o DoctoServico possui código do evento igual a '21' ou se possui agendamento.
     * Nesses casos não permitir bloquear / liberar.
     *
     * @param idDoctoServico Long
     */
    public void validateDoctoServico(Long idDoctoServico) {
        DoctoServico ds = doctoServicoService.findById(idDoctoServico);
        if (!SessionUtils.isFilialSessaoMatriz()) {
            Long idFilialLocalizacao = null;
            if (ds.getFilialLocalizacao() != null) {
                idFilialLocalizacao = ds.getFilialLocalizacao().getIdFilial();
            }
            Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();
            if (idFilialLocalizacao == null || CompareUtils.ne(idFilialLocalizacao, idFilialUsuario)) {
                throw new BusinessException("LMS-17041");
            }
        }
        List<Short> invalidos = new ArrayList<>();
        invalidos.add((short) 25);
        invalidos.add((short) 1);
        invalidos.add((short) 39);
        invalidos.add((short) 53);
        invalidos.add((short) 54);
        if ("C".equals(ds.getTpSituacaoConhecimento().getValue()) || invalidos.contains(ds.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())) {
            throw new BusinessException("LMS-17045");
        }
        if (!doctoServicoService.findDoctoServicoWithAgendamento(idDoctoServico).isEmpty()) {
            throw new BusinessException("LMS-17028");
        }

        List<EventoDocumentoServico> list21 = findEventoDoctoServico(idDoctoServico, (short) 21);
        if (!list21.isEmpty()) {
            throw new BusinessException("LMS-17016");
        }
    }

    /**
     * @param idDoctoServico       Long
     * @param idOcorrenciaBloqueio Long
     * @return
     */
    public Boolean validateExisteEventoDoctoServicoMaiorEventoBloqueio(Long idDoctoServico, Long idOcorrenciaBloqueio) {
        return getEventoDocumentoServicoDAO().validateExisteEventoDoctoServicoMaiorEventoBloqueio(idDoctoServico, idOcorrenciaBloqueio);
    }

    /**
     * @param idDoctoServico
     * @param blOrigemCancelamentoRIM
     * @return
     */
    public EventoDocumentoServico findEventoDoctoServicoByLastDhEventoByIdDoctoServico(Long idDoctoServico, Boolean blOrigemCancelamentoRIM) {
        return getEventoDocumentoServicoDAO().findEventoDoctoServicoByLastDhEventoByIdDoctoServico(idDoctoServico, blOrigemCancelamentoRIM);
    }

    public EventoDocumentoServico findLastEventoNaoCanceladoByCd(long idDoctoServico, short cdEvento) {
        return getEventoDocumentoServicoDAO().findLastEventoNaoCanceladoByCd(idDoctoServico, cdEvento);
    }

    public EventoDocumentoServico findUltimoEventoDocumentoServico(Long idDoctoServico, String tpEvento, Boolean blEventoCancelado) {
        return getEventoDocumentoServicoDAO().findUltimoEventoDocumenttoServico(idDoctoServico, tpEvento, blEventoCancelado);
    }

    public ResultSetPage<EventoDocumentoServico> findPaginatedByIdDocumento(Long id, FindDefinition findDefinition) {
        return getEventoDocumentoServicoDAO().findPaginatedByIdDocumento(id, findDefinition);
    }

    public List<EventoDocumentoServico> findByDocumentoServico(Long idDoctoServico) {
        return getEventoDocumentoServicoDAO().findByDocumentoServico(idDoctoServico);
    }


    /**
     * fachada criada para nao precisar duplicar o metodo findByDocumentoServico(Long idDoctoServico), pois para o projeto tracking
     * precisamos que as recusas (que não tem localizaçãoMercadoria sejam retornadas)
     * Se innerJoinLocalizacaoMercadoria é true então faz inner join, senão faz left join
     *
     * @param idDoctoServico
     * @param innerJoinLocalizacaoMercadoria
     * @return List<EventoDocumentoServico>
     */
    public List<EventoDocumentoServico> findByDocumentoServico(Long idDoctoServico, Boolean innerJoinLocalizacaoMercadoria, Boolean orderByDhEventoDesc) {
        return getEventoDocumentoServicoDAO().findByDocumentoServico(idDoctoServico, innerJoinLocalizacaoMercadoria, orderByDhEventoDesc);
    }

    /**
     * Método responsável por retornar a menor dhEvento do EventoDocumentoServico relacionado
     * ao DoctoServico e Evento com os ids passados como parametros
     *
     * @param idEvento
     * @param idDoctoServico
     * @return DateTime dhEvento
     */
    public DateTime findMenorDhEventoByIdEventoEIdDoctoServico(Long idEvento, Long idDoctoServico) {
        return getEventoDocumentoServicoDAO().findMenorDhEventoByIdEventoEIdDoctoServico(idEvento, idDoctoServico);
    }

    public List<EventoDocumentoServico> findByDoctoServico(Long idDoctoServico, Short... cdEvento) {
        return getEventoDocumentoServicoDAO().findByDoctoServico(idDoctoServico, cdEvento);
    }

    public EventoDocumentoServico findFirstEventoByDoctoServicoAfterDtInicio(Long idDoctoServico, DateTime dtInicio, Short... cdEvento) {
        return getEventoDocumentoServicoDAO().findFirstEventoByDoctoServicoAfterDtInicio(idDoctoServico, dtInicio, cdEvento);
    }

    public Object[] findInfoEventoAtualDoctoByIdDoctoServico(Long idDoctoServico) {
        return getEventoDocumentoServicoDAO().findInfoEventoAtualDoctoByIdDoctoServico(idDoctoServico);
    }

    public void refreshEventoDocumentoServico(EventoDocumentoServico eventoDocumentoServico) {
        getDao().getSessionFactory().getCurrentSession().flush();
        getDao().getSessionFactory().getCurrentSession().refresh(eventoDocumentoServico);
    }

    public List findDadosEvento(Long idEventoDocumentoServico) {
        return getEventoDocumentoServicoDAO().findDadosEvento(idEventoDocumentoServico);
    }
    
    public Short findCdOcorrenciaCliente(Long idCliente, Long idLMS, String tpOcorrencia, String nmTabelaOcorrencia) {
    	return getEventoDocumentoServicoDAO().findCdOcorrenciaCliente(idCliente, idLMS, tpOcorrencia, nmTabelaOcorrencia);
    }
	
    /**
     * @param
     * @return
     * @Author Marcelo Fachinelli
     */
    public List<EventoDocumentoServicoNaturaDMN> findAndFillEventoDocumentoServicoNaturaDMN(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {
        List<EventoDocumentoServicoNaturaDMN> eventoDocumentoServicoNaturaDMNS = new ArrayList<>();

        if (eventoDocumentoServicoDMN.getIdDoctoServico() == null || (eventoDocumentoServicoDMN.getDhEvento() == null && !Short.valueOf("42").equals(eventoDocumentoServicoDMN.getCdOcorrenciaEntrega()))) {
            return eventoDocumentoServicoNaturaDMNS;
        }
        
        DoctoServico doctoServico = this.doctoServicoService.findById(eventoDocumentoServicoDMN.getIdDoctoServico(), false);
    	
    	if (doctoServico != null) {
    		
    		Long idFilialOrigem = (doctoServico.getFilialByIdFilialOrigem() != null) ? doctoServico.getFilialByIdFilialOrigem().getIdFilial() : null;
    		Long idFilialDestino = (doctoServico.getFilialByIdFilialDestino() != null) ? doctoServico.getFilialByIdFilialDestino().getIdFilial() : null;
    		
    		eventoDocumentoServicoDMN.setIdFilialOrigem(idFilialOrigem);
    		eventoDocumentoServicoDMN.setIdFilialDestino(idFilialDestino);
    	}
    	
        List<NotaFiscalConhecimento> notaFiscalConhecimentos = notaFiscalConhecimentoService.findNFByIdConhecimentoAndIdsCliente(eventoDocumentoServicoDMN.getIdDoctoServico(), getIdsNatura());

        if (notaFiscalConhecimentos != null && notaFiscalConhecimentos.size() > 0) {
        	String cdOcorrencia = "";
        	if (eventoDocumentoServicoDMN.getIdEventoDocumentoServico() != null){
        		cdOcorrencia = getEventoDocumentoServicoDAO().findCdOcorrenciaClienteEventoDoctoServicoEdi(eventoDocumentoServicoDMN.getIdDoctoServico(), eventoDocumentoServicoDMN.getIdEventoDocumentoServico());
        	}else{
                //Pega por dh e idDoctoServ para evitar problemas de eventos com idEventoDoctoServ Nulos
        		cdOcorrencia = getEventoDocumentoServicoDAO().findCdOcorrenciaClienteEventoDoctoServicoEdi(eventoDocumentoServicoDMN.getIdDoctoServico(), eventoDocumentoServicoDMN.getDhEvento());
        	}
            if (cdOcorrencia != null) {

            	this.popularListaEventoDocumentoServicoNaturaDMNS(eventoDocumentoServicoDMN,
						eventoDocumentoServicoNaturaDMNS,
						notaFiscalConhecimentos, cdOcorrencia);
    		}
    	}

    	return eventoDocumentoServicoNaturaDMNS;
    }
    
    private void popularListaEventoDocumentoServicoNaturaDMNS(
			EventoDocumentoServicoDMN eventoDocumentoServicoDMN,
			List<EventoDocumentoServicoNaturaDMN> eventoDocumentoServicoNaturaDMNS,
			List<NotaFiscalConhecimento> notaFiscalConhecimentos,
			String cdOcorrencia) {
		Long codigoOcorrencia = Long.valueOf(cdOcorrencia);
		//Busca notas fiscais de conhecimento em que os clientes devedores são natura.
		for (NotaFiscalConhecimento notaFiscalConhecimento : notaFiscalConhecimentos) {
			EventoDocumentoServicoNaturaDMN eventoDocumentoServicoNaturaDMN = new EventoDocumentoServicoNaturaDMN(eventoDocumentoServicoDMN);

			List dsComplementoList = new ArrayList();
			dsComplementoList.add(CONSULTORA_NATURA);
			dsComplementoList.add(NUMERO_CONTROLE_NATURA);
			dsComplementoList.add(PEDIDO);

			List<Map<String, Object>> dadosComplementos = dadosComplementoService.findAllDadosComplementoByIdConhecimentoDsCampos(eventoDocumentoServicoDMN.getIdDoctoServico(), dsComplementoList);
			//Verificar se docto_servico tem complementos

			for (Map<String, Object> dadosComplemento : dadosComplementos) {
				preencherDadosComplementoOcorrenciaNatura(dadosComplemento, eventoDocumentoServicoNaturaDMN);
			}

			if (eventoDocumentoServicoNaturaDMN.getNumeroControleNatura() != null && eventoDocumentoServicoNaturaDMN.getCodigoConsultorNatura() != null
					&& eventoDocumentoServicoNaturaDMN.getNumeroPedidoNatura() != null) {

				eventoDocumentoServicoNaturaDMN.setCdTipoOcorrenciaCliente(codigoOcorrencia);
				eventoDocumentoServicoNaturaDMN.setCnpjTransportadora(notaFiscalConhecimento.getConhecimento().getFilialOrigem().getPessoa().getNrIdentificacao());
				eventoDocumentoServicoNaturaDMN.setNrNotaFiscal(notaFiscalConhecimento.getNrNotaFiscal().toString());
				eventoDocumentoServicoNaturaDMN.setDsSerie(notaFiscalConhecimento.getDsSerie());
				eventoDocumentoServicoNaturaDMN.setCnpjClienteRemetente(notaFiscalConhecimento.getConhecimento().getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao());
				eventoDocumentoServicoNaturaDMN.setNrIdentClienteDevedor(notaFiscalConhecimento.getConhecimento().getDevedorDocServs().get(0).getCliente().getPessoa().getNrIdentificacao());
				eventoDocumentoServicoNaturaDMN.setIdNotaFiscalConhecimento(notaFiscalConhecimento.getIdNotaFiscalConhecimento());

				// constrói comprovante de entrega do pedido
		    	eventoDocumentoServicoNaturaDMN = getEventoDocumentoServicoDAO().doComprovanteEntregaNatura(eventoDocumentoServicoNaturaDMN, getIdsNatura());

				eventoDocumentoServicoNaturaDMNS.add(eventoDocumentoServicoNaturaDMN);

				Long idFilialDestino = eventoDocumentoServicoNaturaDMN.getIdFilialDestino(); 
				Long idDoctoServico = eventoDocumentoServicoDMN.getIdDoctoServico();
				List<Manifesto> manifestos = manifestoService.findManifestoEntregaFilialOrigem(idDoctoServico, idFilialDestino);
				
				if (!manifestos.isEmpty()) {
					
					Manifesto manifesto = manifestos.get(0);
					
					if (manifesto != null) {
						
						ControleCarga controleCarga = manifesto.getControleCarga();
						
						if (controleCarga != null) {
							
							Motorista motorista = controleCarga.getMotorista();
							
							if ((motorista != null) && (motorista.getUsuarioMotorista() != null)) {
								eventoDocumentoServicoNaturaDMN.setNomeMotorista(motorista.getUsuarioMotorista().getNmUsuario());
							}
							
							MeioTransporte meioTransporte = controleCarga.getMeioTransporteByIdTransportado();
							
							if ((meioTransporte != null) && (meioTransporte.getNrIdentificador() != null)) {
								eventoDocumentoServicoNaturaDMN.setPlacaMotorista(meioTransporte.getNrIdentificador());
							}
						}
					}
				}
			}
		}
	}

    private void preencherDadosComplementoOcorrenciaNatura(Map<String, Object> dadosComplemento, EventoDocumentoServicoNaturaDMN eventoDocumentoServicoNaturaDMN) {
        Object key = dadosComplemento.get("key");
        Object objectValue = dadosComplemento.get("value");
        String value = objectValue == null ? null : objectValue.toString();

        if (CONSULTORA_NATURA.equals(key)) {
            eventoDocumentoServicoNaturaDMN.setCodigoConsultorNatura(value);
        } else if (PEDIDO.equals(key)) {
            eventoDocumentoServicoNaturaDMN.setNumeroPedidoNatura(value);
        } else if (NUMERO_CONTROLE_NATURA.equals(key)) {
            eventoDocumentoServicoNaturaDMN.setNumeroControleNatura(value);
        }

        if (eventoDocumentoServicoNaturaDMN.getCodigoConsultorNatura() == null) {
            eventoDocumentoServicoNaturaDMN.setCodigoConsultorNatura("0");
        }
    }

    private List<Long> getIdsNatura() {
        List<Long> idsNatura = new ArrayList<>();
        String dsIdsNatura = (String) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_IDS_NATURA);
        String idNatuaString[] = dsIdsNatura.split(";");
        for (String idNatura : idNatuaString) {
            idsNatura.add(Long.valueOf(idNatura));
        }
        return idsNatura;
    }
    
    public List<EventoDocumentoServicoDMN> findEventosBaixadosComAssNaoEnviadas() {
    	List<EventoDocumentoServicoDMN> listRetorno = new ArrayList <>();
    	List <EventoDocumentoServico> eventos = getEventoDocumentoServicoDAO().findEventosBaixadosComAssNaoEnviadas(getIdsNatura());
    	for (EventoDocumentoServico eventoDocumentoServico : eventos) {
    		listRetorno.add(EventoDoctoServicoHelper.convertEventoDoctoServico(eventoDocumentoServico));
		}
    	return listRetorno;
    }
    
    public EventoDocumentoServico findByIdoctoServicoIdEventoIdOcorrenciaEntrega(Long idDoctoServico, Long idEvento, Long idOcorrenciaEntrega) {
    	return getEventoDocumentoServicoDAO().findByIdoctoServicoIdEventoIdOcorrenciaEntrega(idDoctoServico, idEvento, idOcorrenciaEntrega);
    }
    public Boolean validateEntregaParcial(Long idDoctoServico) {
        return getEventoDocumentoServicoDAO().existsEventoNaoCanceladoComOcorrenciaEntregaParcialByIdDoctoServico(idDoctoServico);
    }  
    
    public Boolean existsEventoComOcorrenciaEntregaParcialByIdDoctoServico(Long idDoctoServico) {
        return getEventoDocumentoServicoDAO().existsEventoComOcorrenciaEntregaParcialByIdDoctoServico(idDoctoServico);
    } 
    
    public List<EventoDocumentoServico> findMaiorEvento(DoctoServico doctoServico, DateTime dataHoraReferencia){
    	return getEventoDocumentoServicoDAO().findMaiorEvento(doctoServico, dataHoraReferencia);
    }
    
    public Boolean existsEventoFinalizacaoEntregaParcialByIdDoctoServico(Long idDoctoServico) {
        return getEventoDocumentoServicoDAO().existsEventoFinalizacaoEntregaParcialByIdDoctoServico(idDoctoServico);
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
        this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }
    
    public void setMotoristaService(MotoristaService motoristaService) {
    	this.motoristaService = motoristaService;
    }

    public void setDadosComplementoService(DadosComplementoService dadosComplementoService) {
        this.dadosComplementoService = dadosComplementoService;
    }

	public ManifestoService getManifestoService() {
		return manifestoService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}


	public List<EventoNotaFiscalWine> findNotasEventoDoctoServicoWine(
			Long idEventoDocumentoServico) {
		
		
		String cnpjParcialWine = (String)configuracoesFacade.getValorParametro("CNPJ_RAIZ_WINE");
		String nmTabelaOcoren = (String)configuracoesFacade.getValorParametro("NM_TABELA_WINE");
		
		List<Object[]> notasEventoWine = this.getEventoDocumentoServicoDAO().findNotasEventoDoctoServicoWine(idEventoDocumentoServico,cnpjParcialWine,nmTabelaOcoren);
		
		if (notasEventoWine != null && notasEventoWine.size() > 0){
			List<EventoNotaFiscalWine> listDto = new ArrayList<>();
			for (Object[] row : notasEventoWine) {
				EventoNotaFiscalWine dto = new EventoNotaFiscalWine();
				dto.setChaveNF((String)row[2]);
				dto.setDataEvento((String)row[0]);
				dto.setDescricaoTracking((String)row[1]);
				
				listDto.add(dto);
			}
			return listDto;
		}
		
		return null;
	}

	
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	
	public void findIdEventoDocumentoServico(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {
        EventoDocumentoServico eventoDocumentoServico = null;
        Long idEventoDocumentoServico = eventoDocumentoServicoDMN.getIdEventoDocumentoServico();
        if(idEventoDocumentoServico == null)
        {
            eventoDocumentoServico =
                    findEventoDoctoServicoFilial
                            (
                                    eventoDocumentoServicoDMN.getIdDoctoServico(),
                                    eventoDocumentoServicoDMN.getCdEvento(),
                                    eventoDocumentoServicoDMN.getIdFilialEvento()
                            );

            if(eventoDocumentoServico == null){
                return;
            }

            idEventoDocumentoServico = eventoDocumentoServico.getIdEventoDocumentoServico();
            eventoDocumentoServicoDMN.setIdEventoDocumentoServico(idEventoDocumentoServico);

		}else{
            eventoDocumentoServico = getEventoDocumentoServicoDAO().findEventoDoctoServico(idEventoDocumentoServico);

            if(eventoDocumentoServico == null){
                return;
            }
        }

        eventoDocumentoServicoDMN
                .setTpDoctoServico(eventoDocumentoServico.getDoctoServico().getTpDocumentoServico().getValue());

	}

    public List<Object[]> findNotasEventoDoctoServicoCremer(Long idEventoDocumentoServico) {

        List<String> cnpjParcialCremer =  new ArrayList<>
            (Arrays.asList(((String)configuracoesFacade
                                        .getValorParametro("CNPJ_RAIZ_CREMER")).split(",")));

        String nmTabelaOcoren = (String)configuracoesFacade.getValorParametro("NM_TABELA_CREMER");
        return this.getEventoDocumentoServicoDAO()
                        .findNotasEventoDoctoServicoCremer
                                (idEventoDocumentoServico, cnpjParcialCremer,nmTabelaOcoren);
    }

    public List<Object[]> findNotasEventoDoctoServicoDecathlon(Long idEventoDocumentoServico) {

        ConvertStringList convertStringList = new ConvertStringList();
        List<String> cnpjParcialCremer = convertStringList.convertStringListParametroCnpjRaiz("CNPJ_RAIZ_DECATHLON", configuracoesFacade);

        return this.getEventoDocumentoServicoDAO()
                        .findNotasEventoDoctoServicoDecathlon(idEventoDocumentoServico, cnpjParcialCremer);
    }

    public List<Object[]> findNotasEventoDecathlon(Long idEventoDocumentoServico) {

        ConvertStringList convertStringList = new ConvertStringList();
        List<String> cnpjParcial = convertStringList.convertStringListParametroCnpjRaiz("CNPJ_RAIZ_DECATHLON", configuracoesFacade);

        return this.getEventoDocumentoServicoDAO().findNotasEventoDecathlon(idEventoDocumentoServico, cnpjParcial);
    }

    public List<Object[]> findNotasEventoDoctoServicoRotaCremer(Long idEventoDocumentoServico) {

        ConvertStringList convertStringList = new ConvertStringList();
        List<String> cnpjParcialCremer = convertStringList
                                            .convertStringListParametroCnpjRaiz
                                                ("CNPJ_RAIZ_CREMER", configuracoesFacade);
        return this.getEventoDocumentoServicoDAO()
            .findNotasEventoDoctoServicoRotaCremer(idEventoDocumentoServico, cnpjParcialCremer);
    }

    public List<Object[]> findNotasEventoDoctoServicoFinalizaRotaCremer(Long idEventoDocumentoServico, Long idFilialEvento)

    {

        ConvertStringList convertStringList = new ConvertStringList();
        List<String> cnpjParcialCremer = convertStringList
                .convertStringListParametroCnpjRaiz
                        ("CNPJ_RAIZ_CREMER", configuracoesFacade);
        return this.getEventoDocumentoServicoDAO()
                .findNotasEventoDoctoServicoFinalizaRotaCremer(idEventoDocumentoServico, idFilialEvento, cnpjParcialCremer);
    }

    public SchedulingParameterCremerDMN findNotasEventoDoctoServicoAgendamentoCremer
        (Long idEventoDocumentoServico, Long idDoctoServico)

    {

        SchedulingParameterCremerDMN schedulingParameterCremer = new SchedulingParameterCremerDMN();
        schedulingParameterCremer.setIdEventoDocumentoServico(idEventoDocumentoServico);
        schedulingParameterCremer.setIdDoctoServico(idDoctoServico);
        List<SchedulingCremerDMN> schedulingCremer = schedulingParameterCremer.getSchedulingCremer();

        ConvertStringList convertStringList = new ConvertStringList();
        List<String> cnpjParcialCremer = convertStringList
                .convertStringListParametroCnpjRaiz
                        ("CNPJ_RAIZ_CREMER", configuracoesFacade);
        String nmTabelaOcorren = (String)configuracoesFacade.getValorParametro("NM_TABELA_CREMER");

        List<Object[]> listAgendamento = this.getEventoDocumentoServicoDAO()
                                        .findNotasEventoDoctoServicoAgendamentoCremer
                                            (idEventoDocumentoServico, cnpjParcialCremer, nmTabelaOcorren);
        if (!listAgendamento.isEmpty()){
            Object[] row = listAgendamento.get(0);
            schedulingParameterCremer.setIdCliente((Long)row[4]);
            for(Object[] rowAgendamento : listAgendamento){
                SchedulingCremerDMN schedulingCremerDMN = new SchedulingCremerDMN();
                schedulingCremerDMN.setKey((String)rowAgendamento[0]);
                schedulingCremerDMN.setDate((String)rowAgendamento[1]);
                schedulingCremerDMN.setAnnotation((String)rowAgendamento[2]);
                schedulingCremerDMN.setCode((String)rowAgendamento[3]);
                schedulingCremer.add(schedulingCremerDMN);
            }
        }

        return schedulingParameterCremer;
    }

    public EventoDoctoServicoAlconDMN findNotasEventoAlcon(Long idEventoDocumentoServico, Long idDoctoServico) {

        EventoDoctoServicoAlconDMN eventoDoctoServicoAlconDMN = new EventoDoctoServicoAlconDMN();
        eventoDoctoServicoAlconDMN.setIdEventoDocumentoServico(idEventoDocumentoServico);
        eventoDoctoServicoAlconDMN.setIdDoctoServico(idDoctoServico);

        ConvertStringList convertStringList = new ConvertStringList();
        List<String> cnpjParcialaAlcon = convertStringList
                .convertStringListParametroCnpjRaiz
                        ("CNPJ_RAIZ_ALCON", configuracoesFacade);
        String nmTabelaOcorren = (String) configuracoesFacade.getValorParametro("NM_TABELA_ALCON");

        List<Object[]> listNotaEvento = this.getEventoDocumentoServicoDAO()
                .findNotasEventoAlcon
                        (idEventoDocumentoServico, nmTabelaOcorren, "NM_ID_ACCOUNT_ALCON", cnpjParcialaAlcon);

        if (listNotaEvento.isEmpty()) {
            throw new WebApplicationException("Evento não retornou notas para enviar ao serviço do cliente.", 400);
        }
        Object[] notaEvento = listNotaEvento.get(0);
        eventoDoctoServicoAlconDMN.setIdCliente((Long) notaEvento[13]);
        CarrierIdentifierDMN carrierIdentifier = new CarrierIdentifierDMN();
        EventoNotaFiscalAlconDMN eventoNotaFiscalAlconDMN = eventoDoctoServicoAlconDMN.getEventoNotaFiscalAlconDMN();
        eventoNotaFiscalAlconDMN.getCustomerAccount().setAccountIdentifier((String) notaEvento[0]);
        carrierIdentifier.setType((String) notaEvento[1]);
        carrierIdentifier.setValue((String) notaEvento[2]);
        eventoNotaFiscalAlconDMN.setCarrierIdentifier(carrierIdentifier);
        List<ShipmentIdentifiersDMN> shipmentIdentifiers = eventoNotaFiscalAlconDMN.getShipmentIdentifiers();
        ShipmentIdentifiersDMN shipmentIdentifiersDMN = new ShipmentIdentifiersDMN();
        shipmentIdentifiersDMN.setType((String) notaEvento[3]);
        shipmentIdentifiersDMN.setValue((String) notaEvento[4]);
        shipmentIdentifiers.add(shipmentIdentifiersDMN);
        eventoNotaFiscalAlconDMN.setStatusCode((String) notaEvento[5]);
        eventoNotaFiscalAlconDMN.setStopType((String) notaEvento[6]);
        eventoNotaFiscalAlconDMN.setStopNumber((Short) notaEvento[7]);
        LocationDMN locationDMN = eventoNotaFiscalAlconDMN.getLocation();
        AddressDMN addressDMN = locationDMN.getAddress();
        addressDMN.setPostalCode((String) notaEvento[8]);
        addressDMN.setAddressLines(Arrays.asList((String) notaEvento[9]));
        locationDMN.setAddress(addressDMN);
        addressDMN.setCity((String) notaEvento[10]);
        addressDMN.setCountry((String) notaEvento[11]);
        eventoNotaFiscalAlconDMN.setTimestamp((String) notaEvento[12]);
        return eventoDoctoServicoAlconDMN;
    }

    public EventoDoctoServicoBlackDeckerDMN findNotasEventoBlackDecker(Long idEventoDocumentoServico, Long idDoctoServico) {

        EventoDoctoServicoBlackDeckerDMN eventoDoctoServicoBlackDeckerDMN = new EventoDoctoServicoBlackDeckerDMN();
        eventoDoctoServicoBlackDeckerDMN.setIdEventoDocumentoServico(idEventoDocumentoServico);

        ConvertStringList convertStringList = new ConvertStringList();
        List<String> cnpjParcialBlackDecker = convertStringList.convertStringListParametroCnpjRaiz("CNPJ_RAIZ_BLACK_E_DECKER", configuracoesFacade);
        String nmTabelaOcorren = (String) configuracoesFacade.getValorParametro("NM_TABELA_BLACK_E_DECKER");

        List<Object[]> listNotaEvento = this.getEventoDocumentoServicoDAO().findNotasEventoBlackDecker(idEventoDocumentoServico, nmTabelaOcorren, cnpjParcialBlackDecker);

        if (listNotaEvento.isEmpty()) {
            throw new WebApplicationException("Evento não retornou notas para enviar ao serviço do cliente.", 400);
        }

        Object[] notaEvento = listNotaEvento.get(0);

        EmbarqueDMN embarque = new EmbarqueDMN();
        embarque.setNumero((String) notaEvento[0]);
        embarque.setSerie((String) notaEvento[1]);

        EmbarcadorDMN embarcador = new EmbarcadorDMN();
        embarcador.setCnpj((String) notaEvento[2]);

        OcorrenciaDMN ocorrencia = new OcorrenciaDMN();
        ocorrencia.setTipoEntrega((String) notaEvento[3]);
        ocorrencia.setDtOcorrencia((String) notaEvento[4]);

        EventoNotaFiscalBlackDeckerDMN eventoNotaFiscalBlackDeckerDMN = eventoDoctoServicoBlackDeckerDMN.getEventoNotaFiscalBlackDeckerDMN();
        eventoNotaFiscalBlackDeckerDMN.setEmbarque(embarque);
        eventoNotaFiscalBlackDeckerDMN.setEmbarcador(embarcador);
        eventoNotaFiscalBlackDeckerDMN.setOcorrencia(ocorrencia);

        eventoDoctoServicoBlackDeckerDMN.setIdEventoDocumentoServico(idEventoDocumentoServico);
        eventoDoctoServicoBlackDeckerDMN.setIdDoctoServico(idDoctoServico);
        eventoDoctoServicoBlackDeckerDMN.setIdCliente((Long) notaEvento[5]);

        return eventoDoctoServicoBlackDeckerDMN;
    }

    public EventoDoctoServicoBoschDMN findNotasEventoDoctoServicoBosch(Long idEventoDocumentoServico, Long idDoctoServico) {
        ConvertStringList convertStringList = new ConvertStringList();
        EventoDoctoServicoBoschDMN eventoDoctoServicoBosch = new EventoDoctoServicoBoschDMN();

        eventoDoctoServicoBosch.setIdEventoDocumentoServico(idEventoDocumentoServico);
        eventoDoctoServicoBosch.setIdDoctoServico(idDoctoServico);

        List<String> cnpjParcial = convertStringList
                .convertStringListParametroCnpjRaiz
                        ("CNPJ_BOSCH", configuracoesFacade);
        String nmTabelaOcoren = (String)configuracoesFacade.getValorParametro("NM_TABELA_BOSCH");
        List<Object[]> eventoBosch = this.getEventoDocumentoServicoDAO()
                                        .findNotasEventoDoctoServicoBosch
                                            (idEventoDocumentoServico, cnpjParcial, nmTabelaOcoren);
        if(!eventoBosch.isEmpty()){
            eventoDoctoServicoBosch.setIdCliente((Long)eventoBosch.get(0)[8]);
            eventoDoctoServicoBosch.setOcorrenciaEntregaBosch(eventoBosch.stream()
                    .map(row ->
                            new OcorrenciaEntregaBoschDMN
                                    (
                                        (Long)row[0], (String)row[1], (String)row[2], (String)row[3],
                                        (String) row[4],(String)row[5], (String)row[6], (String)row[7],
                                        (String)row[9]
                                    )
                    ).collect(Collectors.toList()));
        }
        return eventoDoctoServicoBosch;
    }

    public TraxNfseDMN findNotasEventoDellNfse(Long idFatura, String nrIdentificacaoCliente) {

        ConvertStringList convertStringList = new ConvertStringList();
        List<String> listCnpjDell = convertStringList.convertStringListParametroCnpjRaiz("CNPJ_RAIZ_DELL", configuracoesFacade);
        TraxNfseDMN traxNfseDMNFatura = new TraxNfseDMN();

        boolean isClienteDell = false;

        if(nrIdentificacaoCliente != null && !"".equals(nrIdentificacaoCliente)) {
            isClienteDell = listCnpjDell.contains(StringUtils.substring(nrIdentificacaoCliente.trim(), 0, 8));
        }

        if(!isClienteDell){
            return traxNfseDMNFatura;
        }

        List<Object[]> eventoDellDadosFatura = this.getEventoDocumentoServicoDAO().findNotasEventoDadosFaturaDell(idFatura);
        if (eventoDellDadosFatura.isEmpty()) {
            return new TraxNfseDMN();
        }
        traxNfseDMNFatura.setIdFatura(idFatura);
        Fatura fatura = new Fatura();
        Object[] dadosFaturaDell = eventoDellDadosFatura.get(0);
        fatura.setNumeroDocumento(StringUtils.leftPad((String) dadosFaturaDell[0], 9, "0"));
        fatura.setNumeroFatura((String) dadosFaturaDell[7]);
        fatura.setDataEmissaoDocumento((String) dadosFaturaDell[1]);
        fatura.setCnpjEmitente((String) dadosFaturaDell[2]);
        fatura.setCnpjRemetente((String) dadosFaturaDell[2]);
        fatura.setCnpjDestinatatio((String) dadosFaturaDell[3]);
        fatura.setQuantidadeTotalMercadoria((String) dadosFaturaDell[8]);
        fatura.setMontanteTotalDocumento((String) dadosFaturaDell[8]);
        fatura.setDocumentoCfop(dadosFaturaDell[5] != null ? (String) dadosFaturaDell[5] : String.format("%08d", 0));
        traxNfseDMNFatura.setIdCliente((Long) dadosFaturaDell[6]);
        traxNfseDMNFatura.setFatura(fatura);

        List<Object[]> eventoDellDadosNfse = this.getEventoDocumentoServicoDAO().findNotasEventoDadosNfseDell(idFatura);
        if (eventoDellDadosNfse.isEmpty()) {
            return new TraxNfseDMN();
        }

        Nfse nfse = new Nfse();
        Object[] dadosNfseDell = eventoDellDadosNfse.get(0);
        nfse.setNumeroDocumentoFiscal(StringUtils.leftPad((String) dadosNfseDell[0], 9, "0"));
        nfse.setDataEmissaoDocumentoFiscal((String) dadosNfseDell[1]);
        nfse.setNumeroDocumentoCfop(dadosNfseDell[2] != null ? (String) dadosNfseDell[2] : String.format("%08d", 0));
        String codigo = (String) dadosNfseDell[3];
        String inicio = codigo.substring(0, codigo.length() - 2).concat(".");
        String fim = codigo.substring(codigo.length()-2);
        nfse.setCodigoServico(inicio+fim);
        String taxaIss = (String) dadosNfseDell[4];
        nfse.setTaxaIss(taxaIss == null ? 0 : Double.parseDouble(taxaIss));
        nfse.setPrecoTotal((String) dadosNfseDell[5]);
        nfse.setPrecoUnitario((String) dadosNfseDell[5]);

        String valorIss = (String) dadosNfseDell[6];
        nfse.setValorIss(valorIss == null ? 0 : Double.parseDouble(valorIss));

        traxNfseDMNFatura.setNfse(nfse);

        List<Object[]> eventoDellDadosComplementoFatura = this.getEventoDocumentoServicoDAO().findNotasEventoDadosComplementoFaturaDell(idFatura);
        if (eventoDellDadosComplementoFatura.isEmpty()) {
            return new TraxNfseDMN();
        }

        ComplementoFatura complementoFatura = new ComplementoFatura();
        Object[] complementoFaturaDell = eventoDellDadosComplementoFatura.get(0);
        complementoFatura.setCnpjEmitente((String) complementoFaturaDell[0]);
        complementoFatura.setNumeroDocumento(StringUtils.leftPad((String) complementoFaturaDell[1], 9, "0"));
        complementoFatura.setDataEmisaoDocumento((String) complementoFaturaDell[2]);
        complementoFatura.setDataEmissaoParcela((String) complementoFaturaDell[2]);
        complementoFatura.setCnpjDestinatatio((String) complementoFaturaDell[3]);
        complementoFatura.setDataVencimentoParcela((String) complementoFaturaDell[4]);
        complementoFatura.setValorParcela((String) complementoFaturaDell[5]);
        traxNfseDMNFatura.setComplementoFatura(complementoFatura);

        List<Object[]> eventoDellDadosComplementoNfse = this.getEventoDocumentoServicoDAO().findNotasEventoDadosComplementoNfseDell(idFatura);
        if (eventoDellDadosComplementoNfse.isEmpty()) {
            return new TraxNfseDMN();
        }

        ComplementoNfse complementoNfse = new ComplementoNfse();
        Object[] complementoNfseDell = eventoDellDadosComplementoNfse.get(0);
        complementoNfse.setNumeroOrdemDell((String) complementoNfseDell[0]);
        complementoNfse.setDataColeta((String) complementoNfseDell[1]);
        complementoNfse.setEnderecoOrigem1((String) complementoNfseDell[2]);
        complementoNfse.setCidadeOrigem((String) complementoNfseDell[3]);
        complementoNfse.setUfOrigem((String) complementoNfseDell[4]);
        complementoNfse.setNomeParaEntrega((String) complementoNfseDell[5]);
        complementoNfse.setEnderecoEntrega1((String) complementoNfseDell[6]);
        complementoNfse.setCidadeEntrega((String) complementoNfseDell[7]);
        complementoNfse.setUfEntrega((String) complementoNfseDell[8]);
        complementoNfse.setValorTotalOrdem(this.formataNumeroDecimal(complementoNfseDell[9], DUAS_CASAS_DECIMAIS));
        complementoNfse.setAdValorem(complementoNfseDell[10] != null ? (String) complementoNfseDell[10] : String.format("%010d", 0));
        complementoNfse.setValorTotalIss(complementoNfseDell[11] != null ? this.formataNumeroDecimal(complementoNfseDell[11], DUAS_CASAS_DECIMAIS) : String.format("%010d", 0));
        complementoNfse.setValorTotalDespacho(complementoNfseDell[12] != null ? (String) complementoNfseDell[12] : String.format("%010d", 0));
        complementoNfse.setTotalAgenda(complementoNfseDell[13] != null ? (String) complementoNfseDell[13] : String.format("%010d", 0));
        complementoNfse.setValorTotalGris(complementoNfseDell[14] != null ? (String) complementoNfseDell[14] : String.format("%010d", 0));
        complementoNfse.setValorTotalPedagio(complementoNfseDell[15] != null ? (String) complementoNfseDell[15] : String.format("%010d", 0));
        complementoNfse.setValorTotalFrete(complementoNfseDell[16] != null ? this.formataNumeroDecimal(complementoNfseDell[16], DUAS_CASAS_DECIMAIS) : String.format("%010d", 0));
        complementoNfse.setDataVencimentoFatura((String) complementoNfseDell[17]);
        complementoNfse.setFilCtrc((String) complementoNfseDell[18]);
        complementoNfse.setDataEmissaoCte((String) complementoNfseDell[19]);
        complementoNfse.setNumeroCte((String) complementoNfseDell[20]);
        complementoNfse.setValorTotalInvoice(this.formataNumeroDecimal(complementoNfseDell[21], DUAS_CASAS_DECIMAIS));
        complementoNfse.setChargeableWeight(complementoNfseDell[22] != null ? (String) complementoNfseDell[22] : String.format("%010d", 0));
        complementoNfse.setValorIssRate((String) complementoNfseDell[23]);
        complementoNfse.setValorPisRate(this.formataNumeroDecimal(complementoNfseDell[24].toString().replace(",", "."), TRES_CASAS_DECIMAIS));
        complementoNfse.setValorCofinsRate(this.formataNumeroDecimal(complementoNfseDell[25].toString().replace(",", "."), TRES_CASAS_DECIMAIS));
        complementoNfse.setTipoServico(complementoNfseDell[26] != null ? this.deParaTipoServico(complementoNfseDell[26].toString()) : "SD");
        complementoNfse.setTipoTransporte(complementoNfseDell[27] != null ? this.deParaTipoTransporte(complementoNfseDell[27].toString()) : "");
        complementoNfse.setFatura((String) complementoNfseDell[28]);
        complementoNfse.setDataEmissaoFatura((String) complementoNfseDell[29]);

        traxNfseDMNFatura.setComplementoNfse(complementoNfse);

        validaDadosRetornoQueryTraxNfse(traxNfseDMNFatura);

        return traxNfseDMNFatura;
    }

    public TraxCteDMN findFaturasDelTraxCte(Long idFatura, String nrIdentificacaoCliente) {

        ConvertStringList convertStringList = new ConvertStringList();
        List<String> listCnpjDell = convertStringList.convertStringListParametroCnpjRaiz("CNPJ_RAIZ_DELL", configuracoesFacade);
        boolean isClienteDell = false;
        BigDecimal valorTotalFatura = new BigDecimal("0.00");

        if(nrIdentificacaoCliente != null && !nrIdentificacaoCliente.isEmpty()) {
            isClienteDell = listCnpjDell.contains(StringUtils.substring(nrIdentificacaoCliente.trim(), 0, 8));
        }
        TraxCteDMN traxCteDMN = new TraxCteDMN();
        List<Object[]> eventoDellCte = new ArrayList<>();

        if(isClienteDell) {
            eventoDellCte = this.getEventoDocumentoServicoDAO().findNotasEventoDoctoServicoDell(idFatura);
        }

        if (eventoDellCte.isEmpty()) {
            return traxCteDMN;
        }

        Object[] dadosFaturaDell = eventoDellCte.get(0);

        traxCteDMN.setIdFatura(idFatura);
        traxCteDMN.setIdCliente((Long) dadosFaturaDell[48]);

        FaturaTraxCte fatura = new FaturaTraxCte();
        fatura.setCnpjEmitenteDof((String) dadosFaturaDell[4]);
        fatura.setCnpjRemetenteDof((String) dadosFaturaDell[4]);
        fatura.setCnpjDestinatatioDof((String) dadosFaturaDell[5]);
        fatura.setNumeroDocumentoFiscal(StringUtils.leftPad(dadosFaturaDell[2].toString(), 9, "0"));
        fatura.setDataEmissaoDocumentoFiscal((String) dadosFaturaDell[3]);
        fatura.setValorTotalItensDof(this.formataNumeroDecimal(dadosFaturaDell[6], DUAS_CASAS_DECIMAIS));
        fatura.setValorBrutoDof(this.formataNumeroDecimal(dadosFaturaDell[6], DUAS_CASAS_DECIMAIS));
        fatura.setTipoDof(this.deParaTipoDof(dadosFaturaDell[27].toString()));
        traxCteDMN.setFaturaTraxCte(fatura);

        ComplementoFaturaTraxCte complementoFatura = new ComplementoFaturaTraxCte();
        complementoFatura.setCnpjEmitenteDof((String) dadosFaturaDell[4]);
        complementoFatura.setCnpjDestinatatioDof((String) dadosFaturaDell[5]);
        complementoFatura.setDataEmissaoParcela((String) dadosFaturaDell[3]);
        complementoFatura.setDataVencimentoParcela((String) dadosFaturaDell[7]);
        complementoFatura.setValorParcela(this.formataNumeroDecimal(dadosFaturaDell[6], DUAS_CASAS_DECIMAIS));
        complementoFatura.setNumeroDocumentoFiscal(StringUtils.leftPad((String) dadosFaturaDell[2], 9, "0"));
        complementoFatura.setDataEmissaoDocumentoFiscal((String) dadosFaturaDell[3]);
        traxCteDMN.setComplementoFaturaTraxCte(complementoFatura);

        List<Cte> listCte = new ArrayList<>();
        traxCteDMN.setListaCte(listCte);

        for (Object[] evento : eventoDellCte) {
            valorTotalFatura = valorTotalFatura.add(this.calculaSomaTotalParcelas(evento));
        }

        for (Object[] evento : eventoDellCte) {
            Cte cte = new Cte();

            DadosFaturaCte dadosFaturaCte = new DadosFaturaCte();
            dadosFaturaCte.setNumeroDocumentoFiscal(StringUtils.leftPad(evento[8].toString(), 9, "0"));
            dadosFaturaCte.setDataEmissaoDocumentoFiscal(evento[9].toString());
            dadosFaturaCte.setCfopDocumentoFiscal(String.format("%,d", Long.valueOf(evento[10].toString())));
            dadosFaturaCte.setCnpjEmitenteDof(evento[11].toString());
            dadosFaturaCte.setCnpjRemetenteDof(evento[11].toString());
            dadosFaturaCte.setCnpjDestinatatioDof(evento[58].toString());
            dadosFaturaCte.setValorTotalItensDof(this.formataNumeroDecimal(evento[14], DUAS_CASAS_DECIMAIS));
            dadosFaturaCte.setValorIcms(this.formataNumeroDecimal(evento[16], DUAS_CASAS_DECIMAIS));
            if(new BigDecimal(dadosFaturaCte.getValorIcms()).compareTo(new BigDecimal("0")) > 0) {
                dadosFaturaCte.setValorBaseCalculoIcms(this.formataNumeroDecimal(evento[15], DUAS_CASAS_DECIMAIS));
            }else {
                dadosFaturaCte.setValorBaseCalculoIcms(this.formataNumeroDecimal(null, DUAS_CASAS_DECIMAIS));
            }
            dadosFaturaCte.setValorIcmsSubstituicaoTributaria(this.formataNumeroDecimal(evento[17], DUAS_CASAS_DECIMAIS));
            if(new BigDecimal(dadosFaturaCte.getValorIcmsSubstituicaoTributaria()).compareTo(new BigDecimal("0")) > 0) {
                dadosFaturaCte.setValorBaseCalculoIcmsSt(this.formataNumeroDecimal(evento[15], DUAS_CASAS_DECIMAIS));
            }else {
                dadosFaturaCte.setValorBaseCalculoIcmsSt(this.formataNumeroDecimal(null, DUAS_CASAS_DECIMAIS));
            }
            dadosFaturaCte.setValorBrutoDof(this.formataNumeroDecimal(evento[14], DUAS_CASAS_DECIMAIS));
            dadosFaturaCte.setValorBaseCalculoPis(this.formataNumeroDecimal(evento[14], DUAS_CASAS_DECIMAIS));
            dadosFaturaCte.setValorRetencaoPis(this.formataNumeroDecimal(evento[18], DUAS_CASAS_DECIMAIS));
            dadosFaturaCte.setValorCalculoCofins(this.formataNumeroDecimal(evento[14], DUAS_CASAS_DECIMAIS));
            dadosFaturaCte.setValorRetencaoCofins(this.formataNumeroDecimal(evento[19], DUAS_CASAS_DECIMAIS));
            dadosFaturaCte.setNumeroVolumes(evento[20].toString());
            dadosFaturaCte.setPesoLiquido(this.formataNumeroDecimal(evento[21], QUATRO_CASAS_DECIMAIS));
            dadosFaturaCte.setPesoBruto(this.formataNumeroDecimal(evento[21], QUATRO_CASAS_DECIMAIS));
            dadosFaturaCte.setTipoDof(this.deParaTipoDof(evento[27].toString()));
            dadosFaturaCte.setCondicaoFrete(evento[22].toString());
            dadosFaturaCte.setPesoTransportado(this.formataNumeroDecimal(evento[23], DUAS_CASAS_DECIMAIS));
            dadosFaturaCte.setValorFretePorPesoVolume(this.formataNumeroDecimal(evento[24], DUAS_CASAS_DECIMAIS));
            dadosFaturaCte.setValorItr(this.formataNumeroDecimal(evento[49], DUAS_CASAS_DECIMAIS));
            dadosFaturaCte.setValorDespacho(this.formataNumeroDecimal(evento[25], DUAS_CASAS_DECIMAIS));
            dadosFaturaCte.setValorPedagio(this.formataNumeroDecimal(evento[26], DUAS_CASAS_DECIMAIS));
            dadosFaturaCte.setValorAdeme(this.formataNumeroDecimal(evento[50], DUAS_CASAS_DECIMAIS));
            dadosFaturaCte.setTipoConhecimento(this.deParaTipoFreteConhecimento(evento[22].toString()));
            dadosFaturaCte.setNfeLocalizador(evento[28].toString());
            dadosFaturaCte.setSituacaoNfe(evento[29].toString());
            cte.setDadosFaturaCte(dadosFaturaCte);

            FaturaCte faturaCte = new FaturaCte();
            List<CteNotaFiscal> listaCteNotaFiscal = new ArrayList<>();

            faturaCte.setCnpjEmissorDocumentoPai(evento[4].toString());
            faturaCte.setNumeroDocumentoPai(StringUtils.leftPad(evento[2].toString(), 9, "0"));
            faturaCte.setDataEmissaoDocumentoPai(evento[3].toString());
            faturaCte.setCnpjEmissorDocumentoFilho(evento[11].toString());
            faturaCte.setNumeroDocumentoFilho(StringUtils.leftPad(evento[8].toString(), 9, "0"));
            faturaCte.setDataEmissaoDocumentoFilho(evento[9].toString());

            CteNotaFiscal cteNotaFiscal = new CteNotaFiscal();
            cteNotaFiscal.setCnpjEmissorDocumentoPai(evento[4].toString());
            cteNotaFiscal.setNumeroDocumentoPai(StringUtils.leftPad(evento[8].toString(), 9, "0"));
            cteNotaFiscal.setDataEmissaoDocumentoPai(evento[9].toString());
            cteNotaFiscal.setCnpjEmissorDocumentoFilho(evento[30].toString());
            cteNotaFiscal.setNumeroDocumentoFilho(StringUtils.leftPad(evento[32].toString(), 9, "0"));
            cteNotaFiscal.setDataEmissaoDocumentoFilho(evento[33].toString());
            cteNotaFiscal.setValorNotafiscal(this.formataNumeroDecimal(evento[34], DUAS_CASAS_DECIMAIS));
            cteNotaFiscal.setSerieDocumentoFilho(evento[31] == null ? "" : evento[31].toString());

            listaCteNotaFiscal.add(cteNotaFiscal);

            faturaCte.setListaCteNotaFiscal(listaCteNotaFiscal);

            cte.setFaturaCte(faturaCte);

            ComplementoCte complementoCte = new ComplementoCte();

            complementoCte.setTipoFreteTransporte(this.deParaTipoFrete(evento[22].toString()));
            complementoCte.setTipoModalTransporte(this.deParaTipoModalTransporte(evento[35].toString()));
            complementoCte.setTipoFaturaDell(this.deParaTipoFrete(evento[22].toString()));
            complementoCte.setContaMatrizDell(this.deParaContaMatriz(evento[12].toString(), evento[13].toString(), evento[27].toString(), listCnpjDell.get(0)));
            complementoCte.setNumeroOrdemDell(evento[8].toString());
            complementoCte.setDataColeta(evento[33].toString());
            complementoCte.setPesoCubado(this.formataNumeroDecimal(evento[36], DUAS_CASAS_DECIMAIS));
            complementoCte.setRazaoSocialDell(evento[37].toString());
            complementoCte.setEnderecoOrigem1(evento[38].toString());
            complementoCte.setCidade(evento[39].toString());
            complementoCte.setEstado(evento[40].toString());
            complementoCte.setNomeEntrega(evento[41].toString());
            complementoCte.setEnderecoEntrega1(evento[42].toString());
            complementoCte.setCidadeEntrega(evento[43].toString());
            complementoCte.setEstadoEntrega(evento[44].toString());
            complementoCte.setValorTotalOrigem(evento[14].toString());
            complementoCte.setAdValorem(this.formataNumeroDecimal(evento[45], TRES_CASAS_DECIMAIS));
            complementoCte.setValorTde(this.formataNumeroDecimal(evento[46], TRES_CASAS_DECIMAIS));
            complementoCte.setValorPalete(this.formataNumeroDecimal(evento[51], TRES_CASAS_DECIMAIS));
            complementoCte.setDataComprovante(evento[47] == null ? "0" : evento[47].toString());
            complementoCte.setTipoServico(this.deParaTipoServico(evento[18].toString()));
            complementoCte.setValorIcms(this.formataNumeroDecimal(this.calculaDiferencaValorDevidoTotalParcelas(evento[14], valorTotalFatura), TRES_CASAS_DECIMAIS));
            complementoCte.setValorTotalDespacho(this.formataNumeroDecimal(evento[25], TRES_CASAS_DECIMAIS));
            complementoCte.setValorTotalPedagio(this.formataNumeroDecimal(evento[26], TRES_CASAS_DECIMAIS));
            complementoCte.setValorTotalAgenda(this.formataNumeroDecimal(evento[49], TRES_CASAS_DECIMAIS));
            complementoCte.setValorGris(this.formataNumeroDecimal(evento[50], TRES_CASAS_DECIMAIS));
            complementoCte.setValorFreteValor(this.formataNumeroDecimal(evento[52], TRES_CASAS_DECIMAIS));
            complementoCte.setNumeroFatura(StringUtils.leftPad(evento[2].toString(), 9, "0"));
            complementoCte.setDataEmissaoFatura(evento[9].toString());
            complementoCte.setDataVencimentoFatura(evento[7].toString());
            complementoCte.setFilialCtrc(evento[53].toString());
            complementoCte.setDataEmissaoCte(evento[9].toString());
            complementoCte.setNumeroCte(evento[8].toString());
            complementoCte.setValorTotalFatura(this.formataNumeroDecimal(String.valueOf(valorTotalFatura.add(new BigDecimal(complementoCte.getValorIcms()))), TRES_CASAS_DECIMAIS));
            complementoCte.setValorTrt(this.formataNumeroDecimal(evento[54].toString(), TRES_CASAS_DECIMAIS));
            complementoCte.setTasAdm(this.formataNumeroDecimal(evento[55], TRES_CASAS_DECIMAIS));
            complementoCte.setValorSuframa(this.formataNumeroDecimal(evento[56], TRES_CASAS_DECIMAIS));
            complementoCte.setValorEmex(this.formataNumeroDecimal(evento[57], TRES_CASAS_DECIMAIS));
            complementoCte.setPesoBruto(this.formataNumeroDecimal(evento[21], TRES_CASAS_DECIMAIS));
            complementoCte.setPesoLiquido(this.formataNumeroDecimal(evento[21], TRES_CASAS_DECIMAIS));

            cte.setComplementoCte(complementoCte);

            traxCteDMN.getListaCte().add(cte);
        }
        validaDadosRetornoQueryTraxCte(traxCteDMN);
        return traxCteDMN;
    }

    private void validaDadosRetornoQueryTraxNfse(TraxNfseDMN traxNfseDMNFatura) {
        Fatura fatura = traxNfseDMNFatura.getFatura();
        Nfse nfse = traxNfseDMNFatura.getNfse();
        ComplementoFatura complementoFatura = traxNfseDMNFatura.getComplementoFatura();
        ComplementoNfse complementoNfse = traxNfseDMNFatura.getComplementoNfse();

        validaValor(fatura, "Fatura");
        validaValor(nfse, "NFSE");
        validaValor(complementoFatura, "ComplementoFatura");
        validaValor(complementoNfse, "ComplementoNFSE");
    }

    private void validaDadosRetornoQueryTraxCte(TraxCteDMN faturasDelTraxCte) {
        FaturaTraxCte faturaTraxCte = faturasDelTraxCte.getFaturaTraxCte();
        ComplementoFaturaTraxCte complementoFaturaTraxCte = faturasDelTraxCte.getComplementoFaturaTraxCte();
        List<Cte> listaCte = faturasDelTraxCte.getListaCte();

        validaValor(faturaTraxCte, "FaturaTraxCte");
        validaValor(complementoFaturaTraxCte, "ComplementoFaturaTraxCte");
        validaValor(listaCte, "ListaCte");
    }

    private void validaValor(Object valor, String campo) {
        if(valor == null) {
            throw new BusinessException(" " + campo + " não pode ser nulo ou vazio");
        }
    }

    private String deParaTipoDof(String tpConhecimento) {

        String valor = "";

        if("NO".equals(tpConhecimento) || "RE".equals(tpConhecimento) || "RF".equals(tpConhecimento)) {
            valor = "N";
        }else if ("DE".equals(tpConhecimento) || "DP".equals(tpConhecimento)) {
            valor = "D";
        }else if("CF".equals(tpConhecimento) || "CI".equals(tpConhecimento)) {
            valor = "C";
        }

        return valor;
    }

    private String deParaTipoFreteConhecimento(String tpFrete) {

        String valor = "";

        if("C".equals(tpFrete)) {
            valor = "N";
        }else if("F".equals(tpFrete)){
            valor = "D";
        }

        return valor;
    }

    private String deParaTipoFrete(String tpFrete) {

        String valor = "";

        if("C".equals(tpFrete)) {
            valor = "1";
        }else if("F".equals(tpFrete)){
            valor = "2";
        }

        return valor;
    }

    private String deParaContaMatriz(String cnpjRementente, String cnpjDestinatario, String tipoconhecimento, String raizCnpj) {

        String valor = "";

        if(raizCnpj.equals(StringUtils.substring(cnpjRementente.trim(), 0, 8))
                && raizCnpj.equals(StringUtils.substring(cnpjDestinatario.trim(), 0, 8))) {
            valor = "BRFX0006";
        }else if("DE".equals(tipoconhecimento)) {
            valor = "BRFX0003";
        }else if ("RE".equals(tipoconhecimento)) {
            valor = "BRFX0002";
        }else if("DP".equals(tipoconhecimento)) {
            valor = "BRFX0005";
        }else {
            valor = "BRFX0001";
        }

        return valor;

    }

    private String deParaTipoServico(String pesoReferenciaCalculo) {

        String valor = "";
        BigDecimal valorPeso = new BigDecimal(pesoReferenciaCalculo);

        if((valorPeso.compareTo(new BigDecimal(30)) > 0) && (valorPeso.compareTo(new BigDecimal(150)) == 0)){
            valor = "LTL";
        }else if(valorPeso.compareTo(new BigDecimal(150)) > 0) {
            valor = "FTL";
        }else {
            valor = "SD";
        }

        return valor;
    }

    private String deParaTipoTransporte(String tipoTransporte){

        String valor = "";

        if(tipoTransporte != null) {

            if (tipoTransporte.equals("NSE")) {
                valor = "LTL";
            } else if (tipoTransporte.equals("NTE")) {
                valor = "GRD";
            }
        }
        return valor;
    }

    private String formataNumeroDecimal(Object valor, String pattern) {

        BigDecimal valorDecimal = valor == null ? null : new BigDecimal(valor.toString());

        return FormatUtils.formatDecimal(pattern, valorDecimal, true).replace(",",".");

    }


    private BigDecimal calculaDiferencaValorDevidoTotalParcelas(Object valorDevido, Object totalParcelas) {

        return (new BigDecimal(valorDevido.toString())).subtract(new BigDecimal(totalParcelas.toString())).setScale(2, RoundingMode.HALF_UP) ;

    }

    private BigDecimal calculaSomaTotalParcelas(Object[] valores) {

        return (new BigDecimal(valores[25].toString()).add(new BigDecimal(valores[26].toString())).add(new BigDecimal(valores[45].toString()))
                .add(new BigDecimal(valores[46].toString())).add(new BigDecimal(valores[49].toString())).add(new BigDecimal(valores[50].toString())).add(new BigDecimal(valores[51].toString()))
                        .add(new BigDecimal(valores[52].toString())).add(new BigDecimal(valores[54].toString())).add(new BigDecimal(valores[55].toString())).add(new BigDecimal(valores[56].toString()))
                        ).add(new BigDecimal(valores[57].toString())).setScale(2, RoundingMode.HALF_UP);

    }

    private String deParaTipoModalTransporte(String tipo) {

        String valor = "AIR";

        if("R".equals(tipo)) {
            valor = "GRD";
        }

        return valor;
    }

    public EventoDoctoServicoTelefonicaToutBoxDMN findEnvioCteToutBox(Long idEventoDocumentoServico) {

        EventoDoctoServicoTelefonicaToutBoxDMN eventoDoctoServicoTelefonicaToutBoxDMN = new EventoDoctoServicoTelefonicaToutBoxDMN();
        eventoDoctoServicoTelefonicaToutBoxDMN.setItemCteToutBoxDMNList(new ArrayList<>());
        eventoDoctoServicoTelefonicaToutBoxDMN.setIdEventoDocumentoServico(idEventoDocumentoServico);

        List<Object[]> enviosCtes = this.getEventoDocumentoServicoDAO().findEnvioCteToutBox(idEventoDocumentoServico);

        if(!enviosCtes.isEmpty()) {

            this.validaDadosRetornoQueryEnvioCteToutBox(enviosCtes.get(0));

            ItemCteToutBoxDMN itemCteToutBoxDMN = new ItemCteToutBoxDMN();
            itemCteToutBoxDMN.setValue(enviosCtes.get(0)[1].toString());
            eventoDoctoServicoTelefonicaToutBoxDMN.getItemCteToutBoxDMNList().add(itemCteToutBoxDMN);

            itemCteToutBoxDMN = new ItemCteToutBoxDMN();
            itemCteToutBoxDMN.setValue(enviosCtes.get(0)[2].toString());

            eventoDoctoServicoTelefonicaToutBoxDMN.getItemCteToutBoxDMNList().add(itemCteToutBoxDMN);

            eventoDoctoServicoTelefonicaToutBoxDMN.setnFkey(enviosCtes.get(0)[0].toString());
            eventoDoctoServicoTelefonicaToutBoxDMN.setIdCliente(Long.parseLong(enviosCtes.get(0)[3].toString()));
            eventoDoctoServicoTelefonicaToutBoxDMN.setIdDoctoServico(Long.parseLong(enviosCtes.get(0)[4].toString()));

        }

        return eventoDoctoServicoTelefonicaToutBoxDMN;
    }

    private void validaDadosRetornoQueryEnvioCteToutBox(Object[] valor) {

        String nFkey = valor[0] != null ? valor[0].toString() : null;
        String valorFrete = valor[1] != null ? valor[1].toString() : null;
        String prazoDiasUteis = valor[2] != null ? valor[2].toString() : null;
        String idCliente = valor[3] != null ? valor[3].toString() : null;

        validaValor(nFkey, "NFkey");
        validaValor(valorFrete, "ValorFrete");
        validaValor(prazoDiasUteis, "PrazoDiasUteis");
        validaValor(idCliente, "IdCliente");

    }

    public EventoOcorrenciaEntregaTelefonicaToutBoxDMN findOcorrenciaEntregaToutBox(Long idEventoDocumentoServico) {

        EventoOcorrenciaEntregaTelefonicaToutBoxDMN eventoOcorrenciaEntregaToutBoxDMN = new EventoOcorrenciaEntregaTelefonicaToutBoxDMN();

        eventoOcorrenciaEntregaToutBoxDMN.setIdEventoDocumentoServico(idEventoDocumentoServico);

        List<Object[]> ocorrencias = this.getEventoDocumentoServicoDAO().findOcorrenciaEntregaToutBox(idEventoDocumentoServico);

        if(!ocorrencias.isEmpty()) {

            this.validaDadosRetornoQueryOcorrenciaEntregaToutBox(ocorrencias.get(0));

            EventoData eventoData = new EventoData();
            Events events = new Events();
            eventoData.setEvents(new ArrayList<>());

            eventoData.setNfKey(ocorrencias.get(0)[0].toString());

            events.setEventCode(ocorrencias.get(0)[1].toString());
            events.setDate(ocorrencias.get(0)[2].toString());
            events.setDescription(ocorrencias.get(0)[3].toString());
            events.setAddress(ocorrencias.get(0)[4].toString());
            events.setNumber(ocorrencias.get(0)[5].toString());
            events.setCity(ocorrencias.get(0)[6].toString());
            events.setState(ocorrencias.get(0)[7].toString());

            eventoData.getEvents().add(events);

            eventoOcorrenciaEntregaToutBoxDMN.getEventoDados().getEventsData().add(eventoData);

            eventoOcorrenciaEntregaToutBoxDMN.setIdCliente(Long.parseLong(ocorrencias.get(0)[8].toString()));
            eventoOcorrenciaEntregaToutBoxDMN.setIdDoctoServico(Long.parseLong(ocorrencias.get(0)[9].toString()));

        }

        return eventoOcorrenciaEntregaToutBoxDMN;
    }

    private void validaDadosRetornoQueryOcorrenciaEntregaToutBox(Object[] valor) {

        String nFkey = valor[0] != null ? valor[0].toString() : null;
        String eventoCode = valor[1] != null ? valor[1].toString() : null;
        String date = valor[2] != null ? valor[2].toString() : null;
        String description = valor[3] != null ? valor[3].toString() : null;
        String address = valor[4] != null ? valor[4].toString() : null;
        String number = valor[5] != null ? valor[5].toString() : null;
        String city = valor[6] != null ? valor[6].toString() : null;
        String state = valor[7] != null ? valor[7].toString() : null;

        validaValor(eventoCode, "EventoCode");
        validaValor(date, "Date");
        validaValor(nFkey, "NFkey");
        validaValor(description, "Description");
        validaValor(address, "Address");
        validaValor(number, "Number");
        validaValor(city, "City");
        validaValor(state, "State");

    }

    public String findOcorrenciaEntregaIntelipost(Long idEventoDocumentoServico) throws JsonProcessingException {

        EventoDoctoServicoTelefonicaIntelipostDMN eventoDoctoServicoTelefonicaIntelipostDMN = new EventoDoctoServicoTelefonicaIntelipostDMN();
        eventoDoctoServicoTelefonicaIntelipostDMN.setIdEventoDocumentoServico(idEventoDocumentoServico);

        Map<String, Object> param = new HashMap<>();
        param.put("idEventoDoctoServico" , idEventoDocumentoServico);

        List<Object[]> ocorrenciaEntregaIntelipost = this.getEventoDocumentoServicoDAO().findOcorrenciaEntregaIntelipost(param);

        if(!ocorrenciaEntregaIntelipost.isEmpty()) {

            this.validaDadosRetornoQueryOcorrenciaEntregaIntelipost(ocorrenciaEntregaIntelipost.get(0));

            EventoOcorrenciaEntregaIntelipostDMN eventoOcorrenciaEntrega = new EventoOcorrenciaEntregaIntelipostDMN();
            eventoOcorrenciaEntrega.setEvents(new ArrayList<>());

            eventoOcorrenciaEntrega.setShipper(ocorrenciaEntregaIntelipost.get(0)[0].toString());
            eventoOcorrenciaEntrega.setShipperFederalTaxId(ocorrenciaEntregaIntelipost.get(0)[1].toString());
            eventoOcorrenciaEntrega.setInvoiceKey(ocorrenciaEntregaIntelipost.get(0)[2].toString());

            EventoNotaFiscalIntelipostDMN eventoNotaFiscalIntelipostDMN = new EventoNotaFiscalIntelipostDMN();
            eventoNotaFiscalIntelipostDMN.setEventDate(ocorrenciaEntregaIntelipost.get(0)[3].toString());
            eventoNotaFiscalIntelipostDMN.setOriginalCode(ocorrenciaEntregaIntelipost.get(0)[4].toString());

            eventoOcorrenciaEntrega.getEvents().add(eventoNotaFiscalIntelipostDMN);

            eventoDoctoServicoTelefonicaIntelipostDMN.setEventoOcorrenciaEntregaIntelipostDMN(eventoOcorrenciaEntrega);
            eventoDoctoServicoTelefonicaIntelipostDMN.setIdCliente(Long.parseLong(ocorrenciaEntregaIntelipost.get(0)[5].toString()));
            eventoDoctoServicoTelefonicaIntelipostDMN.setIdDoctoServico(Long.parseLong(ocorrenciaEntregaIntelipost.get(0)[6].toString()));

        }

        return new ObjectMapper().writeValueAsString(eventoDoctoServicoTelefonicaIntelipostDMN);
    }

    private void validaDadosRetornoQueryOcorrenciaEntregaIntelipost(Object[] valor) {

        String shipper = valor[0] != null ? valor[0].toString() : null;
        String shipperFederalTaxId = valor[1] != null ? valor[1].toString() : null;
        String invoiceKey = valor[2] != null ? valor[2].toString() : null;
        String eventDate = valor[3] != null ? valor[3].toString() : null;
        String originalCode = valor[4] != null ? valor[4].toString() : null;

        validaValor(shipper, "Shipper");
        validaValor(shipperFederalTaxId, "ShipperFederalTaxId");
        validaValor(invoiceKey, "InvoiceKey");
        validaValor(eventDate, "EventDate");
        validaValor(originalCode, "OriginalCode");

    }

    private void validaValor(String valor, String campo) {

        if(valor == null || valor.isEmpty()) {
            throw new BusinessException(" " + campo +" não pode ser nulo ou vazio.");
        }
    }

}
