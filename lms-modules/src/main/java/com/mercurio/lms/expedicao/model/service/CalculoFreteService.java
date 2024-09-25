package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.TipoTributacaoIEService;
import com.mercurio.lms.expedicao.model.*;
import com.mercurio.lms.expedicao.model.dao.CalculoFreteDAO;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.service.*;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.tabelaprecos.model.*;
import com.mercurio.lms.tabelaprecos.model.service.GeneralidadeService;
import com.mercurio.lms.tributos.model.AliquotaIcms;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.tributos.model.param.CalcularIDifalParam;
import com.mercurio.lms.tributos.model.param.CalcularIcmsParam;
import com.mercurio.lms.tributos.model.service.CalcularDifalService;
import com.mercurio.lms.tributos.model.service.CalcularIcmsService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.*;
import com.mercurio.lms.vendas.model.service.*;
import com.mercurio.lms.vendas.util.ClienteUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Claiton Grings
 * @spring.bean id="lms.expedicao.calculoFreteService" autowire="no"
 * @spring.property name="calculoFreteDAO" ref="lms.expedicao.calculoFreteDAO"
 * @spring.property name="calculoParcelaFreteService" ref="lms.expedicao.calculoParcelaFreteService"
 * @spring.property name="calculoTributoService" ref="lms.expedicao.calculoTributoService"
 * @spring.property name="configuracoesFacade" ref="lms.configuracoesFacade"
 * @spring.property name="ppeService" ref="lms.municipios.ppeService"
 * @spring.property name="conhecimentoService" ref="lms.expedicao.conhecimentoService"
 * @spring.property name="mcdService" ref="lms.municipios.mcdService"
 * @spring.property name="municipioFilialService" ref="lms.municipios.municipioFilialService"
 * @spring.property name="informacaoDocServicoService" ref="lms.expedicao.informacaoDocServicoService"
 * @spring.property name="inscricaoEstadualService" ref="lms.configuracoes.inscricaoEstadualService"
 * @spring.property name="tabelaDivisaoClienteService" ref="lms.vendas.tabelaDivisaoClienteService"
 * @spring.property name="enderecoPessoaService" ref="lms.configuracoes.enderecoPessoaService"
 * @spring.property name="volumeNotaFiscalService" ref="lms.expedicao.volumeNotaFiscalService"
 * @spring.property name="tipoTributacaoIEService" ref="lms.configuracoes.tipoTributacaoIEService"
 * @spring.property name="restricaoRotaService"    ref="lms.expedicao.restricaoRotaService"
 * @spring.property name="tipoLocalizacaoMunicipioService"    ref="lms.municipios.tipoLocalizacaoMunicipioService"
 * * @spring.property name="municipioDestinoCalculoService"    ref="lms.vendas.municipioDestinoCalculoService"
 * @spring.property name="filialService" ref="lms.municipios.filialService"
 */
public class CalculoFreteService extends CalculoServicoService {

    private static final int INT_VALOR_DIMENSAO_DIVISAO = 1000000;
    private static final int INT_VALOR_ESCALA = 5;
    private ConfiguracoesFacade configuracoesFacade;
    private PpeService ppeService;
    private ConhecimentoService conhecimentoService;
    private McdService mcdService;
    private MunicipioFilialService municipioFilialService;
    private InformacaoDocServicoService informacaoDocServicoService;
    private InscricaoEstadualService inscricaoEstadualService;
    private TabelaDivisaoClienteService tabelaDivisaoClienteService;
    private EnderecoPessoaService enderecoPessoaService;
    private VolumeNotaFiscalService volumeNotaFiscalService;
    private TipoTributacaoIEService tipoTributacaoIEService;
    private RestricaoRotaService restricaoRotaService;
    private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService;
    private MunicipioDestinoCalculoService municipioDestinoCalculoService;
    private FilialService filialService;
    private FatorCubagemDivisaoService fatorCubagemDivisaoService;
    private GeneralidadeService generalidadeService;
    private CalcularIcmsService calcularIcmsService;
    private ImpostoServicoService impostoServicoService;
    private ClienteService clienteService;
    private CalcularDifalService calcularDifalService;
    private ParametroGeralService parametroGeralService;
    private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
    private OcorrenciaPendenciaService ocorrenciaPendenciaService;
    private InformacaoDoctoClienteService informacaoDoctoClienteService;

    public void findPesoReferenciaExterno(CalculoServico calculoServico) {
        findPesoReferencia(calculoServico);
    }

    /**
     * Retorna o peso da mercadoria para cálculo do frete
     * <p>
     * Arredonda o valor do peso conforme seu tipo
     *
     * @param calculoServico
     */

