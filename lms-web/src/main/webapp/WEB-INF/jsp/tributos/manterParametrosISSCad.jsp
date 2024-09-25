<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.manterParametrosISSAction">
	<adsm:form action="/tributos/manterParametrosISS" idProperty="idParametroIssMunicipio">
	
  	    <adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		<adsm:lookup property="municipio" 
		             criteriaProperty="nmMunicipio" 
		             idProperty="idMunicipio" 
		             service="lms.municipios.municipioService.findMunicipioLookup" 
		             required="true" 
		             dataType="text" 
		             disabled="false" 
		             label="municipio" 
		             size="30" 
		             maxLength="60" 
		             action="/municipios/manterMunicipios" labelWidth="20%" 
		             width="32%" 
		             exactMatch="false"
		             minLengthForAutoPopUpSearch="3">
		             
		       <adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
		 </adsm:lookup>            
		
		<adsm:textbox label="diaRecolhimento" 
		              dataType="integer" 
		              property="dtDiaRecolhimento" 
		              size="10" 
		              maxLength="2" 
		              required="true" 
		              labelWidth="18%" 
		              onchange="return validateDtDiaRecolhimento()"
		              width="30%"/>

		<adsm:combobox label="formaPagamento" 
		               property="tpFormaPagamento" 
			           labelWidth="20%" 
			           width="32%"
			           required="true"
			           onlyActiveValues="true"
			           domain="DM_FORMA_PGTO_ISS" />
			           
		<adsm:combobox label="dispositivoLegal" 
		               property="tpDispositivoLegal" 
			           labelWidth="18%" 
			           domain="DM_TIPO_DISPOSITIVO_LEGAL"
			           onlyActiveValues="true"
			           width="30%"/>

		<adsm:textbox label="numeroDispositivo"  
		              dataType="text" 
		              property="nrDispositivoLegal" 
		              size="20" 
		              maxLength="20" 
		              labelWidth="20%" 
		              width="32%"/>
		              
		<adsm:textbox label="anoDispositivo" 
		 			  dataType="JTDate" 
		 			  property="dtAnoDispositivoLegal" 
		 			  size="10"
		 			  picker="false" 
		 			  mask="yyyy"
		 			  labelWidth="18%" 
		 			  width="30%"/>

		<adsm:checkbox label="livroEletronico" 
		               property="blProcEletronicoLivro" 
		               labelWidth="20%" 
		               width="32%"/>
		               
		<adsm:checkbox label="emissaoComCTRC" 
		               property="blEmissaoComCtrc" 
		               labelWidth="18%" 
		               width="30%"/>

		<adsm:textbox label="site" 
		              dataType="text" 
		              property="dsSiteInternet" 
		              size="50" 
		              maxLength="60" 
		              labelWidth="20%" 
		              width="80%"/>

        <adsm:textbox label="cnpjPrefeitura" 
                      dataType="CNPJ" 
                      property="nrCnpj" 
                      size="20" 
                      labelWidth="20%" 
                      width="80%"/>    
                            
        <adsm:textbox label="endereco" 
                      dataType="text" 
                      property="dsEndereco" 
                      size="50" 
                      maxLength="60" 
                      labelWidth="20%" 
                      width="80%"/>

        <adsm:complement label="telefone" labelWidth="20%" width="32%">
            <adsm:textbox dataType="text" property="nrDddTelefone" maxLength="5" size="5" onchange="return validateDddTelefone()"/>
        	<adsm:textbox dataType="text" property="nrTelefone" maxLength="10" size="10"  onchange="return validateTelefone()" />
        </adsm:complement>

        <adsm:complement label="fax" labelWidth="18%" width="30%">
            <adsm:textbox dataType="text" property="nrDddFax" maxLength="5" size="5" onchange="return validateDddFax()" />
        	<adsm:textbox dataType="text" property="nrFax" maxLength="10" size="10" onchange="return validateFax()"/>
        </adsm:complement>

        <adsm:textbox label="contato" 
                      dataType="text" 
                      property="dsContato" 
                      size="40" 
                      maxLength="60" 
                      labelWidth="20%" 
                      width="32%"/>
                      
        <adsm:textbox label="email" 
                      dataType="email" 
                      property="dsEmailContato" 
                      size="40" 
                      maxLength="60" 
                      labelWidth="18%" 
                      width="30%"/>

	
		<adsm:textbox label="dtEmissaoNFe"
					  dataType="JTDate" 
					  size="10" 
					  maxLength="10" 
					  property="dtEmissaoNotaFiscalEletronica" 
					  picker="true"
					  labelWidth="20%" 
                      width="32%" />


		<adsm:checkbox label="arredondamentoIss" 
		               property="blArredondamentoIss" 
		               labelWidth="18%" 
		               width="30%"/>
		
		<adsm:textbox 
			property="vlLimiteRetencaoIss"
			label="limiteMinimoRetencaoIss"
			dataType="currency" 
			minValue="0"
			required="true"
			size="10"
			labelWidth="20%" 
            width="32%"
		/>
		               
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>

