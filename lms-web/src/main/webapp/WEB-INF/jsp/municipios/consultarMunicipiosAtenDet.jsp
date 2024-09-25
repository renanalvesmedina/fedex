<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.municipios.consultarMunicipiosAction">

	<adsm:form action="/municipios/consultarMunicipios" idProperty="idMunicipioFilial" height="160" service="lms.municipios.consultarMunicipiosAction.findByIdMunicipioFilial" onDataLoadCallBack="onDataLoadMunicipiosAtendidos"> 
	
		<adsm:textbox dataType="text" disabled="true" property="nomeEmpresa" label="empresa" maxLength="30" size="37" labelWidth="20%" width="30%"/>
		
		<adsm:textbox property="sgFilial" dataType="text" label="filial" labelWidth="20%" width="30%" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="nmFantasia" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="nmMunicipio" label="municipio" maxLength="30" size="37" labelWidth="20%" width="30%" disabled="true"/>
		<adsm:textbox dataType="text" property="nmMunicipioAlternativo" label="nomeAlternativo" maxLength="30" size="37" labelWidth="20%" width="30%" disabled="true"/>
		
		<adsm:textbox property="sgUnidadeFederativa" dataType="text" label="uf" labelWidth="20%" width="30%" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="nmUnidadeFederativa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="nmPais" label="pais" maxLength="30" size="37" labelWidth="20%" width="30%" disabled="true"/>
		
		<adsm:checkbox property="blDistrito" label="indDistrito" labelWidth="20%" width="30%" disabled="true"/>
		<adsm:textbox dataType="text" property="nmMunicipioDist" label="municDistrito" maxLength="30" size="37" labelWidth="20%" width="30%" disabled="true"/>

		
		<adsm:textbox dataType="integer" disabled="true" property="nrDistanciaChao" label="distanciaChao" maxLength="4" size="6" unit="km2" labelWidth="20%" width="30%"/>
		<adsm:textbox dataType="integer" disabled="true" property="nrDistanciaAsfalto" label="distanciaAsfalto" maxLength="4" size="6" unit="km2" labelWidth="20%" width="30%"/>
		<adsm:textbox dataType="integer" property="nrGrauDificuldade" label="grauDificuldade" maxLength="6" size="6" labelWidth="20%" width="80%" disabled="true" />
		<adsm:checkbox property="blRecebeColetaEventual" label="recebeColetaEventual" labelWidth="20%" width="30%" disabled="true"/>
		<adsm:checkbox property="blDificuldadeEntrega" label="dificuldadeEntrega" labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;" disabled="true"/>
		<adsm:checkbox property="blPadraoMcd" label="padraoMCD" labelWidth="20%" width="30%" disabled="true"/>
		<adsm:checkbox property="blRestricaoAtendimento" label="restricaoAtendimento" labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;" disabled="true"/>
		
		<adsm:section caption="atendimento"/>
		
		<adsm:listbox property="municipioFilialIntervCeps" optionProperty="cepInicialFinal"
				optionLabelProperty="cepInicialFinal" labelWidth="20%" width="30%" size="3" label="intervalosCEP" 
				boxWidth="140" />

		
		<adsm:listbox property="municipioFilialSegmentos" optionProperty="dsSegmentoMercado"
				optionLabelProperty="dsSegmentoMercado"
				labelWidth="20%" width="30%" size="3" label="segmentosMercado" boxWidth="140" /> 
		
		<adsm:listbox property="municipioFilialUfOrigems" optionProperty="siglaDescricaoUF"
				optionLabelProperty="siglaDescricaoUF"
				labelWidth="20%" width="30%" size="3" label="ufsOrigem" boxWidth="140" /> 

		<adsm:listbox property="municipioFilialFilOrigems" optionProperty="siglaDescricaoFil"
				optionLabelProperty="siglaDescricaoFil"
				labelWidth="20%" width="30%" size="3" label="filiaisOrigem" boxWidth="140" />

		<adsm:listbox property="municipioFilialCliOrigems" optionProperty="identificacaoNome"
				optionLabelProperty="identificacaoNome"
				labelWidth="20%" width="80%" size="3" label="remetentes" boxWidth="280" />
				
	</adsm:form>
		<adsm:grid onRowClick="retornaFalse" onPopulateRow="checkStatusAtendimento"  
				service="lms.municipios.consultarMunicipiosAction.findOperacaoServicoLocalVigenteByMunFilial" 
				idProperty="idOperacaoServicoLocaliza" property="operacaoServicoLocaliza" unique="true" 
				title="frequencia" scrollBars="both" gridHeight="100" showPagging="false"  
				showGotoBox="false" selectionMode="none" autoSearch="false">
		
		<adsm:gridColumn title="tipoOperacao" property="tpOperacao" width="100" isDomain="true"/>
		<adsm:gridColumn title="servico" property="servico.dsServico" width="150"/>
		<adsm:gridColumn title="tipoLocalizacao" property="tipoLocalizacaoMunicipio.dsTipoLocalizacaoMunicipio" width="150"/>
		<adsm:gridColumn title="tempoColeta" property="nrTempoColetaEmHoras" width="100" unit="h" dataType="integer"/>
		<adsm:gridColumn title="tempoEntrega" property="nrTempoEntregaEmHoras" width="100" unit="h" dataType="integer"/>
		
		
		<adsm:gridColumn title="atendimentosClientes" property="atendimentoGeral" image="/images/popup.gif" openPopup="true" link="/municipios/consultarMunicipios.do?cmd=listaClientesAtendidos" popupDimension="600,300" width="100" align="center" linkIdProperty="idOperacaoServicoLocaliza" />
		
		<adsm:gridColumn title="dom" property="blDomingo" width="50" renderMode="image-check"/>
		<adsm:gridColumn title="seg" property="blSegunda" width="50" renderMode="image-check" />
		<adsm:gridColumn title="ter" property="blTerca" width="50" renderMode="image-check" />
		<adsm:gridColumn title="qua" property="blQuarta" width="50" renderMode="image-check" />
		<adsm:gridColumn title="qui" property="blQuinta" width="50" renderMode="image-check" />
		<adsm:gridColumn title="sex" property="blSexta" width="50" renderMode="image-check" />
		<adsm:gridColumn title="sab" property="blSabado" width="50" renderMode="image-check" />
		
		<adsm:gridColumn title="indTaxaFluvial" property="blCobraTaxaFluvial" width="80" renderMode="image-check"/>
		<adsm:gridColumn title="aceitaFreteFOB" property="blAceitaFreteFob" width="110" renderMode="image-check"/>
		<adsm:buttonBar freeLayout="true"/>
	</adsm:grid>
</adsm:window>
<script>

function checkStatusAtendimento(tr, data) {
	if(getNestedBeanPropertyValue(data,"blAtendimentoGeral")=="true")
		tr.children[5].innerHTML = "<NOBR></NOBR>";
}	


function onDataLoadMunicipiosAtendidos_cb(data,exception){
    onDataLoad_cb(data,exception);
    var idMunicipioFilial = getElementValue("idMunicipioFilial");
    if (idMunicipioFilial != undefined && idMunicipioFilial != ''){
		var dataId = new Array();
		setNestedBeanPropertyValue(dataId, "idMunicipioFilial", idMunicipioFilial);
		operacaoServicoLocalizaGridDef.executeSearch(dataId);
	} else
		operacaoServicoLocalizaGridDef.resetGrid();
}

function retornaFalse(){
	return false;
}


	
</script>

