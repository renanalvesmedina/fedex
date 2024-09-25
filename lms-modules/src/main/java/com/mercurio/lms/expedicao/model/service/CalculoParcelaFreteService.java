package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.*;
import com.mercurio.lms.tabelaprecos.model.*;
import com.mercurio.lms.tabelaprecos.model.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.NonUniqueResultException;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateSystemException;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.model.ParcelaServico;
import com.mercurio.lms.expedicao.model.ParcelaTaxaColetaEntrega;
import com.mercurio.lms.expedicao.model.dao.CalculoParcelaFreteDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.OperacaoServicoLocaliza;
import com.mercurio.lms.tributos.model.TaxaSuframa;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DestinatarioTdeCliente;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.MunicipioTrtCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.TaxaCliente;
import com.mercurio.lms.vendas.model.TdeCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DestinatarioTdeClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoParcelaService;
import com.mercurio.lms.vendas.model.service.FaixaProgressivaPropostaService;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;
import com.mercurio.lms.vendas.model.service.MunicipioTrtClienteService;
import com.mercurio.lms.vendas.model.service.ServicoAdicionalClienteService;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;
import com.mercurio.lms.vendas.model.service.TdeClienteService;
import com.mercurio.lms.vendas.util.ClienteUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.expedicao.calculoParcelaFreteService" autowire="no"
 * @spring.property name="calculoParcelaFreteDAO" ref="lms.expedicao.calculoParcelaFreteDAO"
 * @spring.property name="calculoFreteService" ref="lms.expedicao.calculoFreteService"
 * @spring.property name="faixaProgressivaService" ref="lms.tabelaprecos.faixaProgressivaService"
 * @spring.property name="mcdService" ref="lms.municipios.mcdService"
 * @spring.property name="enderecoPessoaService" ref="lms.configuracoes.enderecoPessoaService"
 * @spring.property name="tabelaDivisaoClienteService" ref="lms.vendas.tabelaDivisaoClienteService"
 * @spring.property name="operacaoServicoLocalizaService" ref="lms.municipios.operacaoServicoLocalizacaoService"
 * @spring.property name="parcelaPrecoService" ref="lms.tabelaprecos.parcelaPrecoService"
 * @spring.property name="tipoLocalizacaoMunicipioService" ref="lms.municipios.tipoLocalizacaoMunicipioService"
 * @spring.property name="ajusteTarifaService" ref="lms.tabelaprecos.ajusteTarifaService"
 * @spring.property name="unidadeFederativaService" ref="lms.municipios.unidadeFederativaService"
 * @spring.property name="municipioGrupoRegiaoService" ref="lms.tabelaprecos.municipioGrupoRegiaoService"
 * @spring.property name="grupoRegiaoService" ref="lms.tabelaprecos.grupoRegiaoService"
 * @spring.property name="aeroportoService" ref="lms.municipios.aeroportoService"
 * @spring.property name="tarifaPrecoRotaService" ref="lms.tabelaprecos.tarifaPrecoRotaService"
 * @spring.property name="informacaoDoctoClienteService" ref="lms.vendas.informacaoDoctoClienteService"
 * @spring.property name="municipioFilialService" ref="lms.municipios.municipioFilialService"
 * @spring.property name="postoPassagemMunicipioService" ref="lms.municipios.postoPassagemMunicipioService"
 * @spring.property name="municipioTrtClienteService" ref="lms.vendas.municipioTrtClienteService"
 * @spring.property name="parametroGeralService" ref="lms.configuracoes.parametroGeralService"
 * @spring.property name="faixaProgressivaPropostaService" ref="lms.vendas.faixaProgressivaPropostaService"
 * @spring.property name="tdeClienteService" ref="lms.vendas.tdeClienteService"
 * @spring.property name="clienteService" ref="lms.vendas.clienteService"
 * @spring.property name="destinatarioTdeClienteService" ref="lms.vendas.destinatarioTdeClienteService"
 * @spring.property name="divisaoParcelaService" ref="lms.vendas.divisaoParcelaService"
 */
public class CalculoParcelaFreteService extends CalculoParcelaServicoService {
	
	private static final String KM_MINIMA = "KM_MINIMA";	
	private static final String COTACAO = "C";
	private static String msgErroParcelaPreco = "Não foi possível encontrar parcela preço ";
	
	private FaixaProgressivaService faixaProgressivaService;
	private McdService mcdService;
	private EnderecoPessoaService enderecoPessoaService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private OperacaoServicoLocalizaService operacaoServicoLocalizaService;
	private ParcelaPrecoService parcelaPrecoService;
	private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService;
	private AjusteTarifaService ajusteTarifaService;
	private UnidadeFederativaService unidadeFederativaService;
	private AeroportoService aeroportoService;
	private TarifaPrecoRotaService tarifaPrecoRotaService;
	private InformacaoDoctoClienteService informacaoDoctoClienteService;
	private MunicipioFilialService municipioFilialService;
	private PostoPassagemMunicipioService postoPassagemMunicipioService;
	private MunicipioGrupoRegiaoService municipioGrupoRegiaoService;
	private GrupoRegiaoService grupoRegiaoService;
	private MunicipioTrtClienteService municipioTrtClienteService;
	private ParametroGeralService parametroGeralService;
	private ClienteService clienteService;
	private FaixaProgressivaPropostaService faixaProgressivaPropostaService;
	private TdeClienteService tdeClienteService; 
	private DestinatarioTdeClienteService destinatarioTdeClienteService; 
	private DivisaoParcelaService divisaoParcelaService;
	private ServicoAdicionalClienteService servicoAdicionalClienteService;
	private DadosComplementoService dadosComplementoService;
	private TabelaMunicipioEMEXService tabelaMunicipioEMEXService;
	private RotaPrecoService rotaPrecoService;
	private MunicipioService municipioService;
	
	private Logger log = LogManager.getLogger(this.getClass());
	
	/* Contantes para os tipos de Parcelas */
	private static final List<String> PARCELAS_CALCULO_RODO = Arrays.asList(
			   new String[]{ConstantesExpedicao.CD_FRETE_PESO,
					   ConstantesExpedicao.CD_FRETE_VALOR,
					   ConstantesExpedicao.CD_GRIS,
					   ConstantesExpedicao.CD_DESPACHO});

	private static final List<String> PARCELAS_CALCULO_AEREO = Arrays.asList(
			   new String[]{ConstantesExpedicao.CD_FRETE_PESO,
					   ConstantesExpedicao.CD_FRETE_VALOR,
					   ConstantesExpedicao.CD_TAXA_COLETA_URBANA_CONVENCIONAL,
					   ConstantesExpedicao.CD_TAXA_COLETA_URBANA_EMERGENCIA,
					   ConstantesExpedicao.CD_TAXA_COLETA_INTERIOR_CONVENCIONAL,
					   ConstantesExpedicao.CD_TAXA_COLETA_INTERIOR_EMERGENCIA,
					   ConstantesExpedicao.CD_TAXA_ENTREGA_URBANA_CONVENCIONAL,
					   ConstantesExpedicao.CD_TAXA_ENTREGA_URBANA_EMERGENCIA,
					   ConstantesExpedicao.CD_TAXA_ENTREGA_INTERIOR_CONVENCIONAL,
					   ConstantesExpedicao.CD_TAXA_ENTREGA_INTERIOR_EMERGENCIA,
					   ConstantesExpedicao.CD_TAXA_TERRESTRE,
					   ConstantesExpedicao.CD_TAXA_COMBUSTIVEL});
	
	private static final Map<String, String> TIPOS_PEDAGIO = new HashMap<String, String>(){{
		put("P", ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO);
		put("F", ConstantesExpedicao.CD_PEDAGIO_FRACAO);
		put("D", ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO);
		put("X", ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO);
	}};

