<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.diaFaturamentoEmpresaService">
	<adsm:form action="/configuracoes/manterDiasFaturamentoEmpresa" idProperty="idDiaFaturamentoEmpresa" onDataLoadCallBack="myOnDataLoad">
        <adsm:combobox property="tpPeriodicidade" domain="DM_PERIODICIDADE_FATURAMENTO" label="periodicidade" required="true" onchange="javascript:configuraObjetoDiaCorte();" />
		<adsm:textbox dataType="integer" minValue="1" maxValue="10" property="ddCorte" label="diaFaturamento" size="13" maxLength="2" onchange="return validateDiaCorte(this);"  style="position: absolute;">
			<adsm:combobox property="nmDiaSemana" serializable="false" onchange="javascript:setDiaCorte(this);" domain="DM_DIAS_SEMANA"  style="visibility : hidden" /> 
        </adsm:textbox>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
		<adsm:i18nLabels>
			<adsm:include key="diaFaturamento"/>
		</adsm:i18nLabels>
		
<script type="text/javascript">

	//seta o label para a comboBox nmDiaSemana
	getElement("nmDiaSemana").label = i18NLabel.getLabel('diaFaturamento');
 /**
  * seta valor para o campo dia corte
  */
  function setDiaCorte(obj) {
    var ddCorte = document.getElementById("ddCorte");
    ddCorte.value = document.getElementById("nmDiaSemana").value;
  }
  
  function myOnDataLoad_cb(data, error){
      onDataLoad_cb(data, error);
      configuraObjetoDiaCorte();
  }
  
  /**
  * Valida valores minimos e máximos do campo dia corte
  */
  function validateDiaCorte(obj){
     var dmPeriodicidade = getElementValue("tpPeriodicidade"); 
  	 var retorno = myValidateMinMaxValue(obj);
  	 if (!retorno) {
       switch (dmPeriodicidade){
     	 case "E" :	 
             alert('<adsm:label key="LMS-27011"/>');
     	     break;    
     	 case "Q" :	 
     	     alert('<adsm:label key="LMS-27012"/>'); 
     	     break;    
     	 case "M" :	 
             alert('<adsm:label key="LMS-27013"/>'); 
     	     break;    
       }
       resetValue("ddCorte");     
  	 }
  	 return retorno; 
  }
  
  /**
  * Função identica a que esta no validate.js
  * exceto pelo alert
  */
  function myValidateMinMaxValue(obj){
	var retorno = true;
	if ((obj.minValue != null && obj.minValue != '')){
		if (!compareData(obj.minValue,obj.value,obj.dataType,"",obj.mask)){
			retorno = false;
		}	
	}
	if ((obj.maxValue != null && obj.maxValue != '')){
		if (!compareData(obj.value,obj.maxValue,obj.dataType,obj.mask,"")){
			retorno = false;
		}	
	}	
	return retorno;
  }
  
  /**
  * Função para alterar os tipos de objetos conforme a opção selecionada no campo
  * periodicidade
  */
  function configuraObjetoDiaCorte(){
     var dmPeriodicidade = getElementValue("tpPeriodicidade"); 
     var ddCorte = document.getElementById("ddCorte");
     var nmDiaSemana = document.getElementById("nmDiaSemana");
     switch (dmPeriodicidade){
     	 case "D" :
     	     ddCorte.style.visibility = 'visible';
       	     ddCorte.disabled = true;
       	     ddCorte.value = "";
     	     nmDiaSemana.style.visibility = 'hidden';
     	     ddCorte.required = "false";		
     	     nmDiaSemana.required = "false";
     	     break;
     	 case "S" :	
      	     ddCorte.style.visibility = 'hidden';
      	     ddCorte.disabled = false;
     	  	 nmDiaSemana.style.visibility = 'visible';
     	  	 nmDiaSemana.required = "true";
     	  	 ddCorte.required = "false";
     	     break;    
     	 case "E" :	 
     	     ddCorte.style.visibility = 'visible';
     	     ddCorte.disabled = false;
     	     nmDiaSemana.style.visibility = 'hidden';
     	     ddCorte.minValue = "1";
     	     ddCorte.maxValue = "10";
     	     ddCorte.required = "true";
     	     nmDiaSemana.required = "false";
     	     break;    
     	 case "Q" :	 
     	     ddCorte.style.visibility = 'visible';
     	     ddCorte.disabled = false;
     	     nmDiaSemana.style.visibility = 'hidden';
     	     ddCorte.minValue = "1";
     	     ddCorte.maxValue = "15";
     	     ddCorte.required = "true";
     	     nmDiaSemana.required = "false";
     	     break;    
     	 case "M" :	 
     	     ddCorte.style.visibility = 'visible';
     	     ddCorte.disabled = false;
     	     nmDiaSemana.style.visibility = 'hidden';
     	     ddCorte.minValue = "1";
     	     ddCorte.maxValue = "31";
     	     ddCorte.required = "true";
     	     nmDiaSemana.required = "false";
     	     break; 
     	 default :
     	     ddCorte.style.visibility = 'visible';
       	     ddCorte.disabled = true;
       	     ddCorte.value = "";
     	     nmDiaSemana.style.visibility = 'hidden';
     	     ddCorte.required = "false";
     	     nmDiaSemana.required = "false";		
     	     break;
     }     
	 
  } 
</script>		
	</adsm:form>
</adsm:window>
