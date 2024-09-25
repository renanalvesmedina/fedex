<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.manterProdutosAction">
	<adsm:form action="/expedicao/manterProdutos" idProperty="idProduto" onDataLoadCallBack="retornoForm" 
		service="lms.expedicao.manterProdutosAction.findById">
		
		<adsm:listbox label="categoria" property="produtoCategoriaProdutos" 
					  optionProperty="idProdutoCategoriaProduto"
					  labelWidth="19%" width="79%"
					  size="8" boxWidth="200" required="true">  
			<adsm:combobox property="categoriaProduto" 
					   optionLabelProperty="dsCategoriaProduto" 
					   renderOptions="true"
					   optionProperty="idCategoriaProduto" 
					   service="lms.expedicao.categoriaProdutoService.find" 
					   serializable="false"
					   onlyActiveValues="true"
					   />
		</adsm:listbox>
			
		<adsm:combobox property="naturezaProduto.idNaturezaProduto" 
					   optionLabelProperty="dsNaturezaProduto" 
					   optionProperty="idNaturezaProduto" 
					   service="lms.expedicao.naturezaProdutoService.find" 
					   label="naturezaProduto" 
					   required="true" 
					   onlyActiveValues="true"
					   labelWidth="19%" 
					   width="45%" 
		/>
		
		<adsm:combobox property="tipoProduto.idTipoProduto" 
					   optionLabelProperty="dsTipoProduto"
					   optionProperty="idTipoProduto"
					   required="true"
					   service="lms.expedicao.tipoProdutoService.find"
					   label="tipoProduto" 
					   boxWidth="240" labelWidth="19%"
					   onlyActiveValues="true"
		/>
		<adsm:combobox  onchange="carregaSubClasseRisco(this);"
						property="classeRisco.idClasseRisco" 
						optionLabelProperty="nrClasseRisco" 
						optionProperty="idClasseRisco" 
						service="lms.expedicao.classeRiscoService.find" 
						label="classeRisco" labelWidth="19%" width="60%" 
		/>
		<adsm:combobox autoLoad="true" label="subClasseRisco" 
					   property="subClasseRisco.idSubClasseRisco"
					   service="lms.expedicao.subClasseRiscoService.findSubClasseRisco"
					   width="32%" optionProperty="idSubClasseRisco"
					   optionLabelProperty="nrSubClasseRisco" 
					   labelWidth="19%" width="31%">
		</adsm:combobox>
		<adsm:combobox property="tpSituacao" 
					   domain="DM_STATUS" 
					   label="situacao" 
					   required="true"
					   labelWidth="19%" 
					   width="31%" 
		/>
		<adsm:textbox property="nrOnu" 
					  label="nrOnu" 
					  size="5"
					  maxLength="5"   
					  required="false"
					  dataType="integer"					   
					  labelWidth="19%" 
					  width="31%" 
					  disabled="false"
		/>
		<adsm:textbox property="nrOrdem" 
					  label="numeroOrdem" 
					  size="5"
					  maxLength="5"   
					  required="false"
					  dataType="integer"					   
					  labelWidth="19%" 
					  width="31%" 
					  disabled="false"
		/>
		<adsm:textbox label="nrNcm" property="nrNcm" dataType="text" 
					  size="12" serializable="true" labelWidth="19%"/>
		<adsm:checkbox 
			property="blProdutoProibido" 
			label="produtoProibido"
			labelWidth="19%" 	
			width="79%" 
			serializable="true"/>				
		
		<adsm:hidden property="tipoLabelAux"/>
		<adsm:hidden property="tipoValueAux"/>
		<adsm:hidden property="naturezaValueAux"/>
		<adsm:hidden property="naturezaLabelAux"/>
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton />
			<adsm:removeButton id="removeBtn" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript">

// page load callback
function initWindow(eventObj) {

}

function retornoForm_cb(data, error) {
	onDataLoad_cb(data, error);
	
}



function myNewButtonScript(documento)
{
	newButtonScript(documento);
	setElementValue("naturezaValueAux","");
	setElementValue("naturezaLabelAux","");
	setElementValue("tipoLabelAux","");
	setElementValue("tipoValueAux","");
    var t = document.getElementById("tipoProduto.idTipoProduto");
    for (var i = t.options.length; i >= 1 ; i--) 
    {
    	t.options.remove(i);
    }
    var n = document.getElementById("natureza.idNaturezaProduto");
    for (var i = n.options.length; i >= 1 ; i--) 
    {
    	n.options.remove(i);
    }
}


function form_cb(data,errorMsg)
{
	alert("form_cb");
	onDataLoad_cb(data, errorMsg);

	var tipo = getElementValue("tipoProduto.idTipoProduto");
	if(getElementValue("tipoValueAux") == "")
	{
		setElementValue("tipoValueAux",tipo);
		alert('form_cb.tipo='+getElementValue("tipoValueAux"));
	}
	if (tipo != "")
	{
		var t = document.getElementById("tipoProduto.idTipoProduto");
		var tipoLabel = t.options[t.selectedIndex].text;
		alert('a)form_cb.tipoLabel='+tipoLabel);
		if(getElementValue("tipoLabelAux") == "")
		{
			setElementValue("tipoLabelAux",tipoLabel);
			alert('b)form_cb.tipoLabel='+getElementValue("tipoLabelAux"));
		}			
	}
	
	var natureza = getElementValue("naturezaProduto.idNaturezaProduto");
	if(getElementValue("naturezaValueAux") == "")
	{
		setElementValue("naturezaValueAux",tipo);
		alert('form_cb.natureza='+getElementValue("naturezaValueAux"));
	}
	if (natureza != "")
	{
		var n = document.getElementById("naturezaProduto.idNaturezaProduto");
		var naturezaLabel = n.options[n.selectedIndex].text;
		alert('a)form_cb.naturezaLabel='+naturezaLabel);
		if(getElementValue("naturezaLabelAux") == "")
		{
			setElementValue("naturezaLabelAux",naturezaLabel);
			alert('b)form_cb.naturezaLabel='+getElementValue("naturezaLabelAux"));
		}			
	}
		
}

function carregaSubClasseRisco(e) {
	if(e.selectedIndex > 0) {
		var v = getNestedBeanPropertyValue(e.data, ":" + (e.selectedIndex - 1) + ".idClasseRisco");
		var sdo = createServiceDataObject("lms.expedicao.subClasseRiscoService.findSubClasseRisco", "subClasseRisco.idSubClasseRisco", {idClasseRisco:v});
		xmit({serviceDataObjects:[sdo]});
	} else {
		limpaComboSubClasseRisco();
	}
	comboboxChange({e:e});
}

function limpaComboSubClasseRisco(){
	var combo = document.getElementById("subClasseRisco.idSubClasseRisco");
	for(var i = combo.options.length; i >= 1; i--) {
		combo.options.remove(i);
	}
	combo.selectedIndex = 0
}
</script>