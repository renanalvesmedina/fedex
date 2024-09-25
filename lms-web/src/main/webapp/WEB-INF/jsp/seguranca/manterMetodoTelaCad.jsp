<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterMetodoTelaAction">
	<adsm:form action="/seguranca/manterMetodoTela" idProperty="idRecurso" onDataLoadCallBack="myFormCallBack">


 	    <adsm:lookup dataType="text" label="modulo" 
		     	 	 property="modulo" idProperty="idModuloSistema" criteriaProperty="nmModuloSistema"
	 				 exactMatch="false"
	 				 minLengthForAutoPopUpSearch="3"
	  			 	 maxLength="60"
	 				 action="/seguranca/manterModulo"
	 				 service="lms.seguranca.manterMetodoTelaAction.findLookupModulo"
			 	     width="100%"
			 	     required="true"
			 	     >				
	    </adsm:lookup>	


		
		<adsm:combobox property="tpMetodoTela" label="tipo" domain="ADSM_MANTER_METODO_TELA" width="80%" required="true"/>
		<adsm:textbox dataType="text" property="nmRecurso" label="nome" width="80%" size="60" maxLength="200" required="true"/>

		<adsm:textbox dataType="text" property="dsRecurso" label="descricao" width="80%" size="40" maxLength="60" required="true"/>
		<adsm:textbox dataType="text" property="cdRecurso" label="codigo" width="80%" size="40" maxLength="60" required="true"/>


		<adsm:buttonBar>
			<adsm:button caption="abas" action="/seguranca/manterAba" id="btnTela" boxWidth="65" cmd="main">
				<adsm:linkProperty src="idRecurso" target="tela.idRecurso"/>
				<adsm:linkProperty src="nmRecurso" target="tela.nmRecurso"/>
			</adsm:button>
			
			
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton service="lms.seguranca.manterMetodoTelaAction.removeByIdTipo"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	function myFormCallBack_cb(data, errorMessage, errorCode, eventObj) {
				   	  
		onDataLoad_cb(data, errorMessage, errorCode, eventObj);
	
	   var isMetodo = getNestedBeanPropertyValue(data, "metodo") != undefined;
	   
	   if (isMetodo == true){
	      setElementValue("tpMetodoTela", "metodo");
	      setDisabled("btnTela",true);
	 	}else{
	      setElementValue("tpMetodoTela", "tela");
       	  setDisabled("btnTela",false);
   		}
	      
	   setDisabled("tpMetodoTela",true);
	
	}
	
	function initWindow(eventObj) {

		var event = eventObj.name;
		
		if(event == "tab_click"){
	  	  setDisabled("tpMetodoTela",false);
		}else if(event == "storeButton"){
			 	setDisabled("tpMetodoTela",true);
			 	if (getElementValue( "tpMetodoTela" ) == "tela" )  setDisabled("btnTela",false);
			 	else  setDisabled("btnTela",true);
		} else if(event == "removeButton") {
		    setDisabled("tpMetodoTela",false);
		} else if(event == "newButton_click") {
		    setDisabled("tpMetodoTela",false);
		}
    }
    

</script>