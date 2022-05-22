package models;

public class Versao {

    // atributos de classe
    private String versao;  // PK  (HASH)
    private String autor;
    private String quando; // converter data para string
    private String comentario;

    // construtores
    public Versao(){   }  // default

    public Versao(String comentario){  this.comentario = comentario;  } // atributo de instância

    // métodos
    public String getChavePrimaria() {  return versao; }
    public void setVersao(String versao) { this.versao = versao; }
    public void setData(String data) { this.quando = data; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public String getUsuario() { return autor; }
    public void setUsuario(String usuario) { this.autor = usuario; }

}
