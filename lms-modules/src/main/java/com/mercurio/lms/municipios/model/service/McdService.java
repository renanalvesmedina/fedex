package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.municipios.model.Mcd;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.dao.McdDAO;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TarifaPrecoService;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;

/**
 * Classe de servi�o para CRUD:
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.mcdService"
 */
public class McdService extends CrudService<Mcd, Long> {
	private PostoPassagemMunicipioService postoPassagemMunicipioService;
	private MunicipioFilialService municipioFilialService;
	private FluxoFilialService fluxoFilialService;
	private FilialService filialService;
	private TarifaPrecoService tarifaPrecoService;
	private PostoPassagemTrechoService postoPassagemTrechoService;
	private MunicipioService municipioService;

	/**
	 * Recupera uma inst�ncia de <code>Mcd</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public Mcd findById(java.lang.Long id) {
		return (Mcd)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Mcd bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setMcdDAO(McdDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private McdDAO getMcdDAO() {
		return (McdDAO) getDao();
	}

	public void execute(YearMonthDay dtVigencia, Boolean confirmado){
		this.geraMcd(dtVigencia, confirmado);
	}

	/**
	 * Gera as informacoes do MCD (Mapa de Codigos de Distancia)
	 * @param dtVigencia
	 */
	public void geraMcd(YearMonthDay dtVigencia, Boolean confirmado) {
		// Data atual
		YearMonthDay today = JTDateTimeUtils.getDataAtual();

		// N�o permite gerar um MCD pro passado.
		if (dtVigencia.compareTo(today) < 0){
			throw new BusinessException("LMS-29147");
		// N�o permite gerar um MCD para a data atual se j� existe um MCD para este dia.
		}else if (dtVigencia.compareTo(today) == 0){
			Mcd mcd = getMcdDAO().findMcdByVigenciaInicial(dtVigencia);
			if (mcd != null) {
				throw new BusinessException("LMS-29149");
				}
		}

		// Se existem MCDs futuros, estes devem ser deletados ap�s confirma��o do usu�rio.
		List<Long> idsMcdsFuturos = getMcdDAO().findIdMcdsFuturosByVigenciaInicial(dtVigencia);
		if (!idsMcdsFuturos.isEmpty()){
			if (confirmado == null) {
				throw new BusinessException("LMS-29148");
			} else {
				super.removeByIds(idsMcdsFuturos);
			}
		}

		// invoca execu��o da gera��o do MCD.
		executeGerarMCD(dtVigencia);
	}

