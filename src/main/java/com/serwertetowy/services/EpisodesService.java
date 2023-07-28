package com.serwertetowy.services;

import com.serwertetowy.exceptions.FileDownloadException;
import com.serwertetowy.exceptions.FileUploadException;
import com.serwertetowy.services.dto.EpisodeSummary;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface EpisodesService {
    //Episode summary information: id, title and languages
    EpisodeSummary getEpisode(Integer id);
    //Episode data streaming
    Mono<Resource> getEpisodeData(Integer id);
    //method used by series service to get episode lists
    List<EpisodeSummary> getEpisodesBySeries(Integer seriesId);
    //method saving sent mp4 files to storage, may be replaced by cloud based storage
    EpisodeSummary saveEpisode(MultipartFile file, String name, List<String> languagesList, Integer seriesId) throws IOException;
    EpisodeSummary uploadFile(MultipartFile multipartFile, String name, List<String> languagesList, Integer seriesId) throws FileUploadException,IOException;
    StreamingResponseBody streamFile(String filename) throws FileDownloadException,IOException;
    boolean deleteFile(String filename);
    EpisodeSummary putEpisodeData(Long id, MultipartFile file) throws IOException;
    EpisodeSummary putEpisode(Long id, String name, List<String> languagesList, Integer seriesId) throws IOException;
}
