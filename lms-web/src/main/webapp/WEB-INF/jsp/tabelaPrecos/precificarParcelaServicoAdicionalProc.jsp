<%@taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script language="javascript" type="text/javascript">
	function carregaDados() {
		onPageLoad();
		var u = new URL(parent.location.href);
		var idTabelaPrecoParcela = u.parameters["idTabelaPrecoParcela"];
		setElementValue("tabelaPrecoParcela.idTabelaPrecoParcela", idTabelaPrecoParcela);
		setElementValue("tabelaPrecoParcela.tabelaPreco.dsDescricao", u.parameters["tabelaPreco.dsDescricao"]);
		setElementValue("tabelaPrecoParcela.tabelaPreco.tabelaPrecoString", u.parameters["tabelaPreco.tabelaPrecoString"]);
		setElementValue("tabelaPrecoParcela.tabelaPreco.blEfetivada", u.parameters["tabelaPreco.blEfetivada"]);
		setElementValue("tabelaPrecoParcela.tabelaPreco.moeda.dsSimbolo", u.parameters["tabelaPreco.moeda.dsSimbolo"]);

		setElementValue("tabelaPrecoParcela.parcelaPreco.nmParcela", u.parameters["parcelaPreco.nmParcela"]);
		setElementValue("tabelaPrecoParcela.parcelaPreco.tpParcelaPreco", u.parameters["parcelaPreco.tpParcelaPreco.description"]);
		setElementValue("tabelaPrecoParcela.parcelaPreco.tpIndicadorCalculo", u.parameters["parcelaPreco.tpIndicadorCalculo.description"]);

		if(getElementValue('tabelaPrecoParcela.tabelaPreco.blEfetivada') == "true" || u.parameters["tabelaPreco.idPendencia"] != "" || u.parameters["isVisualizacaoWK"] == "true"){
			setDisabled('btnSalvar', true);
		}

		//verifica se já existe preço para a parcela
		var filter = new Array();
		setNestedBeanPropertyValue(filter, "idValorServicoAdicional", idTabelaPrecoParcela);
		var sdo = createServiceDataObject("lms.tabelaprecos.valorServicoAdicionalService.find", "carregaDados", filter);
		xmit({serviceDataObjects:[sdo]});
	}

	function carregaDados_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return;
		}
		var idValorServicoAdicional = getNestedBeanPropertyValue(data,":0.idValorServicoAdicional");
		//se já existe preço para a parcela
		//atribui os valores nos campos (para edição)
		if(idValorServicoAdicional > 0) {
			setElementValue("idValorServicoAdicional", idValorServicoAdicional);
			var obj = document.getElementById("vlServico");
			setElementValue(obj, setFormat(obj, getNestedBeanPropertyValue(data,":0.vlServico")));
			obj = document.getElementById("vlMinimo");
			setElementValue(obj, setFormat(obj, getNestedBeanPropertyValue(data,":0.vlMinimo")));
		}
	}

	function storeCallBack_cb(data, error, key) {
		if (error != undefined && error != "" && error != null) {
			alert(error);
		} else {
			store_cb(data, error);
			if (data.msgAtualizacaoAutomatica != undefined){
				alert(data.msgAtualizacaoAutomatica)
			}
		}
	}

</script>
<adsm:window
	service="lms.tabelaprecos.precificarParcelaServicoAdicionalAction"
	onPageLoad="carregaDados">
	<adsm:form
		action="/tabelaPrecos/precificarParcelaServicoAdicional"
		idProperty="idValorServicoAdicional">

		<adsm:hidden
			property="tabelaPrecoParcela.idTabelaPrecoParcela"/>

		<adsm:hidden
			property="tabelaPrecoParcela.tabelaPreco.blEfetivada"
			serializable="false"/>

		<adsm:complement
			label="tabelaBase">
			<adsm:textbox
				property="tabelaPrecoParcela.tabelaPreco.tabelaPrecoString"
				dataType="text"
				size="8"
				maxLength="7"
				disabled="true"
				serializable="false"/>

			<adsm:textbox
				property="tabelaPrecoParcela.tabelaPreco.dsDescricao"
				dataType="text"
				size="30"
				disabled="true"
				serializable="false"/>

		</adsm:complement>

		<adsm:textbox
			label="moeda"
			property="tabelaPrecoParcela.tabelaPreco.moeda.dsSimbolo"
			dataType="text"
			size="5"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			label="parcela"
			property="tabelaPrecoParcela.parcelaPreco.nmParcela"
			dataType="text"
			size="41"
			width="80%"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			label="tipoParcela"
			property="tabelaPrecoParcela.parcelaPreco.tpParcelaPreco"
			dataType="text"
			size="15"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			label="indicadorCalculo"
			property="tabelaPrecoParcela.parcelaPreco.tpIndicadorCalculo"
			dataType="text"
			size="15"
			disabled="true"
			serializable="false"/>

		<adsm:textbox
			label="valor"
			property="vlServico"
			minValue="0"
			dataType="currency"
			maxLength="18"
			size="10"
			required="true"/>

		<adsm:textbox
			label="valorMinimo"
			property="vlMinimo"
			minValue="0"
			dataType="currency"
			maxLength="18"
			size="10"/>

		<adsm:buttonBar>
			<adsm:storeButton
				id="btnSalvar"
				service="lms.tabelaprecos.precificarParcelaServicoAdicionalAction.storeAtualizaTabela"
				callbackProperty="storeCallBack"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
