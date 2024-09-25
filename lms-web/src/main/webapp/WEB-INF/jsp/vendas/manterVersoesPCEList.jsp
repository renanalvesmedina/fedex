<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterVersoesPCEAction">
	<adsm:form action="/vendas/manterVersoesPCE">
		<adsm:lookup service="lms.vendas.manterVersoesPCEAction.findLookupCliente" dataType="text" property="cliente" idProperty="idCliente"
					criteriaProperty="pessoa.nrIdentificacao" label="cliente" size="20" maxLength="20" labelWidth="15%" 
					width="85%" action="/vendas/manterDadosIdentificacao" exactMatch="true" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado">
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="35" disabled="true"/>
			<adsm:propertyMapping criteriaProperty="tpCliente" modelProperty="tpCliente"/>
		</adsm:lookup>
		<adsm:hidden property="tpCliente" value="S" serializable="false"/>
		
		<adsm:textbox maxLength="8" size="8" dataType="integer" property="nrVersaoPce" label="versao"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="VersaoPce"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idVersaoPce" property="VersaoPce" defaultOrder="cliente_pessoa_.nmPessoa,nrVersaoPce"
			   selectionMode="check" gridHeight="200" unique="true" autoSearch="true" rows="13">
		<adsm:gridColumn title="identificacao" property="cliente.pessoa.tpIdentificacao" isDomain="true" width="45" />
		<adsm:gridColumn title="" property="cliente.pessoa.nrIdentificacaoFormatado" dataType="text" align="right" width="130" />
		<adsm:gridColumn title="cliente" property="cliente.pessoa.nmPessoa"/>
		<adsm:gridColumn title="versao" property="nrVersaoPce" dataType="integer" width="93" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="93" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="93" /> 
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
<!--
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
		var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("descritivos",true);
		var tabCad = tabGroup.getTab("cad");
			tabCad.getElementById("cliente.pessoa.nrIdentificacao").masterLink = "false";
		}
	}
//-->
</script>