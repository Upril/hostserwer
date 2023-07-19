package com.serwertetowy.services.implementations;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.serwertetowy.config.S3ClientConfig;
import com.serwertetowy.entities.Episodes;
import com.serwertetowy.entities.Series;
import com.serwertetowy.exceptions.FileDownloadException;
import com.serwertetowy.exceptions.FileUploadException;
import com.serwertetowy.repos.EpisodesRepository;
import com.serwertetowy.repos.SeriesRepository;
import com.serwertetowy.services.dto.EpisodeSummary;
import com.serwertetowy.services.EpisodesService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
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
    private static final String FORMAT = "classpath:videos/%s.mp4";
    @Value("mangusta")
    private String bucketName;
    private final AmazonS3 s3Client;
    private EpisodesRepository episodesRepository;
    private SeriesRepository seriesRepository;
    @Autowired
    private ResourceLoader resourceLoader;
    @Override
    @Transactional
    public EpisodeSummary getEpisode(Integer id){
        return episodesRepository.findEpisodeSummaryById(id);
    }
    @Override
    public Mono<Resource> getEpisodeData(String title){
        return Mono.fromSupplier(()->resourceLoader.getResource(String.format(FORMAT,title)));
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
        Files.copy(file.getInputStream(), root.resolve(name+".mp4"));
        return episodesRepository.findEpisodeSummaryById(newEpisode.getId().intValue());
    }

    @Override
    public EpisodeSummary uploadFile(MultipartFile multipartFile) throws FileUploadException, IOException {
        //multipart file to file
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(multipartFile.getBytes());
        }
        //filename
        String fileName = new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");
        //upload file
        PutObjectRequest request = new PutObjectRequest(bucketName,fileName,file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("plain/"+ FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        metadata.addUserMetadata("Title","File Upload - " + fileName);
        metadata.setContentLength(file.length());
        request.setMetadata(metadata);
        s3Client.putObject(request);

        file.delete();
        //????
        return new EpisodeSummary() {
            @Override
            public String getTitle() {
                return fileName;
            }

            @Override
            public Long getId() {
                return null;
            }

            @Override
            public List<String> getLanguages() {
                return null;
            }
        };
    }

    @Override
    public Mono<Resource> streamFile(String filename) throws FileDownloadException, IOException {
        return null;
    }

    @Override
    public boolean deleteFile(String filename) {
        return false;
    }

    @Override
    public EpisodeSummary putEpisodeData(Long id, MultipartFile file) throws IOException {
        Path root = Paths.get("target/classes/videos");
        Episodes episodes = episodesRepository.findById(id.intValue())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        String name = episodes.getTitle();
        if(Files.exists(root.resolve(name+".mp4"))){
            Files.delete(root.resolve(name+".mp4"));
            Files.copy(file.getInputStream(), root.resolve(name+".mp4"));
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return episodesRepository.findEpisodeSummaryById(episodes.getId().intValue());
    }
    @Override
    public EpisodeSummary putEpisode(Long id, String name, List<String> languagesList, Integer seriesId) throws IOException {
        Path root = Paths.get("target/classes/videos");
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Episodes episodes = episodesRepository.findById(id.intValue())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(!Objects.equals(name, episodes.getTitle()) && Files.exists(root.resolve(episodes.getTitle()+".mp4"))){
            Files.copy(root.resolve(episodes.getTitle()+".mp4"),root.resolve(name+".mp4"));
            Files.delete(root.resolve(episodes.getTitle()+".mp4"));
        }
        episodes.setTitle(name);
        episodes.setLanguages(languagesList);
        episodes.setSeries(series);
        episodesRepository.save(episodes);
        return episodesRepository.findEpisodeSummaryById(episodes.getId().intValue());
    }


}

