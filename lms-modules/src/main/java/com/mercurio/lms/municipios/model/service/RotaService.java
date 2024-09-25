package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.municipios.model.Rota;
import com.mercurio.lms.municipios.model.dao.RotaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.rotaService"
 */
public class RotaService extends CrudService<Rota, Long> {
	
	private FilialRotaService filialRotaService;
	private HistoricoFilialService historicoFilialService;
	
	public Integer getRowCountRotasViagem(TypedFlatMap criteria) {
		return getRotaDAO().getRowCountRotasViagem(criteria);
	}
	
	public Integer getRowCountRotaViagemEventual(TypedFlatMap criteria) {
		return getRotaDAO().getRowCountRotaViagemEventual(criteria);
	}

	public ResultSetPage findPaginatedRotasViagem(TypedFlatMap criteria) {
		return getRotaDAO().findPaginatedRotasViagem(criteria, FindDefinition.createFindDefinition(criteria));
	}
	
	public ResultSetPage findPaginatedRotaViagemEventual(TypedFlatMap criteria) {
		return getRotaDAO().findPaginatedRotaViagemEventual(criteria, FindDefinition.createFindDefinition(criteria));
	}

	/**
	 * Recupera uma inst�ncia de <code>Rota</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public Rota findByIdPersonalizado(java.lang.Long id) {
		Rota rota = new Rota();
		Rota rotaDs = getRotaDAO().findByIdRota(id);
		List filiaisRota = filialRotaService.findFiliaisRotaByRota(id);
		rota.setFilialRotas(filiaisRota);
		rota.setDsRota(rotaDs.getDsRota());
		rota.setIdRota(rotaDs.getIdRota());
		return rota;
	}

	/**
	 * Retorna id da Rota com a descri��o informada.
	 * 
	 * @author Felipe Ferreira
	 * @param dsRota descri��o da rota com filiais separadas por h�fen.
	 * @return id se encontrar, nulo, caso negativo.
	 */
	public Long findIdRotaByDescricao(String dsRota) {
		return getRotaDAO().findIdRotaByDescricao(dsRota);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		Rota rota = (Rota)findById(id);
		if(rota.getFilialRotas()!= null && !rota.getFilialRotas().isEmpty()){
			for (Iterator iter = rota.getFilialRotas().iterator();iter.hasNext();){
				FilialRota filialRota = (FilialRota)iter.next();
				filialRotaService.removeById(filialRota.getIdFilialRota());
			}
			rota.getFilialRotas().clear();
		}
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		for(int i=0; i< ids.size(); i++){
			Long id = (Long)ids.get(i);
			Rota rota = (Rota)findById(id);
			if(rota.getFilialRotas()!= null && !rota.getFilialRotas().isEmpty()){
				for (Iterator iter = rota.getFilialRotas().iterator();iter.hasNext();){
					FilialRota filialRota = (FilialRota)iter.next();
					filialRotaService.removeById(filialRota.getIdFilialRota());
				}
				rota.getFilialRotas().clear();
			}
		}
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Rota bean) {
		return super.store(bean);
	}
	
	public Long storeRotaViagem(List filiais, String dsRota){
		Long maxID = this.findMaxIdRota();
		Rota rota = this.storeValidaByFilialRotas(filiais);
		Long idRota = rota.getIdRota();
		
		if(maxID.longValue() >= idRota.longValue())
			throw new BusinessException("uniqueConstraintViolated");
		else 
			return idRota;
	}

	/**
	 * FilialSimple � uma classe usada para manter dados e informa��es b�sicas
	 * da filial. N�o h� um POJO do sistema que mantenha estas informa��es encapsuladas
	 * em um s� objeto.
	 * 
	 * @author felipef
	 */
	private class FilialSimple {
		Long idFilial;
		String sgFilial;
		String tpFilial;
		
		/**
		 * Construtor sobrescrito para FilialSimple
		 */
		FilialSimple(Long idFilial, String sgFilial, String tpFilial) {
			this.idFilial = idFilial;
			this.sgFilial = sgFilial;
			this.tpFilial = tpFilial;
		}
		
		/**
		 * Retorna um objeto de Filial com idFilial e sgFilial preenchidos.
		 * @return Filial
		 */
		Filial toFilial() {
			Filial filial = new Filial();
			filial.setIdFilial(idFilial);
			filial.setSgFilial(sgFilial);
			return filial;
		}
	}
	
