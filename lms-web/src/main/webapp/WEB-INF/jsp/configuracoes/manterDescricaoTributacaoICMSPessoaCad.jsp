<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.manterObservacaoICMSPessoaAction" onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/configuracoes/manterDescricaoTributacaoICMSPessoa" idProperty="idObservacaoICMSPessoa" onDataLoadCallBack="myOnDataLoad">
	
		<adsm:hidden property="inscricaoEstadual.idInscricaoEstadual"/>
		<adsm:hidden property="labelPessoaTemp" serializable="false"/>
		<adsm:label key="branco" style="width:0"/>
		<td colspan="15" id="labelPessoa" class="FmLbRequerido"></td>

	    <adsm:textbox dataType="text" property="inscricaoEstadual.pessoa.nrIdentificacao"  size="20" maxLength="20" disabled="true" width="85%" serializable="false" >
	        <adsm:textbox dataType="text" property="inscricaoEstadual.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
	    </adsm:textbox>                    
	    
		<adsm:textbox label="ie" property="inscricaoEstadual.nrInscricaoEstadual" dataType="text" size="40" width="35%" maxLength="20" disabled="true" serializable="false"/>
		
		<adsm:combobox label="tipo" property="tpObservacaoICMSPessoa" domain="DM_TIPO_OBSERVACAO_ICMS_CLIENTE" width="35%" boxWidth="100" required="true"/>
		
		<adsm:range label="vigencia" width="85%">
		   <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" size="10" required="true" />
		   <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" size="10" />
	    </adsm:range>
	    
		<adsm:textarea label="observacao" maxLength="85" property="obObservacaoICMSPessoa" columns="80" rows="3" width="85%" required="true"/>
		
		<adsm:textbox label="ordem" maxLength="3" size="5" property="nrOrdemImpressao" dataType="integer" width="85%" required="true"/>
		
		<adsm:textbox dataType="text" label="cdEmbLegalMasterSaf" disabled="true"
			property="cdEmbLegalMastersaf" size="20" maxLength="10" width="80%" />				

		<adsm:buttonBar>
		   <adsm:button  caption="salvar" onclick="salvarTributacaoICMSPessoa();" disabled="false" id="btnSalvar"/>
		   <adsm:button caption="limpar" onclick="limpar()" disabled="false" id="btnLimpar"/>		
		   <adsm:removeButton id="btnExcluir"/>
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>
<script>

	/*Carrega a página*/
function myOnPageLoad_cb(data, erro){
	onPageLoad_cb();
	document.getElementById('labelPessoa').innerHTML=getElementValue("labelPessoaTemp")+":";
}

	/*Funcao de callback de carregamento da pagina*/
	function myOnDataLoad_cb(data, erro){
		onDataLoad_cb(data,erro);
		validaVigencias(data);
	}

	/*Valida as vigencias*/
	function validaVigencias(data){		
	   	var sdo = createServiceDataObject("lms.configuracoes.manterObservacaoICMSPessoaAction.validaVigencias", 
	   		   	"validaVigencias", {dtVigenciaInicial:data.dtVigenciaInicial, dtVigenciaFinal:data.dtVigenciaFinal});

		xmit({serviceDataObjects:[sdo]});		
	}

	/*Callback da funcao validaVigencias*/
	function validaVigencias_cb(datac,error){		
		if(error != undefined){
			alert(error);
			return;
		}	 	
		if(datac.comparaFinal != undefined && datac.comparaFinal < 0){
			desabilitaTodosCampos();
			setDisabled("dtVigenciaFinal",true);
			setDisabled("btnExcluir",true);
			setDisabled("btnSalvar",true);
		}else{
			if(datac.comparaInicial <= 0 ){
				desabilitaTodosCampos(true);
				setDisabled("dtVigenciaFinal",false);	
				setDisabled("btnExcluir",true);		
			}else{
				desabilitaTodosCampos(false);				
			}
		}		
	}

	/*funcao utilizada pelo botao Salvar*/
	function salvarTributacaoICMSPessoa(){
		storeButtonScript('lms.configuracoes.manterObservacaoICMSPessoaAction.store', 'salvarTributacaoICMSPessoa', document.forms[0]);	
	}

	/*funcao callback salvarTributacaoICMSPessoa*/
	function salvarTributacaoICMSPessoa_cb(data, error){
		onDataLoad_cb(data,error);		
		store_cb(data,error)
	}	

	/**
		Chamado ao iniciar a tela
	*/
	function initWindow(eventObj){
		setDisabled('btnLimpar', false);
		
		
		if (eventObj.name == "tab_click" || eventObj.name == "removeButton" ){
			limpar();
			setFocusOnFirstFocusableField(document);
		}
	
		//desabilita os campos da tela quando vier da grid ou depois de salvar	
		if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton"  ){
			desabilitaTodosCampos();
			if( eventObj.name == "gridRow_click" ){
				setFocusOnFirstFocusableField(document);
			} else {
				setFocus('btnLimpar',true,true);
			}
		}
		
	}	


	/**
 	* Function que limpa os campos da tela e desabilita todos os campos.
 	*
 	* chamado por: botão limpar
 	*/
	function limpar(){
		newButtonScript();
		setDisabled('btnSalvar', false);
		desabilitaTodosCampos(false);
	}


	/**
 	* Function que desabilita todos os campos da tela e seta os valores default
 	*
 	* chamado por: limpar, initWindow
 	*/	
	function desabilitaTodosCampos(val){
		if (val == undefined){ 
			val = true;	
		}
		setDisabled("tpObservacaoICMSPessoa",val);
		setDisabled("dtVigenciaInicial",val);
		setDisabled("obObservacaoICMSPessoa",val);	
		setDisabled("nrOrdemImpressao",val);
		setFocusOnFirstFocusableField();				
	}

</script>
