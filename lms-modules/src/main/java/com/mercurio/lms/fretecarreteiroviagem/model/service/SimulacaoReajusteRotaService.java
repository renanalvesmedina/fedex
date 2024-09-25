package com.mercurio.lms.fretecarreteiroviagem.model.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.fretecarreteiroviagem.model.AplicaReajusteRota;
import com.mercurio.lms.fretecarreteiroviagem.model.SimulacaoReajusteRota;
import com.mercurio.lms.fretecarreteiroviagem.model.dao.SimulacaoReajusteRotaDAO;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.service.RotaViagemService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.fretecarreteiroviagem.simulacaoReajusteRotaService"
 */
public class SimulacaoReajusteRotaService extends CrudService<SimulacaoReajusteRota, Long> {

	public static final int VIGENTE_EM = 1;
	public static final int VIGENTE_DESPOIS_DE = 2;
	private RotaViagemService rotaViagemService;
	/**
	 * Recupera uma instância de <code>SimulacaoReajusteRota</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public SimulacaoReajusteRota findById(java.lang.Long id) {
        return (SimulacaoReajusteRota)super.findById(id);
    }
    
    public Map findByIdCustom(Long idSimulacao) {
    	return getSimulacaoReajusteRotaDAO().findByIdCustom(idSimulacao);
    }
    public List findRotasBySimulacao(Long idSimulacao) {
    	return getSimulacaoReajusteRotaDAO().findRotasBySimulacao(idSimulacao);
    }
    public void validatePossibilidadeReajuste(SimulacaoReajusteRota simulacao) {
    	if (simulacao.getTpReajuste().getValue().equals("V") &&
    			simulacao.getMoedaPais() == null)
    		throw new BusinessException("LMS-24024");
    	
    	List rs = findRotasByCriteriasVigenciaMaior(simulacao);
    	if (rs.size() > 0) {
    		StringBuffer rotas = new StringBuffer();
    		String token = null;
    		//Não mostrar mais do que 90 rotas para que a mensagem caiba na tela
    		int cont = 0;
    		for(Iterator i = rs.iterator(); i.hasNext();) {
    			cont++;
    			Object[] projections = (Object[])i.next();
    			String rota = new StringBuffer(new DecimalFormat("0000").format(((Integer)projections[0]).doubleValue())).append(" ").append((String)projections[1]).toString();
    			if (token == null) {
    				token = ", ";
    				rotas.append(rota);
    			} else rotas.append(token).append(rota);
    			if (cont > 80) {
        			rotas.append(token).append("...");
    				break;
    			}
    		}
    		throw new BusinessException("LMS-24023",new Object[]{rotas.toString()});
    	}
    		
    		
    }
    public void executeEfetivacao(SimulacaoReajusteRota simulacao) {
    	validatePossibilidadeReajuste(simulacao);

    	List rotasDb = findRotasByCriteriasVigente(simulacao);
    	if (rotasDb.size() > 0) {
	    	Map rotasViagem = new HashMap();
	    	List rotasIdaVolta = new ArrayList();
	    	Iterator i = rotasDb.iterator();
	    	RotaIdaVolta rotaIdaVolta = (RotaIdaVolta)i.next();
	    	Long idRotaViagemOld = rotaIdaVolta.getRotaViagem().getIdRotaViagem();
	    	do {
	    		if (!idRotaViagemOld.equals(rotaIdaVolta.getRotaViagem().getIdRotaViagem())) {
	    			rotasViagem.put(idRotaViagemOld,rotasIdaVolta);
	    			idRotaViagemOld = rotaIdaVolta.getRotaViagem().getIdRotaViagem();
	    			rotasIdaVolta = new ArrayList();
	    			TypedFlatMap detail = new TypedFlatMap();
	    			detail.put("nrRota",rotaIdaVolta.getNrRota());
	    			detail.put("idFilialOrigem",getSimulacaoReajusteRotaDAO().findFilialOrigem(rotaIdaVolta.getIdRotaIdaVolta()));
	    			rotasIdaVolta.add(detail);
	    		}else{
	    			TypedFlatMap detail = new TypedFlatMap();
	    			detail.put("nrRota",rotaIdaVolta.getNrRota());
	    			detail.put("idFilialOrigem",getSimulacaoReajusteRotaDAO().findFilialOrigem(rotaIdaVolta.getIdRotaIdaVolta()));
	    			rotasIdaVolta.add(detail);
	    		}
	    		if (i.hasNext())
	    			rotaIdaVolta = (RotaIdaVolta)i.next();
	    		else{
	    			rotasViagem.put(idRotaViagemOld,rotasIdaVolta);
	    			break;
	    		}
	       }while(true);
	       
	       for(i = rotasViagem.keySet().iterator(); i.hasNext();) {
	    	   Long key = (Long)i.next();
	    	   Object[] details = ((List)rotasViagem.get(key)).toArray();
	    	   Long newId = rotaViagemService.storeAlterarRota(key,simulacao.getDtVigenciaInicial());
	    	   int scale = 3;
	    	   int round = BigDecimal.ROUND_UP;
	    	   for(int x = 0; x < details.length; x++) {
	    		   TypedFlatMap detail = (TypedFlatMap)details[x];
	    		   RotaIdaVolta riv = (RotaIdaVolta)getSimulacaoReajusteRotaDAO().getAdsmHibernateTemplate().findUniqueResult(new StringBuffer("SELECT RIV FROM ").append(RotaIdaVolta.class.getName()).append(" AS RIV ")
	    				   .append("INNER JOIN RIV.rotaViagem RV ")
	    				   .append("INNER JOIN RIV.rota R ")
	    				   .append("INNER JOIN R.filialRotas FR ")
	    				   .append("INNER JOIN FR.filial F ")
    		       	   	   .append("WHERE RV.id = ? and RIV.nrRota = ? and RV.dtVigenciaInicial = ? ")
    		       	   	   .append("AND F.id = ? AND FR.blOrigemRota = ? ").toString(),new Object[] {newId,detail.getInteger("nrRota"),simulacao.getDtVigenciaInicial(),detail.getLong("idFilialOrigem"),Boolean.TRUE});
		    	   
		    		if (simulacao.getTpReajuste().getValue().equals("V"))
		    			   riv.setVlFreteKm(riv.getVlFreteKm().add(simulacao.getVlReajuste()).setScale(scale,round));
		    		   else
		    			   riv.setVlFreteKm(simulacao.getVlReajuste().divide(BigDecimal.valueOf(100),5,BigDecimal.ROUND_UP).multiply(riv.getVlFreteKm()).add(riv.getVlFreteKm()).setScale(scale,round));
		    	  getSimulacaoReajusteRotaDAO().getAdsmHibernateTemplate().saveOrUpdate(riv);
	    	   }
	       }
	       simulacao.setTpSituacaoRota(new DomainValue("E"));
	       store(simulacao);
    	}
    }
    
    
    
    
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	getSimulacaoReajusteRotaDAO().deleteFilhos(id);
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
		for(Iterator i = ids.iterator(); i.hasNext();)
			removeById((Long)i.next());
    }
    public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
    	return getSimulacaoReajusteRotaDAO().findPaginated(criteria,findDef);
    }
    public Integer getRowCount(TypedFlatMap criteria) {
    	return getSimulacaoReajusteRotaDAO().getRowCount(criteria);
    }
    protected SimulacaoReajusteRota beforeUpdate(SimulacaoReajusteRota bean) {
    	getSimulacaoReajusteRotaDAO().deleteFilhos(((SimulacaoReajusteRota)bean).getIdSimulacaoReajusteRota());
    	return super.beforeUpdate(bean);
    }
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(SimulacaoReajusteRota bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setSimulacaoReajusteRotaDAO(SimulacaoReajusteRotaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Essa função busca todas as rotas de ida e volta com data de vigencia inicial maior que a data de vigencia informada
     * blAplica é a unica exceção que só é obrigatorio se for informado a lista de idsRotaIdaVolta
     * @param idRegionalOrigem 
     * @param idFilialOrigem
     * @param idRegionalDestino
     * @param idFilialDestino
     * @param tpRota
     * @param idTipoMeioTransporte
     * @param idMoedaPais
     * @param idMeioTransporte
     * @param idProprietario
     * @param idsRotaIdaVolta (Long)
     * @param blAplica
     * @param dtVigencia required
     * @return
     */
    public List findRotasByCriteriasVigenciaMaior(Long idRegionalOrigem, Long idFilialOrigem, Long idRegionalDestino, Long idFilialDestino,
    		String tpRota, Long idTipoMeioTransporte, Long idMoedaPais, Long idMeioTransporte, Long idProprietario, List idsRotaIdaVolta, Boolean blAplica,YearMonthDay dtVigencia) {
    	return getSimulacaoReajusteRotaDAO().findRotasByCriteriasVigenciaMaior(idRegionalOrigem,idFilialOrigem,idRegionalDestino,idFilialDestino,tpRota,idTipoMeioTransporte,idMoedaPais,idMeioTransporte,idProprietario,idsRotaIdaVolta,blAplica,dtVigencia);
    }
    public List findRotasByCriteriasVigenciaMaior(SimulacaoReajusteRota simulacao) {
    	Long idRegionalOrigem = (simulacao.getRegionalOrigem() != null) ? simulacao.getRegionalOrigem().getIdRegional() : null;
    	Long idRegionalDestino = (simulacao.getRegionalDestino() != null) ? simulacao.getRegionalDestino().getIdRegional() : null;
    	Long idFilialOrigem = (simulacao.getFilialOrigem() != null) ? simulacao.getFilialOrigem().getIdFilial() : null;
    	Long idFilialDestino = (simulacao.getFilialDestino() != null) ? simulacao.getFilialDestino().getIdFilial() : null;
    	Long idTipoMeioTransporte = (simulacao.getTipoMeioTransporte() != null) ? simulacao.getTipoMeioTransporte().getIdTipoMeioTransporte() : null;
    	Long idMeioTransporte = (simulacao.getMeioTransporte() != null) ? simulacao.getMeioTransporte().getIdMeioTransporte() : null;
    	Long idMoedaPais = (simulacao.getMoedaPais() != null) ? simulacao.getMoedaPais().getIdMoedaPais() : null;
    	Long idProprietario = (simulacao.getProprietario() != null) ? simulacao.getProprietario().getIdProprietario() : null;
    	String tpRota = (simulacao.getTpRota() != null) ? simulacao.getTpRota().getValue() : null;
    	YearMonthDay dtVigencia = simulacao.getDtVigenciaInicial();
    	List idsRotaIdaVolta = new ArrayList();
    	Boolean blAplica = null;
    	if (simulacao.getParametroSimulacaoRotas() != null && !simulacao.getParametroSimulacaoRotas().isEmpty()) {
    		for(Iterator i = simulacao.getParametroSimulacaoRotas().iterator(); i.hasNext();) {
    			AplicaReajusteRota reajusteRota = (AplicaReajusteRota)i.next();
    			if (blAplica == null)
    				blAplica = reajusteRota.getBlAplicacao();
    			idsRotaIdaVolta.add(reajusteRota.getRotaIdaVolta().getNrRota());
    		}
    	}
    	return findRotasByCriteriasVigenciaMaior(idRegionalOrigem,idFilialOrigem,idRegionalDestino,idFilialDestino,tpRota,idTipoMeioTransporte,idMoedaPais,idMeioTransporte,idProprietario,idsRotaIdaVolta,blAplica,dtVigencia);
    }

