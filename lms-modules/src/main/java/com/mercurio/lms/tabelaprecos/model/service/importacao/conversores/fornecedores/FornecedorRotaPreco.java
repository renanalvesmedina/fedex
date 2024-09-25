package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.fornecedores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.Zona;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.TipoLocalizacaoMunicipioService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.municipios.model.service.ZonaService;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.service.GrupoRegiaoService;
import com.mercurio.lms.tabelaprecos.model.service.RotaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.importacao.ImportacaoException;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.RotaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ValorImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.FornecedorChave;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.TradutorChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.excessao.MensagemErroImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;

public class FornecedorRotaPreco extends FornecedorChave {
	
	private RotaPrecoService rotaPrecoService;
	private ZonaService zonaService;
	private PaisService paisService;
	private UnidadeFederativaService unidadeFederativaService;
	private MunicipioService municipioService;
	private AeroportoService aeroportoService;
	private FilialService filialService;
	private ParametroGeralService parametroGeralService;
	private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService;
	private GrupoRegiaoService grupoRegiaoService;
	private EnderecoPessoaService enderecoPessoaService;
	private MunicipioFilialService municipioFilialService;
	
	private static final int CACHE_SIZE = 50;
	private static final String TP_SITUACAO_ATIVA = "A";
	private static final String PARAMETRO_ID_EMPRESA_MERCURIO = "ID_EMPRESA_MERCURIO";
	private final Long idEmpresaMercurio;
	private Long idTabelaPreco;
	
	private Map<String, Pais> cachePais;
	private Map<String, UnidadeFederativa> cacheUf;
	private Map<String, Aeroporto> cacheAeroporto;
	private Map<String, Filial> cacheFilial;
	private Map<String, Municipio> cacheMunicipio;
	private Map<String, GrupoRegiao> cacheGrupoRegiao;
	private Map<String, TipoLocalizacaoMunicipio> cacheTipoLocalizacaoMunicipio;
	private Map<Long, List<Municipio>> cacheMunicipiosFilial;
	private List<RotaPreco> cacheRotas;
	
	public FornecedorRotaPreco(Long idTabelaPreco, RotaPrecoService rotaPrecoService, ZonaService zonaService, PaisService paisService, 
			UnidadeFederativaService unidadeFederativaService, MunicipioService municipioService, AeroportoService aeroportoService, FilialService filialService,
			ParametroGeralService parametroGeralService, TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService, GrupoRegiaoService grupoRegiaoService,
			EnderecoPessoaService enderecoPessoaService, MunicipioFilialService municipioFilialService) {
		this.idTabelaPreco = idTabelaPreco;
		this.rotaPrecoService = rotaPrecoService;
		this.zonaService = zonaService;
		this.paisService = paisService;
		this.unidadeFederativaService = unidadeFederativaService;
		this.municipioService = municipioService;
		this.aeroportoService = aeroportoService;
		this.filialService = filialService;
		this.parametroGeralService = parametroGeralService;
		this.tipoLocalizacaoMunicipioService = tipoLocalizacaoMunicipioService;
		this.grupoRegiaoService = grupoRegiaoService;
		this.enderecoPessoaService = enderecoPessoaService;
		this.municipioFilialService = municipioFilialService;
		String idEmpresa = this.parametroGeralService.findSimpleConteudoByNomeParametro(PARAMETRO_ID_EMPRESA_MERCURIO);
		this.idEmpresaMercurio = Long.valueOf(idEmpresa);
	}
	
	
	@Override
	protected TradutorChaveProgressao executaForneceChave(ChaveProgressao chaveProgressao) {
		RotaPreco rotaPreco = this.buscarRotaPreco(chaveProgressao);
		if(rotaPreco == null){
			rotaPreco = incluiRota(chaveProgressao);
		}
		return new TradutorChaveProgressao(rotaPreco, null);
	}

	private RotaPreco incluiRota(ChaveProgressao chaveProgressao) {
		RotaPreco rotaPreco = criaEntidadeRotaPreco(chaveProgressao);
		this.processaRotasNovas(rotaPreco);
		return rotaPreco;
	}
	
	private void processaRotasNovas(RotaPreco rotaPreco){
		this.initRotaCache();
		cacheRotas.add(rotaPreco);
		if(cacheRotas.size() == CACHE_SIZE){
			this.rotaPrecoService.storeAll(cacheRotas);
			cacheRotas.clear();
		}
	}
	
