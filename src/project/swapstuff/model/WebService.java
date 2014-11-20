//package project.swapstuff.model;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.protocol.HTTP;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//import org.xmlpull.v1.XmlPullParserFactory;
//
//import android.graphics.Bitmap;
//import android.util.Log;
//
//
//
//public class WebService {
//	
//	
//	
//	
//	
//	
//	
//	
//	String structurexml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
//			+ "<soap:Body>"
//			+ "<GetImagesPath xmlns=\"http://tempuri.org/\">"
//			+ "<userID>"
//			+ ""
//			+ "</userID>"
//			+ " <servicePath>"
//			+ ""
//			+ "</servicePath>"
//			+ "</GetImagesPath>"
//			+ "</soap:Body>" + "</soap:Envelope>";
//
//	HttpClient httpclient = new DefaultHttpClient();
//	
//	HttpPost httppost = new HttpPost(
//			"http://116.193.163.156:9092/RealEstateWS.asmx");
//	XmlPullParserFactory factory = null;
//	XmlPullParser parser = null;
//
//	StringBuilder sb = new StringBuilder();
//	
//	sb.append(structurexml);
//	httppost.addHeader("Accept", "text/xml");
//	httppost.addHeader("Content-Type", "text/xml; UTF-8");
//
//	StringEntity se;
//	
//		try {
//			se = new StringEntity(structurexml, HTTP.UTF_8);
//			httppost.setEntity(se);
//
//			httppost.setEntity(se);
//			HttpResponse response = httpclient.execute(httppost);
//			
//		
//
//			
//
//			InputStream in = response.getEntity().getContent();
//			BufferedReader bf = new BufferedReader(
//					new InputStreamReader(in));
//			
//			Log.i("res-->", response.toString());
//			
//			
//						factory = XmlPullParserFactory.newInstance();
//			factory.setNamespaceAware(true);
//			parser = factory.newPullParser();
//			parser.setInput(bf);
//		
//		int eventType = parser.getEventType();
//
//		while (eventType != XmlPullParser.END_DOCUMENT) {
//
//			String tagname = parser.getName();
//
//			switch (eventType) {
//			case XmlPullParser.TEXT:
//
////				text = parser.getText();
//
//				break;
//
//			case XmlPullParser.END_TAG:
//
//				if (tagname.equalsIgnoreCase("GetImagesPathResult")) {
//
//				
//
//				
//					
//
//					
////					 JSONObject jsonResponse = new JSONObject(newString(textresulturl));
////					 JSONArray mtUsers =jsonResponse.getJSONArray("Head");
//					 
//					 
//					
//					 
//					
//				}
//
//				break;
//
//			default:
//				break;
//			}
//			eventType = parser.next();
//
//		}
//
//	} catch (UnsupportedEncodingException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}
//
//	
//	
//	
//	
//	
//	
//
//
//}
//
