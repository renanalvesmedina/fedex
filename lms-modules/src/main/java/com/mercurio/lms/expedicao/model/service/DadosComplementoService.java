package com.mercurio.lms.expedicao.model.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.dao.DadosComplementoDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.sim.model.dao.LMComplementoDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;

/**
 * Classe de serviço para CRUD:
 *
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.dadosComplementoService"
 */
public class DadosComplementoService extends CrudService<DadosComplemento, Long> {

	private LMComplementoDAO lmComplementoDao;
	private DevedorDocServService devedorDocServService;
	private PessoaService pessoaService;
	private InformacaoDoctoClienteService informacaoDoctoClienteService;
	private DomainValueService domainValueService;

	public void setLmComplementoDao(LMComplementoDAO lmComplementoDao) {
		this.lmComplementoDao = lmComplementoDao;
	}

	/**
	 * Recupera uma instância de <code>DadosComplemento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public DadosComplemento findById(java.lang.Long id) {
		return (DadosComplemento)super.findById(id);
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
	public java.io.Serializable store(DadosComplemento bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @param Instância do DAO.
	 */
	public void setDadosComplementoDAO(DadosComplementoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private DadosComplementoDAO getDadosComplementoDAO() {
		return (DadosComplementoDAO) getDao();
	}


	/**
	 *
	 * Obtem DadosComplemento através do doctoServico e, o mesmo possuir
	 * DadosComplemento.InformacaoDocServico.dsCampo = tpRegistro
	 *
	 * Identifica que este documento deve ser monitorado pelo processo
	 * de expedição
	 *
	 *
	 * @param  doctoServico
	 * @param  cnpjCliente
	 * @param  tpRegistro
	 * @return DadosComplemento
	 */
	public DadosComplemento executeDadosComplementoSPP(final DoctoServico doctoServico, final String cnpjCliente, final String tpRegistro){

		final DevedorDocServ devedor = devedorDocServService.findDevedorByDoctoServico(doctoServico.getIdDoctoServico());
		if(devedor != null
			&& devedor.getCliente() != null
				&& devedor.getCliente().getPessoa().getNrIdentificacao().startsWith(cnpjCliente)){

			final DadosComplemento complemento = this.findByIdConhecimentoTpRegistro(doctoServico.getIdDoctoServico(), tpRegistro);
			if(complemento != null && complemento.getDsValorCampo() != null && IntegerUtils.getInteger(complemento.getDsValorCampo()) > 1){
				return complemento;
			}
		}
		return null;
	}
    
    public DadosComplemento findDadosComplementoToMobile(final DoctoServico doctoServico, final String cnpjCliente, final String[] tpRegistro) {
        final DevedorDocServ devedor = devedorDocServService.findDevedorByDoctoServico(doctoServico.getIdDoctoServico());

        if (devedor == null || devedor.getCliente() == null || !devedor.getCliente().getPessoa().getNrIdentificacao().startsWith(cnpjCliente)) {
            return null;
        }

        DadosComplemento complemento = this.findByIdConhecimentoTpRegistro(doctoServico.getIdDoctoServico(), tpRegistro);
        
        if (complemento == null || complemento.getDsValorCampo() == null) {
            return null;
        }
        
        return complemento;
    }


	/**
	 * Método executa ocorrência SPP
	 *
	 * @param doc
	 * @param complementoBaixa
	 */
	public void executeOcorrenciaSPPCliente(DoctoServico doc, DomainValue complementoBaixa){

		/* CQPRO00023468
		 * FIXME - estas constantes CNPJ_NATURA e COD_CONF poderiam ser
		 * inseridas em um tabela no banco ou parametrizado, seria o mais correto, 
		 * mas o analista (Leonardo Contreras) é quem determina isso :( */
		this.executeOcorrenciaSPP(doc, complementoBaixa, ConstantesExpedicao.CNPJ_NATURA, ConstantesExpedicao.COD_CONF);
	}

	/**
	 * Método executa ocorrência SPP
	 */
	public void executeOcorrenciaSPP(DoctoServico doc, DomainValue complementoBaixa, String cnpjCliente, String tpRegistro){

		DadosComplemento complementoConferencia = this.executeDadosComplementoSPP(doc, cnpjCliente, tpRegistro);
		if(complementoConferencia != null){
			if(complementoBaixa == null || StringUtils.isBlank(complementoBaixa.getValue())){
				throw new BusinessException("LMS-09117");
			}else{
				//LMS-892
				DadosComplemento dadosComplemento = this.findByIdConhecimentoTpRegistro(doc.getIdDoctoServico(), ConstantesExpedicao.NR_OCORRENCIA_ENTREGA);
				if(dadosComplemento == null){
					InformacaoDoctoCliente informacaoDoctoCliente = informacaoDoctoClienteService.
									findInformacaoDoctoCliente(complementoConferencia.getInformacaoDoctoCliente().getCliente().getIdCliente(),
															ConstantesExpedicao.NR_OCORRENCIA_ENTREGA);

					DadosComplemento complemento = new DadosComplemento();
					complemento.setConhecimento(complementoConferencia.getConhecimento());
					complemento.setDsValorCampo(complementoBaixa.getValue());
					complemento.setInformacaoDoctoCliente( informacaoDoctoCliente );
					this.store(complemento);

				}else{
					dadosComplemento.setDsValorCampo(complementoBaixa.getValue());
					this.store(dadosComplemento);
				}
			}
		}
	}

	/**
	 * Obtem a lista de baixas DM_BAIXA_SPP_NATURA
	 * @param criteria
	 * @return
	 */
	public List findBaixaSPP(Map criteria) {
		return domainValueService.findDomainValues("DM_BAIXA_SPP_NATURA");
	}

	public List findIdsByIdConhecimento(Long idConhecimento) {
		return getDadosComplementoDAO().findIdsByIdConhecimento(idConhecimento);
	}

	public DadosComplemento findByIdConhecimentoTpRegistro(Long idConhecimento, String[] tpRegistro) {
		return getDadosComplementoDAO().findByIdConhecimentoTpRegistro(idConhecimento, tpRegistro);
	}

	public DadosComplemento findByIdConhecimentoTpRegistro(Long idConhecimento, String tpRegistro) {
		return getDadosComplementoDAO().findByIdConhecimentoTpRegistro(idConhecimento, tpRegistro);
	}

	public DadosComplemento findByIdConhecimentoDocServico(Long idConhecimento, String dsCampo) {
		return getDadosComplementoDAO().findByIdConhecimentoDocServico(idConhecimento, dsCampo);
	}

	public DadosComplemento findByIdConhecimentoDocCliente(Long idConhecimento, String dsCampo) {
		return getDadosComplementoDAO().findByIdConhecimentoDocCliente(idConhecimento, dsCampo);
	}
	
	public List<DadosComplemento> findVolTotalizadorByConhecimento(Long idConhecimento) {
		return getDadosComplementoDAO().findVolTotalizadorByConhecimento(idConhecimento);
	}

	public DadosComplemento findByIdNFDocCliente(Long idNotaFiscalConhecimento, String dsCampo) {
		return getDadosComplementoDAO().findByIdNFDocCliente(idNotaFiscalConhecimento, dsCampo);
	}

	public List<Map<String, Object>> findByIdConhecimentoDocCliente(Long idConhecimento, List<String> dsCampos) {
		return getDadosComplementoDAO().findByIdConhecimentoDocCliente(idConhecimento, dsCampos);
	}

    public List<Map<String, Object>> findAllDadosComplementoByIdConhecimentoDsCampos(Long idConhecimento, List<String> dsCampos) {
        return getDadosComplementoDAO().findAllDadosComplementoByIdConhecimentoDsCampos(idConhecimento, dsCampos);
    }

	public List findByConhecimento(Long idConhecimento)	{
		return getDadosComplementoDAO().findByConhecimento(idConhecimento);
	}

	public List findPaginatedDadosCompl(Long idDoctoServico){
		return lmComplementoDao.findPaginatedDadosCompl(idDoctoServico);
	}

	public boolean findDadosComplAba(Long idDoctoServico){
		return lmComplementoDao.findDadosComplAba(idDoctoServico);
	}

	public PessoaService getPessoaService() {
		return pessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public DevedorDocServService getDevedorDocServService() {
		return devedorDocServService;
	}

	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}

	public InformacaoDoctoClienteService getInformacaoDoctoClienteService() {
		return informacaoDoctoClienteService;
	}

	public void setInformacaoDoctoClienteService(
			InformacaoDoctoClienteService informacaoDoctoClienteService) {
		this.informacaoDoctoClienteService = informacaoDoctoClienteService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}


}