	/**
	 * Gera��o do MCD.
	 *
	 * Verifica se existe frequ�ncia de opera��o de coleta.
	 * Primeiramente consulta todos as filiais que atendem algum munic�pio que gerem MCD.
	 * O atendimento deve estar vigente.
	 * Ap�s, verifica se h� opera��o com servi�o nulo cadastrada para todos os servi�os.
	 * A opera��o deve estar vigente e ser do tipo 'Coleta' ou 'Ambas'.
	 * Caso algum fluxo inexistente seja idenficado, a exce��o LMS-29084 � gerada passando as
	 * filiais de origem e destino esperadas. 
	 *
	 * Verifica se existe frequ�ncia de opera��o de coleta.
	 * Primeiramente consulta todos as filiais que atendem algum munic�pio que gerem MCD.
	 * O atendimento deve estar vigente.
	 * Ap�s, verifica se h� opera��o com servi�o nulo cadastrada para todos os servi�os.
	 * A opera��o deve estar vigente e ser do tipo 'Coleta' ou 'Ambas'.
	 * Caso algum munic�pio n�o configurado seja encontrado, � gerado
	 * a exce��o LMS-29079 passando o nome do munic�pio.
	 *
	 * Verifica se existe frequ�ncia de opera��o de entrega.
	 * Primeiramente consulta todos as filiais que atendem algum munic�pio com "BL_PADRAO_MCD" verdadeiro.
	 * O atendimento deve estar vigente.
	 * Ap�s, verifica se h� opera��o com servi�o nulo cadastrada para todos os servi�os.
	 * A opera��o deve estar vigente e ser do tipo 'Entrega' ou 'Ambas'.
	 * Caso algum munic�pio n�o configurado seja encontrado, � gerado
	 * a exce��o LMS-29080 passando o nome do munic�pio.
	 *
	 *
	 * @author Felipe Ferreira
	 * @since 27-02-2007
	 * 
	 * @param dtVigencia Data de in�cio de vig�ncia desejada na vers�o do MCD a ser gerado.
	 */
	private void executeGerarMCD(YearMonthDay dtVigencia) {
		// Verifica fluxos n�o cadastrados no sistema.
		List<Object[]> fluxosInexistentes = getMcdDAO().validateGeracaoFluxosInexistentes(dtVigencia);
		if (!fluxosInexistentes.isEmpty()) {
			// Se algum fluxo for retornado, � porque este n�o est� cadastrado no sistema.
			// See documenta��o do m�todo validateGeracaoFluxosInexistentes para entender o que 
			// foi retornado no array.
			Object[] row = fluxosInexistentes.get(0);
			throw new BusinessException("LMS-29084",new Object[]{row[1],row[3]});
		}

		// Verifica se existe frequ�ncia de opera��o de coleta.
		List<Object[]> municipiosSemColeta = getMcdDAO().validateGeracaoMunicipiosSemOperacaoColeta(dtVigencia);
		if (!municipiosSemColeta.isEmpty()) {
			// Se algum munic�pio for retornado, � porque este n�o est� configurado no sistema.
			// See documenta��o do m�todo validateGeracaoMunicipiosSemOperacaoColeta para entender o que 
			// foi retornado no array.
			Object[] row = municipiosSemColeta.get(0);
			throw new BusinessException("LMS-29079",new Object[]{row[1]});
		}

		// Verifica se existe frequ�ncia de opera��o de entrega.
		List<Object[]> municipiosSemEntrega = getMcdDAO().validateGeracaoMunicipiosSemOperacaoEntrega(dtVigencia);
		if (!municipiosSemEntrega.isEmpty()) {
			// Se algum munic�pio for retornado, � porque este n�o est� configurado no sistema.
			// See documenta��o do m�todo validateGeracaoMunicipiosSemOperacaoEntrega para entender o que 
			// foi retornado no array.
			Object[] row = municipiosSemEntrega.get(0);
			throw new BusinessException("LMS-29080",new Object[]{row[1]});
		}

		Mcd oldMcd = getMcdDAO().findMcdVigente();
		if (oldMcd != null) {
			oldMcd.setDtVigenciaFinal(dtVigencia.minusDays(1));
			super.store(oldMcd);
		}

		Mcd mcd = new Mcd();
		mcd.setDtVigenciaInicial(dtVigencia);
		mcd.setDtVigenciaFinal(JTDateTimeUtils.MAX_YEARMONTHDAY);
		super.store(mcd);
	}

	/**
	 * 
	 * Retorna a quantidade de postos de passagem entre municipio de origem e filial de origem, filial de origem e filial de destino (utilizando rota de viagem)
	 * e filial destino e municipio destino
	 * @param idMunicipioFilialOrigem
	 * @param idMunicipioFilialDestino
	 * @param idMunicipioOrigem
	 * @param idMunicipioDestino
	 * @param dtConsulta
	 * @return
	 */
	private Integer findQtdPostosPassagemEntreMunicipiosAtendidos(Long idMunicipioFilialOrigem, Long idMunicipioFilialDestino, Long idMunicipioOrigem, Long idMunicipioDestino, YearMonthDay dtConsulta){
		if (dtConsulta == null)
			dtConsulta = JTDateTimeUtils.getDataAtual();

		Integer qtdPostosPassagemOrigem = Integer.valueOf(0);
		/*
		 * Retornar 0
		 */

		Integer qtdPostosPassagemDestino = Integer.valueOf(0);
		if (!idMunicipioDestino.equals(idMunicipioOrigem)) {
			qtdPostosPassagemDestino = postoPassagemMunicipioService.findQtdPostosPassagemEntreMunicipioEFilial(idMunicipioFilialDestino, dtConsulta);
		} else {
			qtdPostosPassagemDestino = postoPassagemMunicipioService.findQtdPostosPassagemBidirecionalEntreMunicipioEFilial(idMunicipioFilialDestino, dtConsulta);
		}

		Integer qtdPostosPassagemTrecho = findQtdPostoPassagemEntreFiliais(
				municipioFilialService.findById(idMunicipioFilialOrigem).getFilial().getIdFilial(),
				municipioFilialService.findById(idMunicipioFilialDestino).getFilial().getIdFilial(),
				dtConsulta
		);

		return Integer.valueOf(qtdPostosPassagemOrigem.intValue() + qtdPostosPassagemDestino.intValue() + qtdPostosPassagemTrecho.intValue());
	}

