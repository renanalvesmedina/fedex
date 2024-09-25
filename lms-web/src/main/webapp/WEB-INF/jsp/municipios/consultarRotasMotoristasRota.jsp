<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.consultarRotasViagemAction" >
	<adsm:grid property="motoristaRotaViagem" idProperty="idMotoristaRotaViagem" unique="true"
			service="lms.municipios.consultarRotasViagemAction.findMotoristas" onRowClick="onRowClickGrid();"
			selectionMode="none" scrollBars="vertical" showPagging="false" gridHeight="360" >
		<adsm:gridColumn title="identificacao" property="motorista_pessoa_tpIdentificacao.description" width="80" />
		<adsm:gridColumn title="" property="motorista_pessoa_nrIdentificacaoFormatado" width="120" align="right" />
		<adsm:gridColumn title="motorista" property="motorista_pessoa_nmPessoa" />	
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="90"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="80" />
	</adsm:grid>
	<adsm:buttonBar freeLayout="false" />
</adsm:window>
<script>
	function onRowClickGrid() {
		return false;
	}
	
	var ultimoIdRota = -1;
	function detalhamentoAba() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("rtIda");
		
		var idRotaViagem = tabDet.getFormProperty("idRotaViagem");
		if (idRotaViagem == ultimoIdRota)
			return false;
		ultimoIdRota = idRotaViagem;

		carregaGridPrincipal(idRotaViagem);
	}
	
	function carregaGridPrincipal(idRotaViagem) {
		if (idRotaViagem != undefined && idRotaViagem != ''){
			var data = new Array();
			setNestedBeanPropertyValue(data, "idRotaViagem", idRotaViagem);
			motoristaRotaViagemGridDef.executeSearch(data);
		} else 
			motoristaRotaViagemGridDef.resetGrid();
	}
</script>