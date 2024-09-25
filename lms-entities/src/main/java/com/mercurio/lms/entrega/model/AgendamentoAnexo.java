package com.mercurio.lms.entrega.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class AgendamentoAnexo implements Serializable {

	private static final long serialVersionUID = 3L;

    /** identifier field */
    private Long idAgendamentoAnexo;
    
    /** persistent field */
    private String dsAnexo;

    /** persistent field */
    private byte[] dcArquivo;

    public byte[] getDcArquivo() {
		return dcArquivo;
	}

	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}

	/** nullable persistent field */
    private DateTime dhInclusao;
    
    private Usuario usuario;
    
    /** persistent field */
    private com.mercurio.lms.entrega.model.AgendamentoEntrega agendamentoEntrega;
    
    public Long getIdAgendamentoAnexo() {
		return idAgendamentoAnexo;
	}

	public void setIdAgendamentoAnexo(Long idAgendamentoAnexo) {
		this.idAgendamentoAnexo = idAgendamentoAnexo;
	}

	public String getDsAnexo() {
		return dsAnexo;
	}

	public void setDsAnexo(String dsAnexo) {
		this.dsAnexo = dsAnexo;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public com.mercurio.lms.entrega.model.AgendamentoEntrega getAgendamentoEntrega() {
		return agendamentoEntrega;
	}

	public void setAgendamentoEntrega(
			com.mercurio.lms.entrega.model.AgendamentoEntrega agendamentoEntrega) {
		this.agendamentoEntrega = agendamentoEntrega;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idAgendamentoAnexo",
				getIdAgendamentoAnexo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AgendamentoAnexo))
			return false;
		AgendamentoAnexo castOther = (AgendamentoAnexo) other;
		return new EqualsBuilder().append(this.getIdAgendamentoAnexo(),
				castOther.getIdAgendamentoAnexo()).isEquals();
    }


    public int hashCode() {
		return new HashCodeBuilder().append(getIdAgendamentoAnexo())
            .toHashCode();
    }
	
    /** Don't invoke this.  Used by Hibernate only. */
    public void setDcArquivoBlob(Blob dcArquivo) {
     this.dcArquivo = this.toByteArray(dcArquivo);
    }

    /** Don't invoke this.  Used by Hibernate only. */
    public Blob getDcArquivoBlob() {
     return Hibernate.createBlob(this.dcArquivo);
    }

    private byte[] toByteArray(Blob fromBlob) {
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     try {
      return toByteArrayImpl(fromBlob, baos);
     } catch (SQLException e) {
      throw new RuntimeException(e);
     } catch (IOException e) {
      throw new RuntimeException(e);
     } finally {
      if (baos != null) {
       try {
        baos.close();
       } catch (IOException ex) {
       }
      }
     }
    }

    private byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos)
     throws SQLException, IOException {
     byte[] buf = new byte[4000];
     InputStream is = fromBlob.getBinaryStream();
     try {
      for (;;) {
       int dataSize = is.read(buf);

       if (dataSize == -1)
        break;
       baos.write(buf, 0, dataSize);
      }
     } finally {
      if (is != null) {
       try {
        is.close();
       } catch (IOException ex) {
       }
      }
     }
     return baos.toByteArray();
    }
   }