    protected void findPesoReferencia(CalculoServico calculoServico) {

        Boolean naoExisteVolumeSemDimensaoCubagem = true;

        BigDecimal totalDimensao = null;
        BigDecimal totalCubagem = null;
        BigDecimal totalCubagemAferida = null;
        BigDecimal totalCubagemAferidaSorter = null;
        BigDecimal nrCubagemCalculo = null;
        BigDecimal psCubagemAferido = null;

        int nrCasasDesimais = 0;
        String tpFormaArredondamento = null;
        BigDecimal psReferencia = null;

        CalculoFrete calculoFrete = (CalculoFrete) calculoServico;
        Conhecimento doctoServico = calculoFrete.getDoctoServico();
        if (doctoServico == null) {
            doctoServico = new Conhecimento();
        }

        doctoServico.setBlUtilizaPesoEdi(false);
        doctoServico.setBlPesoFatPorCubadoAferido(false);
        doctoServico.setBlPesoCubadoPorDensidade(false);

        // LMS-6531 - ET 04.01.01.08, item 1
        Boolean blPesoModulo = false;
        psCubagemAferido = BigDecimal.ZERO;

        BigDecimal psAferido = doctoServico.getPsAferido();
        BigDecimal psCubado = calculoFrete.getPsCubadoInformado();
        BigDecimal psReal = calculoFrete.getPsRealInformado();
        BigDecimal psAferidoSorter = null;

		/*Verifica se os volumes das notas possue o valor e cálcula o peso cubado*/
        BigDecimal vlTotalVolume = BigDecimal.ZERO;

        TabelaPreco tabelaPreco = calculoFrete.getTabelaPreco();

        /*Caso seja cliente telefonica - PPM 505638*/

        if(tabelaPreco != null && tabelaPreco.getTpCalculoFretePeso() != null && tabelaPreco.getTpCalculoFretePeso().getValue().equals("T") && isClienteTelefonicaCalculoFrete(calculoFrete)){
            calculoFrete.setPsReferencia(obterQtdeVolume(calculoFrete));
            doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
            return;
        }

		/*Verifica se possue id do documento*/
        if (LongUtils.hasValue(doctoServico.getIdDoctoServico())) {
            if (calculoFrete.getBlRecalculoFreteSorter()) {
                vlTotalVolume = BigDecimalUtils.defaultBigDecimal(volumeNotaFiscalService.findTotalDimensaoVolumesSorter(calculoFrete.getDoctoServico().getIdDoctoServico()));
                psAferidoSorter = BigDecimalUtils.defaultBigDecimal(volumeNotaFiscalService.findTotalPsAferidoSorter(calculoFrete.getDoctoServico().getIdDoctoServico()));

                totalCubagemAferidaSorter = vlTotalVolume.divide(BigDecimal.valueOf(INT_VALOR_DIMENSAO_DIVISAO));
            } else {

			/*Obtem o valor total das dimensoes de volumes da nota*/
                vlTotalVolume = BigDecimalUtils.defaultBigDecimal(volumeNotaFiscalService.findTotalDimensaoVolumes(calculoFrete.getDoctoServico().getIdDoctoServico()));
                BigDecimal vlTotalCubagemVolumesDocto = BigDecimalUtils.defaultBigDecimal(volumeNotaFiscalService.findTotalCubagemVolumes(calculoFrete.getDoctoServico().getIdDoctoServico()));

                vlTotalVolume = vlTotalVolume.add(vlTotalCubagemVolumesDocto.multiply(BigDecimal.valueOf(INT_VALOR_DIMENSAO_DIVISAO)));

                //LMS-2353
                totalDimensao = vlTotalVolume.divide(BigDecimal.valueOf(INT_VALOR_DIMENSAO_DIVISAO));
                totalCubagem = vlTotalCubagemVolumesDocto.divide(BigDecimal.valueOf(INT_VALOR_DIMENSAO_DIVISAO));
                if (vlTotalVolume != null && totalCubagem != null) {
                    totalCubagemAferida = totalDimensao.add(totalCubagem);
                } else {
                    totalCubagemAferida = totalCubagem;
                }

                naoExisteVolumeSemDimensaoCubagem = !volumeNotaFiscalService.existeVolumeSemDimensaoCubagem(calculoFrete.getDoctoServico().getIdDoctoServico());

                // LMS-6531 - ET 04.01.01.08, item 1.6
                Long idDoctoServico = calculoFrete.getDoctoServico().getIdDoctoServico();
                if (!volumeNotaFiscalService.pesoOriginadoBalancaModulo(idDoctoServico)) {
                    blPesoModulo = false;
                }
            }

        }

		/*Verifica se possue valor nos volumes*/
        final Boolean blIndicadorEdi = doctoServico.getBlIndicadorEdi();
        final Boolean blUtilizaPesoEDI = CalculoFreteUtils.isUtilizaPesoEDI(doctoServico);

        //LMS-3429 Se esta soma resultar um valor diferente de zero (resultado do passo 3 do item “Para as demais formas de cálculo ou recálculo do frete”
        // ou do passo A do item “Para recálculo do frete do Sorter”) fazer:
        if (BigDecimalUtils.hasValue(vlTotalVolume)) {
			/*Calcula o peso cubado real*/
            doctoServico.setPsCubadoReal(calculaPesoCubadoReal(calculoFrete, vlTotalVolume));
        }



        if (BigDecimalUtils.hasValue(vlTotalVolume) /*LMS-2353*/ && (BooleanUtils.isFalse(blIndicadorEdi) || BooleanUtils.isFalse(blUtilizaPesoEDI))) {

			/*Anula o valor do fator densidade, caso o peso cubado seja calculado através do volume*/
            if (calculoFrete.getDoctoServico() != null) calculoFrete.getDoctoServico().setNrFatorDensidade(null);
            doctoServico.setNrFatorDensidade(null);
			/*Verifica se o cliente é especial*/
            boolean isClienteEspecial = calculoFrete.getClienteBase() != null && calculoFrete.getClienteBase().getTpCliente() != null && ClienteUtils.isParametroClienteEspecial(calculoFrete.getClienteBase().getTpCliente().getValue());
            boolean isFatorCubagemNull = true;
            if (BooleanUtils.isTrue(isClienteEspecial)) {
				/*Obtem a tabela divisao cliente*/
                TabelaDivisaoCliente tdc = tabelaDivisaoClienteService
                        .findTabelaDivisaoCliente(calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico());
				/*Verifica se o atributo NrFatorCubagem possue valor e cálcula
				 * o pesoAforado. Valor obtido no volumes X nrFatorCubagem / 1000000 */
                if (tdc != null) {
                    isFatorCubagemNull = tdc.getNrFatorCubagem() == null;
                    if ((BigDecimalUtils.hasValue(tdc.getNrFatorCubagem()))) {
                        psCubado = BigDecimalUtils.divide(vlTotalVolume.multiply(tdc.getNrFatorCubagem()), new BigDecimal("1000000"));
                    }
                    doctoServico.setNrFatorCubagem(tdc.getNrFatorCubagem());
                }
            }

            if (BooleanUtils.isFalse(isClienteEspecial) || isFatorCubagemNull) {
					/*Caso NrFatorCubagem for zero*/
                BigDecimal psCubicoModal;
                BigDecimal fatorCubagemModal;
                if (ConstantesExpedicao.MODAL_AEREO.equals(calculoFrete.getTpModal())) {
						/*Cálcula peso aforado aereo*/
                    fatorCubagemModal = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_AEREO);
                    psCubicoModal = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_PESO_METRAGEM_CUBICA_AEREO);
                } else {
						/*Cálcula peso aforado rodoviario*/
                    fatorCubagemModal = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_RODO);
                    psCubicoModal = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_PESO_METRAGEM_CUBICA_RODOVIARIO);
                }
                psCubado = BigDecimalUtils.divide(vlTotalVolume, BigDecimalUtils.defaultBigDecimal(psCubicoModal));

                doctoServico.setNrFatorCubagem(fatorCubagemModal);
            }
			/*Caso vlTotalVolume for zero*/

            // LMS-6531 - ET 04.01.01.08, item 1
            psCubagemAferido = psCubado;
            psCubado = calculoFrete.getPsCubadoInformado();
            if (BooleanUtils.isTrue(isClienteEspecial)) {
                TabelaDivisaoCliente tdc = tabelaDivisaoClienteService.findTabelaDivisaoCliente(
                        calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico());
                if (tdc != null && tdc.getTpPesoCalculo() != null) {
                    String tpPesoCalculo = tdc.getTpPesoCalculo().getValue();
                    if (ConstantesExpedicao.TP_PESO_CALCULO_AUTOMATICO.equals(tpPesoCalculo)
                            || ConstantesExpedicao.TP_PESO_CALCULO_CUBADO.equals(tpPesoCalculo)) {
                        psCubado = psCubagemAferido;
                    }
                }
            }

            //LMS-2353
            nrCubagemCalculo = totalCubagemAferida;

            //LMS-5146
            //Se o cliente responsável pelo cálculo do frete for “Especial” ou
            //Filial de cliente   especial” e se CLIENTE.BL_UTILIZA_PESO_EDI = “N”,
            if (BooleanUtils.isTrue(isClienteEspecial) && BooleanUtils.isFalse(blIndicadorEdi)) {
                //verificar se para a TABELA_DIVISAO_CLIENTE da divisão que deverá ser
                //utilizada no cálculo do frete existe um registro
                //e o campo NR_FATOR_DENSIDADE <> NULL e NR_FATOR_CUBAGEM = Zero fazer:
                TabelaDivisaoCliente tdc = tabelaDivisaoClienteService.findTabelaDivisaoCliente(calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico());
                if (tdc != null && tdc.getNrFatorDensidade() != null && BigDecimal.ZERO.equals(tdc.getNrFatorCubagem())) {
                    // LMS-6531 - ET 04.01.01.08, item 1
                    if (CompareUtils.gt(psReal, BigDecimalUtils.defaultBigDecimal(psAferido))) {
                        psCubagemAferido = BigDecimal.ZERO;
                        psCubado = psReal.multiply(tdc.getNrFatorDensidade());
                    } else {
                        psCubagemAferido = psAferido.multiply(tdc.getNrFatorDensidade());
                        psCubado = BigDecimal.ZERO;
                    }

                    //Atualizar o campo DOCTO_SERVICO.NR_FATOR_DENSIDADE = TABELA_DIVISAO_CLIENTE.NR_FATOR_DENSIDADE
                    //O campo TABELA_DIVISAO_CLIENTE.NR_FATOR_DENSIDADE será gravado na tabela DOCTO_SERVICO.NR_FATOR_DENSIDADE.
                    doctoServico.setNrFatorDensidade(tdc.getNrFatorDensidade());
                    //Nesta situação fazer nBL_PESO_CUBADO_POR_DENSIDADE = ‘S’
                    doctoServico.setBlPesoCubadoPorDensidade(true);
                    //e alterar o conteúdo da variável criada no passo 4 para “N”.
                    naoExisteVolumeSemDimensaoCubagem = false;
                }

            }


        }
        else {
			/*Se DOCTO_SERVICO.PS_REAL for igual a DOCTO_SERVICO.PS_AFORADO ou Cubado for nulo ou zero (cubado ainda não calculado) ou Aferido tenha algum valor (recálculo do cubado necessário)*/
            if (psReal != null && (psReal.equals(psCubado) || (psCubado == null) || (psCubado.doubleValue() == 0.0d) || (psAferido != null))
                    && !calculoFrete.getBlRecalculoFreteSorter()) {
		/*Verifica se o peso cubado possue valor, senão preenche com zero*/
                if (BigDecimalUtils.hasValue(psCubado)) {
					/*Calcula o peso cubado através da vlFator da densidade*/
                    if (calculoFrete.getIdDensidade() != null) {
                        Densidade densidade = getCalculoFreteDAO().get(Densidade.class, calculoFrete.getIdDensidade());
                        if ((densidade.getVlFator() != null) && (!ConstantesExpedicao.TP_DENSIDADE_PADRAO.equals(densidade.getTpDensidade().getValue()))) {
                            psCubado = calculoFrete.getPsRealInformado().multiply(densidade.getVlFator());
                        }
                    }
                }
				/*Calcula o peso cubado através do fator de densidade, caso existir, e caso cliente não utilize necessariamente o peso informado via edi*/
                if (calculoFrete.getIdDivisaoCliente() != null && (doctoServico.getClienteByIdClienteRemetente() != null
                        && (BooleanUtils.isFalse(blUtilizaPesoEDI)))) {
					/* caso houver tabela de divisao*/
                    TabelaDivisaoCliente tdc = tabelaDivisaoClienteService.findTabelaDivisaoCliente(calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico());
					/* e caso houver fator densidade cadastrado*/
                    if (tdc != null && tdc.getNrFatorDensidade() != null) {
                        // LMS-6531 - ET 04.01.01.08, item 1
                        if (CompareUtils.gt(psReal, BigDecimalUtils.defaultBigDecimal(psAferido))) {
                            psCubagemAferido = BigDecimal.ZERO;
                            psCubado = psReal.multiply(tdc.getNrFatorDensidade());
                        } else {
                            psCubagemAferido = psAferido.multiply(tdc.getNrFatorDensidade());
                            psCubado = BigDecimal.ZERO;
                        }

						/*grava entao no documento de servico o fator de densidade utilizados*/
                        doctoServico.setNrFatorDensidade(tdc.getNrFatorDensidade());
                        doctoServico.setBlPesoCubadoPorDensidade(true);

                        // LMS-6531 - ET 04.01.01.08, item 1
                        psCubagemAferido = BigDecimalUtils.defaultBigDecimal(psAferido).multiply(tdc.getNrFatorDensidade());
                    }
                }

            }

            //LMS-2353
            BigDecimal nrFatorCubagemCliente = null;

            if (calculoFrete.getIdDivisaoCliente() != null) {

                TabelaDivisaoCliente tdc = tabelaDivisaoClienteService.findTabelaDivisaoCliente(calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico());
                if (tdc != null) {
                    nrFatorCubagemCliente = tdc.getNrFatorCubagem();
                }

            }

            if (BigDecimalUtils.hasValue(psCubado)) {
                if (BigDecimalUtils.hasValue(nrFatorCubagemCliente)) {

                    nrCubagemCalculo = psCubado.divide(nrFatorCubagemCliente, 4, RoundingMode.HALF_UP);

                } else {
                    BigDecimal pesoMetragemCubicaAereo;
                    if (ConstantesExpedicao.MODAL_AEREO.equals(calculoFrete.getTpModal())) {
                        pesoMetragemCubicaAereo = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_AEREO);
                    } else {
                        pesoMetragemCubicaAereo = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_RODO);
                    }

                    nrCubagemCalculo = psCubado.divide(pesoMetragemCubicaAereo, 4, RoundingMode.HALF_UP);

                }
            }
        }/*else*/

        //2.1 -
        doctoServico.setNrFatorCubagemCliente(null);
        doctoServico.setNrFatorCubagemSegmento(null);

        BigDecimal fatorCubagemPadrao;
        if (ConstantesExpedicao.MODAL_AEREO.equals(calculoFrete.getTpModal())) {
            fatorCubagemPadrao = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_AEREO);
        } else {
            fatorCubagemPadrao = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_RODO);
        }


        //2.2 -
        if (calculoFrete.getBlRecalculoFreteSorter()) {
            doctoServico.setNrCubagemEstatistica(totalCubagemAferidaSorter);

            if (doctoServico.getPsCubadoReal() != null && psReal != null && psAferidoSorter != null
                    && CompareUtils.gt(doctoServico.getPsCubadoReal(), psReal) && CompareUtils.gt(doctoServico.getPsCubadoReal(), psAferidoSorter)) {
                doctoServico.setPsEstatistico(doctoServico.getPsCubadoReal());
            } else if (psReal != null && psAferidoSorter != null && CompareUtils.gt(psReal, psAferidoSorter)) {
                doctoServico.setPsEstatistico(psReal);
            } else {
                doctoServico.setPsEstatistico(psAferidoSorter);
            }
        } else if (naoExisteVolumeSemDimensaoCubagem) {
            doctoServico.setNrCubagemEstatistica(totalCubagemAferida);

            if (psReal != null && psAferido != null && doctoServico.getPsCubadoReal() != null && CompareUtils.gt(doctoServico.getPsCubadoReal(), psReal)
                    && CompareUtils.gt(doctoServico.getPsCubadoReal(), psAferido)) {
                doctoServico.setPsEstatistico(doctoServico.getPsCubadoReal());
            } else if (psReal != null && psAferido != null && CompareUtils.gt(psReal, psAferido)) {
                doctoServico.setPsEstatistico(psReal);
            } else {
                doctoServico.setPsEstatistico(BigDecimalUtils.defaultBigDecimal(psAferido, psReal));
            }
        } else if (BigDecimalUtils.hasValue(doctoServico.getNrCubagemDeclarada())) {
            doctoServico.setNrCubagemEstatistica(doctoServico.getNrCubagemDeclarada());

            BigDecimal fatorCubagemModal;
            if (ConstantesExpedicao.MODAL_AEREO.equals(calculoFrete.getTpModal())) {
                /*Cálcula peso aforado aereo*/
                fatorCubagemModal = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_AEREO);
            } else {
                /*Cálcula peso aforado rodoviario*/
                fatorCubagemModal = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_RODO);
            }

            doctoServico.setPsCubadoReal(doctoServico.getNrCubagemEstatistica().multiply(fatorCubagemModal));

            if (psReal != null && CompareUtils.gt(doctoServico.getPsCubadoReal(), psReal)
                    && CompareUtils.gt(doctoServico.getPsCubadoReal(), BigDecimalUtils.defaultBigDecimal(psAferido, BigDecimal.ZERO))) {
                doctoServico.setPsEstatistico(doctoServico.getPsCubadoReal());
            } else if (psReal != null && CompareUtils.gt(psReal, BigDecimalUtils.defaultBigDecimal(psAferido, BigDecimal.ZERO))) {
                doctoServico.setPsEstatistico(psReal);
            } else {
                doctoServico.setPsEstatistico(BigDecimalUtils.defaultBigDecimal(psAferido, psReal));
            }

        } else {
            FatorCubagemDivisao fatorCubagemDivisao = null;
            if (doctoServico.getDivisaoCliente() != null) {
                fatorCubagemDivisao = fatorCubagemDivisaoService.findFatorCubagemVigenteByIdDivisaoCliente(doctoServico.getDivisaoCliente().getIdDivisaoCliente());
            }
            if (fatorCubagemDivisao != null) {
                doctoServico.setNrFatorCubagemCliente(fatorCubagemDivisao.getNrFatorCubagemReal());

                if (psReal != null && psAferido != null && CompareUtils.gt(psReal, psAferido)) {
                    doctoServico.setNrCubagemEstatistica(psReal.divide(fatorCubagemDivisao.getNrFatorCubagemReal(), INT_VALOR_ESCALA, RoundingMode.HALF_UP));
                    doctoServico.setPsEstatistico(psReal.multiply(fatorCubagemPadrao.divide(fatorCubagemDivisao.getNrFatorCubagemReal(), INT_VALOR_ESCALA, RoundingMode.HALF_UP)));
                } else {
                    doctoServico.setNrCubagemEstatistica(BigDecimalUtils.defaultBigDecimal(psAferido, psReal).divide(fatorCubagemDivisao.getNrFatorCubagemReal(), 10, RoundingMode.HALF_UP).setScale(INT_VALOR_ESCALA, RoundingMode.HALF_UP));
                    doctoServico.setPsEstatistico(BigDecimalUtils.defaultBigDecimal(psAferido, psReal).multiply(fatorCubagemPadrao.divide(fatorCubagemDivisao.getNrFatorCubagemReal(), 10, RoundingMode.HALF_UP)).setScale(INT_VALOR_ESCALA, RoundingMode.HALF_UP));
                }
            } else {
                if (calculoFrete.getClienteBase() != null && calculoFrete.getClienteBase().getSegmentoMercado() != null
                        && calculoFrete.getClienteBase().getSegmentoMercado().getNrFatorCubagemReal() != null) {

                    doctoServico.setNrFatorCubagemSegmento(calculoFrete.getClienteBase().getSegmentoMercado().getNrFatorCubagemReal());

                    if (psReal != null && psAferido != null && CompareUtils.gt(psReal, psAferido)) {
                        doctoServico.setNrCubagemEstatistica(psReal.divide(doctoServico.getNrFatorCubagemSegmento(), INT_VALOR_ESCALA, RoundingMode.HALF_UP));
                        doctoServico.setPsEstatistico(psReal.multiply(fatorCubagemPadrao.divide(doctoServico.getNrFatorCubagemSegmento(), INT_VALOR_ESCALA, RoundingMode.HALF_UP)));
                    } else {
                        doctoServico.setNrCubagemEstatistica(BigDecimalUtils.defaultBigDecimal(psAferido, psReal).divide(doctoServico.getNrFatorCubagemSegmento(), 10, RoundingMode.HALF_UP).setScale(INT_VALOR_ESCALA, RoundingMode.HALF_UP));
                        doctoServico.setPsEstatistico(BigDecimalUtils.defaultBigDecimal(psAferido, psReal).multiply(fatorCubagemPadrao.divide(doctoServico.getNrFatorCubagemSegmento(), 10, RoundingMode.HALF_UP)).setScale(INT_VALOR_ESCALA, RoundingMode.HALF_UP));
                    }

                } else {
                    doctoServico.setNrCubagemEstatistica(null);
                    doctoServico.setPsEstatistico(null);
                }
            }

            if (doctoServico.getNrCubagemEstatistica() != null) {
                doctoServico.setPsCubadoReal(doctoServico.getNrCubagemEstatistica().multiply(fatorCubagemPadrao).setScale(3, RoundingMode.HALF_UP));
            } else {
                doctoServico.setPsCubadoReal(null);
            }
        }

        calculoFrete.setTotalDimensao(totalDimensao == null ? BigDecimal.ZERO : totalDimensao);
        calculoFrete.setNrCubagemCalculo(nrCubagemCalculo == null ? BigDecimal.ZERO : nrCubagemCalculo);
		/*Se o tipo de cliente for F FILIAL ou S ESPECIAL*/
        //4
        if (calculoFrete.getClienteBase() != null && calculoFrete.getClienteBase().getTpCliente() != null &&
                (!calculoFrete.getBlCalculoFreteTabelaCheia()) && ClienteUtils.isParametroClienteEspecial(calculoFrete.getClienteBase().getTpCliente().getValue())) {
            TabelaDivisaoCliente tdc = tabelaDivisaoClienteService.findTabelaDivisaoCliente(calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico());

            //LMS-4777
            if (tdc != null && tdc.getNrFatorCubagem() != null && (BigDecimalUtils.isZero(tdc.getNrFatorCubagem()))
                    && tdc.getNrFatorDensidade() == null) {
                if (BooleanUtils.isTrue(blIndicadorEdi)
                        && BooleanUtils.isTrue(blUtilizaPesoEDI)) {
                    psReferencia = psReal;
                    doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
                } else {
                    // LMS-6531 - ET 04.01.01.08, item 1
                    String tpPesoCalculo = tdc.getTpPesoCalculo().getValue();
                    if (ConstantesExpedicao.TP_PESO_CALCULO_AUTOMATICO.equals(tpPesoCalculo)
                            || ConstantesExpedicao.TP_PESO_CALCULO_AUTOMATICO_CUBADO_DECLARADO.equals(tpPesoCalculo)) {
                        if (psReal != null && psAferido != null && CompareUtils.gt(psReal, psAferido) && !blPesoModulo) {
                            psReferencia = psReal;
                            doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
                        } else if (BigDecimalUtils.hasValue(psAferido)) {
                            psReferencia = psAferido;
                            doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_AFERIDO));
                        } else if (BigDecimalUtils.hasValue(psReal)) {
                            psReferencia = psReal;
                            doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
                        }
                    } else if (ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO.equals(tpPesoCalculo)
                            || ConstantesExpedicao.TP_PESO_CALCULO_CUBADO_DECLARADO.equals(tpPesoCalculo)
                            || ConstantesExpedicao.TP_PESO_CALCULO_AUTOMATICO_DECLARADO.equals(tpPesoCalculo)) {
                        psReferencia = psReal;
                        doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
                    } else if (ConstantesExpedicao.TP_PESO_CALCULO_AFERIDO.equals(tpPesoCalculo)
                            || ConstantesExpedicao.TP_PESO_CALCULO_AUTOMATICO_AFERIDO.equals(tpPesoCalculo)) {
                        if (BigDecimalUtils.hasValue(psAferido)) {
                            psReferencia = psAferido;
                            doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_AFERIDO));
                        } else if (BigDecimalUtils.hasValue(psReal)) {
                            psReferencia = psReal;
                            doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
                        }
                    }
                }
            } else {
                // LMS-6531 - ET 04.01.01.08, item 1
                String tpPesoCalculo = ConstantesExpedicao.TP_PESO_CALCULO_AUTOMATICO;
				BigDecimal psCubadoLimite = psCubado;
				BigDecimal psCubadoAferidoLimite = psCubagemAferido;
				doctoServico.setBlDesconsiderouPesoCubado(Boolean.FALSE);

				if (tdc != null) {
					tpPesoCalculo = isTpPesoCalculoNotNull(tdc, tpPesoCalculo);

					if(calculoFrete.getQtVolumes() != null){
						BigDecimal metragemCubica = validateCalculoMetragemCubica(calculoFrete, psCubagemAferido, psCubado);

						if(validateLimiteCubagem(tdc, metragemCubica, calculoFrete.getQtVolumes())){
							psCubadoLimite = BigDecimal.ZERO;
							psCubadoAferidoLimite = BigDecimal.ZERO;
							doctoServico.setBlDesconsiderouPesoCubado(Boolean.TRUE);
                }
					}
				}

                if (ConstantesExpedicao.TP_PESO_CALCULO_AUTOMATICO.equals(tpPesoCalculo)) {
                    if (calculoFrete.getBlCotacao()) {
                        blPesoModulo = false;
                    }
					psReferencia = getPsCalculo(psCubadoLimite, psReal, psAferido, psCubadoAferidoLimite, blPesoModulo, doctoServico);
                } else if (ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO.equals(tpPesoCalculo)) {
                    psReferencia = psReal;
                    doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
                } else if (ConstantesExpedicao.TP_PESO_CALCULO_CUBADO.equals(tpPesoCalculo)) {
					psReferencia = psCubadoLimite;
                    doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_CUBADO));
                } else if (ConstantesExpedicao.TP_PESO_CALCULO_AFERIDO.equals(tpPesoCalculo)) {
					/* Solicitação abaixo está registrada no Quest 21217 */
                    psReferencia = BigDecimalUtils.defaultBigDecimal(psAferido, psReal);
                    if (BigDecimalUtils.hasValue(psAferido)) {
                        doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_AFERIDO));
                    } else {
                        doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
                    }
                } else if (ConstantesExpedicao.TP_PESO_CALCULO_CUBADO_DECLARADO.equals(tpPesoCalculo)) {
					psReferencia = BigDecimalUtils.defaultBigDecimal(psCubadoLimite, psReal);
					if (BigDecimalUtils.hasValue(psCubadoLimite)) {
                        doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_CUBADO));
                    } else {
                        doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
                    }
                } else if (ConstantesExpedicao.TP_PESO_CALCULO_AUTOMATICO_AFERIDO.equals(tpPesoCalculo)) {
					if (CompareUtils.gt(BigDecimalUtils.defaultBigDecimal(psCubadoAferidoLimite), BigDecimalUtils.defaultBigDecimal(psAferido, psReal))) {
						psReferencia = BigDecimalUtils.defaultBigDecimal(psCubadoAferidoLimite);
                        doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_CUBADO_AFERIDO));
                    } else {
                        psReferencia = BigDecimalUtils.defaultBigDecimal(psAferido, psReal);
                        if (BigDecimalUtils.hasValue(psAferido)) {
                            doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_AFERIDO));
                        } else {
                            doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
                        }
                    }
                } else if (ConstantesExpedicao.TP_PESO_CALCULO_AUTOMATICO_DECLARADO.equals(tpPesoCalculo)) {
					if (CompareUtils.gt(BigDecimalUtils.defaultBigDecimal(psCubadoLimite), BigDecimalUtils.defaultBigDecimal(psReal))) {
						psReferencia = BigDecimalUtils.defaultBigDecimal(psCubadoLimite);
                        doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_CUBADO));
                    } else {
                        psReferencia = BigDecimalUtils.defaultBigDecimal(psReal);
                        doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
                    }
                } else if (ConstantesExpedicao.TP_PESO_CALCULO_AUTOMATICO_CUBADO_DECLARADO.equals(tpPesoCalculo)) {
                    if (calculoFrete.getBlCotacao()) {
                        blPesoModulo = false;
                    }
					if (CompareUtils.gt(BigDecimalUtils.defaultBigDecimal(psCubadoAferidoLimite), BigDecimalUtils.defaultBigDecimal(psCubadoLimite))
							&& CompareUtils.gt(BigDecimalUtils.defaultBigDecimal(psCubadoAferidoLimite), BigDecimalUtils.defaultBigDecimal(getPesoRealByBlPesoModulo(psReal, blPesoModulo)))
							&& CompareUtils.gt(BigDecimalUtils.defaultBigDecimal(psCubadoAferidoLimite), BigDecimalUtils.defaultBigDecimal(psAferido))) {
						psReferencia = BigDecimalUtils.defaultBigDecimal(psCubadoAferidoLimite);
                        doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_CUBADO_AFERIDO));
					} else if (CompareUtils.gt(BigDecimalUtils.defaultBigDecimal(psCubadoLimite), BigDecimalUtils.defaultBigDecimal(getPesoRealByBlPesoModulo(psReal, blPesoModulo)))
							&& CompareUtils.gt(BigDecimalUtils.defaultBigDecimal(psCubadoLimite), BigDecimalUtils.defaultBigDecimal(psAferido))) {
						psReferencia = BigDecimalUtils.defaultBigDecimal(psCubadoLimite);
                        doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_CUBADO));
                    } else if (CompareUtils.gt(BigDecimalUtils.defaultBigDecimal(psReal), BigDecimalUtils.defaultBigDecimal(psAferido))) {
                        psReferencia = BigDecimalUtils.defaultBigDecimal(psReal);
                        doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
                    } else {
                        psReferencia = BigDecimalUtils.defaultBigDecimal(psAferido, psReal);
                        if (BigDecimalUtils.hasValue(psAferido)) {
                            doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_AFERIDO));
                        } else {
                            doctoServico.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
                        }
                    }
                }
            }

            if (doctoServico.getBlIndicadorEdi() != null && doctoServico.getBlIndicadorEdi() && doctoServico.getClienteByIdClienteRemetente() != null
                    && BooleanUtils.isTrue(blUtilizaPesoEDI)) {
                doctoServico.setBlUtilizaPesoEdi(true);
            }

            nrCasasDesimais = calculoFrete.getClienteBase().getNrCasasDecimaisPeso().intValue();
            tpFormaArredondamento = calculoFrete.getClienteBase().getTpFormaArredondamento().getValue();
            //3
        } else {

			/*Obtem o numero de casas decimais e o tipod e arredondamento*/
            nrCasasDesimais = ((BigDecimal) configuracoesFacade.getValorParametro("NrCasasDecimaisPeso")).intValue();
            tpFormaArredondamento = (String) configuracoesFacade.getValorParametro("FormaArredondamentoPeso");

            if (calculoFrete.getBlCotacao()) {
                blPesoModulo = false;
            }
            psReferencia = getPsCalculo(psCubado, psReal, psAferido, psCubagemAferido, blPesoModulo, doctoServico);

        }

        // Caso for Calculo de Frete Tabela Cheia, pego o maior peso
        if (calculoFrete.getBlCalculoFreteTabelaCheia() != null && calculoFrete.getBlCalculoFreteTabelaCheia()) {
            psReferencia = psReferencia.max(psAferido).max(psCubado).max(psReal);
        }

		/*Passa para o documento de serviço o valor  do peso aforado*/
        doctoServico.setPsAforado(psCubado);

        BigDecimal psCalculado = BigDecimalUtils.round(psReferencia, nrCasasDesimais, tpFormaArredondamento);

        if (BigDecimalUtils.isZero(psCalculado)) {
            psCalculado = BigDecimal.ONE;
        }

        calculoFrete.setPsReferencia(psCalculado);

        /** [LMS-414] - Valida o LIMITE MAXIMO DE VOLUMES PARA CALCULAR FRETE */
        getDoctoServicoValidateFacade().verifyLimitePeso(calculoFrete);

        if (naoExisteVolumeSemDimensaoCubagem && doctoServico.getTpPesoCalculo() != null
                && ConstantesExpedicao.TP_PESO_CALCULO_CUBADO.equals(doctoServico.getTpPesoCalculo().getValue())) {
            doctoServico.setBlPesoFatPorCubadoAferido(true);
        }

        //LMS-2353
        doctoServico.setNrCubagemAferida(totalCubagemAferida);
        doctoServico.setNrCubagemCalculo(nrCubagemCalculo);
        doctoServico.setPsCubadoAferido(psCubagemAferido);

    }



    private BigDecimal obterQtdeVolume(CalculoFrete calculoFrete){
        Integer volume  = 0;
        double parametroQtdAparelho = getValorParametro("QT_APARELHO_CAIXA");
        double parametroQtdSimCard = getValorParametro("QT_SIMCARD_CAIXA");
        double parametroQtdCartao = getValorParametro("QT_CARTAO_CAIXA");
        double parametroQtdAcessorio = getValorParametro("QT_ACESSORIO_CAIXA");

        double qtdAparelho = 0, qtdSimCard = 0, qtdCartao = 0, qtdAcessorio = 0;

        for(DadosComplemento dc : calculoFrete.getDoctoServico().getDadosComplementos()) {
            InformacaoDoctoCliente idc = informacaoDoctoClienteService.findByIdInitLazyProperties(dc.getInformacaoDoctoCliente().getIdInformacaoDoctoCliente(), false);
            switch(idc.getDsCampo()){
                case "QTDAPARELHO":
                    qtdAparelho += formataDsValorCampo(dc.getDsValorCampo());
                    break;
                case "QTDSIMCARD":
                    qtdSimCard += formataDsValorCampo(dc.getDsValorCampo());
                    break;
                case "QTDCARTAO":
                    qtdCartao += formataDsValorCampo(dc.getDsValorCampo());
                    break;
                case "QTDACESSORIO":
                    qtdAcessorio += formataDsValorCampo(dc.getDsValorCampo());
                    break;
                default:
                    continue;
            }
        }

        if(qtdAparelho > 0 && parametroQtdAparelho > 0){
            volume +=  new BigDecimal(qtdAparelho / parametroQtdAparelho).setScale(0,BigDecimal.ROUND_UP ).round(MathContext.DECIMAL32).intValue();
        }
        if(qtdSimCard > 0 && parametroQtdSimCard > 0){
            volume +=  new BigDecimal(qtdSimCard / parametroQtdSimCard).setScale(0,BigDecimal.ROUND_UP ).round(MathContext.DECIMAL32).intValue();
        }
        if(qtdCartao > 0 && parametroQtdCartao > 0){
            volume +=  new BigDecimal(qtdCartao / parametroQtdCartao).setScale(0,BigDecimal.ROUND_UP ).round(MathContext.DECIMAL32).intValue();
        }
        if(qtdAcessorio > 0 && parametroQtdAcessorio > 0){
            volume +=  new BigDecimal(qtdAcessorio / parametroQtdAcessorio).setScale(0,BigDecimal.ROUND_UP ).round(MathContext.DECIMAL32).intValue();
        }
        return new BigDecimal(volume);
    }

    private double getValorParametro(String dsParametro){
        ParametroGeral parametro = parametroGeralService.findByNomeParametro(dsParametro);
        return parametro == null ? 0 : Double.parseDouble(parametro.getDsConteudo());
    }

    private double formataDsValorCampo(String dsValorCampo){
        Double valor =  Double.parseDouble(dsValorCampo);
        return Double.isNaN(valor) || Double.isInfinite(valor) || valor == 0 ? 0 : valor;

    }

	private String isTpPesoCalculoNotNull(TabelaDivisaoCliente tdc,
			String tpPesoCalculo) {
		if(tdc.getTpPesoCalculo() != null){
			tpPesoCalculo = tdc.getTpPesoCalculo().getValue();
		}
		return tpPesoCalculo;
	}

	private BigDecimal validateCalculoMetragemCubica(CalculoFrete calculoFrete, BigDecimal psCubagemAferido, BigDecimal psCubado){
		BigDecimal metragemCubica = BigDecimal.ZERO;
		if(BigDecimalUtils.hasValue(calculoFrete.getTotalDimensao())){
			metragemCubica = calculoFrete.getTotalDimensao();
		}else{
			if(BigDecimalUtils.hasValue(psCubagemAferido)){
				metragemCubica = psCubagemAferido.divide(validateNrFatorCubagem(calculoFrete), 4, RoundingMode.HALF_UP);
			}else{
				metragemCubica = psCubado.divide(validateNrFatorCubagem(calculoFrete), 4, RoundingMode.HALF_UP);
			}
		}
		return metragemCubica;
	}

	private BigDecimal validateNrFatorCubagem(CalculoFrete calculoFrete){

		if(BigDecimalUtils.hasValue(calculoFrete.getDoctoServico().getNrFatorCubagem())){
			return calculoFrete.getDoctoServico().getNrFatorCubagem();
		}else if(ConstantesExpedicao.MODAL_AEREO.equals(calculoFrete.getTpModal())){
			return (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_AEREO);
        }else{
        	return (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_RODO);
        }
	}

	private boolean validateLimiteCubagem(TabelaDivisaoCliente tdc, BigDecimal metragemCubica, Integer qtVolumes){
		if(tdc.getNrLimiteMetragemCubica() != null  && tdc.getNrLimiteQuantVolume()!= null){
			if(metragemCubica.compareTo(tdc.getNrLimiteMetragemCubica()) <= 0 &&
					qtVolumes.compareTo(tdc.getNrLimiteQuantVolume().intValue()) <= 0){
				return true;
			}
		}else if(tdc.getNrLimiteMetragemCubica() != null && metragemCubica.compareTo(tdc.getNrLimiteMetragemCubica()) <= 0){
			return true;
		}else if(tdc.getNrLimiteQuantVolume()!= null && qtVolumes.compareTo(tdc.getNrLimiteQuantVolume().intValue()) <= 0){
			return true;
		}
		return false;
	}

    private BigDecimal getPesoRealByBlPesoModulo(BigDecimal psReal, boolean blPesoModulo) {
        return (!blPesoModulo ? psReal : BigDecimal.ZERO);
    }

    private BigDecimal calculaPesoCubadoReal(CalculoFrete calculoFrete, BigDecimal vlTotalVolume) {
        BigDecimal psCubicoModal = new BigDecimal(1);
        if (ConstantesExpedicao.MODAL_AEREO.equals(calculoFrete.getTpModal())) {
            psCubicoModal = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_PESO_METRAGEM_CUBICA_AEREO);
        } else {
            psCubicoModal = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_PESO_METRAGEM_CUBICA_RODOVIARIO);
        }

        return vlTotalVolume.divide(psCubicoModal, 3, RoundingMode.HALF_UP);
    }

    /**
     * Obtem o peso referencia para calculo do frete através
     * dos pesos real, aferido, cubado
     * <p>
     * Se   psCubagemAferido > psCubado e psCubagemAferido > psReal e psCubagemAferido > psAferido = psCubagemAferido
     * Se 	 psCubado > psReal e psCubado > psAferido = psCubado
     * Se   psReal > psAferido e (blPesoModulo == false) = psReal
     * Se   psAferido nulo = psReal Senao psAferido
     * <p>
     * CQPRO00027603
     *
     * @param psCubado
     * @param psReal
     * @param psAferido
     * @param psCubagemAferido
     * @param blPesoModulo
     * @return
     */
    private BigDecimal getPsCalculo(BigDecimal psCubado, BigDecimal psReal, BigDecimal psAferido,
                                    BigDecimal psCubagemAferido, Boolean blPesoModulo, Conhecimento conhecimento) {

        BigDecimal psCalculo = BigDecimal.ZERO;

        if (psCubado == null) {
            psCubado = BigDecimal.ZERO;
        }

        if (psReal == null) {
            psReal = BigDecimal.ZERO;
        }

        if (psAferido == null) {
            psAferido = BigDecimal.ZERO;
        }

        // LMS-6531 - ET 04.01.01.08, item 1
        if (BigDecimalUtils.hasValue(psCubagemAferido) && CompareUtils.gt(psCubagemAferido, psCubado)
                && CompareUtils.gt(psCubagemAferido, psReal) && CompareUtils.gt(psCubagemAferido, psAferido)) {
            psCalculo = psCubagemAferido;
            conhecimento.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_CUBADO_AFERIDO));
        } else if (CompareUtils.gt(psCubado, getPesoRealByBlPesoModulo(psReal, blPesoModulo)) && CompareUtils.gt(psCubado, psAferido)) {
            psCalculo = psCubado;
            conhecimento.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_CUBADO));
        } else if (CompareUtils.gt(psReal, psAferido) && !blPesoModulo) {
            psCalculo = psReal;
            conhecimento.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
        } else if (BigDecimalUtils.hasValue(psAferido)) {
            psCalculo = psAferido;
            conhecimento.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_AFERIDO));
        } else {
            psCalculo = psReal;
            conhecimento.setTpPesoCalculo(new DomainValue(ConstantesExpedicao.TP_PESO_CALCULO_DECLARADO));
        }
        return psCalculo;
    }

    /**
     * Preenche o objeto RestricaoRota
     *
     * @param restricaoRota
     * @param municipio
     * @param idTipoLocalizacao
     * @param idTipoLocalizacaoComercial
     */
    private void findRestricaoRota(RestricaoRota restricaoRota, Municipio municipio, Long idTipoLocalizacao, Long idTipoLocalizacaoComercial) {
        restricaoRota.setIdZona(municipio.getUnidadeFederativa().getPais().getZona().getIdZona());
        restricaoRota.setIdPais(municipio.getUnidadeFederativa().getPais().getIdPais());
        restricaoRota.setIdUnidadeFederativa(municipio.getUnidadeFederativa().getIdUnidadeFederativa());
        restricaoRota.setIdMunicipio(municipio.getIdMunicipio());
        restricaoRota.setIdTipoLocalizacao(idTipoLocalizacao);
        restricaoRota.setIdTipoLocalizacaoTarifa(idTipoLocalizacao);
        restricaoRota.setIdTipoLocalizacaoComercial(idTipoLocalizacaoComercial);
    }

    /**
     * Obtem o tipo de Localização municipio
     *
     * @param idMunicipio
     * @param idServico
     * @param nrCep
     * @return
     */
    private Long findIdTipoLocalizacao(Long idMunicipio, Long idServico, String nrCep) {
        Map<String, Object> atendimento = ppeService.findAtendimentoMunicipio(idMunicipio, idServico, Boolean.TRUE, null, nrCep, null, null, null, null, null, "N", null, null);
        return (Long) atendimento.get("idTipoLocalizacaoMunicipio");
    }

    /**
     * Obtem o tipo Localização municipio comercial
     *
     * @param idMunicipio
     * @param idServico
     * @param nrCep
     * @return
     */
    private Long findIdTipoLocalizacaoComercial(Long idMunicipio, Long idServico, String nrCep) {
        Map<String, Object> atendimento = ppeService.findAtendimentoMunicipio(idMunicipio, idServico, Boolean.FALSE,
                null, nrCep, null, null, null, null, null, "N", null, null);
        return (Long) atendimento.get("idTipoLocalizacaoComercial");
    }


    private void findCalculoFrete(CalculoFrete calculoFrete) {
        Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(calculoFrete.getIdDoctoServico(), false);
        calculoFrete.setNrCepColeta(conhecimento.getNrCepColeta());
        calculoFrete.setNrCepEntrega(conhecimento.getNrCepEntrega());
        calculoFrete.setPsCubadoInformado(conhecimento.getPsAforado());
        calculoFrete.setPsRealInformado(conhecimento.getPsReal());

        calculoFrete.setIdServico(conhecimento.getServico().getIdServico());
        if (conhecimento.getDivisaoCliente() != null) {
            calculoFrete.setIdDivisaoCliente(conhecimento.getDivisaoCliente().getIdDivisaoCliente());
        }
        if (conhecimento.getTarifaPreco() != null) {
            calculoFrete.setIdTarifa(conhecimento.getTarifaPreco().getIdTarifaPreco());
        }

        if (conhecimento.getTabelaPreco() != null) {
            calculoFrete.setTabelaPreco(conhecimento.getTabelaPreco());
        }

        calculoFrete.setVlTotalParcelas(conhecimento.getVlTotalParcelas());
        calculoFrete.setVlTotalServicosAdicionais(conhecimento.getVlTotalServicos());
        calculoFrete.setVlTotal(conhecimento.getVlTotalDocServico());

        DevedorDocServ devedorDocServ = (DevedorDocServ) conhecimento.getDevedorDocServs().get(0);
        calculoFrete.setVlDevido(devedorDocServ.getVlDevido());

        ImpostoServico impostoServico = new ImpostoServico();
        impostoServico.setVlImposto(conhecimento.getVlImposto());
        impostoServico.setVlBaseCalculo(conhecimento.getVlBaseCalcImposto());
        calculoFrete.setTributo(impostoServico);

    }


    private List<ParcelaServico> findParcelasFreteTransferencia(CalculoFrete calculoFrete) {
        List<ParcelaDoctoServico> parcelasDoctoServico = getCalculoParcelaFreteService().findParcelasDoctoServico(calculoFrete.getIdDoctoServico());
        ParcelaServico freteParcela = null;
        List<ParcelaServico> lista = new ArrayList<ParcelaServico>();
        for (ParcelaDoctoServico parcelaDoctoServico : parcelasDoctoServico) {
            freteParcela = new ParcelaServico(parcelaDoctoServico.getParcelaPreco());
            freteParcela.setVlParcela(parcelaDoctoServico.getVlParcela());
            lista.add(freteParcela);
        }

        return lista;
    }


    private void findParcelasFrete(CalculoFrete calculoFrete) {
        List<ParcelaDoctoServico> parcelasDoctoServico = getCalculoParcelaFreteService().findParcelasDoctoServico(calculoFrete.getIdDoctoServico());
        ParcelaServico freteParcela = null;

        for (ParcelaDoctoServico parcelaDoctoServico : parcelasDoctoServico) {
            freteParcela = new ParcelaServico(parcelaDoctoServico.getParcelaPreco());
            freteParcela.setVlParcela(parcelaDoctoServico.getVlParcela());
            calculoFrete.addParcelaGeral(freteParcela);
        }
    }

    public ParametroCliente findParametroCliente(Long idDivisaoCliente, Long idServico, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
        return getCalculoFreteDAO().findParametroCliente(idDivisaoCliente, idServico, restricaoRotaOrigem, restricaoRotaDestino);
    }

    protected void findParametroCliente(CalculoServico calculoServico) {

        CalculoFrete calculoFrete = (CalculoFrete) calculoServico;

        ParametroCliente parametroCliente = calculoFrete.getParametroCliente();

        if (!BooleanUtils.isTrue(calculoServico.getRecalculoFrete())) {

            if ((ConstantesExpedicao.CALCULO_COTACAO.equals(calculoFrete.getTpCalculo())) && (calculoFrete.getIdCotacao() != null)) {
                parametroCliente = getCalculoFreteDAO().findParametroClienteByCotacao(calculoFrete.getIdCotacao());
            } else {

				/*Verifica se possue idDivisaoCliente e o cliente é especial*/
                if (calculoFrete.getIdDivisaoCliente() != null
                        && BooleanUtils.isFalse(calculoFrete.getBlCalculoFreteTabelaCheia())
                        && ClienteUtils.isParametroClienteEspecial(calculoFrete.getClienteBase().getTpCliente().getValue())) {
					/* Correção para obter o parametro do cliente correto através da Rota.
					 * ZONA - PAIS - UNIDADE FEDERATIVA - GRUPO REGIÃO - FILIAL - MUNICIPIO
					 * Referente ao JIRA LMS-416*/
                    parametroCliente = getRestricaoRotaService().findParametroClienteRota(calculoFrete);
                }
            }
        }

		/*Seta os parametros do cliente no calculo de frete*/
        calculoFrete.setParametroCliente(parametroCliente);

        if (!calculoFrete.getBlRecalculoCotacao() &&
                !ConstantesExpedicao.CALCULO_COTACAO.equals(calculoFrete.getTpCalculo()) && (calculoFrete.getIdCotacao() == null)) {
            TabelaPreco tabelaPreco = calculoServico.getTabelaPreco();
            if (tabelaPreco != null && !Hibernate.isInitialized(tabelaPreco)) {
                tabelaPreco = tabelaDivisaoClienteService.getTabelaPrecoService().findByIdTabelaPreco(tabelaPreco.getIdTabelaPreco());
                calculoServico.setTabelaPreco(tabelaPreco);
            }
            if (calculoFrete.getParametroCliente() == null && tabelaPreco != null
                    && !"P".equals(tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco())
                    && !"F".equals(tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco())) {

                if ("M".equals(tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())
                        || "T".equals(tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())) {
                    tabelaPreco = findTabelaPrecoMercurio(calculoFrete);
                    if (tabelaPreco == null && ConstantesExpedicao.TP_FRETE_FOB.equals(calculoFrete.getTpFrete())) {
                        calculoFrete.setTpFrete(ConstantesExpedicao.TP_FRETE_CIF);
                        tabelaPreco = findTabelaPrecoMercurio(calculoFrete);
                        calculoFrete.setTpFrete(ConstantesExpedicao.TP_FRETE_FOB);
                    }
                    calculoServico.setTabelaPreco(tabelaPreco);
                    findGrupoRegioes(calculoFrete);
                }
            }
        }

		/*Seta a tarifa preco no cálculo de frete*/
        setTarifaPreco(calculoFrete);
    }

    /**
     * seta a TarifaPreco no cálculo de Frete
     *
     * @param calculoFrete
     */
    private void setTarifaPreco(CalculoFrete calculoFrete) {

        String tpModal = calculoFrete.getTpModal();

		/*Obtem as rotas origem e destino*/
        RestricaoRota restricaoRotaOrigem = calculoFrete.getRestricaoRotaOrigem();
        RestricaoRota restricaoRotaDestino = calculoFrete.getRestricaoRotaDestino();

		/*Obtem os id origem e destino e filial de destino*/
        Long idMunicipioOrigem = restricaoRotaOrigem.getIdMunicipio();
        Long idMunicipioDestino = restricaoRotaDestino.getIdMunicipio();
        Long idFilialDestino = restricaoRotaDestino.getIdFilial();

		/*Obtem o municipio filial de destino*/
        MunicipioFilial municipioFilialDestino = calculoFrete.getMunicipioFilialDestino();
        if (municipioFilialDestino == null) {
            municipioFilialDestino = municipioFilialService.findMunicipioFilial(idMunicipioDestino, idFilialDestino);
            calculoFrete.setMunicipioFilialDestino(municipioFilialDestino);
        }

        if (municipioFilialDestino == null || Boolean.FALSE.equals(municipioFilialDestino.getBlPadraoMcd())) {

            Cliente clienteConsignatario = calculoFrete.getDoctoServico().getClienteByIdClienteConsignatario();
            if (ConstantesExpedicao.MODAL_RODOVIARIO.equals(tpModal)
                    || (ConstantesExpedicao.MODAL_AEREO.equals(tpModal) && (clienteConsignatario == null))) {

                ParametroCliente parametroCliente = calculoFrete.getParametroCliente();

				/*Seta  o cliente redespacho se existir  cliente de redespacho*/
                if (parametroCliente != null && parametroCliente.getClienteByIdClienteRedespacho() != null) {
                    clienteConsignatario = parametroCliente.getClienteByIdClienteRedespacho();
                    setClienteConsignatario(calculoFrete, clienteConsignatario);

                    idMunicipioDestino = clienteConsignatario.getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio();
                }
            }

			/*Se o consignatario  for nulo informa as mensagens LMS-29118 ou LMS-04205*/
            if (clienteConsignatario == null) {
                if (ConstantesExpedicao.MODAL_RODOVIARIO.equals(tpModal)) {
                    throw new BusinessException("LMS-29118");
                } else if (ConstantesExpedicao.MODAL_AEREO.equals(tpModal)) {
                    throw new BusinessException("LMS-04205");
                }
            }
        }

		/*Quando não for recálculo de frete busca a tarifa preco*/
        if (BooleanUtils.isTrue(calculoFrete.getRecalculoFrete()) && calculoFrete.getTarifaPreco() != null) {
            calculoFrete.setIdTarifa(calculoFrete.getTarifaPreco().getIdTarifaPreco());
        } else {
            if (ConstantesExpedicao.MODAL_RODOVIARIO.equals(tpModal)) {
                Long idTarifaPreco = mcdService.findTarifaMunicipios(idMunicipioOrigem, idMunicipioDestino, calculoFrete.getIdServico());
                calculoFrete.setIdTarifa(idTarifaPreco);
            }
        }
    }

    /**
     * Adiciona o consignatario ao conhecimento e dados complementares
     *
     * @param calculoFrete
     * @param clienteConsignatario
     */
    private void setClienteConsignatario(CalculoFrete calculoFrete, Cliente clienteConsignatario) {

        if (clienteConsignatario != null) {
            calculoFrete.getDoctoServico().setClienteByIdClienteConsignatario(clienteConsignatario);
        }

        Long idConsignatario = calculoFrete.getDoctoServico().getClienteByIdClienteConsignatario().getIdCliente();

        InscricaoEstadual inscricaoEstadual = inscricaoEstadualService
                .findByPessoaIndicadorPadrao(idConsignatario, Boolean.TRUE);

        InformacaoDocServico informacaoDocServico = informacaoDocServicoService
                .findInformacaoDoctoServico(ConstantesExpedicao.IE_CONSIGNATARIO,
                        ConstantesExpedicao.IE_CONSIGNATARIO);

		/*Complemento do documento*/
        DadosComplemento dadosComplemento = new DadosComplemento();
        dadosComplemento.setInformacaoDocServico(informacaoDocServico);
        dadosComplemento.setDsValorCampo(inscricaoEstadual.getNrInscricaoEstadual());
        calculoFrete.getDoctoServico().addDadoComplemento(dadosComplemento);

		/*Adiciona informações nos parametros do cálculo para cálcuo do ICMS*/
        List<TipoTributacaoIE> list = tipoTributacaoIEService.findVigentesByIdPessoa(idConsignatario);

        if (list != null && !list.isEmpty()) {
            TipoTributacaoIE tipo = list.get(0);
            calculoFrete.getDadosCliente().setTpSituacaoTributariaRecebedor(tipo.getTpSituacaoTributaria().getValue());
        }

        calculoFrete.getDadosCliente().setIdClienteRecebedor(idConsignatario);
        calculoFrete.getDadosCliente().setIdInscricaoEstadualRecebedor(inscricaoEstadual.getIdInscricaoEstadual());
    }

    /**
     * Retorna o id da Tabela de Preco a ser utilizada para o cálculo do frete
     *
     * @param idServico
     * @param cliente
     * @param idDivisaoCliente
     */
    protected void findTabelaPreco(CalculoServico calculoServico) throws BusinessException {
		/*Verifica os dados do cliente base*/

        CalculoFrete calculoFrete = (CalculoFrete) calculoServico;
        TabelaPreco tabelaPreco = null;

        findClienteBase(calculoFrete);

        if (BooleanUtils.isTrue(calculoServico.getRecalculoFrete())) {

            tabelaPreco = calculoServico.getTabelaPrecoRecalculo();

        } else if (ConstantesExpedicao.CALCULO_COTACAO.equals(calculoFrete.getTpCalculo()) && (calculoFrete.getIdCotacao() != null)) {

            Cotacao cotacao = getCalculoFreteDAO().findCotacao(calculoFrete.getIdCotacao());
            tabelaPreco = cotacao.getTabelaPreco();

        } else {

			/*Se o tipo de cliente é especial*/
            if ((!calculoFrete.getBlCalculoFreteTabelaCheia()) && ClienteUtils.isParametroClienteEspecial(calculoFrete.getClienteBase().getTpCliente().getValue())) {

				/*Verifica se possue divisão cliente e busca a tabela de preço*/
                if (calculoFrete.getIdDivisaoCliente() != null) {

                    tabelaPreco = getCalculoFreteDAO().findTabelaPrecoTipoSubtipo(calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico(), calculoFrete.getTpFrete());

					/*Caso nao encontrar a tabela para FRETE FOB, faz a pesquisa pela CIF*/
                    if (tabelaPreco == null && ConstantesExpedicao.TP_FRETE_FOB.equals(calculoFrete.getTpFrete())) {
                        tabelaPreco = getCalculoFreteDAO().findTabelaPrecoTipoSubtipo(calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico(), ConstantesExpedicao.TP_FRETE_CIF);
                    }
                }

				/*Se encontrou alguma tabela preço verifica se existe parametros sufuciente.
				Verifica se os subtipos da tabela preço não é P e F*/
                if (tabelaPreco != null) {

					/*Se o tipo de frete for FOB obtem a TabelaDivisaoCliente setando
					a TabelaPrecoFob como a TabelaPreco*/
                    if (ConstantesExpedicao.TP_FRETE_FOB.equals(calculoFrete.getTpFrete())) {

                        TabelaDivisaoCliente tdc = tabelaDivisaoClienteService
                                .findTabelaDivisaoCliente(calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico());

                        if (tdc != null && tdc.getTabelaPrecoFob() != null) {
                            tabelaPreco = tdc.getTabelaPrecoFob();
                        }
                    }
                } else {

                    if ("A".equals(calculoServico.getTpModal())) {

                        tabelaPreco = getCalculoFreteDAO().findTabelaPreco(calculoFrete.getIdServico(), "A", "X");

                    } else {

                        tabelaPreco = findTabelaPrecoMercurio(calculoFrete);

                    }
                }

			/*Cliente não especial*/
            } else {
                tabelaPreco = findTabelaPrecoMercurio(calculoFrete);
                if (tabelaPreco == null && ConstantesExpedicao.TP_FRETE_FOB.equals(calculoFrete.getTpFrete())) {
                    calculoFrete.setTpFrete(ConstantesExpedicao.TP_FRETE_CIF);
                    tabelaPreco = findTabelaPrecoMercurio(calculoFrete);
                    calculoFrete.setTpFrete(ConstantesExpedicao.TP_FRETE_FOB);
                }


            }
        }/*else*/

		/*Se não encontrar um tabela de preço informa a mensagem
		LMS-30023 'Não há nenhuma tabela vigente para o serviço solicitado.' */
        if (tabelaPreco == null) {
            throw new BusinessException("LMS-30023");
        }
        calculoFrete.setTabelaPreco(tabelaPreco);
        findGrupoRegioes(calculoFrete);
    }

    /**
     * Verifica se o municipio de origem e estino
     * possuem grupo região
     *
     * @param calculoFrete
     */
    public void findGrupoRegioes(CalculoFrete calculoFrete) {

        Long idMunicipioOrigem = calculoFrete.getRestricaoRotaOrigem().getIdMunicipio();
        Long idMunicipioDestino = calculoFrete.getRestricaoRotaDestino().getIdMunicipio();

        GrupoRegiao grupoRegiaoOrigem = getCalculoParcelaFreteService().findGrupoRegiao(calculoFrete.getTabelaPreco().getIdTabelaPreco(), idMunicipioOrigem);

        GrupoRegiao grupoRegiaoDestino = getCalculoParcelaFreteService().findGrupoRegiao(calculoFrete.getTabelaPreco().getIdTabelaPreco(), idMunicipioDestino);

        calculoFrete.setGrupoRegiaoOrigem(grupoRegiaoOrigem);
        calculoFrete.setGrupoRegiaoDestino(grupoRegiaoDestino);

        if (grupoRegiaoOrigem != null) {
            calculoFrete.getRestricaoRotaOrigem().setIdGrupoRegiao(grupoRegiaoOrigem.getIdGrupoRegiao());
        }

        if (grupoRegiaoDestino != null) {
            calculoFrete.getRestricaoRotaDestino().setIdGrupoRegiao(grupoRegiaoDestino.getIdGrupoRegiao());
        }
    }

    public Map<String, Object> findAeroportoByFilial(Long idFilial) {
        return filialService.findAeroportoFilial(idFilial);
    }

    public MunicipioFilial findMunicipioFilial(Long idMunicipio, Long idFilial) {
        return getCalculoFreteDAO().findMunicipioFilial(idMunicipio, idFilial);
    }

    private Boolean findBlColetaEntregaInterior(RestricaoRota restricaoRota) {
        Boolean blInterior = Boolean.FALSE;
        Aeroporto aeroporto = getCalculoFreteDAO().get(Aeroporto.class, restricaoRota.getIdAeroporto());
        EnderecoPessoa enderecoPessoaFilial = enderecoPessoaService.findByIdPessoa(aeroporto.getFilial().getPessoa().getIdPessoa());
        if (enderecoPessoaFilial != null) {
            blInterior = Boolean.valueOf(CompareUtils.ne(restricaoRota.getIdMunicipio(), enderecoPessoaFilial.getMunicipio().getIdMunicipio()));

            if (blInterior.equals(Boolean.TRUE)) {
                String idTipoLocalizacaoCapital = (String) configuracoesFacade.getValorParametro("IDTipoLocalizacaoCapital");
                String[] result = idTipoLocalizacaoCapital.split(";");
                for (int x = 0; x < result.length; x++) {
                    if (LongUtils.getLong(result[x]).equals(restricaoRota.getIdTipoLocalizacao())) {
                        blInterior = Boolean.FALSE;
                        break;
                    }
                }
            }
        }
        return blInterior;
    }

    public TabelaPreco findTabelaPreco(String tpSubtipoTabelaPreco, Long idServico, String tpTipoTabelaPreco) {
		return getCalculoServicoDAO().findTabelaPreco(idServico, tpTipoTabelaPreco, tpSubtipoTabelaPreco);
	}

    private void executeCalculoFreteRodoviarioNacionalNormalNormal(CalculoFrete calculoFrete) {
        // Altera o municipio destino
        setMunicipioDestinoCalculo(calculoFrete);

        //Define tabela
        findTabelaPreco(calculoFrete);

        //Define parametros iniciais
        setParametrosCalculo(calculoFrete);

        //Define peso de referência
        findPesoReferencia(calculoFrete);

        //Parcelas
        if (Boolean.TRUE.equals(calculoFrete.getBlCalculaParcelas())) {
            ParcelaServico freteParcela;

            //Frete Peso
            freteParcela = getCalculoParcelaFreteService().findParcelaFretePeso(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);


            //Frete Valor
            freteParcela = getCalculoParcelaFreteService().findParcelaFreteValor(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //GRIS
            freteParcela = getCalculoParcelaFreteService().findParcelaGRIS(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Pedagio
            freteParcela = getCalculoParcelaFreteService().findParcelaPedagio(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Generalidades
            findGeneralidades(calculoFrete);

            //Parametros parcelas gerais
            getCalculoParcelaFreteService().findParametroParcelasFrete(calculoFrete);

            //TRT
            freteParcela = getCalculoParcelaFreteService().findParcelaTRT(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            if(Boolean.TRUE.equals(validaCobrancaCalculoTde(calculoFrete))) {
                //Taxa Dificuldade de Entrega
                freteParcela = getCalculoParcelaFreteService().findParcelaTDE(calculoFrete);
                calculoFrete.addParcelaGeral(freteParcela);
            }

            //Taxa de veiculo dedicado
            freteParcela = getCalculoParcelaFreteService().findParcelaTaxaVeiculoDedicado(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Taxa de agendamento coleta/entrega
            freteParcela = getCalculoParcelaFreteService().findParcelaTaxaAgendamentoColetaEntrega(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Taxa de paletização
            freteParcela = getCalculoParcelaFreteService().findParcelaTaxaPaletizacao(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Taxa de custo de descarga
            freteParcela = getCalculoParcelaFreteService().findParcelaTaxaCustoDescarga(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Agendamento Entrega CTE
            freteParcela = getCalculoParcelaFreteService().findParcelaAgendamentoEntregaCTE(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Paletização CTE
            freteParcela = getCalculoParcelaFreteService().findParcelaPaletizacaoCTE(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Taxa EMEX
            freteParcela = getCalculoParcelaFreteService().findParcelaEMEX(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);
        }

        //Servicos adicionais
        if (Boolean.TRUE.equals(calculoFrete.getBlCalculaServicosAdicionais())) {
            findServicosAdicionais(calculoFrete);
        }

    }


    private Boolean validaCobrancaCalculoTde(CalculoFrete calculoFrete) {
        try {
            Cliente clienteRemetente = this.clienteService.findById(calculoFrete.getDadosCliente().getIdClienteRemetente());
            Cliente clienteDestinatario = this.clienteService.findById(calculoFrete.getDadosCliente().getIdClienteDestinatario());

            if (Boolean.TRUE.equals(clienteRemetente.getBlValidaCobrancDifTdeDest()) && (
                    Boolean.TRUE.equals(clienteDestinatario.getBlCobrancaTdeDiferenciada())
                            && Boolean.TRUE.equals(clienteDestinatario.getBlDificuldadeEntrega()))) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    private void executeRecalculoCotacao(CalculoFrete calculoFrete) {

        calculoFrete.resetValores();

        findTabelaPreco(calculoFrete);

        setParametrosCalculo(calculoFrete);

        //Define tabela

        //Define peso de referência
        findPesoReferencia(calculoFrete);

        //LMS-1174 Mercur Parcela de despacho acima de um peso determinado - no recalculo não alterava as parcelas existentes.


        if (Boolean.TRUE.equals(calculoFrete.getBlCalculaParcelas())) {
            calculoFrete.resetParcelasRecalculoCotacao();

            ParcelaServico freteParcela;
            //Frete Peso
            freteParcela = getCalculoParcelaFreteService().findParcelaFretePeso(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Frete Valor
            freteParcela = getCalculoParcelaFreteService().findParcelaFreteValor(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //GRIS
            freteParcela = getCalculoParcelaFreteService().findParcelaGRIS(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Generalidades
            findGeneralidades(calculoFrete);

            //Parametros parcelas gerais
            getCalculoParcelaFreteService().findParametroParcelasFrete(calculoFrete);

            //Calculo de Taxas
            if (calculoFrete.getTaxas() != null) {
                for (ParcelaServico taxa : calculoFrete.getTaxas()) {
                    freteParcela = findParametroTaxaRecalculo(calculoFrete, taxa);
                    calculoFrete.addParcelaGeral(freteParcela);
                }
            }

            //TRT
            freteParcela = getCalculoParcelaFreteService().findParcelaTRT(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            if (ConstantesExpedicao.MODAL_AEREO.equals(calculoFrete.getTpModal())) {
                Boolean blColetaInterior = Boolean.FALSE;
                Boolean blEntregaInterior = Boolean.FALSE;
                if (calculoFrete.getRestricaoRotaOrigem() != null && calculoFrete.getRestricaoRotaOrigem().getIdAeroporto() != null) {
                    blColetaInterior = findBlColetaEntregaInterior(calculoFrete.getRestricaoRotaOrigem());
                }
                if (calculoFrete.getRestricaoRotaDestino() != null && calculoFrete.getRestricaoRotaDestino().getIdAeroporto() != null) {
                    blEntregaInterior = findBlColetaEntregaInterior(calculoFrete.getRestricaoRotaDestino());
                }

                //Pedágio
                freteParcela = getCalculoParcelaFreteService().findParcelaPedagio(calculoFrete, blColetaInterior, blEntregaInterior);
                calculoFrete.addParcelaGeral(freteParcela);
            } else {
                //Pedagio
                freteParcela = getCalculoParcelaFreteService().findParcelaPedagio(calculoFrete);
                calculoFrete.addParcelaGeral(freteParcela);

                //Taxa Dificuldade de Entrega
                freteParcela = getCalculoParcelaFreteService().findParcelaTDE(calculoFrete);
                calculoFrete.addParcelaGeral(freteParcela);

                //Taxa de veiculo dedicado
                freteParcela = getCalculoParcelaFreteService().findParcelaTaxaVeiculoDedicado(calculoFrete);
                calculoFrete.addParcelaGeral(freteParcela);

                //Taxa de agendamento coleta/entrega
                freteParcela = getCalculoParcelaFreteService().findParcelaTaxaAgendamentoColetaEntrega(calculoFrete);
                calculoFrete.addParcelaGeral(freteParcela);

                //Taxa de paletização
                freteParcela = getCalculoParcelaFreteService().findParcelaTaxaPaletizacao(calculoFrete);
                calculoFrete.addParcelaGeral(freteParcela);

                //Taxa de custo de descarga
                freteParcela = getCalculoParcelaFreteService().findParcelaTaxaCustoDescarga(calculoFrete);
                calculoFrete.addParcelaGeral(freteParcela);

                //Agendamento Entrega CTE
                freteParcela = getCalculoParcelaFreteService().findParcelaAgendamentoEntregaCTE(calculoFrete);
                calculoFrete.addParcelaGeral(freteParcela);

                //Paletização CTE
                freteParcela = getCalculoParcelaFreteService().findParcelaPaletizacaoCTE(calculoFrete);
                calculoFrete.addParcelaGeral(freteParcela);

              //EMEX
                freteParcela = getCalculoParcelaFreteService().findParcelaEMEX(calculoFrete);
                calculoFrete.addParcelaGeral(freteParcela);
            }
        }

        finalizaCalculo(calculoFrete);

    }

    private ParcelaServico findParametroTaxaRecalculo(CalculoFrete calculoFrete, ParcelaServico parcelaServico) {
        if (parcelaServico.getParametro() == null) {
            return parcelaServico;
        }
        TaxaCliente taxaCliente = (TaxaCliente) parcelaServico.getParametro();
        if (taxaCliente == null) {
            return parcelaServico;
        }
        String cdParcelaPreco = parcelaServico.getParcelaPreco().getCdParcelaPreco();
        return recalcularTaxa(calculoFrete, parcelaServico, cdParcelaPreco);
    }

    private ParcelaServico recalcularTaxa(CalculoFrete calculoFrete, ParcelaServico parcelaServico, String cdParcelaPreco) {
        if (isRecalculoTaxaColetaEntrega(cdParcelaPreco)) {
            return recalculoTaxaColetaEntrega(calculoFrete, parcelaServico);
        } else if (isRecalculoTaxaColetaEntregaEmergencial(cdParcelaPreco)) {
            return recalculoTaxaColetaEntregaEmergencial(parcelaServico);
        } else if (ConstantesExpedicao.CD_TAXA_COMBUSTIVEL.equals(cdParcelaPreco)) {
            return recalculoTaxaCombustivel(calculoFrete, parcelaServico);
        } else if (ConstantesExpedicao.CD_TAXA_TERRESTRE.equals(cdParcelaPreco)) {
            return recalculoTaxaTerrestre(parcelaServico);
        }
        return parcelaServico;
    }

    private boolean isRecalculoTaxaColetaEntrega(String cdParcelaPreco) {
        return ConstantesExpedicao.CD_TAXA_COLETA_URBANA_CONVENCIONAL.equals(cdParcelaPreco)
                || ConstantesExpedicao.CD_TAXA_COLETA_URBANA_EMERGENCIA.equals(cdParcelaPreco)
                || ConstantesExpedicao.CD_TAXA_COLETA_INTERIOR_CONVENCIONAL.equals(cdParcelaPreco)
                || ConstantesExpedicao.CD_TAXA_ENTREGA_URBANA_CONVENCIONAL.equals(cdParcelaPreco)
                || ConstantesExpedicao.CD_TAXA_ENTREGA_URBANA_EMERGENCIA.equals(cdParcelaPreco)
                || ConstantesExpedicao.CD_TAXA_ENTREGA_INTERIOR_CONVENCIONAL.equals(cdParcelaPreco);
    }

    private boolean isRecalculoTaxaColetaEntregaEmergencial(String cdParcelaPreco) {
        return ConstantesExpedicao.CD_TAXA_COLETA_INTERIOR_EMERGENCIA.equals(cdParcelaPreco)
                || ConstantesExpedicao.CD_TAXA_ENTREGA_INTERIOR_EMERGENCIA.equals(cdParcelaPreco);
    }

    private ParcelaServico recalculoTaxaColetaEntrega(CalculoFrete calculoFrete, ParcelaServico parcelaServico) {
        TaxaCliente taxaCliente = (TaxaCliente) parcelaServico.getParametro();
        BigDecimal vlParcela = parcelaServico.getVlBrutoParcela();
        BigDecimal psReferencia = calculoFrete.getPsReferencia() == null ? BigDecimalUtils.ZERO : calculoFrete.getPsReferencia();
        BigDecimal vlTaxa = taxaCliente.getVlTaxa();
        ValorTaxa valorTaxa = ((ParcelaTaxaColetaEntrega) parcelaServico).getValorTaxa();

        if ("A".equals(taxaCliente.getTpTaxaIndicador().getValue())) {
            vlTaxa = BigDecimalUtils.acrescimo(valorTaxa.getVlTaxa(), taxaCliente.getVlTaxa());
        } else if ("D".equals(taxaCliente.getTpTaxaIndicador().getValue())) {
            vlTaxa = BigDecimalUtils.desconto(valorTaxa.getVlTaxa(), taxaCliente.getVlTaxa());
        }

        if (BigDecimalUtils.hasValue(taxaCliente.getPsMinimo())) {
            if (CompareUtils.le(psReferencia, taxaCliente.getPsMinimo())) {
                vlParcela = vlTaxa;
            } else {
                vlParcela = psReferencia.subtract(taxaCliente.getPsMinimo()).multiply(taxaCliente.getVlExcedente()).add(vlTaxa);
            }
        } else {
            if (!BigDecimalUtils.hasValue(taxaCliente.getVlExcedente())) {
                vlParcela = vlTaxa;
            } else if (CompareUtils.gt(taxaCliente.getVlExcedente(), BigDecimalUtils.ZERO)) {
                if (CompareUtils.le(psReferencia, valorTaxa.getPsTaxado())) {
                    vlParcela = vlTaxa;
                } else {
                    vlParcela = psReferencia.subtract(valorTaxa.getPsTaxado()).multiply(taxaCliente.getVlExcedente()).add(vlTaxa);
                }
            }
        }
        parcelaServico.setVlBrutoParcela(vlParcela);
        parcelaServico.setVlParcela(vlParcela);
        return parcelaServico;
    }

    private ParcelaServico recalculoTaxaColetaEntregaEmergencial(ParcelaServico parcelaServico) {
        TaxaCliente taxaCliente = (TaxaCliente) parcelaServico.getParametro();
        BigDecimal vlParcela = parcelaServico.getVlBrutoParcela();
        BigDecimal vlDistancia = ((ParcelaTaxaColetaEntrega) parcelaServico).getVlDistancia();
        if ("V".equals(taxaCliente.getTpTaxaIndicador().getValue())) {
            if ((taxaCliente.getPsMinimo() == null) || (CompareUtils.gt(vlDistancia, taxaCliente.getPsMinimo()))) {
                vlParcela = vlDistancia.multiply(BigDecimalUtils.getBigDecimal("2")).multiply(taxaCliente.getVlTaxa());
            } else {
                vlParcela = taxaCliente.getPsMinimo()
                        .multiply(BigDecimalUtils.getBigDecimal("2"))
                        .multiply(taxaCliente.getVlTaxa());
            }
        } else if ("A".equals(taxaCliente.getTpTaxaIndicador().getValue())) {
            if ((taxaCliente.getPsMinimo() == null) || (CompareUtils.gt(vlDistancia, taxaCliente.getPsMinimo()))) {
                vlParcela = vlDistancia
                        .multiply(BigDecimalUtils.getBigDecimal("2"))
                        .multiply(BigDecimalUtils.acrescimo(parcelaServico.getVlUnitarioParcela(), taxaCliente.getVlTaxa()));
            } else {
                vlParcela = taxaCliente.getPsMinimo()
                        .multiply(BigDecimalUtils.getBigDecimal("2"))
                        .multiply(BigDecimalUtils.acrescimo(parcelaServico.getVlUnitarioParcela(), taxaCliente.getVlTaxa()));
            }
        } else if ("D".equals(taxaCliente.getTpTaxaIndicador().getValue())) {
            if ((taxaCliente.getPsMinimo() == null) || (CompareUtils.gt(vlDistancia, taxaCliente.getPsMinimo()))) {
                vlParcela = vlDistancia
                        .multiply(BigDecimalUtils.getBigDecimal("2"))
                        .multiply(BigDecimalUtils.desconto(parcelaServico.getVlUnitarioParcela(), taxaCliente.getVlTaxa()));
            } else {
                vlParcela = taxaCliente.getPsMinimo()
                        .multiply(BigDecimalUtils.getBigDecimal("2"))
                        .multiply(BigDecimalUtils.desconto(parcelaServico.getVlUnitarioParcela(), taxaCliente.getVlTaxa()));
            }
        }
        parcelaServico.setVlBrutoParcela(vlParcela);
        parcelaServico.setVlParcela(vlParcela);
        return parcelaServico;
    }

    private ParcelaServico recalculoTaxaCombustivel(CalculoFrete calculoFrete, ParcelaServico parcelaServico) {
        TaxaCliente taxaCliente = (TaxaCliente) parcelaServico.getParametro();
        BigDecimal vlParcela = parcelaServico.getVlBrutoParcela();

        if ("V".equals(taxaCliente.getTpTaxaIndicador().getValue())) {
            vlParcela = taxaCliente.getVlTaxa().multiply(calculoFrete.getPsReferencia());
        } else if ("A".equals(taxaCliente.getTpTaxaIndicador().getValue())) {
            vlParcela = BigDecimalUtils.acrescimo(parcelaServico.getVlUnitarioParcela(), taxaCliente.getVlTaxa()).multiply(calculoFrete.getPsReferencia());
        } else if ("D".equals(taxaCliente.getTpTaxaIndicador().getValue())) {
            vlParcela = BigDecimalUtils.desconto(parcelaServico.getVlUnitarioParcela(), taxaCliente.getVlTaxa()).multiply(calculoFrete.getPsReferencia());
        }
        parcelaServico.setVlBrutoParcela(vlParcela);
        parcelaServico.setVlParcela(vlParcela);
        return parcelaServico;
    }

    private ParcelaServico recalculoTaxaTerrestre(ParcelaServico parcelaServico) {
        TaxaCliente taxaCliente = (TaxaCliente) parcelaServico.getParametro();
        BigDecimal vlParcela = parcelaServico.getVlBrutoParcela();

        if ("V".equals(taxaCliente.getTpTaxaIndicador().getValue())) {
            vlParcela = taxaCliente.getVlTaxa();
        } else if ("A".equals(taxaCliente.getTpTaxaIndicador().getValue())) {
            vlParcela = BigDecimalUtils.acrescimo(parcelaServico.getVlBrutoParcela(), taxaCliente.getVlTaxa());
        } else if ("D".equals(taxaCliente.getTpTaxaIndicador().getValue())) {
            vlParcela = BigDecimalUtils.desconto(parcelaServico.getVlBrutoParcela(), taxaCliente.getVlTaxa());
        }
        parcelaServico.setVlBrutoParcela(vlParcela);
        parcelaServico.setVlParcela(vlParcela);
        return parcelaServico;
    }

    private void executeCalculoFreteRodoviarioNacionalNormalCortesia(CalculoFrete calculoFrete) {
        executeCalculoFreteRodoviarioNacionalNormalNormal(calculoFrete);
        //Total do frete (parcelas, servicos adicionais, tributos)
        getCalculoTributoService().calculaTributos(calculoFrete);
        executeCalculoFreteCortesia(calculoFrete);
    }

    private void executeCalculoFreteRodoviarioInternacionalNormalNormal(CalculoFrete calculoFrete) {
        //Define parametros iniciais
        setParametrosCalculo(calculoFrete);

        //Define tabela
        findTabelaPreco(calculoFrete);

        //Define peso de referência
        findPesoReferencia(calculoFrete);

        if (Boolean.TRUE.equals(calculoFrete.getBlCalculaParcelas())) {
            ParcelaServico freteParcela;
            //Frete Peso
            freteParcela = getCalculoParcelaFreteService().findParcelaFretePeso(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Frete Valor
            freteParcela = getCalculoParcelaFreteService().findParcelaFreteValor(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Generalidades
            findGeneralidades(calculoFrete);

            //Parametros gerais parcelas
            getCalculoParcelaFreteService().findParametroParcelasFrete(calculoFrete);
        }

        //Servicos adicionais
        if (Boolean.TRUE.equals(calculoFrete.getBlCalculaServicosAdicionais())) {
            findServicosAdicionais(calculoFrete);
        }

        finalizaCalculo(calculoFrete);
    }

    private void executeCalculoFreteAereoNacionalNormalNormal(CalculoFrete calculoFrete) {
        //Define parametros iniciais
        setParametrosCalculo(calculoFrete);

        //Define tabela
        findTabelaPreco(calculoFrete);

        //Define peso de referência
        findPesoReferencia(calculoFrete);

        if (Boolean.TRUE.equals(calculoFrete.getBlCalculaParcelas())) {

            ParcelaServico freteParcela;
            //Frete Peso
            freteParcela = getCalculoParcelaFreteService().findParcelaFretePeso(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Frete Valor
            freteParcela = getCalculoParcelaFreteService().findParcelaFreteValor(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //GRIS
            freteParcela = getCalculoParcelaFreteService().findParcelaGRIS(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            Boolean blColetaInterior = Boolean.FALSE;
            Boolean blEntregaInterior = Boolean.FALSE;
            if (calculoFrete.getRestricaoRotaOrigem() != null && calculoFrete.getRestricaoRotaOrigem().getIdAeroporto() != null) {
                blColetaInterior = findBlColetaEntregaInterior(calculoFrete.getRestricaoRotaOrigem());
            }
            if (calculoFrete.getRestricaoRotaDestino() != null && calculoFrete.getRestricaoRotaDestino().getIdAeroporto() != null) {
                blEntregaInterior = findBlColetaEntregaInterior(calculoFrete.getRestricaoRotaDestino());
            }

            //Taxa Coleta
            freteParcela = getCalculoParcelaFreteService().findParcelaTaxaColetaEntrega(calculoFrete, "Coleta", blColetaInterior, calculoFrete.getBlColetaEmergencia());
            calculoFrete.addTaxa(freteParcela);

            //Taxa Entrega
            freteParcela = getCalculoParcelaFreteService().findParcelaTaxaColetaEntrega(calculoFrete, "Entrega", blEntregaInterior, calculoFrete.getBlEntregaEmergencia());
            calculoFrete.addTaxa(freteParcela);

            //Pedágio
            freteParcela = getCalculoParcelaFreteService().findParcelaPedagio(calculoFrete, blColetaInterior, blEntregaInterior);
            calculoFrete.addParcelaGeral(freteParcela);

            //Generalidades
            findGeneralidades(calculoFrete);

            //Parametros gerais
            getCalculoParcelaFreteService().findParametroParcelasFrete(calculoFrete);

            //Taxa Terrestre
            freteParcela = getCalculoParcelaFreteService().findParcelaTaxaTerrestre(calculoFrete);
            calculoFrete.addTaxa(freteParcela);

            //Taxa Combustivel
            freteParcela = getCalculoParcelaFreteService().findParcelaTaxaCombustivel(calculoFrete);
            calculoFrete.addTaxa(freteParcela);

            //Taxa Suframa
            freteParcela = getCalculoParcelaFreteService().findParcelaTaxaSuframa(calculoFrete);
            calculoFrete.addTaxa(freteParcela);

            //TRT
            freteParcela = getCalculoParcelaFreteService().findParcelaTRT(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Taxa Dificuldade de Entrega
            freteParcela = getCalculoParcelaFreteService().findParcelaTDE(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Agendamento Entrega CTE
            freteParcela = getCalculoParcelaFreteService().findParcelaAgendamentoEntregaCTE(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);
            //EMEX
            freteParcela = getCalculoParcelaFreteService().findParcelaEMEX(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);

            //Paletização CTE
            freteParcela = getCalculoParcelaFreteService().findParcelaPaletizacaoCTE(calculoFrete);
            calculoFrete.addParcelaGeral(freteParcela);
        }

        //Servicos adicionais
        if (Boolean.TRUE.equals(calculoFrete.getBlCalculaServicosAdicionais())) {
            findServicosAdicionais(calculoFrete);
        }
    }

    private void executeCalculoFreteAereoNacionalNormalCortesia(CalculoFrete calculoFrete) {
        executeCalculoFreteAereoNacionalNormalNormal(calculoFrete);
        //Total do frete (parcelas, servicos adicionais, tributos)
        getCalculoTributoService().calculaTributos(calculoFrete);
        executeCalculoFreteCortesia(calculoFrete);
    }

    private void executeCalculoFreteNacionalNormalManual(CalculoFrete calculoFrete) {
        //Configura cliente
        findClienteBase(calculoFrete);

        //Valida parametros para calculo
        setParametrosCalculo(calculoFrete);

        //Define peso de referência
        findPesoReferencia(calculoFrete);

        //Servicos adicionais
        //findServicosAdicionais(calculoFrete);

        executeCalculoFreteNacionalManual(calculoFrete, false);
    }

    private void executeCalculoFreteNacionalReentregaNormal(CalculoFrete calculoFrete) {
        //busca os dados do calculo armazenado
        findCalculoFrete(calculoFrete);

        //Define parametros iniciais
        setParametrosCalculoReentrega(calculoFrete);

        //LMS-6359
        DoctoServico doctoServico = calculoFrete.getDoctoServico();
        DoctoServico doctoServicoOriginal = doctoServico.getDoctoServicoOriginal();

        ParametroCliente parametroClienteOriginal = doctoServicoOriginal.getParametroCliente();
        Boolean blImpostoReentrega = null;

        if (parametroClienteOriginal != null && parametroClienteOriginal.getTabelaDivisaoCliente() != null) {
            blImpostoReentrega = parametroClienteOriginal.getTabelaDivisaoCliente().getBlImpBaseReentrega();
        }

        BigDecimal pcCobrancaReentrega = getParametroCliente(parametroClienteOriginal);

        List<ParcelaServico> lista = findParcelasFreteTransferencia(calculoFrete);

        BigDecimal vlIdTransferencia = getVlTransferencia(lista);

        BigDecimal vlImposto = doctoServicoOriginal.getVlImposto();
        BigDecimal vlDesconto = doctoServicoOriginal.getVlDesconto();

        BigDecimal vlParcela = BigDecimal.ZERO;
        vlParcela = calculaVlParcela(calculoFrete, blImpostoReentrega, pcCobrancaReentrega, vlIdTransferencia,
                vlImposto, vlDesconto);

        calculoFrete.addParcelaGeral(getCalculoParcelaFreteService().findParcela(vlParcela, ConstantesExpedicao.CD_FRETE_PESO));

        finalizaCalculo(calculoFrete);
    }

    private BigDecimal getVlTransferencia(List<ParcelaServico> lista) {
        BigDecimal vlTransferencias = BigDecimal.ZERO;
        if (lista != null) {
            for (ParcelaServico parcelaServico : lista) {
                if (parcelaServico.getParcelaPreco() != null
                        && "IDTransferencia".equals(parcelaServico.getParcelaPreco().getCdParcelaPreco())) {
                    vlTransferencias = vlTransferencias.add(parcelaServico.getVlParcela());
                }
            }
        }
        return vlTransferencias;
    }

    private BigDecimal getParametroCliente(ParametroCliente parametroCliente) {
        BigDecimal pcCobrancaReentrega = BigDecimal.ZERO;
        if (parametroCliente != null) {
            if (BigDecimalUtils.hasValue(parametroCliente.getPcCobrancaReentrega())) {
                pcCobrancaReentrega = parametroCliente.getPcCobrancaReentrega();
            }
        } else {
            pcCobrancaReentrega = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.PC_COBRANCA_REENTREGA);
        }
        return pcCobrancaReentrega;
    }

    private BigDecimal calculaVlParcela(CalculoFrete calculoFrete, Boolean blImpostoReentrega, BigDecimal pcCobrancaReentrega, BigDecimal vlTransferencias, BigDecimal vlImposto, BigDecimal vlDesconto) {
        BigDecimal vlParcela;
        BigDecimal vlComDesconto = calculoFrete.getVlTotalParcelas().subtract(vlDesconto);
        // Se existe valor de transferencia
        if (!vlTransferencias.equals(BigDecimal.ZERO)) {
            //LMS-6359
            if ((blImpostoReentrega == null || blImpostoReentrega) || !BigDecimalUtils.hasValue(vlImposto)) {
                BigDecimal totalParcelas = vlComDesconto.subtract(vlTransferencias);
                vlParcela = totalParcelas.multiply(BigDecimalUtils.percent(pcCobrancaReentrega));
            } else {
                //LMS-6359
                BigDecimal totalParcelas = vlComDesconto.subtract(vlImposto).subtract(vlTransferencias);
                vlParcela = totalParcelas.multiply(BigDecimalUtils.percent(pcCobrancaReentrega));
            }
        } else {
            if ((blImpostoReentrega == null || blImpostoReentrega) || !BigDecimalUtils.hasValue(vlImposto)) {
                vlParcela = vlComDesconto.multiply(BigDecimalUtils.percent(pcCobrancaReentrega));
            } else {
                vlParcela = (vlComDesconto.subtract(vlImposto)).multiply(BigDecimalUtils.percent(pcCobrancaReentrega));
            }
        }

        ParcelaServico parcelaServico = getCalculoParcelaFreteService().findParcelaTaxaMinimoReentrega(calculoFrete);

        if (parcelaServico != null && CompareUtils.le(vlParcela, parcelaServico.getVlParcela())) {
            vlParcela = parcelaServico.getVlParcela();
        }
        return vlParcela;
    }

    private void executeCalculoFreteNacionalDevolucaoNormal(CalculoFrete calculoFrete) {

		/*Busca os dados do calculo armazenado*/
        findCalculoFrete(calculoFrete);

		/*Define parametros iniciais*/
        setParametrosCalculo(calculoFrete);

		/*Percentual de cobranca*/

        ParametroCliente parametroCliente = calculoFrete.getParametroCliente();

        DoctoServico doctoServicoOriginal = calculoFrete.getDoctoServico().getDoctoServicoOriginal();
        ParametroCliente parametroClienteOriginal = doctoServicoOriginal.getParametroCliente();

        Boolean blImposto = buscaInidicadorImpostoDevolucao(parametroClienteOriginal);

        BigDecimal pcCobrancaDevolucao = buscaPercentualDevolucao(parametroClienteOriginal);

        BigDecimal vlParcela = calculaValorDevolucao(blImposto, calculoFrete.getVlTotalParcelas(), doctoServicoOriginal.getVlImposto(), pcCobrancaDevolucao);

		/*Conhecimento original*/
        Conhecimento conhecimentoOriginal = (Conhecimento) doctoServicoOriginal;

		/*CQPRO00026161*/
        ParcelaServico parcelaServico = null;
        if (ConstantesExpedicao.TP_FRETE_CIF.equals(conhecimentoOriginal.getTpFrete().getValue())) {
            parcelaServico = getCalculoParcelaFreteService().findParcela(vlParcela, ConstantesExpedicao.CD_FRETE_PESO);
        }

        if (ConstantesExpedicao.TP_FRETE_FOB.equals(conhecimentoOriginal.getTpFrete().getValue())) {
            parcelaServico = getCalculoParcelaFreteService().findParcela(calculoFrete.getVlTotal().multiply(BigDecimal.valueOf(2)), ConstantesExpedicao.CD_OUTROS);
        }

		/*Adiciona a parcelaServico as parcelas de frete*/
        calculoFrete.addParcelaGeral(parcelaServico);

    }

    private BigDecimal buscaPercentualDevolucao(ParametroCliente parametroCliente) {
        BigDecimal pcCobrancaDevolucao = BigDecimalUtils.ZERO;

        if (parametroCliente != null && BigDecimalUtils.hasValue(parametroCliente.getPcCobrancaDevolucoes())) {
            pcCobrancaDevolucao = parametroCliente.getPcCobrancaDevolucoes();
        } else {
            pcCobrancaDevolucao = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.PC_COBRANCA_DEVOLUCAO);
        }
        return pcCobrancaDevolucao;
    }

    private BigDecimal calculaValorDevolucao(Boolean blImposto, BigDecimal vlTotalParcelas, BigDecimal vlImposto, BigDecimal pcCobrancaDevolucao) {
        if (blImposto == null || blImposto || !BigDecimalUtils.hasValue(vlImposto)) {
            return vlTotalParcelas.multiply(BigDecimalUtils.percent(pcCobrancaDevolucao));
        } else {
            return (vlTotalParcelas.subtract(vlImposto)).multiply(BigDecimalUtils.percent(pcCobrancaDevolucao));
        }
    }

    private Boolean buscaInidicadorImpostoDevolucao(ParametroCliente parametroClienteOriginal) {
        if (parametroClienteOriginal != null && parametroClienteOriginal.getTabelaDivisaoCliente() != null) {
            return parametroClienteOriginal.getTabelaDivisaoCliente().getBlImpBaseDevolucao();
        } else {
            return true;
        }
    }

    private void executeCalculoFreteNacionalDevolucaoManual(CalculoFrete calculoFrete) {
        executeCalculoFreteNacionalManual(calculoFrete, true);
    }

    private void executeCalculoFreteNacionalDevolucaoCotacao(CalculoFrete calculoFrete) {
        //busca os dados do calculo armazenado
        findCalculoFrete(calculoFrete);

        //Define parametros iniciais
        setParametrosCalculo(calculoFrete);

        executeCalculoFreteRodoviarioNacionalNormalNormal(calculoFrete);
    }

    private void executeCalculoFreteNacionalDevolucaoCortesia(CalculoFrete calculoFrete) {
        //busca os dados do conhecimento armazenado
        findCalculoFrete(calculoFrete);
        //busca as parcelas do conhecimento armazenado
        findParcelasFrete(calculoFrete);

        //Define parametros iniciais
        setParametrosCalculo(calculoFrete);

        executeCalculoFreteCortesia(calculoFrete);
    }

    private void executeCalculoFreteCortesia(CalculoFrete calculoFrete) {
        BigDecimal vlMinimoFreteCortesia = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.VLR_MINIMO_FRETE_CORTESIA);

        //Calcula o valor minimo de frete cortesia
        CalculoFrete calculoMinimoCortesia = getCalculoMinimoCortesia(calculoFrete, vlMinimoFreteCortesia);

        if (CompareUtils.lt(calculoFrete.getVlTotal(), calculoMinimoCortesia.getVlTotal())) {
            //Reseta as parcelas
            calculoFrete.resetParcelas();

            //Cria parcela unica de frete peso
            calculoFrete.setParcelasGerais(calculoMinimoCortesia.getParcelasGerais());

            //Copia os dados do calculo de minimo cortesia
            calculoFrete.setObservacoes(calculoMinimoCortesia.getObservacoes());
            calculoFrete.setTributo(calculoMinimoCortesia.getTributo());

            calculoFrete.setVlTotalServicosAdicionais(BigDecimalUtils.ZERO);
            calculoFrete.setVlTotalParcelas(calculoMinimoCortesia.getVlTotalParcelas());
            calculoFrete.setVlTotal(calculoMinimoCortesia.getVlTotal());
            calculoFrete.setVlDesconto(BigDecimalUtils.ZERO);
            calculoFrete.setVlDevido(calculoMinimoCortesia.getVlDevido());

        }
        //Ordena parcelas e arredonda valores
        getCalculoParcelaServicoService().finalizaCalculoParcelas(calculoFrete);
    }

    private CalculoFrete getCalculoMinimoCortesia(CalculoFrete calculoFrete, BigDecimal vlMinimoFreteCortesia) {
        CalculoFrete calculoFreteCortesia = ExpedicaoUtils.newCalculoFrete(new DomainValue(calculoFrete.getTpDocumentoServico()));

        calculoFreteCortesia.setTpCalculo(ConstantesExpedicao.CALCULO_NORMAL);
        calculoFreteCortesia.setTpConhecimento(calculoFrete.getTpConhecimento());
        calculoFreteCortesia.setTpAbrangencia(calculoFrete.getTpAbrangencia());
        calculoFreteCortesia.setTpModal(calculoFrete.getTpModal());
        calculoFreteCortesia.setTpFrete(calculoFrete.getTpFrete());

        calculoFreteCortesia.setClienteBase(calculoFrete.getClienteBase());
        calculoFreteCortesia.setDadosCliente(calculoFrete.getDadosCliente());
        calculoFreteCortesia.setPsRealInformado(calculoFrete.getPsRealInformado());
        calculoFreteCortesia.setPsCubadoInformado(calculoFrete.getPsCubadoInformado());
        calculoFreteCortesia.setQtVolumes(calculoFrete.getQtVolumes());
        calculoFreteCortesia.setIdDensidade(calculoFrete.getIdDensidade());
        calculoFreteCortesia.setVlMercadoria(calculoFrete.getVlMercadoria());
        calculoFreteCortesia.setIdServico(calculoFrete.getIdServico());
        calculoFreteCortesia.setIdProdutoEspecifico(calculoFrete.getIdProdutoEspecifico());

        calculoFreteCortesia.setNrCepColeta(calculoFrete.getNrCepColeta());
        calculoFreteCortesia.setNrCepEntrega(calculoFrete.getNrCepEntrega());
        calculoFreteCortesia.setRestricaoRotaOrigem(calculoFrete.getRestricaoRotaOrigem());
        calculoFreteCortesia.setRestricaoRotaDestino(calculoFrete.getRestricaoRotaDestino());
        calculoFreteCortesia.setDoctoServico(calculoFrete.getDoctoServico());

        ParcelaServico parcelaFretePeso = getCalculoParcelaFreteService().findParcela(vlMinimoFreteCortesia, ConstantesExpedicao.CD_FRETE_PESO);
        List<ParcelaServico> parcelas = new ArrayList<ParcelaServico>(1);
        parcelas.add(parcelaFretePeso);
        calculoFreteCortesia.setParcelasGerais(parcelas);

        executeCalculoFreteAereoNacionalNormalNormal(calculoFreteCortesia);
        getCalculoTributoService().calculaTributos(calculoFreteCortesia);

        return calculoFreteCortesia;
    }

    /**
     * Executa o cálculo de frete manual
     *
     * @param calculoFrete
     */
    private void executeCalculoFreteNacionalManual(CalculoFrete calculoFrete, boolean updateParametros) {
        if (updateParametros) {
            setParametrosCalculo(calculoFrete);
        }

        //Parcela unica de Frete Peso
        BigDecimal vlMinimoFretePreso = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.VLR_MINIMO_FRETE_PESO);
        finalizaCalculo(calculoFrete);
        if (CompareUtils.lt(calculoFrete.getVlTotal(), vlMinimoFretePreso)) {
            throw new BusinessException("LMS-04081");
        }
    }

    /**
     * Calcula o s valores das parcelas generalidade
     *
     * @param calculoFrete
     */
    private void findGeneralidades(CalculoFrete calculoFrete) {
        List<ParcelaServico> generalidades = getCalculoParcelaFreteService().executeCalculoParcelasGeneralidade(calculoFrete);
        for (ParcelaServico parcelaServico : generalidades) {
            calculoFrete.addGeneralidade(parcelaServico);
        }
    }


    /**
     * Verificação necessária para informar a UF de origem correta, necessário para casos em que o municipio possui
     * a filial de atendimento em outro estado -  CQPRO00023405
     *
     * @param restricaoRota
     */
    private void verificaUFAtendimentoOrigem(RestricaoRota restricaoRota) {

        //ZONA-PAIS-UF-FILIAL
        if (restricaoRota.getIdZona() != null
                && restricaoRota.getIdPais() != null
                && restricaoRota.getIdUnidadeFederativa() != null
                && restricaoRota.getIdFilial() != null) {

            Long idMunicipio = municipioFilialService.getMunicipioService().findIdMunicipioByPessoa(restricaoRota.getIdFilial());
            Map dat = municipioFilialService.getMunicipioService().findZonaPaisUFByIdMunicipio(idMunicipio);

            restricaoRota.setIdUnidadeFederativa((Long) dat.get("idUnidadeFederativa"));
        }
    }


    /**
     * Adiciona informações sobre as rotas de origem e destino
     * <p>
     * Verifica se a Capitl da UF pertence a alguma filial Matriz
     * <p>
     * Verifica qual o cliente base para cálculo do frete
     * <p>
     * Busca e valida os parametros do cliente
     *
     * @param calculoFrete
     */
    private void setParametrosCalculo(CalculoFrete calculoFrete) {
        setParametrosCalculoCommon(calculoFrete);
        validadeParametros(calculoFrete);
    }

    private void setParametrosCalculoReentrega(CalculoFrete calculoFrete) {
        setParametrosCalculoCommon(calculoFrete);
    }


    @SuppressWarnings("unchecked")
    public void setParametrosCalculoCommon(CalculoFrete calculoFrete) {
		/*Busca tipo de localizacao municipio (origem e destino)*/
        RestricaoRota restricaoRotaOrigem = calculoFrete.getRestricaoRotaOrigem();
        RestricaoRota restricaoRotaDestino = calculoFrete.getRestricaoRotaDestino();


        if (calculoFrete.getTpConhecimento().equals(ConstantesExpedicao.CONHECIMENTO_REENTREGA)
                || calculoFrete.getTpConhecimento().equals(ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_FRETE)
                || (calculoFrete.getTpCalculo().equals(ConstantesExpedicao.CALCULO_COTACAO) && calculoFrete.getIdCotacao() == null)) {

            calculoFrete.getDadosCliente().setIdTipoLocalizacaoOrigem(findIdTipoLocalizacao(restricaoRotaOrigem.getIdMunicipio(), calculoFrete.getIdServico(), calculoFrete.getNrCepColeta()));
            calculoFrete.getDadosCliente().setIdTipoLocalizacaoDestino(findIdTipoLocalizacao(restricaoRotaDestino.getIdMunicipio(), calculoFrete.getIdServico(), calculoFrete.getNrCepEntrega()));
        }

		/*OBTEM O TIPO DE LOCALIZACAO OPERACIONAL*/
        List<TipoLocalizacaoMunicipio> listLocalOperacional = tipoLocalizacaoMunicipioService.findTipoLocalizacaoOperacional();


		/*BUSCA DADOS MUNICIPIO DE ORIGEM*/
        Municipio municipioOrigem = municipioFilialService.getMunicipioService().findById(restricaoRotaOrigem.getIdMunicipio());

		/*Monta restrição para buscar rota de origem*/
        Long idTipoLocalizacao = calculoFrete.getDadosCliente().getIdTipoLocalizacaoOrigem();
        if (idTipoLocalizacao == null) {
            idTipoLocalizacao = findIdTipoLocalizacao(restricaoRotaOrigem.getIdMunicipio(), calculoFrete.getIdServico(), calculoFrete.getNrCepColeta());
        }
        Long idTipoLocalizacaoComercial = findIdTipoLocalizacaoComercial(restricaoRotaOrigem.getIdMunicipio(), calculoFrete.getIdServico(), calculoFrete.getNrCepColeta());

        findRestricaoRota(restricaoRotaOrigem, municipioOrigem, idTipoLocalizacao, idTipoLocalizacaoComercial);
        restricaoRotaOrigem.setListLocalOperacional(listLocalOperacional);

		/*Verificação necessária para informar a UF de origem correta, necessário para casos em que o municipio possui
		a filial de atendimento em outro estado -  CQPRO00023405 */
        verificaUFAtendimentoOrigem(restricaoRotaOrigem);


		/*BUSCA DADOS MUNICIPIO DESTINO*/
        Municipio municipioDestino = municipioFilialService.getMunicipioService().findById(restricaoRotaDestino.getIdMunicipio());

		/*Monta a restrição para buscar rota de destino*/
        idTipoLocalizacao = calculoFrete.getDadosCliente().getIdTipoLocalizacaoDestino();
        if (idTipoLocalizacao == null) {
            idTipoLocalizacao = findIdTipoLocalizacao(restricaoRotaDestino.getIdMunicipio(), calculoFrete.getIdServico(), calculoFrete.getNrCepEntrega());
        }
        idTipoLocalizacaoComercial = findIdTipoLocalizacaoComercial(restricaoRotaDestino.getIdMunicipio(), calculoFrete.getIdServico(), calculoFrete.getNrCepEntrega());

        findRestricaoRota(restricaoRotaDestino, municipioDestino, idTipoLocalizacao, idTipoLocalizacaoComercial);
        restricaoRotaDestino.setListLocalOperacional(listLocalOperacional);

		/*Verifica se a Unidade Federativa de destino cobra TAS (Taxa Adicional de Serviço)*/
        calculoFrete.setBlUFDestinoCobraTas(Boolean.TRUE.equals(municipioDestino.getUnidadeFederativa().getBlCobraTas()));
        calculoFrete.setBlUFDestinoCobraSuframa(Boolean.TRUE.equals(municipioDestino.getUnidadeFederativa().getBlCobraSuframa()));

		/*VERIFICA SE O MUNICIPIO DE ORIGEM*/
        MunicipioFilial municipioFilialOrigem = getCalculoFreteDAO().findMunicipioFilial(restricaoRotaOrigem.getIdMunicipio(), restricaoRotaOrigem.getIdFilial());
        if (municipioFilialOrigem != null) {
            calculoFrete.getDadosCliente().setBlRestricaoTransporteOrigem(Boolean.TRUE.equals(municipioFilialOrigem.getBlRestricaoTransporte()));
            calculoFrete.setMunicipioFilialOrigem(municipioFilialOrigem);
        }

		/*VERIFICA SE O MUNICIPIO DE DESTINO*/
        MunicipioFilial municipioFilialDestino = getCalculoFreteDAO().findMunicipioFilial(restricaoRotaDestino.getIdMunicipio(), restricaoRotaDestino.getIdFilial());
        if (municipioFilialDestino != null) {
            calculoFrete.setBlDificuldadeEntrega(Boolean.TRUE.equals(municipioFilialDestino.getBlDificuldadeEntrega()));
            calculoFrete.getDadosCliente().setBlRestricaoTransporteDestino(Boolean.TRUE.equals(municipioFilialDestino.getBlRestricaoTransporte()));
            calculoFrete.setMunicipioFilialDestino(municipioFilialDestino);
        } else {
            throw new BusinessException("LMS-04083");
        }

        if (calculoFrete.getDoctoServico() != null && calculoFrete.getDoctoServico().getNotaFiscalConhecimentos() != null) {
            int maxNotas = filialService.findMaxNotasCtrc(restricaoRotaDestino.getIdFilial(), SessionUtils.getFilialSessao().getIdFilial());
            if (maxNotas > 0) {
                int notas = calculoFrete.getDoctoServico().getNotaFiscalConhecimentos().size();
                if (notas > maxNotas) {
                    throw new BusinessException("LMS-04368", new Object[]{maxNotas});
                }
            }
        }

		/*Verifica se Capital da UF pertence a alguma Filial da Matriz.*/
        Long idMunicipioCapital = municipioDestino.getUnidadeFederativa().getMunicipio().getIdMunicipio();
        if (!Boolean.TRUE.equals(municipioFilialService.isMunicipioFilialMatriz(idMunicipioCapital)) && municipioFilialDestino != null) {
            List<Map> dataMap = municipioFilialService.findMunicipioFilialVigenteAtualByMunicipio(municipioDestino.getIdMunicipio());
            if (dataMap != null) {
                MunicipioFilial municipioFilialCapital = municipioFilialService.findById(MapUtils.getLong(dataMap.get(0), "idMunicipioFilial"));
                restricaoRotaDestino.setIdUnidadeFederativa(municipioFilialCapital.getFilial().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
            }
        }

		/*Passa o cliente consignatario para os parametros do calculo do frete*/
        if (calculoFrete.getDoctoServico() != null
                && calculoFrete.getDoctoServico().getClienteByIdClienteConsignatario() != null
                && calculoFrete.getDoctoServico().getClienteByIdClienteConsignatario().getIdCliente() != null) {

            Long idConsignatario = calculoFrete.getDoctoServico().getClienteByIdClienteConsignatario().getIdCliente();

            InscricaoEstadual inscricaoEstadual = inscricaoEstadualService.findByPessoaIndicadorPadrao(idConsignatario, Boolean.TRUE);

			/*Adiciona informações nos parametros do cálculo para cálcuo do ICMS*/
            List<TipoTributacaoIE> list = tipoTributacaoIEService.findVigentesByIdPessoa(idConsignatario);
            if (list != null && !list.isEmpty()) {
                TipoTributacaoIE tipo = list.get(0);
                calculoFrete.getDadosCliente().setTpSituacaoTributariaRecebedor(tipo.getTpSituacaoTributaria().getValue());
            }

            calculoFrete.getDadosCliente().setIdClienteRecebedor(idConsignatario);
            calculoFrete.getDadosCliente().setIdInscricaoEstadualRecebedor(inscricaoEstadual.getIdInscricaoEstadual());
        }
    }


    /**
     * Busca a configuração de municipio e altera o destino do calculo frete
     *
     * @param calculoFrete
     */
    public void setMunicipioDestinoCalculo(CalculoFrete calculoFrete) {
        if (calculoFrete != null) {
            MunicipioDestinoCalculo municipio = municipioDestinoCalculoService.findDestinoVigenteByOrigem(calculoFrete.getRestricaoRotaDestino().getIdMunicipio());

            if (municipio != null) {
                Municipio municipioDestino = municipio.getMunicipioDestino();
                RestricaoRota restricaoRotaDestino = calculoFrete.getRestricaoRotaDestino();
                Long idFilialDestino = null;
                List<Map> listAtendimento = municipioFilialService.findMunicipioFilialVigenteAtualByMunicipio(municipioDestino.getIdMunicipio());

                if (listAtendimento != null && listAtendimento.size() > 0) {
                    Map mf = listAtendimento.get(0);
                    idFilialDestino = (Long) mf.get("idFilial");
                }

                restricaoRotaDestino.setIdFilial(idFilialDestino);
                restricaoRotaDestino.setIdMunicipio(municipioDestino.getIdMunicipio());
                restricaoRotaDestino.setIdZona(municipioDestino.getUnidadeFederativa().getPais().getZona().getIdZona());
                restricaoRotaDestino.setIdPais(municipioDestino.getUnidadeFederativa().getPais().getIdPais());
                restricaoRotaDestino.setIdUnidadeFederativa(municipioDestino.getUnidadeFederativa().getIdUnidadeFederativa());

                Map<String, Object> aeroporto = filialService.findAeroportoFilial(idFilialDestino);
                if (aeroporto != null && aeroporto.get("idAeroporto") != null) {
                    restricaoRotaDestino.setIdAeroporto((Long) aeroporto.get("idAeroporto"));
                }

                Long idTipoLocalizacaoComercial = findIdTipoLocalizacaoComercial(restricaoRotaDestino.getIdMunicipio(), calculoFrete.getIdServico(), calculoFrete.getNrCepEntrega());
                Long idTipoLocalizacao = findIdTipoLocalizacao(restricaoRotaDestino.getIdMunicipio(), calculoFrete.getIdServico(), calculoFrete.getNrCepEntrega());

                restricaoRotaDestino.setIdTipoLocalizacao(idTipoLocalizacao);
                restricaoRotaDestino.setIdTipoLocalizacaoTarifa(idTipoLocalizacao);
                restricaoRotaDestino.setIdTipoLocalizacaoComercial(idTipoLocalizacaoComercial);
                calculoFrete.setRestricaoRotaDestino(restricaoRotaDestino);

                // Força a busca do tipo de localização pelo municipio destino da restrição rota
                calculoFrete.getDadosCliente().setIdTipoLocalizacaoDestino(null);
            }
        }
    }


    /**
     * Verifica se existe parametros para o cliente.
     * <p>
     * Verifica se é necessário aplicar o desconto total para o frete
     * <p>
     * Verifica se é necessário calcular serviços adicionais
     *
     * @param calculoFrete
     */
    private void validadeParametros(CalculoFrete calculoFrete) {

		/*Verifica parametro para calculo*/
        findParametroCliente(calculoFrete);

        if (calculoFrete.getParametroCliente() == null) {
            calculoFrete.setBlDescontoTotal(Boolean.FALSE);
        } else {
            calculoFrete.setBlDescontoTotal(Boolean.valueOf(BigDecimalUtils.hasValue(calculoFrete.getParametroCliente().getPcDescontoFreteTotal())));
        }
        if (calculoFrete.getBlCalculaServicosAdicionais() == null) {
            calculoFrete.setBlCalculaServicosAdicionais(Boolean.FALSE);
        }
    }

    /**
     * Executa o cálculo do frete para Conhecimento Nacional (CTRC) do tipo <b>Normal</b>.
     */
    public void executeCalculoCTRNormal(CalculoFrete calculoFrete) {
        if (!ConstantesExpedicao.ABRANGENCIA_NACIONAL.equals(calculoFrete.getTpAbrangencia())
                || !(ConstantesExpedicao.CONHECIMENTO_NORMAL.equals(calculoFrete.getTpConhecimento())
                || ConstantesExpedicao.CONHECIMENTO_REFATURAMENTO.equals(calculoFrete.getTpConhecimento())
                || ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO_PARCIAL.equals(calculoFrete.getTpConhecimento())
                //LMS-4068
                || ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equals(calculoFrete.getTpConhecimento())
        )
                ) {
            throw new BusinessException("parametrosInvalidos");
        }
        if (ConstantesExpedicao.MODAL_RODOVIARIO.equals(calculoFrete.getTpModal())) {
            executeCalculoCTRNormalRodoviario(calculoFrete);
        } else if (ConstantesExpedicao.MODAL_AEREO.equals(calculoFrete.getTpModal())) {
            executeCalculoCTRNormalAereo(calculoFrete);
        }
    }

    /**
     * Executa o cálculo normal rodoviário
     *
     * @param calculoFrete
     */
    private void executeCalculoCTRNormalRodoviario(CalculoFrete calculoFrete) {
        boolean isTpCalculoNormal = ConstantesExpedicao.CALCULO_NORMAL
                .equals(calculoFrete.getTpCalculo());
        boolean isTpCalculoCotacao = ConstantesExpedicao.CALCULO_COTACAO
                .equals(calculoFrete.getTpCalculo());
        boolean isTpCalculoManual = ConstantesExpedicao.CALCULO_MANUAL
                .equals(calculoFrete.getTpCalculo());
        boolean isTpCalculoCortesia = ConstantesExpedicao.CALCULO_CORTESIA
                .equals(calculoFrete.getTpCalculo());
        // LMS-3635
        boolean isTpConhecimentoRefaturamento = ConstantesExpedicao.CONHECIMENTO_REFATURAMENTO.equals(calculoFrete
                .getTpConhecimento());

        if ((isTpCalculoNormal && !isTpConhecimentoRefaturamento)
                || isTpCalculoCotacao) {
            executeCalculoFreteRodoviarioNacionalNormalNormal(calculoFrete);
            finalizaCalculo(calculoFrete);
        } else if (isTpCalculoManual) {
            executeCalculoFreteNacionalNormalManual(calculoFrete);
        } else if (isTpCalculoCortesia && !isTpConhecimentoRefaturamento) {
            executeCalculoFreteRodoviarioNacionalNormalCortesia(calculoFrete);
        } else if ((isTpCalculoNormal || isTpCalculoCortesia)
                && isTpConhecimentoRefaturamento) {
            executeCalculoCTRRefaturamento(calculoFrete);
        }
    }

    /**
     * Executa cálculo normal aéreo
     *
     * @param calculoFrete
     */
    private void executeCalculoCTRNormalAereo(CalculoFrete calculoFrete) {
        boolean isTpCalculoNormal = ConstantesExpedicao.CALCULO_NORMAL
                .equals(calculoFrete.getTpCalculo());
        boolean isTpCalculoCotacao = ConstantesExpedicao.CALCULO_COTACAO
                .equals(calculoFrete.getTpCalculo());
        boolean isTpCalculoManual = ConstantesExpedicao.CALCULO_MANUAL
                .equals(calculoFrete.getTpCalculo());
        boolean isTpCalculoCortesia = ConstantesExpedicao.CALCULO_CORTESIA
                .equals(calculoFrete.getTpCalculo());
        // LMS-3635
        boolean isTpConhecimentoRefaturamento = ConstantesExpedicao.CONHECIMENTO_REFATURAMENTO.equals(calculoFrete
                .getTpConhecimento());

        if ((isTpCalculoNormal && !isTpConhecimentoRefaturamento)
                || isTpCalculoCotacao) {
            executeCalculoFreteAereoNacionalNormalNormal(calculoFrete);
            finalizaCalculo(calculoFrete);
        } else if (isTpCalculoManual) {
            executeCalculoFreteNacionalNormalManual(calculoFrete);
        } else if (isTpCalculoCortesia && !isTpConhecimentoRefaturamento) {
            executeCalculoFreteAereoNacionalNormalCortesia(calculoFrete);
        } else if ((isTpCalculoNormal || isTpCalculoCortesia)
                && isTpConhecimentoRefaturamento) {
            executeCalculoCTRRefaturamento(calculoFrete);
        }
    }

    /**
     * Executa o cálculo do frete para Conhecimento Nacional (CTRC) do tipo <b>Reentrega</b>.
     */
    public void executeCalculoCTRReentrega(CalculoFrete calculoFrete) {
        if (!ConstantesExpedicao.ABRANGENCIA_NACIONAL.equals(calculoFrete.getTpAbrangencia())
                || !ConstantesExpedicao.CONHECIMENTO_REENTREGA.equals(calculoFrete.getTpConhecimento())
                || !ConstantesExpedicao.CALCULO_NORMAL.equals(calculoFrete.getTpCalculo())
                ) {
            throw new BusinessException("parametrosInvalidos");
        }
        executeCalculoFreteNacionalReentregaNormal(calculoFrete);
    }

    /**
     * Executa o cálculo do frete para Conhecimento Nacional (CTRC) do tipo <b>Refaturamento</b>.
     */
    public void executeCalculoCTRRefaturamento(CalculoFrete calculoFrete) {
        if (!ConstantesExpedicao.ABRANGENCIA_NACIONAL.equals(calculoFrete.getTpAbrangencia())
                || !ConstantesExpedicao.CONHECIMENTO_REFATURAMENTO.equals(calculoFrete.getTpConhecimento())
                // LMS-3635
                || !ConstantesExpedicao.CALCULO_NORMAL.equals(calculoFrete.getTpCalculo())
                ) {
            throw new BusinessException("parametrosInvalidos");
        }
        executaCalculoFreteNacionalRefaturamentoNormal(calculoFrete);
    }

    /**
     * Executa o cálculo do frete para Conhecimento Nacional (CTRC) do tipo <b>Complemento de Frete</b>.
     */
    public void executeCalculoCTRComplementoFrete(CalculoFrete calculoFrete) {
        if (!ConstantesExpedicao.ABRANGENCIA_NACIONAL.equals(calculoFrete.getTpAbrangencia())
                || !ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_FRETE.equals(calculoFrete.getTpConhecimento())
                ) {
            throw new BusinessException("parametrosInvalidos");
        }
        String tpCalculo = calculoFrete.getTpCalculo();

        if (ConstantesExpedicao.CALCULO_MANUAL.equals(tpCalculo)) {
            executeCalculoFreteNacionalManual(calculoFrete, true);
        } else if (ConstantesExpedicao.CALCULO_NORMAL.equals(tpCalculo)) {
            executeCalculoFreteComplementoFreteNormal(calculoFrete);
        } else {
            throw new BusinessException("parametrosInvalidos");
        }
    }

    private void executeCalculoFreteComplementoFreteNormal(CalculoFrete calculoFreteNovo) {
        CalculoFrete calculoFreteOriginal = new CalculoFrete();
        calculoFreteOriginal.setIdDoctoServico(calculoFreteNovo.getIdDoctoServico());
        findCalculoFrete(calculoFreteOriginal);

        calculoFreteNovo.setBlCalculaParcelas(Boolean.TRUE);
        calculoFreteNovo.setIdServico(calculoFreteOriginal.getIdServico());
        calculoFreteNovo.setIdTarifa(calculoFreteOriginal.getIdTarifa());
        calculoFreteNovo.setIdDivisaoCliente(calculoFreteOriginal.getIdDivisaoCliente());
        calculoFreteNovo.setNrCepColeta(calculoFreteOriginal.getNrCepColeta());
        calculoFreteNovo.setNrCepEntrega(calculoFreteOriginal.getNrCepEntrega());

        executeCalculoFreteRodoviarioNacionalNormalNormal(calculoFreteNovo);
        finalizaCalculo(calculoFreteNovo);

        BigDecimal vlTotalFreteOriginal = calculoFreteOriginal.getVlTotal().subtract(calculoFreteOriginal.getTributo().getVlImposto());
        BigDecimal vlTotalFreteNovo = calculoFreteNovo.getVlTotal().subtract(calculoFreteNovo.getTributo().getVlImposto());

        BigDecimal vlParcelaFretePeso = vlTotalFreteNovo.subtract(vlTotalFreteOriginal);
        if (CompareUtils.lt(vlParcelaFretePeso, BigDecimalUtils.ZERO)) {
            throw new BusinessException("LMS-04028");
        }

        ParcelaServico parcelaFretePeso = getCalculoParcelaFreteService().findParcela(vlParcelaFretePeso, ConstantesExpedicao.CD_FRETE_PESO);
        parcelaFretePeso.setVlBrutoParcela(vlParcelaFretePeso);
        parcelaFretePeso.setVlParcela(vlParcelaFretePeso);

        calculoFreteNovo.resetParcelas();
        calculoFreteNovo.addParcelaGeral(parcelaFretePeso);

        //Ajusta valores totais
        calculoFreteNovo.setVlTotalServicosAdicionais(BigDecimalUtils.ZERO);
        calculoFreteNovo.setVlDesconto(BigDecimalUtils.ZERO);

        //Recalcula tributos
        finalizaCalculo(calculoFreteNovo);
    }

    /**
     * Executa o cálculo do frete para Conhecimento Nacional (CTRC) do tipo <b>Complemento de ICMS</b>.
     */
    public void executeCalculoCTRComplementoICMS(CalculoFrete calculoFrete) {
        if (!ConstantesExpedicao.ABRANGENCIA_NACIONAL.equals(calculoFrete.getTpAbrangencia())
                || !ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_ICMS.equals(calculoFrete.getTpConhecimento())
                || !ConstantesExpedicao.CALCULO_MANUAL.equals(calculoFrete.getTpCalculo())
                ) {
            throw new BusinessException("parametrosInvalidos");
        }
//		executeCalculoFreteNacionalManual(calculoFrete, true);
        // LMS-3076
        setParametrosCalculo(calculoFrete);
        finalizaCalculo(calculoFrete);
    }

    /**
     * Executa o cálculo do frete para Conhecimento Nacional (CTRC) de <b>Segundo Percurso</b>.
     */
    public void executeCalculoCTRSegundoPercurso(CalculoFrete calculoFrete) {
        executeCalculoCTRNormal(calculoFrete);
    }

    /**
     * Executa o cálculo do frete para Conhecimento Nacional (CTRC) do tipo <b>Devolução</b>.
     */
    public void executeCalculoCTRDevolucao(CalculoFrete calculoFrete) {
        if (!ConstantesExpedicao.ABRANGENCIA_NACIONAL.equals(calculoFrete.getTpAbrangencia()) ||
                !ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO.equals(calculoFrete.getTpConhecimento())
                ) {
            throw new BusinessException("parametrosInvalidos");
        }
        if (ConstantesExpedicao.CALCULO_NORMAL.equals(calculoFrete.getTpCalculo())) {
            calculoFrete.setBlCalculaParcelas(Boolean.TRUE);
            executeCalculoFreteNacionalDevolucaoNormal(calculoFrete);
        } else if (ConstantesExpedicao.CALCULO_MANUAL.equals(calculoFrete.getTpCalculo())) {
            executeCalculoFreteNacionalDevolucaoManual(calculoFrete);
        } else if (ConstantesExpedicao.CALCULO_COTACAO.equals(calculoFrete.getTpCalculo())) {
            executeCalculoFreteNacionalDevolucaoCotacao(calculoFrete);
        } else if (ConstantesExpedicao.CALCULO_CORTESIA.equals(calculoFrete.getTpCalculo())) {
            executeCalculoFreteNacionalDevolucaoCortesia(calculoFrete);
        }
        finalizaCalculo(calculoFrete);
    }

    /**
     * Executa o cálculo do frete para Conhecimento Internacional (CRT) do tipo <b>Normal</b>.
     *
     * @param calculoFrete
     */
    public void executeCalculoCRTNormal(CalculoFrete calculoFrete) {
        if (!ConstantesExpedicao.ABRANGENCIA_INTERNACIONAL.equals(calculoFrete.getTpAbrangencia())
                || !ConstantesExpedicao.CONHECIMENTO_NORMAL.equals(calculoFrete.getTpConhecimento())
                || !ConstantesExpedicao.MODAL_RODOVIARIO.equals(calculoFrete.getTpModal())
                || !ConstantesExpedicao.CALCULO_NORMAL.equals(calculoFrete.getTpCalculo())
                ) {
            throw new BusinessException("parametrosInvalidos");
        }
        executeCalculoFreteRodoviarioInternacionalNormalNormal(calculoFrete);
    }

    /**
     * Executa o cálculo do frete para <b>Cotação</b>.
     *
     * @param calculoFrete
     */
    public void executeCalculoFreteCotacao(CalculoFrete calculoFrete) {
        if (calculoFrete.getBlRecalculoCotacao()) {
            executeRecalculoCotacao(calculoFrete);
            return;
        }
        executeCalculoCTRNormal(calculoFrete);
    }

    protected void finalizaCalculo(CalculoFrete calculoFrete) {
        //Calcula Impostos
        if (!(calculoFrete.isCalculoNotaTransporte()) || calculoFrete.getBlCotacao()) {
            getCalculoTributoService().calculaTributos(calculoFrete);
        } else {
            calculoFrete.setBlCalculaParcelas(true);
            //não calcula sempre pois no caso de nft normal, no momento da digitacao do pre-documento nao gera os impostos, apenas na pesagem
            if (calculoFrete.isCalculoNotaTransporte() && BooleanUtils.isTrue(calculoFrete.getBlCalculaImpostoServico())) {
                getCalculoTributoService().executeCalculoTributacaoNFT(calculoFrete);
            }
            executeCalculoSubTotal(calculoFrete);
        }
        //Ordena parcelas e ajusta valores
        getCalculoParcelaServicoService().finalizaCalculoParcelas(calculoFrete);
    }

    /**
     * LMS-3635
     *
     * @param calculoFrete
     * @return
     */
    public void executaCalculoFreteNacionalRefaturamentoNormal(CalculoFrete calculoFrete) {
        BigDecimal pcRefaturamento = null;
        BigDecimal vlRefaturamento = null;
        Boolean blImposto = null;
        boolean verificarCalculoDifal = false;
        CalcularIcmsParam calcularIcmsParam = null;

        //1
        Conhecimento doctoServico = calculoFrete.getDoctoServico();
        Conhecimento doctoServicoOriginal = conhecimentoService.findById(doctoServico.getDoctoServicoOriginal()
                .getIdDoctoServico());
        calculoFrete.setIdDoctoServico(doctoServico.getIdDoctoServico());
        findCalculoFrete(calculoFrete);
        setParametrosCalculoCommon(calculoFrete);

        //2
        TabelaPreco tabelaReferencial = findTabelaPrecoMercurio(calculoFrete);
        Long idTabelaReferencial = tabelaReferencial.getIdTabelaPreco();
        Generalidade generalidadeRefaturamentoTabelaReferencial = generalidadeService.findPrecificacaoGeneralidadeRefaturamento(idTabelaReferencial);
        //LMS-7198
        if (null != generalidadeRefaturamentoTabelaReferencial) {
            pcRefaturamento = generalidadeRefaturamentoTabelaReferencial.getVlGeneralidade();
            vlRefaturamento = generalidadeRefaturamentoTabelaReferencial.getVlMinimo();
            blImposto = true;

        } else {
            // 3
            TabelaPreco tabelaOriginal = doctoServicoOriginal.getTabelaPreco();
            if (tabelaOriginal != null) {
                Long idTabelaPrecoDoctoServico = tabelaOriginal.getIdTabelaPreco();
                if (!idTabelaReferencial.equals(idTabelaPrecoDoctoServico)) {
                    Generalidade generalidadeRefaturamentoDoctoServico = generalidadeService.findPrecificacaoGeneralidadeRefaturamento(idTabelaPrecoDoctoServico);
                    if (generalidadeRefaturamentoDoctoServico != null) {
                        vlRefaturamento = generalidadeRefaturamentoDoctoServico.getVlMinimo();
                        pcRefaturamento = generalidadeRefaturamentoDoctoServico.getVlGeneralidade();
                        blImposto = true;
                    }
                }
            }
        }

        // 4
        ParametroCliente parametroClienteOriginal = doctoServicoOriginal.getParametroCliente();
        if (parametroClienteOriginal != null) {
            if (parametroClienteOriginal.getGeneralidadeClientes() != null) {
                GeneralidadeCliente generalidadeCliente = getGenClienteIdRefaturamento(parametroClienteOriginal.getGeneralidadeClientes());
                if (generalidadeCliente != null) {
                    TabelaDivisaoCliente tdc = tabelaDivisaoClienteService.findTabelaDivisaoCliente(calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico());
                    vlRefaturamento = calcularMinimoParcela(generalidadeCliente, vlRefaturamento);
                    pcRefaturamento = calcularPercentualTabela(generalidadeCliente, pcRefaturamento);
                    if (tdc != null) {
                        blImposto = tdc.getBlImpBaseRefaturamento();
                    }
                } else {
                    blImposto = getImpostoBaseRefaturamento(parametroClienteOriginal);
                }
            } else {
                blImposto = getImpostoBaseRefaturamento(parametroClienteOriginal);
            }
        } else {
            TabelaDivisaoCliente tdc = tabelaDivisaoClienteService.findTabelaDivisaoCliente(calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico());
            if (tdc != null) {
                blImposto = tdc.getBlImpBaseRefaturamento();
            }
        }

        // 5
        BigDecimal vlParcelaIDTransferencia = getValorParcelaIDTransferencia((Conhecimento) doctoServicoOriginal);
        BigDecimal valorTotalParcelas = doctoServicoOriginal.getVlTotalParcelas();
        BigDecimal vlImpostoOriginal = doctoServicoOriginal.getVlImposto();
        BigDecimal vlRefaturamentoCalculado = calculaVlRefaturamento(vlImpostoOriginal, pcRefaturamento, blImposto,
                vlParcelaIDTransferencia, valorTotalParcelas);
        if (CompareUtils.gt(vlRefaturamento, vlRefaturamentoCalculado)) {
            vlRefaturamentoCalculado = vlRefaturamento;
        }
        calculoFrete.addParcelaGeral(getCalculoParcelaFreteService().findParcela(vlRefaturamentoCalculado,
                ConstantesExpedicao.CD_REFATURAMENTO));

        BigDecimal vlImposto = BigDecimalUtils.ZERO;
        BigDecimal vlTotal = BigDecimalUtils.ZERO;
        //6
        String tpDoctoServico = doctoServico.getTpDoctoServico().getValue();
        if (ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(tpDoctoServico)
                || ConstantesExpedicao.CONHECIMENTO_NACIONAL.equals(tpDoctoServico)) {

            calcularIcmsParam = calcularIcmsService.calcularIcms(calculoFrete);
            vlImposto = calculoFrete.getTributo().getVlImposto();
            vlTotal = vlRefaturamentoCalculado.add(vlImposto);
            calculoFrete.setVlDevido(vlTotal);
            verificarCalculoDifal = true;

        } else if (ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE.equals(tpDoctoServico)
                || ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDoctoServico)) {
            removeImpostoServicoByConhecimento(doctoServico);
            if (calculoFrete instanceof CalculoNFT) {
                CalculoNFT cnft = (CalculoNFT) calculoFrete;
                getCalculoTributoService().executeCalculoTributacaoNFT(cnft);
                vlImposto = getVlImpostoServicoISS(cnft.getTributos());
                vlTotal = vlRefaturamentoCalculado.add(vlImposto);
                calculoFrete.setVlDevido(vlRefaturamentoCalculado);
            }
        }

        //7

        BigDecimal psReferenciaCalculo = doctoServicoOriginal.getPsReferenciaCalculo();
        calculoFrete.setVlTotalParcelas(vlTotal);
        calculoFrete.setVlTotalTributos(vlImposto);
        calculoFrete.setVlTotal(vlTotal);
        calculoFrete.setPsReferencia(psReferenciaCalculo);
        doctoServico.setVlTotalParcelas(vlTotal);
        doctoServico.setVlImposto(calculoFrete.getTributo().getVlImposto());
        doctoServico.setVlTotalDocServico(vlTotal);
        doctoServico.setPsReferenciaCalculo(psReferenciaCalculo);
        doctoServico.setBlDesconsiderouPesoCubado(doctoServicoOriginal.getBlDesconsiderouPesoCubado());
        conhecimentoService.store(doctoServico);

        if(verificarCalculoDifal) {

            if(this.calcularDifalService.verificaIsCalculoDifal(calcularIcmsParam)) {

                Long idUfDestino = calcularIcmsParam.getIdUfDestino();

                AliquotaIcms aliquotaIcmsInterna = this.calcularDifalService.aliquotaIcmsInterna(idUfDestino,
                        calcularIcmsParam.getTpSituacaoTributariaRemetente(),calcularIcmsParam.getTpSituacaoTributariaDestinatario(),
                        calcularIcmsParam.getTpFrete(), calcularIcmsParam.getDtEmissao());

                CalcularIDifalParam calcularIDifalParam = this.calcularDifalService.calcularDifal(calcularIcmsParam,
                        aliquotaIcmsInterna.getPcAliquota(), aliquotaIcmsInterna.getPcEmbute());

                calcularIcmsParam.getCalculoFrete().getDoctoServico().setVlImpostoDifal(calcularIDifalParam.getPcImpostoDifial());
                calcularIcmsParam.getCalculoFrete().getDoctoServico().setPcIcmsUfFim(calcularIDifalParam.getPcIcmsUfFim());

                if(Boolean.FALSE.equals(calculoFrete.getBlCotacao())) {
                    BigDecimal cdOcorrenciaBloqueio = (BigDecimal) parametroGeralService.
                            findConteudoByNomeParametroWithoutException(ConstantesExpedicao.NM_PARAMETRO_CD_OCORRENCIA_BLOQ_DIFAL, false);
                    OcorrenciaPendencia ocorrencia = ocorrenciaPendenciaService.findByCodigoOcorrencia(Short.valueOf(cdOcorrenciaBloqueio.toString()));
                    ocorrenciaDoctoServicoService.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(calcularIcmsParam.getCalculoFrete().getDoctoServico().getIdDoctoServico(),
                            ocorrencia.getIdOcorrenciaPendencia(), null, JTDateTimeUtils.getDataHoraAtual(), null);
                }

            }
        }
    }

    private boolean isClienteTelefonicaCalculoFrete(CalculoFrete calculoFrete){

        TabelaDivisaoCliente tdc = tabelaDivisaoClienteService .findTabelaDivisaoCliente(calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico());
        ParametroGeral parametroDivisao = parametroGeralService.findByNomeParametro("DIVISAO_CONV_CAIXA_TELEFONICA");
        ParametroGeral parametroCliente = parametroGeralService.findByNomeParametro("CLIENTE_CONV_CAIXA_TELEFONICA");
        if(parametroDivisao == null){
            throw new BusinessException("Parametro DIVISAO_CONV_CAIXA_TELEFONICA não cadastrado");
        }
        if(parametroCliente == null){
            throw new BusinessException("Parametro CLIENTE_CONV_CAIXA_TELEFONICA não cadastrado");
        }
        Long idCliente = calculoFrete.getDadosCliente().getIdClienteRemetente();
        Long idDivisao = tdc.getDivisaoCliente().getCdDivisaoCliente();

        List<String> listaClientes = Arrays.asList(parametroCliente.getDsConteudo().trim().split(";"));
        List<String> listaDivisao = Arrays.asList(parametroDivisao.getDsConteudo().trim().split(";"));

        return listaClientes.contains(idCliente.toString()) && listaDivisao.contains(idDivisao.toString());
    }

    private BigDecimal getVlImpostoServicoISS(List<ImpostoServico> tributos) {
        BigDecimal vlimposto = BigDecimalUtils.ZERO;
        for (ImpostoServico imposto : tributos) {
            if (imposto.getBlRetencaoTomadorServico()) {
                vlimposto = vlimposto.add(imposto.getVlImposto());
            }
        }
        return vlimposto;
    }

    private void removeImpostoServicoByConhecimento(Conhecimento conhecimento) {
        if (conhecimento.getImpostoServicos() != null && conhecimento.getImpostoServicos().size() > 0) {
            List<Long> idsImpostoServico = new ArrayList<Long>();

            for (ImpostoServico is : conhecimento.getImpostoServicos()) {
                idsImpostoServico.add(is.getIdImpostoServico());
            }
            getImpostoServicoService().removeByIds(idsImpostoServico);
        }
    }

    private boolean getImpostoBaseRefaturamento(ParametroCliente parametroClienteOriginal) {
        TabelaDivisaoCliente tabelaDivisaoCliente = parametroClienteOriginal.getTabelaDivisaoCliente();
        if (tabelaDivisaoCliente != null) {
            return tabelaDivisaoCliente.getBlImpBaseRefaturamento();
        }
        return true;
    }

    private BigDecimal calculaVlRefaturamento(BigDecimal vlImposto, BigDecimal pcRefaturamento, Boolean blImposto,
                                              BigDecimal vlTransferencia, BigDecimal valorTotalParcelas) {
        BigDecimal vlRefaturamentoCalculado = valorTotalParcelas.subtract(vlTransferencia);
        if (BooleanUtils.isFalse(blImposto)) {
            vlRefaturamentoCalculado = valorTotalParcelas.subtract(vlImposto);
        }
        vlRefaturamentoCalculado = vlRefaturamentoCalculado.multiply(pcRefaturamento).divide(BigDecimalUtils.HUNDRED);
        return vlRefaturamentoCalculado;
    }

    /**
     * LMS-3635 Se para o documento original existe parcela de ‘IDTransferencia’
     * então devolve valor da parcela
     *
     * @param doctoServico
     * @return ParcelaDoctoServico.valorParcela
     */
    private BigDecimal getValorParcelaIDTransferencia(Conhecimento doctoServico) {
        if (doctoServico.getParcelaDoctoServicos() != null) {
            for (ParcelaDoctoServico parcela : doctoServico.getParcelaDoctoServicos()) {
                if (ConstantesExpedicao.CD_TRANSFERENCIA.equals(parcela.getParcelaPreco().getCdParcelaPreco())) {
                    return parcela.getVlParcela();
                }
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * LMS-3635
     * Calcular o mínimo para a parcela
     *
     * @param generalidadeCliente
     * @param vlRefaturamento
     * @return
     */
    private BigDecimal calcularMinimoParcela(GeneralidadeCliente generalidadeCliente, BigDecimal vlRefaturamento) {
        String tpIndicadorMinimo = generalidadeCliente.getTpIndicadorMinimo().getValue();
        if (ConstantesExpedicao.TP_INDICADOR_POR_VALOR.equals(tpIndicadorMinimo)) {
            vlRefaturamento = generalidadeCliente.getVlMinimo();
        } else if (ConstantesExpedicao.TP_INDICADOR_POR_ACRESCIMO.equals(tpIndicadorMinimo)) {
            vlRefaturamento = BigDecimalUtils.acrescimo(vlRefaturamento, generalidadeCliente.getVlMinimo());
        } else if (ConstantesExpedicao.TP_INDICADOR_POR_DESCONTO.equals(tpIndicadorMinimo)) {
            vlRefaturamento = BigDecimalUtils.desconto(vlRefaturamento, generalidadeCliente.getVlMinimo());
        }
        return vlRefaturamento;
    }

    /**
     * LMS-3635
     * Calcular o percentual para a tabela
     *
     * @param generalidadeCliente
     * @param pcRefaturamento
     * @return
     */
    private BigDecimal calcularPercentualTabela(GeneralidadeCliente generalidadeCliente, BigDecimal pcRefaturamento) {
        String tpIndicador = generalidadeCliente.getTpIndicador().getValue();
        if (ConstantesExpedicao.TP_INDICADOR_POR_VALOR.equals(tpIndicador)) {
            pcRefaturamento = generalidadeCliente.getVlGeneralidade();
        } else if (ConstantesExpedicao.TP_INDICADOR_POR_ACRESCIMO.equals(tpIndicador)) {
            pcRefaturamento = BigDecimalUtils.acrescimo(pcRefaturamento, generalidadeCliente.getVlGeneralidade());
        } else if (ConstantesExpedicao.TP_INDICADOR_POR_DESCONTO.equals(tpIndicador)) {
            pcRefaturamento = BigDecimalUtils.desconto(pcRefaturamento, generalidadeCliente.getVlGeneralidade());
        }
        return pcRefaturamento;
    }

    //LMS-3635
    private GeneralidadeCliente getGenClienteIdRefaturamento(List generalidadeClientes) {
        for (Object object : generalidadeClientes) {
            GeneralidadeCliente genCliente = (GeneralidadeCliente) object;
            if (ConstantesExpedicao.CD_REFATURAMENTO.equals(genCliente.getParcelaPreco().getCdParcelaPreco())) {
                return genCliente;
            }
        }
        return null;
    }

    public CalculoFreteDAO getCalculoFreteDAO() {
        return (CalculoFreteDAO) super.getCalculoServicoDAO();
    }

    public void setCalculoFreteDAO(CalculoFreteDAO calculoFreteDAO) {
        super.setCalculoServicoDAO(calculoFreteDAO);
    }

    public CalculoParcelaFreteService getCalculoParcelaFreteService() {
        return (CalculoParcelaFreteService) super.getCalculoParcelaServicoService();
    }

    public void setCalculoParcelaFreteService(CalculoParcelaFreteService calculoParcelaFreteService) {
        super.setCalculoParcelaServicoService(calculoParcelaFreteService);
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setPpeService(PpeService ppeService) {
        this.ppeService = ppeService;
    }

    public void setConhecimentoService(ConhecimentoService conhecimentoService) {
        this.conhecimentoService = conhecimentoService;
    }

    public void setMcdService(McdService mcdService) {
        this.mcdService = mcdService;
    }

    public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
        this.municipioFilialService = municipioFilialService;
    }

    public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
        this.inscricaoEstadualService = inscricaoEstadualService;
    }

    public void setInformacaoDocServicoService(InformacaoDocServicoService informacaoDocServicoService) {
        this.informacaoDocServicoService = informacaoDocServicoService;
    }

    public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
        this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
    }

    public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
        this.enderecoPessoaService = enderecoPessoaService;
    }

    public VolumeNotaFiscalService getVolumeNotaFiscalService() {
        return volumeNotaFiscalService;
    }

    public void setVolumeNotaFiscalService(
            VolumeNotaFiscalService volumeNotaFiscalService) {
        this.volumeNotaFiscalService = volumeNotaFiscalService;
    }

    public TipoTributacaoIEService getTipoTributacaoIEService() {
        return tipoTributacaoIEService;
    }

    public void setTipoTributacaoIEService(
            TipoTributacaoIEService tipoTributacaoIEService) {
        this.tipoTributacaoIEService = tipoTributacaoIEService;
    }

    public RestricaoRotaService getRestricaoRotaService() {
        return restricaoRotaService;
    }

    public void setRestricaoRotaService(RestricaoRotaService restricaoRotaService) {
        this.restricaoRotaService = restricaoRotaService;
    }

    public TipoLocalizacaoMunicipioService getTipoLocalizacaoMunicipioService() {
        return tipoLocalizacaoMunicipioService;
    }

    public void setTipoLocalizacaoMunicipioService(
            TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService) {
        this.tipoLocalizacaoMunicipioService = tipoLocalizacaoMunicipioService;
    }

    public void setMunicipioDestinoCalculoService(MunicipioDestinoCalculoService municipioDestinoCalculoService) {
        this.municipioDestinoCalculoService = municipioDestinoCalculoService;
    }

    /**
     * @param filialService the filialService to set
     */
    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    public void setFatorCubagemDivisaoService(FatorCubagemDivisaoService fatorCubagemDivisaoService) {
        this.fatorCubagemDivisaoService = fatorCubagemDivisaoService;
    }

    public void setGeneralidadeService(GeneralidadeService generalidadeService) {
        this.generalidadeService = generalidadeService;
    }

    public void setCalcularIcmsService(CalcularIcmsService calcularIcmsService) {
        this.calcularIcmsService = calcularIcmsService;
    }

    public ImpostoServicoService getImpostoServicoService() {
        return impostoServicoService;
    }

    public void setImpostoServicoService(ImpostoServicoService impostoServicoService) {
        this.impostoServicoService = impostoServicoService;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void setCalcularDifalService(CalcularDifalService calcularDifalService) {
        this.calcularDifalService = calcularDifalService;
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
        this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
    }

    public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
        this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
    }

    public void setInformacaoDoctoClienteService(
            InformacaoDoctoClienteService informacaoDoctoClienteService) {
        this.informacaoDoctoClienteService = informacaoDoctoClienteService;
    }
}
