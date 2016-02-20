/*
 *      Beschreibung:	Stellt eine Klasse für das Onlineabrufen der Newsdaten bereit
 *      Autoren: 		Daniel Spieker
 *      Projekt:		Campus App 2.0
 *
 *      ╔══════════════════════════════╗
 *      ║ History                      ║
 *      ╠════════════╦═════════════════╣
 *      ║   Datum    ║    Änderung     ║
 *      ╠════════════╬═════════════════╣
 *      ║ 2015-xx-xx ║
 *      ║ 20xx-xx-xx ║
 *      ║ 20xx-xx-xx ║
 *      ╚════════════╩═════════════════╝
 *      Wichtig:           Tabelle sollte mit monospace Schriftart dargestellt werden
 */
package com.dhbwloerrach.dhbwcampusapp20;

import android.text.Html;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class NewsUpdater {
    private NewsXMLExtractor extractor;
    private static final String APIURI = "https://www.dhbw-loerrach.de/index.php?id=59&type=100";

    // Erstellt einen neuen Updater
    public NewsUpdater() {
        extractor= new NewsXMLExtractor();
    }

    // Ruft Newsdaten online ab
    public NewsContainer LoadNewsData() {

        try {
            URL requestUrl = new URL(APIURI);
            URLConnection con = requestUrl.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            int cp;

            while ((cp = in.read()) != -1) {
                sb.append((char) cp);
            }
            return extractor.Extract(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            MessageReporting.ShowMessage(MessageReporting.Messages.NETWORK);
            return null;
        }
    }

    // Stellt eine Klasse für die Extrahierung des XML Codes und die Convertierung zu einem Newscontainer bereit
    private class NewsXMLExtractor{

        public NewsXMLExtractor(){}

        // Convertiert von XML zu Newscontainer
        public NewsContainer Extract(String rowData)
        {
            rowData= rowData.replaceAll("\r\n", "").replaceAll("\t", "").replaceAll(">\\s+<","><").replaceAll("<br>","\r\n");
            Document serverResponse;
            try
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(rowData));
                serverResponse= builder.parse(is);
                Element Channel=(Element) serverResponse.getDocumentElement().getElementsByTagName("channel").item(0);
                NodeList news= Channel.getElementsByTagName("item");
                NewsContainer newsContainer= new NewsContainer(news.getLength());
                for(int i=0;i<news.getLength();i++)
                {
                    Element currentNewsItem=(Element) news.item(i);
                    String date= currentNewsItem.getElementsByTagName("pubDate").item(0).getTextContent();
                    String title= currentNewsItem.getElementsByTagName("title").item(0).getTextContent();
                    String link= currentNewsItem.getElementsByTagName("link").item(0).getTextContent();
                    String description= currentNewsItem.getElementsByTagName("description").item(0).getTextContent();
                    String content= ConverFromHtml(currentNewsItem.getElementsByTagName("content:encoded").item(0).getTextContent());



                    NewsContainer.NewsItem newNewsItem= new NewsContainer.NewsItem(date,title,link,description,content);
                    NodeList currentCategories= currentNewsItem.getElementsByTagName("category");
                    for(int j=0;j<currentCategories.getLength();j++)
                    {
                        switch (currentCategories.item(j).getTextContent())
                        {
                            case "Presse":
                                newNewsItem.SetCategory(NewsContainer.NewsCategories.Presse);
                                break;
                            case "Aktuelles":
                                newNewsItem.SetCategory(NewsContainer.NewsCategories.Aktuelles);
                                break;
                            case "Mitarbeiter":
                                newNewsItem.SetCategory(NewsContainer.NewsCategories.Mitarbeiter);
                                break;
                            case "Dozierende":
                                newNewsItem.SetCategory(NewsContainer.NewsCategories.Dozierende);
                        }
                    }
                    newsContainer.IncertNews(newNewsItem,i);
                }
                newsContainer.SortNews();
                return newsContainer;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                MessageReporting.ShowMessage(MessageReporting.Messages.XML);
                return null;
            }

        }

        // Entfertn aus dem übergebnen String alle HTML-Elemente
        private String ConverFromHtml(String textWithHtml)
        {
            if(textWithHtml.startsWith("<div>"))
                textWithHtml=textWithHtml.substring(5);
            return Html.fromHtml(Html.fromHtml(textWithHtml.replaceAll("<div>", "%nl%%nl%")).toString()).toString().replaceAll("%nl%", "\r\n");
        }
    }
}