<script>
    function initWindow(eventObj){
       document.getElementById("nrDddTelefone").required = "false";
	   document.getElementById("nrTelefone").required = "false";
	   document.getElementById("nrFax").required = "false";
	   document.getElementById("nrDddFax").required = "false";
	
	   if (eventObj.name== 'tab_click' || eventObj.name== 'newButton_click' || eventObj.name== 'removeButton'){
			setElementValue("vlLimiteRetencaoIss", "0,00");
	   }
	   
	   if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton") {
	     setDisabled("municipio.idMunicipio",true);
	   } else {
	     setDisabled("municipio.idMunicipio",false);
	     setFocusOnFirstFocusableField();
	   }
	   
	}
    
    
   /**
	* Validar um intervalo válido para o campo 1..31
	*/
     function validateDtDiaRecolhimento() {
        var dia = getElementValue("dtDiaRecolhimento");
        if (dia < 1 || dia > 31) {
             alert('<adsm:label key="LMS-27013"/>');
             return false;
        }
      return true;  
     }
     
   /**
	* Validar obrigatóriedade dos campos ddd + telefone
	* ambos preenchidos ou ambos em branco
	*/ 
    function validateDddTelefone() {
       var ddd  = getElementValue("nrDddTelefone");
       if (ddd != null && ddd != '') {
         document.getElementById("nrTelefone").required = "true";
       } else { 
         document.getElementById("nrTelefone").required = "false";
         setElementValue("nrTelefone","");
       }
        
       return true;
    }
    
   /**
	* Validar obrigatóriedade dos campos ddd + telefone
	* ambos preenchidos ou ambos em branco
	*/ 
    function validateTelefone(obj) {
       var fone  = getElementValue("nrTelefone");
       var ddd   = getElementValue("nrDddTelefone");
       if (fone != null && fone != '') {
         document.getElementById("nrDddTelefone").required = "true";
         if (ddd.length==0 || ddd == null) {
             document.getElementById("nrDddTelefone").focus();
         }    
       } else { 
         document.getElementById("nrDddTelefone").required = "false";
         setElementValue("nrDddTelefone","");
       }
       return true;
    }
    
    /**
	* Validar obrigatóriedade dos campos ddd + telefone
	* ambos preenchidos ou ambos em branco
	*/ 
    function validateDddFax() {
       var ddd  = getElementValue("nrDddFax");
       if (ddd != null && ddd != '') {
         document.getElementById("nrFax").required = "true";
       } else { 
         document.getElementById("nrFax").required = "false";
         setElementValue("nrFax","");
       }
        
       return true;
    }
    
   /**
	* Validar obrigatóriedade dos campos ddd + telefone
	* ambos preenchidos ou ambos em branco
	*/ 
    function validateFax(obj) {
       var fone  = getElementValue("nrFax");
       var ddd   = getElementValue("nrDddFax");
       if (fone != null && fone != '') {
         document.getElementById("nrDddFax").required = "true";
         if (ddd.length==0 || ddd == null) {
            document.getElementById("nrDddFax").focus();
         }   
       } else { 
         document.getElementById("nrDddFax").required = "false";
         setElementValue("nrDddFax","");
       }
       return true;
    }
    
</script>
	</adsm:form>
</adsm:window>