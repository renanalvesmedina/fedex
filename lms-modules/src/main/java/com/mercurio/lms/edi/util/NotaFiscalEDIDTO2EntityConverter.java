package com.mercurio.lms.edi.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import br.com.tntbrasil.integracao.domains.edi.ClienteWebServiceDto;
import br.com.tntbrasil.integracao.domains.edi.EnderecoWebServiceDto;
import br.com.tntbrasil.integracao.domains.edi.NotaFiscalComplementoWebServiceDto;
import br.com.tntbrasil.integracao.domains.edi.NotaFiscalVolumeWebServiceDto;
import br.com.tntbrasil.integracao.domains.edi.NotaFiscalWebServiceDto;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.NotaFiscalEdiComplemento;
import com.mercurio.lms.edi.model.NotaFiscalEdiVolume;
import com.mercurio.lms.edi.model.service.NotaFiscalEDIService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;

/**
 * Classe responsável por converter o DTO recebido pelo serviço REST para as Entities persistíveis.
 * 
 * @author MauroM
 *
 */
public class NotaFiscalEDIDTO2EntityConverter {

  private static final int CLIENTE_NOME_SIZE = 39;

private static final int CNPJ_SIZE = 14;

private NotaFiscalEDIService notaFiscalEDIService;

  private PessoaService pessoaService;

  private InformacaoDoctoClienteService informacaoDoctoClienteService;
  
  private ParametroGeralService parametroGeralService;

  public NotaFiscalEDIDTO2EntityConverter(NotaFiscalEDIService notaFiscalEDIService, PessoaService pessoaService,
      InformacaoDoctoClienteService informacaoDoctoClienteService, ParametroGeralService parametroGeralService) {
    this.notaFiscalEDIService = notaFiscalEDIService;
    this.pessoaService = pessoaService;
    this.informacaoDoctoClienteService = informacaoDoctoClienteService;
    this.parametroGeralService = parametroGeralService;
  }

  /**
   * Converte um {@link NotaFiscalWebServiceDto} em {@link NotaFiscalEdi}.
   */
  public NotaFiscalEdi convert(NotaFiscalWebServiceDto dto, boolean blNatura) {
    // converte a nota propriamente dita
    NotaFiscalEdi notaFiscalEdi = new NotaFiscalEdi();
    convertToNotaFiscalEDI(dto, notaFiscalEdi);

    // cria a lista de volumes com etiquetas
    List<NotaFiscalEdiVolume> listVolume = notaFiscalEdi.getVolumes();
    listVolume.addAll(buildListVolumes(notaFiscalEdi, dto));

    // cria a lista de complementos
    List<NotaFiscalEdiComplemento> listComplemento = notaFiscalEdi.getComplementos();
    listComplemento.addAll(buildListComplementos(dto, notaFiscalEdi, blNatura));

    return notaFiscalEdi;
  }
  

    public NotaFiscalEdi convert(NotaFiscalWebServiceDto dto) {
        NotaFiscalEdi notaFiscalEdi = new NotaFiscalEdi();
        convertToNotaFiscalEDI(dto, notaFiscalEdi);

        List<NotaFiscalEdiVolume> listVolume = notaFiscalEdi.getVolumes();
        listVolume.addAll(buildVolumes(notaFiscalEdi, dto));

        List<NotaFiscalEdiComplemento> listComplemento = notaFiscalEdi.getComplementos();
        listComplemento.addAll(buildComplementos(dto, notaFiscalEdi));
        return notaFiscalEdi;
    }

    private List<NotaFiscalEdiVolume> buildVolumes(NotaFiscalEdi notaFiscalEdi, NotaFiscalWebServiceDto dto) {
        List<NotaFiscalEdiVolume> listVolume = new ArrayList<NotaFiscalEdiVolume>();
        if (dto.getVolumes() != null && !dto.getVolumes().isEmpty()) {
            for (NotaFiscalVolumeWebServiceDto volumeDto : dto.getVolumes()) {
                NotaFiscalEdiVolume volume = new NotaFiscalEdiVolume();
                volume.setCodigoVolume(volumeDto.getCodigoVolume());
                volume.setNotaFiscalEdi(notaFiscalEdi);
                listVolume.add(volume);
            }
        }

        return listVolume;
    }

