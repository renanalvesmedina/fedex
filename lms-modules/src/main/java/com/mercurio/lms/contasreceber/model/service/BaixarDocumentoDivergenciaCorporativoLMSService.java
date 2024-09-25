package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.RelacaoCobranca;
import com.mercurio.lms.contasreceber.model.param.DevedorDocServFatLookupParam;
import com.mercurio.lms.expedicao.model.dao.ConhecimentosCorporativoDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.baixarDocumentoDivergenciaCorporativoLMSService"
 */
public class BaixarDocumentoDivergenciaCorporativoLMSService {
	
	private FaturaService faturaService;
	private GerarLiquidacaoFaturaService gerarLiquidacaoFaturaService;
	private RelacaoCobrancaService relacaoCobrancaService;
	private GerarEncerramentoCobrancaService gerarEncerramentoCobrancaService;
	private DevedorDocServFatLookUpService devedorDocServFatLookUpService;
	private GerarFaturaFaturaService gerarFaturaFaturaService;
	private ConhecimentosCorporativoDAO conhecimentosCorporativoDAO;
	private DevedorDocServFatService devedorDocServFatService;
	private ConfiguracoesFacade configuracoesFacade;
	private FilialService filialService;
	
	/**
	 * Set FaturaService (Spring - Inversion of control)
	 */
	public void setFaturaService(FaturaService faturaService){
		this.faturaService = faturaService;
	}
	
	/**
	 * Set GerarLiquidacaoFaturaService (Spring - Inversion of control)
	 */
	public void setGerarLiquidacaoFaturaService(GerarLiquidacaoFaturaService gerarLiquidacaoFaturaService){
		this.gerarLiquidacaoFaturaService = gerarLiquidacaoFaturaService;
	}
	
	/**
	 * Set RelacaoCobrancaService (Spring - Inversion of control)
	 */
	public void setRelacaoCobrancaService(RelacaoCobrancaService relacaoCobrancaService){
		this.relacaoCobrancaService = relacaoCobrancaService;
	}
	
	/**
	 * Set GerarEncerramentoCobrancaService (Spring - Inversion of control)
	 */
	public void setGerarEncerramentoCobrancaService(GerarEncerramentoCobrancaService gerarEncerramentoCobrancaService){
		this.gerarEncerramentoCobrancaService = gerarEncerramentoCobrancaService;
	}
	/**
	 * Set DevedorDocServFatLookUpService (Spring - Inversion of control)
	 */
	public void setDevedorDocServFatLookUpService(DevedorDocServFatLookUpService devedorDocServFatLookUpService){
		this.devedorDocServFatLookUpService = devedorDocServFatLookUpService;
	}
	
	/**
	 * Set GerarFaturaFaturaService (Spring - Inversion of control)
	 */
	public void setGerarFaturaFaturaService(GerarFaturaFaturaService gerarFaturaFaturaService){
		this.gerarFaturaFaturaService = gerarFaturaFaturaService;
	}
	
	/**
	 * Set ConhecimentosCorporativoDAO (Spring - Inversion of control)
	 */
	public void setConhecimentosCorporativoDAO(ConhecimentosCorporativoDAO conhecimentosCorporativoDAO){
		this.conhecimentosCorporativoDAO = conhecimentosCorporativoDAO;
	}
	
