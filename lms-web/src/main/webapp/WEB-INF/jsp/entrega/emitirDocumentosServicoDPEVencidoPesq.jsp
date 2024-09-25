<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.emitirDocumentosServicoDPEVencidoAction" onPageLoadCallBack="pageLoad" >
	<adsm:form action="/entrega/emitirDocumentosServicoDPEVencido" height="140">
	
       	<adsm:hidden property="tpEmpresa" value="M" />
       	<adsm:hidden property="tpAcesso" value="A" />

		<adsm:i18nLabels>
                <adsm:include key="LMS-00013"/>
                <adsm:include key="filialOrigem"/>
                <adsm:include key="filialDestino"/>
    	</adsm:i18nLabels> 

		<adsm:lookup dataType="text"
					 property="filialOrigem"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
					 criteriaSerializable="true"
    				 service="lms.entrega.emitirDocumentosServicoDPEVencidoAction.findLookupFilial"
    				 label="filialOrigem" 
    				 size="3" 
    				 maxLength="3" 
    				 labelWidth="20%" 
    				 width="30%" 
    				 exactMatch="true" 
    				 action="/municipios/manterFiliais">
         	
			<adsm:propertyMapping criteriaProperty="tpEmpresa" modelProperty="empresa.tpEmpresa" />
         	<adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia" />         
         	<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" size="25" disabled="true" serializable="true" />
	    </adsm:lookup> 

	    <adsm:lookup dataType="text"  
	    			 property="filialDestino"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
					 criteriaSerializable="true"
    				 service="lms.entrega.emitirDocumentosServicoDPEVencidoAction.findLookupFilialRegional"
    				 label="filialDestino" 
    				 size="3" 
    				 maxLength="3" 
    				 labelWidth="15%"
    				 width="30%" 
    				 exactMatch="true"
    				 action="/municipios/manterFiliais">

			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" />
         	<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia" />
         	<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" size="25" disabled="true" serializable="true" />
	    </adsm:lookup>
		
        <adsm:range label="dpeVencimento" labelWidth="20%" width="30%" required="true">
			<adsm:textbox dataType="integer" property="diaVencimentoInicial" size="5" maxLength="3"/>
			<adsm:textbox dataType="integer" property="diaVencimentoFinal" size="5" maxLength="3"/>
		</adsm:range>
		
        <adsm:range label="periodoEmissao" labelWidth="15%" width="30%" maxInterval="15" required="true">
			<adsm:textbox dataType="JTDate" property="periodoEmissaoInicial"/>
			<adsm:textbox dataType="JTDate" property="periodoEmissaoFinal"/>
		</adsm:range>
		
		<adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio"  
    				   labelWidth="20%"
    				   width="30%"
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>
		
		<adsm:buttonBar>		
			<adsm:reportViewerButton service="lms.entrega.emitirDocumentosServicoDPEVencidoAction" />
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>	
</adsm:window>

<script>

	function pageLoad_cb(data) {
		onPageLoad_cb(data);
	}	
	
	function validateTab() {	
		if (validateTabScript(document.forms)) {
		
			if ( (getElementValue("filialOrigem.idFilial") == null || getElementValue("filialOrigem.idFilial") == "") && 
				 (getElementValue("filialDestino.idFilial") == null || getElementValue("filialDestino.idFilial") == "") ) {
		
				 alert(i18NLabel.getLabel("LMS-00013")
								+ i18NLabel.getLabel("filialOrigem")+ " ou "
								+ i18NLabel.getLabel("filialDestino")+ "." );
				setFocusOnFirstFocusableField();
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
	
</script>