    /**
     * Busca no DTO da nota fiscal os complementos e a partir da descrição do
     * complemento busca o idInformacaoDoctoCliente para gravar o complemento de
     * forma adequada.
     * 
     * Deve substituir os antigos metodos "genericos" que possuem codigo
     * especifico para clientes.
     * 
     * @see buildListComplementos
     * 
     * @param dto
     * @param notaFiscalEdi
     * @return List
     */
    public List<NotaFiscalEdiComplemento> buildComplementos(NotaFiscalWebServiceDto dto, NotaFiscalEdi notaFiscalEdi) {
        List<NotaFiscalEdiComplemento> listComplementos = new ArrayList<NotaFiscalEdiComplemento>();
        if (dto.getComplementos() != null && !dto.getComplementos().isEmpty()) {
            String cnpj = FormatUtils.fillNumberWithZero(notaFiscalEdi.getCnpjReme().toString(), CNPJ_SIZE);
            Pessoa pessoa = pessoaService.findByNrIdentificacao(cnpj);
            for (NotaFiscalComplementoWebServiceDto complementoDto : dto.getComplementos()) {
                NotaFiscalEdiComplemento complemento = buildComplemento(pessoa.getIdPessoa(), notaFiscalEdi,
                        complementoDto.getDsCampo(), complementoDto.getVlCampo());
                if (complemento != null) {
                    listComplementos.add(complemento);
                }
            }
        }
        return listComplementos;
    }

  
/**
   * Cria a lista de complementos, específico para Claro chamando alguns métodos semigenéricos.
   * 
   * @param dto
   * @param notaFiscalEdi
   * @return
   */
  private List<NotaFiscalEdiComplemento> buildListComplementos(NotaFiscalWebServiceDto dto,
      NotaFiscalEdi notaFiscalEdi, boolean blNatura) {
    List<NotaFiscalEdiComplemento> listComplementos = new ArrayList<NotaFiscalEdiComplemento>();

    String cnpj = FormatUtils.fillNumberWithZero(notaFiscalEdi.getCnpjTomador().toString(), 14);
    Pessoa pessoa = pessoaService.findByNrIdentificacao(cnpj);

    Long idInfoDoctoCliente = findIdInformacaoDoctoClienteMatches(pessoa.getIdPessoa(), "Pedido");

    if (idInfoDoctoCliente != null) {
      NotaFiscalEdiComplemento complemento = new NotaFiscalEdiComplemento();

      complemento.setNotaFiscalEdi(notaFiscalEdi);
      complemento.setIndcIdInformacaoDoctoClien(idInfoDoctoCliente);
      complemento.setValorComplemento(dto.getNumeroPedido());

      listComplementos.add(complemento);
    }

	if (blNatura) {
		Long idPessoa = pessoa.getIdPessoa();
		
		CollectionUtils.addIgnoreNull(listComplementos,
				buildComplemento(idPessoa, notaFiscalEdi, "WEBSERVICE", "S"));
		CollectionUtils.addIgnoreNull(listComplementos,
				buildComplemento(idPessoa, notaFiscalEdi, "DT NATURA", String.valueOf(dto.getSequenciaAgrupamento())));
		CollectionUtils.addIgnoreNull(listComplementos,
				buildComplemento(idPessoa, notaFiscalEdi, "TRANSPORTADORA NATURA", dto.getCodTranspEmit()));
		CollectionUtils.addIgnoreNull(listComplementos,
                buildComplemento(idPessoa, notaFiscalEdi, "Quantidade de volumes de 13L", dto.getQuantidadeVolumes().toString()));
	}

    return listComplementos;
  }

  
  
  
	private NotaFiscalEdiComplemento buildComplemento(
			Long idPessoa, NotaFiscalEdi notaFiscalEdi, String dsCampo, String valorComplemento) {
		Long idInformacaoDoctoCliente = findIdInformacaoDoctoClienteMatches(idPessoa, dsCampo);
		if (idInformacaoDoctoCliente == null) {
			return null;
		}
		NotaFiscalEdiComplemento notaFiscalEdiComplemento = new NotaFiscalEdiComplemento();
		notaFiscalEdiComplemento.setNotaFiscalEdi(notaFiscalEdi);
		notaFiscalEdiComplemento.setIndcIdInformacaoDoctoClien(idInformacaoDoctoCliente);
		notaFiscalEdiComplemento.setValorComplemento(valorComplemento);
		return notaFiscalEdiComplemento;
	}

  private Long findIdInformacaoDoctoClienteMatches(Long idPessoa, String complementoDescrParte) {

    List<Map<String, Object>> lidc = informacaoDoctoClienteService.findDadosByCliente(idPessoa);

    Long idInfoComplemento = null;

    for (Map<String, Object> idc : lidc) {
      if (getDescFromInformacaoDoctoCliente(idc).toUpperCase().contains(complementoDescrParte.toUpperCase())) {
        idInfoComplemento = getIdFromInformacaoDoctoCliente(idc);
      }
    }

    return idInfoComplemento;
  }

