package com.mercurio.lms.entrega.model.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.mercurio.lms.constantes.ConstantesNumeros;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import br.com.tntbrasil.integracao.domains.entrega.OcorrenciaDMN;
import br.com.tntbrasil.integracao.domains.entrega.retornofedex.RetornoOcorrenciaFedexDMN;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.entrega.model.dao.ManifestoEntregaDocumentoDAO;
import com.mercurio.lms.entrega.model.dao.ManifestoEntregaVolumeDAO;
import com.mercurio.lms.expedicao.DoctoServicoLookupFacade;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.AwbOcorrenciaLocalizacao;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.AwbOcorrenciaService;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.model.service.TrackingAwbService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.FaseProcesso;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.RegistroPriorizacaoDocto;
import com.mercurio.lms.sim.model.RegistroPriorizacaoEmbarq;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.sim.model.service.MonitoramentoNotasFiscaisCCTService;
import com.mercurio.lms.sim.model.service.RegistroPriorizacaoDoctoService;
import com.mercurio.lms.sim.model.service.RegistroPriorizacaoEmbarqService;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;
import com.mercurio.lms.vendas.util.ConstantesEventosPCE;
import com.mercurio.lms.vol.model.service.VolBaixarEntregasService;
import com.mercurio.lms.vol.model.service.VolDadosSessaoService;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 *
 * @spring.bean id="lms.entrega.manifestoEntregaDocumentoService"
 */
public class ManifestoEntregaDocumentoService extends CrudService<ManifestoEntregaDocumento, Long> {
    
    private static final String TP_FINALIZACAO_CTE_ORIGINAL = "FCO";
    private static final String TP_LOCALIZACAO_AWB_AGUARDANDO_ENTREGA = "AE";
    private static final String ID_OCORRENCIA_MANIFESTO = "ID_OCORRENCIA_MANIFESTO";
    private static final String A = "A";
    private static final String C = "C";
    private static final String E = "E";
    private static final String F = "F";
    private static final String N = "N";
    private static final String P = "P";
    private static final String R = "R";
    private static final String S = "S";
    private static final String DH_OCORRENCIA = "dhOcorrencia";
    private static final String FECH = "FECH";
    private static final Short CD_OCORRENCIA_ENTREGA_PARCIAL = Short.valueOf("102");

    private DoctoServicoService doctoServicoService;
    private OcorrenciaEntregaService ocorrenciaEntregaService;
    private VersaoDescritivoPceService versaoDescritivoPceService;
    private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
    private PreManifestoDocumentoService preManifestoDocumentoService;
    private DoctoServicoLookupFacade doctoServicoLookupFacade;
    private ControleCargaService controleCargaService;
    private AgendamentoEntregaService agendamentoEntregaService;
    private RegistroPriorizacaoDoctoService registroPriorizacaoDoctoService;
    private ConfiguracoesFacade configuracoesFacade;
    private RegistroPriorizacaoEmbarqService registroPriorizacaoEmbarqService;
    private VolBaixarEntregasService volBaixarEntregasService;
    private ManifestoEntregaVolumeDAO manifestoEntregaVolumeDAO;
    private VolumeNotaFiscalService volumeNotaFiscalService;
    private EventoVolumeService eventoVolumeService;
    private ManifestoEntregaVolumeService manifestoEntregaVolumeService;
    private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
    private ReciboReembolsoService reciboReembolsoService;
    private RegistrarBaixaEntregasService registrarBaixaEntregasService;
    private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
    private MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService;
    private CalcularDiasUteisBloqueioAgendamentoService calcularDiasUteisBloqueioAgendamentoService;
    private DomainValueService domainValueService;
    private PlatformTransactionManager transactionManager;
    private EventoDocumentoServicoService eventoDocumentoServicoService;
    private AwbService awbService;
    private AwbOcorrenciaService awbOcorrenciaService;
    private TrackingAwbService trackingAwbService;
    private ParametroGeralService parametroGeralService;
    private ManifestoEntregaService manifestoEntregaService;
    private ManifestoService manifestoService;
    private UsuarioService usuarioService;
    private PaisService paisService;
    private VolDadosSessaoService volDadosSessaoService;
    private LocalizacaoMercadoriaService localizacaoMercadoriaService;
    private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
    private ComprovanteEntregaService comprovanteEntregaService;
    private ConhecimentoService conhecimentoService;

    /**
     * Recupera uma instância de <code>ManifestoEntregaDocumento</code> a partir
     * do ID.
     *
	 * @param id
	 *            representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     */
    @Override
    public ManifestoEntregaDocumento findById(java.lang.Long id) {
		return (ManifestoEntregaDocumento)super.findById(id);
    }

    /**
     * Apaga uma entidade através do Id.
     *
	 * @param id
	 *            indica a entidade que deverá ser removida.
     */
    @Override
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

    /**
     * Apaga várias entidades através do Id.
     *
	 * @param ids
	 *            lista com as entidades que deverão ser removida.
     *
     *
     */
    @Override
    @ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    /**
     * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
     * contrário.
     *
	 * @param bean
	 *            entidade a ser armazenada.
     * @return entidade que foi armazenada.
     */
    @Override
    public java.io.Serializable store(ManifestoEntregaDocumento bean) {
        return super.store(bean);
    }

    public void saveOrUpdate(DoctoServico bean) {
        getManifestoEntregaDocumentoDAO().getAdsmHibernateTemplate().saveOrUpdate(bean);
    }

    /**
     * Método usado na tela de Didgitar CTRC Reentrega.
     *
     * @param idDoctoServico
     * @return
     */
    public Integer getRowCountManifestoEntregaDoctoServicoReentrega(Long idDoctoServico) {
        return getManifestoEntregaDocumentoDAO().getRowCountManifestoEntregaDoctoServicoReentrega(idDoctoServico);
    }

    /**
     *
     * @param rsp
     */
    private void populateListaByEntregasByProgramacaoColetas(ResultSetPage rsp) {
        List<TypedFlatMap> lista = AliasToTypedFlatMapResultTransformer.getInstance()
                .transformListResult(rsp.getList());
		for(TypedFlatMap tfm : lista) {
            Long idDoctoServico = tfm.getLong("doctoServico.idDoctoServico");
            if (idDoctoServico != null) {
                TypedFlatMap mapDataDoctoServico = doctoServicoService.findDoctoServicoByTpDocumento(idDoctoServico);
                tfm.put("nrDoctoServico", mapDataDoctoServico.get("nrDoctoServico"));
				tfm.put("situacaoDoctoServico", mapDataDoctoServico.getString("tpSituacaoDoctoServico") );
				tfm.put("psDoctoServico", mapDataDoctoServico.getBigDecimal("psDoctoServico") );
				tfm.put("vlTotalDocServico", mapDataDoctoServico.getBigDecimal("vlTotalDocServico") );
				tfm.put("qtVolumes", mapDataDoctoServico.getInteger("qtVolumes") );
                String sgMoeda = tfm.getString("doctoServico.moeda.sgMoeda");
                if (sgMoeda != null) {
                    tfm.put("moeda.siglaSimbolo", sgMoeda + " " + tfm.getString("doctoServico.moeda.dsSimbolo"));
                }
            }
        }
        rsp.setList(lista);
    }

    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     *
     * @param idControleCarga
     * @param idFilial
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedEntregasRealizar(Long idControleCarga, Long idFilial, FindDefinition findDefinition) {
        ResultSetPage rsp = getManifestoEntregaDocumentoDAO().findPaginatedEntregasRealizar(idControleCarga, idFilial,
                findDefinition);
        populateListaByEntregasByProgramacaoColetas(rsp);
        return rsp;
    }

    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados
     * para determinados parametros.
     *
     * @param idControleCarga
     * @param idFilial
     * @return
     */
    public Integer getRowCountEntregasRealizar(Long idControleCarga, Long idFilial) {
        return getManifestoEntregaDocumentoDAO().getRowCountEntregasRealizar(idControleCarga, idFilial);
    }

    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     *
     * @param idControleCarga
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedEntregasRealizadasByProgramacaoColetas(Long idControleCarga,
            FindDefinition findDefinition) {
        ResultSetPage rsp = getManifestoEntregaDocumentoDAO().findPaginatedEntregasRealizadasByProgramacaoColetas(
                idControleCarga, findDefinition);
        populateListaByEntregasByProgramacaoColetas(rsp);
        return rsp;
    }

    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados
     * para determinados parametros.
     *
     * @param idControleCarga
     * @return
     */
    public Integer getRowCountEntregasRealizadasByProgramacaoColetas(Long idControleCarga) {
        return getManifestoEntregaDocumentoDAO().getRowCountEntregasRealizadasByProgramacaoColetas(idControleCarga);
    }

