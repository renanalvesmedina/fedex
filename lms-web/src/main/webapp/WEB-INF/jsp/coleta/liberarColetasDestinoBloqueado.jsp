<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript">
	function carregaPagina() {
		onPageLoad();
		
		// Busca os Dados referente ao usuário da sessão.	 
	    var sdo = createServiceDataObject("lms.coleta.liberarColetasDestinoBloqueadoAction.getDadosSessao", "buscarDadosSessao");
		xmit({serviceDataObjects:[sdo]});
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

<adsm:window title="liberarColetasDestinoBloqueado" service="lms.coleta.liberarColetasDestinoBloqueadoAction"
			 onPageLoad="carregaPagina">

	<adsm:form id="formLiberar" action="/coleta/liberarColetasDestinoBloqueado">
		
		<adsm:section caption="liberarColetasDestinoBloqueado"/> 
		
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
 				<adsm:propertyMapping modelProperty="nmUsuario"
 									  relatedProperty="usuario.nmUsuario"/>
													  
			<adsm:textbox property="usuario.nmUsuario" dataType="text" 
						  size="60" maxLength="60" disabled="true"/>
		</adsm:lookup>		
		
		<adsm:combobox label="motivoLiberacao" property="ocorrenciaColeta"
			   		   optionProperty="idOcorrenciaColeta" optionLabelProperty="dsDescricaoCompleta"
			   		   service="lms.coleta.liberarColetasDestinoBloqueadoAction.findOcorrenciaColeta" 
			   		   required="true" width="85%" />
		
		<adsm:textarea property="dsDescricao" label="descricao" maxLength="100" columns="100" rows="2" width="85%"/> 
		
		<adsm:buttonBar>
			<adsm:button caption="liberar" id="liberar" disabled="false" onclick="salvaEventoColeta()"/>
			<adsm:button caption="cancelar" id="cancelar" disabled="false" onclick="window.close()"/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>

<script type="text/javascript">	
	document.getElementById("usuario.nrMatricula").style.textAlign = "right";

	function initWindow(eventObj) {		
		setDisabled("liberar", false);	
		setDisabled("cancelar", false);	
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
			var mapDestinoBloqueado = new Array();    
    		setNestedBeanPropertyValue(mapDestinoBloqueado, "idOcorrenciaColeta", comboBox.value);
    		setNestedBeanPropertyValue(mapDestinoBloqueado, "idUsuario", getElementValue("usuario.idUsuario"));
	    	setNestedBeanPropertyValue(mapDestinoBloqueado, "dsDescricao", getElementValue("dsDescricao"));
						
			// Seta parâmetros na tela pai.
			windowPai.setaDadosPopupLiberarColetaDestinoBloqueado(mapDestinoBloqueado);
			window.close();
		}		
	}	
</script>