	private void initRotaCache(){
		if(cacheRotas == null){
			cacheRotas = new ArrayList<RotaPreco>();
		}
	}

	private RotaPreco buscarRotaPreco(ChaveProgressao chaveProgressao) {
		RotaImportacao rotaImportacao = (RotaImportacao) chaveProgressao;
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idTabelaPreco", this.idTabelaPreco);
		parametros.put("idEmpresaMercurio", this.idEmpresaMercurio);
		parametros.put("idZonaOrigem", rotaImportacao.zonaOrigem());
		parametros.put("idZonaDestino", rotaImportacao.zonaDestino());
		parametros.put("idTipoLocalizacaoMunicipioOrigem", rotaImportacao.tipoLocalizacaoOrigem());
		parametros.put("idTipoLocalizacaoMunicipioDestino", rotaImportacao.tipoLocalizacaoDestino());
		parametros.put("idTipoLocalizacaoMunicipioComercialOrigem", rotaImportacao.tipoLocalComercialOrigem());
		parametros.put("idTipoLocalizacaoMunicipioComercialDestino", rotaImportacao.tipoLocalComercialDestino());
		parametros.put("dsGrupoRegiaoOrigem", rotaImportacao.grupoRegiaoOrigem());
		parametros.put("dsGrupoRegiaoDestino", rotaImportacao.grupoRegiaoDestino());
		parametros.put("sgAeroportoOrigem", rotaImportacao.aeroportoOrigem());
		parametros.put("sgAeroportoDestino", rotaImportacao.aeroportoDestino());
		parametros.put("sgUnidadeFederativaOrigem", rotaImportacao.ufOrigem());
		parametros.put("sgUnidadeFederativaDestino", rotaImportacao.ufDestino());
		parametros.put("sgPaisOrigem", rotaImportacao.paisOrigem());
		parametros.put("sgPaisDestino", rotaImportacao.paisDestino());
		parametros.put("sgFilialOrigem", rotaImportacao.filialOrigem());
		parametros.put("sgFilialDestino", rotaImportacao.filialDestino());
		parametros.put("nmMunicipioOrigem", rotaImportacao.municipioOrigem());
		parametros.put("nmMunicipioDestino", rotaImportacao.municipioDestino());

		return this.rotaPrecoService.findRotaPrecoParaImportacaoTabelaPreco(parametros);
	}
	
