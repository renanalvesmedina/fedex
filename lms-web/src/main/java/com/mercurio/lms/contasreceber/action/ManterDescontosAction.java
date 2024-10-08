package com.mercurio.lms.contasreceber.action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailKey;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DescontoAnexo;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.MotivoDesconto;
import com.mercurio.lms.contasreceber.model.param.DevedorDocServFatLookupParam;
import com.mercurio.lms.contasreceber.model.service.DescontoAnexoService;
import com.mercurio.lms.contasreceber.model.service.DescontoService;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatLookUpService;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.contasreceber.model.service.MotivoDescontoService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.questionamentoFaturas.model.service.QuestionamentoFaturasService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;



/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.manterDescontosAction"
 */
public class ManterDescontosAction extends MasterDetailAction {

    private Logger log = LogManager.getLogger(this.getClass());
    private MotivoDescontoService motivoDescontoService;
    private ClienteService clienteService;
    private FilialService filialService;
    private DomainValueService domainValueService;
    private DoctoServicoService doctoServicoService;
    private MoedaService moedaService;
    private DevedorDocServFatService devedorDocServFatService;
    private DevedorDocServFatLookUpService devedorDocServFatLookUpService;
    private DescontoAnexoService descontoAnexoService;
    private QuestionamentoFaturasService questionamentoFaturasService;
    private FaturaService faturaService;
    private ConfiguracoesFacade configuracoesFacade;
    
    private static String DESCONTO_ANEXO_CONFIG_ALIAS = "descontoAnexo";
    /**
     * M�todo de pesquisa da grid
     * @param tfm Crit�rios de pesquisa
     * @return Dados da listagem e de pagina��o
     */
    public ResultSetPage findPaginated(Map m) {
    	removeMasterFromSession();
    	TypedFlatMap tfm = new TypedFlatMap(m);
        
        ResultSetPage rs = this.getDescontoService().findPaginated(tfm);
        
        List dados = rs.getList();
        List substituta = new ArrayList();        
        
        Map retorno  = null;
        
        for (Iterator iter = dados.iterator(); iter.hasNext();) {
            
            Object[] elementos = (Object[]) iter.next();
            
            retorno  = new HashMap();
            
            retorno.put("idDesconto", (Long) elementos[0]);
            retorno.put("clienteResponsavel",FormatUtils.formatIdentificacao((String)elementos[2],(String)elementos[1]) + " - " + (String) elementos[3]);
            
            retorno.put("tpDoctoServico",(String) elementos[9]);            
            retorno.put("filialNumeroDocumento", FormatUtils.formatAndConcatBySgFilialAndNrConhecimento((String)elementos[5],Long.valueOf((String)elementos[6])));
            
            retorno.put("tpDocumentoServico", (String) elementos[9]);
            retorno.put("sgFilialOrigem", (String)elementos[5]);
            retorno.put("nrDocumentoServico", FormatUtils.formataNrDocumento((String)elementos[6],(String) elementos[4]));
            
            retorno.put("motivo", (String) elementos[7]);
            retorno.put("tpSituacaoAprovacao", (String) elementos[8]);            
            
            substituta.add(retorno);
            
        }
        
        if( substituta != null && !substituta.isEmpty() ){
            rs.setList(substituta);
        }
        
        return rs;
    }
    
    public Object findAnexos(MasterDetailKey key) {
    	DescontoAnexo descontoAnexo = (DescontoAnexo) findItemById(key, DESCONTO_ANEXO_CONFIG_ALIAS);
    	TypedFlatMap mapItemMda = new TypedFlatMap();   		
    	
    	mapItemMda.put("idDescontoAnexo",descontoAnexo.getIdDescontoAnexo()); 
		mapItemMda.put("dsAnexo", descontoAnexo.getDsAnexo());
		mapItemMda.put("dcArquivo", Base64Util.encode(descontoAnexo.getDcArquivo()) );
		mapItemMda.put("blEnvAnexoQuestFat", descontoAnexo.getBlEnvAnexoQuestFat());
		
		return mapItemMda;   	
    }    	
    
    /**
     * M�todo que conta quantos dados ser�o apresentados na listagem
     * @param tfm Crit�rios de pesquisa
     * @return Inteiro informando a quantidade de registros exibidos na grid
     */
    public Integer getRowCount(Map m) {
    	TypedFlatMap tfm = new TypedFlatMap(m);
        return this.getDescontoService().getRowCount(tfm);
    }
    
