package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import com.mercurio.adsm.core.cache.RecursoMensagemCache;
import com.mercurio.lms.expedicao.dto.ConhecimentoDto;
import com.mercurio.lms.expedicao.dto.NotaFiscalConhecimentoDto;
import com.mercurio.lms.expedicao.dto.StoreLogEdiDto;
import com.mercurio.lms.expedicao.dto.VolumeNotaFiscalDto;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.vendas.dto.ClienteDTO;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.edi.model.service.CCEItemService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.WarningCollectorUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.expedicao.digitarDadosNotaNormalNotasFiscaisService"
 */
public class DigitarDadosNotaNormalNotasFiscaisService {

    private static final String KEY_MAP_DS_SERIE = "dsSerie";
	private ConhecimentoNormalService conhecimentoNormalService;
    private VolumeNotaFiscalService volumeNotaFiscalService;
    private DoctoServicoService doctoServicoService;

    // 6160
    private ParametroGeralService parametroGeralService;
    private ConteudoParametroFilialService conteudoParametroFilialService;
    private ClienteService clienteService;
    private CCEItemService cceItemService;
    private IntegracaoJmsService integracaoJmsService;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void storeNotas(Conhecimento conhecimento, List<NotaFiscalConhecimentoDto> notasParameters,
            Cliente cliente,String tpCalculoPreco, Boolean validaLimiteValorMercadoria,
                          Long idFilial, Long idProcessamentoEdi) {
        BigDecimal valorMercadoria = BigDecimalUtils.ZERO;
        BigDecimal psRealTotal = BigDecimalUtils.ZERO;
        BigDecimal psCubadoTotal = BigDecimalUtils.ZERO;
        Integer nrSequencia = IntegerUtils.ZERO;
        Integer qtVolumesTotal = IntegerUtils.ZERO;

        final List notasFiscaisConhecimentos = new ArrayList(notasParameters.size());
        final YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();

        for (NotaFiscalConhecimentoDto notaFiscal : notasParameters) {
            NotaFiscalConhecimento notaFiscalConhecimento = new NotaFiscalConhecimento();
            notaFiscalConhecimento.setNrNotaFiscal(notaFiscal.getNrNotaFiscal());
            notaFiscalConhecimento.setDsSerie(notaFiscal.getDsSerie());
            notaFiscalConhecimento.setDtEmissao(new YearMonthDay(notaFiscal.getDtEmissao()));

            /** Valida Data de Emissão */
            if (JTDateTimeUtils.comparaData(notaFiscalConhecimento.getDtEmissao(), dtAtual) > 0) {
                BusinessException businessException = new BusinessException("LMS-04019",new Object[]{notaFiscalConhecimento.getNrNotaFiscal()});
                String mensage = RecursoMensagemCache.getMessage(businessException.getMessageKey(), LocaleContextHolder.getLocale().toString());
                StoreLogEdiDto storeLogEdiDto = new StoreLogEdiDto();
                storeLogEdiDto.setNrNotaFiscal(notaFiscalConhecimento.getNrNotaFiscal().longValue());
                storeLogEdiDto.setMensagem(mensage);
                storeLogEdiDto.setIdProcessamentoEdi(idProcessamentoEdi);
                IntegracaoJmsService.JmsMessageSender jmsMessageSender = integracaoJmsService
                        .createMessage(Queues.STORE_LOG_EDI, storeLogEdiDto);
                integracaoJmsService.storeMessage(jmsMessageSender);
                continue;
            }
            notaFiscalConhecimento.setNrChave(notaFiscal.getNrChave());

            povoarValoreNota
                (   notaFiscalConhecimento, psCubadoTotal, notaFiscal.getNrCfop(), notaFiscal.getPsCubado(),
                    notaFiscal.getPsCubadoNotfis(), notaFiscal.getPsAferido(), notaFiscal.getVlTotalProdutos(),
                    notaFiscal.getVlBaseCalculoSt(), notaFiscal.getVlIcmsSt(), notaFiscal.getNrPinSuframa());

            notaFiscalConhecimento.setTpDocumento(notaFiscal.getTpDocumento());
            notaFiscalConhecimento.setVlTotal(notaFiscal.getVlTotal());
            notaFiscalConhecimento.setQtVolumes(notaFiscal.getQtVolumes());
            qtVolumesTotal += notaFiscalConhecimento.getQtVolumes();
            notaFiscalConhecimento.setPsMercadoria(notaFiscal.getPsMercadoria());
            notaFiscalConhecimento.setVlIcms(notaFiscal.getVlIcms());
            notaFiscalConhecimento.setVlBaseCalculo(notaFiscal.getVlBaseCalculo());

            // Valores utilizados para Paletização realizada no PA
            Boolean isNotaPaletizada = this.isNotaPaletizadaPA(conhecimento, notaFiscalConhecimento.getNrChave());
            int qtdVolumesNota = notaFiscalConhecimento.getQtVolumes();


            List<VolumeNotaFiscalDto> volumeNotaFiscaisParameters = notaFiscal.getVolumeNotaFiscal();
            List volumesNotasFiscais = new ArrayList();

            Boolean isDanfeSimplificada = cliente != null ? clienteService.validateIsDanfeSimplificada(Long.parseLong(cliente.getPessoa().getNrIdentificacao())) : false;

            if (volumeNotaFiscaisParameters != null && volumeNotaFiscaisParameters.size() > 0) {
                if (isDanfeSimplificada && qtdVolumesNota > 1) {
                    for (int i = 1; i < qtdVolumesNota; i++) {
                        volumeNotaFiscaisParameters.add(volumeNotaFiscaisParameters.get(0));
                    }
                }

                for (int j = 0; j < volumeNotaFiscaisParameters.size(); j++) {
                    VolumeNotaFiscalDto volumeNotaFiscalDto = volumeNotaFiscaisParameters.get(j);
                    if (volumeNotaFiscalDto.getNrVolumeColeta() != null) {
                        VolumeNotaFiscal volumeNotaFiscal = new VolumeNotaFiscal();
                        volumeNotaFiscal.setNotaFiscalConhecimento(notaFiscalConhecimento);
                        volumeNotaFiscal.setNrSequencia(++nrSequencia);
                        volumeNotaFiscal.setNrSequenciaPalete(isDanfeSimplificada ? 1 : nrSequencia);
                        volumeNotaFiscal.setNrVolumeColeta(volumeNotaFiscalDto.getNrVolumeColeta());
                        if (volumeNotaFiscalDto.getCdBarraPostoAvancado() != null) {
                            volumeNotaFiscal.setNrVolumeEmbarque(volumeNotaFiscalDto.getCdBarraPostoAvancado());
                        }
                        if (BooleanUtils.isTrue(conhecimento.getBlPaletizacao())) {
                            if (isNotaPaletizada) {
                                int qtdVolPallet = this.obtemQdeVolumePalete(qtdVolumesNota, j, volumeNotaFiscaisParameters.size());
                                volumeNotaFiscal.setQtVolumes(qtdVolPallet);
                                qtdVolumesNota = qtdVolumesNota - qtdVolPallet;
                            } else {
                                volumeNotaFiscal.setQtVolumes(0);
                            }

                            volumeNotaFiscal.setTpVolume(ConstantesExpedicao.TP_VOLUME_MESTRE);
                        } else {
                            volumeNotaFiscal.setQtVolumes(1);
                            volumeNotaFiscal.setTpVolume(ConstantesExpedicao.TP_VOLUME_UNITARIO);
                        }

                        volumesNotasFiscais.add(volumeNotaFiscal);
                    }
                }

            }
            notaFiscalConhecimento.setVolumeNotaFiscais(volumesNotasFiscais);

            // Totalizadores
            valorMercadoria = valorMercadoria.add(notaFiscalConhecimento.getVlTotal());
            if (notaFiscalConhecimento.getPsMercadoria() != null) {
                psRealTotal = psRealTotal.add(notaFiscalConhecimento.getPsMercadoria());
            }

            notaFiscalConhecimento.setBlProdutoPerigoso(notaFiscal.getBlProdutoPerigoso());
            notaFiscalConhecimento.setBlControladoPoliciaCivil(notaFiscal.getBlControladoPoliciaCivil());
            notaFiscalConhecimento.setBlControladoPoliciaFederal(notaFiscal.getBlControladoPoliciaFederal());
            notaFiscalConhecimento.setBlControladoExercito((Boolean) notaFiscal.getBlControladoExercito());


            notasFiscaisConhecimentos.add(notaFiscalConhecimento);
        }

        conhecimento.setQtPaletes(BooleanUtils.isTrue(conhecimento.getBlPaletizacao()) ? nrSequencia : 0);
        conhecimento.setPsReal(psRealTotal);
        conhecimento.setVlMercadoria(valorMercadoria);
        conhecimento.setQtVolumes(qtVolumesTotal);
        conhecimento.setNotaFiscalConhecimentos(notasFiscaisConhecimentos);

        conhecimento.setTpCalculoPreco(new DomainValue(tpCalculoPreco));
        conhecimentoNormalService.validateLimitePesoFreteCortesia(conhecimento);

        BigDecimal vlMercadoria = conhecimento.getNotaFiscalConhecimentos().stream().map(nfc -> nfc.getVlTotal())
               .reduce(BigDecimal.ZERO, BigDecimal::add);


        TypedFlatMap map = new TypedFlatMap();
        map.put("vlMercadoria", vlMercadoria);

        // LMS-4068 / Não deve validar limete de valor para conhecimento substituto.
        if (BooleanUtils.isTrue(validaLimiteValorMercadoria)) {
            doctoServicoService.executeValidacaoLimiteValorMercadoria(map, idFilial);
        }
    }

