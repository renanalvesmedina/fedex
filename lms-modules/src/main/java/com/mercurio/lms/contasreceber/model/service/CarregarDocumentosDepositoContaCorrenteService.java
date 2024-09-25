package com.mercurio.lms.contasreceber.model.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.DepositoCcorrente;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemDepositoCcorrente;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.carregarDocumentosDepositoContaCorrenteService"
 */
public class CarregarDocumentosDepositoContaCorrenteService {
	
	private static final int CABECALHO = 0;
	private static final int DETALHE   = 1;
	private static final int RODAPE    = 99;
	
	private static final int CTRC      = 0;
	private static final int CRT       = 1;
	private static final int NFS       = 2;
	private static final int NFT       = 3;
	private static final int NDN       = 4;
	private static final int FA		= 5;
	private static final int CTE		= 6;
	private static final int NTE		= 7;
	private static final int NSE		= 8;
	private static final int OUTRO     = -1;
	
	private ClienteService clienteService;
	private CedenteService cedenteService;
	private DepositoCcorrenteService depositoCcorrenteService;
	private FilialService filialService;
	private ConhecimentoService conhecimentoService;
	private CtoInternacionalService ctoInternacionalService;
	private NotaFiscalServicoService notaFiscalServicoService;
	private NotaDebitoNacionalService notaDebitoNacionalService;
	private FaturaService faturaService;
	private ItemDepositoCcorrenteService itemDepositoCcorrenteService;
	private DevedorDocServFatService devedorDocServFatService;
	
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setCtoInternacionalService(
			CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setNotaDebitoNacionalService(
			NotaDebitoNacionalService notaDebitoNacionalService) {
		this.notaDebitoNacionalService = notaDebitoNacionalService;
	}

	public void setNotaFiscalServicoService(
			NotaFiscalServicoService notaFiscalServicoService) {
		this.notaFiscalServicoService = notaFiscalServicoService;
	}
	
	/**
	 * Executa a importação do arquivo 
	 * @param tfm Dados da tela e conteudo do arquivo a ser importado
	 * @return
	 */
	public java.io.Serializable executeImportDocumentosDepositadosContaCorrente(TypedFlatMap tfm) {
		
		try {
			
			ByteArrayInputStream ba = new ByteArrayInputStream(Base64Util.decode(tfm.getString("arquivo")));
			
			BufferedReader in = new BufferedReader(new InputStreamReader(ba));
			String linha = null;
			int tipo = 0;			
			boolean primeiraLinha = true;
			BigDecimal vlDeposito = BigDecimal.ZERO;
			BigDecimal sumVlCreditoDocumento = BigDecimal.ZERO;
			int qtdDetalhe = 0;
			int qtdRegistros = 0;
			
			Cliente clienteValido = clienteService.findClienteByNrIdentificacaoForDepositoContaCorrente(FormatUtils.filterNumber(tfm.getString("nrIdentificacao")));
			
			Long idDepositoCcorrente = null;
			
			while (in.ready()){
				
				linha = in.readLine();
				
				if( primeiraLinha ){
					linha = linha.substring(1024);//FormatUtils.quebraLinha(linha,1024,0,false,0);
					
					//verifica se a primeira linha de dados contem no minimo 200 caracters
					if( linha.length() < 69 ){
						throw new BusinessException("LMS-36193");
					}
					
					primeiraLinha = false;
				}
				
				tipo = Integer.parseInt(FormatUtils.quebraLinha(linha,1,2,false,0));
				
				switch (tipo) {
					
					// REGRA 2.1 == "00”
					case CABECALHO : List retorno = verificaCabecalho(linha, clienteValido, qtdRegistros, qtdDetalhe);
									 vlDeposito = (BigDecimal) retorno.get(0);
									 idDepositoCcorrente = (Long) retorno.get(1);
									 qtdRegistros++;
									 break;		
						
                    // REGRA 2.1 == "01”
					case DETALHE   : sumVlCreditoDocumento = sumVlCreditoDocumento.add(verificaDetalhe(linha,idDepositoCcorrente, clienteValido, qtdRegistros, qtdDetalhe));
								     qtdDetalhe++;
								     qtdRegistros++;
								     break;
					
                    //REGRA 2.1 == "99”
					case RODAPE    : verificaRodape(linha, qtdRegistros, qtdDetalhe, sumVlCreditoDocumento, vlDeposito);
									 qtdRegistros++;
								     break;
	
					default		   : break;
				}
				
			}
			
			in.close();
			
		} catch ( IOException e) {
			throw new BusinessException("LMS-00060");
		}
		
		return null;
	}

	/**
	 * Executa as verificação para o registro RODAPE
	 * @param linha Linha do arquivo a ser quebrada e analisada
	 */
	private void verificaRodape(String linha, int qtdRegistros, int qtdDetalhe, BigDecimal sumVlCreditoDocumento, BigDecimal vlDeposito) {
		
		/** REGRA 2.1  '99' ITEM 1 */
		if( linha.length() != 8 ){
			Object[] arg = new Object[1];
			arg[0] = Integer.valueOf(qtdRegistros + 1);
			throw new BusinessException("LMS-36092",arg);
		}
		
		Integer qtdDocumentos = null;
		
		qtdDocumentos = Integer.valueOf(FormatUtils.quebraLinha(linha,3,6,false,0));//linha.substring(2));
		
		/** REGRA 2.1  '99' ITEM 2 */
		if( qtdDocumentos.compareTo(Integer.valueOf(qtdDetalhe)) != 0 ){
			throw new BusinessException("LMS-36037");
		}
		
		/** REGRA 2.1  '99' ITEM 3 */
		if(!sumVlCreditoDocumento.equals(vlDeposito)){
			throw new BusinessException("LMS-36188");
		}
	}

	/**
	 * Executa as verificação para o registro DETALHE
	 * @param linha Linha do arquivo a ser quebrada e analisada
	 */
	private BigDecimal verificaDetalhe(String linha, Long idDepositoCcorrente, Cliente clienteValido, int qtdRegistros, int qtdDetalhe) {
		
		/** REGRA 2.1  '01' ITEM 1 */
//		if( linha.length() != 298 ){
		if( linha.length() < 43 ){
			Object[] arg = new Object[1];
			arg[0] = Integer.valueOf(qtdRegistros + 1);
			throw new BusinessException("LMS-36092",arg);
		}
		
		//Verificação do tipo de documento
		String tipoDocumento = FormatUtils.quebraLinha(linha,3,2,false,0);//linha.substring(2,4);
		
		int tpDocumento = numerarTipo(tipoDocumento);
		
		/** REGRA 2.1  '01' ITEM 2 */
		// Valida se é um tipo de documento válido
		if( tpDocumento == OUTRO ){
			throw new BusinessException("LMS-36033");
		}
		
		//Verificação da filial de origem e número do documento
		String sgFilial  = FormatUtils.quebraLinha(linha,5,3,false,0);//linha.substring(4,7);
		Long nrDocumento = Long.valueOf(FormatUtils.quebraLinha(linha,8,10,false,0));//linha.substring(7,17));
		
		/** REGRA 2.1  '01' ITEM 3 */
		List documentos = validaDocumento(tpDocumento,sgFilial,nrDocumento);
		
		//Verificação da data de Emissão
		DoctoServico doctoServico = null;
		Fatura fatura = null;
		
		switch (tpDocumento) {
		
			case CTRC: 
			case CTE: 
			case CRT : 
			case NFS : 
			case NFT : 
			case NTE : 
			case NSE : 
			case NDN : doctoServico = (DoctoServico) documentos.get(0);
			           break;
			case FA  : fatura = (Fatura) documentos.get(0);					   
			           break;
			   
			default  : break;
		
		} 
		
		boolean erroDataEmissao = false;
		
		if( doctoServico == null && fatura == null ){
			erroDataEmissao = true;
		} else {
			
			YearMonthDay dtEmissao = JTDateTimeUtils.convertDataStringToYearMonthDay(FormatUtils.quebraLinha(linha,18,8,false,0));//linha.substring(17,25));
			
			if( doctoServico != null ){
				if( JTDateTimeUtils.comparaData(dtEmissao,
						                        doctoServico.getDhEmissao().toYearMonthDay()) != 0 ){
					erroDataEmissao = true;					
				}
			} else {
				if( JTDateTimeUtils.comparaData(dtEmissao,fatura.getDtEmissao()) != 0 ){
					erroDataEmissao = true;
				}
			}
		}
		
		/** REGRA 2.1  '01' ITEM 4 */
		if( erroDataEmissao ){
			throw new BusinessException("LMS-36035", new Object[]{findTpDocumento(tpDocumento) + " " 
					+ sgFilial + " " + FormatUtils.formataNrDocumento(nrDocumento.toString()
							, findTpDocumento(tpDocumento))});
		}
		
		//Cria um DevedorDocServFat
		DevedorDocServFat devedorDocServFat = null;
		
		//Validação do Valor crédito documento
		BigDecimal vlCreditoDocumento = new BigDecimal(FormatUtils.quebraLinha(linha,26,18,true,2));//linha.substring(25,41)+"."+linha.substring(41,43));
		
		/** REGRA 2.1  '01' ITEM 5 */
		if( vlCreditoDocumento.compareTo(new BigDecimal(0)) != 1 ){
			throw new BusinessException("LMS-36036", new Object[]{findTpDocumento(tpDocumento) + " " 
					+ sgFilial + " " + FormatUtils.formataNrDocumento(nrDocumento.toString()
							, findTpDocumento(tpDocumento))});
		}
		
		boolean lancaBusinessException = false;
		/** REGRA 2.1  '01' ITEM 6 */
		if( doctoServico != null ){
			
			Desconto desconto = null;
			
			devedorDocServFat = devedorDocServFatService.findById(devedorDocServFatService
					.findDevedorByIdDoctoServicoIdCliente(doctoServico.getIdDoctoServico(), clienteValido.getIdCliente()).getIdDevedorDocServFat());
			
			if(devedorDocServFat != null && devedorDocServFat.getDescontos() != null && !devedorDocServFat.getDescontos().isEmpty()){
				desconto = (Desconto) devedorDocServFat.getDescontos().get(0);
			}
			
			if(desconto != null && desconto.getTpSituacaoAprovacao().getValue().equals("A")){
				BigDecimal vlSubstract = null;
				if (devedorDocServFat != null && devedorDocServFat.getVlDevido() != null ) {
					vlSubstract = devedorDocServFat.getVlDevido().subtract(desconto.getVlDesconto());
				}
				
				// Valida se vlCreditoDocumento é menor a vlSubstract
				if(vlSubstract == null || vlCreditoDocumento.compareTo(vlSubstract) == -1)
					lancaBusinessException = true;
				
			} else {
				// Valida se vlCreditoDocumento é menor a valor devido
				if(vlCreditoDocumento.compareTo(devedorDocServFat.getVlDevido()) == -1)
					lancaBusinessException = true;
			}
				
		} else if (fatura != null){
			
			BigDecimal vlSubstract = fatura.getVlTotal().subtract(fatura.getVlDesconto());
			
			// Valida se vlCreditoDocumento é menor a vlSubstract
			if(vlCreditoDocumento.compareTo(vlSubstract) == -1)
				lancaBusinessException = true;
		
		}
		
		if( lancaBusinessException ){
			throw new BusinessException("LMS-36189", new Object[]{findTpDocumento(tpDocumento) + " " 
					+ sgFilial + " " + FormatUtils.formataNrDocumento(nrDocumento.toString()
							, findTpDocumento(tpDocumento))});
		}
			
		
		/** REGRA 2.1  '01' ITEM 7 */
		
		//Save do item de depósito Conta Corrente
		DepositoCcorrente depositoCcorrente = new DepositoCcorrente();		
		depositoCcorrente = depositoCcorrenteService.findById(idDepositoCcorrente);
		
		ItemDepositoCcorrente itemDepositoCcorrente = new ItemDepositoCcorrente();
		
		if( doctoServico != null ){
			
			if( devedorDocServFat != null ){
				itemDepositoCcorrente.setDevedorDocServFat(devedorDocServFat);				
			}
			
		} else {
			itemDepositoCcorrente.setFatura(fatura);			
		}
		
		itemDepositoCcorrente.setDepositoCcorrente(depositoCcorrente);
		
		itemDepositoCcorrenteService.store(itemDepositoCcorrente);
		
		return vlCreditoDocumento;
	}

	/**
	 * Retorna um inteiro de acordo com o tipo de documento informado. 
	 * @param tipoDocumento String do tipo de documento nos formatos CT, CR, NF, NT, ND e FA
	 * @return 	<code>0 (zero)</code>   Para Conhecimentos<br>
	 * 			<code>1 (um)</code>     Para CTO Internacionais<br>
	 * 			<code>2 (dois)</code>   Para Notas Fiscais de Serviço<br>
	 * 			<code>3 (três)</code>   Para Notas Fiscais de Transportes<br>
	 * 			<code>4 (quatro)</code> Para Notas Débito Nacional<br>
	 * 			<code>5 (cinco)</code>  Para Fatura<br>
	 */
	private int numerarTipo(String tipoDocumento) {
		
		if( tipoDocumento.equals("CT") ){
			return CTRC;
		}
		
		if( tipoDocumento.equals("CR") ){
			return CRT;
		}
		
		if( tipoDocumento.equals("NF") ){
			return NFS;
		}
		
		if( tipoDocumento.equals("NT") ) {
			return NFT;
		}
		
		if( tipoDocumento.equals("ND") ) {
			return NDN;
		}
		
		if( tipoDocumento.equals("FA") ){
			return FA;
		}
		
		if( tipoDocumento.equals("CE") ){
			return CTE;
		}

		if( tipoDocumento.equals("NE") ){
			return NTE;
		}

		if( tipoDocumento.equals("NS") ){
			return NSE;
		}

		return -1;
	}
	
	private String findTpDocumento(int tpDoc){
		
		String tpDocumento = "";
		
		if( tpDoc == CTRC ){
			tpDocumento =  "CTR";
		}
		
		if( tpDoc == CRT ){
			tpDocumento = "CRT";
		}
		
		if( tpDoc == NFS ){
			tpDocumento = "NFS";
		}
		
		if( tpDoc == NFT ) {
			tpDocumento = "NFT";
		}
		
		if( tpDoc == NDN ) {
			tpDocumento = "NDN";
		}
		
		if( tpDoc == FA ){
			tpDocumento = "FAT";
		}
		
		if( tpDoc == CTE ){
			tpDocumento = "CTE";
		}

		if( tpDoc == NTE ){
			tpDocumento = "NTE";
		}

		if( tpDoc == NSE ){
			tpDocumento = "NSE";
		}

		return tpDocumento;
	}

	/**
	 * Verifica se o tipo de documento, filial e número do documento estão corretos e existe um documento correspondente
	 * @param tpDocumento Tipo do documento
	 * @param sgFilial Sigla da Filial Origem
	 * @param nrDocumento Número do documento
	 */
	private List validaDocumento(int tpDocumento, String sgFilial, Long nrDocumento) {
		
		Map criteria = new HashMap();
		criteria.put("sgFilial", sgFilial);
		
		List filiais = this.filialService.findLookupBySgFilial(criteria);
		
		Filial filialOrigem = null;
		
		if( filiais != null && !filiais.isEmpty() ){
			Map map = (Map) filiais.get(0);
			filialOrigem = new Filial();
			filialOrigem.setIdFilial((Long) map.get("idFilial"));
		} else {
			throw new BusinessException("LMS-36034"
					, new Object[]{findTpDocumento(tpDocumento) + " " 
							+ sgFilial + " " + FormatUtils.formataNrDocumento(nrDocumento.toString()
									, findTpDocumento(tpDocumento))});
		}
		
		TypedFlatMap tfm = null;
		
		List documentos = null;
		
		switch (tpDocumento) {
		
			case CTRC: 
			case CTE: tfm = new TypedFlatMap();
					   tfm.put("nrConhecimento",nrDocumento);
					   tfm.put("filialByIdFilialOrigem.idFilial",filialOrigem.getIdFilial());
					   documentos = findDocumentNumberCTR(tfm,findTpDocumento(tpDocumento));					   
					   break;
					   
			case CRT : tfm = new TypedFlatMap();
					   tfm.put("ctoInternacional.nrCrt",nrDocumento);
					   tfm.put("filialByIdFilialOrigem.idFilial",filialOrigem.getIdFilial());
					   documentos = findDocumentNumberCRT(tfm);			   
			   		   break;
			   		   
			case NFS :
			case NSE :tfm = new TypedFlatMap();
					   tfm.put("nrNotaFiscalServico",nrDocumento);
					   tfm.put("filialByIdFilialOrigem.idFilial",filialOrigem.getIdFilial());
					   documentos = findDocumentNumberNFS(tfm);					   
			   		   break;
			   
			case NFT : 
			case NTE :
					   tfm = new TypedFlatMap();
					   tfm.put("nrConhecimento",nrDocumento);
					   tfm.put("filialByIdFilialOrigem.idFilial",filialOrigem.getIdFilial());
					   documentos = findDocumentNumberCTR(tfm,findTpDocumento(tpDocumento));					   
					   break;
			   
			case NDN : tfm = new TypedFlatMap();
					   tfm.put("tpDocumentoServico","NDN");
					   tfm.put("filial.idFilial",filialOrigem.getIdFilial());
					   tfm.put("nrDocumento",nrDocumento);
					   documentos = findDocumentNumberNDN(tfm);					   
			   		   break;
			   
			case FA  : tfm = new TypedFlatMap();					   
					   tfm.put("idFilial",filialOrigem.getIdFilial());
					   tfm.put("nrFatura",nrDocumento);
					   documentos = findFaturas(tfm);					   
			           break;
			   
			default  : break;
			
		}
		
		if( documentos == null || documentos.isEmpty() ){
			throw new BusinessException("LMS-36034"
					, new Object[]{findTpDocumento(tpDocumento) + " " 
							+ sgFilial + " " + FormatUtils.formataNrDocumento(nrDocumento.toString()
									, findTpDocumento(tpDocumento))});
		}
		
		return documentos;
		
	}
	
	/**
	 * Executa as verificação para o registro CABECALHO
	 * @param linha Linha do arquivo a ser quebrada e analisada
	 */
	private List verificaCabecalho(String linha, Cliente clienteValido, int qtdRegistros, int qtdDetalhe) {
		
		List retorno = new ArrayList();
		YearMonthDay dtDeposito = null;	
		
		/** REGRA 2.1  '00' ITEM 1 */
//		if( linha.length() != 324 ){
		if( linha.length() < 69 ){
			Object[] arg = new Object[1];
			arg[0] = Integer.valueOf(qtdRegistros + 1);
			throw new BusinessException("LMS-36092",arg);
		}
		
		/** REGRA 2.1  '00' ITEM 2 */
		//Validacao da data de geração
		try {
			JTDateTimeUtils.convertDataStringToYearMonthDay(linha.substring(2,10));
		} catch (Exception e) {
			throw new BusinessException("LMS-36028");
		}
		
		/** REGRA 2.1  '00' ITEM 3 */
		// Validacao do CNPJ do Cliente
		if( clienteValido == null ){
			throw new BusinessException("LMS-36029");
		}
		
		//Validação do CNPJ do cliente com o cliente informado na tela
		String cnpj = FormatUtils.quebraLinha(linha,11,14,false,0);//linha.substring(10,24);
		
		/** REGRA 2.1  '00' ITEM 4 */
		if( !cnpj.equalsIgnoreCase(clienteValido.getPessoa().getNrIdentificacao()) ){
			throw new BusinessException("LMS-36065");
		}
		
		//Validação Banco, Agencia e Conta Corrente
		Long nrBanco       = Long.valueOf(FormatUtils.quebraLinha(linha,25,3,false,0));//linha.substring(24,27);
		Long nrAgencia     = Long.valueOf(FormatUtils.quebraLinha(linha,28,4,false,0));//linha.substring(27,31);
		String contaCorrente = FormatUtils.quebraLinha(linha,32,12,false,0).trim();//linha.substring(31,43);
		
		List cedentes = cedenteService.findCedentes(contaCorrente, nrAgencia, nrBanco);
		
		/** REGRA 2.1  '00' ITEM 5 */
		if( cedentes == null || cedentes.isEmpty() ){
			throw new BusinessException("LMS-36030");			
		}
		
		//Validação Valor de Depósito
		BigDecimal vlDeposito = null;
		
		/** REGRA 2.1  '00' ITEM 6 */
		try {
			
			vlDeposito = new BigDecimal(FormatUtils.quebraLinha(linha,44,18,true,2));//linha.substring(43,59) + "." + linha.substring(59,61) );
			retorno.add(vlDeposito);
			
			if( vlDeposito.compareTo(new BigDecimal(0)) == 0 ){
				throw new BusinessException("LMS-36031");
			}
			
		} catch (Exception e) {
			throw new BusinessException("LMS-36031");
		}
		
		/** REGRA 2.1  '00' ITEM 7 */
		//Validacao da data de Depósito
		try {
			dtDeposito = JTDateTimeUtils.convertDataStringToYearMonthDay(FormatUtils.quebraLinha(linha,62,8,false,0));//linha.substring(61,69));
		} catch (Exception e) {
			throw new BusinessException("LMS-36032");
		}
		
		String obDeposito = null;
	
		if( linha.length() > 69 ){
			obDeposito = FormatUtils.quebraLinha(linha,70,linha.length() - 69,false,0);//linha.substring(69);
		}
						
		/** REGRA 2.1  '00' ITEM 9 */
		DepositoCcorrente depositoCcorrente = new DepositoCcorrente();
		depositoCcorrente.setCliente(clienteValido);
        depositoCcorrente.setVlDeposito(vlDeposito);
        depositoCcorrente.setDtDeposito(dtDeposito);
        depositoCcorrente.setDtCarga(JTDateTimeUtils.getDataAtual());
        depositoCcorrente.setTpOrigem(new DomainValue("E"));
        depositoCcorrente.setBlRelacaoFechada(Boolean.TRUE);
        depositoCcorrente.setTpSituacaoRelacao(new DomainValue("A"));
        depositoCcorrente.setCedente((Cedente)cedentes.get(0));

		
		if( obDeposito != null && !obDeposito.equals("") ){
			depositoCcorrente.setObDepositoCcorrente(obDeposito);
		}
		
		retorno.add((Long) depositoCcorrenteService.store(depositoCcorrente));
		
		return retorno;
		
	}

	/**
	 * Busca os dados do Documento de Serviço - Conhecimento
	 * @param tfm Critérios de pesquisa
	 * @return Lista de Conhecimentos (CTRC)
	 */
	private List findDocumentNumberCTR(TypedFlatMap tfm, String tipoDocumento) {
		return this.conhecimentoService.findByNrConhecimentoByFilial(tfm.getLong("nrConhecimento"),
																	 tfm.getLong("filialByIdFilialOrigem.idFilial"),
																	 tipoDocumento);
	}

     /**
      * Busca os dados do Documento de Serviço - Cto Internacional
      * @param map Critérios de pesquisa
      * @return Lista de Ctos Internacionais (CRT)
      */
     public List findDocumentNumberCRT(Map map) {
        return this.ctoInternacionalService.findLookup(map);
     }
     
     /**
      * Busca os dados do Documento de Serviço - Nota Fiscal Serviço
      * @param tfm Critérios de pesquisa
      * @return Lista de Notas Fiscais de Serviço (NFS)
      */
     public List findDocumentNumberNFS(TypedFlatMap tfm) {
    	 return this.notaFiscalServicoService.findByNrnotaFiscalByFilial(tfm.getLong("nrNotaFiscalServico"),
    			 														 tfm.getLong("filialByIdFilialOrigem.idFilial"));
     }
     
     /**
      * Busca os dados do Documento de Serviço - Nota Débito Nacional
      * @param tfm Critérios de pesquisa
      * @return Lista de Notas Débito Nacional
      */
 	private List findDocumentNumberNDN(TypedFlatMap tfm) {
 		return notaDebitoNacionalService.findNotaDebitoNacional(tfm.getString("tpDocumentoServico"),
 																tfm.getLong("filial.idFilial"),
 																tfm.getLong("nrDocumento"));
 	}
 	
 	/**
 	 * Busca os dados de Faturas de acordo com os critérios de pesquisa
 	 * @param tfm Critérios de pesquisa
 	 * @return Lista de faturas
 	 */
 	private List findFaturas(TypedFlatMap tfm) {
 		return faturaService.findByNrFaturaByFilial(tfm.getLong("nrFatura"),tfm.getLong("idFilial"));
	}	

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setCedenteService(CedenteService cedenteService) {
		this.cedenteService = cedenteService;
	}

	public void setDepositoCcorrenteService(
			DepositoCcorrenteService depositoCcorrenteService) {
		this.depositoCcorrenteService = depositoCcorrenteService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setItemDepositoCcorrenteService(
			ItemDepositoCcorrenteService itemDepositoCcorrenteService) {
		this.itemDepositoCcorrenteService = itemDepositoCcorrenteService;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

}
