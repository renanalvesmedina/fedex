<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
var controleCarga;
var idControleCarga
function carregaPagina() {
	onPageLoad();

	var u = new URL(parent.location.href);
	controleCarga = u.parameters["controleCarga"];
	idControleCarga = u.parameters["idControleCarga"];
	carregaGrid();
	if (document.forms[0].controleCargaConcatenado && controleCarga != null)
		document.forms[0].controleCargaConcatenado.value = controleCarga.split(" ")[0];
	if (document.forms[0].controleCarga && controleCarga != null)
		document.forms[0].controleCarga.value = controleCarga.split(" ")[1];
}

function recarregaPagina() {
	onPageLoad();

	var u = new URL(parent.location.href);
	controleCarga = u.parameters["controleCarga"];
	idControleCarga = u.parameters["idControleCarga"];
	carregaGrid();
	if (document.forms[0].controleCargaConcatenado && controleCarga != null)
		document.forms[0].controleCargaConcatenado.value = controleCarga.split(" ")[0];
	if (document.forms[0].controleCarga && controleCarga != null)
		document.forms[0].controleCarga.value = controleCarga.split(" ")[1];
}

</script>
<adsm:window service="lms.coleta.controleCargaAction" onPageLoad="carregaPagina">
	<adsm:form action="/coleta/cadastrarPedidoColeta">
	<adsm:section caption="enquadramento" />
		<adsm:textbox label="controleCargas" size="3" maxLength="3" width="79%" 
					  dataType="text" property="controleCargaConcatenado" disabled="true">
			<adsm:textbox dataType="text" property="controleCarga" size="9" maxLength="8" 
					  mask="00000000" disabled="true"/>
	  	</adsm:textbox>
		<adsm:grid idProperty="idEnquadramentoRegra" property="servico" selectionMode="none" 
				   gridHeight="330" rows="13" autoSearch="false" onRowClick="rowClickNone" 
				   unique="true"
				   service="lms.coleta.consultaEnquadramentoAction.findCarregaEnquadramentos"
				   rowCountService="lms.coleta.consultaEnquadramentoAction.getRowCountEnquadramentos"
	   			   defaultOrder="servico_.dsServico:asc">
			<adsm:gridColumn title="enquadramento" property="dsEnquadramentoRegra" width="400"/>
			<adsm:gridColumn title="limiteInicial" property="vlLimiteMinimo" width="110" dataType="currency"/>
			<adsm:gridColumn title="limiteFinal" property="vlLimiteMaximo" width="110" dataType="currency"/>
			<adsm:gridColumn title="requerLiberacaoCMOP" property="blRequerLiberacaoCemop" width="110" renderMode="image-check"/>
		</adsm:grid>
	</adsm:form>
	
	<adsm:buttonBar>
		<adsm:button caption="fechar" disabled="false" onclick="window.close()"/>
	</adsm:buttonBar>
	
</adsm:window>

<script type="text/javascript">
function carregaGrid() {
	var fb = buildFormBeanFromForm(this.document.forms[0], 'LIKE_END');
	var u = new URL(parent.location.href);
	fb['idControleCarga'] = idControleCarga;
	fb['blVerificacaoGeral'] = "S";
		fb['tpConteudo'] = "P";
	servico_cb(fb);
}

function rowClickNone() {
	return false;
}


function carregaEnquadramento_cb(data,error){
	if( error ){
		return;
	}
	showModalDialog('/sgr/manterProcedimentosFaixasValores.do?cmd=exigencias'+
			'&enquadramentoRegra.idEnquadramentoRegra='+data.idEnquadramentoRegra+
			'&enquadramentoRegra.dsEnquadramentoRegra='+data.dsEnquadramentoRegra+
			'&enquadramentoRegra.moeda.idMoeda='+data.idMoeda+
			'&enquadramentoRegra.moeda.siglaSimbolo='+data.dsSimbolo+
			'&faixaDeValor.idFaixaDeValor='+data.idFaixaDeValor+
			'&vlLimiteMinimo='+data.vlLimiteMinimo+
			'&vlLimiteMaximo='+data.vlLimiteMaximo+
			'&blRequerLiberacaoCemop='+data.blRequerLiberacaoCemop
			,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');	
}
</script>