    public Map storeNotas(Map parameters) {
        BigDecimal valorMercadoria = BigDecimalUtils.ZERO;
        BigDecimal psRealTotal = BigDecimalUtils.ZERO;
        BigDecimal psCubadoTotal = BigDecimalUtils.ZERO;
        Integer nrSequencia = IntegerUtils.ZERO;
        Integer qtVolumesTotal = IntegerUtils.ZERO;
        
        final Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
        
        final List notasParameters = (List) parameters.get("notaFiscalConhecimento");
        final List notasFiscaisConhecimentos = new ArrayList(notasParameters.size());
        final YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();

        Cliente cliente = (Cliente) parameters.get("clienteRemetente");
        Boolean isDanfeSimplificada = cliente != null ? clienteService.validateIsDanfeSimplificada(Long.parseLong(cliente.getPessoa().getNrIdentificacao())) : false;

        /** Valida Data de Emissão */
        for (Object notasParameter : notasParameters) {
            Map notaFiscal = (Map) notasParameter;
            NotaFiscalConhecimento notaFiscalConhecimento = new NotaFiscalConhecimento();
            notaFiscalConhecimento.setNrNotaFiscal((Integer) notaFiscal.get("nrNotaFiscal"));
            String dsSerie = notaFiscal.get(KEY_MAP_DS_SERIE) == null ? null : notaFiscal.get(KEY_MAP_DS_SERIE).toString().trim();
            notaFiscalConhecimento.setDsSerie(dsSerie);
            notaFiscalConhecimento.setDtEmissao((YearMonthDay) notaFiscal.get("dtEmissao"));

            /** Valida Data de Emissão */
            if (JTDateTimeUtils.comparaData(notaFiscalConhecimento.getDtEmissao(), dtAtual) > 0) {
                throw new BusinessException("LMS-04019", new Object[]{notaFiscalConhecimento.getNrNotaFiscal()});
            }
            notaFiscalConhecimento.setNrChave((String) notaFiscal.get("nrChave"));

            Integer nrCfop = (Integer) notaFiscal.get("nrCfop");
            BigDecimal psCubado = (BigDecimal) notaFiscal.get("psCubado");
            // LMS-4940
            BigDecimal psCubadoNotfis = (BigDecimal) notaFiscal.get("psCubadoNotfis");
            BigDecimal psAferido = (BigDecimal) notaFiscal.get("psAferido");
            BigDecimal vlTotalProdutos = (BigDecimal) notaFiscal.get("vlTotalProdutos");
            BigDecimal vlBaseCalculoSt = (BigDecimal) notaFiscal.get("vlBaseCalculoSt");
            BigDecimal vlIcmsSt = (BigDecimal) notaFiscal.get("vlIcmsSt");
            Integer nrPinSuframa = (Integer) notaFiscal.get("nrPinSuframa");

            povoarValoreNota
                    (   notaFiscalConhecimento, psCubadoTotal, nrCfop, psCubado, psCubadoNotfis, psAferido,
                            vlTotalProdutos, vlBaseCalculoSt, vlIcmsSt, nrPinSuframa);

            notaFiscalConhecimento.setTpDocumento((String) notaFiscal.get("tpDocumento"));
            notaFiscalConhecimento.setVlTotal((BigDecimal) notaFiscal.get("vlTotal"));
            notaFiscalConhecimento.setQtVolumes((Short) notaFiscal.get("qtVolumes"));
            qtVolumesTotal += notaFiscalConhecimento.getQtVolumes();
            notaFiscalConhecimento.setPsMercadoria((BigDecimal) notaFiscal.get("psMercadoria"));
            notaFiscalConhecimento.setVlIcms((BigDecimal) notaFiscal.get("vlIcms"));
            notaFiscalConhecimento.setVlBaseCalculo((BigDecimal) notaFiscal.get("vlBaseCalculo"));

            // Valores utilizados para Paletização realizada no PA
            Boolean isNotaPaletizada = this.isNotaPaletizadaPA(conhecimento, notaFiscalConhecimento.getNrChave());
            int qtdVolumesNota = notaFiscalConhecimento.getQtVolumes();

            //se for cliente DanfeSimplificada, recupera o volume e salva novamente
            // Volume Nota Fiscais


            List volumeNotaFiscaisParameters = (List) notaFiscal.get("volumeNotaFiscal");
            List volumesNotasFiscais = new ArrayList();


            if (volumeNotaFiscaisParameters != null && !volumeNotaFiscaisParameters.isEmpty()) {
                if (isDanfeSimplificada && qtdVolumesNota > 1) {
                    for (int i = 1; i < qtdVolumesNota; i++) {
                        volumeNotaFiscaisParameters.add(volumeNotaFiscaisParameters.get(0));
                    }
                }

                for (int j = 0; j < volumeNotaFiscaisParameters.size(); j++) {
                    Map volumeNotaFiscalMap = (Map) volumeNotaFiscaisParameters.get(j);
                    if (volumeNotaFiscalMap.get("nrVolumeColeta") != null) {
                        VolumeNotaFiscal volumeNotaFiscal = new VolumeNotaFiscal();
                        volumeNotaFiscal.setNotaFiscalConhecimento(notaFiscalConhecimento);
                        volumeNotaFiscal.setNrSequencia(++nrSequencia);
                        volumeNotaFiscal.setNrSequenciaPalete(isDanfeSimplificada ? 1 : nrSequencia);
                        volumeNotaFiscal.setNrVolumeColeta(volumeNotaFiscalMap.get("nrVolumeColeta").toString());
                        if (volumeNotaFiscalMap.get("cdBarraPostoAvancado") != null) {
                            volumeNotaFiscal.setNrVolumeEmbarque(volumeNotaFiscalMap.get("cdBarraPostoAvancado").toString());
                        }
                        if (BooleanUtils.isTrue(conhecimento.getBlPaletizacao())) {
                            if (isNotaPaletizada) {
                                int qtdVolPallet = this.obtemQdeVolumePalete(qtdVolumesNota, j, volumeNotaFiscaisParameters.size());
                                volumeNotaFiscal.setQtVolumes(qtdVolPallet);
                                qtdVolumesNota = qtdVolumesNota - qtdVolPallet;
                            } else {
                                volumeNotaFiscal.setQtVolumes(0);
                            }

                            volumeNotaFiscal.setTpVolume(ConstantesExpedicao.TP_VOLUME_MESTRE);
                        } else {
                            volumeNotaFiscal.setQtVolumes(1);
                            volumeNotaFiscal.setTpVolume(ConstantesExpedicao.TP_VOLUME_UNITARIO);
                        }

                        volumesNotasFiscais.add(volumeNotaFiscal);
                    }
                }

            }
            notaFiscalConhecimento.setVolumeNotaFiscais(volumesNotasFiscais);

            // Totalizadores
            valorMercadoria = valorMercadoria.add(notaFiscalConhecimento.getVlTotal());
            if (notaFiscalConhecimento.getPsMercadoria() != null) {
                psRealTotal = psRealTotal.add(notaFiscalConhecimento.getPsMercadoria());
            }

            notaFiscalConhecimento.setBlProdutoPerigoso((Boolean) notaFiscal.get("blProdutoPerigoso"));
            notaFiscalConhecimento.setBlControladoPoliciaCivil((Boolean) notaFiscal.get("blControladoPoliciaCivil"));
            notaFiscalConhecimento.setBlControladoPoliciaFederal((Boolean) notaFiscal.get("blControladoPoliciaFederal"));
            notaFiscalConhecimento.setBlControladoExercito((Boolean) notaFiscal.get("blControladoExercito"));


            notasFiscaisConhecimentos.add(notaFiscalConhecimento);
        }

        conhecimento.setQtPaletes(BooleanUtils.isTrue(conhecimento.getBlPaletizacao()) ? nrSequencia : 0);
        conhecimento.setPsReal(psRealTotal);
        conhecimento.setVlMercadoria(valorMercadoria);
        conhecimento.setQtVolumes(qtVolumesTotal);
        conhecimento.setNotaFiscalConhecimentos(notasFiscaisConhecimentos);

        String tpCalculo = String.valueOf(parameters.get("tpCalculoPreco"));
        conhecimento.setTpCalculoPreco(new DomainValue(tpCalculo));
        conhecimentoNormalService.validateLimitePesoFreteCortesia(conhecimento);

        BigDecimal vlMercadoria = BigDecimal.ZERO;
        for (NotaFiscalConhecimento nfc : conhecimento.getNotaFiscalConhecimentos()) {
            vlMercadoria = vlMercadoria.add(nfc.getVlTotal());
        }

        TypedFlatMap map = new TypedFlatMap();
        map.put("vlMercadoria", vlMercadoria);

        // LMS-4068 / Não deve validar limete de valor para conhecimento substituto.
        if (parameters.get("validaLimiteValorMercadoria") == null || (Boolean) parameters.get("validaLimiteValorMercadoria")) {
            doctoServicoService.executeValidacaoLimiteValorMercadoria(map);
        }

        Map result = new LinkedHashMap<String, Object>();
        result.put("conhecimento", conhecimento);
        
        return result;
    }

