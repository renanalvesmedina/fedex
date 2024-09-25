<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.composicaoServicoService">
	<adsm:form action="/configuracoes/manterComposicaoServicosAdicionais" idProperty="idComposicaoServico">
	    <adsm:hidden property="servicoAdicional.idServicoAdicional"/>
	    <adsm:textbox dataType="text" property="servicoAdicional.dsServicoAdicional" label="servicoAdicional" maxLength="60" size="70" disabled="true" width="85%"/>
        <adsm:combobox property="servico.idServico" label="servico" service="lms.configuracoes.servicoService.find" optionLabelProperty="dsServico" optionProperty="idServico" width="85%"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="servicosAdicionais"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>	
    <adsm:grid idProperty="idComposicaoServico" defaultOrder="servicoAdicional_.dsServicoAdicional,servico_.dsServico" property="servicosAdicionais" selectionMode="check" gridHeight="200" rows="12">
   		<adsm:gridColumn width="40%" title="servicoAdicional" property="servicoAdicional.dsServicoAdicional" />
        <adsm:gridColumn width="40%" title="servico" property="servico.dsServico" />
    	<adsm:gridColumn width="20%" title="situacao" property="tpSituacao" isDomain="true"/>
 		<adsm:buttonBar> 
	   		<adsm:removeButton/>
	   	</adsm:buttonBar>	
    </adsm:grid>	
</adsm:window>
