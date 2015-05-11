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

        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private void loadProperties() throws IOException {
        prop = new Properties();
        /* Our properties file is in the root dir. Make sure we're looking there.*/
        String propFileName = System.getProperty("user.dir")+ "/config.properties";
        File f = new File(propFileName);
        if(!f.exists()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("property file '" + propFileName + "' not found. Creating it for you.");
            System.out.println("Please enter your consumerSecret:");
            consumerSecret = br.readLine();
            System.out.println("Please enter your consumerKey:");
            consumerKey = br.readLine();
            storeConsumerTokens(consumerSecret, consumerKey);
        }
        else {

            try {
                prop.load(new FileInputStream(propFileName));
                consumerSecret = prop.getProperty("Dtwitter4j.oauth.consumerSecret");
                consumerKey = prop.getProperty("Dtwitter4j.oauth.consumerKey");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            useId = twitter.verifyCredentials().getId(); // TODO superfluous?
            storeAccessToken(accessToken);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    /* TODO turn store methods into one generalized beast */
    private void storeConsumerTokens(String consumerSecret, String consumerKey) {
        OutputStream output = null;
        try {
            output = new FileOutputStream("config.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        prop.setProperty("Dtwitter4j.oauth.consumerSecret", consumerSecret);
        prop.setProperty("Dtwitter4j.oauth.consumerKey", consumerKey);

        System.out.println(prop);
        try {
            prop.store(output, null);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeAccessToken(AccessToken accessToken){
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