    private void povoarValoreNota
        (NotaFiscalConhecimento notaFiscalConhecimento, BigDecimal psCubadoTotal, Integer nrCfop,  BigDecimal psCubado,
         BigDecimal psCubadoNotfis, BigDecimal psAferido, BigDecimal vlTotalProdutos, BigDecimal vlBaseCalculoSt,
         BigDecimal vlIcmsSt, Integer nrPinSuframa){

        if (nrCfop != null) {
            notaFiscalConhecimento.setNrCfop(new BigInteger(String.valueOf(nrCfop)));
        }

        if (psCubado != null) {
            notaFiscalConhecimento.setPsCubado(new BigDecimal(String.valueOf(psCubado)));
            // LMS-2353
            psCubadoTotal = psCubadoTotal.add(psCubado);
        }

        // LMS-4940
        if (psCubadoNotfis != null) {
            notaFiscalConhecimento.setPsCubadoNotfis(new BigDecimal(String.valueOf(psCubado)));
        }

        if (psAferido != null) {
            notaFiscalConhecimento.setPsAferido(new BigDecimal(String.valueOf(psAferido)));
        }

        if (vlTotalProdutos != null) {
            notaFiscalConhecimento.setVlTotalProdutos(new BigDecimal(String.valueOf(vlTotalProdutos)));
        }

        if (vlBaseCalculoSt != null) {
            notaFiscalConhecimento.setVlBaseCalculoSt(new BigDecimal(String.valueOf(vlBaseCalculoSt)));
        }

        if (vlIcmsSt != null) {
            notaFiscalConhecimento.setVlIcmsSt(new BigDecimal(String.valueOf(vlIcmsSt)));
        }

        if (nrPinSuframa != null) {
            notaFiscalConhecimento.setNrPinSuframa(nrPinSuframa);
        }
    }

