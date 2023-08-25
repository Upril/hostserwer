package com.serwertetowy.services.implementations;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.serwertetowy.entities.Episodes;
import com.serwertetowy.entities.Series;
import com.serwertetowy.exceptions.EpisodeNotFoundException;
import com.serwertetowy.exceptions.FileDownloadException;
import com.serwertetowy.exceptions.FileUploadException;
import com.serwertetowy.exceptions.SeriesNotFoundException;
import com.serwertetowy.repos.EpisodesRepository;
import com.serwertetowy.repos.SeriesRepository;
import com.serwertetowy.services.EpisodesService;
import com.serwertetowy.services.dto.EpisodeSummary;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class EpisodeServiceImpl implements EpisodesService {
    private static final String VIDEO_FORMAT = "classpath:videos/%s.mp4";
    private static final String S3_BUCKET_NAME = "mangusta";
    private final AmazonS3 s3Client;
    private EpisodesRepository episodesRepository;
    private SeriesRepository seriesRepository;
    @Autowired
    private ResourceLoader resourceLoader;
    @Override
    @Transactional
    public EpisodeSummary getEpisode(Integer id){
        if(!episodesRepository.existsById(id)) throw new EpisodeNotFoundException();
        return episodesRepository.findEpisodeSummaryById(id);
    }
    @Override
    public Mono<Resource> getEpisodeData(Integer id){
        if(!episodesRepository.existsById(id)) throw new EpisodeNotFoundException();
        return Mono.fromSupplier(()->resourceLoader.getResource(String.format(VIDEO_FORMAT,id)));
    }
    @Override
    @Transactional
    public List<EpisodeSummary> getEpisodesBySeries(Integer seriesId){
        return episodesRepository.findEpisodeSummaryBySeriesId(seriesId);
    }
    @Override
    public EpisodeSummary saveEpisode(MultipartFile file, String name, List<String> languagesList, Integer seriesId) throws IOException {
        Path root = Paths.get("target/classes/videos");
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        Episodes newEpisode = new Episodes(name,series,languagesList);
        episodesRepository.save(newEpisode);
        Files.copy(file.getInputStream(), root.resolve(newEpisode.getId()+".mp4"));
        return episodesRepository.findEpisodeSummaryById(newEpisode.getId().intValue());
    }

    @Override
    public EpisodeSummary uploadFile(MultipartFile multipartFile, String name, List<String> languagesList, Integer seriesId) throws IOException {
        //multipart file to file
        File file = new File(name);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(multipartFile.getBytes());
        }
        //filename
        String fileName =generateFileName(multipartFile);

        //upload file
        PutObjectRequest request = new PutObjectRequest(S3_BUCKET_NAME,fileName,file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("plain/"+ FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        metadata.addUserMetadata("Title","File Upload - " + fileName);
        metadata.setContentLength(file.length());
        request.setMetadata(metadata);
        s3Client.putObject(request);

        file.delete();

        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        Episodes newEpisode = new Episodes(name,series,languagesList);
        episodesRepository.save(newEpisode);

        return episodesRepository.findEpisodeSummaryById(newEpisode.getId().intValue());
    }

    @Override
    public StreamingResponseBody streamFile(String filename) throws FileDownloadException {
        if(bucketIsEmpty()) throw new FileDownloadException("Requested bucket does not exist or is empty");
        S3Object object = s3Client.getObject(S3_BUCKET_NAME, filename);
        S3ObjectInputStream s3is = object.getObjectContent();
        return outputStream -> {
            int bytesToWrite = 0;
            byte[] dataBuffer = new byte[1024];
            while ((bytesToWrite = s3is.read(dataBuffer,0,dataBuffer.length)) != -1){
                outputStream.write(dataBuffer,0,bytesToWrite);
            }
            s3is.close();
        };
//    } catch (Exception e) {
//        System.err.println("Error "+ e.getMessage());
//        return new ResponseEntity<StreamingResponseBody>(HttpStatus.BAD_REQUEST);
//    }
    }

    @Override
    public boolean deleteFile(String filename) {
        File file = Paths.get(filename).toFile();
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }
    public boolean controllerDeleteFile(Integer id) {
        File file = Paths.get("target/classes/videos/"+id+".mp4").toFile();
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    @Override
    public EpisodeSummary putEpisodeData(Long id, MultipartFile file) throws IOException {
        Path root = Paths.get("target/classes/videos");
        Episodes episodes = episodesRepository.findById(id.intValue())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        String fileName = episodes.getTitle()+".mp4";
        Path episodePath = root.resolve(fileName);
        if(Files.exists(episodePath)){
            Files.delete(episodePath);
            Files.copy(file.getInputStream(), episodePath);
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return episodesRepository.findEpisodeSummaryById(episodes.getId().intValue());
    }
    @Override
    public EpisodeSummary putEpisode(Long id, String name, List<String> languagesList, Integer seriesId) throws IOException {
        if(!seriesRepository.existsById(seriesId)) throw new SeriesNotFoundException();
        if(!episodesRepository.existsById(id.intValue())) throw new EpisodeNotFoundException();
        Path root = Paths.get("target/classes/videos");
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Episodes episodes = episodesRepository.findById(id.intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String fileName = episodes.getTitle()+".mp4";
        Path episodePath = root.resolve(fileName);
        if(!Objects.equals(name, episodes.getTitle()) && Files.exists(episodePath)){
            Files.copy(episodePath,root.resolve(name+".mp4"));
            Files.delete(episodePath);
        }
        episodes.setTitle(name);
        episodes.setLanguages(languagesList);
        episodes.setSeries(series);
        episodesRepository.save(episodes);
        return episodesRepository.findEpisodeSummaryById(episodes.getId().intValue());
    }

    @Override
    public void deleteEpisode(Integer episodeId) {
        if(!episodesRepository.existsById(episodeId)) throw new EpisodeNotFoundException();
        Episodes episode = episodesRepository.findById(episodeId).orElseThrow();
        controllerDeleteFile(episodeId);
        episodesRepository.delete(episode);
    }

    private boolean bucketIsEmpty() {
        ListObjectsV2Result result = s3Client.listObjectsV2(S3_BUCKET_NAME);
        if(result == null) return false;
        List<S3ObjectSummary> objectSummaries = result.getObjectSummaries();
        return objectSummaries.isEmpty();
    }
    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

}