    /**
     * Essa função busca todas as rotas de ida e volta vigentes na data de vigencia informada
     * blAplica é a unica exceção que só é obrigatorio se for informado a lista de idsRotaIdaVolta
     * @param idRegionalOrigem 
     * @param idFilialOrigem
     * @param idRegionalDestino
     * @param idFilialDestino
     * @param tpRota
     * @param idTipoMeioTransporte
     * @param idMoedaPais
     * @param idMeioTransporte
     * @param idProprietario
     * @param idsRotaIdaVolta (Long)
     * @param blAplica
     * @param dtVigencia required
     * @return
     */
    public List findRotasByCriteriasVigente(Long idRegionalOrigem, Long idFilialOrigem, Long idRegionalDestino, Long idFilialDestino,
    		String tpRota, Long idTipoMeioTransporte, Long idMoedaPais, Long idMeioTransporte, Long idProprietario, List idsRotaIdaVolta, Boolean blAplica, YearMonthDay dtVigencia) {
    	return getSimulacaoReajusteRotaDAO().findRotasByCriterias(idRegionalOrigem,idFilialOrigem,idRegionalDestino,idFilialDestino,tpRota,idTipoMeioTransporte,idMoedaPais,idMeioTransporte,idProprietario,idsRotaIdaVolta,blAplica,dtVigencia,VIGENTE_EM);
    }
    public List findRotasByCriteriasVigente(SimulacaoReajusteRota simulacao) {
    	Long idRegionalOrigem = (simulacao.getRegionalOrigem() != null) ? simulacao.getRegionalOrigem().getIdRegional() : null;
    	Long idRegionalDestino = (simulacao.getRegionalDestino() != null) ? simulacao.getRegionalDestino().getIdRegional() : null;
    	Long idFilialOrigem = (simulacao.getFilialOrigem() != null) ? simulacao.getFilialOrigem().getIdFilial() : null;
    	Long idFilialDestino = (simulacao.getFilialDestino() != null) ? simulacao.getFilialDestino().getIdFilial() : null;
    	Long idTipoMeioTransporte = (simulacao.getTipoMeioTransporte() != null) ? simulacao.getTipoMeioTransporte().getIdTipoMeioTransporte() : null;
    	Long idMeioTransporte = (simulacao.getMeioTransporte() != null) ? simulacao.getMeioTransporte().getIdMeioTransporte() : null;
    	Long idMoedaPais = (simulacao.getMoedaPais() != null) ? simulacao.getMoedaPais().getIdMoedaPais() : null;
    	Long idProprietario = (simulacao.getProprietario() != null) ? simulacao.getProprietario().getIdProprietario() : null;
    	String tpRota = (simulacao.getTpRota() != null) ? simulacao.getTpRota().getValue() : null;
    	YearMonthDay dtVigencia = simulacao.getDtVigenciaInicial();
    	List idsRotaIdaVolta = new ArrayList();
    	Boolean blAplica = null;
    	if (simulacao.getParametroSimulacaoRotas() != null && !simulacao.getParametroSimulacaoRotas().isEmpty()) {
    		for(Iterator i = simulacao.getParametroSimulacaoRotas().iterator(); i.hasNext();) {
    			AplicaReajusteRota reajusteRota = (AplicaReajusteRota)i.next();
    			if (blAplica == null)
    				blAplica = reajusteRota.getBlAplicacao();
    			idsRotaIdaVolta.add(reajusteRota.getRotaIdaVolta().getNrRota());
    		}
    	}
    	 return findRotasByCriteriasVigente(idRegionalOrigem,idFilialOrigem,idRegionalDestino,idFilialDestino,tpRota,idTipoMeioTransporte,idMoedaPais,idMeioTransporte,idProprietario,idsRotaIdaVolta,blAplica,dtVigencia);
    }
    
