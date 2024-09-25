<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.manterGruposRegioesAction" >
	<adsm:form action="/tabelaPrecos/manterGruposRegioes" idProperty="idGruposRegioes" >
		
		<adsm:hidden property="uf.siglaDescricao"/>
		<adsm:combobox 
			label="uf"
			service="lms.tabelaprecos.manterGruposRegioesAction.findUnidadeFederativaByPais"
			property="uf.idUnidadeFederativa"
			optionLabelProperty="sgUnidadeFederativa"
			optionProperty="idUnidadeFederativa"
			onlyActiveValues="true"
			labelWidth="12%"
			width="40%"
			boxWidth="150"
		>
			<adsm:propertyMapping relatedProperty="uf.siglaDescricao" modelProperty="siglaDescricao"/>
		</adsm:combobox>
		
		<adsm:lookup
			service="lms.tabelaprecos.manterAjustesTarifaAction.findTabelaPreco" 
			action="tabelaPrecos/manterTabelasPreco" 
			label="tabela" 
			property="tabelaPrecoByIdTabelaPreco" 
			idProperty="idTabelaPreco"
			criteriaProperty="tabelaPrecoString"
			dataType="text" 
			size="8" 
			maxLength="7" 
			labelWidth="12%" 
			width="36%">
			<adsm:propertyMapping relatedProperty="tabelaPrecoByIdTabelaPreco.dsDescricao" 		modelProperty="dsDescricao"/>
			<adsm:propertyMapping relatedProperty="tabelaPrecoByIdTabelaPreco.idTabelaPreco" 	modelProperty="tabelaPreco.idTabelaPreco" />	

			<adsm:textbox
			 	property="tabelaPrecoByIdTabelaPreco.dsDescricao"
			 	dataType="text"
			 	size="35"
			 	disabled="true"
			 	maxLength="60"/>
		</adsm:lookup>

		<adsm:hidden property="grupoRegiao.dsGrupoRegiao"/>
		<adsm:combobox
			label="grupoRegiao"
			service="lms.tabelaprecos.manterGruposRegioesAction.findGruposRegioes"
			property="grupoRegiao"
			optionLabelProperty="dsGrupoRegiao"
			optionProperty="idGrupoRegiao"
			onlyActiveValues="true"
			labelWidth="12%"
			width="70%"
			boxWidth="150"
			
		>
			<adsm:propertyMapping relatedProperty="grupoRegiao.dsGrupoRegiao" modelProperty="dsGrupoRegiao"/>
		</adsm:combobox>
				
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gruposRegioes"/>
			<adsm:resetButton />
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid 
		idProperty="idGrupoRegiao" property="gruposRegioes" gridHeight="250" unique="true" rows="18" >

		<adsm:gridColumn title="gruposRegioes"		property="gruposRegioes.dsGrupoRegiao" 	width="22%" />
		<adsm:gridColumn title="uf" 				property="uf.sgUnidadeFederativa" 		width="12%" />
		<adsm:gridColumn title="tabela" 			property="tabela" 						width="18%" />		
		<adsm:gridColumn title="tipoAjuste" 		property="tipoAjuste" 					width="11%" />
		<adsm:gridColumn title="tipoValorAjuste"	property="tipoValorAjuste" 				width="11%" />
		<adsm:gridColumn title="valorAjustePadrao" 	property="valorAjustePadrao" 			width="13%" dataType="decimal" />
		<adsm:gridColumn title="valorAjusteMinimo" 	property="valorAjusteMinimo" 			width="13%" dataType="decimal" />

		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>	
	 
</adsm:window>
<script type="text/javascript">
<!--

function setaFocus() {
	focus(document.getElementById("grupoRegiao"));
}

function onclickPickerLookupTabelaPreco() {
	var tabelaPrecoString = getElementValue("tabelaPreco.tabelaPrecoString");
	if(tabelaPrecoString != "") {
		setElementValue("tabelaPreco.tabelaPrecoString","");
	}
	lookupClickPicker({e:document.forms[0].elements['tabelaPreco.idTabelaPreco']});
	//if(tabelaPrecoString != "")
	if(getElementValue("tabelaPreco.tabelaPrecoString")=='' && tabelaPrecoString != "") {
		setElementValue("tabelaPreco.tabelaPrecoString",tabelaPrecoString);
	}
}

 
//-->
</script>
