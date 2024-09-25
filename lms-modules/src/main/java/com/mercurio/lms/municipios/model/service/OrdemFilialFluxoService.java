package com.mercurio.lms.municipios.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.OrdemFilialFluxo;
import com.mercurio.lms.municipios.model.dao.OrdemFilialFluxoDAO;
/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.ordemFilialFluxoService"
 */
public class OrdemFilialFluxoService extends CrudService<OrdemFilialFluxo, Long> {

	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setOrdemFilialFluxoDAO(OrdemFilialFluxoDAO dao) {
        setDao( dao );
    } 
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private OrdemFilialFluxoDAO getOrdemFilialFluxoDAO() {
        return (OrdemFilialFluxoDAO) getDao();
    }
    
	/**
	 * Método que recebe como parâmetro o ID da Filial de Origem e da Filial de Destino 
	 * e retorna uma list de idFilialFluxo caso o nrOrdem da Filial de Destino no OrdemFilialFluxo
	 * seja maior que o nrOrdem da Filial de Origem no OrdemFilialFluxo.
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @return List
	 */
    public List findIdFluxoFilialByIdFilialOrigemAndIdFilialDestino(Long idFilialOrigem, Long idFilialDestino) {    	
    	return getOrdemFilialFluxoDAO().findIdFluxoFilialByIdFilialOrigemAndIdFilialDestino(idFilialOrigem, idFilialDestino);
    }    
    
    public List findOrdensFilialFluxoByIdFilialOrigemAndIdFilialDestino(Long idFilialOrigem, Long idFilialDestino, YearMonthDay dtConsulta) {
    	return getOrdemFilialFluxoDAO().findOrdensFilialFluxoByIdFilialOrigemAndIdFilialDestino(idFilialOrigem, idFilialDestino, dtConsulta);
    }
    
    /**
     * Obtém a quantidade de registros para uma pesquisa filtrando por filial origem, filial destino e documento de servico do fluxo filial.
     * @param idFilialOrigem
     * @param idFilialDestino
     * @param idDoctoServico
     * @return
     */
    public Integer getRowCountFluxoFilialByFilialOrigemDestinoAndDoctoServico(Long idFilialOrigem, Long idFilialDestino, Long idDoctoServico) {
    	return getOrdemFilialFluxoDAO().getRowCountFluxoFilialByFilialOrigemDestinoAndDoctoServico(idFilialOrigem, idFilialDestino, idDoctoServico);
    }
    
	/**
	 * Método que recebe como parâmetro o ID do Fluxo Filial e o Número de Ordem e retorna um objeto de OrdemFilialFluxo.
	 * @param idFluxoFilial
	 * @param nrOrdem
	 * @return OrdemFilialFluxo
	 */
    public OrdemFilialFluxo findOrdemFilialFluxoByIdFluxoFilialByNrOrdem(Long idFluxoFilial, Byte nrOrdem) {    	
    	return getOrdemFilialFluxoDAO().findOrdemFilialFluxoByIdFluxoFilialByNrOrdem(idFluxoFilial, nrOrdem);
    }    

    public List findByIdFluxoFilial(Long idFluxoFilial) {
    	return getOrdemFilialFluxoDAO().findByIdFluxoFilial(idFluxoFilial);    	
    }
    
}
