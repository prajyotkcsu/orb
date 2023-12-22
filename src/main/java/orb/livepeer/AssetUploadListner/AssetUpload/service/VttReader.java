package orb.livepeer.AssetUploadListner.AssetUpload.service;

import lombok.extern.slf4j.Slf4j;
import orb.livepeer.AssetUploadListner.AssetUpload.model.Thumbnail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
@Slf4j
public class VttReader {

    public static List<Thumbnail> convertVttToJPEG(String vttFilePath) throws IOException {
        log.info("Converting vtt to images....");
        List<Thumbnail> thumbnails = new ArrayList<>();
        URL urlObject = new URL(vttFilePath);
        String domain=vttFilePath.substring(0,vttFilePath.lastIndexOf('/'));
        // Open a connection to the URL
        URLConnection connection = urlObject.openConnection();

        // Create a BufferedReader to read the content
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            Thumbnail thumbnail=new Thumbnail();
            while ((line = reader.readLine()) != null) {

                if(line.contains("-->")){
                    String[] timeframe=line.split("-->");
                    String filename=String.join("/",domain,line);
                    thumbnail.setStart(convertToMilliseconds(timeframe[0]));
                    thumbnail.setEnd(convertToMilliseconds(timeframe[1]));
                }
                else if(line.contains(".jpg")){
                    thumbnail.setFilename(String.join("/",domain,line));
                    thumbnails.add(thumbnail);
                    thumbnail=new Thumbnail();
                }

            }
        }catch(Exception ex){
            log.error(String.valueOf(ex));
        }
        return thumbnails;
    }
    private static String convertToMilliseconds(String time) {
        String cleanedTime = time.replaceAll("^0+", "").replaceAll("[^0-9]", "");
        return Long.valueOf(cleanedTime).toString();
    }

}
