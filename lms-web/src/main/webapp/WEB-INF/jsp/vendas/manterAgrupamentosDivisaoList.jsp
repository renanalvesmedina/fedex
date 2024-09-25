<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterAgrupamentosDivisaoAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterAgrupamentosDivisao" idProperty="idAgrupamentoCliente">
		<adsm:complement label="cliente" labelWidth="17%" width="83%">
			<adsm:textbox dataType="text" disabled="true" 
				property="divisaoCliente.cliente.pessoa.nrIdentificacao"
				serializable="false" size="20"/>
			<adsm:textbox dataType="text" disabled="true" 
				property="divisaoCliente.cliente.pessoa.nmPessoa"
				serializable="false" size="30"/>
		</adsm:complement>
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="divisaoCliente.idDivisaoCliente"/>
		<adsm:hidden property="divisaoCliente.cliente.idCliente" serializable="false"/>
		<adsm:textbox dataType="text" disabled="true" label="divisao" 
			labelWidth="17%" property="divisaoCliente.dsDivisaoCliente"
			serializable="false" size="51" width="83%"/>
	    		<adsm:combobox property="formaAgrupamento.idFormaAgrupamento" autoLoad="false" label="formaAgrupamento" 
			optionLabelProperty="dsFormaAgrupamento" optionProperty="idFormaAgrupamento" 
			service="lms.vendas.manterAgrupamentosDivisaoAction.findByCliente"
			labelWidth="17%" boxWidth="200" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton  callbackProperty="agrupamentoCliente"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idAgrupamentoCliente" onSelectAll="permissaoExcluir" onSelectRow="permissaoExcluir" property="agrupamentoCliente" defaultOrder="formaAgrupamento_.dsFormaAgrupamento" gridHeight="200" unique="true" rows="12">
		<adsm:gridColumn title="formaAgrupamento" property="formaAgrupamento.dsFormaAgrupamento" width="100%" />
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript">
	function myPageLoad_cb(){
	onPageLoad_cb();
	var auxCliente = getElementValue("divisaoCliente.cliente.idCliente");
	var sdo = createServiceDataObject("lms.vendas.manterAgrupamentosDivisaoAction.findByCliente", "formaAgrupamento.idFormaAgrupamento", {cliente:{idCliente:auxCliente}});
    xmit({serviceDataObjects:[sdo]});
	var idFilial = getElementValue("idFilial");
	var data = new Array();	
	setNestedBeanPropertyValue(data, "idFilial", idFilial);
	var sdd = createServiceDataObject("lms.vendas.manterAgrupamentosDivisaoAction.validatePermissao", "validarPermissoes", data);
	xmit({serviceDataObjects:[sdd]});
	}
	
	function permissaoExcluir(data){
		if (document.getElementById("permissao").value!="true") {
			setDisabled("removeButton", true);
			return false;
		}
	}
	
	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validarPermissoes_cb(data, error){
		setElementValue("permissao", data._value);
		if(data._value!="true") {
			setDisabled("removeButton", true); 
		}
	}
	
	// Seta a propriedade masterLink para true para que o campo não seja limpo com a funcionalidade "Limpar"
	document.getElementById("permissao").masterLink = "true";
</script>