    public void executeBaixaDocumento(Long idManifesto, Long idDoctoServico, Short cdOcorrenciaEntrega,
            String tpFormaBaixa, String tpEntregaParcial, DateTime dhOcorrencia, String nmRecebedor, String obManifesto,
            Boolean isValidExistenciaPceRemetente, Boolean isValidExistenciaPceDestinatario, String rg, List<Long> idsVolumeBaixados,
            String grauParentesco) {
        executeBaixaDocumento(idManifesto, idDoctoServico, cdOcorrenciaEntrega, tpFormaBaixa, tpEntregaParcial, dhOcorrencia,
                nmRecebedor, obManifesto, isValidExistenciaPceRemetente, isValidExistenciaPceDestinatario, Boolean.TRUE, rg, idsVolumeBaixados,
                grauParentesco, SessionUtils.getFilialSessao(), SessionUtils.getUsuarioLogado());
    }

	public DateTime findDhChegadaPortaria(Long idManifesto){
        EventoControleCarga ecc = getManifestoEntregaDocumentoDAO().findEventoControleCarga(idManifesto, "CP");
        return ecc.getDhEvento();
    }

    public void executeBaixaDocumentoIntegracaoFedex(RetornoOcorrenciaFedexDMN retornoOcorrenciaFedexDMN) {

        Long idDoctoServico = retornoOcorrenciaFedexDMN.getIdDoctoServico();

        List<ManifestoEntregaDocumento> manifestos = findManifestoSemOcorrenciaEntregaByIdDoctoServico(idDoctoServico, new String[]{"TC", "TE", "ED", "AD"});
        ManifestoEntregaDocumento med = manifestos.get(0);
        ManifestoEntrega manifestoEntrega = manifestoEntregaService.findById(med.getManifestoEntrega().getIdManifestoEntrega());

        Usuario usuario = usuarioService.findUsuarioByLogin("integracao");
        Manifesto manifesto = (Manifesto) manifestoService.findById(manifestoEntrega.getManifesto().getIdManifesto());
        Filial filial = manifesto.getControleCarga().getFilialByIdFilialOrigem();
        Pais pais = paisService.findByIdPessoa(filial.getIdFilial());
        volDadosSessaoService.executeDadosSessaoBanco(usuario, filial, pais);

        for (OcorrenciaDMN ocorrenciaDMN : retornoOcorrenciaFedexDMN.getListOcorrencias()) {

            Short cdOcorrenciaEntrega = Short.valueOf(ocorrenciaDMN.getCodOcorrencia());

            Boolean isValidExistenciaPceRemetente = Boolean.FALSE;
            Boolean isValidExistenciaPceDestinatario = Boolean.FALSE;
            Boolean isDocumentoFisico = Boolean.TRUE;
            String nmRecebedor = ocorrenciaDMN.getNomeRecebedor();
            String obManifesto = "";
            DateTime dhOcorrencia = JTDateTimeUtils.formatStringToDateTimeWithSeconds(ocorrenciaDMN.getDataRegistroOcorrencia());
            String tpFormaBaixa = P;
            String tpEntregaParcial = "";

            this.executeBaixaDocumento(
                    manifestoEntrega.getIdManifestoEntrega(), idDoctoServico, cdOcorrenciaEntrega, tpFormaBaixa, tpEntregaParcial, dhOcorrencia, nmRecebedor,
                    obManifesto, isValidExistenciaPceRemetente, isValidExistenciaPceDestinatario, isDocumentoFisico, null, null, null, SessionUtils.getFilialSessao(), SessionUtils.getUsuarioLogado());
        }

    }
    
