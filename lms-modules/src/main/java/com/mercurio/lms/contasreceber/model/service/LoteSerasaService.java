package com.mercurio.lms.contasreceber.model.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.FetchMode;
import org.joda.time.DateTime;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemLoteSerasa;
import com.mercurio.lms.contasreceber.model.LoteSerasa;
import com.mercurio.lms.contasreceber.model.dao.ItemLoteSerasaDAO;
import com.mercurio.lms.contasreceber.model.dao.LoteSerasaDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.questionamentoFaturas.model.service.QuestionamentoFaturasService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.loteSerasaService"
 */
public class LoteSerasaService extends CrudService<LoteSerasa, Long> {

	private Logger log = LogManager.getLogger(this.getClass());
	private ParametroGeralService parametroGeralService;
	private FilialService filialService;
	private FaturaService faturaService;
	private QuestionamentoFaturasService questionamentoFaturasService;
	private RelacaoPagtoParcialService relacaoPagtoParcialService;
	private ItemLoteSerasaDAO itemLoteSeradaDao;
	private PropriedadeColuna propriedadeColuna;
	
	private static final long ID_EMPRESA = 361L;
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @param Instância do DAO.
	 */
	public void setLoteSerasaDAO(LoteSerasaDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private LoteSerasaDAO getLoteSerasaDAO() {
		return (LoteSerasaDAO) getDao();
	}

	
	/**
	 * Retorna uma lista de itens com as faturas de determinado lote enviado ao Serasa
	 * 
	 * @param idLoteSerasa
	 * @return
	 */
	public List<ItemLoteSerasa> findItemLoteSerasaByIdLoteSerasa(Long idLoteSerasa) {
		
		List<ItemLoteSerasa> listItemLoteSerasa = getLoteSerasaDAO().findItemLoteSerasaByIdLoteSerasa(idLoteSerasa);
		
		for (ItemLoteSerasa itemLoteSerasa : listItemLoteSerasa) {
			itemLoteSerasa.setNrFaturaDesc(itemLoteSerasa.getSgFilial() + " " + itemLoteSerasa.getNrFatura());
			itemLoteSerasa.setNrIdentificacaoFormatado(FormatUtils.formatIdentificacao(itemLoteSerasa.getTpIdentificacao(),itemLoteSerasa.getNrIdentificacao()));
			itemLoteSerasa.setNmPessoa(itemLoteSerasa.getNrIdentificacaoFormatado() + " - " + itemLoteSerasa.getNmPessoa());
			
			BigDecimal vlTotal = findValorSaldo(itemLoteSerasa.getFatura());
			
			itemLoteSerasa.setVlSaldo(vlTotal);
		}
		
		return listItemLoteSerasa;
	}

	/**
	 * Quantidade de itens de um lote enviado ao Serasa
	 * 
	 * @param masterId
	 * @return
	 */
	public Integer getRowCountItemLoteSerasa(Long idLoteSerasa) {
		return getLoteSerasaDAO().getRowCountItemLoteSerasa(idLoteSerasa);
	}

	/**
	 * Salva o mestre (LoteSerasa) e os detalhes (ItemLoteSerasa -> Fatura )
	 * 
	 * @param loteSerasa
	 * @param itemList
	 */
	public LoteSerasa store(final LoteSerasa loteSerasa, final ItemList itemList) {
		if (loteSerasa.getIdLoteSerasa() == null) {
			Long nrLote = parametroGeralService.generateValorParametroSequencial("NR_LOTE_SERASA", false, 1);
			loteSerasa.setNrLote(nrLote);
		}
		return (LoteSerasa)getLoteSerasaDAO().store(loteSerasa, itemList);
	}
	
	public LoteSerasa store(final LoteSerasa loteSerasa) {
		if (loteSerasa.getIdLoteSerasa() == null) {
			Long nrLote = parametroGeralService.generateValorParametroSequencial("NR_LOTE_SERASA", false, 1);
			loteSerasa.setNrLote(nrLote);
		}
		return (LoteSerasa)getLoteSerasaDAO().store(loteSerasa);
	}
	public ItemLoteSerasa store(final ItemLoteSerasa loteSerasa) {
		return (ItemLoteSerasa) itemLoteSeradaDao.store(loteSerasa);
	}

	/**
	 * Consulta uma instancia especifica do bean pelo id.
	 * 
	 */
	
	public LoteSerasa findById(Long id) {
		return getLoteSerasaDAO().findById(id);
	}
	
	/**
	 * Remove a instancia especifica do bean pelo id.
	 * 
	 */
	@Override
	public void removeById(Long id) {
		LoteSerasa loteSerasa = this.findById(id);
		if (loteSerasa.getDhGeracao() != null) {
			throw new BusinessException("LMS-36272");
		}
		getLoteSerasaDAO().removeById(id);
	}
	
	/**
	 * Remove uma lista de instancias pelo id.
	 * 
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		if (!ids.isEmpty()) {
			LoteSerasa loteSerasa = this.findById(ids.get(0));
			if (loteSerasa.getDhGeracao() != null) {
				throw new BusinessException("LMS-36272");
			}
			getLoteSerasaDAO().removeByIds(ids);
		}
	}


	/**
	 * Gera arquivo para envio ao Serasa
	 * 
	 * @param idLoteSerasa
	 */
	public LoteSerasa executeGerarArquivo(Long idLoteSerasa) {
		Map lazyFindList = new HashMap();
		lazyFindList.put("itemLoteSerasa", FetchMode.JOIN);
		getLoteSerasaDAO().initFindListLazyProperties(lazyFindList);
		LoteSerasa loteSerasa = (LoteSerasa) this.findByIdInitLazyProperties(idLoteSerasa, Boolean.TRUE);
		
		propriedadeColuna = new PropriedadeColuna();
		
		StringWriter sw = new StringWriter();
		try {
			Map mapDados = montaDados(loteSerasa);
			
			//Header
			sw.append(gerarHeader((List<PropriedadeColuna>)mapDados.get("mapHeader")));
			
			//Detail 
			for (List<PropriedadeColuna> mapDetail : (List<ArrayList<PropriedadeColuna>>)mapDados.get("mapDetail")) {
				sw.append(gerarDetail(mapDetail));
			}
			
			//Footer
			sw.append(gerarFooter((List<PropriedadeColuna>)mapDados.get("mapFooter")));
			
		} catch (Exception e) {
			log.error(e);
		} finally {
			try {
				if (sw != null) {
					String nomeArquivo = "Lote " + loteSerasa.getNrLote() + ".txt";
					loteSerasa.setArquivoGerado(FormatUtils.mountFileInArrayByteASCII(nomeArquivo, FormatUtils.removeAccents(sw.toString())));
					sw.close();
				}
			} catch (IOException e) {
				log.error(e);
			}
		}
		
		loteSerasa.setDhGeracao(JTDateTimeUtils.getDataHoraAtual());

		DateTime dhGeracao = loteSerasa.getDhGeracao();
		
		List<ItemLoteSerasa> lista = loteSerasa.getItensLoteSerasa();
		List<Fatura> listaFaturas = new ArrayList<Fatura>();
		
		for (ItemLoteSerasa itemLoteSerasa : lista) {
			Fatura fatura = itemLoteSerasa.getFatura();
			if ("N".equals(loteSerasa.getTpLote().getValue())) {
				fatura.setDhNegativacaoSerasa(dhGeracao);	 
			} else {
				fatura.setDhExclusaoSerasa(dhGeracao);
			}
			listaFaturas.add(fatura);
		}
		faturaService.storeAll(listaFaturas);
		
		this.store(loteSerasa);
		
		loteSerasa = findById(idLoteSerasa);
	
		return loteSerasa;
	}
	
	/**
	 * Prepara dados para a montagem do arquivo de envio ao Serasa
	 * 
	 * @param loteSerasa
	 * @return Map
	 */
	public Map montaDados(LoteSerasa loteSerasa) {
		Map mapDados = new HashMap<String, ArrayList<PropriedadeColuna>>();
		
		// Monta Header
		mapDados.put("mapHeader", montaHeader(loteSerasa));
		
		
		// Monta Detail
		int i = 2;
		List<ArrayList<PropriedadeColuna>> mapListDetail = new ArrayList<ArrayList<PropriedadeColuna>>(); // Lista Detail
		
		for (ItemLoteSerasa itemLoteSerasa : loteSerasa.getItensLoteSerasa()) {
			ArrayList<PropriedadeColuna> linhaDetail = new ArrayList<PropriedadeColuna>();
			linhaDetail.addAll(montaDetail(loteSerasa, itemLoteSerasa, i++)); // Linha Detail
			mapListDetail.add(linhaDetail); // Add linha na lista
		}

		mapDados.put("mapDetail", mapListDetail);
		
		// Monta Footer
		mapDados.put("mapFooter", montaFooter(i++));

		return mapDados;
	}
	
	/**
	 * Monta o Header do arquivo
	 * 
	 * @param loteSerasa
	 * @return List
	 */
	public List<PropriedadeColuna> montaHeader(LoteSerasa loteSerasa) {
		// Monta Header
		List<PropriedadeColuna> propriedadeColunaHeader = new ArrayList<PropriedadeColuna>();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		
		//01	001	001	N	Z	Código do registro = “0” (zero)	(1)	“0”
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(1);
		propriedadeColuna.setDado("0");
		propriedadeColuna.setTamanho(1);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//02	002	009	N	Z	Número do CNPJ da instituição informante ajustado à direita e preenchido com zeros à esquerda	(1)	“95591723”
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(2);
		propriedadeColuna.setDado("95591723");
		propriedadeColuna.setTamanho(9);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//03	011	008	N	Z	Data do movimento (AAAAMMDD) – data de geração do arquivo	(1)	DATA ATUAL
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(3);
		propriedadeColuna.setDado(""+dateFormat.format(new Date()));
		propriedadeColuna.setTamanho(8);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//04	019	004	N	Z	Número de DDD do telefone de contato da instituição informante	(1)	“0011”
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(4);
		propriedadeColuna.setDado("0011");
		propriedadeColuna.setTamanho(4);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//05	023	008	N	Z	Número do telefone de contato da instituição informante	(1)	“36223103”
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(5);
		propriedadeColuna.setDado("36223103");
		propriedadeColuna.setTamanho(8);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//06	031	004	N	Z	Número de ramal do telefone de contato da instituição informante	(1)	“3103”
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(6);
		propriedadeColuna.setDado("3103");
		propriedadeColuna.setTamanho(4);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//07	035	070	A	Z	Nome do contato da instituição informante	(1)	“WASHINGTON BERGAMO”
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(7);
		propriedadeColuna.setDado("WASHINGTON BERGAMO");
		propriedadeColuna.setTamanho(70);
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//08	105	015	X	Z	Identificação do arquivo fixo “SERASA-CONVEM04”	(1)	“SERASA-CONVEM04”
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(8);
		propriedadeColuna.setDado("SERASA-CONVEM04");
		propriedadeColuna.setTamanho(15);
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//09	120	006	N	Z	Número da remessa do arquivo seqüencial do 000001, incrementando de 1 a cada novo movimento	(1)	LOTE_SERASA.NR_LOTE
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(9);
		propriedadeColuna.setDado(""+loteSerasa.getNrLote());
		propriedadeColuna.setTamanho(6);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//10	126	001	A	Z	Código de envio de arquivo:	(1) “E” - Entrada “R” - Retorno	“E”
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(10);
		propriedadeColuna.setDado("E");
		propriedadeColuna.setTamanho(1);
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//11	127	004	X	Z	Diferencial de remessa, caso a instituição informante tenha necessidade de enviar mais de uma remessa independentes por deptos., no mesmo dia, de 0000 à 9999. Caso contrário, em branco.	BRANCOS
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(11);
		propriedadeColuna.setTamanho(4);
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//12	131	003	X	Z	Deixar em branco	BRANCOS
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(12);
		propriedadeColuna.setTamanho(3);
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//13	134	008	X	Z	Informar o LOGON a  ser  utilizado  na  contabilização  das  cartas comunicado e anotações.	BRANCOS
		propriedadeColuna = new PropriedadeColuna();
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro("LOGON_CONT_SERASA");
		if (parametroGeral != null) {
			propriedadeColuna.setDado(parametroGeral.getDsConteudo().toString());
		}
		propriedadeColuna.setOrdem(13);
		propriedadeColuna.setTamanho(8);
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//14	142	392	X	Z	Deixar em branco	BRANCOS
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(14);
		propriedadeColuna.setTamanho(392);
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//15	534	060	X	Z	Código de erros – 3 posições ocorrendo 20 vezes. Ausência de códigos indica que foi aceito no movimento de retorno. Na entrada, preencher com brancos.	BRANCOS
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(15);
		propriedadeColuna.setTamanho(60);
		propriedadeColunaHeader.add(propriedadeColuna);
		
		//16	594	007	N	Z	Seqüência do registro no arquivo igual a 0000001 para o header.	(1)	“0000001”
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(16);
		propriedadeColuna.setDado("1");
		propriedadeColuna.setTamanho(7);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaHeader.add(propriedadeColuna);
		
		return propriedadeColunaHeader;
	}
	
	/**
	 * Monta o Detail do arquivo
	 * 
	 * @param loteSerasa
	 * @param itemLoteSerasa
	 * @param seq
	 * @return List
	 */
	public List<PropriedadeColuna> montaDetail(LoteSerasa loteSerasa, ItemLoteSerasa itemLoteSerasa, Integer seq) {
		// Monta Detail
		List<PropriedadeColuna> propriedadeColunaDetail = new ArrayList<PropriedadeColuna>();
		
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(1);
		propriedadeColuna.setDado(colunaDetail1());
		propriedadeColuna.setTamanho(1);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(2);
		propriedadeColuna.setDado(colunaDetail2(loteSerasa));
		propriedadeColuna.setTamanho(1);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(3);
		propriedadeColuna.setDado(colunaDetail3(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(6);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(4);
		propriedadeColuna.setDado(colunaDetail4(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(8);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(5);
		propriedadeColuna.setDado(colunaDetail5(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(8);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(6);
		propriedadeColuna.setDado(colunaDetail6());
		propriedadeColuna.setTamanho(3);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(7);
		propriedadeColuna.setDado(colunaDetail7());
		propriedadeColuna.setTamanho(4);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(8);
		propriedadeColuna.setDado(colunaDetail8(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(1);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(9);
		propriedadeColuna.setDado(colunaDetail9(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(1);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(10);
		propriedadeColuna.setDado(colunaDetail10(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(15);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(11);
		propriedadeColuna.setDado(colunaDetail11(loteSerasa,itemLoteSerasa));
		propriedadeColuna.setTamanho(2);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(12);
		propriedadeColuna.setDado(colunaDetail12());
		propriedadeColuna.setTamanho(1);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(13);
		propriedadeColuna.setDado(colunaDetail13());
		propriedadeColuna.setTamanho(15);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(14);
		propriedadeColuna.setDado(colunaDetail14());
		propriedadeColuna.setTamanho(2);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(15);
		propriedadeColuna.setDado(colunaDetail15());
		propriedadeColuna.setTamanho(1);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(16);
		propriedadeColuna.setDado(colunaDetail16());
		propriedadeColuna.setTamanho(1);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(17);
		propriedadeColuna.setDado(colunaDetail17());
		propriedadeColuna.setTamanho(15);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(18);
		propriedadeColuna.setDado(colunaDetail18());
		propriedadeColuna.setTamanho(2);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(19);
		propriedadeColuna.setDado(colunaDetail19());
		propriedadeColuna.setTamanho(1);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(20);
		propriedadeColuna.setDado(colunaDetail20());
		propriedadeColuna.setTamanho(15);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(21);
		propriedadeColuna.setDado(colunaDetail21());
		propriedadeColuna.setTamanho(2);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(22);
		propriedadeColuna.setDado(colunaDetail22(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(70);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(23);
		propriedadeColuna.setDado(colunaDetail23());
		propriedadeColuna.setTamanho(8);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(24);
		propriedadeColuna.setDado(colunaDetail24());
		propriedadeColuna.setTamanho(70);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(25);
		propriedadeColuna.setDado(colunaDetail25());
		propriedadeColuna.setTamanho(70);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(26);
		propriedadeColuna.setDado(colunaDetail26(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(45);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(27);
		propriedadeColuna.setDado(colunaDetail27(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(20);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(28);
		propriedadeColuna.setDado(colunaDetail28(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(25);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(29);
		propriedadeColuna.setDado(colunaDetail29(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(2);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(30);
		propriedadeColuna.setDado(colunaDetail30(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(8);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(31);
		propriedadeColuna.setDado(colunaDetail31(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(15);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(32);
		propriedadeColuna.setDado(colunaDetail32(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(16);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(33);
		propriedadeColuna.setDado(colunaDetail33(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(9);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(34);
		propriedadeColuna.setDado(colunaDetail34(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(25);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(35);
		propriedadeColuna.setDado(colunaDetail35(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(4);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(36);
		propriedadeColuna.setDado(colunaDetail36(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(9);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(37);
		propriedadeColuna.setDado(colunaDetail37(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(8);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(38);
		propriedadeColuna.setDado(colunaDetail38(itemLoteSerasa.getFatura()));
		propriedadeColuna.setTamanho(15);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(39);
		propriedadeColuna.setDado(colunaDetail39());
		propriedadeColuna.setTamanho(6);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(40);
		propriedadeColuna.setDado(colunaDetail40());
		propriedadeColuna.setTamanho(1);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(41);
		propriedadeColuna.setDado(colunaDetail41());
		propriedadeColuna.setTamanho(2);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(42);
		propriedadeColuna.setDado(colunaDetail42());
		propriedadeColuna.setTamanho(60);
		propriedadeColunaDetail.add(propriedadeColuna);

		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(43);
		propriedadeColuna.setDado(colunaDetail43(seq++));
		propriedadeColuna.setTamanho(7);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaDetail.add(propriedadeColuna);

		return propriedadeColunaDetail;
	}
	
	//43	594	007	N	Z	Seqüência do registro no arquivo	Seqüencial a partir do seqüencial do registro “1” + 1
	private String colunaDetail43(Integer seq) {
		return seq.toString();
	}

	//42	534	060	X	Z	Códigos de erros – 3 posições ocorrendo 20 vezes. Ausência de códigos indica que o registro foi  aceito no movto de retorno. Na  entrada, preencher com brancos.	BRANCOS
	private String colunaDetail42() {
		return "";
	}

	//41	532	002	X	Z	Deixar em branco	BRANCOS
	private String colunaDetail41() {
		return "";
	}

	//40	531	001	X	Z	Indicativo do Tipo de Comunicado ao Devedor: Branco - FAC “B” - Comunicado com Boleto Bancário “C” - Comunicado com Pgto de Contas Públicas “S”: Comunicado com SMS	BRANCOS
	private String colunaDetail40() {
		return "";
	}

	//39	525	006	X	Z	Deixar em branco	BRANCOS
	private String colunaDetail39() {
		return "";
	}

	//38	510	015	N	Z	Valor total do compromisso assumido pelo devedor, com 2 decimais, sem ponto e virgula.	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_FATURA -> BOLETO.VL_TOTAL se não nulo. Caso contrário LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.VL_TOTAL
	private String colunaDetail38(Fatura fatura) {
		return colunaDetail31(fatura);
	}

	//37	502	008	N	Z	Data do compromisso assumido pelo devedor - formato AAAAMMDD.	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_FATURA -> BOLETO.DT_VENCIMENTO se não nulo. Caso contrário LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.DT_VENCIMENTO
	private String colunaDetail37(Fatura fatura) {
		return colunaDetail4(fatura);
	}

	//36	493	009	N	Z	Número do telefone do devedor.	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> TELEFONE_ENDERECO.NR_TELEFONE onde TELEFONE_ENDERECO.TP_USO = ‘FO’
	private String colunaDetail36(Fatura fatura) {
		boolean temFaturaCliente = fatura.getCliente() != null;
		boolean temFaturaClientePessoa = temFaturaCliente && fatura.getCliente().getPessoa() != null;
		boolean temFaturaClientePessoaEnderecoPessoa = temFaturaClientePessoa && fatura.getCliente().getPessoa().getEnderecoPessoa() != null;
		boolean temFaturaClientePessoaEnderecoPessoaTelefoneEnderecos = temFaturaClientePessoaEnderecoPessoa && fatura.getCliente().getPessoa().getEnderecoPessoa().getTelefoneEnderecos() != null;
		
		String tel = "";
		if (temFaturaClientePessoaEnderecoPessoaTelefoneEnderecos) {
			for (TelefoneEndereco telefoneEndereco : fatura.getCliente().getPessoa().getEnderecoPessoa().getTelefoneEnderecos()) {
				if (telefoneEndereco.getTpUso() != null && telefoneEndereco.getTpUso().getValue() != null && "FO".equals(telefoneEndereco.getTpUso().getValue())) {
					if (telefoneEndereco.getNrTelefone() != null) {
						tel = telefoneEndereco.getNrTelefone();
						break;
					}
				}
			}
		}
		tel = tel.replace("-", "");
		return tel;
	}

	//35	489	004	N	Z	DDD do telefone do devedor.	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> TELEFONE_ENDERECO.NR_DDD onde TELEFONE_ENDERECO.TP_USO = ‘FO’
	private String colunaDetail35(Fatura fatura) {
		boolean temFaturaCliente = fatura.getCliente() != null;
		boolean temFaturaClientePessoa = temFaturaCliente && fatura.getCliente().getPessoa() != null;
		boolean temFaturaClientePessoaEnderecoPessoa = temFaturaClientePessoa && fatura.getCliente().getPessoa().getEnderecoPessoa() != null;
		boolean temFaturaClientePessoaEnderecoPessoaTelefoneEnderecos = temFaturaClientePessoaEnderecoPessoa && fatura.getCliente().getPessoa().getEnderecoPessoa().getTelefoneEnderecos() != null;
		
		String dddtel = "";
		if (temFaturaClientePessoaEnderecoPessoaTelefoneEnderecos) {
			for (TelefoneEndereco telefoneEndereco : fatura.getCliente().getPessoa().getEnderecoPessoa().getTelefoneEnderecos()) {
				if (telefoneEndereco.getTpUso() != null && telefoneEndereco.getTpUso().getValue() != null && "FO".equals(telefoneEndereco.getTpUso().getValue())) {
					if (telefoneEndereco.getNrDdd() != null) {
						dddtel = telefoneEndereco.getNrDdd();
						break;
					}
				}
			}
		}
		return dddtel;
	}

	//34	464	025	X	Z	Complemento do endereço do devedor – usar somente quando o campo de seq. nr. 26 (endereço completo) não for suficiente.	Copiar 25 posições a partir do 46º caracter de: LOTE_SERASA.ID_LOTE_SERASA -> Valor para pt_BR de ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> ENDERECO_PESSOA.ID_TIPO_LOGRADOURO -> TIPO_LOGRADOURO.DS_TIPO_LOGRADOURO_I + “ ” + ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> ENDERECO_PESSOA.DS_ENDERECO + “, ” + ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> ENDERECO_PESSOA.NR_ENDERECO + Se ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> ENDERECO_PESSOA. DS_COMPLEMENTO <> NULL Então “/” + ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> ENDERECO_PESSOA. DS_COMPLEMENTO Fim Se
	private String colunaDetail34(Fatura fatura) {
		
		String endereco = colunaDetail26(fatura);
		if (endereco.length() > 45) {
			return endereco.substring(45, endereco.length()).toString();
		} else {
			return "";
		}
	}

	//33	455	009	N	Z	Nosso número	Copiar os últimos 9 caracteres de LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_FATURA -> BOLETO.NR_BOLETO
	private String colunaDetail33(Fatura fatura) {
		boolean temFaturaBoleto = fatura.getBoleto() != null;
		boolean temFaturaBoletoNrBoleto = temFaturaBoleto && fatura.getBoleto().getNrBoleto() != null;
		
		String nossoNumero = "";
		if (temFaturaBoletoNrBoleto) {
			nossoNumero = fatura.getBoleto().getNrBoleto().toString();
		}
		
		return getLastCharacters(nossoNumero, 9);
	}
	
	//32	439	016	X	Z	O número do contrato deve ser  único para o principal e seus coobrigados	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_FILIAL -> FILIAL.SG_FILIAL + “ ” + LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.NR_FATURA
	private String colunaDetail32(Fatura fatura) {
		boolean temFaturaFilialByIdFilial = fatura.getFilialByIdFilial() != null;
		boolean temFaturaNrFatura = fatura.getNrFatura() != null;
		
		String nrContrato = "";
		if (temFaturaFilialByIdFilial) {
			nrContrato = filialService.findSgFilialLegadoByIdFilial(fatura.getFilialByIdFilial().getIdFilial());
		}
		if (temFaturaNrFatura) {
			nrContrato = nrContrato +" "+ fatura.getNrFatura().toString();
		}
		return nrContrato;
	}

	//31	424	015	N	Z	Valor com 2 decimais, alinhar a direita com zeros a esquerda	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_FATURA -> BOLETO.VL_TOTAL se não nulo. Caso contrário LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.VL_TOTAL
	private String colunaDetail31(Fatura fatura) {
		BigDecimal vlTotal = findValorSaldo(fatura);
		String valorTotal = "";

		valorTotal = FormatUtils.formatDecimal("#000000000000.00", vlTotal, true);

		return valorTotal.replace(".", "").replace(",", "");
	}

	//30	416	008	N	Z	Código de endereçamento postal completo	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> ENDERECO_PESSOA.NR_CEP
	private String colunaDetail30(Fatura fatura) {
		boolean temFaturaCliente = fatura.getCliente() != null;
		boolean temFaturaClientePessoa = temFaturaCliente && fatura.getCliente().getPessoa() != null;
		boolean temFaturaClientePessoaEnderecoPessoa = temFaturaClientePessoa && fatura.getCliente().getPessoa().getEnderecoPessoa() != null;
		boolean temFaturaClientePessoaEnderecoPessoaNrCep = temFaturaClientePessoaEnderecoPessoa && fatura.getCliente().getPessoa().getEnderecoPessoa().getNrCep() != null;
		
		String cep = "";
		if (temFaturaClientePessoaEnderecoPessoaNrCep) {
			cep = fatura.getCliente().getPessoa().getEnderecoPessoa().getNrCep();
		}
		return cep;
	}

	//29	414	002	A	Z	Sigla Unidade Federativa	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> ENDERECO_PESSOA.ID_MUNICIPIO -> MUNICIPIO.ID_UNIDADE_FEDERATIVA -> UNIDADE_FEDERATIVA.SG_UNIDADE_FEDERATIVA
	private String colunaDetail29(Fatura fatura) {
		boolean temFaturaCliente = fatura.getCliente() != null;
		boolean temFaturaClientePessoa = temFaturaCliente && fatura.getCliente().getPessoa() != null;
		boolean temFaturaClientePessoaEnderecoPessoa = temFaturaClientePessoa && fatura.getCliente().getPessoa().getEnderecoPessoa() != null;
		boolean temFaturaClientePessoaEnderecoPessoaMunicipio = temFaturaClientePessoaEnderecoPessoa && fatura.getCliente().getPessoa().getEnderecoPessoa().getMunicipio() != null;
		boolean temFaturaClientePessoaEnderecoPessoaMunicipioUnidadeFederativa = temFaturaClientePessoaEnderecoPessoaMunicipio && fatura.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa() != null;
		boolean temFaturaClientePessoaEnderecoPessoaMunicipioUnidadeFederativaSgUnidadeFederativa = temFaturaClientePessoaEnderecoPessoaMunicipioUnidadeFederativa && fatura.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa() != null;
		
		String sigla = "";
		if (temFaturaClientePessoaEnderecoPessoaMunicipioUnidadeFederativaSgUnidadeFederativa) {
			sigla = fatura.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();
		}
		return sigla;
	}

	//28	389	025	X	Z	Município correspondente	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> ENDERECO_PESSOA.ID_MUNICIPIO -> MUNICIPIO.NM_MUNICIPIO
	private String colunaDetail28(Fatura fatura) {
		boolean temFaturaCliente = fatura.getCliente() != null;
		boolean temFaturaClientePessoa = temFaturaCliente && fatura.getCliente().getPessoa() != null;
		boolean temFaturaClientePessoaEnderecoPessoa = temFaturaClientePessoa && fatura.getCliente().getPessoa().getEnderecoPessoa() != null;
		boolean temFaturaClientePessoaEnderecoPessoaMunicipio = temFaturaClientePessoaEnderecoPessoa && fatura.getCliente().getPessoa().getEnderecoPessoa().getMunicipio() != null;
		boolean temFaturaClientePessoaEnderecoPessoaMunicipioNmMunicipio = temFaturaClientePessoaEnderecoPessoaMunicipio && fatura.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio() != null;
		
		String municipio = "";
		if (temFaturaClientePessoaEnderecoPessoaMunicipioNmMunicipio) {
			municipio = fatura.getCliente().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio();
		}
		return municipio;
	}

	//27	369	020	X	Z	Bairro correspondente	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> ENDERECO_PESSOA.DS_BAIRRO
	private String colunaDetail27(Fatura fatura) {
		boolean temFaturaCliente = fatura.getCliente() != null;
		boolean temFaturaClientePessoa = temFaturaCliente && fatura.getCliente().getPessoa() != null;
		boolean temFaturaClientePessoaEnderecoPessoa = temFaturaClientePessoa && fatura.getCliente().getPessoa().getEnderecoPessoa() != null;
		boolean temFaturaClientePessoaEnderecoPessoaDsBairro = temFaturaClientePessoaEnderecoPessoa && fatura.getCliente().getPessoa().getEnderecoPessoa().getDsBairro() != null;
		
		String bairro = "";
		if (temFaturaClientePessoaEnderecoPessoaDsBairro) {
			bairro = fatura.getCliente().getPessoa().getEnderecoPessoa().getDsBairro();
		}
		return bairro;
	}

	//26	324	045	X	Z	Endereço completo (rua, Av., nº etc.)	Copiar os primeiros 45 caracteres de: LOTE_SERASA.ID_LOTE_SERASA -> Valor para pt_BR de ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> ENDERECO_PESSOA.ID_TIPO_LOGRADOURO -> TIPO_LOGRADOURO.DS_TIPO_LOGRADOURO_I + “ ” + ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> ENDERECO_PESSOA.DS_ENDERECO + “, ” + ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> ENDERECO_PESSOA.NR_ENDERECO + Se ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> ENDERECO_PESSOA. DS_COMPLEMENTO <> NULL Então “/” + ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.ID_ENDERECO_PESSOA -> ENDERECO_PESSOA. DS_COMPLEMENTO Fim Se
	private String colunaDetail26(Fatura fatura) {
		boolean temFaturaCliente = fatura.getCliente() != null;
		boolean temFaturaClientePessoa = temFaturaCliente && fatura.getCliente().getPessoa() != null;
		boolean temFaturaClientePessoaEnderecoPessoa = temFaturaClientePessoa && fatura.getCliente().getPessoa().getEnderecoPessoa() != null;
		boolean temFaturaClientePessoaEnderecoPessoaDsEndereco = temFaturaClientePessoaEnderecoPessoa && fatura.getCliente().getPessoa().getEnderecoPessoa().getDsEndereco() != null;
		boolean temFaturaClientePessoaEnderecoPessoaNrEndereco = temFaturaClientePessoaEnderecoPessoa && fatura.getCliente().getPessoa().getEnderecoPessoa().getNrEndereco() != null;
		boolean temFaturaClientePessoaEnderecoPessoaDsComplemento = temFaturaClientePessoaEnderecoPessoa && fatura.getCliente().getPessoa().getEnderecoPessoa().getDsComplemento() != null;
		boolean temFaturaClientePessoaEnderecoPessoaTipoLogradouro = temFaturaClientePessoaEnderecoPessoa && fatura.getCliente().getPessoa().getEnderecoPessoa().getTipoLogradouro() != null;
		boolean temFaturaClientePessoaEnderecoPessoaTipoLogradouroDsTipoLogradouro = temFaturaClientePessoaEnderecoPessoaTipoLogradouro && fatura.getCliente().getPessoa().getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro() != null;
		boolean temFaturaClientePessoaEnderecoPessoaTipoLogradouroDsTipoLogradouroValue = temFaturaClientePessoaEnderecoPessoaTipoLogradouroDsTipoLogradouro && fatura.getCliente().getPessoa().getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro().getValue() != null;
		
		StringBuilder rua = new StringBuilder();
		rua.append("");
		if (temFaturaClientePessoaEnderecoPessoaTipoLogradouroDsTipoLogradouroValue) {
			rua.append(fatura.getCliente().getPessoa().getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro().getValue()+" ");
		}
		if (temFaturaClientePessoaEnderecoPessoaDsEndereco) {
			rua.append(fatura.getCliente().getPessoa().getEnderecoPessoa().getDsEndereco()+", ");
		}
		if (temFaturaClientePessoaEnderecoPessoaNrEndereco) {
			rua.append(fatura.getCliente().getPessoa().getEnderecoPessoa().getNrEndereco());
		}
		if (temFaturaClientePessoaEnderecoPessoaDsComplemento) {
			rua.append("/"+fatura.getCliente().getPessoa().getEnderecoPessoa().getDsComplemento());
		}
		
		return rua.toString();
	}

	//25	254	070	X	Z	Nome da mãe. Caso não possua, brancos.	BRANCOS
	private String colunaDetail25() {
		return "";
	}

	//24	184	070	X	Z	Nome do pai. Caso não possua, brancos.	BRANCOS
	private String colunaDetail24() {
		return "";
	}

	//23	176	008	N	Z	A data do nascimento (AAAAMMDD) deve ser superior a 18 anos (só para pessoa física). Se não, colocar 00000000.	“00000000”
	private String colunaDetail23() {
		return "00000000";
	}

	//22	106	070	X	Z	Nome do devedor	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.NM_PESSOA
	private String colunaDetail22(Fatura fatura) {
		boolean temFaturaCliente = fatura.getCliente() != null;
		boolean temFaturaClientePessoa = temFaturaCliente && fatura.getCliente().getPessoa() != null;
		boolean temFaturaClientePessoaNmPessoa = temFaturaClientePessoa && fatura.getCliente().getPessoa().getNmPessoa() != null;
		
		String nomeDevedor = "";
		if (temFaturaClientePessoaNmPessoa) {
			nomeDevedor = fatura.getCliente().getPessoa().getNmPessoa();
		}
		return nomeDevedor;
	}

	//21	104	002	A	Z	Unidade da Federação, quando documento for RG, se não, espaços.	BRANCOS
	private String colunaDetail21() {
		return "";
	}

	//20	089	015	X	Z	Segundo documento do coobrigado, se houver. Se não, espaços.	BRANCOS
	private String colunaDetail20() {
		return "";
	}

	//19	088	001	X	Z	Tipo do segundo documento do coobrigado: 3 – RG, se houver. Se não, espaços (só para pessoa física )	BRANCOS
	private String colunaDetail19() {
		return "";
	}

	//18	086	002	X	Z	Espaços	BRANCOS
	private String colunaDetail18() {
		return "";
	}

	//17	071	015	X	Z	Primeiro documento do coobrigado: CPF completo: base + dígito ou CNPJ completo:  base + filial + dígito Ajustado à direita e preenchido com zeros à esquerda.	(4)(6)	BRANCOS
	private String colunaDetail17() {
		return "";
	}

	//16	070	001	X	Z	Tipo do primeiro documento do coobrigado: 1 – CNPJ 2 – CPF	(4)(6)	BRANCOS
	private String colunaDetail16() {
		return "";
	}

	//15	069	001	A	Z	Tipo de pessoa do coobrigado: F – Física J – Jurídica	(4)(6)	BRANCOS
	private String colunaDetail15() {
		return "";
	}

	//14	067	002	A	Z	UF quando documento for RG, se não, espaços.	BRANCOS
	private String colunaDetail14() {
		return "";
	}

	//13	052	015	X	Z	Segundo documento do principal, se houver. Se não, espaços.	BRANCOS
	private String colunaDetail13() {
		return "";
	}

	//12	051	001	X	Z	Tipo do segundo documento do principal: 3 – RG, se houver. Se não, espaços (só para pessoa física).	BRANCOS
	private String colunaDetail12() {
		return "";
	}

	//11	049	002	X	Z	Motivo da baixa	Se LOTE_SERASA.TP_LOTE = ‘N’ Então BRANCOS Senão “01” Fim Se
	private String colunaDetail11(LoteSerasa loteSerasa,ItemLoteSerasa item) {
		boolean temTpLote = loteSerasa.getTpLote() != null;
		boolean temTpLoteValue = temTpLote && loteSerasa.getTpLote().getValue() != null;
		
		String motivoBaixa = "";
		if (temTpLoteValue && !"N".equals(loteSerasa.getTpLote().getValue())) {
			if ( item.getMotivoBaixaSerasa() != null && !item.getMotivoBaixaSerasa().getCdMotivoBaixaSerasa().isEmpty() ){
				motivoBaixa = item.getMotivoBaixaSerasa().getCdMotivoBaixaSerasa();
			}else{
				motivoBaixa = "01";
			}
		}
		return motivoBaixa;
	}

	//10	034	015	N	Z	Primeiro documento do principal: CPF completo: base + dígito ou CNPJ completo: base + filial + dígito Ajustado à direita e preenchido com zeros à esquerda	(2)(3)(4)(5)	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.NR_IDENTIFICACAO
	private String colunaDetail10(Fatura fatura) {
		boolean temFatura = fatura != null;
		boolean temFaturaCliente = temFatura && fatura.getCliente() != null;
		boolean temFaturaClientePessoa = temFaturaCliente && fatura.getCliente().getPessoa() != null;
		boolean temFaturaClientePessoaNrIdentificacao = temFaturaClientePessoa && fatura.getCliente().getPessoa().getNrIdentificacao() != null;
		
		String cnpjContratante = "";
		if (temFaturaClientePessoaNrIdentificacao) {
			cnpjContratante = fatura.getCliente().getPessoa().getNrIdentificacao();
		}
		
		return cnpjContratante;
	}

	//09	033	001	X	Z	Tipo do primeiro docto. do principal: 1 – CNPJ ou 2 – CPF	Se LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.TP_IDENTIFICACAO = ‘CNPJ’ Então “1” Senão “2” Fim Se
	private String colunaDetail9(Fatura fatura) {
		boolean temFaturaCliente = fatura.getCliente() != null;
		boolean temFaturaClientePessoa = temFaturaCliente && fatura.getCliente().getPessoa() != null;
		boolean temFaturaClientePessoaTpIdentificacao = temFaturaClientePessoa && fatura.getCliente().getPessoa().getTpIdentificacao() != null;
		boolean temFaturaClientePessoaTpIdentificacaoValue = temFaturaClientePessoaTpIdentificacao && fatura.getCliente().getPessoa().getTpIdentificacao().getValue() != null;
		
		String tipoPrimeiroDoc = "";
		if (temFaturaClientePessoaTpIdentificacaoValue) {
			if ("CNPJ".equals(fatura.getCliente().getPessoa().getTpIdentificacao().getValue())) {
				tipoPrimeiroDoc = "1";
			} else {
				tipoPrimeiroDoc = "2";
			}
		}
		return tipoPrimeiroDoc;
	}

	//08	032	001	A	Z	Tipo de pessoa do principal: F – Física ou J – Jurídica	(3)(4)(5)	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_CLIENTE -> PESSOA.TP_PESSOA
	private String colunaDetail8(Fatura fatura) {
		boolean temFaturaCliente = fatura.getCliente() != null;
		boolean temFaturaClientePessoa = temFaturaCliente && fatura.getCliente().getPessoa() != null;
		boolean temFaturaClientePessoaTpPessoa = temFaturaClientePessoa && fatura.getCliente().getPessoa().getTpPessoa() != null;
		boolean temFaturaClientePessoaTpPessoaValue = temFaturaClientePessoaTpPessoa && fatura.getCliente().getPessoa().getTpPessoa().getValue() != null;
		
		String tipoPessoa = "";
		if (temFaturaClientePessoaTpPessoaValue) {
			tipoPessoa = fatura.getCliente().getPessoa().getTpPessoa().getValue();
		}
		return tipoPessoa;
	}

	//07	028	004	A	Z	Código da praça Embratel (que originou a dívida )	BRANCOS
	private String colunaDetail7() {
		return "";
	}

	//06	025	003	X	Z	Código de natureza da operação	“DP”
	private String colunaDetail6() {
		return "DP";
	}

	//05	017	008	N	Z	Data do término do contrato – formato “AAAAMMDD”. Caso não possua, repetir a data da ocorrência	(1)	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_FATURA -> BOLETO.DT_VENCIMENTO se não nulo. Caso contrário LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.DT_VENCIMENTO
	private String colunaDetail5(Fatura fatura) {
		String data = colunaDetail4(fatura);
		Date tempDate = null;
		try {
			tempDate = new SimpleDateFormat("yyyyMMdd").parse(data);
		} catch (ParseException e) {
			log.error(e);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(tempDate);
		cal.add(Calendar.MONTH, 11);
		cal.add(Calendar.YEAR, 4);
		
		data = new SimpleDateFormat("yyyyMMdd").format(cal.getTime()).toString();
		return data;
	}

	//04	009	008	N	Z	Data da ocorrência (AAAAMMDD) – data do vencimento da dívida, não superior a 4 anos e 11 meses , e inferior à 4 dias da data do movimento	LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_FATURA -> BOLETO.DT_VENCIMENTO se não nulo. Caso contrário LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.DT_VENCIMENTO
	private static String colunaDetail4(Fatura fatura) {
		boolean temFaturaBoleto = fatura.getBoleto() != null;
		boolean temFaturaBoletoDtVencimento = temFaturaBoleto && fatura.getBoleto().getDtVencimento() != null;
		boolean temFaturaDtVencimento =  fatura.getDtVencimento() != null;
		
		String dataOcorrencia = "";
		if (temFaturaBoletoDtVencimento) {
			dataOcorrencia = fatura.getBoleto().getDtVencimento().toString("yyyyMMdd");
		} else if (temFaturaDtVencimento) {
			dataOcorrencia = fatura.getDtVencimento().toString("yyyyMMdd");
		}
		return dataOcorrencia;
	}

	//03	003	006	N	Z	Filial e dígito do CNPJ da contratante	Copiar 6 posições a partir do 9º caracter de LOTE_SERASA.ID_LOTE_SERASA -> ITEM_LOTE_SERASA.ID_FATURA -> FATURA.ID_FILIAL -> PESSOA.NR_IDENTIFICACAO
	private String colunaDetail3(Fatura fatura) {
		boolean temFatura = fatura != null;
		boolean temFaturaFilialByIdFilial = temFatura && fatura.getFilialByIdFilial() != null;
		boolean temFaturaFilialByIdFilialPessoa = temFaturaFilialByIdFilial && fatura.getFilialByIdFilial().getPessoa() != null;
		boolean temFaturaFilialByIdFilialPessoaNrIdentificacao = temFaturaFilialByIdFilialPessoa && fatura.getFilialByIdFilial().getPessoa().getNrIdentificacao() != null;
		
		String cnpjContratante = "";
		if (temFaturaFilialByIdFilialPessoaNrIdentificacao) {
			cnpjContratante = fatura.getFilialByIdFilial().getPessoa().getNrIdentificacao();
		}
		if (cnpjContratante.length() > 9) {
			cnpjContratante = cnpjContratante.substring(8);
		}
		return cnpjContratante;
	}

	//02	002	001	A	Z	Código da Operação I – inclusão E – exclusão	(1)(2)	Se LOTE_SERASA.TP_LOTE = ‘N’ Então “I” Senão “E” Fim Se
	private String colunaDetail2(LoteSerasa loteSerasa) {
		boolean temTpLote = loteSerasa.getTpLote() != null;
		boolean temTpLoteValue = temTpLote && loteSerasa.getTpLote().getValue() != null;
		
		String codigoOperacao = "";
		if (temTpLoteValue && "N".equals(loteSerasa.getTpLote().getValue())) {
			codigoOperacao = "I";
		} else {
			codigoOperacao = "E";
		}
		
		return codigoOperacao;
	}

	//01	001	001	N	Z	Código do registro = “1” – detalhes	“1”
	private String colunaDetail1() {
		return "1";
	}
	
	/**
	 * Monta o Footer do arquivo
	 * 
	 * @param seq
	 * @return List
	 */
	public List<PropriedadeColuna> montaFooter(Integer seq) {
		
		List<PropriedadeColuna> propriedadeColunaFooter = new ArrayList<PropriedadeColuna>();
		
		//01	001	001	N	Z	Código do registro = “9” (nove)	9
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(1);
		propriedadeColuna.setDado("9");
		propriedadeColuna.setTamanho(1);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaFooter.add(propriedadeColuna);
		
		//02	002	592	X	Z	Deixar em branco	BRANCOS
		propriedadeColuna = new PropriedadeColuna(); 
		propriedadeColuna.setOrdem(2);
		propriedadeColuna.setTamanho(592);
		propriedadeColunaFooter.add(propriedadeColuna);
		
		//03	594	007	N	Z	Seqüência do registro no arquivo	Seqüencial a partir do último seqüencial do registro “2” + 1
		propriedadeColuna = new PropriedadeColuna();
		propriedadeColuna.setOrdem(3);
		propriedadeColuna.setDado(""+seq);
		propriedadeColuna.setTamanho(7);
		propriedadeColuna.setTipo("0");
		propriedadeColuna.setAlinhamento("left");
		propriedadeColunaFooter.add(propriedadeColuna);
		
		return propriedadeColunaFooter;
	}
	
	/**
	 * Gera o Header do arquivo
	 * 
	 * @param mapHeader
	 * @return String
	 */
	public String gerarHeader(List<PropriedadeColuna> mapHeader){
		StringBuilder header = new StringBuilder();
		header.append(gerarLinha(mapHeader));
		header.append("\r\n");
		return header.toString();
	}
	
	/**
	 * Gera o Detail do arquivo
	 * 
	 * @param mapDetail
	 * @return String
	 */
	public String gerarDetail(List<PropriedadeColuna> mapDetail){
		StringBuilder detail = new StringBuilder();
		detail.append(gerarLinha(mapDetail));
		detail.append("\r\n");
		return detail.toString();
	}
	
	/**
	 * Gera o Footer do arquivo
	 * 
	 * @param mapFooter
	 * @return String
	 */
	public String gerarFooter(List<PropriedadeColuna> mapFooter){
		StringBuilder footer = new StringBuilder();
		footer.append(gerarLinha(mapFooter));
		footer.append("\r\n");
		return footer.toString();
	}
	
	/**
	 * Gera linha do arquivo 
	 * 
	 * @param listaColunas
	 * @return StringBuilder
	 */
	public StringBuilder gerarLinha(List<PropriedadeColuna> listaColunas) {
		StringBuilder linha = new StringBuilder();
		
		for (PropriedadeColuna propriedadeColuna : listaColunas) {
			if (propriedadeColuna.getDado().length() > propriedadeColuna.getTamanho()) {
				propriedadeColuna.setDado(propriedadeColuna.getDado().substring(0, propriedadeColuna.getTamanho()));
			}
			if ("left".equals(propriedadeColuna.getAlinhamento())) {
				linha.append(StringUtils.leftPad(propriedadeColuna.getDado(), propriedadeColuna.getTamanho(), propriedadeColuna.getTipo()));
			} else {
				linha.append(StringUtils.rightPad(propriedadeColuna.getDado(), propriedadeColuna.getTamanho(), propriedadeColuna.getTipo()));
			}
		}
		
		return linha;
	}
	
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}
	
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	/**
	 * Classe com propriedades para geração arquivo
	 *
	 */
	class PropriedadeColuna {
		private Integer ordem;
		private String dado = "";
		private Integer tamanho = 0;
		private String tipo = " ";
		private String alinhamento = "right";
		
		public Integer getOrdem() {
			return ordem;
		}
		public void setOrdem(Integer ordem) {
			this.ordem = ordem;
		}
		public String getDado() {
			return dado;
		}
		public void setDado(String dado) {
			this.dado = dado;
		}
		public Integer getTamanho() {
			return tamanho;
		}
		public void setTamanho(Integer tamanho) {
			this.tamanho = tamanho;
		}
		public String getTipo() {
			return tipo;
		}
		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
		public String getAlinhamento() {
			return alinhamento;
		}
		public void setAlinhamento(String alinhamento) {
			this.alinhamento = alinhamento;
		}
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria,FindDefinition findDef) {
		return getLoteSerasaDAO().findPaginated(criteria, findDef);
	}
	

	public Integer getRowCount(TypedFlatMap criteria) {
		return getLoteSerasaDAO().getRowCount(criteria);
	}	
	

	/**
	 * Executa a Importacao arquivo CSV e retorna uma lista de faturas para 
	 * envio ao Serasa 
	 * 
	 * @param parameters
	 * @return Map
	 */
	public Map executeImportacao(TypedFlatMap parameters) {
		String tpLote = parameters.getString("tpLote");
		
		Scanner sc = null;
		Map map = null;
		try {
			byte[] tmpArquivo = Base64Util.decode(MapUtils.getString(parameters, "arquivoCSV"));
			//remove nome do arquivo
			byte[] arquivo = Arrays.copyOfRange(tmpArquivo, 1024, tmpArquivo.length);
			InputStream is = new ByteArrayInputStream(arquivo);
			sc = new Scanner(is).useDelimiter("[;\r\n]+");
			map = this.processaArquivoCSV(sc,tpLote);
		} catch	(Exception e) {
			log.error(e);
			throw new BusinessException("LMS-36274");
		} finally {
			try {
				if (sc != null) {
					sc.close();
				}
			} catch (Exception e) {	
				
			}
		}
		
		return map;
	}

	/**
	 * Processa o arquivo csv e gera o lote para as faturas encontradas
	 * 
	 * @param sc
	 * @param tpLote 
	 * @return Map
	 */
	private Map processaArquivoCSV(Scanner sc, String tpLote) {
		
		Filial filial ;
		Fatura fatura ;
		
		final Long idEmpresa = ID_EMPRESA;
		
		List<BusinessException> exceptions = new ArrayList<BusinessException>();
		List<Long> listaFaturas = new ArrayList<Long>();
		
		TypedFlatMap result = new TypedFlatMap();
		
		while (sc.hasNext()) {
			//sc.next();
			String siglaFilial = sc.next();
			String nrFatura = sc.next();
			
			if (StringUtils.isBlank(siglaFilial) || StringUtils.isBlank(nrFatura) || !StringUtils.isNumeric(nrFatura)) {
				BusinessException be = new BusinessException("LMS-36274");
				throw be;
			} else {
				if (siglaFilial.length() == 2) {
					filial = getFilialService().findFilialBySgFilialLegado(siglaFilial);
				} else {
					filial = getFilialService().findFilial(idEmpresa, siglaFilial);
				}
				
				if (filial != null) {
					fatura = getFaturaService().findFaturaByNrFaturaAndIdFilial(Long.valueOf(nrFatura), filial.getIdFilial());
					if (fatura != null) {
						if (validaFatura(fatura, tpLote)) {
							listaFaturas.add(fatura.getIdFatura());
						} else {
							exceptions.add(new BusinessException("LMS-36277", new Object[] { siglaFilial,nrFatura }));
						}
					} else {
						exceptions.add(new BusinessException("LMS-36276",new Object[] { siglaFilial, nrFatura }));
					}
				} else {
					exceptions.add(new BusinessException("LMS-36275",new Object[] { siglaFilial }));
				}
			}	
		}
	
		result.put("listaErros", exceptions);
		result.put("listaFaturas", listaFaturas);
		
		return result;
	}

	/**
	 * Valida se fatura pode ser enviada
	 * 
	 * @param fatura
	 * @param tpLote
	 * @return boolean
	 */
	public boolean validaFatura(Fatura fatura, String tpLote) {
		Boolean blDataVencimentoInValida = JTDateTimeUtils.comparaData(fatura.getDtVencimento(), JTDateTimeUtils.getDataAtual()) > 0;
		Boolean blSituacaoInValida = "LI".equals(fatura.getTpSituacaoFatura().getValue()) || "CA".equals(fatura.getTpSituacaoFatura().getValue()) ;
		
		if ("N".equals(tpLote)) {
			Boolean blQuestionamentoFaturaInValida = questionamentoFaturasService.validateFatura(fatura);
			if (blSituacaoInValida || blDataVencimentoInValida || blQuestionamentoFaturaInValida) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Calcula o valor Saldo da fatura
	 * 
	 * @param fatura
	 * @return BigDecimal - VlrSaldo
	 */
	public BigDecimal findValorSaldo(Fatura fatura) {
		BigDecimal vlPagamento = relacaoPagtoParcialService.findByIdFaturaTotalvlPagamento(fatura.getIdFatura());
		BigDecimal vlTotal = fatura.getVlTotal();
		if (vlPagamento != null) {
			vlTotal = vlTotal.subtract(vlPagamento);
		}
		BigDecimal vlDesconto = fatura.getVlDesconto();
		if (vlDesconto != null) {
			vlTotal = vlTotal.subtract(vlDesconto);
		}
		return vlTotal;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public FaturaService getFaturaService() {
		return faturaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public QuestionamentoFaturasService getQuestionamentoFaturasService() {
		return questionamentoFaturasService;
	}

	public void setQuestionamentoFaturasService(
			QuestionamentoFaturasService questionamentoFaturasService) {
		this.questionamentoFaturasService = questionamentoFaturasService;
	}

	public RelacaoPagtoParcialService getRelacaoPagtoParcialService() {
		return relacaoPagtoParcialService;
	}
	
	public void setRelacaoPagtoParcialService(
			RelacaoPagtoParcialService relacaoPagtoParcialService) {
		this.relacaoPagtoParcialService = relacaoPagtoParcialService;
	}
	
	public String getLastCharacters(String inputString,int i){
		int length = inputString.length();
		if(length <= i){
			return inputString;
		}
		int startIndex = length-i;
		return inputString.substring(startIndex);
	}
	
	public void setItemLoteSeradaDao(ItemLoteSerasaDAO itemLoteSeradaDao) {
		this.itemLoteSeradaDao = itemLoteSeradaDao;
	}
}
