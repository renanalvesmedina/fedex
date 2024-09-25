<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.gerencialAction" onPageLoad="myOnPageLoad" >
	
	<adsm:form action="/vol/gerencial"  >
	    <adsm:hidden property="idFilial"/>
	    <adsm:hidden property="sgFilial"/>
	    <adsm:hidden property="nmFantasia"/>
	    <adsm:hidden property="idGrupoFrota"/>
	    <adsm:hidden property="tipoFrota"/>
	    <adsm:hidden property="tipoEntrega"/>
	    <adsm:hidden property="tipoColeta"/>
	    <adsm:hidden property="idFrota"/>
	    <adsm:hidden property="codRelatorio"/>
	    
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="pararAtualizacao" buttonType="a" id="btnParar"
				boxWidth="125" onclick="pararAtualizacao();" />
			<adsm:button caption="acaoImediata" onclick="filtrosDefault();"
				buttonType="outros" disabled="false" />
			<adsm:button caption="todos" onclick="clean();" buttonType="outros"
				disabled="false" />
			<adsm:button caption="irPara" buttonType="a" onclick="irPara();" />
		</adsm:buttonBar>
		
		<adsm:label key="ligacoesPendentesSom" width="1%" style="border:none;"/>			
			
		<adsm:textbox label="ligacoesPendentes" labelWidth="18%" property="ligacoesPendentes" 
					  dataType="integer" disabled="true" width="28%" size="2" />
					  
					 	
		<adsm:textbox label="coletasNaoRecebidas" labelWidth="18%" property="totalColetasSemInteracaoVol" 
					  dataType="integer" disabled="true" width="10%" size="2" />
					  
		<adsm:textbox label="totalColetas" labelWidth="15%" property="totalColetas" 
					  dataType="integer" disabled="true" width="10%" size="2" />		  
					  

		<adsm:hidden property="frotaPlaca"/>
		<adsm:label key="espacoBranco" width="4%" style="border:none;"/>
		<adsm:lookup label="irParaFrotaPlaca" labelWidth="150" width="275" picker="false"
				property="txtFrota"
				idProperty="idMeioTransporte"
				criteriaProperty="nrFrota"
				action="/contratacaoVeiculos/manterMeiosTransporte"
				service="lms.vol.controleEquipamentosAction.findLookupMeioTransporte"
				dataType="text"
				size="6"
				maxLength="6"
				exactMatch="true" >
				
				<adsm:propertyMapping criteriaProperty="idFilial" modelProperty="filial.idFilial"/>
				<adsm:propertyMapping criteriaProperty="sgFilial" modelProperty="filial.sgFilial"/>
				<adsm:propertyMapping criteriaProperty="nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
				<adsm:propertyMapping relatedProperty="txtPlaca.nrIdentificador" modelProperty="nrIdentificador" />
				
					<!-- Criteria por nrIdentificador -->
					<adsm:lookup
							property="txtPlaca"
							idProperty="idMeioTransporte"
							criteriaProperty="nrIdentificador"
							action="/contratacaoVeiculos/manterMeiosTransporte"
							service="lms.vol.controleEquipamentosAction.findLookupMeioTransporte"
							dataType="text"
							size="20"
							maxLength="25"
							exactMatch="false"
							minLengthForAutoPopUpSearch="5">
							
							<adsm:propertyMapping criteriaProperty="idFilial" modelProperty="filial.idFilial"/>
							<adsm:propertyMapping criteriaProperty="sgFilial" modelProperty="filial.sgFilial"/>
							<adsm:propertyMapping criteriaProperty="nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
							<adsm:propertyMapping relatedProperty="txtFrota.nrFrota" modelProperty="nrFrota" />
							<adsm:propertyMapping relatedProperty="txtFrota.idMeioTransporte" modelProperty="idMeioTransporte" />
					</adsm:lookup>
		</adsm:lookup>


	</adsm:form>
   
   	<adsm:grid property="gerencial" selectionMode="none" onRowClick="disableClick"
			   service="lms.vol.gerencialAction.findAcompanhamentoGerencial" onPopulateRow="populateRow"
			   rowCountService="lms.vol.gerencialAction.getRowCount" disableMarkAll="true" rows="500"		   
			   gridHeight="300" unique="true" scrollBars="vertical" idProperty="idMeioTransporte" showPagging="false"
			   onDataLoadCallBack="findAcompanhamentoGerencial"
			   >
		<adsm:gridColumn align="center" property="istatus" title="status" width="37" image="/images/bandeira_vermelha_piscante.gif" openPopup="true" link="/vol/gerencialTratativas.do?cmd=main" linkIdProperty="meioTransporte2.nrFrota" popupDimension="786,480" />
		<adsm:gridColumn title="frotaPlaca" property="frota" width="113" ></adsm:gridColumn>
		<adsm:editColumn title="hidden" property="evento" dataType="text" field="hidden" />
		<adsm:gridColumn align="center" property="tratativas" title="tratativas" width="38" image="/images/tratativas.gif" openPopup="true" link="/vol/gerencialTratativas.do?cmd=main" linkIdProperty="meioTransporte2.nrFrota" popupDimension="786,510" />
		<adsm:gridColumn align="center" property="eficiencia" title="eficiencia" width="40" />
		<adsm:gridColumn align="center" property="frequencia" title="frequencia" width="55" dataType="integer" mask="###,###,###"/>
		<adsm:gridColumn align="center" property="entregas.istatus" title="status" width="37" image="/images/bandeira_vermelha_piscante.gif" openPopup="true" link="/vol/gerencialTratativas.do?cmd=main" linkIdProperty="meioTransporte2.nrFrota" popupDimension="786,510" />
		<adsm:gridColumn align="center" property="entregas.manif" title="controleCarga" width="91" />
		<adsm:gridColumn align="center" property="entregas.reali" title="reali" width="57" dataType="integer" mask="###,###,###"/>
		<adsm:gridColumn align="center" property="entregas.total" title="total" width="56" dataType="integer" mask="###,###,###"/>
		<adsm:gridColumn align="center" property="coletas.istatus" title="status" width="38" image="/images/bandeira_vermelha_piscante.gif" imageLabel="status" openPopup="true" link="/vol/gerencialTratativas.do?cmd=main" linkIdProperty="meioTransporte2.nrFrota" popupDimension="786,510" />
		<adsm:gridColumn align="center" property="coletas.autom" title="autom" width="55" dataType="integer" mask="###,###,###"/>
		<adsm:gridColumn align="center" property="coletas.reali" title="reali" width="56" dataType="integer" mask="###,###,###"/>
		<adsm:gridColumn align="center" property="coletas.total" title="total" width="55" dataType="integer" mask="###,###,###" />
		<adsm:editColumn title="hidden" property="dsNumero" dataType="text" field="hidden" />
		<adsm:editColumn title="hidden" property="nrIdentificador" dataType="text" field="hidden" />
		
	</adsm:grid>
	<adsm:i18nLabels>
		<adsm:include key="status"/>
		<adsm:include key="frotaPlaca"/>
		<adsm:include key="evento"/>
		
		<adsm:include key="entregas"/>
		<adsm:include key="tratativas"/>
    	<adsm:include key="eficienciaAndamento"/>
    	<adsm:include key="freqAndamento"/>
		<adsm:include key="controleCarga"/>
		<adsm:include key="reali"/>
		
		<adsm:include key="coletas"/>
		<adsm:include key="total"/>
		<adsm:include key="autom"/>
		<adsm:include key="total"/>
		<adsm:include key="totais"/>
		<adsm:include key="LMS-41018"/>
		<adsm:include key="LMS-00061"/>
		<adsm:include key="pararAtualizacao"/>
		<adsm:include key="ativarAtualizacao"/>
	</adsm:i18nLabels>
