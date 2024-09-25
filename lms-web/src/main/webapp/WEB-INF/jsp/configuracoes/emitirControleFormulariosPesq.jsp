<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.emitirControleFormulariosAction" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form action="/configuracoes/emitirControleFormularios">
     	<adsm:lookup label="filial" property="filial" dataType="text"  service="lms.municipios.filialService.findLookup" exactMatch="false"
			idProperty="idFilial" criteriaProperty="sgFilial" size="3" maxLength="3" width="35%" action="/municipios/manterFiliais">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="25" disabled="true" serializable="false"/>			
		</adsm:lookup>
		
		<adsm:combobox property="tpFormulario" label="tipoDocumento" domain="DM_TIPO_FORMULARIO"/>
		<adsm:combobox property="tpSituacaoFormulario" label="situacao" domain="DM_SITUACAO_FORMULARIO"/>
		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio" 
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>
		
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.configuracoes.emitirControleFormulariosAction" disabled="false"/>
			<adsm:button caption="limpar"  id="btnLimpar"  onclick="limparCampos()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	/**
	*	Executa a busca da filial do usuário para setá-la como padrão na lookup de filial
	*
	*/
	function myOnPageLoadCallBack_cb(data, erro){
	
		onPageLoad_cb(data, erro);
		
		var dados = new Array();
         
        var sdo = createServiceDataObject("lms.configuracoes.emitirControleFormulariosAction.findFilialUsuario",
                                          "retornoBuscaFilialUsuario",
                                          dados);
        xmit({serviceDataObjects:[sdo]});		
		
	}
	
	/**
	*	Seta a filial do usuário como padrão.
	*/
	function retornoBuscaFilialUsuario_cb(data,erro){
		
		if( erro != undefined ){
			alert(erro);
			setFocus(document.getElementById('filial.sgFilial'));
			return false;		
		}
		
		setElementValue('filial.idFilial',data.filial.idFilial);
		setElementValue('filial.sgFilial',data.filial.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.filial.pessoa.nmFantasia);
		setFocusOnFirstFocusableField(document);		
		
	}
	
	/* executado ao entrar na tela */
	function initWindow(){
		setDisabled('btnLimpar',false);
	}
	
	//depois de limpar refaz a consulta para setar a filial do usuario
	function limparCampos(){
		cleanButtonScript();

		var dados = new Array();

       	var sdo = createServiceDataObject("lms.configuracoes.emitirControleFormulariosAction.findFilialUsuario",
                                          "retornoBuscaFilialUsuario",
                                          dados);
        xmit({serviceDataObjects:[sdo]});	
		
	}
	
</script>