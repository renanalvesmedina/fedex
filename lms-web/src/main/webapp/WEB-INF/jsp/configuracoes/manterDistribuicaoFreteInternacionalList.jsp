<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window
	service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction">
	<adsm:form action="/configuracoes/manterDistribuicaoFreteInternacional">

		<adsm:lookup label="filialOrigem" 
					 property="filialOrigem"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
					 service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.findLookupFiliaisList" 
					 dataType="text"
					 size="3" 
					 maxLength="3" 
					 action="/municipios/manterFiliais"
					 onDataLoadCallBack="filialOrigem">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialOrigem.pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" disabled="true" />
		</adsm:lookup>

		<adsm:textbox label="permisso" property="cdPermisso"
			dataType="integer" size="8" maxLength="4" />

		<adsm:lookup label="filialDestino" 
					 property="filialDestino"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
					 service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.findLookupFiliaisList" 
					 dataType="text"
					 size="3" 
					 maxLength="3" 
					 onDataLoadCallBack="filialDestino"
					 action="/municipios/manterFiliais">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialDestino.pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" disabled="true" />
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="distribuicoes" />
			<adsm:resetButton />
		</adsm:buttonBar>

		<script>
			//Mensagens i18n
			var lms00044 = parent.i18NLabel.getLabel("LMS-00044");
		</script>

	</adsm:form>
	<adsm:grid idProperty="idDistrFreteInternacional" property="distribuicoes" gridHeight="200" rows="12"
		       defaultOrder="filialOrigem_.sgFilial, filialOrigem_pessoa_.nmPessoa, filialDestino_.sgFilial, filialDestino_pessoa_.nmPessoa">
		<adsm:gridColumn title="filialOrigem" property="filialOrigem.siglaNomeFilial" width="20%" />
		<adsm:gridColumn title="filialDestino" property="filialDestino.siglaNomeFilial" width="20%" />
		<adsm:gridColumn title="permisso" property="cdPermisso" width="8%" dataType="integer" />
		<adsm:gridColumn title="distanciaKM" property="distanciaKm" width="8%" dataType="integer" />
		<adsm:gridColumn title="tempo" property="nrTempoViagem" width="10%" dataType="currency" />
		<adsm:gridColumn title="freteOrigemPercentual" property="pcFreteOrigem" width="12%" dataType="currency" />
		<adsm:gridColumn title="freteDestinoPercentual" property="pcFreteDestino" width="11%" dataType="currency" />
		<adsm:gridColumn title="freteExternoPercentual" property="pcFreteExterno" width="11%" dataType="currency" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	function filialOrigem_cb(data,erro){
		filialOrigem_sgFilial_exactMatch_cb(data);
		if (data[0] != undefined){
			if (data[0].idFilial == getElementValue("filialDestino.idFilial")){
				alert(lms00044);
				resetValue(document.getElementById("filialOrigem.idFilial"));
				setFocus(document.getElementById("filialOrigem.sgFilial"));
			}
		}
	}

	function filialDestino_cb(data,erro){
		filialDestino_sgFilial_exactMatch_cb(data);
		if (data[0] != undefined){
			if (data[0].idFilial == getElementValue("filialOrigem.idFilial")){
				alert(lms00044);
				resetValue(document.getElementById("filialDestino.idFilial"));
				setFocus(document.getElementById("filialDestino.sgFilial"));
			}
		}
	}

</script>
