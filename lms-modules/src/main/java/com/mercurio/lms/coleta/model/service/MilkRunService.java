package com.mercurio.lms.coleta.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.MilkRemetente;
import com.mercurio.lms.coleta.model.MilkRun;
import com.mercurio.lms.coleta.model.SemanaRemetMrun;
import com.mercurio.lms.coleta.model.dao.MilkRunDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.milkRunService"
 */
public class MilkRunService extends CrudService<MilkRun, Long> {

	private MilkRemetenteService milkRemetenteService;
	private SemanaRemetMrunService semanaRemetMrunService;

	/**
	 * Recupera uma instância de <code>MilkRun</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public MilkRun findById(java.lang.Long id) {
    	MilkRun milkRun = (MilkRun)super.findById(id);
    	milkRun.setMilkRemetentes(getMilkRemetenteService().findByIdMilkRun(id));
        return milkRun;
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(Long idMilkRun) {
    	List listMilkRemetente = this.getMilkRemetenteService().findByIdMilkRun(idMilkRun);
    	for (int i = 0; i < listMilkRemetente.size(); i++) {
    		MilkRemetente milkRemetente = (MilkRemetente) listMilkRemetente.get(i);
   			this.getSemanaRemetMrunService().removeByIdMilkRemetente(milkRemetente.getIdMilkRemetente());    		
		}
    	this.getMilkRemetenteService().removeByIdMilkRun(idMilkRun);
    	this.getMilkRunDAO().removeById(idMilkRun);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List idsMilkRun) {
    	for (int i = 0; i < idsMilkRun.size(); i++) {
    		Long idMilkRun = (Long) idsMilkRun.get(i);
        	List listMilkRemetente = this.getMilkRemetenteService().findByIdMilkRun(idMilkRun);
        	for (int j = 0; j < listMilkRemetente.size(); j++) {
        		MilkRemetente milkRemetente = (MilkRemetente) listMilkRemetente.get(j);
       			this.getSemanaRemetMrunService().removeByIdMilkRemetente(milkRemetente.getIdMilkRemetente());    		
    		}
        	this.getMilkRemetenteService().removeByIdMilkRun(idMilkRun);
        	this.getMilkRunDAO().removeById(idMilkRun);
		}        
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @param list 
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(MilkRun bean, ItemList items, ItemListConfig config, TypedFlatMap mapBean) {
		if (!items.hasItems()) {
			throw new BusinessException("LMS-02018");
		}
		boolean rollbackMasterId = bean.getIdMilkRun() == null;  
				
    	// Pega a ordem dos registros da listbox
		List listSemanaRemetMrun = new ArrayList();		
		getOrdemListboxDiaSemana(mapBean, listSemanaRemetMrun);
				
    	try {
        	Long masterId = null;
        	
			MilkRemetente oldMilkRemetente = new MilkRemetente();
			
        	for(Iterator iter = items.iterator(bean.getIdMilkRun(), config); iter.hasNext();) {
        		MilkRemetente milkRemetente = (MilkRemetente) iter.next();
        		
        		List listSemanas = milkRemetente.getSemanaRemetMruns();

        		for(int i=0; i < listSemanas.size(); i++) {
        			SemanaRemetMrun semanaRemetMrun = (SemanaRemetMrun) listSemanas.get(i);
        			
        			if(semanaRemetMrun.getIdSemanaRemetMrun() == null) {
        				
	        			if(milkRemetente.getMilkRun().getIdMilkRun() != null) {	        				
	        				setNrDiaSemana(milkRemetente, semanaRemetMrun);		        		
	        			} else {	        				
	        				this.setDadosSemana(
	        						oldMilkRemetente.getSemanaRemetente(
	        								semanaRemetMrun.getTpSemanaDoMes().getValue()), 
	        								semanaRemetMrun);     
	        			}
	        			
        			} else {
        				
        				setNrDiaSemana(milkRemetente, semanaRemetMrun);
        				
           				if(listSemanaRemetMrun.size() > 0) {
           					for(int j = 0; j < listSemanaRemetMrun.size(); j++) {
           						SemanaRemetMrun semana = (SemanaRemetMrun) listSemanaRemetMrun.get(j);
								if (semana.getIdSemanaRemetMrun().longValue() == semanaRemetMrun.getIdSemanaRemetMrun().longValue()) {
									if(semanaRemetMrun.getNrDomingo() != null) {
										semanaRemetMrun.setNrDomingo(semana.getNrDomingo());
									}									
									if(semanaRemetMrun.getNrSegundaFeira() != null) {
										semanaRemetMrun.setNrSegundaFeira(semana.getNrSegundaFeira());
									}
									if(semanaRemetMrun.getNrTercaFeira() != null) {
										semanaRemetMrun.setNrTercaFeira(semana.getNrTercaFeira());
									}
									if(semanaRemetMrun.getNrQuartaFeira() != null) {
										semanaRemetMrun.setNrQuartaFeira(semana.getNrQuartaFeira());
									}
									if(semanaRemetMrun.getNrQuintaFeira() != null) {
										semanaRemetMrun.setNrQuintaFeira(semana.getNrQuintaFeira());
									}
									if(semanaRemetMrun.getNrSextaFeira() != null) {
										semanaRemetMrun.setNrSextaFeira(semana.getNrSextaFeira());
									}
									if(semanaRemetMrun.getNrSabado() != null) {
										semanaRemetMrun.setNrSabado(semana.getNrSabado());
									}
								}           						
							}
           				}
        				
        			}
        		}
        		oldMilkRemetente = milkRemetente;        		
        	}
        	
			bean = getMilkRunDAO().store(bean, items, config); 
			masterId = bean.getIdMilkRun();			
			return masterId;
			
    	} catch (RuntimeException e) {
    		this.rollbackMasterState(bean, rollbackMasterId, e); 
            items.rollbackItemsState(); 
            throw e;
    	}
    }

	/**
	 * Método que verifica o ultimo numero do dia da semana em questão e soma mais 1 para o novo registro.
	 * 
	 * @param milkRemetente
	 * @param semanaRemetMrun
	 */
	private void setNrDiaSemana(MilkRemetente milkRemetente, SemanaRemetMrun semanaRemetMrun) {
		
		if(semanaRemetMrun.getHrInicialDomingo() != null && (semanaRemetMrun.getNrDomingo() == null ||
				 											 semanaRemetMrun.getNrDomingo().intValue() == 0)) {
			semanaRemetMrun.setNrDomingo(
					this.getMaiorNrDiaSemanaMaisUm(
							milkRemetente.getMilkRun().getIdMilkRun(), 
							semanaRemetMrun.getTpSemanaDoMes().getValue(), 
							"nrDomingo"));			        				
		} else if(semanaRemetMrun.getHrInicialDomingo() == null && (semanaRemetMrun.getNrDomingo().intValue() > 0)) {
			semanaRemetMrun.setNrDomingo(Short.valueOf("0"));
		}		
		
		if(semanaRemetMrun.getHrInicialSegundaFeira() != null && (semanaRemetMrun.getNrSegundaFeira() == null || 
																  semanaRemetMrun.getNrSegundaFeira().intValue() == 0)) {
			semanaRemetMrun.setNrSegundaFeira(
					this.getMaiorNrDiaSemanaMaisUm(
							milkRemetente.getMilkRun().getIdMilkRun(), 
							semanaRemetMrun.getTpSemanaDoMes().getValue(), 
							"nrSegundaFeira"));
		} else if(semanaRemetMrun.getHrInicialSegundaFeira() == null && (semanaRemetMrun.getNrSegundaFeira().intValue() > 0)) {
			semanaRemetMrun.setNrSegundaFeira(Short.valueOf("0"));
		}
		
		if(semanaRemetMrun.getHrInicialTercaFeira() != null && (semanaRemetMrun.getNrTercaFeira() == null ||
																semanaRemetMrun.getNrTercaFeira().intValue() == 0)) {	    	    			
			semanaRemetMrun.setNrTercaFeira(
					this.getMaiorNrDiaSemanaMaisUm(
							milkRemetente.getMilkRun().getIdMilkRun(), 
							semanaRemetMrun.getTpSemanaDoMes().getValue(), 
							"nrTercaFeira"));	        				
		} else if(semanaRemetMrun.getHrInicialTercaFeira() == null && (semanaRemetMrun.getNrTercaFeira().intValue() > 0)) {
			semanaRemetMrun.setNrTercaFeira(Short.valueOf("0"));
		}
		
		if(semanaRemetMrun.getHrInicialQuartaFeira() != null && (semanaRemetMrun.getNrQuartaFeira() == null ||
																 semanaRemetMrun.getNrQuartaFeira().intValue() == 0)) {
			semanaRemetMrun.setNrQuartaFeira(
					this.getMaiorNrDiaSemanaMaisUm(
							milkRemetente.getMilkRun().getIdMilkRun(), 
							semanaRemetMrun.getTpSemanaDoMes().getValue(), 
							"nrQuartaFeira"));	        				
		} else if(semanaRemetMrun.getHrInicialQuartaFeira() == null && (semanaRemetMrun.getNrQuartaFeira().intValue() > 0)) {
			semanaRemetMrun.setNrQuartaFeira(Short.valueOf("0"));
		}

		if(semanaRemetMrun.getHrInicialQuintaFeira() != null && (semanaRemetMrun.getNrQuintaFeira() == null ||
																 semanaRemetMrun.getNrQuintaFeira().intValue() == 0)) {	    
			semanaRemetMrun.setNrQuintaFeira(
					this.getMaiorNrDiaSemanaMaisUm(
							milkRemetente.getMilkRun().getIdMilkRun(), 
							semanaRemetMrun.getTpSemanaDoMes().getValue(), 
							"nrQuintaFeira"));		        				
		} else if(semanaRemetMrun.getHrInicialQuintaFeira() == null && (semanaRemetMrun.getNrQuintaFeira().intValue() > 0)) {
			semanaRemetMrun.setNrQuintaFeira(Short.valueOf("0"));
		}
		
		if(semanaRemetMrun.getHrInicialSextaFeira() != null && (semanaRemetMrun.getNrSextaFeira() == null ||
																semanaRemetMrun.getNrSextaFeira().intValue() == 0)) {	    	    			
			semanaRemetMrun.setNrSextaFeira(
					this.getMaiorNrDiaSemanaMaisUm(
							milkRemetente.getMilkRun().getIdMilkRun(), 
							semanaRemetMrun.getTpSemanaDoMes().getValue(), 
							"nrSextaFeira"));	        				
		} else if(semanaRemetMrun.getHrInicialSextaFeira() == null && (semanaRemetMrun.getNrSextaFeira().intValue() > 0)) {
			semanaRemetMrun.setNrSextaFeira(Short.valueOf("0"));
		}
		
		if(semanaRemetMrun.getHrInicialSabado() != null && (semanaRemetMrun.getNrSabado() == null ||
															semanaRemetMrun.getNrSabado().intValue() == 0)) {
			semanaRemetMrun.setNrSabado(
					this.getMaiorNrDiaSemanaMaisUm(
							milkRemetente.getMilkRun().getIdMilkRun(), 
							semanaRemetMrun.getTpSemanaDoMes().getValue(), 
							"nrSabado"));		        				
		} else if(semanaRemetMrun.getHrInicialSabado() == null && (semanaRemetMrun.getNrSabado().intValue() > 0)) {
			semanaRemetMrun.setNrSabado(Short.valueOf("0"));
		}
		
	}
    