	/**
	 * Set DevedorDocServFatService (Spring - Inversion of control)
	 */
	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService){
		this.devedorDocServFatService = devedorDocServFatService;
	}
	
	/**
	 * Set ConfiguracoesFacade (Spring - Inversion of control)
	 */
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade){
		this.configuracoesFacade = configuracoesFacade;
	}
	
	/**
	 * Set FilialService (Spring - Inversion of control)
	 */
	public void setFilialService(FilialService filialService){
		this.filialService = filialService;
	}
	
	/**
	 * Método responsável por executar a rotina Baixa de Documentos
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 26/07/2006
	 *
	 */
	public void executeBaixaDocumento(String tpDocumento
									, Long idDocumento
									, Long idFilialRelacaoCobranca
									, Long nrRelacaoCobranca
									, String sgFilialRelacaoCobranca
									, YearMonthDay dtLiquidacao){
		
		Fatura fat = null;
		
		/** REGRA 1 DA ESPECIFICAÇÃO */
		fat = this.findFaturaByDocumento(tpDocumento, idDocumento);
		
		/** REGRA 2 DA ESPECIFICAÇÃO */
		this.gerarLiquidacaoFaturaService.executeLiquidarFatura(fat, dtLiquidacao);
		
		/** REGRA 3 DA ESPECIFICAÇÃO */
		
		// Encerra as cobranças da fatura
		this.gerarEncerramentoCobrancaService.executeEncerrarCobranca();
		
		// Gera uma relacaoCobranca para a fatura
		this.insertRelacaoCobrancaByFaturaLiquidada(nrRelacaoCobranca
												  , sgFilialRelacaoCobranca
												  , fat
												  , dtLiquidacao);
		
	}
	
	/**
	 * Carrega a fatura de acordo com tpDocumento e o idDocumento
	 * 
	 * Valida se tpDocumento é 'FAT' para carregar a fatura ou 
	 * doctoServico para gerar uma fatura
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 26/07/2006
	 *
	 * @return
	 *
	 */
	private Fatura findFaturaByDocumento(String tpDocumento, Long idDocumento){
		Fatura fat;
		 
		/** Caso o documento seja fatura */
		if(tpDocumento.equals("FAT")){
			fat = faturaService.findById(idDocumento);
			
		/** Caso não seja fatura, cria uma fatura conforme o documento informado */
		}else{
			DevedorDocServFat ddsf = new DevedorDocServFat();
			ddsf.setIdDevedorDocServFat(idDocumento);
			
			fat = new Fatura();
			
			/** Seta para FALSE para não gerar Boleto */
			fat.setBlGerarBoleto(Boolean.FALSE);
			
			gerarFaturaFaturaService.executeInicializeDadosFatura(fat);
			fat = gerarFaturaFaturaService.storeFaturaWithDevedorDocServFat(fat, ddsf);
			fat = faturaService.findById(fat.getIdFatura());
		}
		
		//Se a filial de origem da fatura é diferente da filial da sessão ou não é matriz, lança uma exception
		if (fat.getFilialByIdFilial().getIdFilial().compareTo(SessionUtils.getFilialSessao().getIdFilial()) != 0
				&& !SessionUtils.isFilialSessaoMatriz()){
			throw new BusinessException("LMS-36210");
		}
		
		return fat;
	}
	
	/**
	 * Valida se a relacao cobranca informada existe no LMS
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 08/12/2006
	 *
	 * @param sgFilial
	 * @param nrRelacaoCobranca
	 * @return
	 *
	 */
	public RelacaoCobranca verifyExistsRelacaoCobrancaLms(String sgFilial, Long nrRelacaoCobranca){
		Filial filial = null;
		RelacaoCobranca rc = null;
		List lst = null;
		/** Busca a relacao de cobranca no LMS */
		if(!SessionUtils.isIntegrationRunning()){
			lst = relacaoCobrancaService.findRelacaoCobrancaByNrRelacaoCobranca(nrRelacaoCobranca, sgFilial);
		if(lst != null && !lst.isEmpty()){
			rc = (RelacaoCobranca) lst.get(0);
		} 
		}else{
			filial = filialService.findFilial(SessionUtils.getEmpresaSessao().getIdEmpresa(), sgFilial);
			Long idFilial = filial.getIdFilial();
		
			rc = relacaoCobrancaService.findRelacaoCobrancaByNrRelacaoCobrancaFilialAndIdFilial(nrRelacaoCobranca,idFilial);
		}
		/** Caso exista alguma, carrega para ser editada */
		
		
		return rc;
		
	}
	
	/**
	 * Método responsável por inserir ou editar uma RelacaoCobranca	 * Alterado para setar situacao = E e dsOrigem = CORPORATIVO BANCOS para funcionar para a Integração.
	 * @author Hector Julian Esnaola Junior
	 * @since 27/07/2006
	 *
	 * @param nrRelacaoCobranca
	 * @param sgFilial
	 * @param fat
	 * @param dtLiquidacao
	 * 
	 */
	public void insertRelacaoCobrancaByFaturaLiquidada(Long nrRelacaoCobranca
													  , String sgFilial
													  , Fatura fat
													  , YearMonthDay dtLiquidacao){
		
		RelacaoCobranca rc = verifyExistsRelacaoCobrancaLms(sgFilial, nrRelacaoCobranca);
		
		/** 
		 * Caso tenha sido informado uma relacaoCobranca que não exista no LMS, cria uma nova relacaoCobranca
		 */
		if(rc == null) {

			rc = new RelacaoCobranca();

			/** Seta a filial da relacao de cobranca */
			Filial filial = filialService.findFilial(SessionUtils.getEmpresaSessao().getIdEmpresa(), sgFilial);
			rc.setFilial(filial);

			/** INFORMADA UMA RELACAO COBRANCA INEXISTENTE NO LMS - Seta o nrRelacaoCobranca (relCobInexistente) informado */
			rc.setNrRelacaoCobrancaFilial(nrRelacaoCobranca);

			/** Busca o número atual da relacao de cobrnaca nos parametros gerais */ 
			Long maxNrRelacaoCobranca = ((BigDecimal)configuracoesFacade.getValorParametro(filial.getIdFilial(), "NR_RELACAO_COBRANCA")).longValue();

			/** Atualiza parametro da filial somente se o número da última relacao de cobranca do LMS é menor que a relacao de cobranca do corporativo */ 
			if ( maxNrRelacaoCobranca.compareTo(nrRelacaoCobranca) < 0 ) {
				/** Salva o nrRelacaoCobranca informado, como paramentro filial atual */
				configuracoesFacade.storeValorParametro(filial.getIdFilial(), "NR_RELACAO_COBRANCA", new BigDecimal(nrRelacaoCobranca));
			}

			/** Seta os atributos da RelacaoCobranca */

			rc.setVlJuros(BigDecimal.ZERO);
			rc.setVlTarifa(BigDecimal.ZERO);
			rc.setVlCsll(BigDecimal.ZERO);
			rc.setVlPis(BigDecimal.ZERO);
			rc.setVlCofins(BigDecimal.ZERO);
			rc.setVlIr(BigDecimal.ZERO);
			if(!SessionUtils.isIntegrationRunning()){
			rc.setTpSituacaoRelacaoCobranca(new DomainValue("A"));
			rc.setDsOrigem("DIVERGENCIA CORPORATIVO");
			}else{
				rc.setTpSituacaoRelacaoCobranca(new DomainValue("E"));
				rc.setDsOrigem("CORPORATIVO BANCOS");
			}
			rc.setDtLiquidacao(dtLiquidacao);
			
		}
		
		/** Seta os atributos da RelacaoCobranca tanto para edição quanto para uma nova RelacaoCobranca */
		
		if( rc.getVlFrete() != null ){
			rc.setVlFrete(fat.getVlTotal().add(rc.getVlFrete()));
		}else{
			rc.setVlFrete(fat.getVlTotal());
		}
		
		if( rc.getVlDesconto() != null ){
			rc.setVlDesconto(fat.getVlDesconto().add(rc.getVlDesconto()));
		}else {
			rc.setVlDesconto(fat.getVlDesconto());
		}
			
		
		/** Salva ou edita a RelacaoCobranca em questão */
		this.relacaoCobrancaService.store(rc);
		
		/** Edita a fatura, setando a RelacaoCobranca */
		fat.setRelacaoCobranca(rc);
		this.faturaService.store(fat);
		
	}
	
	/**
	 * Método responsável por buscar a fatura de acordo com a filial e o número de fatura,
	 * e fazer algumas validações.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 25/07/2006
	 *
	 * @param map
	 * @return
	 *
	 */
	public List findFatura(TypedFlatMap map){
		Long idFilial = map.getLong("filialByIdFilial.idFilial");	
		Long nrDocumento = map.getLong("nrFatura");		

		List lstFatura = faturaService.findByNrFaturaByFilial(nrDocumento, idFilial);
		
		if (lstFatura.size() == 1){
			TypedFlatMap mapRetorno = new TypedFlatMap();
			Fatura fat = (Fatura)lstFatura.get(0);
			
			mapRetorno.put("idFatura", fat.getIdFatura());
			mapRetorno.put("idDocumento", fat.getIdFatura());
			mapRetorno.put("nrFatura", fat.getNrFatura());
			mapRetorno.put("filialByIdFilial.idFilial", fat.getFilialByIdFilial().getIdFilial());
			mapRetorno.put("filialByIdFilial.sgFilial", fat.getFilialByIdFilial().getSgFilial());
			mapRetorno.put("filialByIdFilial.pessoa.nmFantasia", fat.getFilialByIdFilial().getPessoa().getNmFantasia());
			
			List lstRetorno = new ArrayList();
			
			lstRetorno.add(mapRetorno);
			
			return lstRetorno;
			
		} else {
			return lstFatura;
		}
	}
	
	/**
	 * Busca o primeiro DevedorDocServFat da fatura 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 01/08/2006
	 *
	 * @param Long idFatura
	 * @return DevedorDocServFat devedor
	 *
	 */
	public DevedorDocServFat getFirstNrConhecimentoByFatura(Long idFatura){
		
		List lst = faturaService.findNrDoctoServicoByIdFatura(idFatura);
		DevedorDocServFat devedor = null;
	
		if(lst != null && !lst.isEmpty()){
			devedor = (DevedorDocServFat) lst.get(0);
		}
	
		return devedor;
	}
	
	/**
	 * Método responsável por fazer as validações da fatura
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 26/07/2006
	 *
	 * @param fat
	 *
	 */
	public void validateFatura(Long idFatura){
		Fatura fat = faturaService.findById(idFatura);
		
		validateSituacaoFatura(fat);
		
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/06/2007
	 *
	 * @param fat
	 * @throws BusinessException
	 *
	 */
	public void validateSituacaoFatura(Fatura fat) throws BusinessException {
		/** Valida a situação da fatura. Deve ser Emitida, Em boleto ouEm recibo. 
		 * Caso contrário deve ser lançada uma excessão
		 */
		if(!fat.getTpSituacaoFatura().getValue().equals("EM")
				&& !fat.getTpSituacaoFatura().getValue().equals("BL")
				&& !fat.getTpSituacaoFatura().getValue().equals("RC")){
			throw new BusinessException("LMS-36164");
		}
	}
	
	/**
	 * Método responsável por buscar o devedorDocServFat de acordo com a filial e o número de fatura,
	 * e fazer algumas validações. 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 25/07/2006
	 *
	 * @param map
	 * @return
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
		
		List lstDevedor = this.devedorDocServFatLookUpService.findDevedorDocServFat(devedorDocServFatLookupParam);
		
		if (lstDevedor.size() == 1){
			TypedFlatMap mapRetorno = new TypedFlatMap();
			Map mapDevedor = (Map)lstDevedor.get(0);

			mapRetorno.put("idDocumento", mapDevedor.get("idDevedorDocServFat"));
			mapRetorno.put("idDevedorDocServFat", mapDevedor.get("idDevedorDocServFat"));
			mapRetorno.put("nrDoctoServico", mapDevedor.get("doctoServico_nrDoctoServico"));
			mapRetorno.put("idFilialOrigem", mapDevedor.get("idFilialOrigem"));
			mapRetorno.put("sgFilialOrigem", mapDevedor.get("sgFilialOrigem"));
			
			List lstRetorno = new ArrayList();
			
			lstRetorno.add(mapRetorno);
			
			return lstRetorno;
			
		} else {
			return lstDevedor;
		}		
	}
	
	/**
	 * Método responsável por fazer as validações do conhecimento
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 26/07/2006
	 *
	 * @param fat
	 *
	 */
	public void validateDoctoServico(Long idDevedor){
		
		DevedorDocServFat ddsf = devedorDocServFatService.findByIdInitLazyProperties(idDevedor, false);
		
		/** Valida se a situação do DoctoServico é 'Pendente' ou 'Em carteira' */
		if(!ddsf.getTpSituacaoCobranca().getValue().equals("P")
				&& !ddsf.getTpSituacaoCobranca().getValue().equals("C")){
			throw new BusinessException("LMS-36165");
		}
	}
	
	/**
	 * Busca as datas da tabela v_conhecimentos_corporativo. 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 01/08/2006
	 *
	 * @param nrConhecimento
	 * @return YearMonthDay
	 *
	 */
	public Map findDadosRelacaoCobranca(Long idDocumento, String sgFilial, String tpDocumento){
		
		Long nrConhecimento = null;
		
		/** Caso seja fatura, busca o primeiro conhecimento e traz seu nrConhecimento */
		if(tpDocumento.equals("FAT")){ 
			/** Busca o primeiro número de conhecimento da fatura */
			DevedorDocServFat ddsf = getFirstNrConhecimentoByFatura(idDocumento);
			nrConhecimento = ddsf.getDoctoServico().getNrDoctoServico();
			sgFilial = ddsf.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial();
		/** Caso seja devedorDocServFat */
		}else{
			/** Busca o número do conhecimento */
			nrConhecimento = devedorDocServFatService.findById(idDocumento).getDoctoServico().getNrDoctoServico();
		}
		
		/** Busca a data de liquidação de acordo com o nrConhecimento e a sgFilial */
		List lst = conhecimentosCorporativoDAO.findDadosConhecimento(nrConhecimento, sgFilial);
		
		YearMonthDay ymd = null;
		BigDecimal nrRelacaoCobranca  = null;
		String sgFilialRelacaoCobranca = null;
		
		if(lst != null && !lst.isEmpty()){
			
			Map map = (Map) lst.get(0);
			
			Timestamp dtPagamento = (Timestamp) map.get("DT_PAGAMENTO");
			Timestamp dtEntCobJur = (Timestamp) map.get("DT_ENT_COB_JUR");
			
			nrRelacaoCobranca = (BigDecimal) map.get("NR_RELACAO_COBRANCA");
			sgFilialRelacaoCobranca = (String) map.get("SG_FILIAL_RELACAO_COBRANCA");
			
			/** Seta a data  de liquidação */
			if(dtPagamento != null)
				ymd = new YearMonthDay(dtPagamento);
			else if(dtEntCobJur != null)
				ymd = new YearMonthDay(dtEntCobJur);
		
		/** Caso a List esteja vazia, o documento não foi liquidado no CORPORATIVO */ 
		}else{
			throw new BusinessException("LMS-36176");
		}
		
		Map map = new HashMap();
		
		map.put("dtLiquidacao", ymd);
		map.put("nrRelacaoCobranca", nrRelacaoCobranca);
		map.put("sgFilialRelacaoCobranca", sgFilialRelacaoCobranca);
		
		return map;
	}
}