	public List<ParcelaDoctoServico> findParcelasDoctoServico(Long idDoctoServico) {
		return getCalculoParcelaFreteDAO().findParcelasDoctoServico(idDoctoServico);
	}

	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}

	private BigDecimal findValorPrecoFreteMenor(Long idTabelaPreco) {
		BigDecimal vlPrecoFrete = getCalculoParcelaFreteDAO().findValorFretePesoMenor(idTabelaPreco, ConstantesExpedicao.CD_TARIFA_MINIMA);
		if(vlPrecoFrete == null) {
			throw new BusinessException("LMS-30030", new Object[]{ConstantesExpedicao.CD_TARIFA_MINIMA});
		}
		return vlPrecoFrete;
	}

	private List<String> findTiposDeParcelas(CalculoFrete calculoFrete, String parcelaPreco) {
		if (calculoFrete.getIdDivisaoCliente() != null) {
			List<Map<String, Object>> parcelas = divisaoParcelaService.findByDivisaoCliente(
				calculoFrete.getIdDivisaoCliente(), parcelaPreco
			);
			if (!parcelas.isEmpty()) {
				List<String> strParcelas = new ArrayList<String>();
				for (Map<String, Object> parcela : parcelas) {
					strParcelas.add((String)parcela.get("CD_PARCELA_PRECO_COBRANCA"));
				}
				return strParcelas;
			}
		}
		if (ConstantesExpedicao.MODAL_RODOVIARIO.equals(calculoFrete.getTpModal())) {
			return PARCELAS_CALCULO_RODO;
		} else {
			return PARCELAS_CALCULO_AEREO;
		}
	}
	
	/**
	 * Calcula o valor da parcela de Frete Peso
	 *
	 * @param calculoFrete
	 * @return ParcelaServico
	 */
	public ParcelaServico findParcelaFretePeso(CalculoFrete calculoFrete) {

		BigDecimal psReferencia = BigDecimalUtils.defaultBigDecimal(calculoFrete.getPsReferencia());
		BigDecimal vlUnitarioParcela = BigDecimalUtils.ZERO;
		BigDecimal vlBrutoParcela = BigDecimalUtils.ZERO;

		/*Tabela utilizada para cálculo*/
		TabelaPreco tabelaPreco = calculoFrete.getTabelaPreco();

		/*Obtem a ParcelaPreco através do IDFretePeso*/
		ParcelaPreco parcelaPreco = findParcelaPreco(tabelaPreco.getIdTabelaPreco(), ConstantesExpedicao.CD_FRETE_PESO);

		/*Informa a mensagem LMS-04120 caso não encontrar a ParcelaPreco*/
		if(parcelaPreco == null) {
			parcelaPreco = getCalculoParcelaFreteDAO().findParcelaPreco(ConstantesExpedicao.CD_FRETE_PESO);
			throw new BusinessException("LMS-04120", new Object[]{parcelaPreco.getNmParcelaPreco().getValue(), tabelaPreco.getTabelaPrecoString()});
		}

		/*Adiciona a ParcelaPreco a ParcelaServico*/
		ParcelaServico freteParcela = new ParcelaServico(parcelaPreco);

		/*Obtem o tipo da tabela preço*/
		String tpTabelaPreco = tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue();

		if(ConstantesExpedicao.MODAL_RODOVIARIO.equals(calculoFrete.getTpModal()) || "D".equals(tpTabelaPreco)) {

			Boolean blPagaFreteTonelada = Boolean.FALSE;
			ValorFaixaProgressivaDTO valorFaixaProgressivaDTO;

			/*Verifica se o cliente é especial*/
			if(calculoFrete.getClienteBase() != null && calculoFrete.getClienteBase().getTpCliente() != null && (!calculoFrete.getBlCalculoFreteTabelaCheia()) && ClienteUtils.isParametroClienteEspecial(calculoFrete.getClienteBase().getTpCliente().getValue())) {
				TabelaDivisaoCliente tabelaDivisaoCliente = tabelaDivisaoClienteService.findTabelaDivisaoCliente(calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico());
				if(tabelaDivisaoCliente != null) {
					blPagaFreteTonelada = tabelaDivisaoCliente.getBlPagaFreteTonelada();
				}
			}
			valorFaixaProgressivaDTO = findValorUnitarioFretePeso(calculoFrete, freteParcela, blPagaFreteTonelada);
			vlUnitarioParcela = BigDecimalUtils.defaultBigDecimal(valorFaixaProgressivaDTO.getValorUnitarioParcela());

			/*Frete Volume Natura*/
			if(tabelaPreco.getTpCalculoFretePeso() != null && "N".equals(tabelaPreco.getTpCalculoFretePeso().getValue())){
				freteParcela.setVlUnitarioParcela(vlUnitarioParcela);
				freteParcela.setVlBrutoParcela(vlUnitarioParcela);
				return freteParcela;
			}

			/*Se o psReferencia for menor que o peso minimo da tabela preço seta
			no valor bruto da parcela o valor unitario retornado no metodo findValorUnitarioFretePeso*/
			if(CompareUtils.le(psReferencia, BigDecimalUtils.defaultBigDecimal(tabelaPreco.getPsMinimo()))) {
				vlBrutoParcela = vlUnitarioParcela;
			} else {


				if(("M".equals(tpTabelaPreco) || "T".equals(tpTabelaPreco) || "R".equals(tpTabelaPreco)) &&  Boolean.TRUE.equals(blPagaFreteTonelada)) {
					FaixaProgressiva faixaProgressiva = faixaProgressivaService.findFaixaProgressivaEnquadrada(tabelaPreco.getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), psReferencia);

					if("PE".equals(faixaProgressiva.getTpIndicadorCalculo().getValue())) {
						vlBrutoParcela = psReferencia.multiply(vlUnitarioParcela);
					} else if("VL".equals(faixaProgressiva.getTpIndicadorCalculo().getValue())) {
						vlBrutoParcela = vlUnitarioParcela;
					}
				} else {
					/*Progressivo excedente e Excedente - para este tipo o valor é cálculado no método
					 * findValorUnitarioFretePeso */
					if("P".equals(tabelaPreco.getTpCalculoFretePeso().getValue())
							|| "E".equals(tabelaPreco.getTpCalculoFretePeso().getValue())) {

						vlBrutoParcela = vlUnitarioParcela;

					} else if("D".equals(tabelaPreco.getTpCalculoFretePeso().getValue())) {

						Long idTarifaPreco = valorFaixaProgressivaDTO.getIdTarifaPreco();
						Long idTabelaPreco = tabelaPreco.getIdTabelaPreco();
						BigDecimal vlTaxaFixa = valorFaixaProgressivaDTO.getVlTaxaFixa();
						BigDecimal vlKmExtra = valorFaixaProgressivaDTO.getVlKmExtra();

						if(vlTaxaFixa == null) {
							throw new BusinessException("LMS-30089");
						}

						if(vlKmExtra == null) {
							throw new BusinessException("LMS-30090");
						}

						RotaPreco rotaPreco = this.rotaPrecoService.findRotaPrecoByIdTarifaPrecoAndIdTabelaPreco(idTarifaPreco, idTabelaPreco);
						FaixaProgressiva faixaProgressiva = faixaProgressivaService.findFaixaProgressivaEnquadrada(tabelaPreco.getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), psReferencia);

						if("PE".equals(faixaProgressiva.getTpIndicadorCalculo().getValue())) {
							vlBrutoParcela = psReferencia.multiply(vlUnitarioParcela).add(vlTaxaFixa);

						} else if("VL".equals(faixaProgressiva.getTpIndicadorCalculo().getValue())) {
							vlBrutoParcela = vlUnitarioParcela.add(vlTaxaFixa);
						}

						if(rotaPreco != null && rotaPreco.getMunicipioByIdMunicipioDestino() == null) {

							MunicipioFilial municipioFilialDestino = this.municipioFilialService.findById(calculoFrete.getMunicipioFilialDestino().getIdMunicipioFilial());
							Municipio municipioEntrega = municipioFilialDestino.getMunicipio();
							vlBrutoParcela = vlBrutoParcela.add(vlKmExtra.multiply(BigDecimal.valueOf(municipioEntrega.getNrDistanciaCapital())));
						}

					}else {

						vlBrutoParcela = psReferencia.multiply(vlUnitarioParcela);
					}
				}
			}/*else*/

		/*Se o tipo modal for aéreo*/
		} else if(ConstantesExpedicao.MODAL_AEREO.equals(calculoFrete.getTpModal())) {

			/*Obtem a faixa progressiva*/
			FaixaProgressiva faixaProgressiva = faixaProgressivaService.findFaixaProgressivaEnquadrada(tabelaPreco.getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), psReferencia);

			Long idRotaPreco = findValorFaixaProgressivaRotaPreco(calculoFrete, parcelaPreco.getIdParcelaPreco(), faixaProgressiva.getIdFaixaProgressiva()).getRotaPreco().getIdRotaPreco();

			vlUnitarioParcela = findValorUnitarioFretePeso(calculoFrete, freteParcela, idRotaPreco).getValorUnitarioParcela();

			/*Frete Volume Natura*/
			if(tabelaPreco.getTpCalculoFretePeso() != null && "N".equals(tabelaPreco.getTpCalculoFretePeso().getValue())){
				freteParcela.setVlUnitarioParcela(vlUnitarioParcela);
				freteParcela.setVlBrutoParcela(vlUnitarioParcela);
				return freteParcela;
			}

			/*Cálcula o valor da parcela*/
			BigDecimal vlTarifaMinima = findValorUnitarioTarifaMinima(tabelaPreco.getIdTabelaPreco(), idRotaPreco);
			freteParcela.addParametroAuxiliar("vlMinimo", vlTarifaMinima);
			vlBrutoParcela = psReferencia.multiply(vlUnitarioParcela);

			if(CompareUtils.le(vlBrutoParcela, vlTarifaMinima)) {
				vlBrutoParcela = vlTarifaMinima;
			}
		}
		validaValorParcela(vlUnitarioParcela);
		freteParcela.setVlUnitarioParcela(vlUnitarioParcela);
		freteParcela.setVlBrutoParcela(vlBrutoParcela);

		return freteParcela;
	}

	/**
	 * Calcula o valor unitário da parcela de Frete Peso para o Modal Rodoviario
	 *
	 * @param calculoFrete
	 * @param idParcelaPreco
	 * @param faixaProgressiva
	 * @param blPagaFreteTonelada
	 * @return ValorFaixaProgressivaDTO
	 */
	private ValorFaixaProgressivaDTO findValorUnitarioFretePeso(CalculoFrete calculoFrete,ParcelaServico parcelaFretePeso, Boolean blPagaFreteTonelada){

		/*Obtem a tabela preco corrente*/
		TabelaPreco tabelaPreco = calculoFrete.getTabelaPreco();

		/*Id da tarifa*/
		Long idTarifaPreco = calculoFrete.getIdTarifa();

		/*Tipo da tabela preco*/
		String tpTabelaPreco = tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue();

		/*Sub tipo da tabela preco*/
		String sbTpTabelaPreco = tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco();

		TarifaPreco tarifaPreco = null;

		/*Obtem a tarifa preço*/
		if( idTarifaPreco == null
			|| "D".equals(tpTabelaPreco)
			|| "@".equals(tpTabelaPreco)
			|| "B".equals(tpTabelaPreco)
			|| "I".equals(tpTabelaPreco)
				|| ( ( "F".equals(sbTpTabelaPreco) || "P".equals(sbTpTabelaPreco) ) && "M".equals(tpTabelaPreco) ) ){

			tarifaPreco = findTarifaPreco(calculoFrete);
			idTarifaPreco = tarifaPreco.getIdTarifaPreco();
			calculoFrete.setIdTarifa(idTarifaPreco);

		}else{
			
			/*CQPRO00025223 - Vendas Brasil*/
			String paramDtImplantacao = (String)getConfiguracoesFacade().getValorParametro("IMPLANTACAO_VENDA_BRASIL");
			if(paramDtImplantacao == null){
				throw new BusinessException("Parametro IMPLANTACAO_VENDA_BRASIL não encontrado");
			}

			YearMonthDay dtImplantacaoVendasBrasil = JTDateTimeUtils.convertDataStringToYearMonthDay(paramDtImplantacao);
			if(dtImplantacaoVendasBrasil != null){

				/*Se a tabela utilizada possuir a data de vigência inicial maior ou igual a data no
				parâmetro geral IMPLANTACAO_VENDA_BRASIL deverá ser utilizado a tarifa atual
				senão a tarifa antiga*/
				TarifaPreco[] tarifas = mcdService.findTarifasPrecoLMS(calculoFrete.getRestricaoRotaOrigem().getIdMunicipio(), calculoFrete.getRestricaoRotaDestino().getIdMunicipio(), calculoFrete.getIdServico());
				if(JTDateTimeUtils.comparaData(tabelaPreco.getDtVigenciaInicial(), dtImplantacaoVendasBrasil) >= 0){
					tarifaPreco = tarifas[0];
				}else{
					tarifaPreco = tarifas[1];
				}

				/*Informa a tarifa*/
				idTarifaPreco = tarifaPreco.getIdTarifaPreco();
				calculoFrete.setIdTarifa(idTarifaPreco);

			}/*if*/

		}/*else*/

		return getValorUnitarioFretePesoFaixaPeso(calculoFrete, parcelaFretePeso, idTarifaPreco, blPagaFreteTonelada);
	}

	/**
	 * Calcula o valor unitário da parcela de Frete Peso para o Modal Aéreo
	 * @param calculoFrete
	 * @param idParcelaPreco
	 * @param faixaProgressiva
	 * @param idRotaPreco
	 * @return
	 */
	private ValorFaixaProgressivaDTO findValorUnitarioFretePeso(CalculoFrete calculoFrete,ParcelaServico parcelaFretePeso,Long idRotaPreco) {

		TabelaPreco tabelaPreco = calculoFrete.getTabelaPreco();

		ParcelaPreco parcelaPreco = parcelaFretePeso.getParcelaPreco();

		Long idTabelaPreco = tabelaPreco.getIdTabelaPreco();
		Long idParcelaPreco = parcelaPreco.getIdParcelaPreco();
		Long idTarifaPreco = calculoFrete.getIdTarifa();

		BigDecimal valorUnitarioParcela = null;
		ValorFaixaProgressiva valorFaixaProgressiva = null;
		BigDecimal psReferencia = calculoFrete.getPsReferencia() == null ? BigDecimalUtils.ZERO : calculoFrete.getPsReferencia();
		ValorFaixaProgressivaDTO valorFaixaProgressivaDTO = null;

		if("D".equals(tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())) {

			valorFaixaProgressivaDTO = getValorUnitarioFretePesoFaixaPeso(calculoFrete, parcelaFretePeso, idTarifaPreco, Boolean.FALSE);
			valorUnitarioParcela = valorFaixaProgressivaDTO.getValorUnitarioParcela();

		} else {

			Long idProdutoEspecifico = calculoFrete.getIdProdutoEspecifico();
			if( (idProdutoEspecifico != null) && (idProdutoEspecifico > 0) ) {
				// se informou Produto Especifico
				valorFaixaProgressiva = getCalculoParcelaFreteDAO().findValorFaixaProgressiva(idTabelaPreco, idParcelaPreco, idRotaPreco, idProdutoEspecifico);
			} else {
				// se nao informou Produto Especifico
				FaixaProgressiva faixaProgressiva = faixaProgressivaService.findFaixaProgressivaEnquadrada(idTabelaPreco, idParcelaPreco, psReferencia);
				if(faixaProgressiva != null) {
					valorFaixaProgressiva = getCalculoParcelaFreteDAO().findValorFaixaProgressiva(faixaProgressiva.getIdFaixaProgressiva(), idRotaPreco, null, null, JTDateTimeUtils.getDataAtual());
					if(valorFaixaProgressiva == null) {
						valorFaixaProgressiva = getCalculoParcelaFreteDAO().findValorFaixaProgressiva(faixaProgressiva.getIdFaixaProgressiva(), idRotaPreco, null, null, null);
					}
				}
			}
			if(valorFaixaProgressiva != null) {
				valorUnitarioParcela = valorFaixaProgressiva.getVlFixo();
				if(valorFaixaProgressiva.getPcDesconto() != null) {
					BigDecimal vlDesconto = valorUnitarioParcela.multiply(valorFaixaProgressiva.getPcDesconto().divide(BigDecimalUtils.HUNDRED, 4, BigDecimal.ROUND_UNNECESSARY));
					valorUnitarioParcela = valorUnitarioParcela.subtract(vlDesconto);
				}
				if(valorFaixaProgressiva.getVlAcrescimo() != null) {
					valorUnitarioParcela = valorUnitarioParcela.add(valorFaixaProgressiva.getVlAcrescimo());
				}
				valorFaixaProgressivaDTO = new ValorFaixaProgressivaDTO(valorUnitarioParcela, valorFaixaProgressiva.getVlTaxaFixa(),
						valorFaixaProgressiva.getVlKmExtra(), null);
			}
		}

		/* CQPRO00027200 - Correção para problema de null pointer na tela de emissão de conhecimento, quando informado Produto Específico */
		if(valorUnitarioParcela == null)
			throw new BusinessException("LMS-30059");

		return valorFaixaProgressivaDTO;
	}

	/**
	 * Verifica se é necessário reajuste na parcela
	 * @param calculoFrete
	 * @return Valor de reajuste
	 */
	private BigDecimal executeAjusteTarifa(CalculoFrete calculoFrete, BigDecimal vlParcela){

		BigDecimal vlAjuste = BigDecimal.ZERO;

		AjusteTarifa ajuste = ajusteTarifaService.findByRota(calculoFrete.getRestricaoRotaOrigem(), calculoFrete.getRestricaoRotaDestino(), calculoFrete.getTabelaPreco());
		if(ajuste != null){

			/*Isso foi necessário para a regra do preco frete*/
			calculoFrete.setAjusteTarifa(ajuste);

			if("A".equals(ajuste.getTpAjusteFretePeso().getValue())){

				if("P".equals(ajuste.getTpValorFretePreso().getValue())){
					vlAjuste = BigDecimalUtils.acrescimo(vlParcela, ajuste.getVlFretePeso());
				}else
					if("V".equals(ajuste.getTpValorFretePreso().getValue())){
						vlAjuste = BigDecimalUtils.add(vlParcela, ajuste.getVlFretePeso());
				}
			}else
				if("D".equals(ajuste.getTpAjusteFretePeso().getValue())){

					if("P".equals(ajuste.getTpValorFretePreso().getValue())){
						vlAjuste = BigDecimalUtils.desconto(vlParcela, ajuste.getVlFretePeso());
					}else
						if("V".equals(ajuste.getTpValorFretePreso().getValue())){
							vlAjuste = BigDecimalUtils.defaultBigDecimal(vlParcela).subtract(ajuste.getVlFretePeso());
					}
			}

		}/*ajuste*/

		return vlAjuste;
	}

	/**
	 * Aatravés do PrecoFrete obtem o valor unitário da parcela identificando
	 * o tipo de cálculo frete peso D, E, P ou Q
	 *
	 * @param  calculoFrete
	 * @param  parcelaFretePeso
	 * @param  idTarifaPreco
	 * @param  blPagaFreteTonelada
	 * @return ValorFaixaProgressivaDTO
	 */
	private ValorFaixaProgressivaDTO getValorUnitarioFretePesoFaixaPeso(CalculoFrete calculoFrete, ParcelaServico parcelaFretePeso, Long idTarifaPreco, Boolean blPagaFreteTonelada){

		BigDecimal valorUnitarioParcela;
		ValorFaixaProgressivaDTO valorFaixaProgressivaDTOAux = new ValorFaixaProgressivaDTO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, null);

		/*Obtem a tabela preço*/
		TabelaPreco tabelaPreco = calculoFrete.getTabelaPreco();

		/*Obtem o preço frete, se for nulo informa LMS-30030*/
		ParcelaPreco parcelaPreco = parcelaFretePeso.getParcelaPreco();

		if(tabelaPreco.getTpCalculoFretePeso() != null && "N".equals(tabelaPreco.getTpCalculoFretePeso().getValue())){

			/*FRETE VOLUME*/
			/*Verifica se o complemento do documento possui Quantidade de volumes de 27L e 13L*/
			valorFaixaProgressivaDTOAux.setValorUnitarioParcela(findValorComplementarFreteVolume(calculoFrete));

		}else{

			PrecoFrete precoFrete = getCalculoParcelaFreteDAO().findFretePeso(tabelaPreco.getIdTabelaPreco(), ConstantesExpedicao.CD_FRETE_QUILO, idTarifaPreco, null);
			if(precoFrete == null && !Arrays.asList("D", "T").contains(tabelaPreco.getTpCalculoFretePeso().getValue())) {
				throw new BusinessException("LMS-30030", new Object[]{ConstantesExpedicao.CD_FRETE_QUILO});
			}

			/*Verifica o tipo e sub tipo da tabela preço*/
			String tpTabelaPreco = tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue();
			String sbTpTabelaPreco = tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco();

			if("A".equals(calculoFrete.getTpModal()) && "D".equals(tpTabelaPreco) && (idTarifaPreco == null)){
				throw new BusinessException("LMS-30022");
			} else if(!"D".equals(tpTabelaPreco) && !"@".equals(tpTabelaPreco)
					&& !"B".equals(tpTabelaPreco) && !"I".equals(tpTabelaPreco)
					&& !("F".equals(sbTpTabelaPreco) && "M".equals(tpTabelaPreco))
					&& !("P".equals(sbTpTabelaPreco) && "M".equals(tpTabelaPreco))) {
				idTarifaPreco = null;
			}

			BigDecimal vlParcelaFreteQuilo = BigDecimal.ZERO;

			if(precoFrete == null && ("D".equals(tabelaPreco.getTpCalculoFretePeso().getValue())) ||
					precoFrete != null && ("D".equals(tabelaPreco.getTpCalculoFretePeso().getValue()))) {
				vlParcelaFreteQuilo = null;
				parcelaFretePeso.addParametroAuxiliar("vlParcelaFreteQuilo", vlParcelaFreteQuilo);
				
			}else if (precoFrete != null && !("D".equals(tabelaPreco.getTpCalculoFretePeso().getValue()))) {

				vlParcelaFreteQuilo = precoFrete.getVlPrecoFrete();
				parcelaFretePeso.addParametroAuxiliar("vlParcelaFreteQuilo", vlParcelaFreteQuilo);
			}

			BigDecimal psReferencia = BigDecimalUtils.defaultBigDecimal(calculoFrete.getPsReferencia());

			/*Calcula o valor unitario da parcela através do TpCalculoFretePeso*/
			if(CompareUtils.le(psReferencia, BigDecimalUtils.defaultBigDecimal(tabelaPreco.getPsMinimo()))) {
				valorFaixaProgressivaDTOAux  = getValorUnitarioFretePesoFaixaProgressiva(tabelaPreco.getIdTabelaPreco(),
						parcelaPreco.getIdParcelaPreco(), psReferencia, vlParcelaFreteQuilo, idTarifaPreco,
						calculoFrete.getIdTarifa());
				valorUnitarioParcela = valorFaixaProgressivaDTOAux.getValorUnitarioParcela();

				/*CQPRO00025223*/
				BigDecimal vlAjusteTarifa = this.executeAjusteTarifa(calculoFrete, valorUnitarioParcela);
				if(BigDecimalUtils.hasValue(vlAjusteTarifa)){
					valorUnitarioParcela = vlAjusteTarifa;
				}

				valorFaixaProgressivaDTOAux.setValorUnitarioParcela(valorUnitarioParcela);

			} else {

				/*Valor quilo*/
				if("Q".equals(tabelaPreco.getTpCalculoFretePeso().getValue())) {

					Integer nrFaixaMaiorPesoMinimo = getCalculoParcelaFreteDAO().findCountFaixaProgressiva(tabelaPreco.getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), tabelaPreco.getPsMinimo());
					if(CompareUtils.gt(nrFaixaMaiorPesoMinimo, IntegerUtils.ZERO) ) {
						if(Boolean.TRUE.equals(blPagaFreteTonelada)) {
							valorFaixaProgressivaDTOAux = getValorUnitarioFretePesoFaixaProgressiva(tabelaPreco.getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), psReferencia, vlParcelaFreteQuilo, idTarifaPreco, calculoFrete.getIdTarifa());
							valorUnitarioParcela = valorFaixaProgressivaDTOAux.getValorUnitarioParcela();
						} else {
							valorUnitarioParcela = vlParcelaFreteQuilo;
						}
					} else {
						valorUnitarioParcela = vlParcelaFreteQuilo;
					}


					/*CQPRO00025223*/
					BigDecimal vlAjusteTarifa = this.executeAjusteTarifa(calculoFrete, valorUnitarioParcela);
					if(BigDecimalUtils.hasValue(vlAjusteTarifa)){
						valorUnitarioParcela = vlAjusteTarifa;
					}

					valorFaixaProgressivaDTOAux.setValorUnitarioParcela(valorUnitarioParcela);

					/*Excedente*/
				} else if("E".equals(tabelaPreco.getTpCalculoFretePeso().getValue())) {

					BigDecimal psExcedente = psReferencia.subtract(tabelaPreco.getPsMinimo());
					valorFaixaProgressivaDTOAux = getValorUnitarioFretePesoFaixaProgressiva(tabelaPreco.getIdTabelaPreco(),
							parcelaPreco.getIdParcelaPreco(), tabelaPreco.getPsMinimo(), vlParcelaFreteQuilo, idTarifaPreco,
							calculoFrete.getIdTarifa());
					BigDecimal vlParcelaPsMinimo = valorFaixaProgressivaDTOAux.getValorUnitarioParcela();
					valorFaixaProgressivaDTOAux.setValorUnitarioParcela(psExcedente.multiply(vlParcelaFreteQuilo).add(vlParcelaPsMinimo));

					/*Progressivo Excedente*/
				}else if("P".equals(tabelaPreco.getTpCalculoFretePeso().getValue())) {

					BigDecimal psExcedente = psReferencia.subtract(tabelaPreco.getPsMinimo());
					BigDecimal vlParcelaPsReferencia = getValorUnitarioFretePesoFaixaProgressiva(tabelaPreco.getIdTabelaPreco(),
							parcelaPreco.getIdParcelaPreco(),psReferencia,null,calculoFrete.getIdTarifa(),
							calculoFrete.getIdTarifa()).getValorUnitarioParcela();

					valorFaixaProgressivaDTOAux = getValorUnitarioFretePesoFaixaProgressiva(tabelaPreco.getIdTabelaPreco(),
							parcelaPreco.getIdParcelaPreco(), tabelaPreco.getPsMinimo(), vlParcelaFreteQuilo, idTarifaPreco,
							calculoFrete.getIdTarifa());
					BigDecimal vlParcelaPsMinimo = valorFaixaProgressivaDTOAux.getValorUnitarioParcela();

					valorFaixaProgressivaDTOAux.setValorUnitarioParcela(psExcedente.multiply(vlParcelaPsReferencia).add(vlParcelaPsMinimo));
				}else if ("D".equals(tabelaPreco.getTpCalculoFretePeso().getValue()) || "T".equals(tabelaPreco.getTpCalculoFretePeso().getValue())) {

					valorFaixaProgressivaDTOAux = getValorUnitarioFretePesoFaixaProgressiva(tabelaPreco.getIdTabelaPreco(),
							parcelaPreco.getIdParcelaPreco(), psReferencia, vlParcelaFreteQuilo, idTarifaPreco,
							calculoFrete.getIdTarifa());

				}
			}

		}

		return valorFaixaProgressivaDTOAux;
	}


	/**
	 * Obtem o valor da faixa através dos paramteros passados
	 *
	 * @param  idTabelaPreco
	 * @param  idParcelaPreco
	 * @param  psReferencia
	 * @param  vlParcelaFreteQuilo
	 * @param  idTarifaPreco
	 * @param  idTarifaPrecoOriginal
	 * @return ValorFaixaProgressivaDTO
	 */
	private ValorFaixaProgressivaDTO getValorUnitarioFretePesoFaixaProgressiva(Long idTabelaPreco, Long idParcelaPreco,
																 BigDecimal psReferencia, BigDecimal vlParcelaFreteQuilo,
																 Long idTarifaPreco, Long idTarifaPrecoOriginal) {

		Boolean noUniqueResult = Boolean.FALSE;

		/*Obtem o valor da faixa atraves dos parametros passados*/
		ValorFaixaProgressiva valorFaixaProgressiva = null;
		try {
			valorFaixaProgressiva = getCalculoParcelaFreteDAO().findValorFaixaProgressivaEnquadrada(idTabelaPreco, idParcelaPreco, psReferencia, null, null, idTarifaPreco, null,false);
		} catch (HibernateSystemException hse) {
			if (hse.getCause() instanceof NonUniqueResultException) {
				/*Caso nao encontrar a faixa progressiva através dos parametros normais,
				verifica se o id da tarifa original não é nulo e tenta buscar através da tarifa original*/
				if(idTarifaPreco == null && idTarifaPrecoOriginal != null){
					try {
						valorFaixaProgressiva = getCalculoParcelaFreteDAO().findValorFaixaProgressivaEnquadrada(idTabelaPreco, idParcelaPreco, psReferencia, null, null, idTarifaPrecoOriginal, null,false);
					} catch (HibernateSystemException hsex) {
						if (hsex.getCause() instanceof NonUniqueResultException) {
							noUniqueResult = Boolean.TRUE;
							idTarifaPreco  = idTarifaPrecoOriginal;
						}else{
							throw hsex;
		}
					}
				}else{
					noUniqueResult = Boolean.TRUE;
				}
			} else {
				throw hse;
			}
		}/*try*/


		/*Mostra a mensagem de erro informano que retornou mais de uma faixa progressiva*/
		if(noUniqueResult){

			StringBuilder info = new StringBuilder()
			.append(" Retornou mais de um registro a consulta pelo  ")
			.append(" ValorFaixaProgressivaEnquadrada - Parâmetros: ")
			.append(" {idTabelaPreco: ").append(idTabelaPreco)
			.append(" , idParcelaPreco: ").append(idParcelaPreco)
			.append(" , psReferencia : ").append(psReferencia)
			.append(" , idProdutoEspecifico: null , idRotaPreco: null ")
			.append(" , idTarifaPreco: ").append(idTarifaPreco)
			.append(" , dtVigenciaPromocao: null } ");

			throw new BusinessException(info.toString());
		}

		/*Informa exception caso não encontrar uma faixa*/
		if(valorFaixaProgressiva == null) {
			throw new BusinessException("LMS-30030", new Object[]{ConstantesExpedicao.CD_FRETE_PESO});
		}

		/*Retorna o valor unitario da parcela*/
		BigDecimal valorUnitarioParcela;
		if(BigDecimalUtils.hasValue(valorFaixaProgressiva.getVlFixo())) {
			valorUnitarioParcela = valorFaixaProgressiva.getVlFixo();
		} else {
			valorUnitarioParcela = vlParcelaFreteQuilo;
			if(BigDecimalUtils.hasValue(valorFaixaProgressiva.getNrFatorMultiplicacao())) {
				valorUnitarioParcela = valorUnitarioParcela.multiply(valorFaixaProgressiva.getNrFatorMultiplicacao());
			}
			if(BigDecimalUtils.hasValue(valorFaixaProgressiva.getPcDesconto())) {
				valorUnitarioParcela = BigDecimalUtils.desconto(valorUnitarioParcela, valorFaixaProgressiva.getPcDesconto());
			}
			if(BigDecimalUtils.hasValue(valorFaixaProgressiva.getVlAcrescimo())) {
				valorUnitarioParcela = valorUnitarioParcela.add(valorFaixaProgressiva.getVlAcrescimo());
			}
		}

		return new ValorFaixaProgressivaDTO(valorUnitarioParcela, valorFaixaProgressiva.getVlTaxaFixa(),
				valorFaixaProgressiva.getVlKmExtra(), idTarifaPreco != null ? idTarifaPreco : idTarifaPrecoOriginal);
	}

	/**
	 * Calcula o valor unitário de parcela de Taxa Mínima
	 * @param idTabelaPreco
	 * @param idRotaPreco
	 * @return
	 */
	private BigDecimal findValorUnitarioTarifaMinima(Long idTabelaPreco, Long idRotaPreco) {
		PrecoFrete precoFrete = getCalculoParcelaFreteDAO().findPrecoFrete(idTabelaPreco, ConstantesExpedicao.CD_TARIFA_MINIMA, idRotaPreco);
		if(precoFrete == null) {
			return BigDecimalUtils.ZERO;
		} else {
			return precoFrete.getVlPrecoFrete();
		}
	}

	/**
	 * Calcula o valor da parcela de Frete Valor
	 *
	 * @param calculoFrete
	 * @return ParcelaServico
	 */
	public ParcelaServico findParcelaFreteValor(CalculoFrete calculoFrete) {

		/*Obtem a tabela preço*/
		TabelaPreco tabelaPreco = calculoFrete.getTabelaPreco();

		/*Obtem a parcela preco*/
		String cdParcelaPreco = ConstantesExpedicao.CD_FRETE_VALOR;
		ParcelaPreco parcelaPreco = getCalculoParcelaFreteDAO().findParcelaPreco(cdParcelaPreco);
		if(parcelaPreco == null) {
			throw new BusinessException("LMS-04120", new Object[]{ConstantesExpedicao.CD_FRETE_VALOR, tabelaPreco.getTabelaPrecoString()});
		}

		BigDecimal vlUnitarioParcela = BigDecimalUtils.ZERO;
		BigDecimal vlBrutoParcela = BigDecimalUtils.ZERO;


		BigDecimal pesoMinimo = getCalculoParcelaFreteDAO().findMenorPeso(tabelaPreco.getIdTabelaPreco(), calculoFrete.getIdTarifa(), ConstantesExpedicao.CD_ADVALOREM);
		
		if(!checkPesoMinimoWithPesoReferencia(pesoMinimo, calculoFrete)){
			BigDecimal psReferencia = BigDecimalUtils.defaultBigDecimal(calculoFrete.getPsReferencia());
			BigDecimal vlMercadoria = BigDecimalUtils.defaultBigDecimal(calculoFrete.getVlMercadoria());
	
			/*
			 * Calculo Natura - Busca o valor da mercadoria baseado no campo adicional do cliente.
			 */
			if (calculoFrete.getTabelaPreco().getTpCalculoFretePeso().getValue().equals("N")){
				//Procura o devedor do frete
				Conhecimento docServ = calculoFrete.getDoctoServico();
				Long idClienteDevedor = null;
				if (docServ != null && docServ.getDevedorDocServs()!=null && !docServ.getDevedorDocServs().isEmpty()){
					DevedorDocServ devedorDocServ = docServ.getDevedorDocServs().get(0);
					idClienteDevedor = devedorDocServ.getCliente().getIdCliente();
				}
				//Se não achou, usa o cliente base
				if (idClienteDevedor == null){
					idClienteDevedor = calculoFrete.getClienteBase().getIdCliente();
				}
	
				//O valor retornado é um string. Este campo adicional vem do EDI, já formatado.
				try{
					String vlCampo = getCalculoParcelaFreteDAO().findVlMercadoriaNatura(idClienteDevedor);
					if (vlCampo !=null && vlCampo.length() > 0){
						vlMercadoria = new BigDecimal(vlCampo);
					}
				}catch (NumberFormatException nfe){
					//Gerar log???
				}
			}
	
			/*Rodoviário*/
			if(ConstantesExpedicao.MODAL_RODOVIARIO.equals(calculoFrete.getTpModal())
				|| "D".equals(tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())) {
	
				Long idTarifaPreco = calculoFrete.getIdTarifa();
				if(idTarifaPreco == null || "D".equals(tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue()) ) {
					TarifaPreco tarifaPreco = findTarifaPreco(calculoFrete);
					idTarifaPreco = tarifaPreco.getIdTarifaPreco();
				}
				vlUnitarioParcela = findValorUnitarioFreteValor(tabelaPreco, calculoFrete, idTarifaPreco, null);
				vlBrutoParcela = vlMercadoria.multiply(BigDecimalUtils.percent(vlUnitarioParcela));
	
			/*Aéreo*/
			} else if(ConstantesExpedicao.MODAL_AEREO.equals(calculoFrete.getTpModal())) {
				BigDecimal vlPrecoFrete = findValorPrecoFreteMenor(tabelaPreco.getIdTabelaPreco());
				if(CompareUtils.le(vlMercadoria.divide(psReferencia, BigDecimal.ROUND_HALF_UP), vlPrecoFrete.multiply(BigDecimalUtils.HUNDRED))) {
					ParcelaPreco parcelaPrecoAdvalorem = findParcelaPreco(tabelaPreco.getIdTabelaPreco(), ConstantesExpedicao.CD_ADVALOREM_1);
					vlUnitarioParcela = findValorUnitarioParcela(tabelaPreco.getIdTabelaPreco(), parcelaPrecoAdvalorem, psReferencia, null, null, null);
					vlBrutoParcela = vlMercadoria.multiply(BigDecimalUtils.percent(vlUnitarioParcela));
				} else {
					ParcelaPreco parcelaPrecoAdvalorem = findParcelaPreco(tabelaPreco.getIdTabelaPreco(), ConstantesExpedicao.CD_ADVALOREM_2);
					vlUnitarioParcela = findValorUnitarioParcela(tabelaPreco.getIdTabelaPreco(), parcelaPrecoAdvalorem, psReferencia, null, null, null);
					vlBrutoParcela = vlMercadoria.multiply(BigDecimalUtils.percent(vlUnitarioParcela));
				}
			}
		}

		validaValorParcela(vlUnitarioParcela);
		return new ParcelaServico(parcelaPreco, vlUnitarioParcela, vlBrutoParcela);
	}

	/**
	 * Calcula o valor unitário da parcela de Frete Valor
	 *
	 * @param tabelaPreco
	 * @param idTarifaPreco
	 * @param idRotaPreco
	 * @return BigDecimal
	 */
	private BigDecimal findValorUnitarioFreteValor(TabelaPreco tabelaPreco, CalculoFrete calculoFrete, Long idTarifaPreco, Long idRotaPreco){

		ParcelaPreco parcelaPreco = null;

		String tpAbrangencia = calculoFrete.getTpAbrangencia();

		/*Verifica a abrangencia e obtem a parcela preco*/
		if(ConstantesExpedicao.ABRANGENCIA_INTERNACIONAL.equals(tpAbrangencia)) {
			parcelaPreco = findParcelaPreco(tabelaPreco.getIdTabelaPreco(), ConstantesExpedicao.CD_ADVALOREM_1);
		} else {
			parcelaPreco = findParcelaPreco(tabelaPreco.getIdTabelaPreco(), ConstantesExpedicao.CD_ADVALOREM);
		}

		/*Se não encontrar a parcela informa BusinessException*/
		if(parcelaPreco == null) {
			throw new BusinessException("Advalorem1_Advalorem_Nao_Encontrado", new Object[]{ConstantesExpedicao.CD_FRETE_VALOR});
		}
		BigDecimal vlUnitarioParcela = null;

		/*Verifica qual o tipo da precificação e obtem e calcula o valor unitario da parcela*/
		String tpPrecificacao = parcelaPreco.getTpPrecificacao().getValue();
		if("P".equals(tpPrecificacao)) {
			PrecoFrete precoFrete = findPrecoFrete(tabelaPreco.getIdTabelaPreco(), parcelaPreco.getCdParcelaPreco(), idTarifaPreco, idRotaPreco);
			vlUnitarioParcela = precoFrete.getVlPrecoFrete();

			AjusteTarifa ajusteTarifa = calculoFrete.getAjusteTarifa();
			if(ajusteTarifa != null){
				if("A".equals(ajusteTarifa.getTpAjusteFreteValor().getValue())){
					vlUnitarioParcela = BigDecimalUtils.add(vlUnitarioParcela, ajusteTarifa.getVlFreteValor());
				}else
					if("D".equals(ajusteTarifa.getTpAjusteFreteValor().getValue())){
						vlUnitarioParcela = BigDecimalUtils.defaultBigDecimal(vlUnitarioParcela).subtract(BigDecimalUtils.defaultBigDecimal(ajusteTarifa.getVlFreteValor()));
			}
			}

		} else if("G".equals(tpPrecificacao)) {
			Generalidade generalidade = findGeneralidade(tabelaPreco.getIdTabelaPreco(), parcelaPreco.getCdParcelaPreco());
			vlUnitarioParcela = generalidade.getVlGeneralidade();
		}
		return vlUnitarioParcela;
	}

	/**
	 * Calcula o valor da parcela de Gris
	 *
	 * @param calculoFrete
	 * @return ParcelaServico
	 */
	public ParcelaServico findParcelaGRIS(CalculoFrete calculoFrete) {

		/*Obtem as generalidades*/
		Generalidade generalidade = getCalculoParcelaFreteDAO().findGeneralidade(calculoFrete.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_GRIS);
		if(generalidade == null) {
			return null;
		}

		BigDecimal vlUnitarioParcela = BigDecimal.ZERO;
		BigDecimal vlBrutoParcela = BigDecimal.ZERO;
		
		if(!checkPesoMinimoWithPesoReferencia(generalidade.getPsMinimo(), calculoFrete)) {

			/*
			 * Calculo Natura - Busca o valor da mercadoria baseado no campo adicional do cliente.
			 */
			if (calculoFrete.getTabelaPreco().getTpCalculoFretePeso().getValue().equals("N")){
				//Procura o devedor do frete
				Conhecimento docServ = calculoFrete.getDoctoServico();
				Long idClienteDevedor = null;
				if (docServ != null && docServ.getDevedorDocServs()!=null && !docServ.getDevedorDocServs().isEmpty()){
					DevedorDocServ devedorDocServ = docServ.getDevedorDocServs().get(0);
					idClienteDevedor = devedorDocServ.getCliente().getIdCliente();
				}
				//Se não achou, usa o cliente base
				if (idClienteDevedor == null){
					idClienteDevedor = calculoFrete.getClienteBase().getIdCliente();
				}
	
				//O valor retornado é um string. Este campo adicional vem do EDI, já formatado.
				BigDecimal vlMercadoria = null;
				try{
					String vlCampo = getCalculoParcelaFreteDAO().findVlMercadoriaNatura(idClienteDevedor);
					if (vlCampo !=null && !StringUtils.isEmpty(vlCampo)){
						vlMercadoria = new BigDecimal(vlCampo);
					}
				}catch (NumberFormatException nfe){
					//Gerar log???
				}
				if (vlMercadoria != null){
					calculoFrete.setVlMercadoria(vlMercadoria);
				}
			}
			
			vlUnitarioParcela = generalidade.getVlGeneralidade();
			
			vlBrutoParcela = calculoFrete.getVlMercadoria().multiply(BigDecimalUtils.percent(vlUnitarioParcela));

			/*Se vlBrutoParcela é menor que o valor minimo da generalidade*/
			if(BigDecimalUtils.hasValue(generalidade.getVlMinimo()) && CompareUtils.lt(vlBrutoParcela, generalidade.getVlMinimo())) {
				vlBrutoParcela = generalidade.getVlMinimo();
			}
		}

		ParcelaServico parcelaServico = new ParcelaServico(generalidade.getTabelaPrecoParcela().getParcelaPreco(), vlUnitarioParcela, vlBrutoParcela);
		parcelaServico.addParametroAuxiliar("vlMinimo", generalidade.getVlMinimo());
		
		if(!checkPesoMinimoWithPesoReferencia(generalidade.getPsMinimo(), calculoFrete)) {
			return findParametroGRIS(calculoFrete, parcelaServico);
		}
		return parcelaServico;
	}

	/**
	 * Aplica parametrização para parcela de Gris
	 * @param calculoFrete
	 * @param freteParcela
	 * @param vlMinimoParcela
	 * @param vlMercadoria
	 * @return
	 */
	private ParcelaServico findParametroGRIS(CalculoFrete calculoFrete, ParcelaServico freteParcela) {
		//LMS-7004
		BigDecimal vlParcela = BigDecimal.ZERO;
		
		Generalidade generalidade = getCalculoParcelaFreteDAO().findGeneralidade(calculoFrete.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_GRIS);
		
		if(generalidade == null || !checkPesoMinimoWithPesoReferencia(generalidade.getPsMinimo(), calculoFrete)){
			ParametroCliente parametroCliente = calculoFrete.getParametroCliente();
			if( (parametroCliente != null) && (!calculoFrete.getBlDescontoTotal().booleanValue()) ) {
				String tpIndicador;
				BigDecimal vlMinimoParcela = freteParcela.getParametroAuxiliar("vlMinimo");
				BigDecimal vlMinimoGris = null;

				tpIndicador = parametroCliente.getTpIndicadorMinimoGris().getValue();
				if("T".equals(tpIndicador)) {
					vlMinimoGris = vlMinimoParcela;
				} else if("V".equals(tpIndicador)) {
					vlMinimoGris = parametroCliente.getVlMinimoGris();
				} else if("A".equals(tpIndicador)) {
					vlMinimoGris = BigDecimalUtils.acrescimo(vlMinimoParcela, parametroCliente.getVlMinimoGris());
				} else if("D".equals(tpIndicador)) {
					vlMinimoGris = BigDecimalUtils.desconto(vlMinimoParcela, parametroCliente.getVlMinimoGris());
				}

				tpIndicador = parametroCliente.getTpIndicadorPercentualGris().getValue();
				BigDecimal vlGRIS = null;
				if("T".equals(tpIndicador)) {
					vlGRIS = freteParcela.getVlUnitarioParcela();
				} else if("V".equals(tpIndicador)) {
					vlGRIS = parametroCliente.getVlPercentualGris();
				} else if("D".equals(tpIndicador)) {
					vlGRIS = BigDecimalUtils.desconto(freteParcela.getVlUnitarioParcela(), parametroCliente.getVlPercentualGris());
				} else if("A".equals(tpIndicador)) {
					vlGRIS = BigDecimalUtils.acrescimo(freteParcela.getVlUnitarioParcela(), parametroCliente.getVlPercentualGris());
				} else if("P".equals(tpIndicador)) {
					vlGRIS = freteParcela.getVlUnitarioParcela().add(parametroCliente.getVlPercentualGris());
				}


				vlParcela = calculoFrete.getVlMercadoria().multiply(BigDecimalUtils.percent(vlGRIS));
				try {
				if(CompareUtils.lt(vlParcela, vlMinimoGris)) {
					vlParcela = vlMinimoGris;
				}
				} catch (IllegalArgumentException e) {
					throw new BusinessException("vlMinimo da parcela GRIS deve conter valor");
				}
				freteParcela.setVlBrutoParcela(vlParcela);
				freteParcela.setVlParcela(vlParcela);
			}
		}else{
			freteParcela.setVlBrutoParcela(vlParcela);
			freteParcela.setVlParcela(vlParcela);
		}
		return freteParcela;
	}

	
	/**
     * Calcula o valor da parcela de Paletizacao CTE 
     *
     * @param calculoFrete
     * @return ParcelaServico
     */
    public ParcelaServico findParcelaPaletizacaoCTE(CalculoFrete calculoFrete) {
        ParcelaServico parcelaPaletizacaoCTE = null;
        if(calculoFrete.getParametroCliente() != null && 
			calculoFrete.getParametroCliente().getTabelaDivisaoCliente() != null &&
			calculoFrete.getDadosCliente().getIdClienteDestinatario()!= null){
            ParcelaPreco parcela = parcelaPrecoService.findByCdParcelaPreco(ConstantesExpedicao.CD_PALETIZACAO);
            
            ServicoAdicionalCliente servicoAdicionalCliente = servicoAdicionalClienteService.findServicoAdicionalCliente(
                    calculoFrete.getParametroCliente().getTabelaDivisaoCliente().getIdTabelaDivisaoCliente(), 
                    parcela.getIdParcelaPreco());
            
            Cliente clienteDestinatario = clienteService.findById(calculoFrete.getDadosCliente().getIdClienteDestinatario()); 
            
            if(servicoAdicionalCliente!= null 
                    && BooleanUtils.isTrue(servicoAdicionalCliente.getBlCobrancaCte()) 
                    && BooleanUtils.isTrue(clienteDestinatario.getBlPaletizacao())){
                BigDecimal vlBrutoParcela = executeCalculoParcelaPaletizacaoCTE(
                        servicoAdicionalCliente.getVlValor(), 
                        servicoAdicionalCliente.getVlMinimo(),
                        calculoFrete.getPsReferencia());
                
                parcelaPaletizacaoCTE = new ParcelaServico(parcela, null, vlBrutoParcela);
            }
        }
        
        return parcelaPaletizacaoCTE;
    }
    
    private BigDecimal executeCalculoParcelaPaletizacaoCTE(BigDecimal vlValor, BigDecimal vlMinimo, BigDecimal psReferenciaCalculo) {
        if(vlValor == null || vlMinimo == null || psReferenciaCalculo == null) {
            return null;
        }
        BigDecimal vlParcela = vlValor.multiply(psReferenciaCalculo).add(vlMinimo);
        return vlParcela;
    }
    
        /**
      * Calcula o valor da parcela de Paletizacao CTE 
      *
      * @param calculoFrete
      * @return ParcelaServico
      */
     public ParcelaServico findParcelaAgendamentoEntregaCTE(CalculoFrete calculoFrete) {
         ParcelaServico parcelaPaletizacaoCTE = null;
         if(calculoFrete.getParametroCliente() != null && 
			calculoFrete.getParametroCliente().getTabelaDivisaoCliente() != null &&
			calculoFrete.getDadosCliente().getIdClienteDestinatario()!= null){
             ParcelaPreco parcela = parcelaPrecoService.findByCdParcelaPreco(ConstantesExpedicao.CD_AGENDAMENTO_COLETA);
             
             ServicoAdicionalCliente servicoAdicionalCliente = servicoAdicionalClienteService.findServicoAdicionalCliente(
                     calculoFrete.getParametroCliente().getTabelaDivisaoCliente().getIdTabelaDivisaoCliente(), 
                     parcela.getIdParcelaPreco());
					 
			Cliente clienteDestinatario = clienteService.findById(calculoFrete.getDadosCliente().getIdClienteDestinatario()); 		 
             
             if(servicoAdicionalCliente!= null 
				&& BooleanUtils.isTrue(servicoAdicionalCliente.getBlCobrancaCte())
				&& BooleanUtils.isTrue(clienteDestinatario.getBlAgendamento())){
                 BigDecimal vlBrutoParcela = executeCalculoParcelaAgendamentoEntregaCTE(
                         servicoAdicionalCliente.getVlValor(), 
                         servicoAdicionalCliente.getVlMinimo(),
                         calculoFrete);
                 parcelaPaletizacaoCTE = new ParcelaServico(parcela, null, vlBrutoParcela);
                 parcelaPaletizacaoCTE.setBlCalculoBaseVlMinimo(vlBrutoParcela.equals(servicoAdicionalCliente.getVlMinimo()));
             }
         }
         
         return parcelaPaletizacaoCTE;
     }
     
     private BigDecimal executeCalculoParcelaAgendamentoEntregaCTE(BigDecimal vlValor, BigDecimal vlMinimo, CalculoFrete calculoFrete) {
         if(vlValor == null || vlMinimo == null) {
             return null;
         }
         
         BigDecimal vlCalculoParcelasFrete = BigDecimal.ZERO;
         if(calculoFrete.getParcelasGerais() != null) {
             for (ParcelaServico parcelaServico : calculoFrete.getParcelasGerais()) {
                 if(parcelaServico.getVlParcela() == null) {
                     parcelaServico.setVlParcela(parcelaServico
                             .getVlBrutoParcela());
                 }
                 vlCalculoParcelasFrete = vlCalculoParcelasFrete.add(parcelaServico.getVlParcela());
             }
         }
         
         BigDecimal vlParcela = vlCalculoParcelasFrete.multiply(BigDecimalUtils.percent(vlValor));        
         return CompareUtils.max(vlParcela,vlMinimo);
     }
    
	/**
	 * Calcula o valor da parcela de EMEX 
	 *
	 * @param calculoFrete
	 * @return ParcelaServico
	 */
	public ParcelaServico findParcelaEMEX(CalculoFrete calculoFrete) {
		//Busca municipio de entrega		
		MunicipioFilial municipioFilialDestino = municipioFilialService.findById(calculoFrete.getMunicipioFilialDestino().getIdMunicipioFilial());
		Long idMunicipioEntrega = municipioFilialDestino.getMunicipio().getIdMunicipio(); 
		
		ParcelaServico parcelaEMEX = null;
		
		if (calculoFrete.getTabelaPreco() != null){
			TabelaMunicipioEMEX tabelaMunicipioEMEXDestino = tabelaMunicipioEMEXService.findByTabelaMunicipio(calculoFrete.getTabelaPreco().getIdTabelaPreco(),idMunicipioEntrega);
			
			if (tabelaMunicipioEMEXDestino != null){
				parcelaEMEX = executeCalculoParcelaEMEX(calculoFrete);
			}
		}
		
		return parcelaEMEX;
	}
	
	
	private ParcelaServico executeCalculoParcelaEMEX(CalculoFrete calculoFrete) {
		
		Generalidade generalidade = getCalculoParcelaFreteDAO().findGeneralidade(calculoFrete.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_EMEX);
		if(generalidade == null) {
			return null;
		}
		
		BigDecimal fatorPeso = BigDecimalUtils.round(calculoFrete.getPsReferencia().divide(BigDecimalUtils.HUNDRED),0,BigDecimalUtils.ROUND_UP);
		BigDecimal vlGeneralidade = new BigDecimal(generalidade.getVlGeneralidade().toPlainString());
		BigDecimal vlMercadoria = calculoFrete.getVlMercadoria().multiply(vlGeneralidade.divide(BigDecimalUtils.HUNDRED));
		BigDecimal vlParcela = generalidade.getVlMinimo().multiply(fatorPeso).add(vlMercadoria);
		
		/*Seta os valores da parcela*/
		ParcelaServico parcelaServico = new ParcelaServico(generalidade.getTabelaPrecoParcela().getParcelaPreco(), vlGeneralidade, vlParcela);
		
		BigDecimal vlMinimoParcela = BigDecimalUtils.ZERO;
		if(!checkPesoMinimoWithPesoReferencia(generalidade.getPsMinimo(), calculoFrete)) {
			vlMinimoParcela = BigDecimalUtils.hasValue(generalidade.getVlMinimo()) ? generalidade.getVlMinimo() : BigDecimalUtils.ZERO;
		}
		parcelaServico.addParametroAuxiliar("vlMinimo", vlMinimoParcela);

		parcelaServico = findParametroEMEX(calculoFrete, parcelaServico);
		
		return parcelaServico;
	}
	
	/**
	 * Aplica parametrização para parcela de EMEX
	 *
	 * @param calculoFrete
	 * @param freteParcela
	 * @return ParcelaServico
	 */
	private ParcelaServico findParametroEMEX(CalculoFrete calculoFrete, ParcelaServico freteParcela) {
		ParametroCliente parametroCliente = calculoFrete.getParametroCliente();
		
		if( (parametroCliente != null) && (!calculoFrete.getBlDescontoTotal().booleanValue()) ) {
			
			GeneralidadeCliente generalidadeCliente = findGeneralidadeCliente(calculoFrete, freteParcela);
			
			if(generalidadeCliente != null) {
			
				String tpIndicador;
				String tpIndicadorMinimo;
				BigDecimal vlMinimoParcela = freteParcela.getParametroAuxiliar("vlMinimo");
				BigDecimal vlMinimoEMEX = null;
			
				
			
				/** Calcula VL_MINIMO */
				if(BigDecimalUtils.hasValue(vlMinimoParcela)) {
					tpIndicadorMinimo = generalidadeCliente.getTpIndicadorMinimo().getValue();
					if("T".equals(tpIndicadorMinimo)) {
						vlMinimoEMEX = vlMinimoParcela;
					} else if("V".equals(tpIndicadorMinimo)) {
						vlMinimoEMEX = generalidadeCliente.getVlMinimo();
					} else if("A".equals(tpIndicadorMinimo)) {
						vlMinimoEMEX = BigDecimalUtils.acrescimo(vlMinimoParcela, generalidadeCliente.getVlMinimo());
					} else if("D".equals(tpIndicadorMinimo)) {
						vlMinimoEMEX = BigDecimalUtils.desconto(vlMinimoParcela, generalidadeCliente.getVlMinimo());
					}
				}

				tpIndicador = generalidadeCliente.getTpIndicador().getValue();
				BigDecimal vlEMEX = null;
				if("T".equals(tpIndicador)) {
					vlEMEX = freteParcela.getVlUnitarioParcela();
				} else if("V".equals(tpIndicador)) {
					vlEMEX = generalidadeCliente.getVlGeneralidade();
				} else if("D".equals(tpIndicador)) {
					vlEMEX = BigDecimalUtils.desconto(freteParcela.getVlUnitarioParcela(), generalidadeCliente.getVlGeneralidade());
				} else if("A".equals(tpIndicador)) {
					vlEMEX = BigDecimalUtils.acrescimo(freteParcela.getVlUnitarioParcela(), generalidadeCliente.getVlGeneralidade());
				}
				
				BigDecimal fatorPeso = BigDecimalUtils.round(calculoFrete.getPsReferencia().divide(BigDecimalUtils.HUNDRED),0,BigDecimalUtils.ROUND_UP);
				BigDecimal vlMercadoria = BigDecimalUtils.hasValue(vlEMEX) ? calculoFrete.getVlMercadoria().multiply(vlEMEX.divide(BigDecimalUtils.HUNDRED)) : BigDecimal.ZERO;
				BigDecimal vlParcela = BigDecimalUtils.hasValue(vlMinimoEMEX) ? vlMinimoEMEX.multiply(fatorPeso).add(vlMercadoria) : vlMercadoria;
				
				freteParcela.setVlUnitarioParcela(vlParcela);
				freteParcela.setVlBrutoParcela(vlParcela);
				
			}

		}
		
		
		return freteParcela;
	}
	
	/**
	 * Calcula o valor da parcela de TRT (CalcularTaxaRestricaoTransporte)
	 *
	 * @param calculoFrete
	 * @return ParcelaServico
	 */
	public ParcelaServico findParcelaTRT(CalculoFrete calculoFrete) {
		//Procura o devedor do frete
		Conhecimento docServ = calculoFrete.getDoctoServico();
		Long idClienteDevedor = null;
		if (docServ != null && docServ.getDevedorDocServs()!=null && !docServ.getDevedorDocServs().isEmpty()){
			DevedorDocServ devedorDocServ = docServ.getDevedorDocServs().get(0);
			if( devedorDocServ.getCliente() != null ){
			idClienteDevedor = devedorDocServ.getCliente().getIdCliente();
		}
		}
		//Se não achou, usa o cliente base
		if (idClienteDevedor == null){
			idClienteDevedor = calculoFrete.getClienteBase().getIdCliente();
		}
		//Busca municipio de entrega		
		MunicipioFilial municipioFilialDestino = municipioFilialService.findById(calculoFrete.getMunicipioFilialDestino().getIdMunicipioFilial());
		Long idMunicipioEntrega = municipioFilialDestino.getMunicipio().getIdMunicipio(); 

		ParcelaServico parcelaTRT = null;
		if(idClienteDevedor != null && findParcelaTRTComParametroCalculoPreparado(
				calculoFrete, idMunicipioEntrega, idClienteDevedor)){

			parcelaTRT = executeCalculoParcelaTRT(calculoFrete);
		}
		return parcelaTRT;
	}

	private Boolean findParcelaTRTComParametroCalculoPreparado(
			CalculoFrete calculoFrete, Long idMunicipioEntrega, Long idClienteDevedor) {
		
		Boolean cobraPorCliente = cobraPorCliente(calculoFrete, idMunicipioEntrega, idClienteDevedor);
		if(cobraPorCliente != null){
			return cobraPorCliente;
		}
		
		Boolean cobraPorTabela = cobraPorTabela(calculoFrete, idMunicipioEntrega, idClienteDevedor);
		if(cobraPorTabela != null){
			return cobraPorTabela;
		}
		
		if ( tabelaPadraoDoModalCobraTRT(calculoFrete, idMunicipioEntrega)){
			return true;
		}
		return false;
	}
	
	private Boolean cobraPorTabela(CalculoFrete calculoFrete,
			Long idMunicipioEntrega, Long idClienteDevedor) {
		List<MunicipioTrtCliente> municipiosCobraPorTabela = municipioTrtClienteService.cobraPorTabela(calculoFrete.getTabelaPreco().getIdTabelaPreco());
		if(municipiosCobraPorTabela != null && municipiosCobraPorTabela.size() > 0){
			MunicipioTrtCliente cobraMunicipio = findMunicipioTabela(municipiosCobraPorTabela, idMunicipioEntrega);
			if( cobraMunicipio != null){
				return cobraMunicipio.getBlCobraTrt();
			}
			return false;
		}
		return null;
	}

	private Boolean cobraPorCliente(CalculoFrete calculoFrete,
			Long idMunicipioEntrega, Long idClienteDevedor) {
		List<MunicipioTrtCliente> municipiosCliente = municipioTrtClienteService.findTRTVigenteParaCliente(idClienteDevedor);

		if(municipiosCliente != null  && municipiosCliente.size() > 0){
			MunicipioTrtCliente municipioTrtCliente = findMunicipio(municipiosCliente, idMunicipioEntrega);
			if (municipioTrtCliente != null){
				return municipioTrtCliente.getBlCobraTrt();
			}
			return false;
		}
		return null;
	}

	private MunicipioTrtCliente findMunicipioTabela(
			List<MunicipioTrtCliente> municipiosCobraPorTabela,
			Long idMunicipioEntrega) {
		for(MunicipioTrtCliente municipio : municipiosCobraPorTabela){
			if(municipio.getMunicipio().getIdMunicipio().equals(idMunicipioEntrega)){
				return municipio;
			}
		}
		return null;
	}

	private MunicipioTrtCliente findMunicipio(
			List<MunicipioTrtCliente> municipiosCLiente, Long idMunicipioEntrega) {
		for(MunicipioTrtCliente municipio : municipiosCLiente){
			if(municipio.getMunicipio().getIdMunicipio().equals(idMunicipioEntrega)){
				return municipio;
			}
		}
		return null;
	}

	private Boolean tabelaPadraoDoModalCobraTRT(CalculoFrete calculoFrete,
			Long idMunicipioEntrega) {
	
		Long tabelaPadrao = municipioTrtClienteService.findTabelaPadraoModal(calculoFrete.getIdServico());
		
		if(tabelaPadrao == null){
			throw new BusinessException("LMS-04534");
		}
		
		return "S".equals( municipioTrtClienteService.cobraPorTabelaMunicipio(idMunicipioEntrega, tabelaPadrao));
	}

	/**
	 * Método auxiliar para calculo da parcela TRT acima
	 * 
	 * @param calculoFrete
	 * @return ParcelaServico
	 */
	public ParcelaServico executeCalculoParcelaTRT(CalculoFrete calculoFrete){
		
		ParcelaServico parcelaTRT = null;

		/*Retorna nulo caso não encontrar nenhuma generalidade*/
		Generalidade generalidade = getCalculoParcelaFreteDAO().findGeneralidade(calculoFrete.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TRT);
		if(generalidade == null) {
			return null;
		}
		BigDecimal vlTotalParcelas = BigDecimalUtils.ZERO;
		BigDecimal vlBrutoParcela = BigDecimalUtils.ZERO;
		BigDecimal vlMinimoParcela = BigDecimalUtils.ZERO;
		
		if(!checkPesoMinimoWithPesoReferencia(generalidade.getPsMinimo(), calculoFrete)) {

		/*Informa quais as parcelas generalidades devem ser calculadas*/
		calculoFrete.setVlTotalParcelas(findTiposDeParcelas(calculoFrete, ConstantesExpedicao.CD_TRT));

			vlTotalParcelas = calculoFrete.getVlTotalParcelas();
			vlBrutoParcela = vlTotalParcelas.multiply(BigDecimalUtils.percent(generalidade.getVlGeneralidade()));
			vlMinimoParcela = generalidade.getVlMinimo();

		if(BigDecimalUtils.hasValue(vlMinimoParcela)) {
			if(CompareUtils.lt(vlBrutoParcela, vlMinimoParcela)) {
				vlBrutoParcela = vlMinimoParcela;
			}
		} 
		else {
			vlMinimoParcela = BigDecimalUtils.ZERO;
		}
		}

		/*Seta os valores da parcela*/
		ParcelaServico parcelaServico = new ParcelaServico(generalidade.getTabelaPrecoParcela().getParcelaPreco(), generalidade.getVlGeneralidade(), vlBrutoParcela);
		parcelaServico.addParametroAuxiliar("vlMinimo", vlMinimoParcela);
		parcelaServico.addParametroAuxiliar("vlTotalParcelas", vlTotalParcelas);

			parcelaTRT = findParametroTRT(calculoFrete, parcelaServico);

		return parcelaTRT;
	}

	/**
	 * Aplica parametrização para parcela de TRT
	 *
	 * @param calculoFrete
	 * @param freteParcela
	 * @return ParcelaServico
	 */
	private ParcelaServico findParametroTRT(CalculoFrete calculoFrete, ParcelaServico freteParcela) {
		ParametroCliente parametroCliente = calculoFrete.getParametroCliente();
		if( (parametroCliente != null) && (!calculoFrete.getBlDescontoTotal().booleanValue()) ) {
			String tpIndicador;
			BigDecimal vlMinimoParcela = freteParcela.getParametroAuxiliar("vlMinimo");
			BigDecimal vlTotalParcelas = freteParcela.getParametroAuxiliar("vlTotalParcelas");
			BigDecimal vlMinimoTRT = null;

			tpIndicador = parametroCliente.getTpIndicadorMinimoTrt().getValue();
			if("T".equals(tpIndicador)) {
				vlMinimoTRT = vlMinimoParcela;
			} else if("V".equals(tpIndicador)) {
				vlMinimoTRT = parametroCliente.getVlMinimoTrt();
			} else if("A".equals(tpIndicador)) {
				vlMinimoTRT = BigDecimalUtils.acrescimo(vlMinimoParcela, parametroCliente.getVlMinimoTrt());
			} else if("D".equals(tpIndicador)) {
				vlMinimoTRT = BigDecimalUtils.desconto(vlMinimoParcela, parametroCliente.getVlMinimoTrt());
			}

			tpIndicador = parametroCliente.getTpIndicadorPercentualTrt().getValue();
			BigDecimal vlTRT = null;
			if("T".equals(tpIndicador)) {
				vlTRT = freteParcela.getVlUnitarioParcela();
			} else if("V".equals(tpIndicador)) {
				vlTRT = parametroCliente.getVlPercentualTrt();
			} else if("D".equals(tpIndicador)) {
				vlTRT = BigDecimalUtils.desconto(freteParcela.getVlUnitarioParcela(), parametroCliente.getVlPercentualTrt());
			} else if("A".equals(tpIndicador)) {
				vlTRT = BigDecimalUtils.acrescimo(freteParcela.getVlUnitarioParcela(), parametroCliente.getVlPercentualTrt());
			} else if("P".equals(tpIndicador)) {
				vlTRT = freteParcela.getVlUnitarioParcela().add(parametroCliente.getVlPercentualTrt());
			}

			BigDecimal vlParcela = vlTotalParcelas.multiply(BigDecimalUtils.percent(vlTRT));
			if(CompareUtils.lt(vlParcela, vlMinimoTRT)) {
				vlParcela = vlMinimoTRT;
			}

			freteParcela.setVlBrutoParcela(vlParcela);
			freteParcela.setVlParcela(vlParcela);

			/*Zerar o valor total das parcelas para não afetar o valor total do frete*/
			calculoFrete.setVlTotalParcelas(BigDecimal.ZERO);

		}
		return freteParcela;
	}

	/**
	 * Calcula o valor da parcela de Pedágio para frete modal Rodoviário
	 *
	 * @param calculoFrete
	 * @return ParcelaServico
	 */
	public ParcelaServico findParcelaPedagio(CalculoFrete calculoFrete) {
		
		BigDecimal vlParcela = BigDecimalUtils.ZERO;
		BigDecimal vlUnitarioParcela  = null;
		/*Numero de postos de pedagio*/
		BigDecimal nrPedagios = BigDecimalUtils.getBigDecimal(obtemNumeroPedagiosRota(calculoFrete));
		
		
		/*Informa o o peso referencia para o PCP - Peso para cálculo do pedágio*/
		BigDecimal pcp = BigDecimalUtils.defaultBigDecimal(calculoFrete.getPsReferencia());

		/*Se o cliente possuir peso aforado pedagio*/
		if(calculoFrete.getClienteBase() != null && BooleanUtils.isTrue(calculoFrete.getClienteBase().getBlPesoAforadoPedagio())){
			pcp = calculoFrete.getPsRealInformado();
		}else{
			if(CompareUtils.gt(BigDecimalUtils.defaultBigDecimal(calculoFrete.getPsRealInformado()), pcp)){
				pcp = calculoFrete.getPsRealInformado();
			}
		}

		/*Obtem o valor percentual pcp*/
		BigDecimal vlPerc =  BigDecimalUtils.hundredFraction(pcp);

		ParcelaPreco   parcelaPreco   = null;
		

		/*Verifica se exite e obtem o tipo de calculo do pedagio*/
		String tpCalculoPedagio = null;
		if(calculoFrete.getTabelaPreco() != null && calculoFrete.getTabelaPreco().getTpCalculoPedagio() != null){
			tpCalculoPedagio = calculoFrete.getTabelaPreco().getTpCalculoPedagio().getValue();
		}

		/*Posto fração*/
		if("P".equals(tpCalculoPedagio)){

			/*Obtem a parcela preço do posto fração*/
			parcelaPreco = findParcelaPreco(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
					ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO);

			if(parcelaPreco == null) {
					throw new BusinessException(msgErroParcelaPreco + ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO);
			}
			
			if(checkPesoMinimoByTipoPedagio(ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO, calculoFrete)){
				/*Obtem o valor da parcela através da tabela preço e parcela preço*/
				vlUnitarioParcela = findValorUnitarioParcela(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
						parcelaPreco, calculoFrete.getPsReferencia(), null, null, null);

				if(BigDecimalUtils.hasValue(nrPedagios)){
					vlParcela = nrPedagios.multiply(vlUnitarioParcela).multiply(vlPerc) ;
				}
			}

		/*Pedágio fração*/
		}else if("F".equals(tpCalculoPedagio)){

			/*Obtem a parcela preço pedágio fração através do id da tabela preço*/
			parcelaPreco = findParcelaPreco(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
					ConstantesExpedicao.CD_PEDAGIO_FRACAO);

			if (parcelaPreco == null) {
				throw new BusinessException(msgErroParcelaPreco + ConstantesExpedicao.CD_PEDAGIO_FRACAO);
			}
			
			if(checkPesoMinimoByTipoPedagio(ConstantesExpedicao.CD_PEDAGIO_FRACAO, calculoFrete)){
				if(!"T".equals(calculoFrete.getTabelaPreco().getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())
						|| !"F".equals(calculoFrete.getTabelaPreco().getSubtipoTabelaPreco().getTpSubtipoTabelaPreco())
							|| BigDecimalUtils.hasValue(nrPedagios) ){
	
				/*Obtem o valor da parcela atraves da tabela preço e da parcela preço*/
				vlUnitarioParcela = findValorUnitarioParcela(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
						parcelaPreco, calculoFrete.getPsReferencia(), null, null, null);
	
				vlParcela = vlUnitarioParcela.multiply(vlPerc);
				}else{
					vlParcela = BigDecimal.ZERO;
				}
			}

		/*Pedágio por documento*/
		}else if("D".equals(tpCalculoPedagio)){

			/*Obtem o pedágio por documento através da tabela preço*/
			parcelaPreco = findParcelaPreco(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
					ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO);

			if (parcelaPreco == null) {
				throw new BusinessException(msgErroParcelaPreco + ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO);
			}
			
			if(checkPesoMinimoByTipoPedagio(ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO, calculoFrete)){
				/*Obtem o valor unitário da parcela através da tabela preço e da parcela preço*/
				vlUnitarioParcela = findValorUnitarioParcela(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
						parcelaPreco, calculoFrete.getPsReferencia(), null, null, null);
	
				vlParcela = vlUnitarioParcela;
			}

		/*Pedágio faixa de peso*/
		}else if("X".equals(tpCalculoPedagio)){

			/*Obtem faixa peso através da parcela preço através da tabela preço*/
			parcelaPreco = findParcelaPreco(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
					ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO);

			if (parcelaPreco == null) {
				throw new BusinessException(msgErroParcelaPreco + ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO);
			}
			
			if(checkPesoMinimoByTipoPedagio(ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO, calculoFrete)){

				/*Obtem o valor unitário da parcela através da tabela preço e da parcela preço*/
				vlUnitarioParcela = findValorUnitarioParcela(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
						parcelaPreco, calculoFrete.getPsReferencia(), null, null, null);
	
				/*Obtem o fator multiplicacao*/
				BigDecimal vlFator = obtemFatorPedagio(pcp);
	
				vlParcela = vlUnitarioParcela.multiply(vlFator);
			}
		}

		

		/*O retorno da parcela serviço deve conter a parcela pedágio IDPedágio
		solicitação feita pelo Eri em 24/07/2009*/
		ParcelaPreco parcelaPrecoPedagio = findParcelaPreco(ConstantesExpedicao.CD_PEDAGIO);
		ParcelaServico parcelaServico = new ParcelaServico(parcelaPrecoPedagio);

		parcelaServico.setVlUnitarioParcela(vlUnitarioParcela);
		parcelaServico.setVlBrutoParcela(vlParcela);
		parcelaServico.setVlParcela(vlParcela);
		parcelaServico.addParametroAuxiliar("nrPostosPedagio", nrPedagios);

		return findParametroPedagio(calculoFrete, parcelaServico);
	}
	
	private boolean checkPesoMinimoByTipoPedagio(String tipoPedagio, CalculoFrete calculoFrete){
		Generalidade generalidade = getCalculoParcelaFreteDAO().findGeneralidade(calculoFrete.getTabelaPreco().getIdTabelaPreco(), tipoPedagio);
		
		if(!checkPesoMinimoWithPesoReferencia(generalidade == null ? null : generalidade.getPsMinimo(), calculoFrete)) {
			return true;
		}
		return false;
	}


	/**
	 * Obtem o numero de pegadios entre municipios
	 *
	 * @param  calculoFrete
	 * @return Integer
	 */
	private Integer obtemNumeroPedagiosRota(CalculoFrete calculoFrete){

		Long idMunicipioOrigem = calculoFrete.getRestricaoRotaOrigem().getIdMunicipio();
		Long idMunicipioDestino = calculoFrete.getRestricaoRotaDestino().getIdMunicipio();
		MunicipioFilial municipioFilialDestino = calculoFrete.getMunicipioFilialDestino();
		if( (municipioFilialDestino == null)
				|| (Boolean.FALSE.equals(municipioFilialDestino.getBlPadraoMcd())) ) {
			Cliente clienteConsignatario = calculoFrete.getDoctoServico().getClienteByIdClienteConsignatario();
			idMunicipioDestino = clienteConsignatario.getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio();
		}
		return findQtdPostosPassagemEntreMunicipios(idMunicipioOrigem, idMunicipioDestino,calculoFrete.getIdServico());
	}

	private Integer findQtdPostosPassagemEntreMunicipios(Long idMunicipioOrigem, Long idMunicipioDestino,Long idServico){
		Object[] origem = municipioFilialService.findMunicipioFilialVigenteByMunicipioPadraoMCD(idMunicipioOrigem);
		Object[] destino = municipioFilialService.findMunicipioFilialVigenteByMunicipioPadraoMCD(idMunicipioDestino);

		if (origem == null || destino == null)
			throw new BusinessException("LMS-29102");

		Long idMunicipioFilialOrigem = (Long) origem[0];
		Long idMunicipioFilialDestino = (Long) destino[0];

		YearMonthDay dtConsulta = JTDateTimeUtils.getDataAtual();
		Integer qtdPostosPassagemOrigem = new Integer(0);
		Integer qtdPostosPassagemDestino = new Integer(0);

		if (!idMunicipioDestino.equals(idMunicipioOrigem)) {
			qtdPostosPassagemDestino = postoPassagemMunicipioService.findQtdPostosPassagemEntreMunicipioEFilial(idMunicipioFilialDestino, dtConsulta);
		} else {
			qtdPostosPassagemDestino = postoPassagemMunicipioService.findQtdPostosPassagemBidirecionalEntreMunicipioEFilial(idMunicipioFilialDestino, dtConsulta);
		}

		Integer qtdPostosPassagemTrecho = getCalculoParcelaFreteDAO().findQtdPostoPassagemEntreFiliais(
				municipioFilialService.findById(idMunicipioFilialOrigem).getFilial().getIdFilial(),
				municipioFilialService.findById(idMunicipioFilialDestino).getFilial().getIdFilial(),
				idServico,
				dtConsulta
		);

		return new Integer(qtdPostosPassagemOrigem.intValue() + qtdPostosPassagemDestino.intValue() + qtdPostosPassagemTrecho.intValue());
	}

	/**
	 * Calcula o valor da parcela de Pedágio para frete modal Aéreo
	 *
	 * @param calculoFrete
	 * @param blColetaInterior
	 * @param blEntregaInterior
	 * @return ParcelaServico
	 */
	public ParcelaServico findParcelaPedagio(CalculoFrete calculoFrete,Boolean blColetaInterior,Boolean blEntregaInterior) {

		/*Verifica se exite e obtem o tipo de calculo do pedagio*/
		String tpCalculoPedagio = null;
		if(calculoFrete.getTabelaPreco() != null && calculoFrete.getTabelaPreco().getTpCalculoPedagio() != null){
			tpCalculoPedagio = calculoFrete.getTabelaPreco().getTpCalculoPedagio().getValue();
		}

		if("P".equals(tpCalculoPedagio)) {
			tpCalculoPedagio = ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO;
			
		} else if("F".equals(tpCalculoPedagio)) {
			tpCalculoPedagio = ConstantesExpedicao.CD_PEDAGIO_FRACAO;
			
		} else if("D".equals(tpCalculoPedagio)) {
			tpCalculoPedagio = ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO;
			
		} else if("X".equals(tpCalculoPedagio)) {
			tpCalculoPedagio = ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO;
			
		} else {
			tpCalculoPedagio = ConstantesExpedicao.CD_PEDAGIO;
			
		}
		
		Generalidade generalidade = getCalculoParcelaFreteDAO().findGeneralidade(calculoFrete.getTabelaPreco().getIdTabelaPreco(), tpCalculoPedagio);
		if((generalidade == null) || (blColetaInterior.equals(Boolean.FALSE)
				&& blEntregaInterior.equals(Boolean.FALSE))) {
			return null;
		}

		// LMSA-3088
		ParcelaPreco parcelaPrecoPedagio = findParcelaPreco(ConstantesExpedicao.CD_PEDAGIO);
		ParcelaServico parcelaServico = new ParcelaServico(parcelaPrecoPedagio);

		parcelaServico.setVlBrutoParcela(generalidade.getVlGeneralidade());
		parcelaServico.setVlUnitarioParcela(generalidade.getVlGeneralidade());
		parcelaServico.addParametroAuxiliar("nrPostosPedagio", BigDecimal.ONE);

		return findParametroPedagio(calculoFrete, parcelaServico);
	}

	/**
	 * Obtem o valor do fator multiplicação do pedágio
	 *
	 * @param  pcp
	 * @return BigDecimal
	 */
	private BigDecimal obtemFatorPedagio(BigDecimal pcp){

		BigDecimal vlFator = BigDecimal.ONE;
		if(CompareUtils.le(pcp, BigDecimalUtils.HUNDRED)){
			vlFator = BigDecimalUtils.ONE;
		}else if(CompareUtils.between(pcp, new  BigDecimal(101), new  BigDecimal(500))){
			vlFator = BigDecimalUtils.TWO;
		}else if(CompareUtils.between(pcp, new  BigDecimal(501), new  BigDecimal(1000))){
			vlFator = BigDecimalUtils.TREE;
		}else if(CompareUtils.gt(pcp, new  BigDecimal(1000))){
			vlFator = BigDecimalUtils.TREE.add(pcp.divide(new BigDecimal(1000), 0, BigDecimal.ROUND_FLOOR));
		}
		return vlFator;
	}

	/**
	 * Aplica parametrização da parcela de Pedágio
	 *
	 * @param calculoFrete
	 * @param freteParcela
	 * @param nrPostosPedagio
	 * @return ParcelaServico
	 */
	private ParcelaServico findParametroPedagio(CalculoFrete calculoFrete, ParcelaServico parcelaServico) {
		ParcelaPreco parcelaPreco = null;
		BigDecimal   vlUnitarioParcela = null;

		/*Se o cliente possuir peso aforado pedagio*/
		BigDecimal pcp = calculoFrete.getPsReferencia();
		if(calculoFrete.getClienteBase() != null && BooleanUtils.isTrue(calculoFrete.getClienteBase().getBlPesoAforadoPedagio())){
			pcp = calculoFrete.getPsRealInformado();
		}

		BigDecimal vlPerc = BigDecimalUtils.hundredFraction(pcp);

		/*Obtem o parametro cliente vinculado*/
		ParametroCliente parametroCliente = calculoFrete.getParametroCliente();

		/*Verifica se o parametro cliente existe*/
		if(parametroCliente != null && !calculoFrete.getBlDescontoTotal().booleanValue() ) {

			BigDecimal vlParcela = null;
			
			/*Obtem o fator multiplicacao pedágio*/
			BigDecimal vlFator = obtemFatorPedagio(pcp);

			/*Obtem o numero de postoss pedagio através da ParcelaServico*/
			BigDecimal nrPostosPedagio = parcelaServico.getParametroAuxiliar("nrPostosPedagio");

			/*Obtem o indicador do pedagio*/
			String tpIndicador = parametroCliente.getTpIndicadorPedagio().getValue();

			/*Obtem o tipo de calculo do pedagio*/
			String tpCalculoPedagio = null;
			if(calculoFrete.getTabelaPreco() != null
					&& calculoFrete.getTabelaPreco().getTpCalculoPedagio() != null){
				tpCalculoPedagio = calculoFrete.getTabelaPreco().getTpCalculoPedagio().getValue();
			}

			/* 28877 - Propriedade que armazena valor bruto, pois a propriedade vlBrutoParcela está sendo atualizado com o desconto/acréscimo
			 * e a cada recálculo estava dando um desconto sobre o valor já com desconto. Foi criado uma nova propriedade na classe ParcelaServico
			 * por solicitaçãodo analista Marco Vanoni conforme sugerido pelo Andrei da CWI, para não impactar em outras funcionalidades do sistema */
			parcelaServico.setVlParcelaOriginal(parcelaServico.getVlBrutoParcela());
			/* **********************************************************************************************************************/

			/*Verifica qual o tipo de indicador do pedágio através dos parametros do cliente.
			Calcula o valor da parcela pedágio através através do tipo de calculo do pedágio
			da tabela preço*/
			if("T".equals(tpIndicador)) {
				vlParcela = parcelaServico.getVlBrutoParcela();

			} else if("V".equals(tpIndicador)) {

				if("P".equals(tpCalculoPedagio)){
					vlParcela = nrPostosPedagio.multiply(parametroCliente.getVlPedagio()).multiply(vlPerc);
				}else if("F".equals(tpCalculoPedagio)){
					vlParcela = parametroCliente.getVlPedagio().multiply(vlPerc);
				}else if("D".equals(tpCalculoPedagio)){
					vlParcela = parametroCliente.getVlPedagio();
				}else if("X".equals(tpCalculoPedagio)){
					vlParcela = parametroCliente.getVlPedagio().multiply(vlFator);
				}

			} else if("A".equals(tpIndicador)) {

				vlParcela = BigDecimalUtils.acrescimo(parcelaServico.getVlBrutoParcela(),parametroCliente.getVlPedagio());

			} else if("D".equals(tpIndicador)) {

				vlParcela = BigDecimalUtils.desconto(parcelaServico.getVlBrutoParcela(),parametroCliente.getVlPedagio());

			/*Pedágio posto fração*/
			} else if("P".equals(tpIndicador)) {

				if((BigDecimalUtils.hasValue(parametroCliente.getVlPedagio()))) {
					vlParcela = parametroCliente.getVlPedagio().multiply(pcp);
				} else {
					parcelaPreco = findParcelaPreco(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
							ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO);

					if (parcelaPreco == null) {
						throw new BusinessException(msgErroParcelaPreco + ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO);
				}

					vlUnitarioParcela = findValorUnitarioParcela(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
							parcelaPreco, calculoFrete.getPsReferencia(), null, null, null);

					vlParcela = vlUnitarioParcela.multiply(pcp);
				}
			} else if("F".equals(tpIndicador)) {

				vlParcela = parametroCliente.getVlPedagio();

			/*Pedágio fração*/
			} else if("Q".equals(tpIndicador)) {

				if((BigDecimalUtils.hasValue(parametroCliente.getVlPedagio()))) {
					vlParcela = parametroCliente.getVlPedagio().multiply(vlPerc);
				} else {
					parcelaPreco = findParcelaPreco(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
							ConstantesExpedicao.CD_PEDAGIO_FRACAO);

					if (parcelaPreco == null) {
						throw new BusinessException(msgErroParcelaPreco + ConstantesExpedicao.CD_PEDAGIO_FRACAO);
				}

					vlUnitarioParcela = findValorUnitarioParcela(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
							parcelaPreco, calculoFrete.getPsReferencia(), null, null, null);

					vlParcela = vlUnitarioParcela.multiply(vlPerc);
				}

			/*Pedágio faixa de peso*/
			} else if("X".equals(tpIndicador)) {

				if((BigDecimalUtils.hasValue(parametroCliente.getVlPedagio()))) {
					vlParcela = parametroCliente.getVlPedagio().multiply(vlFator);
				} else {

					parcelaPreco = findParcelaPreco(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
							ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO);

					if (parcelaPreco == null) {
						throw new BusinessException(msgErroParcelaPreco + ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO);
				}

					vlUnitarioParcela = findValorUnitarioParcela(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
							parcelaPreco, calculoFrete.getPsReferencia(), null, null, null);

					vlParcela = vlUnitarioParcela.multiply(vlFator);
			}
				/*Pedágio faixa de peso*/
			} else if("O".equals(tpIndicador)) {
				if((BigDecimalUtils.hasValue(parametroCliente.getVlPedagio()))) {
					vlParcela = parametroCliente.getVlPedagio().multiply(nrPostosPedagio).multiply(vlPerc);
				} else {
					parcelaPreco = findParcelaPreco(calculoFrete.getTabelaPreco().getIdTabelaPreco(),
							ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO);

					if (parcelaPreco == null) {
						throw new BusinessException(msgErroParcelaPreco + ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO);
			}

					vlUnitarioParcela = findValorUnitarioParcela(calculoFrete.getTabelaPreco().getIdTabelaPreco(),parcelaPreco, calculoFrete.getPsReferencia(), null, null, null);

					vlParcela = vlUnitarioParcela.multiply(nrPostosPedagio).multiply(vlPerc);
				}
			}
			parcelaServico.setVlBrutoParcela(vlParcela);
			parcelaServico.setVlParcela(vlParcela);
			
			String tipoPedagio = TIPOS_PEDAGIO.get(tpCalculoPedagio);
			
			if(tipoPedagio != null && !checkPesoMinimoByTipoPedagio(tipoPedagio, calculoFrete)){
				parcelaServico.setVlBrutoParcela(BigDecimal.ZERO);
				parcelaServico.setVlParcela(BigDecimal.ZERO);
			}
		}
		return parcelaServico;
	}
	
	/**
	 * Calcula valor das parcelas de generalidade
	 *
	 * @param calculoFrete
	 * @return List
	 */
	public List<ParcelaServico> executeCalculoParcelasGeneralidade(CalculoFrete calculoFrete) {

		String cdParcelaPreco = null;

		BigDecimal psReferencia = calculoFrete.getPsReferencia() == null ? BigDecimalUtils.ZERO : calculoFrete.getPsReferencia();
		BigDecimal vlBrutoParcela = BigDecimalUtils.ZERO;
		BigDecimal vlMinimoParcela = BigDecimalUtils.ZERO;

		ParcelaPreco parcelaPreco = null;
		List<ParcelaServico> result = new ArrayList<ParcelaServico>();
		boolean blCalculaParcela = true;

		/*Parcelas especiais não serão consideradas*/
		String[] cdParcelasEspeciais = new String[] {
				ConstantesExpedicao.CD_GRIS,
				ConstantesExpedicao.CD_PEDAGIO,
				ConstantesExpedicao.CD_TRT,
				ConstantesExpedicao.CD_TDE,
				ConstantesExpedicao.CD_TAXA_SUFRAMA,
				ConstantesExpedicao.CD_ADVALOREM,
				ConstantesExpedicao.CD_ADVALOREM_1,
				ConstantesExpedicao.CD_ADVALOREM_2,
				ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO,
				ConstantesExpedicao.CD_PEDAGIO_FRACAO,
				ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO,
				ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO,
				ConstantesExpedicao.CD_VEICULO_DEDICADO,
				ConstantesExpedicao.CD_AGENDAMENTO_COLETA_ENTREGA,
				ConstantesExpedicao.CD_TAXA_PALETIZACAO,
				ConstantesExpedicao.CD_CUSTO_DESCARGA,
				ConstantesExpedicao.CD_MINIMO_REENTREGA,
				// LMS-3635 - desconsiderar parcela de refaturamento
				ConstantesExpedicao.CD_REFATURAMENTO,
				ConstantesExpedicao.CD_PALETIZACAO,
				ConstantesExpedicao.CD_AGENDAMENTO_ENTREGA,
				ConstantesExpedicao.CD_EMEX
		};
		TabelaPreco tabelaPreco = calculoFrete.getTabelaPreco();
		List<Generalidade> generalidades = getCalculoParcelaFreteDAO().findGeneralidades(tabelaPreco.getIdTabelaPreco(), cdParcelasEspeciais);

		Boolean blEmbuteParcela;
		for (Generalidade generalidade : generalidades) {

			blEmbuteParcela = false;
			parcelaPreco = generalidade.getTabelaPrecoParcela().getParcelaPreco();
			cdParcelaPreco = parcelaPreco.getCdParcelaPreco();
			blCalculaParcela = true;

			if(ConstantesExpedicao.CD_TAD.equals(cdParcelaPreco)) {
				/*Calcula se o Municipio destino possui dificuldade de entrega*/
				blCalculaParcela = calculoFrete.getBlDificuldadeEntrega().booleanValue();

			} else if(ConstantesExpedicao.CD_TAS.equals(cdParcelaPreco)) {
				/*Calcula se a UF destino cobra Taxa TAS*/
				blCalculaParcela = calculoFrete.getBlUFDestinoCobraTas().booleanValue();

			} else if( (ConstantesExpedicao.CD_TAXA_FLUVIAL.equals(cdParcelaPreco)
							|| ConstantesExpedicao.CD_TAXA_FLUVIAL_BALSA.equals(cdParcelaPreco)
								|| ConstantesExpedicao.CD_SEGURO_FLUVIAL.equals(cdParcelaPreco)) ) {

				/*Calcula se o Municipio entrega ou coleta cobra Taxa Fluvial*/
				blCalculaParcela = executeCobrancaTaxaFluvial(calculoFrete);

			} else if(ConstantesExpedicao.CD_SUFRAMA.equals(cdParcelaPreco)){
				blCalculaParcela = calculoFrete.getBlUFDestinoCobraSuframa().booleanValue();				
				}
			/*Verifica se deve ser calculado as generalidades*/
			if(blCalculaParcela) {

				Boolean  blTabelaY   = ( parcelaPreco.getBlEmbuteParcela() && "Y".equals(tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco()) && CompareUtils.le(calculoFrete.getPsReferencia(), tabelaPreco.getPsMinimo()) );
				Boolean  blTabelaFOB = ( parcelaPreco.getBlEmbuteParcela() && "T".equals(tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue()) && "F".equals(tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco()) && CompareUtils.le(calculoFrete.getPsReferencia(), tabelaPreco.getPsMinimo()) );

				/*Verifica se deve ser embutido a parcela*/
				blEmbuteParcela =  blTabelaY || blTabelaFOB;

				/*Calcula  o valor da parcela*/
				BigDecimal psMinino = generalidade.getPsMinimo();
				if( (psMinino != null) && CompareUtils.ge(psMinino, psReferencia)) {
					vlBrutoParcela = BigDecimalUtils.ZERO;

				} else {

					if(blEmbuteParcela) {

						ParcelaServico parcelaFretePeso = calculoFrete.getParcelaGeral(ConstantesExpedicao.CD_FRETE_PESO);
						ValorFaixaProgressiva valorFaixaProgressiva = getCalculoParcelaFreteDAO().findValorFaixaProgressivaEnquadrada(
							tabelaPreco.getIdTabelaPreco(),
							parcelaFretePeso.getParcelaPreco().getIdParcelaPreco(),
							calculoFrete.getPsReferencia(),
							null,
							null,
							null,
							null,
							false
						);
						if(valorFaixaProgressiva == null) {
							throw new BusinessException("LMS-30022", new Object[]{ConstantesExpedicao.CD_FRETE_PESO});
						}
						if(BigDecimalUtils.hasValue(valorFaixaProgressiva.getPcTaxa())) {
							vlBrutoParcela = BigDecimalUtils.desconto(generalidade.getVlGeneralidade(), valorFaixaProgressiva.getPcTaxa());
						} else {
							vlBrutoParcela = generalidade.getVlGeneralidade();
						}

					} else {

						if(ConstantesExpedicao.CD_TAD.equals(cdParcelaPreco)) {

							vlBrutoParcela = generalidade.getVlGeneralidade().multiply(BigDecimalUtils.hundredFraction(calculoFrete.getPsReferencia()));

						} else if (ConstantesExpedicao.CD_SEGURO_FLUVIAL.equals(cdParcelaPreco)) {

							BigDecimal vlUnitarioParcela = generalidade.getVlGeneralidade();
							vlMinimoParcela = generalidade.getVlMinimo();
							vlBrutoParcela = calculoFrete.getVlMercadoria().multiply(BigDecimalUtils.percent(vlUnitarioParcela));
							if(BigDecimalUtils.hasValue(vlMinimoParcela) && CompareUtils.lt(vlBrutoParcela, vlMinimoParcela)) {
								vlBrutoParcela = vlMinimoParcela;
							}

						} else if (ConstantesExpedicao.CD_TAXA_FLUVIAL_BALSA.equals(cdParcelaPreco)) {

							vlBrutoParcela = generalidade.getVlGeneralidade();

						} else if(ConstantesExpedicao.CD_SUFRAMA.equals(cdParcelaPreco)){

							vlBrutoParcela = generalidade.getVlGeneralidade();

						}  else if(ConstantesExpedicao.CD_TRANSFERENCIA.equals(cdParcelaPreco)){
							vlBrutoParcela = BigDecimalUtils.ZERO;
							if("VL".equals(generalidade.getTabelaPrecoParcela().getParcelaPreco().getTpIndicadorCalculo().getValue())){
								vlBrutoParcela = generalidade.getVlGeneralidade();
							}else if("PE".equals(generalidade.getTabelaPrecoParcela().getParcelaPreco().getTpIndicadorCalculo().getValue())){
								vlBrutoParcela = generalidade.getVlGeneralidade().multiply(psReferencia);
							}else if("PC".equals(generalidade.getTabelaPrecoParcela().getParcelaPreco().getTpIndicadorCalculo().getValue())){
								vlBrutoParcela = calculoFrete.getVlMercadoria().multiply(generalidade.getVlGeneralidade().divide(BigDecimalUtils.HUNDRED));
							}else if("PF".equals(generalidade.getTabelaPrecoParcela().getParcelaPreco().getTpIndicadorCalculo().getValue())){
								vlBrutoParcela = calculoFrete.getVlTotalParcelas().multiply(generalidade.getVlGeneralidade().divide(BigDecimalUtils.HUNDRED));
							}else if("VU".equals(generalidade.getTabelaPrecoParcela().getParcelaPreco().getTpIndicadorCalculo().getValue())){
								if(calculoFrete.getBlCotacao()){
									vlBrutoParcela = generalidade.getVlGeneralidade().multiply(calculoFrete.getMetragemCubicaCotacao());
								}else{
									if(BigDecimalUtils.hasValue(calculoFrete.getTotalDimensao())){
										vlBrutoParcela = generalidade.getVlGeneralidade().multiply(calculoFrete.getTotalDimensao());
									}else{
										vlBrutoParcela = generalidade.getVlGeneralidade().multiply(calculoFrete.getNrCubagemCalculo());
									}
								}
							}
							
							if(BigDecimalUtils.hasValue(vlMinimoParcela) && CompareUtils.lt(vlBrutoParcela, vlMinimoParcela)) {
								vlBrutoParcela = vlMinimoParcela;
							}
						} else {

							vlBrutoParcela = generalidade.getVlGeneralidade();
						}

					}/*blEmbuteParcela*/


				}/*Calcula  o valor da parcela*/

				ParcelaServico parcelaServico = new ParcelaServico(parcelaPreco, generalidade.getVlGeneralidade(), vlBrutoParcela);

				parcelaServico.setBlEmbuteParcela(blEmbuteParcela);
				parcelaServico.addParametroAuxiliar("vlMinimo", vlMinimoParcela);
				result.add(executeParametroGeneralidade(calculoFrete, parcelaServico));

			}/*blCalculaParcela*/

		}/*for*/
		return result;
	}

	/**
	 * Verifica se  deve ser executada a cobrança de taxa fluvial na entrega ou coleta
	 *
	 * @param  calculoFrete
	 * @return Boolean
	 */
	private Boolean executeCobrancaTaxaFluvial(CalculoFrete calculoFrete){

		Boolean executeCobranca = Boolean.FALSE;

		List<OperacaoServicoLocaliza> servicosLocalizacao = new ArrayList<OperacaoServicoLocaliza>();

		if(calculoFrete.getMunicipioFilialDestino() != null){
			servicosLocalizacao = operacaoServicoLocalizaService.findOperacaoServicoLocalizacao(null, calculoFrete.getMunicipioFilialDestino().getIdMunicipioFilial(), calculoFrete.getIdServico(), JTDateTimeUtils.getDataAtual());
			if(!servicosLocalizacao.isEmpty()) {
				executeCobranca = servicosLocalizacao.get(0).getBlCobraTaxaFluvial();
		}
	}

		if(BooleanUtils.isFalse(executeCobranca)){
			if(calculoFrete.getMunicipioFilialOrigem() != null){
				servicosLocalizacao = operacaoServicoLocalizaService.findOperacaoServicoLocalizacao(null, calculoFrete.getMunicipioFilialOrigem().getIdMunicipioFilial(), calculoFrete.getIdServico(), JTDateTimeUtils.getDataAtual());
				if(!servicosLocalizacao.isEmpty()) {
					executeCobranca = servicosLocalizacao.get(0).getBlCobraTaxaFluvial();
			}
		}
		}
		return executeCobranca;
	}

	/**
	 * Aplica parametrização das parcelas de generalidade
	 *
	 * @param calculoFrete
	 * @param freteParcela
	 * @return ParcelaServico
	 */
	private ParcelaServico executeParametroGeneralidade(
			CalculoFrete calculoFrete,
			ParcelaServico parcelaFrete
	) {

		/* 28810 - Propriedade que armazena valor bruto, pois a propriedade vlBrutoParcela está sendo atualizado com o desconto/acréscimo
		 * e a cada recálculo estava dando um desconto sobre o valor já com desconto. Foi criado uma nova propriedade na classe ParcelaServico
		 * por solicitaçãodo analista Marco Vanoni conforme sugerido pelo Andrei da CWI, para não impactar em outras funcionalidades do sistema */
		if( parcelaFrete.getVlParcelaOriginal() == null ) {
			parcelaFrete.setVlParcelaOriginal(parcelaFrete.getVlBrutoParcela());
		}else{
			parcelaFrete.setVlBrutoParcela(parcelaFrete.getVlParcelaOriginal());
		}
		/* **********************************************************************************************************************/
		BigDecimal vlParcela = parcelaFrete.getVlBrutoParcela();
		
		boolean calculaParcela = checkCalculaParametroGeneralidadeByPesoMinimo(parcelaFrete, calculoFrete);
		
		if(calculaParcela && calculoFrete.getParametroCliente() != null && !calculoFrete.getBlDescontoTotal().booleanValue() ) {

			GeneralidadeCliente generalidadeCliente = findGeneralidadeCliente(calculoFrete, parcelaFrete);

			if(generalidadeCliente != null) {
				
				/** Calcula VL_MINIMO */
				String tpIndicador = "T";
				BigDecimal vlMinimoParcela = parcelaFrete.getParametroAuxiliar("vlMinimo");
				if(BigDecimalUtils.hasValue(vlMinimoParcela)) {
					tpIndicador = generalidadeCliente.getTpIndicadorMinimo().getValue();
					if("V".equals(tpIndicador)) {
						vlMinimoParcela = generalidadeCliente.getVlMinimo();
					} else if("A".equals(tpIndicador)) {
						vlMinimoParcela = BigDecimalUtils.acrescimo(vlMinimoParcela, generalidadeCliente.getVlMinimo());
					} else if("D".equals(tpIndicador)) {
						vlMinimoParcela = BigDecimalUtils.desconto(vlMinimoParcela, generalidadeCliente.getVlMinimo());
					}
				}
		
				tpIndicador = generalidadeCliente.getTpIndicador().getValue();
				String tpIndicadorCalculo = parcelaFrete.getParcelaPreco().getTpIndicadorCalculo().getValue();
				/** Valor parcela generalidade */
				if("VL".equals(tpIndicadorCalculo)) {
					if("V".equals(tpIndicador)) {
						vlParcela = generalidadeCliente.getVlGeneralidade();
					} else if("D".equals(tpIndicador)) {
						vlParcela = BigDecimalUtils.desconto(parcelaFrete.getVlBrutoParcela(), generalidadeCliente.getVlGeneralidade());
					} else if("A".equals(tpIndicador)) {
						vlParcela = BigDecimalUtils.acrescimo(parcelaFrete.getVlBrutoParcela(), generalidadeCliente.getVlGeneralidade());
					} else if("Q".equals(tpIndicador)) {
						BigDecimal vlFactor = BigDecimalUtils.hundredFraction(calculoFrete.getPsReferencia());
						if(!BigDecimalUtils.hasValue(generalidadeCliente.getVlGeneralidade())) {
							vlParcela = calculoFrete.getParcelaGeral(ConstantesExpedicao.CD_PEDAGIO).getVlParcela().multiply(vlFactor);
						} else {
							vlParcela = generalidadeCliente.getVlGeneralidade().multiply(vlFactor);
						}
					}
				/** Valor generalidade da tabela de preços  */
				} else if("PC".equals(tpIndicadorCalculo)) {
					if("V".equals(tpIndicador)) {
						vlParcela = generalidadeCliente.getVlGeneralidade();
					} else if("D".equals(tpIndicador)) {
						vlParcela = BigDecimalUtils.desconto(parcelaFrete.getVlUnitarioParcela(), generalidadeCliente.getVlGeneralidade());
					} else if("A".equals(tpIndicador)) {
						vlParcela = BigDecimalUtils.acrescimo(parcelaFrete.getVlUnitarioParcela(), generalidadeCliente.getVlGeneralidade());
					}
					
					if(!"T".equals(tpIndicador) && BigDecimalUtils.hasValue(vlParcela)) {
						vlParcela = calculoFrete.getVlMercadoria().multiply(vlParcela.divide(BigDecimalUtils.HUNDRED));
					}
				} else if("VU".equals(tpIndicadorCalculo)) {
					if("V".equals(tpIndicador)) {
						vlParcela = generalidadeCliente.getVlGeneralidade();
					} else if("D".equals(tpIndicador)) {
						vlParcela = BigDecimalUtils.desconto(parcelaFrete.getVlUnitarioParcela(), generalidadeCliente.getVlGeneralidade());
					} else if("A".equals(tpIndicador)) {
						vlParcela = BigDecimalUtils.acrescimo(parcelaFrete.getVlUnitarioParcela(), generalidadeCliente.getVlGeneralidade());
					} else if("M".equals(tpIndicador)) {
						if(calculoFrete.getBlCotacao()){
							vlParcela = generalidadeCliente.getVlGeneralidade().multiply(calculoFrete.getMetragemCubicaCotacao());
						}else{
							if(BigDecimalUtils.hasValue(calculoFrete.getTotalDimensao())){
								vlParcela = generalidadeCliente.getVlGeneralidade().multiply(calculoFrete.getTotalDimensao());
							}else{
								vlParcela = generalidadeCliente.getVlGeneralidade().multiply(calculoFrete.getNrCubagemCalculo());
							}
						}
					}
				}	
				
				if(!"T".equals(tpIndicador)) {
					if(BigDecimalUtils.hasValue(vlMinimoParcela) && CompareUtils.lt(vlParcela, vlMinimoParcela)) {
						vlParcela = vlMinimoParcela;
					}
				}
			}
		}
		if(!calculaParcela){
			vlParcela = BigDecimal.ZERO;
		}

		/*Embute na parcela frete peso o valor da generalidade*/
		if(BooleanUtils.isTrue(parcelaFrete.getBlEmbuteParcela())) {

			parcelaFrete.setVlEmbuteParcela(vlParcela);

			/*Zera valor da Generalidade*/
			parcelaFrete.setVlBrutoParcela(BigDecimalUtils.ZERO);
			parcelaFrete.setVlParcela(BigDecimalUtils.ZERO);

		} else {
			parcelaFrete.setVlBrutoParcela(vlParcela);
			parcelaFrete.setVlParcela(vlParcela);
		}

		return parcelaFrete;
	}
	
	private boolean checkCalculaParametroGeneralidadeByPesoMinimo(ParcelaServico parcelaFrete, CalculoFrete calculoFrete){
		String cdParcelaPreco = parcelaFrete.getParcelaPreco().getCdParcelaPreco();
		if(ConstantesExpedicao.CD_DESPACHO.equals(cdParcelaPreco) 
				|| ConstantesExpedicao.CD_TAS.equals(cdParcelaPreco) 
				|| ConstantesExpedicao.CD_TAXA_FLUVIAL_BALSA.equals(cdParcelaPreco)
				|| ConstantesExpedicao.CD_SEGURO_FLUVIAL.equals(cdParcelaPreco)){

			Generalidade generalidade = getCalculoParcelaFreteDAO().findGeneralidade(calculoFrete.getTabelaPreco().getIdTabelaPreco(), cdParcelaPreco);
		
			if(checkPesoMinimoWithPesoReferencia(generalidade.getPsMinimo(), calculoFrete)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Calcula o valor da parcela de Taxa Coleta Entrega
	 *
	 * @param calculoFrete
	 * @param cdPrefixoParametroParcela
	 * @param blInterior
	 * @param blEmergencia
	 * @return ParcelaServico
	 */
	public ParcelaServico findParcelaTaxaColetaEntrega(
			CalculoFrete calculoFrete,
			String cdColetaEntrega,
			Boolean blInterior,
			Boolean blEmergencia
	) {

		String cdParcelaPreco = "ID" + cdColetaEntrega;
		if(BooleanUtils.isTrue(blInterior) ) {
			cdParcelaPreco = cdParcelaPreco + "Interior";
		} else {
			cdParcelaPreco = cdParcelaPreco + "Urbana";
		}
		if(BooleanUtils.isTrue(blEmergencia)) {
			cdParcelaPreco = cdParcelaPreco + "Emergencia";
		} else {
			cdParcelaPreco = cdParcelaPreco + "Convencional";
		}

		ValorTaxa valorTaxa = getCalculoParcelaFreteDAO().findValorTaxa(calculoFrete.getTabelaPreco().getIdTabelaPreco(), cdParcelaPreco);
		if(valorTaxa == null) {
			return null;
		}

		BigDecimal vlBrutoParcela = BigDecimalUtils.ZERO;
		BigDecimal psReferencia = calculoFrete.getPsReferencia() == null ? BigDecimalUtils.ZERO : calculoFrete.getPsReferencia();
		BigDecimal vlDistancia = BigDecimalUtils.ZERO;
		if(
				ConstantesExpedicao.CD_TAXA_COLETA_URBANA_CONVENCIONAL.equals(cdParcelaPreco) ||
				ConstantesExpedicao.CD_TAXA_COLETA_URBANA_EMERGENCIA.equals(cdParcelaPreco) ||
				ConstantesExpedicao.CD_TAXA_COLETA_INTERIOR_CONVENCIONAL.equals(cdParcelaPreco) ||
				ConstantesExpedicao.CD_TAXA_ENTREGA_URBANA_CONVENCIONAL.equals(cdParcelaPreco) ||
				ConstantesExpedicao.CD_TAXA_ENTREGA_URBANA_EMERGENCIA.equals(cdParcelaPreco) ||
				ConstantesExpedicao.CD_TAXA_ENTREGA_INTERIOR_CONVENCIONAL.equals(cdParcelaPreco)
		) {
			if(CompareUtils.le(psReferencia, valorTaxa.getPsTaxado())) {
				vlBrutoParcela = valorTaxa.getVlTaxa();
			} else {
				vlBrutoParcela = psReferencia.subtract(valorTaxa.getPsTaxado()).multiply(valorTaxa.getVlExcedente()).add(valorTaxa.getVlTaxa());
			}
		} else if(
				ConstantesExpedicao.CD_TAXA_COLETA_INTERIOR_EMERGENCIA.equals(cdParcelaPreco) ||
				ConstantesExpedicao.CD_TAXA_ENTREGA_INTERIOR_EMERGENCIA.equals(cdParcelaPreco)
		) {
			if("Coleta".equals(cdColetaEntrega)) {
				vlDistancia = findDistanciaEntreMunicipio(calculoFrete.getRestricaoRotaOrigem());
			} else if("Entrega".equals(cdColetaEntrega)) {
				vlDistancia = findDistanciaEntreMunicipio(calculoFrete.getRestricaoRotaDestino());
			}
			
			Object o = parametroGeralService.findConteudoByNomeParametroWithoutException(KM_MINIMA, false);
			BigDecimal minDist = (o instanceof BigDecimal) ? ((BigDecimal)o) : null;
			
			/** Se valor da distância minima é maior que a distância, deve usar a distância minima */
			vlDistancia = minDist != null && CompareUtils.gt(minDist, vlDistancia) ? minDist : vlDistancia;
			
			vlBrutoParcela = vlDistancia.multiply(BigDecimalUtils.getBigDecimal("2")).multiply(valorTaxa.getVlTaxa());
		}
		return findParametroTaxaColetaEntrega(calculoFrete, new ParcelaTaxaColetaEntrega(valorTaxa.getTabelaPrecoParcela().getParcelaPreco(), valorTaxa.getVlTaxa(), vlBrutoParcela, valorTaxa, vlDistancia));
	}

	public BigDecimal findDistanciaEntreMunicipio(RestricaoRota rota){
		Long idMunicipioDestino = rota.getIdMunicipio();

		Long idMunicipioOrigem = null;
		if( rota.getIdAeroporto() != null ){
			Aeroporto aeroporto = aeroportoService.findById(rota.getIdAeroporto());
			if( aeroporto != null && aeroporto.getFilial() != null ){
				idMunicipioOrigem = aeroporto.getFilial().getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio();
			}
		}

		if( idMunicipioOrigem == null ){
			idMunicipioOrigem = idMunicipioDestino;
		}
		Integer nrDistancia = mcdService.findDistanciaEntreMunicipiosSemGrauDificuldade(idMunicipioOrigem, idMunicipioDestino, null);
		return BigDecimalUtils.getBigDecimal(nrDistancia);
	}

	/**
	 * Aplica parametrização da parcela de Taxa Coleta Entrega
	 *
	 * @param calculoFrete
	 * @param freteParcela
	 * @param vlDistancia
	 * @return ParcelaServico
	 */
	private ParcelaServico findParametroTaxaColetaEntrega(CalculoFrete calculoFrete, ParcelaTaxaColetaEntrega parcelaFrete) {
		ParametroCliente parametroCliente = calculoFrete.getParametroCliente();
		if( (parametroCliente != null) && (!calculoFrete.getBlDescontoTotal().booleanValue()) ) {
			//seta valor padrao para nao precisar testar indicador "T"
			BigDecimal vlParcela = parcelaFrete.getVlBrutoParcela();

			TaxaCliente taxaCliente = findTaxaCliente(calculoFrete, parcelaFrete);
			if(taxaCliente == null) {
				return parcelaFrete;
			}

			ValorTaxa valorTaxa = parcelaFrete.getValorTaxa();
			BigDecimal vlDistancia = parcelaFrete.getVlDistancia();
			String cdParcelaPreco = parcelaFrete.getParcelaPreco().getCdParcelaPreco();
			String tpTaxaIndicador = taxaCliente.getTpTaxaIndicador().getValue();
			if(
					ConstantesExpedicao.CD_TAXA_COLETA_URBANA_CONVENCIONAL.equals(cdParcelaPreco) ||
					ConstantesExpedicao.CD_TAXA_COLETA_URBANA_EMERGENCIA.equals(cdParcelaPreco) ||
					ConstantesExpedicao.CD_TAXA_COLETA_INTERIOR_CONVENCIONAL.equals(cdParcelaPreco) ||
					ConstantesExpedicao.CD_TAXA_ENTREGA_URBANA_CONVENCIONAL.equals(cdParcelaPreco) ||
					ConstantesExpedicao.CD_TAXA_ENTREGA_URBANA_EMERGENCIA.equals(cdParcelaPreco) ||
					ConstantesExpedicao.CD_TAXA_ENTREGA_INTERIOR_CONVENCIONAL.equals(cdParcelaPreco)
			) {
				BigDecimal vlTaxa = taxaCliente.getVlTaxa();
				BigDecimal psReferencia = calculoFrete.getPsReferencia() == null ? BigDecimalUtils.ZERO : calculoFrete.getPsReferencia();
				if("A".equals(tpTaxaIndicador)) {
					vlTaxa = BigDecimalUtils.acrescimo(valorTaxa.getVlTaxa(), taxaCliente.getVlTaxa());
				} else if("D".equals(tpTaxaIndicador)) {
					vlTaxa = BigDecimalUtils.desconto(valorTaxa.getVlTaxa(), taxaCliente.getVlTaxa());
				}
				if(BigDecimalUtils.hasValue(taxaCliente.getPsMinimo())) {
					if(CompareUtils.le(psReferencia, taxaCliente.getPsMinimo())) {
						vlParcela = vlTaxa;
					} else {
						vlParcela = psReferencia.subtract(taxaCliente.getPsMinimo()).multiply(taxaCliente.getVlExcedente()).add(vlTaxa);
					}
				} else {
					if(!BigDecimalUtils.hasValue(taxaCliente.getVlExcedente())) {
						vlParcela = vlTaxa;
					} else if(CompareUtils.gt(taxaCliente.getVlExcedente(), BigDecimalUtils.ZERO)) {
						if(CompareUtils.le(psReferencia, valorTaxa.getPsTaxado())) {
							vlParcela = vlTaxa;
						} else {
							vlParcela = psReferencia.subtract(valorTaxa.getPsTaxado()).multiply(taxaCliente.getVlExcedente()).add(vlTaxa);
						}
					}
				}
			} else if(
					ConstantesExpedicao.CD_TAXA_COLETA_INTERIOR_EMERGENCIA.equals(cdParcelaPreco) ||
					ConstantesExpedicao.CD_TAXA_ENTREGA_INTERIOR_EMERGENCIA.equals(cdParcelaPreco)
			) {
				if("V".equals(tpTaxaIndicador)) {
					if((taxaCliente.getPsMinimo() == null) || (CompareUtils.gt(vlDistancia, taxaCliente.getPsMinimo())) ) {
						vlParcela = vlDistancia.multiply(BigDecimalUtils.getBigDecimal("2")).multiply(taxaCliente.getVlTaxa());
					} else {
						vlParcela = taxaCliente.getPsMinimo().multiply(BigDecimalUtils.getBigDecimal("2")).multiply(taxaCliente.getVlTaxa());
					}
				} else if("A".equals(tpTaxaIndicador)) {
					if((taxaCliente.getPsMinimo() == null) || (CompareUtils.gt(vlDistancia, taxaCliente.getPsMinimo())) ) {
						vlParcela = vlDistancia.multiply(BigDecimalUtils.getBigDecimal("2")).multiply(BigDecimalUtils.acrescimo(parcelaFrete.getVlUnitarioParcela(), taxaCliente.getVlTaxa()));
					} else {
						vlParcela = taxaCliente.getPsMinimo().multiply(BigDecimalUtils.getBigDecimal("2")).multiply(BigDecimalUtils.acrescimo(parcelaFrete.getVlUnitarioParcela(), taxaCliente.getVlTaxa()));
					}
				} else if("D".equals(tpTaxaIndicador)) {
					if((taxaCliente.getPsMinimo() == null) || (CompareUtils.gt(vlDistancia, taxaCliente.getPsMinimo())) ) {
						vlParcela = vlDistancia.multiply(BigDecimalUtils.getBigDecimal("2")).multiply(BigDecimalUtils.desconto(parcelaFrete.getVlUnitarioParcela(), taxaCliente.getVlTaxa()));
					} else {
						vlParcela = taxaCliente.getPsMinimo().multiply(BigDecimalUtils.getBigDecimal("2")).multiply(BigDecimalUtils.desconto(parcelaFrete.getVlUnitarioParcela(), taxaCliente.getVlTaxa()));
					}
				}
			}
			parcelaFrete.setVlBrutoParcela(vlParcela);
			parcelaFrete.setVlParcela(vlParcela);
		}
		return parcelaFrete;
	}

	/**
	 * Calcula valor da parcela de Taxa Terrestre
	 *
	 * @param calculoFrete
	 * @return ParcelaServico
	 */
	public ParcelaServico findParcelaTaxaTerrestre(CalculoFrete calculoFrete) {
		ParcelaPreco parcelaPreco = findParcelaPreco(calculoFrete.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TAXA_TERRESTRE);
		if(parcelaPreco == null) {
			return null;
		}
		boolean blCalculaTaxa = true;
		if(ConstantesExpedicao.MODAL_AEREO.equals(calculoFrete.getTpModal()) && calculoFrete.getRestricaoRotaDestino().getIdAeroporto() != null ) {
			Aeroporto aeroporto = getCalculoParcelaFreteDAO().get(Aeroporto.class, calculoFrete.getRestricaoRotaDestino().getIdAeroporto());
			blCalculaTaxa = aeroporto.getBlTaxaTerrestreObrigatoria().booleanValue();
		}
		if(!blCalculaTaxa) {
			return null;
		}

		//pega o valor pela faixa de menor peso
		FaixaProgressiva faixaProgressiva = faixaProgressivaService.findFaixaProgressivaMenorPeso(calculoFrete.getTabelaPreco().getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco());
		BigDecimal vlFaixaMenor = findValorFaixaProgressivaRotaPreco(calculoFrete, parcelaPreco.getIdParcelaPreco(), faixaProgressiva.getIdFaixaProgressiva()).getVlFixo();

		BigDecimal psReferencia = calculoFrete.getPsReferencia() == null ? BigDecimalUtils.ZERO : calculoFrete.getPsReferencia();
		BigDecimal vlBrutoParcela = BigDecimalUtils.ZERO;
		BigDecimal vlPesoRestante = BigDecimalUtils.ZERO;
		if(CompareUtils.lt(psReferencia, faixaProgressiva.getVlFaixaProgressiva())) {
			vlBrutoParcela = vlFaixaMenor;
		} else {
			//pega valor da faixa enquadrada
			ValorFaixaProgressiva valorFaixaProgressiva = getCalculoParcelaFreteDAO().findValorFaixaProgressivaEnquadrada(calculoFrete.getTabelaPreco().getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), psReferencia, null, null, null, null,false);
			vlPesoRestante = psReferencia.subtract(faixaProgressiva.getVlFaixaProgressiva());
			vlBrutoParcela = vlPesoRestante.multiply(valorFaixaProgressiva.getVlFixo()).add(vlFaixaMenor);
		}
		return findParametroTaxa(calculoFrete, new ParcelaServico(parcelaPreco, null, vlBrutoParcela));
	}

	/**
	 * Calcula valor da parcela de Taxa Combustível
	 *
	 * @param calculoFrete
	 * @return ParcelaServico
	 */
	public ParcelaServico findParcelaTaxaCombustivel(CalculoFrete calculoFrete) {
		ParcelaPreco parcelaPreco = findParcelaPreco(calculoFrete.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TAXA_COMBUSTIVEL);
		if(parcelaPreco == null) {
			return null;
		}
		BigDecimal psReferencia = calculoFrete.getPsReferencia() == null ? BigDecimalUtils.ZERO : calculoFrete.getPsReferencia();
		FaixaProgressiva faixaProgressiva = faixaProgressivaService.findFaixaProgressivaEnquadrada(calculoFrete.getTabelaPreco().getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), psReferencia);
		BigDecimal vlUnitarioParcela = findValorFaixaProgressivaRotaPreco(calculoFrete, parcelaPreco.getIdParcelaPreco(), faixaProgressiva.getIdFaixaProgressiva()).getVlFixo();
		BigDecimal vlBrutoParcela = psReferencia.multiply(vlUnitarioParcela);

		return findParametroTaxaCombustivel(calculoFrete, new ParcelaServico(parcelaPreco, vlUnitarioParcela, vlBrutoParcela));
	}

	/**
	 * Aplica parametrização da parcela de Taxa Combustível
	 *
	 * @param calculoFrete
	 * @param freteParcela
	 * @return ParcelaServico
	 */
	private ParcelaServico findParametroTaxaCombustivel(CalculoFrete calculoFrete, ParcelaServico parcelaFrete) {
		ParametroCliente parametroCliente = calculoFrete.getParametroCliente();
		if( (parametroCliente != null) && (!calculoFrete.getBlDescontoTotal().booleanValue()) ) {
			//seta valor padrao para nao precisar testar indicador "T"
			BigDecimal vlParcela = parcelaFrete.getVlBrutoParcela();

			TaxaCliente taxaCliente = findTaxaCliente(calculoFrete, parcelaFrete);
			if(taxaCliente == null) {
				return parcelaFrete;
			}

			String tpTaxaIndicador = taxaCliente.getTpTaxaIndicador().getValue();
			if("V".equals(tpTaxaIndicador)) {
				vlParcela = taxaCliente.getVlTaxa().multiply(calculoFrete.getPsReferencia());
			} else if("A".equals(tpTaxaIndicador)) {
				vlParcela = BigDecimalUtils.acrescimo(parcelaFrete.getVlUnitarioParcela(), taxaCliente.getVlTaxa()).multiply(calculoFrete.getPsReferencia());
			} else if("D".equals(tpTaxaIndicador)) {
				vlParcela = BigDecimalUtils.desconto(parcelaFrete.getVlUnitarioParcela(), taxaCliente.getVlTaxa()).multiply(calculoFrete.getPsReferencia());
			}
			parcelaFrete.setVlBrutoParcela(vlParcela);
			parcelaFrete.setVlParcela(vlParcela);
		}
		return parcelaFrete;
	}

	private ParcelaServico findParametroTaxa(CalculoFrete calculoFrete, ParcelaServico parcelaFrete) {
		ParametroCliente parametroCliente = calculoFrete.getParametroCliente();
		if( (parametroCliente != null) && (!calculoFrete.getBlDescontoTotal().booleanValue()) ) {
			//seta valor padrao para nao precisar testar indicador "T"
			BigDecimal vlParcela = parcelaFrete.getVlBrutoParcela();

			TaxaCliente taxaCliente = findTaxaCliente(calculoFrete, parcelaFrete);
			if(taxaCliente != null) {
			String tpTaxaIndicador = taxaCliente.getTpTaxaIndicador().getValue();
			if("V".equals(tpTaxaIndicador)) {
				vlParcela = taxaCliente.getVlTaxa();
			} else if("A".equals(tpTaxaIndicador)) {
				vlParcela = BigDecimalUtils.acrescimo(parcelaFrete.getVlBrutoParcela(), taxaCliente.getVlTaxa());
			} else if("D".equals(tpTaxaIndicador)) {
				vlParcela = BigDecimalUtils.desconto(parcelaFrete.getVlBrutoParcela(), taxaCliente.getVlTaxa());
			}
			}

			parcelaFrete.setVlBrutoParcela(vlParcela);
			parcelaFrete.setVlParcela(vlParcela);
		}
		return parcelaFrete;
	}

	/**
	 * Calcula valor da parcela de Taxa Suframa
	 *
	 * @param calculoFrete
	 * @return ParcelaServico
	 */
	public ParcelaServico findParcelaTaxaSuframa(CalculoFrete calculoFrete) {
		ParcelaPreco parcelaPreco = getCalculoParcelaFreteDAO().findParcelaPreco(calculoFrete.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TAXA_SUFRAMA);
		if(parcelaPreco == null) {
			return null;
		}
		BigDecimal vlBrutoParcela = null;
		if(calculoFrete.getRestricaoRotaDestino().getIdAeroporto() != null) {
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(calculoFrete.getRestricaoRotaDestino().getIdAeroporto());
			if(!enderecoPessoa.getMunicipio().getUnidadeFederativa().getBlCobraSuframa()) {
			return null;
		}
		}

		BigDecimal vlMercadoria = calculoFrete.getVlMercadoria();
		TaxaSuframa taxaSuframa = getCalculoParcelaFreteDAO().findTaxaSuframa(vlMercadoria);
		if(taxaSuframa == null) {
			return null;
		}
		if("PC".equals(taxaSuframa.getTpIndicadorCalculo().getValue())) {
			vlBrutoParcela = vlMercadoria.multiply(BigDecimalUtils.percent(taxaSuframa.getVlLiquido()));
		} else if("VL".equals(taxaSuframa.getTpIndicadorCalculo().getValue())) {
			vlBrutoParcela = taxaSuframa.getVlLiquido();
		}
		return findParametroTaxa(calculoFrete, new ParcelaServico(parcelaPreco, null, vlBrutoParcela));
	}
	
	private boolean checkPesoMinimoWithPesoReferencia(BigDecimal psMinimo, CalculoFrete calculoFrete){
		BigDecimal psReferencia = calculoFrete.getPsReferencia() == null ? BigDecimalUtils.ZERO : calculoFrete.getPsReferencia();
		if(psMinimo != null && CompareUtils.ge(psMinimo, psReferencia)) {
			return true;
		}
		return false;
	}

	private boolean validateTDEByDestinatario(Long idTdeCliente, Long idClienteDestinatario) {
		Boolean existsTdeByDestinatario = Boolean.FALSE;
		DestinatarioTdeCliente destinatarioTdeCliente = destinatarioTdeClienteService.findByIdTdeClienteAndIdClienteDestinatario(idTdeCliente, idClienteDestinatario);

		if (destinatarioTdeCliente != null) {
			existsTdeByDestinatario = Boolean.TRUE;
		}			
		
		return existsTdeByDestinatario;
	}

	/**
	 * Obtem a taxa de dificuldade de entrega
	 *
	 * @param  calculoFrete
	 * @return ParcelaServico
	 *
	 */
	public ParcelaServico findParcelaTDE(CalculoFrete calculoFrete) {
		
		Long idClienteTDE = findIdClienteTDE(calculoFrete);
		
		TdeCliente tdeCliente = tdeClienteService.findTdeVigenteByDivisaoClienteAndIdCliente(
				calculoFrete.getIdDivisaoCliente(), idClienteTDE, JTDateTimeUtils.getDataAtual());
		
		if (tdeCliente != null) {
			if (!this.validateTDEByDestinatario(tdeCliente.getIdTdeCliente(), 
					calculoFrete.getDadosCliente().getIdClienteDestinatario())) {
				return null;
			}
		} else {
		if(!Boolean.TRUE.equals(calculoFrete.getDadosCliente().getBlDificuldadeEntregaDestinatario())) {
			return null;
		}
		}
		
		Generalidade generalidade = getCalculoParcelaFreteDAO().findGeneralidade(calculoFrete.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TDE);
		if(generalidade == null) {
			return null;
		}
		
		BigDecimal vlTotalParcelas = BigDecimalUtils.ZERO;
		BigDecimal vlBrutoParcela = BigDecimalUtils.ZERO;
		BigDecimal vlMinimoParcela = BigDecimalUtils.ZERO;
		
		if(!checkPesoMinimoWithPesoReferencia(generalidade.getPsMinimo(), calculoFrete)) {
			/** Verifica quais Parcelas somar */
			calculoFrete.setVlTotalParcelas(findTiposDeParcelas(calculoFrete, ConstantesExpedicao.CD_TDE));

			vlTotalParcelas = calculoFrete.getVlTotalParcelas();
			vlBrutoParcela = vlTotalParcelas.multiply(BigDecimalUtils.percent(generalidade.getVlGeneralidade()));
			vlMinimoParcela = generalidade.getVlMinimo();
			if(BigDecimalUtils.hasValue(vlMinimoParcela)) {
				if(CompareUtils.lt(vlBrutoParcela, vlMinimoParcela)) {
					vlBrutoParcela = vlMinimoParcela;
				}
			} else {
				vlMinimoParcela = BigDecimalUtils.ZERO;
			}
		}
		
		ParcelaServico parcelaServico = new ParcelaServico(generalidade.getTabelaPrecoParcela().getParcelaPreco(), generalidade.getVlGeneralidade(), vlBrutoParcela);
		parcelaServico.addParametroAuxiliar("vlMinimo", vlMinimoParcela);
		parcelaServico.addParametroAuxiliar("vlTotalParcelas", vlTotalParcelas);

		executeParametroDificuldadeEntrega(calculoFrete, parcelaServico);
		return parcelaServico;
	}

	private Long findIdClienteTDE(CalculoFrete calculoFrete) {
		Long idClienteTDE = null;
		if (calculoFrete.getClienteBase().getTpCliente() != null
				&& ConstantesExpedicao.TP_CLIENTE_FILIAL.equals(calculoFrete.getClienteBase().getTpCliente().getValue())) {
			idClienteTDE = clienteService.findIdClienteMatriz(calculoFrete.getClienteBase().getIdCliente());
		} else {
			idClienteTDE = calculoFrete.getClienteBase().getIdCliente();
		}
		return idClienteTDE;
	}

	public ParcelaServico findParcelaTDEToVeiculoDedicado(CalculoFrete calculoFrete) {
		Generalidade generalidade = getCalculoParcelaFreteDAO().findGeneralidade(calculoFrete.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TDE);
		if(generalidade == null) {
			return null;
		}

		/** Verifica quais Parcelas somar */
		calculoFrete.setVlTotalParcelas(findTiposDeParcelas(calculoFrete, ConstantesExpedicao.CD_TDE));

		BigDecimal vlTotalParcelas = calculoFrete.getVlTotalParcelas();
		BigDecimal vlBrutoParcela = vlTotalParcelas.multiply(BigDecimalUtils.percent(generalidade.getVlGeneralidade()));
		BigDecimal vlMinimoParcela = generalidade.getVlMinimo();

		if(BigDecimalUtils.hasValue(vlMinimoParcela)) {
			if(CompareUtils.lt(vlBrutoParcela, vlMinimoParcela)) {
				vlBrutoParcela = vlMinimoParcela;
			}
		} else {
			vlMinimoParcela = BigDecimalUtils.ZERO;
		}

		ParcelaServico parcelaServico = new ParcelaServico(generalidade.getTabelaPrecoParcela().getParcelaPreco(), generalidade.getVlGeneralidade(), vlBrutoParcela);
		parcelaServico.addParametroAuxiliar("vlMinimo", vlMinimoParcela);
		parcelaServico.addParametroAuxiliar("vlTotalParcelas", vlTotalParcelas);

		executeParametroDificuldadeEntrega(calculoFrete, parcelaServico);
		return parcelaServico;
	}


	public ParcelaServico findParcelaTaxaVeiculoDedicado(CalculoFrete calculoFrete) {
		if (!Boolean.TRUE.equals(calculoFrete.getDadosCliente().getBlVeiculoDedicadoDestinatario())) {
			return null;
		}

		return executeParcelaDificultadeEntrega(calculoFrete, ConstantesExpedicao.CD_VEICULO_DEDICADO);
	}


	public ParcelaServico findParcelaTaxaAgendamentoColetaEntrega(CalculoFrete calculoFrete) {
		if (!Boolean.TRUE.equals(calculoFrete.getDadosCliente().getBlAgendamentoEntregaDestinatario())) {
			return null;
		}

		return executeParcelaDificultadeEntrega(calculoFrete, ConstantesExpedicao.CD_AGENDAMENTO_COLETA_ENTREGA);
	}


	public ParcelaServico findParcelaTaxaPaletizacao(CalculoFrete calculoFrete) {
		if (!Boolean.TRUE.equals(calculoFrete.getDadosCliente().getBlPaletizacaoDestinatario())) {
			return null;
		}

		return executeParcelaDificultadeEntrega(calculoFrete, ConstantesExpedicao.CD_TAXA_PALETIZACAO);
	}


	public ParcelaServico findParcelaTaxaCustoDescarga(CalculoFrete calculoFrete) {
		if (!Boolean.TRUE.equals(calculoFrete.getDadosCliente().getBlCustoDescargaDestinatario())) {
			return null;
		}

		return executeParcelaDificultadeEntrega(calculoFrete, ConstantesExpedicao.CD_CUSTO_DESCARGA);
	}


	public ParcelaServico findParcelaTaxaMinimoReentrega(CalculoFrete calculoFrete) {
		return executeParcelaDificultadeEntrega(calculoFrete, ConstantesExpedicao.CD_MINIMO_REENTREGA);
	}


	private ParcelaServico executeParcelaDificultadeEntrega(CalculoFrete calculoFrete, String cdParcelaPreco) {

		ParcelaServico parcelaServico = null;
		Generalidade generalidade = null;

		if( calculoFrete.getTabelaPreco() != null ){
			generalidade = getCalculoParcelaFreteDAO().findGeneralidade(calculoFrete.getTabelaPreco().getIdTabelaPreco(), cdParcelaPreco);
			if (generalidade == null) {
				// TODO LMS-2655
				if(ConstantesExpedicao.CD_VEICULO_DEDICADO.equalsIgnoreCase(cdParcelaPreco)){
					return findParcelaTDEToVeiculoDedicado(calculoFrete);
				}else{
					return null;
			}
			}

			BigDecimal vlParcela = null;
			BigDecimal vlGeneralidade = generalidade.getVlGeneralidade();
			BigDecimal vlTotalParcelas = calculoFrete.getVlTotalParcelas();
			BigDecimal vlMinimoParcela = generalidade.getVlMinimo() == null ? BigDecimal.ZERO : generalidade.getVlMinimo();
			String tpIndicador = generalidade.getTabelaPrecoParcela().getParcelaPreco().getTpIndicadorCalculo().getValue();

			if ("VL".equals(tpIndicador)) {
				vlParcela = vlGeneralidade;
			} else if ("PE".equals(tpIndicador)) {
				vlParcela = vlGeneralidade.multiply(calculoFrete.getPsReferencia());
			} else if ("PC".equals(tpIndicador)) {
				vlParcela = calculoFrete.getVlMercadoria().multiply(vlGeneralidade).divide(new BigDecimal(100));
			} else if ("PF".equals(tpIndicador)) {
				vlParcela = vlTotalParcelas.multiply(vlGeneralidade).divide(new BigDecimal(100));
			} else if ("VU".equals(tpIndicador)) {
				vlParcela = vlGeneralidade.multiply(new BigDecimal(calculoFrete.getQtVolumes()));
			}

			if (CompareUtils.lt(vlParcela, vlMinimoParcela)){
				vlParcela = vlMinimoParcela;
			}

			if (cdParcelaPreco.equalsIgnoreCase(ConstantesExpedicao.CD_VEICULO_DEDICADO)  && "IDTde".equalsIgnoreCase(generalidade.getTabelaPrecoParcela().getParcelaPreco().getCdParcelaPreco())) {
				parcelaServico = new ParcelaServico(generalidade.getTabelaPrecoParcela().getParcelaPreco(), generalidade.getVlGeneralidade(), vlParcela);
			}else{
				parcelaServico = new ParcelaServico(generalidade.getTabelaPrecoParcela().getParcelaPreco());
			}

			parcelaServico.addParametroAuxiliar("vlMinimo", vlMinimoParcela);
			parcelaServico.addParametroAuxiliar("vlTotalParcelas", vlTotalParcelas);
			parcelaServico.setVlParcela(vlParcela);
			parcelaServico.setVlBrutoParcela(vlParcela);

			executeParametroDificuldadeEntrega(calculoFrete, parcelaServico);
		}
		return parcelaServico;
	}


	private void executeParametroDificuldadeEntrega(CalculoFrete calculoFrete, ParcelaServico freteParcela) {
		
		boolean calculaParcela = checkParametroDificuldadeEntregaByPesoMinimo(freteParcela, calculoFrete);
		
		ParametroCliente parametroCliente = calculoFrete.getParametroCliente();
		if(calculaParcela && parametroCliente != null && !calculoFrete.getBlDescontoTotal().booleanValue() ) {
			String tpIndicador;
			String cdParcelaPreco = freteParcela.getParcelaPreco().getCdParcelaPreco();
			BigDecimal vlMinimoParcela = freteParcela.getParametroAuxiliar("vlMinimo");
			BigDecimal vlTotalParcelas = freteParcela.getParametroAuxiliar("vlTotalParcelas");
			BigDecimal vlMinimo = BigDecimal.ZERO;
			BigDecimal vlParcela = null;

			if (ConstantesExpedicao.CD_TDE.equals(cdParcelaPreco)) {

			tpIndicador = parametroCliente.getTpIndicadorMinimoTde().getValue();
			if("T".equals(tpIndicador)) {
					vlMinimo = vlMinimoParcela;
			} else if("V".equals(tpIndicador)) {
					vlMinimo = parametroCliente.getVlMinimoTde();
			} else if("A".equals(tpIndicador)) {
					vlMinimo = BigDecimalUtils.acrescimo(vlMinimoParcela, parametroCliente.getVlMinimoTde());
			} else if("D".equals(tpIndicador)) {
					vlMinimo = BigDecimalUtils.desconto(vlMinimoParcela, parametroCliente.getVlMinimoTde());
			}

			tpIndicador = parametroCliente.getTpIndicadorPercentualTde().getValue();
			BigDecimal vlTde = null;
			if("T".equals(tpIndicador)) {
				vlTde = freteParcela.getVlUnitarioParcela();
			} else if("V".equals(tpIndicador)) {
				vlTde = parametroCliente.getVlPercentualTde();
			} else if("D".equals(tpIndicador)) {
				vlTde = BigDecimalUtils.desconto(freteParcela.getVlUnitarioParcela(), parametroCliente.getVlPercentualTde());
			} else if("A".equals(tpIndicador)) {
				vlTde = BigDecimalUtils.acrescimo(freteParcela.getVlUnitarioParcela(), parametroCliente.getVlPercentualTde());
			} else if("P".equals(tpIndicador)) {
				vlTde = freteParcela.getVlUnitarioParcela().add(parametroCliente.getVlPercentualTde());
			}

				vlParcela = vlTotalParcelas.multiply(BigDecimalUtils.percent(vlTde));
				if (CompareUtils.lt(vlParcela, vlMinimo)) {
					vlParcela = vlMinimo;
			}
			freteParcela.setVlBrutoParcela(vlParcela);
			freteParcela.setVlParcela(vlParcela);

			} else if (	ConstantesExpedicao.CD_VEICULO_DEDICADO.equals(cdParcelaPreco) ||
						ConstantesExpedicao.CD_AGENDAMENTO_COLETA_ENTREGA.equals(cdParcelaPreco) ||
						ConstantesExpedicao.CD_TAXA_PALETIZACAO.equals(cdParcelaPreco) ||
						ConstantesExpedicao.CD_CUSTO_DESCARGA.equals(cdParcelaPreco) ||
						ConstantesExpedicao.CD_MINIMO_REENTREGA.equals(cdParcelaPreco)) {

				if (freteParcela.getVlParcelaOriginal() == null ) {
					freteParcela.setVlParcelaOriginal(freteParcela.getVlBrutoParcela());
				} else {
					freteParcela.setVlBrutoParcela(freteParcela.getVlParcelaOriginal());
		}

				GeneralidadeCliente generalidadeCliente = findGeneralidadeCliente(calculoFrete, freteParcela);
				if (generalidadeCliente != null) {
					if (CompareUtils.gt(vlMinimoParcela, BigDecimal.ZERO)) {
						tpIndicador = generalidadeCliente.getTpIndicadorMinimo().getValue();
						if ("T".equals(tpIndicador)) {
							vlMinimo = vlMinimoParcela;
						} else if ("V".equals(tpIndicador)) {
							vlMinimo = generalidadeCliente.getVlMinimo();
						} else if ("D".equals(tpIndicador)) {
							vlMinimo = vlMinimoParcela.subtract(vlMinimoParcela.multiply(generalidadeCliente.getVlMinimo()).divide(new BigDecimal(100)));
						} else if ("A".equals(tpIndicador)) {
							vlMinimo = vlMinimoParcela.add(vlMinimoParcela.multiply(generalidadeCliente.getVlMinimo()).divide(new BigDecimal(100)));
	}
					}

					tpIndicador = generalidadeCliente.getTpIndicador().getValue();
					if ("T".equals(tpIndicador)) {
						vlParcela = freteParcela.getVlBrutoParcela();
					} else if ("V".equals(tpIndicador)) {
						vlParcela = generalidadeCliente.getVlGeneralidade();
					} else if ("D".equals(tpIndicador)) {
						vlParcela = freteParcela.getVlBrutoParcela().subtract(freteParcela.getVlBrutoParcela().multiply(generalidadeCliente.getVlGeneralidade()).divide(new BigDecimal(100)));
					} else if ("A".equals(tpIndicador)) {
						vlParcela = freteParcela.getVlBrutoParcela().add(freteParcela.getVlBrutoParcela().multiply(generalidadeCliente.getVlGeneralidade()).divide(new BigDecimal(100)));
					}

					if (CompareUtils.le(vlParcela, vlMinimo)) {
						vlParcela = vlMinimo;
					}

					// TODO LMS-2655
					if (cdParcelaPreco.equalsIgnoreCase(ConstantesExpedicao.CD_VEICULO_DEDICADO) && BigDecimal.ZERO.equals(vlParcela)) {

						Generalidade generalidadeTDE = getCalculoParcelaFreteDAO().findGeneralidade(calculoFrete.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TDE);

						/** Verifica quais Parcelas somar */
						calculoFrete.setVlTotalParcelas(findTiposDeParcelas(calculoFrete, ConstantesExpedicao.CD_TDE));

						BigDecimal vlTotalParcelasTDE = calculoFrete.getVlTotalParcelas();
						BigDecimal vlBrutoParcelaTDE = vlTotalParcelasTDE.multiply(BigDecimalUtils.percent(generalidadeTDE.getVlGeneralidade()));
						BigDecimal vlMinimoParcelaTDE = generalidadeTDE.getVlMinimo();

						if(BigDecimalUtils.hasValue(vlMinimoParcelaTDE)) {
							if(CompareUtils.lt(vlBrutoParcelaTDE, vlMinimoParcelaTDE)) {
								vlBrutoParcelaTDE = vlMinimoParcelaTDE;
							}
						} else {
							vlMinimoParcela = BigDecimalUtils.ZERO;
						}

						ParcelaServico parcelaServicoTDE = new ParcelaServico(generalidadeTDE.getTabelaPrecoParcela().getParcelaPreco(), generalidadeTDE.getVlGeneralidade(), vlBrutoParcelaTDE);
						parcelaServicoTDE.addParametroAuxiliar("vlMinimo", vlMinimoParcelaTDE);
						parcelaServicoTDE.addParametroAuxiliar("vlTotalParcelas", vlTotalParcelasTDE);

						tpIndicador = parametroCliente.getTpIndicadorMinimoTde().getValue();
						if("T".equals(tpIndicador)) {
								vlMinimo = vlMinimoParcelaTDE;
						} else if("V".equals(tpIndicador)) {
								vlMinimo = parametroCliente.getVlMinimoTde();
						} else if("A".equals(tpIndicador)) {
								vlMinimo = BigDecimalUtils.acrescimo(vlMinimoParcelaTDE, parametroCliente.getVlMinimoTde());
						} else if("D".equals(tpIndicador)) {
								vlMinimo = BigDecimalUtils.desconto(vlMinimoParcelaTDE, parametroCliente.getVlMinimoTde());
						}

						tpIndicador = parametroCliente.getTpIndicadorPercentualTde().getValue();
						BigDecimal vlTde = null;
						if("T".equals(tpIndicador)) {
							vlTde = parcelaServicoTDE.getVlUnitarioParcela();
						} else if("V".equals(tpIndicador)) {
							vlTde = parametroCliente.getVlPercentualTde();
						} else if("D".equals(tpIndicador)) {
							vlTde = BigDecimalUtils.desconto(parcelaServicoTDE.getVlUnitarioParcela(), parametroCliente.getVlPercentualTde());
						} else if("A".equals(tpIndicador)) {
							vlTde = BigDecimalUtils.acrescimo(parcelaServicoTDE.getVlUnitarioParcela(), parametroCliente.getVlPercentualTde());
						} else if("P".equals(tpIndicador)) {
							vlTde = parcelaServicoTDE.getVlUnitarioParcela().add(parametroCliente.getVlPercentualTde());
						}

							vlParcela = vlTotalParcelasTDE.multiply(BigDecimalUtils.percent(vlTde));
							if (CompareUtils.lt(vlParcela, vlMinimo)) {
								vlParcela = vlMinimo;
						}
						// Busca os valores da Parcela TDE
						freteParcela.setParcelaPreco(parcelaPrecoService.findByCdParcelaPreco("IDTde"));

						freteParcela.setVlBrutoParcela(vlParcela);
						freteParcela.setVlParcela(vlParcela);
					}else{
						freteParcela.setVlBrutoParcela(vlParcela);
						freteParcela.setVlParcela(vlParcela);
				}
			}
		}
	}else if(!calculaParcela){
		freteParcela.setVlBrutoParcela(BigDecimal.ZERO);
		freteParcela.setVlParcela(BigDecimal.ZERO);
	}
	}
	
	private boolean checkParametroDificuldadeEntregaByPesoMinimo(ParcelaServico freteParcela, CalculoFrete calculoFrete){
		String cdParcelaPreco = freteParcela.getParcelaPreco().getCdParcelaPreco();
		if(ConstantesExpedicao.CD_TDE.equals(cdParcelaPreco)){
			Generalidade generalidade = getCalculoParcelaFreteDAO().findGeneralidade(calculoFrete.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TDE);
		
			if(checkPesoMinimoWithPesoReferencia(generalidade.getPsMinimo(), calculoFrete)){
				return false;
			}
		}
		return true;
	}


	/**
	 * Aplica parametrizações gerais do cálculo do frete
	 *
	 * @param calculoFrete
	 */
	public void findParametroParcelasFrete(CalculoFrete calculoFrete) {

		BigDecimal vlFretePeso  = BigDecimalUtils.ZERO;
		BigDecimal vlFreteValor = BigDecimalUtils.ZERO;

		/*Obtem o parametro do cliente*/
		ParametroCliente parametroCliente = calculoFrete.getParametroCliente();

		/*Obtem a TabelaPreco*/
		TabelaPreco tabelaPreco = calculoFrete.getTabelaPreco();
		BigDecimal psReferencia = calculoFrete.getPsReferencia() == null ? BigDecimalUtils.ZERO : calculoFrete.getPsReferencia();
		BigDecimal vlMercadoria = calculoFrete.getVlMercadoria();

		/*Obtem a ParcelaServico CD_FRETE_VALOR*/
		ParcelaServico parcelaFreteValor = calculoFrete.getParcelaGeral(ConstantesExpedicao.CD_FRETE_VALOR);

		/*Obtem a ParcelaServico CD_FRETE_PESO*/
		ParcelaServico parcelaFretePeso = calculoFrete.getParcelaGeral(ConstantesExpedicao.CD_FRETE_PESO);

		/*Obtem o valor da parcela frete peso*/
		BigDecimal vlFretePesoTabela = parcelaFretePeso.getVlBrutoParcela();

		/*Embute as parcelas generalidades necessárias*/
		if(BigDecimalUtils.hasValue(calculoFrete.getVlEmbutidoParcelas())){
			vlFretePesoTabela = BigDecimalUtils.defaultBigDecimal(vlFretePesoTabela).add(calculoFrete.getVlEmbutidoParcelas());
		}

		//Se o grupo regiao de destino nao foi informado, verifica se o municipio de destino se encontra em um grupo regiao grupo regiao
		if ( calculoFrete.getGrupoRegiaoDestino()!= null && 
				getCalculoFreteService().getRestricaoRotaService().validateGrupoRegiaoDestinoPorTarifa(calculoFrete) &&
				(parametroCliente == null || calculoFrete.getBlDescontoTotal().booleanValue()) ){
			executeCalculoFretePesoGrupoRegiao(calculoFrete.getGrupoRegiaoDestino(), calculoFrete);
			executeCalculoFreteValorGrupoRegiao(calculoFrete.getGrupoRegiaoDestino(), calculoFrete);
		}

		/*Retorna se parametroCliente for nulo ou se existir desconto total*/
		if((parametroCliente == null) || calculoFrete.getBlDescontoTotal().booleanValue()) {
			/*Embute as parcelas generalidados no frete peso, caso necessário*/
			if(BigDecimalUtils.hasValue(calculoFrete.getVlEmbutidoParcelas())){
				parcelaFretePeso.setVlParcela(vlFretePesoTabela);
				parcelaFretePeso.setVlBrutoParcela(vlFretePesoTabela);
			}
	
			return;
		}

		/*FRETE PERCENTUAL*/
		if(BigDecimalUtils.hasValue(parametroCliente.getPcFretePercentual())) {

			//Parte 1
			BigDecimal vlFreteValorPercentual = vlMercadoria.multiply(BigDecimalUtils.percent(parametroCliente.getPcFretePercentual()));
			vlFretePeso = BigDecimalUtils.ZERO;
			vlFreteValor = vlFreteValorPercentual;

			/*Se vlFreteValorPercentual for menor que VlMinimoFretePercentual
			seta vlFreteValorPercentual, vlFretePeso, vlFreteValor*/
			if(CompareUtils.lt(vlFreteValorPercentual, parametroCliente.getVlMinimoFretePercentual())) {
				vlFreteValorPercentual = parametroCliente.getVlMinimoFretePercentual();
				vlFretePeso = vlFreteValorPercentual;
				vlFreteValor = BigDecimalUtils.ZERO;
			}


			/*Informa  os valores vlFreteValor e vlFretePeso*/
			BigDecimal vlFretePesoPercentual = BigDecimalUtils.ZERO;
			if( CompareUtils.gt(psReferencia, parametroCliente.getPsFretePercentual()) && (BigDecimalUtils.hasValue(parametroCliente.getVlToneladaFretePercentual())) ) {
				//Parte 2
				vlFretePesoPercentual = psReferencia.multiply(parametroCliente.getVlToneladaFretePercentual().divide(BigDecimalUtils.getBigDecimal("1000"), 5, BigDecimal.ROUND_HALF_UP));
				if(CompareUtils.le(vlFreteValorPercentual, vlFretePesoPercentual)) {
					vlFreteValor = BigDecimalUtils.ZERO;
					vlFretePeso = vlFretePesoPercentual;
				}
			} else {
				vlFreteValor = vlFreteValorPercentual;
				vlFretePeso = BigDecimalUtils.ZERO;
			}

			/*Seta os valores das parcelas*/
			parcelaFretePeso.setVlBrutoParcela(vlFretePeso);
			parcelaFretePeso.setVlParcela(vlFretePeso);
			parcelaFreteValor.setVlBrutoParcela(vlFreteValor);
			parcelaFreteValor.setVlParcela(vlFreteValor);
			calculoFrete.setBlParametroFretePercentual(Boolean.TRUE);
			return;

		}/* ------> FRETE PERCENTUAL*/


		/*FRETE PESO*/
		String tpIndicadorFretePeso = parametroCliente.getTpIndicadorFretePeso().getValue();
		String tpIndicadorMinimoFretePeso = parametroCliente.getTpIndicadorMinFretePeso().getValue();
		String tpModal = calculoFrete.getTpModal();
		String tpTabelaPreco = tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue();


		/*Calcula Frete Peso através dos parametros do grupo região*/
		if(!"V".equals(tpIndicadorFretePeso) && calculoFrete.getGrupoRegiaoDestino() != null && getCalculoFreteService().getRestricaoRotaService().validateGrupoRegiaoDestinoPorTarifa(calculoFrete) ){
			executeCalculoFretePesoGrupoRegiao(calculoFrete.getGrupoRegiaoDestino(),calculoFrete);
			vlFretePesoTabela = parcelaFretePeso.getVlBrutoParcela();
		}

		/*FRETE VOLUME*/
		if(BigDecimalUtils.hasValue(parametroCliente.getVlFreteVolume())) {
			BigDecimal qtVolumes = null;

			if(calculoFrete.getIdDoctoServico() != null){
				List<DadosComplemento> dadosComplementos = dadosComplementoService.findVolTotalizadorByConhecimento(calculoFrete.getIdDoctoServico());
	
				if (dadosComplementos != null && !dadosComplementos.isEmpty()) {
					qtVolumes = BigDecimal.ZERO;
	
					for (DadosComplemento dadosComplemento : dadosComplementos) {
						try {
							qtVolumes = qtVolumes.add(BigDecimalUtils.getBigDecimal(dadosComplemento.getDsValorCampo()));
						} catch (NumberFormatException nfe) {
							log.error(MessageFormat.format("Ocorreu um erro ao validar o número disponível nos Dados Complemento presentes no Docto_Servico = {0}", calculoFrete.getIdDoctoServico()));
						}
					}
				} else {
					qtVolumes = BigDecimalUtils.getBigDecimal(calculoFrete.getQtVolumes());
				}
			}else{
				qtVolumes = BigDecimalUtils.getBigDecimal(calculoFrete.getQtVolumes());
			}
			vlFretePeso = parametroCliente.getVlFreteVolume().multiply(qtVolumes);
			if ("V".equals(tpIndicadorMinimoFretePeso) && BigDecimalUtils.hasValue(parametroCliente.getVlMinFretePeso()) && CompareUtils.lt(vlFretePeso, parametroCliente.getVlMinFretePeso())) {
					vlFretePeso = parametroCliente.getVlMinFretePeso();
				}

			/*Volume Natura*/
			if(tabelaPreco.getTpCalculoFretePeso() != null && "N".equals(tabelaPreco.getTpCalculoFretePeso().getValue())){
				/*Verifica se o complemento do documento possui Quantidade de volumes de 27L e 13L*/
				BigDecimal vlFreteVolume = findValorComplementarFreteVolume(calculoFrete);
				if(BigDecimalUtils.hasValue(vlFreteVolume)){
					vlFretePeso = vlFreteVolume;
				}
			}

			parcelaFretePeso.setVlBrutoParcela(vlFretePeso);
			parcelaFretePeso.setVlParcela(vlFretePeso);

		} else {

			/*FRETE PESO*/

			/*Caso TP_CALCULO_FRETE_PESO for N não calcula frete peso*/
			if(tabelaPreco.getTpCalculoFretePeso() != null && "N".equals(tabelaPreco.getTpCalculoFretePeso().getValue())){

				parcelaFretePeso.setVlBrutoParcela(BigDecimal.ZERO);
				parcelaFretePeso.setVlParcela(BigDecimal.ZERO);

			}else{

				Boolean embute = Boolean.FALSE;

				BigDecimal vlTarifaMinima = BigDecimalUtils.ZERO;

				/*Aéreo e tipo da tabela preço A*/
				if(ConstantesExpedicao.MODAL_AEREO.equals(tpModal) && "A".equals(tpTabelaPreco)) {

					String tpIndicadorTarifaMinima = parametroCliente.getTpTarifaMinima().getValue();
					BigDecimal vlTarifaMinimaTabela = parcelaFretePeso.getParametroAuxiliar("vlMinimo");

					/*Verifica o indicador da tarifa minima*/
					if((parametroCliente.getVlTarifaMinima() == null) || ("T".equals(tpIndicadorTarifaMinima))) {
						vlTarifaMinima = vlTarifaMinimaTabela;
					} else if("V".equals(tpIndicadorTarifaMinima)) {
						vlTarifaMinima = parametroCliente.getVlTarifaMinima();
					} else if("D".equals(tpIndicadorTarifaMinima)) {
						vlTarifaMinima = BigDecimalUtils.desconto(vlTarifaMinimaTabela, parametroCliente.getVlTarifaMinima());
					} else if("A".equals(tpIndicadorTarifaMinima)) {
						vlTarifaMinima = BigDecimalUtils.acrescimo(vlTarifaMinimaTabela, parametroCliente.getVlTarifaMinima());
					}
				}

				if(ConstantesExpedicao.MODAL_AEREO.equals(tpModal)
						&& "A".equals(tpTabelaPreco)
						&& !(calculoFrete.getIdProdutoEspecifico() == null)) {

					String tpIndicadorValorTabelaEspecifica = parametroCliente.getTpIndicVlrTblEspecifica().getValue();

					/*Verifica o tpIndicadorValorTabelaEspecifica*/
					if("T".equals(tpIndicadorValorTabelaEspecifica)) {
						vlFretePeso = vlFretePesoTabela;
					} else if("V".equals(tpIndicadorValorTabelaEspecifica)) {
						vlFretePeso = parametroCliente.getVlTblEspecifica().multiply(psReferencia);

						//LMS-8363
						if(null != parametroCliente.getSimulacao()) {
							BigDecimal vlFaixa = faixaProgressivaPropostaService.findVlFaixaProgressivaByIdProdutoEspecifico(parametroCliente.getIdParametroCliente(), parametroCliente.getSimulacao().getIdSimulacao(), calculoFrete.getIdProdutoEspecifico());
							if(vlFaixa != null){
								vlFretePeso = psReferencia.multiply(vlFaixa);
							}
						}
					} else if("D".equals(tpIndicadorValorTabelaEspecifica)) {
						vlFretePeso = BigDecimalUtils.desconto(parcelaFretePeso.getVlUnitarioParcela(), parametroCliente.getVlTblEspecifica()).multiply(psReferencia);
					} else if("A".equals(tpIndicadorValorTabelaEspecifica)) {
						vlFretePeso = BigDecimalUtils.acrescimo(parcelaFretePeso.getVlUnitarioParcela(), parametroCliente.getVlTblEspecifica()).multiply(psReferencia);
					}

					if(CompareUtils.gt(vlTarifaMinima, vlFretePeso)) {
						vlFretePeso = vlTarifaMinima;
					}
				} else {

					/*Ajuste referente ao quest 30963*/
					if("Y".equals(tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco())){
						embute = Boolean.TRUE;
						vlFretePesoTabela = parcelaFretePeso.getVlBrutoParcela();
					}

					/*Indicador Frete Peso T*/
					if("T".equals(tpIndicadorFretePeso)) {

						String tpIndicadorPercMinimoProgr = parametroCliente.getTpIndicadorPercMinimoProgr().getValue();

						/*Indicador minimo frete peso T*/
						if("T".equals(tpIndicadorMinimoFretePeso)) {
							vlFretePeso = vlFretePesoTabela;
							if(BigDecimalUtils.hasValue(tabelaPreco.getPsMinimo()) && CompareUtils.lt(psReferencia, tabelaPreco.getPsMinimo())) {
								if(parametroCliente.getVlPercMinimoProgr() != null){
									if("D".equals(tpIndicadorPercMinimoProgr)) {
										vlFretePeso = BigDecimalUtils.desconto(vlFretePesoTabela, parametroCliente.getVlPercMinimoProgr());
									} else if("A".equals(tpIndicadorPercMinimoProgr)) {
										vlFretePeso = BigDecimalUtils.acrescimo(vlFretePesoTabela, parametroCliente.getVlPercMinimoProgr());
									}
								}
							}

							/*Indicador minimo frete peso P*/
						} else if("P".equals(tpIndicadorMinimoFretePeso)) {
							BigDecimal vlParcelaFreteQuilo = parcelaFretePeso.getParametroAuxiliar("vlParcelaFreteQuilo");
							if(CompareUtils.lt(psReferencia, parametroCliente.getVlMinFretePeso())) {
								vlFretePesoTabela = vlParcelaFreteQuilo.multiply(parametroCliente.getVlMinFretePeso());


								if(parametroCliente.getVlPercMinimoProgr() != null){
									if("D".equals(tpIndicadorPercMinimoProgr)) {
										vlFretePeso = BigDecimalUtils.desconto(vlFretePesoTabela, parametroCliente.getVlPercMinimoProgr());
									} else if("A".equals(tpIndicadorPercMinimoProgr)) {
										vlFretePeso = BigDecimalUtils.acrescimo(vlFretePesoTabela, parametroCliente.getVlPercMinimoProgr());
									}
								}
							} else {
								vlFretePeso = psReferencia.multiply(vlParcelaFreteQuilo);
							}
						}

						/*Indicador Frete Peso V*/
					} else if("V".equals(tpIndicadorFretePeso)) {

						/*Indicador minimo frete peso T*/
						if("T".equals(tpIndicadorMinimoFretePeso)) {

							vlFretePeso = psReferencia.multiply(parametroCliente.getVlFretePeso());
							if(!ConstantesExpedicao.MODAL_AEREO.equals(tpModal) || !"A".equals(tpTabelaPreco)){
								if(CompareUtils.le(psReferencia, BigDecimalUtils.defaultBigDecimal(tabelaPreco.getPsMinimo()))) {
									Long idTarifaPreco = calculoFrete.getIdTarifa();
									if(!"D".equals(tpTabelaPreco) && !"@".equals(tpTabelaPreco)) {
										idTarifaPreco = null;
									}
									vlFretePeso = getValorUnitarioFretePesoFaixaProgressiva(tabelaPreco.getIdTabelaPreco(),
											parcelaFretePeso.getParcelaPreco().getIdParcelaPreco(), psReferencia,
											parametroCliente.getVlFretePeso(), idTarifaPreco, calculoFrete.getIdTarifa()).getValorUnitarioParcela();
								}
							}

							/*Indicador minimo frete peso V*/
						} else if("V".equals(tpIndicadorMinimoFretePeso)) {
							vlFretePeso = psReferencia.multiply(parametroCliente.getVlFretePeso());
							
							if(CompareUtils.lt(vlFretePeso, parametroCliente.getVlMinFretePeso())) {
								vlFretePeso = parametroCliente.getVlMinFretePeso();
							}
							
							/*Indicador minimo frete peso P*/
						} else if("P".equals(tpIndicadorMinimoFretePeso)) {
							if(Boolean.TRUE.equals(parametroCliente.getBlPagaPesoExcedente())) {
								if(CompareUtils.lt(psReferencia, parametroCliente.getVlMinFretePeso())) {
									vlFretePeso = parametroCliente.getVlMinimoFreteQuilo();
								} else {
									vlFretePeso = psReferencia.subtract(parametroCliente.getVlMinFretePeso()).multiply(parametroCliente.getVlFretePeso()).add(parametroCliente.getVlMinimoFreteQuilo());
								}
							} else {
								if(CompareUtils.lt(psReferencia, parametroCliente.getVlMinFretePeso())) {
									if(BigDecimalUtils.hasValue(parametroCliente.getVlMinimoFreteQuilo())) {
										vlFretePeso = parametroCliente.getVlMinimoFreteQuilo();
									} else {
										vlFretePeso = parametroCliente.getVlMinFretePeso().multiply(parametroCliente.getVlFretePeso());
									}
								} else {

									vlFretePeso = psReferencia.multiply(parametroCliente.getVlFretePeso());
									if(BigDecimalUtils.hasValue(parametroCliente.getVlMinimoFreteQuilo())
											&& CompareUtils.lt(vlFretePeso, parametroCliente.getVlMinimoFreteQuilo()) ){
										vlFretePeso = parametroCliente.getVlMinimoFreteQuilo();
									}
								}
							}
						}
						
						if(parametroCliente.getSimulacao() != null){
							BigDecimal vlFaixa = faixaProgressivaPropostaService.findVlFaixaProgressivaProposta(parametroCliente.getIdParametroCliente(), parametroCliente.getSimulacao().getIdSimulacao(), psReferencia);
							if(vlFaixa != null){
								vlFretePeso = psReferencia.multiply(vlFaixa);
								if(CompareUtils.gt(vlTarifaMinima, vlFretePeso) && ConstantesExpedicao.MODAL_AEREO.equals(tpModal)) {
									vlFretePeso = vlTarifaMinima;
								}
							}
						}

						//CQPRO00029160 - Se há 'valor mínimo frete quilo' deve desconsiderar a tabela
						if ((!BigDecimalUtils.hasValue(parametroCliente.getVlMinimoFreteQuilo()) || (!ConstantesExpedicao.MODAL_AEREO.equals(tpModal)))) {
							/*CQPRO00024485 - Solicitado por Eri*/
							if(CompareUtils.gt(vlTarifaMinima, vlFretePeso)) {
								vlFretePeso = vlTarifaMinima;
							}
						}
					} else {

						/*Rofoviário*/
						if(ConstantesExpedicao.MODAL_RODOVIARIO.equals(tpModal)|| "D".equals(tpTabelaPreco)) {

							/*Indicador Frete Peso D*/
							if("D".equals(tpIndicadorFretePeso)) {

								/*Indicador minimo frete peso T*/
								if( ("T".equals(tpIndicadorMinimoFretePeso)) || ("V".equals(tpIndicadorMinimoFretePeso)) ) {
									if(CompareUtils.lt(psReferencia, tabelaPreco.getPsMinimo())) {
										vlFretePeso = BigDecimalUtils.desconto(vlFretePesoTabela, parametroCliente.getVlPercMinimoProgr());
									} else {
										vlFretePeso = BigDecimalUtils.desconto(vlFretePesoTabela, parametroCliente.getVlFretePeso());
									}
									if("V".equals(tpIndicadorMinimoFretePeso)) {
										if(BigDecimalUtils.hasValue(parametroCliente.getVlMinFretePeso()) && CompareUtils.lt(vlFretePeso, parametroCliente.getVlMinFretePeso())) {
											vlFretePeso = parametroCliente.getVlMinFretePeso();
										}
									}

									/*Indicador minimo frete peso P*/
								} else if("P".equals(tpIndicadorMinimoFretePeso)) {
									BigDecimal vlParcelaFreteQuilo = parcelaFretePeso.getParametroAuxiliar("vlParcelaFreteQuilo");
									if(CompareUtils.lt(psReferencia, parametroCliente.getVlMinFretePeso())) {
										vlFretePeso = BigDecimalUtils.desconto(vlParcelaFreteQuilo.multiply(parametroCliente.getVlMinFretePeso()), parametroCliente.getVlPercMinimoProgr());
									} else {
										vlFretePeso = BigDecimalUtils.desconto(vlParcelaFreteQuilo.multiply(psReferencia), parametroCliente.getVlFretePeso());
									}
								}

								/*Indicador Frete Peso A*/
							} else if("A".equals(tpIndicadorFretePeso)) {

								/*Indicador minimo frete peso T ou V*/
								if( ("T".equals(tpIndicadorMinimoFretePeso)) || ("V".equals(tpIndicadorMinimoFretePeso)) ) {
									if(CompareUtils.lt(psReferencia, tabelaPreco.getPsMinimo())) {
										vlFretePeso = BigDecimalUtils.acrescimo(vlFretePesoTabela, parametroCliente.getVlPercMinimoProgr());
									} else {
										vlFretePeso = BigDecimalUtils.acrescimo(vlFretePesoTabela, parametroCliente.getVlFretePeso());
									}
									if(BigDecimalUtils.hasValue(parametroCliente.getVlMinFretePeso()) && CompareUtils.lt(vlFretePeso, parametroCliente.getVlMinFretePeso())) {
										vlFretePeso = parametroCliente.getVlMinFretePeso();
									}

									/*Indicador minimo frete peso P*/
								} else if("P".equals(tpIndicadorMinimoFretePeso)) {
									BigDecimal vlParcelaFreteQuilo = parcelaFretePeso.getParametroAuxiliar("vlParcelaFreteQuilo");
									if(CompareUtils.lt(psReferencia, parametroCliente.getVlMinFretePeso())) {
										vlFretePeso = BigDecimalUtils.acrescimo(vlParcelaFreteQuilo.multiply(parametroCliente.getVlMinFretePeso()), parametroCliente.getVlPercMinimoProgr());
									} else {
										vlFretePeso = BigDecimalUtils.acrescimo(vlParcelaFreteQuilo.multiply(psReferencia), parametroCliente.getVlFretePeso());
									}
								}
							}

							/*Aéreo*/
						} else if(ConstantesExpedicao.MODAL_AEREO.equals(tpModal) && "A".equals(tpTabelaPreco)){

							if("D".equals(tpIndicadorFretePeso)) {
								vlFretePeso = BigDecimalUtils.desconto(vlFretePesoTabela, parametroCliente.getVlFretePeso());
							} else if("A".equals(tpIndicadorFretePeso)) {
								vlFretePeso = BigDecimalUtils.acrescimo(vlFretePesoTabela, parametroCliente.getVlFretePeso());

								if(CompareUtils.gt(vlTarifaMinima, vlFretePeso)) {
									vlFretePeso = vlTarifaMinima;
								}
							}
						}
					}

				}

				/*Descrimina valores das generalidades no cálculo do frete*/
				if(BigDecimalUtils.hasValue(parametroCliente.getVlMinFretePeso()) && CompareUtils.ge(psReferencia, parametroCliente.getVlMinFretePeso())
						|| !BigDecimalUtils.hasValue(parametroCliente.getVlMinFretePeso()) && CompareUtils.ge(psReferencia, BigDecimalUtils.defaultBigDecimal(tabelaPreco.getPsMinimo()))) {
					descriminarGeneralidades(calculoFrete);
					embute = Boolean.FALSE;
				}

				/*Embute as parcelas generalidades necessárias*/
				if(embute && BigDecimalUtils.hasValue(calculoFrete.getVlEmbutidoParcelas())){
					vlFretePeso = BigDecimalUtils.defaultBigDecimal(vlFretePeso).add(calculoFrete.getVlEmbutidoParcelas());
				}

				/*Seta os valores cálculados*/
				parcelaFretePeso.setVlBrutoParcela(vlFretePeso);
				parcelaFretePeso.setVlParcela(vlFretePeso);

			}

		}/*--> FRETE PESO*/

		/*FRETE VALOR*/
		BigDecimal pesoMinimo = getCalculoParcelaFreteDAO().findMenorPeso(tabelaPreco.getIdTabelaPreco(), calculoFrete.getIdTarifa(), ConstantesExpedicao.CD_ADVALOREM);
		if(!checkPesoMinimoWithPesoReferencia(pesoMinimo, calculoFrete)){
			calcularFreteValor(calculoFrete, vlFreteValor, parametroCliente, tabelaPreco, vlMercadoria, parcelaFreteValor, tpModal, tpTabelaPreco);
		}
	}

	private void calcularFreteValor(CalculoFrete calculoFrete, BigDecimal vlFreteValor, ParametroCliente parametroCliente, TabelaPreco tabelaPreco, BigDecimal vlMercadoria, ParcelaServico parcelaFreteValor, String tpModal, String tpTabelaPreco) {
		String tpIndicadorAdvalorem = parametroCliente.getTpIndicadorAdvalorem().getValue();
		String tpIndicadorAdvalorem2 = parametroCliente.getTpIndicadorAdvalorem2().getValue();
		BigDecimal vlAdvalorem = parcelaFreteValor.getVlBrutoParcela();
		BigDecimal vlAdvaloremTabela = parcelaFreteValor.getVlUnitarioParcela();

		/*Calcula Frete Valor através dos parametros do grupo região*/
		if(!"V".equals(tpIndicadorAdvalorem) && calculoFrete.getGrupoRegiaoDestino() != null && getCalculoFreteService().getRestricaoRotaService().validateGrupoRegiaoDestinoPorTarifa(calculoFrete) ){
			executeCalculoFreteValorGrupoRegiao(calculoFrete.getGrupoRegiaoDestino(),calculoFrete);
			vlAdvaloremTabela = parcelaFreteValor.getVlUnitarioParcela();
			parcelaFreteValor.setVlBrutoParcela(calculoFrete.getVlMercadoria().multiply(BigDecimalUtils.percent(vlAdvaloremTabela)));
		}

		if(ConstantesExpedicao.MODAL_RODOVIARIO.equals(tpModal) || "D".equals(tpTabelaPreco)) {
			calcularFreteValorModalRodoviario(vlFreteValor, parametroCliente, vlMercadoria, parcelaFreteValor, tpIndicadorAdvalorem, vlAdvalorem, vlAdvaloremTabela);
		} else if(ConstantesExpedicao.MODAL_AEREO.equals(tpModal) && "A".equals(tpTabelaPreco)) {
			calcularFreteValorModalAereo(calculoFrete, parametroCliente, tabelaPreco, vlMercadoria, parcelaFreteValor, tpIndicadorAdvalorem, tpIndicadorAdvalorem2, vlAdvalorem);
		}
	}

	private void calcularFreteValorModalRodoviario(BigDecimal vlFreteValor, ParametroCliente parametroCliente, BigDecimal vlMercadoria, ParcelaServico parcelaFreteValor, String tpIndicadorAdvalorem, BigDecimal vlAdvalorem, BigDecimal vlAdvaloremTabela) {
		if(!BigDecimalUtils.hasValue(parametroCliente.getPcFretePercentual())) {

            /*Verifica o indicador AdValorem*/
			if("T".equals(tpIndicadorAdvalorem)) {
				vlFreteValor = parcelaFreteValor.getVlBrutoParcela();
			} else if("V".equals(tpIndicadorAdvalorem)) {
				vlFreteValor = vlMercadoria.multiply(BigDecimalUtils.percent(parametroCliente.getVlAdvalorem()));
			} else if("D".equals(tpIndicadorAdvalorem) || "A".equals(tpIndicadorAdvalorem) || "P".equals(tpIndicadorAdvalorem)) {
				if("D".equals(tpIndicadorAdvalorem)) {
					vlAdvalorem = BigDecimalUtils.desconto(vlAdvaloremTabela, parametroCliente.getVlAdvalorem());
				} else if("A".equals(tpIndicadorAdvalorem)) {
					vlAdvalorem = BigDecimalUtils.acrescimo(vlAdvaloremTabela, parametroCliente.getVlAdvalorem());
				} else if("P".equals(tpIndicadorAdvalorem)) {
					vlAdvalorem = vlAdvaloremTabela.add(parametroCliente.getVlAdvalorem());
				}
				vlFreteValor = vlMercadoria.multiply(BigDecimalUtils.percent(vlAdvalorem));
			}

			validaValorParcela(vlFreteValor);
			parcelaFreteValor.setVlBrutoParcela(vlFreteValor);
			parcelaFreteValor.setVlParcela(vlFreteValor);
		}
	}

	private void calcularFreteValorModalAereo(CalculoFrete calculoFrete, ParametroCliente parametroCliente, TabelaPreco tabelaPreco, BigDecimal vlMercadoria, ParcelaServico parcelaFreteValor, String tpIndicadorAdvalorem, String tpIndicadorAdvalorem2, BigDecimal vlAdvalorem) {
		BigDecimal vlReferencia = BigDecimalUtils.ZERO;

		BigDecimal vlMenorTaxaMinimaTabela = findValorPrecoFreteMenor(tabelaPreco.getIdTabelaPreco());
		String tpIndicadorValorReferencia = parametroCliente.getTpIndicadorValorReferencia().getValue();

			/*Verifica o indicador tpIndicadorValorReferencia*/
		if("T".equals(tpIndicadorValorReferencia)) {
            vlReferencia = vlMenorTaxaMinimaTabela.multiply(BigDecimalUtils.HUNDRED);
        } else if("V".equals(tpIndicadorValorReferencia)) {
            vlReferencia = parametroCliente.getVlValorReferencia();
        } else if("A".equals(tpIndicadorValorReferencia)) {
            vlReferencia = vlMenorTaxaMinimaTabela.multiply(BigDecimalUtils.HUNDRED);
            vlReferencia = BigDecimalUtils.acrescimo(vlReferencia, parametroCliente.getVlValorReferencia());
        } else if("D".equals(tpIndicadorValorReferencia)) {
            vlReferencia = vlMenorTaxaMinimaTabela.multiply(BigDecimalUtils.HUNDRED);
            vlReferencia = BigDecimalUtils.desconto(vlReferencia, parametroCliente.getVlValorReferencia());
        }

			/*Verifica os indicadores de AdValorem*/
		if(CompareUtils.le(BigDecimalUtils.divide(vlMercadoria, calculoFrete.getPsRealInformado()), vlReferencia)) {
            BigDecimal vlAdvalorem1 = findGeneralidade(tabelaPreco.getIdTabelaPreco(), ConstantesExpedicao.CD_ADVALOREM_1).getVlGeneralidade();
            if("T".equals(tpIndicadorAdvalorem)) {
                vlAdvalorem = vlAdvalorem1;
            } else if("V".equals(tpIndicadorAdvalorem)) {
                vlAdvalorem = parametroCliente.getVlAdvalorem();
            } else if("A".equals(tpIndicadorAdvalorem)) {
                vlAdvalorem = BigDecimalUtils.acrescimo(vlAdvalorem1, parametroCliente.getVlAdvalorem());
            } else if("D".equals(tpIndicadorAdvalorem)) {
                vlAdvalorem = BigDecimalUtils.desconto(vlAdvalorem1, parametroCliente.getVlAdvalorem());
            } else if("P".equals(tpIndicadorAdvalorem)) {
                vlAdvalorem = vlAdvalorem1.add(parametroCliente.getVlAdvalorem());
            }
        } else {
            BigDecimal vlAdvalorem2 = findGeneralidade(tabelaPreco.getIdTabelaPreco(), ConstantesExpedicao.CD_ADVALOREM_2).getVlGeneralidade();
            if("T".equals(tpIndicadorAdvalorem2)) {
                vlAdvalorem = vlAdvalorem2;
            } else if("V".equals(tpIndicadorAdvalorem2)) {
                vlAdvalorem = parametroCliente.getVlAdvalorem2();
            } else if("A".equals(tpIndicadorAdvalorem2)) {
                vlAdvalorem = BigDecimalUtils.acrescimo(vlAdvalorem2, parametroCliente.getVlAdvalorem2());
            } else if("D".equals(tpIndicadorAdvalorem2)) {
                vlAdvalorem = BigDecimalUtils.desconto(vlAdvalorem2, parametroCliente.getVlAdvalorem2());
            } else if("P".equals(tpIndicadorAdvalorem2)) {
                vlAdvalorem = vlAdvalorem2.add(parametroCliente.getVlAdvalorem2());
            }
        }
		BigDecimal vlFreteValor = vlMercadoria.multiply(BigDecimalUtils.percent(vlAdvalorem));

			/*Seta os valores da parcela*/
		parcelaFreteValor.setVlBrutoParcela(vlFreteValor);
		parcelaFreteValor.setVlParcela(vlFreteValor);
	}

	/**
	 * Descrimina o valor da generalidade nas parcelas
	 *
	 * @param calculoFrete
	 */
	public void descriminarGeneralidades(CalculoFrete calculoFrete){
		List<ParcelaServico> generalidades = calculoFrete.getGeneralidades();
		if(CollectionUtils.isNotEmpty(generalidades)){
			for (ParcelaServico parcelaServico : generalidades) {
				if(BooleanUtils.isTrue(parcelaServico.getBlEmbuteParcela())){
					parcelaServico.setVlBrutoParcela(parcelaServico.getVlEmbuteParcela());
					parcelaServico.setVlParcela(parcelaServico.getVlEmbuteParcela());
				}
			}
		}
	}

	/**
	 *
	 * Identifica se o documento possue complemento
	 * de volume 27 e 13L e calcula o valor do frete volume
	 *
	 * @param calculoFrete
	 * @return
	 */
	public BigDecimal findValorComplementarFreteVolume(CalculoFrete calculoFrete){

		BigDecimal vlFreteVolume = BigDecimal.ZERO;
		if(calculoFrete.getDoctoServico() == null || CollectionUtils.isEmpty(calculoFrete.getDoctoServico().getDadosComplementos())){
			return vlFreteVolume;
		}
		
		Integer qtVolumes13 = 0;
		Integer qtVolTotaliz = 0;
		Integer qtVolumes27 = 0;

			for(DadosComplemento dc : calculoFrete.getDoctoServico().getDadosComplementos()){

			if( dc.getInformacaoDoctoCliente() == null ){
				continue;
			}
				InformacaoDoctoCliente idc = informacaoDoctoClienteService.findByIdInitLazyProperties(dc.getInformacaoDoctoCliente().getIdInformacaoDoctoCliente(), false);
				if("Quantidade de volumes de 27L".equals(idc.getDsCampo())){
					qtVolumes27 = IntegerUtils.getInteger(dc.getDsValorCampo());
				}
				if("Quantidade de volumes de 13L".equals(idc.getDsCampo())){
					qtVolumes13 = IntegerUtils.getInteger(dc.getDsValorCampo());
				}
			if("VOLTOTALIZ".equals(idc.getDsCampo())){
				qtVolTotaliz = IntegerUtils.getInteger(dc.getDsValorCampo());
				}
			}/*for*/

			ParcelaPreco parcela = parcelaPrecoService.findByCdParcelaPreco(ConstantesExpedicao.CD_FRETE_PESO);

			BigDecimal vlFaixa132 = findValorFixoFaixa(calculoFrete,parcela,new BigDecimal(132));
			BigDecimal vlFaixa271 = findValorFixoFaixa(calculoFrete,parcela,new BigDecimal(271));

			if(qtVolumes27 > 0){

				BigDecimal vlFaixa272 = findValorFixoFaixa(calculoFrete,parcela,new BigDecimal(272));

				vlFreteVolume = vlFaixa271.add(BigDecimalUtils.getBigDecimal(qtVolumes13).multiply(vlFaixa132))
					.add( BigDecimalUtils.getBigDecimal((qtVolumes27 - 1)).multiply(vlFaixa272) );

			}else{

				if(qtVolumes13 > 0){
					BigDecimal vlFaixa131 = findValorFixoFaixa(calculoFrete, parcela,new BigDecimal(131));
					vlFreteVolume = vlFaixa131.add(BigDecimalUtils.getBigDecimal((qtVolumes13 - 1)).multiply(vlFaixa132));

				}else{
				if(qtVolTotaliz > 0) {						
					BigDecimal vlFaixa131 = findValorFixoFaixa(calculoFrete, parcela,new BigDecimal(131));				
					vlFreteVolume = vlFaixa131.add(this.obterValorVolTotal(vlFaixa132, qtVolTotaliz));
				}else {
					vlFreteVolume = vlFaixa271;
				}
			}
		}

		return vlFreteVolume;
	}
	private BigDecimal obterValorVolTotal(BigDecimal vlFaixa132 ,Integer qtVolTotaliz){
		Integer qtdeParesExtras = qtVolTotaliz - ConstantesExpedicao.QTDE_MINIMA_PARES_SAPATOS ;
		if(qtdeParesExtras > 0){
			return BigDecimalUtils.getBigDecimal((qtdeParesExtras)).multiply(vlFaixa132);
		}
			return BigDecimal.ZERO;		
	}

	private BigDecimal findValorFixoFaixa(CalculoFrete calculoFrete, ParcelaPreco parcelaPreco, BigDecimal vlFaixa){
		BigDecimal vlUnitarioParcela = BigDecimal.ZERO;

		FaixaProgressiva faixaProgressiva = faixaProgressivaService.findFaixaProgressivaEnquadrada(calculoFrete.getTabelaPreco().getIdTabelaPreco(), parcelaPreco.getIdParcelaPreco(), vlFaixa);
		ValorFaixaProgressiva valorFaixaProgressiva = getCalculoParcelaFreteDAO().findValorFaixaProgressiva(faixaProgressiva.getIdFaixaProgressiva(), null, calculoFrete.getIdTarifa(), null,null );

		if( valorFaixaProgressiva != null){
			vlUnitarioParcela = valorFaixaProgressiva.getVlFixo();
		}
		return vlUnitarioParcela;
	}

	/**
	 * Obtem ParcelaServico através de ParcelaPreco
	 *
	 * @param  vlParcela
	 * @param  cdParcelaPreco
	 * @return ParcelaServico
	 */
	public ParcelaServico findParcela(BigDecimal vlParcela, String cdParcelaPreco) {
		ParcelaPreco parcelaPreco = getCalculoParcelaFreteDAO().findParcelaPreco(cdParcelaPreco);

		ParcelaServico freteParcela = new ParcelaServico(parcelaPreco);
		freteParcela.setVlParcela(vlParcela);

		return freteParcela;
	}

	/**
	 * Obtem TaxaCliente verificando o atributo calculoFrete.blRecalculoCotacao
	 *
	 * @param  calculoFrete
	 * @param  parcelaServico
	 * @return TaxaCliente
	 */
	private TaxaCliente findTaxaCliente(CalculoFrete calculoFrete,	ParcelaServico parcelaServico) {
		if(Boolean.TRUE.equals(calculoFrete.getBlRecalculoCotacao())) {
			if (calculoFrete.getIdDivisaoCliente() != null){
				parcelaServico.setParametro(getCalculoParcelaFreteDAO().findTaxaCliente(calculoFrete.getTabelaPreco().getIdTabelaPreco(), parcelaServico.getParcelaPreco().getIdParcelaPreco(), calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico(), calculoFrete.getRestricaoRotaOrigem(), calculoFrete.getRestricaoRotaDestino()));
		} else {
				return (TaxaCliente) parcelaServico.getParametro();
			}
		} else {
			ParametroCliente pc = calculoFrete.getParametroCliente();
			TaxaCliente taxaCliente = getCalculoParcelaFreteDAO().findTaxaClienteByParametro(pc.getIdParametroCliente(),parcelaServico.getParcelaPreco().getIdParcelaPreco());
			if(COTACAO.equals(calculoFrete.getTpCalculo()) && taxaCliente != null) {
				parcelaServico.setParametro(taxaCliente);
			}else if (calculoFrete.getIdDivisaoCliente() != null){
				parcelaServico.setParametro(getCalculoParcelaFreteDAO().findTaxaCliente(calculoFrete.getTabelaPreco().getIdTabelaPreco(), parcelaServico.getParcelaPreco().getIdParcelaPreco(), calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico(), calculoFrete.getRestricaoRotaOrigem(), calculoFrete.getRestricaoRotaDestino()));
			}
		}
		return (TaxaCliente) parcelaServico.getParametro();
	}

	/**
	 * Obtem o objeto PrecoFrete
	 *
	 * @param  idTabelaPreco
	 * @param  cdParcelaPreco
	 * @param  idTarifaPreco
	 * @param  idRotaPreco
	 * @return PrecoFrete
	 */
	private PrecoFrete findPrecoFrete(Long idTabelaPreco, String cdParcelaPreco, Long idTarifaPreco, Long idRotaPreco) {
		PrecoFrete precoFrete = getCalculoParcelaFreteDAO().findFretePeso(idTabelaPreco, cdParcelaPreco, idTarifaPreco, idRotaPreco);
		if(precoFrete == null) {
			throw new BusinessException("LMS-30022", new Object[]{cdParcelaPreco});
		}
		return precoFrete;
	}

	/**
	 * Calcula o frete valor através dos parametros do grupo região
	 * informado
	 *
	 * @param grupoRegiao
	 * @param calculoFrete
	 */
	public void executeCalculoFreteValorGrupoRegiao(GrupoRegiao grupoRegiao, CalculoFrete calculoFrete){

		ParcelaServico parcelaFreteValor = calculoFrete.getParcelaGeral(ConstantesExpedicao.CD_FRETE_VALOR);

		executeParametrizacaoParcelaFreteValorGrupoRegiao(parcelaFreteValor,grupoRegiao);

		parcelaFreteValor.setVlBrutoParcela(calculoFrete.getVlMercadoria().multiply(BigDecimalUtils.percent(parcelaFreteValor.getVlUnitarioParcela())));

	}

	public void executeParametrizacaoParcelaFreteValorGrupoRegiao(ParcelaServico parcelaServico, GrupoRegiao grupoRegiao){

		if(parcelaServico == null){
			return;
		}

		BigDecimal vlParcela = BigDecimalUtils.defaultBigDecimal(parcelaServico.getVlUnitarioParcela());

		if(grupoRegiao.getTpValorAjusteAdvalorem() == null){
			if("V".equals(grupoRegiao.getTpValorAjusteAdvalorem().getValue())){
				vlParcela = grupoRegiao.getVlAjustePadraoAdvalorem();
			}

		}else{

			if("A".equals(grupoRegiao.getTpAjusteAdvalorem().getValue())){
				if("P".equals(grupoRegiao.getTpValorAjusteAdvalorem().getValue())){
					vlParcela = BigDecimalUtils.acrescimo(vlParcela, grupoRegiao.getVlAjustePadraoAdvalorem());
				}else if("V".equals(grupoRegiao.getTpValorAjusteAdvalorem().getValue()) || "T".equals(grupoRegiao.getTpValorAjusteAdvalorem().getValue())){
					vlParcela = vlParcela.add(grupoRegiao.getVlAjustePadraoAdvalorem());
				}
			}else if("D".equals(grupoRegiao.getTpAjusteAdvalorem().getValue())){
				if("P".equals(grupoRegiao.getTpValorAjusteAdvalorem().getValue())){
					vlParcela = BigDecimalUtils.desconto(vlParcela, grupoRegiao.getVlAjustePadraoAdvalorem());
				}else if("V".equals(grupoRegiao.getTpValorAjusteAdvalorem().getValue()) || "T".equals(grupoRegiao.getTpValorAjusteAdvalorem().getValue())){
					vlParcela = vlParcela.subtract(grupoRegiao.getVlAjustePadraoAdvalorem());
				}
			}
		}/*else*/

		/*Frete Valor*/
		parcelaServico.setVlUnitarioParcela(vlParcela);

	}

	/**
	 * Aplica a parametrização do frete peso grupo região
	 *
	 * @param grupoRegiao
	 * @param calculoFrete
	 */
	public void executeCalculoFretePesoGrupoRegiao(GrupoRegiao grupoRegiao, CalculoFrete calculoFrete){

		ParcelaServico parcelaFretePeso  = calculoFrete.getParcelaGeral(ConstantesExpedicao.CD_FRETE_PESO);
		ParcelaServico parcelaFreteQuilo = calculoFrete.getParcelaGeral(ConstantesExpedicao.CD_FRETE_QUILO);

		executeParametrizacaoParcelaFretePesoGrupoRegiao(parcelaFretePeso,grupoRegiao);
		executeParametrizacaoParcelaFreteValorGrupoRegiao(parcelaFreteQuilo,grupoRegiao);

	}

	/**
	 * Através dos parametros do grupo região calcula o valor da parcela
	 *
	 * @param parcelaServico
	 * @param grupoRegiao
	 */
	public void executeParametrizacaoParcelaFretePesoGrupoRegiao(ParcelaServico parcelaServico, GrupoRegiao grupoRegiao){

		if(parcelaServico == null){
			return;
		}

		BigDecimal vlParcela = BigDecimalUtils.defaultBigDecimal(parcelaServico.getVlBrutoParcela());

		if(grupoRegiao.getTpValorAjuste() == null && BigDecimalUtils.hasValue(grupoRegiao.getVlAjustePadrao())){
				vlParcela  = grupoRegiao.getVlAjustePadrao();
		}else{
			if("A".equals(grupoRegiao.getTpAjuste().getValue()) && "P".equals(grupoRegiao.getTpValorAjuste().getValue()) && BigDecimalUtils.hasValue(grupoRegiao.getVlAjustePadrao())){
						vlParcela  = BigDecimalUtils.acrescimo(vlParcela, grupoRegiao.getVlAjustePadrao());
			}else if("D".equals(grupoRegiao.getTpAjuste().getValue())){
				if("P".equals(grupoRegiao.getTpValorAjuste().getValue()) && BigDecimalUtils.hasValue(grupoRegiao.getVlAjustePadrao())){
						vlParcela  = BigDecimalUtils.desconto(vlParcela, grupoRegiao.getVlAjustePadrao());
				}else if("V".equals(grupoRegiao.getTpValorAjuste().getValue()) && BigDecimalUtils.hasValue(grupoRegiao.getVlAjustePadrao())){
						vlParcela  = vlParcela.subtract(grupoRegiao.getVlAjustePadrao());
				}
			}

		}/*else*/

		parcelaServico.setVlBrutoParcela(vlParcela);
	}


	/**
	 * Obtem o grupo região para o municipio
	 *
	 * @param idTabelaPreco
	 * @param restricaoRota
	 */
	public GrupoRegiao findGrupoRegiao(Long idTabelaPreco, Long idMunicipio){

		return municipioGrupoRegiaoService.findGrupoRegiaoAtendimento(idTabelaPreco, idMunicipio);
	}

	/**
	 * Obtem o TarifaPreco verificando o tipo (@,B,I) e sub tipo da tabela (F,P)
	 *
	 * @param  calculoFrete
	 * @return TarifaPreco
	 */
	private TarifaPreco findTarifaPreco(CalculoFrete calculoFrete) {

		TarifaPreco tarifaPreco = null;

		if(BooleanUtils.isTrue(calculoFrete.getRecalculoFrete()) && calculoFrete.getTarifaPreco()!=null){
			tarifaPreco = calculoFrete.getTarifaPreco();
			calculoFrete.setIdTarifa(tarifaPreco.getIdTarifaPreco());
		}

			String tpTabelaPreco = calculoFrete.getTabelaPreco().getTipoTabelaPreco().getTpTipoTabelaPreco().getValue();
			String sbTpTabelaPreco = calculoFrete.getTabelaPreco().getSubtipoTabelaPreco().getTpSubtipoTabelaPreco();
			if("D".equals(tpTabelaPreco)
					|| "@".equals(tpTabelaPreco)
					|| "B".equals(tpTabelaPreco)
					|| "I".equals(tpTabelaPreco)
					|| "F".equals(sbTpTabelaPreco)
					|| "P".equals(sbTpTabelaPreco)) {


				/*Adiciona informarções a rota de origem para a consulta da TarifaPreco*/
				RestricaoRota restricaoRotaOrigem = calculoFrete.getRestricaoRotaOrigem();
				restricaoRotaOrigem.setSbTabelaPreco(sbTpTabelaPreco);

				String dsLocalComercial = tipoLocalizacaoMunicipioService.findById(restricaoRotaOrigem.getIdTipoLocalizacaoComercial()).getDsTipoLocalizacaoMunicipio().getValue();

				Boolean isLocalComercialCapital = "Capital".equals(dsLocalComercial);
				restricaoRotaOrigem.setIsLocalComercialCapital(isLocalComercialCapital);

				/*Grupo Regiao Origem*/
				GrupoRegiao grupoRegiaoOrigem = calculoFrete.getGrupoRegiaoOrigem();
				if(grupoRegiaoOrigem != null){
					restricaoRotaOrigem.setIdGrupoRegiao(grupoRegiaoOrigem.getIdGrupoRegiao());
				}

				/*Adiciona informações a rota de destino para a consulta da TarifaPreco*/
				RestricaoRota restricaoRotaDestino = calculoFrete.getRestricaoRotaDestino();
				restricaoRotaDestino.setSbTabelaPreco(sbTpTabelaPreco);

				dsLocalComercial = tipoLocalizacaoMunicipioService.findById(restricaoRotaDestino.getIdTipoLocalizacaoComercial()).getDsTipoLocalizacaoMunicipio().getValue();

				isLocalComercialCapital = "Capital".equals(dsLocalComercial);
				restricaoRotaDestino.setIsLocalComercialCapital(isLocalComercialCapital);

				/*Grupo Regiao Destino*/
				GrupoRegiao grupoRegiaoDestino = calculoFrete.getGrupoRegiaoDestino();
				if(grupoRegiaoDestino != null){
					restricaoRotaDestino.setIdGrupoRegiao(grupoRegiaoDestino.getIdGrupoRegiao());
				}


				/* Correção para obter a tarifa correta através da Rota.
				 * ZONA - PAIS - UNIDADE FEDERATIVA - GRUPO REGIÃO - FILIAL - MUNICIPIO
				 * Referente ao JIRA LMS-416*/
				TarifaPrecoRota tarifaPrecoRota = getCalculoFreteService().getRestricaoRotaService().findTarifaPrecoRota(calculoFrete);
				if( tarifaPrecoRota != null ){
					tarifaPreco = tarifaPrecoRota.getTarifaPreco();
			}
		}
		
		if(tarifaPreco == null) {
			throw new BusinessException("LMS-30029");
		}
		
		return tarifaPreco;
	}

	private BigDecimal findValorUnitarioParcela(
			Long idTabelaPreco,
			ParcelaPreco parcelaPreco,
			BigDecimal psReferencia,
			Long idRotaPreco,
			Long idTarifaPreco,
			Long idProdutoEspecifico
	) {
		BigDecimal vlUnitarioParcela = null;

		if("G".equals(parcelaPreco.getTpPrecificacao().getValue())) {
			Generalidade generalidade = findGeneralidade(idTabelaPreco, parcelaPreco.getCdParcelaPreco());
			if(generalidade.getTabelaPrecoParcela().getParcelaPreco().getBlEmbuteParcela().booleanValue()) {
				ValorFaixaProgressiva valorFaixaProgressiva = getCalculoParcelaFreteDAO().findValorFaixaProgressivaEnquadrada(idTabelaPreco, parcelaPreco.getIdParcelaPreco(), psReferencia, idProdutoEspecifico, idRotaPreco, idTarifaPreco, null,false);
				vlUnitarioParcela = BigDecimalUtils.desconto(generalidade.getVlGeneralidade(), valorFaixaProgressiva.getPcTaxa());
			} else {
				vlUnitarioParcela = generalidade.getVlGeneralidade();
			}
		}
		return vlUnitarioParcela;
	}

	/**
	 * Obtem o valor da faixa progressiva através de um rota
	 *
	 * @param  calculoFrete
	 * @param  idParcelaPreco
	 * @param  idFaixaProgressiva
	 * @return ValorFaixaProgressiva
	 */
	private ValorFaixaProgressiva findValorFaixaProgressivaRotaPreco(CalculoFrete calculoFrete, Long idParcelaPreco, Long idFaixaProgressiva) {

		ValorFaixaProgressiva valorFaixaProgressiva = null;

		/*Obtem tipo tabela preço*/
		String tpTabelaPreco = calculoFrete.getTabelaPreco().getTipoTabelaPreco().getTpTipoTabelaPreco().getValue();

		/*Se o tipo da tabela for aéreo obtem o valor da faixa progressiva*/
		String cdParcelaPreco = parcelaPrecoService.findByIdInitLazyProperties(idParcelaPreco, false).getCdParcelaPreco();
		if("A".equals(tpTabelaPreco)) {
			valorFaixaProgressiva = getCalculoParcelaFreteDAO().findValorFaixaProgressivaRotaPreco(cdParcelaPreco,calculoFrete.getTabelaPreco().getIdTabelaPreco(), idParcelaPreco, idFaixaProgressiva, calculoFrete.getRestricaoRotaOrigem(), calculoFrete.getRestricaoRotaDestino());
		}

		/*Caso valorFaixaProgressiva for nulo retorna uma
		LMS-30024*/
		if(valorFaixaProgressiva == null){
			throw new BusinessException("LMS-30024");
		}

		/*Se a parcela preço não for CD_TAXA_TERRESTRE é obrigatório
		a faixa progressiva ter RotaPreco*/
		if(!ConstantesExpedicao.CD_TAXA_TERRESTRE.equals(cdParcelaPreco)
				&& valorFaixaProgressiva.getRotaPreco() == null){
			throw new BusinessException("LMS-30024");
		}

		return valorFaixaProgressiva;
	}

	/**
	 * Obtem ParcelaDoctoServico
	 *
	 * @param  idDoctoServico
	 * @param  cdParcelaPreco
	 * @return ParcelaDoctoServico
	 */
	public ParcelaDoctoServico findParcelaDoctoServico(Long idDoctoServico, String cdParcelaPreco) {
		return getCalculoParcelaFreteDAO().findParcelaDoctoServico(idDoctoServico, cdParcelaPreco);
	}

	private void validaValorParcela(BigDecimal valorParcela){
		if (valorParcela != null && valorParcela.compareTo(BigDecimal.ZERO) < 0){
			throw new BusinessException("LMS-04363");
		}
	}

	public CalculoParcelaFreteDAO getCalculoParcelaFreteDAO() {
		return (CalculoParcelaFreteDAO) super.getCalculoParcelaServicoDAO();
	}
	public void setCalculoParcelaFreteDAO(CalculoParcelaFreteDAO calculoParcelaFreteDAO) {
		super.setCalculoParcelaServicoDAO(calculoParcelaFreteDAO);
	}

	public CalculoFreteService getCalculoFreteService() {
		return (CalculoFreteService) super.getCalculoServicoService();
	}
	public void setCalculoFreteService(CalculoFreteService calculoFreteService) {
		super.setCalculoServicoService(calculoFreteService);
	}
	public void setFaixaProgressivaService(FaixaProgressivaService faixaProgressivaService) {
		this.faixaProgressivaService = faixaProgressivaService;
	}
	public void setMcdService(McdService mcdService) {
		this.mcdService = mcdService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setOperacaoServicoLocalizaService(OperacaoServicoLocalizaService operacaoServicoLocalizaService) {
		this.operacaoServicoLocalizaService = operacaoServicoLocalizaService;
	}

	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}

	public void setTipoLocalizacaoMunicipioService(
			TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService) {
		this.tipoLocalizacaoMunicipioService = tipoLocalizacaoMunicipioService;
	}

	public AjusteTarifaService getAjusteTarifaService() {
		return ajusteTarifaService;
	}

	public void setAjusteTarifaService(AjusteTarifaService ajusteTarifaService) {
		this.ajusteTarifaService = ajusteTarifaService;
	}

	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
	}

	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}
		public TarifaPrecoRotaService getTarifaPrecoRotaService() {		return tarifaPrecoRotaService;	}	public void setTarifaPrecoRotaService(			TarifaPrecoRotaService tarifaPrecoRotaService) {		this.tarifaPrecoRotaService = tarifaPrecoRotaService;	}
	public MunicipioGrupoRegiaoService getMunicipioGrupoRegiaoService() {
		return municipioGrupoRegiaoService;
	}

	public void setMunicipioGrupoRegiaoService(
			MunicipioGrupoRegiaoService municipioGrupoRegiaoService) {
		this.municipioGrupoRegiaoService = municipioGrupoRegiaoService;
	}

	public GrupoRegiaoService getGrupoRegiaoService() {
		return grupoRegiaoService;
	}

	public void setGrupoRegiaoService(GrupoRegiaoService grupoRegiaoService) {
		this.grupoRegiaoService = grupoRegiaoService;
	}

	public InformacaoDoctoClienteService getInformacaoDoctoClienteService() {
		return informacaoDoctoClienteService;
	}

	public void setInformacaoDoctoClienteService(
			InformacaoDoctoClienteService informacaoDoctoClienteService) {
		this.informacaoDoctoClienteService = informacaoDoctoClienteService;
	}

	public void setMunicipioFilialService(
			MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

	public void setPostoPassagemMunicipioService(
			PostoPassagemMunicipioService postoPassagemMunicipioService) {
		this.postoPassagemMunicipioService = postoPassagemMunicipioService;
	}

	public MunicipioTrtClienteService getMunicipioTrtClienteService() {
		return municipioTrtClienteService;
	}

	public void setMunicipioTrtClienteService(
			MunicipioTrtClienteService municipioTrtClienteService) {
		this.municipioTrtClienteService = municipioTrtClienteService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setFaixaProgressivaPropostaService(
			FaixaProgressivaPropostaService faixaProgressivaPropostaService) {
		this.faixaProgressivaPropostaService = faixaProgressivaPropostaService;
	}	

	public TdeClienteService getTdeClienteService() {
		return tdeClienteService;
	}

	public void setTdeClienteService(TdeClienteService tdeClienteService) {
		this.tdeClienteService = tdeClienteService;
	}

	public DestinatarioTdeClienteService getDestinatarioTdeClienteService() {
		return destinatarioTdeClienteService;
	}

	public void setDestinatarioTdeClienteService(
			DestinatarioTdeClienteService destinatarioTdeClienteService) {
		this.destinatarioTdeClienteService = destinatarioTdeClienteService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setDivisaoParcelaService(DivisaoParcelaService divisaoParcelaService) {
		this.divisaoParcelaService = divisaoParcelaService;
	}

	public void setServicoAdicionalClienteService(ServicoAdicionalClienteService servicoAdicionalClienteService) {
        this.servicoAdicionalClienteService = servicoAdicionalClienteService;
    }

	public DadosComplementoService getDadosComplementoService() {
        return dadosComplementoService;
	}

    public void setDadosComplementoService(DadosComplementoService dadosComplementoService) {
        this.dadosComplementoService = dadosComplementoService;
    }

	public void setTabelaMunicipioEMEXService(TabelaMunicipioEMEXService tabelaMunicipioEMEXService) {
		this.tabelaMunicipioEMEXService = tabelaMunicipioEMEXService;
	}

	public void setRotaPrecoService(RotaPrecoService rotaPrecoService) {
		this.rotaPrecoService = rotaPrecoService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
}