  private String getDescFromInformacaoDoctoCliente(Map<String, Object> idc) {
    return (String) idc.get("dsCampo");
  }

  private Long getIdFromInformacaoDoctoCliente(Map<String, Object> idc) {
    return Long.valueOf(idc.get("idInformacaoDoctoCliente").toString());
  }

  private Collection<NotaFiscalEdiVolume> buildListVolumes(NotaFiscalEdi notaFiscalEdi,
      NotaFiscalWebServiceDto dto) {
    List<NotaFiscalEdiVolume> listVolume = new ArrayList<NotaFiscalEdiVolume>();

    for (Integer i = 1; i <= dto.getQuantidadeVolumes(); ++i) {
      NotaFiscalEdiVolume volume = new NotaFiscalEdiVolume();

      listVolume.add(volume);

      volume.setCodigoVolume(buildCodigoVolumeFor(i, dto));
      volume.setNotaFiscalEdi(notaFiscalEdi);
    }

    return listVolume;
  }


  private String buildCodigoVolumeFor(Integer i, NotaFiscalWebServiceDto dto) {
    String cnpjsNatura = null;
    try{
        cnpjsNatura = (String)parametroGeralService.findConteudoByNomeParametro("CNPJ_NATURA_WS", Boolean.FALSE);
    }catch(BusinessException e){
        //deixa como nulo o parametro, irá gerar etiquetas no formato claro.
    }
    
    if (StringUtils.isNotBlank(cnpjsNatura) && cnpjsNatura.contains(dto.getRemetente().getIdentificacao())){
        return buildEtiquetaNatura(i, dto);
    }else{
        return buildEtiquetaClaro(i, dto);
    }
  }

  /**
   * Método ad hoc para criação de etiquetas da Claro.
   * 
   * Regra de formação da etiqueta da claro: * 6 chars com número da nota + 3 char com série da nota
   * + 3 chars com contador de volumes.
   * 
   * @todo criar um Service para pegar as informações de layout de etiqueta de um cliente genérico.
   * 
   * @param i
   * @param dto
   * @return
   */  
  private String buildEtiquetaClaro(Integer i, NotaFiscalWebServiceDto dto){
      final int SIZE_NFNUM_ETIQ = 6;
      final int SIZE_NFSERIE_ETIQ = 3;
      String numS = dto.getNumero().toString();
      String serieS = dto.getSerie();

      numS = truncateOrFillWithLZero(SIZE_NFNUM_ETIQ, numS);
      serieS = truncateOrFillWithLZero(SIZE_NFSERIE_ETIQ, serieS);

      DecimalFormat countFormater = new DecimalFormat("000");

      String etiqueta = numS + serieS + countFormater.format(i);

      return etiqueta;
  }
  
  private String buildEtiquetaNatura(Integer i, NotaFiscalWebServiceDto dto){
      final int SIZE_NFNUM_ETIQ = 9;
      final int SIZE_NFSERIE_ETIQ = 2;
      String numS = dto.getNumero().toString();
      String serieS = dto.getSerie();

      numS = truncateOrFillWithLZero(SIZE_NFNUM_ETIQ, numS);
      serieS = truncateOrFillWithLZero(SIZE_NFSERIE_ETIQ, serieS);

      DecimalFormat countFormater = new DecimalFormat("000");

      String etiqueta = serieS + numS  + countFormater.format(i);

      return etiqueta;
  }
  
  
  private String truncateOrFillWithLZero(final int size, String str) {
    int numSLen = str.length();
    if (numSLen > size) {
      str = str.substring(numSLen - size);
    } else if (numSLen < size) {
      while (str.length() < size) {
        str = '0' + str;
      }
    }
    return str;
  }