    /**
     * M�todo que popula a combo de tipos de documento apenas com CTR, CRT, NFS.
     * 
     * @param criteria
     * @return List
     */
    public List findTipoDocumentoServico(Map criteria) {
		String tpDocFat = (String)configuracoesFacade
				.getValorParametro("TP_DOCTO_FAT3");

		String[] dm = tpDocFat.split(";");
        List dominiosValidos = Arrays.asList(dm);
        List retorno = this.domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
        return retorno;
    }
    
    /**
     * Valida o valor de desconto que n�o pode ser maior que o valor do documento e calcula o 
     * percentual do desconto
     * @param tfm Valor do desconto (vlDesconto) e o valor do documento (vlDocumento)
     * @return Valor o percentual de desconto
     */
    public TypedFlatMap validateValores(TypedFlatMap tfm){
        
        BigDecimal vlDocumento = tfm.getBigDecimal("vlDocumento");
        BigDecimal vlDesconto  = tfm.getBigDecimal("vlDesconto");
        
        BigDecimal percentual = getDescontoService().calculaPorcentagemDesconto(vlDesconto, vlDocumento);
        
        TypedFlatMap tfmRetorno = new TypedFlatMap();
        
        tfmRetorno.put("percentualDesconto",percentual.setScale(3,BigDecimal.ROUND_DOWN));
        
        return tfmRetorno; 
        
    }
    
    /**
     * Busca a lista de motivos de desconto para a combo de motivos desconto
     * @param map Crit�rios da pesquisa
     * @return Lista de Motivos do Desconto
     */
    public List findComboMotivoDesconto(TypedFlatMap criteria){
    	 List<MotivoDesconto> motivos = this.motivoDescontoService
    	 		.findMotivoDescontoByTpSituacaoAndIdDesconto(
    	 				criteria.getString("tpSituacao"),       
    	 				criteria.getLong("desconto.idDesconto"));  
     	
         for (MotivoDesconto m : motivos) { 
         	m.setDsMotivoDesconto(new VarcharI18n(m.getCdMotivoDesconto() + " - " + m.getDsMotivoDesconto().getValue()));
         }
         
     	return motivos;
    }    
    
    /**
     * Busca a lista de clientes para a lookup de Clientes Respons�veis
     * @param map Crit�rios de pesquisa
     * @return Lista de clientes
     */
    public List findLookupClienteResponsavel(Map map){
        return this.clienteService.findLookup(map);
    }
    
    /**
     * Remove um registro que esteja sendo detalhando no Manter Descontos
     * @param tfm Identificador do Desconto 
     */
    public void removeById(TypedFlatMap tfm) {
        getDescontoService().removeById(tfm.getLong("idDesconto"));
        
        newMaster();
                    }
                
