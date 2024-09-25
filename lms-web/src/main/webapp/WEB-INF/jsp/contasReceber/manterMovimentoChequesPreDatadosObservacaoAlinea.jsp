<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window>
	<adsm:form id="observacao.form" action="/contasReceber/manterMovimentoChequesPreDatados">

		<adsm:lookup action="/contasReceber/manterAlineasDevolucoesCheques"
					 service="lms.contasreceber.manterChequesPreDatadosAction.findLookupAlinea" 
					 dataType="integer" 
					 required="true"
					 property="alinea" 
					 idProperty="idAlinea"
					 criteriaProperty="cdAlinea"
					 label="alinea" 
					 size="3" 
					 maxLength="2" 
					 width="40%"
					 labelWidth="10%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="dsAlinea" formProperty="alinea.dsAlinea"/>
			<adsm:propertyMapping modelProperty="cdAlinea" criteriaProperty="alinea.cdAlinea"/>
			<adsm:propertyMapping modelProperty="cdAlinea" formProperty="alinea.cdAlinea"/>
			<adsm:textbox dataType="text" property="alinea.dsAlinea" size="40" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:textarea label="observacao" property="observacao" width="90%" maxLength="255" labelWidth="10%" 
			columns="70" rows="5"/>

		<adsm:buttonBar>
			<adsm:button caption="confirmar" onclick="setValue();" disabled="false"/>
			<adsm:button caption="cancelar" onclick="javascript:window.close();" disabled="false"/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>

<script>
	function setValue(){
		valid = validateTabScript(document.getElementById("observacao.form"));

		// apenas prossegue se a valida??o dos dados foi realizada com sucesso.
		if (valid == false) {
			return false;
		}
			
		dialogArguments.window.document.getElementById("observacao").value = document.getElementById("observacao").value;
		dialogArguments.window.document.getElementById("alinea").value = document.getElementById("alinea.idAlinea").value;		
		dialogArguments.window.aplicarAcaoScript();
		window.close();
	}
</script>