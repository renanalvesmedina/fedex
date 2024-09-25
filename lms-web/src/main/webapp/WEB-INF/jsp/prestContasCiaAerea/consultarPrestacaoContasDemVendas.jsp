<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.prestcontasciaaerea.consultarPrestacaoContasAction">
	<adsm:form action="/prestContasCiaAerea/consultarPrestacaoContas">
	
		<adsm:hidden property="prestacaoConta.idPrestacaoConta" />
		<adsm:hidden property="isGol" />
	
		<adsm:label key="branco" style="text-align: middle;" width="20%"/>
		<adsm:label key="freteAPagar" style="text-align: center;" width="15%"/>
		<adsm:label key="branco"  width="5%"/>
		<adsm:label key="contaCorrente" style="text-align: center;" width="15%"/>
		<adsm:label key="branco"  width="5%"/>
		<adsm:label key="pagos" style="text-align: center;" width="15%"/>
		<adsm:label key="branco"  width="5%"/>
		<adsm:label key="total" width="15%" style="text-align: center;"/>
		<adsm:label key="branco"  width="5%"/>

		<adsm:label key="qtdeAWBs" width="20%"/>
		<adsm:textbox property="quantidadePagar" dataType="integer" mask="#" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="quantidadeConta" dataType="integer" mask="#" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="quantidadePagos" dataType="integer" mask="#" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="quantidadeTotal" dataType="integer" mask="#" disabled="true" width="20%" size="20"/>

		<adsm:label key="frete" width="20%"/>
		<adsm:textbox property="fretePagar" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="freteConta" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="fretePagos" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="freteTotal" dataType="currency" disabled="true" width="20%" size="20"/>

		<adsm:label key="taxaDeCombustivel" width="20%"/>
		<adsm:textbox property="origemPagar" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="origemConta" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="origemPagos" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="origemTotal" dataType="currency" disabled="true" width="20%" size="20"/>

		<adsm:label key="txTerDestino" width="20%"/>
		<adsm:textbox property="destinoPagar" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="destinoConta" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="destinoPagos" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="destinoTotal" dataType="currency" disabled="true" width="20%" size="20"/>

		<adsm:label key="adValorem" width="20%"/>
		<adsm:textbox property="valoremPagar" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="valoremConta" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="valoremPagos" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="valoremTotal" dataType="currency" disabled="true" width="20%" size="20"/>

		<adsm:label key="frapDevolvido" width="20%"/>
		<adsm:textbox property="frapPagar" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="frapConta" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="frapPagos" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="frapTotal" dataType="currency" disabled="true" width="20%" size="20"/>

		<adsm:label key="pesoTotal" width="20%"/>
		<adsm:textbox property="pesoPagar" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="pesoConta" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="pesoPagos" dataType="currency" disabled="true" width="20%" size="20"/>
		<adsm:textbox property="pesoTotal" dataType="currency" disabled="true" width="20%" size="20"/>

		<adsm:label key="totais" width="20%" style="font-weight: bold;"/>
		<adsm:textbox property="totaisPagar" dataType="currency" disabled="true" width="20%" style="font-weight: bold;" size="16"/>
		<adsm:textbox property="totaisConta" dataType="currency" disabled="true" width="20%" style="font-weight: bold;" size="16"/>
		<adsm:textbox property="totaisPagos" dataType="currency" disabled="true" width="20%" style="font-weight: bold;" size="16"/>
		<adsm:textbox property="totaisTotal" dataType="currency" disabled="true" width="20%" style="font-weight: bold;" size="16"/>
	</adsm:form>

</adsm:window>

