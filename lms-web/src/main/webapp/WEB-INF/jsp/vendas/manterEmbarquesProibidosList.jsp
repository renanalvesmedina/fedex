<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.proibidoEmbarqueService" onPageLoadCallBack="myPageLoad">
	<adsm:form action="/vendas/manterEmbarquesProibidos" >
		<adsm:hidden property="idFilial" serializable="false"/>
		<adsm:hidden property="permissao" serializable="false"/>
		<adsm:hidden property="msgErroPermissao" serializable="false"/>
		<adsm:hidden property="cliente.idCliente"/>

		<adsm:complement
			label="cliente"
			labelWidth="15%"
			width="85%"
			separator="branco">
			<adsm:textbox
				property="cliente.pessoa.nrIdentificacao"
				dataType="text"
				size="20"
				maxLength="20"
				disabled="true"
				serializable="false">
				<adsm:textbox
					property="cliente.pessoa.nmPessoa"
					dataType="text"
					size="30"
					disabled="true"
					serializable="false" 
					maxLength="60"/>
			</adsm:textbox>
		</adsm:complement>

		<adsm:range label="dataBloqueio" labelWidth="15%" width="30%">
			<adsm:textbox dataType="JTDate" property="dtBloqueioInicial" />
			<adsm:textbox dataType="JTDate" property="dtBloqueioFinal" />
		</adsm:range>

        <adsm:hidden property="usuario.tpCategoriaUsuario" value="F"/>
        <adsm:lookup label="responsavel"
					 property="usuarioBloqueio" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text" labelWidth="10%"
					 size="16" width="40%"
					 maxLength="16"	
					 onchange="return usuarioBloqueioOnChange(this);"
					 service="lms.workflow.manterIntegrantesComiteAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="usuarioBloqueio.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="usuarioBloqueio.nmUsuario" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:combobox property="motivoProibidoEmbarque.idMotivoProibidoEmbarque" 
					   label="motivoBloqueio" 
					   optionLabelProperty="dsMotivoProibidoEmbarque" 
					   optionProperty="idMotivoProibidoEmbarque" 
					   service="lms.vendas.motivoProibidoEmbarqueService.find" 
					   labelWidth="15%" 
					   width="60%"
					   onDataLoadCallBack="myOnDataLoadCallBackMotivoProibido"/>

		<adsm:range label="dataDesbloqueio" labelWidth="15%" width="30%">
			<adsm:textbox dataType="JTDate" property="dtDesbloqueioInicial" />
			<adsm:textbox dataType="JTDate" property="dtDesbloqueioFinal" />
		</adsm:range>

        <adsm:lookup label="responsavel"
					 property="usuarioDesbloqueio" 
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 dataType="text" labelWidth="10%"
					 size="16" width="40%"
					 maxLength="16"	
					 onchange="return usuarioDesbloqueioOnChange(this);"	 					 
					 service="lms.workflow.manterIntegrantesComiteAction.findLookupUsuarioFuncionario"
					 action="/seguranca/consultarUsuarioLMS">
			<adsm:propertyMapping relatedProperty="usuarioDesbloqueio.nmUsuario" modelProperty="nmUsuario" />
			<adsm:propertyMapping criteriaProperty="usuario.tpCategoriaUsuario" modelProperty="tpCategoriaUsuario"/>
			<adsm:textbox dataType="text" property="usuarioDesbloqueio.nmUsuario" disabled="true" serializable="false"/>
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="embarques"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="embarques" idProperty="idProibidoEmbarque" gridHeight="200" scrollBars="horizontal" rows="10"
		defaultOrder="dtBloqueio" onSelectAll="myOnSelectAll" onSelectRow="myOnSelectRow"
		service="lms.vendas.manterEmbarquesProibidosAction.findPaginated"
		rowCountService="lms.vendas.manterEmbarquesProibidosAction.getRowCount">
		<adsm:gridColumn title="dataBloqueio" property="dtBloqueio" width="120" align="center" dataType="JTDate"/>
		<adsm:gridColumn title="motivoBloqueio" property="motivoProibidoEmbarque.dsMotivoProibidoEmbarque" width="250" />
		<adsm:gridColumn title="respBloqueio" property="usuarioByIdUsuarioBloqueio.nmUsuario" width="200" />
		<adsm:gridColumn title="dataDesbloqueio" property="dtDesbloqueio" width="140" align="center" dataType="JTDate"/>
		<adsm:gridColumn title="respDesbloqueio" property="usuarioByIdUsuarioDesbloqueio.nmUsuario" width="200" />
		<adsm:buttonBar>
			<adsm:removeButton caption="excluir" id="removeButton" onclick="return myRemoveButtonOnClick();" disabled="true"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	document.getElementById("permissao").masterLink = "true";
	document.getElementById("msgErroPermissao").masterLink = "true";

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function myPageLoad_cb(data, error) {
		onPageLoad_cb();

		var idFilial = getElementValue("idFilial");
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdo = createServiceDataObject("lms.vendas.proibidoEmbarqueService.validatePermissaoUsuarioAcessoFilial", "validarPermissoes", data);
		xmit({serviceDataObjects:[sdo]});
	}

	/*
	 Criada para validar acesso do usuário 
	 logado à filial do cliente
	*/
	function validarPermissoes_cb(data, error){
		var valido = (error == undefined);
		
		setElementValue("permissao", valido);
		if (error != undefined) setElementValue("msgErroPermissao", error);
	}

	/**
	* Esta função é utilizada para verificar a permissão de acesso pelo usuário
	* em relação a filial responsável operacional pelo cliente, desabilitando
	* o botão de excluir da listagem caso não tenha permissão.
	*/
	function myOnSelectRow(){
		var permissao = getElementValue("permissao");
		if(permissao != "true" ){
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
	
	/**
	* Método utilizado para setar a informação de motivo Financeiro a descrição do motivo	
	* a ser mostrado no combo de motivos de bloqueio
	*/
	function myOnDataLoadCallBackMotivoProibido_cb(data){
		var blFinanceiro, x;
		for( x = 0; x < data.length; x++ ){
			blFinanceiro = getNestedBeanPropertyValue(data[x],"blFinanceiro");
			if( blFinanceiro == "true" ){
				data[x].dsMotivoProibidoEmbarque += " - Financeiro";
			}
		}
		motivoProibidoEmbarque_idMotivoProibidoEmbarque_cb(data);
	}
	
	/**
	* Este método destina-se a verificar se o usuário logado tem permissão
	* de acesso à filial responsável comercialmente pelo cliente antes
	* de excluir os registros selecionados.
	*/
	function myRemoveButtonOnClick(){
		var idFilial = getElementValue("idFilial");
		var data = new Array();
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		var sdo = createServiceDataObject("lms.vendas.proibidoEmbarqueService.validatePermissaoUsuarioAcessoFilial", 
										  "removeItens", 
										  data);
		xmit({serviceDataObjects:[sdo]});			
		return true;
	}
	
	/**
	* Método responsável pela exclusão dos registros selecionados na grid após verificações
	* de regras de negócio. 
	* Caso os testes das regras de negócio retornem OK, exclui os itens selecionados, caso contrário
	* informa o erro ocorrido.
	*/
	function removeItens_cb(data,error){
		var valido = (error == undefined);
		
		if( valido ){		
			embarquesGridDef.removeService = "lms.vendas.proibidoEmbarqueService.removeByIds";
			embarquesGridDef.removeByIds();
		} else {
			alert(error);
			return false;
		}	
	}
	
	function usuarioBloqueioOnChange(eThis){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value == "false" && eThis.value != ""){
			alert(''+getTabGroup(document).getTab("pesq").getElementById("msgErroPermissao").value);
			resetValue(document.getElementById("usuarioBloqueio.nrMatricula"));
			return false;
		}
		return usuarioBloqueio_nrMatriculaOnChangeHandler();
	}

	function usuarioDesbloqueioOnChange(eThis){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value == "false" && eThis.value != ""){
			alert(''+getTabGroup(document).getTab("pesq").getElementById("msgErroPermissao").value);
			resetValue(document.getElementById("usuarioDesbloqueio.idUsuario"));
			return false;
		}
		return usuarioDesbloqueio_nrMatriculaOnChangeHandler();
	}
</script>