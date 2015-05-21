package es.us.eii.sugus.sugusnews;

/**
 * Created by guilledelacruz on 10/05/15.
 */
public class New {
    private String titulo;
    private String link;
    private String descripcion;
    private String guid;
    private String fecha;
    private String publicador;

    public New(){
        titulo = "No hay noticias disponibles";
        link = "";
        descripcion = "";
        guid = "";
        fecha = "";
        publicador = "";
    }

    public String getTitulo() {
        return titulo;
    }

    public String getLink() {
        return link;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getGuid() {
        return guid;
    }

    public String getFecha() {
        return fecha;
    }

    public String getPublicador() {
        return publicador;
    }

    public void setTitulo(String t) {
        titulo = t;
    }

    public void setLink(String l) {
        link = l;
    }

    public void setDescripcion(String d) {
        descripcion = d;
    }

    public void setGuid(String g) {
        guid = g;
    }

    public void setFecha(String f) {
        fecha = f;
    }

    public void setPublicador(String p) {
        publicador = p;
    }
}