</adsm:window>

<script>
    
    var oInterval = "" ;
    var blPlay = false;

    function findAcompanhamentoGerencial_cb(data,error) {
       if (blPlay) {
          playBeep();
       }
       blPlay = false;
       atualizaTela = true;
       tabGroup.setDisabledTab("det", true);
    }
    
    function playBeep() {
       document.all['BGSOUND_ID'].loop='10';
       document.all['BGSOUND_ID'].volume= '-600';
       document.all['BGSOUND_ID'].src='../audio/goodread.wav';
    }
    
    function stopBeep() {
       document.all['BGSOUND_ID'].src='';
    }
        
    /*
     * Busca em todos os 'title' o primeiro que contempla os criterios passados, neste caso 'nrFrota<*> nrIdentificador',
     * setando o 'location' com a ancora ja existente para a linha do title.
     */
	function irPara() {
		var nrFrota = getElementValue('txtFrota.nrFrota');
		var nrIdentificador = getElementValue('txtPlaca.nrIdentificador');
		var listaTR = document.getElementById("gerencial.dataTable").getElementsByTagName('tr');
		var frotaPlaca = new RegExp(nrFrota + '(\\*|\\s)?\\s' + nrIdentificador, 'g');
		
		window.document.location= "#" + nrFrota;

		if (nrFrota != "") {
			for (var i = 0; i < listaTR.length; i++) {
				if ((listaTR[i].cells[1] != null) && (frotaPlaca.test(listaTR[i].cells[1].title))) {
					window.document.location= "#" + listaTR[i].getElementsByTagName('a')[0].id;
					return;
				}
			}
		}
		
		alert(i18NLabel.getLabel("LMS-00061"));
	}
	
    function pararAtualizacao() {
       if (oInterval == undefined) {
          oInterval = window.setInterval("fnInitFindGerencial()",480000);
          obj = document.getElementById("btnParar");
          obj.value = i18NLabel.getLabel("pararAtualizacao");
       } else {
          oInterval= window.clearInterval(oInterval); 
          obj = document.getElementById("btnParar");
          obj.value = i18NLabel.getLabel("ativarAtualizacao");
          
       } 
    }
    
    /**
    * variável utilizada como semáforo, evitando que findButtonScript('gerencial', document.forms[0]) seja executado
    * sem que o find anterior tenha retornado
    */
    var atualizaTela = true;
    
    function fnInitFindGerencial(){
    	if (atualizaTela){
       		tabShowAcompanhamento()
       }
    }
    
	function disableClick() {
		return false;
	}
	
    function initWindow(eventObj) {
       if (eventObj.name == "tab_click") {
          oInterval = window.setInterval("fnInitFindGerencial()",480000);
       } 
       setElementValue('ligacoesPendentes','');  
       setElementValue('totalColetasSemInteracaoVol','');
       setElementValue('totalColetas','');
       
    }
    
    function tabHideAcompanhamento() {	
       oInterval= window.clearInterval(oInterval); 
    }
    
	function tabShowAcompanhamento() {
	
      var tabGroup = getTabGroup(this.document);
      var tabFiltro = tabGroup.getTab("filtro");
      
      var idFilial = tabFiltro.getFormProperty("filial.idFilial");
      var sgFilial = tabFiltro.getFormProperty("filial.sgFilial");
      var nmFantasia = tabFiltro.getFormProperty("filial.pessoa.nmFantasia");
      var idGrupoFrota = tabFiltro.getFormProperty("grupo.idGrupoFrota");
      var tipoFrota = tabFiltro.getFormProperty("tipoFrota");
      var tipoEntrega = tabFiltro.getFormProperty("tipoEntrega");
      var tipoColeta = tabFiltro.getFormProperty("tipoColeta");
      
      setElementValue("idFilial",idFilial);
      setElementValue("sgFilial",sgFilial);
      setElementValue("nmFantasia",nmFantasia);
      setElementValue("idGrupoFrota",idGrupoFrota);
      setElementValue("tipoFrota",tipoFrota);
      setElementValue("tipoEntrega",tipoEntrega);
      setElementValue("tipoColeta",tipoColeta);
      
      atualizaTela = false;
      findButtonScript('gerencial', document.forms[0]);
      
   }
   
   function clean() {
        setElementValue('ligacoesPendentes','');	
        setElementValue('totalColetasSemInteracaoVol','');
        setElementValue('totalColetas','');
		setElementValue('tipoFrota','');		
		setElementValue('tipoEntrega','');		
		setElementValue('tipoColeta','');
		findButtonScript('gerencial', document.forms[0]);
	}
	
	function filtrosDefault() {
	    setElementValue('ligacoesPendentes','');	
        setElementValue('totalColetasSemInteracaoVol','');
        setElementValue('totalColetas','');
		setElementValue('tipoFrota','CHS');		
		setElementValue('tipoEntrega','');		
		setElementValue('tipoColeta','NR');
		findButtonScript('gerencial', document.forms[0]);
   }
   
	 /*  
   function enviaColetas(idEquipamento) {
		if (confirm('Deseja enviar um SMS com as Coletas novamente?')) {
		    if (idEquipamento != undefined) {
		        var data = new Array();
		        setNestedBeanPropertyValue(data, "idEquipamento", idEquipamento);
		    
			    var sdo = createServiceDataObject("lms.vol.gerencialAction.executeEnvioSms", "executeEnvioSms", data);
    		    xmit({serviceDataObjects:[sdo]});
		    }
		}
   }

   function executeEnvioSms_cb(data,error) {
      if (error != undefined) {
         alert(error);
      } else {
        showMessage(i18NLabel.getLabel("LMS-41018"), null, true);
      }  
   }
   
   */
   function executaRelatorio(operacao,tipo,chave) {
		setElementValue('idFrota',chave);
		setElementValue('codRelatorio',operacao);
		if (tipo == 'COLENT'){
			reportButtonScript('lms.vol.resumoOperacaoService', 'openPdf', document.forms[0]);
		} else {
			reportButtonScript('lms.vol.relatorioTotaisChamadasService', 'openPdf', document.forms[0]);
		}
	}
	 
	function programacaoColetas(nrFrota,nrIdentificador,idMeioTransporte){
		var url = null;
		url = "&nrFrota="+nrFrota+"&nrIdentificador="+nrIdentificador+"&idMeioTransporte="+idMeioTransporte;
		parent.parent.redirectPage('coleta/programacaoColetasVeiculos.do?cmd=main'+url);
	} 
	 
	
	var tabGroup = getTabGroup(this.document);
	var abaDetalhamento = tabGroup.getTab("det");
	function selectTabDetalhamento(nrFrota,nrIdentificador,idMeioTransporte){

		abaDetalhamento.getElementById("idMeioTransporte").value = idMeioTransporte;
		abaDetalhamento.getElementById("frotaPlaca").value = nrFrota + ' ' + nrIdentificador;
		
		tabGroup.setDisabledTab("det", false);
		tabGroup.selectTab('det');
		
	} 	
	 
    /**
    * Função chamada no onPopulateRow da grid que controla as imagens da grid na paginação e/ou no findPaginated
    */
      var frota;
      
      function populateRow(tr,data) { 
         var statusFrota = getNestedBeanPropertyValue(data,"status");
         var statusEnt = getNestedBeanPropertyValue(data,"entregas.status");
         var statusCol = getNestedBeanPropertyValue(data,"coletas.status");         
         var tratativas = getNestedBeanPropertyValue(data,"tratativas");         
         var quebra = getNestedBeanPropertyValue(data,"quebra");
         var totais = getNestedBeanPropertyValue(data,"totais");
         var ligacoesPendentes = getNestedBeanPropertyValue(data,"ligacoesPendentes");
         var totalColetasSemInteracaoVol = getNestedBeanPropertyValue(data,"totalColetasSemInteracaoVol");
         var nrFrota = getNestedBeanPropertyValue(data,"meioTransporte2.nrFrota");
         var nrIdentificador = getNestedBeanPropertyValue(data,"meioTransporte.nrIdentificador");
         var idMeioTransporte = getNestedBeanPropertyValue(data,"idMeioTransporte"); 
         var idEquipamento = getNestedBeanPropertyValue(data,"idEquipamento"); 
         var dsNumero = getNestedBeanPropertyValue(data,"dsNumero"); 
         var coletasSemRetorno = getNestedBeanPropertyValue(data,"coletasSemRetorno"); 
         var entTotais = getNestedBeanPropertyValue(data,"entregas.total");
         var colTotais = getNestedBeanPropertyValue(data,"coletas.total");         
         var chave = idMeioTransporte;
         
           
         var IMAGEM_CELULAR = '1';
		 var BANDEIRA_BRANCA = '2';
		 var BANDEIRA_VERDE = '3';
		 var BANDEIRA_VERMELHA = '4';
		 var BANDEIRA_AMARELA = '5';
		 var BANDEIRA_AZUL = '6';
		
         frota = getNestedBeanPropertyValue(data,"frota");
		 
         setElementValue('ligacoesPendentes',ligacoesPendentes);

         setElementValue('totalColetasSemInteracaoVol', totalColetasSemInteracaoVol);

         setElementValue('totalColetas', colTotais);

         colTotais
         
         if(tr.rowIndex == 0) {
         	tr.style.height = '0';
         	return; 
         }
         
         //Se a linha for uma quebra
         if(quebra != undefined && quebra != '') {                  	         	
         	for(var i = tr.children.length - 1; i > 0; i--)
         		tr.removeChild(tr.children[i]);       	
         	
         	tr.children[0].innerHTML = "<b>" + quebra + "</b>";
         	tr.children[0].style.color = "#FFFFFF";
       		tr.children[0].style.backgroundColor = "#FF6600";
       		tr.children[0].align = 'center';         
         	tr.children[0].colSpan = '14';         	
         	tr.children[0].width = '';    
         	         	
         	return;
         }  
         
         //Se for a linha de totais
         if(totais != undefined && totais != '') {         	
         	tr.children[0].innerHTML = i18NLabel.getLabel("totais") + ':';
         	tr.children[0].style.color = "#FFFFFF";
			tr.children[2].innerHTML = '';
			tr.children[3].innerHTML = '';
			tr.children[6].innerHTML = '';
            tr.children[10].innerHTML = '';

         	for(var i = tr.children.length - 1; i >= 0; i--) {        
	       		tr.children[i].style.backgroundColor = "#FF6600";
	       		tr.children[i].align = 'center';                  	         	
         	}
         	         	
         	return;         
         }
	
		 //Tira a imagem dos campos que estiverem vazios
         if (statusFrota == undefined || statusFrota == '') {
            tr.children[0].innerHTML = "<NOBR></NOBR>";
         }
         if (tratativas == undefined || tratativas == '') {
            tr.children[3].innerHTML = "<NOBR></NOBR>";
         }
         if (statusEnt == undefined || statusEnt == '') {
            tr.children[5].innerHTML = "<NOBR></NOBR>";
         }
         if (statusCol == undefined || statusCol == '') {
            tr.children[10].innerHTML = "<NOBR></NOBR>";
         }                  
        
        var param = '&sgFilial=' + getElementValue("sgFilial") + '&idMeioTransporte=' + idMeioTransporte + '&dsNumero=' + dsNumero;
        
        //Cria as âncoras
        tr.children[2].innerHTML = tr.children[2].innerHTML.replace("idMeioTransporte","idMeioTransporte=" + idMeioTransporte);
        tr.children[3].innerHTML = tr.children[3].innerHTML.replace("main","main" + param + '&isTratativa=true' );
       // tr.children[13].innerHTML = tr.children[13].innerHTML.replace("<A", "<A href='#' name= '" + colTotais + "'");
        tr.children[13].innerHTML = "<A href=\"javascript:programacaoColetas('" + colTotais  + "','" + nrFrota + "','" + nrIdentificador + "','" + idMeioTransporte + "');\" onclick=\"programacaoColetas('" + nrFrota + "','" + nrIdentificador + "','" + idMeioTransporte + "');\">" + colTotais + "</A>";
        
        //Altera a coluna do status da frota 
         switch(statusFrota) {
            case IMAGEM_CELULAR : 
		 	     tr.children[0].innerHTML = getNestedBeanPropertyValue(data,"ligacoes") + ' ' + 
		 				         tr.children[0].innerHTML.replace("bandeira_vermelha_piscante.gif","celular.gif") + '<br>(' +
		 				         getNestedBeanPropertyValue(data,"hr_ligacoes") + ')';
		 	     tr.children[0].innerHTML = tr.children[0].innerHTML.replace("main","main" + param);
		 	     blPlay = true;
		 	     break;
		 	case BANDEIRA_BRANCA : 
		 		tr.children[0].innerHTML = tr.children[0].innerHTML.replace("bandeira_vermelha_piscante.gif","bandeira_branca.gif"); 
		 	//	document.getElementById('gerencial:' + tr.rowIndex + '.istatus_href').onclick = '';
		 	    tr.children[0].innerHTML = tr.children[0].innerHTML.replace("main","main&bRed=true" + param);
		 	    break;
		 	case BANDEIRA_VERDE :
		 		tr.children[0].innerHTML = tr.children[0].innerHTML.replace("bandeira_vermelha_piscante.gif","bandeira_verde.gif"); 
		 	//	document.getElementById('gerencial:' + tr.rowIndex + '.istatus_href').onclick = '';
		 	    tr.children[0].innerHTML = tr.children[0].innerHTML.replace("main","main&bRed=true" + param);
		 		break;
		 	case BANDEIRA_VERMELHA :
		 		tr.children[0].innerHTML = tr.children[0].innerHTML.replace("main","main&bRed=true" + param);
		 		break;			
		 }

		 //Altera a coluna do Status da Entrega
		 switch(statusEnt) {
		    case BANDEIRA_AMARELA :
		 	    tr.children[6].innerHTML = tr.children[6].innerHTML.replace("bandeira_vermelha_piscante.gif","bandeira_amarela.gif"); 
		 	    tr.children[6].innerHTML = tr.children[6].innerHTML.replace("main","main&bRed=true" + param);
		 	    break;
		    case BANDEIRA_BRANCA :
		 		tr.children[6].innerHTML = tr.children[6].innerHTML.replace("bandeira_vermelha_piscante.gif","bandeira_branca.gif"); 
		 		tr.children[6].innerHTML = tr.children[6].innerHTML.replace("main","main&bRed=true" + param);
		 		break;
		 	case BANDEIRA_VERDE :
		 		tr.children[6].innerHTML = tr.children[6].innerHTML.replace("bandeira_vermelha_piscante.gif","bandeira_verde.gif"); 
		 		tr.children[6].innerHTML = tr.children[6].innerHTML.replace("main","main&bRed=true" + param);
		 		break;
		 	case BANDEIRA_VERMELHA :
		 		tr.children[6].innerHTML = tr.children[6].innerHTML.replace("main","main&bRed=true" + param);
		 		break;				
		 }
		 
		 /* Coluna de totais */
         if ( (entTotais > 0) || (colTotais > 0) ) { 
            tr.children[7].innerHTML = '<A href="#" onClick=javascript:executaRelatorio(5,"COLENT",' + chave + ')>' + tr.children[7].innerHTML + '</A>'; 
         }
          
         tr.children[1].innerHTML = "<A href=\"#\" onClick=javascript:selectTabDetalhamento('" + nrFrota + "','" + nrIdentificador + "','" + idMeioTransporte + "')>" + frota + "</A>";
		 //Altera a coluna do Status da Coleta
		 switch(statusCol) {
		    case BANDEIRA_AMARELA :
		 	    tr.children[10].innerHTML = tr.children[10].innerHTML.replace("bandeira_vermelha_piscante.gif","bandeira_amarela.gif"); 
		 	    tr.children[10].innerHTML = tr.children[10].innerHTML.replace("main","main&bRed=true" + param);
		 	    break;
		    case BANDEIRA_BRANCA :
		 	    tr.children[10].innerHTML = tr.children[10].innerHTML.replace("bandeira_vermelha_piscante.gif","bandeira_branca.gif"); 
		 	    tr.children[10].innerHTML = tr.children[10].innerHTML.replace("main","main&bRed=true" + param);
		 	    break;
            case BANDEIRA_VERDE :
 			    tr.children[10].innerHTML = tr.children[10].innerHTML.replace("bandeira_vermelha_piscante.gif","bandeira_verde.gif"); 
 			    tr.children[10].innerHTML = tr.children[10].innerHTML.replace("main","main&bRed=true" + param);
 			    break;
		    case BANDEIRA_AZUL : 
		 //      document.getElementById('gerencial:' + tr.rowIndex + '.coletas.istatus_href').onclick = 'javascript:enviaColetas(" + idEquipamento + ");';
		       
		//       tr.children[10].innerHTML = tr.children[10].innerHTML.replace("title=Status", "title=" + imageLabel);
			//   tr.children[10].innerHTML = tr.children[10].innerHTML.replace("<A", "<A href='javascript:enviaColetas(" + idEquipamento + ");'");
		 	   tr.children[10].innerHTML = tr.children[10].innerHTML.replace("bandeira_vermelha_piscante.gif","bandeira_azul.gif ");
		 		break;
		 	case BANDEIRA_VERMELHA :
		 		tr.children[10].innerHTML = tr.children[10].innerHTML.replace("main","main&bRed=true" + param);
		 		break;												    
         }	
      }  
      function myOnPageLoad() {
         		
        var gridHTML = new Array();
        gridHTML.push("<TABLE>");
        gridHTML.push("<THEAD><TR><TH class=FmSep id=gerencial.header.istatus width='35' rowspan='2'>" + i18NLabel.getLabel("status") + "</TH>");
        gridHTML.push("<TH class=FmSep id=gerencial.header.frota width='110' rowspan='2' >" + i18NLabel.getLabel("frotaPlaca") + "</TH>");
        gridHTML.push("<TH class=FmSep id=gerencial.header.tratativas width='35' rowspan='2' >" + i18NLabel.getLabel("tratativas") + "</TH>");
        gridHTML.push("<TH class=FmSep id=gerencial.header.eficiencia width='39' rowspan='2' >" + i18NLabel.getLabel("eficienciaAndamento") + "</TH>");
        gridHTML.push("<TH class=FmSep id=gerencial.header.frequencia width='52' rowspan='2' >" + i18NLabel.getLabel("freqAndamento") + "</TH>");
		gridHTML.push("<TH class=FmSep id=gerencial.header.entregas.x1 colspan='4' >" +i18NLabel.getLabel("entregas") + "</TH>");
		gridHTML.push("<TH class=FmSep id=gerencial.header.entregas.x2 colspan='5'>" + i18NLabel.getLabel("coletas") + "</TH>");
		gridHTML.push("<TR>");
		gridHTML.push("<TH class=FmSep id=gerencial.header.entregas.istatus width='35'>" + i18NLabel.getLabel("status") + "</TH>");
		gridHTML.push("<TH class=FmSep id=gerencial.header.entregas.manif width='90'>" + i18NLabel.getLabel("controleCarga") + "</TH>");
		gridHTML.push("<TH class=FmSep id=gerencial.header.entregas.reali width='55'>" + i18NLabel.getLabel("reali") + "</TH>");
		gridHTML.push("<TH class=FmSep id=gerencial.header.entregas.total width='54'>" + i18NLabel.getLabel("total") + "</TH>");
		gridHTML.push("<TH class=FmSep id=gerencial.header.coletas.istatus width='35'>" + i18NLabel.getLabel("status")+ "</TH>");
		gridHTML.push("<TH class=FmSep id=gerencial.header.coletas.autom width='53'>" + i18NLabel.getLabel("autom")+ "</TH>");
		gridHTML.push("<TH class=FmSep id=gerencial.header.coletas.reali width='53'>" + i18NLabel.getLabel("reali")+ "</TH>");
		gridHTML.push("<TH class=FmSep id=gerencial.header.coletas.total width='53'>" + i18NLabel.getLabel("total")+ "</TH>");
		gridHTML.push("</TR>");
		gridHTML.push("<TH style='BACKGROUND-COLOR: #e7e7e7' width=13></TH></TR></THEAD>");
		gridHTML.push("<TBODY></TBODY>");
		gridHTML.push("</TABLE>");
        gerencialGridDef.replaceHeader(gridHTML.join(''));

        onPageLoad();
        
      }
 
</script>