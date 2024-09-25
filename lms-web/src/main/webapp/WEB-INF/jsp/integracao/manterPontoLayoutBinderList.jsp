<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window 
	service="lms.integracao.manterPontoLayoutBinderAction">
	
	<adsm:form action="/integracao/manterPontoLayoutBinder">
	
		<adsm:combobox property="pontoIntegracao.id"
			optionProperty="id" optionLabelProperty="nome"
			label="pontoIntegracao"
			service="lms.integracao.manterPontoLayoutBinderAction.findAllPontoIntegracao"
			width="100%" boxWidth="220" />
			
		<adsm:combobox property="layoutBinder.id" onchange="changeComboLayout()"
			optionProperty="id" optionLabelProperty="nome"
			label="layout" 
			service="lms.integracao.manterPontoLayoutBinderAction.findAllLayout"
			width="100%" boxWidth="220" />
			
		<adsm:combobox property="grupoLayout.id" onchange="changeComboGrupoLayout()"
			optionProperty="id" optionLabelProperty="nome"
			label="grupoLayout"
			service="lms.integracao.manterPontoLayoutBinderAction.findAllGruposLayout"
			width="100%" boxWidth="220" />
			
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="pontoLayoutBinder" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="id"
		property="pontoLayoutBinder" defaultOrder="nome" rows="10"
		rowCountService="lms.integracao.manterPontoLayoutBinderAction.getRowCount"
		service="lms.integracao.manterPontoLayoutBinderAction.findPaginated"
		selectionMode="check">

		<adsm:gridColumn title="pontoIntegracao" property="pontoBinder.nome"
			align="center" width="40%" />

		<adsm:gridColumn title="layout" property="nmLayoutFormated"
			align="center" width="30%" />

		<adsm:gridColumn title="grupoLayout" property="nmGrupoLayoutFormated"
			align="center" width="30%" />

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
	function changeComboLayout() {
		var grupoLayout = document.getElementById("grupoLayout.id").value;
		if (grupoLayout != '')
			setElementValue("grupoLayout.id", '');
	}
	
	function changeComboGrupoLayout() {
		var layoutBinder = document.getElementById("layoutBinder.id").value;
		if (layoutBinder != '')
			setElementValue("layoutBinder.id", '');
	}		
		
</script>
 