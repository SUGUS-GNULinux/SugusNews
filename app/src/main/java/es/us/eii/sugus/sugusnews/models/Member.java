package es.us.eii.sugus.sugusnews.models;

/**
 * Created by guilledelacruz on 10/05/15.
 */
public class Member {
    private String name;
    private String link;

    public Member(){
        name = "No hay nadie.";
        link = "";
    }

    public Member(String n, String l){
        name = n;
        link = l;
    }

    public String getName(){
        return name;
    }
    public String getLink(){
        return link;
    }
    public void setName(String n){
        name = n;
    }
    public void setLink(String l){
        link = l;
    }
}
