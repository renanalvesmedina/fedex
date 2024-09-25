<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.manterSetoresMotivoAberturaNaoConformidadeAction">
	<adsm:form action="/rnc/manterSetoresMotivoAberturaNaoConformidade">
		<adsm:combobox labelWidth="32%" width="68%" label="motivoAberturaNaoConformidade" 
					   property="motivoAberturaNc.idMotivoAberturaNc" 
					   service="lms.rnc.motivoAberturaNcService.findOrderByDsMotivoAbertura" 
					   optionProperty="idMotivoAberturaNc"
					   optionLabelProperty="dsMotivoAbertura" 
		/>
		
		<adsm:combobox labelWidth="32%" width="68%"label="setor" 
					    property="setor.idSetor"
					    service="lms.configuracoes.setorService.findSetorOrderByDsSetor" 
					    optionProperty="idSetor" 
					    optionLabelProperty="dsSetor" 
		/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="setorMotivoAberturaNc"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="setorMotivoAberturaNc" idProperty="idSetorMotivoAberturaNc" gridHeight="200" unique="true" rows="13"
				service="lms.rnc.manterSetoresMotivoAberturaNaoConformidadeAction.findPaginatedSetorMotivo" rowCountService="lms.rnc.manterSetoresMotivoAberturaNaoConformidadeAction.getRowCount"
				defaultOrder="motivoAberturaNc_.dsMotivoAbertura, setor_.dsSetor:asc">
		<adsm:gridColumn title="motivoAberturaNaoConformidade" property="motivoAberturaNc.dsMotivoAbertura" width="50%" />
		<adsm:gridColumn title="setor" property="setor.dsSetor" width="50%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>