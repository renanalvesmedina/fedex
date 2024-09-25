<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.diaFaturamentoService" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterDiasFaturamentoDivisao" idProperty="idDiaFaturamento">
		<adsm:complement label="cliente" width="85%" separator="branco">
			<adsm:textbox dataType="text" disabled="true" size="20"
				property="divisaoCliente.cliente.pessoa.nrIdentificacao"
				serializable="false"/>
			<adsm:textbox dataType="text" disabled="true" size="30"
				property="divisaoCliente.cliente.pessoa.nmPessoa"
				serializable="false" />	
		</adsm:complement>
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="divisaoCliente.idDivisaoCliente"/>
		<adsm:textbox property="divisaoCliente.dsDivisaoCliente" size="51"
			label="divisao" labelWidth="15%" dataType="text" width="40%" 
			disabled="true" serializable="false"/>
		<adsm:combobox boxWidth="100" onchange="buscaServicos();" property="tpModal" label="modal" domain="DM_MODAL" labelWidth="20%" width="25%"/>

		<adsm:combobox boxWidth="100" onchange="buscaServicos();" property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" labelWidth="15%" width="40%"/>

		<adsm:combobox boxWidth="140" property="servico.idServico" label="servico" 
			optionLabelProperty="dsServico" optionProperty="idServico" 
			service="lms.configuracoes.servicoService.find"  labelWidth="20%" width="25%">
			<adsm:propertyMapping relatedProperty="tpModal" 
				modelProperty="tpModal"/>
			<adsm:propertyMapping relatedProperty="tpAbrangencia" 
				modelProperty="tpAbrangencia"/>
		</adsm:combobox>
		
		<adsm:combobox boxWidth="80" property="tpFrete" label="tipoFrete" domain="DM_TIPO_FRETE" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="diaFaturamento"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idDiaFaturamento" property="diaFaturamento" onSelectAll="permissaoExcluir" onSelectRow="permissaoExcluir"
		gridHeight="200" defaultOrder="tpModal, tpAbrangencia, tpFrete" unique="true">
		<adsm:gridColumn title="modal" property="tpModal" isDomain="true" width="20%" />
		<adsm:gridColumn title="abrangencia" property="tpAbrangencia" isDomain="true" width="20%" />
		<adsm:gridColumn title="tipoFrete" property="tpFrete" isDomain="true" width="20%" />
		<adsm:gridColumn title="periodicidade" property="tpPeriodicidade" isDomain="true" width="20%" />
		<adsm:gridColumn title="diaRefFaturamento" property="diaRefFaturamento" width="20%" align="left" />
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
	function buscaServicos(){
		var modal = getElementValue("tpModal");
		var abrangencia = getElementValue("tpAbrangencia");
		resetServicos({tpModal:modal,tpAbrangencia:abrangencia});
	}
	
	function resetServicos(criteria) {
		var sdo = createServiceDataObject("lms.configuracoes.servicoService.find", "servico.idServico", criteria);
      	xmit({serviceDataObjects:[sdo]});		
	}
	
	function initWindow(eventObj) {
		if(eventObj.name == "cleanButton_click"){
			resetServicos();
		} 
    }
    
   	function permissaoExcluir(data){
		if (document.getElementById("permissao").value!="true") {
			setDisabled("removeButton", true);
			return false;
		}
	}
	
	
	function myPageLoad_cb(){
		onPageLoad_cb();
		var idFilial = getElementValue("idFilial");
		var data = new Array();	
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdd = createServiceDataObject("lms.vendas.manterDiasFaturamentoDivisaoAction.validatePermissao", "validarPermissoes", data);
		xmit({serviceDataObjects:[sdd]});
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
	
//-->
</script>