	/**
	 * Descobre o numero de postos de passagem entre duas filiais 
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param dtConsulta
	 * @return
	 */
	public Integer findQtdPostoPassagemEntreFiliais(Long idFilialOrigem,Long idFilialDestino, YearMonthDay dtConsulta) {
		if (dtConsulta == null)
			dtConsulta = JTDateTimeUtils.getDataAtual();
		return postoPassagemTrechoService.findQtdPostosPassagemByRotaFilialOrigemFilialDestino(idFilialOrigem, idFilialDestino, dtConsulta);
	}

	/**
	 * Busca a quantidade de postos de passagem entre dois municipios
	 * @param idMunicipioOrigem
	 * @param idMunicipioDestino
	 * @return Integer
	 */
	public Integer findQtdPostosPassagemEntreMunicipios(Long idMunicipioOrigem, Long idMunicipioDestino){
		Object[] origem = municipioFilialService.findMunicipioFilialVigenteByMunicipioPadraoMCD(idMunicipioOrigem);
		Object[] destino = municipioFilialService.findMunicipioFilialVigenteByMunicipioPadraoMCD(idMunicipioDestino);

		if (origem == null || destino == null)
			throw new BusinessException("LMS-29102");

		Long idMunicipioFilialOrigem = (Long) origem[0];
		Long idMunicipioFilialDestino = (Long) destino[0];

		return findQtdPostosPassagemEntreMunicipiosAtendidos(idMunicipioFilialOrigem, idMunicipioFilialDestino,idMunicipioOrigem, idMunicipioDestino, null);
	}

	/**
	 * Retorna a distancia existente entre os municipios
	 * @param idMunicipioFilialOrigem
	 * @param idMunicipioFilialDestino
	 * @param idServico
	 * @param dtConsulta
	 * @return
	 */
	private Integer findDistanciaEntreMuncipiosAtendidos(Long idMunicipioFilialOrigem, Long idMunicipioFilialDestino, Long idFilialOrigem, Long idFilialDestino, Long idServico){
		FluxoFilial fluxoFilial = this.findFluxoEntreFiliais(idFilialOrigem, idFilialDestino, idServico, null);
		Integer nrDistancia = fluxoFilial.getNrDistancia();
		if(fluxoFilial.getNrGrauDificuldade() != null) {
			nrDistancia = IntegerUtils.add(nrDistancia, fluxoFilial.getNrDistancia());
		}

		Integer nrDistanciaFilialDestino = municipioFilialService.findDistanciaTotalMunicipioFilial(idMunicipioFilialDestino);
		nrDistancia = IntegerUtils.add(nrDistancia, nrDistanciaFilialDestino);

		return nrDistancia;
	}

	/**
	 * Busca a distancia entre municipios
	 * @param idMunicipioOrigem
	 * @param idMunicipioDestino
	 * @param idServico
	 * @return Integer
	 */
	public Integer findDistanciaEntreMunicipiosSemGrauDificuldade(Long idMunicipioOrigem, Long idMunicipioDestino, Long idServico){
		Object[] origem = municipioFilialService.findMunicipioFilialVigenteByMunicipioPadraoMCD(idMunicipioOrigem);
		Object[] destino = municipioFilialService.findMunicipioFilialVigenteByMunicipioPadraoMCD(idMunicipioDestino);

		if (origem == null  || origem.length == 0 || destino == null || destino.length == 0)
			throw new BusinessException("LMS-29102");
		
		Long idMunicipioFilialOrigem = (Long)origem[0];
		Long idFilialOrigem = (Long)origem[1];

		Long idMunicipioFilialDestino = (Long)destino[0];
		Long idFilialDestino = (Long)destino[1];

		if (idMunicipioFilialOrigem == null || idMunicipioFilialDestino == null)
			throw new BusinessException("LMS-29102");

		FluxoFilial fluxoFilial = this.findFluxoEntreFiliais(idFilialOrigem, idFilialDestino, idServico, null);
		Integer nrDistancia = fluxoFilial.getNrDistancia();

		MunicipioFilial municipioFilial = municipioFilialService.findById(idMunicipioFilialDestino);
		if( municipioFilial.getNrDistanciaAsfalto() != null ){
			nrDistancia = IntegerUtils.add(nrDistancia, municipioFilial.getNrDistanciaAsfalto());
		}
		if( municipioFilial.getNrDistanciaChao() != null ){
			nrDistancia = IntegerUtils.add(nrDistancia, municipioFilial.getNrDistanciaChao());
		}
		return nrDistancia;
	}


