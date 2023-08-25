package com.serwertetowy.services.implementations;

import com.serwertetowy.entities.*;
import com.serwertetowy.exceptions.SeriesNotFoundException;
import com.serwertetowy.repos.*;
import com.serwertetowy.services.EpisodesService;
import com.serwertetowy.services.dto.*;
import com.serwertetowy.services.SeriesService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class SeriesServiceImpl implements SeriesService {
    private EpisodesService episodesService;
    private SeriesRepository seriesRepository;
    private SeriesTagsRepository seriesTagsRepository;
    private TagRepository tagRepository;
    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public Series saveSeries(String name, String description, List<Integer> tagIds) throws IOException {
        //assembling series tag data from request data
        Series series = seriesAssemble(name,description,tagIds);
        series.setImageData(resourceLoader.getResource("classpath:/images/banner.jpg").getContentAsByteArray());
        seriesRepository.save(series);
        return series;
    }

    @Override
    public Series saveSeriesWithImage(MultipartFile file, String name, String description, List<Integer> tagIds) throws IOException {
        Series series = seriesAssemble(name,description,tagIds);
        series.setImageData(file.getBytes());
        seriesRepository.save(series);
        return series;
    }

    @Override
    @Transactional
    public List<SeriesSummary> getAllSeries() {
        List<SeriesSummary> seriesSummaries = new ArrayList<>();
        List<SeriesData> data = seriesRepository.findAllData();
        //assembling series summary data from the SeriesData dto, containing only id,name and desc
        for(SeriesData foundSeries : data){
            seriesSummaries.add(assembleSeriesSummary(foundSeries));
        }
        return seriesSummaries;
    }

    @Override
    @Transactional
    public SeriesSummary getSeriesById(Integer id) {
        SeriesData data = seriesRepository.findSeriesDataById(id);
        if(data == null) throw new SeriesNotFoundException();
        return assembleSeriesSummary(data);
    }

    @Override
    @Transactional
    public Mono<Resource> getSeriesImageData(Integer id) {
        if(seriesRepository.existsById(id)){
        ByteArrayResource imageData = new ByteArrayResource(seriesRepository.findById(id).orElseThrow
                (()->new ResponseStatusException(HttpStatus.NOT_FOUND)).getImageData());
        return Mono.fromSupplier(()->imageData);
        } else {
            throw new SeriesNotFoundException();
        }
    }


    private SeriesSummary assembleSeriesSummary(SeriesData data){
        SeriesSummary summary = new SeriesSummary();
        summary.setId(data.getId());
        summary.setName(data.getName());
        summary.setDescription(data.getDescription());
        //tags insert from seriestags-findbyseriesId -> id of tags -> tagrepo-findByid
        List<Tags> tags = new ArrayList<>();
        for(SeriesTags sTag: seriesTagsRepository.findBySeriesId(summary.getId())){
            Tags foundTags = tagRepository.findById(sTag.getTags().getId().intValue())
                    .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
            tags.add(foundTags);
        }
        summary.setSeriesTags(tags);
        //assembling the series summary - adding the episode summary data without the video file
        summary.setEpisodes(episodesService.getEpisodesBySeries(summary.getId().intValue()));
        return summary;
    }
    public Series seriesAssemble(String name, String description, List<Integer> tagIds){
        //assembling series tag data from request data
        Set<SeriesTags> seriesTagsSet = new HashSet<>();
        Series series = new Series(name,description);
        for (Integer id : tagIds){
            seriesTagsSet.add(new SeriesTags(series,tagRepository.findById(id).orElseThrow(
                    ()->new ResponseStatusException(HttpStatus.NOT_FOUND))));
        }
        series.setSeriesTags(seriesTagsSet);
        return series;
    }
}
