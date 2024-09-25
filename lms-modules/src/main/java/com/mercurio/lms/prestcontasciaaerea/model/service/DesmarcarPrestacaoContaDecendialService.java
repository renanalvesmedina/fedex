package com.mercurio.lms.prestcontasciaaerea.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.municipios.model.service.CiaFilialMercurioService;
import com.mercurio.lms.prestcontasciaaerea.model.PrestacaoConta;

/**
 * Classe de serviço para CRUD: Não inserir documentação após ou remover a tag
 * do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser
 * utilizado para referenciar este serviço.
 * 
 * @spring.bean id="lms.prestcontasciaaerea.desmarcarPrestacaoContaDecendialService"
 */
public class DesmarcarPrestacaoContaDecendialService {
    
    private PrestacaoContaService prestacaoContaService;
    private AwbService awbService;
    private IcmsPrestacaoService icmsPrestacaoService;
    private ValorPrestacaoContaService valorPrestacaoContaService;
    private IntervaloAwbService intervaloAwbService;
    private AwbCanceladoService awbCanceladoService;
    private CiaFilialMercurioService ciaFilialMercurioService;
    
    /**
     * Rotina para excluir uma prestação de contas já gerada
     * @param idEmpresa
     * @param nrPrestacaoConta
     */
    public void deletePrestacaoContaDecencial(Long idEmpresa, Long nrPrestacaoContaP){

        // Regra 1 - prestacaoContaService.findDesmarcar
    	PrestacaoConta prestacaoConta = this.getPrestacaoContaService().findDesmarcar(idEmpresa, nrPrestacaoContaP);
    	Long idPrestacaoConta = prestacaoConta.getIdPrestacaoConta();
    	Long idCiaFilialMercurio = prestacaoConta.getCiaFilialMercurio().getIdCiaFilialMercurio();
        
        if( idPrestacaoConta == null ){
            throw new BusinessException("LMS-37002");
        }

        // Regra 2 - awbService.updateAwbDesmarcarPrestacaoConta
        this.getAwbService().updateAwbDesmarcarPrestacaoConta(idPrestacaoConta);

        /* Regra 3 
         * - icmsPrestacaoService.removeDesmarcarPrestacaoConta
         * - valorePrestacaoContaService.removeDesmarcarPrestacaoConta
         * - intervaloAwbService.removeDesmarcarPrestacaoConta
         * - awbCanceladoService.removeDesmarcarPrestacaoConta
         * - prestacaoContaService.removeById
        */
        this.getIcmsPrestacaoService().removeDesmarcarPrestacaoConta(idPrestacaoConta);
        this.getValorPrestacaoContaService().removeDesmarcarPrestacaoConta(idPrestacaoConta);
        this.getIntervaloAwbService().removeDesmarcarPrestacaoConta(idPrestacaoConta);
        this.getAwbCanceladoService().removeDesmarcarPrestacaoConta(idPrestacaoConta);
        this.getPrestacaoContaService().removeById(idPrestacaoConta);        
    
        /* Regra 4
         * - Se o campo nrPrestacaoContas recebido por parâmetro for igual ao valor nrPrestacaoContas obtido na regra 1
         * - então subtrair 1 da coluna  NR_PRESTACAO_CONTAS da tabela CIA_FILIAL_MERCURIO
         * - idCiaFilialMercurio obtido na regra 1
        */
        getCiaFilialMercurioService().updateNrPrestacaoContas(idCiaFilialMercurio,nrPrestacaoContaP);
    }
    
    /**
     * @return Returns the awbCanceladoService.
     */
    public AwbCanceladoService getAwbCanceladoService() {
        return awbCanceladoService;
    }

    /**
     * @param awbCanceladoService The awbCanceladoService to set.
     */
    public void setAwbCanceladoService(AwbCanceladoService awbCanceladoService) {
        this.awbCanceladoService = awbCanceladoService;
    }

    /**
     * @return Returns the intervaloAwbService.
     */
    public IntervaloAwbService getIntervaloAwbService() {
        return intervaloAwbService;
    }

    /**
     * @param intervaloAwbService The intervaloAwbService to set.
     */
    public void setIntervaloAwbService(IntervaloAwbService intervaloAwbService) {
        this.intervaloAwbService = intervaloAwbService;
    }

    /**
     * @return Returns the valorPrestacaoContaService.
     */
    public ValorPrestacaoContaService getValorPrestacaoContaService() {
        return valorPrestacaoContaService;
    }

    /**
     * @param valorPrestacaoContaService The valorPrestacaoContaService to set.
     */
    public void setValorPrestacaoContaService(
            ValorPrestacaoContaService valorPrestacaoContaService) {
        this.valorPrestacaoContaService = valorPrestacaoContaService;
    }    

    /**
     * @return Returns the prestacaoContaService.
     */
    public PrestacaoContaService getPrestacaoContaService() {
        return prestacaoContaService;
    }

    /**
     * @param prestacaoContaService The prestacaoContaService to set.
     */
    public void setPrestacaoContaService(PrestacaoContaService prestacaoContaService) {
        this.prestacaoContaService = prestacaoContaService;
    }

    /**
     * @return Returns the awbService.
     */
    public AwbService getAwbService() {
        return awbService;
    }

    /**
     * @param awbService The awbService to set.
     */
    public void setAwbService(AwbService awbService) {
        this.awbService = awbService;
    }

    /**
     * @return Returns the icmsPrestacaoService.
     */
    public IcmsPrestacaoService getIcmsPrestacaoService() {
        return icmsPrestacaoService;
    }

    /**
     * @param icmsPrestacaoService The icmsPrestacaoService to set.
     */
    public void setIcmsPrestacaoService(IcmsPrestacaoService icmsPrestacaoService) {
        this.icmsPrestacaoService = icmsPrestacaoService;
    }
	
	public void setCiaFilialMercurioService(CiaFilialMercurioService ciaFilialMercurioService) {
		this.ciaFilialMercurioService = ciaFilialMercurioService;
}

	public CiaFilialMercurioService getCiaFilialMercurioService() {
		return ciaFilialMercurioService;
	}	
}