	public Integer findDistanciaEntreMunicipios(Long idMunicipioOrigem, Long idMunicipioDestino, Long idServico){
		Object[] origem = municipioFilialService.findMunicipioFilialVigenteByMunicipioPadraoMCD(idMunicipioOrigem);
		Object[] destino = municipioFilialService.findMunicipioFilialVigenteByMunicipioPadraoMCD(idMunicipioDestino);

		if (origem == null  || origem.length == 0 || destino == null || destino.length == 0)
			throw new BusinessException("LMS-29102");
		
		Long idMunicipioFilialOrigem = (Long)origem[0];
		Long idFilialOrigem = (Long)origem[1];

		Long idMunicipioFilialDestino = (Long)destino[0];
		Long idFilialDestino = (Long)destino[1];

		if (idMunicipioFilialOrigem == null || idMunicipioFilialDestino == null)
			throw new BusinessException("LMS-29102");

		return findDistanciaEntreMuncipiosAtendidos(idMunicipioFilialOrigem, idMunicipioFilialDestino, idFilialOrigem, idFilialDestino, idServico);
	}

	/**
	 * Rotina que retorna qual tarifa sera cobrada entre os municipios atendidos de origem e destino 
	 * @param idMunicipioFilialOrigem
	 * @param idMunicipioFilialDestino
	 * @param idServico
	 * @return
	 */
	public Long findTarifa(Long distanciaMunicipiosAtendidos){
		
		Long idTarifaPreco = tarifaPrecoService.findByNrKm(distanciaMunicipiosAtendidos);

		if (idTarifaPreco == null){
			throw new BusinessException("LMS-29085", new Object[]{distanciaMunicipiosAtendidos});
		}	

		return idTarifaPreco;
	}

	/**
	 * Obtem a tarifa atual atrav�s da km 
	 * 
	 * CQPRO00025428
	 * 
	 * @param nrKm
	 * @return TarifaPreco
	 */
	private TarifaPreco findTarifaPrecoAtual(Long nrKm){		
		return tarifaPrecoService.findTarifaPrecoAtual(nrKm);
	}
	
	/**
	 * Obtem a tarifa antiga atrav�s da km 
	 * 
	 * CQPRO00025428
	 * 
	 * @param nrKm
	 * @return TarifaPreco
	 */
	private TarifaPreco findTarifaPrecoAntiga(Long nrKm){		
		return tarifaPrecoService.findTarifaPrecoAntiga(nrKm);
	}	

	/**
	 * Obtem as {@link TarifaPreco} antiga e atual 
	 * 
	 * Atrav�s do municipio de origem, destino e servi�o a distancia entre os
	 * municipios  e utiliza est� dist�ncia para obter as tarifas na tabela
	 * TARIFA_PRECO 
	 * 
	 * @param idMunicipioOrigem
	 * @param idMunicipioDestino
	 * @param idServico
	 * @return
	 */
	public TarifaPreco[] findTarifasPrecoLMS(Long idMunicipioOrigem, Long idMunicipioDestino, Long idServico){
		
		Integer	distanciaMunicipios = this.findDistanciaEntreMunicipios(idMunicipioOrigem, idMunicipioDestino, idServico);
		
		TarifaPreco[] tarifas = new TarifaPreco[2];
		
		tarifas[0] = findTarifaPrecoAtual(LongUtils.getLong(distanciaMunicipios));
		tarifas[1] = findTarifaPrecoAntiga(LongUtils.getLong(distanciaMunicipios));
		
		return tarifas;
	}
	