	private RotaPreco criaEntidadeRotaPreco(ChaveProgressao chaveProgressao) {
		RotaImportacao rotaImportacao = (RotaImportacao) chaveProgressao;
		
		RotaPreco rotaPreco = new RotaPreco();
		int linhaRota = rotaImportacao.linha();
		int colunaRota = rotaImportacao.coluna();
		rotaPreco.setTpSituacao((DomainValue) ReflectionUtils.toObject(TP_SITUACAO_ATIVA, DomainValue.class));
		
		List<MensagemErroImportacao> mensagens = new ArrayList<MensagemErroImportacao>();
		
		rotaPreco.setZonaByIdZonaOrigem(this.buscarZona(rotaImportacao.zonaOrigem(), this.buscaLinha(linhaRota, rotaImportacao.valorZonaOrigem()), this.buscaColuna(colunaRota, rotaImportacao.valorZonaOrigem()), mensagens));
		rotaPreco.setZonaByIdZonaDestino(this.buscarZona(rotaImportacao.zonaDestino(), this.buscaLinha(linhaRota, rotaImportacao.valorZonaDestino()), this.buscaColuna(colunaRota, rotaImportacao.valorZonaDestino()), mensagens));
		
		rotaPreco.setPaisByIdPaisOrigem(this.buscarPais(rotaImportacao.paisOrigem(), this.buscaLinha(linhaRota, rotaImportacao.valorPaisOrigem()), buscaColuna(colunaRota, rotaImportacao.valorPaisOrigem()), mensagens));
		rotaPreco.setPaisByIdPaisDestino(this.buscarPais(rotaImportacao.paisDestino(), this.buscaLinha(linhaRota, rotaImportacao.valorPaisDestino()), buscaColuna(colunaRota, rotaImportacao.valorPaisDestino()), mensagens));
		
		rotaPreco.setUnidadeFederativaByIdUfOrigem(this.buscarUnidadeFederativa(rotaImportacao.ufOrigem(), rotaImportacao.paisOrigem(), this.buscaLinha(linhaRota, rotaImportacao.valorUfOrigem()), this.buscaColuna(colunaRota, rotaImportacao.valorUfOrigem()), mensagens));
		rotaPreco.setUnidadeFederativaByIdUfDestino(this.buscarUnidadeFederativa(rotaImportacao.ufDestino(), rotaImportacao.paisDestino(), this.buscaLinha(linhaRota, rotaImportacao.valorUfDestino()), this.buscaColuna(colunaRota, rotaImportacao.valorUfDestino()), mensagens));
		
		rotaPreco.setFilialByIdFilialOrigem(this.buscarFilial(rotaImportacao.filialOrigem(), rotaPreco.getUnidadeFederativaByIdUfOrigem(), this.buscaLinha(linhaRota, rotaImportacao.valorFilialOrigem()), this.buscaColuna(colunaRota, rotaImportacao.valorFilialOrigem()), idEmpresaMercurio,  mensagens));
		rotaPreco.setFilialByIdFilialDestino(this.buscarFilial(rotaImportacao.filialDestino(), rotaPreco.getUnidadeFederativaByIdUfDestino(), this.buscaLinha(linhaRota, rotaImportacao.valorFilialDestino()), this.buscaColuna(colunaRota, rotaImportacao.valorFilialDestino()), idEmpresaMercurio, mensagens));
		
		rotaPreco.setMunicipioByIdMunicipioOrigem(this.buscarMunicipio(rotaImportacao.municipioOrigem(), rotaImportacao.ufOrigem(), rotaPreco.getFilialByIdFilialOrigem(), this.buscaLinha(linhaRota, rotaImportacao.valorMunicipioOrigem()), this.buscaColuna(colunaRota, rotaImportacao.valorMunicipioOrigem()), mensagens));
		rotaPreco.setMunicipioByIdMunicipioDestino(this.buscarMunicipio(rotaImportacao.municipioDestino(), rotaImportacao.ufDestino(), rotaPreco.getFilialByIdFilialDestino(), this.buscaLinha(linhaRota, rotaImportacao.valorMunicipioDestino()), this.buscaColuna(colunaRota, rotaImportacao.valorMunicipioDestino()), mensagens));
		
		rotaPreco.setAeroportoByIdAeroportoOrigem(this.buscarAeroPorto(rotaImportacao.aeroportoOrigem(), rotaPreco.getUnidadeFederativaByIdUfOrigem(), this.buscaLinha(linhaRota, rotaImportacao.valorAeroportoOrigem()), this.buscaColuna(colunaRota, rotaImportacao.valorAeroportoOrigem()), mensagens));
		rotaPreco.setAeroportoByIdAeroportoDestino(this.buscarAeroPorto(rotaImportacao.aeroportoDestino(), rotaPreco.getUnidadeFederativaByIdUfDestino(), this.buscaLinha(linhaRota, rotaImportacao.valorAeroportoDestino()), this.buscaColuna(colunaRota, rotaImportacao.valorAeroportoDestino()), mensagens));
		
		rotaPreco.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(
				this.buscarTipoLocalizacaoMuniciopio(rotaImportacao.tipoLocalizacaoOrigem(), this.buscaLinha(linhaRota, rotaImportacao.valorTipoLocalizacaoOrigem()), this.buscaColuna(colunaRota, rotaImportacao.valorTipoLocalizacaoOrigem()), "tipo localização", mensagens));
		rotaPreco.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(
				this.buscarTipoLocalizacaoMuniciopio(rotaImportacao.tipoLocalizacaoDestino(), this.buscaLinha(linhaRota, rotaImportacao.valorTipoLocalizacaoDestino()), this.buscaColuna(colunaRota, rotaImportacao.valorTipoLocalizacaoDestino()), "tipo localização", mensagens));
		
		rotaPreco.setTipoLocalizacaoMunicipioComercialOrigem(
				this.buscarTipoLocalizacaoMuniciopio(rotaImportacao.tipoLocalComercialOrigem(), this.buscaLinha(linhaRota, rotaImportacao.valorTipoLocalComercialOrigem()), this.buscaColuna(colunaRota, rotaImportacao.valorTipoLocalComercialOrigem()), "tipo local comercial", mensagens));
		rotaPreco.setTipoLocalizacaoMunicipioComercialDestino(
				this.buscarTipoLocalizacaoMuniciopio(rotaImportacao.tipoLocalComercialDestino(), this.buscaLinha(linhaRota, rotaImportacao.valorTipoLocalComercialDestino()), this.buscaColuna(colunaRota, rotaImportacao.valorTipoLocalComercialDestino()), "tipo local comercial", mensagens));
		
		rotaPreco.setGrupoRegiaoOrigem(this.buscarGrupoRegiao(rotaImportacao.grupoRegiaoOrigem(), rotaPreco.getUnidadeFederativaByIdUfOrigem(), this.buscaLinha(linhaRota, rotaImportacao.valorGrupoRegiaoOrigem()), this.buscaColuna(colunaRota, rotaImportacao.valorGrupoRegiaoOrigem()), mensagens));
		rotaPreco.setGrupoRegiaoDestino(this.buscarGrupoRegiao(rotaImportacao.grupoRegiaoDestino(), rotaPreco.getUnidadeFederativaByIdUfDestino(), this.buscaLinha(linhaRota, rotaImportacao.valorGrupoRegiaoDestino()), this.buscaColuna(colunaRota, rotaImportacao.valorGrupoRegiaoDestino()), mensagens));
		
		if (CollectionUtils.isNotEmpty(mensagens)) {
			throw new ImportacaoException(mensagens);
		}
		
		return rotaPreco;
	}
	