  private void convertToNotaFiscalEDI(NotaFiscalWebServiceDto dto, NotaFiscalEdi notaFiscalEdi) {

    fillDataConsignatario(dto.getConsignatario(), dto.getEnderecoConsignatario(), notaFiscalEdi);

    fillDataDestinatario(dto.getDestinatario(), dto.getEnderecoDestinatario(), notaFiscalEdi);

    fillDataRedespacho(dto.getRedespacho(), dto.getEnderecoRedespacho(), notaFiscalEdi);

    fillDataRemetente(dto.getRemetente(), dto.getEnderecoRemetente(), notaFiscalEdi);

    fillDataTomador(dto.getTomador(), dto.getEnderecoTomador(), notaFiscalEdi);

    notaFiscalEdi.setAliqIcms(dto.getIcmsAliquota());

    notaFiscalEdi.setCfopNf(dto.getCodigoFiscalOperacao() != null ? dto.getCodigoFiscalOperacao().shortValue() : null);
    notaFiscalEdi.setChaveNfe(dto.getChaveAcesso());

    notaFiscalEdi.setDataEmissaoNf(new Date(dto.getEmissao()));
    notaFiscalEdi.setDsDivisaoCliente(dto.getDivisaoTabela());
    notaFiscalEdi.setNrCtrcSubcontratante(dto.getNrCtrcSubcontratante());

    notaFiscalEdi.setEspecie(dto.getEspecieAcondicionamento());

    notaFiscalEdi.setModalFrete(dto.getModal());

    notaFiscalEdi.setNatureza(dto.getNaturezaOperacao());

    notaFiscalEdi.setNrNotaFiscal(dto.getNumero().intValue());

    notaFiscalEdi.setOutrosValores(dto.getParcelaOutros());


    notaFiscalEdi.setPesoCubado(dto.getPesoDensidade());

    notaFiscalEdi.setPesoReal(dto.getPesoBruto());

    notaFiscalEdi.setPinSuframa(dto.getPinSuframa());

    notaFiscalEdi.setQtdeVolumes(asBigDecimal(dto.getQuantidadeVolumes()));
    notaFiscalEdi.setQtVolumeInformado(asBigDecimal(dto.getQuantidadeVolumes()));

    notaFiscalEdi.setSequenciaAgrupamento(dto.getSequenciaAgrupamento());
    notaFiscalEdi.setSerieNf(dto.getSerie());

    notaFiscalEdi.setTipoFrete(dto.getCondicaoFrete());

    notaFiscalEdi.setTipoTabela(dto.getTabelaFrete());

    notaFiscalEdi.setVlrBaseCalcIcms(dto.getIcmsBaseCalculo());

    notaFiscalEdi.setVlrBaseCalcStNf(dto.getIcmsBaseCalculoSubstituicaoTributaria());
    notaFiscalEdi.setVlrCat(dto.getParcelaCat());

    notaFiscalEdi.setVlrDespacho(dto.getParcelaDespacho());
    notaFiscalEdi.setVlrFreteLiquido(dto.getFreteLiquido());
    notaFiscalEdi.setVlrFretePeso(dto.getParcelaFretePeso());
    notaFiscalEdi.setVlrFreteValor(dto.getParcelaFreteValor());
    notaFiscalEdi.setVlrIcms(dto.getIcms());

    notaFiscalEdi.setVlrIcmsNf(dto.getIcms());
    notaFiscalEdi.setVlrIcmsStNf(dto.getIcmsSubstituicaoTributaria());
    notaFiscalEdi.setVlrItr(dto.getParcelaItr());

    notaFiscalEdi.setVlrPedagio(dto.getParcelaPedagio());
    notaFiscalEdi.setVlrTaxas(dto.getParcelaTaxas());
    notaFiscalEdi.setVlrTotalMerc(dto.getValorTotalProdutos());
    notaFiscalEdi.setVlrTotalMercTotal(dto.getValorTotalProdutos());
    notaFiscalEdi.setVlrTotProdutosNf(dto.getValorTotalProdutos());
  }

  private void fillDataTomador(ClienteWebServiceDto cli, EnderecoWebServiceDto end,
      NotaFiscalEdi notaFiscalEdi) {
    /* tomador */
    if (cli != null) {
      notaFiscalEdi.setCnpjTomador(getClienteCNPJAsLong(cli));
      notaFiscalEdi.setIeTomador(cli.getInscricaoEstadual());
      notaFiscalEdi.setNomeTomador(cli.getNome());
    }
    /* endereço tomador */
    if (end != null) {
      notaFiscalEdi.setBairroTomador(end.getBairro());
      notaFiscalEdi.setCepEnderTomador(end.getCep());
      notaFiscalEdi.setCepMunicTomador(end.getCepMunicipio());
      notaFiscalEdi.setEnderecoTomador(end.getEnderco());
      notaFiscalEdi.setMunicipioTomador(end.getNomeMunicipio());
      notaFiscalEdi.setUfTomador(end.getUf());
    }
  }

