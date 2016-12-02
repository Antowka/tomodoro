package ru.antowka.tomodoro.infrastructure;


import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GoogleAnalyticsTracking {

  private String gaID;
  private String clientID;
  private URL urlGa;
  private final String url = "https://www.google-analytics.com";
  private final String USER_AGENT = "Mozilla/5.0";

  public GoogleAnalyticsTracking(String gaID, String clientID) {
    this.gaID = gaID;
    this.clientID = clientID;

    //init url
    try {
      urlGa = new URL(url);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  /**
   *
   * @param actionType - Event hit type; &t=event
   * @param actionCategory - Event Category; &ec=video
   * @param actionName - Event Action; &ea=play
   */
  public void trackAction(String actionType, String actionCategory, String actionName) {

    try {
      HttpsURLConnection con = (HttpsURLConnection) urlGa.openConnection();

      con.setRequestMethod("POST");

      //add reuqest header
      con.setRequestMethod("POST");
      con.setRequestProperty("User-Agent", USER_AGENT);
      con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

      con.setRequestProperty("v", "1");
      con.setRequestProperty("tid", gaID);
      con.setRequestProperty("cid", clientID);
      con.setRequestProperty("t", actionType);

      con.setRequestProperty("ec", actionCategory);
      con.setRequestProperty("ea", actionName);

      con.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(con.getOutputStream());
      wr.flush();
      wr.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
