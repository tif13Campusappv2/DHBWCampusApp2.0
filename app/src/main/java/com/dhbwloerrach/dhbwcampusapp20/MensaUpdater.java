/*
 *      Beschreibung:	Stellt eine Klasse für das Onlineabrufen der Mensadaten bereit
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MensaUpdater {
    private MensaXMLExtractor extractor;
    private static final String APIURI = "http://www.swfr.de/index.php?id=1400&type=98&tx_swfrspeiseplan_pi1[apiKey]=c3841e89a2c8c301b890723ecdb786ad&tx_swfrspeiseplan_pi1[ort]=677";

	// Erstellt einen neuen MensaUpdater
    public MensaUpdater() {
        extractor= new MensaXMLExtractor();
    }

	// Ruft den aktuellen Mensaplan online ab 
    public MensaPlan LoadMensaData() {

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

	// Stellt eine Klasse für die Extrahierung der Mensadaten bereit
    private class MensaXMLExtractor{

        public MensaXMLExtractor(){}

		// Nimmt einen XML String mit Mensadaten entgegen und erstellt einen Mensacontainer
        public MensaPlan Extract(String rowData)
        {
            rowData= rowData.replaceAll("\r\n","").replaceAll("\t", "").replaceAll(">\\s+<","><").replaceAll("<br>","\r\n");
            Document serverResponse;
            try
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(rowData));
                serverResponse= builder.parse(is);
                Element Loerrach=(Element) serverResponse.getDocumentElement().getElementsByTagName("ort").item(0);
                NodeList days= Loerrach.getElementsByTagName("tagesplan");
                MensaPlan plan= new MensaPlan(days.getLength());
                for(int i=0;i<days.getLength();i++)
                {
                    Element currentDay=(Element) days.item(i);
                    MensaPlan.Menue menues[]={null,null,null,null};
                    NodeList menueList=currentDay.getElementsByTagName("menue");
                    for(int j=0; j<menueList.getLength();j++)
                    {
                        Element currentMenu=(Element) menueList.item(j);
                        String[] prices={"0,00€","0,00€","0,00€","0,00€"};

                        Element priceNode=(Element) currentMenu.getElementsByTagName("preis").item(0);
                        prices[MensaPlan.Prices.Schueler]=priceNode.getElementsByTagName("schueler").item(0).getTextContent();
                        prices[MensaPlan.Prices.Studenten]=priceNode.getElementsByTagName("studierende").item(0).getTextContent();
                        prices[MensaPlan.Prices.Mitarbeiter]=priceNode.getElementsByTagName("angestellte").item(0).getTextContent();
                        prices[MensaPlan.Prices.Gaeste]=priceNode.getElementsByTagName("gaeste").item(0).getTextContent();

                        String Kennzeichnungen= " " + currentMenu.getElementsByTagName("kennzeichnungen").item(0).getTextContent();
                        String Allergene= " " + currentMenu.getElementsByTagName("allergikerhinweise").item(0).getTextContent();
                        if(Kennzeichnungen.equals(" "))
                            Kennzeichnungen="-";
                        else
                        {
                            List<String> tmp= new ArrayList<>();
                            Pattern Brackets = Pattern.compile("\\s[\\w]+:\\s");
                            Matcher m= Brackets.matcher(Kennzeichnungen);
                            while (m.find())
                                tmp.add(m.group(0).trim().replaceAll(":",""));
                            Kennzeichnungen=", ";
                            for(int k=0;k<tmp.size();k++)
                                if(!Kennzeichnungen.contains(", " + tmp.get(k) + ","))
                                    Kennzeichnungen+= tmp.get(k)+", ";
                            Kennzeichnungen=Kennzeichnungen.replaceAll("^,\\s", "").replaceAll(",\\s$","");
                        }
                        if(Allergene.equals(" "))
                            Allergene="-";
                        else
                        {
                            List<String> tmp= new ArrayList<>();
                            Pattern Brackets = Pattern.compile("\\s[\\w]+:\\s");
                            Matcher m= Brackets.matcher(Allergene);
                            while (m.find())
                                tmp.add(m.group(0).trim().replaceAll(":",""));
                            Allergene=", ";
                            for(int k=0;k<tmp.size();k++)
                                if(!Allergene.contains(", " + tmp.get(k) + ","))
                                    Allergene+= tmp.get(k)+", ";
                            Allergene=Allergene.replaceAll("^,\\s", "").replaceAll(",\\s$","");
                        }


                        MensaPlan.Menue newMenue= new MensaPlan.Menue(currentMenu.getAttribute("zusatz"), currentMenu.getElementsByTagName("nameMitUmbruch").item(0).getTextContent(), prices,Kennzeichnungen,Allergene);
                        switch (currentMenu.getAttribute("art"))
                        {
                            case "Essen 1":
                                menues[MensaPlan.Menues.Menue1]=newMenue;
                                break;
                            case "Essen 2":
                                menues[MensaPlan.Menues.Menue2]=newMenue;
                                break;
                            case "Essen 3":
                                menues[MensaPlan.Menues.Menue3]=newMenue;
                                break;
                            case "Buffet":
                                menues[MensaPlan.Menues.Buffet]=newMenue;
                                break;
                        }
                    }
                    plan.InsertDay(i,new MensaPlan.Day(currentDay.getAttribute("datum"),menues));
                }
                plan.SortDays();
                return plan;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                MessageReporting.ShowMessage(MessageReporting.Messages.XML);
                return null;
            }

        }
    }
}
