<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
	
<adsm:window service="lms.recepcaodescarga.finalizarDescargaAction" onPageLoadCallBack="carregaPagina">
	<adsm:form action="/recepcaoDescarga/finalizarDescarga" >
		
		<adsm:hidden property="idControleCarga" />
		<adsm:hidden property="idFilialControleCarga" />
		<adsm:hidden property="tpRelatorio" />
		<adsm:hidden property="tpControleCarga" />
		<adsm:hidden property="idCarregamentoDescarga" />
		
		<adsm:textbox label="docsDescarregadosNaoConferidos" property="totDocDescarregadoNaoConferido" dataType="text"  
					  disabled="true" size="5" maxLength="4" labelWidth="500" width="70%">
					  <adsm:button id="docsDescarregadosNaoConferidos" caption="visualizar" disabled="false" onclick="viewReport('docsDescarregadosNaoConferidos')"/>
		</adsm:textbox>
		<adsm:textbox label="docsNaoDescarregadosConferidos" property="totDocNaoDescarregadoConferido" dataType="text"  
					  disabled="true" size="5" maxLength="4" labelWidth="500" width="70%">
					  <adsm:button id="docsNaoDescarregadosConferidos" caption="visualizar" disabled="false" onclick="viewReport('docsNaoDescarregadosConferidos')"/>
		</adsm:textbox>
		<adsm:textbox label="docsDescarregadosSemDoc" property="totDocDescarregadoSemDoc" dataType="text"  
					  disabled="true" size="5" maxLength="4" labelWidth="500" width="70%">
					  <adsm:button id="docsDescarregadosSemDoc" caption="visualizar" disabled="false" onclick="viewReport('docsDescarregadosSemDoc')"/>
		</adsm:textbox>
		<adsm:textbox label="docsDescarregadosIncompletos" property="totDocDescarregadoIncompleto" dataType="text"  
					  disabled="true" size="5" maxLength="4" labelWidth="500" width="70%">
					  <adsm:button id="docsDescarregadosIncompletos" caption="visualizar" disabled="false" onclick="viewReport('docsDescarregadosIncompletos')"/>
		</adsm:textbox>
		<adsm:textbox label="volumeSobra" property="totVolumeSobra" dataType="text"  
					  disabled="true" size="5" maxLength="4" labelWidth="500" width="70%">
					  <adsm:button id="volumeSobra" caption="visualizar" disabled="false" onclick="viewReport('volumeSobra')"/>
		</adsm:textbox>
		<adsm:textbox label="volumesColetadosNaoDescarregados" property="totVolumesColetadosNaoDescarregados" dataType="text"  
					  disabled="true" size="5" maxLength="4" labelWidth="500" width="70%">
					  <adsm:button id="volumesColetadosNaoDescarregados" caption="visualizar" disabled="false" onclick="viewReport('volumesColetadosNaoDescarregados')"/>
		</adsm:textbox>
		
		<adsm:buttonBar>
			<adsm:button id="btnVoltar" caption="voltar" onclick="window.close()" disabled="false"/>
			<adsm:button id="btnContinuarDescarga" caption="continuarFimDescarga" onclick="continuarFinalizarDescarga()" disabled="false"/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script type="text/javascript">
	var blContinuaDescarga = false;
	function carregaPagina_cb() {
		var search = this.location.search;
		
		//Busca o id do controle de carga a partir da URL da modal
		var idControleCarga = search.match('idControleCarga=\\d+');
		if (idControleCarga.length > 0){
			setElementValue('idControleCarga', idControleCarga[0].match('\\d+')[0]);
		}
		
		//Busca o id da filial do controle de carga a partir da URL da modal
		var idFilialControleCarga = search.match('idFilialControleCarga=\\d+');
		if (idFilialControleCarga.length > 0){
			setElementValue('idFilialControleCarga', idFilialControleCarga[0].match('\\d+')[0]);
		}
		
		//Busca o tipo do controle de carga  a partir da URL da modal
		var tpControleCarga = search.match('tpControleCarga=\\w');
		if (tpControleCarga.length > 0){
			setElementValue('tpControleCarga', tpControleCarga[0].charAt(16));
		}
		
		var idCarregamentoDescarga = search.match('idCarregamentoDescarga=\\d+');
		if (idCarregamentoDescarga != undefined && idCarregamentoDescarga.length > 0){
			setElementValue('idCarregamentoDescarga', idCarregamentoDescarga[0].match('\\d+')[0]);
		}

		var paramBlContinuaDescarga = search.match('blContinuaDescarga=\[A-z]{4}');
		if (paramBlContinuaDescarga != undefined && paramBlContinuaDescarga.length > 0 && paramBlContinuaDescarga[0].split("=")[1] == "true"){
			blContinuaDescarga = true;
		}
		
		buscarDadosSessao();
	}


	/**
	 * Função chamada ao iniciar a tela
	 */
	function initWindow(eventObj) {
	}

	function viewReport(name){
		setElementValue('tpRelatorio',name);
		reportButtonScript('lms.recepcaodescarga.descarregarVeiculoDivergenciaReportAction.executeReport','openPdf',document.forms[0])
	}						

	function continuarFinalizarDescarga(){
		window.showModalDialog('recepcaoDescarga/finalizarDescarga.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:820px;dialogHeight:500px;');
		window.close();
		
	}

	/**
	 * Busca a Data e a Hora atual do sistema.
	 */
	function buscarDadosSessao() {
		var data = new Object();
		data.idControleCarga = getElementValue("idControleCarga");
		data.tpControleCarga = getElementValue("tpControleCarga");
		data.idCarregamentoDescarga = getElementValue("idCarregamentoDescarga");
		setDisabled("btnContinuarDescarga", !blContinuaDescarga);
		var sdo = createServiceDataObject("lms.recepcaodescarga.descarregarVeiculoDivergenciaReportAction.getRowCountByTpRelatorio", "buscarDadosSessao", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Retorno da pesquisa de Data Atual em getDadosSessao().
	 */
	function buscarDadosSessao_cb(data, error) {
		setElementValue("totDocDescarregadoNaoConferido", data.totDocDescarregadoNaoConferido);
		setElementValue("totDocNaoDescarregadoConferido", data.totDocNaoDescarregadoConferido);
		setElementValue("totDocDescarregadoSemDoc", data.totDocDescarregadoSemDoc);
		setElementValue("totDocDescarregadoIncompleto", data.totDocDescarregadoIncompleto);
		setElementValue("totVolumeSobra", data.totVolumeSobra);
		setElementValue("totVolumesColetadosNaoDescarregados", data.totVolumesColetadosNaoDescarregados);
	}	

</script>
