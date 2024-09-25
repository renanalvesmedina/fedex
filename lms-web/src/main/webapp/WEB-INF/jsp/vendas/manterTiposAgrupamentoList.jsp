<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.tipoAgrupamentoService" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterTiposAgrupamento" idProperty="idTipoAgrupamento">
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="agrupamentoCliente.idAgrupamentoCliente"/>
		
		<adsm:complement 
			label="cliente"
			width="80%"
			labelWidth="20%"
			>
			<adsm:textbox 
				dataType="text"
				property="agrupamentoCliente.divisaoCliente.cliente.pessoa.nrIdentificacao"
				size="20" 
				maxLength="20"
				disabled="true"
				serializable="false"
			/>
			<adsm:textbox 
				dataType="text" 
				maxLength="50" 
				property="agrupamentoCliente.divisaoCliente.cliente.pessoa.nmPessoa"
				size="30" 
				disabled="true"
				serializable="false"
			/>
		</adsm:complement>

		<adsm:textbox 
			label="divisao"
			dataType="text" 
			maxLength="50" 
			property="agrupamentoCliente.divisaoCliente.dsDivisaoCliente"
			size="30" 
			width="30%"
			labelWidth="20%"
			disabled="true"
			serializable="false"
		/>

		<adsm:textbox 
			label="formaAgrupamento"
			dataType="text" 
			maxLength="50" 
			labelWidth="20%" 
			width="30%"
			size="30" 
			property="agrupamentoCliente.formaAgrupamento.dsFormaAgrupamento"
			disabled="true"
			serializable="false"
		/>
		<adsm:textbox 
			label="codigo"
			maxLength="10" 
			labelWidth="20%" 
			width="30%"		
			dataType="text" 
			size="10" 
			property="cdTipoAgrupamento"
		/>
		<adsm:textbox 
			label="descricao"
			maxLength="60" 
			labelWidth="20%" 
			width="30%"		
			dataType="text" 
			size="30" 
			property="dsTipoAgrupamento"
		/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoAgrupamento"/>
			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid selectionMode="check" idProperty="idTipoAgrupamento" onSelectAll="permissaoExcluir" onSelectRow="permissaoExcluir"   property="tipoAgrupamento" gridHeight="200" unique="true" rows="12" defaultOrder="cdTipoAgrupamento">
		<adsm:gridColumn title="codigo" property="cdTipoAgrupamento" width="30%" />
		<adsm:gridColumn title="descricao" property="dsTipoAgrupamento" width="70%" />
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript">
	function myPageLoad_cb(){
	onPageLoad_cb();
	var idFilial = getElementValue("idFilial");
	var data = new Array();	
	setNestedBeanPropertyValue(data, "idFilial", idFilial);
	var sdd = createServiceDataObject("lms.vendas.manterTiposAgrupamentoAction.validatePermissao", "validarPermissoes", data);
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
	
	function permissaoExcluir(data){
		if (document.getElementById("permissao").value!="true") {
			setDisabled("removeButton", true);
			return false;
		}
	}
	

	// Seta a propriedade masterLink para true para que o campo não seja limpo com a funcionalidade "Limpar"
	document.getElementById("permissao").masterLink = "true";
</script>