<script>

	function myOnShow(){
	
		var idPrestacao = getElementValue("prestacaoConta.idPrestacaoConta");
		var isGol = getElementValue("isGol");

		resetValue(document);

		setElementValue("prestacaoConta.idPrestacaoConta", idPrestacao);
		setElementValue("isGol", isGol);

		findDemVendas(getElementValue("prestacaoConta.idPrestacaoConta"));

	}

	function findDemVendas(idPrestacaoConta_){
		var filtro = {
			idPrestacaoConta	: idPrestacaoConta_
		};
		var sdo = createServiceDataObject("lms.prestcontasciaaerea.consultarPrestacaoContasAction.findVlTipoPrestacaoConta",
			"demVendas",filtro);
		xmit({serviceDataObjects:[sdo]});
	}

	function fretePagar(valor, tpValor){
		if (tpValor == "QA"){
			setElementValue("quantidadePagar", valor);
		}else if (tpValor == "FR"){
			setElementValue("fretePagar", setFormat("fretePagar", valor));
		}else if (tpValor == "TC"){
			setElementValue("origemPagar", setFormat("origemPagar", valor));
		}else if (tpValor == "TD"){
			setElementValue("destinoPagar", setFormat("destinoPagar", valor));
		}else if (tpValor == "PE"){
			setElementValue("pesoPagar", setFormat("pesoPagar", valor));
		}
	}
	
	function pagos(valor, tpValor){
		if (getElementValue("isGol")=="true"){
			if (tpValor == "QA"){
				setElementValue("quantidadeConta", valor);
			}else if (tpValor == "FR"){
				setElementValue("freteConta", setFormat("freteConta", valor));
			}else if (tpValor == "TC"){
				setElementValue("origemConta", setFormat("origemConta", valor));
			}else if (tpValor == "TD"){
				setElementValue("destinoConta", setFormat("destinoConta", valor));
			}else if (tpValor == "PE"){
				setElementValue("pesoConta", setFormat("pesoConta", valor));
			}
		} else {
			if (tpValor == "QA"){
				setElementValue("quantidadePagos", valor);
			}else if (tpValor == "FR"){
				setElementValue("fretePagos", setFormat("fretePagos", valor));
			}else if (tpValor == "TC"){
				setElementValue("origemPagos", setFormat("origemPagos", valor));
			}else if (tpValor == "TD"){
				setElementValue("destinoPagos", setFormat("destinoPagos", valor));
			}else if (tpValor == "PE"){
				setElementValue("pesoPagos", setFormat("pesoPagos", valor));
			}
		}
	}
	
	/**
	 * Formata, utilizando o metodo da arquitetura, todos os campos do formulario.
	 */
	function formatAll(prefix_, sufix_){
		var prefix = (prefix_ == undefined) ? new Array("quantidade", "frete", "origem", "destino", "peso", "valorem", "frap", "totais") : prefix_;
		var sufix = (sufix_ == undefined ) ? new Array("Pagar", "Conta", "Pagos", "Total") : sufix_;
		for (var i = 0; i < prefix.length; i++){
			for (var j = 0; j < sufix.length; j++){
				var elementID  =  prefix[i] +  sufix[j];
				format(  document.getElementById(elementID) );
			}
		}
	}
	
	/**
	 * Atribui 0,00 para todos os campos.
	 */
	function setBlank2Zero(prefix_, sufix_){
		var prefix = (prefix_ == undefined) ? new Array("quantidade", "frete", "origem", "destino", "peso", "valorem", "frap", "totais") : prefix_;
		var sufix = (sufix_ == undefined ) ? new Array("Pagar", "Conta", "Pagos", "Total") : sufix_;
		for (var i = 0; i < prefix.length; i++){
			for (var j = 0; j < sufix.length; j++){
				var elementID  =  prefix[i] +  sufix[j];
				if (getElementValue(elementID) == ""){
					setElementValue(  elementID , "0,00");
				}
			}
		}
	}

	function findTotaisByTpFormaPagamento(idPrestacaoConta_){
		var sdo = createServiceDataObject("lms.prestcontasciaaerea.consultarPrestacaoContasAction.findTotaisByTpFormaPagamento",
			"totalColuna",{prestacaoConta : {idPrestacaoConta : idPrestacaoConta_ }});
		xmit({serviceDataObjects:[sdo]});
	}
	function totalColuna_cb(data,erro){
		if (erro != undefined || data == undefined) return;
		for (var i = 0; i < data.length; i++){
			if (data[i].tpFormaPagamento == "AP"){
				setElementValue("totaisPagar", setFormat("totaisPagar", data[i].vlTipoPrestacaoConta));
			}else if (data[i].tpFormaPagamento == "PG"){
				if (getElementValue("isGol")=="true"){
					setElementValue("totaisConta", setFormat("totaisConta", data[i].vlTipoPrestacaoConta));
				} else {
					setElementValue("totaisPagos", setFormat("totaisPagos", data[i].vlTipoPrestacaoConta));
				}
			}
		}
		setElementValue("totaisTotal", setFormat("totaisTotal", (Number(getElementValue("totaisPagar"))+Number(getElementValue("totaisConta"))+Number(getElementValue("totaisPagos"))).toString()));
	}

	function findTotaisByTpValor(idPrestacaoConta_){
		var sdo = createServiceDataObject("lms.prestcontasciaaerea.consultarPrestacaoContasAction.findTotaisByTpValor",
			"totalLinha",{prestacaoConta : {idPrestacaoConta : idPrestacaoConta_ }});
		xmit({serviceDataObjects:[sdo]});
	}

	function totalLinha_cb(data,erro){
		if (erro != undefined || data == undefined) return;
		for (var i = 0; i < data.length; i++){
			if (data[i].tpValor == "QA"){
				setElementValue( "quantidadeTotal", data[i].vlTipoPrestacaoConta);
				format( document.getElementById("quantidadeTotal") );
			}else if (data[i].tpValor == "FR"){
				setElementValue( "freteTotal", setFormat("freteTotal", data[i].vlTipoPrestacaoConta) );
				format( document.getElementById("freteTotal") );
			}else if (data[i].tpValor == "TC"){
				setElementValue( "origemTotal", setFormat("origemTotal", data[i].vlTipoPrestacaoConta) );
				format( document.getElementById("origemTotal") );
			}else if (data[i].tpValor == "TD"){
				setElementValue( "destinoTotal", setFormat("destinoTotal", data[i].vlTipoPrestacaoConta) );
				format( document.getElementById("destinoTotal") );
			}else if (data[i].tpValor == "PE"){
				setElementValue( "pesoTotal", setFormat("pesoTotal", data[i].vlTipoPrestacaoConta) );
				format( document.getElementById("pesoTotal") );
			}
		}
	}
	
	function demVendas_cb(data,error){
		if (error!=undefined){
			alert(''+error);
			return;
		}
		
		if (data == undefined) return;
		setBlank2Zero();		
		for (var i = 0; i < data.length; i++){
			if (data[i].tpFormaPagamento == "AP"){
				fretePagar(data[i].vlTipoPrestacaoConta, data[i].tpValor);
			}else if (data[i].tpFormaPagamento == "PG"){
				pagos(data[i].vlTipoPrestacaoConta, data[i].tpValor);
			}
		}
		var idPrestacaoConta = getElementValue("prestacaoConta.idPrestacaoConta");
		findTotaisByTpValor(idPrestacaoConta);
		findTotaisByTpFormaPagamento(idPrestacaoConta);
		formatAll();
	}
	
	
	
	
	
	
	
</script>
