package com.mercurio.lms.contasreceber.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.contasreceber.report.EmitirMaioresDevedoresService;
import com.mercurio.lms.contasreceber.report.EmitirMaioresDevedoresSinteticoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * @author Rafael Andrade de Oliveira
 * @since 28/03/2006
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.emitirMaioresDevedoresAction"
 */

public class EmitirMaioresDevedoresAction extends ReportActionSupport {

	EmitirMaioresDevedoresService emitirMaioresDevedoresService;
	
	EmitirMaioresDevedoresSinteticoService emitirMaioresDevedoresSinteticoService;
	
	FilialService filialService;

	public void setEmitirMaioresDevedoresService(EmitirMaioresDevedoresService emitirMaioresDevedoresService) {
		this.emitirMaioresDevedoresService = emitirMaioresDevedoresService;
	}

	public void setEmitirMaioresDevedoresSinteticoService(EmitirMaioresDevedoresSinteticoService emitirMaioresDevedoresSinteticoService) {
		this.emitirMaioresDevedoresSinteticoService = emitirMaioresDevedoresSinteticoService;
	}

	private DomainValueService domainValueService;
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	@Override
	public java.io.File execute(TypedFlatMap parameters) throws Exception {
		
		boolean sintetico = parameters.getBoolean("soTotais").booleanValue();

		if (sintetico) {
			this.reportServiceSupport = this.emitirMaioresDevedoresSinteticoService;
		} else {
			this.reportServiceSupport = this.emitirMaioresDevedoresService;
		}
		
		return super.execute(parameters);
	}

    /**
     * Busca a data limite ( data atual + 40 anos )
     * @return String data limite
     */
    public String findDataLimite(){
    	YearMonthDay dtLimite = JTDateTimeUtils.getDataAtual();
    	dtLimite = JTDateTimeUtils.setYear(dtLimite, dtLimite.getYear() + 40);
    	return JTFormatUtils.format(dtLimite, JTFormatUtils.YEARMONTHDAY);
    } 
	
    /**
     * Busca a data atual
     * @return String data atual
     */
    public String findDataAtual(){
    	return JTFormatUtils.format(JTDateTimeUtils.getDataAtual(), JTFormatUtils.YEARMONTHDAY);
    } 
	
    /**
	 * Busca a filial do usu�rio e verifica se a filial do usu�rio � a Matriz
	 * @param tfm Crit�rios de pesquisa
	 * @return TypedFlatMap com dados de filial e a informa��o se a filial � ou n�o a Matriz
	 */
	public TypedFlatMap findFilialUsuario(TypedFlatMap tfm){
		
		Filial filialUsuario = SessionUtils.getFilialSessao();
		
		tfm = new TypedFlatMap();
		
		tfm.put("idFilial",filialUsuario.getIdFilial());
		tfm.put("sgFilial",filialUsuario.getSgFilial());
		tfm.put("pessoa.nmFantasia",filialUsuario.getPessoa().getNmFantasia());
		
		// Seta no Map se a filial do usu�rio logado � Matriz
		tfm.put("filialUserIsMatriz", SessionUtils.isFilialSessaoMatriz());
		
		return tfm;
	}
	
    /**
     * Busca a moeda do usuario logado
     * @return Moeda
     */
    public Moeda findMoedaUsuario(){
    	return SessionUtils.getMoedaSessao();
    } 
    
    public List findTipoCliente(Map criteria){
        List dominiosValidos = new ArrayList();
        dominiosValidos.add("E");
        dominiosValidos.add("S");
        dominiosValidos.add("P");
        List retorno = domainValueService.findByDomainNameAndValues("DM_TIPO_CLIENTE", dominiosValidos);
        return retorno;
	}
    
    public List findLookupFilial(TypedFlatMap criteria) {
		return filialService.findLookupFilial(criteria);
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}