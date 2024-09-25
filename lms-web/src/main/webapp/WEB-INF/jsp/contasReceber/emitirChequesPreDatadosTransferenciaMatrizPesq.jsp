<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.emitirChequesPreDatadosTransferenciaMatrizAction" > 
	<adsm:form action="/contasReceber/emitirChequesPreDatadosTransferenciaMatriz">

	<adsm:hidden property="filial1.pessoa.nmFantasia" />

		<adsm:lookup
			label="redeco" 
			labelWidth="15%"
			width="30%"
			dataType="text"
			property="filial" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial"
			service="lms.contasreceber.emitirChequesPreDatadosTransferenciaMatrizAction.findFilial" 
			action="/municipios/manterFiliais" 
			onPopupSetValue="setaFilial"
			popupLabel="pesquisarFilial"
			onDataLoadCallBack="setaFilialcb"
			size="3" 
			maxLength="3" 
			picker="false"
			serializable="false">
			
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial1.pessoa.nmFantasia"/>
 	    	<adsm:lookup property="redeco" 
						 dataType="integer"
						 size="10"
						 maxLength="10"
						 popupLabel="pesquisarRedeco"
						 idProperty="idRedeco" 
						 criteriaProperty="nrRedeco" 
						 onPopupSetValue="setRedeco"
						 mask="0000000000" 
						 action="/contasReceber/manterRedeco" 
						 service="lms.contasreceber.emitirChequesPreDatadosTransferenciaMatrizAction.findRedeco">
				<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
				<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="filial1.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" inlineQuery="false"/>
			</adsm:lookup>
		</adsm:lookup>	
			
		<!--  adsm:hidden property="nrLoteFinal" / -->
		
	    <adsm:lookup label="lote" dataType="integer" 
        	property="loteCheque" idProperty="idLoteCheque" criteriaProperty="nrLoteCheque" 
			size="15" maxLength="15"  disabled="false"
			labelWidth="15%" width="30%"
			service="lms.contasreceber.emitirChequesPreDatadosTransferenciaMatrizAction.findLookupLoteCheque" 
			action="/contasReceber/manterChequesPreDatados">

		<adsm:propertyMapping criteriaProperty="loteCheque.nrLoteCheque" modelProperty="nrLoteInicial" inlineQuery="false" disable="false"/>
		<adsm:propertyMapping criteriaProperty="loteCheque.nrLoteCheque" modelProperty="nrLoteFinal" inlineQuery="false" disable="false"/>
		
		<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
		<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false"/>

		<adsm:propertyMapping criteriaProperty="redeco.idRedeco" modelProperty="redeco.idRedeco" />
		<adsm:propertyMapping criteriaProperty="redeco.nrRedeco" modelProperty="redeco.nrRedeco" inlineQuery="false"/>
		
	</adsm:lookup>	
	        
	    <adsm:combobox property="tpFormatoRelatorio" 
					   label="formatoRelatorio" 
					   required="true"
					   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirChequesPreDatadosTransferenciaMatrizAction" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
        
	</adsm:form>

</adsm:window>

<script>

	/**
	 * Function que seta os valores na lookup de filial
	 *
	 * chamado por: lookup filial, setRedeco
	 */
	function setaFilial(data) {

		setElementValue("filial.sgFilial", data.sgFilial);
		setElementValue("filial.idFilial", data.idFilial);
		if( data.pessoa.nmFantasia != undefined && data.pessoa.nmFantasia != '' ){
			setElementValue("filial1.pessoa.nmFantasia", data.pessoa.nmFantasia);
		}
		return false;
		
	}

	/**
	 * Function que trata o retorno da lookup de filial
	 *
	 * chamado por: lookup filial
	 */
	function setaFilialcb_cb(data, errorMessage, errorCode, eventObj){

		var ret = filial_sgFilial_likeEndMatch_cb(data);

		if (ret){
			if ( data != undefined  && data.length > 0 ){
				setElementValue("filial.sgFilial", data[0].filial.sgFilial);
				setElementValue("filial.idFilial", data[0].filial.idFilial);
				setElementValue("filial1.pessoa.nmFantasia", data[0].pessoa.nmFantasia);
			}
		}

		return ret;
	}

	
	/**
	 * Function que seta os valores na lookup de redeco e filial
	 *
	 * chamado por: lookup redeco, setRedeco
	 */	
	function setRedeco(data,error){
		
		setElementValue("redeco.nrRedeco", data.nrRedeco);
		format("redeco.nrRedeco");
		setElementValue("redeco.idRedeco", data.idRedeco);
		setaFilial(data,error);
		return false;
	}
</script>