<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	function carregaPagina() {
		onPageLoad();
	    var sdo = createServiceDataObject("lms.coleta.emitirRelatorioEstatisticoColetaUsuarioAction.getDadosSessao", "buscarDadosSessao");
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Retorno da pesquisa de dados do usuário da sessão em getDadosSessao().
	 */
	var dadosSessao;
	function buscarDadosSessao_cb(data, error) {
		dadosSessao = data;	
		setaDadosSessao();
	}		
</script>

<adsm:window service="lms.coleta.emitirRelatorioEstatisticoColetaUsuarioAction" onPageLoad="carregaPagina">
	<adsm:form action="/coleta/emitirRelatorioEstatisticoColetaUsuario">
		
		<adsm:range label="periodo" width="85%" required="true" >
			<adsm:textbox property="dataInicial" dataType="JTDate" size="10" maxLength="10" picker="true" />
			<adsm:textbox property="dataFinal" dataType="JTDate" size="10" maxLength="10" picker="true"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:reportViewerButton id="btnVisualizar" caption="visualizar" 
									 service="lms.coleta.emitirRelatorioEstatisticoColetaUsuarioAction"/>
			<adsm:resetButton caption="limpar"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">

	/**
	 * Função chamada quando a tela é iniciada.
	 */
	function initWindow(eventObj) {
		if(eventObj.name == "cleanButton_click") {
			setaDadosSessao();
		}
	}
	
	/**
	 * Função chamada para setar os dados na sessão.
	 */
	function setaDadosSessao() {
		setElementValue("dataInicial", setFormat(document.getElementById("dataInicial"), dadosSessao.dataPrimeiroDiaMes));
		setElementValue("dataFinal", setFormat(document.getElementById("dataFinal"), dadosSessao.dataAtual));
	}	
</script>