<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript">
	function carregaPagina() {
		onPageLoad();
		
		// Rotina que pega a referência da tela pai para usar parametros ou chamar funções
		// que serão usadas na tela filho.
		var doc;
		if (window.dialogArguments != undefined && window.dialogArguments.window != undefined) {
			doc = window.dialogArguments.window.document;
		} else {
		   doc = document;
		}
		
		// Busca os Dados referente ao usuário da sessão.	 
	    var sdo = createServiceDataObject("lms.coleta.liberarBloqueioCreditoColetaAction.getDadosSessao", "buscarDadosSessao");
		xmit({serviceDataObjects:[sdo]});
		
		//Busca o idCliente pela url{get} desta tela
		var url = new URL(window.location.href);
		var idCliente;
		
		if (url.parameters["idCliente"] != undefined){
		    //Se o parâmetro foi informado   
		    idCliente = url.parameters["idCliente"];  
		}else{
		    // Senão pega parâmetros da tela pai.
            var tabGroup = getTabGroup(doc);
            var tabDet = tabGroup.getTab("pedidoColeta");
            idCliente = tabDet.getFormProperty("idClientePessoa");
		}  
		
		setElementValue("idCliente", idCliente);
		
		carregaGrid();
	}

	/**
	 * Retorno da pesquisa de dados do usuário da sessão em getDadosSessao().
	 */
	function buscarDadosSessao_cb(data, error) {
		setElementValue("filialSessao.idFilial", data.idFilialSessao);
		setElementValue("filialSessao.sgFilial", data.sgFilialSessao);
		setElementValue("filialSessao.pessoa.nmFantasia", data.nmFilialSessao);
	}
</script>
<adsm:window title="liberarBloqueioCreditoColeta" service="lms.coleta.liberarBloqueioCreditoColetaAction" 
			 onPageLoad="carregaPagina">
	<adsm:form id="formLiberar" action="/coleta/liberarBloqueioCreditoColeta" >
	
		<adsm:section caption="liberarBloqueioCreditoColeta"/>
	
		<adsm:hidden property="idCliente" />
		<adsm:hidden property="filialSessao.idFilial" />
		<adsm:hidden property="filialSessao.sgFilial" />
		<adsm:hidden property="filialSessao.pessoa.nmFantasia" />
		
		<adsm:lookup property="usuario"
					 idProperty="idUsuario"
					 criteriaProperty="nrMatricula" 
					 action="/configuracoes/consultarFuncionariosView"
					 service="lms.coleta.liberarBloqueioCreditoColetaAction.findLookupUsuarioFuncionario" 
					 dataType="text" label="funcionario" size="16" 
					 maxLength="16" width="85%" exactMatch="true" required="true">		
				<adsm:propertyMapping criteriaProperty="filialSessao.idFilial" 
									  modelProperty="filial.idFilial" disable="true"/>
				<adsm:propertyMapping criteriaProperty="filialSessao.sgFilial" 
									  modelProperty="filial.sgFilial" disable="true"/>
				<adsm:propertyMapping criteriaProperty="filialSessao.pessoa.nmFantasia" 
									  modelProperty="filial.pessoa.nmFantasia" disable="true"/>					 
	 			<adsm:propertyMapping modelProperty="nmUsuario" relatedProperty="usuario.nmUsuario"/>					 	
													  
			<adsm:textbox property="usuario.nmUsuario" dataType="text" 
						  size="60" maxLength="60" disabled="true"/>
		</adsm:lookup>		
		
		<adsm:combobox label="motivoLiberacao" property="ocorrenciaColeta"
					   optionProperty="idOcorrenciaColeta" optionLabelProperty="dsDescricaoCompleta"
					   service="lms.coleta.liberarBloqueioCreditoColetaAction.findOcorrenciaColeta" 
					   required="true" width="85%" />
		
		<adsm:textarea property="dsDescricao" label="descricao" maxLength="100" columns="100" rows="2" width="85%"/> 

	</adsm:form>

	<adsm:grid property="proibidoEmbarque" idProperty="idProibidoEmbarque" onRowClick="rowClickNone" 
			   autoSearch="false" rows="5" selectionMode="none" defaultOrder="motivoProibidoEmbarque_.dsMotivoProibidoEmbarque:asc"
			   showPagging="true" showGotoBox="true" gridHeight="40" unique="true"  
			   service="lms.coleta.liberarBloqueioCreditoColetaAction.findPaginatedProibidoEmbarque"
			   rowCountService="lms.coleta.liberarBloqueioCreditoColetaAction.getRowCountProibidoEmbarque" >

		<adsm:gridColumn title="motivos" property="motivoProibidoEmbarque.dsMotivoProibidoEmbarque" width="50%"/>
		<adsm:gridColumn title="descricao" property="dsBloqueio" width="50%"/>

		<adsm:buttonBar>
			<adsm:button caption="liberar" disabled="false" onclick="salvaEventoColeta()"/>
			<adsm:button caption="cancelar" disabled="false" onclick="window.close()"/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script type="text/javascript">
	document.getElementById("usuario.nrMatricula").style.textAlign = "right";

	/**
	 * Carrega dados da Grid
	 */
	function carregaGrid() {
		var fb = buildFormBeanFromForm(this.document.forms[0], 'LIKE_END'); 
		proibidoEmbarque_cb(fb);
	}
	
	/**
	 * Envia os dados para a tela de cadastro de Pedido de Coleta para
	 * gravar o Evento de Coleta.
	 */	
	function salvaEventoColeta() {
		var comboBox = document.getElementById("ocorrenciaColeta");		
		var windowPai;
		var doc;		
		if (window.dialogArguments != undefined && window.dialogArguments.window != undefined) {			
			windowPai = window.dialogArguments.window;
		} else {
		   doc = document;
		}			
		
		if(validateForm(document.getElementById('formLiberar'))) {
			var mapBloqueioCredito = new Array();  
			setNestedBeanPropertyValue(mapBloqueioCredito, "idOcorrenciaColeta", comboBox.value);
		    setNestedBeanPropertyValue(mapBloqueioCredito, "idUsuario", getElementValue("usuario.idUsuario"));
	    	setNestedBeanPropertyValue(mapBloqueioCredito, "dsDescricao", getElementValue("dsDescricao"));
							
			// Seta parâmetros na tela pai.
			windowPai.setaDadosPopupLiberarBloqueioCreditoColeta(mapBloqueioCredito);		
			window.close();
		}
	}
	
	function rowClickNone() {
		return false;
	}	
	
</script>