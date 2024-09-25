<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
//SETANDO A FILIAL COM A FILIAL DO USUARIO LOGADO
	function pageLoad_cb(data) {
		onPageLoad_cb(data);

		var url = new URL(parent.location.href);
		var filial = url.parameters["filial"];
		if (filial == undefined){
			var sdo = createServiceDataObject("lms.vol.recusaAction.findDataSession","dataSession",null);
			xmit({serviceDataObjects:[sdo]});
		} else {						
			setElementValue('dataFinal',setFormat(document.getElementById("dataFinal"),url.parameters["dataFin"]));
			setElementValue('dataInicial',setFormat(document.getElementById("dataInicial"),url.parameters["dataIni"]));
			
			setaValorComboStatus(url.parameters["coluna"]);
						
			var dados = new Array();
			setNestedBeanPropertyValue(dados, 'filial', filial);
			setNestedBeanPropertyValue(dados, 'frota', url.parameters["frota"]);
			var sdo = createServiceDataObject("lms.vol.recusaAction.buscaDadosIniciais","setaDadosIniciais",dados);
			xmit({serviceDataObjects:[sdo]});
		}
		
	}
	
	/**seta o período com a data atual
	*/
	function buscaData(){
		var dados = new Array();
		var sdo = createServiceDataObject("lms.vol.recusaAction.buscaDataAtual","setaData",dados);
			xmit({serviceDataObjects:[sdo]});
	}
	
	function setaData_cb(data){
		setElementValue("dataInicial", data.dataAtual);
		setElementValue("dataFinal", data.dataAtual);
	}
	
	function setaValorComboStatus(valor){
		if ((valor != undefined) && (valor != 'undefined')){
			var value = "";
			if (valor == 'REE'){
				value = 'R';
			} else {
				value = 'D';
			}
			
			var combo = document.getElementById('status');	
			for (var i = 0; i < combo.length; i++){
				if (value == combo.children[i].value){
					combo.children[i].selected = true;
					break;
				}
			}
		}

	}
	
	var dataUsuario;	
	function dataSession_cb(data, error) {
		if (error != undefined){
			alert(error);
		} else {
			if (!isLookup()){
				dataUsuario = data;	
				var id = getNestedBeanPropertyValue(data,"filial.idFilial");
				var sgFilial = getNestedBeanPropertyValue(data,"filial.sgFilial");
				var nmFantasia = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
			
				setElementValue("filial.idFilial",id);
				setElementValue("filial.sgFilial",sgFilial);
				setElementValue("filial.pessoa.nmFantasia",nmFantasia);
			}		
		}
	}
	
	
	function setaDadosIniciais_cb(data, error){
		if (error != undefined){
			alert(error);
		} else {		
			setElementValue("filial.idFilial",data.filial.idFilial);
			setElementValue("filial.sgFilial",data.filial.sgFilial);
			setElementValue("filial.pessoa.nmFantasia",data.filial.pessoa.nmFantasia);
			
			setElementValue("meioTransporte.idMeioTransporte",data.meioTransporte.idMeioTransporte);
			setElementValue("meioTransporte.nrFrota",data.meioTransporte.nrFrota);
			setElementValue("meioTransporte2.idMeioTransporte",data.meioTransporte.idMeioTransporte);
			setElementValue("meioTransporte2.nrIdentificador",data.meioTransporte.nrIdentificador);
			
			findButtonScript('recusas', document.forms[0]);
		}
	}
	
	
	/**
	 * Preenche os campos relacionados com o usuario.
	 */
	function fillDataUsuario() {
		if(dataUsuario){
			setElementValue("filial.idFilial", dataUsuario.filial.idFilial);
			setElementValue("filial.sgFilial", dataUsuario.filial.sgFilial);
			setElementValue("filial.pessoa.nmFantasia", dataUsuario.filial.pessoa.nmFantasia);
		}
	}
	
	function initWindow(eventObj) {
		
		if (eventObj.name == "cleanButton_click") {
			fillDataUsuario();
			
			filial_sgFilialOnChangeHandler();	
			
			setFocus(document.getElementById("filial.sgFilial"));
		} 
		buscaData();
	}
	
	/**
	 * Retorna o parametro 'mode' que contem o modo em que a tela esta sendo utilizada.
	 * Caso mode seja igual a 'lookup' significa que a tela esta sendo aberta por uma lookup.
	 */
	function isLookup() {
		var url = new URL(parent.location.href);
		var mode = url.parameters["mode"];
		if ((mode!=undefined) && (mode=="lookup")) return true;
		return false;
	}