    public SqlTemplate createHQLRotasByCriteria(Long idRegionalOrigem, Long idFilialOrigem, Long idRegionalDestino, Long idFilialDestino,
    		String tpRota, Long idTipoMeioTransporte, Long idMoedaPais, Long idMeioTransporte, Long idProprietario, List idsRotaIdaVolta, Boolean blAplica, YearMonthDay dtVigencia, int opcaoVigencia,Boolean addFetchs) {
    	return getSimulacaoReajusteRotaDAO().createHQLRotasByCriteria(idRegionalOrigem, idFilialOrigem, idRegionalDestino, idFilialDestino, tpRota, idTipoMeioTransporte, idMoedaPais, idMeioTransporte, idProprietario, idsRotaIdaVolta, blAplica, dtVigencia, opcaoVigencia, addFetchs);
    }
    public SqlTemplate createHQLRotasByCriteria(SimulacaoReajusteRota simulacao, int opcaoVigencia, Boolean addFetchs) {
    	Long idRegionalOrigem = (simulacao.getRegionalOrigem() != null) ? simulacao.getRegionalOrigem().getIdRegional() : null;
    	Long idRegionalDestino = (simulacao.getRegionalDestino() != null) ? simulacao.getRegionalDestino().getIdRegional() : null;
    	Long idFilialOrigem = (simulacao.getFilialOrigem() != null) ? simulacao.getFilialOrigem().getIdFilial() : null;
    	Long idFilialDestino = (simulacao.getFilialDestino() != null) ? simulacao.getFilialDestino().getIdFilial() : null;
    	Long idTipoMeioTransporte = (simulacao.getTipoMeioTransporte() != null) ? simulacao.getTipoMeioTransporte().getIdTipoMeioTransporte() : null;
    	Long idMeioTransporte = (simulacao.getMeioTransporte() != null) ? simulacao.getMeioTransporte().getIdMeioTransporte() : null;
    	Long idMoedaPais = (simulacao.getMoedaPais() != null) ? simulacao.getMoedaPais().getIdMoedaPais() : null;
    	Long idProprietario = (simulacao.getProprietario() != null) ? simulacao.getProprietario().getIdProprietario() : null;
    	String tpRota = (simulacao.getTpRota() != null) ? simulacao.getTpRota().getValue() : null;
    	YearMonthDay dtVigencia = simulacao.getDtVigenciaInicial();
    	List idsRotaIdaVolta = new ArrayList();
    	Boolean blAplica = null;
    	if (simulacao.getParametroSimulacaoRotas() != null && !simulacao.getParametroSimulacaoRotas().isEmpty()) {
    		for(Iterator i = simulacao.getParametroSimulacaoRotas().iterator(); i.hasNext();) {
    			AplicaReajusteRota reajusteRota = (AplicaReajusteRota)i.next();
    			if (blAplica == null)
    				blAplica = reajusteRota.getBlAplicacao();
    			idsRotaIdaVolta.add(reajusteRota.getRotaIdaVolta().getNrRota());
    		}
    	}
    	return createHQLRotasByCriteria(idRegionalOrigem,idFilialOrigem,idRegionalDestino,idFilialDestino,tpRota,idTipoMeioTransporte,idMoedaPais,idMeioTransporte,idProprietario,idsRotaIdaVolta,blAplica,dtVigencia,opcaoVigencia,addFetchs);
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private SimulacaoReajusteRotaDAO getSimulacaoReajusteRotaDAO() {
        return (SimulacaoReajusteRotaDAO) getDao();
    }

	public void setRotaViagemService(RotaViagemService rotaViagemService) {
		this.rotaViagemService = rotaViagemService;
	}
   }