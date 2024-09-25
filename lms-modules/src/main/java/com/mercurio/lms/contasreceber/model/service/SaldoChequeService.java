package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.contasreceber.model.SaldoCheque;
import com.mercurio.lms.contasreceber.model.dao.SaldoChequeDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.saldoChequeService"
 */
public class SaldoChequeService extends CrudService<SaldoCheque, Long> {

	FilialService filialService;
	HistoricoFilialService historicoFilialService;
	
	/**
	 * Recupera uma instância de <code>SaldoCheque</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public SaldoCheque findById(java.lang.Long id) {
        return (SaldoCheque)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
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
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(SaldoCheque bean) {
        return super.store(bean);
    }

    /**
     * Método que cria ou atualiza o saldo dos cheque da filial do dia e da moeda informada.
     * O tipo de historico cheque determina se tem que acrescentar ou diminuir o valor.
     * 
     * @author Mickaël Jalbert
     * 21/03/2006
     * 
     * @param historicoCheque
     * @param operacaoSaldo
     */
    public void updateSaldoCheque(MoedaPais moedaPais, BigDecimal vlChequeAtual, int operacaoSaldo){

    	// Buscar a filial da sessão, caso a mesma seja uma sucursal
    	// Caso contrário, buscar a filial matriz da empresa da sessão
    	Filial filial = SessionUtils.getFilialSessao(); 
    	if (!historicoFilialService.validateFilialByTpFilial(SessionUtils.getFilialSessao().getIdFilial(), "SU")) {
        	filial = filialService.findFilialByIdEmpresaTpFilial(SessionUtils.getEmpresaSessao().getIdEmpresa(), "MA"); 
    	}
    	
    	YearMonthDay dtSaldo = JTDateTimeUtils.getDataAtual(); 
    	
    	// Multiplica o valor do cheque por 1 ou -1, dependendo da operação que deve ser realizada no saldo
    	BigDecimal vlCheque = vlChequeAtual.multiply(new BigDecimal(operacaoSaldo));
    	
    	SaldoCheque ultimoSaldoCheque = this.findLastSaldoCheque(filial.getIdFilial(), moedaPais.getIdMoedaPais());
    	SaldoCheque saldoCheque = new SaldoCheque();
    	BigDecimal vlSaldo = new BigDecimal(0);
    	
    	saldoCheque.setFilial(filial);
    	saldoCheque.setMoedaPais(moedaPais);
    	saldoCheque.setDtSaldo(dtSaldo);
    	
    	//Se já tinha um saldo cheque
    	if (ultimoSaldoCheque != null) {
    		vlSaldo = ultimoSaldoCheque.getVlSaldo();
    		
    		//Se já tinha um saldo cheque com a data informada 
    		if (JTDateTimeUtils.comparaData(ultimoSaldoCheque.getDtSaldo(), dtSaldo) == 0){
    			//Usar o registro que vem do banco
    			saldoCheque = ultimoSaldoCheque;    			
    		} else {
    			//Usar o valor do saldo anterior
    			saldoCheque.setVlSaldo(ultimoSaldoCheque.getVlSaldo());
    		}
    	}
    	
    	//Acrescentar o valor do cheque ao valor do saldo
    	vlSaldo = vlSaldo.add(vlCheque);
    	
    	saldoCheque.setVlSaldo(vlSaldo);
    	
    	this.store(saldoCheque);
    }
    
    /**
     * Retorna o último saldo cheque da filial e moeda pais informado.
     * 
     * @author Mickaël Jalbert
     * 21/03/2006
     * 
     * @param Long idFilial
     * @param Long idMoedaPais
     */
    public SaldoCheque findLastSaldoCheque(Long idFilial, Long idMoedaPais){
    	return findLastSaldoCheque(idFilial, idMoedaPais, null); 
    }
        
    public SaldoCheque findLastSaldoCheque(Long idFilial, Long idMoedaPais, YearMonthDay dtSaldo){
    	List lstSaldoCheque = this.getSaldoChequeDAO().findLastSaldoCheque(idFilial, idMoedaPais, dtSaldo);
    	
    	if (lstSaldoCheque.size() > 0){
    		//Retorna o primeiro (que é o registro o mais novo por causa do order by da query)
    		return (SaldoCheque) lstSaldoCheque.get(0);
    	} else {
    		return null;
    	}
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setSaldoChequeDAO(SaldoChequeDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private SaldoChequeDAO getSaldoChequeDAO() {
        return (SaldoChequeDAO) getDao();
    }

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setHistoricoFilialService(
			HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}

   }