</script>

<adsm:window service="lms.vol.recusaAction" onPageLoadCallBack="pageLoad" >
	<adsm:form action="/vol/recusa" >
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>
		<adsm:hidden property="tpAcesso" serializable="false" value="F"/>
		
        <adsm:lookup label="filial" labelWidth="15%" width="85%"
		             property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.recusaAction.findLookupFilialByUsuarioLogado" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             required="true"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3">
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" 
        						  modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado"  modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" />
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
        
        <adsm:lookup label="remetente" labelWidth="15%" width="85%"
              property="pessoa" 
              idProperty="idPessoa"
              criteriaProperty="pessoa.nrIdentificacao" 
              relatedCriteriaProperty="nrIdentificacaoFormatado"
              action="/configuracoes/manterPessoas"
              service="lms.vol.recusaAction.findLookupPessoa"  
              dataType="text" 
              size="20" 
              maxLength="20" > 
			<adsm:propertyMapping modelProperty="idPessoa" relatedProperty="pessoa.idPessoa" />
			<adsm:propertyMapping modelProperty="nrIdentificacao" relatedProperty="pessoa.nrIdentificacao" />
			<adsm:propertyMapping modelProperty="nmPessoa" relatedProperty="pessoa.nmPessoa" />
			
			<adsm:textbox dataType="text" property="pessoa.nmPessoa" size="50" maxLength="50" disabled="true" />
			<adsm:hidden property="pessoa.nrIdentificacao"/>
		</adsm:lookup>

		<adsm:combobox property="status"  label="statusRecusa" domain="DM_STATUS_RECUSA" width="35%"/>

		<adsm:range label="periodo" width="35%" required="true">
			<adsm:textbox dataType="JTDate" property="dataInicial" />
			<adsm:textbox dataType="JTDate" property="dataFinal" />
		</adsm:range>
		
		
		<adsm:lookup label="meioTransporte" labelWidth="15%" width="80%" picker="false"
                      property="meioTransporte"
                      idProperty="idMeioTransporte"
                      criteriaProperty="nrFrota"
                      action="/contratacaoVeiculos/manterMeiosTransporte"
                      service="lms.vol.recusaAction.findLookupMeioTransporte" 
                      dataType="text"
                      size="8" 
                      maxLength="6"
                      exactMatch="true"
                      required="false"
          >
	          <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
              <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
              <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
              <adsm:propertyMapping relatedProperty="meioTransporte2.nrIdentificador" modelProperty="nrIdentificador" />
              <!--  Criteria por nrIdentificador -->        
              <adsm:lookup 
                         property="meioTransporte2"
                         idProperty="idMeioTransporte"
                         criteriaProperty="nrIdentificador"
                         action="/contratacaoVeiculos/manterMeiosTransporte"
                         service="lms.vol.recusaAction.findLookupMeioTransporte" 
                         dataType="text"
                         size="30" 
                         maxLength="25"
                         exactMatch="false"
                         serializable="false"
                         minLengthForAutoPopUpSearch="5"
              >
	              <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
                  <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
                  <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
                  <adsm:propertyMapping relatedProperty="meioTransporte.nrFrota" modelProperty="nrFrota" />
                  <adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte" />      
             </adsm:lookup>
        </adsm:lookup>		

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="recusas"/>
			<adsm:resetButton/>
		</adsm:buttonBar>				
	
	</adsm:form>

	<adsm:grid property="recusas" idProperty="idRecusa" disableMarkAll="true" onPopulateRow="populateRow" scrollBars="horizontal"
		selectionMode="none" rows="8" gridHeight="190" unique="true" onRowClick="executeRetorno" service="lms.vol.recusaAction.findPaginatedRecusa"
		rowCountService="lms.vol.recusaAction.getRowCountRecusa" >
		<adsm:gridColumn property="istatus" title="status" width="60" image="/images/bandeira_vermelha.gif" imageLabel="resolvidoMercurio" align="center"/>
		<adsm:gridColumn property="ictrc" title="ctrc" width="50" image="/images/tratativas.gif"/>	
		<adsm:gridColumn property="nrConhecimento" title="" width="90" align="right" dataType="integer" mask="0000000000"/> 
		<adsm:gridColumn property="destinatario" title="destinatario" width="160"/>
		<adsm:gridColumn property="frota" title="frota" width="70"/>
		<adsm:gridColumn property="nrIdentificador" title="" width="90" />
		<adsm:gridColumn property="numero" title="numeroEquipamento" width="90" dataType="integer" mask="###############"/>
		<adsm:gridColumn property="dpe" dataType="JTDate"  title="dpe" width="70"/>
		<adsm:gridColumn property="dhRecusa" dataType="JTDateTimeZone" title="motivo" width="130"/>
		<adsm:gridColumn property="dsOcorrenciaEntrega" title="" width="150"/>
		<adsm:gridColumn property="opcaoCliente" title="opcaoCliente" isDomain="true" width="100"/>
		<adsm:gridColumn property="dhOcorrencia" dataType="JTDateTimeZone" title="baixa" width="130"/>
		<adsm:gridColumn property="faltam" title="faltam" width="70" dataType="integer"/>
		<adsm:gridColumn property="dhResolucao" dataType="JTDateTimeZone" title="resolucao" width="130"/>
                
    	<adsm:gridColumn property="remetente" title="remetente" width="160"/>				
				
		<adsm:buttonBar>
			<adsm:button caption="relatorio" id="enviarRelatorio" onclick="emitirRelatorio();" />
		</adsm:buttonBar>
	</adsm:grid>
	
	<adsm:i18nLabels>
		<adsm:include key="resolvidoMercurio"/>
	</adsm:i18nLabels>

