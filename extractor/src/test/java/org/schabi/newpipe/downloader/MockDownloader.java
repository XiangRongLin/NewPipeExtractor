package org.schabi.newpipe.downloader;

import com.google.gson.GsonBuilder;

import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.downloader.Request;
import org.schabi.newpipe.extractor.downloader.Response;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * <p>
 * Mocks requests by using json files created by {@link RecordingDownloader}
 * </p>
 */
class MockDownloader extends Downloader {

    private final String path;
    private final Map<Request, Response> mocks;

    public MockDownloader(@Nonnull String path) throws IOException {
        this.path = path;
        this.mocks = new HashMap<>();
        final File[] files = new File(path).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(RecordingDownloader.FILE_NAME_PREFIX)) {
                    final FileReader reader = new FileReader(file);
                    final TestRequestResponse response = new GsonBuilder()
                            .create()
                            .fromJson(reader, TestRequestResponse.class);
                    reader.close();
                    mocks.put(response.getRequest(), response.getResponse());
                }
            }
        } else {
            System.out.println("No mock files in path " + path);
        }
    }

    @Override
    public Response execute(@Nonnull Request request) {
        Response result = mocks.get(request);
        if (result == null) {
            final StringBuilder urls = new StringBuilder();
            mocks.forEach((request1, response) -> urls.append(request1).append("\n"));
            throw new NullPointerException("No mock response for request with url '" + request
                    + "' exists in path '" + path + "'.\nPlease make sure to run the tests with " +
                    "the RecordingDownloader first after changes.\n" +
                    "Mock responses exist for " + urls.toString());
        }
        return result;
    }
}