	/**
	 * Retorna qual tarifa ser� cobrada entre os munic�pios de origem e destino para determinado servi�o.
	 * @param idMunicipioOrigem
	 * @param idMunicipioDestino
	 * @param idServico
	 * @return
	 */
	public Long findTarifaMunicipios(Long idMunicipioOrigem, Long idMunicipioDestino, Long idServico) {
		Object[] municipioFilialOrigem = municipioFilialService.findMunicipioFilialVigenteByMunicipioPadraoMCD(idMunicipioOrigem);
		Object[] municipioFilialDestino = municipioFilialService.findMunicipioFilialVigenteByMunicipioPadraoMCD(idMunicipioDestino);

		if (municipioFilialDestino == null || municipioFilialDestino.length <=0) {
			Map municipioDestino = municipioService.findDadosMunicipioById(idMunicipioDestino);
			String nmMunicipio = (String) municipioDestino.get("nmMunicipio") + " - " + (String) municipioDestino.get("sgUnidadeFederativa");
			throw new BusinessException("LMS-29080", new Object[]{nmMunicipio});
		}

		Long idMunicipioFilialOrigem = (Long) municipioFilialOrigem[0];
		Long idMunicipioFilialDestino = (Long) municipioFilialDestino[0];

		Long idFilialOrigem = (Long) municipioFilialOrigem[1];
		Long idFilialDestino = (Long) municipioFilialDestino[1];

		Integer	distanciaMunicipiosAtendidos = findDistanciaEntreMuncipiosAtendidos(idMunicipioFilialOrigem, idMunicipioFilialDestino, idFilialOrigem, idFilialDestino, idServico);

		return this.findTarifa(Long.valueOf(distanciaMunicipiosAtendidos));
	}

	/**
	 * Retorna a dist�ncia existente entre os munic�pios
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idServico
	 * @param dtConsulta
	 * @return
	 */
	public FluxoFilial findFluxoEntreFiliais(Long idFilialOrigem, Long idFilialDestino, Long idServico, YearMonthDay dtConsulta) {
		if (dtConsulta == null)
			dtConsulta = JTDateTimeUtils.getDataAtual();

		FluxoFilial fluxoFilial = fluxoFilialService.findFluxoFilial(idFilialOrigem, idFilialDestino, dtConsulta, idServico);
		if (fluxoFilial == null) {
			Filial filialOrigem = filialService.findById(idFilialOrigem);
			Filial filialDestino = filialService.findById(idFilialDestino);
			throw new BusinessException("LMS-29084", new Object[]{filialOrigem.getSiglaNomeFilial(), filialDestino.getSiglaNomeFilial()});
		}
		return fluxoFilial;
	}

	/**
	 * Retorna o mcd vigente na data informada
	 * @param dtVigencia
	 * @return MCD
	 */
	public Mcd findMcdByVigente(YearMonthDay dtVigencia){
		List<Mcd> lstMcds = getMcdDAO().findMcdVigente(dtVigencia);
		if (!lstMcds.isEmpty()) {
			return (Mcd)lstMcds.get(0);
		} else return null;
	}

	/**
	 * LMS-2537
	 * Retorna o valor do calculo de pontos de passagens entre um fluxo de carga
	 *  
	 * @param idMunicipioOrigem
	 * @param idMunicipioDestino
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param dtPesquisa
	 * @param idFFServico
	 * @return
	 */
	public Integer countPostosPassagens(Long idMunicipioOrigem, Long idMunicipioDestino, Long idFilialOrigem, Long idFilialDestino,
			YearMonthDay dtPesquisa, Long idFFServico) {
		
		return postoPassagemMunicipioService.countPostosPassagens(idMunicipioOrigem, idMunicipioDestino, idFilialOrigem, idFilialDestino, dtPesquisa, idFFServico);
	}

	/**
	 * @param postoPassagemMunicipioService The postoPassagemMunicipioService to set.
	 */
	public void setPostoPassagemMunicipioService(PostoPassagemMunicipioService postoPassagemMunicipioService) {
		this.postoPassagemMunicipioService = postoPassagemMunicipioService;
	}
	/**
	 * @param municipioFilialService The municipioFilialService to set.
	 */
	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}
	/**
	 * @param fluxoFilialService The fluxoFilialService to set.
	 */
	public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
		this.fluxoFilialService = fluxoFilialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	/**
	 * @param tarifaPrecoService The tarifaPrecoService to set.
	 */
	public void setTarifaPrecoService(TarifaPrecoService tarifaPrecoService) {
		this.tarifaPrecoService = tarifaPrecoService;
	}
	public void setPostoPassagemTrechoService(PostoPassagemTrechoService postoPassagemTrechoService) {
		this.postoPassagemTrechoService = postoPassagemTrechoService;
	}

	public MunicipioService getMunicipioService() {
		return municipioService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

}