    /**
     * Remove os registros selecionados na grid
     * @param ids Lista de ids dos registros selecionados
     *
     */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        getDescontoService().removeByIds(ids);
            }            
            
    /**
     * Busca um registro de Desconto quando este � selecionado na lista da grid
     * @param id Identificador do registro de Desconto
     * @return Desconto encontrado na pesquisa
     */
    public TypedFlatMap findById(Long id) {
    	TypedFlatMap retorno = new TypedFlatMap();
    	
    	Desconto desconto = (Desconto)getDescontoService().findById(id);
    	
    	retorno.put("idDesconto", desconto.getIdDesconto());
    	retorno.put("devedorDocServFat.doctoServico.tpDocumentoServico", desconto.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue());
    	retorno.put("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial", desconto.getDevedorDocServFat().getDoctoServico().getFilialByIdFilialOrigem().getIdFilial());
    	retorno.put("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial", desconto.getDevedorDocServFat().getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
    	retorno.put("devedorDocServFat.idDevedorDocServFat", desconto.getDevedorDocServFat().getIdDevedorDocServFat());
    	retorno.put("devedorDocServFat.doctoServico.nrDoctoServico", desconto.getNrDocumento());
    	retorno.put("nrDocumento", desconto.getNrDocumento());
    	retorno.put("devedorDocServFat.filial.idFilial", desconto.getDevedorDocServFat().getFilial().getIdFilial());
    	retorno.put("devedorDocServFat.filial.sgFilial", desconto.getDevedorDocServFat().getFilial().getSgFilial());
    	retorno.put("devedorDocServFat.filial.pessoa.nmFantasia", desconto.getDevedorDocServFat().getFilial().getPessoa().getNmFantasia());
    	retorno.put("motivoDesconto.idMotivoDesconto", desconto.getMotivoDesconto().getIdMotivoDesconto());
    	retorno.put("devedorDocServFat.cliente.idCliente", desconto.getDevedorDocServFat().getCliente().getIdCliente());
    	retorno.put("devedorDocServFat.cliente.pessoa.nrIdentificacao", desconto.getDevedorDocServFat().getCliente().getPessoa().getNrIdentificacaoFormatado());
    	retorno.put("devedorDocServFat.cliente.pessoa.nmPessoa", desconto.getDevedorDocServFat().getCliente().getPessoa().getNmPessoa());
    	retorno.put("tpSituacaoAprovacao", desconto.getTpSituacaoAprovacao().getValue());
    	retorno.put("vlDesconto", desconto.getVlDesconto());
    	retorno.put("devedorDocServFat.doctoServico.moeda.idMoeda", desconto.getDevedorDocServFat().getDoctoServico().getMoeda().getIdMoeda());
    	retorno.put("devedorDocServFat.doctoServico.moeda.siglaSimbolo", desconto.getDevedorDocServFat().getDoctoServico().getMoeda().getSiglaSimbolo());
    	retorno.put("devedorDocServFat.vlDevido", desconto.getDevedorDocServFat().getVlDevido());
    	retorno.put("percentualDesconto", desconto.getPercentualDesconto());
    	retorno.put("obDesconto", desconto.getObDesconto());
    	retorno.put("obAcaoCorretiva", desconto.getObAcaoCorretiva());
    	retorno.put("setorCausadorAbatimento", desconto.getTpSetorCausadorAbatimento().getValue());
    	
    	if (desconto.getDevedorDocServFat().getDoctoServico().getServico() != null){
	    	retorno.put("devedorDocServFat.doctoServico.servico.idServico", desconto.getDevedorDocServFat().getDoctoServico().getServico().getIdServico());
	    	retorno.put("devedorDocServFat.doctoServico.servico.tpModal", desconto.getDevedorDocServFat().getDoctoServico().getServico().getTpModal().getValue());
	    	retorno.put("devedorDocServFat.doctoServico.servico.tpAbrangencia", desconto.getDevedorDocServFat().getDoctoServico().getServico().getTpAbrangencia().getValue());
    	}
    	
    	retorno.put("devedorDocServFat.doctoServico.idDoctoServico", desconto.getDevedorDocServFat().getDoctoServico().getIdDoctoServico());
    	
    	retorno.put("idPendencia", desconto.getIdPendencia());
    	
    	//Se a filial de cobran�a � a mesma que a filial da sess�o
    	if (desconto.getDevedorDocServFat().getFilial().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())){
    		retorno.put("blDesabilitar", false);
    	} else {
    		retorno.put("blDesabilitar", true);
    	}
    	
    	putMasterInSession(desconto);
    	
    	
    	return retorno;
    }
    
    /**
     * Salva um registro criado pelo detalhamento do Manter Desconto
     * @param tfm Dados do Desconto
     * @return Inst�ncia do Desconto salvo
     */
    public Serializable store(TypedFlatMap tfm) {
        
    	
        MasterEntry master = getMasterFromSession(tfm.getLong("idDesconto"), true);
        ItemList listDescontos = getItemsFromSession(master, DESCONTO_ANEXO_CONFIG_ALIAS);
        
        Long idMotivoDesconto      = tfm.getLong("motivoDesconto.idMotivoDesconto");
        DomainValue tpSituacaoAprovacao = tfm.getDomainValue("tpSituacaoAprovacao");
        BigDecimal vlDesconto      = tfm.getBigDecimal("vlDesconto");
        BigDecimal vlDocumento     = tfm.getBigDecimal("devedorDocServFat.vlDevido");
        
        DomainValue tpDocumentoServico = this.domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_SERVICO",tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico"));
        
        MotivoDesconto motivoDesconto = motivoDescontoService.findById(idMotivoDesconto);
        
        DevedorDocServFat devedorDocServFat = null;
        NotaFiscalServico nfs = null;
        Conhecimento conhecimento = null;         
        Moeda moeda = null;        
        Servico servico = null;
        Filial filialDevedor = null;
        
        Filial filialOrigem = null;
        Long idFilialOrigem = null;
        
        idFilialOrigem = tfm.getLong("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial");
        
        if( idFilialOrigem == null ){
        	idFilialOrigem = tfm.getLong("devedorDocServFat.filialByIdFilialOrigem.idFilial");
        }
        
        filialOrigem = filialService.findById(idFilialOrigem);         
        
        if( tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("NFS") ||
        	tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("NSE")){
            
        	nfs = new NotaFiscalServico();
        	
        	Long id = tfm.getLong("devedorDocServFat.doctoServico.idDoctoServico");
        	
            nfs.setIdDoctoServico(id);
            
        }
        
        if( tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("CTR") || 
        	tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("NFT") ||
        	tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("CTE") ||
        	tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("NTE")){
            
        	conhecimento = new Conhecimento();
        	
        	Long id = tfm.getLong("devedorDocServFat.doctoServico.idDoctoServico");
            
            conhecimento.setIdDoctoServico(id);
            
        }
        
        servico = new Servico();
        servico.setIdServico(tfm.getLong("devedorDocServFat.doctoServico.servico.idServico"));
        servico.setTpModal(domainValueService.findDomainValueByValue("DM_MODAL",tfm.getString("devedorDocServFat.doctoServico.servico.tpModal")));
        servico.setTpAbrangencia(domainValueService.findDomainValueByValue("DM_ABRANGENCIA",tfm.getString("devedorDocServFat.doctoServico.servico.tpAbrangencia")));
        
        moeda = moedaService.findById(tfm.getLong("devedorDocServFat.doctoServico.moeda.idMoeda"));       
                
        if( tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("NFS") ||
        	tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("NSE")){
        	nfs.setFilialByIdFilialOrigem(filialOrigem);
            nfs.setTpDocumentoServico(tpDocumentoServico);
            nfs.setMoeda(moeda);
            nfs.setServico(servico);
        }
        
        if( tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("CTR") || 
        	tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("NFT") ||
        	tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("NTE") ||
        	tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("CTE")){
        	conhecimento.setFilialByIdFilialOrigem(filialOrigem);
            conhecimento.setTpDocumentoServico(tpDocumentoServico);
            conhecimento.setMoeda(moeda);
            conhecimento.setServico(servico);
        }
        
        Cliente cliente = new Cliente();        
        cliente.setIdCliente(tfm.getLong("devedorDocServFat.cliente.idCliente"));
        
        filialDevedor = new Filial();
        filialDevedor.setIdFilial(tfm.getLong("devedorDocServFat.filial.idFilial"));
        
        devedorDocServFat = new DevedorDocServFat();
        
        devedorDocServFat.setIdDevedorDocServFat(tfm.getLong("devedorDocServFat.idDevedorDocServFat"));
        devedorDocServFat.setVlDevido(vlDocumento);
        devedorDocServFat.setCliente(cliente);        
        devedorDocServFat.setFilial(filialDevedor);
        
        
        if( tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("NFS") ||
        	tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("NSE")){
        	devedorDocServFat.setDoctoServico(nfs);
        }
        
        if( tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("CTR") ||
        	tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("NFT") ||
        	tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("NTE") ||
        	tfm.getString("devedorDocServFat.doctoServico.tpDocumentoServico").equals("CTE")){
        	devedorDocServFat.setDoctoServico(conhecimento);
        }
        
        Desconto desconto = (Desconto) master.getMaster();
        
        if( tfm.getLong("idDesconto") != null ){
            desconto.setIdDesconto(tfm.getLong("idDesconto"));
        }
        
        BigDecimal percentualDesconto = tfm.getBigDecimal("percentualDesconto");
        
        if( percentualDesconto == null ){            
            percentualDesconto = getDescontoService().calculaPorcentagemDesconto(vlDesconto, vlDocumento);                
        }
        
        
        desconto.setDevedorDocServFat(devedorDocServFat);
        desconto.setMotivoDesconto(motivoDesconto);
        desconto.setTpSituacaoAprovacao(tpSituacaoAprovacao);
        desconto.setVlDesconto(vlDesconto);
        desconto.setPercentualDesconto(percentualDesconto);
        desconto.setNrDocumento(tfm.getString("nrDocumento"));
        desconto.setIdPendencia(tfm.getLong("idPendencia"));
        
        if( tfm.containsKey("obDesconto") && StringUtils.isNotBlank(tfm.getString("obDesconto")) ){
            desconto.setObDesconto(tfm.getString("obDesconto"));
        }        
        
        desconto.setObAcaoCorretiva(tfm.getString("obAcaoCorretiva"));
        
        desconto.setTpSetorCausadorAbatimento( tfm.getDomainValue("setorCausadorAbatimento") );
  
        getDescontoService().store(desconto, listDescontos);
        

        listDescontos.resetItemsState();
        
        return mountMap(desconto);
    }
    

    /**
     * Busca os dados do respons�vel e o valor do documento
     * @param tfm Crit�rios de pesquisa
     * @return Desconto com os dados necess�rios aninhados
     */
    public Serializable findDadosClienteEValores(TypedFlatMap tfm){
    	TypedFlatMap mapRetorno = new TypedFlatMap();
    	
    	DevedorDocServFat devedorDocServFat = devedorDocServFatService.findByIdWithDocumento(tfm.getLong("idDevedorDocServFat"));
    	
    	mapRetorno.put("devedorDocServFat.filial.idFilial", devedorDocServFat.getFilial().getIdFilial());
    	mapRetorno.put("devedorDocServFat.filial.sgFilial", devedorDocServFat.getFilial().getSgFilial());
    	mapRetorno.put("devedorDocServFat.filial.pessoa.nmFantasia", devedorDocServFat.getFilial().getPessoa().getNmFantasia());
    	mapRetorno.put("devedorDocServFat.cliente.idCliente", devedorDocServFat.getCliente().getIdCliente());
    	mapRetorno.put("devedorDocServFat.cliente.pessoa.nrIdentificacao", devedorDocServFat.getCliente().getPessoa().getNrIdentificacaoFormatado());
    	mapRetorno.put("devedorDocServFat.cliente.pessoa.nmPessoa", devedorDocServFat.getCliente().getPessoa().getNmPessoa());
    	mapRetorno.put("devedorDocServFat.doctoServico.moeda.idMoeda", devedorDocServFat.getDoctoServico().getMoeda().getIdMoeda());
    	mapRetorno.put("devedorDocServFat.doctoServico.moeda.siglaSimbolo", devedorDocServFat.getDoctoServico().getMoeda().getSiglaSimbolo());
    	mapRetorno.put("devedorDocServFat.vlDevido", devedorDocServFat.getVlDevido());
    	
    	if (devedorDocServFat.getDoctoServico().getServico() != null) {
	    	mapRetorno.put("devedorDocServFat.doctoServico.servico.idServico", devedorDocServFat.getDoctoServico().getServico().getIdServico());
	    	mapRetorno.put("devedorDocServFat.doctoServico.servico.tpModal", devedorDocServFat.getDoctoServico().getServico().getTpModal().getValue());
	    	mapRetorno.put("devedorDocServFat.doctoServico.servico.tpAbrangencia", devedorDocServFat.getDoctoServico().getServico().getTpAbrangencia().getValue());
    	}
    	
    	mapRetorno.put("devedorDocServFat.doctoServico.idDoctoServico", devedorDocServFat.getDoctoServico().getIdDoctoServico());

        return mapRetorno;
    }
    
    public List findLookupFilial(TypedFlatMap map){
		return filialService.findLookup(map);
	}
    
    /**
	 * Busca a filial do usu�rio logado
	 * @param tfm Neste caso sem nenhum crit�rio
	 * @return Filial do usu�rio logado
	 */
	public TypedFlatMap findFilialUsuario(TypedFlatMap tfm){
		
		TypedFlatMap map = new TypedFlatMap();
		
		Filial filial = SessionUtils.getFilialSessao();
		
		map.put("filialCobranca.idFilial",filial.getIdFilial());
		map.put("filialCobranca.sgFilial",filial.getSgFilial());
		map.put("filialCobranca.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
		
		return map;
	}
    
    /**
	 * Retorna a lista de devedores a partir do tipo de documento informado. 
	 * O parametro tpDocumento � obrigat�rio!
	 * 
	 * @author Jos� Rodrigo Moraes
	 * @since  26/06/2006
	 * 
	 * @param Long nrDocumento N�mero do documento informado
	 * @param String tpDocumento Tipo do Documento CTR - CRT - NFS e NFT
	 * @param Long idFilial Identificador da filial de origem associada ao documento de servi�o
	 * @return Lista de dados para a lookup de documento de servi�o do manter descontos
	 * 
	 */	
    public List findDevedorServDocFat(TypedFlatMap map){
	
    	Long idFilial = map.getLong("doctoServico.filialByIdFilialOrigem.idFilial");	
		Long nrDocumento = map.getLong("doctoServico.nrDoctoServico");
		String tpDocumentoServico = map.getString("doctoServico.tpDocumentoServico");
		
		DevedorDocServFatLookupParam devedorDocServFatLookupParam = new DevedorDocServFatLookupParam();
		
		devedorDocServFatLookupParam.setIdFilial(idFilial);
		devedorDocServFatLookupParam.setNrDocumentoServico(nrDocumento);
		devedorDocServFatLookupParam.setTpDocumentoServico(tpDocumentoServico);
		devedorDocServFatLookupParam.setTpSituacaoDevedorDocServFatValido(map.getInteger("tpSituacaoDevedorDocServFatValido"));
		
		return AliasToNestedMapResultTransformer.getInstance().transformListResult(devedorDocServFatLookUpService.findDevedorDocServFat(devedorDocServFatLookupParam, "LMS-36006"));
	}
    
	/**
	 * @author Jos� Rodrigo Moraes
	 * @since  01/06/2006
     * 
     * Verifica se o documento de servi�o informado est� Emitido ou Bloqueado
     * @param tfm idDoctoServico Identificador do Documento de servico e tpDocumentoServico Tipo do documento de servi�o
     * @return <code>true</code> se o documento de servi�o est� Emitido ou Bloqueado, caso contr�rio levanta a BusinessException LMS-36142
     * @exception com.mercurio.adsm.framework.BusinessException LMS-36142
     */
    public TypedFlatMap validateDoctoEmitidoBloqueado(TypedFlatMap tfm){
    	
    	Long idDoctoServico = tfm.getLong("idDoctoServico");    	
    	String tpDocumentoServico = tfm.getString("tpDocumentoServico.value");
    	
    	Boolean retorno = getDescontoService().validateDoctoEmitidoBloqueado(idDoctoServico, tpDocumentoServico);
    	
    	TypedFlatMap mapRetorno = new TypedFlatMap();
    	mapRetorno.put("_value", retorno);
    	
    	return mapRetorno;
    }
    
    private TypedFlatMap mountMap(Desconto desconto){
    	TypedFlatMap retorno = new TypedFlatMap();
    	
    	retorno.put("idDesconto", desconto.getIdDesconto());
    	retorno.put("devedorDocServFat.doctoServico.tpDocumentoServico", desconto.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue());
    	retorno.put("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial", desconto.getDevedorDocServFat().getDoctoServico().getFilialByIdFilialOrigem().getIdFilial());
    	retorno.put("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial", desconto.getDevedorDocServFat().getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
    	retorno.put("devedorDocServFat.idDevedorDocServFat", desconto.getDevedorDocServFat().getIdDevedorDocServFat());
    	retorno.put("devedorDocServFat.doctoServico.nrDoctoServico", FormatUtils.formataNrDocumento(desconto.getDevedorDocServFat().getDoctoServico().getNrDoctoServico().toString(), desconto.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue()));
    	retorno.put("nrDocumento", FormatUtils.formataNrDocumento(desconto.getDevedorDocServFat().getDoctoServico().getNrDoctoServico().toString(), desconto.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue()));
    	retorno.put("tpDocumentoHidden", desconto.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue());
    	retorno.put("tpDocumentoServicoHidden", desconto.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue());
    	retorno.put("devedorDocServFat.filial.idFilial", desconto.getDevedorDocServFat().getFilial().getIdFilial());
    	retorno.put("devedorDocServFat.filial.sgFilial", desconto.getDevedorDocServFat().getFilial().getSgFilial());
    	retorno.put("devedorDocServFat.filial.pessoa.nmFantasia", desconto.getDevedorDocServFat().getFilial().getPessoa().getNmFantasia());
    	retorno.put("motivoDesconto.idMotivoDesconto", desconto.getMotivoDesconto().getIdMotivoDesconto());
    	retorno.put("devedorDocServFat.cliente.idCliente", desconto.getDevedorDocServFat().getCliente().getIdCliente());
    	retorno.put("devedorDocServFat.cliente.pessoa.nrIdentificacao", desconto.getDevedorDocServFat().getCliente().getPessoa().getNrIdentificacaoFormatado());
    	retorno.put("devedorDocServFat.cliente.pessoa.nmPessoa", desconto.getDevedorDocServFat().getCliente().getPessoa().getNmPessoa());
    	retorno.put("tpSituacaoAprovacao", desconto.getTpSituacaoAprovacao().getValue());
    	retorno.put("vlDesconto", desconto.getVlDesconto());
    	retorno.put("devedorDocServFat.doctoServico.moeda.idMoeda", desconto.getDevedorDocServFat().getDoctoServico().getMoeda().getIdMoeda());
    	retorno.put("devedorDocServFat.doctoServico.moeda.siglaSimbolo", desconto.getDevedorDocServFat().getDoctoServico().getMoeda().getSiglaSimbolo());
    	retorno.put("devedorDocServFat.vlDevido", desconto.getDevedorDocServFat().getVlDevido());
    	retorno.put("percentualDesconto", desconto.getPercentualDesconto());
    	retorno.put("obDesconto", desconto.getObDesconto());
    	
    	if (desconto.getDevedorDocServFat().getDoctoServico().getServico() != null){
	    	retorno.put("devedorDocServFat.doctoServico.servico.idServico", desconto.getDevedorDocServFat().getDoctoServico().getServico().getIdServico());
	    	
	    	if (desconto.getDevedorDocServFat().getDoctoServico().getServico().getTpModal() != null){
	    		retorno.put("devedorDocServFat.doctoServico.servico.tpModal", desconto.getDevedorDocServFat().getDoctoServico().getServico().getTpModal().getValue());
	    	}
	    	
	    	if (desconto.getDevedorDocServFat().getDoctoServico().getServico().getTpAbrangencia() != null){
	    		retorno.put("devedorDocServFat.doctoServico.servico.tpAbrangencia", desconto.getDevedorDocServFat().getDoctoServico().getServico().getTpAbrangencia().getValue());
	    	}
    	}
    	
    	retorno.put("devedorDocServFat.doctoServico.idDoctoServico", desconto.getDevedorDocServFat().getDoctoServico().getIdDoctoServico());
    	
  		retorno.put("idPendencia", desconto.getIdPendencia());

    	return retorno;
    }
    
    public void setDescontoService(DescontoService descontoService){
    	setMasterService(descontoService);
    }
    
    /**
     * Retorna a service padr�o desta tela
     * @return Inst�ncia da service Padr�o
     */
    private DescontoService getDescontoService(){
        return (DescontoService) getMasterService();
    }

    public void setMotivoDescontoService(MotivoDescontoService motivoDescontoService) {
        this.motivoDescontoService = motivoDescontoService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    public void setDomainValueService(DomainValueService domainValueService) {
        this.domainValueService = domainValueService;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }
    
    public void setMoedaService(MoedaService moedaService) {
        this.moedaService = moedaService;
    }

	public void setDevedorDocServFatLookUpService(
			DevedorDocServFatLookUpService devedorDocServFatLookUpService) {
		this.devedorDocServFatLookUpService = devedorDocServFatLookUpService;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}
       
	
	public Integer getRowCountDescontoAnexo(TypedFlatMap criteria) {
		getMasterFromSession(criteria.getLong("idDesconto"), true);
		return getRowCountItemList(criteria, DESCONTO_ANEXO_CONFIG_ALIAS);
	}
	
	public ResultSetPage findPaginatedDescontoAnexo(TypedFlatMap criteria) {
		ResultSetPage rspItemAnexo = findPaginatedItemList(criteria, DESCONTO_ANEXO_CONFIG_ALIAS);
		List lista = new ArrayList();
		for (Object descontoAnexoObject: rspItemAnexo.getList()) {
			DescontoAnexo descontoAnexo = (DescontoAnexo) descontoAnexoObject;

			Map map = new HashMap();
			map.put("idDescontoAnexo" , descontoAnexo.getIdDescontoAnexo());
			map.put("descricao" , descontoAnexo.getDsAnexo());
			map.put("dhinclusao" , descontoAnexo.getDhCriacao());
			map.put("nmusuario" , descontoAnexo.getUsuario().getNmUsuario() );

			try {
				// pega o nome do arquivo
				ByteArrayInputStream ba = new ByteArrayInputStream(  descontoAnexo.getDcArquivo() );
				BufferedReader buff = new BufferedReader(new InputStreamReader(ba));
				
				String ln = buff.readLine();
				
				map.put("nmarquivo", ln.substring(0,1024) );
				
			} catch (Exception e) {
				// nao encontrou nome ???
				map.put("nmarquivo", "");
			}
			
			lista.add(map);
		} 
		
		rspItemAnexo.setList(lista);
    	return rspItemAnexo;
	}
	
	
	@Override
	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {

		/**
		 * Declaracao da classe pai
		 */		
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(Desconto.class, true);		
				
		/**
		 * Esta classe e reponsavel por ordenar a List dos filhos que estao
		 * em memoria de acordo com as regras de negocio
		 */
    	Comparator descComparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
        		return ((DescontoAnexo)obj1).getDhCriacao().compareTo( (( DescontoAnexo )obj2).getDhCriacao() ); 		
			}
    	};		
		
    	
    	/**
    	 * Esta instancia � responsavel por carregar os 
    	 * items filhos na sess�o a partir do banco de dados.
    	 */
    	ItemListConfig itemInit = new ItemListConfig() {
    		
    		@Override
    		public void setMasterOnItem(Object master, Object itemBean) {
    			((DescontoAnexo) itemBean).setDesconto((Desconto)master);
    		}
 
    		/**
    		 * Find paginated do filho
    		 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
    		 * Apos a primeira vez ela e carregada da memoria
    		 * 
    		 * @param masterId id do pai
    		 * @param parameters todos os parametros vindo da tela pai
    		 */    		
			public List initialize(Long masterId, Map parameters) {	
				return getDescontoAnexoService().findDescontoAnexosByIdDesconto(masterId);
			}

			/**
			 * Busca rowCount da grid da tela filha
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
    		 * Apos a primeira vez ela e carregada da memoria
			 * 
			 * @param masterId id do pai
			 * @param parameters todos os parametros vindo da tela pai
			 */			
			public Integer getRowCount(Long masterId, Map parameters) {
				return Integer.valueOf(getDescontoAnexoService().findDescontoAnexosByIdDesconto(masterId).size());
			}
			
			/**
			 * Chama esta funcao depois de editar um item da grid filho
			 * E retira atributos desnecessarios para o filho
			 * 
			 * @param newBean 
			 * @param oldBean 
			 */
			public void modifyItemValues(Object newBean, Object bean) {
		        DescontoAnexo anexoModified = (DescontoAnexo) newBean;
		        DescontoAnexo baseAnexo = (DescontoAnexo) bean;
		        
		        baseAnexo.setDsAnexo(anexoModified.getDsAnexo());
		        baseAnexo.setDcArquivo(anexoModified.getDcArquivo());
		        baseAnexo.setBlEnvAnexoQuestFat(anexoModified.getBlEnvAnexoQuestFat());
		        
		        baseAnexo.setDhModificacao( new DateTime() );
			}
			
			/**
			 * Todos os dados a serem carregados na grid pelo form passam antes por este
			 * metodo. Para se fazer uma validacao...
			 * Recomenda-se que o bean em questao seja gerado nesta classe a partir dos
			 * parametros enviados da tela para se evitar um 'ReflectionUtils'
			 * 
			 * @param mapParameter 
			 * @param bean a ser istanciado
			 * @return Object bean instanciado
			 */
			public Object populateNewItemInstance(Map mapParameter, Object bean) {
				DescontoAnexo descontoAnexo = (DescontoAnexo) bean;				
				TypedFlatMap parameters = (TypedFlatMap) mapParameter;
				
		    	
		    	descontoAnexo.setIdDescontoAnexo( parameters.getLong("idDescontoAnexo") );
		    	descontoAnexo.setDsAnexo( parameters.getString("dsAnexo") );
		    	try {
					descontoAnexo.setDcArquivo(Base64Util.decode(parameters.getString("dcArquivo")));
				} catch (IOException e) {
					log.error(e);
				}
		    	descontoAnexo.setBlEnvAnexoQuestFat( parameters.getBoolean("blEnvAnexoQuestFat"));
				descontoAnexoService.prepareValuesToStore(descontoAnexo);
				return descontoAnexo;
			}			

    	};    	
    	
    	config.addItemConfig(DESCONTO_ANEXO_CONFIG_ALIAS, DescontoAnexo.class, itemInit, descComparator);
    	
		return config;
	}
	
	public void clearSessionItens(){
		super.removeMasterFromSession();
	}

	public void setDescontoAnexoService(DescontoAnexoService descontoAnexoService) {
		this.descontoAnexoService = descontoAnexoService;
	}

	public DescontoAnexoService getDescontoAnexoService() {
		return descontoAnexoService;
	}
	
    public Serializable storeAnexo(TypedFlatMap parameters) {
		return saveItemInstance( parameters, DESCONTO_ANEXO_CONFIG_ALIAS);
    }     
    
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIdsAnexoDesconto(List ids) {
    	super.removeItemByIds(ids, DESCONTO_ANEXO_CONFIG_ALIAS);
    }

	public void setQuestionamentoFaturasService(
			QuestionamentoFaturasService questionamentoFaturasService) {
		this.questionamentoFaturasService = questionamentoFaturasService;
	}

	public QuestionamentoFaturasService getQuestionamentoFaturasService() {
		return questionamentoFaturasService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public FaturaService getFaturaService() {
		return faturaService;
	}     
	
    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	
    /**
     * Verifica se o usu�rio logado possui permiss�es de acesso ao documento de servi�o
     * @param tfm : idDoctoServico : Identificador do documento de servi�o
     *              idFilial : Identificador da filial
     * @return <code>TRUE</code> Se o usu�rio logado possui permiss�es de acesso ao documento de servi�o informado ou <code>FALSE</code> 
     *         caso contr�rio.
     */
    public Boolean validatePermissaoDocumentoUsuario(TypedFlatMap tfm){
        
        Long idDoctoServico = tfm.getLong("idDoctoServico");
        Long idFilial       = tfm.getLong("idFilial");

        return this.doctoServicoService.validatePermissaoDocumentoUsuario(idDoctoServico, idFilial);
    }
    
    public BigDecimal findValorMinimoDocumentoDesconto(){
    	return getDescontoService().findValorMinimoDocumentoDesconto();
    }
    
    
}