	/**
	 * Adciona uma filial recebida a uma lista de FilialSimple. 
	 * 
	 * @param filiais lista que ser� manipulada.
	 * @param filial filial que deve ser adcionada � lista.
	 */
	private void mountListFilialSimples(List<FilialSimple> filiais, Filial filial) {
		Long idFilial = filial.getIdFilial();
		HistoricoFilial hf = historicoFilialService.findHistoricoFilialVigente(idFilial);
		
		// Classe private FilialSimple sendo instanciada.
		FilialSimple filialSimple =
			new FilialSimple(
					idFilial,
					filial.getSgFilial(),
					hf.getTpFilial().getValue());
		filiais.add(filialSimple);
	}
	
	/**
	 * Valida a exist�ncia de uma rota. Caso a rota ainda n�o exista no sistema,
	 * uma nova rota � inserida.
	 * 
	 * @param filialRotas um objeto que implemente a interface List e seja composta
	 * de objetos da classe FilialRota
	 * @return uma inst�ncia da Rota encontrada ou da Rota inserida.
	 */
	public Rota storeValidaByFilialRotas(List<FilialRota> filialRotas) {
		List<FilialSimple> filiais = new ArrayList<FilialSimple>();
		// Primeiramente deve construir uma Collection de FilialSimple
		for (FilialRota filialRota : filialRotas) {
			Filial filial = filialRota.getFilial();
			mountListFilialSimples(filiais, filial);
		}
		// M�todo default para valida��o da rota
		return this.validaRota(filiais);
	}

	/**
	 * Valida a exist�ncia de uma rota. Caso a rota ainda n�o exista no sistema,
	 * uma nova rota � inserida.
	 * 
	 * @param listaFiliais um objeto que implemente a interface List e seja composta
	 * de objetos da classe Filial
	 * @return uma inst�ncia da Rota encontrada ou da Rota inserida.
	 */
	public Rota storeValidaByFiliais(List<Filial> listaFiliais) {
		List<FilialSimple> filiais = new ArrayList<FilialSimple>();
		// Primeiramente deve construir uma Collection de FilialSimple
		for (Filial filial : listaFiliais) {
			mountListFilialSimples(filiais, filial);
		}
		// M�todo default para valida��o da rota
		return this.validaRota(filiais);	
	}