	private int buscaLinha(int linhaRota, ValorImportacao valor) {
		return valor == null || valor.linha() == null ? linhaRota : valor.linha();
	}
	
	private int buscaColuna(int colunaRota, ValorImportacao valor) {
		return valor == null || valor.coluna() == null ? colunaRota : valor.coluna();
	}
	
	private Municipio buscarMunicipio(String nmMunicipio, String uf, Filial filial, int linha, int coluna, List<MensagemErroImportacao> mensagens) {
		
		if(StringUtils.isBlank(nmMunicipio) || StringUtils.isBlank(uf) || filial == null) {
			return null;
		}
		
		this.initMunicipioCache();
		String chave = uf+"-"+filial.getSgFilial()+"-"+nmMunicipio;
		if(cacheMunicipio.containsKey(chave)){
			return cacheMunicipio.get(chave);
		}

		Municipio municipio = this.municipioService.findByNmMunicipioAndUf(nmMunicipio, uf);
		if (municipio == null) {
			mensagens.add(new MensagemErroImportacao(linha, coluna, String.format("O município '%s' não pertence à UF '%s'.", nmMunicipio, uf)));
			return null;
		}
		
		List<Municipio> municipiosAtendidosFiliais = null;
		this.initMunicipiosFilialCache();
		Long chaveAux = filial.getIdFilial();
		if(cacheMunicipiosFilial.containsKey(chaveAux)){
			municipiosAtendidosFiliais = cacheMunicipiosFilial.get(chaveAux);
		}else{
			municipiosAtendidosFiliais = this.municipioFilialService.findMunicipioByIdFilialParaImportador(chaveAux);
			cacheMunicipiosFilial.put(chaveAux, municipiosAtendidosFiliais);
		}
		
		if (CollectionUtils.isEmpty(municipiosAtendidosFiliais) || !filialAtendeMunicipio(municipiosAtendidosFiliais, municipio)) {
			mensagens.add(new MensagemErroImportacao(linha, coluna, String.format("O município '%s' não é atendido pela filial '%s'.", nmMunicipio, filial.getSgFilial())));
			return null;
		}
		
		cacheMunicipio.put(chave, municipio);
		return municipio;
	}
	
	private void initMunicipioCache(){
		if(cacheMunicipio == null) {
			cacheMunicipio = new HashMap<String, Municipio>();
		}
	}
	
	private boolean filialAtendeMunicipio(final List<Municipio> municipiosFilial, final Municipio municipio) {
		return CollectionUtils.find(municipiosFilial, new Predicate(){
			@Override
			public boolean evaluate(Object object) {
				Municipio municipioAux = (Municipio) object;
				return municipio.equals(municipioAux);
			}
		}) != null;
	}


	private Zona buscarZona(Long idZona, int linha, int coluna, List<MensagemErroImportacao> mensagens) {
		if (idZona == null) {
			return null;
		}
		Zona zona = this.zonaService.findById(idZona);
		if (zona == null) {
			mensagens.add(new MensagemErroImportacao(linha, coluna, String.format("A zona '%d' não foi encontrada na base de dados.", idZona)));
		}
		return zona;
	}
	
	private Pais buscarPais(String sgPais, int linha, int coluna, List<MensagemErroImportacao> mensagens) {
		if(StringUtils.isBlank(sgPais)) {
			return null;
		}
		
		this.initPaisCache();
		if(cachePais.containsKey(sgPais)){
			return cachePais.get(sgPais);
		}
		
		Pais pais = this.paisService.findPaisBySgPais(sgPais);
		if (pais == null) {
			mensagens.add(new MensagemErroImportacao(linha, coluna, String.format("O país '%s' não foi encontrado na base de dados.", sgPais)));
		}
		
		cachePais.put(sgPais, pais);
		return pais;
	}
	
