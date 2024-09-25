<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterPotencialComercializacaoClienteAction" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterPotencialComercializacaoCliente">
	
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		
		<adsm:hidden property="cliente.idCliente"/>
		<adsm:textbox dataType="text" property="cliente.pessoa.nrIdentificacao" disabled="true" 
					  label="cliente" size="20" maxLength="20" width="82%" labelWidth="18%" serializable="false">
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" disabled="true" size="60"  serializable="false"/>
		</adsm:textbox>				
		
		<adsm:combobox property="tpFrete" domain="DM_TIPO_FRETE" label="tipoFrete" labelWidth="18%" width="82%"/>
		<adsm:combobox property="tpModal" domain="DM_MODAL" label="modal" labelWidth="18%" width="32%"/>
		<adsm:combobox property="tpAbrangencia" domain="DM_ABRANGENCIA" label="abrangencia" labelWidth="15%" width="35%"/>		 

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="potencialComercialCliente"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>	

	<adsm:grid gridHeight="200" unique="true" idProperty="idPotencialComercialCliente" rows="10"
			   service="lms.vendas.manterPotencialComercializacaoClienteAction.findPaginated"
			   rowCountService="lms.vendas.manterPotencialComercializacaoClienteAction.getRowCount"
			   property="potencialComercialCliente"  onSelectAll="myOnSelectAll" onSelectRow="myOnSelectRow" scrollBars="horizontal">

		<adsm:gridColumn title="paisOrigem" property="nmPaisOrigem" dataType="text" width="75"/>
		<adsm:gridColumn title="ufOrigem2" property="sgUfOrigem" width="50" />
		<adsm:gridColumn title="tipoLocalizacaoOrigem" property="tipoLocalizacaoOrigem" width="145" />
		<adsm:gridColumn title="paisDestino" property="nmPaisDestino" dataType="text" width="75"/>
		<adsm:gridColumn title="ufDestino2" property="sgUfDestino" width="50" />
		<adsm:gridColumn title="tipoLocalizacaoDestino" property="tipoLocalizacaoDestino" width="135" />
		<adsm:gridColumn title="tipoFrete" property="tpFrete" isDomain="true" width="80"/>
		<adsm:gridColumn title="modal" property="tpModal" isDomain="true" width="65"/>
		<adsm:gridColumn title="abrangencia" property="tpAbrangencia" isDomain="true" width="80"/>
		<adsm:gridColumn title="transportadora" property="dsTransportadora" width="100" />
		<adsm:gridColumn title="detencao" property="pcDetencao" width="65" unit="percent" dataType="percent"/>
		<adsm:gridColumn title="valorPotencialFaturamento" property="sgSimboloMoeda" width="55" align="left"/>
		<adsm:gridColumn title="" property="vlFaturamentoPotencial" width="55" dataType="currency" align="right"/>
		
		
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window>
<script>
	
	/**
	* Criada para validar o acesso do usuário 
	* logado à filial do cliente
	*/
	function myPageLoad_cb(data, error) {
		onPageLoad_cb();
		var idFilial = getElementValue("idFilial");
		var data = new Array();	   
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdo = createServiceDataObject("lms.vendas.potencialComercialClienteService.validatePermissao", "validarPermissoes", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	* Criada para validar o acesso do usuário 
	* logado à filial do cliente
	*/
	function validarPermissoes_cb(data, error){
		setElementValue("permissao", data._value);
		if(data._value!="true") {
			setDisabled("removeButton", true);
		}
	}

 	// Seta a propriedade masterLink para true para que o campo não seja limpo com a funcionalidade "Limpar"
	document.getElementById("permissao").masterLink = "true";

	/**
	* Esta função é utilizada para verificar a permissão de acesso pelo usuário
	* em relação a filial responsável operacional pelo cliente, desabilitando
	* o botão de excluir da listagem caso não tenha permissão.
	*/
	function myOnSelectRow(){

		var permissao = document.getElementById("permissao");
		
		if( permissao.value != "true" ){
			setDisabled("removeButton",true);
			return false;
		}
	}
	
	/**
	* Esta função deve executar exatamente a mesma tarefa que a função myOnSelectRow.
	*/
	function myOnSelectAll(){
		return myOnSelectRow();
	}

</script>