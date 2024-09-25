<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="branco" service="lms.municipios.manterRotasViagemAction">
	<adsm:form action="/municipios/manterRotasViagem" idProperty="idRotaViagem" >
		
		<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" label="vigenciaInicial" required="true" />
		
		<adsm:buttonBar>
			<adsm:button caption="salvar" service="lms.municipios.manterRotasViagemAction.storeAlterarRota"
					callbackProperty="store" disabled="false" />			
			<adsm:button caption="fechar" onclick="self.close();" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window> 
<script>
	function store_cb(dados, erros) {
		if (erros == undefined) {
			dados.acaoVigenciaAtual=3;
			dialogArguments.rotaViagemLoad_cb(dados, erros);
			window.close();
		} else
			alert(erros);
	}

	if (dialogArguments.document.forms[0].elements["idRotaViagem"])
		document.forms[0].elements["idRotaViagem"].value = dialogArguments.document.forms[0].elements["idRotaViagem"].value;
		
</script>  