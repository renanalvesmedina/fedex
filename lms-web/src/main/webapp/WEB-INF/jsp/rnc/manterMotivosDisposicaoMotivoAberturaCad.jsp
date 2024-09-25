<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.rnc.motAberturaMotDisposicaoService">
	<adsm:form action="/rnc/manterMotivosDisposicaoMotivoAbertura" idProperty="idMotAberturaMotDisposicao">
		<adsm:combobox property="motivoAberturaNc.idMotivoAberturaNc" onlyActiveValues="true" label="motivoAberturaNaoConformidade" service="lms.rnc.motivoAberturaNcService.findOrderByDsMotivoAbertura"  optionLabelProperty="dsMotivoAbertura" optionProperty="idMotivoAberturaNc" labelWidth="32%" width="68%" required="true" />
		<adsm:combobox property="motivoDisposicao.idMotivoDisposicao" onlyActiveValues="true" label="motivoDisposicao" service="lms.rnc.motivoDisposicaoService.findOrderByDsMotivo"  optionLabelProperty="dsMotivo" optionProperty="idMotivoDisposicao" labelWidth="32%" width="68%" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>			
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>