<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.rnc.setorMotivoAberturaNcService">
	<adsm:form action="/rnc/manterSetoresMotivoAberturaNaoConformidade"
		idProperty="idSetorMotivoAberturaNc">
		<adsm:combobox labelWidth="32%" width="68%"
			label="motivoAberturaNaoConformidade"
			property="motivoAberturaNc.idMotivoAberturaNc"
			service="lms.rnc.motivoAberturaNcService.findOrderByDsMotivoAbertura"
			optionProperty="idMotivoAberturaNc"
			optionLabelProperty="dsMotivoAbertura"
			onlyActiveValues="true"
			required="true" />

		<adsm:combobox labelWidth="32%" width="68%" label="setor"
			property="setor.idSetor"
			service="lms.configuracoes.setorService.findSetorOrderByDsSetor"
			optionProperty="idSetor"
			optionLabelProperty="dsSetor"
			onlyActiveValues="true"
			required="true" />

		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