</adsm:window>

<script>	
        function executeRetorno(idRecusa){
            if(confirm("Você deseja realiza a reentrega deste documento?")){
                var data = new Object();
                data.id  = idRecusa;
                var sdo = createServiceDataObject('lms.vol.recusaAction.executeRetorno',  'executeRetorno', data); 
                xmit({serviceDataObjects:[sdo]});
            }
            return false;
        }
        
        function executeRetorno_cb(data,error){
            if (error != undefined) {
	      alert(error)
            }
            return false;
        }
        
	function popupHistorico(idRecusa, documento, frota, numero) {	
		showModalDialog('/vol/recusaHistorico.do?cmd=main&idRecusa=' + idRecusa + '&frota=' + frota + '&documento=' + documento + '&numero=' + numero,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
	}
		
	function carta(documento,idDoctoServicoValue,tpDocumentoServico,idFilial,sgFilial,idRecusa){
		var	dadosURL = null;
		dadosURL = "&doctoServico.nrDoctoServico="+documento+"&doctoServico.idDoctoServico="+idDoctoServicoValue+"&doctoServico.tpDocumentoServico="+tpDocumentoServico+"&doctoServico.filialByIdFilialOrigem.idFilial="+idFilial+"&doctoServico.filialByIdFilialOrigem.sgFilial="+sgFilial+"&idRecusa="+idRecusa;
		parent.parent.redirectPage('pendencia/emitirCartaMercadoriasDisposicao.do?cmd=main'+dadosURL);
	
	}
	
	
	function tratativas(documento,idRecusa,destinatario,status) {
		var dadosURL = null;
		dadosURL = "&documento="+documento+"&idRecusa="+idRecusa+"&destinatario="+destinatario+"&status="+status;
		//parent.parent.redirectPage('vol/recusaTratativas.do?cmd=main'+dadosURL);
		showModalDialog('vol/recusaTratativas.do?cmd=main'+dadosURL, window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:500px;');
		findButtonScript('recusas', document.forms[0]);
	}	
	
	
		
	function populateRow(tr,data) { 
         var status = getNestedBeanPropertyValue(data,"status.value");
         var documento = getNestedBeanPropertyValue(data,"nrConhecimento");
         var frota = getNestedBeanPropertyValue(data,"frota");
         var dhResolucao = getNestedBeanPropertyValue(data,"dhResolucao");
   		 var idRecusa = getNestedBeanPropertyValue(data,"idRecusa");
   		 var numero = getNestedBeanPropertyValue(data,"numero");
   		 var idDoctoServicoValue = getNestedBeanPropertyValue(data,"idDoctoServico");
   		 var idFilial = getNestedBeanPropertyValue(data, "idFilialDoctoServico");
   		 var sgFilial = getNestedBeanPropertyValue(data, "sgFilialDoctoServico");
   		 var tpDocumentoServico = data.tpDocumentoServico.value;
   		 var destinatario = getNestedBeanPropertyValue(data, "destinatario");
   		 
         if (status == undefined || status == '') {
              tr.children[0].innerHTML = "<NOBR></NOBR>";
         }             
          
    
       
         //seta hint das bandeiras
         if(dhResolucao == null){
       		  tr.children[0].innerHTML = tr.children[0].innerHTML.replace(i18NLabel.getLabel("resolvidoMercurio"),data.opcaoCliente.description);
         }
             
         tr.children[0].innerHTML = tr.children[0].innerHTML.replace("<A", "<A href=javascript:popupHistorico('" + idRecusa + "','" + documento.trim()  + "','" + frota + "','" + numero + "');");
        	
         //Altera a coluna do Status da Entrega
         if(status == 'E'){
         	 tr.children[1].innerHTML = '';
         	 tr.children[0].innerHTML = tr.children[0].innerHTML.replace("bandeira_vermelha.gif","bandeira_verde.gif");
         	 tr.children[2].innerHTML = sgFilial + " " + getFormattedValue("integer", documento.trim(), "0000000000", true);
         } else {
			 if(status == 'C') {
			 	tr.children[0].innerHTML = tr.children[0].innerHTML.replace("bandeira_vermelha.gif","bandeira_amarela.gif"); 		 			 	
			 	tr.children[1].innerHTML = "<A href=\"#\" onclick=\"gerarCarta('"+ idDoctoServicoValue +"','"+ idRecusa +"');\">" +"<img src='../images/tratativas.gif' border='none'/>"+ "</A>";
			 //	tr.children[2].innerHTML = "<A href=\"javascript:tratativas('" + documento.trim()  + "','" + idRecusa  + "','" + destinatario + "','" + status + "');\" onclick=\"tratativas('" + documento.trim()  + "','" + idRecusa + "','" + destinatario + "','" + status + "');\">" + sgFilial + " " +  getFormattedValue("integer", documento.trim(), "0000000000", true) + "</A>";
			    tr.children[2].innerHTML = "<A href=\"javascript:tratativas('" + getFormattedValue("integer", documento.trim(), "0000000000", true) + "','" + idRecusa  + "','" + destinatario + "','" + status + "');\" onclick=\"tratativas('" + getFormattedValue("integer", documento.trim(), "0000000000", true) + "','" + idRecusa + "','" + destinatario + "','" + status + "');\">" + sgFilial + " " +  getFormattedValue("integer", documento.trim(), "0000000000", true) + "</A>";
		  	}else {
			   	if((status == 'D' || status == 'R') && dhResolucao == null) {
			 	 	 tr.children[0].innerHTML = tr.children[0].innerHTML.replace("bandeira_vermelha.gif","bandeira_azul.gif"); 
			 		 tr.children[2].innerHTML = "<A href=\"javascript:tratativas('" + documento.trim()  + "','" + idRecusa  + "','" + destinatario + "','" + status + "');\" onclick=\"tratativas('" + documento.trim()  + "','" + idRecusa + "','" + destinatario + "','" + status + "');\">" + sgFilial + " " +  getFormattedValue("integer", documento.trim(), "0000000000", true) + "</A>";
			 	}else{
			 		if(dhResolucao != null) {
			 			tr.children[0].innerHTML = tr.children[0].innerHTML.replace("bandeira_vermelha.gif","bandeira_verde.gif");
			 			tr.children[2].innerHTML = sgFilial + " " + getFormattedValue("integer", documento.trim(), "0000000000", true);
			 		//	tr.children[2].innerHTML = "<A href=\"javascript:carta('" + documento.trim()  + "','" + idDoctoServicoValue + "','" + tpDocumentoServico + "','" + idFilial + "','" + sgFilial + "','" + idRecusa + "');\" onclick=\"carta('" + documento.trim()  + "','" + idDoctoServicoValue + "','" + tpDocumentoServico + "','" + idFilial + "','" + sgFilial + "','" + idRecusa + "');\">" + sgFilial + " " + getFormattedValue("integer", documento.trim(), "0000000000", true) + "</A>";
			 		}
			 		else {
			 		   tr.children[1].innerHTML = '';
			 		   tr.children[2].innerHTML = "<A href=\"javascript:carta('" + documento.trim()  + "','" + idDoctoServicoValue + "','" + tpDocumentoServico + "','" + idFilial + "','" + sgFilial + "','" + idRecusa + "');\" onclick=\"carta('" + documento.trim()  + "','" + idDoctoServicoValue + "','" + tpDocumentoServico + "','" + idFilial + "','" + sgFilial + "','" + idRecusa + "');\">" + sgFilial + " " + getFormattedValue("integer", documento.trim(), "0000000000", true) + "</A>";
			 		}
		 		}	
		    } 
	    }
    }

	function gerarCarta(idDoc, idRecusa) {
		loadParamsReport(idDoc, idRecusa);						
	}

	function loadParamsReport(idDoc, idRecusa){
		var data = new Object();
		data.idDoctoServico = idDoc;
		data.idRecusa = idRecusa;
		var idFilial = document.getElementById("filial.idFilial").value;
		data.idFilial = idFilial;
		var sdo = createServiceDataObject('lms.vol.recusaAction.findParamsCartaReport',  'loadParamsReport', data); 
		xmit({serviceDataObjects:[sdo]});
	}

	function loadParamsReport_cb(data, error){
		emitirCarta(data);	
	}

	function emitirCarta(data) {
		var dataObject = data;
		//dataObject.formBean = buildFormBeanFromForm(this.document.forms[0]);
		//Determina que este xmit sera para um relatorio...
		var sdo = createServiceDataObject('lms.pendencia.emitirCartaMercadoriasDisposicaoAction.execute',  'emitirCarta', dataObject); 
		xmit({serviceDataObjects:[sdo]});
	}
	
	function emitirCarta_cb(data, error) {
		openReportWithLocator(data, error);
	}
	
	function emitirRelatorio() {
		var form = buildFormBeanFromForm(document.forms[0]);
		
		var value = document.getElementById('status').value;
		var combo = document.getElementById('status');	
		for (var i = 1; i < combo.length; i++){
			if (value == combo.children[i].value){
				form.FILTRO_STATUS = combo.children[i].text;
				break;
			}
		}
		
		form.FILTRO_FILIAL = document.getElementById("filial.sgFilial").value+" "+document.getElementById("filial.pessoa.nmFantasia").value;
		form.FILTRO_FROTA  = document.getElementById("meioTransporte.nrFrota").value+" "+document.getElementById("meioTransporte2.nrIdentificador").value;
		
		var sdo = createServiceDataObject('lms.vol.recusaAction.execute',  'emitirCarta', form); 
		xmit({serviceDataObjects:[sdo]});
	}

</script>
