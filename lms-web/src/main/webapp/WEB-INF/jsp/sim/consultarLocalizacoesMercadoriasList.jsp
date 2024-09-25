<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarLocalizacaoDetalhadaWeb" service="lms.sim.consultarLocalizacoesMercadoriasAction">
	
	<adsm:grid property="doctoServico" idProperty="idDoctoServico" selectionMode="none" autoSearch="false"
	service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedConsultaLocalizacaoMercadoria"
	rowCountService="lms.sim.consultarLocalizacoesMercadoriasAction.getRowCountConsultaLocalizacaoMercadoria"
	unique="true" rows="14" scrollBars="horizontal" onRowClick="desabilitaAbaDetalhe" onDataLoadCallBack="verificaRowCount" gridHeight="320" onPopulateRow="populateRow">
		
		<adsm:gridColumn title="branco" property="bola" align="center" width="23" image="/images/bola_verde.gif" />
		
		<adsm:gridColumn title="documentoServico" property="tpDoctoServico" width="50" />
		 <adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="" property="sgFilialOrigem" width="80" />
			<adsm:gridColumn title="" property="nrDoctoServico" dataType="integer" mask="00000000" width="0" />
		</adsm:gridColumnGroup>	
		
		<adsm:gridColumn width="100" title="finalidade" property="finalidade" />
		<adsm:gridColumn width="60" title="filialDestino2" property="sgFilialDestino"/>
		<adsm:gridColumn width="110" title="dataEmissao" property="dhEmissao" align="center" dataType="JTDateTimeZone"/>
		<adsm:gridColumn width="100" title="dataPrevista" property="dtPrevista" align="center" dataType="JTDate"/>
		<adsm:gridColumn width="110" title="dataEntrega" property="dtEntrega" align="center" dataType="JTDateTimeZone"/>
		<adsm:gridColumn width="160" title="localizacao" property="dsLocMerc"/>
		<adsm:gridColumn width="100" title="notaFiscal" property="nf"  dataType="integer" mask="00000000"/>
		
		<adsm:gridColumn width="40"  title="identificacaoRemetente" property="remTpIdentificacao" />
		<adsm:gridColumn width="150" title="" property="remNrIdentificacao" align="right"/>
		<adsm:gridColumn width="180" title="remetente" property="remNmPessoa"/>	
		
		<adsm:gridColumn width="40"  title="identificacaoDestinatario" property="destTpIdentificacao"/>
		<adsm:gridColumn width="130" title="" property="destNrIdentificacao" align="right"/>
		<adsm:gridColumn width="180" title="destinatario" property="destNmPessoa"/>	
				
		<adsm:gridColumn width="40"  title="identificacaoConsignatario" property="consTpIdentificacao" />
		<adsm:gridColumn width="130" title="" property="consNrIdentificacao" align="right"/>
		<adsm:gridColumn width="180" title="consignatario" property="consNmPessoa"/>
		
		<adsm:gridColumn width="40"  title="identificacaoRedespacho" property="redesTpIdentificacao" />
		<adsm:gridColumn width="130" title="" property="redesNrIdentificacao" align="right"/>
		<adsm:gridColumn width="180" title="redespacho" property="redesNmPessoa"/>
		
		<adsm:gridColumn width="40"  title="identificacaoResponsavelFrete" property="respTpIdentificacao" />
		<adsm:gridColumn width="130" title="" property="respNrIdentificacao" align="right"/>
		<adsm:gridColumn width="180" title="responsavelFrete" property="respNmPessoa"/>
		<adsm:gridColumn width="100" title="dataAgendamento" property="dtAgen" align="center" dataType="JTDate"/>
		<adsm:buttonBar/>
	</adsm:grid>
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="0">
		<adsm:hidden property="idDoctoServicoSelecionado"/>
		<adsm:i18nLabels>
			<adsm:include key="LMS-10040"/>
		</adsm:i18nLabels>
	</adsm:form>
</adsm:window>
<script>

function populateRow(tr,data) { 
     var cor = getNestedBeanPropertyValue(data,"corBola");
     var indice = 0;
     if (cor == 'Vermelha'){
         tr.children[indice].innerHTML = tr.children[indice].innerHTML.replace("verde","vermelha"); 

     }else if (cor == 'Amarela')   
     	tr.children[indice].innerHTML = tr.children[indice].innerHTML.replace("verde","amarela");     
     else if (cor == undefined)	
     	tr.children[indice].innerHTML = "<NOBR></NOBR>";
}



function desabilitaAbaDetalhe(id){
	setElementValue("idDoctoServicoSelecionado", id);
	var tabGroup = getTabGroup(document);
	tabGroup.setDisabledTab("cad", false);
	tabGroup.selectTab("cad",{name:'tab_click'},true);
	
	return false;
}


function executaConsulta(){
		var tabGroup = getTabGroup(document);
		if(tabGroup.oldSelectedTab.properties.id == "doc"){
			var tabCons = tabGroup.getTab("doc");
			var searchValues = buildFormBeanFromForm(tabCons.getElementById("form_idDoctoServico"), 'LIKE_END'); 
			doctoServicoGridDef.executeSearch(searchValues, null, null, true);
		}	
}

function verificaRowCount_cb(data, errorMessage){
	 
			if(data.list != undefined){
				if (data.list.length == 1 && data.hasNextPage == "false" && data.hasPriorPage == "false"){
					var id = data.list[0].idDoctoServico;
					desabilitaAbaDetalhe(id);
				}
				
			}else if (data.list == undefined){
				alert(i18NLabel.getLabel("LMS-10040"));
				var tabGroup = getTabGroup(document);
				tabGroup.selectTab("doc",{name:'tab_click'},true);
				
			}
		
}		
		
		
		
</script>