	private void initPaisCache(){
		if(cachePais == null) {
			cachePais = new HashMap<String, Pais>();
		}
	}
	
	private UnidadeFederativa buscarUnidadeFederativa(String sgUnidadeFederativa, String sgPais, int linha, int coluna, List<MensagemErroImportacao> mensagens) {
		if(StringUtils.isBlank(sgUnidadeFederativa) || StringUtils.isBlank(sgPais)) {
			return null;
		}
		
		this.initUfCache();
		String chave = sgPais+"-"+sgUnidadeFederativa;
		if(cacheUf.containsKey(chave)){
			return cacheUf.get(chave);
		}
		
		UnidadeFederativa uf = this.unidadeFederativaService.findBySgUnidadeFederativaSgPais(sgUnidadeFederativa, sgPais);
		if (uf == null) {
			mensagens.add(new MensagemErroImportacao(linha, coluna, String.format("A unidade federativa '%s' não pertence ao país '%s'.", sgUnidadeFederativa, sgPais)));
			return null;
		}
		
		cacheUf.put(chave, uf);
		return uf;
	}
	
	private void initUfCache(){
		if(cacheUf == null) {
			cacheUf = new HashMap<String, UnidadeFederativa>();
		}
	}

	private Aeroporto buscarAeroPorto(String sgAeroporto, UnidadeFederativa uf, int linha, int coluna, List<MensagemErroImportacao> mensagens) {
		if(StringUtils.isBlank(sgAeroporto) || uf == null) {
			return null;
		}
		
		this.initAeroportoCache();
		String chave = uf.getSgUnidadeFederativa()+"-"+sgAeroporto;
		if(cacheAeroporto.containsKey(chave)){
			return cacheAeroporto.get(chave);
		}
		
		Aeroporto aeroporto = this.aeroportoService.findBySgAeroporto(sgAeroporto);
		if (aeroporto == null) {
			mensagens.add(new MensagemErroImportacao(linha, coluna, String.format("O aeroporto '%s' não foi encontrado na base de dados.", sgAeroporto)));
			return null;
		}
		EnderecoPessoa enderecoPessoaOrigem = enderecoPessoaService.findByIdPessoa(aeroporto.getPessoa().getIdPessoa());
		if(enderecoPessoaOrigem == null || !enderecoPessoaOrigem.getMunicipio().getUnidadeFederativa().equals(uf)){
			mensagens.add(new MensagemErroImportacao(linha, coluna, String.format("O aeroporto '%s' não pertence a UF %s.", sgAeroporto, uf.getSgUnidadeFederativa())));
			return null;
		}
		
		cacheAeroporto.put(chave, aeroporto);
		return aeroporto;
	}
	
	private void initAeroportoCache(){
		if(cacheAeroporto == null) {
			cacheAeroporto = new HashMap<String, Aeroporto>();
		}
		
	}
	
	private Filial buscarFilial(String sgFilial, UnidadeFederativa uf, int linha, int coluna, Long idEmpresaMercurio, List<MensagemErroImportacao> mensagens) {
		if(StringUtils.isBlank(sgFilial) || uf == null) {
			return null;
		}
		
		this.initFilialCache();
		String chave = uf.getSgUnidadeFederativa()+"-"+sgFilial;
		if(cacheFilial.containsKey(chave)){
			return cacheFilial.get(chave);
		}
		
		Filial filial = this.filialService.findFilial(idEmpresaMercurio, sgFilial);
		if (filial == null) {
			mensagens.add(new MensagemErroImportacao(linha, coluna, String.format("A filial '%s' não foi encontrada na base de dados.", sgFilial)));
			return null;
		}
		
		List<Municipio> municipiosAtendidosFiliais = null;
		this.initMunicipiosFilialCache();
		Long chaveAux = filial.getIdFilial();
		if(cacheMunicipiosFilial.containsKey(chaveAux)){
			municipiosAtendidosFiliais = cacheMunicipiosFilial.get(chaveAux);
		}else{
			municipiosAtendidosFiliais = this.municipioFilialService.findMunicipioByIdFilialParaImportador(chaveAux);
			cacheMunicipiosFilial.put(chaveAux, municipiosAtendidosFiliais);
		}
		
		if (CollectionUtils.isEmpty(municipiosAtendidosFiliais) || !ufEhAtendidaPelaFilial(municipiosAtendidosFiliais, uf)) {
			mensagens.add(new MensagemErroImportacao(linha, coluna, String.format("A filial '%s' não pertence à uf '%s'.", sgFilial, uf.getSgUnidadeFederativa())));
			return null;
		}
		
		cacheFilial.put(chave, filial);
		return filial;
	}
	
