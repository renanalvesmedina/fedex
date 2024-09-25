package com.mercurio.lms.configuracoes.jmx;

import java.util.List;
import com.mercurio.lms.configuracoes.model.Imagem;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(objectName = "bean:name=LMS-ImagemCache", log = false)
public class ImagemCache implements ImagemCacheMBean {

	private List<Imagem> imagens;
	private boolean blRefresh;

	public ImagemCache(){
		blRefresh = true;
	}

	@ManagedOperation(description = "Retrieves cache data.")
	public List<Imagem> getImagens() {
		return imagens;
	}

	@ManagedOperation(description = "Updates the cache data with the received information.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "Imagens", description = "New cache data") })
	public void setImagens(List<Imagem> imagens) {
		this.imagens = imagens;
		blRefresh = false;
	}

	@ManagedOperation(description = "Show the value for the Imagem in the cache.")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "chave", description = "Imagem chave") })
	public String getValue(String chave) {
		if (imagens == null) {
			return "Valores da cache não inicializados.";
		}
		for (Imagem imagem : imagens) {
			if (imagem.getChave().equals(chave)) {
				return imagem.getPicture();
			}
		}
		return "Imagem não encontrado.";
	}

	@ManagedOperation(description = "Show all values in the cache.")
	public String listValues() {
		if (imagens == null) {
			return "Valores da cache não inicializados.";
		}
		StringBuilder result = new StringBuilder();
		for (Imagem imagem : imagens) {
			result.append(imagem.getChave());
			result.append(": ");
			result.append(imagem.getPicture());
			result.append("\n");
		}
		return result.toString();
	}

	@ManagedOperation(description = "Checks if this cache needs refresh.")
	public boolean needRefresh() {
		return blRefresh;
	}

	@ManagedOperation(description = "Clears the current cache data and change the update flag to true")
	public void refresh() {
		blRefresh = true;
		imagens = null;
	}

}
