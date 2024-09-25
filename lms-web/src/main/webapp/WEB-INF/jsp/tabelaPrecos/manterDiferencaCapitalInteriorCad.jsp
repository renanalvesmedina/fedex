<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.tabelaprecos.manterDiferencaCapitalInteriorAction">

	<adsm:form action="/tabelaPrecos/manterDiferencaCapitalInterior" idProperty="idDiferencaCapitalInterior">
		
		<adsm:hidden property="ufOrigem.siglaDescricao"/>
		<adsm:combobox
			label="ufOrigem"
			service="lms.tabelaprecos.manterDiferencaCapitalInteriorAction.findUnidadeFederativaByPais"
			property="ufOrigem.idUnidadeFederativa"
			optionLabelProperty="sgUnidadeFederativa"
			optionProperty="idUnidadeFederativa"
			onlyActiveValues="true"
			labelWidth="20%"
			width="70%"
			boxWidth="150"
		>
			<adsm:propertyMapping relatedProperty="ufOrigem.siglaDescricao" modelProperty="siglaDescricao"/>
		</adsm:combobox>

		<adsm:hidden property="ufDestino.siglaDescricao"/>
		<adsm:combobox
			label="ufDestino"
			service="lms.tabelaprecos.manterDiferencaCapitalInteriorAction.findUnidadeFederativaByPais"
			property="ufDestino.idUnidadeFederativa"
			optionLabelProperty="sgUnidadeFederativa"
			optionProperty="idUnidadeFederativa"
			onlyActiveValues="true"
			labelWidth="20%"
			width="70%"
			boxWidth="150"
		>
			<adsm:propertyMapping relatedProperty="ufDestino.siglaDescricao" modelProperty="siglaDescricao"/>
		</adsm:combobox>
		
		<adsm:section caption="fretePeso"/>
		
		<adsm:textbox dataType="decimal" required="true" label="pcDiferencaPadrao" property="pcDiferencaPadrao" mask="###,##0.00" size="18" labelWidth="20%" width="30%"/>
		<adsm:textbox dataType="decimal" required="true" label="pcDiferencaMinima" property="pcDiferencaMinima" mask="###,##0.00" size="18" labelWidth="20%" width="30%"/>
						
		<adsm:section caption="freteValor"/>
			<adsm:textbox dataType="decimal" required="true" label="vlDiferencaPadrao" property="pcDiferencaPadraoAdvalorem" mask="###,##0.00" size="18" labelWidth="20%" width="30%"/>
			<adsm:textbox dataType="decimal" required="true" label="vlDiferencaMinima" property="pcDiferencaMinimaAdvalorem" mask="###,##0.00" size="18" labelWidth="20%" width="30%"/>

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
