<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterAgrupamentosDivisaoAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterAgrupamentosDivisao" idProperty="idAgrupamentoCliente" onDataLoadCallBack="myDataLoad">
		<adsm:complement label="cliente" labelWidth="17%" 
			required="true" width="83%">
			<adsm:textbox dataType="text" disabled="true" 
				property="divisaoCliente.cliente.pessoa.nrIdentificacao"
				serializable="false"size="20"/>
			<adsm:textbox dataType="text" disabled="true" 
				property="divisaoCliente.cliente.pessoa.nmPessoa"
				serializable="false" size="30"/>
		</adsm:complement>
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="divisaoCliente.idDivisaoCliente"/>
		<adsm:hidden property="divisaoCliente.cliente.idCliente" serializable="false"/>
		<adsm:textbox dataType="text" disabled="true" label="divisao" 
			labelWidth="17%" property="divisaoCliente.dsDivisaoCliente"
			required="true"	serializable="false" size="51" width="83%"/>
		<adsm:combobox property="formaAgrupamento.idFormaAgrupamento" autoLoad="false" label="formaAgrupamento" 
			optionLabelProperty="dsFormaAgrupamento" optionProperty="idFormaAgrupamento" onlyActiveValues="true"
			service="lms.vendas.manterAgrupamentosDivisaoAction.findByCliente" required="true" 
			labelWidth="17%" boxWidth="200">
			<adsm:propertyMapping 
				relatedProperty="formaAgrupamento.dsFormaAgrupamento" 
				modelProperty="dsFormaAgrupamento"
			/>
		</adsm:combobox>

		<adsm:hidden property="formaAgrupamento.dsFormaAgrupamento" serializable="false"/>
		
		<adsm:buttonBar>
			<adsm:button caption="tiposAgrupamento" action="/vendas/manterTiposAgrupamento" cmd="main">	
				<adsm:linkProperty src="idAgrupamentoCliente" target="agrupamentoCliente.idAgrupamentoCliente"/>
				<adsm:linkProperty src="idFilial" target="idFilial"/>
				<adsm:linkProperty src="formaAgrupamento.dsFormaAgrupamento" target="agrupamentoCliente.formaAgrupamento.dsFormaAgrupamento"/>
				<adsm:linkProperty src="divisaoCliente.dsDivisaoCliente" target="agrupamentoCliente.divisaoCliente.dsDivisaoCliente"/>
				<adsm:linkProperty src="divisaoCliente.cliente.pessoa.nrIdentificacao" target="agrupamentoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao"/>
				<adsm:linkProperty src="divisaoCliente.cliente.pessoa.nmPessoa" target="agrupamentoCliente.divisaoCliente.cliente.pessoa.nmPessoa"/>
			</adsm:button>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript">
function myPageLoad_cb(){
	onPageLoad_cb();
	 var auxCliente = getElementValue("divisaoCliente.cliente.idCliente");
	 var sdo = createServiceDataObject("lms.vendas.manterAgrupamentosDivisaoAction.findByCliente", "formaAgrupamento.idFormaAgrupamento", {cliente:{idCliente:auxCliente},tpSituacao:'A'});
     xmit({serviceDataObjects:[sdo]});
	}
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		validaPermissao();
	}
	
		
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validaPermissao(){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
		}
	}
	
	function initWindow(obj){
		if (obj.name == "tab_click" || obj.name == "gridRow_click"){
			validaPermissao();
			setElementValue("idFilial",getTabGroup(document).getTab("pesq").getElementById("idFilial").value)
		}
	}

</script>