    // Verifica se a Nota Fiscal foi Paletizada no Posto avançado.
    private Boolean isNotaPaletizadaPA(Conhecimento conhecimento, String nrChave) {
		return BooleanUtils.isTrue(conhecimento.getBlPaletizacao()) && cceItemService.findExistsNotasPaletizadas(Arrays.asList(nrChave));
	}
    
    private int obtemQdeVolumePalete(int qtdVolumetotal, int posicaPalet, int qtdPalet) {
    	if((posicaPalet +1) < qtdPalet) { 
    		qtdVolumetotal = qtdVolumetotal -2;
    		return 2;
    	}else {
    		return qtdVolumetotal;
    	}
    }

    /**
     * Valida Notas Fiscais Conhecimento
     * 
     * @param criteria
     * @return Warnings
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map validateNotasConhecimento(Map<String, Object> criteria) {
        List<Map<String, Object>> notasFiscais = (List<Map<String, Object>>) criteria.get("notasFiscais");
        List<Integer> nrNotasFiscais = new ArrayList<Integer>(notasFiscais.size());
        List<String> dsSeriesNotasFiscais = new ArrayList<String>(notasFiscais.size());
        List<YearMonthDay> datas = new ArrayList<YearMonthDay>(notasFiscais.size());
        List<String> tipoDocs = new ArrayList<String>();
        List<String> chaves = new ArrayList<String>(notasFiscais.size());

        WarningCollectorUtils.remove();

        for (Map<String, Object> notaFiscal : notasFiscais) {
            nrNotasFiscais.add((Integer) notaFiscal.get("nrNotaFiscal"));
            datas.add((YearMonthDay) notaFiscal.get("dtEmissao"));
            String dsSerie = notaFiscal.get(KEY_MAP_DS_SERIE) == null ? null : notaFiscal.get(KEY_MAP_DS_SERIE).toString().trim();
            dsSeriesNotasFiscais.add(dsSerie);
            tipoDocs.add((String) notaFiscal.get("tpDocumento"));
            chaves.add((String) notaFiscal.get("nrChave"));
        }

        volumeNotaFiscalService.validateIntervaloEtiquetasNaoUsado(notasFiscais);

        Long idClienteRemetente = (Long) criteria.get("idClienteRemetente");
        // LMSA-6598
        conhecimentoNormalService.verifyNotasConhecimento(idClienteRemetente, nrNotasFiscais, datas,
                dsSeriesNotasFiscais, chaves, tipoDocs, (Boolean) criteria.get("redespachoIntermediario") );
        conhecimentoNormalService.verifyMiscellaneousDocuments(notasFiscais);
        Map result = new HashMap();
        WarningCollectorUtils.putAll(result);
        return result;
    }
    
    /**
     * validacao chave NFe/CTe conforme valor de 'redespacho intermediario', onde
     * redespachoIntermediario = true então chave refere-se a um CTe, do contrario um NFe
     * LMSA-6160: 
     *   ** este metodo foi migrado da Action DigitarDadosNotaNormalNotasFiscaisAction por ser uma validacao de negocio 
     *      utilizado por outras Actions tambem e desta forma nao manter a mesma regra em mais de local no codigo  
     * @param redespachoIntermediario
     * @param nrChave
     * @param nrNotaFiscal
     * @param dtEmissao
     * @param idClienteRemetente
     */
    public void validateChaveNfe
    (boolean redespachoIntermediario, String nrChave, Integer nrNotaFiscal,
     YearMonthDay dtEmissao, Long idClienteRemetente, Long idFilial) {
        if (StringUtils.isNotEmpty(nrChave)) {
            // considera-se que aqui o valor de chave Nfe/Cte, ja foi validada quanto a tamanho e digito verificador
            String identificadorChaveDigitada = nrChave.substring(20,22);
            String codigoChaveCte = (String) parametroGeralService
                    .findConteudoByNomeParametro("IDENTIFICADOR_CHAVE_CTE", false);
            if (Boolean.TRUE.equals(redespachoIntermediario) && StringUtils.isNotEmpty(codigoChaveCte)) {
                if (!identificadorChaveDigitada.equals(codigoChaveCte)) {
                    throw new BusinessException("LMS-04400");
                }
            } else { 
                String codigoChaveNfe = (String) (String) parametroGeralService
                        .findConteudoByNomeParametro("IDENTIFICADOR_CHAVE_NFE", false);
                if (!Boolean.TRUE.equals(redespachoIntermediario) && StringUtils.isNotEmpty(codigoChaveNfe)) {
                    if (!identificadorChaveDigitada.equals(codigoChaveNfe)) {
                        throw new BusinessException("LMS-04400");
                    }
                }
            }
        }

        if (!redespachoIntermediario) {
            String validaChaveNFE = (String) conteudoParametroFilialService
                    .findConteudoByNomeParametro(idFilial, "VALIDA_CHAVE_NFE", false);
            if("S".equals(validaChaveNFE)){
                validateRemetenteChaveNfe(idClienteRemetente, nrChave);
                validateNrNotaFiscalChaveNfe(nrNotaFiscal, nrChave);
                validateDataEmissaoChaveNfe(dtEmissao, nrChave);
            }
        }
    }


