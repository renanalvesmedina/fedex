<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
function customPageLoad(){
	onPageLoad_cb();
	if (document.getElementById("idFilialTemp").masterLink == 'true'){
		setElementValue("filial.idFilial", getElementValue("idFilialTemp"));
		document.getElementById("filial.pessoa.nmFantasia").masterLink = 'false';			
		setFocusOnFirstFocusableField();
		
		notifyElementListeners({e:document.getElementById("filial.idFilial")});
	} else {
		executeFunction("lms.portaria.informarChegadaTMSAction.onPageLoad", "returnCustomPageLoad", new Array());
	}
}

function returnCustomPageLoad_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	if (data != undefined) {
		idFilialLogado = getNestedBeanPropertyValue(data,"filial.idFilial");
		sgFilialLogado = getNestedBeanPropertyValue(data,"filial.sgFilial");
		nmFilialLogado = getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia");
		if (idFilialLogado != undefined &&
				sgFilialLogado != undefined &&
				nmFilialLogado != undefined) {
			setElementValue("filial.idFilial",idFilialLogado);
			setElementValue("filial.sgFilial",sgFilialLogado);
			setElementValue("filial.pessoa.nmFantasia",nmFilialLogado);
			notifyElementListeners({e:document.getElementById("filial.idFilial")});
		}
	}
}

function myRowClick(id){
	var data = {
			idManifestoTMS:id
		}	
	executeFunction("lms.portaria.informarChegadaTMSAction.montaConfirmacao","montaConfirmacao",data)
	return false;
}

function montaConfirmacao_cb(data,error){
	if (error != undefined) {
		alert(error);
		return false;
	}
	var r = confirma(data.msg);
	if( r ){
		validateManifestoTMS(data.idFilial,data.nrManifesto,data.idManifesto);
	}
}

function validateManifestoTMS(id,nr,idManifesto){
	var data = {
		idFilial:id,
		nrManifesto:nr,
		idManifesto:idManifesto
	}	
	executeFunction("lms.portaria.informarChegadaTMSAction.validateManifestoTMS","validateManifestoTMS",data)
}

function validateManifestoTMS_cb(data,error){
	if (error != undefined) {
		alert(error);
		return false;
	}
	refresh(data);
}

function refresh(data){
	var grid = getElement("gridInfChegada.dataTable").gridDefinition;
	grid.resetGrid();
	grid.onDataLoad_cb(data,null);
}

function executeFunction(f , r, d){
	var sdo = createServiceDataObject(f, r, d);
   	xmit({serviceDataObjects:[sdo]});
}

</script>
<adsm:window service="lms.portaria.informarChegadaTMSAction" onPageLoad="customPageLoad" >
	<adsm:form action="/portaria/informarChegadaTMS" idProperty="idManifestoTMS" >
  		<adsm:hidden property="idFilialTemp"/> 		
		<adsm:lookup 
			service="lms.portaria.informarChegadaTMSAction.findLookupFilial" 
			dataType="text"
			property="filial"  
			criteriaProperty="sgFilial" 
			label="filial" 
			size="3" 
			maxLength="3"
			width="50%" 
			action="/municipios/manterFiliais" 
			required="true"
			idProperty="idFilial" disabled="true">
	        	<adsm:propertyMapping 
	        		modelProperty="pessoa.nmFantasia" 
	        		relatedProperty="filial.pessoa.nmFantasia"/>
				<adsm:textbox 
					dataType="text" 
					property="filial.pessoa.nmFantasia" 
					size="30" 
					disabled="true" 
					serializable="false"/>	        		
        </adsm:lookup>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridInfChegada"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="gridInfChegada" idProperty="idManifestoTMS" unique="true" rows="10"
			selectionMode="none" defaultOrder="filialManifesto.sgFilial" onRowClick="myRowClick">
		<adsm:gridColumn title="veiculo" property="dsVeiculo" width="35%" />	
		<adsm:gridColumn title="filial" property="sgFilial" width="35%" />
		<adsm:gridColumn title="manifesto" property="nrManifesto" width="20%" dataType="integer"/>	
		<adsm:gridColumn title="qtdeCTOs" property="nrQtdCtos" width="10%" dataType="integer" />
	</adsm:grid>
		<adsm:buttonBar> 
			<adsm:button id="informarSaidas" caption="informarSaidasButton" action="/portaria/informarSaida" cmd="main" disabled="false">
				<adsm:linkProperty src="filial.idFilial" target="idFilialTemp" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="filial.pessoa.nmFantasia" />
			</adsm:button>
			<adsm:button caption="informarChegadasButton" id="informarChegadas" action="/portaria/informarChegada" cmd="main" disabled="false">
				<adsm:linkProperty src="filial.idFilial" target="idFilialTemp"/>
				<adsm:linkProperty src="filial.sgFilial" target="filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="filial.pessoa.nmFantasia" />
			</adsm:button>
		</adsm:buttonBar>
</adsm:window>
