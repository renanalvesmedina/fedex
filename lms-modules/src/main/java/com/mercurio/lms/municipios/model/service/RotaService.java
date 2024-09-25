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
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
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
	 * Recupera uma instância de <code>Rota</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
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
	 * Retorna id da Rota com a descrição informada.
	 * 
	 * @author Felipe Ferreira
	 * @param dsRota descrição da rota com filiais separadas por hífen.
	 * @return id se encontrar, nulo, caso negativo.
	 */
	public Long findIdRotaByDescricao(String dsRota) {
		return getRotaDAO().findIdRotaByDescricao(dsRota);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
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
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
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
	 * FilialSimple é uma classe usada para manter dados e informações básicas
	 * da filial. Não há um POJO do sistema que mantenha estas informações encapsuladas
	 * em um só objeto.
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
	 * @param filiais lista que será manipulada.
	 * @param filial filial que deve ser adcionada à lista.
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
	 * Valida a existência de uma rota. Caso a rota ainda não exista no sistema,
	 * uma nova rota é inserida.
	 * 
	 * @param filialRotas um objeto que implemente a interface List e seja composta
	 * de objetos da classe FilialRota
	 * @return uma instância da Rota encontrada ou da Rota inserida.
	 */
	public Rota storeValidaByFilialRotas(List<FilialRota> filialRotas) {
		List<FilialSimple> filiais = new ArrayList<FilialSimple>();
		// Primeiramente deve construir uma Collection de FilialSimple
		for (FilialRota filialRota : filialRotas) {
			Filial filial = filialRota.getFilial();
			mountListFilialSimples(filiais, filial);
		}
		// Método default para validação da rota
		return this.validaRota(filiais);
	}

	/**
	 * Valida a existência de uma rota. Caso a rota ainda não exista no sistema,
	 * uma nova rota é inserida.
	 * 
	 * @param listaFiliais um objeto que implemente a interface List e seja composta
	 * de objetos da classe Filial
	 * @return uma instância da Rota encontrada ou da Rota inserida.
	 */
	public Rota storeValidaByFiliais(List<Filial> listaFiliais) {
		List<FilialSimple> filiais = new ArrayList<FilialSimple>();
		// Primeiramente deve construir uma Collection de FilialSimple
		for (Filial filial : listaFiliais) {
			mountListFilialSimples(filiais, filial);
		}
		// Método default para validação da rota
		return this.validaRota(filiais);	
	}

	/**
	 * Método principal da validação da rota.
	 * Aqui encontra-se o core da validação da Rota, assim como a inserção de uma rota nova
	 * recebida na lista.
	 * @param filiais Collection de FilialSimple.
	 * @return instância de Rota que for encontrada ou inserida.
	 */
	private Rota validaRota(List<FilialSimple> filiais) {
		/** A rota não pode ter apenas uma filial. */
		if (filiais.size() <= 1) {
			throw new BusinessException("LMS-26066");
		}
		
		/** Duas variáveis de apoio.
		 * 	Estas variáveis indicarão informações da rota a ser inserida, se for o caso */
		boolean blEnvolveParceira = false;
		String dsRota = null;
		
		/** É usado a primeira filial para iniciar as validações. */
		FilialSimple filial = filiais.get(0);
		/** A primeira filial da rota não pode ser parceira. */
		if (filial.tpFilial.equals("PA")) {
			throw new BusinessException("LMS-26102");
		} else {
			/** Caso não for parceira, iniciamos a descrição da rota */  
			dsRota = filial.sgFilial;
		}
		
		/** É iterado a lista de filiais para montar a descrição da rota e validar se envolve parceira.
		 *  A primeira filial é descartada pois ela já foi validada e já está na rota. */
		for (int i = 1 ; i < filiais.size() ; i++) {
			// Recupera a filial da lista.
			filial = filiais.get(i);
			// Atualiza a descrição.
			dsRota += "-" + filial.sgFilial;
			// Valida se a filial é parceira, alterando a flag blEnvolve parceira se sim.
			if (filial.tpFilial.equals("PA")) {
				blEnvolveParceira = true;
			}
		}
		
		/** Encontra todas as rotas com a mesma descrição da rota recebida por parâmetro. */
		List<Rota> rotas = this.find(dsRota,blEnvolveParceira);
		// se retornou pelo menos uma rota:
		if (!rotas.isEmpty()) {
			/** Se não envolve parceira, retorna a rota encontrada.
			 *  O negócio garante que apenas uma Rota foi encontrada. */
			if (!blEnvolveParceira) {
				return rotas.get(0);
			} else {
				/** Se alguma rota que envolve parceira foi encontrada, devemos validar todas as filiais
				 *  para validar que todas as filiais que estão na rota coincidem com as rotas recebidas
				 *  por parâmetro no método. */ 
				forRotas: // label do 'for' externo que percorre as rotas encontradas.
				for (Rota rota : rotas) {
					/** retorna uma lista com os objetos FilialRota da rota informada */
					List<Filial> filiaisRota = filialRotaService.findFiliaisRotaByRotaPojos(rota.getIdRota());
					
					// Garantia de que as listas são consistentes e possuem o mesmo tamanho.
					// A princípio, é um overhead, Mas garante que não ocorra um possível ArrayIndexOutOfBoundsException 
					if (filiaisRota.size() == filiais.size()) {
						// Realiza-se uma iteração sobre os índices das listas.
						// É pego o tamanho de qualquer uma das listas, porque os tamanhos são iguais.
						for (int i = 0 ; i < filiaisRota.size() ; i++) {
							/** Atenção, pois uma lista é de Filial e a outra é de FilialSimple. */
							Filial filial1 = (Filial)filiaisRota.get(i);
							FilialSimple filial2 = (FilialSimple)filiais.get(i);
							
							/** Se as filiais forem diferentes, a rota não será igual.
							 *  Com isso, interrompemos a iteração e tentamos validar a próxima rota.
							 */ 
							if (!filial1.getIdFilial().equals(filial2.idFilial)) {
								break forRotas;
							}
						}
						/** Caso a iteração pelas filiais ocorreu até o final, significa que
						 *  todas as filiais do objeto 'rota' já existente no sistema coincidem
						 *  com a lista de filiais recebida.
						 *  Com isto, é retornado a rota encontrada no sistema. 
						 */
						return rota;
					}
				}
			}
		}
		
		/**
		 * Se a rotina chegar a este ponto, significa que não existe uma rota no sistema que coincide com
		 * a rota passada por parâmetro.
		 */
		Rota rota = new Rota();
		// dsRota gerado em iterações anteriores.
		rota.setDsRota(dsRota);
		// blEnvolveParceira consistido em iterações anteriores.
		rota.setBlEnvolveParceira(blEnvolveParceira);
		this.store(rota);

		// número de filiais na rota.
		int numFiliais = filiais.size();
		/** Salva registros na tabela filialRota. */
		for (int i = 0 ; i < numFiliais ; i++) {
			// Recupera objeto FilialSimple que contém as filiais da rota. 
			FilialSimple filialToStore = filiais.get(i);
			
			/** Cria e popula novo objeto de Filial rota */
			FilialRota filialRota = new FilialRota();
			filialRota.setFilial(filialToStore.toFilial());
			
			filialRota.setRota(rota);
			// O número da ordem começa em 1. Como o index do array começa em 0, somamos 1 ao index.
			filialRota.setNrOrdem(Byte.valueOf(String.valueOf(i+1)));

			// O item de index 0 é a origem.
			filialRota.setBlOrigemRota(Boolean.valueOf(i == 0));
			// O item de index tem que ser um número anterior ao número de filiais na lista.
			filialRota.setBlDestinoRota(Boolean.valueOf((i+1) == numFiliais));

			// Salva a filial rota.
			filialRotaService.store(filialRota);
		}
		
		/** Retorna a nova rota. */
		return rota;
	}
	
	/**
	 * Retorna uma entidade com os parâmetros informados.
	 * 
	 * @author Felipe Ferreira
	 * @param dsRota descrição da rota com filiais separadas por hífen.
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
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * @param Instância do DAO.
	 */
	public void setRotaDAO(RotaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 * @return Instância do DAO.
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