	/**
	 * Método q pega o maior número do dia da semana do Milk Remetente e soma mais 1
	 * 
	 * @param idMilkRun
	 * @param tpSemanaDoMes
	 * @param nrDiaSemana
	 * @return Short
	 */
    private Short getMaiorNrDiaSemanaMaisUm(Long idMilkRun, String tpSemanaDoMes, String nrDiaSemana) {
		Short maiorNumero = this.getSemanaRemetMrunService().findMaiorNrDiaSemana(idMilkRun, tpSemanaDoMes, nrDiaSemana);
		Short numero = Short.valueOf("1");
		if(maiorNumero != null) {
			numero = Short.valueOf((Integer.valueOf(maiorNumero.intValue() + 1)).toString());
		}    	
    	return numero;
    }
    
    
    /**
     * Método que passa os dados de uma semana de um Milk Remetente para a semana
     * de outro Milk Remetente. 
     */
    private void setDadosSemana(SemanaRemetMrun semanaOldMilkRemetente, SemanaRemetMrun semanaMilkRemetente) {
    	if(semanaOldMilkRemetente != null) {
			if(semanaMilkRemetente.getHrInicialDomingo() != null) {				
				semanaMilkRemetente.setNrDomingo(Short.valueOf(
						Integer.valueOf(semanaOldMilkRemetente.getNrDomingo().intValue() + 1).toString()));
			}      		
			if(semanaMilkRemetente.getHrInicialSegundaFeira() != null) {				
				semanaMilkRemetente.setNrSegundaFeira(Short.valueOf(
						Integer.valueOf(semanaOldMilkRemetente.getNrSegundaFeira().intValue() + 1).toString()));
			}
			if(semanaMilkRemetente.getHrInicialTercaFeira() != null) {								
				semanaMilkRemetente.setNrTercaFeira(Short.valueOf(
						Integer.valueOf(semanaOldMilkRemetente.getNrTercaFeira().intValue() + 1).toString()));
			}
			if(semanaMilkRemetente.getHrInicialQuartaFeira() != null) {
				semanaMilkRemetente.setNrQuartaFeira(Short.valueOf(
						Integer.valueOf(semanaOldMilkRemetente.getNrQuartaFeira().intValue() + 1).toString()));
			}
			if(semanaMilkRemetente.getHrInicialQuintaFeira() != null) {
				semanaMilkRemetente.setNrQuintaFeira(Short.valueOf(
						Integer.valueOf(semanaOldMilkRemetente.getNrQuintaFeira().intValue() + 1).toString()));
			}
			if(semanaMilkRemetente.getHrInicialSextaFeira() != null) {				
				semanaMilkRemetente.setNrSextaFeira(Short.valueOf(
						Integer.valueOf(semanaOldMilkRemetente.getNrSextaFeira().intValue() + 1).toString()));
			}
			if(semanaMilkRemetente.getHrInicialSabado() != null) {
				semanaMilkRemetente.setNrSabado(Short.valueOf(
						Integer.valueOf(semanaOldMilkRemetente.getNrSabado().intValue() + 1).toString()));
			}
    	} else {
			if(semanaMilkRemetente.getHrInicialDomingo() != null) {
				semanaMilkRemetente.setNrDomingo(Short.valueOf("1"));
			}      		
			if(semanaMilkRemetente.getHrInicialSegundaFeira() != null) {
				semanaMilkRemetente.setNrSegundaFeira(Short.valueOf("1"));
			}
			if(semanaMilkRemetente.getHrInicialTercaFeira() != null) {
				semanaMilkRemetente.setNrTercaFeira(Short.valueOf("1"));
			}
			if(semanaMilkRemetente.getHrInicialQuartaFeira() != null) {
				semanaMilkRemetente.setNrQuartaFeira(Short.valueOf("1"));
			}
			if(semanaMilkRemetente.getHrInicialQuintaFeira() != null) {
				semanaMilkRemetente.setNrQuintaFeira(Short.valueOf("1"));
			}
			if(semanaMilkRemetente.getHrInicialSextaFeira() != null) {
				semanaMilkRemetente.setNrSextaFeira(Short.valueOf("1"));
			}
			if(semanaMilkRemetente.getHrInicialSabado() != null) {
				semanaMilkRemetente.setNrSabado(Short.valueOf("1"));
			}  		
    	}
    }
    
    
	/**
	 * Seta o numero do dia da semana para organizar a ordem q os registros aparecem nas listbox
	 * 
	 * @param mapSemana
	 * @param listSemanaRemetMrun
	 */
	private void getOrdemListboxDiaSemana(TypedFlatMap mapSemanas, List listSemanaRemetMrun) {
			
		for (int i = 1; i < 5; i++) {
			
			List listDomingo = (List) mapSemanas.get("semana" + i + ".domingo");
			if(listDomingo != null) {			
				for (int j=0; j<listDomingo.size(); j++) {
					TypedFlatMap tfm = (TypedFlatMap) listDomingo.get(j);
					SemanaRemetMrun semanaRemetMrun = getSemanaFromListSemana(listSemanaRemetMrun, tfm.getLong("id"));
					Integer ordem = Integer.valueOf((String) tfm.get("ordem"));	
					semanaRemetMrun.setNrDomingo(Short.valueOf(Integer.valueOf(ordem.intValue() + 1).toString()));
				}
			}			
			
			List listSegundaFeira = (List) mapSemanas.get("semana" + i + ".segundaFeira");
			if(listSegundaFeira != null) {			
				for (int j=0; j<listSegundaFeira.size(); j++) {
					TypedFlatMap tfm = (TypedFlatMap) listSegundaFeira.get(j);
					SemanaRemetMrun semanaRemetMrun = getSemanaFromListSemana(listSemanaRemetMrun, tfm.getLong("id"));
					Integer ordem = Integer.valueOf((String) tfm.get("ordem"));	
					semanaRemetMrun.setNrSegundaFeira(Short.valueOf(Integer.valueOf(ordem.intValue() + 1).toString()));
				}
			}
			
			List listTercaFeira = (List) mapSemanas.get("semana" + i + ".tercaFeira");
			if(listTercaFeira != null) {			
				for (int j=0; j<listTercaFeira.size(); j++) {
					TypedFlatMap tfm = (TypedFlatMap) listTercaFeira.get(j);
					SemanaRemetMrun semanaRemetMrun = getSemanaFromListSemana(listSemanaRemetMrun, tfm.getLong("id"));
					Integer ordem = Integer.valueOf((String) tfm.get("ordem"));	
					semanaRemetMrun.setNrTercaFeira(Short.valueOf(Integer.valueOf(ordem.intValue() + 1).toString()));
				}
			}
			
			List listQuartaFeira = (List) mapSemanas.get("semana" + i + ".quartaFeira");
			if(listQuartaFeira != null) {			
				for (int j=0; j<listQuartaFeira.size(); j++) {
					TypedFlatMap tfm = (TypedFlatMap) listQuartaFeira.get(j);
					SemanaRemetMrun semanaRemetMrun = getSemanaFromListSemana(listSemanaRemetMrun, tfm.getLong("id"));
					Integer ordem = Integer.valueOf((String) tfm.get("ordem"));	
					semanaRemetMrun.setNrQuartaFeira(Short.valueOf(Integer.valueOf(ordem.intValue() + 1).toString()));
				}
			}	
			
			List listQuintaFeira = (List) mapSemanas.get("semana" + i + ".quintaFeira");
			if(listQuintaFeira != null) {			
				for (int j=0; j<listQuintaFeira.size(); j++) {
					TypedFlatMap tfm = (TypedFlatMap) listQuintaFeira.get(j);					
					SemanaRemetMrun semanaRemetMrun = getSemanaFromListSemana(listSemanaRemetMrun, tfm.getLong("id"));
					Integer ordem = Integer.valueOf((String) tfm.get("ordem"));	
					semanaRemetMrun.setNrQuintaFeira(Short.valueOf(Integer.valueOf(ordem.intValue() + 1).toString()));
				}
			}	
			
			List listSextaFeira = (List) mapSemanas.get("semana" + i + ".sextaFeira");
			if(listSextaFeira != null) {			
				for (int j=0; j<listSextaFeira.size(); j++) {
					TypedFlatMap tfm = (TypedFlatMap) listSextaFeira.get(j);
					SemanaRemetMrun semanaRemetMrun = getSemanaFromListSemana(listSemanaRemetMrun, tfm.getLong("id"));
					Integer ordem = Integer.valueOf((String) tfm.get("ordem"));	
					semanaRemetMrun.setNrSextaFeira(Short.valueOf(Integer.valueOf(ordem.intValue() + 1).toString()));
				}
			}	
			
			List listSabado = (List) mapSemanas.get("semana" + i + ".sabado");
			if(listSabado != null) {			
				for (int j=0; j<listSabado.size(); j++) {
					TypedFlatMap tfm = (TypedFlatMap) listSabado.get(j);
					SemanaRemetMrun semanaRemetMrun = getSemanaFromListSemana(listSemanaRemetMrun, tfm.getLong("id"));
					Integer ordem = Integer.valueOf((String) tfm.get("ordem"));	
					semanaRemetMrun.setNrSabado(Short.valueOf(Integer.valueOf(ordem.intValue() + 1).toString()));
				}
			}
			
		}
		
	}    
	