    private void validateRemetenteChaveNfe(Long idClienteRemetente, String chaveNfe) {
        Cliente cliente = clienteService.findById(idClienteRemetente);
        if(chaveNfe == null || cliente == null || !cliente.getPessoa().getNrIdentificacao().equals(chaveNfe.substring(6, 20))){
            throw new BusinessException("LMS-04497");
        }
    }

    private void validateNrNotaFiscalChaveNfe(Integer nrNotaFiscal, String chaveNfe) {
        Integer nrNotaFiscalChave = Integer.valueOf(chaveNfe.substring(25, 34));
        if(nrNotaFiscal == null || !(nrNotaFiscal.equals(nrNotaFiscalChave))){
            throw new BusinessException("LMS-04562");
        }
    }
    
    private void validateDataEmissaoChaveNfe(YearMonthDay dtEmissao, String chaveNfe){
        String dtChaveNfe = chaveNfe.substring(2, 6);
        String formattedDtEmissao = JTDateTimeUtils.getFormattedYearMonth(dtEmissao);
        
        if(dtChaveNfe == null || formattedDtEmissao == null || !dtChaveNfe.equals(formattedDtEmissao)){
            throw new BusinessException("LMS-04563");
        }
    }
    

    public void setConhecimentoNormalService(ConhecimentoNormalService conhecimentoNormalService) {
        this.conhecimentoNormalService = conhecimentoNormalService;
    }

    public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
        this.volumeNotaFiscalService = volumeNotaFiscalService;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }
   
    // LMSA-6160
    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }
    
    public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
        this.conteudoParametroFilialService = conteudoParametroFilialService;
    }
    
    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    
	public void setCceItemService(CCEItemService cceItemService) {
		this.cceItemService = cceItemService;
}

    public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
        this.integracaoJmsService = integracaoJmsService;
}
}
