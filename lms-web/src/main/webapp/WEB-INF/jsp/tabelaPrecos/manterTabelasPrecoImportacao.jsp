<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tabelaprecos.importacaoTabelaPrecoAction">
	
	<adsm:form action="/tabelaPrecos/manterTabelasPreco" idProperty="idTabelaPreco">

		<adsm:textbox labelWidth="15%" label="arquivo" property="arquivo" dataType="file" width="78%" size="60" required="true" serializable="true"/>
		
		<adsm:buttonBar>
			<adsm:storeButton caption="importar" id="importarMdaButton"  
							  service="lms.tabelaprecos.importacaoTabelaPrecoAction.importarTabela" />
							  
			<adsm:button id="dicionario" caption="dicionario" onclick="dicionarioTags()"/>
		</adsm:buttonBar>
	</adsm:form>

</adsm:window>

<script language="javascript" type="text/javascript">
	function dicionarioTags() {
		showModalDialog('tabelaPrecos/consultarTagsTabelaPreco.do?cmd=main',window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:790px;dialogHeight:520px;');
	}
</script>