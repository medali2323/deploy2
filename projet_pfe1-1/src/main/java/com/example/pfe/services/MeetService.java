package com.example.pfe.services;
import org.springframework.stereotype.Service;

import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.apps.meet.v2.CreateSpaceRequest;
import com.google.apps.meet.v2.Space;
import com.google.apps.meet.v2.SpacesServiceClient;
import com.google.apps.meet.v2.SpacesServiceSettings;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ClientId;
import com.google.auth.oauth2.DefaultPKCEProvider;
import com.google.auth.oauth2.TokenStore;
import com.google.auth.oauth2.UserAuthorizer;

@Service
public class MeetService {

    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/meetings.space.created");
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String USER = "default";

    private static final TokenStore TOKEN_STORE = new TokenStore() {
        private Path pathFor(String id) {
            return Paths.get(".", TOKENS_DIRECTORY_PATH, id + ".json");
        }

        @Override
        public String load(String id) throws IOException {
            if (!Files.exists(pathFor(id))) {
                return null;
            }
            return Files.readString(pathFor(id));
        }

        @Override
        public void store(String id, String token) throws IOException {
            Files.createDirectories(Paths.get(".", TOKENS_DIRECTORY_PATH));
            Files.writeString(pathFor(id), token);
        }

        @Override
        public void delete(String id) throws IOException {
            if (!Files.exists(pathFor(id))) {
                return;
            }
            Files.delete(pathFor(id));
        }
    };

    private static UserAuthorizer getAuthorizer(URI callbackUri) throws IOException {
        try (InputStream in = MeetService.class.getResourceAsStream(CREDENTIALS_FILE_PATH)) {
            if (in == null) {
                throw new FileNotFoundException("Resource not found: wwww" + CREDENTIALS_FILE_PATH);
            }

            ClientId clientId = ClientId.fromStream(in);

            UserAuthorizer authorizer = UserAuthorizer.newBuilder()
                    .setClientId(clientId)
                    .setCallbackUri(callbackUri)
                    .setScopes(SCOPES)
                    .setPKCEProvider(new DefaultPKCEProvider() {
                        @Override
                        public String getCodeChallenge() {
                            return super.getCodeChallenge().split("=")[0];
                        }
                    })
                    .setTokenStore(TOKEN_STORE).build();
            return authorizer;
        }
    }

    private static Credentials getCredentials() throws Exception {
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().build();
        try {
            URI callbackUri = URI.create(receiver.getRedirectUri());
            UserAuthorizer authorizer = getAuthorizer(callbackUri);

            Credentials credentials = authorizer.getCredentials(USER);
            if (credentials != null) {
                return credentials;
            }

            URL authorizationUrl = authorizer.getAuthorizationUrl(USER, "", null);
            if (Desktop.isDesktopSupported() &&
                    Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(authorizationUrl.toURI());
            } else {
                System.out.printf("Open the following URL to authorize access: %s\n",
                        authorizationUrl.toExternalForm());
            }

            String code = receiver.waitForCode();
            credentials = authorizer.getAndStoreCredentialsFromCode(USER, code, callbackUri);
            return credentials;
        } finally {
            receiver.stop();
        }
    }

    public String createMeetingSpace() {
        try {
            Credentials credentials = getCredentials();
            SpacesServiceSettings settings = SpacesServiceSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();

            try (SpacesServiceClient spacesServiceClient = SpacesServiceClient.create(settings)) {
                CreateSpaceRequest request = CreateSpaceRequest.newBuilder()
                        .setSpace(Space.newBuilder().build())
                        .build();
                Space response = spacesServiceClient.createSpace(request);
                return "Space created: " + response.getMeetingUri();
            } catch (IOException e) {
                e.printStackTrace();
                return "Error creating space: " + e.getMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error getting credentials: " + e.getMessage();
        }
    }
}
