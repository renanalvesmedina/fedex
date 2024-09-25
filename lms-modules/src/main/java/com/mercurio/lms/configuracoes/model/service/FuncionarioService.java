package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.dao.GerarDadosFrotaDAO;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.Funcionario;
import com.mercurio.lms.configuracoes.model.dao.FuncionarioDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.FuncionarioService"
 */
public class FuncionarioService extends CrudService<Funcionario, Long> {

	private GerarDadosFrotaDAO gerarDadosFrotaDAO;

	public static final String SITUACAO_INVALIDA_FUNCIONARIO = "D,I";
	/**
	 * Recupera uma instância de <code>Funcionario</code> a partir do NrMatricula.
	 * @param nrMatricula
	 */
    public Funcionario findByNrMatricula(String nrMatricula) {
    	return getFuncionarioDAO().findByNrMatricula(nrMatricula);
    }
    
    
    public Funcionario findByNrIdentificacao(String nrIdentificacao) {
    	return getFuncionarioDAO().findByNrIdentificacao(nrIdentificacao);
    }
    
    public Funcionario findByIdUsuario(Long idUsuario) {
    	return getFuncionarioDAO().findByIdUsuario(idUsuario);
    }
    
    /**
     * Metodo encapsulador da findUsuarioFilial para utilizar os parâmetros gerais ao invés de
     * constantes HardCoded para os codigos das funções do filtro
     * 
     * @param idUsuario
     * @param idFilial
     * @param cdFuncao String contendo os códigos separados por ";"
     * @return list 
     * 
     * @author Vagner Huzalo
     */
    public List findUsuarioByFilialFuncao(Long idUsuario, Long idFilial, String cdFuncao){
    	ArrayList<String> listCodigos = new ArrayList<String>(); 
    	if (cdFuncao.length() > 0){
    		StringTokenizer tokens = new StringTokenizer(cdFuncao,";");
        	while(tokens.hasMoreElements()){
        		listCodigos.add((String)tokens.nextElement());
        	}
    	}
    	return getFuncionarioDAO().findUsuarioByFilial(idUsuario, idFilial, listCodigos);
    }


    public List findUsuarioByRegional(Long idUsuario, Long idRegional, String cdFuncao) {
    	return getFuncionarioDAO().findUsuarioByRegional(idUsuario, idRegional, cdFuncao);
    }

    /**
     * Metodo que consulta gerentes da filial
     * 
     * @param idFilial identificador da filial
     * @return lista dos funcionarios gerentes da filial
     */
    public List findGerenteFilial(Long idFilial) {
    	return getFuncionarioDAO().findUsuarioByFuncao(idFilial
    		,ConstantesConfiguracoes.CD_GERENTE_FILIAL);
    }

    /**
     * Metodo que consulta gerentes da regional
     * 
     * @param idRegional
     * @return lista dos funcionarios gerentes da regional
     */
    public List findGerenteByRegional(Long idRegional) {
    	return getFuncionarioDAO().findGerenteByRegional(idRegional
    		,ConstantesConfiguracoes.CD_GERENTE_REGIONAL);
    }

    /**
     * Metodo que consulta gerentes de comercial
     * @return
     */
    public List findGerenteComercialByFilial(Long idFilial) {
    	return getFuncionarioDAO().findUsuarioByFuncao(idFilial
    		,ConstantesConfiguracoes.CD_GERENTE_COMERCIAL);
    }    	

    /**
     * Metodo que consulta diretores
     * @return
     */
    public List findDiretorRegionalByFilial(Long idFilial) {
    	return getFuncionarioDAO().findUsuarioByFuncao(idFilial
    		,ConstantesConfiguracoes.CD_DIRETOR_REGIONAL);
    }

    public List findDiretorComercialByFilial(Long idFilial) {
    	return getFuncionarioDAO().findUsuarioByFuncao(idFilial
    		,ConstantesConfiguracoes.CD_DIRETOR_COMERCIAL);
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
    public java.io.Serializable store(Funcionario bean) {
        return super.store(bean);
    }

    /**
     * Atualiza a coluna CODCLIDED na tabela PFCOMPL
     * @param nrMatricula
     * @param idCliente
     */
	public void updateCODCLIDED(String nrMatricula, String idCliente) {
		if(idCliente == null){
			idCliente = StringUtils.rightPad("", 8, ' ');
		}
		gerarDadosFrotaDAO.updateCODCLIDED(nrMatricula,idCliente);
	}    

    public void setFuncionarioDAO(FuncionarioDAO dao) {
        setDao( dao );
    }
    private FuncionarioDAO getFuncionarioDAO() {
        return (FuncionarioDAO) getDao();
    }

	public GerarDadosFrotaDAO getGerarDadosFrotaDAO() {
		return gerarDadosFrotaDAO;
	}

	public void setGerarDadosFrotaDAO(GerarDadosFrotaDAO gerarDadosFrotaDAO) {
		this.gerarDadosFrotaDAO = gerarDadosFrotaDAO;
	}

}