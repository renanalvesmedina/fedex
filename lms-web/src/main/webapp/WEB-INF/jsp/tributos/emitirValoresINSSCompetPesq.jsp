<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.tributos.emitirValoresINSSCompetAction">
	<adsm:form action="/tributos/emitirValoresINSSCompet">


		<adsm:i18nLabels>
			<adsm:include key="LMS-23004"/>
		</adsm:i18nLabels>

		<adsm:range label="competencia" width="45%" labelWidth="10%">
			<adsm:textbox dataType="monthYear" property="competenciaInicial" required="true"/>
	    	<adsm:textbox dataType="monthYear" property="competenciaFinal" required="true"/>
		</adsm:range>
		
		<adsm:hidden property="siglaFilial" serializable="true"/>
		
		<adsm:lookup dataType="text" 
					 property="filial" 
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.tributos.emitirValoresINSSCompetAction.findLookupFilial" 
					 action="/municipios/manterFiliais" 
					 label="filial" 
					 size="3" 
					 maxLength="3" 
					 width="32%"
					 labelWidth="13%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="siglaFilial"/>			
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="31" maxLength="30" disabled="true"/>
		</adsm:lookup>
		

		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		<adsm:hidden property="cpfProprietario" serializable="true"/>
		
		<adsm:lookup service="lms.tributos.emitirValoresINSSCompetAction.findLookupProprietario" 
					 property="proprietario"
					 label="proprietario"
					 idProperty="idProprietario"
					 criteriaProperty="pessoa.nrIdentificacao"					 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"					 
					 dataType="text"
					 maxLength="20"
					 action="/contratacaoVeiculos/manterProprietarios"  
					 exactMatch="true"
					 minLengthForAutoPopUpSearch="5"
					 afterPopupSetValue="verificaPessoaPopup"
					 onDataLoadCallBack="verificaPessoa"
					 size="20"
					 width="45%"
					 labelWidth="10%">                    

			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" relatedProperty="cpfProprietario"/>							 
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="proprietario.pessoa.nmPessoa"/>
 		    <adsm:textbox  size="31"  dataType="text" property="proprietario.pessoa.nmPessoa" disabled="true"/>   
		</adsm:lookup>
		
		<adsm:checkbox property="blExibirProprietarios" label="exibirProprietarios" width="32%" labelWidth="13%" onclick="mudaBotaoVisualizar()"/>

		<adsm:combobox property="tpFormatoRelatorio" 
			label="formatoRelatorio" 
			domain="DM_FORMATO_RELATORIO"
			defaultValue="pdf"
			labelWidth="10%" 
			width="80%"
			required="true"/>		
		
		<adsm:buttonBar>
			<adsm:reportViewerButton id="btnVisualizar" service="lms.tributos.emitirValoresINSSCompetAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>
<script>
	
	/**
	* De acordo com a escolha do usuário o caption(value) do botão btnVisualizar muda para 'Visualizar' ou 'Visualizar po CPF'
	*/
	function mudaBotaoVisualizar(){
		var element = document.getElementById("btnVisualizar");
		if( element.value == parent.i18NLabel.getLabel("visualizar") ){
			setElementValue("btnVisualizar", parent.i18NLabel.getLabel("visualizarPorCPF"));
		} else {
			setElementValue("btnVisualizar", parent.i18NLabel.getLabel("visualizar"));
		}
		
	}
	
		function verificaPessoaPopup(data){

		if( data != undefined ){
			if( data.pessoa.tpIdentificacao.value != 'CPF' ){
				alert(i18NLabel.getLabel('LMS-23004'));
				resetValue('proprietario.idproprietario');
				setFocus('proprietario.pessoa.nrIdentificacao');
				return false;
			}
		}
		
	}
	
	function verificaPessoa_cb(data,error){
		
		var retorno = proprietario_pessoa_nrIdentificacao_exactMatch_cb(data);
		
		if( retorno == true ){
			verificaPessoaPopup(data[0]);
		}		
		
		return retorno;
		
	}
	
	
</script>
