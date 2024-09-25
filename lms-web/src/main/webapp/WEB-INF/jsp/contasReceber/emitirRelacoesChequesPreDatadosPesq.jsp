<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myOnPageLoad">

	<adsm:form action="/contasReceber/emitirRelacoesChequesPreDatados">

		<adsm:textbox dataType="integer" property="nrLote" label="lote" size="10"  maxLength="10" width="30%"/>


		<adsm:hidden property="sgFilial"/>
		
		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filial" 
					 size="3" 
					 maxLength="3" 
					 width="30%"
					 labelWidth="17%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" formProperty="sgFilial"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="20" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>
        
		 <adsm:lookup property="usuario" 
        			  idProperty="idUsuario" 
        			  criteriaProperty="nrMatricula" 
        			  serializable="true"
                      dataType="text" 
					  maxLength="16"
					  label="usuario" 
				      size="16" 
                      minLengthForAutoPopUpSearch="3"
					  criteriaSerializable="true" 
				 	  width="77%" 
                      service="lms.municipios.manterRegionaisAction.findLookupUsuarioFuncionario" 
                      action="/configuracoes/consultarFuncionariosView">
                <adsm:propertyMapping relatedProperty="nmUsuario" modelProperty="nmUsuario"/>
                <adsm:textbox dataType="text" property="nmUsuario" size="45" maxLength="60" disabled="true" serializable="true"/>
		</adsm:lookup>        
        
        <adsm:combobox label="situacaoCheque" property="tpSituacaoCheque" domain="DM_STATUS_CHEQUE" width="30%" />

		<adsm:combobox label="incluirChequeDevolvido" required="true" property="incluirChequeDevolvido" domain="DM_SIM_NAO" defaultValue="N" labelWidth="17%" width="30%"/>
	
        <adsm:combobox property="tipoQuebra" label="tipoQuebra" domain="DM_TIPO_QUEBRA_CHEQUE" defaultValue="V" width="30%" required="true"/>

        <adsm:combobox label="listarChequesSucursais" required="true" property="listarChequesSucursais" domain="DM_SIM_NAO" defaultValue="N" labelWidth="17%" width="30%"/>

		<adsm:range label="dataEmissao"  width="30%">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" picker="true"/>		
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" picker="true"/>		
		</adsm:range>
		
		<adsm:range label="dataVencimento" labelWidth="17%" width="30%">
			<adsm:textbox dataType="JTDate" property="dtVencimentoInicial" picker="true"/>		
			<adsm:textbox dataType="JTDate" property="dtVencimentoFinal" picker="true"/>		
		</adsm:range>
		
		<adsm:range label="dataAcao"  width="30%">
			<adsm:textbox dataType="JTDate" property="dtAcaoInicial" picker="true"/>
			<adsm:textbox dataType="JTDate" property="dtAcaoFinal" picker="true"/>
		</adsm:range>

        <adsm:combobox label="ultimaAcao" property="ultimaAcao" domain="DM_HISTORICO_CHEQUE" width="77%"  />

        <adsm:combobox  width="30%" boxWidth="85" label="moeda" property="moedaPais.idMoedaPais" required="true"
        	optionLabelProperty="dsSimbolo"
        	optionProperty="idMoedaPais"
        	autoLoad="true"
        	service="lms.contasreceber.manterChequesPreDatadosAction.findComboMoeda"
        >
        	<adsm:propertyMapping modelProperty="dsSimbolo" relatedProperty="siglaSimbolo"/>
        </adsm:combobox>
        
        <adsm:hidden serializable="true" property="siglaSimbolo"/>

		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio" 
    				   labelWidth="17%" 
    				   width="30%"
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirRelacoesChequesPreDatadosAction" disabled="false"/>
			<adsm:resetButton/>	
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>

<script language="javascript">
	setMasterLink(this.document, true);
	
	function initWindow(eventObj){
	
		if(eventObj.name == 'cleanButton_click') {
			buscaMoeda();
		}
	        
	}
	
	function myOnPageLoad_cb(data, error){
		onPageLoad_cb(data, error);
		buscaMoeda();
	}
	
	function buscaMoeda(){
	
		_serviceDataObjects = new Array();
		
		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterChequesPreDatadosAction.getMoedaUsuarioLogado",
			"setMoedaUsuario", 
			new Array()));
	
        xmit(false);

		_serviceDataObjects = new Array();

	}

	function setMoedaUsuario_cb(data, error) {
	
		if( error != undefined ){
			alert(error);
			setFocusOnFirstFocusableField(document);
			return false;
		}

		if (data != null) {
			var moeda = data._value;
			var e = getElement("moedaPais.idMoedaPais");
			
			for (var i = 0; i < e.options.length; i++) {
				if (e.options[i].value == moeda) {
					e.options[i].selected = true;
					setElementValue("siglaSimbolo", e.options[i].text);
					break;
				}
			}
			
		}
	}

	
</script>