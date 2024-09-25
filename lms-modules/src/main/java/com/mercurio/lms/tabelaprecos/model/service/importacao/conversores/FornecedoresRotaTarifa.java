package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.TipoLocalizacaoMunicipioService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.municipios.model.service.ZonaService;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.GrupoRegiaoService;
import com.mercurio.lms.tabelaprecos.model.service.RotaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.TarifaPrecoRotaService;
import com.mercurio.lms.tabelaprecos.model.service.TarifaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.fornecedores.FornecedorRotaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.fornecedores.FornecedorTarifaPreco;
import com.mercurio.lms.tabelaprecos.model.service.importacao.conversores.fornecedores.FornecedorTarifaVsRota;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao.TipoChaveProgressao;

public class FornecedoresRotaTarifa {
	
	private ZonaService zonaService;
	private PaisService paisService;
	private UnidadeFederativaService unidadeFederativaService;
	private MunicipioService municipioService;
	private AeroportoService aeroportoService;
	private FilialService filialService;
	private ParametroGeralService parametroGeralService;
	private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService;
	private GrupoRegiaoService grupoRegiaoService;
	private RotaPrecoService rotaPrecoService;
	private TarifaPrecoService tarifaPrecoService;
	private TarifaPrecoRotaService tarifaVsRotaService;
	private EnderecoPessoaService enderecoPessoaService;
	private MunicipioFilialService municipioFilialService;
	private TabelaPreco tabela;
	private Map<TipoChaveProgressao, FornecedorChave> fornecedoresPorTipo = new HashMap<ChaveProgressao.TipoChaveProgressao, FornecedorChave>();
	
	public void informaTabelaPreco(TabelaPreco tabela) {
		if (tabela == null) {
			throw new IllegalArgumentException("NULL não é uma tabela de preço válida.");
		}
		this.tabela = tabela;
		this.fornecedoresPorTipo = new HashMap<ChaveProgressao.TipoChaveProgressao, FornecedorChave>();
	}

	public FornecedorChave fornecedorPara(TipoChaveProgressao tipoChave) {
		if (tipoChave == null) {
			throw new IllegalArgumentException("NULL não é um tipo de chave válido.");
		}
		if (this.fornecedoresPorTipo .containsKey(tipoChave)) {
			return this.fornecedoresPorTipo.get(tipoChave);
		}
		FornecedorChave fornecedor = criaFornecedor(tipoChave);
		fornecedor.inicializa();
		return fornecedor;
	}

	private FornecedorChave criaFornecedor(TipoChaveProgressao tipoChave) {
		if (TipoChaveProgressao.ROTA.equals(tipoChave)) {
			return this.criaFornecedorRota();
		}
		if (TipoChaveProgressao.TARIFA.equals(tipoChave)) {
			return this.criaFornecedorTarifa();
		}
		return this.criaFornecedorTarifaVsRota(this.criaFornecedorRota(), this.criaFornecedorTarifa());
	}

	private FornecedorRotaPreco criaFornecedorRota() {
		FornecedorRotaPreco fornecedorRota = 
				new FornecedorRotaPreco(this.tabela.getIdTabelaPreco(), rotaPrecoService, zonaService, 
						paisService, unidadeFederativaService, municipioService, aeroportoService, filialService, 
						parametroGeralService, tipoLocalizacaoMunicipioService, grupoRegiaoService, enderecoPessoaService, municipioFilialService);
		this.fornecedoresPorTipo.put(TipoChaveProgressao.ROTA, fornecedorRota);
		return fornecedorRota;
	}

	private FornecedorTarifaPreco criaFornecedorTarifa() {
		FornecedorTarifaPreco fornecedorTarifaPreco = new FornecedorTarifaPreco(tarifaPrecoService);
		this.fornecedoresPorTipo.put(TipoChaveProgressao.TARIFA, fornecedorTarifaPreco);
		return fornecedorTarifaPreco;
	}
	
	private FornecedorChave criaFornecedorTarifaVsRota(FornecedorRotaPreco criaFornecedorRota, FornecedorTarifaPreco criaFornecedorTarifa) {
		FornecedorTarifaVsRota fornecedorTarifaVsRota = new FornecedorTarifaVsRota(this.tabela, this.tarifaVsRotaService, criaFornecedorRota, criaFornecedorTarifa);
		this.fornecedoresPorTipo.put(TipoChaveProgressao.TARIFA_X_ROTA, fornecedorTarifaVsRota);
		return fornecedorTarifaVsRota;
	}

	public void setRotaPrecoService(RotaPrecoService rotaPrecoService) {
		this.rotaPrecoService = rotaPrecoService;
	}

	public void setTarifaPrecoService(TarifaPrecoService tarifaPrecoService) {
		this.tarifaPrecoService = tarifaPrecoService;
	}

	public void setZonaService(ZonaService zonaService) {
		this.zonaService = zonaService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setTipoLocalizacaoMunicipioService(TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService) {
		this.tipoLocalizacaoMunicipioService = tipoLocalizacaoMunicipioService;
	}

	public void setGrupoRegiaoService(GrupoRegiaoService grupoRegiaoService) {
		this.grupoRegiaoService = grupoRegiaoService;
	}

	public void setTarifaVsRotaService(TarifaPrecoRotaService tarifaVsRotaService) {
		this.tarifaVsRotaService = tarifaVsRotaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}
	
	public void persisteTodosCaches(){
		for(FornecedorChave fornecedorChave : this.fornecedoresPorTipo.values()){
			fornecedorChave.persisteCache();
		}
		
	}
	
}
