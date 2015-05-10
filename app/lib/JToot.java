package lib;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.Properties;

/**
 * Created by lotte on 23.03.15.
 */
public class JToot {
    Properties prop;
    String consumerSecret, consumerKey, accessKey, accessSecret;
    Twitter twitter;
    RequestToken requestToken;
    AccessToken accessToken;

    public JToot() throws TwitterException, FileNotFoundException {

        loadProperties();

        /* My consumerKey brings all the tokens to the yard */
        if (prop.getProperty("accessToken") == null) {
            twitter = TwitterFactory.getSingleton();
            twitter.setOAuthConsumer(consumerKey, consumerSecret);
            requestToken = twitter.getOAuthRequestToken();
            getAccessToken();
        }

        accessKey = prop.getProperty("accessToken"); // yup, horrible naming, sry
        accessSecret = prop.getProperty("accessTokenSecret");

        /* We've got our tokens. Let's get ready to toot. */
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessKey)
                .setOAuthAccessTokenSecret(accessSecret);

        System.out.println(consumerKey + "  " + consumerSecret + "  " + accessKey + "  " + accessSecret);

        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public void toot(String text) throws TwitterException {
        Status status = twitter.updateStatus(text);
        System.out.println("Successfully updated the status to [" + status.getText() + "].");
    }

    private void loadProperties() throws FileNotFoundException {
        prop = new Properties();
        String propFileName = "config.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            try {
                prop.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }

        System.out.println("I can haz props. "+prop);
        consumerSecret = prop.getProperty("Dtwitter4j.oauth.consumerSecret");
        consumerKey = prop.getProperty("Dtwitter4j.oauth.consumerKey");
    }

    private void getAccessToken() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
            try{
                String pin = br.readLine();

                if(pin.length() > 0){
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                }else{
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if(401 == te.getStatusCode()){
                    System.out.println("Unable to get the access token.");
                }else{
                    te.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long useId = 0;
        try {
            useId = twitter.verifyCredentials().getId();
            storeAccessToken(useId , accessToken);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    private void storeAccessToken(long useId, AccessToken accessToken){
        OutputStream output = null;
        try {
            output = new FileOutputStream("config.properties");
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }

        prop.setProperty("accessToken", accessToken.getToken());
        prop.setProperty("accessTokenSecret", accessToken.getTokenSecret());

        System.out.println(prop);
        try {
            prop.store(output, null);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {
            JToot jtoot = new JToot();
        } catch (TwitterException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