    @SuppressWarnings("unchecked")
    public void executeBaixaDocumento(Long idManifesto, Long idDoctoServico, Short cdOcorrenciaEntrega,
            String tpFormaBaixa, String tpEntregaParcial, DateTime dhOcorrencia, String nmRecebedor, String obManifesto,
            Boolean isValidExistenciaPceRemetente, Boolean isValidExistenciaPceDestinatario, Boolean isDocumentoFisico,
            String rg, List<Long> idsVolumeBaixados, String grauParentesco, Filial filialSessao, Usuario usuarioLogado) {
    	
       //LMS-4332 
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
       
		if (dhOcorrencia == null)
            dhOcorrencia = JTDateTimeUtils.getDataHoraAtual();

		if (isValidExistenciaPceDestinatario == null)
            isValidExistenciaPceDestinatario = Boolean.TRUE;

		if (isValidExistenciaPceRemetente == null)
            isValidExistenciaPceRemetente = Boolean.TRUE;

        if (nmRecebedor != null && !"".equals(nmRecebedor)) {
            if (nmRecebedor.length() < ConstantesNumeros.CINCO
                    || !nmRecebedor.matches("[a-zA-ZÁÉÍÓÚáéíóúâêôûãõç\\s][0-9a-zA-ZÁÉÍÓÚáéíóúâêôûãõç\\s]*")) {
				throw new BusinessException("LMS-04153", new Object[] { "Recebedor" });
            }
        }

        /* 1 - Verificar se existe alguma ocorrência de entrega com o código recebido 
		   (tabela OCORRENCIA_ENTREGA onde CD_OCORRENCIA_ENTREGA = cdOcorrenciaEntrega). 
		   Se não existir mostrar mensagem LMS-09043 passando o código recebido e abortar a operação */
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("cdOcorrenciaEntrega", cdOcorrenciaEntrega);
        List<OcorrenciaEntrega> ocorrencias = ocorrenciaEntregaService.find(criteria);
		if (ocorrencias.isEmpty() || cdOcorrenciaEntrega == null)
			throw new BusinessException("LMS-09043",new Object[]{cdOcorrenciaEntrega});
        OcorrenciaEntrega ocorrenciaEntrega = ocorrencias.get(0);
        
        /* 2 - Apenas se DocumentoFisico = TRUE. Se a ocorrência de entrega for do tipo 
		   “Entrega” “Entrega Parcial” (OCORRENCIA_ENTREGA.TP_OCORRENCIA = ‘E’ OU ‘P’): 
		   	 Se for um documento de serviço cujo tipo de serviço seja aéreo (DOCTO_SERVICO->SERVICO.TP_MODAL = ‘A’) 
		   	 ou o cliente remetente obrigue que se informe o nome do recebedor 
		   	 (DOCTO_SERVICO->CLIENTE_REMETENTE->CLIENTE.BL_OBRIGA_RECEBEDOR = ‘S’)
		   e o Recebedor não tenha sido informado mostrar mensagem LMS-09039 e abortar a operação */
        DoctoServico docto = (DoctoServico) getManifestoEntregaDocumentoDAO().getAdsmHibernateTemplate().load(
                DoctoServico.class, idDoctoServico);

        Boolean isEntregaParcial = eventoDocumentoServicoService.validateEntregaParcial(idDoctoServico);
        if(!ocorrenciaEntrega.getCdOcorrenciaEntrega().equals(CD_OCORRENCIA_ENTREGA_PARCIAL) && 
                isEntregaParcial && 
                !TP_FINALIZACAO_CTE_ORIGINAL.equals(tpEntregaParcial)){
            throw new BusinessException("LMS-09166");
        }
        
        if(TP_FINALIZACAO_CTE_ORIGINAL.equals(tpEntregaParcial)){
            tpEntregaParcial = null;
        }
        
        Cliente clienteRemetente = docto.getClienteByIdClienteRemetente();        
        // LMSA-7476        
        if(E.equals(ocorrenciaEntrega.getTpOcorrencia().getValue())){
            this.verificaObriagatoriedadeRg(clienteRemetente.getBlObrigaRg(), tpFormaBaixa, rg);
        }
        this.verificaObrigatoriedadeRgEdi(clienteRemetente.getBlObrigaRgEdi(), tpFormaBaixa, rg);
        // LMSA-7476
        
        FaseProcesso faseProcesso = ocorrenciaDoctoServicoService.getFaseProcessoService().findByIdDoctoServico(idDoctoServico);
        
        if (isDocumentoFisico.booleanValue()) {
            if (E.equals(ocorrenciaEntrega.getTpOcorrencia().getValue())
                    || P.equals(ocorrenciaEntrega.getTpOcorrencia().getValue())) {

                boolean isObrigaRecebedor = (clienteRemetente != null
                        && clienteRemetente.getBlObrigaRecebedor() != null && clienteRemetente.getBlObrigaRecebedor());
                boolean isRecebedorNaoInformado = (StringUtils.isBlank(nmRecebedor));

                //LMS-3952 - Obrigatoriedade da observação "Obrigatório nome e recebedor" para TNT Express - rodoviário e aéreo
                boolean isClienteDestinatarioParametroObrigaRecebedor = isClienteParametroObrigaRecebedor(docto.getClienteByIdClienteDestinatario());
                boolean isClienteDevedorParametroObrigaRecebedor = isClienteParametroObrigaRecebedor(docto.getDevedorDocServs() != null
                        && docto.getDevedorDocServs().get(0) != null ? docto.getDevedorDocServs().get(0).getCliente() : null);

			
                if ((isObrigaRecebedor || isClienteDestinatarioParametroObrigaRecebedor || isClienteDevedorParametroObrigaRecebedor)
						&& isRecebedorNaoInformado)
                    throw new BusinessException("LMS-09039");
                }
            }

        Awb awb = awbService.findByIdDoctoServicoAndFilialOrigem(docto.getIdDoctoServico(), filialSessao.getIdFilial());
		if(ocorrenciaEntrega.getCdOcorrenciaEntrega().intValue() == ConstantesEventosDocumentoServico.CD_OCORRENCIA_ENTREGA_REALIZADA_AEROPORTO 
                && awb == null) {
            throw new BusinessException("LMS-09145");
        }

        if (awb != null) {
            Filial filialDestino = awb.getFilialByIdFilialDestino();
			if(ocorrenciaEntrega.getCdOcorrenciaEntrega().intValue() == ConstantesEventosDocumentoServico.CD_OCORRENCIA_ENTREGA_REALIZADA	
                    && !filialDestino.getIdFilial().equals(filialSessao.getIdFilial())) {
                throw new BusinessException("LMS-09146");
            }
        }

        /*
		 * 3 - Se a data/hora da baixa (dhBaixa) for menor que a data/hora de
		 * emissão do manifesto de entrega (MANIFESTO.DH_EMISSAO) mostrar
		 * mensagem LMS-09047 e abortar a operação
         */
        Manifesto manifesto = (Manifesto) getManifestoEntregaDocumentoDAO().getAdsmHibernateTemplate().load(
                Manifesto.class, idManifesto);
        if ((manifesto.getDhEmissaoManifesto().compareTo(dhOcorrencia) > 0)
                && (findDataUltimaOcorrenciaManifesto(idDoctoServico) == null)) {
            throw new BusinessException("LMS-09047");
        }

        // LMS 3169
		if(StringUtils.isNotBlank(tpEntregaParcial)){
			if(manifesto.getTpManifestoViagem() != null){
				if("ED".equals(manifesto.getTpManifestoViagem().getValue())){
                    throw new BusinessException("LMS-09142");
                }
            }

			if(tpFormaBaixa != null && !C.equals(tpFormaBaixa)){
                SessionContext.set("LMS-09143", true);
            }
        }

		
        /*
		 * 4 - Apenas se DocumentoFisico = TRUE. Se a filial estiver configurada
		 * para baixa de documentos na ordem * (FILIAL.
		 * BL_OBRIGA_BAIXA_ENTREGA_ORDEM=’S’): Se a ordem de baixa do documento
		 * de serviço for diferente de 1 (PRE_MANIFESTO_DOCUMENTO.NR_ORDEM <>
		 * 1): Se o documento de serviço de ordem imediatamente anterior não já
		 * tiver sido baixado (Buscar registro na tabela PRE_MANIFESTO_DOCUMENTO
		 * para o mesmo manifesto onde o documento está localizado e a ordem
		 * seja igual a ordem (NR_ORDEM) do documento de serviço -1, associar a
		 * tabela MANIFESTO_ENTREGA_DOCUMENTO onde ID_OCORRENCIA_ENTREGA IS
		 * NULL): Mostrar mensagem LMS-09055 e abortar a operação
         */
        Filial filial = filialSessao;

        //LMS-3569 - Se o tipo de baixa é 'Celular' troca a filial para a do CC
        if (tpFormaBaixa != null && tpFormaBaixa.equals(C)) {
            filial = manifesto.getControleCarga().getFilialByIdFilialOrigem();
        }

        if (isDocumentoFisico.booleanValue()) {
            if (filial.getBlObrigaBaixaEntregaOrdem().booleanValue()) {
                PreManifestoDocumento pre = preManifestoDocumentoService.findPreManifestoDocumentoByIdManifestoDocto(
                        manifesto.getIdManifesto(), docto.getIdDoctoServico());
                if (pre.getNrOrdem().intValue() > 1) {
                    PreManifestoDocumento pre2 = preManifestoDocumentoService.findPreManifestoDocumentoByIdManifesto(
                            manifesto.getIdManifesto(), Integer.valueOf(pre.getNrOrdem().intValue() - 1));
                    if (pre2 != null) {
                        ManifestoEntregaDocumento temp = getManifestoEntregaDocumentoDAO()
                                .findByIdDoctoServicoManifesto(pre2.getDoctoServico().getIdDoctoServico(),
                                        pre2.getManifesto().getIdManifesto());
					if (temp == null || temp.getOcorrenciaEntrega() == null)
                            throw new BusinessException("LMS-09055");
                        }
                    }
                }
            }

        /*
		 * 5 - Se a forma de baixa for normal (tpFormaBaixa = ‘N’) ou a partir
		 * do fechamento (tpFormaBaixa = ‘F’) Se a data/hora da baixa (dhBaixa)
		 * for menor que a data/hora de saída do controle de cargas na portaria
		 * (MANIFESTO -> CONTROLE_CARGA -> EVENTO_CONTROLE_CARGA.DH_EVENTO onde
		 * TP_EVENTO_CONTROLE_CARGA = ‘SP’) mostrar mensagem LMS-09046 e abortar
		 * a operação; Se a data/hora da baixa (dhBaixa) for maior que a
		 * data/hora de entrada do controle de cargas na portaria (MANIFESTO ->
		 * CONTROLE_CARGA -> EVENTO_CONTROLE_CARGA.DH_EVENTO onde
		 * TP_EVENTO_CONTROLE_CARGA = ‘CP’) mostrar mensagem LMS-09046 e abortar
		 * a operação
         */
		
        if (null != manifesto.getTpManifestoEntrega()
                && !"CR".equals(manifesto.getTpManifestoEntrega().getValue())
                && !"PR".equals(manifesto.getTpManifestoEntrega().getValue())) {
            EventoControleCarga ecc = getManifestoEntregaDocumentoDAO().findEventoControleCarga(idManifesto, "CP");

            if (ecc != null && dhOcorrencia.compareTo(ecc.getDhEvento()) > 0) {
				if(C.equals(tpFormaBaixa)){
                    return;
                } else {
                    throw new BusinessException("LMS-09046");
                }
            }
        }

		
        if (N.equals(tpFormaBaixa) || F.equals(tpFormaBaixa)) {
            EventoControleCarga ecc = getManifestoEntregaDocumentoDAO().findEventoControleCarga(idManifesto, "CP");

            ecc = getManifestoEntregaDocumentoDAO().findEventoControleCarga(idManifesto, "SP");
			if (ecc != null && ecc.getDhEvento().compareTo(dhOcorrencia) > 0)
                throw new BusinessException("LMS-09046");
            }

		
        /*
		 * 6 e 7 Se validarPCE for verdadeiro: Chamar rotina Verificar
		 * existência PCE (item 01.01.04.06 Visualizar mensagem ação PCE)
		 * passando: - ID_CLIENTE_DESTINATARIO (destinatário do documento que se
		 * está baixando); - 8 – Processo; - 19 – Evento; - Se a ocorrência for
		 * do tipo Reentrega solicitada (‘S’): 29 – Ocorrência; - Se a
		 * ocorrência for do tipo Entrega (‘E’): 27 – Ocorrência; - Se a
		 * ocorrência for do tipo Não entregue (‘N’): 28 – Ocorrência; - Caso
		 * contrário: 30 – Ocorrência
         */
        if (isDocumentoFisico.booleanValue()) {
            if (isValidExistenciaPceDestinatario.booleanValue() || isValidExistenciaPceRemetente.booleanValue()) {
                String tpOcorrencia = ocorrenciaEntrega.getTpOcorrencia().getValue();
                Long ocorrencia;
			if (S.equals(tpOcorrencia))
                    ocorrencia = ConstantesEventosPCE.CD_OCORRENCIA_TIPO_REENTREGA;
			else if (E.equals(tpOcorrencia))
                    ocorrencia = ConstantesEventosPCE.CD_OCORRENCIA_TIPO_ENTREGA;
			else if (N.equals(tpOcorrencia))
                    ocorrencia = ConstantesEventosPCE.CD_OCORRENCIA_TIPO_NAO_ENTREGUE;
			else
                    ocorrencia = ConstantesEventosPCE.CD_OCORRENCIA;

                Long cdProcesso = ConstantesEventosPCE.CD_PROCESSO_ENTREGA;
                Long cdEvento = Long.valueOf(ConstantesNumeros.DEZENOVE);

                if (isValidExistenciaPceRemetente.booleanValue() && clienteRemetente != null) {
                    Long id = versaoDescritivoPceService.validateifExistPceByCriteria(clienteRemetente.getIdCliente(), cdProcesso, cdEvento, ocorrencia);
				if (id != null)
                        throw new BusinessException(new StringBuffer("PCE_R_").append(id).toString());
                    }
                if (isValidExistenciaPceDestinatario.booleanValue() && docto.getClienteByIdClienteDestinatario() != null) {
                    Long id = versaoDescritivoPceService.validateifExistPceByCriteria(docto.getClienteByIdClienteDestinatario().getIdCliente(),
                            cdProcesso, cdEvento, ocorrencia);
				if (id != null)
                        throw new BusinessException(new StringBuffer("PCE_D_").append(id).toString());
                    }
                }
            }

        //LMS-5056 - parte 1
        LocalizacaoMercadoria localizacaoMercadoriaAux = null;
		if(!tpFormaBaixa.equals(A) && ocorrenciaEntrega.getOcorrenciaPendencia() != null){
            ManifestoEntregaDocumento manifestoEntregaDocumento = getManifestoEntregaDocumentoDAO().findLastManifestoEntregaDocumentoByIdDoctoServico(idDoctoServico);

				if(manifestoEntregaDocumento != null 
                    && manifestoEntregaDocumento.getManifestoEntrega() != null
                    && manifestoEntregaDocumento.getManifestoEntrega().getManifesto() != null
                    && manifestoEntregaDocumento.getManifestoEntrega().getManifesto().getTpManifestoEntrega() != null
                    && !"CR".equals(manifestoEntregaDocumento.getManifestoEntrega().getManifesto().getTpManifestoEntrega().getValue())
					&& !"PR".equals(manifestoEntregaDocumento.getManifestoEntrega().getManifesto().getTpManifestoEntrega().getValue())){

                localizacaoMercadoriaAux = docto.getLocalizacaoMercadoria();
            }
        }

        /*
		 * 9 - Se houver evento associado a ocorrência de entrega
		 * (OCORRENCIA_ENTREGA.ID_EVENTO IS NOT NULL): Incluir evento no
		 * documento de serviço. Chamar rotina Gerar Evento Documento.
         */
        if (ocorrenciaEntrega.getEvento() != null) {
            if ((docto.getLocalizacaoMercadoria() != null
                    && !docto.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA)
                    && !docto.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesSim.CD_MERCADORIA_CANCELADA)
                    && !docto.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesExpedicao.CD_MERCADORIA_DEVOLVIDA)
                    && !docto.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesExpedicao.CD_MERCADORIA_REFATURADA)
                    && docto.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().intValue() != ConstantesNumeros.TRINTA_NOVE)
					|| docto.getLocalizacaoMercadoria() == null){
                String dsObs = null;
				if(StringUtils.isNotBlank(tpEntregaParcial)) {
                    dsObs = domainValueService.findDomainValueDescription("DM_TIPO_ENTREGA_PARCIAL", tpEntregaParcial).toLowerCase();
                }

                incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(ocorrenciaEntrega.getEvento().getCdEvento(),
                        idDoctoServico, filial.getIdFilial(),
                        doctoServicoLookupFacade.formatarDocumentoByTipo(docto.getTpDocumentoServico().getValue(),
                                docto.getFilialByIdFilialOrigem().getSgFilial(), docto.getNrDoctoServico().toString()),
                        dhOcorrencia, null, dsObs, docto.getTpDocumentoServico().getValue(),
                        ocorrenciaEntrega.getIdOcorrenciaEntrega(), null, null, usuarioLogado);
            }
        }

        // 10		
        if (manifesto.getTpManifesto().getValue().equals(ConstantesEntrega.TP_MANIFESTO_ENTREGA)) {
            List<ManifestoEntregaVolume> listManifEntVol =  getManifestoEntregaVolumeDAO().findByManifestoAndDoctoServico(idManifesto, idDoctoServico);   
            // 8
            boolean existeBaixa = false;
            
                for (ManifestoEntregaVolume manifestoEntregaVolume : listManifEntVol) {
                    short cdOcorrenciaEntregaVolume = cdOcorrenciaEntrega;

                    if (manifestoEntregaVolume.getDhOcorrencia() != null) {
                        existeBaixa = true;
                    } else {
                        if (P.equals(ocorrenciaEntrega.getTpOcorrencia().getValue())) {
                            cdOcorrenciaEntregaVolume = ConstantesNumeros.UM;
                        }
                    }
                    if(idsVolumeBaixados != null && !idsVolumeBaixados.isEmpty()){
                        if(!idsVolumeBaixados.contains(manifestoEntregaVolume.getVolumeNotaFiscal().getIdVolumeNotaFiscal())){
                            cdOcorrenciaEntregaVolume = "FV".equals(tpEntregaParcial) ? (short) ConstantesNumeros.TRINTA_SETE : "AV".equals(tpEntregaParcial) ? (short) ConstantesNumeros.TRINTA_OITO : !idsVolumeBaixados.isEmpty() ? (short) 1 : null;
                        } else {
                            cdOcorrenciaEntregaVolume = ConstantesNumeros.UM;
                        }
                    } else {
                        cdOcorrenciaEntregaVolume = cdOcorrenciaEntrega;
                    }
                    
                    /**
                     * Para evitar erros de relatorio, caso o tpFormaBaixa='E' passar para tpFormaBaixa='N'
                     */
                    Boolean tipoFormaBaixaEdi=false;
                    if (E.equalsIgnoreCase(tpFormaBaixa)){
                    	tipoFormaBaixaEdi=true;
                    	tpFormaBaixa = N;
                    }
                    
                    // LMS-6504 (foi movido para fora do else, assim sempre atualizando o ManifestoEntregaVolume, não somente quando a DH_OCORRENCIA for diferente de null)
                    
                    if(!ocorrenciaEntrega.getCdOcorrenciaEntrega().equals(CD_OCORRENCIA_ENTREGA_PARCIAL) && !TP_FINALIZACAO_CTE_ORIGINAL.equals(tpEntregaParcial)){
                        registrarBaixaEntregasService.storeOcorrenciaOnManifestoEntregaVolume(
                                manifestoEntregaVolume.getIdManifestoEntregaVolume(), cdOcorrenciaEntregaVolume,
                                tpFormaBaixa, dhOcorrencia, isValidExistenciaPceDestinatario,
                                isValidExistenciaPceRemetente, filial, usuarioLogado);
                    }
                    
                    if (tipoFormaBaixaEdi){
                    	tpFormaBaixa= E;
                    }
                }

            List<ManifestoEntregaDocumento> manifestoDocList = getManifestoEntregaDocumentoDAO().findManifestoByDoctoServicoManifesto(idDoctoServico, idManifesto);
            for (ManifestoEntregaDocumento manifestoDoc : manifestoDocList) {
                if (P.equals(ocorrenciaEntrega.getTpOcorrencia().getValue()) && !existeBaixa) {
                    throw new BusinessException("LMS-09xxx");
                    /*
					 * LMS-09xxxx Impossivel baixar o documento como Baixa
					 * Parcial, se todos os volumes estão sem informação de
					 * baixa.
                     */
                }
                if( StringUtils.isNotBlank(rg)) {
                	manifestoDoc.setNmRgRecebedor(rg);
                }
                
                if (StringUtils.isNotBlank(grauParentesco)) {
                    manifestoDoc.setTpGrauRecebedor(new DomainValue(grauParentesco));
                } 
               
                if (isDocumentoFisico.booleanValue()) {
                    manifestoDoc.setDhOcorrencia(dhOcorrencia);
                    manifestoDoc.setTpFormaBaixa(new DomainValue(tpFormaBaixa));
					if(StringUtils.isNotBlank(tpEntregaParcial)) {
                        manifestoDoc.setTpEntregaParcial(new DomainValue(tpEntregaParcial));
					}else{
                        manifestoDoc.setTpEntregaParcial(null);
                    }
                    manifestoDoc.setNmRecebedor(nmRecebedor);
                    manifestoDoc.setObManifestoEntregaDocumento(obManifesto);
                    manifestoDoc.setOcorrenciaEntrega(ocorrenciaEntrega);

                    if (E.equals(ocorrenciaEntrega.getTpOcorrencia().getValue())) {
						if ("PBRC".equals(manifestoDoc.getTpSituacaoDocumento().getValue()))
                            manifestoDoc.setTpSituacaoDocumento(new DomainValue("PRCO"));
						else if ("PBRE".equals(manifestoDoc.getTpSituacaoDocumento().getValue()))
                            manifestoDoc.setTpSituacaoDocumento(new DomainValue("PREC"));
						else if ("PBCO".equals(manifestoDoc.getTpSituacaoDocumento().getValue()))
                            manifestoDoc.setTpSituacaoDocumento(new DomainValue("PCOM"));
						else if ("PBAI".equals(manifestoDoc.getTpSituacaoDocumento().getValue()))
                            manifestoDoc.setTpSituacaoDocumento(new DomainValue(FECH));
                    } else {
                        manifestoDoc.setTpSituacaoDocumento(new DomainValue(FECH));
                    }
					if(!"A".equals(tpFormaBaixa)) {
                        AgendamentoEntrega ae = agendamentoEntregaService.findAgendamentoAbertoDoctoServico(idDoctoServico);
                        if (ae != null) {
							if(ae.getDtAgendamento() != null){
                                ae.setDhFechamento(new DateTime(ae.getDtAgendamento().getYear(), ae.getDtAgendamento().getMonthOfYear(), ae.getDtAgendamento().getDayOfMonth(), 0, 0, 0, 0));
                            }
                            ae.setTpSituacaoAgendamento(new DomainValue(ConstantesEntrega.TP_SITUACAO_AGENDAMENTO_FECHADO));
                            agendamentoEntregaService.store(ae);
                        }
                    }
                }
            }
            getManifestoEntregaDocumentoDAO().getAdsmHibernateTemplate().saveOrUpdateAll(manifestoDocList);

        } else if (manifesto.getTpManifesto().getValue().equals(ConstantesEntrega.TP_MANIFESTO_VIAGEM)) {
            if (isDocumentoFisico.booleanValue()) {
                // 11
                AgendamentoEntrega ae = agendamentoEntregaService.findAgendamentoAberto(idDoctoServico);
                if (ae != null) {
				if(ae.getDtAgendamento() != null){
                        ae.setDhFechamento(new DateTime(ae.getDtAgendamento().getYear(), ae.getDtAgendamento().getMonthOfYear(), ae.getDtAgendamento().getDayOfMonth(), 0, 0, 0, 0));
                    }

                    ae.setTpSituacaoAgendamento(new DomainValue(ConstantesEntrega.TP_SITUACAO_AGENDAMENTO_FECHADO));
                    agendamentoEntregaService.store(ae);
                }
            }
        }

        /*
		 * LMS-4567 
		 * Se as ocorrências de entrega possuir ocorrência de pendência
		 * vinculada (OCORRENCIA_ENTREGA.id_ocorrencia_pendencia IS NOT NULL)
		 * Lançar um bloqueio para o documento chamando a rotina
		 * "Bloquear ou Liberar documento de serviço por ocorrência" da ET:
		 * 17.01.01.03 Registrar ocorrência para Documento de Serviço passando
		 * por parâmetro: - IdDocumento := ID_DOCTO_SERVICO
		 * OcorrênciaPendência := OCORRENCIA_ENTREGA.id_ocorrencia_pendencia
		 * DhOcorrencia := MANIFESTO_ENTREGA_DOCUMENTO.dh_ocorrencia.
		 * 
		 * Regra deve ser aplicada independente do tipo de manifesto, e fora do "for" de volumes.
         */
		if(!tpFormaBaixa.equals(A) && ocorrenciaEntrega.getOcorrenciaPendencia() != null){
            DateTime dhOcorenciaBloqueioLiberacao = null;

            ManifestoEntregaDocumento manifestoEntregaDocumento = getManifestoEntregaDocumentoDAO().findLastManifestoEntregaDocumentoByIdDoctoServico(idDoctoServico);
			if(manifestoEntregaDocumento != null){
                dhOcorenciaBloqueioLiberacao = manifestoEntregaDocumento.getDhOcorrencia();
            }

            ocorrenciaDoctoServicoService.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(idDoctoServico,
                    ocorrenciaEntrega.getOcorrenciaPendencia().getIdOcorrenciaPendencia(), null, dhOcorenciaBloqueioLiberacao, faseProcesso);
        }

        //LMS-5056 - parte 2
		if(localizacaoMercadoriaAux != null){
            docto.setLocalizacaoMercadoria(localizacaoMercadoriaAux);
            getManifestoEntregaDocumentoDAO().getAdsmHibernateTemplate().saveOrUpdate(docto);

            volumeNotaFiscalService.updateLocalizacaoMercadoriaByDoctoServico(docto.getIdDoctoServico(), docto.getLocalizacaoMercadoria());
        }

		if(manifesto.getTpManifestoEntrega() != null && "EP".equals(manifesto.getTpManifestoEntrega().getValue())){

            String recusaTpManifEP = N;
            try {
				recusaTpManifEP = (String) configuracoesFacade.getValorParametro(docto.getFilialByIdFilialDestino().getIdFilial(), "RECUSA_TP_MANIF_EP");
            } catch (BusinessException e) {
            }
			if(S.equals(recusaTpManifEP) &&
					(R.equals(ocorrenciaEntrega.getTpOcorrencia().getValue()) || N.equals(ocorrenciaEntrega.getTpOcorrencia().getValue())) &&
					ocorrenciaEntrega.getBlOcasionadoMercurio()){

                LocalizacaoMercadoria localizacaoNoTerminal = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(ConstantesSim.CD_MERCADORIA_NO_TERMINAL);
                docto.setLocalizacaoMercadoria(localizacaoNoTerminal);
                manifestoEntregaDocumentoService.saveOrUpdate(docto);

                volumeNotaFiscalService.updateLocalizacaoMercadoriaByDoctoServico(docto.getIdDoctoServico(), docto.getLocalizacaoMercadoria());

				if(ocorrenciaEntrega.getOcorrenciaPendencia() != null){

                    OcorrenciaDoctoServico ocorrenciaDoctoServicoBuscado = ocorrenciaDoctoServicoService.findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(docto.getIdDoctoServico());
                    FaseProcesso fp = new FaseProcesso();
                    fp.setCdFase(ConstantesExpedicao.CD_FASE_PROCESSO);

                    ocorrenciaDoctoServicoBuscado.setFaseProcesso(ocorrenciaDoctoServicoService.getFaseProcessoService().findByCdFaseProcesso(fp).get(0));
                    ocorrenciaDoctoServicoService.store(ocorrenciaDoctoServicoBuscado);
                }
            }
        }

        // 12
        if (isDocumentoFisico.booleanValue()) {
            // LMS-4187 chama o calculo dos dias somente se documento fisico = 'S'. item 18.1
            calcularDiasUteisBloqueioAgendamentoService.executeCalcularDiasUteisBloqueioAgendamento(docto, Boolean.TRUE);

            if (E.equals(ocorrenciaEntrega.getTpOcorrencia().getValue())) {
                ReciboReembolso reciboReembolso = getManifestoEntregaDocumentoDAO().findReciboReembolsoAssociado(idDoctoServico);
                if (reciboReembolso != null) {
                    reciboReembolso.setTpSituacaoRecibo(new DomainValue("EN"));
                    reciboReembolsoService.store(reciboReembolso);
                }
            }

            ControleCarga controleCarga = manifesto.getControleCarga();
		if( (controleCarga != null) && (C.equals(controleCarga.getTpControleCarga().getValue())) ) {
                controleCargaService.generateAtualizacaoTotaisParaCcColetaEntregaByIdControleCarga(
                        controleCarga.getIdControleCarga(), true, filialSessao);
            }

            // 14
            if (!E.equals(ocorrenciaEntrega.getTpOcorrencia().getValue())) {
                String parametroOcorrencias = (String) configuracoesFacade
                        .getValorParametro("CD_OCORRENCIAS_NAO_CANC_PRIOR_EMBARQ");

                if (StringUtils.isNotBlank(parametroOcorrencias)) {
                    boolean cancela = true;
                    StringTokenizer stOcorrencias = new StringTokenizer(parametroOcorrencias, ";");
                    while (stOcorrencias.hasMoreTokens()) {
                        if (cdOcorrenciaEntrega.equals(Short.valueOf(stOcorrencias.nextToken()))) {
                            cancela = false;
                            break;
                        }
                    }

                    if (cancela) {
                        List<RegistroPriorizacaoDocto> registros = registroPriorizacaoDoctoService.findByIdDocto(
                                docto.getIdDoctoServico(), Boolean.FALSE, null);
                        if (registros != null && !registros.isEmpty()) {
                            for (RegistroPriorizacaoDocto rpd : registros) {
                                RegistroPriorizacaoEmbarq rpe = rpd.getRegistroPriorizacaoEmbarq();
                                Long idReciboPriorizacaoEmbarque = rpe.getIdRegistroPriorizacaoEmbarq();
                                String obCancelamento = configuracoesFacade.getMensagem(
                                        "canceladaOcorrenciaEntrega",
								new Object[] { ocorrenciaEntrega.getCdOcorrenciaEntrega(), ocorrenciaEntrega.getDsOcorrenciaEntrega() });

                                registroPriorizacaoEmbarqService.cancelaRegistro(idReciboPriorizacaoEmbarque, obCancelamento);
                            }
                        }
                    }
                }
            }

            // 22
            /**
             * Gera o registro de ocorrência de recusa para o VOL
             *
             * @author lucianos
             */
            ManifestoEntregaDocumento manifestoEntregaDocumento = getManifestoEntregaDocumentoDAO().findByIdDoctoServicoManifesto(idDoctoServico, idManifesto);
            getVolBaixarEntregasService().generateRegistroRecusa(cdOcorrenciaEntrega, manifestoEntregaDocumento);

        }

        //LMS-4332
        List<EventoDocumentoServico> lista = eventoDocumentoServicoService.findEventoDocumentoServicoComEntregaRealizada(idDoctoServico);

        // REGRA - 16
        // LMS-3252
        // Atenção: Somente executar este item se o documento de serviço foi
        // baixado como “Entrega Realizada”
	    if(cdOcorrenciaEntrega == 1) {
            // Executar uma consulta de notas fiscais vinculadas ao documento
            // recebido pelo parâmetro (NOTA_FISCAL_CONHECIMENTO.ID_CONHECIMENTO
            // = <idDocumentoServico>)
            List<NotaFiscalConhecimento> nfcs = this.notaFiscalConhecimentoService.findByConhecimento(idDoctoServico);

            for (NotaFiscalConhecimento nfc : nfcs) {
                this.monitoramentoNotasFiscaisCCTService.storeEvento("RE", nfc.getNrChave(), null, null, null, null, null, usuarioLogado);
            }

            // LMS-4332 - Contingência para
            // "3. Documentos com duplicidade de evento de entrega lançados com 1 minuto de diferença".
            if (!lista.isEmpty() && lista.size() > 1) {
                transactionManager.rollback(status);
                return;
            }
        }

        // LMS-4332 - Contingência para
        // "1. Documentos com ocorrência de entrega no manifesto, e com localização de entrega realizada, mas sem evento de entrega".
        if (lista.isEmpty() && docto.getLocalizacaoMercadoria() != null
                && docto.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA)) {

            incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(ocorrenciaEntrega.getEvento().getCdEvento(), idDoctoServico,
                    filial.getIdFilial(), doctoServicoLookupFacade.formatarDocumentoByTipo(docto.getTpDocumentoServico().getValue(), docto
                    .getFilialByIdFilialOrigem().getSgFilial(), docto.getNrDoctoServico().toString()), dhOcorrencia, null, null, docto
                    .getTpDocumentoServico().getValue(), ocorrenciaEntrega.getIdOcorrenciaEntrega(), null, null, usuarioLogado);
        }

        // LMS-4332 - Contingência para
        // "Também há um problema na rotina, que grava valor zero no campo de dias reais de entrega".
        if (docto.getNrDiasRealEntrega() != null && docto.getNrDiasRealEntrega().intValue() < 1) {
            docto.setNrDiasRealEntrega(Short.valueOf("1"));
            getManifestoEntregaDocumentoDAO().getAdsmHibernateTemplate().saveOrUpdate(docto);
        }

		if(awb!=null){
            this.checkChangeAwbStatus(awb);
        }
		
		if(!BooleanUtils.isTrue(ocorrenciaEntrega.getBlOcasionadoMercurio())){
            Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(docto.getIdDoctoServico(), false);
            Long nrReentrega = conhecimento.getNrReentrega() != null ? conhecimento.getNrReentrega()+1 : 1;  
            conhecimento.setNrReentrega(nrReentrega);
            manifestoEntregaDocumentoService.saveOrUpdate(conhecimento);
        }
		
        transactionManager.commit(status);
    }   
    /**
     * <a href="http://lx-swjir01/jira/browse/LMSA-7476>LMSA-7476</a>
     */
    private void verificaObriagatoriedadeRg(Boolean obrigaRg, String tpFormaBaixa, String rg) {

        if (casoABaixaNaoOcorraPeloCelular(tpFormaBaixa)) {
            return;
        }

        if (BooleanUtils.isTrue(obrigaRg) && StringUtils.isBlank(rg)) {
            throw new BusinessException("LMS-09158");
        }
    }

	private boolean casoABaixaNaoOcorraPeloCelular(String tpFormaBaixa) {
		return !C.contains(tpFormaBaixa);
	}	    

	private void verificaObrigatoriedadeRgEdi(Boolean obrigaRgEdi, String tpFormaBaixa, String rg){
    	if (!E.equalsIgnoreCase(tpFormaBaixa)){
    		return;
    	}
    	
    	if (BooleanUtils.isTrue(obrigaRgEdi) && StringUtils.isBlank(rg)) {
            throw new BusinessException("LMS-09158");
        }
    }
    
    private void checkChangeAwbStatus(Awb awb) {
        List<Long> idsDoc = awbService.findDocumentosServico(awb.getIdAwb());
        for (Long idDoctoServico : idsDoc) {
            OcorrenciaEntrega oe = ocorrenciaEntregaService.findOcorrenciaEntregaByIdDoctoServico(idDoctoServico);
			if(oe == null || !oe.getCdOcorrenciaEntrega().equals(Short.valueOf((short)ConstantesEventosDocumentoServico.CD_OCORRENCIA_ENTREGA_REALIZADA_AEROPORTO))){
                return;
            }
        }

        awb.setTpLocalizacao(new DomainValue(TP_LOCALIZACAO_AWB_AGUARDANDO_ENTREGA));
        awbService.store(awb);

        List<AwbOcorrenciaLocalizacao> ocorrencias = awbOcorrenciaService.findAwbOcorrenciaLocalizacaoByIdAwbAndTpLocalizacao(awb.getIdAwb(), TP_LOCALIZACAO_AWB_AGUARDANDO_ENTREGA);
		if(ocorrencias != null && !ocorrencias.isEmpty()){
            return;
        }

        awbOcorrenciaService.store(awb, new DomainValue(TP_LOCALIZACAO_AWB_AGUARDANDO_ENTREGA), JTDateTimeUtils.getDataHoraAtual());
        trackingAwbService.storeTrackingAwb(awb, awb.getCiaFilialMercurio().getEmpresa().getIdEmpresa(), TP_LOCALIZACAO_AWB_AGUARDANDO_ENTREGA);
    }

    private boolean isClienteParametroObrigaRecebedor(Cliente cliente) {

		if(cliente != null && cliente.getIdCliente() != null){
            String idsClientesObrigaReceber = (String) configuracoesFacade.getValorParametro("NR_IDENTIFICACOES_OBRIGA_RECEBEDOR");

			if(StringUtils.isNotBlank(idsClientesObrigaReceber)){
                List<String> idClientes = new ArrayList<String>();
                idClientes = Arrays.asList(idsClientesObrigaReceber.split(";"));
                return idClientes.contains(cliente.getIdCliente().toString());

            }
        }
        return false;
    }

    /**
     * Consulta dados dos documentos associados ao ManifestoEntrega, utilizado
     * no cancelamento de manifestos de entrega
     *
     * @param idManifestoEntrega
     * @return
     */
    public List<Map<String, Object>> findDocumentosByManifestoEntrega(Long idManifestoEntrega) {
        return getManifestoEntregaDocumentoDAO().findDocumentosByManifestoEntrega(idManifestoEntrega);
    }

    /**
     * Altera a situacao do manifesto_entrega_documento
     *
     * @param idManifestoEntregaDocumento
     * @param tpSituacao
     */
	public void updateSituacaoManifestoEntregaDocumento(final Long idManifestoEntregaDocumento, final String tpSituacao){
        getManifestoEntregaDocumentoDAO().updateSituacaoManifestoEntregaDocumento(idManifestoEntregaDocumento,
                tpSituacao);
    }

	public List<Map<String, Object>> findManifestoEntregaByDoctoServico(Long idDoctoServico){
        return getManifestoEntregaDocumentoDAO().findManifestoEntregaByDoctoServico(idDoctoServico);
    }

	public List<Map<String, Object>> findManifestoEntregaByDoctoServicoAndTpStatusManifesto(Long idDoctoServico){
        List<String> tpStatusManifestoList = new ArrayList<String>();
        tpStatusManifestoList.add("FE");
        tpStatusManifestoList.add("CA");
        return getManifestoEntregaDocumentoDAO().findManifestoEntregaByDoctoServico(idDoctoServico, E, tpStatusManifestoList);
    }

    public List<ManifestoEntregaDocumento> findManifestoEntregaDocumentoByDoctoServico(Long idDoctoServico) {
        return getManifestoEntregaDocumentoDAO().findManifestoEntregaDocumentoByDoctoServico(idDoctoServico);
    }


    /**
     * Busca o último manifestoEntregaDocumento a partir do id do doctoServico;
     *
     * @param idDoctoServico
     * @return
     */
	public ManifestoEntregaDocumento findLastManifestoEntregaDocumentoByIdDoctoServico(Long idDoctoServico){
        return getManifestoEntregaDocumentoDAO().findLastManifestoEntregaDocumentoByIdDoctoServico(idDoctoServico);
    }

    /**
     * Retorna Manifesto Entrega Documento; Método utilizado pela Integração
     *
     * @author Andre Valadas
     *
     * @param idManifestoEntrega
     * @param idDoctoServico
     * @return
     */
    public ManifestoEntregaDocumento findManifestoEntregaDocumento(Long idManifestoEntrega, Long idDoctoServico) {
        return getManifestoEntregaDocumentoDAO().findManifestoEntregaDocumento(idManifestoEntrega, idDoctoServico);
    }

	public ReciboReembolso findReciboReembolsoAssociado(Long idDoctoServico){
        return getManifestoEntregaDocumentoDAO().findReciboReembolsoAssociado(idDoctoServico);
    }

	public YearMonthDay findDtEntregaDoctoServico(Long idDoctoServico){
        YearMonthDay dataEntrega = null;
        List<Map<String, Object>> lista = getManifestoEntregaDocumentoDAO().findDtEntregaDoctoServico(idDoctoServico);
		if(!lista.isEmpty()){
            Map<String, Object> map = lista.get(0);
			if(map.get(DH_OCORRENCIA)!= null)
				dataEntrega = ((DateTime)map.get(DH_OCORRENCIA)).toYearMonthDay();
            }
        return dataEntrega;
    }

    public DateTime findDhOcorrenciaByMeioTransporte(Long idMeioTransporte) {
        return getManifestoEntregaDocumentoDAO().findDhOcorrenciaByMeioTransporte(idMeioTransporte);
    }

	public void removeByIdManifestoEntrega(Long idManifestoEntrega){
        getManifestoEntregaDocumentoDAO().removeByIdManifestoEntrega(idManifestoEntrega);
    }

    /**
     * Verifica se o devedor do Documento de Serviço não possui indicador de
     * retenção do comprovante de entrega
     *
     * @param idDoctoServico
     * @return número de devedores encontrados.
     */
    public Integer getRowCountDevedorSemRetencaoComprovante(Long idDoctoServico) {
        return getManifestoEntregaDocumentoDAO().getRowCountDevedorSemRetencaoComprovante(idDoctoServico);
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência
     * dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private final ManifestoEntregaDocumentoDAO getManifestoEntregaDocumentoDAO() {
        return (ManifestoEntregaDocumentoDAO) getDao();
    }

    public List<ManifestoEntregaDocumento> findManifestoSemOcorrenciaEntregaByIdDoctoServico(Long idDoctoServico, String[] tpStatusManifesto) {
        return getManifestoEntregaDocumentoDAO().findManifestoSemOcorrenciaEntregaByIdDoctoServico(idDoctoServico, tpStatusManifesto);
    }

    /**
     * Retorna lista de manifestos de um Documento de Serviço
     *
     * @param idDoctoServico
     * @return List
     */
    public List<ManifestoEntregaDocumento> findByIdDoctoServico(Long idDoctoServico) {
        return getManifestoEntregaDocumentoDAO().findByIdDoctoServico(idDoctoServico);
    }

    public List<ManifestoEntregaDocumento> findManifestoByIdDoctoServico(Long idDoctoServico) {
        return getManifestoEntregaDocumentoDAO().findManifestoByIdDoctoServico(idDoctoServico);
    }

    public ManifestoEntregaDocumento findManifestoByIdDoctoServico(Long idDoctoServico, Long idControleCarga) {
        return getManifestoEntregaDocumentoDAO().findManifestoByIdDoctoServico(idDoctoServico, idControleCarga);
    }

    /**
	 * Retorna uma lista de manifestos de entrega de documento através do idDoctoServico e idManifesto
     *
     * @param idDoctoServico
     * @param idManifesto
     * @return List ManifestoEntregaDocumento
     */
    public List<ManifestoEntregaDocumento> findManifestoByDoctoServicoManifesto(Long idDoctoServico, Long idManifesto) {
        return getManifestoEntregaDocumentoDAO().findManifestoByDoctoServicoManifesto(idDoctoServico, idManifesto);
    }

    /**
	 * Método responsável por retornar a data de ocorrencia do manifesto de entrega 
	 * caso o documento de serviço tem uma ocorrência de entrega relacionada ao manifesto de entrega documento
     * @param idDoctoServico
     * @return DateTime a data de ocorrencia
     */
    public DateTime findMenorDhOcorrenciaByIdDoctoServico(Long idDoctoServico) {
        return getManifestoEntregaDocumentoDAO().findMenorDhOcorrenciaByIdDoctoServico(idDoctoServico);
    }

    /**
     * Retorna os manifestos que tenham ocorrências de não entrega relacionadas
     * ao documento de serviço e que a responsabilidade não seja da TNT
     *
     * @param idDoctoServico
     * @return
     */
    public List<ManifestoEntregaDocumento> findManifestoByIdDoctoServicoSemOcorrenciaEntregaENaoOcasionadoTnt(Long idDoctoServico) {
        return getManifestoEntregaDocumentoDAO().findManifestoByIdDoctoServicoSemOcorrenciaEntregaENaoOcasionadoTnt(idDoctoServico);
    }

    public List findNomeRecebedorByIdDoctoServico(Long idDoctoServico) {
        return getManifestoEntregaDocumentoDAO().findNomeRecebedorByIdDoctoServico(idDoctoServico);
    }

	
    /**
	 * Retorna o manifesto que tenham ocorrências de entrega nula,
	 * que o tipo de situação do documento seja Pendente de Baixa (PBAI),
	 * e que o documento de serviço e a data e a hora do fechamento for nula.
     *
     * @param doctoServico
	 * @return idDoctoServico (para comparações ao lado client), número do conhecimento, sigla da filial, id manifesto de entrega
     *
     */
    public Map findManifestoByIdDoctoServicoSemOcorrenciaEntregaEPendenteDeBaixa(Long doctoServico) {
        return getManifestoEntregaDocumentoDAO().findManifestoByIdDoctoServicoSemOcorrenciaEntregaEPendenteDeBaixa(doctoServico);
    }

    public List<Map<String, Object>> findEntregasRealizar(TypedFlatMap criteria) {
        return getManifestoEntregaDocumentoDAO().findEntregasRealizar(criteria);
    }

    public List<Map<String, Object>> findEntregasRealizadas(TypedFlatMap criteria) {
        return getManifestoEntregaDocumentoDAO().findEntregasRealizadas(criteria);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste
     * serviço.
	 * @param dao
     */
    public void setManifestoEntregaDocumentoDAO(ManifestoEntregaDocumentoDAO dao) {
		setDao( dao );
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }

    public void setDoctoServicoLookupFacade(DoctoServicoLookupFacade doctoServicoLookupFacade) {
        this.doctoServicoLookupFacade = doctoServicoLookupFacade;
    }

    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }

    public void setOcorrenciaEntregaService(OcorrenciaEntregaService ocorrenciaEntregaService) {
        this.ocorrenciaEntregaService = ocorrenciaEntregaService;
    }

    public void setVersaoDescritivoPceService(VersaoDescritivoPceService versaoDescritivoPceService) {
        this.versaoDescritivoPceService = versaoDescritivoPceService;
    }

    public void setIncluirEventosRastreabilidadeInternacionalService(
            IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
        this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
    }

    public void setPreManifestoDocumentoService(PreManifestoDocumentoService preManifestoDocumentoService) {
        this.preManifestoDocumentoService = preManifestoDocumentoService;
    }

    public void setAgendamentoEntregaService(AgendamentoEntregaService agendamentoEntregaService) {
        this.agendamentoEntregaService = agendamentoEntregaService;
    }

    public void setRegistroPriorizacaoDoctoService(RegistroPriorizacaoDoctoService registroPriorizacaoDoctoService) {
        this.registroPriorizacaoDoctoService = registroPriorizacaoDoctoService;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setRegistroPriorizacaoEmbarqService(RegistroPriorizacaoEmbarqService registroPriorizacaoEmbarqService) {
        this.registroPriorizacaoEmbarqService = registroPriorizacaoEmbarqService;
    }

    public VolBaixarEntregasService getVolBaixarEntregasService() {
        return volBaixarEntregasService;
    }

    public void setVolBaixarEntregasService(VolBaixarEntregasService volBaixarEntregasService) {
        this.volBaixarEntregasService = volBaixarEntregasService;
    }

    public void setManifestoEntregaVolumeDAO(ManifestoEntregaVolumeDAO manifestoEntregaVolumeDAO) {
        this.manifestoEntregaVolumeDAO = manifestoEntregaVolumeDAO;
    }

    public ManifestoEntregaVolumeDAO getManifestoEntregaVolumeDAO() {
        return manifestoEntregaVolumeDAO;
    }

    public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
        this.eventoVolumeService = eventoVolumeService;
    }

    public EventoVolumeService getEventoVolumeService() {
        return eventoVolumeService;
    }

    public void setManifestoEntregaVolumeService(ManifestoEntregaVolumeService manifestoEntregaVolumeService) {
        this.manifestoEntregaVolumeService = manifestoEntregaVolumeService;
    }

    public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
        this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
    }

    public ManifestoEntregaVolumeService getManifestoEntregaVolumeService() {
        return manifestoEntregaVolumeService;
    }

    public void setReciboReembolsoService(ReciboReembolsoService reciboReembolsoService) {
        this.reciboReembolsoService = reciboReembolsoService;
    }

    public ReciboReembolsoService getReciboReembolsoService() {
        return reciboReembolsoService;
    }

    public void setRegistrarBaixaEntregasService(RegistrarBaixaEntregasService registrarBaixaEntregasService) {
        this.registrarBaixaEntregasService = registrarBaixaEntregasService;
    }

    public RegistrarBaixaEntregasService getRegistrarBaixaEntregasService() {
        return registrarBaixaEntregasService;
    }

    public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
        this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
    }

    public void setMonitoramentoNotasFiscaisCCTService(MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService) {
        this.monitoramentoNotasFiscaisCCTService = monitoramentoNotasFiscaisCCTService;
    }

    public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
        this.volumeNotaFiscalService = volumeNotaFiscalService;
    }

    public void setCalcularDiasUteisBloqueioAgendamentoService(
            CalcularDiasUteisBloqueioAgendamentoService calcularDiasUteisBloqueioAgendamentoService) {
        this.calcularDiasUteisBloqueioAgendamentoService = calcularDiasUteisBloqueioAgendamentoService;
    }

    /**
	 * @param eventoDocumentoServicoService the eventoDocumentoServicoService to set
     */
    public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
        this.eventoDocumentoServicoService = eventoDocumentoServicoService;
    }

    public List<Map<String, Object>> findUltimasOcorrenciasByDoctoServico(Long idDoctoServico) {
        return getManifestoEntregaDocumentoDAO().findUltimasOcorrenciasByDoctoServico(idDoctoServico);
    }

    public Date findDataUltimaOcorrenciaManifesto(Long idDoctoServico) {
        if (idDoctoServico == null) {
            return null;
        }
        List<Map<String, Object>> ultimasOcorrencias = findUltimasOcorrenciasByDoctoServico(idDoctoServico);
        if (!ultimasOcorrencias.isEmpty()) {
            Map<String, Object> ultimaOcorrencia = ultimasOcorrencias.get(0);
			Long cdOcorrenciaEntrega = (Long)ultimaOcorrencia.get("CD_OCORRENCIA_ENTREGA");
            String[] filtroOcorrencias = parametroGeralService.findByNomeParametro(ID_OCORRENCIA_MANIFESTO, false).getDsConteudo().split(",");
            for (String ocorrencia : filtroOcorrencias) {
                if (ocorrencia.equals(cdOcorrenciaEntrega.toString())) {
					return (Date)ultimaOcorrencia.get("DH_OCORRENCIA");
                }
            }
        }
        return null;
    }

    public List<Map<String, Object>> findRecalcularDpeHorario() {
        return getManifestoEntregaDocumentoDAO().findRecalcularDpeHorario();
    }

    public List<Map<String, Object>> findRecalcularDpeDiario(int diaEmissaoDoctoServico) {
        return getManifestoEntregaDocumentoDAO().findRecalcularDpeDiario(diaEmissaoDoctoServico);
    }

    /**
     * @param transactionManager the transactionManager to set
     */
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setDomainValueService(DomainValueService domainValueService) {
        this.domainValueService = domainValueService;
    }

    public void setAwbService(AwbService awbService) {
        this.awbService = awbService;
    }

    public void setAwbOcorrenciaService(AwbOcorrenciaService awbOcorrenciaService) {
        this.awbOcorrenciaService = awbOcorrenciaService;
    }

    public TrackingAwbService getTrackingAwbService() {
        return trackingAwbService;
    }

    public void setTrackingAwbService(TrackingAwbService trackingAwbService) {
        this.trackingAwbService = trackingAwbService;
    }

    public ParametroGeralService getParametroGeralService() {
        return parametroGeralService;
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
        this.manifestoEntregaService = manifestoEntregaService;
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void setPaisService(PaisService paisService) {
        this.paisService = paisService;
    }

    public void setVolDadosSessaoService(VolDadosSessaoService volDadosSessaoService) {
        this.volDadosSessaoService = volDadosSessaoService;
    }

    public void setLocalizacaoMercadoriaService(LocalizacaoMercadoriaService localizacaoMercadoriaService) {
        this.localizacaoMercadoriaService = localizacaoMercadoriaService;
    }

    public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
        this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
    }
    
    public ComprovanteEntregaService getComprovanteEntregaService() {
        return comprovanteEntregaService;
    }

    public void setComprovanteEntregaService(ComprovanteEntregaService comprovanteEntregaService) {
        this.comprovanteEntregaService = comprovanteEntregaService;
    }

    public void setConhecimentoService(ConhecimentoService conhecimentoService) {
        this.conhecimentoService = conhecimentoService;
    }
    
    public NotaFiscalConhecimentoService getNotaFiscalConhecimentoService() {
        return notaFiscalConhecimentoService;
    }

    
    public void executeReentrega(Long idManifestoEntregaDocumento) {
        ManifestoEntregaDocumento manifesto = limpaInfosParaReentrega(idManifestoEntregaDocumento);
        OcorrenciaEntrega ocorrencia = manifesto.getOcorrenciaEntrega();
        if (ocorrencia == null || !ocorrencia.getCdOcorrenciaEntrega().equals(Short.valueOf("1"))){
        	throw new BusinessException("LMS-12010");
        }

        this.getManifestoEntregaDocumentoDAO().store(manifesto, true);
        if(manifesto.getDoctoServico().getBlBloqueado()) {
            ParametroGeral cdOcorrencia = parametroGeralService.findByNomeParametro("COD_NATURA_LIBERACAO_REENTREGA");
            this.doctoServicoService.generateLiberacaoDocumentoServico(
                manifesto.getDoctoServico().getIdDoctoServico(), JTDateTimeUtils.getDataHoraAtual(), Short.valueOf(cdOcorrencia.getDsConteudo()));
        }
    }
    
    /**
     * Realiza a busca no manifesto entrega documento pelo id clicado na tela de recusas
     * e realiza a limpeza das informação para permitir uma reentrega
     * @param idManifestoEntregaDocumento
     * @return 
     */
    private ManifestoEntregaDocumento limpaInfosParaReentrega(Long idManifestoEntregaDocumento){
        ManifestoEntregaDocumento manifesto = this.findById(idManifestoEntregaDocumento);
        manifesto.setDhOcorrencia(null);
        manifesto.setOcorrenciaEntrega(null);
        manifesto.setTpSituacaoDocumento(new DomainValue("PBAI"));
        manifesto.setTpFormaBaixa(null);
        manifesto.setDhBaixa(null);
        manifesto.setObManifestoEntregaDocumento(null);
        return manifesto;
    }
    
    
    public ManifestoEntregaDocumento findByIdDoctoServicoManifesto(Long idDoctoServico, Long idManifestoEntrega){
        return getManifestoEntregaDocumentoDAO().findByIdDoctoServicoManifesto(idDoctoServico, idManifestoEntrega);
    }
    
    public Long findIdUltimoManifestoEntregaDocByCdOcorrencia(final Long idDoctoServico, final Short cdOcorrenciaEntrega) {
    	return getManifestoEntregaDocumentoDAO().findIdUltimoManifestoEntregaDocByCdOcorrencia(idDoctoServico, cdOcorrenciaEntrega);
    }

	public List findDadosNotasEntregaParcial(Long idManifestoEntrega, Long idDoctoServico) {
		return getManifestoEntregaDocumentoDAO().findDadosNotasEntregaParcial(idManifestoEntrega, idDoctoServico);
	}

    public boolean isFormaBaixaByIdDoctoServico(Long idDoctoServico){
        return getManifestoEntregaDocumentoDAO().isFormaBaixaByIdDoctoServico(idDoctoServico);
    }
}
