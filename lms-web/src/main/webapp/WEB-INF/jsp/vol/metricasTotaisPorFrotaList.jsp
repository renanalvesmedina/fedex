<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script>

	var dadosIniciais = new Array();
	function fillDatasDefault(){
		montaGridHeader();
		onPageLoad();
		var sdo = createServiceDataObject("lms.vol.metricasTotaisPorFrotaAction.retornaDatasDefault", "fillDatasDefault", null);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function fillDatasDefault_cb(data, error){
		if (error != undefined){
			alert(error);
		} else {
			dadosIniciais = data;
			setaDadosIniciais();
		}
	}
	
    
	function montaGridHeader(){
		var gridHTML = new Array();
		gridHTML.push("<TABLE class=Form id=rFrota.headerTable style='TABLE-LAYOUT: fixed' cellSpacing=2 cellPadding=3 width='100%' border=0>");
		gridHTML.push("<THEAD>");
		gridHTML.push("<TR>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.frota width=50 rowSpan=3>" + i18NLabel.getLabel("frota") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.Totais width=150 colspan=3>" + i18NLabel.getLabel("totais") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.Volumes width=150 colSpan=3>" + i18NLabel.getLabel("volumes") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.NaoRealizadas width=150 colSpan=3>" + i18NLabel.getLabel("naoRealizadas") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.NaoBaixados width=150 colSpan=3>" + i18NLabel.getLabel("naoBaixadas") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.Chamadas width=150 colSpan=3>" + i18NLabel.getLabel("chamadas") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.Recusas width=210 colSpan=3>" + i18NLabel.getLabel("recusas") + "</TH>");
		
		gridHTML.push("<TH class=FmSep id=rFrota.header.coletasAutomaticas width=80 rowSpan=3>" + i18NLabel.getLabel("coletasAutomaticas") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.colTrocaFrotas width=80 rowSpan=3>" + i18NLabel.getLabel("colTrocaFrotas") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.reentregas width=80 rowSpan=3>" + i18NLabel.getLabel("reentregas") + "</TH>");
				
		gridHTML.push("<TR>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.entTotais width=50 rowSpan=2>" + i18NLabel.getLabel("ent") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.colTotais width=50 rowSpan=2>" + i18NLabel.getLabel("col") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.totTotais width=50 rowSpan=2>" + i18NLabel.getLabel("total") + "</TH>");
		
		gridHTML.push("<TH class=FmSep id=rFrota.header.entVolumes width=50 rowSpan=2>" + i18NLabel.getLabel("ent") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.colVolumes width=50 rowSpan=2>" + i18NLabel.getLabel("col") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.totVolumes width=50 rowSpan=2>" + i18NLabel.getLabel("total") + "</TH>");
		
		gridHTML.push("<TH class=FmSep id=rFrota.header.entNaoRealizadas width=50 rowSpan=2>" + i18NLabel.getLabel("ent") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.colNaoRealizadas width=50 rowSpan=2>" + i18NLabel.getLabel("col") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.totNaoRealizadas width=50 rowSpan=2>" + i18NLabel.getLabel("total") + "</TH>");
		
		gridHTML.push("<TH class=FmSep id=rFrota.header.entNaoBaixados width=50 rowSpan=2>" + i18NLabel.getLabel("ent") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.colNaoBaixados width=50 rowSpan=2>" + i18NLabel.getLabel("col") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.totNaoBaixados width=50 rowSpan=2>" + i18NLabel.getLabel("total") + "</TH>");
		
		gridHTML.push("<TH class=FmSep id=rFrota.header.entChamadas width=50 rowSpan=2>" + i18NLabel.getLabel("ent") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.colChamadas width=50 rowSpan=2>" + i18NLabel.getLabel("col") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.totChamadas width=50 rowSpan=2>" + i18NLabel.getLabel("total") + "</TH>");
		
		gridHTML.push("<TH class=FmSep id=rFrota.header.entRecusas width=70 rowSpan=2>" + i18NLabel.getLabel("ent") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.tratadas width=200 colspan=2>" + i18NLabel.getLabel("tratadas") + "</TH>");
		gridHTML.push("<TR>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.tratadasReentregas width=70>" + i18NLabel.getLabel("reentregas") + "</TH>");
		gridHTML.push("<TH class=FmSep id=rFrota.header.tratadasDevolucao width=70>" + i18NLabel.getLabel("devolucao") + "</TH>");
				
		gridHTML.push("</TR></TR></TR></THEAD><TBODY></TBODY></TABLE>");
		
		rFrotaGridDef.replaceHeader(gridHTML.join(''));
		//mantem a barra horizontal fixa
		document.getElementById('rFrota.div').style.height = 262;
	}

	
	function setaDadosIniciais(){
		setElementValue('dataFinal',setFormat(document.getElementById("dataFinal"),dadosIniciais.atual));
		setElementValue('dataInicial',setFormat(document.getElementById("dataInicial"),dadosIniciais.mes));
		setElementValue('filial.idFilial',dadosIniciais.filial.idFilial);
		setElementValue('filial.sgFilial',dadosIniciais.filial.sgFilial);
		setElementValue('filial.pessoa.nmFantasia',dadosIniciais.filial.pessoa.nmFantasia);
		
		setElementValue('sgFilial',dadosIniciais.filial.sgFilial);
	}
</script>
<adsm:window service="lms.vol.metricasTotaisPorFrotaAction" onPageLoad="fillDatasDefault">
	<adsm:form  action="/vol/metricasTotaisPorFrota">
	
		<adsm:hidden property="tipoRelatorio"/>
		<adsm:hidden property="codRelatorio"/>
		<adsm:hidden property="idFrota"/>		
		<adsm:hidden property="sgFilial"/>
		<adsm:hidden property="tpAcesso" serializable="false" value="F"/>
		<adsm:lookup label="filial" labelWidth="110" width="650"
		             property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.metricasTotaisPorFrotaAction.findLookupFilialByUsuarioLogado" 
		             dataType="text"
		             size="3" 
		             maxLength="3"
		             required="true"
		             exactMatch="true"
		             minLengthForAutoPopUpSearch="3">
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia" />
        	<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" />
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true"/>
        </adsm:lookup>
		
		
		<adsm:hidden property="dsNome"/>
	   	<adsm:lookup label="grupoFrota" labelWidth="110" width="320"
		        property="grupo"
		        idProperty="idGrupoFrota"
		        criteriaProperty="dsNome"
		        action="/vol/manterGruposFrotas"
		        service="lms.vol.metricasTotaisPorFrotaAction.findLookupGruposFrotas"
		        dataType="text"  
				size="20" 
				maxLength="20"
				exactMatch="false" 
				minLengthForAutoPopUpSearch="5"
				afterPopupSetValue="buscaDadosFilial">
				
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
     
    		<adsm:propertyMapping relatedProperty="filial.idFilial" modelProperty="filial.idFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="filial.sgFilial" modelProperty="filial.sgFilial" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" blankFill="false" />
		</adsm:lookup>
		
		<adsm:hidden property="frotaPlaca"/> 
		<adsm:lookup label="meioTransporte" labelWidth="110" width="350" picker="false"
	             property="meioTransporte"
	             idProperty="idMeioTransporte"
	             criteriaProperty="nrFrota"
	             action="/contratacaoVeiculos/manterMeiosTransporte"
	             service="lms.vol.controleEquipamentosAction.findLookupMeioTransporte" 
	             dataType="text"
	             size="8" 
	             maxLength="6"
	             exactMatch="true" >
		         <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			     <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			     <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
	             <adsm:propertyMapping relatedProperty="meioTransporte2.nrIdentificador" modelProperty="nrIdentificador" />
       
            <!--  Criteria por nrIdentificador -->        
            <adsm:lookup 
	             property="meioTransporte2"
	             idProperty="idMeioTransporte"
	             criteriaProperty="nrIdentificador"
	             action="/contratacaoVeiculos/manterMeiosTransporte"
	             service="lms.vol.controleEquipamentosAction.findLookupMeioTransporte" 
	             dataType="text"
	             size="30" 
	             maxLength="25"
	             exactMatch="false"
	             minLengthForAutoPopUpSearch="5">
	             
	            <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
		        <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
		        <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
	            <adsm:propertyMapping relatedProperty="meioTransporte.nrFrota" modelProperty="nrFrota" />
                <adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte" modelProperty="idMeioTransporte" />               
        	</adsm:lookup>
       </adsm:lookup>		

	  <adsm:range label="periodo" labelWidth="110" width="320" required="true">
			<adsm:textbox dataType="JTDate" property="dataInicial" />
			<adsm:textbox dataType="JTDate" property="dataFinal" />
	  </adsm:range>
	  
	  <adsm:combobox property="tpPossuiCelular" domain="DM_SIM_NAO" label="possuiCelular" labelWidth="110" width="135"/> 
	  <adsm:checkbox property="tpExcel" label="gerarExcel" labelWidth="110" width="15"/> 
		
	  <adsm:buttonBar freeLayout="true">
		    <adsm:button caption="consultar" buttonType="findButton" onclick="executaConsulta();"/>
			<adsm:button caption="limpar" buttonType="cleanButton" onclick="cleanButton();"/>
	</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idFrota" property="rFrota" 
	           selectionMode="none" onRowClick="disableClick" onPopulateRow="populateRow"
			   service="lms.vol.metricasTotaisPorFrotaAction.findPaginatedTotaisPorFrota" 
			   rowCountService="lms.vol.metricasTotaisPorFrotaAction.getRowCountTotaisPorFrota" disableMarkAll="true" rows="8"	
			   unique="true" scrollBars="horizontal" >
		<adsm:gridColumn align="left" property="frota" title="frota" width="50" />
		
		<adsm:gridColumn dataType="integer" property="entTotais" mask="###.###.###" title="totais" width="50" />
		<adsm:gridColumn dataType="integer" property="colTotais" mask="###.###.###" title="" width="50" />
		<adsm:gridColumn dataType="integer" property="totTotais" mask="###.###.###" title="" width="50" />

		<adsm:gridColumn dataType="integer" property="entVolumes" mask="###.###.###" title="volumes" width="50" />
		<adsm:gridColumn dataType="integer" property="colVolumes" mask="###.###.###" title="" width="50" />
		<adsm:gridColumn dataType="integer" property="totVolumes" mask="###.###.###" title="" width="50" />
		
        <adsm:gridColumn dataType="integer" property="entNaoRealizadas" mask="###.###.###" title="naoRealizadas" width="50" />
		<adsm:gridColumn dataType="integer" property="colNaoRealizadas" mask="###.###.###" title="" width="50" />
		<adsm:gridColumn dataType="integer" property="totNaoRealizadas" mask="###.###.###" title="" width="50" />
		
        <adsm:gridColumn dataType="integer" property="entNaoBaixados" mask="###.###.###" title="naoBaixadas" width="50" />
		<adsm:gridColumn dataType="integer" property="colNaoBaixados" mask="###.###.###" title="" width="50" />
		<adsm:gridColumn dataType="integer" property="totNaoBaixados" mask="###.###.###" title="" width="50" />

		<adsm:gridColumn dataType="integer" property="entChamadas" mask="###.###.###" title="chamadas" width="50" />
		<adsm:gridColumn dataType="integer" property="colChamadas" mask="###.###.###" title="" width="50" />
		<adsm:gridColumn dataType="integer" property="totChamadas" mask="###.###.###" title="" width="50" />
		
		<adsm:gridColumn dataType="integer" property="entRecusas" mask="###.###.###" title="recusas" width="70" />
		<adsm:gridColumn dataType="integer" property="tratadasReentregas" mask="###.###.###" title="" width="70" />
		<adsm:gridColumn dataType="integer" property="tratadasDevolucao" mask="###.###.###" title="" width="70" />
		
		<adsm:gridColumn dataType="integer" property="coletasAutomaticas" mask="###.###.###" title="coletasAutomaticas" width="80" />
		<adsm:gridColumn dataType="integer" property="colTrocaFrotas" mask="###.###.###" title="colTrocaFrotas" width="80" />
		<adsm:gridColumn dataType="integer" property="reentregas" mask="###.###.###" title="reentregas" width="80" />		
		
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>
		
	<adsm:i18nLabels>
	    <adsm:include key="meioTransporte"/>
		<adsm:include key="frota"/>
		<adsm:include key="volumes"/>
		<adsm:include key="naoRealizadas"/>
		<adsm:include key="naoBaixadas"/>
		<adsm:include key="chamadas"/>
		<adsm:include key="recusas"/>
		<adsm:include key="tratadas"/>
		<adsm:include key="coletasAutomaticas"/>
		<adsm:include key="colTrocaFrotas"/>
		<adsm:include key="reentregas"/>
		<adsm:include key="devolucao"/>
		<adsm:include key="total"/>
		<adsm:include key="totais"/>
		<adsm:include key="ent"/>
		<adsm:include key="col"/>
	</adsm:i18nLabels>
</adsm:window>
<script>
		
	function disableClick() {
		return false;
	}
	
	function buscaDadosFilial(data) {
	    setElementValue("filial.sgFilial",data.sgFilial);
	    var dados = new Array();
	    
		setNestedBeanPropertyValue(dados,"sgFilial",data.sgFilial);
		var sdo = createServiceDataObject ("lms.vol.metricasTotaisPorFrotaAction.findLookupFilialByUsuarioLogado", "populaLookupFilial", dados);
        xmit({serviceDataObjects:[sdo]});
        return 
	}
	
	document.getElementById("filial.pessoa.nmFantasia").serializable = true;
	function populaLookupFilial_cb(data,error) {
	   if (error != undefined) {
	      alert(error)
	   } else {
	      setElementValue("filial.idFilial",getNestedBeanPropertyValue(data[0], "idFilial"));
		  setElementValue("filial.pessoa.nmFantasia",getNestedBeanPropertyValue(data[0],"pessoa.nmFantasia"));
	   }
	}
	
	function executaConsulta(){
		if (getElementValue('tpExcel') == true){
		
			var siglaFilial = getElementValue('filial.sgFilial');
			setElementValue('sgFilial',siglaFilial);
			
			var grupo = getElementValue('grupo.dsNome');
			setElementValue('dsNome',grupo);
			
			var nrFrota = getElementValue('meioTransporte.nrFrota');
			var nrIdentificador = getElementValue('meioTransporte2.nrIdentificador');
			var frotaPlaca = nrFrota + " " + nrIdentificador;
			setElementValue('frotaPlaca', frotaPlaca);
			
			
//			setElementValue('nrIdentificador', nrIdentificador);
		
			reportButtonScript('lms.vol.totaisPorFrotaService', 'openPdf', document.forms[0]);
		} else {		
			findButtonScript('rFrota', document.forms[0]);
		} 
	}
	
	function cleanButton(){
		cleanButtonScript(this.document);
		setaDadosIniciais();
	}
	
	function executaRelatorio(operacao,tipo,chave) {
		setElementValue('idFrota',chave);
		setElementValue('codRelatorio',operacao);
		if (tipo == 'ENT'){
			reportButtonScript('lms.vol.relatorioDeEntregasService', 'openPdf', document.forms[0]);
		} else if (tipo == 'COL'){
			reportButtonScript('lms.vol.relatorioDeColetasService', 'openPdf', document.forms[0]);
		} else {
			reportButtonScript('lms.vol.relatorioTotaisChamadasService', 'openPdf', document.forms[0]);
		}
	}
	
	
	
	function redirecionaRecusas(coluna,id){
		var dataInicial = getElementValue('dataInicial');
		var dataFinal = getElementValue('dataFinal');
		var filial = getElementValue('filial.idFilial');

		var url = '&frota=' + id + '&dataIni=' + dataInicial + '&dataFin=' + dataFinal + '&filial=' + filial + '&coluna=' + coluna;
		parent.parent.redirectPage('vol/recusa.do?cmd=main'+url);
	}
	
	
	/**
      * Fun��o chamada no onPopulateRow da grid 
      */
      var frota;
      var countRows = 0;
      function populateRow(tr,data) { 
         var tipo  = getNestedBeanPropertyValue(data,"tipo");
         var chave = getNestedBeanPropertyValue(data,"idFrota");
         var frota = getNestedBeanPropertyValue(data,"frota");
         
         var entTotais = getNestedBeanPropertyValue(data,"entTotais");
		 var colTotais = getNestedBeanPropertyValue(data,"colTotais");
		 var totTotais = getNestedBeanPropertyValue(data,"totTotais");

         var entNaoRealizadas = getNestedBeanPropertyValue(data,"entNaoRealizadas");
  		 var colNaoRealizadas = getNestedBeanPropertyValue(data,"colNaoRealizadas");
		 var totNaoRealizadas = getNestedBeanPropertyValue(data,"totNaoRealizadas");
		
         var entNaoBaixados = getNestedBeanPropertyValue(data,"entNaoBaixados");
		 var colNaoBaixados = getNestedBeanPropertyValue(data,"colNaoBaixados");
		 var totNaoBaixados = getNestedBeanPropertyValue(data,"totNaoBaixados");

 		 var entChamadas = getNestedBeanPropertyValue(data,"entChamadas");
	     var colChamadas = getNestedBeanPropertyValue(data,"colChamadas");
		 var totChamadas = getNestedBeanPropertyValue(data,"totChamadas");
		
		 var entRecusas = getNestedBeanPropertyValue(data,"entRecusas");
		 var tratadasReentregas = getNestedBeanPropertyValue(data,"tratadasReentregas");
		 var tratadasDevolucao = getNestedBeanPropertyValue(data,"tratadasDevolucao");

		 var coletasAutomaticas = getNestedBeanPropertyValue(data,"coletasAutomaticas");
		 var colTrocaFrotas = getNestedBeanPropertyValue(data,"colTrocaFrotas");
		 var reentregas	= getNestedBeanPropertyValue(data,"reentregas"); 
         
         //Se for a linha de totais
         if(frota == i18NLabel.getLabel("total")) {      
            for(var i = tr.children.length - 1; i >= 0; i--) {        
	       		tr.children[i].style.backgroundColor = "#A5B5B6";
	       		tr.children[i].align = 'right';                   	         	
         	}         	         	
         	countRows ++;  
         	tr.children[0].align = 'left';  
         	return;
         }  
         
         /* Coluna de totais */
         if (entTotais > 0 ) {
            tr.children[1].innerHTML = '<A href="#" onClick=javascript:executaRelatorio(1,"ENT",' + chave + ')>' + tr.children[1].innerHTML + '</A>'; 
         }
         if (colTotais > 0 ) {
            tr.children[2].innerHTML = '<A href="#" onClick=javascript:executaRelatorio(1,"COL",' + chave + ')>' + tr.children[2].innerHTML + '</A>'; 
         }
         /*
         if (totTotais > 0 ) {
            tr.children[3].innerHTML = '<A href="#" onClick=javascript:executaRelatorio("TOTAIS","TOT",' + chave + ')>' + tr.children[3].innerHTML + '</A>'; 
         }
         */
         /* N�o realizadas */
         if (entNaoRealizadas > 0 ) {
            tr.children[7].innerHTML = '<A href="#" onClick=javascript:executaRelatorio(2,"ENT",' + chave + ')>' + tr.children[7].innerHTML + '</A>'; 
         }
         if (colNaoRealizadas > 0 ) {
            tr.children[8].innerHTML = '<A href="#" onClick=javascript:executaRelatorio(2,"COL",' + chave + ')>' + tr.children[8].innerHTML + '</A>'; 
         }
         /*
         if (totNaoRealizadas > 0 ) {
            tr.children[9].innerHTML = '<A href="#" onClick=javascript:executaRelatorio("NAO_REALIZADAS","TOT",' + chave + ')>' + tr.children[9].innerHTML + '</A>'; 
         }
         */
         /* N�o baixados */
         if (entNaoBaixados > 0 ) {
            tr.children[10].innerHTML = '<A href="#" onClick=javascript:executaRelatorio(3,"ENT",' + chave + ')>' + tr.children[10].innerHTML + '</A>'; 
         }
         if (colNaoBaixados > 0 ) {
            tr.children[11].innerHTML = '<A href="#" onClick=javascript:executaRelatorio(3,"COL",' + chave + ')>' + tr.children[11].innerHTML + '</A>'; 
         }
         /*
         if (totNaoBaixados > 0 ) {
            tr.children[12].innerHTML = '<A href="#" onClick=javascript:executaRelatorio("NAO_BAIXADOS","TOT",' + chave + ')>' + tr.children[12].innerHTML + '</A>'; 
         }
         */
         /* Chamadas */
         if (entChamadas > 0 ) {
            tr.children[13].innerHTML = '<A href="#" onClick=javascript:executaRelatorio(1,"CHA",' + chave + ')>' + tr.children[13].innerHTML + '</A>'; 
         }
         if (colChamadas > 0 ) {
            tr.children[14].innerHTML = '<A href="#" onClick=javascript:executaRelatorio(2,"CHA",' + chave + ')>' + tr.children[14].innerHTML + '</A>'; 
         }
         
         if (totChamadas > 0 ) {
            tr.children[15].innerHTML = '<A href="#" onClick=javascript:executaRelatorio(3,"CHA",' + chave + ')>' + tr.children[15].innerHTML + '</A>'; 
         }
		 
         /* Recusas */
                 
         if (entRecusas > 0 ) { 
            tr.children[16].innerHTML = '<A href="#" onClick=javascript:redirecionaRecusas(undefined,' + chave + ')>' + tr.children[16].innerHTML + '</A>'; 
         }
         if (tratadasReentregas > 0 ) {
            tr.children[17].innerHTML = '<A href="#" onClick=javascript:redirecionaRecusas("REE",' + chave + ')>' + tr.children[17].innerHTML + '</A>'; 
         }
         if (tratadasDevolucao > 0 ) {
            tr.children[18].innerHTML = '<A href="#" onClick=javascript:redirecionaRecusas("DEV",' + chave + ')>' + tr.children[18].innerHTML + '</A>'; 
         }
  
		 /* Coletas automaticas */
         if (coletasAutomaticas > 0 ) {
            tr.children[19].innerHTML = '<A href="#" onClick=javascript:executaRelatorio(4,"COL",' + chave + ')>' + tr.children[19].innerHTML + '</A>'; 
         }
         /* Coletas com troca frota */
         if (colTrocaFrotas > 0 ) {
            tr.children[20].innerHTML = '<A href="#" onClick=javascript:executaRelatorio(5,"COL",' + chave + ')>' + tr.children[20].innerHTML + '</A>'; 
         }         
         /* Reentrega */
         if (reentregas > 0 ) {
            tr.children[21].innerHTML = '<A href="#" onClick=javascript:executaRelatorio(4,"ENT",' + chave + ')>' + tr.children[21].innerHTML + '</A>'; 
         }
      }	
      
   
</script>