	/**
	 * M�todo principal da valida��o da rota.
	 * Aqui encontra-se o core da valida��o da Rota, assim como a inser��o de uma rota nova
	 * recebida na lista.
	 * @param filiais Collection de FilialSimple.
	 * @return inst�ncia de Rota que for encontrada ou inserida.
	 */
	private Rota validaRota(List<FilialSimple> filiais) {
		/** A rota n�o pode ter apenas uma filial. */
		if (filiais.size() <= 1) {
			throw new BusinessException("LMS-26066");
		}
		
		/** Duas vari�veis de apoio.
		 * 	Estas vari�veis indicar�o informa��es da rota a ser inserida, se for o caso */
		boolean blEnvolveParceira = false;
		String dsRota = null;
		
		/** � usado a primeira filial para iniciar as valida��es. */
		FilialSimple filial = filiais.get(0);
		/** A primeira filial da rota n�o pode ser parceira. */
		if (filial.tpFilial.equals("PA")) {
			throw new BusinessException("LMS-26102");
		} else {
			/** Caso n�o for parceira, iniciamos a descri��o da rota */  
			dsRota = filial.sgFilial;
		}
		
		/** � iterado a lista de filiais para montar a descri��o da rota e validar se envolve parceira.
		 *  A primeira filial � descartada pois ela j� foi validada e j� est� na rota. */
		for (int i = 1 ; i < filiais.size() ; i++) {
			// Recupera a filial da lista.
			filial = filiais.get(i);
			// Atualiza a descri��o.
			dsRota += "-" + filial.sgFilial;
			// Valida se a filial � parceira, alterando a flag blEnvolve parceira se sim.
			if (filial.tpFilial.equals("PA")) {
				blEnvolveParceira = true;
			}
		}
		
		/** Encontra todas as rotas com a mesma descri��o da rota recebida por par�metro. */
		List<Rota> rotas = this.find(dsRota,blEnvolveParceira);
		// se retornou pelo menos uma rota:
		if (!rotas.isEmpty()) {
			/** Se n�o envolve parceira, retorna a rota encontrada.
			 *  O neg�cio garante que apenas uma Rota foi encontrada. */
			if (!blEnvolveParceira) {
				return rotas.get(0);
			} else {
				/** Se alguma rota que envolve parceira foi encontrada, devemos validar todas as filiais
				 *  para validar que todas as filiais que est�o na rota coincidem com as rotas recebidas
				 *  por par�metro no m�todo. */ 
				forRotas: // label do 'for' externo que percorre as rotas encontradas.
				for (Rota rota : rotas) {
					/** retorna uma lista com os objetos FilialRota da rota informada */
					List<Filial> filiaisRota = filialRotaService.findFiliaisRotaByRotaPojos(rota.getIdRota());
					
					// Garantia de que as listas s�o consistentes e possuem o mesmo tamanho.
					// A princ�pio, � um overhead, Mas garante que n�o ocorra um poss�vel ArrayIndexOutOfBoundsException 
					if (filiaisRota.size() == filiais.size()) {
						// Realiza-se uma itera��o sobre os �ndices das listas.
						// � pego o tamanho de qualquer uma das listas, porque os tamanhos s�o iguais.
						for (int i = 0 ; i < filiaisRota.size() ; i++) {
							/** Aten��o, pois uma lista � de Filial e a outra � de FilialSimple. */
							Filial filial1 = (Filial)filiaisRota.get(i);
							FilialSimple filial2 = (FilialSimple)filiais.get(i);
							
							/** Se as filiais forem diferentes, a rota n�o ser� igual.
							 *  Com isso, interrompemos a itera��o e tentamos validar a pr�xima rota.
							 */ 
							if (!filial1.getIdFilial().equals(filial2.idFilial)) {
								break forRotas;
							}
						}
						/** Caso a itera��o pelas filiais ocorreu at� o final, significa que
						 *  todas as filiais do objeto 'rota' j� existente no sistema coincidem
						 *  com a lista de filiais recebida.
						 *  Com isto, � retornado a rota encontrada no sistema. 
						 */
						return rota;
					}
				}
			}
		}
		
		/**
		 * Se a rotina chegar a este ponto, significa que n�o existe uma rota no sistema que coincide com
		 * a rota passada por par�metro.
		 */
		Rota rota = new Rota();
		// dsRota gerado em itera��es anteriores.
		rota.setDsRota(dsRota);
		// blEnvolveParceira consistido em itera��es anteriores.
		rota.setBlEnvolveParceira(blEnvolveParceira);
		this.store(rota);

		// n�mero de filiais na rota.
		int numFiliais = filiais.size();
		/** Salva registros na tabela filialRota. */
		for (int i = 0 ; i < numFiliais ; i++) {
			// Recupera objeto FilialSimple que cont�m as filiais da rota. 
			FilialSimple filialToStore = filiais.get(i);
			
			/** Cria e popula novo objeto de Filial rota */
			FilialRota filialRota = new FilialRota();
			filialRota.setFilial(filialToStore.toFilial());
			
			filialRota.setRota(rota);
			// O n�mero da ordem come�a em 1. Como o index do array come�a em 0, somamos 1 ao index.
			filialRota.setNrOrdem(Byte.valueOf(String.valueOf(i+1)));

			// O item de index 0 � a origem.
			filialRota.setBlOrigemRota(Boolean.valueOf(i == 0));
			// O item de index tem que ser um n�mero anterior ao n�mero de filiais na lista.
			filialRota.setBlDestinoRota(Boolean.valueOf((i+1) == numFiliais));

			// Salva a filial rota.
			filialRotaService.store(filialRota);
		}
		
		/** Retorna a nova rota. */
		return rota;
	}
	
	/**
	 * Retorna uma entidade com os par�metros informados.
	 * 
	 * @author Felipe Ferreira
	 * @param dsRota descri��o da rota com filiais separadas por h�fen.
	 * @param blEnvolveParceira true se a rota deve envolver parceira.
	 * @return Rota se encontrar, nulo, caso negativo.
	 */
	public List<Rota> find(String dsRota, Boolean blEnvolveParceira) {
		return getRotaDAO().find(dsRota, blEnvolveParceira);
	}
	

	/** Valida se a rota esta vigente no periodo informado
	 * @author Samuel Herrmann
	 * @param idRota
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public boolean validateIsVigenteEm(Long idRota, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		Rota rota = getRotaDAO().findByIdVigenteEm(idRota,dtVigenciaInicial,dtVigenciaFinal);
		if (rota == null)
			return false;
		return true;
	}

	public Rota findById(Long id) {
		return (Rota)super.findById(id);
	}

	public Long findMaxIdRota(){
		List lista = getRotaDAO().findMaxIdRota();
		return (Long)lista.get(0);
	}

	/**
	 * @param idRota (required)
	 * @return 
	 */
	public Rota findToTrechosById(Long idRota) {
		return getRotaDAO().findToTrechosById(idRota);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * @param Inst�ncia do DAO.
	 */
	public void setRotaDAO(RotaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * @return Inst�ncia do DAO.
	 */
	private final RotaDAO getRotaDAO() {
		return (RotaDAO) getDao();
	}
	
	public void setFilialRotaService(FilialRotaService filialRotaService) {
		this.filialRotaService = filialRotaService;
	}

	public void setHistoricoFilialService(
			HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}
}