	/**
	 * Método que verifica a existência de uma semana dentro da list com o mesmo ID passado por referência. 
	 * @param listSemanaRemetMrun
	 * @param idSemanaRemetMrun
	 * @return
	 */
	private SemanaRemetMrun getSemanaFromListSemana(List listSemanaRemetMrun, Long idSemanaRemetMrun) {
		for (int i = 0; i < listSemanaRemetMrun.size(); i++) {
			SemanaRemetMrun semanaRemetMrun = (SemanaRemetMrun) listSemanaRemetMrun.get(i);
			if(semanaRemetMrun.getIdSemanaRemetMrun().equals(idSemanaRemetMrun)) {
				return semanaRemetMrun;
			}
		}
		
		// Caso não exista nenhma ocorrencia da semana na lista
//		SemanaRemetMrun semanaRemetMrun = this.getSemanaRemetMrunService().findById(idSemanaRemetMrun);
		SemanaRemetMrun semanaRemetMrun = new SemanaRemetMrun();
		semanaRemetMrun.setIdSemanaRemetMrun(idSemanaRemetMrun);
		semanaRemetMrun.setNrSegundaFeira(Short.valueOf("0"));
		semanaRemetMrun.setNrTercaFeira(Short.valueOf("0"));
		semanaRemetMrun.setNrQuartaFeira(Short.valueOf("0"));
		semanaRemetMrun.setNrQuintaFeira(Short.valueOf("0"));
		semanaRemetMrun.setNrSextaFeira(Short.valueOf("0"));
		semanaRemetMrun.setNrSabado(Short.valueOf("0"));
		semanaRemetMrun.setNrDomingo(Short.valueOf("0"));
		
		listSemanaRemetMrun.add(semanaRemetMrun);
		
		return semanaRemetMrun;		
	}
    
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMilkRunDAO(MilkRunDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MilkRunDAO getMilkRunDAO() {
        return (MilkRunDAO) getDao();
    }

	public MilkRemetenteService getMilkRemetenteService() {
		return milkRemetenteService;
	}

	public void setMilkRemetenteService(MilkRemetenteService milkRemetenteService) {
		this.milkRemetenteService = milkRemetenteService;
	}
	
	public SemanaRemetMrunService getSemanaRemetMrunService() {
		return semanaRemetMrunService;
	}

	public void setSemanaRemetMrunService(SemanaRemetMrunService semanaRemetMrunService) {
		this.semanaRemetMrunService = semanaRemetMrunService;
	}
	

	/**
	 * Retorna a sigla inicial representando a faixa de dias para uma coleta MilkRun de acordo com a data passada por parâmetro.
	 * Valores possíveis de retorno: "P" (primeira faixa), "S" (segunda faixa), "T" (terceira faixa), "Q" (quarta faixa).
	 * Moacir Zardo Junior - 23/11/2005 
	 * @return String -> letra representando a semana a qual a data informada se refere.
	 */
	public String getSiglaSemana(YearMonthDay data){
	    int nroSemana = JTDateTimeUtils.getWeekOfMonth(data);
	    switch (nroSemana) {
        case 1:
            return "P";
        case 2:
            return "S";
        case 3:
            return "T";
        case 4:
            return "Q";
        case 5:
            return "P";
        default:
            return null;
        }
	}
	
}