  private void fillDataRemetente(ClienteWebServiceDto cli, EnderecoWebServiceDto end,
      NotaFiscalEdi notaFiscalEdi) {
    /* remetente */
    if (cli != null) {
      notaFiscalEdi.setCnpjReme(getClienteCNPJAsLong(cli));
      notaFiscalEdi.setNomeReme(cli.getNome());

      Pessoa pessoa = notaFiscalEDIService.findDadosRemetente(cli.getIdentificacao());
      if (pessoa != null) {
        if (CollectionUtils.isNotEmpty(pessoa.getInscricaoEstaduais())) {
          notaFiscalEdi.setIeReme(pessoa.getInscricaoEstaduais().get(0).getNrInscricaoEstadual());
        }
        EnderecoPessoa endereco = pessoa.getEnderecoPessoa();
        if (endereco != null) {
          notaFiscalEdi.setEnderecoReme(endereco.getDsEndereco());
          notaFiscalEdi.setMunicipioReme(endereco.getMunicipio().getNmMunicipio());
          notaFiscalEdi.setUfReme(endereco.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
          notaFiscalEdi.setCepEnderReme(Integer.valueOf(endereco.getNrCep()));
        }
      }
    }
    /* endereço remetente */
    if (end != null) {
      notaFiscalEdi.setBairroReme(end.getBairro());
      notaFiscalEdi.setCepMuniReme(end.getCepMunicipio());
    }
  }

  private void fillDataRedespacho(ClienteWebServiceDto cli, EnderecoWebServiceDto end,
      NotaFiscalEdi notaFiscalEdi) {
    /* redespacho */
    if (cli != null) {
      notaFiscalEdi.setCnpjRedesp(getClienteCNPJAsLong(cli));
      notaFiscalEdi.setIeRedesp(cli.getInscricaoEstadual());
      notaFiscalEdi.setNomeRedesp(cli.getNome());
    }
    /* endereço redespacho */
    if (end != null) {
      notaFiscalEdi.setBairroRedesp(end.getBairro());
      notaFiscalEdi.setCepEnderRedesp(end.getCep());
      notaFiscalEdi.setCepMunicRedesp(end.getCepMunicipio());
      notaFiscalEdi.setEnderecoRedesp(end.getEnderco());
      notaFiscalEdi.setMunicipioRedesp(end.getNomeMunicipio());
      notaFiscalEdi.setUfRedesp(end.getUf());
    }
  }


  private void fillDataDestinatario(ClienteWebServiceDto cli, EnderecoWebServiceDto end,
      NotaFiscalEdi notaFiscalEdi) {
    /* destinatário */
    if (cli != null) {
      notaFiscalEdi.setCnpjDest(getClienteCNPJAsLong(cli));
      notaFiscalEdi.setIeDest(cli.getInscricaoEstadual());
      notaFiscalEdi.setNomeDest(StringUtils.substring(cli.getNome(), 0,CLIENTE_NOME_SIZE));
    }
    // endereço destinatário
    if (end != null) {
      notaFiscalEdi.setBairroDest(end.getEnderco());
      notaFiscalEdi.setCepEnderDest(end.getCep());
      notaFiscalEdi.setCepMunicDest(end.getCepMunicipio());
      notaFiscalEdi.setEnderecoDest(end.getEnderco());
      notaFiscalEdi.setMunicipioDest(end.getNomeMunicipio());
      notaFiscalEdi.setUfDest(end.getUf());
    }
  }

  private void fillDataConsignatario(ClienteWebServiceDto cli, EnderecoWebServiceDto end,
      NotaFiscalEdi notaFiscalEdi) {
    /* consignatário */
    if (cli != null) {
      notaFiscalEdi.setCnpjConsig(getClienteCNPJAsLong(cli));
      notaFiscalEdi.setIeConsig(cli.getInscricaoEstadual());
      notaFiscalEdi.setNomeConsig(cli.getNome());
    }
    /* endereço consignatário */
    if (cli != null) {
      notaFiscalEdi.setBairroConsig(end.getBairro());
      notaFiscalEdi.setCepEnderConsig(end.getCep());
      notaFiscalEdi.setCepMunicConsig(end.getCepMunicipio());
      notaFiscalEdi.setEnderecoConsig(end.getEnderco());
      notaFiscalEdi.setMunicipioConsig(end.getNomeMunicipio());
      notaFiscalEdi.setUfConsig(end.getUf());
    }
  }

  private BigDecimal asBigDecimal(Long l) {
    return new BigDecimal(l);
  }

  private BigDecimal asBigDecimal(Integer i) {
    return asBigDecimal(i.longValue());
  }

  /*
   * Ignora erros de parsing, qualquer problema, retorna 0l
   */
  private Long getClienteCNPJAsLong(ClienteWebServiceDto cliente) {
    Long cnpj = 0l;
    try {
      cnpj = Long.parseLong(cliente.getIdentificacao());
    } finally {
    }
    return cnpj;
  }
  
  
}