	private void initMunicipiosFilialCache(){
		if(cacheMunicipiosFilial == null) {
			cacheMunicipiosFilial = new HashMap<Long, List<Municipio>>();
		}
	}
	
	private void initFilialCache(){
		if(cacheFilial == null) {
			cacheFilial = new HashMap<String, Filial>();
		}
	}
	
	private boolean ufEhAtendidaPelaFilial(List<Municipio> municipiosAtendidosFiliais, UnidadeFederativa uf) {
		for (Municipio municipio : municipiosAtendidosFiliais) {
			if (uf.equals(municipio.getUnidadeFederativa())){
				return true;
			}
		}
		return false;
	}
	
	private TipoLocalizacaoMunicipio buscarTipoLocalizacaoMuniciopio(Long idTipoLocalizacaoMunicipioOrigem, int linha, int coluna, String campo, List<MensagemErroImportacao> mensagens) {
		if(idTipoLocalizacaoMunicipioOrigem == null) {
			return null;
		}
		
		this.initTipoLocalizacaoMunicipioCache();
		if(cacheTipoLocalizacaoMunicipio.containsKey(idTipoLocalizacaoMunicipioOrigem.toString())){
			return cacheTipoLocalizacaoMunicipio.get(idTipoLocalizacaoMunicipioOrigem.toString());
		}
		
		TipoLocalizacaoMunicipio tipoLocalizacao = this.tipoLocalizacaoMunicipioService.findById(idTipoLocalizacaoMunicipioOrigem);
		if (tipoLocalizacao == null) {
			mensagens.add(new MensagemErroImportacao(linha, coluna, String.format("A %s '%d' não foi encontrada na base de dados", campo, idTipoLocalizacaoMunicipioOrigem)));
		}
		
		cacheTipoLocalizacaoMunicipio.put(idTipoLocalizacaoMunicipioOrigem.toString(), tipoLocalizacao);
		return tipoLocalizacao;
	}
	
	private void initTipoLocalizacaoMunicipioCache(){
		if(cacheTipoLocalizacaoMunicipio == null) {
			cacheTipoLocalizacaoMunicipio = new HashMap<String, TipoLocalizacaoMunicipio>();
		}
	}
			
	private GrupoRegiao buscarGrupoRegiao(String dsGrupoRegiao, UnidadeFederativa uf, int linha, int coluna, List<MensagemErroImportacao> mensagens) {
		if(StringUtils.isBlank(dsGrupoRegiao)) {
			return null;
		}
		if (uf == null) {
			mensagens.add(new MensagemErroImportacao(linha, coluna, "Para informar um grupo região, uma UF deve ser informada."));
			return null;
		}
		
		this.initGrupoRegiaoCache();
		String chave = uf.getSgUnidadeFederativa()+"-"+dsGrupoRegiao;
		if(cacheGrupoRegiao.containsKey(chave)){
			return cacheGrupoRegiao.get(chave);
		}
		
		GrupoRegiao grupo = this.grupoRegiaoService.findByDsGrupoTabelaUf(dsGrupoRegiao, idTabelaPreco, uf.getIdUnidadeFederativa());
		if (grupo == null) {
			mensagens.add(new MensagemErroImportacao(linha, coluna, String.format("Não foi encontrado grupo região com descrição '%s' pertencente à uf %s e tabela %d.", 
																					dsGrupoRegiao, uf.getIdUnidadeFederativa(), idTabelaPreco)));
			return null;
		}
		
		cacheGrupoRegiao.put(chave, grupo);
		return grupo;
	}
	
	private void initGrupoRegiaoCache(){
		if(cacheGrupoRegiao == null) {
			cacheGrupoRegiao = new HashMap<String, GrupoRegiao>();
		}
	}

	@Override
	public void inicializa() {
		//intencionalmente deixado em branco pois não há necessidade de faze-lo
	}


	@Override
	public void persisteCache() {
		if(!CollectionUtils.isEmpty(cacheRotas)){
			this.rotaPrecoService.storeAll(cacheRotas);
			cacheRotas.clear();
		}
	}

}