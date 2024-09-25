package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.contratacaoveiculos.model.Beneficiario;
import com.mercurio.lms.contratacaoveiculos.model.dao.BeneficiarioDAO;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contratacaoveiculos.beneficiarioService"
 */
public class BeneficiarioService extends CrudService<Beneficiario, Long> {
	private PessoaService pessoaService;

	/**
	 * Recupera uma inst�ncia de <code>Beneficiario</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public Beneficiario findById(java.lang.Long id) {
		return (Beneficiario)super.findById(id);
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
	 * Remove a empresa e tenta remover a pessoa.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 *
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void removeBeneficiarioById(Long id) {
		boolean removePessoa = true;

		try {
			this.removeById(id);
		} catch (Exception e){
			// Se o benefeciario possuir associacoes com proprietarios, nao remove a pessoa
			removePessoa = false;
		}

		if (removePessoa){
			try {
				this.removePessoa(id);
			} catch (Exception e) {
			// ignora erros de FK na pessoa
			}
		}
	}

	/**
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	private void removePessoa(Long id){
		pessoaService.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades quee dever�o ser removida.
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeByIds(List ids) {
		for (Iterator iterIds = ids.iterator(); iterIds.hasNext();) {
			Long id = (Long) iterIds.next();
			this.removeBeneficiarioById(id);
		}
	}

	protected Beneficiario beforeStore(Beneficiario bean) {
		Beneficiario beneficiario = (Beneficiario) bean;
		if (beneficiario.getIdBeneficiario() == null) {
			if (verificaExistenciaEspecializacao(beneficiario))
				beneficiario.setIdBeneficiario(beneficiario.getPessoa().getIdPessoa());
		}
		return super.beforeStore(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Beneficiario bean) {
		pessoaService.store(bean);
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setBeneficiarioDAO(BeneficiarioDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private BeneficiarioDAO getBeneficiarioDAO() {
		return (BeneficiarioDAO) getDao();
	}

	public List findBeneficiarioVigente(Long idBeneficiario,Date dtVigenciaInicial, Date dtVigenciaFinal) {
		return getBeneficiarioDAO().findBeneficiarioVigente(idBeneficiario,dtVigenciaInicial,dtVigenciaFinal);
	}

	public boolean verificaExistenciaEspecializacao(Beneficiario beneficiario){
		return getBeneficiarioDAO().verificaExistenciaEspecializacao(beneficiario);
	}

	/**
	 * Retorna 'true' se a pessoa informada � um beneficiario ativo sen�o, retorna 'false'.
	 * 
	 * @author Micka�l Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isBeneficiario(Long idPessoa){
		return getBeneficiarioDAO().isBeneficiario(idPessoa);
	} 	

	/**
	 * @param pessoaService The pessoaService to set.
	 */
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

}