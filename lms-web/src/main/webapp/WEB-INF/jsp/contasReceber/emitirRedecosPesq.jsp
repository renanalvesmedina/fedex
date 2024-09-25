<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.emitirRedecosAction">

	<adsm:form action="/contasReceber/emitirRedecos">
		


		<adsm:lookup
			label="redeco" 
			popupLabel="pesquisarFilial"
			labelWidth="15%"
			width="45%"
			dataType="text"
			property="filial" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial"
			service="lms.contasreceber.manterChequesPreDatadosAction.findLookupFilial"
			action="/municipios/manterFiliais" 
			size="3" 
			maxLength="3" 
			picker="false"
			minLengthForAutoPopUpSearch="3"
			serializable="true">
			<adsm:propertyMapping formProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping formProperty="siglaFilial" modelProperty="sgFilial"/>
			
	 	    	<adsm:lookup property="redeco" 
	 	    				 popupLabel="pesquisarRedeco"
							 dataType="integer"
							 size="10"
							 maxLength="10"
							 mask="0000000000"
							 idProperty="idRedeco" 
							 criteriaProperty="nrRedeco" 
							 onPopupSetValue="setaFilial"
							 onDataLoadCallBack="retornoLookupRedeco"
							 action="/contasReceber/manterRedeco" 
							 service="lms.contasreceber.manterRedecoAction.findRedeco">
					<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
					<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
					<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" inlineQuery="true"/>
					 <adsm:propertyMapping formProperty="nrRedeco" modelProperty="nrRedeco"/>
					
				</adsm:lookup>
		</adsm:lookup>	

		<adsm:hidden property="filial.pessoa.nmFantasia" serializable="false"/>

		<adsm:hidden property="siglaFilial"/>			 		
		<adsm:hidden property="nrRedeco"/>	


		
		<adsm:combobox property="tpFormatoRelatorio" label="formatoRelatorio" domain="DM_FORMATO_RELATORIO" required="true" defaultValue="pdf"/>
		
			
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirRedecosAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>	

	</adsm:form>

</adsm:window>
<script>
	/* seta a filial de acordo com o redeco selecionado na poup-up */
	function setaFilial(data, error) {
		setElementValue("filial.sgFilial", data.sgFilial);
		setElementValue('filial.idFilial', data.idFilial);
		setElementValue("siglaFilial", data.sgFilial);
		setElementValue('filial.pessoa.nmFantasia',data.pessoa.nmFantasia);
	}


	/*
		Trata o retorno da lookup de redeco, seta a filial
	*/
	function retornoLookupRedeco_cb(data,error){
	
		if( error != undefined ){
			alert(error);
			setFocusOnFirstFocusableField(document);
			return false;
		}
		
		var retorno = redeco_nrRedeco_exactMatch_cb(data); 		
		
		if( retorno == true && data[0] != null ){		

			setElementValue('filial.idFilial',data[0].filial.idFilial);
			setElementValue('filial.sgFilial',data[0].filial.sgFilial);
			setElementValue('filial.pessoa.nmFantasia',data[0].filial.pessoa.nmFantasia);
		
		}
		
		return retorno;
	
	}		
		

</script>