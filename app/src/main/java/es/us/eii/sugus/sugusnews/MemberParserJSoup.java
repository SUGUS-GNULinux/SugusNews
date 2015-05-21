package es.us.eii.sugus.sugusnews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guilledelacruz on 17/05/15.
 */
public class MemberParserJSoup {

    private String htmlUrl;

    public MemberParserJSoup(){
        htmlUrl = "http://sugus.eii.us.es/en_sugus.html";
    }

    public List<Member> parse(){

        List<Member> miembros = new ArrayList<Member>();
        Document doc;
        try{
            doc = Jsoup.connect(htmlUrl).get();
        }catch (IOException e){
            doc = null;
        }

        if (doc == null) {
            return null;
        }

        Elements liList = doc.select("li");
        for (Element li: liList){
            Member m = new Member();
            m.setName(li.text());
            miembros.add(m);
        }

        return miembros;
    }
}
