package com.mercurio.lms.prestcontasciaaerea.model.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.prestcontasciaaerea.model.FaturaCiaAerea;
import com.mercurio.lms.prestcontasciaaerea.model.FaturaCiaAereaAnexo;
import com.mercurio.lms.prestcontasciaaerea.model.ItemFaturaCiaAerea;
import com.mercurio.lms.prestcontasciaaerea.model.dao.FaturaCiaAereaDAO;
import com.mercurio.lms.util.ArquivoUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.prestcontasciaaerea.faturaCiaAereaService"
 */
public class FaturaCiaAereaService extends CrudService<FaturaCiaAerea, Long> {

	private DomainValueService domainValueService;
	private ConfiguracoesFacade configuracoesFacade;
	private AwbService awbService;
	private ParametroGeralService parametroGeralService;

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setFaturaCiaAereaDAO(FaturaCiaAereaDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private FaturaCiaAereaDAO getFaturaCiaAereaDAO() {
		return (FaturaCiaAereaDAO) getDao();
	}

	public FaturaCiaAerea findById(Long id) {
		return (FaturaCiaAerea) super.findById(id);
	}
	
	/**
	 * Remove a instancia especifica do bean pelo id
	 * e os filhos.
	 */
	@Override
	public void removeById(Long id) {
		FaturaCiaAerea faturaCiaAerea = this.findById(id);
		
		if(faturaCiaAerea.getDtEnvioJDE() != null || faturaCiaAerea.getDtPagamento() != null){
			throw new BusinessException("LMS-37014");
		}
		
		getFaturaCiaAereaDAO().removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		for (Long id : ids) {
			this.removeById(id);
		}
	}

	/**
	 * 
	 * @param faturaCiaAerea
	 * @param itemFaturaCiaAereaList
	 * @param faturaCiaAereaAnexoList
	 * @return
	 */
	public FaturaCiaAerea store(FaturaCiaAerea faturaCiaAerea, ItemList itemFaturaCiaAereaList, List<ItemFaturaCiaAerea> listaItemFaturaCiaAerea, ItemList faturaCiaAereaAnexoList) {
		validateFaturaCiaAerea(faturaCiaAerea, listaItemFaturaCiaAerea);
		
		if(faturaCiaAerea.getIdFaturaCiaAerea() == null){
			faturaCiaAerea.setDtInclusao(new YearMonthDay());
		}
		
		return (FaturaCiaAerea)getFaturaCiaAereaDAO().store(faturaCiaAerea, itemFaturaCiaAereaList, faturaCiaAereaAnexoList);
	}
	
	/**
	 * 
	 * @param faturaCiaAerea
	 */
	private void validateFaturaCiaAerea(FaturaCiaAerea faturaCiaAerea, List<ItemFaturaCiaAerea> listaItemFaturaCiaAerea){
		/*
		 * Se existir registro na seguinte consulta: SELECT * FROM FATURA_CIA_AEREA 
		 * WHERE FATURA_CIA_AEREA.ID_FATURA_CIA_AEREA <> FATURA_CIA_AEREA.ID_FATURA_CIA_AEREA (registro atual) 
		 * AND FATURA_CIA_AEREA.ID_CIA_AEREA = "Cia aérea" (FATURA_CIA_AEREA.ID_CIA_AEREA) 
		 * AND FATURA_CIA_AEREA.NR_FATURA_CIA_AEREA = "Número da fatura" (FATURA_CIA_AEREA.NR_FATURA_CIA_AEREA) 
		 * Então o Exibir mensagem LMS-00002.
		 */
		List<FaturaCiaAerea> listaFaturasCiaAerea = getFaturaCiaAereaDAO().findFaturaCiaAereaMesmaCiaAereaNrFatura(faturaCiaAerea.getIdFaturaCiaAerea(), faturaCiaAerea.getCiaAerea().getIdEmpresa(), 
				faturaCiaAerea.getNrFaturaCiaAerea());
		if(listaFaturasCiaAerea != null && !listaFaturasCiaAerea.isEmpty()){
			throw new BusinessException("LMS-00002");
		}
		
		
		/*
		 * Se "Data de vencimento" < "Data de emissão" Então Exibir mensagem LMS-37021.
		 */
		if(faturaCiaAerea.getDtVencimento().compareTo(faturaCiaAerea.getDtEmissao()) < 0){
			throw new BusinessException("LMS-37021");
		}
		
		/*
		 * Se não existirem registros filhos de ITEM_FATURA_CIA_AEREA, exibir a mensagem LMS-37023. 
		 */
		if(listaItemFaturaCiaAerea == null || listaItemFaturaCiaAerea.isEmpty()){
			throw new BusinessException("LMS-37023");
		}
		
		/*
		 * Para cada registro filho de ITEM_FATURA_CIA_AEREA: Se
		 * ITEM_FATURA_CIA_AEREA.ID_AWB -> AWB.ID_CIA_FILIAL_MERCURIO ->
		 * CIA_FILIAL_MERCURIO.ID_EMPRESA <> FATURA_CIA_AEREA.ID_CIA_AEREA Então
		 * exibir mensagem LMS-37009.
		 */
		for(ItemFaturaCiaAerea itemFaturaCiaAerea : listaItemFaturaCiaAerea){
			Awb awb = itemFaturaCiaAerea.getAwb();
			getFaturaCiaAereaDAO().getHibernateTemplate().refresh(awb);
			
			if (awb.getCiaFilialMercurio().getEmpresa().getIdEmpresa().compareTo(faturaCiaAerea.getCiaAerea().getIdEmpresa()) != 0) {
				String dsSerie = "";
				if (awb.getDsSerie()!= null) {
					dsSerie = awb.getDsSerie();
				}
				String nrAwb = "";
				if (awb.getNrAwb() != null) {
					nrAwb = awb.getNrAwb().toString();
				}
				String dvAwb = "";
				if (awb.getDvAwb()!= null) {
					dvAwb = awb.getDvAwb().toString();
				}
				
				/*
				 * Parâmetros inicializados com "" para não escrever "null" na
				 * mensagem, e convertidos para String para evitar formatação
				 * padrão de acordo com o tipo, no caso utilizava "." como
				 * separador de milhar.
				 */
				throw new BusinessException("LMS-37009", new Object[] {dsSerie, nrAwb, dvAwb});
			}
		};
		
		FaturaCiaAerea faturaCiaAereaBancoDados = null;
		if(faturaCiaAerea.getIdFaturaCiaAerea() != null){
			faturaCiaAereaBancoDados = this.findById(faturaCiaAerea.getIdFaturaCiaAerea());
			getFaturaCiaAereaDAO().getHibernateTemplate().evict(faturaCiaAereaBancoDados);
			
			/*
			 * Se FATURA_CIA_AEREA.DT_ENVIO_JDE está preenchida no banco de
			 * dados (não considerar a informação da tela) e informação de
			 * "Data de envio ao JDE" está em branco então exibir mensagem LMS-37010.
			 */
			if(faturaCiaAereaBancoDados.getDtEnvioJDE() != null && faturaCiaAerea.getDtEnvioJDE() == null){
				throw new BusinessException("LMS-37010");
			}
			
			/*
			 * Se FATURA_CIA_AEREA.DT_PAGAMENTO está preenchida no banco de
			 * dados (não considerar a informação da tela) e informação de
			 * "Data de pagamento" está em branco então exibir mensagem LMS-37011.
			 */
			if(faturaCiaAereaBancoDados.getDtPagamento() != null && faturaCiaAerea.getDtPagamento() == null){
				throw new BusinessException("LMS-37011");
			}
		}
		
		/*
		 * Se "Data de pagamento" diferente de nulo e "Data de envio ao JDE"
		 * diferente de nulo e "Data de pagamento" < "Data de envio ao JDE"
		 * Então exibir mensagem LMS-37012.
		 */
		if(faturaCiaAerea.getDtPagamento() != null && faturaCiaAerea.getDtEnvioJDE() != null && faturaCiaAerea.getDtPagamento().compareTo(faturaCiaAerea.getDtEnvioJDE()) < 0){
			throw new BusinessException("LMS-37012");
		}
		
		/*
		 * Validação LMS-37013 - parte 2:
		 * 
		 * Se FATURA_CIA_AEREA.DT_ENVIO_JDE está preenchida no banco de dados
		 * (não considerar a informação da tela) 
		 * OU 
		 * FATURA_CIA_AEREA.DT_PAGAMENTO está preenchida no banco de dados 
		 * (não considerar a informação da tela) 
		 * 
		 * Então Alterar e persistir instância de FATURA_CIA_AEREA, conforme abaixo: 
		 * - DT_ENVIO_JDE = "Data de envio ao JDE" 
		 * - DT_PAGAMENTO = "Data de pagamento" 
		 * - Persistir instâncias de FATURA_CIA_AEREA_ANEXO 
		 * - NÃO persistir instâncias de ITEM_FATURA_CIA_AEREA
		 */
		if(faturaCiaAereaBancoDados != null){
			if(faturaCiaAereaBancoDados.getDtEnvioJDE() != null || faturaCiaAereaBancoDados.getDtPagamento() != null){
				faturaCiaAerea.setCiaAerea(faturaCiaAereaBancoDados.getCiaAerea());
				faturaCiaAerea.setNrFaturaCiaAerea(faturaCiaAereaBancoDados.getNrFaturaCiaAerea());
				faturaCiaAerea.setDtEmissao(faturaCiaAereaBancoDados.getDtEmissao());
				faturaCiaAerea.setDtVencimento(faturaCiaAereaBancoDados.getDtVencimento());
				faturaCiaAerea.setDtPeriodoInicial(faturaCiaAereaBancoDados.getDtPeriodoInicial());
				faturaCiaAerea.setDtPeriodoFinal(faturaCiaAereaBancoDados.getDtPeriodoFinal());
				faturaCiaAerea.setVlDesconto(faturaCiaAereaBancoDados.getVlDesconto());
				faturaCiaAerea.setVlAcrescimo(faturaCiaAereaBancoDados.getVlAcrescimo());
				faturaCiaAerea.setObFatura(faturaCiaAereaBancoDados.getObFatura());
				faturaCiaAerea.setDtInclusao(faturaCiaAereaBancoDados.getDtInclusao());
				faturaCiaAerea.setAlteracaoCompleta(Boolean.FALSE);
			}
		}
	}
	
	/**
	 * Validação LMS-37013 - parte 1: 
	 * Se FATURA_CIA_AEREA.DT_ENVIO_JDE está  preenchida no banco de dados (não considerar a informação da tela) 
	 * OU FATURA_CIA_AEREA.DT_PAGAMENTO está preenchida no banco de dados (não considerar a informação da tela) 
	 * Então exibir aviso LMS-37013. 
	 * 
	 * @param faturaCiaAerea
	 */
	public void validateAlteracaoFaturaCiaAerea(FaturaCiaAerea faturaCiaAerea){
		if(faturaCiaAerea.getIdFaturaCiaAerea() != null){
			FaturaCiaAerea faturaCiaAereaBancoDados = this.findById(faturaCiaAerea.getIdFaturaCiaAerea());
			if(faturaCiaAereaBancoDados.getDtEnvioJDE() != null || faturaCiaAereaBancoDados.getDtPagamento() != null){
				throw new BusinessException("LMS-37013");
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		ResultSetPage rsp = getFaturaCiaAereaDAO().findPaginated(criteria, findDef);

		List listaRetorno = new ArrayList();
		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {

			FaturaCiaAerea faturaCiaAerea = (FaturaCiaAerea) iter.next();

			Map map = new HashMap();
			map.put("idFaturaCiaAerea", faturaCiaAerea.getIdFaturaCiaAerea());
			map.put("companhiaAerea", faturaCiaAerea.getCiaAerea().getPessoa().getNmPessoa());
			map.put("numeroFatura", faturaCiaAerea.getNrFaturaCiaAerea());

			/* Situação: Utilizar domínio DM_SITUACAO_FATURA_AEREO conforme retorno da seguinte condição:
				-	Se FATURA_CIA_AEREA.DT_PAGAMENTO IS NOT NULL Então "LI"
				-	Senão se FATURA_CIA_AEREA.DT_ENVIO_JDE IS NOT NULL Então "EJ"
				-	Senão "DI"
			 */
			String tpSituacao = null;
			if (faturaCiaAerea.getDtPagamento() != null) {
				tpSituacao = "LI";
			} else if (faturaCiaAerea.getDtEnvioJDE() != null) {
				tpSituacao = "EJ";
			} else {
				tpSituacao = "DI";
			}
			map.put("tpSituacao", this.getDomainValueService().findDomainValueByValue("DM_SITUACAO_FATURA_AEREO", tpSituacao));

			map.put("dataEmissao", faturaCiaAerea.getDtEmissao());
			map.put("dataVencimento", faturaCiaAerea.getDtVencimento());

			setarTotais(faturaCiaAerea);
			map.put("valorFrete", faturaCiaAerea.getVlSomaAwbs());
			map.put("valorDesconto", faturaCiaAerea.getVlSomaDescontosAwbs());
			map.put("valorAcrescimo", faturaCiaAerea.getVlSomaAcrescimosAwbs());
			map.put("valorTotal", faturaCiaAerea.getVlTotalFatura());

			listaRetorno.add(map);
		}

		rsp.setList(listaRetorno);

		return rsp;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getFaturaCiaAereaDAO().getRowCount(criteria);
	}

	/**
	 * 
	 * @param faturaCiaAerea
	 */
	private void setarTotais(FaturaCiaAerea faturaCiaAerea) {
		BigDecimal vlSomaAwbs = BigDecimal.ZERO;
		BigDecimal vlSomaDescontosAwbs = faturaCiaAerea.getVlDesconto();
		BigDecimal vlSomaAcrescimosAwbs = faturaCiaAerea.getVlAcrescimo();

		if (faturaCiaAerea.getItensFaturaCiaAerea() != null) {
			for (ItemFaturaCiaAerea itemFaturaCiaAerea : faturaCiaAerea.getItensFaturaCiaAerea()) {
				// Valor frete: Soma de AWB.VL_FRETE_CALCULADO onde
				// FATURA_CIA_AEREA.ID_FATURA_CIA_AEREA ->
				// ITEM_FATURA_CIA_AEREA.ID_AWB -> AWB
				if (itemFaturaCiaAerea.getAwb().getVlFreteCalculado() != null) {
					vlSomaAwbs = vlSomaAwbs.add(itemFaturaCiaAerea.getAwb().getVlFreteCalculado());
				}

				// Valor descontos: FATURA_CIA_AEREA.VL_DESCONTO + Soma de
				// ITEM_FATURA_CIA_AEREA.VL_DESCONTO onde
				// FATURA_CIA_AEREA.ID_FATURA_CIA_AEREA -> ITEM_FATURA_CIA_AEREA
				if (itemFaturaCiaAerea.getVlDesconto() != null) {
					vlSomaDescontosAwbs = vlSomaDescontosAwbs.add(itemFaturaCiaAerea.getVlDesconto());
				}

				// Valor acréscimos: FATURA_CIA_AEREA.VL_ACRESCIMO + Soma de
				// ITEM_FATURA_CIA_AEREA.VL_ACRESCIMO onde
				// FATURA_CIA_AEREA.ID_FATURA_CIA_AEREA -> ITEM_FATURA_CIA_AEREA
				if (itemFaturaCiaAerea.getVlAcrescimo() != null) {
					vlSomaAcrescimosAwbs = vlSomaAcrescimosAwbs.add(itemFaturaCiaAerea.getVlAcrescimo());
				}
			}
		}

		faturaCiaAerea.setVlSomaAwbs(vlSomaAwbs);
		faturaCiaAerea.setVlSomaDescontosAwbs(vlSomaDescontosAwbs);
		faturaCiaAerea.setVlSomaAcrescimosAwbs(vlSomaAcrescimosAwbs);

		// Valor total: "Valor frete" – "Valor descontos" + "Valor acréscimos"
		faturaCiaAerea.setVlTotalFatura(vlSomaAwbs.subtract(vlSomaDescontosAwbs).add(vlSomaAcrescimosAwbs));
	}

	/**
	 * 
	 * @param idFaturaCiaAerea
	 * @return
	 */
	public List<ItemFaturaCiaAerea> findItemFaturaCiaAereaByIdFaturaCiaAerea(Long idFaturaCiaAerea) {
		List<ItemFaturaCiaAerea> listItemFaturaCiaAerea = getFaturaCiaAereaDAO().findItemFaturaCiaAereaByIdFaturaCiaAerea(idFaturaCiaAerea);

		for (ItemFaturaCiaAerea itemFaturaCiaAerea : listItemFaturaCiaAerea) {
			Awb awb = itemFaturaCiaAerea.getAwb();
			itemFaturaCiaAerea.setDsSerie(awb.getDsSerie());
			itemFaturaCiaAerea.setNrAwb(awb.getNrAwb());
			itemFaturaCiaAerea.setDvAwb(awb.getDvAwb());
			itemFaturaCiaAerea.setNrAwbFormatado(AwbUtils.getSgEmpresaAndNrAwbFormated(awb));
			itemFaturaCiaAerea.setDhEmissao(awb.getDhEmissao());
			itemFaturaCiaAerea.setTpStatusAwb(awb.getTpStatusAwb());
			itemFaturaCiaAerea.setVlFrete(awb.getVlFreteCalculado());
			itemFaturaCiaAerea.setQtVolumes(awb.getQtVolumes());
			itemFaturaCiaAerea.setPsTotal(awb.getPsTotal());
			itemFaturaCiaAerea.setPsCubado(awb.getPsCubado());
			
			if(awb.getAeroportoByIdAeroportoOrigem() != null){
				itemFaturaCiaAerea.setSgAeroportoOrigem(awb.getAeroportoByIdAeroportoOrigem().getSgAeroporto());
			}
			
			if(awb.getAeroportoByIdAeroportoDestino() != null){
				itemFaturaCiaAerea.setSgAeroportoDestino(awb.getAeroportoByIdAeroportoDestino().getSgAeroporto());
			}
			
			if (itemFaturaCiaAerea.getVlFrete() != null) {
				itemFaturaCiaAerea.setVlDiferencaFrete(itemFaturaCiaAerea.getVlCobrado().subtract(itemFaturaCiaAerea.getVlFrete()));
			} else {
				itemFaturaCiaAerea.setVlDiferencaFrete(itemFaturaCiaAerea.getVlCobrado());
			}
			
			itemFaturaCiaAerea.setDiferencaFrete(this.setarImagemValor(itemFaturaCiaAerea.getVlDiferencaFrete()));
		}

		return listItemFaturaCiaAerea;
	}
	
	public String setarImagemValor(BigDecimal vlDiferencaFrete) {
		vlDiferencaFrete = BigDecimal.valueOf(Math.abs(vlDiferencaFrete.doubleValue()));
		
		BigDecimal limiteDiferencaFatura = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("LIMITE_DIFERENCA_FATURA_CIA", false);
		
		if (vlDiferencaFrete.compareTo(limiteDiferencaFatura) == 1) {
			return "/image/bola_vermelha.gif";
		} else {
			return "/image/bola_verde.gif";
		} 
	}
	
	/**
	 * 
	 * @param idAwb
	 * @return
	 */
	public List<FaturaCiaAerea> findFaturaCiaAereaByIdAwb(Long idAwb) {
		return getFaturaCiaAereaDAO().findFaturaCiaAereaByIdAwb(idAwb);
	}
	
	/**
	 * 
	 * @param idAwb
	 * @param idFaturaCiaAerea
	 * @return
	 */
	public List<ItemFaturaCiaAerea> findFaturaCiaAereaByIdAwbByIdFatura(Long idAwb, Long idFaturaCiaAerea) {
		return getFaturaCiaAereaDAO().findFaturaCiaAereaByIdAwbByIdFatura(idAwb, idFaturaCiaAerea);
	}
	
	/**
	 * 
	 * 
	 * @param masterId
	 * @return
	 */
	public Integer getRowCountItemFaturaCiaAerea(Long idFaturaCiaAerea) {
		return getFaturaCiaAereaDAO().getRowCountItemFaturaCiaAerea(idFaturaCiaAerea);
	}
	
	
	/**
	 * "ROTINA DE VALIDAÇÃO DE AWB"
	 * 
	 * @param itemFaturaCiaAerea
	 */
	public void validateAwb(ItemFaturaCiaAerea itemFaturaCiaAerea, List<ItemFaturaCiaAerea> itensFaturaSessao, FaturaCiaAerea faturaCiaAerea){

		Awb awb = itemFaturaCiaAerea.getAwb();
		String dsSerie = "";
		
		Object[] params = new Object[] {AwbUtils.getSgEmpresaAndNrAwbFormated(awb)}; 
		if (awb.getCiaFilialMercurio().getEmpresa().getIdEmpresa().compareTo(faturaCiaAerea.getCiaAerea().getIdEmpresa()) != 0) {
			throw new BusinessException("LMS-37009", params);
		}
		
		if (awb.getTpStatusAwb() == null || !ConstantesExpedicao.TP_STATUS_AWB_EMITIDO.equals(awb.getTpStatusAwb().getValue())) {
			throw new BusinessException("LMS-37015", params);
		}
		
		if (awb.getDhEmissao() != null && new YearMonthDay(awb.getDhEmissao()).compareTo(faturaCiaAerea.getDtEmissao()) > 0) {
			throw new BusinessException("LMS-37016", params);
		}
		
		for (ItemFaturaCiaAerea itemFaturaCiaAereaIt : itensFaturaSessao) {
			if (itemFaturaCiaAereaIt.getAwb().getIdAwb().compareTo(awb.getIdAwb()) == 0 
					&& (itemFaturaCiaAerea.getIdItemFaturaCiaAerea() == null || itemFaturaCiaAereaIt.getIdItemFaturaCiaAerea().compareTo(itemFaturaCiaAerea.getIdItemFaturaCiaAerea()) != 0)) {
				throw new BusinessException("LMS-37017", params);
			}
		}
		
		List<ItemFaturaCiaAerea> listaItensFatura  = this.findFaturaCiaAereaByIdAwbByIdFatura(awb.getIdAwb(), faturaCiaAerea.getIdFaturaCiaAerea());
		if (listaItensFatura != null && !listaItensFatura.isEmpty()) {
			throw new BusinessException("LMS-37017", params);
		}
	}
	
	/**
	 * 
	 * @param parameters
	 * @param itensFaturaSessao
	 * @param faturaCiaAerea
	 * @return
	 */
	public Map executeImportacao(TypedFlatMap parameters, List<ItemFaturaCiaAerea> itensFaturaSessao, FaturaCiaAerea faturaCiaAerea) {
		
		Scanner sc = null;
		Map map = null;
		try {
			byte[] tmpArquivo;
			try {
				tmpArquivo = Base64Util.decode(MapUtils.getString(parameters, "arquivoCSV"));
			} catch (IOException e) {
				throw new BusinessException("LMS-37018");
			}
			
			//remove nome do arquivo
			byte[] arquivo = Arrays.copyOfRange(tmpArquivo, 1024, tmpArquivo.length);
			InputStream is = new ByteArrayInputStream(arquivo);
			sc = new Scanner(is).useDelimiter("[;\r\n]+");
			map = this.processaArquivoCSV(sc, itensFaturaSessao, faturaCiaAerea);
	
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		
		return map;
	}
	
	/**
	 * 
	 * @param sc
	 * @param itensFaturaSessao
	 * @param faturaCiaAerea
	 * @return
	 */
	private Map processaArquivoCSV(Scanner sc, List<ItemFaturaCiaAerea> itensFaturaSessao, FaturaCiaAerea faturaCiaAerea) {
		TypedFlatMap result = new TypedFlatMap();
		String exceptions = new String();
		Map<Long, ItemFaturaCiaAerea> listaItens = new HashMap<Long, ItemFaturaCiaAerea>();
		Long idCiaAerea = faturaCiaAerea.getCiaAerea().getIdEmpresa();
		
		//Pula a primeira linha
		if (sc.hasNextLine()) {
			String cabecalho = sc.nextLine();
			/* ver isso*/
			String[] colunas = cabecalho.split(";");
			if (colunas.length != 5) {
				throw new BusinessException("LMS-37018");
			}
		}
		while (sc.hasNextLine()) {
			String linha = sc.nextLine();
	
			String[] colunas = linha.split(";");
			
			String coluna1 = "";
			String coluna2 = "";
			String coluna3 = "";
			String coluna4 = "";
			String coluna5 = "";
			try {
				coluna1 = colunas[0];
			} catch (ArrayIndexOutOfBoundsException e) {
			}
			try {
				coluna2 = colunas[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new BusinessException("LMS-37018");
			}
			
			try {
				coluna3 = colunas[2];
			} catch (ArrayIndexOutOfBoundsException e) {
			}
			try {
				coluna4 = colunas[3];
			} catch (ArrayIndexOutOfBoundsException e) {
			}
			try {
				coluna5 = colunas[4];
			} catch (ArrayIndexOutOfBoundsException e) {
			}
			
			Boolean valoresNumericos = StringUtils.isNumeric(coluna2);
			valoresNumericos &= StringUtils.isNumeric(coluna3);
			valoresNumericos &= StringUtils.isNumeric(coluna4.replaceAll(",", ""));
			valoresNumericos &= StringUtils.isNumeric(coluna5.replaceAll(",", ""));
			if (!valoresNumericos) {
				throw new BusinessException("LMS-37018");
			}

			
			List<Object> paramException = new ArrayList<Object>();
			String dsSerie = null;
			if (StringUtils.isNotBlank(coluna1)) {
				dsSerie = coluna1;
				paramException.add(dsSerie);
			} else {
				paramException.add("");
			}
			
			Long nrAwb = null;
			if (StringUtils.isNotBlank(coluna2)) {
				nrAwb = Long.valueOf(coluna2);
			} else {
				throw new BusinessException("LMS-37018");
			}
		
			paramException.add(nrAwb.toString());
			Integer dvAwb = null;
			if (!StringUtils.isBlank(coluna3)) {
				dvAwb = Integer.valueOf(coluna3);
				paramException.add(dvAwb);
			} else {
				paramException.add("");
			}
			
			List<Awb> listAwb = awbService.findBySerieByNrAwbByDvAwbCiaAerea(dsSerie, nrAwb, dvAwb, idCiaAerea);
			if (listAwb == null || listAwb.isEmpty()) {
				exceptions += "LMS-37009 - " + configuracoesFacade.getMensagem("LMS-37009", paramException.toArray()) + "\n";
				continue;
			} else if(listAwb.size() > 1) {
				exceptions += "LMS-37025 - " + configuracoesFacade.getMensagem("LMS-37025", paramException.toArray()) + "\n";
				continue;
			}
			
			Awb awb = listAwb.get(0);
			ItemFaturaCiaAerea itemFaturaCiaAerea = new ItemFaturaCiaAerea();
			itemFaturaCiaAerea.setAwb(awb);
			BigDecimal vlCobrado = BigDecimal.ZERO;
			if (StringUtils.isNotBlank(coluna4)) {
				coluna4 = coluna4.replaceAll("\\.", "");
				coluna4 = coluna4.replaceAll(",", ".");
				vlCobrado = new BigDecimal(coluna4);
			}
			itemFaturaCiaAerea.setVlCobrado(vlCobrado);
			BigDecimal psCobrado = BigDecimal.ZERO;
			if (StringUtils.isNotBlank(coluna5)) {
				coluna5 = coluna5.replaceAll("\\.", "");
				coluna5 = coluna5.replaceAll(",", ".");
				psCobrado = new BigDecimal(coluna5);
			}
			itemFaturaCiaAerea.setPsCobrado(psCobrado);
			itemFaturaCiaAerea.setVlDesconto(BigDecimal.ZERO);
			itemFaturaCiaAerea.setVlAcrescimo(BigDecimal.ZERO);
			
			if (vlCobrado.compareTo(BigDecimal.ZERO) != 0 && vlCobrado.compareTo(awb.getVlFrete()) != 0) {
				exceptions += "LMS-37019 - " + configuracoesFacade.getMensagem("LMS-37019", paramException.toArray()) + "\n";
				continue;
			}
			
			if (psCobrado.compareTo(BigDecimal.ZERO) != 0 && psCobrado.compareTo(awb.getPsCubado()) != 0) {
				exceptions += "LMS-37020 - " + configuracoesFacade.getMensagem("LMS-37020", paramException.toArray()) + "\n";
				continue;
			}
			
			try {
				validateAwb(itemFaturaCiaAerea, itensFaturaSessao, faturaCiaAerea);
			} catch (BusinessException e) {
				exceptions += e.getMessageKey() + " - " + configuracoesFacade.getMensagem(e.getMessageKey(), e.getMessageArguments()) + "\n";
				continue;
			}
			
			if (listaItens.isEmpty() || !listaItens.containsKey(awb.getIdAwb())) {
				listaItens.put(awb.getIdAwb(), itemFaturaCiaAerea);
			} else {
				exceptions += "LMS-37022 - " + configuracoesFacade.getMensagem("LMS-37022", paramException.toArray()) + "\n";
			}

		}

		result.put("erros", exceptions);
		result.put("listaItens", new ArrayList<ItemFaturaCiaAerea>(listaItens.values()));
		
		return result;
	}
	
	public FaturaCiaAereaAnexo findFaturaCiaAereaAnexoById(Long idFaturaCiaAereaAnexo){
		return getFaturaCiaAereaDAO().findFaturaCiaAereaAnexoById(idFaturaCiaAereaAnexo);
	}
	
	/**
	 * 
	 * @param idFaturaCiaAerea
	 * @return
	 */
	public List<FaturaCiaAereaAnexo> findFaturaCiaAereaAnexoByIdFaturaCiaAerea(Long idFaturaCiaAerea) {
		List<FaturaCiaAereaAnexo> listFaturaCiaAereaAnexo = getFaturaCiaAereaDAO().findFaturaCiaAereaAnexoByIdFaturaCiaAerea(idFaturaCiaAerea);
		
		for (FaturaCiaAereaAnexo faturaCiaAereaAnexo : listFaturaCiaAereaAnexo) {
			faturaCiaAereaAnexo.setNmArquivo(ArquivoUtils.getNomeArquivo(faturaCiaAereaAnexo.getDcArquivo()));
			faturaCiaAereaAnexo.setNmUsuario(faturaCiaAereaAnexo.getUsuario().getUsuarioADSM().getNmUsuario());
		}
		
		return listFaturaCiaAereaAnexo;
	}	
	
	public Integer getRowCountFaturaCiaAereaAnexo(Long idFaturaCiaAerea) {
		return getFaturaCiaAereaDAO().getRowCountFaturaCiaAereaAnexo(idFaturaCiaAerea);
	}
	
	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public AwbService getAwbService() {
		return awbService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
}
