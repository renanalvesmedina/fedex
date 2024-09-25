<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.prestcontasciaaerea.consultarPrestacaoContasAction">
	<adsm:form action="/prestContasCiaAerea/consultarPrestacaoContas">

		<adsm:hidden property="prestacaoConta.idPrestacaoConta" />
		<adsm:hidden property="idEmpresa" />
		<adsm:hidden property="idFilial" />

		<adsm:textbox  label="totalFrete" property="vlFrete" dataType="currency" disabled="true" 
			labelWidth="25%" width="25%"/>
		
		<adsm:textbox  label="irrfIssSobreComissao" property="vlIrrfIssComissao" dataType="currency" disabled="true" 
			labelWidth="25%" width="25%"/>
		
		<adsm:textbox  label="comissoesFrete" property="vlComissaoFrete" dataType="currency" disabled="true" 
			labelWidth="25%" width="25%"/>
		
		<adsm:textbox  label="valorPagarAgente" property="vlPagarAgente" dataType="currency" disabled="true" 
			labelWidth="25%" width="25%"/>
	</adsm:form>	
</adsm:window>

<script>

	function prepareFindValorPagarAgente(){
		if (getElementValue("vlFrete") != "" && getElementValue("vlIrrfIssComissao") != "" && getElementValue("vlComissaoFrete") != ""){
			findValorPagarAgente(getElementValue("vlFrete"), getElementValue("vlIrrfIssComissao"), getElementValue("vlComissaoFrete"));
		}
	}

	function findComissaoSobreFrete( idPrestacaoConta_, idEmpresa_, idFilial_ ){
		var filtro = {
			prestacaoConta : { idPrestacaoConta : idPrestacaoConta_ },
			empresa : { idEmpresa : idEmpresa_ },
			filial  : { idFilial  : idFilial_  }
		};
		var sdo = createServiceDataObject("lms.prestcontasciaaerea.consultarPrestacaoContasAction.findComissaoSobreFrete",
			"vlComissaoFrete", filtro );
		xmit({serviceDataObjects:[sdo]});
	}

	function vlComissaoFrete_cb(data,erro){
		if (erro != undefined){
			alert(''+erro);
			return;
		}
		if (data == undefined) return;
		if (data._value == undefined) return;
		setElementValue("vlComissaoFrete", data._value.replace(/[.]/g, ","));
		format(document.getElementById("vlComissaoFrete"));
		prepareFindValorPagarAgente();
	}
	
	function findValorPagarAgente(vlFrete_, vlIrrfIssComissao_, vlComissaoFrete_){
		var filtro = {
			vlFrete : vlFrete_, 
			vlIrrfIssComissao : vlIrrfIssComissao_, 
			vlComissaoFrete : vlComissaoFrete_
		};
		var sdo = createServiceDataObject("lms.prestcontasciaaerea.consultarPrestacaoContasAction.findValorPagarAgente",
			"vlPagarAgente", filtro );
		xmit({serviceDataObjects:[sdo]});
	}

	function vlPagarAgente_cb(data,erro){
		if (erro != undefined){
			alert(''+erro);
			return;
		}
		if (data == undefined) return;
		if (data._value == undefined) return;
		setElementValue("vlPagarAgente", data._value.replace(/[.]/g, ","));
		format(document.getElementById("vlPagarAgente"));
	}
	
	function findIRRSobreComissao(_idPrestacaoConta){
		var sdo = createServiceDataObject("lms.prestcontasciaaerea.consultarPrestacaoContasAction.findIRRFSobreComissao",
			"vlIrrfIssComissao",{prestacaoConta:{idPrestacaoConta:_idPrestacaoConta}});
		xmit({serviceDataObjects:[sdo]});
	}
	
	function vlIrrfIssComissao_cb(data,erro){
		if (erro != undefined){
			alert(''+erro);
			return;
		}
		if (data == undefined) return;
		if (data._value == undefined) return;
		setElementValue("vlIrrfIssComissao", data._value.replace(/[.]/g, ","));
		format(document.getElementById("vlIrrfIssComissao"));
		prepareFindValorPagarAgente();
	}

	function findTotalFrete(tipoValor){
		var sdo = createServiceDataObject("lms.prestcontasciaaerea.consultarPrestacaoContasAction.findVlTipoPrestacaoContaByTpValor",
			"vlFrete",{tpValor:tipoValor, idPrestacaoConta:getElementValue("prestacaoConta.idPrestacaoConta")});
		xmit({serviceDataObjects:[sdo]});
	}

	function vlFrete_cb(data,erro){
		if (erro != undefined){
			alert(''+erro);
			return;
		}
		if (data == undefined) return;
		if (data._value == undefined) return;
		setElementValue("vlFrete", data._value.replace(/[.]/g, ","));
		format(document.getElementById("vlFrete"));
		prepareFindValorPagarAgente();
	}

	function myOnShow(){
	
		findDemonstrativo();

	}

	function findDemonstrativo(){
		var idPrestacao = getElementValue("prestacaoConta.idPrestacaoConta");
		var idEmpresa = getElementValue("idEmpresa");
		var idFilial = getElementValue("idFilial");

		resetValue(document);

		setElementValue("prestacaoConta.idPrestacaoConta", idPrestacao);
		setElementValue("idEmpresa", idEmpresa);
		setElementValue("idFilial", idFilial);

		findTotalFrete("FR"); 
		findIRRSobreComissao(idPrestacao); 
		findComissaoSobreFrete(idPrestacao, getElementValue("idEmpresa"), getElementValue("idFilial")); 
	}
</script>