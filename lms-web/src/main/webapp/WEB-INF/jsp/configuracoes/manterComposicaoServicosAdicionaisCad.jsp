<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.composicaoServicoService">
	<adsm:form action="/configuracoes/manterComposicaoServicosAdicionais" idProperty="idComposicaoServico">
	    <adsm:hidden property="servicoAdicional.idServicoAdicional"/>
  	    <adsm:textbox dataType="text" property="servicoAdicional.dsServicoAdicional" label="servicoAdicional" maxLength="60" size="70" disabled="true" width="85%" />
        <adsm:combobox onlyActiveValues="true" property="servico.idServico" label="servico" service="lms.configuracoes.servicoService.find" optionLabelProperty="dsServico" optionProperty="idServico" width="85%" required="true"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true"/>
        <adsm:buttonBar>
		   <adsm:storeButton/>
           <adsm:newButton/>
		   <adsm:removeButton/>
	    </adsm:buttonBar>
	</adsm:form>
</adsm:window>