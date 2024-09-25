<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.prestcontasciaaerea.faturamentoCiaAereaAction" onPageLoadCallBack="carregaPagina" >
	<adsm:form action="/prestContasCiaAerea/faturamentoCiaAerea" idProperty="idFaturamentoCiaAerea" onDataLoadCallBack="carregaDadosPagina" height="388" >

		<adsm:combobox property="ciaFilialMercurio.empresa.idEmpresa" 
						optionLabelProperty="pessoa.nmPessoa" 
						optionProperty="idEmpresa"   
						service="lms.municipios.empresaService.findCiaAerea"
						onlyActiveValues="true"
						label="ciaAerea" width="60%" labelWidth="17%" serializable="true" required="true">
		</adsm:combobox>
		
		<adsm:lookup property="ciaFilialMercurio.filial" idProperty="idFilial" service="lms.municipios.filialService.findLookup" dataType="text" criteriaProperty="sgFilial" label="filial" size="3" maxLength="3" action="/municipios/manterFiliais" labelWidth="17%" width="60%" exactMatch="true" required="true" > 
				<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="ciaFilialMercurio.filial.pessoa.nmFantasia"/>
				<adsm:textbox dataType="text" property="ciaFilialMercurio.filial.pessoa.nmFantasia" size="37" maxLength="50" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:combobox 	boxWidth="120" property="tpPeriodicidade" domain="DM_PERIODICIDADE_FATURAMENTO" label="periodicidade" onchange="javascript:configuraObjetoDiaCorte(true);" required="true" width="37%" labelWidth="17%" serializable="true"/>

		<adsm:textbox dataType="integer" property="ddFaturamento" label="diaReferenciaFaturamento" size="20" maxLength="2" labelWidth="26%" width="20%" disabled="true" required="true" style="position: absolute;">
			<adsm:combobox boxWidth="120" property="tpDiaSemana" domain="DM_DIAS_SEMANA" style="visibility : hidden" serializable="true" /> 
        </adsm:textbox>
        
        <adsm:textbox dataType="integer" property="nrPrazoPagamento" label="prazoPagamento" width="83%" labelWidth="17%" required="true" serializable="true" />
		<adsm:textbox dataType="percent" property="pcComissao" label="percentualComissao" width="83%" labelWidth="17%" required="true" serializable="true" />

		<adsm:range label="vigencia" labelWidth="17%" width="83%">		
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" serializable="true" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>							
		
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
  	/**
  	* Função para alterar os tipos de objetos conforme a opção selecionada no campo
  	* periodicidade
  	*/
  	
  	function configuraObjetoDiaCorte(){
/*  		if(updatePeriodicidade || !hasValue(getElementValue("tpPeriodicidade")) || !hasValue(getElementValue("idDiaFaturamento")) || !getHasAccess()) {
			setElementValue("tpPeriodicidade", getElementValue("tpPeriodicidadeSolicitado"));
			updatePeriodicidade = true;
		}
*/

		var dmPeriodicidade = getElementValue("tpPeriodicidade")

		// limpa os campos numero dias faturamento e dia da semana.
   	    resetValue("ddFaturamento");
   	    resetValue("tpDiaSemana");
		
		var ddCorte = getElement("ddFaturamento");
	    var nmDiaSemana = getElement("tpDiaSemana");

	    // funcoes para habilitar / desabilitar os campos

   	    switch (dmPeriodicidade){
	    	case "D" :
	    		ddCorte.style.visibility = 'visible';
	      	    ddCorte.disabled = true;
	      	  	ddCorte.value = '';
	      	  	ddCorte.required='false';
	      	  	nmDiaSemana.style.visibility = 'hidden';
	      	  	nmDiaSemana.selectedIndex  = 0;
	      	  	nmDiaSemana.selectedIndex.required='false';
	      	  	nmDiaSemana.required='false';
	      	  	
	     	    break;
	     	case "S" :
	      	    ddCorte.style.visibility = 'hidden';
	      	    ddCorte.disabled = false;
	      	  	ddCorte.value = null;
	      	  	ddCorte.required='false';

	      	  	nmDiaSemana.required='true';
	      	  	nmDiaSemana.style.visibility = 'visible';
	      	  	setDisabled("tpDiaSemana", false);

	     	    break;
	     	case "E" :
	     	case "Q" :
	     	case "M" :
	     	    ddCorte.style.visibility = 'visible';
	     	    ddCorte.disabled = false;
	     	    nmDiaSemana.style.visibility = 'hidden';
	     	    nmDiaSemana.selectedIndex  = 0;
	     	   	nmDiaSemana.required='false';
	     	    break;
	     	default:
	     		setElementValue("ddFaturamento",  "");
				nmDiaSemana.selectedIndex  = 0;
				setDisabled("ddFaturamento", true);
				setDisabled("tpDiaSemana", true);
		}
  	}

	function carregaDadosPagina_cb(data, error) {
		onDataLoad_cb(data);

		var dmPeriodicidade = getElementValue("tpPeriodicidade")
		var nmDiaSemana = getElement("tpDiaSemana");
		var ddCorte = getElement("ddFaturamento");
		if (dmPeriodicidade == "S"){
			nmDiaSemana.selectedIndex  = parseInt(ddCorte.value) + 1;
      	  	nmDiaSemana.required='true';
      	  	nmDiaSemana.style.visibility = 'visible';
      	  	setDisabled("tpDiaSemana", false);

      	  	ddCorte.style.visibility = 'hidden';
      	    ddCorte.disabled = false;
      	  	ddCorte.value = null;
      	  	ddCorte.required='false';

		} else if (dmPeriodicidade == "D"){
			configuraObjetoDiaCorte();
		} else {
     	    ddCorte.style.visibility = 'visible';
     	    ddCorte.disabled = false;

     	    nmDiaSemana.style.visibility = 'hidden';
     	    nmDiaSemana.selectedIndex  = 0;
     	   	nmDiaSemana.required='false';
     	   	
      	  	setDisabled("tpDiaSemana", false);
		}

		if(getElementValue("ddFaturamento") == 0 || getElementValue("ddFaturamento") == ""){
			setDisabled("ddFaturamento", true);
			
			setElementValue("ddFaturamento",  "");
		}
	}
	
	function carregaPagina_cb(data, error) {
		onPageLoad_cb(data, error);
		
		configuraObjetoDiaCorte();
	}
</script>
